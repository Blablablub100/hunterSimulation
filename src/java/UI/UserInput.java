package java.UI;

public class UserInput {

    private int boardWidth;
    private int boardHeight;
    private int initialHunterCount;
    private int initialPreyCount;
    private int initialObstacleCount;

    public UserInput(int boardWidth, int boardHeight, int initialHunterCount, int initialPreyCount, int initialObstacleCount) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.initialHunterCount = initialHunterCount;
        this.initialPreyCount = initialPreyCount;
        this.initialObstacleCount = initialObstacleCount;
    }
}
