package za.co.yellowfire.navigation;

import javax.faces.FacesException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URL;
import java.util.List;

@ApplicationScoped
@ManagedBean(name = "navigationParser")
public class NavigationParser {

    private List<GroupDescriptor> groupsList;
    private List<LinkDescriptor> linksList;

    @XmlRootElement(name = "root")
    private static final class CapitalsHolder {

        private List<GroupDescriptor> groups;
        private List<LinkDescriptor> links;
        
        @XmlElement(name = "group")
        public List<GroupDescriptor> getGroups() {
            return groups;
        }

        @SuppressWarnings("unused")
        public void setGroups(List<GroupDescriptor> groups) {
            this.groups = groups;
        }

        @XmlElementWrapper(name="links")
        @XmlElement(name = "link")
        public List<LinkDescriptor> getLinks() {
            return links;
        }

        @SuppressWarnings("unused")
        public void setLinks(List<LinkDescriptor> links) {
            this.links = links;
        }
    }

    public synchronized List<GroupDescriptor> getGroupsList() {
        if (groupsList == null) {
            ClassLoader ccl = Thread.currentThread().getContextClassLoader();
            URL resource = ccl.getResource("za/co/bluefire/navigation/navigation.xml");
            JAXBContext context;
            try {
                context = JAXBContext.newInstance(CapitalsHolder.class);
                CapitalsHolder capitalsHolder = (CapitalsHolder) context.createUnmarshaller().unmarshal(resource);
                groupsList = capitalsHolder.getGroups();
            } catch (JAXBException e) {
                throw new FacesException(e.getMessage(), e);
            }
        }

        return groupsList;
    }

    public synchronized List<LinkDescriptor> getLinksList() {
        if (linksList == null) {
            ClassLoader ccl = Thread.currentThread().getContextClassLoader();
            URL resource = ccl.getResource("za/co/bluefire/navigation/navigation.xml");
            JAXBContext context;
            try {
                context = JAXBContext.newInstance(CapitalsHolder.class);
                CapitalsHolder capitalsHolder = (CapitalsHolder) context.createUnmarshaller().unmarshal(resource);
                linksList = capitalsHolder.getLinks();
            } catch (JAXBException e) {
                throw new FacesException(e.getMessage(), e);
            }
        }

        return linksList;
    }

    public static void main(String[] args) throws Exception {
        NavigationParser parser = new NavigationParser();

        for (LinkDescriptor link : parser.getLinksList()) {
            System.out.println("link = " + link.getName() + ": href = " + link.getHref());
        }

        for (GroupDescriptor group : parser.getGroupsList()) {
            System.out.println("group = " + group.getName());
            for (SectionDescriptor section : group.getSections()) {
                System.out.println("\tsection = " + section.getName());
                for(ItemDescriptor item : section.getItems()) {
                    System.out.println("\t\titem = " + item.getName());
                }
            }
        }
    }
}

