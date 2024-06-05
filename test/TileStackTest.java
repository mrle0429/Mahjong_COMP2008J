import Model.Tile;
import Model.TileStack;
import Model.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TileStackTest {
    private TileStack tileStack;

    @BeforeEach
    public void setUp() {
        tileStack = new TileStack();
    }

    @Test
    public void testDrawTile() {
        Tile tile = tileStack.drawTile();
        assertNotNull(tile, "Drawn tile should not be null");
    }

    @Test
    public void testAddDiscardedTile() {
        Tile tile = new Tile(TileType.Bamboo, 5);
        tileStack.playerDiscardTile(tile);
        assertTrue(tileStack.getDiscardTiles().contains(tile), "Discarded tile should be in the discard pile");
    }

    @Test
    public void testResetTileStack() {
        tileStack.resetTileStack();
        assertEquals(136, tileStack.getTiles().size(), "Tile stack should be empty after reset");
        assertTrue(tileStack.getDiscardTiles().isEmpty(), "Discard pile should be empty after reset");
    }
}