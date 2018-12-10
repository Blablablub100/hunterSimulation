package javacode.Simulation.SimulationObjects;

public class Prey implements LivingCreature {

    private Location loc;

    public Prey(Location loc) {
        this.loc = loc;
    }

    @Override
    public void setLocation(Location loc) {
        this.loc = loc;
    }

    @Override
    public Location getLocation(Location loc) {
        return loc;
    }
}
