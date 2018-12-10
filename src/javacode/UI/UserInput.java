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
        throw new WrongUserInputException("a", "b");
    }
}
