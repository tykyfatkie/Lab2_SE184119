package dispatcher;

import manager.ClubsManager;
import manager.PlayersManager;

import java.util.Scanner;

public class Menu {

    private final ClubsManager    clubsManager;
    private final PlayersManager  playersManager;
    private final AppController   appController;
    private final Scanner         sc;

    public Menu() {
        this.clubsManager   = new ClubsManager();
        this.playersManager = new PlayersManager(clubsManager);
        this.appController  = new AppController(clubsManager, playersManager);
        this.sc             = new Scanner(System.in);
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public void run() {
        System.out.println("=================================================");
        System.out.println("  European Elite League – Club & Player Manager  ");
        System.out.println("=================================================");

        // Auto-load on startup
        appController.loadData(false);

        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":  clubsManager.listAllClubs();              break;
                case "2":  clubsManager.addClub(sc);                 break;
                case "3":  clubsManager.searchClubById(sc);          break;
                case "4":  clubsManager.updateClub(sc);              break;
                case "5":  clubsManager.listClubsByBudget(sc);       break;
                case "6":  playersManager.listAllPlayersSorted();    break;
                case "7":  playersManager.searchByName(sc);          break;
                case "8":  playersManager.addPlayer(sc);             break;
                case "9":  playersManager.removePlayer(sc);          break;
                case "10": playersManager.updatePlayer(sc);          break;
                case "11": playersManager.listByPosition(sc);        break;
                case "12": appController.saveData();                break;
                case "13": appController.loadData(true);             break;
                case "14": running = appController.quit();           break;
                default:   System.out.println("Invalid choice! Please enter 1-14."); break;
            }
        }
        sc.close();
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