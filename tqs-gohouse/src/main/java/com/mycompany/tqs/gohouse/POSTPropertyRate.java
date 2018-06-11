/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

/**
 * Object received via JSON POST.
 * @author demo
 */
public class POSTPropertyRate {
    private long delegate;
    private long id;
    private double rate;

    public long getDelegate() {
        return delegate;
    }

    public void setDelegate(long delegate) {
        this.delegate = delegate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
    
    
}
