package Util;

import Model.MeldType;
import Model.Tile;
import Model.TileType;

import java.util.ArrayList;
import java.util.List;

public class CheckTile {
    public static boolean isNumberType(TileType tileType){
        return tileType == TileType.Character || tileType == TileType.Circle || tileType == TileType.Bamboo;
    }

    public static boolean isNumberType(Tile tile){
        TileType tileType = tile.getTileType();
        return tileType == TileType.Character || tileType == TileType.Circle || tileType == TileType.Bamboo;
    }

    public static Tile findTile(String tileName){
        if (tileName == null || tileName.isEmpty()){
            return null;
        }

        String[] strings = tileName.split("-");
        if (strings.length != 2){
            return null;
        }

        String type = strings[1];
        String value = strings[0];

        try{
            TileType tileType = TileType.valueOf(type);
            if (CheckTile.isNumberType(tileType)){
                int val = Integer.parseInt(value);
                return new Tile(tileType, val);
            }else{
                return new Tile(tileType, value);
            }
        }catch (RuntimeException e){
            throw new RuntimeException();
        }
    }

    public static List<Tile> findMeld(List<Tile> tiles, MeldType meldType){
        List<Tile> result = new ArrayList<>();
        if (meldType == MeldType.EAT){
            for (int i = 0; i != tiles.size() - 2; i++) {
                if (tiles.get(i).getTileType() == tiles.get(i + 1).getTileType() && tiles.get(i).getTileType() == tiles.get(i + 2).getTileType()
                && tiles.get(i).getValue() + 1 == tiles.get(i + 1).getValue() && tiles.get(i).getValue() + 2 == tiles.get(i + 2).getValue()){
                    result.add(tiles.get(i));
                    result.add(tiles.get(i + 1));
                    result.add(tiles.get(i + 2));
                }
            }
        }else{
            for (int i = 0; i != tiles.size() - 2; i++) {
                if (meldType == MeldType.PENG && tiles.get(i).equals(tiles.get(i + 1)) &&
                tiles.get(i).equals(tiles.get(i + 2))){
                    result.add(tiles.get(i));
                    result.add(tiles.get(i + 1));
                    result.add(tiles.get(i + 2));
                } else if (i + 3 < tiles.size() && tiles.get(i).equals(tiles.get(i + 1)) &&
                tiles.get(i).equals(tiles.get(i + 2)) && tiles.get(i).equals(tiles.get(i + 3))) {
                    result.add(tiles.get(i));
                    result.add(tiles.get(i + 1));
                    result.add(tiles.get(i + 2));
                    result.add(tiles.get(i + 3));
                }
            }
        }
        return result;
    }

    public static boolean isHu(List<Tile> tiles){
        return routineWay(tiles, false);
        // TODO add more methods as Hu
    }

    private static boolean routineWay(List<Tile> tiles, boolean findPair){
        if (tiles.isEmpty()){
            return true;
        }

        // Test if it has pair
        if (!findPair){
            for (int i = 0; i != tiles.size() - 2; i++) {
                if (tiles.get(i).equals(tiles.get(i + 1)) && !tiles.get(i).equals(tiles.get(i + 2))){
                    ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                    newTiles.remove(i + 1);
                    newTiles.remove(i);
                    return routineWay(newTiles, true);
                }
            }
            int lastIndex = tiles.size() - 2;
            if (tiles.get(lastIndex).equals(tiles.get(lastIndex + 1))){
                ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                newTiles.remove(lastIndex);
                newTiles.remove(lastIndex);
                return routineWay(newTiles, true);
            }
            return false; // Not find pair, not in this win way.
        }

        if (tiles.size() < 3){
            return false;
        }

        Tile firstTile = tiles.get(0);
        Tile secondTile = tiles.get(1);
        Tile thirdTile = tiles.get(2);

        // Test the other can be formed by Sequence
        if (CheckTile.isNumberType(firstTile)){
            if (firstTile.getTileType() == secondTile.getTileType() && firstTile.getTileType() == thirdTile.getTileType() &&
            firstTile.getValue() + 1 == secondTile.getValue() && firstTile.getValue() + 2 == thirdTile.getValue()){
                ArrayList<Tile> newTiles = new ArrayList<>(tiles);
                newTiles.remove(firstTile);
                newTiles.remove(secondTile);
                newTiles.remove(thirdTile);
                return routineWay(newTiles, true);
            }
        }

        // Test the other can be formed by Set
        if (firstTile.equals(secondTile) && firstTile.equals(thirdTile)){
            ArrayList<Tile> newTiles = new ArrayList<>(tiles);
            newTiles.remove(firstTile);
            newTiles.remove(secondTile);
            newTiles.remove(thirdTile);
            return routineWay(newTiles, true);
        }
        return false;
    }

    // TODO more ways of Hu
}
