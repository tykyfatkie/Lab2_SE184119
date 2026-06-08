package model;

/**
 * Represents a Player belonging to a Football Club.
 * Extends Entity (Inheritance) with encapsulated fields.
 */
public class Player extends Entity {

    private String clubId;
    private String playerName;
    private String position;
    private int    shirtNumber;

    public Player(String id, String clubId, String playerName,
                  String position, int shirtNumber) {
        super(id);
        this.clubId      = clubId;
        this.playerName  = playerName;
        this.position    = position;
        this.shirtNumber = shirtNumber;
    }

    // ── Getters & Setters (Encapsulation) ──────────────────────────────────
    public String getClubId()               { return clubId; }
    public void   setClubId(String c)       { this.clubId = c; }

    public String getPlayerName()           { return playerName; }
    public void   setPlayerName(String n)   { this.playerName = n; }

    public String getPosition()             { return position; }
    public void   setPosition(String p)     { this.position = p; }

    public int  getShirtNumber()            { return shirtNumber; }
    public void setShirtNumber(int s)       { this.shirtNumber = s; }

    // ── Abstract method implementations ────────────────────────────────────
    @Override
    public String toTableRow() {
        return String.format("| %-6s | %-8s | %-22s | %-12s | %4d |",
                id, clubId, playerName, position, shirtNumber);
    }

    @Override
    public String toFileString() {
        return id + ", " + clubId + ", " + playerName
                + ", " + position + ", " + shirtNumber;
    }

    @Override
    public String toString() {
        return "Player{id='" + id + "', club='" + clubId
                + "', name='" + playerName + "', pos='" + position
                + "', shirt=" + shirtNumber + "}";
    }
}
