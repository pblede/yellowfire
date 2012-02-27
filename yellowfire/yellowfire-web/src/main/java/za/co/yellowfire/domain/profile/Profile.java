package za.co.yellowfire.domain.profile;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.config.QueryHints;
import org.hibernate.validator.constraints.Email;
import org.picketlink.idm.api.User;
import za.co.yellowfire.common.domain.Address;
import za.co.yellowfire.common.domain.PersonName;
import za.co.yellowfire.common.domain.Title;
import za.co.yellowfire.domain.DomainEntity;
import za.co.yellowfire.jaxb.DateTypeAdapter;

import javax.persistence.*;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.*;

/**
 * TODO Do not implemented the User interface but rather return a property that contains this information. This causing
 * issues with DomainObject and DomainEntity throughout the system.
 *
 * @author Mark P Ashworth
 * @version 0.1.0
 */
@Entity(name = "User")
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(
            name="qry.user.login",
            query="select u from User u where u.userName = :userName and u.password = :password",
            hints={
                @QueryHint(name= QueryHints.REFRESH, value="true")
            }
        ),
        @NamedQuery(
            name="qry.user.name",
            query="select u from User u where u.userName = :userName"
        ),
        @NamedQuery(
            name="qry.user.verification.key",
            query="select u from User u where u.verificationKey = :verificationKey"
        )
		
})
@Table(name = "person", schema = "cde", uniqueConstraints = {@UniqueConstraint(columnNames = {"person_name"})})
public class Profile extends DomainEntity implements User {
    private static final long serialVersionUID = 1L;

    public static final String QRY_USER_LOGIN = "qry.user.login";
    public static final String QRY_USER_NAME = "qry.user.name";
    public static final String QRY_USER_VERIFICATION_KEY = "qry.user.verification.key";

    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_NAME = "userName";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_FIRST_NAME = "name.name";
    public static final String FIELD_LAST_NAME = "name.surname";
    public static final String FIELD_VERIFICATION_KEY = "verificationKey";

    public static final String[] TRACKED = new String[] {
            FIELD_USER_NAME,
            FIELD_PASSWORD,
            FIELD_FIRST_NAME,
            FIELD_LAST_NAME
    };


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id", nullable = false, insertable = true, updatable = true)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    @Basic @Size(min = 1, max = 64)
    @Column(name = "person_name", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @XmlAttribute(name = "name", required = true)
    private String userName;

    @Basic @Size(min = 5, max = 64)
    @Column(name = "person_password", nullable = false, insertable = true, updatable = true, length = 512, precision = 0)
    @XmlAttribute(name = "name", required = true)
    private String password;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "first_name", nullable = false, insertable = true, updatable = true, length = 64, precision = 0)),
            @AttributeOverride(name = "surname", column = @Column(name = "last_name", nullable = false, insertable = true, updatable = true, length = 64, precision = 0))
    })
    private PersonName name;

    @Column(name = "id_number")
    @XmlAttribute(name = "id_number", required = true)
    private String idNumber;

    @Past(message = "The birth date must be in the past")
    @XmlJavaTypeAdapter(value=DateTypeAdapter.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "dob", nullable = false, insertable = true, updatable = true)
    @XmlAttribute(name = "dob", required = true)
    private Date birthDate;

    @Basic @Email
    @Column(name = "email")
    @XmlAttribute(name = "email", required = true)
    private String email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "line01", column = @Column(name = "physical_address_01")),
            @AttributeOverride(name = "line02", column = @Column(name = "physical_address_02")),
            @AttributeOverride(name = "line03", column = @Column(name = "physical_address_03")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "physical_address_code"))
    })
    private Address physicalAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "line01", column = @Column(name = "postal_address_01")),
            @AttributeOverride(name = "line02", column = @Column(name = "postal_address_02")),
            @AttributeOverride(name = "line03", column = @Column(name = "postal_address_03")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "postal_address_code"))
    })
    private Address postalAddress;

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

    public Profile() {
        this(null, null, null, null, new PersonName(), new Address(), new Address());
    }

    public Profile(String userName, String password, String idNumber, String email, String firstName, String lastName, Title title, String suffix,
                   String physicalAddress01, String physicalAddress02, String physicalAddress03, String physicalPostalCode,
                   String postalAddress01, String postalAddress02, String postalAddress03, String postalPostalCode) {
        this(
                userName,
                password,
                idNumber,
                email,
                new PersonName(firstName, lastName, title, suffix),
                new Address(physicalAddress01, physicalAddress02, physicalAddress03, physicalPostalCode),
                new Address(postalAddress01, postalAddress02, postalAddress03, postalPostalCode));
    }
    
    public Profile(String userName, String password, String idNumber, String email, PersonName name, Address physicalAddress, Address postalAddress) {
        this.userName = userName;
        this.password = password;
        this.idNumber = idNumber;
        this.email = email;
        this.name = name;
        this.physicalAddress = physicalAddress;
        this.postalAddress = postalAddress;
    }

    
    @Override
    public String getId() {
        return this.userName;
    }

    public Long getUserId() {
        return id;
    }

    /**
     * @return the pointer to the IdentityType. For User this will return same value as getId(). For Group key contains
     *         encoded group type and name imformation. In default implementation it can look as follows:
     *         "jbpid_group_id_._._GROUP_TYPE_._._GROUP_NAME". Still prefix and format of key can change in the future so
     *         PersistenceManager.createGroupId(String groupName, String groupType) method should be used to create it for Group.
     */
    @Override
    public String getKey() {
        return this.userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = userName;
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

    public PersonName getName() {
        return name;
    }

    public void setName(PersonName name) {
        this.name = name;
    }

    public String getFirstName() {
        return name != null ? name.getName() : null;
    }

    public void setFirstName(String firstName) {
        if (name == null) {
            name = new PersonName(firstName, null, null, null);
        } else {
            this.name.setName(firstName);;
        }
    }

    public String getLastName() {
        return name != null ? name.getSurname() : null;
    }

    public void setLastName(String lastName) {
        if (name == null) {
            name = new PersonName(null, lastName, null, null);
        } else {
            this.name.setSurname(lastName);;
        }
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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Address getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(Address physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public Address getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(Address postalAddress) {
        this.postalAddress = postalAddress;
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
    public String[] getTrackedProperties() {
        return TRACKED;
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
                ", name='" + userName + '\'' +
                ", firstName='" + name.getName() + '\'' +
                ", lastName='" + name.getSurname() + '\'' +
                ", password='*******\'" +
                ", birthDate=" + birthDate +
                ", verified=" + verified +
                ", verifiedDate=" + verifiedDate +
                ", email='" + email + '\'' +
                '}';
    }
}
