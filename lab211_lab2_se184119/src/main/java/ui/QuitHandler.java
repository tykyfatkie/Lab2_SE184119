package ui;

import manager.ClubsManager;
import manager.PlayersManager;

/**
 * Handles application exit, auto-saving unsaved changes before quit.
 */
public class QuitHandler {

    private final ClubsManager   clubsManager;
    private final PlayersManager playersManager;

    private static final String CLUBS_FILE   = "clubs.txt";
    private static final String PLAYERS_FILE = "players.txt";

    public QuitHandler(ClubsManager clubsManager, PlayersManager playersManager) {
        this.clubsManager   = clubsManager;
        this.playersManager = playersManager;
    }

    /**
     * Feature 14: Quit the application.
     * Auto-saves if there are unsaved changes.
     *
     * @param hasChanges whether unsaved changes exist
     * @return false to signal the main loop to stop
     */
    public boolean quit(boolean hasChanges) {
        if (hasChanges) {
            System.out.println("\nUnsaved changes detected. Saving before exit...");
            clubsManager.saveToFile(CLUBS_FILE);
            playersManager.saveToFile(PLAYERS_FILE);
        }
        System.out.println("Goodbye!");
        return false;
    }
}