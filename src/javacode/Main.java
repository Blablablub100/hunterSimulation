package javacode;

import javacode.Simulation.SimulationController;
import javacode.UI.CLI;

public class Main {

    public static void main(String[] args) {

        CLI test = new CLI();
        SimulationController sim = new SimulationController(test.getInput());
        test.printBoard(sim.getBoard());
        sim.simulateNextStep();

        for (int i = 0; i < 10000; i++) {
            sim.simulateNextStep();
            System.out.println("=============================================");
            test.printBoard(sim.getBoard());
            if (i == 999) System.out.println(i);
            try {
                //Thread.sleep(100);
            } catch (Exception e) {

            }
        }

        System.out.println("END");
    }
}
