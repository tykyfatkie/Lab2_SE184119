package model;


public class Club {

    private String clubId;
    private String clubName;
    private String sponsorBrand;
    private double budget; // in million EUR

    // ── Constructors ──────────────────────────────────────────────────────────

    public Club() {}

    public Club(String clubId, String clubName, String sponsorBrand, double budget) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.sponsorBrand = sponsorBrand;
        this.budget = budget;
    }

    // ── Getters & Setters (Encapsulation) ─────────────────────────────────────

    public String getClubId()                        { return clubId; }
    public void   setClubId(String clubId)           { this.clubId = clubId; }

    public String getClubName()                      { return clubName; }
    public void   setClubName(String clubName)       { this.clubName = clubName; }

    public String getSponsorBrand()                  { return sponsorBrand; }
    public void   setSponsorBrand(String sponsorBrand){ this.sponsorBrand = sponsorBrand; }

    public double getBudget()                        { return budget; }
    public void   setBudget(double budget)           { this.budget = budget; }

    // ── Override ──────────────────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Club)) return false;
        Club other = (Club) o;
        return clubId != null && clubId.equalsIgnoreCase(other.clubId);
    }

    @Override
    public int hashCode() {
        return clubId == null ? 0 : clubId.toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return String.format("%-10s | %-30s | %-10s | %,.0f M EUR",
                clubId, clubName, sponsorBrand, budget);
    }

    /** Convert to file-save format */
    public String toFileString() {
        return String.format("%s, %s, %s, %.0f", clubId, clubName, sponsorBrand, budget);
    }
}
