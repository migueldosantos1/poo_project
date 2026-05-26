package uno.v1.engine;

import uno.v1.io.Command;
import uno.v1.model.*;

/**
 * The core UNO game engine.
 *
 * <p>Processes one {@link Command} at a time, validates it against the current
 * {@link GameState}, applies its effect, and emits events through the
 * {@link EventLogger}.
 *
 * <p>Design note: uses the State pattern implicitly through {@link GameState#getPhase()}.
 * Each command handler first checks the current phase before applying any effect,
 * keeping phase-transition logic centralised here.
 */
public class GameEngine {

    private final GameState state;
    private final EventLogger logger;

    /**
     * Constructs the engine with the given initial state and logger.
     *
     * @param state  fully initialised game state
     * @param logger event logger
     */
    public GameEngine(GameState state, EventLogger logger) {
        this.state  = state;
        this.logger = logger;
    }

    /**
     * Processes one command from the script.
     * Validates, applies effects, logs events, and advances the turn when appropriate.
     *
     * <p>If the command is invalid, an error event is printed and the engine stops
     * (the caller should not call this method again after an error).
     *
     * @param cmd the command to process
     * @return true if the command was valid and the game should continue,
     *         false if an error occurred (game must stop)
     */
    public boolean processCommand(Command cmd) {
        logger.printCommand(cmd.getRawLine());

        switch (cmd.getType()) {
            case PLAY:  return handlePlay(cmd);
            case DRAW:  return handleDraw(cmd);
            case COLOR: return handleColor(cmd);
            default:
                logger.printError("Unknown command type");
                return false;
        }
    }

    // =========================================================================
    // PLAY handler
    // =========================================================================

    private boolean handlePlay(Command cmd) {
        int pid   = cmd.getPlayerId();
        int index = cmd.getCardIndex();
        Player current = state.getCurrentPlayer();

        // Phase check
        if (state.getPhase() == GameState.Phase.AWAITING_COLOR) {
            logger.printError("Must choose a color before playing another card");
            return false;
        }
        if (state.getPhase() == GameState.Phase.FINISHED) {
            logger.printError("Game is already finished");
            return false;
        }

        // Turn check
        if (pid != current.getId()) {
            logger.printError("Not player " + pid + " turn");
            return false;
        }

        // Index check
        if (index < 0 || index >= current.handSize()) {
            logger.printError("Invalid card index " + index);
            return false;
        }

        Card card = current.getCard(index);

        // Playability check
        if (!card.isPlayable(state.getDiscardPile().getTopCard(), state.getCurrentColor())) {
            logger.printError("Card " + card.getCode() + " is not playable");
            return false;
        }

        // Remove from hand and place on discard
        current.removeCard(index);
        state.getDiscardPile().place(card);

        // Apply card effect
        applyCardEffect(card, current);

        return true;
    }

    /**
     * Applies the effect of the card just played and logs the appropriate events.
     */
    private void applyCardEffect(Card card, Player current) {
        Rank rank = card.getRank();

        switch (rank) {
            case WILD:
                // Log play event; turn does NOT advance yet — wait for COLOR
                logger.printPlayWild(current.getId());
                state.setCurrentColor(Color.WILD); // placeholder until COLOR chosen
                state.setPhase(GameState.Phase.AWAITING_COLOR);
                checkWin(current);
                break;

            case WILD_DRAW_FOUR: {
                // Compute affected player BEFORE advancing
                int affectedIdx = state.peekNextPlayerIndex();
                Player affected = state.getPlayers().get(affectedIdx);
                logger.printPlayWildDrawFour(current.getId(), affected.getId());
                // Give 4 cards to the affected player (silently)
                drawCardsForced(affected, 4);
                // Mark waiting for color; skip the affected player (advance 2)
                state.setCurrentColor(Color.WILD);
                state.setPhase(GameState.Phase.AWAITING_COLOR);
                // Store that after color we advance 2 (skip affected)
                // We handle the advance after COLOR is given
                checkWin(current);
                // Store the skip-count so COLOR handler knows
                pendingSkipAfterWild = 2;
                break;
            }

            case SKIP: {
                logger.printPlaySkip(current.getId());
                state.setCurrentColor(card.getColor());
                checkWin(current);
                if (!state.isFinished()) {
                    // Advance 2 to skip the next player
                    state.advancePlayer(2);
                    logger.printTurnAdvance(state.getCurrentPlayer().getId());
                }
                break;
            }

            case REVERSE:
                logger.printPlayReverse(current.getId());
                state.setCurrentColor(card.getColor());
                state.reverseDirection();
                checkWin(current);
                if (!state.isFinished()) {
                    if (state.getPlayerCount() == 2) {
                        // In 2-player, REVERSE acts like SKIP: same player plays again
                        // direction reversed, advance 2 brings us back to same player
                        state.advancePlayer(2);
                    } else {
                        state.advancePlayer(1);
                    }
                    logger.printTurnAdvance(state.getCurrentPlayer().getId());
                }
                break;

            case DRAW_TWO: {
                int affectedIdx = state.peekNextPlayerIndex();
                Player affected = state.getPlayers().get(affectedIdx);
                logger.printPlayDrawTwo(current.getId(), affected.getId());
                state.setCurrentColor(card.getColor());
                drawCardsForced(affected, 2);
                checkWin(current);
                if (!state.isFinished()) {
                    // Skip the affected player: advance 2
                    state.advancePlayer(2);
                    logger.printTurnAdvance(state.getCurrentPlayer().getId());
                }
                break;
            }

            default:
                // Number card — normal play
                logger.printPlayCard(current.getId(), card);
                state.setCurrentColor(card.getColor());
                checkWin(current);
                if (!state.isFinished()) {
                    state.advancePlayer(1);
                    logger.printTurnAdvance(state.getCurrentPlayer().getId());
                }
                break;
        }
    }

