package Model;

import java.util.List;

public class Player {
    private PlayerType location;
    private Hand hand;
    private boolean isWinner;
    private boolean isZhuang;

    public Player(PlayerType location) {
        this.location = location;
        hand = new Hand();
        isWinner = false;
        isZhuang = false;
    }

    public boolean drawTile(TileStack tileStack){  // 抓牌
        boolean hasDrawn = hand.drawTile(tileStack);
        if (hand.checkIsWin()) {
            isWinner = true;
        }
        return hasDrawn;
    }

    public boolean discardTile(TileStack tileStack, Tile tile){
        return hand.discardTile(tileStack, tile);
    }

    public void pengTile(Tile tile) {
        hand.operation(MeldType.PENG, tile);
    }

    public void gangTile(Tile tile) {
        hand.operation(MeldType.GANG, tile);
    }

    public void eatTile(Tile tile) {
        hand.operation(MeldType.EAT, tile);
    }

    public boolean checkPung(Tile tile) {
        return hand.canPeng(tile);
    }

    public boolean checkGang(Tile tile) {
        return hand.canGang(tile);
    }

    public boolean concealedKongTile() {
       return hand.concealedKong();
    }

    public boolean isWinner() {
        return isWinner;
    }

    public Hand getHand() {
        return hand;
    }

    public PlayerType getLocation() {
        return location;
    }

    public void setZhuang(boolean zhuang) {
        isZhuang = zhuang;
    }

    public boolean isZhuang() {
        return isZhuang;
    }

    @Override
    public String toString() {
        return this.location.toString();
    }

    public boolean firstDrawFinish() {
        return hand.isDealingFinished();
    }

    public List<Tile> getTiles() {
        return hand.getTiles();
    }

    public List<Tile> getMeldTiles() {
        return hand.getMeldTiles();
    }
}
