package uno.v1.engine;

import uno.v1.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for setting up the initial {@link GameState} from an ordered
 * list of cards, a player count, and a cards-per-player count.
 *
 * <p>Setup procedure (per spec Section 1.1.2):
 * <ol>
 *   <li>The first card in the list becomes the initial top of the discard pile.</li>
 *   <li>If that first card is a WILD card, setup aborts with an error.</li>
 *   <li>The remaining cards form the initial draw pile in file order.</li>
 *   <li>Cards are dealt one at a time in round-robin order starting from player 0.</li>
 *   <li>Player 0 plays first; initial direction is CLOCKWISE.</li>
 *   <li>Initial color = color of the top card.</li>
 * </ol>
 */
public class GameSetup {

    /**
     * Builds and returns the initial game state.
     *
     * @param allCards       all cards in file order (first = initial discard)
     * @param playerCount    number of players (2–6)
     * @param cardsPerPlayer number of cards dealt to each player at start
     * @return ready-to-play GameState
     * @throws IllegalArgumentException if the initial discard is a wild card,
     *                                  or there are not enough cards to deal
     */
    public GameState setup(List<Card> allCards, int playerCount, int cardsPerPlayer) {
        if (allCards.isEmpty()) {
            throw new IllegalArgumentException("Deck file is empty");
        }

        // 1. Initial discard
        Card initialDiscard = allCards.get(0);
        if (initialDiscard.getRank().isWild()) {
            throw new IllegalArgumentException(
                "Initial discard is a wild card — aborting");
        }

        // 2. Draw pile = rest of cards
        List<Card> drawList = new ArrayList<>(allCards.subList(1, allCards.size()));

        // 3. Create players
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            players.add(new Player(i));
        }

        // 4. Deal cards round-robin
        int needed = playerCount * cardsPerPlayer;
        if (drawList.size() < needed) {
            throw new IllegalArgumentException(
                "Not enough cards in deck to deal " + cardsPerPlayer
                + " cards to " + playerCount + " players");
        }

        int cardIdx = 0;
        for (int round = 0; round < cardsPerPlayer; round++) {
            for (int p = 0; p < playerCount; p++) {
                players.get(p).addCard(drawList.get(cardIdx++));
            }
        }

        // 5. Remaining draw pile
        List<Card> remaining = drawList.subList(cardIdx, drawList.size());
        DrawPile drawPile = new DrawPile(remaining);

        // 6. Discard pile
        DiscardPile discardPile = new DiscardPile(initialDiscard);

        // 7. Initial color = color of initial discard (action cards use their color, not effect)
        Color initialColor = initialDiscard.getColor();

        return new GameState(players, drawPile, discardPile, initialColor);
    }
}
