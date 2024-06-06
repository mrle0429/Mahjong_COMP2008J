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
 *Test in order
 * Test valid seven-pair winning hand
 * Test invalid seven-pair winning hand (less than 14 cards)
 * Test invalid seven-pair winning hand (one card is not a pair)
 * Test valid seven-pair winning hand (mixed cards of different types)
 */
public class TestSevenPairs {

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
    public void testIsSevenPairsInvalid() {
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 1),
                new Tile(TileType.Character, 2), new Tile(TileType.Character, 2),
                new Tile(TileType.Character, 3), new Tile(TileType.Character, 3),
                new Tile(TileType.Character, 4), new Tile(TileType.Character, 4),
                new Tile(TileType.Character, 5), new Tile(TileType.Character, 5),
                new Tile(TileType.Character, 6), new Tile(TileType.Character, 6),
                new Tile(TileType.Character, 7)
        ));
        assertFalse(WaysOfHu.isSevenPairs(tiles));

        tiles = new ArrayList<>(Arrays.asList(
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
    public void testIsSevenPairsMixedTypes() {
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 1),
                new Tile(TileType.Circle, 2), new Tile(TileType.Circle, 2),
                new Tile(TileType.Bamboo, 3), new Tile(TileType.Bamboo, 3),
                new Tile(TileType.Character, 4), new Tile(TileType.Character, 4),
                new Tile(TileType.Circle, 5), new Tile(TileType.Circle, 5),
                new Tile(TileType.Bamboo, 6), new Tile(TileType.Bamboo, 6),
                new Tile(TileType.Character, 7), new Tile(TileType.Character, 7)
        ));
        assertTrue(WaysOfHu.isSevenPairs(tiles));
    }
}
