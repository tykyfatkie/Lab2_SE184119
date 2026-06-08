package ui;

import manager.ClubsManager;
import manager.PlayersManager;

/**
 * Handles saving data from managers to files.
 */
public class DataSaver {

    private final ClubsManager   clubsManager;
    private final PlayersManager playersManager;

    private static final String CLUBS_FILE   = "clubs.txt";
    private static final String PLAYERS_FILE = "players.txt";

    public DataSaver(ClubsManager clubsManager, PlayersManager playersManager) {
        this.clubsManager   = clubsManager;
        this.playersManager = playersManager;
    }

    /**
     * Feature 12: Save all data to files.
     */
    public void saveData() {
        System.out.println("\n=== SAVE DATA TO FILES ===");
        clubsManager.saveToFile(CLUBS_FILE);
        playersManager.saveToFile(PLAYERS_FILE);
    }
}