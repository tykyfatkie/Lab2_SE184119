package football.utils;

import football.model.Player;


public class ValidationUtils {

    private ValidationUtils() {} // utility class – no instantiation

    // ── Club validations ──────────────────────────────────────────────────────

    /**
     * Club ID format: CL-xxxx  (2 uppercase letters, dash, 4 digits)
     */
    public static boolean isValidClubId(String id) {
        if (id == null) return false;
        return id.matches("CL-\\d{4}");
    }

    /**
     * Non-null and non-blank string.
     */
    public static boolean isNonEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Budget must be a positive number.
     */
    public static boolean isValidBudget(double budget) {
        return budget > 0;
    }

    // ── Player validations ────────────────────────────────────────────────────

    /**
     * Player ID format: Pxxxx  (letter P followed by exactly 4 digits)
     */
    public static boolean isValidPlayerId(String id) {
        if (id == null) return false;
        return id.matches("P\\d{4}");
    }

    /**
     * Position must be one of the five accepted values (case-insensitive).
     */
    public static boolean isValidPosition(String position) {
        if (position == null) return false;
        for (String valid : Player.VALID_POSITIONS) {
            if (valid.equalsIgnoreCase(position.trim())) return true;
        }
        return false;
    }

    /**
     * Normalises a raw position string to the canonical capitalised form.
     * Returns null if invalid.
     */
    public static String normalisePosition(String raw) {
        if (raw == null) return null;
        for (String valid : Player.VALID_POSITIONS) {
            if (valid.equalsIgnoreCase(raw.trim())) return valid;
        }
        return null;
    }

    /**
     * Shirt number must be an integer in [1, 99].
     */
    public static boolean isValidShirtNumber(int number) {
        return number >= 1 && number <= 99;
    }

    // ── Parse helpers ─────────────────────────────────────────────────────────

    /**
     * Safely parse a String to double; returns -1 on failure.
     */
    public static double parseDouble(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Safely parse a String to int; returns -1 on failure.
     */
    public static int parseInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
