package za.co.yellowfire.common.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Embeddable
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String line01;
    private String line02;
    private String line03;
    private String postalCode;

    public Address() { }

    public Address(String line01, String line02, String line03, String postalCode) {
        this.line01 = line01;
        this.line02 = line02;
        this.line03 = line03;
        this.postalCode = postalCode;
    }

    public String getLine01() {
        return line01;
    }

    public void setLine01(String line01) {
        this.line01 = line01;
    }

    public String getLine02() {
        return line02;
    }

    public void setLine02(String line02) {
        this.line02 = line02;
    }

    public String getLine03() {
        return line03;
    }

    public void setLine03(String line03) {
        this.line03 = line03;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "Address {" +
                "line01='" + line01 + '\'' +
                ", line02='" + line02 + '\'' +
                ", line03='" + line03 + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
