package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Tile class represents a tile in a Mahjong game.
 *
 * Each tile has a type, represented by the TileType enum, and a value or character.
 * The value is used for numerical tiles (Dots, Bamboos, Craks), and the character is used for character tiles (Winds, Dragons).
 *
 * The class provides methods to get the type, value, and character of the tile, and to check if two tiles are equal.
 *
 */
public class TileStack implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Tile> tiles;
    private List<Tile> discardTiles;

    public TileStack() {
        tiles = new ArrayList<>();
        discardTiles = new ArrayList<>();
        initializeTiles();
        shuffle();
    }

    private void initializeTiles() {
        TileType[] numberTileTypes = new TileType[]{TileType.Crak, TileType.Dot, TileType.Bamboo};
        String[] windCharacters = new String[]{"North", "East", "West", "South"};
        String[] dragonCharacters = new String[]{"Red", "Green", "White"};

        // For number tiles
        createNumTile(numberTileTypes);

        // For wind tiles
        createWindTiles(windCharacters);

        // For dragon tiles
        createDragonTiles(dragonCharacters);

    }

    private void createDragonTiles(String[] dragonCharacters) {
        for (String dragon : dragonCharacters) {
            for (int i = 0; i != 4; i++) {
                tiles.add(new Tile(TileType.Dragon, dragon));
            }
        }
    }

    private void createWindTiles(String[] windCharacters) {
        for (String wind : windCharacters) {
            for (int i = 0; i != 4; i++) {
                tiles.add(new Tile(TileType.Wind, wind));
            }
        }
    }


    private void createNumTile(TileType[] numberTileTypes) {
        for (TileType tileType : numberTileTypes) {
            for (int value = 1; value != 10; value++) {
                for (int i = 0; i != 4; i++) {
                    tiles.add(new Tile(tileType, value));
                }
            }
        }
    }

    private void shuffle() {
        Collections.shuffle(tiles);
    }

    public Tile drawTile() {
        if (tiles.isEmpty()) {
            return null;
        }
        return tiles.remove(tiles.size() - 1);
    }

    public void playerDiscardTile(Tile tile) {
        discardTiles.add(tile);
    }


    public List<Tile> getDiscardTiles() {
        return discardTiles;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void resetTileStack() {
        tiles = new ArrayList<>();
        initializeTiles();
        shuffle();
    }

    public int size() {
        return tiles.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}
