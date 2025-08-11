package com.propertyservice.entity;



import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="rooms")
public class Rooms {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String roomType;

    @Column(name = "base_price")
    private double basePrice;

    @ManyToOne
    @JoinColumn(name="property_id")
    @JsonBackReference
    private Property property;
    
    

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getRoomType() {
		return roomType;
	}

	public double getBasePrice() {
		return basePrice;
	}

	

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	
    

}
