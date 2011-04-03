package za.co.yellowfire.navigation;

import javax.xml.bind.annotation.XmlElement;

public class LinkDescriptor extends BaseDescriptor {
    private static final long serialVersionUID = 2704627392818039062L;

    private String href;

    @XmlElement
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
