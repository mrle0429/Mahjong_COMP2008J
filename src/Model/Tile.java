package Model;

import Util.CheckTile;

import java.io.Serializable;

public class Tile implements Serializable, Comparable<Tile> {
    private static final long serialVersionUID = 1L;
    private final TileType tileType;
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
        if (!(o instanceof Tile tile)){
            return false;
        }
        if (CheckTile.isNumberType(this)){
            return this.tileType == tile.getTileType() && this.value == tile.getValue();
        }else{
            return this.tileType == tile.getTileType() && this.character.equals(tile.getCharacter());
        }
    }

    @Override
    public int hashCode() {
        if (CheckTile.isNumberType(this)) {
            return 31 * tileType.hashCode() + value;
        } else {
            return 31 * tileType.hashCode() + character.hashCode();
        }
    }

    @Override
    public int compareTo(Tile other) {
        int typeComparison = this.tileType.compareTo(other.tileType);
        if (typeComparison != 0) {
            return typeComparison;
        }
        if (CheckTile.isNumberType(this)) {
            return Integer.compare(this.value, other.value);
        } else {
            return this.character.compareTo(other.character);
        }
    }


    public boolean isCharacter() {
        return tileType == TileType.Wind || tileType == TileType.Dragon;
    }

    public boolean isWind() {
        return tileType == TileType.Wind;
    }
}
