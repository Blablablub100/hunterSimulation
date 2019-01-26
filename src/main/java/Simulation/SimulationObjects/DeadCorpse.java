package Simulation.SimulationObjects;

public class DeadCorpse implements BoardObject {

    private Board board;
    private Location loc;
    int share;
    private int timesEaten;
    private int pieces;

    public DeadCorpse(Board board, Location loc, int size, int groupSize) {
        timesEaten = 0;
        this.board = board;
        this.loc = loc;
        this.pieces = groupSize;
        share = size / groupSize;
    }

    public int eat() {
        timesEaten++;
        if (timesEaten == pieces) board.removeFromBoard(this);
        return share;
    }

    public void rot() {
        board.removeFromBoard(this);
    }

    @Override
    public Location getLocation() {
        return loc;
    }
}