import Model.Tile;
import Model.TileType;
import Util.CheckTile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    }
}
