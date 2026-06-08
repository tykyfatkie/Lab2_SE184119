package utils;

/**
 * Interface for objects that can be saved to / loaded from files.
 * Demonstrates Interface usage (OOP principle).
 */
public interface Saveable {
    void saveToFile(String filename);
    void loadFromFile(String filename);
}
