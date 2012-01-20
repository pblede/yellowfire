package za.co.yellowfire.domain.profile;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Mark Ashworth
 * @version 0.0.1
 */
public class RoleTypeAdapter extends XmlAdapter<String, RoleType> {

    @Override
    public String marshal(RoleType v) throws Exception {
        return v.name();
    }

    @Override
    public RoleType unmarshal(String v) throws Exception {
        return RoleType.valueOf(v);
    }
}
