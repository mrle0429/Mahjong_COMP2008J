package Model;

import Util.CheckTile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Hand {
    private List<Tile> tiles;
    private List<Tile> meldTiles;
    private boolean isDealingFinished;
    private TileSortType tileSortType;

    public Hand() {
        this.tiles = new ArrayList<>();
        this.meldTiles = new ArrayList<>();
        isDealingFinished = false;
        tileSortType = TileSortType.MinToMax;
    }

    public void addTile(Tile tile){
        tiles.add(tile);
        sortTiles(tiles);
    }

    public void addMeldTile(Tile tile){
        meldTiles.add(tile);
        sortTiles(meldTiles);
    }

    public boolean removeTile(Tile tile){
        boolean hasRemoved = tiles.remove(tile);
        if (hasRemoved){
            sortTiles(tiles);
        }
        return hasRemoved;
    }

    public void sortTiles(List<Tile> tiles){
        if (tileSortType == null){
            return;
        }
        tiles.sort(new Comparator<Tile>() {
            @Override
            public int compare(Tile t1, Tile t2) {
                // Compare the type of tile
                int typeCompare = t1.getTileType().compareTo(t2.getTileType());
                if (typeCompare != 0){
                    if (tileSortType == TileSortType.MinToMax){
                        return typeCompare;
                    }else{
                        return -typeCompare;
                    }
                }
                // If the type is same, compare their number
                if (CheckTile.isNumberType(t1)){
                    return t1.getValue() - t2.getValue();
                }else{
                    return t1.getCharacter().compareTo(t2.getCharacter());
                }
            }
        });
    }

    public boolean checkIsWin(){
        return CheckTile.isHu(tiles);
    }

    public boolean discardTile(TileStack tileStack, String tileName){
        Tile tile = CheckTile.findTile(tileName);
        if (tile == null){
            return false;
        }
        tileStack.playerDiscard(tile);  // 加入弃牌堆
        return tiles.remove(tile);      // 从手牌中删除
    }

    public boolean discardTile(TileStack tileStack, Tile tile){
        tileStack.playerDiscard(tile);
        return removeTile(tile);
    }

    public void showHandTiles(){
        for (int i = 0; i != tiles.size(); i++) {
            if ((i + 1) % 5 == 0){
                System.out.println(tiles.get(i));
            }else{
                System.out.print(tiles.get(i) + "\t");
            }
        }
        System.out.println();
    }



    // 对手牌执行操作，碰或杠
    public void operation(MeldType meldType, Tile tile) {
        List<Tile> pair;

        if (meldType == MeldType.PENG || meldType == MeldType.GANG){
            pair = CheckTile.findPair(tiles, meldType, tile);
        } else {
            pair = CheckTile.findSequence(tiles, tile);
            addMeldTile(tile); // 吃操作特有，因为吃操作返回的pair只是顺子的其他两个，因此需要把原先的牌加进去
        }

        for (Tile t : pair) {
            removeTile(t);
            addMeldTile(t);
        }
    }

    /**
     * 判断是否可以碰
     * 需要依据上一张弃牌
     * @param tile
     * @return
     */
    public boolean canPeng(Tile tile){
        return CheckTile.canPeng(tiles, tile);
    }

    public boolean canPeng(){
        List<Tile> result = CheckTile.canPeng(tiles);
        if (result.isEmpty()){
            return false;
        }
        for (Tile tile : result) {
            removeTile(tile);
            addMeldTile(tile);
        }
        return true;
    }



    public boolean canGang(){
        List<Tile> result = CheckTile.canGang(tiles);
        if (result.isEmpty()){
            return false;
        }
        for (Tile tile : result) {
            removeTile(tile);
            addMeldTile(tile);
        }
        return true;
    }

    public boolean canGang(Tile tile){
        return CheckTile.canGang(tiles, tile);
    }

    public boolean canEat(Tile tile){
        if (!CheckTile.isNumberType(tile)){
            return false;
        }
        return CheckTile.canEat(tiles, tile);
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
}
