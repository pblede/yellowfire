package za.co.yellowfire.ui.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.Contact;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.domain.profile.SystemManager;
import za.co.yellowfire.domain.training.ContentType;
import za.co.yellowfire.domain.training.DurationType;
import za.co.yellowfire.domain.training.TrainingCourse;
import za.co.yellowfire.domain.training.TrainingProvider;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.manager.DomainQueryHint;
import za.co.yellowfire.ui.model.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.persistence.OptimisticLockException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@ViewScoped @ManagedBean(name = "coursesController")
public class CoursesController implements DataTableListener<TrainingCourse>, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

    @EJB(name = "SystemManager")
    private SystemManager systemManager;

    @EJB(name = "DomainManager")
    private DomainManager manager;
    private DataTableModel<TrainingCourse> dataModel;

    private String timezone;
    private List<DurationType> durationTypes = Arrays.asList(DurationType.Days, DurationType.Hours);
    private List<TrainingProvider> providers;
    private List<ContentType> contentTypes;
    
    @PostConstruct
    private void init() {
        dataModel = new DataTableModel<TrainingCourse>(this, null);
    }

    @SuppressWarnings("unused")
    public String getTimezone() {
        if (timezone == null) {
            timezone = systemManager.getTimezone();
        }
        return timezone;
    }

    public List<DurationType> getDurationTypes() {
        return durationTypes;
    }

    @SuppressWarnings("unchecked")
    public List<TrainingProvider> getTrainingProviders() {
        if (providers == null) {
            this.providers = (List<TrainingProvider>) manager.query(TrainingProvider.QRY_TRAINING_PROVIDERS, null, DomainQueryHint.REFRESH);
        }
        return this.providers;
    }

    @SuppressWarnings("unchecked")
    public List<ContentType> getContentTypes() {
        if (contentTypes == null) {
            this.contentTypes = (List<ContentType>) manager.query(ContentType.QRY_CONTENT_TYPES, null, DomainQueryHint.REFRESH);
        }
        return this.contentTypes;
    }

    private static TrainingCourse createEmptyEntity() {
        TrainingProvider p = new TrainingProvider();
        p.setId(0L);

        Contact o = new Contact();

        TrainingCourse c = new TrainingCourse();
        c.setId(0L);
        c.setContact(o);
        c.setProvider(p);

        return c;
    }

    public DataTableModel<TrainingCourse> getDataModel() {
        return dataModel;
    }

    /**
     * Creates an empty object which is used for new rows
     *
     * @return T
     */
    @Override
    public TrainingCourse createEmpty() {
        return createEmptyEntity();
    }

    /**
     * Used by the datatable model to load
     * @param event The action event that initiated the load
     * @return List
     * @throws DataTableException
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<DataTableRow<TrainingCourse>> onLoad(ActionEvent event) throws DataTableException {
        List<DataTableRow<TrainingCourse>> rows;
        List<TrainingCourse> values = (List<TrainingCourse>) manager.query(TrainingCourse.QRY_TRAINING_COURSES, null, DomainQueryHint.REFRESH);
        if (values != null) {
            rows = new ArrayList<DataTableRow<TrainingCourse>>(values.size());
            for (TrainingCourse value : values) {
                //Initialise @Embedded classes
                if (value.getContact() == null) value.setContact(new Contact());

                rows.add(new DataTableRow<TrainingCourse>(value));
            }
        } else {
            rows = new ArrayList<DataTableRow<TrainingCourse>>(0);
        }

        return rows;
    }

    /**
     * Called before the add function is intiated. The object returned will be used for the edit function.
     *
     * @param event The event that initiated the action
     * @return The blank object that should be used as a template for the add
     * @throws za.co.yellowfire.ui.model.DataTableException
     *          If there was an error encountered
     */
    @Override
    public TrainingCourse onAdd(ActionEvent event) throws DataTableException {
        return createEmptyEntity();
    }

    /**
     * Called before the modify function is intiated. The object returned will be used for the edit function.
     *
     * @param event  The event that initiated the action
     * @param object The object that should be used as a template for the add
     * @throws za.co.yellowfire.ui.model.DataTableException
     *          If there was an error encountered
     */
    @Override
    public void onModify(ActionEvent event, TrainingCourse object) throws DataTableException {
    }

    /**
     * Saves the object to a persistent store
     * @param event The event that initiated the action
     * @param course The course to save
     * @return DataTableAction.Add indicates that the saved object should be added to the table, else it was modified
     * @throws DataTableException If there was an error encountered
     */
    @Override 
    public DataTableAction onSave(ActionEvent event, TrainingCourse course) throws DataTableException {
        LOGGER.debug("onSave() : " + event + ":" + course);

        try {
            DataTableAction action;
            if (course.getId() != null && course.getId() > 0) {
                manager.merge(course);
                action = DataTableAction.Modify;
            } else {
                manager.persist(course);
                action = DataTableAction.Add;
            }
            return action;
        } catch (Throwable e) {
            if (!(e instanceof EJBException)) LOGGER.error(e.getMessage());

            String error;
            if (e.getCause() != null & e.getCause() instanceof OptimisticLockException) {
                error = "Cannot save the value because it has changed or been deleted since it was last read.";
                throw new DataTableException(error, e.getCause());
            }

            throw new DataTableException("Unable to save course", e);
        }
    }

    /**
     * Deletes the object from the persistent store
     *
     * @param event  The event that initiated the action
     * @param course The course to delete
     * @throws za.co.yellowfire.ui.model.DataTableException
     *          If there was an error encountered
     */
    @Override
    public void onDelete(ActionEvent event, TrainingCourse course) throws DataTableException {
        try {
            manager.remove(course);
        } catch (Throwable e) {
            if (!(e instanceof EJBException)) LOGGER.error(e.getMessage());

            String error;
            if (e.getCause() != null & e.getCause() instanceof OptimisticLockException) {
                error = "Cannot delete the value because it has changed or been deleted since it was last read.";
                throw new DataTableException(error, e.getCause());
            }

            throw new DataTableException("Unable to delete course", e);
        }
    }
}
