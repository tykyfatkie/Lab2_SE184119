package utils;

/**
 * Utility class for all input validation.
 * Demonstrates Pattern Recognition via reusable validate methods.
 */
public class ValidationUtils {

    // Valid positions (case-insensitive)
    public static final String[] VALID_POSITIONS = {
        "Goalkeeper", "Defender", "Midfielder", "Forward", "Winger"
    };

    /**
     * Validates Club ID format: CL-xxxx (e.g., CL-0001)
     */
    public static boolean isValidClubId(String id) {
        if (id == null) return false;
        return id.matches("CL-\\d{4}");
    }

    /**
     * Validates Player ID format: Pxxxx (e.g., P0001)
     */
    public static boolean isValidPlayerId(String id) {
        if (id == null) return false;
        return id.matches("P\\d{4}");
    }

    /**
     * Checks that a string is not null or blank.
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates that budget is a positive number.
     */
    public static boolean isValidBudget(double budget) {
        return budget > 0;
    }

    /**
     * Validates shirt number: integer between 1 and 99.
     */
    public static boolean isValidShirtNumber(int number) {
        return number >= 1 && number <= 99;
    }

    /**
     * Validates position is one of the allowed values (case-insensitive).
     */
    public static boolean isValidPosition(String position) {
        if (position == null) return false;
        for (String valid : VALID_POSITIONS) {
            if (valid.equalsIgnoreCase(position.trim())) return true;
        }
        return false;
    }

    /**
     * Normalizes a position string to its canonical capitalized form.
     */
    public static String normalizePosition(String position) {
        if (position == null) return null;
        for (String valid : VALID_POSITIONS) {
            if (valid.equalsIgnoreCase(position.trim())) return valid;
        }
        return position;
    }

    /**
     * Tries to parse a double; returns -1 if it fails.
     */
    public static double parseDouble(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Tries to parse an int; returns -1 if it fails.
     */
    public static int parseInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
