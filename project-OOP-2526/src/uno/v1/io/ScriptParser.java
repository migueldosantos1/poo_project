package uno.v1.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Reads a script file line by line, returning one {@link Command} per call
 * to {@link #nextCommand()}.
 *
 * <p>Script file format:
 * <ul>
 *   <li>{@code PLAYER <id> PLAY <index>}</li>
 *   <li>{@code PLAYER <id> DRAW}</li>
 *   <li>{@code PLAYER <id> COLOR <R|Y|G|B>}</li>
 *   <li>Blank lines and full-line comments ({@code #}) are ignored.</li>
 *   <li>Inline comments are NOT supported.</li>
 * </ul>
 */
public class ScriptParser implements AutoCloseable {

    private final BufferedReader reader;

    /**
     * Creates a parser reading from the given reader.
     * @param reader reader associated with the script file
     */
    public ScriptParser(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    /**
     * Reads and returns the next command from the script.
     * Skips blank lines and comment lines.
     *
     * @return next Command, or {@code null} if the script is exhausted
     * @throws IOException              if an I/O error occurs
     * @throws IllegalArgumentException if a script line is malformed
     */
    public Command nextCommand() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            String cleaned = cleanLine(line);
            if (cleaned == null) continue;

            String[] parts = cleaned.split("\\s+");
            if (parts.length < 3 || !"PLAYER".equals(parts[0])) {
                throw new IllegalArgumentException("Invalid script line: " + line);
            }

            int playerId;
            try {
                playerId = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid player id in: " + line);
            }

            String cmd = parts[2].toUpperCase();
            switch (cmd) {
                case "PLAY":
                    if (parts.length != 4)
                        throw new IllegalArgumentException("PLAY requires index: " + line);
                    int idx;
                    try {
                        idx = Integer.parseInt(parts[3]);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid card index in: " + line);
                    }
                    return Command.play(playerId, idx, cleaned);

                case "DRAW":
                    if (parts.length != 3)
                        throw new IllegalArgumentException("DRAW has no arguments: " + line);
                    return Command.draw(playerId, cleaned);

                case "COLOR":
                    if (parts.length != 4)
                        throw new IllegalArgumentException("COLOR requires color code: " + line);
                    return Command.color(playerId, parts[3], cleaned);

                default:
                    throw new IllegalArgumentException("Unknown command: " + cmd + " in: " + line);
            }
        }
        return null; // end of script
    }

    /**
     * Cleans a single line: trims, skips blanks and full-line comments.
     * Rejects inline comments.
     *
     * @param line raw line
     * @return cleaned line, or {@code null} if line should be skipped
     * @throws IllegalArgumentException if inline comment detected
     */
    private String cleanLine(String line) {
        String trimmed = line.trim();
        if (trimmed.isEmpty() || trimmed.startsWith("#")) return null;
        if (trimmed.indexOf('#') >= 0)
            throw new IllegalArgumentException(
                "Inline comments are not allowed in script: " + line);
        return trimmed;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
