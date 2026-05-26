package uno.v1.model;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Represents the discard pile on which players place played cards.
 * The top card is always accessible.
 */
public class DiscardPile {

    private final Deque<Card> cards;

    /**
     * Constructs a discard pile with the given initial top card.
     * @param initialCard first card placed on the pile
     */
    public DiscardPile(Card initialCard) {
        this.cards = new ArrayDeque<>();
        this.cards.push(initialCard);
    }

    /**
     * Places a card on top of the discard pile.
     * @param card card to place
     */
    public void place(Card card) {
        cards.push(card);
    }

    /**
     * Returns (without removing) the top card of the discard pile.
     * @return top card
     */
    public Card getTopCard() {
        return cards.peek();
    }
}
