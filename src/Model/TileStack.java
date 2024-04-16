package Model;

import Util.CheckTile;
import Util.ResetTiles;

import java.util.ArrayList;
import java.util.List;

public class TileStack {
    private List<Tile> tiles;
    private List<Tile> discardTiles;

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
        String[] seasonCharacters = new String[]{"Spring", "Summer", "Autumn", "Winter"};
        String[] flowerCharacters = new String[]{"Plum", "Orchid", "Chrysan_themum", "Bamboo"};

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

        // For season tiles
        for (String season : seasonCharacters) {
            for (int i = 0; i != 4; i++) {
                tiles.add(new Tile(TileType.Season, season));
            }
        }

        // For flower tiles
        for (String flower : flowerCharacters) {
            for (int i = 0; i != 4; i++) {
                tiles.add(new Tile(TileType.Flower, flower));
            }
        }

    }

    private void shuffle(){
        ResetTiles.randomSort(tiles);
    }

    public void playerDiscard(Tile tile){
        discardTiles.add(tile);
    }

    public Tile takeTile(){
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
}
