package config;

public class DBConfig {
    public static final String URL = "jdbc:postgresql://localhost:5432/event_platform";
    public static final String USERNAME = "postgres"; 
    public static final String PASSWORD = System.getenv("DB_PASSWORD"); 
}