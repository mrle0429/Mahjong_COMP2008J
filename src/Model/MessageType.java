package Model;

/**
 * This class distinguishes the type of information transmitted each time.
 */

public enum MessageType {
    NoTiles,
    HasWinner,
    OptionWithChow,
    OptionWithKong,
    OptionWithPass,
    OptionWithPung,
    OptionWithTile,
    PlayerKung,
    TakeTurn,
    DiscardTile,
    TileStack,
    TurnInfo,
    Waiting,
    EnoughPlayers
}
