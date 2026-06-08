package ui;

import manager.ClubsManager;
import manager.PlayersManager;
import model.Club;
import model.Player;
import utils.FileUtils;

import java.util.List;
import java.util.Scanner;

/**
 * Console menu for the Football Club & Player Management System.
 * Handles all user interaction and delegates to manager classes.
 */
public class Menu {

    private ClubsManager  clubsManager;
    private PlayersManager playersManager;
    private Scanner        sc;

    private static final String CLUBS_FILE   = "clubs.txt";
    private static final String PLAYERS_FILE = "players.txt";

    // Track whether unsaved changes exist
    private boolean hasChanges = false;

    public Menu() {
        clubsManager   = new ClubsManager();
        playersManager = new PlayersManager(clubsManager);
        sc             = new Scanner(System.in);
    }

    // ── Entry point ─────────────────────────────────────────────────────────
    public void run() {
        System.out.println("============================================");
        System.out.println("  EUROPEAN ELITE LEAGUE - MANAGEMENT SYSTEM");
        System.out.println("============================================");

        // Auto-load data on startup (Function 13 logic)
        autoLoad();

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");
            String input = sc.nextLine().trim();
            int choice = parseChoice(input);

            switch (choice) {
                case 1:  clubsManager.listAllClubs();         break;
                case 2:  clubsManager.addClub(sc);            hasChanges = true; break;
                case 3:  clubsManager.searchClubById(sc);     break;
                case 4:  clubsManager.updateClub(sc);         hasChanges = true; break;
                case 5:  clubsManager.listClubsByBudget(sc);  break;
                case 6:  playersManager.listPlayersSorted();  break;
                case 7:  playersManager.searchPlayerByName(sc); break;
                case 8:  playersManager.addPlayer(sc);        hasChanges = true; break;
                case 9:  playersManager.removePlayer(sc);     hasChanges = true; break;
                case 10: playersManager.updatePlayer(sc);     hasChanges = true; break;
                case 11: playersManager.listPlayersByPosition(sc); break;
                case 12: saveData();                          break;
                case 13: reloadData();                        break;
                case 14: running = quit();                    break;
                default: System.out.println("Invalid choice! Please enter 1-14.");
            }
        }
        sc.close();
    }

    // ── Menu display ─────────────────────────────────────────────────────────
    private void printMenu() {
        System.out.println("\n============================================");
        System.out.println("           MAIN MENU");
        System.out.println("============================================");
        System.out.println("  --- Club Management ---");
        System.out.println("  1.  List all clubs");
        System.out.println("  2.  Add a new club");
        System.out.println("  3.  Search club by ID");
        System.out.println("  4.  Update club by ID");
        System.out.println("  5.  List clubs with budget ≤ value");
        System.out.println("  --- Player Management ---");
        System.out.println("  6.  List all players (sorted)");
        System.out.println("  7.  Search players by name");
        System.out.println("  8.  Add a new player");
        System.out.println("  9.  Remove a player");
        System.out.println(" 10.  Update a player");
        System.out.println(" 11.  List players by position");
        System.out.println("  --- System ---");
        System.out.println(" 12.  Save data to files");
        System.out.println(" 13.  Load data from files");
        System.out.println(" 14.  Quit");
        System.out.println("============================================");
    }

    // ── Startup auto-load ────────────────────────────────────────────────────
    private void autoLoad() {
        System.out.println("Loading data from files...");
        List<Club> loadedClubs = FileUtils.loadClubs(CLUBS_FILE);
        if (loadedClubs == null) {
            System.out.println("Load data failed!");
            return;
        }
        clubsManager.setClubs(loadedClubs);

        List<Player> loadedPlayers = FileUtils.loadPlayers(PLAYERS_FILE, loadedClubs);
        if (loadedPlayers == null) {
            System.out.println("Load data failed!");
            return;
        }
        playersManager.setPlayers(loadedPlayers);
        System.out.println("Load data successfully!");
    }

    // ── Feature 12: Save ─────────────────────────────────────────────────────
    private void saveData() {
        System.out.println("\n=== SAVE DATA TO FILES ===");
        clubsManager.saveToFile(CLUBS_FILE);
        playersManager.saveToFile(PLAYERS_FILE);
        hasChanges = false;
    }

    // ── Feature 13: Reload from files ────────────────────────────────────────
    private void reloadData() {
        System.out.println("\n=== LOAD DATA FROM FILES ===");
        clubsManager.getClubs().clear();
        playersManager.getPlayers().clear();

        clubsManager.loadFromFile(CLUBS_FILE);

        // Only proceed to load players if clubs loaded fine
        if (!clubsManager.getClubs().isEmpty()) {
            playersManager.loadFromFile(PLAYERS_FILE);
            if (!playersManager.getPlayers().isEmpty()
                    || FileUtils.loadPlayers(PLAYERS_FILE, clubsManager.getClubs()) != null) {
                System.out.println("Load data successfully!");
                hasChanges = false;
            }
        }
    }

    // ── Feature 14: Quit ─────────────────────────────────────────────────────
    private boolean quit() {
        if (hasChanges) {
            System.out.println("\nUnsaved changes detected. Saving before exit...");
            clubsManager.saveToFile(CLUBS_FILE);
            playersManager.saveToFile(PLAYERS_FILE);
        }
        System.out.println("Goodbye!");
        return false; // signals loop to stop
    }

    // ── Helper ───────────────────────────────────────────────────────────────
    private int parseChoice(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
