import Model.Tile;
import Model.TileType;
import Util.CheckTile;
import Util.WaysOfHu;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCheckTile {

    @Test
    public void testHuiNRoutineWay(){
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(TileType.Crak, 1));
        tiles.add(new Tile(TileType.Crak, 1));
        tiles.add(new Tile(TileType.Crak, 1));
        tiles.add(new Tile(TileType.Crak, 2));
        tiles.add(new Tile(TileType.Crak, 2));
        tiles.add(new Tile(TileType.Crak, 2));
        tiles.add(new Tile(TileType.Bamboo, 3));
        tiles.add(new Tile(TileType.Bamboo, 3));
        tiles.add(new Tile(TileType.Bamboo, 3));
        tiles.add(new Tile(TileType.Dot, 6));
        tiles.add(new Tile(TileType.Dot, 6));
        tiles.add(new Tile(TileType.Crak, 7));
        tiles.add(new Tile(TileType.Crak, 8));
        tiles.add(new Tile(TileType.Crak, 9));

        assertTrue(CheckTile.isHu(tiles));

        tiles.clear();
    }

    @Test
    public void testIsPureHand() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(TileType.Crak, 1));
        tiles.add(new Tile(TileType.Crak, 3));
        tiles.add(new Tile(TileType.Crak, 5));
        tiles.add(new Tile(TileType.Crak, 7));
        tiles.add(new Tile(TileType.Crak, 9));
        tiles.add(new Tile(TileType.Crak, 2));
        tiles.add(new Tile(TileType.Crak, 4));
        tiles.add(new Tile(TileType.Crak, 1));
        tiles.add(new Tile(TileType.Crak, 3));
        tiles.add(new Tile(TileType.Crak, 5));
        tiles.add(new Tile(TileType.Crak, 7));
        tiles.add(new Tile(TileType.Crak, 9));
        tiles.add(new Tile(TileType.Crak, 2));
        tiles.add(new Tile(TileType.Crak, 4));
        assertTrue(CheckTile.isHu(tiles));


    }


    @Test
    public void testIsSevenPairs() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(TileType.Crak, 1));
        tiles.add(new Tile(TileType.Crak, 1));
        tiles.add(new Tile(TileType.Crak, 2));
        tiles.add(new Tile(TileType.Crak, 2));
        tiles.add(new Tile(TileType.Crak, 3));
        tiles.add(new Tile(TileType.Crak, 3));
        tiles.add(new Tile(TileType.Bamboo, 4));
        tiles.add(new Tile(TileType.Bamboo, 4));
        tiles.add(new Tile(TileType.Dot, 5));
        tiles.add(new Tile(TileType.Dot, 5));
        tiles.add(new Tile(TileType.Dragon, "Red"));
        tiles.add(new Tile(TileType.Dragon, "Red"));
        tiles.add(new Tile(TileType.Wind, "East"));
        tiles.add(new Tile(TileType.Wind, "East"));

        assertTrue(CheckTile.isHu(tiles), "Should be SevenPairsHu because there are seven distinct pairs of tiles");
    }

    @Test
    public void testIsDeluxeSevenPairs() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(TileType.Crak, 1));
        tiles.add(new Tile(TileType.Crak, 1));
        tiles.add(new Tile(TileType.Crak, 1));
        tiles.add(new Tile(TileType.Crak, 1));
        tiles.add(new Tile(TileType.Crak, 2));
        tiles.add(new Tile(TileType.Crak, 2));
        tiles.add(new Tile(TileType.Crak, 3));
        tiles.add(new Tile(TileType.Crak, 3));
        tiles.add(new Tile(TileType.Bamboo, 4));
        tiles.add(new Tile(TileType.Bamboo, 4));
        tiles.add(new Tile(TileType.Dot, 5));
        tiles.add(new Tile(TileType.Dot, 5));
        tiles.add(new Tile(TileType.Dragon, "Red"));
        tiles.add(new Tile(TileType.Dragon, "Red"));

        assertTrue(WaysOfHu.isDeluxeSevenPairs(tiles), "Should be DeluxeSevenPairsHu because there are four identical pairs and three distinct pairs of tiles");
    }


    @Test
    public void testIsAllPongs() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(TileType.Crak, 1));
        tiles.add(new Tile(TileType.Crak, 1));
        tiles.add(new Tile(TileType.Crak, 1));
        tiles.add(new Tile(TileType.Crak, 2));
        tiles.add(new Tile(TileType.Crak, 2));
        tiles.add(new Tile(TileType.Crak, 2));
        tiles.add(new Tile(TileType.Crak, 3));
        tiles.add(new Tile(TileType.Crak, 3));
        tiles.add(new Tile(TileType.Crak, 3));
        tiles.add(new Tile(TileType.Bamboo, 4));
        tiles.add(new Tile(TileType.Bamboo, 4));
        tiles.add(new Tile(TileType.Bamboo, 4));
        tiles.add(new Tile(TileType.Dot, 5));
        tiles.add(new Tile(TileType.Dot, 5));
        tiles.add(new Tile(TileType.Dot, 5));
        tiles.add(new Tile(TileType.Dot, 5));

        assertTrue(WaysOfHu.isAllPongs(tiles), "Should be AllPongsHu because all tiles are in sets of three or four");
    }


    @Test
    public void testIsAllWinds() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(TileType.Wind, "East"));
        tiles.add(new Tile(TileType.Wind, "East"));
        tiles.add(new Tile(TileType.Wind, "East"));
        tiles.add(new Tile(TileType.Wind, "West"));
        tiles.add(new Tile(TileType.Wind, "West"));
        tiles.add(new Tile(TileType.Wind, "West"));
        tiles.add(new Tile(TileType.Wind, "South"));
        tiles.add(new Tile(TileType.Wind, "South"));
        tiles.add(new Tile(TileType.Wind, "South"));
        tiles.add(new Tile(TileType.Wind, "North"));
        tiles.add(new Tile(TileType.Wind, "North"));
        tiles.add(new Tile(TileType.Wind, "North"));
        tiles.add(new Tile(TileType.Wind, "North"));

        assertTrue(WaysOfHu.isAllWinds(tiles), "Should be AllWindsHu because all tiles are wind tiles");
    }

}
