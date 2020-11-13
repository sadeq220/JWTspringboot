package com.sadeqstore.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "ps")
public class Product {
    @Id
    private String name;
    private Double cost;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
