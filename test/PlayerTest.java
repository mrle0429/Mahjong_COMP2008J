/**
 * @author Le Liu
 * @create 2024-06
 */
import Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;
    private TileStack tileStack;
    private Tile tile;

    @BeforeEach
    public void setUp() {
        player = new Player(PlayerType.East);
        tileStack = new TileStack();
        tile = new Tile(TileType.Dot, 2); // You may need to initialize your Tile here
    }

    @Test
    public void testDrawTile() {
        assertTrue(player.drawTile(tileStack));
        assertEquals(1, player.getHand().getTiles().size());
    }

    @Test
    public void testDiscardTile() {
        player.getHand().addTile(tile);
        assertTrue(player.discardTile(tileStack, tile));
        assertEquals(0, player.getHand().getTiles().size());
    }

    @Test
    public void testPungTile() {
        for (int i = 0; i < 2; i++) {
            player.getHand().addTile(tile);
        }
        player.pungTile(tile);
        assertEquals(0, player.getHand().getTiles().size());
        assertEquals(3, player.getHand().getMeldTiles().size());
    }

    @Test
    public void testKongTile() {
        for (int i = 0; i < 3; i++) {
            player.getHand().addTile(tile);
        }
        player.kongTile(tile);
        assertEquals(0, player.getHand().getTiles().size());
        assertEquals(4, player.getHand().getMeldTiles().size());
    }

    @Test
    public void testChowTile() {
        // You may need to add more tiles to the hand before testing chowTile
        Tile tile1 = new Tile(TileType.Dot, 1);
        Tile tile2 = new Tile(TileType.Dot, 2);
        Tile tile3 = new Tile(TileType.Dot, 3);
        player.getHand().addTile(tile1);
        player.getHand().addTile(tile2);
        player.chowTile(tile3);
        assertEquals(0, player.getHand().getTiles().size());
        assertEquals(3, player.getHand().getMeldTiles().size());
    }

    @Test
    public void testCheckPung() {
        player.getHand().addTile(tile);
        player.getHand().addTile(tile);
        assertTrue(player.checkPung(tile));
    }

    @Test
    public void testCheckKong() {
        for (int i = 0; i < 3; i++) {
            player.getHand().addTile(tile);
        }
        assertTrue(player.checkKong(tile));
    }

    @Test
    public void testCheckChow() {
        // You may need to add more tiles to the hand before testing checkChow
        player.getHand().addTile(tile);
        player.getHand().addTile(tile);
        assertFalse(player.checkChow(tile)); // Assuming the tiles are not in sequence
    }

    @Test
    public void testCheckConcealedKong() {
        for (int i = 0; i < 4; i++) {
            player.getHand().addTile(tile);
        }
        assertTrue(player.checkConcealedKong());
    }

    @Test
    public void testIsWinner() {
        for (int i = 0; i < 14; i++) {
            player.getHand().addTile(tile);
        }
        assertFalse(player.isWinner()); // Assuming the tiles do not form a winning hand
    }
}
