import Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {
    private Hand hand;
    private TileStack tileStack;
    private Tile tile;

    @BeforeEach
    public void setUp() {
        hand = new Hand();
        tileStack = new TileStack();
        tile = new Tile(TileType.Dot, 5); // You may need to initialize your Tile here
    }

    @Test
    public void testAddTile() {
        assertTrue(hand.addTile(tile));
        assertEquals(1, hand.getTiles().size());
    }

    @Test
    public void testRemoveTile() {
        hand.addTile(tile);
        assertTrue(hand.removeTile(tile));
        assertEquals(0, hand.getTiles().size());
    }

    @Test
    public void testAddMeldTile() {
        assertTrue(hand.addMeldTile(tile));
        assertEquals(1, hand.getMeldTiles().size());
    }

    @Test
    public void testDrawTile() {
        // You may need to add a tile to the tileStack before drawing
        assertTrue(hand.drawTile(tileStack));
        assertEquals(1, hand.getTiles().size());
    }

    @Test
    public void testDiscardTile() {
        hand.addTile(tile);
        assertTrue(hand.discardTile(tileStack, tile));
        assertEquals(0, hand.getTiles().size());
    }

    @Test
    public void testOperation() {
        hand.addTile(tile);
        hand.addTile(tile);
        hand.operation(MeldType.PUNG, tile);
        assertEquals(0, hand.getTiles().size());
        assertEquals(3, hand.getMeldTiles().size());
    }

    @Test
    public void testCanPung() {
        hand.addTile(tile);
        hand.addTile(tile);
        assertTrue(hand.canPung(tile));
    }

    @Test
    public void testCanKong() {
        hand.addTile(tile);
        hand.addTile(tile);
        hand.addTile(tile);
        assertTrue(hand.canKong(tile));
    }

    @Test
    public void testCanChow() {
        // You may need to add more tiles to the hand before testing canChow
        hand.addTile(tile);
        hand.addTile(tile);
        assertFalse(hand.canChow(tile)); // Assuming the tiles are not in sequence
    }

    @Test
    public void testCanConcealedKong() {
        hand.addTile(tile);
        hand.addTile(tile);
        hand.addTile(tile);
        hand.addTile(tile);
        assertTrue(hand.canConcealedKong());
    }


}