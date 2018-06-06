package dbclasses;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class PlatformUser extends GeneralEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(nullable = false, unique=true)
    private String email;
    
    @Column(nullable = false)
    private LocalDate age;
    
    @OneToMany (targetEntity = Property.class, mappedBy="owner")
    @JoinColumn(nullable=false)
    private Set<Property> ownedProperties;
    
    @Column(nullable = false)
    private boolean isDelegate;
    
    @OneToMany (targetEntity = Room.class, mappedBy="renter")
    @JoinColumn(nullable=false)
    private Set<Room> rentedRooms;
    
    @ManyToOne
    @JoinColumn()
    private University university;

    public PlatformUser() {
        super();
    }

    public PlatformUser(String password, String email, String name, LocalDate age, boolean isDelegate) {
        super();
        this.password = password;
        this.name = name;
        this.email = email;
        this.age = age;
        this.isDelegate = isDelegate;
        this.ownedProperties = new TreeSet<>();
        
    }

    public Set<Room> getRentedRooms() {
        return rentedRooms;
    }

    public void setRentedRooms(Set<Room> rentedRooms) {
        this.rentedRooms = rentedRooms;
    }
    
    public boolean addRentedRoom(Room room){
        return rentedRooms.add(room);
    }
    
    public boolean removeRentedRoom(Room room){
        return rentedRooms.remove(room);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    public String toString() {
        return "PlatformUser{" + "id=" + id + ", email=" + email + ", name=" + name + ", age=" + age + ", ownedProperties=" + ownedProperties + ", userRating=" + userRating + ", isDelegate=" + isDelegate + ", universityId=" + university + '}';
    }

    
}
