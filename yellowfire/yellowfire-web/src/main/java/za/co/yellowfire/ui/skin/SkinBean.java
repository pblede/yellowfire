package za.co.yellowfire.ui.skin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@SessionScoped
@ManagedBean(name = "skinBean")
public class SkinBean implements Serializable {
    private static final long serialVersionUID = -2399884208294434812L;
    private static final String SKIN_VIEW_PARAMETER = "skin";

    @ManagedProperty(value = "sunny")
    private String skin;

    private List<String> skins;

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

    // TODO: move to utility class. used in navigator also.
    private String getViewParameter(String name) {
        FacesContext fc = FacesContext.getCurrentInstance();
        String param = (String) fc.getExternalContext().getRequestParameterMap().get(name);
        if (param != null && param.trim().length() > 0) {
            return param;
        } else {
            return null;
        }
    }

    public String getSkin() {
        String currentSkin = getViewParameter(SKIN_VIEW_PARAMETER);
        if (currentSkin != null){
            skin = currentSkin;
        }
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public List<String> getSkins() {
        return skins;
    }

    @Override
    public String toString() {
        return getSkin();
    }
}

