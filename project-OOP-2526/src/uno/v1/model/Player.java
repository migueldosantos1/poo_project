package uno.v1.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a player in the UNO game.
 * Holds the player's id and their current hand of cards.
 */
public class Player {

    private final int id;
    private final List<Card> hand;

    /**
     * Constructs a player with the given id and an empty hand.
     * @param id player identifier (0-based)
     */
    public Player(int id) {
        this.id = id;
        this.hand = new ArrayList<>();
    }

    /** @return player id */
    public int getId() { return id; }

    /**
     * Returns an unmodifiable view of the player's current hand.
     * Cards are ordered by deal/draw order.
     * @return unmodifiable list of cards
     */
    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }

    /** @return number of cards currently in hand */
    public int handSize() { return hand.size(); }

    /** @return true if the player's hand is empty */
    public boolean hasEmptyHand() { return hand.isEmpty(); }

    /**
     * Adds a card to the end of this player's hand.
     * @param card card to add
     */
    public void addCard(Card card) {
        hand.add(card);
    }

    /**
     * Removes and returns the card at the given zero-based index.
     * @param index position in hand
     * @return removed card
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public Card removeCard(int index) {
        return hand.remove(index);
    }

    /**
     * Returns the card at the given index without removing it.
     * @param index position in hand
     * @return card at that position
     */
    public Card getCard(int index) {
        return hand.get(index);
    }
}
