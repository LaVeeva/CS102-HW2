



public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;
    int[][] colorCounts;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the game
        colorCounts = new int[8][4];
    }

    /*
     * Removes and returns the tile in given index
     */
    public Tile getAndRemoveTile(int index) {

        if (index < 0 || index >= numberOfTiles) {
            return null;
        }

        Tile removed = playerTiles[index];
        for (int i = index; i < numberOfTiles - 1; i++) {
            playerTiles[i] = playerTiles[i + 1];
        }
        playerTiles[numberOfTiles - 1] = null;
        numberOfTiles--;

        return removed;

    }

    /*
     * Adds the given tile to the playerTiles in order
     * should also update numberOfTiles accordingly.
     * make sure playerTiles are not more than 15 at any time
     */
     public void addTile(Tile t) {

        if (numberOfTiles >= 15) return;

        int insertIndex = 0;
        while (insertIndex < numberOfTiles && playerTiles[insertIndex].compareTo(t) < 0) {
            insertIndex++;
        }

        for (int i = numberOfTiles; i > insertIndex; i--) {
            playerTiles[i] = playerTiles[i - 1];
        }

        playerTiles[insertIndex] = t;
        numberOfTiles++;

    }

    /*
     * Checks if this player's hand satisfies the winning condition
     * to win this player should have 3 chains of length 4, extra tiles
     * does not disturb the winning condition
     * @return
     */
    public boolean isWinningHand() {

        countColors();
    
        int chainCount = 0;
        for (int value = 1; value <= 7; value++) {
            int uniqueColors = 0;
            for (int colorIndex = 0; colorIndex < 4; colorIndex++) {
                if (colorCounts[value][colorIndex] > 0) {
                    uniqueColors++;
                }
            }
            if (uniqueColors >= 4) {
                chainCount++;
            }
        }

        return chainCount >= 3;

    }

    private void countColors() {
        for (int i = 0; i < numberOfTiles; i++) {
            Tile tile = playerTiles[i];
            int value = tile.getValue();
            int colorIndex = tile.colorNameToInt();
            colorCounts[value][colorIndex]++;
        }
    }

    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if (playerTiles[i].compareTo(t) == 0) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles; i++) {
            System.out.printf("%4d:%3s%n", i, playerTiles[i].toString());
        }
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    private void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }

    /*
     * Returns false if the player already has the card.
     * (This implicitly means it will return false if the chain is complete)
     * Otherwise, it is dependent on a random chance, determined by how close the player is to completing a chain with the given card.
     */
    public boolean needsCard(Tile otherTile) {

        int chainLength = 0;
        for (Tile tile: playerTiles) {
            if (tile == null) break;
            if (tile.toString().equals(otherTile.toString())) return false;
            if (otherTile.canFormChainWith(tile)) chainLength++;
        }
        return (chainLength/3.0) > Math.random();

    }

}
