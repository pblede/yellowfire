package za.co.yellowfire.ui.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.domain.training.Outcome;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.model.AbstractDomainManagerDataTableListener;
import za.co.yellowfire.ui.model.DataTableModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@ViewScoped @ManagedBean(name = "outcomesController")
public class OutcomesController implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());
    private static final String NAME = "OutcomesController";

    @EJB(name = "DomainManager")
    private DomainManager manager;
    private DataTableModel<Outcome> dataModel;

    @PostConstruct
    private void init() {

        dataModel =
                new DataTableModel<Outcome>(
                        /* DataTableListener*/
                        new AbstractDomainManagerDataTableListener<Outcome>() {
                            @Override
                            public DomainManager getManager() {
                                return manager;
                            }

                            @Override
                            public String getLoadQuery() {
                                return Outcome.QRY_OUTCOMES;
                            }

                            @Override
                            public Outcome createEmpty() {
                                return new Outcome();
                            }
                        },
                        /* DataTableSearchListener*/
                        null);
    }



    public DataTableModel<Outcome> getDataModel() {
        return dataModel;
    }
}
