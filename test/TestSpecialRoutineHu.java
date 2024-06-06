import Model.Tile;
import Model.TileType;
import Util.WaysOfHu;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test in order
 * Test valid Pure Hand win (Qing Yi Se)
 * Test valid Zi Yi Se win
 *
 * Test valid DaSiXi win
 * Test invalid DaSiXi win
 *
 * Test valid all Pongs win
 * Test invalid all pongs win
 *
 */
public class TestSpecialRoutineHu {

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
                new Tile(TileType.Wind, "East"), new Tile(TileType.Wind, "East"),
                new Tile(TileType.Dragon, "Middle"), new Tile(TileType.Dragon, "Middle")
        ));
        assertTrue(WaysOfHu.isZiYiSe(tiles));

        tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 2),
                new Tile(TileType.Wind, "East")
        ));
        assertFalse(WaysOfHu.isZiYiSe(tiles));
    }

    @Test
    public void testIsDaSiXiValid() {
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Wind, "East"), new Tile(TileType.Wind, "East"), new Tile(TileType.Wind, "East"),
                new Tile(TileType.Wind, "South"), new Tile(TileType.Wind, "South"), new Tile(TileType.Wind, "South"),
                new Tile(TileType.Wind, "West"), new Tile(TileType.Wind, "West"), new Tile(TileType.Wind, "West"),
                new Tile(TileType.Wind, "North"), new Tile(TileType.Wind, "North"), new Tile(TileType.Wind, "North")
        ));
        assertTrue(WaysOfHu.isDaSiXi(tiles));
    }

    @Test
    public void testIsDaSiXiInvalid() {
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Wind, "East"), new Tile(TileType.Wind, "East"), new Tile(TileType.Wind, "East"),
                new Tile(TileType.Wind, "South"), new Tile(TileType.Wind, "South"), new Tile(TileType.Wind, "South"),
                new Tile(TileType.Wind, "West"), new Tile(TileType.Wind, "West"), new Tile(TileType.Wind, "West"),
                new Tile(TileType.Wind, "North"), new Tile(TileType.Wind, "North")
        ));
        assertFalse(WaysOfHu.isDaSiXi(tiles));
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

    @Test
    public void testIsAllPongsInvalid() {
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 1), new Tile(TileType.Character, 1),
                new Tile(TileType.Circle, 2), new Tile(TileType.Circle, 2)
        ));
        assertFalse(WaysOfHu.isAllPongs(tiles));

        tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 1), new Tile(TileType.Character, 1),
                new Tile(TileType.Circle, 2), new Tile(TileType.Circle, 2), new Tile(TileType.Circle, 2),
                new Tile(TileType.Bamboo, 3), new Tile(TileType.Bamboo, 3), new Tile(TileType.Bamboo, 4)
        ));
        assertFalse(WaysOfHu.isAllPongs(tiles));
    }


}
