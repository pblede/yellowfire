package za.co.yellowfire.navigation;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class GroupDescriptor extends BaseDescriptor {

    private static final long serialVersionUID = -3481702232804120885L;

    private List<SectionDescriptor> sections;

    @XmlElementWrapper(name="sections")
    @XmlElement(name="section")
    public List<SectionDescriptor> getSections() {
        return sections;
    }

    public void setSections(List<SectionDescriptor> sections) {
        this.sections = sections;
    }

}
