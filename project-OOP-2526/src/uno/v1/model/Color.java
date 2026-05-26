package uno.v1.model;

/**
 * Represents the color of an UNO card.
 */
public enum Color {
    RED("R", "RED"),
    YELLOW("Y", "YELLOW"),
    GREEN("G", "GREEN"),
    BLUE("B", "BLUE"),
    WILD("W", "WILD");

    private final String code;
    private final String fullName;

    Color(String code, String fullName) {
        this.code = code;
        this.fullName = fullName;
    }

    /** @return single-letter code used in files */
    public String getCode() { return code; }

    /** @return full name used in event log */
    public String getFullName() { return fullName; }

    /**
     * Parse a color from its single-letter code.
     * @param code R, Y, G, B, or W
     * @return corresponding Color
     * @throws IllegalArgumentException if code is unknown
     */
    public static Color fromCode(String code) {
        for (Color c : values()) {
            if (c.code.equals(code)) return c;
        }
        throw new IllegalArgumentException("Unknown color code: " + code);
    }

    /**
     * Parse a playable color (R, Y, G, B) from a COLOR command argument.
     * @param code color code
     * @return corresponding Color (not WILD)
     * @throws IllegalArgumentException if code is invalid or WILD
     */
    public static Color fromColorCommand(String code) {
        Color c = fromCode(code);
        if (c == WILD) throw new IllegalArgumentException("Cannot choose WILD as a color");
        return c;
    }
}
