package Model;

import Util.CheckTile;

import java.io.Serializable;

/**
 * The Tile class represents a tile in a Mahjong game.
 * <p>
 * Each tile has a type, represented by the TileType enum, and a value or character.
 * The value is used for numerical tiles (Dots, Bamboos, Craks), and the character is used for character tiles (Winds, Dragons).
 * <p>
 * The class provides methods to get the type, value, and character of the tile, and to check if two tiles are equal.
 */
public class Tile implements Serializable {
    private static final long serialVersionUID = 1L;
    private final TileType tileType;
    private int value;
    private String character;

    // Constructor for number type tile
    // - Dots, Bamboos, Craks.
    public Tile(TileType tileType, int value) {
        this.tileType = tileType;
        this.value = value;
    }

    // Constructor for character type tile
    // - Winds, Dragons.
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tile tile)) {
            return false;
        }
        if (CheckTile.isNumberType(this)) {
            return this.tileType == tile.getTileType() && this.value == tile.getValue();
        } else {
            return this.tileType == tile.getTileType() && this.character.equals(tile.getCharacter());
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tileType == null) ? 0 : tileType.hashCode());
        if (CheckTile.isNumberType(this)) {
            result = prime * result + value;
        } else {
            result = prime * result + ((character == null) ? 0 : character.hashCode());
        }
        return result;
    }
}
