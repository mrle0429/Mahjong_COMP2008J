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
