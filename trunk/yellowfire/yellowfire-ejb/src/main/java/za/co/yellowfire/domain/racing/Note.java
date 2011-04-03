package za.co.yellowfire.domain.racing;

import za.co.yellowfire.domain.jaxb.NoteTypeAdapter;

import java.io.Serializable;

import javax.persistence.*;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * @author Mark Ashworth
 * @version 0.0.1
 */
@Entity(name = "Note")
@Access(AccessType.FIELD)
@Table(name = "note", schema = "rce")
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "note", propOrder = {"id", "type", "text"})
public class Note implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @XmlAttribute(name = "id", required = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "note_id", table = "note", nullable = false, insertable = true, updatable = true, length = 19, precision = 0)
    private Long id;

    @XmlJavaTypeAdapter(value=NoteTypeAdapter.class)
    @XmlAttribute(name = "type", required = true)
    @Column(name = "note_type", table = "note", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private NoteType type;

    @XmlTransient
    @ManyToOne(targetEntity = Race.class)
    @JoinColumn(name = "race_id", referencedColumnName = "race_id")
    private Race race;

    @XmlValue
    @Column(name = "note_text", table = "note", nullable = true, insertable = true, updatable = true)
    private String text;

    /*Default constructor*/
    public Note() {}

    public Note(NoteType type, String text) {
        this.type = type;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    
    public NoteType getType() {
        return type;
    }

    public void setType(NoteType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Note{" + "id=" + id + ", type=" + type + ", text=" + text + '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Note other = (Note) obj;
        if (this.type != other.type) {
            return false;
        }
        if ((this.text == null) ? (other.text != null) : !this.text.equals(other.text)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 97 * hash + (this.text != null ? this.text.hashCode() : 0);
        return hash;
    }
}
