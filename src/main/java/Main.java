import Simulation.SimulationController;
import UI.CLI;

public class Main {

    /* TODO
    1. [X] fix AI statuses
    2. [X] code cleanup
    3. [X] implement Statistics
    4. [] design UI
     */



    public static void main(String[] args) {

        CLI test = new CLI();
        SimulationController sim = new SimulationController(test.getInput());
        test.printBoard(sim.getBoard());
        sim.simulateNextStep();

        for (int i = 0; i < 500; i++) {
            sim.simulateNextStep();
            System.out.println("=============================================");
            test.printBoard(sim.getBoard());
            if (i == 499)
                System.out.println(i);
            try {
                //Thread.sleep(100);
            } catch (Exception e) {

            }
        }

        System.out.println("END");
    }
}