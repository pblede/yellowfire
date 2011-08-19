package za.co.yellowfire.ui.training;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.Venue;
import za.co.yellowfire.domain.geocode.GeocodeManager;
import za.co.yellowfire.domain.geocode.GeocodeResult;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.manager.DomainQueryHint;
import za.co.yellowfire.solarflare.SearchManager;
import za.co.yellowfire.ui.FacesUtil;
import za.co.yellowfire.ui.model.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@ViewScoped @Named("venuesController")
public class VenuesController extends AbstractTrainingUIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

    @Inject
    private SearchManager searchManager;
    
    @EJB(name = "DomainManager")
    private DomainManager manager;
    private transient DataTable dataTable;
    private List<DataTableRow<Venue>> rows;
    private DataTableRow<Venue> selectedRow = new DataTableRow<Venue>(new Venue());

    /*Venue*/
    private DataTableModel<Venue> dataModel;

    /*Geocode*/
    @EJB(name = "GeocodeManager")
    private GeocodeManager geocodeManager;
    private DataTableModel<GeocodeResult> searchModel;
    private MapModel searchMapModel;

    private String searchText;

    @PostConstruct
    private void init() {
        searchMapModel = new DefaultMapModel();
        searchModel =
                new DataTableModel<GeocodeResult>(
                        /* DataTableListener*/
                        new GeocodeResultDataTableModelListener(manager, searchMapModel),
                        /* DataTableSearchListener*/
                        new GeocodeManagerDataTableSearchListener() {
                            @Override
                            public GeocodeManager getManager() {
                                return geocodeManager;
                            }
                        }
                );

        dataModel =
                new DataTableModel<Venue>(
                        /* DataTableListener*/
                        new VenueDataTableModelListener(manager),
                        /* DataTableSearchListener*/
                        new AbstractSearchManagerDataTableSearchListener<Venue>() {
                            @Override public SearchManager getManager() { return searchManager; }
                            @Override public Class getModelClass() { return Venue.class; }
                        }
                );
    }



    public DataTableModel<Venue> getDataModel() {
        return dataModel;
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
            this.selectedRow = new DataTableRow<Venue>(new Venue());
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


//    public void onSaveRow(ActionEvent event) throws Exception {
//        LOGGER.debug("onSaveRow() : " + event);
//
//        try {
//            Venue o = this.selectedRow.getObject();
//            if (o.getId() != null && o.getId() > 0) {
//                manager.merge(o);
//            } else {
//                manager.persist(o);
//                if (this.rows != null) {
//                    this.rows.add(new DataTableRow<Venue>(o));
//                }
//            }
//
//            this.selectedRow.getResult().reset();
//
//            //Set the rows to null so it'll be refreshed on the next get
//            this.rows = null;
//
//            //Reset selection
//            if (dataTable != null) {
//                LOGGER.info("dataTable.rowIndex = " + dataTable.getRowIndex());
//                this.dataTable.setSelection(null);
//                this.dataTable.setRowIndex(-1);
//                this.dataTable.reset();
//            }
//        } catch (Throwable e) {
//            if (!(e instanceof EJBException)) LOGGER.error(e.getMessage());
//
//            String error = e.getMessage();
//            if (e.getCause() != null & e.getCause() instanceof OptimisticLockException) {
//                error = "Cannot save the value because it has changed or been deleted since it was last read.";
//            }
//            this.selectedRow.getResult().failed(error);
//
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", error);
//            FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(), msg);
//        }
//
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.addCallbackParam("result", this.selectedRow.getResult());
//    }
//
//    public void onAddRow(ActionEvent event) {
//        LOGGER.info("onAddRow() : " + event);
//        //this.action = DataTableAction.Add;
//        Venue o = new Venue();
//        setSelectedRow(new DataTableRow<Venue>(o));
//
//        FacesUtil.addInfoMessage(event.getComponent().getId(), MessageResources.MESSAGE(MessageKey.dialogInfo), "Enter the venue details and press Save");
//    }
//
//    public void onDeleteRow(ActionEvent event) {
//        LOGGER.debug("onDeleteRow() : " + event);
//
//        try {
//            DataTableRow<Venue> row = getSelectedRow();
//            if (row != null && row.getObject() != null) {
//                manager.remove(row.getObject());
//
//                this.rows.remove(row);
//
//                FacesMessage msg = new FacesMessage("Venue Deleted", row.getObject().getName());
//                FacesContext.getCurrentInstance().addMessage(null, msg);
//            }
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//            this.selectedRow.getResult().failed(e.getMessage());
//            FacesUtil.addErrorMessage(e.getMessage());
//        }
//        PrimeFacesUtil.addCallbackParam();
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.addCallbackParam("result", this.selectedRow.getResult());
//    }

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
    public void onCreateVenue(ActionEvent event) throws DataTableException {

        try {
            DataTableRow<GeocodeResult> selected = this.getSearchModel().getSelected();
            if (selected != null && selected.getObject() != null) {
                GeocodeResult result = selected.getObject();

                DecimalFormat format = new DecimalFormat("##0.0######");

                Venue venue = new Venue();
                venue.setAddress(result.getFormattedAddress());
                venue.setGpsLatitude(format.parse(result.getGeometry().getLocation().getLatitude()).doubleValue());
                venue.setGpsLongitude(format.parse(result.getGeometry().getLocation().getLongitude()).doubleValue());
                venue.setName(result.getFormattedAddress());

                this.selectedRow = new DataTableRow<Venue>(venue);
            } else {
                this.selectedRow = new DataTableRow<Venue>(new Venue());
            }
        } catch (ParseException e) {
            throw new DataTableException("Unable to create venue because GPS coordinates could not be parsed", e);
        }
    }

    public void onLocationSelect(SelectEvent event) {
        DataTableRow<GeocodeResult> result = (DataTableRow<GeocodeResult>) event.getObject();
        FacesUtil.addInfoMessage("Location Selected", result.getObject().getFormattedAddress());
    }
}
