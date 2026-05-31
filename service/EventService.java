package service;

import model.Event;
import model.Organizer;
import model.Venue;
import repository.EventRepository;
import repository.OrganizerRepository;
import repository.VenueRepository;

import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

public class EventService implements IService<Event> {
   
    private EventRepository eventRepository;
    private VenueRepository venueRepository;
    private OrganizerRepository organizerRepository;

    public EventService() {
        this.eventRepository = EventRepository.getInstance();
        this.venueRepository = VenueRepository.getInstance();
        this.organizerRepository = OrganizerRepository.getInstance();
    }


    @Override
    public void add(Event entity) {
        eventRepository.create(entity);
        AuditService.getInstance().logAction("add_event");
    }

    @Override
    public Event findById(String id) {
        AuditService.getInstance().logAction("find_event_by_id");
        return eventRepository.read(id);
    }

    @Override
    public Collection<Event> getAll() {
        AuditService.getInstance().logAction("get_all_events");
        return eventRepository.readAll();
    }



    public void addVenue(Venue venue) {
        venueRepository.create(venue);
        AuditService.getInstance().logAction("add_venue");
    }

    public void addOrganizer(Organizer organizer) {
        organizerRepository.create(organizer);
        AuditService.getInstance().logAction("add_organizer");
    }

    public void createEvent(String id, String title, Date date, String venueId, String organizerId) {

        Venue venue = venueRepository.read(venueId);
        Organizer organizer = organizerRepository.read(organizerId);

        if (venue == null || organizer == null) {
            System.out.println("Eroare: Locatia sau Organizatorul nu exista in baza de date.");
            return;
        }

        Event event = new Event(id, title, date, venue, organizer);
        this.add(event); 
        AuditService.getInstance().logAction("create_event_complex");
    }


    public void displayUpcomingEvents() {
        System.out.println("\n--- Evenimente Viitoare (Sortate Cronologic) ---");
        
        Collection<Event> eventsFromDb = eventRepository.readAll();
        
        TreeSet<Event> sortedEvents = new TreeSet<>(eventsFromDb);
        
        for (Event event : sortedEvents) {
            System.out.println("ID: " + event.getId() + " | Titlu: " + event.getTitle() + " | Locatie: " + event.getVenue().getName() + " | Data: " + event.getDate());
        }
        
        AuditService.getInstance().logAction("display_upcoming_events");
    }

    public void displayEventsByOrganizer(String organizerId) {
        System.out.println("\n--- Evenimente pentru organizatorul: " + organizerId + " ---");
        
        Collection<Event> eventsFromDb = eventRepository.readAll();
        for (Event event : eventsFromDb) {
            if (event.getOrganizer().getId().equals(organizerId)) {
                System.out.println("- " + event.getTitle());
            }
        }
        
        AuditService.getInstance().logAction("display_events_by_organizer");
    }
}