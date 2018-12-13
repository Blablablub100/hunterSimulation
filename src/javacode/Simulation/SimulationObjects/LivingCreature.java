package javacode.Simulation.SimulationObjects;

import javacode.Simulation.AI.AI;

import java.util.List;

public abstract class LivingCreature implements BoardObject {

    private AI brain;
    private Board myBoard;
    Location loc;
    private int maxMovementSpeed;
    private int strength;
    private int sightDistance;
    private int hunger;
    private String direction; // can be left, right, up, down

    public abstract void react();

    abstract List<BoardObject> see();

    abstract void move();

    abstract void eat();

    abstract void attack(LivingCreature opponent);

    void die() {
        myBoard.removeFromBoard(this);
    }

    public Location getLocation() {
        return loc;
    }
}
