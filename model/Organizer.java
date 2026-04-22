package model;

public class Organizer extends User {
    private String companyName;

    public Organizer(String id, String name, String email, String companyName) {
        super(id, name, email);
        this.companyName = companyName;
    }

    public String getCompanyName() { 
        return companyName; 
    }
    public void setCompanyName(String companyName) { 
        this.companyName = companyName; 
    }
}