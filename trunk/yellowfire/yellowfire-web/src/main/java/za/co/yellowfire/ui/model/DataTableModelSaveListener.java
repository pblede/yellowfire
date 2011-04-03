package za.co.yellowfire.ui.model;

import javax.el.ELResolver;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: MarkA
 * Date: 2011/03/29
 * Time: 4:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataTableModelSaveListener  implements ActionListener {

        @Override
        public void processAction(ActionEvent event) throws AbortProcessingException {
            FacesContext context = FacesContext.getCurrentInstance();
            ELResolver resolver = context.getApplication().getELResolver();
            resolver.getValue(context.getELContext(), null, "dataModel");
        }
    }
