package Simulation;

/**
 * Provides the ability to save the statistics of an ongoing simulation.
 */
public class Statistics {

    /**
     * The amount of hunters the simulation started with.
     */
    private int initialHunterCount;
    /**
     * The amount of preys the simulation started with.
     */
    private int initialPreyCount;
    /**
     * The amount of obstacles the simulation started with.
     */
    private int initialObstacleCount;
    /**
     * The amount of prey that got killed by hunters.
     */
    private int amountPreyKilledByHunter;
    /**
     * The amount of hunter that got killed by preys.
     */
    private int amountHunterKilledByPrey;
    /**
     * The amount of starved prey.
     */
    private int amountPreyStarved;
    /**
     * The amount of starved hunter.
     */
    private int amountHunterStarved;
    /**
     * The amount of food gain for all prey together.
     */
    private int amountFoodGainPrey;
    /**
     * The amount of food gain for all hunters together.
     */
    private int amountFoodGainHunter;
    /**
     * The simulation controller for accessing the simulation.
     */
    private SimulationController simulation;

    /**
     * The only constructor for creating new statistics objects.
     * @param simulation the simulation controller controlling the ongoing simulation.
     */
    public Statistics(SimulationController simulation) {
        this.simulation = simulation;
        amountPreyKilledByHunter = 0;
        amountHunterKilledByPrey = 0;
        amountPreyStarved = 0;
        amountHunterStarved = 0;
        amountFoodGainPrey = 0;
        amountFoodGainHunter = 0;
        initialHunterCount = simulation.getBoard().getHunters().size();
        initialPreyCount = simulation.getBoard().getPreys().size();
        initialObstacleCount = simulation.getBoard().getNonLivingBoardObjects().size();
    }

    /**
     * Increments the amount of prey killed by hunter.
     */
    public void incPreyKilledByHunter() {
        amountPreyKilledByHunter++;
    }

    /**
     * Increments the amount of hunter killed by prey.
     */
    public void incHunterKilledByPrey() {
        amountHunterKilledByPrey++;
    }

    /**
     * Increments the amount of prey starved.
     */
    public void incPreyStarved() {
        amountPreyStarved++;
    }

    /**
     * Increments the amount of hunter starved.
     */
    public void incHunterStarved() {
        amountHunterStarved++;
    }

    /**
     * Adds to the amount of food gain.
     * @param amt the amount of what is going to be added.
     */
    public void addFoodGainPrey(int amt) {
        amountFoodGainPrey += amt;
    }

    /**
     * Adds to the amount of food gain.
     * @param amt the amount of what is going to be added.
     */
    public void addFoodGainHunter(int amt) {
        amountFoodGainHunter += amt;
    }

    /**
     * Calculates how many hunters are on the board.
     * @return amount of hunters on the board.
     */
    private int getHunterCount() {
        return (simulation.getBoard().getHunters().size());
    }

    /**
     * Calculates how many preys are on the board.
     * @return amount of preys on the board.
     */
    private int getPreyCount() {
        return (simulation.getBoard().getPreys().size());
    }

    /**
     * Return how many obstacles are on the board.
     * @return amount of obstacles on the board.
     */
    private int getObstacleCount() {
        return initialObstacleCount;
    }

    /**
     * Gets the ration of how many hunters are on the board compared to prey.
     * @return hunter / prey ratio.
     */
    public double getHunterPreyRatio() {
        if (getPreyCount() == 0) return 0;
        return (double)getHunterCount() / (double)getPreyCount();
    }

    /**
     * Gets the initial hunter count.
     * @return initial hunter count.
     */
    private int getInitialHunterCount() {
        return initialHunterCount;
    }

    /**
     * Gets the initial prey count.
     * @return initial prey count.
     */
    private int getInitialPreyCount() {
        return initialPreyCount;
    }

    /**
     * Get how many preys are already dead.
     * @return amount of dead preys.
     */
    public int getAmtPreyDead() {
        return initialPreyCount - getPreyCount();
    }

    /**
     * Get how many hunters are already dead
     * @return amount of dead hunters.
     */
    public int getAmtHunterDead() {
        return initialHunterCount - getHunterCount();
    }

    /**
     * Get amount of Carrion on the board.
     * @return amount of carrion on the board.
     */
    public int getAmtDeadCorpse() {
        return simulation.getBoard().getNonLivingBoardObjects().size() - getObstacleCount();
    }

    /**
     * Calculates how many preys are killed on average by one hunter.
     * @return how many preys are killed on average by one hunter.
     */
    public double getAvgPreyKilledByHunter() {
        if (amountPreyKilledByHunter == 0) return 0;
        return (double) amountPreyKilledByHunter / (double) getInitialHunterCount();
    }

    /**
     * Calculates how many hunters are killed on average by one prey.
     * @return how many hunter are killed on average by one prey.
     */
    public double getAvgHunterKilledByPrey() {
        if (amountHunterKilledByPrey == 0) return 0;
        return (double)amountHunterKilledByPrey / (double)getInitialPreyCount();
    }

    /**
     * Gets amount of hunters starved.
     * @return amount of hunters starved.
     */
    public int getAmtHunterStarved() {
        return amountHunterStarved;
    }

    /**
     * Gets amount of preys starved.
     * @return amount of preys starved.
     */
    public int getAmtPreyStarved() {
        return amountPreyStarved;
    }

    /**
     * Calculates average food gain per iteration for hunters.
     * @return average food gain per iteration for hunters.
     */
    public double getAvgFoodGainPerIterationHunter() {
        if (simulation.getSimSteps() == 0) return 0;
        return (double)amountFoodGainHunter / (double)simulation.getSimSteps();
    }

    /**
     * Calculates average food gain per iteration for preys.
     * @return average food gain per iteration for preys.
     */
    public double getAvgFoodGainPerIterationPrey() {
        if (simulation.getSimSteps() == 0) return 0;
        return (double)amountFoodGainPrey / (double)simulation.getSimSteps();
    }

    /**
     * Gets the amount of preys killed by hunters.
     * @return amount of preys killed by hunters.
     */
    public int getAmountPreyKilledByHunter() {
        return amountPreyKilledByHunter;
    }

    /**
     * Gets the amount of hunters killed by preys.
     * @return amount of hunter killed by preys.
     */
    public int getAmountHunterKilledByPrey() {
        return amountHunterKilledByPrey;
    }
}