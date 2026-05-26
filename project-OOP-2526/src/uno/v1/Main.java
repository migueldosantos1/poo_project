package uno.v1;

import uno.v1.engine.EventLogger;
import uno.v1.engine.GameEngine;
import uno.v1.engine.GameSetup;
import uno.v1.io.Command;
import uno.v1.io.DeckLoader;
import uno.v1.io.ScriptParser;
import uno.v1.model.Card;
import uno.v1.model.GameState;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;

/**
 * Main entry point for the UNO game engine (Phase 1).
 *
 * <p>Usage:
 * <pre>
 *   java -jar project-v1.jar &lt;deckFile&gt; &lt;scriptFile&gt; &lt;playerCount&gt; [&lt;cardsPerPlayer&gt;]
 * </pre>
 */
public class Main {

    /** Default number of cards dealt per player. */
    private static final int DEFAULT_CARDS_PER_PLAYER = 7;

    /**
     * Program entry point.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: java -jar project-v1.jar <deckFile> <scriptFile> "
                    + "<playerCount> [<cardsPerPlayer>]");
            System.exit(1);
        }

        String deckFile   = args[0];
        String scriptFile = args[1];
        int playerCount;
        int cardsPerPlayer = DEFAULT_CARDS_PER_PLAYER;

        try {
            playerCount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid player count: " + args[2]);
            System.exit(1);
            return;
        }

        if (args.length >= 4) {
            try {
                cardsPerPlayer = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid cards-per-player: " + args[3]);
                System.exit(1);
                return;
            }
        }

        EventLogger logger = new EventLogger();

        // Print startup header
        logger.printStartupHeader(
            Main.class.getName(), deckFile, scriptFile, playerCount, cardsPerPlayer);

        // Load deck
        List<Card> allCards;
        try (Reader r = new FileReader(deckFile)) {
            allCards = new DeckLoader().loadDeck(r);
        } catch (Exception e) {
            System.err.println("Error loading deck: " + e.getMessage());
            System.exit(1);
            return;
        }

        // Setup game state
        GameState state;
        try {
            state = new GameSetup().setup(allCards, playerCount, cardsPerPlayer);
        } catch (IllegalArgumentException e) {
            System.err.println("Game setup error: " + e.getMessage());
            System.exit(1);
            return;
        }

        // Print game start events
        logger.printGameStart(
            playerCount,
            state.getDiscardPile().getTopCard(),
            state.getCurrentColor(),
            state.getPlayers(),
            state.getCurrentPlayer().getId()
        );

        // Run the game
        GameEngine engine = new GameEngine(state, logger);

        try (ScriptParser parser = new ScriptParser(new FileReader(scriptFile))) {
            Command cmd;
            while (!state.isFinished() && (cmd = parser.nextCommand()) != null) {
                boolean ok = engine.processCommand(cmd);
                if (!ok) break; // error occurred — stop
            }
        } catch (Exception e) {
            System.err.println("Error reading script: " + e.getMessage());
            System.exit(1);
            return;
        }

        // If script exhausted without a winner, print GAME_END
        if (!state.isFinished()) {
            logger.printGameEndNoWinner();
        }
    }
}
