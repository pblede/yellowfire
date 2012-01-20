package za.co.yellowfire.domain.racing;

import za.co.yellowfire.domain.racing.NoteType;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Mark Ashworth
 * @version 0.0.1
 */
public class NoteTypeAdapter extends XmlAdapter<String, NoteType> {

    @Override
    public String marshal(NoteType v) throws Exception {
        return v.name();
    }

    @Override
    public NoteType unmarshal(String v) throws Exception {
        return NoteType.valueOf(v);
    }
}
