package model;

public class Customer extends User {
    private String phoneNumber;

    public Customer(String id, String name, String email, String phoneNumber) {
        super(id, name, email);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }
}