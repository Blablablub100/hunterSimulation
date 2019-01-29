package Simulation;

import Simulation.SimulationObjects.LivingCreature;
import Simulation.SimulationObjects.Board;
import UI.UserInput;

public class SimulationController {

    //TODO !!!! WENN HUNTER GLEICH GROß PREY KLEBT ER DIE GANZE ZEIT DRAN

    // TODO
    /*
    [] Do Simulation Control

    Bug fixes:
    - Avg Hunter killed by Prey not working
    - Avg Prey killed by Hunter not working
    - Amount Prey killed not working
    - Draw Group Radius
    - if hunter strengths equals prey strength, hunter will stand in front of it for ever
     */

    private Board board;
    private Statistics stats;
    private int simSteps;

    public SimulationController(UserInput input) {
        initSimulation(input);
    }

    // musst be called to create the initial state of the simulation
    private void initSimulation(UserInput input) {
        board = new Board(input.getBoardWidth()
                , input.getBoardHeight()
                , input.getInitialHunterCount()
                , input.getInitialPreyCount()
                , input.getInitialObstacleCount()
                , this);
        this.stats = new Statistics(this);
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
