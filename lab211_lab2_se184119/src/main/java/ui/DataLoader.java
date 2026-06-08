package ui;

import manager.ClubsManager;
import manager.PlayersManager;
import model.Club;
import model.Player;
import utils.FileUtils;

import java.util.List;

/**
 * Handles loading data from files into the managers.
 */
public class DataLoader {

    private final ClubsManager   clubsManager;
    private final PlayersManager playersManager;

    private static final String CLUBS_FILE   = "clubs.txt";
    private static final String PLAYERS_FILE = "players.txt";

    public DataLoader(ClubsManager clubsManager, PlayersManager playersManager) {
        this.clubsManager   = clubsManager;
        this.playersManager = playersManager;
    }

    /**
     * Auto-load on startup. Returns true if successful.
     */
    public boolean autoLoad() {
        System.out.println("Loading data from files...");

        List<Club> loadedClubs = FileUtils.loadClubs(CLUBS_FILE);
        if (loadedClubs == null) {
            System.out.println("Load data failed!");
            return false;
        }
        clubsManager.setClubs(loadedClubs);

        List<Player> loadedPlayers = FileUtils.loadPlayers(PLAYERS_FILE, loadedClubs);
        if (loadedPlayers == null) {
            System.out.println("Load data failed!");
            return false;
        }
        playersManager.setPlayers(loadedPlayers);

        System.out.println("Load data successfully!");
        return true;
    }

    /**
     * Feature 13: Reload from files (manual trigger from menu).
     * Returns true if successful.
     */
    public boolean reloadData() {
        System.out.println("\n=== LOAD DATA FROM FILES ===");

        clubsManager.getClubs().clear();
        playersManager.getPlayers().clear();

        clubsManager.loadFromFile(CLUBS_FILE);

        if (clubsManager.getClubs().isEmpty()) {
            return false;
        }

        playersManager.loadFromFile(PLAYERS_FILE);

        boolean playersOk = !playersManager.getPlayers().isEmpty()
                || FileUtils.loadPlayers(PLAYERS_FILE, clubsManager.getClubs()) != null;

        if (playersOk) {
            System.out.println("Load data successfully!");
        }

        return playersOk;
    }
}