package uno.v1.io;

/**
 * Represents a single parsed command from the script file.
 * A command is one of: PLAY index, DRAW, or COLOR colorCode.
 */
public class Command {

    /** The type of command. */
    public enum Type { PLAY, DRAW, COLOR }

    private final int playerId;
    private final Type type;
    private final int cardIndex;   // used only for PLAY
    private final String colorCode; // used only for COLOR
    private final String rawLine;  // original line as read from file

    /** Private constructor; use static factory methods. */
    private Command(int playerId, Type type, int cardIndex,
                    String colorCode, String rawLine) {
        this.playerId  = playerId;
        this.type      = type;
        this.cardIndex = cardIndex;
        this.colorCode = colorCode;
        this.rawLine   = rawLine;
    }

    /** Factory: PLAY command */
    public static Command play(int playerId, int cardIndex, String rawLine) {
        return new Command(playerId, Type.PLAY, cardIndex, null, rawLine);
    }

    /** Factory: DRAW command */
    public static Command draw(int playerId, String rawLine) {
        return new Command(playerId, Type.DRAW, -1, null, rawLine);
    }

    /** Factory: COLOR command */
    public static Command color(int playerId, String colorCode, String rawLine) {
        return new Command(playerId, Type.COLOR, -1, colorCode, rawLine);
    }

    /** @return the player id this command belongs to */
    public int getPlayerId() { return playerId; }

    /** @return the type of this command */
    public Type getType() { return type; }

    /** @return card index (only valid for PLAY commands) */
    public int getCardIndex() { return cardIndex; }

    /** @return color code string (only valid for COLOR commands) */
    public String getColorCode() { return colorCode; }

    /** @return the original raw script line */
    public String getRawLine() { return rawLine; }
}
