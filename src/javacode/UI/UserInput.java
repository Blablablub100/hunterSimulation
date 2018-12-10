package javacode.UI;

public class UserInput {

    private int boardWidth;
    private int boardHeight;
    private int initialHunterCount;
    private int initialPreyCount;
    private int initialObstacleCount;

    public UserInput(int boardWidth
            , int boardHeight
            , int initialHunterCount
            , int initialPreyCount
            , int initialObstacleCount) throws WrongUserInputException {

        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.initialHunterCount = initialHunterCount;
        this.initialPreyCount = initialPreyCount;
        this.initialObstacleCount = initialObstacleCount;
        checkInput();
    }

    public void checkInput() throws WrongUserInputException {
        //TODO überprüfen ob die inputs alle stimmen wenn nicht:
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public int getInitialHunterCount() {
        return initialHunterCount;
    }

    public int getInitialPreyCount() {
        return initialPreyCount;
    }

    public int getInitialObstacleCount() {
        return initialObstacleCount;
    }

}
