package service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    // 1. Instanța unică (Singleton)
    private static AuditService instance = null;
    
    // Numele fișierului CSV care se va genera automat în folderul proiectului
    private static final String FILE_PATH = "audit_log.csv";
    
    // Formatul pentru dată și oră (timestamp)
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 2. Constructor privat
    private AuditService() {}

    // 3. Metoda de obținere a instanței
    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    // Metoda care scrie în fișier
    public void logAction(String actionName) {
        // Folosim try-with-resources pentru a închide automat scriitorul
        // Parametrul 'true' din FileWriter înseamnă "Append" (adaugă la final, nu șterge ce era înainte)
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            String timestamp = LocalDateTime.now().format(formatter);
            writer.append(actionName).append(",").append(timestamp).append("\n");
        } catch (IOException e) {
            System.out.println("Eroare la scrierea in fisierul de audit: " + e.getMessage());
        }
    }
}