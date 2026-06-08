package manager;

import model.Club;
import utils.FileUtils;
import utils.Manageable;
import utils.Saveable;
import utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ClubsManager implements Manageable<Club>, Saveable {

    private List<Club> clubs;
    private static final String CLUBS_FILE = "clubs.txt";

    // Table separator line
    private static final String TABLE_SEP =
        "+----------+--------------------------------+------------+------------+";
    private static final String TABLE_HEADER =
        "| Club ID  | Club Name                      | Sponsor    |  Budget(M) |";

    public ClubsManager() {
        clubs = new ArrayList<>();
    }

    public List<Club> getClubs() { return clubs; }
    public void setClubs(List<Club> clubs) { this.clubs = clubs; }

    // Interface: Manageable

    @Override
    public void add(Club club) {
        clubs.add(club);
    }

    @Override
    public Club findById(String id) {
        for (Club c : clubs) {
            if (c.getId().equalsIgnoreCase(id)) return c;
        }
        return null;
    }

    @Override
    public boolean removeById(String id) {
        return clubs.removeIf(c -> c.getId().equalsIgnoreCase(id));
    }

    @Override
    public void displayAll() {
        if (clubs.isEmpty()) {
            System.out.println("No clubs found.");
            return;
        }
        printTableHeader();
        for (Club c : clubs) {
            System.out.println(c.toTableRow());
            System.out.println(TABLE_SEP);
        }
    }

    // Interface: Saveable

    @Override
    public void saveToFile(String filename) {
        if (FileUtils.saveClubs(filename, clubs)) {
            System.out.println("Clubs saved to " + filename + " successfully.");
        } else {
            System.out.println("Error: Failed to save clubs.");
        }
    }

    @Override
    public void loadFromFile(String filename) {
        List<Club> loaded = FileUtils.loadClubs(filename);
        if (loaded == null) {
            System.out.println("Load data failed!");
        } else {
            clubs = loaded;
        }
    }

    // Feature 1: List all clubs 
    public void listAllClubs() {
        System.out.println("\n=== LIST OF ALL CLUBS ===");
        displayAll();
    }

    // Feature 2: Add a new club
    public void addClub(Scanner sc) {
        System.out.println("\n=== ADD NEW CLUB ===");

        // Club ID
        String id;
        while (true) {
            System.out.print("Enter Club ID (format CL-xxxx): ");
            id = sc.nextLine().trim();
            if (!ValidationUtils.isValidClubId(id)) {
                System.out.println("Invalid Club ID format! Must be CL-xxxx (e.g., CL-0001).");
                continue;
            }
            if (findById(id) != null) {
                System.out.println("This club ID already exists!");
                return;
            }
            break;
        }

        // Club name
        String name;
        while (true) {
            System.out.print("Enter Club Name: ");
            name = sc.nextLine().trim();
            if (!ValidationUtils.isNotEmpty(name)) {
                System.out.println("Club name cannot be empty!");
            } else break;
        }

        // Sponsor brand
        String brand;
        while (true) {
            System.out.print("Enter Sponsor Brand: ");
            brand = sc.nextLine().trim();
            if (!ValidationUtils.isNotEmpty(brand)) {
                System.out.println("Sponsor brand cannot be empty!");
            } else break;
        }

        // Budget
        double budget;
        while (true) {
            System.out.print("Enter Budget (million EUR): ");
            budget = ValidationUtils.parseDouble(sc.nextLine());
            if (!ValidationUtils.isValidBudget(budget)) {
                System.out.println("Budget must be a positive number!");
            } else break;
        }

        add(new Club(id, name, brand, budget));
        System.out.println("Club added successfully!");
    }

    // Feature 3: Search club by ID 
    public void searchClubById(Scanner sc) {
        System.out.println("\n=== SEARCH CLUB BY ID ===");
        System.out.print("Enter Club ID: ");
        String id = sc.nextLine().trim();
        Club club = findById(id);
        if (club == null) {
            System.out.println("This club does not exist!");
        } else {
            printTableHeader();
            System.out.println(club.toTableRow());
            System.out.println(TABLE_SEP);
        }
    }

    // Feature 4: Update club by ID 
    public void updateClub(Scanner sc) {
        System.out.println("\n=== UPDATE CLUB ===");
        System.out.print("Enter Club ID to update: ");
        String id = sc.nextLine().trim();
        Club club = findById(id);
        if (club == null) {
            System.out.println("This club does not exist!");
            return;
        }
        System.out.println("Leave blank to keep current value.");

        // Club name
        System.out.print("Enter new Club Name [" + club.getClubName() + "]: ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) club.setClubName(name);

        // Sponsor brand
        System.out.print("Enter new Sponsor Brand [" + club.getSponsorBrand() + "]: ");
        String brand = sc.nextLine().trim();
        if (!brand.isEmpty()) club.setSponsorBrand(brand);

        // Budget
        System.out.print("Enter new Budget [" + club.getBudget() + "]: ");
        String budgetStr = sc.nextLine().trim();
        if (!budgetStr.isEmpty()) {
            double budget = ValidationUtils.parseDouble(budgetStr);
            if (!ValidationUtils.isValidBudget(budget)) {
                System.out.println("Invalid budget. Budget not updated.");
            } else {
                club.setBudget(budget);
            }
        }
        System.out.println("Club updated successfully!");
    }

    // Feature 5: List clubs with budget <= input
    public void listClubsByBudget(Scanner sc) {
        System.out.println("\n=== CLUBS WITH BUDGET ≤ INPUT ===");
        double maxBudget;
        while (true) {
            System.out.print("Enter maximum budget (million EUR): ");
            maxBudget = ValidationUtils.parseDouble(sc.nextLine());
            if (maxBudget < 0) {
                System.out.println("Please enter a valid non-negative number.");
            } else break;
        }

        boolean found = false;
        printTableHeader();
        for (Club c : clubs) {
            if (c.getBudget() <= maxBudget) {
                System.out.println(c.toTableRow());
                System.out.println(TABLE_SEP);
                found = true;
            }
        }
        if (!found) System.out.println("No clubs found with budget ≤ " + maxBudget + "M.");
    }

    // Helpers
    private void printTableHeader() {
        System.out.println(TABLE_SEP);
        System.out.println(TABLE_HEADER);
        System.out.println(TABLE_SEP);
    }
}
