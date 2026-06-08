package model;

/**
 * Represents a Football Club in the EEL tournament.
 * Extends Entity (Inheritance) with encapsulated fields.
 */
public class Club extends Entity {

    private String clubName;
    private String sponsorBrand;
    private double budget; // in million EUR

    public Club(String id, String clubName, String sponsorBrand, double budget) {
        super(id);
        this.clubName = clubName;
        this.sponsorBrand = sponsorBrand;
        this.budget = budget;
    }

    // ── Getters & Setters (Encapsulation) ──────────────────────────────────
    public String getClubName()             { return clubName; }
    public void   setClubName(String n)     { this.clubName = n; }

    public String getSponsorBrand()         { return sponsorBrand; }
    public void   setSponsorBrand(String s) { this.sponsorBrand = s; }

    public double getBudget()               { return budget; }
    public void   setBudget(double b)       { this.budget = b; }

    // ── Abstract method implementations ────────────────────────────────────
    @Override
    public String toTableRow() {
        return String.format("| %-8s | %-30s | %-10s | %10.1f |",
                id, clubName, sponsorBrand, budget);
    }

    @Override
    public String toFileString() {
        return id + ", " + clubName + ", " + sponsorBrand + ", " + (int) budget;
    }

    @Override
    public String toString() {
        return "Club{id='" + id + "', name='" + clubName
                + "', sponsor='" + sponsorBrand + "', budget=" + budget + "}";
    }
}
