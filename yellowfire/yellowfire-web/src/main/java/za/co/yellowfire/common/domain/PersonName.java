package za.co.yellowfire.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Embeddable
public class PersonName implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Size(min = 1, max = 64)
    @Column(name = "first_name")
    private String name;

    @Size(min = 1, max = 64)
    @Column(name = "last_name")
    private String surname;

    @NotNull
    @Column(name = "title")
    @Enumerated(EnumType.STRING)
    private Title title;

    @Column(name = "suffix")
    private String suffix;

    public PersonName() { }

    public PersonName(String name, String surname, Title title, String suffix) {
        this.name = name;
        this.surname = surname;
        this.title = title;
        this.suffix = suffix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return "Name {" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", title=" + title +
                ", suffix='" + suffix + '\'' +
                '}';
    }
}
