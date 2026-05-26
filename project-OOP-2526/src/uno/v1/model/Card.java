package uno.v1.model;

/**
 * Represents a single UNO card with a color and a rank.
 * Immutable value object.
 */
public class Card {

    private final Color color;
    private final Rank rank;

    /**
     * Constructs a card with the given color and rank.
     * @param color card color
     * @param rank  card rank
     */
    public Card(Color color, Rank rank) {
        this.color = color;
        this.rank = rank;
    }

    /** @return this card's color */
    public Color getColor() { return color; }

    /** @return this card's rank */
    public Rank getRank() { return rank; }

    /**
     * Returns the textual code for this card as used in files and event log,
     * e.g. R-5, W-WILD, B-DRAW_TWO.
     * @return card code string
     */
    public String getCode() {
        return color.getCode() + "-" + rank.getCode();
    }

    /**
     * Checks whether this card is playable on top of the given top card
     * given the current active color.
     * A card is playable if:
     * - its color matches the current color, or
     * - its rank matches the top card's rank, or
     * - it is a wild card.
     *
     * @param topCard      the current top card of the discard pile
     * @param currentColor the currently active color
     * @return true if playable
     */
    public boolean isPlayable(Card topCard, Color currentColor) {
        if (rank.isWild()) return true;
        if (color == currentColor) return true;
        if (rank == topCard.getRank()) return true;
        return false;
    }

    /**
     * Parses a card from its code string (e.g. "R-5", "W-WILD_DRAW_FOUR").
     * @param code card code string
     * @return parsed Card
     * @throws IllegalArgumentException if the code is invalid
     */
    public static Card fromCode(String code) {
        int dash = code.indexOf('-');
        if (dash < 0) throw new IllegalArgumentException("Invalid card code: " + code);
        String colorPart = code.substring(0, dash);
        String rankPart  = code.substring(dash + 1);
        Color c = Color.fromCode(colorPart);
        Rank  r = Rank.fromCode(rankPart);
        return new Card(c, r);
    }

    @Override
    public String toString() {
        return getCode();
    }
}
