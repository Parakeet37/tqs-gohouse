package dbClasses;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PlatformUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String name;
    private int age;
    private ArrayList<Long> rentedProperties;
    private ArrayList<Long> ownedProperties;
    private boolean isCollegeStudent;
    private int userRating;
    private boolean isModerator;
    private boolean isDelegate;
    private int universityId;

    public PlatformUser() {
    }

    public PlatformUser(String email, String name, int age, boolean isCollegeStudent, boolean isModerator, boolean isDelegate, int universityId) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.isCollegeStudent = isCollegeStudent;
        this.isModerator = isModerator;
        this.isDelegate = isDelegate;
        this.universityId = universityId;
        this.rentedProperties = new ArrayList<>();
        this.ownedProperties = new ArrayList<>();
        this.userRating = -1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<Long> getRentedProperties() {
        return rentedProperties;
    }

    public void setRentedProperties(ArrayList<Long> rentedProperties) {
        this.rentedProperties = rentedProperties;
    }

    public ArrayList<Long> getOwnedProperties() {
        return ownedProperties;
    }

    public void setOwnedProperties(ArrayList<Long> ownedProperties) {
        this.ownedProperties = ownedProperties;
    }

    public boolean isIsCollegeStudent() {
        return isCollegeStudent;
    }

    public void setIsCollegeStudent(boolean isCollegeStudent) {
        this.isCollegeStudent = isCollegeStudent;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public boolean isIsModerator() {
        return isModerator;
    }

    public void setIsModerator(boolean isModerator) {
        this.isModerator = isModerator;
    }

    public boolean isIsDelegate() {
        return isDelegate;
    }

    public void setIsDelegate(boolean isDelegate) {
        this.isDelegate = isDelegate;
    }

    public int getUniversityId() {
        return universityId;
    }

    public void setUniversityId(int universityId) {
        this.universityId = universityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlatformUser)) {
            return false;
        }
        PlatformUser other = (PlatformUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbClasses.User[ id=" + id + " ]";
    }
    
}
