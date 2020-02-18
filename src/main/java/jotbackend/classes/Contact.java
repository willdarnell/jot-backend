package jotbackend.classes;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.*;
import javax.persistence.*;
import jotbackend.classes.Activity;

@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contactId;

    private Integer userId;

    private String googleId;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String phoneNumber;

    private String organization;

    private String role;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
    private Date createDate = new Date();

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "contacts_attributes",
        joinColumns = {@JoinColumn(name = "contact_id")},
        inverseJoinColumns = {@JoinColumn(name = "attribute_id")})
    private Set<Attribute> attributes = new HashSet<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "contact_id")
    private List<Activity> activities = new ArrayList<>();

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateTime() {
        return createDate;
    }

    public void setCreateTime(Date createTime) {
        this.createDate = createTime;
    }

    public Set<Attribute> getAttributes() { return attributes; };

    public List<Activity> getActivities() { return activities; };
}
