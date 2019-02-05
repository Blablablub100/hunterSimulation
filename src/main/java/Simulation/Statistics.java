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
        initialObstacleCount = simulation.getBoard().getNonLivingBoardObjects().size();
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

    public double getHunterPreyRatio() {
        if (getPreyCount() == 0) return 0;
        return (double)getHunterCount() / (double)getPreyCount();
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
        return simulation.getBoard().getNonLivingBoardObjects().size() - getObstacleCount();
    }

    public double getAvgPreyKilledByHunter() {
        if (amountPreyKilledByHunter == 0) return 0;
        return (double) amountPreyKilledByHunter / (double) getInitialHunterCount();
    }

    public double getAvgHunterKilledByPrey() {
        if (amountHunterKilledByPrey == 0) return 0;
        return (double)amountHunterKilledByPrey / (double)getInitialPreyCount();
    }

    public int getAmtHunterStarved() {
        return amountHunterStarved;
    }

    public int getAmtPreyStarved() {
        return amountPreyStarved;
    }

    public double getAvgFoodGainPerIterationHunter() {
        if (simulation.getSimSteps() == 0) return 0;
        return (double)amountFoodGainHunter / (double)simulation.getSimSteps();
    }

    public double getAvgFoodGainPerIterationPrey() {
        if (simulation.getSimSteps() == 0) return 0;
        return (double)amountFoodGainPrey / (double)simulation.getSimSteps();
    }

    public int getAmountPreyKilledByHunter() {
        return amountPreyKilledByHunter;
    }

    public int getAmountHunterKilledByPrey() {
        return amountHunterKilledByPrey;
    }
}