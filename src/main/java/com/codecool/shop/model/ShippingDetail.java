package com.codecool.shop.model;

import com.codecool.shop.Globals;
import com.codecool.shop.order.Order;
import com.codecool.shop.user.User;

public class ShippingDetail {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private int zipCode;
    private String city;
    private String address;
    private User user;

    public ShippingDetail(String firstName, String lastName, String email, int zipCode, String city, String address, User user) {
        id = Globals.shippingDao.getAll().size();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.zipCode = zipCode;
        this.city = city;
        this.address = address;
        this.user = user;
    }

    public ShippingDetail(int id, String firstName, String lastName, String email, int zipCode, String city, String address, User user) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.zipCode = zipCode;
        this.city = city;
        this.address = address;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public int getUserId(){
        return this.user.getId();
    }

    public int checkDetailIfAlreadyExists(){
        for (ShippingDetail detail: Globals.shippingDao.getBy(getUserId())){
            if (this.getFirstName().equals(detail.getFirstName()) &&
                this.getLastName().equals(detail.getLastName()) &&
                this.getEmail().equals(detail.getEmail()) &&
                this.getZipCode() == detail.getZipCode() &&
                this.getCity().equals(detail.getCity()) &&
                this.getAddress().equals(detail.getAddress())){

                return detail.getId();

            }
        }
        return -1;
    }


}
