package Model;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private Player player;
    private Player originalPlayer;
    private MessageType type;
    private Tile tile;
    private TileStack tileStack;
    private Tile newTile;
    private List<Tile> optionTiles;
    private int tileCount;

    public TileStack getTileStack() {
        return tileStack;
    }

    public void setTileStack(TileStack tileStack) {
        this.tileStack = tileStack;
    }

    public Message() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Player getOriginalPlayer() {
        return originalPlayer;
    }

    public void setOriginalPlayer(Player originalPlayer) {
        this.originalPlayer = originalPlayer;
    }

    public Tile getNewTile() {
        return newTile;
    }

    public void setNewTile(Tile newTile) {
        this.newTile = newTile;
    }

    public List<Tile> getOptionTiles() {
        return optionTiles;
    }

    public void setOptionTiles(List<Tile> optionTiles) {
        this.optionTiles = optionTiles;
    }

    public int getTileCount() {
        return tileCount;
    }

    public void setTileCount(int tileCount) {
        this.tileCount = tileCount;
    }
}
