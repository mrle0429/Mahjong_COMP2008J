package Model;

/**
 * @author Le Liu
 * @create 2024-04
 */
public enum PlayerType {
    East,
    South,
    West,
    North;

    public PlayerType next(){
        switch (this) {
            case East:
                return North;
            case South:
                return East;
            case West:
                return South;
            case North:
                return West;
            default:
                return null;
        }
    }
}
