package com.tianya.bean;

/**
 * Created by lsy on 2018/2/26.
 */

public class LocationDataBean {
    private double latitude;
    private double longitude;
    private float radius;
    private String addr;
    private String country;
    private String province;
    private String city;
    private String district;
    private String street;
    private String locationDescribe;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocationDescribe() {
        return locationDescribe;
    }

    public void setLocationDescribe(String locationDescribe) {
        this.locationDescribe = locationDescribe;
    }

    @Override
    public String toString() {
        return "{" +
                "\"latitude\":" + latitude +
                ", \"longitude\":" + longitude +
                ", \"radius\":" + radius +
                ", \"addr\":\"" + addr + '\"' +
                ", \"country\":\"" + country + '\"' +
                ", \"province\":\"" + province + '\"' +
                ", \"city\":\"" + city + '\"' +
                ", \"district\":\"" + district + '\"' +
                ", \"street\":\"" + street + '\"' +
                ", \"locationDescribe\":\"" + locationDescribe + '\"' +
                '}';
    }
}
