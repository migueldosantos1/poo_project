package exampledeck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Support class for the UNO project.
 *
 * <p>This class illustrates how to read a deck file line by line, remove
 * comments and empty lines, and split each valid line into its textual
 * components.</p>
 *
 * <p>In this simplified version, the extracted information is printed to the
 * terminal. In the actual UNO project, students should replace those print
 * instructions by the creation of the internal objects required by their own
 * solution, storing the corresponding information in memory.</p>
 * 
 * <p>Inline comments are supported in deck lines.</p>
 */
public class DeckLoader {

    /**
     * Reads a deck file through the given {@link Reader}, removes comments and
     * empty lines, splits each valid card line into color and rank, and prints
     * the result.
     *
     * <p>In the actual UNO project, the print instruction should be replaced by
     * the creation and storage of the internal objects required by the
     * student's own implementation.</p>
     *
     * @param reader reader associated with the deck file
     * @throws IOException if an I/O error occurs while reading the file
     */
    public void loadDeck(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        String line;

        while ((line = br.readLine()) != null) {
            String cleaned = cleanLine(line);
            if (cleaned == null) {
                continue;
            }

            String[] parts = cleaned.split("-");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid deck line: " + line);
            }

            String color = parts[0];
            String rank = parts[1];

            // In the actual UNO project, replace this print by the
            // initialization of the in-memory objects required by your solution
            System.out.println("color: " + color + " rank: " + rank);
        }
    }

    /**
     * Cleans one line of the deck file.
     *
     * <p>This method trims the line, removes full-line comments, removes inline
     * comments starting with {@code #}, and returns {@code null} if the result
     * is empty.</p>
     *
     * @param line original line read from the file
     * @return cleaned line, or {@code null} if the line should be ignored
     */
    private String cleanLine(String line) {
        String trimmed = line.trim();

        if (trimmed.isEmpty() || trimmed.startsWith("#")) {
            return null;
        }
        
        // inline comments
        int commentIndex = trimmed.indexOf('#');
        if (commentIndex >= 0) {
            trimmed = trimmed.substring(0, commentIndex).trim();
            if (trimmed.isEmpty()) {
                return null;
            }
        }

        return trimmed;
    }

    /**
     * Small standalone test program that reads a deck file and prints the
     * extracted color/rank pairs to the terminal.
     *
     * <p>This is only intended to illustrate how the support class works.
     * In the actual UNO project, students are expected to integrate this logic
     * into their own solution and create the appropriate in-memory objects.</p>
     *
     * @param args command-line arguments; {@code args[0]} must be the deck file
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java exampledeck.DeckLoader <deckFile>");
            System.exit(1);
        }

        String deckFile = args[0];

        try (Reader reader = new FileReader(deckFile)) {
            DeckLoader loader = new DeckLoader();
            loader.loadDeck(reader);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
