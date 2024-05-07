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

    public static PlayerType getPlayerType(int index){
        switch (index){
            case 0:
                return East;
            case 1:
                return North;
            case 2:
                return West;
            case 3:
                return South;
            default:
                return null;
        }
    }
}
