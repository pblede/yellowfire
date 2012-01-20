package za.co.yellowfire.ui.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.Contact;
import za.co.yellowfire.domain.NullDomainObject;
import za.co.yellowfire.domain.profile.SystemManager;
import za.co.yellowfire.domain.training.*;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.model.AbstractDomainManagerDataTableListener;
import za.co.yellowfire.ui.model.DataTableModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@ConversationScoped
@Named("coursesController")
public class CoursesController extends AbstractTrainingUIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

    @EJB(name = "SystemManager")
    private SystemManager systemManager;

    @EJB(name = "DomainManager")
    private DomainManager manager;
    private DataTableModel<TrainingCourse> dataModel;

    //@Inject
    //@org.jboss.seam.solder.logging.Category("courses")
    //private UILogger logger;

    private String timezone;
    private List<DurationType> durationTypes = Arrays.asList(DurationType.Days, DurationType.Hours);
    private List<TrainingProvider> trainingProviders;
    private List<ContentType> contentTypes;
    private List<Category> categories;
    private SelectItem nullSelectItem = new SelectItem(new NullDomainObject(), "Select...", "Select...", false, false, true);

    @Inject
    Conversation conversation;

    @PostConstruct
    private void init() {
        dataModel =
                new DataTableModel<TrainingCourse>(
                        /* DataTableListener*/
                        new AbstractDomainManagerDataTableListener<TrainingCourse>() {
                            @Override
                            public DomainManager getManager() {
                                return manager;
                            }

                            @Override
                            public String getLoadQuery() {
                                return TrainingCourse.QRY_TRAINING_COURSES;
                            }

                            @Override
                            public TrainingCourse createEmpty() {
                                return createEmptyEntity();
                            }
                        },
                        /* DataTableSearchListener*/
                        null);
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
        if (conversation.isTransient())
            conversation.begin();

        return "course";
    }

    /**
     * Completes the conversation
     *
     * @return The view to redirect to
     */
    public String onCompleteConversation() {
        if (!conversation.isTransient())
            conversation.end();

        return "courses?faces-redirect=true";
    }

    @SuppressWarnings("unused")
    public String getTimezone() {
        if (timezone == null) {
            timezone = systemManager.getTimezone();
        }
        return timezone;
    }

    /**
     * Returns the domain object that represents a null
     *
     * @return SelectItem
     */
    public SelectItem getNullSelectItem() {
        return nullSelectItem;
    }

    public List<DurationType> getDurationTypes() {
        return durationTypes;
    }

    @SuppressWarnings("unchecked")
    public List<TrainingProvider> getTrainingProviders() {
        if (this.trainingProviders == null)
            this.trainingProviders = (List<TrainingProvider>) manager.query(TrainingProvider.QRY_TRAINING_PROVIDERS, null);
        return this.trainingProviders;
    }

    @SuppressWarnings("unchecked")
    public List<ContentType> getContentTypes() {
        if (this.contentTypes == null)
            this.contentTypes = (List<ContentType>) manager.query(ContentType.QRY_CONTENT_TYPES, null);
        return this.contentTypes;
    }

    @SuppressWarnings("unchecked")
    public List<Category> getCategories() {
        if (this.categories == null)
            this.categories = (List<Category>) manager.query(Category.QRY_COURSE_CATEGORIES, null);
        return categories;
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
}
