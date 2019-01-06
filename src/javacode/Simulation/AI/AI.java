package javacode.Simulation.AI;

import javacode.Simulation.SimulationObjects.BoardObject;
import javacode.Simulation.SimulationObjects.LivingCreature;

public abstract class AI {

    Memory[] longTermMemory;
    private Status status;
    LivingCreature owner;

    public abstract void react(
            // call notifyNextRound() at beginning!
            // 1. see
            // 2. does seen stuff change status?
            // 3. act according to status
            // --> see -> think -> react
    );

    public AI(LivingCreature owner) {
        this.owner = owner;
    }

    void notifyNextRound() {
        for (Memory memory: longTermMemory) {
            memory.notifyNewRound();
        }
        status.notifyNextRound();
    }



    public class Memory {

        public Memory(BoardObject memory) {
            thingMemorized = memory;
            time = 0;
        }

        private BoardObject thingMemorized;
        private int time;

        public BoardObject getThingMemorized() {
            return thingMemorized;
        }

        public int getTime() {
            return time;
        }

        public void notifyNewRound() {
            time++;
        }
    }




    public class Status {

        private final String[] statuses = {
                "calm"
                , "attacking"
                , "alarmed"
                , "searching group"
                , "fleeing"
        };
        private int currentStatus;
        private int time;

        public boolean setStatus(int newStatus) {
            if (!this.contains(newStatus)) return false;
            if (currentStatus != newStatus) time = 0;
            currentStatus = newStatus;
            return true;
        }

        public boolean setStatus(String newStatus) {
            int i = search(newStatus);
            if (i == -1) return false;
            if (!getStatus().equals(newStatus)) time = 0;
            currentStatus = i;
            return true;
        }

        public String getStatus() {
            return statuses[currentStatus];
        }

        public int getStatusIndex() {
            return currentStatus;
        }

        public int getTime() {
            return time;
        }

        public void notifyNextRound() {
            time++;
        }

        // private helping methods
        private boolean contains(int i) {
            return (i >= 0 && i < statuses.length);
        }

        private int search(String s) {
            for (int i = 0; i < statuses.length; i++) {
                if (statuses[i].equals(s)) return i;
            }
            return -1;
        }
    }
}
