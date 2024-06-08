package Util;

import Model.Tile;
import Model.TileType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The WaysOfHu class provides methods for checking various winning conditions in a game of Mahjong.
 * It includes methods for checking if a hand is a winning hand (Hu) in various ways
 * - routine Hu
 * - pure hand
 * - seven pairs
 * - deluxe seven pairs
 * - all pongs
 * - all winds
 * It also includes a method for
 * calculating the score of a hand based on these winning conditions.
 */
public class WaysOfHu {
    public static boolean isRoutineHu(List<Tile> tiles, boolean findPair){
        if (tiles.isEmpty()){
            return true;
        }

        // Test if it has pair
        if (!findPair) {

            if (tiles.size() < 2){
                return false;
            }

            if (tiles.size() == 2){
                return tiles.get(0).equals(tiles.get(1));
            }

            int firstIndex = 0;
            if (tiles.get(firstIndex).equals(tiles.get(firstIndex + 1)) && !tiles.get(firstIndex).equals(tiles.get(firstIndex + 2))) {
                ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                newTiles.remove(firstIndex);
                newTiles.remove(firstIndex);

                if (isRoutineHu(newTiles, true)){
                    return true;
                }
            }

            for (int i = 1; i != tiles.size() - 2; i++) {
                if (!tiles.get(i).equals(tiles.get(i - 1)) && tiles.get(i).equals(tiles.get(i + 1)) && !tiles.get(i + 1).equals(tiles.get(i + 2))) {
                    ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                    newTiles.remove(i + 1);
                    newTiles.remove(i);

                    if (isRoutineHu(newTiles, true)){

                        return true;
                    }
                }
            }

            int lastIndex = tiles.size() - 2;
            if (tiles.get(lastIndex).equals(tiles.get(lastIndex + 1))) {
                ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                newTiles.remove(lastIndex);
                newTiles.remove(lastIndex);

                return isRoutineHu(newTiles, true);
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

                        return isRoutineHu(newTiles, true);
                    }else{
                        if (tiles.size() > 3){

                            Tile fourthTile = tiles.get(3);
                            if (firstTile.getTileType() == fourthTile.getTileType() && firstTile.getValue() + 2 == fourthTile.getValue()) {
                                ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                                newTiles.remove(firstTile);
                                newTiles.remove(secondTile);
                                newTiles.remove(fourthTile);
                                return isRoutineHu(newTiles, true);
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
            return isRoutineHu(newTiles, true);
        }
        return false;
    }


    // PureHandHU
    public static boolean isPureHand(List<Tile> tiles) {
        if (tiles == null || tiles.isEmpty()) return false;

        TileType firstType = tiles.get(0).getTileType();
        if (!CheckTile.isNumberType(tiles.get(0))) {
            return false;
        }

        for (Tile tile : tiles) {
            if (tile.getTileType() != firstType || !CheckTile.isNumberType(tile)) {
                return false;
            }
        }
        return true;
    }


    // SevenPairsHu
    public static boolean isSevenPairs(List<Tile> tiles) {
        if (tiles.size() != 14) return false;
        Map<Tile, Integer> counts = countTiles(tiles);
        for (int count : counts.values()) {
            if (count != 2) {
                return false;
            }
        }
        return true;

    }

    // SevenPairsHuPlus
    public static boolean isDeluxeSevenPairs(List<Tile> tiles) {
        if (tiles.size() != 14) return false;
        Map<Tile, Integer> counts = countTiles(tiles);
        boolean hasQuad = false;
        for (int count : counts.values()) {
            if (count == 4) {
                hasQuad = true;
            } else if (count != 2) {
                return false;
            }
        }
        return hasQuad;
    }

    // AllPongs
    public static boolean isAllPongs(List<Tile> tiles) {
        Map<Tile, Integer> counts = countTiles(tiles);
        for (int count : counts.values()) {
            if (count != 3 && count != 4) {
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
        if (isRoutineHu(tiles, false)) {
            fanNum += 1; // 1  basic fan
        }
        if (isPureHand(tiles)) {
            fanNum += 4; // 4 fan
        }

        if (isSevenPairs(tiles)) {
            fanNum += Math.max(fanNum, 4); // 4 based on basic
        } else if (isDeluxeSevenPairs(tiles)) {
            fanNum += 8; // 8 based on fan 4
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
