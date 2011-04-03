package za.co.yellowfire.ui.training;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.model.map.MapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.Venue;
import za.co.yellowfire.domain.geocode.*;
import za.co.yellowfire.domain.training.SkillArea;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.manager.DomainQueryHint;
import za.co.yellowfire.ui.model.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.OptimisticLockException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@ViewScoped @ManagedBean(name = "venuesController")
public class VenuesController implements Serializable, DataTableSearchListener<GeocodeResult> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

    @EJB(name = "DomainManager")
    private DomainManager manager;
    private transient DataTable dataTable;
    private List<DataTableRow<Venue>> rows;
    private DataTableRow<Venue> selectedRow = new DataTableRow<Venue>(new Venue());

    /*Geocode*/
    @EJB(name = "GeocodeManager")
    private GeocodeManager geocodeManager;
    private DataTableModel<GeocodeResult> searchModel;
    private MapModel searchMapModel;
    private String searchText;
    
//    private static final GeocodeResult EMPTY_RESULT() {
//        GeocodeResult result = new GeocodeResult();
//        GeocodeLocation location = new GeocodeLocation();
//        GeocodeGeometry geometry = new GeocodeGeometry();
//        location.setLatitude("0");
//        location.setLongitude("0");
//        geometry.setLocation(location);
//        result.setGeometry(geometry);
//        return result;
//    }

    private static final Venue EMPTY_VENUE() {
        return new Venue();
    }

    @PostConstruct
    private void init() {
        searchModel =
                new DataTableModel<GeocodeResult>(
                        /* DataTableListener*/
                        new AbstractDomainManagerDataTableListener<GeocodeResult>() {
                            @Override
                            public DomainManager getManager() {
                                return manager;
                            }

                            @Override
                            public String getLoadQuery() {
                                return null;
                            }

                            @Override
                            public GeocodeResult createEmpty() {
                                GeocodeResult result = new GeocodeResult();
                                result.setGeometry(new GeocodeGeometry(new GeocodeLocation("0", "0")));
                                return result;
                            }
                        },
                        /* DataTableSearchListener*/
                        this);
    }

    @SuppressWarnings("unchecked")
    public List<DataTableRow<Venue>> getRows() {
        if (this.rows == null) {
            List<Venue> venues = (List<Venue>) manager.query(Venue.QRY_VENUES, null, DomainQueryHint.REFRESH);
            if (venues != null) {
                this.rows = new ArrayList<DataTableRow<Venue>>(venues.size());
                for (Venue venue : venues) {
                    rows.add(new DataTableRow<Venue>(venue));
                }
            }
        }
        return this.rows;
    }

    public DataTableRow<Venue> getSelectedRow() {
        if (selectedRow == null) {
            this.selectedRow = new DataTableRow<Venue>(EMPTY_VENUE());
        }
        return selectedRow;
    }

    public void setSelectedRow(DataTableRow<Venue> row) {
        LOGGER.info("setSelectedRow() : " + row);

        if (row != null) {
            this.selectedRow = row;
        } else {
            this.selectedRow = new DataTableRow<Venue>(new Venue());
        }
    }

    public DataTableModel<GeocodeResult> getSearchModel() {
        return searchModel;
    }

    public MapModel getSearchMapModel() {
        return searchMapModel;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }


    public void onSaveRow(ActionEvent event) throws Exception {
        LOGGER.debug("onSaveRow() : " + event);

        try {
            Venue o = this.selectedRow.getObject();
            if (o.getId() != null && o.getId() > 0) {
                manager.merge(o);
            } else {
                manager.persist(o);
                if (this.rows != null) {
                    this.rows.add(new DataTableRow<Venue>(o));
                }
            }
            
            this.selectedRow.getResult().reset();

            //Set the rows to null so it'll be refreshed on the next get
            this.rows = null;

            //Reset selection
            if (dataTable != null) {
                LOGGER.info("dataTable.rowIndex = " + dataTable.getRowIndex());
                this.dataTable.setSelection(null);
                this.dataTable.setRowIndex(-1);
                this.dataTable.reset();
            }
        } catch (Throwable e) {
            if (!(e instanceof EJBException)) LOGGER.error(e.getMessage());

            String error = e.getMessage();
            if (e.getCause() != null & e.getCause() instanceof OptimisticLockException) {
                error = "Cannot save the value because it has changed or been deleted since it was last read.";
            }
            this.selectedRow.getResult().failed(error);

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", error);
            FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(), msg);
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("result", this.selectedRow.getResult());
    }

    public void onAddRow(ActionEvent event) {
        LOGGER.info("onAddRow() : " + event);
        //this.action = DataTableAction.Add;
        Venue o = new Venue();
        setSelectedRow(new DataTableRow<Venue>(o));

        FacesMessage msg = new FacesMessage("Info", "Enter the venue details and press Save");
        FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(), msg);
    }

    public void onDeleteRow(ActionEvent event) {
        LOGGER.debug("onDeleteRow() : " + event);

        try {
            DataTableRow<Venue> row = getSelectedRow();
            if (row != null && row.getObject() != null) {
                manager.remove(row.getObject());    

                this.rows.remove(row);
                
                FacesMessage msg = new FacesMessage("Venue Deleted", row.getObject().getName());
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            this.selectedRow.getResult().failed(e.getMessage());

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(), msg);
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("result", this.selectedRow.getResult());
    }

    public void onRefreshRows(ActionEvent event) {
        /* Deselected row */
        this.selectedRow = new DataTableRow<Venue>(new Venue());

        if (this.dataTable != null) {
            this.dataTable.setSelection(null);
            this.dataTable.setRowIndex(-1);
            this.dataTable.reset();
        }
        /* Set the cached rows to null */
        this.rows = null;
        /* Refresh the rows */
        getRows();
    }

    /**
     * This method creates a Venue record from the selected address search record. This is so that an address from
     * the Google search can be converted into a Venue used by the system.
     * @param event The action event
     */
    public void onCreateVenue(ActionEvent event) {
        DataTableRow<GeocodeResult> selected = this.getSearchModel().getSelected();
        if (selected != null && selected.getObject() != null) {
            GeocodeResult result = selected.getObject();

            Venue venue = new Venue();
            venue.setAddress(result.getFormattedAddress());
            venue.setGpsLatitude(result.getGeometry().getLocation().getLatitude());
            venue.setGpsLongitude(result.getGeometry().getLocation().getLongitude());
            venue.setName(result.getFormattedAddress());

            this.selectedRow = new DataTableRow<Venue>(venue);
        } else {
            this.selectedRow = new DataTableRow<Venue>(EMPTY_VENUE());
        }
    }

    /**
     * WARNING: Used by the DataTableModel.onSearch() method and should not be called directly
     * @param event The ActionEvent
     * @return List<DataTableRow<GeocodeResult>>
     * @throws DataTableException If there was an error searching
     */
    @Override
    public List<DataTableRow<GeocodeResult>> onSearch(ActionEvent event) throws DataTableException {
        try {
            List<DataTableRow<GeocodeResult>> rows = new ArrayList<DataTableRow<GeocodeResult>>();
            GeocodeResponse response = geocodeManager.findAddress(getSearchText());
            switch (response.getStatus()) {
                case OK:
                    for (GeocodeResult result : response.getResults()) {
                        rows.add(new DataTableRow(result));
                    }
                    break;
                case INVALID_REQUEST:
                    throw new GeocodeException("Invalid request");
                case REQUEST_DENIED:
                    throw new GeocodeException("Request denied");
                case OVER_QUERY_LIMIT:
                    throw new GeocodeException("Over query limit");
                case ZERO_RESULTS:
                    break;
            }
            return rows;
        } catch (Throwable e) {
            throw new DataTableException("Search failed", e);
        }
    }
}
