package za.co.yellowfire.navigation;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;

@SessionScoped
@ManagedBean(name = "navigator")
public class Navigator implements Serializable {
    private static final long serialVersionUID = 3970933260901989658L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LogType.CONTROLLER.getCategory());

    private static final String SECTION_VIEW_PARAMETER = "section";
    private static final String ITEM_VIEW_PARAMETER = "item";
    private static final String SEPARATOR = "/";

    @ManagedProperty(value = "#{navigationParser.groupsList}")
    private List<GroupDescriptor> groups;
    private SectionDescriptor currentSection;
    private ItemDescriptor currentItem;
    private String item;
    private String section;

    @PostConstruct
    public void init() {
        currentSection =null;
        currentItem = null;
    }

    public SectionDescriptor getCurrentSection() {
        String id = getViewParameter(SECTION_VIEW_PARAMETER);
        if (currentSection == null || !currentSection.getId().equals(id)) {
            if (id != null) {
                currentSection = findSectionById(id);
                currentItem = null;
            }
            if (currentSection == null) {
                currentSection = groups.get(0).getSections().get(0);
                currentItem = null;
            }
        }
        return currentSection;
    }

    public ItemDescriptor getCurrentItem() {
        String id = getViewParameter(ITEM_VIEW_PARAMETER);
        if (currentItem == null || !currentItem.getId().equals(id)) {
            if (id != null) {
                currentItem = getCurrentSection().getItemById(id);
            }
            if (currentItem == null) {
                currentItem = getCurrentSection().getItems().get(0);
            }
        }
        return currentItem;
    }

    private String getViewParameter(String name) {
        FacesContext fc = FacesContext.getCurrentInstance();
        String param = fc.getExternalContext().getRequestParameterMap().get(name);
        if (param != null && param.trim().length() > 0) {
            return param;
        } else {
            return null;
        }
    }

    public SectionDescriptor findSectionById(String id) {
        Iterator<GroupDescriptor> it = groups.iterator();
        while (it.hasNext()) {
            GroupDescriptor group = it.next();
            Iterator<SectionDescriptor> dit = group.getSections().iterator();
            while (dit.hasNext()) {
                SectionDescriptor section = (SectionDescriptor) dit.next();
                if (section.getId().equals(id)) {
                    return section;
                }
            }
        }
        return null;
    }

    public String getItemURI() {
        FacesContext context = FacesContext.getCurrentInstance();

        NavigationHandler handler = context.getApplication().getNavigationHandler();

        if (handler instanceof ConfigurableNavigationHandler) {
            ConfigurableNavigationHandler navigationHandler = (ConfigurableNavigationHandler) handler;

            final StringBuffer url = new StringBuffer(getCurrentSection().getId()).append(SEPARATOR).append(getCurrentItem().getId());
            NavigationCase navCase = navigationHandler.getNavigationCase(context, null, url.toString());
            String viewId = navCase.getToViewId(context);

            LOGGER.info("URL: " + url.toString() + " = " + viewId);
            return viewId;
        }

        return null;
    }

    public List<GroupDescriptor> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDescriptor> groups) {
        this.groups = groups;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}

