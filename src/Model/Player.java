package Model;

import Util.CheckTile;

import java.util.List;

public class Player {
    private String name;
    private Hand hand;
    private boolean isWinner;
    private boolean isZhuang;

    public Player(String name) {
        this.name = name;
        hand = new Hand();
        isWinner = false;
    }

    public void catchTileFromStack(TileStack tileStack){
        Tile tile = tileStack.takeTile();
        if (tile != null){
            hand.addTile(tile);
            System.out.println(name + " catches a new tile: " + tile);

            if (hand.checkIsWin()){
                isWinner = true;
            }
        }else{
            System.out.println("There are not enough tiles for this game");
        }
    }

    public boolean catchTileFromTable(TileStack tileStack){
        List<Tile> discardTiles = tileStack.getDiscardTiles();
        if (discardTiles.isEmpty()){
            return false;
        }
        Tile tile = discardTiles.get(discardTiles.size() - 1);
        hand.addTile(tile);
        if (hand.checkIsWin()){
            isWinner = true;
            return true;
        } else if (hand.canGang()) {
            return true;
        } else if (hand.canPeng()) {
            return true;
        }else{
            hand.removeTile(tile);
        }
        return false;
    }

    public boolean discard(TileStack tileStack, String tileName){
        if(!hand.discardTile(tileStack, tileName)){
            System.out.println("You just can discard tile that on your hand");
            return false;
        }
        return true;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public void setZhuang(boolean zhuang) {
        isZhuang = zhuang;
    }

    public boolean isZhuang() {
        return isZhuang;
    }
}
