package dbclasses;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Property implements Serializable, Comparable<Property> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    
    @OneToMany(targetEntity=Room.class, mappedBy="property")
    @JoinColumn(nullable = false)
    private Set<Room> rooms;
    
    @Column(nullable = false)
    private float longitude;
    
    @Column(nullable = false)
    private float latitude;;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private PlatformUser owner;
    
    @Column(nullable = false)
    private boolean occupied;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PropertyType type;
    
    @Column(nullable = false)
    private char block;
    
    @Column(nullable = false)
    private int floor;

    @Column(nullable = false)
    private boolean verified;
    
    @ManyToOne
    @JoinColumn()
    private University verifiedBy;
    
    @Column(nullable = false)
    private double moderatorRating;
    
    @Column(nullable = false)
    private double userRating;
    
    @Column(nullable = false)
    private int nVotes;
    
    @Column(nullable = false)
    private double weightedRating;
    
    @Column(nullable = false)
    private Set<String> photos;
    
    public Property() {
    }

    public Property(PlatformUser owner, float longitude, float latitude, String address, PropertyType type, char block, int floor, Set<Room> rooms) {
        this.owner = owner;
        this.address = address;
        this.type = type;
        this.block = block;
        this.floor = floor;
        this.occupied = false;
        this.rooms = rooms;
        this.verified = false;
        this.longitude = longitude;
        this.latitude = latitude;
        this.moderatorRating = 0;
        this.userRating = 0;
        this.nVotes = 0;
        this.weightedRating = 0;
        this.photos = new TreeSet<>();
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getModeratorRating() {
        return moderatorRating;
    }

    public void setModeratorRating(double moderatorRating) {
        this.moderatorRating = moderatorRating;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public int getnVotes() {
        return nVotes;
    }

    public void setnVotes(int nVotes) {
        this.nVotes = nVotes;
    }

    public double getWeightedRating() {
        return weightedRating;
    }

    public void setWeightedRating(double weightedRating) {
        this.weightedRating = weightedRating;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public PlatformUser getOwner() {
        return owner;
    }

    public void setOwner(PlatformUser owner) {
        this.owner = owner;
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

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
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
    
    public boolean addRoom(Room room){
        return rooms.add(room);
    }
    
    public boolean removeRoom(Room room){
        return rooms.remove(room);
    }

    public University getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(University verifiedBy) {
        this.verifiedBy = verifiedBy;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.address);
        hash = 41 * hash + this.block;
        hash = 41 * hash + this.floor;
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
        final Property other = (Property) obj;
        if (this.block != other.block) {
            return false;
        }
        if (this.floor != other.floor) {
            return false;
        }
        return Objects.equals(this.address, other.address);
    }

    @Override
    public String toString() {
        return "Property{" + "id=" + id + ", address=" + address + ", block=" + block + ", floor=" + floor + '}';
    }

    @Override
    public int compareTo(Property other) {
        if (this.weightedRating > other.weightedRating){
            return 1;
        } else if (this.weightedRating < other.weightedRating){
            return -1;
        } else {
            return (this.moderatorRating >= other.moderatorRating ? 1 :-1);
        }
    }
}
