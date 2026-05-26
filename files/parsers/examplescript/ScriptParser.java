package examplescript;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Support class for the UNO project.
 *
 * <p>This class illustrates how to read a script file line by line, ignore
 * empty lines and full-line comments, and extract the textual components of
 * each command.</p>
 *
 * <p>In this simplified version, the extracted information is printed to the
 * terminal. In the actual UNO project, students should replace those print
 * instructions by the creation of the internal objects required by their own
 * solution, storing the corresponding information in memory.</p>
 *
 * <p>Inline comments are not supported in script lines.</p>
 */
public class ScriptParser implements AutoCloseable {

    private final BufferedReader reader;

    /**
     * Creates a parser that reads commands from the given reader.
     *
     * @param reader reader associated with the script file
     */
    public ScriptParser(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    /**
     * Reads the script file line by line, ignores empty lines and full-line
     * comments, extracts the command components, and prints them to the terminal.
     *
     * <p>In the actual UNO project, the print instructions should be replaced by
     * the creation and storage of the command objects required by the student's
     * own implementation.</p>
     *
     * @throws IOException if an I/O error occurs while reading the file
     */
    public void nextCommand() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            String cleaned = cleanLine(line);
            if (cleaned == null) {
                continue;
            }

            String[] parts = cleaned.split("\\s+");
            if (parts.length < 3 || !"PLAYER".equals(parts[0])) {
                throw new IllegalArgumentException("Invalid script line: " + line);
            }

            int playerId = Integer.parseInt(parts[1]);
            String cmd = parts[2];

            if ("PLAY".equalsIgnoreCase(cmd)) {
                if (parts.length != 4) {
                    throw new IllegalArgumentException("PLAY requires index: " + line);
                }
                int idx = Integer.parseInt(parts[3]);

                // In the actual UNO project, replace this print by the
                // initialization of the in-memory objects required by your solution
                System.out.println("playerId: " + playerId +
                                   " command: PLAY index: " + idx);

            } else if ("DRAW".equalsIgnoreCase(cmd)) {
                if (parts.length != 3) {
                    throw new IllegalArgumentException("DRAW has no arguments: " + line);
                }

                // In the actual UNO project, replace this print by the
                // initialization of the in-memory objects required by your solution
                System.out.println("playerId: " + playerId +
                                   " command: DRAW");

            } else if ("COLOR".equalsIgnoreCase(cmd)) {
                if (parts.length != 4) {
                    throw new IllegalArgumentException("COLOR requires color code: " + line);
                }
                String colorCode = parts[3];

                // In the actual UNO project, replace this print by the
                // initialization of the in-memory objects required by your solution
                System.out.println("playerId: " + playerId +
                                   " command: COLOR color: " + colorCode);

            } else {
                throw new IllegalArgumentException("Unknown command: " + cmd + " in line " + line);
            }
        }
    }

    /**
     * Cleans one line of the script file.
     *
     * <p>This method trims the line, ignores empty lines and full-line comments
     * starting with {@code #}, and rejects inline comments.</p>
     *
     * @param line original line read from the file
     * @return cleaned line, or {@code null} if the line should be ignored
     */
    private String cleanLine(String line) {
        String trimmed = line.trim();

        if (trimmed.isEmpty() || trimmed.startsWith("#")) {
            return null;
        }

        if (trimmed.indexOf('#') >= 0) {
            throw new IllegalArgumentException(
                "Inline comments are not allowed in script lines: " + line
            );
        }

        return trimmed;
    }

    /**
     * Closes the underlying reader.
     *
     * @throws IOException if an I/O error occurs while closing the reader
     */
    @Override
    public void close() throws IOException {
        reader.close();
    }

    /**
     * Small standalone test program that reads a script file and prints the
     * extracted command information to the terminal.
     *
     * <p>This is only intended to illustrate how the support class works.
     * In the actual UNO project, students are expected to integrate this logic
     * into their own solution and create the appropriate in-memory objects.</p>
     *
     * @param args command-line arguments; {@code args[0]} must be the script file
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java examplescript.ScriptParser <scriptFile>");
            System.exit(1);
        }

        String scriptFile = args[0];
        try (Reader reader = new FileReader(scriptFile);
             ScriptParser parser = new ScriptParser(reader)) {
            parser.nextCommand();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
