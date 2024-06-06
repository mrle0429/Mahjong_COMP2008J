import Model.Tile;
import Model.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The TileTest class provides unit tests for the Tile class.
 */
public class TileTest {
    private Tile tile;

    @BeforeEach
    public void setUp() {
        tile = new Tile(TileType.Circle, 5);
    }

    @Test
    public void testGetTileType() {
        assertEquals(TileType.Circle, tile.getTileType());
    }

    @Test
    public void testGetValue() {
        assertEquals(5, tile.getValue());
    }

    @Test
    public void testEquals() {
        Tile sameTile = new Tile(TileType.Circle, 5);
        Tile differentTile = new Tile(TileType.Bamboo, 5);

        assertTrue(tile.equals(sameTile));
        assertFalse(tile.equals(differentTile));
    }
}
