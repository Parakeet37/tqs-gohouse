package dbClasses;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class PlatformUser extends GeneralEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(nullable = false)
    protected String name;
    
    @Column(nullable = false, unique=true)
    private String email;
    
    @Column(nullable = false)
    private LocalDate age;
    
    @OneToMany (targetEntity = Room.class, mappedBy="renter")
    @JoinColumn(nullable=false)
    private Set<Room> rentedRooms;
    
    @OneToMany (targetEntity = Property.class, mappedBy="owner")
    @JoinColumn(nullable=false)
    private Set<Property> ownedProperties;
    
    @Column(nullable = false)
    private boolean isDelegate;
    
    @ManyToOne
    @JoinColumn(nullable = true)
    private University university;

    public PlatformUser() {
        super();
    }

    public PlatformUser(String email, String name, LocalDate age, boolean isDelegate) {
        super();
        this.name = name;
        this.email = email;
        this.age = age;
        this.isDelegate = isDelegate;
        this.ownedProperties = new TreeSet<>();
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Set<Room> getRentedRooms() {
        return rentedRooms;
    }

    public void setRentedRooms(Set<Room> rentedRooms) {
        this.rentedRooms = rentedRooms;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public LocalDate getAge() {
        return age;
    }

    public void setAge(LocalDate age) {
        this.age = age;
    }
    
    public Set<Property> getOwnedProperties() {
        return ownedProperties;
    }

    public void setOwnedProperties(Set<Property> ownedProperties) {
        this.ownedProperties = ownedProperties;
    }

    public boolean addOwnedProperty(Property property) {
        return ownedProperties.add(property);
    }
    
    public boolean removeOwnedProperty(Property property) {
        return ownedProperties.remove(property);
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.email, other.email);
    }
    
    

    @Override
    public String toString() {
        return "PlatformUser{" + "id=" + id + ", email=" + email + ", name=" + name + ", age=" + age + ", rentedProperties=" + rentedProperties + ", ownedProperties=" + ownedProperties + ", userRating=" + userRating + ", isDelegate=" + isDelegate + ", universityId=" + university + '}';
    }

    
}
