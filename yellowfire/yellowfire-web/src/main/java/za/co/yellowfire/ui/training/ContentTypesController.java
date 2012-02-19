package za.co.yellowfire.ui.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.training.ContentType;
import za.co.yellowfire.common.log.LogType;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.UILookupController;
import za.co.yellowfire.ui.model.AbstractDomainManagerDataTableListener;
import za.co.yellowfire.ui.model.DataTableModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@ConversationScoped
@Named("contentTypesController")
public class ContentTypesController extends AbstractTrainingUIController implements UILookupController<ContentType> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

    @EJB(name = "DomainManager")
    private DomainManager manager;

    @Inject
    private Conversation conversation;

    private DataTableModel<ContentType> dataModel;

    @PostConstruct
    private void init() {

        dataModel =
                new DataTableModel<ContentType>(
                        /* DataTableListener*/
                        new AbstractDomainManagerDataTableListener<ContentType>() {
                            @Override
                            public DomainManager getManager() {
                                return manager;
                            }

                            @Override
                            public String getLoadQuery() {
                                return ContentType.QRY_CONTENT_TYPES;
                            }

                            @Override
                            public ContentType createEmpty() {
                                return new ContentType();
                            }
                        },
                        /* DataTableSearchListener*/
                        null);
    }



    public DataTableModel<ContentType> getDataModel() {
        return dataModel;
    }

    public boolean isInConversation() {
        return (conversation != null && !conversation.isTransient());
    }

    /**
     * Starts the conversation of editing
     * @return The view to proceed to
     */
    public String onStartConversation() {
        LOGGER.debug("Starting conversation: outcome");
        if (conversation.isTransient())
            conversation.begin();

        return "content_type";
    }

    /**
     * Completes the conversation
     * @return The view to redirect to
     */
    public String onCompleteConversation() {
        LOGGER.debug("Completing conversation: outcome");
        if (!conversation.isTransient())
            conversation.end();

        return "content_types?faces-redirect=true";
    }
}