    // =========================================================================
    // DRAW handler
    // =========================================================================

    private boolean handleDraw(Command cmd) {
        int pid = cmd.getPlayerId();
        Player current = state.getCurrentPlayer();

        // Phase check
        if (state.getPhase() == GameState.Phase.AWAITING_COLOR) {
            logger.printError("Must choose a color before drawing");
            return false;
        }
        if (state.getPhase() == GameState.Phase.FINISHED) {
            logger.printError("Game is already finished");
            return false;
        }

        // Turn check
        if (pid != current.getId()) {
            logger.printError("Not player " + pid + " turn");
            return false;
        }

        // Draw pile check
        if (state.getDrawPile().isEmpty()) {
            logger.printGameEndNoCards();
            state.setFinishedNoWinner();
            return false;
        }

        Card drawn = state.getDrawPile().draw();
        current.addCard(drawn);
        logger.printDrawCard(current.getId(), drawn);

        // Advance turn
        state.advancePlayer(1);
        logger.printTurnAdvance(state.getCurrentPlayer().getId());
        return true;
    }

    // =========================================================================
    // COLOR handler
    // =========================================================================

    /** How many steps to advance after a wild color is chosen (1 for WILD, 2 for WILD_DRAW_FOUR). */
    private int pendingSkipAfterWild = 1;

    private boolean handleColor(Command cmd) {
        int pid = cmd.getPlayerId();
        Player current = state.getCurrentPlayer();

        // Phase check
        if (state.getPhase() != GameState.Phase.AWAITING_COLOR) {
            logger.printError("Cannot choose color - no wild card was played");
            return false;
        }

        // Player check
        if (pid != current.getId()) {
            logger.printError("Only player " + current.getId() + " can choose the color");
            return false;
        }

        // Color validation
        Color chosen;
        try {
            chosen = Color.fromColorCommand(cmd.getColorCode());
        } catch (IllegalArgumentException e) {
            if ("W".equals(cmd.getColorCode()) || "WILD".equalsIgnoreCase(cmd.getColorCode())) {
                logger.printError("Cannot choose WILD as a color");
            } else {
                logger.printError("Invalid color: " + cmd.getColorCode());
            }
            return false;
        }

        state.setCurrentColor(chosen);
        logger.printChooseColor(current.getId(), chosen);
        state.setPhase(GameState.Phase.NORMAL);

        // Advance turn (unless game already finished by win)
        if (!state.isFinished()) {
            state.advancePlayer(pendingSkipAfterWild);
            pendingSkipAfterWild = 1; // reset to default
            logger.printTurnAdvance(state.getCurrentPlayer().getId());
        }

        return true;
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    /**
     * Draws {@code count} cards from the draw pile for the affected player.
     * If the pile runs dry mid-draw, stops silently (no game-end here;
     * game-end due to empty pile only triggers when a player tries to draw voluntarily).
     */
    private void drawCardsForced(Player player, int count) {
        for (int i = 0; i < count; i++) {
            if (state.getDrawPile().isEmpty()) break;
            player.addCard(state.getDrawPile().draw());
        }
    }

    /**
     * Checks whether the current player has won (empty hand) and, if so,
     * logs the win and marks the game finished.
     */
    private void checkWin(Player player) {
        if (player.hasEmptyHand()) {
            state.setWinner(player);
            logger.printGameEndWinner(player.getId());
        }
    }
}
