package uno.v1.model;

/**
 * Represents the rank (type) of an UNO card.
 */
public enum Rank {
    ZERO("0"), ONE("1"), TWO("2"), THREE("3"), FOUR("4"),
    FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"),
    SKIP("SKIP"), REVERSE("REVERSE"), DRAW_TWO("DRAW_TWO"),
    WILD("WILD"), WILD_DRAW_FOUR("WILD_DRAW_FOUR");

    private final String code;

    Rank(String code) { this.code = code; }

    /** @return rank code as appears in deck file */
    public String getCode() { return code; }

    /** @return true if this rank is a number (0-9) */
    public boolean isNumber() {
        return this == ZERO || this == ONE || this == TWO || this == THREE ||
               this == FOUR || this == FIVE || this == SIX || this == SEVEN ||
               this == EIGHT || this == NINE;
    }

    /** @return true if this rank requires a wild color choice */
    public boolean isWild() {
        return this == WILD || this == WILD_DRAW_FOUR;
    }

    /** @return true if this is an action card (SKIP, REVERSE, DRAW_TWO, or wild) */
    public boolean isAction() {
        return this == SKIP || this == REVERSE || this == DRAW_TWO || isWild();
    }

    /**
     * Parse rank from its string code.
     * @param code rank code string
     * @return corresponding Rank
     * @throws IllegalArgumentException if code is unknown
     */
    public static Rank fromCode(String code) {
        for (Rank r : values()) {
            if (r.code.equals(code)) return r;
        }
        throw new IllegalArgumentException("Unknown rank code: " + code);
    }
}
