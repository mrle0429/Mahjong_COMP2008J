package Util;

import Model.MeldType;
import Model.Tile;
import Model.TileType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckTile {
    public static boolean isNumberType(TileType tileType){
        return tileType == TileType.Character || tileType == TileType.Circle || tileType == TileType.Bamboo;
    }

    public static boolean isNumberType(Tile tile){
        TileType tileType = tile.getTileType();
        return tileType == TileType.Character || tileType == TileType.Circle || tileType == TileType.Bamboo;
    }

    public static Tile findTile(String tileName){
        if (tileName == null || tileName.isEmpty()){
            return null;
        }

        String[] strings = tileName.split("-");
        if (strings.length != 2){
            return null;
        }

        String type = strings[1];
        String value = strings[0];

        try{
            TileType tileType = TileType.valueOf(type);
            if (CheckTile.isNumberType(tileType)){
                int val = Integer.parseInt(value);
                return new Tile(tileType, val);
            }else{
                return new Tile(tileType, value);
            }
        }catch (RuntimeException e){
            throw new RuntimeException();
        }
    }

    /**
     * 手牌中找到能执行碰或杠的牌
     * 把他们加入result中
     *
     * @param tiles
     * @param meldType
     * @param tile
     * @return
     */
    public static List<Tile> findPair(List<Tile> tiles, MeldType meldType, Tile tile){
        List<Tile> result = new ArrayList<>();
        // 只写碰和杠
        if(meldType == MeldType.PENG) {
            for(int i = 0; i != tiles.size() - 1; i++) {
                if(tile.equals(tiles.get(i)) && tile.equals(tiles.get(i + 1))) {
                    result.add(tiles.get(i));
                    result.add(tiles.get(i + 1));
                    result.add(tile);
                }
            }
        } else if(meldType == MeldType.GANG) {
            for(int i = 0; i != tiles.size() - 2; i++) {
                if(tile.equals(tiles.get(i)) && tile.equals(tiles.get(i + 1)) && tile.equals(tiles.get(i + 2))) {
                    result.add(tiles.get(i));
                    result.add(tiles.get(i + 1));
                    result.add(tiles.get(i + 2));
                    result.add(tile);
                }
            }
        }
        return result;
    }

    public static List<Tile> findSequence(List<Tile> tiles, Tile tile){
        List<Tile> result = new ArrayList<>();
        TileType tileType = tile.getTileType();
        int tileValue = tile.getValue();

        int[][] possibleValues = {{tileValue + 1, tileValue + 2}, {tileValue - 1, tileValue + 1}, {tileValue - 2, tileValue - 1}};

        for (int[] values : possibleValues) {
            Tile seqTile = new Tile(tileType, values[0]);
            Tile seqTile_ = new Tile(tileType, values[1]);

            if (tiles.contains(seqTile_) && tiles.contains(seqTile)){
                result.add(seqTile);
                result.add(seqTile_);
                return result;
            }
        }
        return result;
    }

    public static boolean canPeng(List<Tile> tiles, Tile tile) {
        List<Tile> result = findPair(tiles, MeldType.PENG, tile);
        return !result.isEmpty();
    }

    public static boolean canGang(List<Tile> tiles, Tile tile) {
        List<Tile> result = findPair(tiles, MeldType.GANG, tile);
        return !result.isEmpty();
    }

    public static boolean canEat(List<Tile> tiles, Tile tile){
        List<Tile> result = findSequence(tiles, tile);
        return !result.isEmpty();
    }

    public static List<Tile> canPeng(List<Tile> tiles){
        List<Tile> result = new ArrayList<>();
        for (int i = 0; i != tiles.size() - 2; i++) {
            if (tiles.get(i).equals(tiles.get(i + 1)) &&
                    tiles.get(i).equals(tiles.get(i + 2))){
                result.add(tiles.get(i));
                result.add(tiles.get(i + 1));
                result.add(tiles.get(i + 2));
            }
        }
        return result;
    }

    public static List<Tile> canGang(List<Tile> tiles){
        List<Tile> result = new ArrayList<>();
        for (int i = 0; i != tiles.size() - 3; i++) {
            if (tiles.get(i).equals(tiles.get(i + 1)) &&
                    tiles.get(i).equals(tiles.get(i + 2)) &&
                    tiles.get(i).equals(tiles.get(i + 3))){
                result.add(tiles.get(i));
                result.add(tiles.get(i + 1));
                result.add(tiles.get(i + 2));
                result.add(tiles.get(i + 3));
            }
        }
        return result;
    }

    public static boolean isHu(List<Tile> tiles){
        return routineWay(tiles, false) || duiziHu(tiles);
        // TODO add more methods as Hu
    }

    private static boolean routineWay(List<Tile> tiles, boolean findPair){
        if (tiles.isEmpty()){
            return true;
        }

        // Test if it has pair
        if (!findPair){
            int firstIndex = 0;
            if (tiles.get(firstIndex).equals(tiles.get(firstIndex + 1)) && !tiles.get(firstIndex).equals(tiles.get(firstIndex + 2))){
                ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                newTiles.remove(firstIndex);
                newTiles.remove(firstIndex);
                if (routineWay(newTiles, true)){
                    return true;
                }
            }
            for (int i = 1; i != tiles.size() - 2; i++) {
                if (!tiles.get(i).equals(tiles.get(i - 1)) && tiles.get(i).equals(tiles.get(i + 1)) && !tiles.get(i + 1).equals(tiles.get(i + 2))){
                    ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                    newTiles.remove(i + 1);
                    newTiles.remove(i);
                    if (routineWay(newTiles, true)){
                        return true;
                    }
                }
            }
            int lastIndex = tiles.size() - 2;
            if (tiles.get(lastIndex).equals(tiles.get(lastIndex + 1))){
                ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                newTiles.remove(lastIndex);
                newTiles.remove(lastIndex);
                if (routineWay(newTiles, true)){
                    return true;
                }
            }
            return false; // Not find pair, not in this win way.
        }

        if (tiles.size() < 3){
            return false;
        }


        Tile firstTile = tiles.get(0);
        Tile secondTile = tiles.get(1);
        Tile thirdTile = tiles.get(2);

        // Test the other can be formed by Sequence
        if (CheckTile.isNumberType(firstTile)){
            if (firstTile.getTileType() == secondTile.getTileType() &&
            firstTile.getValue() + 1 == secondTile.getValue()){
                if (firstTile.getTileType() == thirdTile.getTileType()){
                    if (firstTile.getValue() + 2 == thirdTile.getValue()){
                        ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                        newTiles.remove(firstTile);
                        newTiles.remove(secondTile);
                        newTiles.remove(thirdTile);
                        return routineWay(newTiles, true);
                    }else{
                        if (tiles.size() > 3){
                            Tile fourthTile = tiles.get(3);
                            if (firstTile.getTileType() == fourthTile.getTileType() && firstTile.getValue() + 2 == fourthTile.getValue()){
                                ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                                newTiles.remove(firstTile);
                                newTiles.remove(secondTile);
                                newTiles.remove(fourthTile);
                                return routineWay(newTiles, true);
                            }
                        }
                    }
                }
            }
        }

        // Test the other can be formed by Set
        if (firstTile.equals(secondTile) && firstTile.equals(thirdTile)){
            ArrayList<Tile> newTiles = new ArrayList<>(tiles);
            newTiles.remove(firstTile);
            newTiles.remove(firstTile);
            newTiles.remove(firstTile);
            return routineWay(newTiles, true);
        }
        return false;
    }

    // TODO more ways of Hu
    private static boolean duiziHu(List<Tile> tiles) {   // 新增对子胡
        Map<Tile, Integer> tileCount = new HashMap<>();
        for (Tile tile : tiles) {
            tileCount.put(tile, tileCount.getOrDefault(tile, 0) + 1);
        }
        for(int count : tileCount.values()) {
            if (count % 2 != 0) {
                return false;
            }
        }
        return true;
    }
}
