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


        if(boardWidth <= 0) throw new WrongUserInputException("Board Width", "Width can't be smaller or equal 0.\n");
        if(boardHeight <= 0) throw new WrongUserInputException("Board Height", "Height can't be smaller or equal 0.\n");
        if(initialHunterCount < 0) throw new WrongUserInputException("Count of hunters", "Count of hunters can't be " +
                "negative.\n");
        if(initialPreyCount < 0) throw new WrongUserInputException("Count of preys", "Count of preys can't be " +
                "negative.\n");
        if(initialObstacleCount < 0) throw new WrongUserInputException("Count of obstacles", "Count of obstacles can't be " +
                "negative.\n");
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
