package manager;

import model.Club;
import model.Player;
import utils.FileUtils;
import utils.Manageable;
import utils.Saveable;
import utils.ValidationUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Manages all Player-related CRUD operations.
 * Implements Manageable<Player> and Saveable (Polymorphism via Interfaces).
 */
public class PlayersManager implements Manageable<Player>, Saveable {

    private List<Player> players;
    private ClubsManager clubsManager; // dependency for FK validation

    private static final String PLAYERS_FILE = "players.txt";

    private static final String TABLE_SEP =
        "+--------+----------+------------------------+--------------+------+";
    private static final String TABLE_HEADER =
        "| Plyr ID| Club ID  | Player Name            | Position     | Shirt|";

    public PlayersManager(ClubsManager clubsManager) {
        this.players      = new ArrayList<>();
        this.clubsManager = clubsManager;
    }

    public List<Player> getPlayers() { return players; }
    public void setPlayers(List<Player> players) { this.players = players; }

    // ── Interface: Manageable ───────────────────────────────────────────────

    @Override
    public void add(Player player) {
        players.add(player);
    }

    @Override
    public Player findById(String id) {
        for (Player p : players) {
            if (p.getId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    @Override
    public boolean removeById(String id) {
        return players.removeIf(p -> p.getId().equalsIgnoreCase(id));
    }

    @Override
    public void displayAll() {
        if (players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }
        printTableHeader();
        for (Player p : players) {
            System.out.println(p.toTableRow());
            System.out.println(TABLE_SEP);
        }
    }

    // ── Interface: Saveable ─────────────────────────────────────────────────

    @Override
    public void saveToFile(String filename) {
        if (FileUtils.savePlayers(filename, players)) {
            System.out.println("Players saved to " + filename + " successfully.");
        } else {
            System.out.println("Error: Failed to save players.");
        }
    }

    @Override
    public void loadFromFile(String filename) {
        List<Player> loaded = FileUtils.loadPlayers(filename, clubsManager.getClubs());
        if (loaded == null) {
            System.out.println("Load data failed!");
        } else {
            players = loaded;
        }
    }

    // ── Feature 6: List all players sorted by club name, then shirt number ─
    public void listPlayersSorted() {
        System.out.println("\n=== ALL PLAYERS (sorted by Club Name, Shirt Number) ===");
        if (players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }

        List<Player> sorted = new ArrayList<>(players);
        Collections.sort(sorted, new Comparator<Player>() {
            @Override
            public int compare(Player a, Player b) {
                Club clubA = clubsManager.findById(a.getClubId());
                Club clubB = clubsManager.findById(b.getClubId());
                String nameA = (clubA != null) ? clubA.getClubName() : a.getClubId();
                String nameB = (clubB != null) ? clubB.getClubName() : b.getClubId();
                int cmp = nameA.compareToIgnoreCase(nameB);
                if (cmp != 0) return cmp;
                return Integer.compare(a.getShirtNumber(), b.getShirtNumber());
            }
        });

        printTableHeader();
        for (Player p : sorted) {
            System.out.println(p.toTableRow());
            System.out.println(TABLE_SEP);
        }
    }

    // ── Feature 7: Search players by partial name ───────────────────────────
    public void searchPlayerByName(Scanner sc) {
        System.out.println("\n=== SEARCH PLAYERS BY NAME ===");
        System.out.print("Enter partial player name: ");
        String keyword = sc.nextLine().trim().toLowerCase();

        boolean found = false;
        printTableHeader();
        for (Player p : players) {
            if (p.getPlayerName().toLowerCase().contains(keyword)) {
                System.out.println(p.toTableRow());
                System.out.println(TABLE_SEP);
                found = true;
            }
        }
        if (!found) System.out.println("No players found matching \"" + keyword + "\".");
    }

    // ── Feature 8: Add a new player ─────────────────────────────────────────
    public void addPlayer(Scanner sc) {
        System.out.println("\n=== ADD NEW PLAYER ===");

        // Player ID
        String id;
        while (true) {
            System.out.print("Enter Player ID (format Pxxxx): ");
            id = sc.nextLine().trim();
            if (!ValidationUtils.isValidPlayerId(id)) {
                System.out.println("Invalid Player ID format! Must be Pxxxx (e.g., P0001).");
                continue;
            }
            if (findById(id) != null) {
                System.out.println("This player ID already exists!");
                return;
            }
            break;
        }

        // Show clubs and pick club ID
        System.out.println("-- Available Clubs --");
        clubsManager.displayAll();
        String clubId;
        while (true) {
            System.out.print("Enter Club ID for this player: ");
            clubId = sc.nextLine().trim();
            if (clubsManager.findById(clubId) == null) {
                System.out.println("This club does not exist!");
            } else break;
        }

        // Player name
        String name;
        while (true) {
            System.out.print("Enter Player Name: ");
            name = sc.nextLine().trim();
            if (!ValidationUtils.isNotEmpty(name)) {
                System.out.println("Player name cannot be empty!");
            } else break;
        }

        // Position
        String position;
        while (true) {
            System.out.print("Enter Position (Goalkeeper/Defender/Midfielder/Forward/Winger): ");
            position = sc.nextLine().trim();
            if (!ValidationUtils.isValidPosition(position)) {
                System.out.println("Invalid position! Must be one of: Goalkeeper, Defender, Midfielder, Forward, Winger.");
            } else break;
        }
        position = ValidationUtils.normalizePosition(position);

        // Shirt number
        int shirt;
        while (true) {
            System.out.print("Enter Shirt Number (1-99): ");
            shirt = ValidationUtils.parseInt(sc.nextLine());
            if (!ValidationUtils.isValidShirtNumber(shirt)) {
                System.out.println("Shirt number must be an integer between 1 and 99!");
                continue;
            }
            if (isShirtTaken(clubId, shirt, null)) {
                System.out.println("This shirt number already exists in this club!");
                continue;
            }
            break;
        }

        add(new Player(id, clubId, name, position, shirt));
        System.out.println("Player added successfully!");
    }

    // ── Feature 9: Remove player by ID ─────────────────────────────────────
    public void removePlayer(Scanner sc) {
        System.out.println("\n=== REMOVE PLAYER ===");
        System.out.print("Enter Player ID to remove: ");
        String id = sc.nextLine().trim();
        if (findById(id) == null) {
            System.out.println("This player does not exist!");
        } else {
            removeById(id);
            System.out.println("Player removed successfully!");
        }
    }

    // ── Feature 10: Update player by ID ────────────────────────────────────
    public void updatePlayer(Scanner sc) {
        System.out.println("\n=== UPDATE PLAYER ===");
        System.out.print("Enter Player ID to update: ");
        String id = sc.nextLine().trim();
        Player player = findById(id);
        if (player == null) {
            System.out.println("This player does not exist!");
            return;
        }
        System.out.println("Leave blank to keep current value.");

        // Player name
        System.out.print("Enter new Player Name [" + player.getPlayerName() + "]: ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) player.setPlayerName(name);

        // Position
        System.out.print("Enter new Position [" + player.getPosition() + "]: ");
        String pos = sc.nextLine().trim();
        if (!pos.isEmpty()) {
            if (!ValidationUtils.isValidPosition(pos)) {
                System.out.println("Invalid position. Position not updated.");
            } else {
                player.setPosition(ValidationUtils.normalizePosition(pos));
            }
        }

        // Shirt number
        System.out.print("Enter new Shirt Number [" + player.getShirtNumber() + "]: ");
        String shirtStr = sc.nextLine().trim();
        if (!shirtStr.isEmpty()) {
            int shirt = ValidationUtils.parseInt(shirtStr);
            if (!ValidationUtils.isValidShirtNumber(shirt)) {
                System.out.println("Invalid shirt number. Not updated.");
            } else if (isShirtTaken(player.getClubId(), shirt, player.getId())) {
                System.out.println("This shirt number already exists in this club!");
            } else {
                player.setShirtNumber(shirt);
            }
        }
        System.out.println("Player updated successfully!");
    }

    // ── Feature 11: List players by position ───────────────────────────────
    public void listPlayersByPosition(Scanner sc) {
        System.out.println("\n=== PLAYERS BY POSITION ===");
        System.out.print("Enter Position (Goalkeeper/Defender/Midfielder/Forward/Winger): ");
        String pos = sc.nextLine().trim();

        if (!ValidationUtils.isValidPosition(pos)) {
            System.out.println("Invalid position!");
            return;
        }
        String normalized = ValidationUtils.normalizePosition(pos);
        boolean found = false;
        printTableHeader();
        for (Player p : players) {
            if (p.getPosition().equalsIgnoreCase(normalized)) {
                System.out.println(p.toTableRow());
                System.out.println(TABLE_SEP);
                found = true;
            }
        }
        if (!found) System.out.println("No players found with position: " + normalized);
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    /**
     * Check if a shirt number is already taken in a club.
     * excludePlayerId is the current player's ID when updating (so we skip self).
     */
    private boolean isShirtTaken(String clubId, int shirt, String excludePlayerId) {
        for (Player p : players) {
            if (!p.getClubId().equals(clubId)) continue;
            if (excludePlayerId != null && p.getId().equals(excludePlayerId)) continue;
            if (p.getShirtNumber() == shirt) return true;
        }
        return false;
    }

    private void printTableHeader() {
        System.out.println(TABLE_SEP);
        System.out.println(TABLE_HEADER);
        System.out.println(TABLE_SEP);
    }
}
