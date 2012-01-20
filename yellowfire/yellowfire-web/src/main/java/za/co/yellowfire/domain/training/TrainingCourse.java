package za.co.yellowfire.domain.training;

import org.eclipse.persistence.annotations.Partitioned;
import org.eclipse.persistence.annotations.UnionPartitioning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.domain.Archiveable;
import za.co.yellowfire.domain.Contact;
import za.co.yellowfire.domain.DomainEntity;
import za.co.yellowfire.log.LogType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity(name = "TrainingCourse")
@Access(AccessType.FIELD)
@Table(name = "course", schema = "cde")
@Cacheable(false)
@NamedQueries({
        @NamedQuery(
            name="qry.training.courses",
            query="select c from TrainingCourse c"
        )
})
@UnionPartitioning(
        name="UnionPartitioningAllNodes",
        /*Do not replicate changes to all data sources*/
        replicateWrites=false)
@Partitioned("UnionPartitioningAllNodes")
public class TrainingCourse extends DomainEntity implements Archiveable, Comparable<TrainingCourse> {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.DOMAIN.getCategory());

    public static final String QRY_TRAINING_COURSES = "qry.training.courses";

    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute(name = "id", required = false)
    private Long id;

    @Size(min = 1, max = 64)
    @Column(name = "course_title", length = 64)
    private String title;

    @Column(name = "course_duration")
    private int duration;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "course_duration_type_id")
    private DurationType durationType;

    @NotNull(message = "error.training.provider.notnull")
    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = TrainingProvider.class)
    @JoinColumn(name = "training_provider_id", nullable = false, updatable = true, referencedColumnName = "training_provider_id")
    private TrainingProvider provider;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = SkillArea.class)
    @JoinColumn(name = "skill_area_id", nullable = false, updatable = true, referencedColumnName = "skill_area_id")
    private SkillArea skillArea;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = ContentType.class)
    @JoinColumn(name = "content_type_id", nullable = false, updatable = true, referencedColumnName = "content_type_id")
    private ContentType contentType;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = Category.class)
    @JoinColumn(name = "course_category_id", nullable = false, updatable = true, referencedColumnName = "course_category_id")
    private Category category;

    @Size(min = 0, max = 64)
    @Column(name = "course_accrediting_body", length = 64)
    private String accreditingBody;

    @Size(min = 0, max = 256)
    @Column(name = "course_overview", length = 256)
    private String overview;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "course_contact_person")),
        @AttributeOverride(name = "telephoneNo", column = @Column(name = "course_contact_no"))
    })
    private Contact contact;

    @Column(name = "course_internal")
    private boolean internal;

    @Column(name = "course_core")
    private boolean core;

    @Version
    @Column(name = "version")
    @SuppressWarnings("unused")
    private int version;

    @Column(name = "archived")
    @SuppressWarnings("unused")
    private boolean archived;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public DurationType getDurationType() {
        return durationType;
    }

    public void setDurationType(DurationType durationType) {
        this.durationType = durationType;
    }

    @SuppressWarnings("unused")
    public String getDurationText() {
        return getDuration() + " " + getDurationType();
    }

    @SuppressWarnings("unused")
    public void setDurationText(String text) {
        if (text == null || text.trim().equals("")) {
            duration = 0;
            durationType = DurationType.Days;
            return;
        }

        String[] parts = text.split(" ");
        if (parts.length != 2) throw new IllegalArgumentException("The duration text `" + text + "` is invalid");
        setDuration(Integer.parseInt(parts[0]));
        setDurationType(DurationType.valueOf(parts[1]));
    }

    @SuppressWarnings("unused")
    public TrainingProvider getProvider() {
        return provider;
    }

    public void setProvider(TrainingProvider provider) {
        this.provider = provider;
    }

    @SuppressWarnings("unused")
    public SkillArea getSkillArea() {
        return skillArea;
    }

    @SuppressWarnings("unused")
    public void setSkillArea(SkillArea skillArea) {
        this.skillArea = skillArea;
    }

    @SuppressWarnings("unused")
    public ContentType getContentType() {
        return contentType;
    }

    @SuppressWarnings("unused")
    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    @SuppressWarnings("unused")
    public Category getCategory() {
        return category;
    }

    @SuppressWarnings("unused")
    public void setCategory(Category category) {
        this.category = category;
    }

    @SuppressWarnings("unused")
    public String getAccreditingBody() {
        return accreditingBody;
    }

    @SuppressWarnings("unused")
    public void setAccreditingBody(String accreditingBody) {
        this.accreditingBody = accreditingBody;
    }

    @SuppressWarnings("unused")
    public String getOverview() {
        return overview;
    }

    @SuppressWarnings("unused")
    public void setOverview(String overview) {
        this.overview = overview;
    }

    @SuppressWarnings("unused")
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @SuppressWarnings("unused")
    public boolean isInternal() {
        return internal;
    }

    @SuppressWarnings("unused")
    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    @SuppressWarnings("unused")
    public boolean isCore() {
        return core;
    }

    @SuppressWarnings("unused")
    public void setCore(boolean core) {
        this.core = core;
    }

    public boolean isArchived() {
        return archived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrainingCourse that = (TrainingCourse) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public int compareTo(TrainingCourse o) {
        return (int) (this.id - o.getId());
    }

    @Override
    public String toString() {
        return "TrainingCourse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                ", durationType=" + durationType +
                ", provider=" + provider +
                ", accreditingBody='" + accreditingBody + '\'' +
                ", overview='" + overview + '\'' +
                ", contact=" + contact +
                ", internal=" + internal +
                ", core=" + core +
                ", version=" + version +
                '}';
    }
}
