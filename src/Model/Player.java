package Model;

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

    public void drawTile(TileStack tileStack){  // 抓牌
        Tile tile = tileStack.takeTile();

        if (tile != null) {
            hand.addTile(tile);
            if (hand.checkIsWin()) {
                isWinner = true;
            }
        }
    }

    public boolean discardTile(TileStack tileStack, String tileName){
        if(!hand.discardTile(tileStack, tileName)){
            System.out.println("You just can discard tile that on your hand");
            return false;
        }
        return true;
    }

    public boolean discardTile(TileStack tileStack, Tile tile){
        return hand.discardTile(tileStack, tile);
    }

    public void showHandTiles(){
        hand.showHandTiles();
    }

    public void peng(Tile tile) {
        hand.operation(MeldType.PENG, tile);
    }

    public void gang(Tile tile) {
        hand.operation(MeldType.GANG, tile);
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
}
