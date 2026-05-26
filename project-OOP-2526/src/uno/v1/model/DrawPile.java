package uno.v1.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents the draw pile (deck) from which players draw cards.
 * Cards are drawn from the front of the queue (FIFO order preserving file order).
 */
public class DrawPile {

    private final Queue<Card> cards;

    /**
     * Constructs a draw pile from an ordered list of cards.
     * @param cards ordered list of cards (first = top of pile)
     */
    public DrawPile(List<Card> cards) {
        this.cards = new LinkedList<>(cards);
    }

    /**
     * Draws and removes the top card from the pile.
     * @return top card, or null if the pile is empty
     */
    public Card draw() {
        return cards.poll();
    }

    /** @return true if the draw pile has no cards remaining */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /** @return number of cards remaining in the draw pile */
    public int size() {
        return cards.size();
    }
}
