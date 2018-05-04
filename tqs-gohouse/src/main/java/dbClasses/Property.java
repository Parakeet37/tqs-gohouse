package dbClasses;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private ArrayList<Long> rooms;
    private int rent;
    private long owner;
    private long renter;
    private boolean occupied;
    private String address;
    private String type;
    private char block;
    private int floor;

    public Property() {
    }

    public Property(int rent, long owner, String address, String type, char block, int floor) {
        this.rent = rent;
        this.owner = owner;
        this.address = address;
        this.type = type;
        this.block = block;
        this.floor = floor;
        this.occupied = false;
        this.rooms = new ArrayList<>();
        this.renter = 0;
    }

    public ArrayList<Long> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Long> rooms) {
        this.rooms = rooms;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public long getRenter() {
        return renter;
    }

    public void setRenter(long renter) {
        this.renter = renter;
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
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbClasses.Property[ id=" + id + " ]";
    }
    
}
