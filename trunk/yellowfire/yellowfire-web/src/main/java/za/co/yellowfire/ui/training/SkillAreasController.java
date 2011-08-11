package za.co.yellowfire.ui.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.training.SkillArea;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.model.AbstractDomainManagerDataTableListener;
import za.co.yellowfire.ui.model.DataTableModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@ConversationScoped
@Named("skillAreasController")
public class SkillAreasController extends AbstractTrainingUIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

    @EJB(name = "DomainManager")
    private DomainManager manager;
    private DataTableModel<SkillArea> dataModel;

    @Inject
    Conversation conversation;
    
    @PostConstruct
    private void init() {

        dataModel =
                new DataTableModel<SkillArea>(
                        /* DataTableListener*/
                        new AbstractDomainManagerDataTableListener<SkillArea>() {
                            @Override
                            public DomainManager getManager() {
                                return manager;
                            }

                            @Override
                            public String getLoadQuery() {
                                return SkillArea.QRY_SKILL_AREAS;
                            }

                            @Override
                            public SkillArea createEmpty() {
                                return new SkillArea();
                            }
                        },
                        /* DataTableSearchListener*/
                        null);
    }



    public DataTableModel<SkillArea> getDataModel() {
        return dataModel;
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
        LOGGER.debug("Starting conversation: skill_area");
        if (conversation.isTransient())
            conversation.begin();

        return "skill_area";
    }

    /**
     * Completes the conversation
     *
     * @return The view to redirect to
     */
    public String onCompleteConversation() {
        LOGGER.debug("Completing conversation: skill_areas");
        if (!conversation.isTransient())
            conversation.end();

        return "skill_areas?faces-redirect=true";
    }
}
