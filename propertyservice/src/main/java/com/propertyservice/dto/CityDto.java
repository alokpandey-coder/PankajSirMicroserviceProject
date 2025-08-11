package com.propertyservice.dto;

public class CityDto {

    private long id;
    private String name;

    public CityDto() {
    }

    public CityDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
