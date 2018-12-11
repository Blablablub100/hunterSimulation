package javacode.Simulation;

public class Statistics {

    private SimulationController simulation;

    public Statistics(SimulationController simulation) {
        this.simulation = simulation;
    }

    public int getHunterCounter() {
        return (simulation.getBoard().getHunters().size());
    }

    public int getPreyCount() {
        return (simulation.getBoard().getPreys().size());
    }

    public int getObstacleCount() {
        return (simulation.getBoard().getObstacles().size());
    }

    public int getHunterPreyRatio() {
        return (simulation.getBoard().getObstacles().size());
    }
}