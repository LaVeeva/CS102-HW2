import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OkeyGame {

    Player[] players;
    Tile[] tiles;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public OkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[112];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 7; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i,'Y');
                tiles[currentTile++] = new Tile(i,'B');
                tiles[currentTile++] = new Tile(i,'R');
                tiles[currentTile++] = new Tile(i,'K');
            }
        }
    }

    /*
     * Distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already shuffled
     */
    public void distributeTilesToPlayers() {
        for (int i = 1; i <= 15; i++)
        {
            players[0].addTile(tiles[112 - i]);
            tiles[112 - i] = null;
        }
        for (int k = 0; k < 3; k++)
        {
            for (int i = 1; i <= 14; i++)
            {
                players[k + 1].addTile(tiles[97 - (14 * k) - i]);
                tiles[97 - (14 * k) - i] = null;
            }
        }
    } 

    /*
     * Get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() {

        if (lastDiscardedTile == null) return null;

        String tile = lastDiscardedTile.toString();

        players[currentPlayerIndex].addTile(lastDiscardedTile);
        lastDiscardedTile = null;

        return tile;

    }

    /*
     * Get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {

        for (int i = 54; i >= 0; i--) {
            if (tiles[i] != null) {
                Tile topTile = tiles[i];
                players[currentPlayerIndex].addTile(topTile);
                tiles[i] = null;
                return topTile.toString();
            }
        }

        return "No more tiles to pull";

    }

    /*
     * Should randomly shuffle the tiles array before game starts
     */
    
    public void shuffleTiles() {
        List<Tile> tileList = Arrays.asList(tiles);
        Collections.shuffle(tileList);
        this.tiles = tileList.toArray(tiles);
    }

    /*
     * Checks if game still continues, should return true if current player
     * finished the game, use isWinningHand() method of Player to decide
     */
    public boolean didGameFinish() {
        return players[currentPlayerIndex].isWinningHand();
    }

    /*
     * Pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You should consider if the discarded tile is useful for the computer in
     * the current status. Print whether computer picks from tiles or discarded ones.
     */
    public void pickTileForComputer() {

        Player computer = players[currentPlayerIndex];

        if (lastDiscardedTile != null && computer.needsCard(lastDiscardedTile)) {
            System.out.printf("%s picks up the last discarded card, %s%n",
                getCurrentPlayerName(),
                getLastDiscardedTile()
            );
        }

        else {
            System.out.printf("%s picks up the top card, %s%n",
                getCurrentPlayerName(),
                getTopTile()
            );
        }
        
    }

    /*
     * Current computer player will discard the least useful tile.
     * this method should print what tile is discarded since it should be
     * known by other players. You may first discard duplicates and then
     * the single tiles and tiles that contribute to the smallest chains.
     */
    public void discardTileForComputer() {

        Player computer = players[currentPlayerIndex];
        Tile[] hand = computer.getTiles();
        int numTiles = computer.numberOfTiles;
        int discardIndex = -1;
        
        for (int i = 0; i < numTiles; i++) {
            for (int x = i + 1; x < numTiles; x++) {
                if (hand[i].compareTo(hand[x]) == 0) {
                    discardIndex = i;
                    break;
                }
            }
        }

        if (discardIndex != -1) {
            System.out.printf("%s discards %s%n", players[currentPlayerIndex].getName(), hand[discardIndex].toString());
            discardTile(discardIndex);
        }

        else {
            discardIndex = 0;
            int[] chainNum = new int[numTiles];
            for (int i = 0; i < numTiles; i++) {
                chainNum[i] = 0;
            }
            for (int i = 0; i < numTiles; i++) {
                for (int x = 0; x < numTiles; x++) {
                    if (hand[i].canFormChainWith(hand[x])) {
                        chainNum[i]++;
                    }
                }
            }
            for (int i = 0; i < numTiles; i++) {
                if (chainNum[discardIndex] > chainNum[i]) {
                    discardIndex = i;
                }
            }
            System.out.printf("%s discards %s%n", players[currentPlayerIndex].getName(), hand[discardIndex].toString());
            discardTile(discardIndex);
        }
    }

    /*
     * Discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        Tile discarded = players[currentPlayerIndex].getAndRemoveTile(tileIndex);
        lastDiscardedTile = discarded;
    }

    public void displayDiscardInformation() {
        if (lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if (index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
