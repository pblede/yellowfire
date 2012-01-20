package za.co.yellowfire.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Embeddable
public class Contact implements Serializable {

    @Size(min = 0, max = 24)
    @Column(name = "contact_name")
    private String name;

    @Size(min = 0, max = 24)
    @Column(name = "contact_telephone_no")
    private String telephoneNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", telephoneNo='" + telephoneNo + '\'' +
                '}';
    }
}
