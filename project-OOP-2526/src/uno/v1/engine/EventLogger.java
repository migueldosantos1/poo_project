package uno.v1.engine;

import uno.v1.model.Card;
import uno.v1.model.Color;
import uno.v1.model.Player;

import java.util.List;

/**
 * Responsible for printing the structured event log to standard output.
 *
 * <p>All output lines follow the specification in Section 1.4 of the project description.
 * Only this class writes to stdout, keeping output formatting centralised.
 */
public class EventLogger {

    // ---- Startup header ----------------------------------------------------

    /**
     * Prints the startup header before any events.
     *
     * @param mainClass      fully qualified main class name
     * @param deckFile       deck file path
     * @param scriptFile     script file path
     * @param playerCount    number of players
     * @param cardsPerPlayer cards dealt per player
     */
    public void printStartupHeader(String mainClass, String deckFile,
                                   String scriptFile, int playerCount,
                                   int cardsPerPlayer) {
        System.out.println("Running " + mainClass + " with:");
        System.out.println("   DeckFile: " + deckFile);
        System.out.println("   Script file: " + scriptFile);
        System.out.println("   Nb players: " + playerCount);
        System.out.println("   Nb cards per player: " + cardsPerPlayer);
        System.out.println();
    }

    // ---- Game start --------------------------------------------------------

    /**
     * Prints GAME_START and initial state events.
     *
     * @param playerCount  number of players
     * @param topCard      initial top card of discard pile
     * @param topColor     initial active color
     * @param players      ordered list of players with their dealt hands
     * @param firstPlayer  index of the first player (always 0 in Phase 1)
     */
    public void printGameStart(int playerCount, Card topCard, Color topColor,
                               List<Player> players, int firstPlayer) {
        System.out.println("GAME_START players=" + playerCount);
        System.out.println("EVENT TOP_CARD " + topCard.getCode()
                + " color=" + topColor.getFullName());
        for (Player p : players) {
            StringBuilder sb = new StringBuilder("EVENT HAND player=");
            sb.append(p.getId()).append(" cards=");
            List<Card> hand = p.getHand();
            for (int i = 0; i < hand.size(); i++) {
                if (i > 0) sb.append(' ');
                sb.append(hand.get(i).getCode());
            }
            System.out.println(sb.toString());
        }
        System.out.println("EVENT TURN_START player=" + firstPlayer);
    }

    // ---- Command logging ---------------------------------------------------

    /**
     * Logs a command line as read from the script file.
     * @param rawLine original script line
     */
    public void printCommand(String rawLine) {
        System.out.println("EVENT COMMAND line=\"" + rawLine + "\"");
    }

    // ---- Action events -----------------------------------------------------

    /**
     * Prints a PLAY_CARD event for a normal (non-special) card.
     * @param playerId player who played
     * @param card     card played
     */
    public void printPlayCard(int playerId, Card card) {
        System.out.println("EVENT PLAY_CARD Player " + playerId
                + " played " + card.getCode());
    }

    /**
     * Prints a PLAY_CARD event for a SKIP card.
     * @param playerId player who played
     */
    public void printPlaySkip(int playerId) {
        System.out.println("EVENT PLAY_CARD Player " + playerId + " played SKIP");
    }

    /**
     * Prints a PLAY_CARD event for a REVERSE card.
     * @param playerId player who played
     */
    public void printPlayReverse(int playerId) {
        System.out.println("EVENT PLAY_CARD Player " + playerId + " played REVERSE");
    }

    /**
     * Prints a PLAY_CARD event for a DRAW_TWO card (includes forced draw message).
     * @param playerId      player who played
     * @param affectedId    player who must draw
     */
    public void printPlayDrawTwo(int playerId, int affectedId) {
        System.out.println("EVENT PLAY_CARD Player " + playerId
                + " played DRAW_TWO; Player " + affectedId
                + " draws 2 and is skipped");
    }

    /**
     * Prints a PLAY_CARD event for a WILD card.
     * @param playerId player who played
     */
    public void printPlayWild(int playerId) {
        System.out.println("EVENT PLAY_CARD Player " + playerId
                + " played WILD (color will be chosen)");
    }

    /**
     * Prints a PLAY_CARD event for a WILD_DRAW_FOUR card.
     * @param playerId   player who played
     * @param affectedId player who must draw 4
     */
    public void printPlayWildDrawFour(int playerId, int affectedId) {
        System.out.println("EVENT PLAY_CARD Player " + playerId
                + " played WILD_DRAW_FOUR; Player " + affectedId
                + " draws 4 and is skipped");
    }

    /**
     * Prints a DRAW_CARD event when a player draws voluntarily.
     * @param playerId player who drew
     * @param card     the card drawn
     */
    public void printDrawCard(int playerId, Card card) {
        System.out.println("EVENT DRAW_CARD Player " + playerId
                + " draws 1 card (" + card.getCode() + ")");
    }

    /**
     * Prints a CHOOSE_COLOR event.
     * @param playerId player who chose
     * @param color    chosen color
     */
    public void printChooseColor(int playerId, Color color) {
        System.out.println("EVENT CHOOSE_COLOR Player " + playerId
                + " chose color " + color.getFullName());
    }

    /**
     * Prints a TURN_ADVANCE event.
     * @param nextPlayerId id of the player whose turn is next
     */
    public void printTurnAdvance(int nextPlayerId) {
        System.out.println("EVENT TURN_ADVANCE Next player: " + nextPlayerId);
    }

    // ---- Error events ------------------------------------------------------

    /**
     * Prints an error event with the given description.
     * @param description error description
     */
    public void printError(String description) {
        System.out.println("EVENT ERROR " + description);
    }

    // ---- Game end ----------------------------------------------------------

    /**
     * Prints game-end events when a player wins.
     * @param winnerId id of the winning player
     */
    public void printGameEndWinner(int winnerId) {
        System.out.println("EVENT GAME_END Player " + winnerId + " wins");
        System.out.println("EVENT WINNER player=" + winnerId);
        System.out.println("GAME_END");
    }

    /**
     * Prints game-end events when the draw pile is exhausted.
     */
    public void printGameEndNoCards() {
        System.out.println("EVENT GAME_END No cards available to draw");
        System.out.println("GAME_END");
    }

    /**
     * Prints game-end when the script is exhausted without a winner.
     */
    public void printGameEndNoWinner() {
        System.out.println("GAME_END");
    }
}
