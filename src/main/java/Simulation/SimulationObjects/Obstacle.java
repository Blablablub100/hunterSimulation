package Simulation.SimulationObjects;

/**
 * This class represents an obstacle that is blocking a location on the simulation.
 */
public class Obstacle implements BoardObject {

    /**
     * The location the obstacle is at.
     */
    private Location loc;

    /**
     * The only constructor for creating an obstacle.
     * @param loc location the obstacle will be at.
     */
    Obstacle(Location loc) {
        this.loc = loc;
    }

    /**
     * Generic getter for getting the obstacles location.
     * @return obstacles location.
     */
    @Override
    public Location getLocation() {
        return loc;
    }
}
