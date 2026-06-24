package utils;

import model.Club;
import model.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class FileUtils {

    private FileUtils() {}

    // ── Read ──────────────────────────────────────────────────────────────────

    /**
     * Load clubs from file.
     * Returns null and prints an error if any line is invalid.
     */
    public static List<Club> loadClubs(String filePath) {
        List<Club> clubs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", 4);
                if (parts.length != 4) {
                    System.out.println("Load data failed!");
                    return null;
                }

                String id     = parts[0].trim();
                String name   = parts[1].trim();
                String brand  = parts[2].trim();
                double budget = ValidationUtils.parseDouble(parts[3].trim());

                // Strict validation
                if (!ValidationUtils.isValidClubId(id)
                        || !ValidationUtils.isNonEmpty(name)
                        || !ValidationUtils.isNonEmpty(brand)
                        || !ValidationUtils.isValidBudget(budget)) {
                    System.out.println("Load data failed!");
                    return null;
                }

                clubs.add(new Club(id, name, brand, budget));
            }
        } catch (IOException e) {
            System.out.println("Cannot read clubs file: " + e.getMessage());
            return null;
        }
        return clubs;
    }

    /**
     * Load players from file; requires the already-loaded club list for FK validation.
     * Returns null and prints an error if any line is invalid.
     */
    public static List<Player> loadPlayers(String filePath, List<Club> clubs) {
        List<Player> players = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", 5);
                if (parts.length != 5) {
                    System.out.println("Load data failed!");
                    return null;
                }

                String pid      = parts[0].trim();
                String cid      = parts[1].trim();
                String name     = parts[2].trim();
                String position = parts[3].trim();
                int    shirt    = ValidationUtils.parseInt(parts[4].trim());

                // Strict validation
                boolean clubExists = clubs.stream()
                        .anyMatch(c -> c.getClubId().equalsIgnoreCase(cid));

                String normPos = ValidationUtils.normalisePosition(position);

                if (!ValidationUtils.isValidPlayerId(pid)
                        || !clubExists
                        || !ValidationUtils.isNonEmpty(name)
                        || normPos == null
                        || !ValidationUtils.isValidShirtNumber(shirt)) {
                    System.out.println("Load data failed!");
                    return null;
                }

                players.add(new Player(pid, cid, name, normPos, shirt));
            }
        } catch (IOException e) {
            System.out.println("Cannot read players file: " + e.getMessage());
            return null;
        }
        return players;
    }

    // ── Write ─────────────────────────────────────────────────────────────────

    public static boolean saveClubs(String filePath, List<Club> clubs) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (Club c : clubs) {
                pw.println(c.toFileString());
            }
            return true;
        } catch (IOException e) {
            System.out.println("Cannot save clubs file: " + e.getMessage());
            return false;
        }
    }

    public static boolean savePlayers(String filePath, List<Player> players) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (Player p : players) {
                pw.println(p.toFileString());
            }
            return true;
        } catch (IOException e) {
            System.out.println("Cannot save players file: " + e.getMessage());
            return false;
        }
    }
}
