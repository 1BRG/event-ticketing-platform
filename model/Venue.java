package model;

public class Venue {
    private String id;
    private String name;
    private String address;
    private int capacity;

    public Venue(String id, String name, String address, int capacity) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    // Getters and Setters
    public String getId() { 
        return id; 
    }
    public String getName() { 
        return name; 
    }
    public String getAddress() {
        return address; 
    }
    public int getCapacity() { 
        return capacity; 
    }

    public void setId(String id) { 
        this.id = id; 
    }
    public void setName(String name) { 
        this.name = name; 
    }
    public void setAddress(String address) { 
        this.address = address; 
    }
    public void setCapacity(int capacity) { 
        this.capacity = capacity; 
    }
}