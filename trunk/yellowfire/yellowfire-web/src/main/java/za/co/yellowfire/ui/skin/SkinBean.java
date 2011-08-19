package za.co.yellowfire.ui.skin;

import za.co.yellowfire.ui.FacesUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 * @deprecated This should acually be part of CurrentUser because the skin can be @Produced
 */
@SessionScoped
@Named("skinBean")
public class SkinBean implements Serializable {
    private static final long serialVersionUID = -2399884208294434812L;
    private static final String SKIN_VIEW_PARAMETER = "skin";

    @ManagedProperty(value = "sunny")
    private String skin;

    private List<String> skins;

    /**
     * Initializes the array of available skins
     */
    @PostConstruct
    public void initialize() {
        skins = new ArrayList<String>();
        skins.add("sunny");
        skins.add("bluesky");
        skins.add("base");
        skins.add("redmond");
        skins.add("vader");
        skins.add("casablanca");
        skins.add("sam");
        skins.add("start");
    }

    /**
     * Returns the current skin
     * @return The skin name
     */
    public String getSkin() {
        String currentSkin = FacesUtil.getViewParameter(SKIN_VIEW_PARAMETER);
        if (currentSkin != null){
            skin = currentSkin;
        }
        return skin;
    }

    /**
     * Sets the skin
     * @param skin The new skin name
     */
    public void setSkin(String skin) {
        this.skin = skin;
    }

    /**
     * Returns the list of available skins
     * @return The list of skin names
     */
    public List<String> getSkins() {
        return skins;
    }

    /**
     * String value of the current skin name
     * @return The skin name
     */
    @Override
    public String toString() {
        return getSkin();
    }
}

