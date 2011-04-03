package za.co.yellowfire.ui.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.domain.training.SkillArea;
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
@ViewScoped @ManagedBean(name = "skillAreasController")
public class SkillAreasController implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());
    private static final String NAME = "SkillAreasController";

    @EJB(name = "DomainManager")
    private DomainManager manager;
    private DataTableModel<SkillArea> dataModel;

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
}
