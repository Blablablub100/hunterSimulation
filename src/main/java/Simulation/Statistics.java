package Simulation;

import Simulation.SimulationObjects.Hunter;
import Simulation.SimulationObjects.Prey;

import java.util.List;

public class Statistics {

    int initialHunterCount;
    int initialPreyCount;
    int initialObstacleCount;
    int amountPreyKilledByHunter;
    int amountHunterKilledByPrey;
    int amountPreyStarved;
    int amountHunterStarved;
    int amountFoodGainPrey;
    int amountFoodGainHunter;

    private SimulationController simulation;

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
        initialObstacleCount = simulation.getBoard().getBoardObjects().size();
    }

    public void incPreyKilledByHunter() {
        amountPreyKilledByHunter++;
    }

    public void incHunterKilledByPrey() {
        amountHunterKilledByPrey++;
    }

    public void incPreyStarved() {
        amountPreyStarved++;
    }

    public void incHunterStarved() {
        amountHunterStarved++;
    }

    public void addFoodGainPrey(int amt) {
        amountFoodGainPrey += amt;
    }

    public void addFoodGainHunter(int amt) {
        amountFoodGainHunter += amt;
    }

    public int getHunterCount() {
        return (simulation.getBoard().getHunters().size());
    }

    public int getPreyCount() {
        return (simulation.getBoard().getPreys().size());
    }

    public int getObstacleCount() {
        return initialObstacleCount;
    }

    public int getHunterPreyRatio() {
        if (getPreyCount() == 0) return 0;
        return getHunterCount() / getPreyCount();
    }

    public int getInitialHunterCount() {
        return initialHunterCount;
    }

    public int getInitialPreyCount() {
        return initialPreyCount;
    }

    public int getAmtPreyDead() {
        return initialPreyCount - getPreyCount();
    }

    public int getAmtHunterDead() {
        return initialHunterCount - getHunterCount();
    }

    public int getAmtDeadCorpse() {
        return getObstacleCount() - initialHunterCount;
    }

    public int getAvgHunterSpeed() {
        List<Hunter> hunters = simulation.getBoard().getHunters();
        int sum = 0;
        for (Hunter hunter: hunters) {
            sum = sum + hunter.getMaxMovementSpeed();
        }
        return sum/hunters.size();
    }

    public int getAvgPreySpeed() {
        List<Prey> preys = simulation.getBoard().getPreys();
        int sum = 0;
        for (Prey prey: preys) {
            sum = sum + prey.getMaxMovementSpeed();
        }
        return sum/preys.size();
    }

    public int getAvgPreyKilledByHunter() {
        return getInitialHunterCount() / amountPreyKilledByHunter;
    }

    public int getAvgHunterKilledByPrey() {
        return getInitialPreyCount() / amountHunterKilledByPrey;
    }

    public int getAmtHunterStarved() {
        return amountHunterStarved;
    }

    public int getAmtPreyStarved() {
        return amountPreyStarved;
    }

    public double getAvgFoodGainPerIterationHunter() {
        return (double)amountFoodGainHunter / (double)simulation.getSimSteps();
    }

    public double getAvgFoodGainPerIterationPrey() {
        return (double)amountFoodGainPrey / (double)simulation.getSimSteps();
    }
}