package dbClasses;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class GeneralEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(nullable = false)
    protected String name;
    
    @Column(nullable = false)
    protected double userRating;
    
    @Column(nullable = false)
    protected int nVotes;
    
    @Column(nullable = false)
    protected double weightedRating;
    
    @OneToMany (targetEntity = Property.class, mappedBy="renter")
    @JoinColumn(nullable = false)
    protected Set<Property> rentedProperties;

    public GeneralEntity() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GeneralEntity(String name) {
        this.name = name;
        this.rentedProperties = new TreeSet<>();
        this.userRating = 0;
        this.nVotes = 0;
        this.weightedRating = 0;
    }
    
    public Set<Property> getRentedProperties() {
        return rentedProperties;
    }

    public void setRentedProperties(Set<Property> rentedProperties) {
        this.rentedProperties = rentedProperties;
    }
 
    public boolean addRentedProperty(Property property) {
        return rentedProperties.add(property);
    }
    
    public boolean removeRentedProperty(Property property) {
        return rentedProperties.remove(property);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
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
        final GeneralEntity other = (GeneralEntity) obj;
        return Objects.equals(this.id, other.id);
    }
    
    

    @Override
    public String toString() {
        return "GeneralEntity{" + "id=" + id + ", name=" + name + ", rentedProperties=" + rentedProperties + '}';
    }
}
