package javacode.Simulation;

import javacode.Simulation.SimulationObjects.Board;
import javacode.UI.UserInput;

public class SimulationController {

    private Board board;
    private Statistics stats;

    public void initSimulation(UserInput input) {

    }

    public Statistics getStats() {
        return stats;
    }

    public Board getBoard() {
        return board;
    }
}
