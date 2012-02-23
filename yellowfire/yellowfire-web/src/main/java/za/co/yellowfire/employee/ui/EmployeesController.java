package za.co.yellowfire.employee.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.common.annotation.Titles;
import za.co.yellowfire.common.domain.Title;
import za.co.yellowfire.common.log.LogType;
import za.co.yellowfire.domain.ChangeItem;
import za.co.yellowfire.domain.ChangeTrackingException;
import za.co.yellowfire.domain.profile.Profile;
import za.co.yellowfire.employee.domain.Employee;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.UILookupController;
import za.co.yellowfire.ui.model.AbstractDomainManagerDataTableListener;
import za.co.yellowfire.ui.model.DataTableException;
import za.co.yellowfire.ui.model.DataTableModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Named
@ConversationScoped
public class EmployeesController extends AbstractEmployeeUIController implements UILookupController<Employee> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

    @EJB(name = "DomainManager")
    private DomainManager manager;

    @Inject
    private Conversation conversation;

    @Inject @Titles
    private String[] titles = null;
    
    private DataTableModel<Employee> dataModel;

    @PostConstruct
    private void init() {

        dataModel =
                new DataTableModel<Employee>(
                        /* DataTableListener*/
                        new AbstractDomainManagerDataTableListener<Employee>() {
                            @Override
                            public DomainManager getManager() {
                                return manager;
                            }

                            @Override
                            public String getLoadQuery() {
                                return Employee.QRY_EMPLOYEES;
                            }

                            @Override
                            public Employee createEmpty() {
                                return new Employee();
                            }

                            @Override
                            public Employee onSaveExisting(ActionEvent event, Employee employee) throws DataTableException {
                                try {
                                    if (employee != null && employee.getProfile() != null) {
                                        List<ChangeItem> changeItems = employee.getProfile().changes();
                                        if (changeItems.size() > 0) {
                                            employee.setProfile((Profile) manager.merge(Profile.class, employee.getProfile().getUserId(), changeItems));
                                        }
                                    }
                                } catch (ChangeTrackingException e) {
                                    throw new DataTableException("Unable to save profile to database", e);
                                }
                                return employee;
                            }
                        },
                        /* DataTableSearchListener*/
                        null);

        dataModel.addColumn("Code", "code");
        dataModel.addColumn("Name", "firstName");
        dataModel.addColumn("Surname", "lastName");
        dataModel.addColumn("ID Number", "idNumber");
    }

    public String[] getTitles() {
        return titles;
    }

    public DataTableModel<Employee> getDataModel() {
        return dataModel;
    }

    public List<Profile> getUnlinkedProfiles() {
        return (List<Profile>) manager.query(Employee.QRY_UNLINKED_PROFILES, null);
    }

    public boolean isInConversation() {
        return (conversation != null && !conversation.isTransient());
    }

    /**
     * Starts the conversation of editing
     * @return The view to proceed to
     */
    public String onStartConversation() {
        LOGGER.debug("Starting conversation: employee");
        if (conversation.isTransient())
            conversation.begin();

        dataModel.onTrackChanges(null);

        return "employee";
    }

    /**
     * Completes the conversation
     * @return The view to redirect to
     */
    public String onCompleteConversation() {
        LOGGER.debug("Completing conversation: employee");
        if (!conversation.isTransient())
            conversation.end();
        return "employees?faces-redirect=true";
    }

    /**
     * Called to link the profile to an employee record
     */
    public String onLinkProfile() {
        Employee employee = dataModel.getSelected().getObject();
        if (employee.getProfile() != null) {
            dataModel.getSelected().setObject((Employee) manager.merge(employee));
        }
        return "employee";
    }
}
