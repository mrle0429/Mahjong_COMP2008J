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
        tileStack.playerDiscard(tile);
        return tiles.remove(tile);
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

    public boolean meldTiles(MeldType meldType){
        List<Tile> result = CheckTile.findMeld(tiles, meldType);
        if (result.isEmpty()){
            return false;
        }
        tiles.removeAll(result);     // 移除碰或杠的牌
        return tiles.addAll(result);  // 将碰和杠的牌加入到手牌中，包括其他人弃的那一张

    }

    // 对手牌执行操作，碰或杠
    public void operation(MeldType meldType, Tile tile) {
        List<Tile> pair = CheckTile.findPair(tiles, meldType, tile);
        tiles.removeAll(pair);    // 先把手牌中的对删除
        tiles.addAll(pair);       // 再把对加入到手牌中
    }

    public boolean canPeng(){
        return meldTiles(MeldType.PENG);
    }

    public boolean canGang(){
        return meldTiles(MeldType.GANG);
    }

    public List<Tile> getTiles() {
        return tiles;
    }
}
