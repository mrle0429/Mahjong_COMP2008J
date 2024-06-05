package Util;

import Model.Tile;
import Model.TileType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Map;

public class WaysOfHu {

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


    // Qi Dui

    public static boolean isSevenPairs(List<Tile> tiles) {
        if (tiles.size() != 14) {
            return false;
        }

        Collections.sort(tiles);

        for (int i = 0; i < tiles.size(); i += 2) {
            if (!tiles.get(i).equals(tiles.get(i + 1))) {
                return false;
            }
        }
        return true;
    }


    // Qing Yi Se
    public static boolean isPureHand(List<Tile> tiles) {
        TileType firstType = tiles.get(0).getTileType();
        for (Tile tile : tiles) {
            if (tile.getTileType() != firstType) {
                return false;
            }
        }
        return true;
    }

    // Zi Yi Se
    public static boolean isZiYiSe(List<Tile> tiles) {
        for (Tile tile : tiles) {
            if (!tile.isCharacter()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDaSiXi(List<Tile> tiles) {
        int[] windCounts = new int[4];
        for (Tile tile : tiles) {
            if (tile.isWind()) {
                windCounts[tile.getValue() - 1]++;
            }
        }
        for (int count : windCounts) {
            if (count < 3) {
                return false;
            }
        }
        return true;
    }

    public static boolean isXiaoSiXi(List<Tile> tiles) {
        int triplets = 0;
        int pair = 0;
        int[] windCounts = new int[4]; // 东南西北
        for (Tile tile : tiles) {
            if (tile.isWind()) {
                windCounts[tile.getValue() - 1]++;
            }
        }
        for (int count : windCounts) {
            if (count == 3) {
                triplets++;
            } else if (count == 2) {
                pair++;
            }
        }
        return triplets == 3 && pair == 1;
    }

    public static boolean isShiSanYao(List<Tile> tiles) {
        if (tiles.size() != 14) {
            return false;
        }
        int[] yaoTiles = {1, 9, 11, 19, 21, 29, 31, 32, 33, 34, 35, 36, 37};
        boolean[] found = new boolean[13];
        boolean hasPair = false;

        for (Tile tile : tiles) {
            int value = tile.getValue();
            for (int i = 0; i < yaoTiles.length; i++) {
                if (value == yaoTiles[i]) {
                    if (found[i]) {
                        hasPair = true;
                    } else {
                        found[i] = true;
                    }
                }
            }
        }

        for (boolean f : found) {
            if (!f) {
                return false;
            }
        }

        return hasPair;
    }
    

    // AllPongs

    public static boolean isAllPongs(List<Tile> tiles) {
        if (tiles.size() % 3 != 0) {
            return false;
        }

        Collections.sort(tiles);
        for (int i = 0; i < tiles.size(); i += 3) {
            if (!tiles.get(i).equals(tiles.get(i + 1)) || !tiles.get(i + 1).equals(tiles.get(i + 2))) {
                return false;
            }
        }
        return true;
    }

    // AllWinds
    public static boolean isAllWinds(List<Tile> tiles) {
        for (Tile tile : tiles) {
            if (tile.getTileType() != TileType.Wind) {
                return false;
            }
        }
        return true;
    }


    private static Map<Tile, Integer> countTiles(List<Tile> tiles) {
        Map<Tile, Integer> tileCount = new HashMap<>();
        for (Tile tile : tiles) {
            tileCount.put(tile, tileCount.getOrDefault(tile, 0) + 1);
        }
        return tileCount;
    }

    public static int calculateScore(List<Tile> tiles) {
        int fanNum = 1;
        if (isroutineHu(tiles, false)) {
            fanNum += 1; // 1  basic fan
        }
        if (isPureHand(tiles)) {
            fanNum += 4; // 4 fan
        }

        if (isSevenPairs(tiles)) {
            fanNum += Math.max(fanNum, 4); // 4 based on basic
        }

        if (isAllPongs(tiles)) {
            fanNum += Math.max(fanNum, 2);
        }
        if (isAllWinds(tiles)) {
            fanNum += Math.max(fanNum, 2);
        }
        return fanNum;
    }

}
