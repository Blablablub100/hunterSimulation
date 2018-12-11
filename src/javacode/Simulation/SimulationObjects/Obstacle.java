package javacode.Simulation.SimulationObjects;

public class Obstacle implements BoardObject {

    private Location loc;

    public Obstacle(Location loc) {
        this.loc = loc;
    }

    @Override
    public void setLocation(Location loc) {
        this.loc = loc;
    }

    @Override
    public Location getLocation() {
        return loc;
    }
}
