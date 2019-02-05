package UI;

/**
 * This class holds the minimum user input required for a simulation.
 */
public class UserInput {

    /**
     * The width of the board the simulation is going to be on.
     */
    private int boardWidth;
    /**
     * The height of the board the simulation is going to be on.
     */
    private int boardHeight;
    /**
     * The initial hunter count of the simulation.
     */
    private int initialHunterCount;
    /**
     * The initial prey count of the simulation.
     */
    private int initialPreyCount;
    /**
     * The initial obstacle count of the stimulation.
     */
    private int initialObstacleCount;

    /**
     * The only constructor for creating a new UserInput object.
     * @param boardWidth The width of the board the simulation is going to be on.
     * @param boardHeight The height of the board the simulation is going to be on.
     * @param initialHunterCount The initial hunter count of the simulation.
     * @param initialPreyCount The initial prey count of the simulation
     * @param initialObstacleCount The initial obstacle count of the stimulation.
     * @throws WrongUserInputException thrown if the users input is somehow not logical.
     */
    UserInput(int boardWidth
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

    /**
     * checks if everything about the user input is okay.
     * @throws WrongUserInputException if user input in not okay.
     */
    private void checkInput() throws WrongUserInputException {

        if(initialObstacleCount + initialPreyCount + initialHunterCount > boardWidth * boardHeight){
            throw new WrongUserInputException("Object count", "Too many Objects placed on the board.\n");
        }
        if(boardWidth <= 0) throw new WrongUserInputException("Board Width", "Width can't be smaller or equal 0.\n");
        if(boardHeight <= 0) throw new WrongUserInputException("Board Height", "Height can't be smaller or equal 0.\n");
        if(initialHunterCount < 0) throw new WrongUserInputException("Count of hunters", "Count of hunters can't be " +
                "negative.\n");
        if(initialPreyCount < 0) throw new WrongUserInputException("Count of preys", "Count of preys can't be " +
                "negative.\n");
        if(initialObstacleCount < 0){
            throw new WrongUserInputException("Count of obstacles", "Count of obstacles can't be " + "negative.\n");
        }
    }

    /**
     * Generic getter for getting the board width.
     * @return board width.
     */
    public int getBoardWidth() {
        return boardWidth;
    }

    /**
     * Generic getter for getting the board height.
     * @return board height.
     */
    public int getBoardHeight() {
        return boardHeight;
    }

    /**
     * Generic getter for getting the initial hunter count.
     * @return initial hunter count.
     */
    public int getInitialHunterCount() {
        return initialHunterCount;
    }

    /**
     * Generic getter for getting the initial prey count.
     * @return
     */
    public int getInitialPreyCount() {
        return initialPreyCount;
    }

    /**
     * Generic getter for getting the initial obstacle count.
     * @return
     */
    public int getInitialObstacleCount() {
        return initialObstacleCount;
    }

}
