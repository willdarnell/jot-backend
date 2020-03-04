package jotbackend.classes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "attributes")
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attributeId;

    private Integer userId;

    private String title;

    private String description;

    // created date/time
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
    private Date createDate = new Date();

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY,
        cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
        }, mappedBy = "attributes")
    private Set<Contact> contacts = new HashSet<>();

    @PreRemove
    private void removeAttributeFromContact() {
        for (Contact c : contacts) {
            c.removeAttribute(this);
        }
    }


    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer id) {
        this.attributeId = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Set<Contact> getContacts() { return contacts; };
}
