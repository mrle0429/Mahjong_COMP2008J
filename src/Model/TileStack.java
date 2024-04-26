package Model;

import java.util.ArrayList;
import java.util.Collections;
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

/*    public static void main(String[] args) {
        TileStack tileStack = new TileStack();
        System.out.println(tileStack.size());
        tileStack.showTiles();
    }*/

    private void initializeTiles(){
        TileType[] numberTileTypes = new TileType[]{TileType.Character, TileType.Circle, TileType.Bamboo};
        String[] windCharacters = new String[]{"North", "East", "West", "South"};
        String[] dragonCharacters = new String[]{"Red", "Green", "White"};
        String[] jokerCharacters = new String[]{"Joker1", "Joker2", "Joker3", "Joker4"};


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
        for (String joker: jokerCharacters){
            tiles.add(new Tile(TileType.Joker, joker));
        }

    }

    private void shuffle(){
        Collections.shuffle(tiles);
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

    public void showTiles(){
        for (int i = 0; i != tiles.size(); i++) {
            if ((i + 1) % 5 == 0){
                System.out.println(tiles.get(i));
            }else{
                System.out.print(tiles.get(i) + "\t");
            }
        }
        System.out.println();
    }

    public int size(){
        return tiles.size();
    }

    public boolean isEmpty(){
        return size() == 0;
    }


}
