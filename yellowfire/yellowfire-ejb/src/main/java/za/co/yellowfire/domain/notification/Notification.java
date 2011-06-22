package za.co.yellowfire.domain.notification;

import za.co.yellowfire.domain.Archiveable;
import za.co.yellowfire.domain.DomainEntity;
import za.co.yellowfire.domain.listener.DomainEntityListener;
import za.co.yellowfire.domain.listener.SearchIndexListener;
import za.co.yellowfire.domain.search.Searchable;
import za.co.yellowfire.domain.search.SearchableProperty;
import za.co.yellowfire.domain.search.SearchablePropertyId;
import za.co.yellowfire.domain.search.SearchablePropertyType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.Date;
import java.util.Map;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity(name = "Notification")
@EntityListeners({DomainEntityListener.class, SearchIndexListener.class})
@Access(AccessType.FIELD)
@Table(name = "notification", schema = "cde")
@Cacheable(true)
@Searchable(name = "Notification")
@NamedQueries({
        @NamedQuery(
            name="qry.notifications",
            query="select n from Notification n"
        )
})
//@UnionPartitioning(
//        name="UnionPartitioningAllNodes",
//        /*Do not replicate changes to all data sources*/
//        replicateWrites=false)
public class Notification extends DomainEntity implements Archiveable, Comparable<Notification> {
    private static final long serialVersionUID = 1L;
    public static final String QRY_NOTIFICATIONS = "qry.notifications";

    @Id
    @Column(name = "notification_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute(name = "id", required = false)
    @SearchablePropertyId
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType type;

    @Size(min = 1, max = 128)
    @Column(name = "notification_from", length = 128)
    @SearchableProperty(name = "from", dynamic = true, type = SearchablePropertyType.STRING)
    private String from;

    @Size(min = 1, max = 256)
    @Column(name = "notification_to", length = 256)
    @SearchableProperty(name = "to", boost = 2)
    private String to;

    @Size(min = 0, max = 256)
    @Column(name = "notification_cc", length = 256)
    @SearchableProperty(name = "cc", dynamic = true, type = SearchablePropertyType.STRING)
    private String cc;

    @Size(min = 0, max = 256)
    @Column(name = "notification_bcc", length = 256)
    @SearchableProperty(name = "bcc", dynamic = true, type = SearchablePropertyType.STRING)
    private String bcc;

    @Size(min = 0, max = 256)
    @Column(name = "notification_subject", length = 256)
    @SearchableProperty(name = "subject")
    private String subject;

    @Size(min = 1)
    @Column(name = "notification_body")
    @SearchableProperty(name = "body")
    private String body;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "notification_sent")
    @SearchableProperty(name = "notification_sent", dynamic = true, type = SearchablePropertyType.DATE)
    private Date sent;

    @Version
    @Column(name = "version")
    @SuppressWarnings("unused")
    private int version;

    @Column(name = "archived")
    @SuppressWarnings("unused")
    private boolean archived;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public String[] getToRecipients() {
        if (to == null || to.trim().equals("")) return new String[0];
        return to.split(";");
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public String[] getCcRecipients() {
        if (cc == null || cc.trim().equals("")) return new String[0];
        return cc.split(";");
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public String[] getBccRecipients() {
        if (bcc == null || bcc.trim().equals("")) return new String[0];
        return bcc.split(";");
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    @Override
    public boolean isArchived() {
        return archived;
    }
    
    @Override
    public int compareTo(Notification o) {
        return (int) (this.id - o.getId());
    }
}
