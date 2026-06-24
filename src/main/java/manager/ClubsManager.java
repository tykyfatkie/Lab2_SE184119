package manager;

import model.Club;
import utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ClubsManager {

    private List<Club> clubs;
    private boolean    modified; // track unsaved changes

    public ClubsManager() {
        this.clubs    = new ArrayList<>();
        this.modified = false;
    }

    // ── Data access ───────────────────────────────────────────────────────────

    public List<Club> getClubs()          { return clubs; }
    public void       setClubs(List<Club> clubs) { this.clubs = clubs; }
    public boolean    isModified()        { return modified; }
    public void       setModified(boolean m)     { this.modified = m; }

    // ── Helper ────────────────────────────────────────────────────────────────

    /** Find a club by ID (case-insensitive). */
    public Club findById(String id) {
        for (Club c : clubs) {
            if (c.getClubId().equalsIgnoreCase(id)) return c;
        }
        return null;
    }

    /** Check if club ID already exists. */
    public boolean clubExists(String id) {
        return findById(id) != null;
    }

    // ── Feature 1: List all clubs ─────────────────────────────────────────────

    public void listAllClubs() {
        if (clubs.isEmpty()) {
            System.out.println("No clubs available.");
            return;
        }
        printClubHeader();
        for (Club c : clubs) {
            System.out.println(c);
        }
        printDivider();
        System.out.println("Total: " + clubs.size() + " club(s).");
    }

    // ── Feature 2: Add a new club ─────────────────────────────────────────────

    public void addClub(Scanner sc) {
        System.out.println("\n=== Add a New Club ===");

        // Step 1 – Club ID
        String id;
        while (true) {
            System.out.print("Enter Club ID (format CL-xxxx): ");
            id = sc.nextLine().trim();
            if (!ValidationUtils.isValidClubId(id)) {
                System.out.println("Invalid Club ID format! Must be CL-xxxx.");
                continue;
            }
            if (clubExists(id)) {
                System.out.println("This club ID already exists!");
                return;
            }
            break;
        }

        // Step 2 – Club Name
        String name;
        while (true) {
            System.out.print("Enter Club Name: ");
            name = sc.nextLine().trim();
            if (!ValidationUtils.isNonEmpty(name)) {
                System.out.println("Club name cannot be empty!");
            } else break;
        }

        // Step 3 – Sponsor Brand
        String brand;
        while (true) {
            System.out.print("Enter Sponsor Brand: ");
            brand = sc.nextLine().trim();
            if (!ValidationUtils.isNonEmpty(brand)) {
                System.out.println("Sponsor brand cannot be empty!");
            } else break;
        }

        // Step 4 – Budget
        double budget;
        while (true) {
            System.out.print("Enter Budget (million EUR, positive number): ");
            budget = ValidationUtils.parseDouble(sc.nextLine());
            if (!ValidationUtils.isValidBudget(budget)) {
                System.out.println("Budget must be a positive number!");
            } else break;
        }

        // Step 5 – Add
        clubs.add(new Club(id, name, brand, budget));
        modified = true;
        System.out.println("Club added successfully!");
    }

    // ── Feature 3: Search club by ID ──────────────────────────────────────────

    public void searchClubById(Scanner sc) {
        System.out.print("\nEnter Club ID to search: ");
        String id = sc.nextLine().trim();
        Club c = findById(id);
        if (c == null) {
            System.out.println("This club does not exist!");
        } else {
            printClubHeader();
            System.out.println(c);
            printDivider();
        }
    }

    // ── Feature 4: Update a club ──────────────────────────────────────────────

    public void updateClub(Scanner sc) {
        System.out.print("\nEnter Club ID to update: ");
        String id = sc.nextLine().trim();
        Club c = findById(id);
        if (c == null) {
            System.out.println("This club does not exist!");
            return;
        }

        System.out.println("Current info: " + c);
        System.out.println("(Press Enter to skip a field)");

        // Update name
        System.out.print("New Club Name [" + c.getClubName() + "]: ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) c.setClubName(name);

        // Update sponsor brand
        System.out.print("New Sponsor Brand [" + c.getSponsorBrand() + "]: ");
        String brand = sc.nextLine().trim();
        if (!brand.isEmpty()) c.setSponsorBrand(brand);

        // Update budget
        System.out.print("New Budget [" + c.getBudget() + "]: ");
        String budgetStr = sc.nextLine().trim();
        if (!budgetStr.isEmpty()) {
            double budget = ValidationUtils.parseDouble(budgetStr);
            if (!ValidationUtils.isValidBudget(budget)) {
                System.out.println("Invalid budget – update skipped.");
            } else {
                c.setBudget(budget);
            }
        }

        modified = true;
        System.out.println("Club updated successfully!");
    }

    // ── Feature 5: List clubs with budget ≤ input ────────────────────────────

    public void listClubsByBudget(Scanner sc) {
        System.out.print("\nEnter maximum budget (million EUR): ");
        double maxBudget = ValidationUtils.parseDouble(sc.nextLine());
        if (maxBudget < 0) {
            System.out.println("Invalid budget value!");
            return;
        }

        List<Club> result = new ArrayList<>();
        for (Club c : clubs) {
            if (c.getBudget() <= maxBudget) result.add(c);
        }

        if (result.isEmpty()) {
            System.out.println("No clubs found with budget ≤ " + maxBudget + " M EUR.");
        } else {
            printClubHeader();
            for (Club c : result) System.out.println(c);
            printDivider();
            System.out.println("Found: " + result.size() + " club(s).");
        }
    }

    // ── Print helpers ─────────────────────────────────────────────────────────

    private void printClubHeader() {
        printDivider();
        System.out.printf("%-10s | %-30s | %-10s | %s%n",
                "Club ID", "Club Name", "Sponsor", "Budget (M EUR)");
        printDivider();
    }

    private void printDivider() {
        System.out.println(new String(new char[72]).replace('\0', '-'));
    }
}
