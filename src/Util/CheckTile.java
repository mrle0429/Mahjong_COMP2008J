package Util;

import Model.MeldType;
import Model.Tile;
import Model.TileType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckTile {
    public static boolean isNumberType(TileType tileType) {
        return tileType == TileType.Character || tileType == TileType.Circle || tileType == TileType.Bamboo;
    }

    public static boolean isNumberType(Tile tile) {
        TileType tileType = tile.getTileType();
        return tileType == TileType.Character || tileType == TileType.Circle || tileType == TileType.Bamboo;
    }

    public static List<Tile> findPair(List<Tile> tiles, MeldType meldType, Tile tile) {
        List<Tile> result = new ArrayList<>();
        // 只写碰和杠
        if (meldType == MeldType.PUNG) {
            for (int i = 0; i != tiles.size() - 1; i++) {
                if (tile.equals(tiles.get(i)) && tile.equals(tiles.get(i + 1))) {
                    result.add(tiles.get(i));
                    result.add(tiles.get(i + 1));
                    result.add(tile);
                }
            }
        } else if (meldType == MeldType.KONG) {
            for (int i = 0; i != tiles.size() - 2; i++) {
                if (tile.equals(tiles.get(i)) && tile.equals(tiles.get(i + 1)) && tile.equals(tiles.get(i + 2))) {
                    result.add(tiles.get(i));
                    result.add(tiles.get(i + 1));
                    result.add(tiles.get(i + 2));
                    result.add(tile);
                }
            }
        }
        return result;
    }

    // 找到手牌中连续两张牌，用于吃操作
    public static List<Tile> findSequence(List<Tile> tiles, Tile tile) {
        List<Tile> result = new ArrayList<>();

        TileType tileType = tile.getTileType();

        // 只有数字牌才能吃
        if (!isNumberType(tileType)) {
            return result;
        }

        int tileValue = tile.getValue();

        // 调整可能吃的情况
        // 只能吃首尾，不能吃中间
        int[][] possibleValues = {{tileValue + 1, tileValue + 2}, {tileValue - 2, tileValue - 1}};

        for (int[] values : possibleValues) {
            Tile seqTile = new Tile(tileType, values[0]);
            Tile seqTile_ = new Tile(tileType, values[1]);

            if (tiles.contains(seqTile_) && tiles.contains(seqTile)) {  // 根据Tile类中的equal方式判断。牌类型+牌数字
                result.add(seqTile);
                result.add(seqTile_);
                result.add(tile);
                return result;
            }
        }
        return result;
    }

    // 暗杠,玩家手牌中有四张相同的牌.仅考虑同时存在一组暗杠
    // Todo: 使用了一种新的找四个相同的牌的方法。可与先前的找三元组和四元组的方法进行比较，并选择最优方法
    public static List<Tile> findQuadForAngang(List<Tile> tiles) {
        List<Tile> result = new ArrayList<>();
        Map<Tile, Integer> tileCount = new HashMap<>();
        for (Tile tile : tiles) {
            tileCount.put(tile, tileCount.getOrDefault(tile, 0) + 1);
        }
        for (Tile tile : tileCount.keySet()) {
            if (tileCount.get(tile) == 4) {
                for (int i = 0; i < 4; i++) {
                    result.add(tile);
                }
                return result;

            }
        }
        return result;
    }

    public static boolean canPeng(List<Tile> tiles, Tile tile) {
        List<Tile> result = findPair(tiles, MeldType.PUNG, tile);
        return !result.isEmpty();
    }

    public static boolean canGang(List<Tile> tiles, Tile tile) {
        List<Tile> result = findPair(tiles, MeldType.KONG, tile);
        return !result.isEmpty();
    }

    public static boolean canEat(List<Tile> tiles, Tile tile) {
        List<Tile> result = findSequence(tiles, tile);
        return !result.isEmpty();
    }

    public static boolean canAnGang(List<Tile> tiles) {
        List<Tile> result = findQuadForAngang(tiles);
        return !result.isEmpty();
    }

    public static List<Tile> canPeng(List<Tile> tiles) {
        List<Tile> result = new ArrayList<>();
        for (int i = 0; i != tiles.size() - 2; i++) {
            if (tiles.get(i).equals(tiles.get(i + 1)) &&
                    tiles.get(i).equals(tiles.get(i + 2))) {
                result.add(tiles.get(i));
                result.add(tiles.get(i + 1));
                result.add(tiles.get(i + 2));
            }
        }
        return result;
    }

    public static List<Tile> canGang(List<Tile> tiles) {
        List<Tile> result = new ArrayList<>();
        for (int i = 0; i != tiles.size() - 3; i++) {
            if (tiles.get(i).equals(tiles.get(i + 1)) &&
                    tiles.get(i).equals(tiles.get(i + 2)) &&
                    tiles.get(i).equals(tiles.get(i + 3))) {
                result.add(tiles.get(i));
                result.add(tiles.get(i + 1));
                result.add(tiles.get(i + 2));
                result.add(tiles.get(i + 3));
            }
        }
        return result;
    }


    public static boolean isHu(List<Tile> tiles){
     
        return isroutineHu(tiles, false) || WaysOfHu.isPureHand(tiles)
                || WaysOfHu.isSevenPairs(tiles)
                || WaysOfHu.isDeluxeSevenPairs(tiles) || WaysOfHu.isAllPongs(tiles)
                || WaysOfHu.isAllWinds(tiles);
    }

    public static boolean isroutineHu(List<Tile> tiles, boolean findPair){
      // 可操作手牌为空
        if (tiles.isEmpty()){
            return true;
        }

        // Test if it has pair
        if (!findPair) {

            if (tiles.size() == 2){
                return tiles.get(0).equals(tiles.get(1));
            }

            int firstIndex = 0;
            if (tiles.get(firstIndex).equals(tiles.get(firstIndex + 1)) && !tiles.get(firstIndex).equals(tiles.get(firstIndex + 2))) {
                ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                newTiles.remove(firstIndex);
                newTiles.remove(firstIndex);

                if (isroutineHu(newTiles, true)){
                    return true;
                }
            }
          
            for (int i = 1; i != tiles.size() - 2; i++) {
                if (!tiles.get(i).equals(tiles.get(i - 1)) && tiles.get(i).equals(tiles.get(i + 1)) && !tiles.get(i + 1).equals(tiles.get(i + 2))) {
                    ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                    newTiles.remove(i + 1);
                    newTiles.remove(i);

                    if (isroutineHu(newTiles, true)){

                        return true;
                    }
                }
            }
          
            int lastIndex = tiles.size() - 2;
            if (tiles.get(lastIndex).equals(tiles.get(lastIndex + 1))) {
                ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                newTiles.remove(lastIndex);
                newTiles.remove(lastIndex);

                return isroutineHu(newTiles, true);
            }
            return false; // Not find pair, not in this win way.
        }

        if (tiles.size() < 3) {
            return false;
        }


        Tile firstTile = tiles.get(0);
        Tile secondTile = tiles.get(1);
        Tile thirdTile = tiles.get(2);

        // Test the other can be formed by Sequence
        if (CheckTile.isNumberType(firstTile)) {
            if (firstTile.getTileType() == secondTile.getTileType() &&
                    firstTile.getValue() + 1 == secondTile.getValue()) {
                if (firstTile.getTileType() == thirdTile.getTileType()) {
                    if (firstTile.getValue() + 2 == thirdTile.getValue()) {
                        ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                        newTiles.remove(firstTile);
                        newTiles.remove(secondTile);
                        newTiles.remove(thirdTile);

                        return isroutineHu(newTiles, true);
                    }else{
                        if (tiles.size() > 3){

                            Tile fourthTile = tiles.get(3);
                            if (firstTile.getTileType() == fourthTile.getTileType() && firstTile.getValue() + 2 == fourthTile.getValue()) {
                                ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                                newTiles.remove(firstTile);
                                newTiles.remove(secondTile);
                                newTiles.remove(fourthTile);
                                return isroutineHu(newTiles, true);
                            }
                        }
                    }
                }
            }
        }

        // Test the other can be formed by Set
        if (firstTile.equals(secondTile) && firstTile.equals(thirdTile)) {
            ArrayList<Tile> newTiles = new ArrayList<>(tiles);
            newTiles.remove(firstTile);
            newTiles.remove(firstTile);
            newTiles.remove(firstTile);
            return isroutineHu(newTiles, true);
        }
        return false;
    }

}
