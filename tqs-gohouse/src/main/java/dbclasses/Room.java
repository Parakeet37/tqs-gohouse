package dbclasses;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Room implements Serializable, Comparable<Room>{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private int rent;

    private String description;
    
    @ManyToOne
    @JoinColumn(nullable=false)
    private Property property;
    
    @ManyToOne
    @JoinColumn
    private University university;
    
    @ManyToOne
    @JoinColumn
    private PlatformUser renter;

    private boolean occupied;

    @Column(nullable = false)
    private Set<String> photos;
    
    public Room() {
    }

    public Room(String description, int rent, Property property) {
        this.rent = rent;
        this.description = description;
        this.occupied = false;
        this.property = property;
        this.photos = new TreeSet<>();
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
    
    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public PlatformUser getRenter() {
        return renter;
    }

    public void setRenter(PlatformUser renter) {
        this.renter = renter;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final Room other = (Room) obj;
        return Objects.equals(this.id, other.id);
    }

    

    @Override
    public String toString() {
        return "dbClasses.Room[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Room other) {
        if (this.rent > other.rent){
            return 1;
        } else if (this.rent < other.rent){
            return -1;
        } else {
            return 0;
        }
    }
    
}
