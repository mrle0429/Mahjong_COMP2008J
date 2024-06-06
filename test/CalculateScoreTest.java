import Model.Tile;
import Model.TileType;
import Util.WaysOfHu;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateScoreTest {

    @Test
    public void testCalculateScoreRoutineHu() {
        // 常规胡牌
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 1),
                new Tile(TileType.Character, 2), new Tile(TileType.Character, 2),
                new Tile(TileType.Character, 3), new Tile(TileType.Character, 3),
                new Tile(TileType.Bamboo, 4), new Tile(TileType.Bamboo, 4),
                new Tile(TileType.Bamboo, 5), new Tile(TileType.Bamboo, 5),
                new Tile(TileType.Bamboo, 6), new Tile(TileType.Bamboo, 6),
                new Tile(TileType.Circle, 9), new Tile(TileType.Circle, 9)
        ));
        assertEquals(2, WaysOfHu.calculateScore(tiles), "Failed on routine Hu scoring");
    }

    @Test
    public void testCalculateScoreSevenPairs() {
        // Seven Dui
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 1),
                new Tile(TileType.Character, 2), new Tile(TileType.Character, 2),
                new Tile(TileType.Character, 3), new Tile(TileType.Character, 3),
                new Tile(TileType.Bamboo, 4), new Tile(TileType.Bamboo, 4),
                new Tile(TileType.Bamboo, 5), new Tile(TileType.Bamboo, 5),
                new Tile(TileType.Bamboo, 6), new Tile(TileType.Bamboo, 6),
                new Tile(TileType.Circle, 9), new Tile(TileType.Circle, 9)
        ));
        assertEquals(2, WaysOfHu.calculateScore(tiles), "Failed on seven pairs Hu scoring");
    }
    @Test
    public void testCalculateScorePureHand() {
        // Qing Yi Se
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Character, 1), new Tile(TileType.Character, 2),
                new Tile(TileType.Character, 3), new Tile(TileType.Character, 4),
                new Tile(TileType.Character, 5), new Tile(TileType.Character, 6),
                new Tile(TileType.Character, 7), new Tile(TileType.Character, 8),
                new Tile(TileType.Character, 9), new Tile(TileType.Character, 1),
                new Tile(TileType.Character, 2), new Tile(TileType.Character, 3),
                new Tile(TileType.Character, 4), new Tile(TileType.Character, 5)
        ));
        assertEquals(4, WaysOfHu.calculateScore(tiles), "Failed on pure hand Hu scoring");
    }

    @Test
    public void testCalculateScoreZiYiSe() {
        // Zi Yi Se
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Wind, "东"), new Tile(TileType.Wind, "东"), new Tile(TileType.Wind, "东"),
                new Tile(TileType.Wind, "南"), new Tile(TileType.Wind, "南"), new Tile(TileType.Wind, "南"),
                new Tile(TileType.Wind, "西"), new Tile(TileType.Wind, "西"), new Tile(TileType.Wind, "西"),
                new Tile(TileType.Wind, "北"), new Tile(TileType.Wind, "北"), new Tile(TileType.Wind, "北"),
                new Tile(TileType.Dragon, "中"), new Tile(TileType.Dragon, "中")
        ));
        assertEquals(5, WaysOfHu.calculateScore(tiles), "Failed on Zi Yi Se Hu scoring");
    }

    @Test
    public void testCalculateScoreDaSiXi() {
        // 大四喜胡牌
        List<Tile> tiles = new ArrayList<>(Arrays.asList(
                new Tile(TileType.Wind, "东"), new Tile(TileType.Wind, "东"), new Tile(TileType.Wind, "东"),
                new Tile(TileType.Wind, "南"), new Tile(TileType.Wind, "南"), new Tile(TileType.Wind, "南"),
                new Tile(TileType.Wind, "西"), new Tile(TileType.Wind, "西"), new Tile(TileType.Wind, "西"),
                new Tile(TileType.Wind, "北"), new Tile(TileType.Wind, "北"), new Tile(TileType.Wind, "北"),
                new Tile(TileType.Dragon, "中"), new Tile(TileType.Dragon, "中")
        ));
        assertEquals(5, WaysOfHu.calculateScore(tiles), "Failed on Da Si Xi Hu scoring");
    }
}
