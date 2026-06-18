package football.model;


public class Player {

    // Valid positions (Pattern Recognition – constant list)
    public static final String[] VALID_POSITIONS = {
        "Goalkeeper", "Defender", "Midfielder", "Forward", "Winger"
    };

    private String playerId;
    private String clubId;
    private String playerName;
    private String position;
    private int    shirtNumber;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Player() {}

    public Player(String playerId, String clubId, String playerName,
                  String position, int shirtNumber) {
        this.playerId    = playerId;
        this.clubId      = clubId;
        this.playerName  = playerName;
        this.position    = position;
        this.shirtNumber = shirtNumber;
    }

    // ── Getters & Setters (Encapsulation) ─────────────────────────────────────

    public String getPlayerId()                       { return playerId; }
    public void   setPlayerId(String playerId)        { this.playerId = playerId; }

    public String getClubId()                         { return clubId; }
    public void   setClubId(String clubId)            { this.clubId = clubId; }

    public String getPlayerName()                     { return playerName; }
    public void   setPlayerName(String playerName)    { this.playerName = playerName; }

    public String getPosition()                       { return position; }
    public void   setPosition(String position)        { this.position = position; }

    public int    getShirtNumber()                    { return shirtNumber; }
    public void   setShirtNumber(int shirtNumber)     { this.shirtNumber = shirtNumber; }

    // ── Override ──────────────────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player other = (Player) o;
        return playerId != null && playerId.equalsIgnoreCase(other.playerId);
    }

    @Override
    public int hashCode() {
        return playerId == null ? 0 : playerId.toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return String.format("%-7s | %-8s | %-25s | %-12s | %2d",
                playerId, clubId, playerName, position, shirtNumber);
    }

    /** Convert to file-save format */
    public String toFileString() {
        return String.format("%s, %s, %s, %s, %d",
                playerId, clubId, playerName, position, shirtNumber);
    }
}
