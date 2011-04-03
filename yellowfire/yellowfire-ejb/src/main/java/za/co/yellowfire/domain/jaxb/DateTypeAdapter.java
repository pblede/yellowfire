package za.co.yellowfire.domain.jaxb;

import za.co.yellowfire.Format;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Mark Ashworth
 * @version 0.0.1
 */
public class DateTypeAdapter extends XmlAdapter<String, Date> {

    @Override
    public String marshal(Date v) throws Exception {
        return (v != null ? new SimpleDateFormat(Format.DATE.getFormat()).format(v) : "");
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        if (v != null && !v.trim().equals("")) {
            return new SimpleDateFormat(Format.DATE.getFormat()).parse(v);
        }
        return null;
    }
}