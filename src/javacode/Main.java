package javacode;

import javacode.Simulation.SimulationController;
import javacode.UI.CLI;

public class Main {

    public static void main(String[] args) {
        //TODO: 1. CLI.getInput()
        //TODO: 3. SimulationController.getBoard()
        //TODO: 4. CLI.printBoard()

        CLI test = new CLI();
        SimulationController sim = new SimulationController(test.getInput());
        test.printBoard(sim.getBoard());
        sim.simulateNextStep();

        System.out.println("END");
    }
}
