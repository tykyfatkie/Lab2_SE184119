package football;

import football.manager.ClubsManager;
import football.manager.PlayersManager;
import football.model.Club;
import football.model.Player;
import football.utils.FileUtils;

import java.util.List;
import java.util.Scanner;

public class Menu {

    private static final String CLUBS_FILE   = "data/clubs.txt";
    private static final String PLAYERS_FILE = "data/players.txt";

    private final ClubsManager   clubsManager;
    private final PlayersManager playersManager;
    private final Scanner        sc;

    public Menu() {
        this.clubsManager   = new ClubsManager();
        this.playersManager = new PlayersManager(clubsManager);
        this.sc             = new Scanner(System.in);
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public void run() {
        System.out.println("=================================================");
        System.out.println("  European Elite League – Club & Player Manager  ");
        System.out.println("=================================================");

        // Auto-load on startup
        loadData(false);

        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":  clubsManager.listAllClubs();            break;
                case "2":  clubsManager.addClub(sc);               break;
                case "3":  clubsManager.searchClubById(sc);        break;
                case "4":  clubsManager.updateClub(sc);            break;
                case "5":  clubsManager.listClubsByBudget(sc);     break;
                case "6":  playersManager.listAllPlayersSorted();  break;
                case "7":  playersManager.searchByName(sc);        break;
                case "8":  playersManager.addPlayer(sc);           break;
                case "9":  playersManager.removePlayer(sc);        break;
                case "10": playersManager.updatePlayer(sc);        break;
                case "11": playersManager.listByPosition(sc);      break;
                case "12": saveData();                             break;
                case "13": loadData(true);                         break;
                case "14": running = quit();                       break;
                default:   System.out.println("Invalid choice! Please enter 1-14."); break;
            }
        }
        sc.close();
    }

    // ── Feature 12: Save data ─────────────────────────────────────────────────

    private void saveData() {
        boolean ok1 = FileUtils.saveClubs(CLUBS_FILE, clubsManager.getClubs());
        boolean ok2 = FileUtils.savePlayers(PLAYERS_FILE, playersManager.getPlayers());
        if (ok1 && ok2) {
            clubsManager.setModified(false);
            playersManager.setModified(false);
            System.out.println("Data saved successfully!");
        }
    }

    // ── Feature 13: Load data ─────────────────────────────────────────────────

    private void loadData(boolean interactive) {
        List<Club> loadedClubs = FileUtils.loadClubs(CLUBS_FILE);
        if (loadedClubs == null) return; // error already printed

        // Pass clubs to player loader for FK validation
        List<Player> loadedPlayers = FileUtils.loadPlayers(PLAYERS_FILE, loadedClubs);
        if (loadedPlayers == null) return;

        // Clear current data and replace
        clubsManager.getClubs().clear();
        clubsManager.getClubs().addAll(loadedClubs);
        playersManager.getPlayers().clear();
        playersManager.getPlayers().addAll(loadedPlayers);

        clubsManager.setModified(false);
        playersManager.setModified(false);

        if (interactive) {
            System.out.println("Load data successfully! "
                    + loadedClubs.size() + " clubs, "
                    + loadedPlayers.size() + " players loaded.");
        }
    }

    // ── Feature 14: Quit ─────────────────────────────────────────────────────

    /** Returns false to stop the loop. */
    private boolean quit() {
        if (clubsManager.isModified() || playersManager.isModified()) {
            System.out.println("Unsaved changes detected. Saving before exit...");
            saveData();
        }
        System.out.println("Goodbye!");
        return false;
    }

    // ── Print menu ────────────────────────────────────────────────────────────

    private void printMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println(" 1.  List all clubs");
        System.out.println(" 2.  Add a new club");
        System.out.println(" 3.  Search for a club by ID");
        System.out.println(" 4.  Update a club by ID");
        System.out.println(" 5.  List clubs with budget ≤ value");
        System.out.println(" 6.  List all players (sorted)");
        System.out.println(" 7.  Search players by name");
        System.out.println(" 8.  Add a new player");
        System.out.println(" 9.  Remove a player");
        System.out.println("10.  Update a player");
        System.out.println("11.  List players by position");
        System.out.println("12.  Save data to files");
        System.out.println("13.  Load data from files");
        System.out.println("14.  Quit");
        System.out.println("================================");
        System.out.print("Enter your choice: ");
    }
}
