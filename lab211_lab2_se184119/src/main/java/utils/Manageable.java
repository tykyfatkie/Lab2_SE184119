package utils;

/**
 * Interface for manager classes that handle CRUD operations.
 * Demonstrates Interface + Polymorphism (OOP principles).
 */
public interface Manageable<T> {
    void add(T item);
    T findById(String id);
    boolean removeById(String id);
    void displayAll();
}
