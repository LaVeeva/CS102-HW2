public class Tile {
    
    int value;
    char color;

    /*
     * Creates a tile using the given color and value, colors are represented
     * using the following letters: Y: Yellow, B: Blue, R: Red, K: Black
     * Values can be in the range [1,7]. There are four tiles of each color value
     * combination (7 * 4 * 4) = 112 tiles, false jokers are not included in this game.
     */
    public Tile(int value, char color) {
        this.value = value;
        this.color = color;
    }

    /*
     * Compares tiles so that they can be added to the hands in order
     */
    public int compareTo(Tile t) {

        if (getValue() < t.getValue()) return -1;
        else if (getValue() > t.getValue()) return 1;

        else {
            if (colorNameToInt() < t.colorNameToInt()) return -1;
            else if (colorNameToInt() > t.colorNameToInt()) return 1;
            else return 0;
        }
        
    }

    public int colorNameToInt() {
        return switch (color) {
            case 'Y' -> 0;
            case 'B' -> 1;
            case 'R' -> 2;
            default -> 3;
        };
    }

    // determines if this tile can make a chain with the given tile
    public boolean canFormChainWith(Tile t) {
        // can make chain if same number but different color
        return t.getColor() != color && t.getValue() == value;
    }

    @Override
    public String toString() {
        return "" + value + color;
    }

    public int getValue() {
        return value;
    }

    public char getColor() {
        return color;
    }
    
}
