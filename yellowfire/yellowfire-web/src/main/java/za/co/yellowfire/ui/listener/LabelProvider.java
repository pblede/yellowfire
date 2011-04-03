package za.co.yellowfire.ui.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ListenerFor;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

/**
 * Taken from http://cagataycivici.wordpress.com/2011/02/11/label-provider-for-jsf-input-components/
 *
 * The listener sets the component.label to the outputLabel.value.
 * <h:outputLabel for="age" value="Age" />
 * <h:inputText id="age" value="#{bean.property}" label="Age" required="true"/>
 *
 * By doing this, validation messages do not have the component.id but rather the component.label in the message.
 */
@ListenerFor(sourceClass=HtmlOutputLabel.class, systemEventClass=javax.faces.event.PreValidateEvent.class)
public class LabelProvider implements SystemEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.LISTENER.getCategory());

    public boolean isListenerForSource(Object source) {
        LOGGER.info("isListenerForSource() : " + source);
        return true;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        HtmlOutputLabel outputLabel = (HtmlOutputLabel) event.getSource();
        UIComponent target = outputLabel.findComponent(outputLabel.getFor());

        if(target != null) {
            target.getAttributes().put("label", outputLabel.getValue());
        }
    }
}
