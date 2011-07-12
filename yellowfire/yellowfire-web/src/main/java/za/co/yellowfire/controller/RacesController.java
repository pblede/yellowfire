package za.co.yellowfire.controller;

import org.joda.time.DateMidnight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.racing.Race;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.model.AbstractDomainManagerDataTableListener;
import za.co.yellowfire.ui.model.DataTableModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ViewScoped
@Named("racesController")
public class RacesController extends AbstractController {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

    @EJB(name = "DomainManager")
    private DomainManager manager;
    private DataTableModel<Race> dataModel;

    private DateMidnight currentDate = new DateMidnight();

    @PostConstruct
    private void init() {
        dataModel =
                new DataTableModel<Race>(
                        /* DataTableListener*/
                        new AbstractDomainManagerDataTableListener<Race>() {

                            @Override
                            public DomainManager getManager() {
                                return manager;
                            }

                            @Override
                            public String getLoadQuery() {
                                return Race.QRY_UPCOMING_RACES;
                            }

                            @Override
                            public Map<String, Object> getLoadQueryParameters() {
                                Map<String, Object> params = new HashMap<String, Object>();
                                params.put(Race.FIELD_DATE, currentDate.toDate());
                                return params;
                            }

                            @Override
                            public Race createEmpty() {
                                return createEmptyEntity();
                            }
                        },
                        /* DataTableSearchListener*/
                        null);
    }

    private static Race createEmptyEntity() {

        Race r = new Race();
        r.setId(0L);

        return r;
    }

    public DataTableModel<Race> getDataModel() {
        return dataModel;
    }

    public String getAction() {
        return "/race";
    }

    /**
     * Select a race for viewing
     */
    public String view() { return "race"; }

    /**
     * Select a race for registering
     */
    public String register() { return "race/registration"; }
    
    /**
     * Select a race
     * @param event The event
     */
    public void select(ActionEvent event) {
    }
}
