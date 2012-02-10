package za.co.yellowfire.domain.profile;

import org.eclipse.persistence.config.QueryHints;
import org.hibernate.validator.constraints.Email;
import org.picketlink.idm.api.User;
import org.picketlink.idm.impl.api.model.SimpleUser;
import za.co.yellowfire.domain.racing.Club;
import za.co.yellowfire.jaxb.DateTypeAdapter;

import javax.persistence.*;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author Mark P Ashworth
 * @version 0.1.0
 */
@Entity(name = "User")
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(
            name="qry.user.login",
            query="select u from User u where u.name = :name and u.password = :password",
            hints={
                @QueryHint(name= QueryHints.REFRESH, value="true")
            }
        ),
        @NamedQuery(
            name="qry.user.name",
            query="select u from User u where u.name = :name"
        ),
        @NamedQuery(
            name="qry.user.verification.key",
            query="select u from User u where u.verificationKey = :verificationKey"
        )
		
})
@Table(name = "person", schema = "cde", uniqueConstraints = {@UniqueConstraint(columnNames = {"person_name"})})
public class Profile implements User, Serializable {
    private static final long serialVersionUID = 1L;

    public static final String QRY_USER_LOGIN = "qry.user.login";
    public static final String QRY_USER_NAME = "qry.user.name";
    public static final String QRY_USER_VERIFICATION_KEY = "qry.user.verification.key";

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_VERIFICATION_KEY = "verificationKey";

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id", nullable = false, insertable = true, updatable = true)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    @Basic @Size(min = 1, max = 64)
    @Column(name = "person_name", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @XmlAttribute(name = "name", required = true)
    private String name;

    @Basic @Size(min = 5, max = 64)
    @Column(name = "person_password", nullable = false, insertable = true, updatable = true, length = 512, precision = 0)
    @XmlAttribute(name = "name", required = true)
    private String password;

    @Basic @Size(min = 1, max = 64)
    @Column(name = "person_firstname", nullable = false, insertable = true, updatable = true, length = 64, precision = 0)
    @XmlAttribute(name = "first_name", required = true)
    private String firstName;

    @Basic @Size(min = 1, max = 64)
    @Column(name = "person_lastname", nullable = false, insertable = true, updatable = true, length = 64, precision = 0)
    @XmlAttribute(name = "last_name", required = true)
    private String lastName;


    @Past(message = "The birth date must be in the past")
    @XmlJavaTypeAdapter(value=DateTypeAdapter.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "person_dob", nullable = false, insertable = true, updatable = true)
    @XmlAttribute(name = "dob", required = true)
    private Date birthDate;

    @Basic @Email
    @Column(name = "person_email")
    @XmlAttribute(name = "email", required = true)
    private String email;

    @XmlJavaTypeAdapter(value=RoleTypeAdapter.class)
    @XmlAttribute(name = "role", required = true)
    @Column(name = "person_group_name")
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column(name = "person_verification_key", length = 128)
    private String verificationKey;

    @Column(name = "person_verified")
    private boolean verified;

    @Column(name = "create_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    
    @Column(name = "update_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Column(name = "verified_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date verifiedDate;


    /* Transient */
    @Transient private boolean passwordChanged = false;
    @Transient private String passwordConfirmation;

    public Profile() {}

    public Profile(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Long getUserId() {
        return id;
    }

    @Override
    public String getId() {
        return this.name;
    }

    /**
     * @return the pointer to the IdentityType. For User this will return same value as getId(). For Group key contains
     *         encoded group type and name imformation. In default implementation it can look as follows:
     *         "jbpid_group_id_._._GROUP_TYPE_._._GROUP_NAME". Still prefix and format of key can change in the future so
     *         PersistenceManager.createGroupId(String groupName, String groupType) method should be used to create it for Group.
     */
    @Override
    public String getKey() {
        return this.name;
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
        if (password == null && this.password == null) {
            this.passwordChanged = false;
        } else if (this.password != null) {
            this.passwordChanged = this.password.equals(password);
        } else {
            this.passwordChanged = true;
        }
        this.password = password;
    }

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}
 
	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (this.email == null && email != null) {
            generateVerificationKey();
            setVerified(false);
        } else if (this.email != null && !this.email.equals(email)) {
            generateVerificationKey();
            setVerified(false);
        }

        this.email = email;
    }

    
	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

    public String getVerificationKey() {
        return verificationKey;
    }

    public void setVerificationKey(String verificationKey) {
        this.verificationKey = verificationKey;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
        if (this.verified == true) {
            this.verifiedDate = new Date();
        } else {
            this.verifiedDate = null;
        }
    }

    public Date getCreated() {
		return created;
	}

	public Date getUpdated() {
		return updated;
	}

    public Date getVerified() {
        return verifiedDate;
    }

    protected void generateVerificationKey() {
        this.verificationKey = UUID.randomUUID().toString();
    }

    public void setEntityUpdated() {
		this.updated = new Date();
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile user = (Profile) o;

        if (name != null ? !name.equals(user.name) : user.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='*******\'" +
                ", birthDate=" + birthDate +
                ", verified=" + verified +
                ", verifiedDate=" + verifiedDate +
                ", email='" + email + '\'' +
                '}';
    }
}
