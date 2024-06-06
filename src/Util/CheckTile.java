package Util;

import Model.MeldType;
import Model.Tile;
import Model.TileType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The CheckTile class provides utility methods for checking various conditions in a game of Mahjong.
 * It includes methods for checking if a tile is of a number type, finding pairs and sequences of tiles,
 * and checking if certain actions can be performed (like Pung, Kong, Chow, and Concealed Kong).
 * It also includes a method for checking if a given set of tiles constitutes a winning hand (Hu).
 */
public class CheckTile {
    public static boolean isNumberType(TileType tileType) {
        return tileType == TileType.Character || tileType == TileType.Circle || tileType == TileType.Bamboo;
    }

    public static boolean isNumberType(Tile tile) {
        TileType tileType = tile.getTileType();
        return tileType == TileType.Character || tileType == TileType.Circle || tileType == TileType.Bamboo;
    }

    // Helper method for Pung & Kong
    public static List<Tile> findPair(List<Tile> tiles, MeldType meldType, Tile tile) {
        List<Tile> result = new ArrayList<>();
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

    // Helper method for Chow
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

    // Helper method for Concealed Kong
    public static List<Tile> findQuad(List<Tile> tiles) {
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

    public static boolean canPung(List<Tile> tiles, Tile tile) {
        List<Tile> result = findPair(tiles, MeldType.PUNG, tile);
        return !result.isEmpty();
    }

    public static boolean canKong(List<Tile> tiles, Tile tile) {
        List<Tile> result = findPair(tiles, MeldType.KONG, tile);
        return !result.isEmpty();
    }

    public static boolean canChow(List<Tile> tiles, Tile tile) {
        List<Tile> result = findSequence(tiles, tile);
        return !result.isEmpty();
    }

    public static boolean canConcealedKong(List<Tile> tiles) {
        List<Tile> result = findQuad(tiles);
        return !result.isEmpty();
    }


    public static boolean isHu(List<Tile> tiles) {

        return WaysOfHu.isRoutineHu(tiles, false) || WaysOfHu.isPureHand(tiles)
                || WaysOfHu.isSevenPairs(tiles)
                || WaysOfHu.isDeluxeSevenPairs(tiles) || WaysOfHu.isAllPongs(tiles)
                || WaysOfHu.isAllWinds(tiles);
    }


}
