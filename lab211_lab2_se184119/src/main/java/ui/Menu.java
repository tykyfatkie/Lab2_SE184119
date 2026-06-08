package ui;

import manager.ClubsManager;
import manager.PlayersManager;

import java.util.Scanner;

public class Menu {

    private final ClubsManager   clubsManager;
    private final PlayersManager playersManager;
    private final Scanner        sc;

    private final DataLoader  dataLoader;
    private final DataSaver   dataSaver;
    private final QuitHandler quitHandler;

    private boolean hasChanges = false;

    public Menu() {
        clubsManager   = new ClubsManager();
        playersManager = new PlayersManager(clubsManager);
        sc             = new Scanner(System.in);

        dataLoader  = new DataLoader(clubsManager, playersManager);
        dataSaver   = new DataSaver(clubsManager, playersManager);
        quitHandler = new QuitHandler(clubsManager, playersManager);
    }

    public void run() {
        System.out.println("============================================");
        System.out.println("  EUROPEAN ELITE LEAGUE - MANAGEMENT SYSTEM");
        System.out.println("============================================");

        dataLoader.autoLoad();

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");
            int choice = parseChoice(sc.nextLine().trim());

            switch (choice) {
                case 1:  clubsManager.listAllClubs();              break;
                case 2:  clubsManager.addClub(sc);                 hasChanges = true; break;
                case 3:  clubsManager.searchClubById(sc);          break;
                case 4:  clubsManager.updateClub(sc);              hasChanges = true; break;
                case 5:  clubsManager.listClubsByBudget(sc);       break;
                case 6:  playersManager.listPlayersSorted();       break;
                case 7:  playersManager.searchPlayerByName(sc);    break;
                case 8:  playersManager.addPlayer(sc);             hasChanges = true; break;
                case 9:  playersManager.removePlayer(sc);          hasChanges = true; break;
                case 10: playersManager.updatePlayer(sc);          hasChanges = true; break;
                case 11: playersManager.listPlayersByPosition(sc); break;
                case 12: dataSaver.saveData();                     hasChanges = false; break;
                case 13: if (dataLoader.reloadData()) hasChanges = false; break;
                case 14: running = quitHandler.quit(hasChanges);   break;
                default: System.out.println("Invalid choice! Please enter 1-14.");
            }
        }
        sc.close();
    }

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

    // Helper 
    private int parseChoice(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}