import Model.Tile;
import Model.TileType;
import Util.CheckTile;
import Util.WaysOfHu;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCheckTile {

    @Test
    public void testHuiNRoutineWay(){
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(TileType.Character, 1));
        tiles.add(new Tile(TileType.Character, 1));
        tiles.add(new Tile(TileType.Character, 1));
        tiles.add(new Tile(TileType.Character, 2));
        tiles.add(new Tile(TileType.Character, 2));
        tiles.add(new Tile(TileType.Character, 2));
        tiles.add(new Tile(TileType.Bamboo, 3));
        tiles.add(new Tile(TileType.Bamboo, 3));
        tiles.add(new Tile(TileType.Bamboo, 3));
        tiles.add(new Tile(TileType.Circle, 6));
        tiles.add(new Tile(TileType.Circle, 6));
        tiles.add(new Tile(TileType.Character, 7));
        tiles.add(new Tile(TileType.Character, 8));
        tiles.add(new Tile(TileType.Character, 9));

        assertTrue(CheckTile.isHu(tiles));

        tiles.clear();

        tiles.add(new Tile(TileType.Character, 1));
        tiles.add(new Tile(TileType.Character, 2));
        tiles.add(new Tile(TileType.Character, 2));
        tiles.add(new Tile(TileType.Character, 3));
        tiles.add(new Tile(TileType.Character, 3));
        tiles.add(new Tile(TileType.Character, 4));
        tiles.add(new Tile(TileType.Character, 5));
        tiles.add(new Tile(TileType.Character, 5));

        assertTrue(CheckTile.isHu(tiles));

        tiles.clear();

        tiles.add(new Tile(TileType.Circle, 9));
        tiles.add(new Tile(TileType.Circle, 9));
        tiles.add(new Tile(TileType.Circle, 9));
        tiles.add(new Tile(TileType.Circle, 7));
        tiles.add(new Tile(TileType.Circle, 7));
        tiles.add(new Tile(TileType.Wind, "West"));
        tiles.add(new Tile(TileType.Wind, "West"));
        tiles.add(new Tile(TileType.Wind, "West"));
        assertTrue(CheckTile.isHu(tiles));
    }

    @Test
    public void testIsPureHand() {
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 2),
                new Tile(TileType.Character, 3), new Tile(TileType.Character, 4),
                new Tile(TileType.Character, 5), new Tile(TileType.Character, 6)
        ));
        assertTrue(WaysOfHu.isPureHand(tiles));

        tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 2),
                new Tile(TileType.Character, 3), new Tile(TileType.Bamboo, 4),
                new Tile(TileType.Bamboo, 5), new Tile(TileType.Bamboo, 6)
        ));
        assertFalse(WaysOfHu.isPureHand(tiles));
    }

    @Test
    public void testIsZiYiSe() {
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Wind, "东"), new Tile(TileType.Wind, "东"),
                new Tile(TileType.Dragon, "中"), new Tile(TileType.Dragon, "中")
        ));
        assertTrue(WaysOfHu.isZiYiSe(tiles));

        tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 2),
                new Tile(TileType.Wind, "东")
        ));
        assertFalse(WaysOfHu.isZiYiSe(tiles));
    }


    @Test
    public void testIsSevenPairsValid() {
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 1),
                new Tile(TileType.Character, 2), new Tile(TileType.Character, 2),
                new Tile(TileType.Character, 3), new Tile(TileType.Character, 3),
                new Tile(TileType.Character, 4), new Tile(TileType.Character, 4),
                new Tile(TileType.Character, 5), new Tile(TileType.Character, 5),
                new Tile(TileType.Character, 6), new Tile(TileType.Character, 6),
                new Tile(TileType.Character, 7), new Tile(TileType.Character, 7)
        ));
        assertTrue(WaysOfHu.isSevenPairs(tiles));
    }

    @Test
    public void testIsSevenPairsMixedInvalid() {
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 1),
                new Tile(TileType.Character, 2), new Tile(TileType.Character, 2),
                new Tile(TileType.Character, 3), new Tile(TileType.Character, 3),
                new Tile(TileType.Character, 4), new Tile(TileType.Character, 4),
                new Tile(TileType.Character, 5), new Tile(TileType.Character, 5),
                new Tile(TileType.Character, 6), new Tile(TileType.Character, 6),
                new Tile(TileType.Character, 7), new Tile(TileType.Character, 8)
        ));
        assertFalse(WaysOfHu.isSevenPairs(tiles));
    }

    @Test
    public void testIsAllWindsValid() {
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Wind, "东"), new Tile(TileType.Wind, "东"), new Tile(TileType.Wind, "东"),
                new Tile(TileType.Wind, "南"), new Tile(TileType.Wind, "南"), new Tile(TileType.Wind, "南"),
                new Tile(TileType.Wind, "西"), new Tile(TileType.Wind, "西"), new Tile(TileType.Wind, "西")
        ));
        assertTrue(WaysOfHu.isAllWinds(tiles));
    }

    @Test
    public void testIsAllPongsValid() {
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 1), new Tile(TileType.Character, 1),
                new Tile(TileType.Circle, 2), new Tile(TileType.Circle, 2), new Tile(TileType.Circle, 2),
                new Tile(TileType.Bamboo, 3), new Tile(TileType.Bamboo, 3), new Tile(TileType.Bamboo, 3)
        ));
        assertTrue(WaysOfHu.isAllPongs(tiles));

        tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 1), new Tile(TileType.Character, 1),
                new Tile(TileType.Circle, 2), new Tile(TileType.Circle, 2), new Tile(TileType.Circle, 2),
                new Tile(TileType.Bamboo, 3), new Tile(TileType.Bamboo, 3), new Tile(TileType.Bamboo, 3),
                new Tile(TileType.Character, 4), new Tile(TileType.Character, 4), new Tile(TileType.Character, 4)
        ));
        assertTrue(WaysOfHu.isAllPongs(tiles));
    }
}
