package uno.v1.io;

import uno.v1.model.Card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads a deck file and returns an ordered list of {@link Card} objects.
 *
 * <p>The deck file format:
 * <ul>
 *   <li>One card per line in {@code COLOR-RANK} notation.</li>
 *   <li>Blank lines are ignored.</li>
 *   <li>Lines beginning with {@code #} are comments and are ignored.</li>
 *   <li>Inline comments starting with {@code #} are stripped.</li>
 * </ul>
 */
public class DeckLoader {

    /**
     * Reads all cards from the given reader in file order.
     *
     * @param reader reader associated with the deck file
     * @return ordered list of cards (first = initial discard, rest = draw pile)
     * @throws IOException              if an I/O error occurs
     * @throws IllegalArgumentException if a line cannot be parsed as a card
     */
    public List<Card> loadDeck(Reader reader) throws IOException {
        List<Card> cards = new ArrayList<>();
        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
            String cleaned = cleanLine(line);
            if (cleaned == null) continue;
            try {
                cards.add(Card.fromCode(cleaned));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid deck line: " + line, e);
            }
        }
        return cards;
    }

    /**
     * Cleans a single line: trims, removes full-line and inline comments.
     *
     * @param line raw line from file
     * @return cleaned line, or {@code null} if the line should be skipped
     */
    private String cleanLine(String line) {
        String trimmed = line.trim();
        if (trimmed.isEmpty() || trimmed.startsWith("#")) return null;
        int ci = trimmed.indexOf('#');
        if (ci >= 0) {
            trimmed = trimmed.substring(0, ci).trim();
            if (trimmed.isEmpty()) return null;
        }
        return trimmed;
    }
}
