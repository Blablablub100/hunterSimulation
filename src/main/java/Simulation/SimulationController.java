package Simulation;

import Simulation.SimulationObjects.LivingCreature;
import Simulation.SimulationObjects.Board;
import UI.FullUserInput;
import UI.UserInput;

public class SimulationController {

    private Board board;
    private Statistics stats;
    private int simSteps;

    public SimulationController(UserInput input) {
        initSimulation(input);
        this.stats = new Statistics(this);
    }

    public SimulationController(FullUserInput input) {
        initSimulation(input);
        this.stats = new Statistics(this);
    }

    // must be called to create the initial state of the simulation
    private void initSimulation(UserInput input) {
        board = new Board(input.getBoardWidth()
                , input.getBoardHeight()
                , input.getInitialHunterCount()
                , input.getInitialPreyCount()
                , input.getInitialObstacleCount()
                , this);
    }

    private void initSimulation(FullUserInput input) {
        board = new Board(input.getBoardWidth(), input.getBoardHeight(), 0, 0, input.getInitialObstacleCount(),this);
        for (int i = 0; i < input.getInitialHunterCount(); i++) {
            board.spawnHunter(input.getHunterSpeed(), input.getHunterStrength(), input.getHunterSight(), input.getHunterEnergy());
        }
        for (int i = 0; i < input.getInitialPreyCount(); i++) {
            board.spawnPrey(input.getPreySpeed(), input.getPreyStrength(), input.getPreySight(), input.getPreyEnergy());
        }
    }

    public void simulateNextStep() {
        for (LivingCreature curr: getBoard().getLivingCreatures()) {
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
