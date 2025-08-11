package com.propertyservice.dto;


public class RoomsDto {

    private long id;
    private String roomType;
    private double basePrice;
    private long propertyId;   // We'll expose property ID, not full object

    public RoomsDto() {}

    public RoomsDto(long id, String roomType, double basePrice, long propertyId) {
        this.id = id;
        this.roomType = roomType;
        this.basePrice = basePrice;
        this.propertyId = propertyId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }
}
