package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileStack {
    private List<Tile> tiles;
    private List<Tile> discardTiles;
    private Tile jokerTile;

    public TileStack() {
        tiles = new ArrayList<>();
        discardTiles = new ArrayList<>();
        initializeTiles();
        shuffle();
    }

    private void initializeTiles(){
        TileType[] numberTileTypes = new TileType[]{TileType.Character, TileType.Circle, TileType.Bamboo};
        String[] windCharacters = new String[]{"North", "East", "West", "South"};
        String[] dragonCharacters = new String[]{"Red", "Green", "White"};

        // For number tiles
        for (TileType tileType : numberTileTypes) {
            for (int value = 1; value != 10; value++) {
                for (int i = 0; i != 4; i++) {
                    tiles.add(new Tile(tileType, value));
                }
            }
        }

        // For wind tiles
        for (String wind : windCharacters) {
            for (int i = 0; i != 4; i++) {
                tiles.add(new Tile(TileType.Wind, wind));
            }
        }

        // For dragon tiles
        for (String dragon : dragonCharacters) {
            for (int i = 0; i != 4; i++) {
                tiles.add(new Tile(TileType.Dragon, dragon));
            }
        }

        // For Joker tiles
        setJokerTile();

    }

    private void shuffle(){
        Collections.shuffle(tiles);
    }

    public void setJokerTile(){
        Tile tile = tiles.get((int) (Math.random() * 136));  // Randomly select a tile as joker
        for (Tile t : tiles) {

            if (t.equals(tile)){
                t.setLaiZi(true);
            }
        }
        jokerTile = tile;
    }

    public void playerDiscardTile(Tile tile){
        discardTiles.add(tile);
    }

    public Tile playerDrawTile(){
        if (tiles.isEmpty()){
            return null;
        }
        return tiles.remove(tiles.size() - 1); // Take the last one to player
    }

    public List<Tile> getDiscardTiles() {
        return discardTiles;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void resetGame(){
        tiles = new ArrayList<>();
        initializeTiles();
        shuffle();
    }


    public int size(){
        return tiles.size();
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public Tile getJokerTile() {
        return jokerTile;
    }
}
