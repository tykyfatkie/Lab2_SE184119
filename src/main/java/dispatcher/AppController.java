package dispatcher;

import manager.ClubsManager;
import manager.PlayersManager;
import model.Club;
import model.Player;
import utils.FileUtils;

import java.util.List;


public class AppController {

    private static final String CLUBS_FILE   = "data/clubs.txt";
    private static final String PLAYERS_FILE = "data/players.txt";

    private final ClubsManager   clubsManager;
    private final PlayersManager playersManager;

    public AppController(ClubsManager clubsManager, PlayersManager playersManager) {
        this.clubsManager   = clubsManager;
        this.playersManager = playersManager;
    }

    // ── Feature 12: Save data ─────────────────────────────────────────────────

    public void saveData() {
        boolean ok1 = FileUtils.saveClubs(CLUBS_FILE, clubsManager.getClubs());
        boolean ok2 = FileUtils.savePlayers(PLAYERS_FILE, playersManager.getPlayers());
        if (ok1 && ok2) {
            clubsManager.setModified(false);
            playersManager.setModified(false);
            System.out.println("Data saved successfully!");
        }
    }

    // ── Feature 13: Load data ─────────────────────────────────────────────────

    public void loadData(boolean interactive) {
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

    /** Returns false to signal the Menu loop to stop. */
    public boolean quit() {
        if (clubsManager.isModified() || playersManager.isModified()) {
            System.out.println("Unsaved changes detected. Saving before exit...");
            saveData();
        }
        System.out.println("Goodbye!");
        return false;
    }
}