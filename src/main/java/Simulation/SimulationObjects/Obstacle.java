package Simulation.SimulationObjects;

public class Obstacle implements BoardObject {

    private Location loc;

    Obstacle(Location loc) {
        this.loc = loc;
    }

    @Override
    public Location getLocation() {
        return loc;
    }
}
