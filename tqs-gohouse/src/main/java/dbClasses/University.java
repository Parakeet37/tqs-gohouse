/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class University implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String address;
    
    @OneToMany (targetEntity = Property.class)
    private List rentedProperties;

    public University() {
    }

    public University(String name, String address) {
        this.name = name;
        this.address = address;
        rentedProperties = new ArrayList();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List getRentedProperties() {
        return rentedProperties;
    }

    public void setRentedProperties(List rentedProperties) {
        this.rentedProperties = rentedProperties;
    }
 
    public void addRentedProperty(Property property) {
        rentedProperties.add(property);
    }
    
    public void removeRentedProperty(Property property) {
        rentedProperties.remove(property);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final University other = (University) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.rentedProperties, other.rentedProperties);
    }

    @Override
    public String toString() {
        return "University{" + "id=" + id + ", name=" + name + ", address=" + address + ", rentedProperties=" + rentedProperties + '}';
    }
    
    
    
}
