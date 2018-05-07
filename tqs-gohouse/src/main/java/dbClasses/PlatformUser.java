package dbClasses;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class PlatformUser implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String email;
    private String name;
    private LocalDate age;
    
    @OneToMany (targetEntity = Property.class)
    private List rentedProperties;
    
    @OneToMany (targetEntity = Property.class)
    private List ownedProperties;
    
    private boolean isCollegeStudent;
    private double userRating;
    private int nVotes;
    private double weightedRating;
    private boolean isModerator;
    private boolean isDelegate;
    
    @ManyToOne
    private University university;

    public PlatformUser() {
    }

    public PlatformUser(String email, String name, LocalDate age, boolean isCollegeStudent, boolean isDelegate) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.isCollegeStudent = isCollegeStudent;
        this.isModerator = false;
        this.isDelegate = isDelegate;
        this.university = null;
        this.rentedProperties = new ArrayList<>();
        this.ownedProperties = new ArrayList<>();
        this.userRating = 0;
        this.nVotes = 0;
        this.weightedRating = 0;
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
    
    public void setNVotes(int nVotes) {
        this.nVotes = nVotes;
    }

    public int getNVotes() {
        return nVotes;
    }
    
    public double getWeightedRating(){
        return weightedRating;
    }
    
    public void setWeightedRanking(double weightedRating){
        this.weightedRating = weightedRating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getAge() {
        return age;
    }

    public void setAge(LocalDate age) {
        this.age = age;
    }

    public List getRentedProperties() {
        return rentedProperties;
    }

    public void setRentedProperties(ArrayList rentedProperties) {
        this.rentedProperties = rentedProperties;
    }

    public void addRentedProperty(Property property) {
        rentedProperties.add(property);
    }
    
    public void removeRentedProperty(Property property) {
        rentedProperties.remove(property);
    }
    
    public List getOwnedProperties() {
        return ownedProperties;
    }

    public void setOwnedProperties(List ownedProperties) {
        this.ownedProperties = ownedProperties;
    }

    public void addOwnedProperty(Property property) {
        ownedProperties.add(property);
    }
    
    public void removeOwnedProperty(Property property) {
        ownedProperties.remove(property);
    }
    
    public boolean isIsCollegeStudent() {
        return isCollegeStudent;
    }

    public void setIsCollegeStudent(boolean isCollegeStudent) {
        this.isCollegeStudent = isCollegeStudent;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
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

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlatformUser other = (PlatformUser) obj;
        if (this.isCollegeStudent != other.isCollegeStudent) {
            return false;
        }
        if (this.userRating != other.userRating) {
            return false;
        }
        if (this.isModerator != other.isModerator) {
            return false;
        }
        if (this.isDelegate != other.isDelegate) {
            return false;
        }
        if (this.university != other.university) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.age, other.age)) {
            return false;
        }
        if (!Objects.equals(this.rentedProperties, other.rentedProperties)) {
            return false;
        }
        return Objects.equals(this.ownedProperties, other.ownedProperties);
    }

    @Override
    public String toString() {
        return "PlatformUser{" + "id=" + id + ", email=" + email + ", name=" + name + ", age=" + age + ", rentedProperties=" + rentedProperties + ", ownedProperties=" + ownedProperties + ", isCollegeStudent=" + isCollegeStudent + ", userRating=" + userRating + ", isModerator=" + isModerator + ", isDelegate=" + isDelegate + ", universityId=" + university + '}';
    }

    
}
