package model;

/**
 * Abstract base class for all entities in the system.
 * Demonstrates Abstraction + Inheritance (OOP principles).
 */
public abstract class Entity {
    protected String id;

    public Entity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Each entity must implement its own display format.
     */
    public abstract String toTableRow();

    /**
     * Each entity must implement its own file-save format.
     */
    public abstract String toFileString();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Entity other = (Entity) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
