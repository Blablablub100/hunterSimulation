package javacode.Simulation.SimulationObjects;

import java.util.List;

public class Hunter extends LivingCreature {

    Hunter(Location loc) {
        this.loc = loc;
        // TODO randomize rest
    }


    @Override
    public void react() {

    }

    @Override
    List<BoardObject> see() {
        return null;
    }

    @Override
    void move() {

    }

    @Override
    void eat() {

    }

    @Override
    void attack(LivingCreature opponent) {

    }

    @Override
    void die() {

    }
}
