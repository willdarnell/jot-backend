package jotbackend.classes;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.*;
import javax.persistence.*;


@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "fullName")
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contactId;

    private Integer userId;

    private String googleId;

    private String firstName;

    private String lastName;

    private String fullName;

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

    public void removeAttribute(Attribute attribute){
        for (Attribute a : attributes) {
            if(a.getAttributeId().equals(attribute.getAttributeId())){
                attributes.remove(a);
                return;
            }
        }
    }

    //@JsonBackReference
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "contact"
    )
    private List<Activity> activities;

    public Contact() {

    }

    public Contact(Integer userId, String googleId, String firstName,
                   String lastName, String emailAddress, String phoneNumber,
                   String organization, String role) {
        this.userId = userId;
        this.googleId = googleId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.organization = organization;
        this.role = role;
        this.activities = new ArrayList<>();
    }

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

    public String getFullName() { return firstName.concat(" ".concat(lastName)); }

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
