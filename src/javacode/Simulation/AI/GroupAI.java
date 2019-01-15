package javacode.Simulation.AI;

import javacode.Simulation.SimulationObjects.Prey;

import java.util.List;

public class GroupAI {

    private List<HunterAI> members;
    private Prey target;
    private int time;

    /*
    1. do a circle around the target
    2. Come closer to the target step by step
    3. When target is reached cadaver is spawned
    4. Every hunter eats a fair share
     */

    public void steerGroup() {
        if (time >= 10) {
            // ask for every member to recommit to group
            time = 0;
        }
        // make a line in front of target
        // go step by step closer
        // kill
        // let everybody eat a fair share
    }

    public void doFormation() {

    }

    public void moveTowardsTarget() {

    }

    public void kill() {

    }

    public void letEverybodyEat() {

    }
}
