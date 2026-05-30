package service;

import model.Event;
import model.Organizer;
import model.Venue;

import java.util.*;

public class EventService implements IService<Event> {
    private Set<Event> events;
    
    private Map<String, Venue> venues;
    private Map<String, Organizer> organizers;

    public EventService() {
        this.events = new TreeSet<>();
        this.venues = new HashMap<>();
        this.organizers = new HashMap<>();
    }

    @Override
    public void add(Event entity) {
        events.add(entity);
        System.out.println("Eveniment adaugat/salvat: " + entity.getTitle());
    }

    @Override
    public Event findById(String id) {
        for (Event event : events) {
            if (event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }

    @Override
    public Collection<Event> getAll() {
        return events;
    }

    public void addVenue(Venue venue) {
        venues.put(venue.getId(), venue);
    }

    public void addOrganizer(Organizer organizer) {
        organizers.put(organizer.getId(), organizer);
    }

    public void createEvent(String id, String title, Date date, String venueId, String organizerId) {
        Venue venue = venues.get(venueId);
        Organizer organizer = organizers.get(organizerId);

        if (venue == null || organizer == null) {
            System.out.println("Eroare: Locatia sau Organizatorul nu exista.");
            return;
        }

        Event event = new Event(id, title, date, venue, organizer);
        this.add(event);
    }

    public void displayUpcomingEvents() {
        System.out.println("\n--- Evenimente Viitoare (Sortate Cronologic) ---");
        for (Event event : events) {
            System.out.println(event.getTitle() + " | Locatie: " + event.getVenue().getName() + " | Data: " + event.getDate());
        }
    }

    public void displayEventsByOrganizer(String organizerId) {
        System.out.println("\n--- Evenimente pentru organizatorul: " + organizerId + " ---");
        for (Event event : events) {
            if (event.getOrganizer().getId().equals(organizerId)) {
                System.out.println("- " + event.getTitle());
            }
        }
    }
}