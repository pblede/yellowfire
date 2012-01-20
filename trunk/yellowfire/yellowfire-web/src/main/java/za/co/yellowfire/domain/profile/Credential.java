package za.co.yellowfire.domain.profile;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class Credential implements Serializable {
    private static final long serialVersionUID = 1L;

    @Size(min = 1, max = 64)
    private String name;

    @Size(min = 5, max = 64)
    private String password;

    public Credential() {}

    public Credential(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
