package Simulation.SimulationObjects;

/**
 * Represents a carrion. If a group has killed a large prey a carrion is going to be spawn. It will be on the board
 * until every hunter that was part of the group are its part or until the carrion rots away.
 */
public class DeadCorpse implements BoardObject {

    /**
     * Board object that is used to make the rotting possible.
     */
    private Board board;
    /**
     * Location the DeadCorpse is at on the board.
     */
    private Location loc;
    /**
     * Share size every hunter is going to get.
     */
    private int share;
    /**
     * Counter for how many hunter have already eaten from this.
     */
    private int timesEaten;
    /**
     * Amount of how many pieces this DeadCorpse consists of.
     */
    private int pieces;

    /**
     * This is the only contructor to create a new DeadCorpse object.
     * @param board the board that this DeadCorpse is placed upon. The board is necessary for the rotting feature.
     * @param loc the location the DeadCorpse is laying at.
     * @param size the size of DeadCorpse. Normally this is the strength of the killed prey.
     * @param groupSize the size of the group that killed the prey.
     */
    public DeadCorpse(Board board, Location loc, int size, int groupSize) {
        timesEaten = 0;
        this.board = board;
        this.loc = loc;
        this.pieces = groupSize;
        share = size / groupSize;
        if (share == 0) share = 1;
    }

    /**
     * Eats a part of the DeadCorpse.
     * @return how much of the DeadCorpse was eaten.
     */
    public int eat() {
        timesEaten++;
        if (timesEaten == pieces) board.removeFromBoard(this);
        return share;
    }

    /**
     * Removes itself from the board.
     */
    public void rot() {
        board.removeFromBoard(this);
    }

    /**
     * Gets the amount of pieces this DeadCorpse consists of.
     * @return amount of pices this DeadCorpse consists of.
     */
    public int getPieces() {
        return pieces;
    }

    /**
     * Generic getter for the size of one share of this DeadCorpse.
     * @return size of one piece of the DeadCorpse.
     */
    public int getShare() {
        return share;
    }

    /**
     * Generic getter for the counter of how many pieces are already eaten.
     * @return amount of pieces that are eaten.
     */
    public int getTimesEaten() {
        return timesEaten;
    }

    /**
     * Generic getter for the DeadCorpse Location.
     * @return Current location of the DeadCorpse
     */
    @Override
    public Location getLocation() {
        return loc;
    }
}
