package za.co.yellowfire.ui.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;
import za.co.yellowfire.domain.training.ContentType;
import za.co.yellowfire.manager.DomainManager;
import za.co.yellowfire.ui.model.AbstractDomainManagerDataTableListener;
import za.co.yellowfire.ui.model.DataTableModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@ViewScoped @Named("contentTypesController")
public class ContentTypesController extends AbstractTrainingUIController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());
    private static final String NAME = "ContentTypesController";

    @EJB(name = "DomainManager")
    private DomainManager manager;
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
}
