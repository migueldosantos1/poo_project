package uno.v1.model;

/**
 * Represents the current direction of play.
 */
public enum Direction {
    CLOCKWISE,
    COUNTER_CLOCKWISE;

    /**
     * Returns the opposite direction.
     * @return reversed direction
     */
    public Direction reverse() {
        return this == CLOCKWISE ? COUNTER_CLOCKWISE : CLOCKWISE;
    }
}
