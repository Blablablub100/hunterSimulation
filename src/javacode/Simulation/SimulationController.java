package javacode.Simulation;

import javacode.Simulation.SimulationObjects.Board;
import javacode.Simulation.SimulationObjects.BoardObject;
import javacode.Simulation.SimulationObjects.LivingCreature;
import javacode.UI.UserInput;

import java.util.List;

public class SimulationController {

    private Board board;
    private Statistics stats;

    public SimulationController(UserInput input) {
        initSimulation(input);
    }

    // musst be called to create the initial state of the simulation
    private void initSimulation(UserInput input) {
        board = new Board(input.getBoardWidth()
                , input.getBoardHeight()
                , input.getInitialHunterCount()
                , input.getInitialPreyCount()
                , input.getInitialObstacleCount());
    }

    public void simulateNextStep() {
        for (LivingCreature curr: getBoard().getLivingCreatues()) {
            curr.react();
        }
    }

    public Statistics getStats() {
        return stats;
    }

    public Board getBoard() {
        return board;
    }
}
