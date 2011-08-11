package za.co.yellowfire.ui.training;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.DateSelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.Venue;
import za.co.yellowfire.domain.profile.SystemManager;
import za.co.yellowfire.domain.training.TrainingCourse;
import za.co.yellowfire.domain.training.TrainingCourseDate;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.model.AbstractDomainManagerDataTableListener;
import za.co.yellowfire.ui.model.DataTableModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@ConversationScoped
@Named("courseDatesController")
public class CourseDatesController extends AbstractTrainingUIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

    @EJB(name = "SystemManager")
    private SystemManager systemManager;

    @EJB(name = "DomainManager")
    private DomainManager manager;

    private DataTableModel<TrainingCourseDate> dataModel;

    private transient DataTable dataTable;
    //private List<DataTableRow<TrainingCourseDate>> rows;
    //private DataTableRow<TrainingCourseDate> selectedRow = new DataTableRow<TrainingCourseDate>(createEmptyEntity());
    private List<TrainingCourse> courses;
    private List<Venue> venues;

    private String timezone;
    
    @Inject
    Conversation conversation;

    @PostConstruct
    private void init() {
        dataModel =
                new DataTableModel<TrainingCourseDate>(
                        /* DataTableListener*/
                        new AbstractDomainManagerDataTableListener<TrainingCourseDate>() {
                            @Override
                            public DomainManager getManager() {
                                return manager;
                            }

                            @Override
                            public String getLoadQuery() {
                                return TrainingCourseDate.QRY_COURSE_DATES;
                            }

                            @Override
                            public TrainingCourseDate createEmpty() {
                                return createEmptyEntity();
                            }
                        },
                        /* DataTableSearchListener*/
                        null);
    }

    public DataTableModel<TrainingCourseDate> getDataModel() {
        return dataModel;
    }
    
    public String getTimezone() {
        if (timezone == null) {
            timezone = systemManager.getTimezone();
        }
        return timezone;
    }

    private static TrainingCourseDate createEmptyEntity() {
        TrainingCourse c = new TrainingCourse();
        c.setId(0L);

        Venue v = new Venue();
        v.setId(0L);

        TrainingCourseDate o = new TrainingCourseDate();
        o.setCourse(c);
        o.setVenue(v);
        return o;
    }

//    @SuppressWarnings("unchecked")
//    public List<DataTableRow<TrainingCourseDate>> getRows() {
//        if (this.rows == null) {
//            List<TrainingCourseDate> values = (List<TrainingCourseDate>) manager.query(TrainingCourseDate.QRY_COURSE_DATES, null, DomainQueryHint.REFRESH);
//            if (values != null) {
//                this.rows = new ArrayList<DataTableRow<TrainingCourseDate>>(values.size());
//                for (TrainingCourseDate value : values) {
//                    rows.add(new DataTableRow<TrainingCourseDate>(value));
//                }
//            }
//        }
//        return this.rows;
//    }

    @SuppressWarnings("unchecked")
    public List<TrainingCourse> getCourses() {
        if (this.courses == null) {
            this.courses = (List<TrainingCourse>) manager.query(TrainingCourse.QRY_TRAINING_COURSES, null);
        }
        return this.courses;
    }

    @SuppressWarnings("unchecked")
    public List<Venue> getVenues() {
        if (this.venues == null) {
            this.venues = (List<Venue>) manager.query(Venue.QRY_VENUES, null);
        }
        return this.venues;
    }
//
//    public DataTableRow<TrainingCourseDate> getSelectedRow() {
//        return selectedRow;
//    }
//
//    public void setSelectedRow(DataTableRow<TrainingCourseDate> row) {
//        LOGGER.info("setSelectedRow() : " + row);
//
//        if (row != null) {
//            this.selectedRow = row;
//        } else {
//            this.selectedRow = new DataTableRow<TrainingCourseDate>(createEmptyEntity());
//        }
//    }

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
//            TrainingCourseDate o = this.selectedRow.getObject();
//            if (o.getId() != null && o.getId() > 0) {
//                manager.merge(o);
//            } else {
//                manager.persist(o);
//                LOGGER.info("onSaveRow() : " + o);
//                this.rows.add(new DataTableRow<TrainingCourseDate>(o));
//            }
//
//            this.selectedRow.getResult().reset();
//
//            //Set the rows to null so it'll be refreshed on the next get
//            this.rows = null;
//
//            //Reset selection
//            LOGGER.info("dataTable.rowIndex = " + dataTable.getRowIndex());
//            this.dataTable.setSelection(null);
//            this.dataTable.setRowIndex(-1);
//            this.dataTable.reset();
//
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
//
//
//        setSelectedRow(new DataTableRow<TrainingCourseDate>(createEmptyEntity()));
//
//        FacesMessage msg = new FacesMessage("Info", "Enter the course date details and press Save");
//        FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(), msg);
//    }
//
//    public void onDeleteRow(ActionEvent event) {
//        LOGGER.debug("onDeleteRow() : " + event);
//
//        try {
//            DataTableRow<TrainingCourseDate> row = getSelectedRow();
//            if (row != null && row.getObject() != null) {
//                manager.remove(row.getObject());
//
//                this.rows.remove(row);
//
//                FacesMessage msg = new FacesMessage("Course date deleted", row.getObject().getStartDate().toString());
//                FacesContext.getCurrentInstance().addMessage(null, msg);
//            }
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//            this.selectedRow.getResult().failed(e.getMessage());
//
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
//            FacesContext.getCurrentInstance().addMessage(event.getComponent().getId(), msg);
//        }
//
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.addCallbackParam("result", this.selectedRow.getResult());
//    }
//
//    public void onRefreshRows(ActionEvent event) {
//        /* Deselected row */
//        this.selectedRow = new DataTableRow<TrainingCourseDate>(new TrainingCourseDate());
//        this.dataTable.setSelection(null);
//        this.dataTable.setRowIndex(-1);
//        this.dataTable.reset();
//        /* Set the cached rows to null */
//        this.rows = null;
//        /* Refresh the rows */
//        getRows();
//    }

    /**
     * Defaults the training end date to the start date if:-
     *   1. The end date is null
     *   2. The end date is after the start date
     * @param event The date selection event
     */
    @SuppressWarnings("unused")
    public void onStartDateSelected(DateSelectEvent event) {
        if (dataModel.getSelected() != null) {
            if (dataModel.getSelected().getObject() != null) {
                TrainingCourseDate o = dataModel.getSelected().getObject();
                if (o.getEndDate() == null) {
                    o.setEndDate(event.getDate());
                } else if (o.getEndDate().after(event.getDate())) {
                    o.setEndDate(event.getDate());
                }
            }
        }
    }


    public boolean isInConversation() {
        return (conversation != null && !conversation.isTransient());
    }

    /**
     * Starts the conversation of editing the course
     *
     * @return The view to proceed to
     */
    public String onStartConversation() {
        LOGGER.debug("Starting conversation: outcome");
        if (conversation.isTransient())
            conversation.begin();

        return "outcome";
    }

    /**
     * Completes the conversation
     *
     * @return The view to redirect to
     */
    public String onCompleteConversation() {
        LOGGER.debug("Completing conversation: outcome");
        if (!conversation.isTransient())
            conversation.end();

        return "outcomes?faces-redirect=true";
    }}
