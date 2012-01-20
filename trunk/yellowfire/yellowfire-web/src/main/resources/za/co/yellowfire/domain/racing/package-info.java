@javax.xml.bind.annotation.XmlSchema(
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns= {
                @XmlNs(prefix="race", namespaceURI="http://www.bluefire.co.za/race"),
                @XmlNs(prefix="xs", namespaceURI="http://www.w3.org/2001/XMLSchema")
        })
@XmlAccessorType(value = XmlAccessType.FIELD)
package za.co.yellowfire.domain.racing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
