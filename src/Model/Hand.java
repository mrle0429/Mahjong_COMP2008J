package Model;

import Util.CheckTile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Hand class represents the tiles that player can operate and the tiles that player can't operate
 * (After Pung, Kong, Chow)
 * <p>
 * The Hand class is responsible for the following operations:
 * 1. Draw a tile from the tile stack
 * 2. Discard a tile to the tile stack
 * 3. Check if the player has won the game
 * 4. Check if the player can Pung, Kong, Chow
 * 5. Execute the Pung, Kong, Chow operation
 */
public class Hand implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Tile> tiles;  // stores the tiles that player can operate
    private List<Tile> meldTiles;  // stores the tiles that player can't operate (After Pung, Kong, Chow)
    private boolean isDealingFinished;  // Flag to indicate whether the player has finished dealing

    private TileSortType tileSortType;  // Flag to indicate the sort type of tiles(Ascending or Descending)

    public Hand() {
        this.tiles = new ArrayList<>();
        this.meldTiles = new ArrayList<>();
        isDealingFinished = false;
        tileSortType = TileSortType.MinToMax;
    }


    private boolean addTileTo(List<Tile> tileList, Tile tile) {
        boolean hasAdd = tileList.add(tile);
        sortTiles(tileList);
        return hasAdd;
    }

    public boolean addTile(Tile tile) {
        return addTileTo(tiles, tile);
    }


    public boolean addMeldTile(Tile tile) {
        return addTileTo(meldTiles, tile);
    }

    public boolean removeTile(Tile tile) {
        boolean hasRemoved = tiles.remove(tile);
        if (hasRemoved) {
            sortTiles(tiles);
        }
        return hasRemoved;
    }


    /**
     * Check if the player has won the game
     *
     * @return True if the player has won the game, otherwise false
     */
    public boolean checkIsWin() {
        return isDealingFinished && CheckTile.isHu(tiles);
    }

    /**
     * Draw a tile from the tile stack.
     * This is fundamental operation in the game.
     *
     * @param tileStack The tile stack to draw a tile from
     * @return
     */
    public boolean drawTile(TileStack tileStack) {
        Tile tile = tileStack.drawTile();
        return addTile(tile);
    }

    /**
     * Discard a tile from the hand
     *
     * @param tileStack The tile stack to discard the tile to
     * @param tile      The tile to discard
     * @return
     */
    public boolean discardTile(TileStack tileStack, Tile tile) {
        tileStack.playerDiscardTile(tile);
        return removeTile(tile);
    }


    /**
     * Execute the chow operation
     *
     * @param tile The tile other player discarded
     */
    private List<Tile> chow(Tile tile) {
        // Find the sequence of tiles
        List<Tile> sequence = CheckTile.findSequence(tiles, tile);

        // Remove the tiles from the tiles and add them to the meldTiles
        for (Tile t : sequence) {
            removeTile(t);
            addMeldTile(t);
        }

        return sequence;
    }

    /**
     * Execute the pung operation
     *
     * @param tile The tile other player discarded
     */
    private List<Tile> pung(Tile tile) {
        // Find the triplet of tiles
        List<Tile> triplet = CheckTile.findPair(tiles, MeldType.PUNG, tile);

        // Remove the tiles from the tiles and add them to the meldTiles
        for (Tile t : triplet) {
            removeTile(t);
            addMeldTile(t);
        }

        return triplet;
    }

    /**
     * Execute the kong operation
     *
     * @param tile The tile other player discarded
     */
    private List<Tile> kong(Tile tile) {
        // Find the quad of tiles
        List<Tile> quad = CheckTile.findPair(tiles, MeldType.KONG, tile);

        // Remove the tiles from the tiles and add them to the meldTiles
        for (Tile t : quad) {
            removeTile(t);
            addMeldTile(t);
        }

        return quad;
    }

    /**
     * Execute the concealed kong operation
     */
    private List<Tile> concealedKong() {
        // Find the quad of tiles
        List<Tile> quad = CheckTile.findQuad(tiles);

        // Remove the tiles from the tiles and add them to the meldTiles
        for (Tile t : quad) {
            removeTile(t);
            addMeldTile(t);
        }

        return quad;
    }

    public List<Tile> operation(MeldType meldType, Tile tile) {
        if (meldType == MeldType.PUNG) {
            return pung(tile);
        } else if (meldType == MeldType.KONG) {
            return kong(tile);
        } else if (meldType == MeldType.CHOW) {
            return chow(tile);
        } else if (meldType == MeldType.CONCEALEDKONG) {
            return concealedKong();
        }
        return null;
    }


    public boolean canPung(Tile tile) {
        return tiles.size() > 1 && CheckTile.canPung(tiles, tile);
    }


    public boolean canKong(Tile tile) {
        return tiles.size() > 2 && CheckTile.canKong(tiles, tile);
    }


    public boolean canChow(Tile tile) {
        return tiles.size() > 1 && CheckTile.canChow(tiles, tile);
    }

    public boolean canConcealedKong() {
        return CheckTile.canConcealedKong(tiles);
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Tile> getMeldTiles() {
        return meldTiles;
    }

    public boolean isDealingFinished() {
        return isDealingFinished;
    }

    public void setDealingFinished() {
        isDealingFinished = true;
    }

    public void setMaxType() {
        this.tileSortType = TileSortType.MaxToMin;
        sortTiles(tiles);
    }

    public void setMinType() {
        this.tileSortType = TileSortType.MinToMax;
        sortTiles(tiles);
    }

    public TileSortType getTileSortType() {
        return tileSortType;
    }

    public void sortTiles(List<Tile> tiles) {
        if (tileSortType == null) {
            return;
        }
        tiles.sort(new Comparator<Tile>() {
            @Override
            public int compare(Tile t1, Tile t2) {
                // Compare the type of tile
                int typeCompare = t1.getTileType().compareTo(t2.getTileType());
                if (typeCompare != 0) {
                    if (tileSortType == TileSortType.MinToMax) {
                        return typeCompare;
                    } else {
                        return -typeCompare;
                    }
                }
                // If the type is same, compare their number
                if (CheckTile.isNumberType(t1)) {
                    return t1.getValue() - t2.getValue();
                } else {
                    return t1.getCharacter().compareTo(t2.getCharacter());
                }
            }
        });
    }
}
