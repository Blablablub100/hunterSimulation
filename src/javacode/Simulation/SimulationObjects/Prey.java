package javacode.Simulation.SimulationObjects;

import java.util.List;

public class Prey extends LivingCreature {

    Prey(Location loc) {
        // TODO randomize rest
        this.loc = loc;
    }

    // methods from LivingCreature
    @Override
    public void react() {

    }

    @Override
    List<BoardObject> see() {
        return null;
    }

    @Override
    void eat() {

    }

    @Override
    boolean attack(LivingCreature opponent) {
        return false;
    }
}
