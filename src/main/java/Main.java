import Simulation.SimulationController;
import UI.CLI;

/**
 * Is used only for debugging purposes together with the CLI class.
 */
class Main {
    /**
     * Starts the CLI, sets up simulation and runs it for 500 steps.
     * @param args not used.
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
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("END");
    }
}
