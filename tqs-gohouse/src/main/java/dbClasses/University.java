/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbClasses;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class University extends GeneralEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(nullable = false, unique=true)
    protected String name;

    @Column(nullable = false)
    private String address;

    @OneToMany (targetEntity = PlatformUser.class, mappedBy="university")
    @JoinColumn(nullable = false)
    private Set<PlatformUser> students;
    
    @OneToMany (targetEntity = PlatformUser.class, mappedBy="university")
    @JoinColumn(nullable = false)
    private Set<PlatformUser> delegates;
    
    public University() {
        super();
    }

    public University(String name, String address) {
        super();
        this.name = name;
        this.address = address;
        students = new TreeSet<>();
        delegates = new TreeSet<>();
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
    
    public Set<PlatformUser> getStudents() {
        return students;
    }

    public void setStudents(Set<PlatformUser> students) {
        this.students = students;
    }
 
    public boolean addStudent(PlatformUser student) {
        return students.add(student);
    }
    
    public boolean removeStudent(PlatformUser student) {
        return students.remove(student);
    }
    
    public Set<PlatformUser> getDelegates() {
        return delegates;
    }

    public void setDelegates(Set<PlatformUser> delegates) {
        this.delegates = delegates;
    }
 
    public boolean addDelegate(PlatformUser delegate) {
        return delegates.add(delegate);
    }
    
    public boolean removeDelegate(PlatformUser delegate) {
        return delegates.remove(delegate);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
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
        return Objects.equals(this.id, other.id);
    }
    
    
    
}
