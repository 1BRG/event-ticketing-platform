package model;

public class Venue {
    private String name;
    private String address;
    private int capacity;

    public Venue(String name, String address, int capacity) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    // Getters and Setters
    public String getName() { 
        return name; 
    }
    public String getAddress() {
        return address; }
    public int getCapacity() { 
        return capacity; }
}