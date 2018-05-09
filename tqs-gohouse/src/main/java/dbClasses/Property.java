package dbClasses;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToMany(targetEntity=Room.class, mappedBy="property")
    @JoinColumn(nullable = false)
    private Set<Room> rooms;
    
    @ManyToOne
    @JoinColumn
    private GeneralEntity renter;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private PlatformUser owner;
    
    @Column(nullable = false)
    private int rent;
    
    @Column(nullable = false)
    private boolean occupied;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    private String type;
    
    @Column(nullable = false)
    private char block;
    
    @Column(nullable = false)
    private int floor;

    public Property() {
    }

    public Property(PlatformUser owner, int rent, String address, String type, char block, int floor, Set<Room> rooms) {
        this.owner = owner;
        this.rent = rent;
        this.address = address;
        this.type = type;
        this.block = block;
        this.floor = floor;
        this.occupied = false;
        this.rooms = rooms;
    }

    public GeneralEntity getRenterUser() {
        return renter;
    }

    public void setRenterUser(GeneralEntity renter) {
        this.renter = renter;
    }

    public PlatformUser getOwner() {
        return owner;
    }

    public void setOwner(PlatformUser owner) {
        this.owner = owner;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public char getBlock() {
        return block;
    }

    public void setBlock(char block) {
        this.block = block;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }
    
    public void addRoom(Room room){
        rooms.add(room);
    }
    
    public void removeRoom(Room room){
        rooms.remove(room);
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
        if (!(object instanceof Property)) {
            return false;
        }
        Property other = (Property) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "dbClasses.Property[ id=" + id + " ]";
    }
    
}
