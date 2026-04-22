package model;

import java.util.Date;


public class Event implements Comparable<Event> {
    private String id;
    private String title;
    private Date date;
    private Venue venue;
    private Organizer organizer;

    public Event(String id, String title, Date date, Venue venue, Organizer organizer) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.venue = venue;
        this.organizer = organizer;
    }

    @Override
    public int compareTo(Event other) {
        return this.date.compareTo(other.date);
    }

    // Getters and Setters
    public String getId() { 
        return id; 
    }
    public String getTitle() { 
        return title; 
    }
    public Date getDate() { 
        return date; 
    }
    public Venue getVenue() { 
        return venue; 
    }
    public Organizer getOrganizer() { 
        return organizer; 
    }
}