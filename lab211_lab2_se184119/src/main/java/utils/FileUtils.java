package utils;

import model.Club;
import model.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for all file I/O operations.
 * Handles reading clubs.txt and players.txt with strict validation.
 */
public class FileUtils {

    /**
     * Loads clubs from clubs.txt.
     * Returns null if any invalid line is found.
     */
    public static List<Club> loadClubs(String filename) {
        List<Club> clubs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 4) return null;

                String id     = parts[0].trim();
                String name   = parts[1].trim();
                String brand  = parts[2].trim();
                String budgetStr = parts[3].trim();

                if (!ValidationUtils.isValidClubId(id))    return null;
                if (!ValidationUtils.isNotEmpty(name))     return null;
                if (!ValidationUtils.isNotEmpty(brand))    return null;

                double budget = ValidationUtils.parseDouble(budgetStr);
                if (!ValidationUtils.isValidBudget(budget))  return null;

                clubs.add(new Club(id, name, brand, budget));
            }
        } catch (IOException e) {
            return null;
        }
        return clubs;
    }

    /**
     * Loads players from players.txt.
     * Returns null if any invalid line is found.
     * Requires a club list for FK validation.
     */
    public static List<Player> loadPlayers(String filename, List<Club> clubs) {
        List<Player> players = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 5) return null;

                String id       = parts[0].trim();
                String clubId   = parts[1].trim();
                String name     = parts[2].trim();
                String position = parts[3].trim();
                String shirtStr = parts[4].trim();

                if (!ValidationUtils.isValidPlayerId(id)) return null;
                if (!clubExists(clubId, clubs))           return null;
                if (!ValidationUtils.isNotEmpty(name))   return null;
                if (!ValidationUtils.isValidPosition(position)) return null;

                int shirt = ValidationUtils.parseInt(shirtStr);
                if (!ValidationUtils.isValidShirtNumber(shirt)) return null;

                players.add(new Player(id, clubId, name,
                        ValidationUtils.normalizePosition(position), shirt));
            }
        } catch (IOException e) {
            return null;
        }
        return players;
    }

    /**
     * Saves clubs to clubs.txt (UTF-8).
     */
    public static boolean saveClubs(String filename, List<Club> clubs) {
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"))) {
            for (Club c : clubs) {
                pw.println(c.toFileString());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Saves players to players.txt (UTF-8).
     */
    public static boolean savePlayers(String filename, List<Player> players) {
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"))) {
            for (Player p : players) {
                pw.println(p.toFileString());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // ── Helper ─────────────────────────────────────────────────────────────
    private static boolean clubExists(String clubId, List<Club> clubs) {
        for (Club c : clubs) {
            if (c.getId().equals(clubId)) return true;
        }
        return false;
    }
}
