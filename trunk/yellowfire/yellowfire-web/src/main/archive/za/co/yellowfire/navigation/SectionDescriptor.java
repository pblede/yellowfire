package za.co.yellowfire.navigation;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class SectionDescriptor extends BaseDescriptor {

    private static final long serialVersionUID = 6822187362271025752L;

    private static final String BASE_SAMPLES_DIR = "/richfaces/";

    private List<ItemDescriptor> items;

    public ItemDescriptor getItemById(String id) {
        for (ItemDescriptor item : getItems()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return items.get(0);
    }

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    public List<ItemDescriptor> getItems() {
        return items;
    }

    public void setItems(List<ItemDescriptor> items) {
        this.items = items;
    }

}
