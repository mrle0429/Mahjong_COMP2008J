package Model;

import java.io.Serializable;
import java.util.List;

/**
 * Player class represents the player in the game
 *
 * The Player class is responsible for the following operations:
 * 1. Draw a tile from the tile stack
 * 2. Discard a tile to the tile stack
 * 3. Check if the player has won the game
 * 4. Check if the player can Pung, Kong, Chow
 * 5. Execute the Pung, Kong, Chow operation
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private final PlayerType location;
    private Hand hand;
    private boolean isWinner;
    private boolean isBanker;
    private int score;

    public Player(PlayerType location) {
        this.location = location;
        hand = new Hand();
        isWinner = false;
        isBanker = false;
        score = 500;
    }

    public boolean drawTile(TileStack tileStack) {
        boolean hasDrawn = hand.drawTile(tileStack);
        if (hand.checkIsWin()) {
            isWinner = true;
        }
        return hasDrawn;
    }

    public boolean discardTile(TileStack tileStack, Tile tile) {
        return hand.discardTile(tileStack, tile);
    }

    public void pungTile(Tile tile) {
        hand.operation(MeldType.PUNG, tile);
    }

    public void kongTile(Tile tile) {
        hand.operation(MeldType.KONG, tile);
    }

    public void chowTile(Tile tile) {
        hand.operation(MeldType.CHOW, tile);
    }

    public void concealedKongTile() {
        hand.operation(MeldType.CONCEALEDKONG, null);
    }

    public boolean checkPung(Tile tile) {
        return hand.canPung(tile);
    }

    public boolean checkKong(Tile tile) {
        return hand.canKong(tile);
    }

    public boolean checkChow(Tile tile) {
        return hand.canChow(tile);
    }

    public boolean checkConcealedKong() {
        return hand.canConcealedKong();
    }

    public boolean isWinner() {
        return isWinner;
    }

    public Hand getHand() {
        return hand;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        if (score < 0) {
            return;
        }
        this.score = score;
    }

    public PlayerType getLocation() {
        return location;
    }

    public void setBanker(boolean banker) {
        isBanker = banker;
    }

    public boolean isBanker() {
        return isBanker;
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

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public int getTilesCount(){
        return this.getHand().getTiles().size();
    }
}
