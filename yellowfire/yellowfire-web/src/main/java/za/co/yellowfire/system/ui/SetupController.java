package za.co.yellowfire.system.ui;

import za.co.yellowfire.common.ui.AbstractCommonUIController;
import za.co.yellowfire.system.ApplicationManager;
import za.co.yellowfire.system.Manifest;
import za.co.yellowfire.system.SetupManager;
import za.co.yellowfire.system.Version;
import za.co.yellowfire.ui.FacesUtil;

import javax.enterprise.inject.Model;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import java.sql.SQLException;

/**
 * @author Mark P Ashworth
 * @version 1.0.0
 */
@Model
public class SetupController extends AbstractCommonUIController {
    private static final long serialVersionUID = 1L;

    //@Inject
    //Conversation conversation;
    
    @Inject
    private SetupManager setupManager;

    @Inject
    private ApplicationManager applicationManager;

    private Manifest manifest;
    
    private String script;
    
    public boolean isSetupRequired() {
        try {
            return setupManager.isSetupRequired(getManifest());
        } catch (SQLException e) {
            FacesUtil.addErrorMessage("Unable to retrieve application version", e);
        }
        return false;
    }

    public String getScript() {
        return script;
    }

    public Manifest getManifest() {
        if (manifest == null) {
            manifest = applicationManager.getManifest();
        }
        return manifest;
    }

    public Version getDatabaseVersion() {
        return getManifest().getDatabaseVersion();
    }

    public Version getApplicationVersion() {
        return getManifest().getImplementationVersion();
    }

    public void onScript(ActionEvent event) {
        try {
            this.script = setupManager.scriptUpgrade();
            FacesUtil.addInfoMessage("Database upgrade scripted!!!");
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Unable to script database", e);
        }
    }

    public void onUpgrade(ActionEvent event) {
        try {
            setupManager.executeUpgrade();
            applicationManager.reloadVersionInformation();
            FacesUtil.addInfoMessage("Database upgrade executed!!!");
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Unable to upgrade database", e);
        }
    }
}
