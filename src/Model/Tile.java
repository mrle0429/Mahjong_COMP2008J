package Model;

import Util.CheckTile;

public class Tile {
    private TileType tileType;
    private int value; // For WAN, TONG and TIAO
    private String character; // For FENG and JIAN
    private boolean isLaiZi;

    public Tile(TileType tileType, int value) {
        this.tileType = tileType;
        this.value = value;
    }

    public Tile(TileType tileType, String character) {
        this.tileType = tileType;
        this.character = character;
    }

    @Override
    public String toString() {
        if (CheckTile.isNumberType(this)) {
            return value + "-" + tileType.toString();
        } else {
            return character + "-" + tileType.toString();
        }
    }

    public TileType getTileType() {
        return tileType;
    }

    public int getValue() {
        return value;
    }

    public String getCharacter() {
        return character;
    }

    public boolean isLaiZi() {
        return isLaiZi;
    }

    public void setLaiZi(boolean laiZi) {
        isLaiZi = laiZi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof Tile)){
            return false;
        }
        Tile tile = (Tile) o;
        if (CheckTile.isNumberType(this)){
            return this.tileType == tile.getTileType() && this.value == tile.getValue();
        }else{
            return this.tileType == tile.getTileType() && this.character.equals(tile.getCharacter());
        }
    }
}
