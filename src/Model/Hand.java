package Model;

import Util.CheckTile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Hand implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Tile> tiles;
    private List<Tile> meldTiles;
    private boolean isDealingFinished;
    private TileSortType tileSortType;

    public Hand() {
        this.tiles = new ArrayList<>();
        this.meldTiles = new ArrayList<>();
        isDealingFinished = false;
        tileSortType = TileSortType.MinToMax;

    }

    // 为可操作牌堆添加牌，用于抓牌操作的辅助
    public boolean addTile(Tile tile) {
        boolean hasAdd = tiles.add(tile);
        sortTiles(tiles);
        return hasAdd;
    }

    // 为不可操作牌堆添加牌，用于碰杠吃操作的辅助
    private void addMeldTile(Tile tile) {
        meldTiles.add(tile);
        sortTiles(meldTiles);
    }

    // 从可操作牌堆中移除牌，用于打牌操作的辅助
    public boolean removeTile(Tile tile) {
        boolean hasRemoved = tiles.remove(tile);
        if (hasRemoved) {
            sortTiles(tiles);
        }
        return hasRemoved;
    }


    public boolean checkIsWin() {
        // 合并可操纵的牌和不可操作的牌。即玩家真正拥有的14张牌，判断是否胡牌
        List<Tile> hand = new ArrayList<>();
        hand.addAll(tiles);
        hand.addAll(meldTiles);
        return hand.size() == 14 && CheckTile.isHu(hand);
    }

    // 抓牌操作
    // 1. 从牌堆中抓一张牌
    // 2. 将抓到的牌加入到可操作牌堆中
    public boolean drawTile(TileStack tileStack) {
        Tile tile = tileStack.playerDrawTile();
        return addTile(tile);

    }


    // 打牌操作
    // 1. 将打出的牌加入到弃牌堆
    // 2. 从可操作牌堆中移除打出的牌
    public boolean discardTile(TileStack tileStack, Tile tile) {
        tileStack.playerDiscardTile(tile);
        return removeTile(tile);
    }

    // 吃操作
    // 1. 从可操作牌堆中找到连续的两张牌
    private void chow(Tile tile) {
        List<Tile> sequence = CheckTile.findSequence(tiles, tile);  // 返回吃后的三张牌
        for (Tile t : sequence) {
            removeTile(t);                     // 将sequence的牌，从可操纵手牌中移除，并加入不可操作牌中
            addMeldTile(t);
        }
    }

    private void pung(Tile tile) {
        List<Tile> triplet = CheckTile.findPair(tiles, MeldType.PENG, tile);
        for (Tile t : triplet) {
            removeTile(t);
            addMeldTile(t);
        }
    }

    private void kong(Tile tile) {
        List<Tile> quad = CheckTile.findPair(tiles, MeldType.GANG, tile);
        for (Tile t : quad) {
            removeTile(t);
            addMeldTile(t);
        }
    }

    private void concealedKong() {
        List<Tile> quad = CheckTile.findQuadForAngang(tiles);
        for (Tile t : quad) {
            removeTile(t);
            addMeldTile(t);
        }
    }

    // 对手牌执行操作，碰或杠或吃
    public void operation(MeldType meldType, Tile tile) {
        List<Tile> pair;
        if (meldType == MeldType.PENG) {
            pung(tile);
        } else if (meldType == MeldType.GANG) {
            kong(tile);
        } else if (meldType == MeldType.EAT) {
            chow(tile);
        } else if (meldType == MeldType.ANGANG) {
            concealedKong();
        }
    }

    // 判断是否可以暗杠
    // 不做操作
    public boolean canConcealedKong() {
        return CheckTile.canAnGang(tiles);

    }

    // 判断是否可以碰
    // 不做操作
    public boolean canPeng(Tile tile) {
        return tiles.size() > 1 && CheckTile.canPeng(tiles, tile);
    }

    // 判断是否可以杠
    // 不做操作
    public boolean canGang(Tile tile) {
        return tiles.size() > 2 && CheckTile.canGang(tiles, tile);
    }

    // 判断是否可以吃
    // 不做操作
    // 只有数字牌可以吃的逻辑移至findSequence
    public boolean canEat(Tile tile) {
        return tiles.size() > 1 && CheckTile.canEat(tiles, tile);
    }

    public boolean canAnGang() {
        return CheckTile.canAnGang(tiles);
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Tile> getMeldTiles() {
        return meldTiles;
    }

    public boolean isDealingFinished() {
        return isDealingFinished;
    }

    public void setDealingFinished() {
        isDealingFinished = true;
    }

    public void setMaxType() {
        this.tileSortType = TileSortType.MaxToMin;
        sortTiles(tiles);
    }

    public void setMinType() {
        this.tileSortType = TileSortType.MinToMax;
        sortTiles(tiles);
    }

    public TileSortType getTileSortType() {
        return tileSortType;
    }

    public void sortTiles(List<Tile> tiles) {
        if (tileSortType == null) {
            return;
        }
        tiles.sort(new Comparator<Tile>() {
            @Override
            public int compare(Tile t1, Tile t2) {
                // Compare the type of tile
                int typeCompare = t1.getTileType().compareTo(t2.getTileType());
                if (typeCompare != 0) {
                    if (tileSortType == TileSortType.MinToMax) {
                        return typeCompare;
                    } else {
                        return -typeCompare;
                    }
                }
                // If the type is same, compare their number
                if (CheckTile.isNumberType(t1)) {
                    return t1.getValue() - t2.getValue();
                } else {
                    return t1.getCharacter().compareTo(t2.getCharacter());
                }
            }
        });
    }
}
