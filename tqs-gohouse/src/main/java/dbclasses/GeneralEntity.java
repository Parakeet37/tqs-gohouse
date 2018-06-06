package dbclasses;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class GeneralEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    
    @Column(nullable = false)
    protected String password;
    
    @Column(nullable = false)
    protected String name;
    
    @Column(nullable = false)
    protected double userRating;
    
    @Column(nullable = false)
    protected int nVotes;
    
    @Column(nullable = false)
    protected double weightedRating;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    
    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }
    
    public boolean changePassword(String oldpassword, String password){
        if (verifyPassword(oldpassword)){
            this.password = password;
            return true;
        } else return false;
    }
    
    public boolean verifyPassword(String password){
        return password.equals(this.password);
    }
    
    public GeneralEntity() {
        this.userRating = 0;
        this.nVotes = 0;
        this.weightedRating = 0;
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
        return "GeneralEntity{" + "id=" + id + ", rentedProperties=" + '}';

    }
}
