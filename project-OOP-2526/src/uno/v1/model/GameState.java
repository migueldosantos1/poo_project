package uno.v1.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the full mutable state of an UNO game:
 * players, draw pile, discard pile, current player, direction, active color,
 * and game-phase flags.
 */
public class GameState {

    /** Game phases used to validate commands. */
    public enum Phase {
        /** Normal turn: current player may PLAY or DRAW. */
        NORMAL,
        /** After playing a wild card: current player must choose COLOR. */
        AWAITING_COLOR,
        /** Game has ended (winner found or draw pile exhausted). */
        FINISHED
    }

    private final List<Player> players;
    private DrawPile drawPile;
    private DiscardPile discardPile;
    private int currentPlayerIndex;
    private Direction direction;
    private Color currentColor;
    private Phase phase;
    private Player winner;

    /**
     * Constructs the initial game state.
     *
     * @param players    ordered list of players (already dealt)
     * @param drawPile   remaining draw pile after dealing
     * @param discardPile discard pile initialised with the top card
     * @param currentColor initial active color (color of top card)
     */
    public GameState(List<Player> players, DrawPile drawPile,
                     DiscardPile discardPile, Color currentColor) {
        this.players = new ArrayList<>(players);
        this.drawPile = drawPile;
        this.discardPile = discardPile;
        this.currentPlayerIndex = 0;
        this.direction = Direction.CLOCKWISE;
        this.currentColor = currentColor;
        this.phase = Phase.NORMAL;
        this.winner = null;
    }

    // ---- Players -----------------------------------------------------------

    /** @return unmodifiable list of all players */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /** @return number of players in the game */
    public int getPlayerCount() { return players.size(); }

    /**
     * Returns the player with the given id.
     * @param id player id
     * @return Player object
     */
    public Player getPlayer(int id) { return players.get(id); }

    // ---- Current player ----------------------------------------------------

    /** @return the player whose turn it currently is */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /** @return index of the current player */
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }

    /**
     * Advances the current player by {@code steps} positions in the current direction.
     * @param steps number of positions to advance (usually 1, or 2 for SKIP)
     */
    public void advancePlayer(int steps) {
        int n = players.size();
        if (direction == Direction.CLOCKWISE) {
            currentPlayerIndex = (currentPlayerIndex + steps) % n;
        } else {
            currentPlayerIndex = (currentPlayerIndex - steps % n + n) % n;
        }
    }

    // ---- Direction ---------------------------------------------------------

    /** @return current direction of play */
    public Direction getDirection() { return direction; }

    /** Reverses the direction of play. */
    public void reverseDirection() {
        direction = direction.reverse();
    }

    // ---- Piles -------------------------------------------------------------

    /** @return the draw pile */
    public DrawPile getDrawPile() { return drawPile; }

    /** @return the discard pile */
    public DiscardPile getDiscardPile() { return discardPile; }

    // ---- Color -------------------------------------------------------------

    /** @return the currently active color */
    public Color getCurrentColor() { return currentColor; }

    /**
     * Sets the currently active color.
     * @param color new active color
     */
    public void setCurrentColor(Color color) { this.currentColor = color; }

    // ---- Phase -------------------------------------------------------------

    /** @return the current game phase */
    public Phase getPhase() { return phase; }

    /**
     * Sets the current game phase.
     * @param phase new phase
     */
    public void setPhase(Phase phase) { this.phase = phase; }

    // ---- Winner ------------------------------------------------------------

    /** @return the winner player, or null if the game has not yet ended with a winner */
    public Player getWinner() { return winner; }

    /**
     * Sets the winner and marks the game as finished.
     * @param winner winning player
     */
    public void setWinner(Player winner) {
        this.winner = winner;
        this.phase = Phase.FINISHED;
    }

    /** Marks the game as finished without a winner (draw pile exhausted). */
    public void setFinishedNoWinner() {
        this.phase = Phase.FINISHED;
    }

    /** @return true if the game has ended */
    public boolean isFinished() { return phase == Phase.FINISHED; }

    /**
     * Computes the next player index (1 step in the current direction)
     * without modifying state.
     * @return index of the next player
     */
    public int peekNextPlayerIndex() {
        int n = players.size();
        if (direction == Direction.CLOCKWISE) {
            return (currentPlayerIndex + 1) % n;
        } else {
            return (currentPlayerIndex - 1 + n) % n;
        }
    }

    /**
     * Computes the player index N steps ahead (in current direction)
     * without modifying state.
     * @param steps number of steps
     * @return player index
     */
    public int peekNextPlayerIndex(int steps) {
        int n = players.size();
        if (direction == Direction.CLOCKWISE) {
            return (currentPlayerIndex + steps) % n;
        } else {
            return (currentPlayerIndex - steps + n * steps) % n;
        }
    }
}
