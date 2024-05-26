package Util;

import Model.Tile;
import Model.TileType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaysOfHu {

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
        System.out.println("isSevenPairs");
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
        System.out.println("isDeluxeSevenPairs");
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
        System.out.println("isAllPongs");
        return true;
    }

    // AllWinds
    public static boolean isAllWinds(List<Tile> tiles) {
        for (Tile tile : tiles) {
            if (tile.getTileType() != TileType.Wind) {
                return false;
            }
        }
        System.out.println("isAllWinds");
        return true;
    }


//    // ThirteenOrphans
//    public static boolean isThirteenOrphans(List<Tile> tiles) {
//        if (tiles.size() != 14) return false; // 必须正好14张牌
//
//        // 国士无双的必需牌型
//        List<Tile> requiredTiles = List.of(
//                new Tile(TileType.Character,1), new Tile(TileType.Character, 9),
//                new Tile(TileType.Bamboo, 1), new Tile(TileType.Bamboo, 9),
//                new Tile(TileType.Circle, 1), new Tile(TileType.Circle, 9),
//                new Tile(TileType.Wind, "East"), new Tile(TileType.Wind, "South"),
//                new Tile(TileType.Wind, "West"), new Tile(TileType.Wind, "North"),
//                new Tile(TileType.Dragon, "Zhong"), new Tile(TileType.Dragon, "Fa"),
//                new Tile(TileType.Dragon, "White")
//        );
//        System.out.println("isThirteenOrphans");
//
//        Map<Tile, Integer> counts = countTiles(tiles);
//        int pairCount = 0;
//        for (Tile requiredTile : requiredTiles) {
//            int count = counts.getOrDefault(requiredTile, 0);
//            if (count == 0) {
//                return false;
//            } else if (count > 1) {
//                pairCount++;
//            }
//        }
//        return pairCount == 1;
//
//    }


    private static Map<Tile, Integer> countTiles(List<Tile> tiles) {
        Map<Tile, Integer> tileCount = new HashMap<>();
        for (Tile tile : tiles) {
            tileCount.put(tile, tileCount.getOrDefault(tile, 0) + 1);
        }
        return tileCount;
    }

    public static int calculateScore(List<Tile> tiles) {
        System.out.println("WaysOfHu calculateScore");
        int fanNum = 1;
        if (CheckTile.isroutineHu(tiles, false)) {
            fanNum += 1; // 1  basic fan
        }
        if (isPureHand(tiles)) {
            fanNum += 4; // 4 fan
        }

        if (isSevenPairs(tiles)) {
            fanNum += Math.max(fanNum, 4); // 4 based on basic
        }else if (isDeluxeSevenPairs(tiles)) {
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
