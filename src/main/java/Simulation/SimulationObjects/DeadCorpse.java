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
        if (share == 0) share = 1;
        System.out.println(share);
    }

    public int eat() {
        timesEaten++;
        if (timesEaten == pieces) board.removeFromBoard(this);
        return share;
    }

    public void rot() {
        board.removeFromBoard(this);
    }

    public int getPieces() {
        return pieces;
    }

    public int getShare() {
        return share;
    }

    public int getTimesEaten() {
        return timesEaten;
    }

    @Override
    public Location getLocation() {
        return loc;
    }
}
