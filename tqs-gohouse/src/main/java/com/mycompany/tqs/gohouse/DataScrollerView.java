/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

/**
 *
 * @author demo
 */
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
 
@ManagedBean(eager = true)
@ViewScoped
public class DataScrollerView implements Serializable {

    private List<Car> cars;
         
     
    @PostConstruct
    public void init() {
        ArrayList<Car> temp = new ArrayList<>();
        temp.add(new Car("1", "Benus", "96", "Green"));
        temp.add(new Car("3", "Bang", "98", "Red"));
        temp.add(new Car("12", "Bane", "2000", "Blue"));
        temp.add(new Car("5", "Dang", "98", "Red"));
        temp.add(new Car("1", "Benus", "96", "Green"));
        temp.add(new Car("3", "Bang", "98", "Red"));
        temp.add(new Car("12", "Bane", "2000", "Blue"));
        temp.add(new Car("5", "Dang", "98", "Red"));
        temp.add(new Car("1", "Benus", "96", "Green"));
        temp.add(new Car("3", "Bang", "98", "Red"));
        temp.add(new Car("12", "Bane", "2000", "Blue"));
        temp.add(new Car("5", "Dang", "98", "Red"));
        temp.add(new Car("1", "Benus", "96", "Green"));
        temp.add(new Car("3", "Bang", "98", "Red"));
        temp.add(new Car("12", "Bane", "2000", "Blue"));
        temp.add(new Car("5", "Dang", "98", "Red"));
        cars = temp;
    }
 
    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}
