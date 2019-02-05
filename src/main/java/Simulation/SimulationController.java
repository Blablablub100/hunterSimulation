package Simulation;

import Simulation.SimulationObjects.LivingCreature;
import Simulation.SimulationObjects.Board;
import UI.FullUserInput;
import UI.UserInput;

/**
 * Provides the interface for controlling the simulation.
 */
public class SimulationController {

    /**
     * The board the simulation is taking place upon.
     */
    private Board board;
    /**
     * The statistics of the simulation.
     */
    private Statistics stats;
    /**
     * The amount of steps the simulation has already been through.
     */
    private int simSteps;

    /**
     * Provides the ability to start a simulation with some simple user input.
     * @param input input done by the user.
     */
    public SimulationController(UserInput input) {
        initSimulation(input);
        this.stats = new Statistics(this);
    }

    /**
     * Provides the ability to start a simulation with all possible user input.
     * @param input input done by the user.
     */
    public SimulationController(FullUserInput input) {
        initSimulation(input);
        this.stats = new Statistics(this);
    }

    /**
     * Initializes the simulation.
     * @param input input that is used for initializing.
     */
    private void initSimulation(UserInput input) {
        board = new Board(input.getBoardWidth()
                , input.getBoardHeight()
                , input.getInitialHunterCount()
                , input.getInitialPreyCount()
                , input.getInitialObstacleCount()
                , this);
    }

    /**
     * Initializes the simulation.
     * @param input input that is used for initializing.
     */
    private void initSimulation(FullUserInput input) {
        board = new Board(input.getBoardWidth(), input.getBoardHeight(), 0, 0, input.getInitialObstacleCount(),this);
        for (int i = 0; i < input.getInitialHunterCount(); i++) {
            board.spawnHunter(input.getHunterSpeed(), input.getHunterStrength(), input.getHunterSight(), input.getHunterEnergy());
        }
        for (int i = 0; i < input.getInitialPreyCount(); i++) {
            board.spawnPrey(input.getPreySpeed(), input.getPreyStrength(), input.getPreySight(), input.getPreyEnergy());
        }
    }

    /**
     * Simulates the next simulation step.
     */
    public void simulateNextStep() {
        for (LivingCreature curr: getBoard().getLivingCreatures()) {
            curr.react();
        }
        simSteps++;
    }

    /**
     * Generic getter for how many simulation steps are already done.
     * @return amount of simulation steps.
     */
    int getSimSteps() {
        return simSteps;
    }

    /**
     * Generic getter for getting the simulations statistics.
     * @return simulations statistics.
     */
    public Statistics getStats() {
        return stats;
    }

    /**
     * Generic getter for getting the board the simulation is taking place on
     * @return board of the simulation.
     */
    public Board getBoard() {
        return board;
    }
}
