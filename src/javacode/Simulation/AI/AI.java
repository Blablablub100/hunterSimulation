package javacode.Simulation.AI;

import javacode.Simulation.SimulationObjects.BoardObject;
import javacode.Simulation.SimulationObjects.LivingCreature;

import java.util.HashMap;
import java.util.List;

public abstract class AI {

    HashMap<Integer, String> status = new HashMap<Integer, String>();
    int statusDuration;
    Memory[] longTermMemory;
    LivingCreature owner;

    public abstract void react(
            // 1. see
            // 2. does seen stuff change status?
            // 3. act according to status
            // --> see -> think -> react
    );

    public AI(LivingCreature owner) {
        this.owner = owner;
        initStatus();
    }

    private void initStatus() {
        status.put(1, "calm");
        status.put(2, "attacking");
        status.put(3, "alarmed");
        status.put(4, "seaching group");
        status.put(5, "fleeing");
    }





    public class Memory {

        private BoardObject thingMemorized;
        private int time;

        public BoardObject getThingMemorized() {
            return thingMemorized;
        }

        public void setThingMemorized(BoardObject thingMemorized) {
            this.thingMemorized = thingMemorized;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
