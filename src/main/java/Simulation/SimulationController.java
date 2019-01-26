package Simulation;

import Simulation.SimulationObjects.LivingCreature;
import Simulation.SimulationObjects.Board;
import UI.UserInput;

public class SimulationController {

    private Board board;
    private Statistics stats;
    private int simSteps;

    public SimulationController(UserInput input) {
        initSimulation(input);
    }

    // musst be called to create the initial state of the simulation
    private void initSimulation(UserInput input) {
        this.stats = new Statistics(this);
        board = new Board(input.getBoardWidth()
                , input.getBoardHeight()
                , input.getInitialHunterCount()
                , input.getInitialPreyCount()
                , input.getInitialObstacleCount()
                , this);
    }

    public void simulateNextStep() {
        for (LivingCreature curr: getBoard().getLivingCreatues()) {
            curr.react();
        }
        simSteps++;
    }

    public int getSimSteps() {
        return simSteps;
    }

    public Statistics getStats() {
        return stats;
    }

    public Board getBoard() {
        return board;
    }
}
