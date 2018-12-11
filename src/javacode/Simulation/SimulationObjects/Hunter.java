package javacode.Simulation.SimulationObjects;

public class Hunter implements LivingCreature {

    private Location loc;

    public Hunter(Location loc) {
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
