package com.gc.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
 
@Entity
@Table(name = "address")
public class Address {
 
    long id;
    String buildingName;
    String town;
    String postCode;
    Set<Person> persons;
 
    public Address() {
 
    }
 
    public Address(String buildingName, String town, String postCode) {
        this.buildingName = buildingName;
        this.town = town;
        this.postCode = postCode;
    }
 
    @Id
    @GeneratedValue
    @Column(name = "address_id")
    public long getId() {
        return id;
    }
 
    public void setId(long id) {
        this.id = id;
    }
 
    @Column
    public String getBuildingName() {
        return buildingName;
    }
 
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
 
    @Column
    public String getTown() {
        return town;
    }
 
    public void setTown(String town) {
        this.town = town;
    }
 
    @Column
    public String getPostCode() {
        return postCode;
    }
 
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
 
    @ManyToMany(mappedBy = "addresses")
    public Set<Person> getPersons() {
        return persons;
    }
 
    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }
 
}
