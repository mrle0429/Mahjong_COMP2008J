package Model;

import Util.CheckTile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Hand {
    private List<Tile> tiles;

    public Hand() {
        this.tiles = new ArrayList<>();
    }

    public void addTile(Tile tile){
        tiles.add(tile);
        sortTiles();
    }

    public boolean removeTile(Tile tile){
        boolean hasRemoved = tiles.remove(tile);
        if (hasRemoved){
            sortTiles();
        }
        return hasRemoved;
    }

    public void sortTiles(){
        tiles.sort(new Comparator<Tile>() {
            @Override
            public int compare(Tile t1, Tile t2) {
                // Compare the type of tile
                int typeCompare = t1.getTileType().compareTo(t2.getTileType());
                if (typeCompare != 0){
                    return typeCompare;
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
        if (tiles.size() != 14){
            return false;
        }
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
        List<Tile> pair = CheckTile.findPair(tiles, meldType, tile);
        tiles.removeAll(pair);    // 先把手牌中的对删除
        tiles.addAll(pair);       // 再把对加入到手牌中
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

    public boolean canGang(Tile tile){
        return CheckTile.canGang(tiles, tile);
    }

    public List<Tile> getTiles() {
        return tiles;
    }
}
