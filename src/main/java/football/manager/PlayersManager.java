package football.manager;

import football.model.Club;
import football.model.Player;
import football.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class PlayersManager {

    private List<Player>  players;
    private ClubsManager  clubsManager; // dependency for club FK validation
    private boolean       modified;

    public PlayersManager(ClubsManager clubsManager) {
        this.players      = new ArrayList<>();
        this.clubsManager = clubsManager;
        this.modified     = false;
    }

    // ── Data access ───────────────────────────────────────────────────────────

    public List<Player> getPlayers()                   { return players; }
    public void         setPlayers(List<Player> p)     { this.players = p; }
    public boolean      isModified()                   { return modified; }
    public void         setModified(boolean m)         { this.modified = m; }

    // ── Helpers ───────────────────────────────────────────────────────────────

    public Player findById(String id) {
        for (Player p : players) {
            if (p.getPlayerId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    public boolean playerExists(String id) {
        return findById(id) != null;
    }

    /** Check if shirt number is already taken within a club (excluding a player ID). */
    private boolean shirtTaken(String clubId, int shirt, String excludePlayerId) {
        for (Player p : players) {
            if (p.getClubId().equalsIgnoreCase(clubId)
                    && p.getShirtNumber() == shirt
                    && !p.getPlayerId().equalsIgnoreCase(excludePlayerId)) {
                return true;
            }
        }
        return false;
    }

    // ── Feature 6: List all players sorted ────────────────────────────────────

    public void listAllPlayersSorted() {
        if (players.isEmpty()) {
            System.out.println("No players available.");
            return;
        }

        List<Player> sorted = new ArrayList<>(players);
        sorted.sort((a, b) -> {
            // Sort by club name ascending
            String nameA = getClubName(a.getClubId());
            String nameB = getClubName(b.getClubId());
            int cmp = nameA.compareToIgnoreCase(nameB);
            // Tie-break: shirt number ascending
            return cmp != 0 ? cmp : Integer.compare(a.getShirtNumber(), b.getShirtNumber());
        });

        printPlayerHeader();
        for (Player p : sorted) System.out.println(p);
        printDivider();
        System.out.println("Total: " + sorted.size() + " player(s).");
    }

    private String getClubName(String clubId) {
        Club c = clubsManager.findById(clubId);
        return c != null ? c.getClubName() : clubId;
    }

    // ── Feature 7: Search by partial player name ──────────────────────────────

    public void searchByName(Scanner sc) {
        System.out.print("\nEnter partial player name: ");
        String keyword = sc.nextLine().trim();
        if (keyword.isEmpty()) {
            System.out.println("Search keyword cannot be empty!");
            return;
        }

        List<Player> result = new ArrayList<>();
        for (Player p : players) {
            if (p.getPlayerName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(p);
            }
        }

        if (result.isEmpty()) {
            System.out.println("No players found matching \"" + keyword + "\".");
        } else {
            printPlayerHeader();
            for (Player p : result) System.out.println(p);
            printDivider();
            System.out.println("Found: " + result.size() + " player(s).");
        }
    }

    // ── Feature 8: Add a new player ───────────────────────────────────────────

    public void addPlayer(Scanner sc) {
        System.out.println("\n=== Add a New Player ===");

        // Player ID
        String pid;
        while (true) {
            System.out.print("Enter Player ID (format Pxxxx): ");
            pid = sc.nextLine().trim();
            if (!ValidationUtils.isValidPlayerId(pid)) {
                System.out.println("Invalid Player ID format! Must be Pxxxx.");
                continue;
            }
            if (playerExists(pid)) {
                System.out.println("This player ID already exists!");
                return;
            }
            break;
        }

        // Club ID – show list first
        clubsManager.listAllClubs();
        String cid;
        while (true) {
            System.out.print("Enter Club ID for this player: ");
            cid = sc.nextLine().trim();
            if (!clubsManager.clubExists(cid)) {
                System.out.println("This club does not exist!");
            } else break;
        }

        // Player name
        String name;
        while (true) {
            System.out.print("Enter Player Name: ");
            name = sc.nextLine().trim();
            if (!ValidationUtils.isNonEmpty(name)) {
                System.out.println("Player name cannot be empty!");
            } else break;
        }

        // Position
        System.out.println("Valid positions: " + String.join(", ", Player.VALID_POSITIONS));
        String position;
        while (true) {
            System.out.print("Enter Position: ");
            position = ValidationUtils.normalisePosition(sc.nextLine().trim());
            if (position == null) {
                System.out.println("Invalid position!");
            } else break;
        }

        // Shirt number
        int shirt;
        while (true) {
            System.out.print("Enter Shirt Number (1-99): ");
            shirt = ValidationUtils.parseInt(sc.nextLine());
            if (!ValidationUtils.isValidShirtNumber(shirt)) {
                System.out.println("Shirt number must be between 1 and 99!");
                continue;
            }
            if (shirtTaken(cid, shirt, "")) {
                System.out.println("This shirt number already exists in this club!");
                continue;
            }
            break;
        }

        players.add(new Player(pid, cid, name, position, shirt));
        modified = true;
        System.out.println("Player added successfully!");
    }

    // ── Feature 9: Remove a player ────────────────────────────────────────────

    public void removePlayer(Scanner sc) {
        System.out.print("\nEnter Player ID to remove: ");
        String id = sc.nextLine().trim();
        Player p = findById(id);
        if (p == null) {
            System.out.println("This player does not exist!");
        } else {
            players.remove(p);
            modified = true;
            System.out.println("Player removed successfully!");
        }
    }

    // ── Feature 10: Update a player ───────────────────────────────────────────

    public void updatePlayer(Scanner sc) {
        System.out.print("\nEnter Player ID to update: ");
        String id = sc.nextLine().trim();
        Player p = findById(id);
        if (p == null) {
            System.out.println("This player does not exist!");
            return;
        }

        System.out.println("Current info: " + p);
        System.out.println("(Press Enter to skip a field)");

        // Name
        System.out.print("New Player Name [" + p.getPlayerName() + "]: ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) p.setPlayerName(name);

        // Position
        System.out.print("New Position [" + p.getPosition() + "]: ");
        String rawPos = sc.nextLine().trim();
        if (!rawPos.isEmpty()) {
            String norm = ValidationUtils.normalisePosition(rawPos);
            if (norm == null) {
                System.out.println("Invalid position – update skipped.");
            } else {
                p.setPosition(norm);
            }
        }

        // Shirt number
        System.out.print("New Shirt Number [" + p.getShirtNumber() + "]: ");
        String shirtStr = sc.nextLine().trim();
        if (!shirtStr.isEmpty()) {
            int shirt = ValidationUtils.parseInt(shirtStr);
            if (!ValidationUtils.isValidShirtNumber(shirt)) {
                System.out.println("Invalid shirt number – update skipped.");
            } else if (shirtTaken(p.getClubId(), shirt, p.getPlayerId())) {
                System.out.println("This shirt number already exists in this club!");
            } else {
                p.setShirtNumber(shirt);
            }
        }

        modified = true;
        System.out.println("Player updated successfully!");
    }

    // ── Feature 11: List by position ─────────────────────────────────────────

    public void listByPosition(Scanner sc) {
        System.out.println("Valid positions: " + String.join(", ", Player.VALID_POSITIONS));
        System.out.print("Enter Position: ");
        String raw = sc.nextLine().trim();
        String position = ValidationUtils.normalisePosition(raw);
        if (position == null) {
            System.out.println("Invalid position!");
            return;
        }

        List<Player> result = new ArrayList<>();
        for (Player p : players) {
            if (p.getPosition().equalsIgnoreCase(position)) result.add(p);
        }

        if (result.isEmpty()) {
            System.out.println("No players found for position: " + position);
        } else {
            printPlayerHeader();
            for (Player p : result) System.out.println(p);
            printDivider();
            System.out.println("Found: " + result.size() + " player(s).");
        }
    }

    // ── Print helpers ─────────────────────────────────────────────────────────

    private void printPlayerHeader() {
        printDivider();
        System.out.printf("%-7s | %-8s | %-25s | %-12s | %s%n",
                "ID", "Club ID", "Player Name", "Position", "Shirt#");
        printDivider();
    }

    private void printDivider() {
        System.out.println(new String(new char[72]).replace('\0', '-'));
    }
}
