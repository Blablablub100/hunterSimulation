package Simulation.AI;

import Simulation.SimulationObjects.LivingCreature;
import Simulation.SimulationObjects.BoardObject;

import java.util.ArrayList;
import java.util.List;

public abstract class AI {

    Memory[] longTermMemory;
    Status status = new Status();
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


    void flee(List<LivingCreature> threats) {
        System.out.println("fleeing");

        BoardObject.Location loc = (BoardObject.Location) owner.getLocation().clone();

        while(owner.getPossibleSteps() > 1) {
            int[] distanceSums = getDistanceSums(threats, loc);
            List<Integer> orderedDistance = new ArrayList<>();
            int max = distanceSums[0];
            int picked = 0;

            for (int j = 0; j < 8; j++) {
                for (int i = 0; i < 8; i++) {
                    if (distanceSums[i] > max) {
                            max = distanceSums[i];
                            picked = i;
                    }
                }
                distanceSums[picked] = -1;
                orderedDistance.add(picked);
            }

            int x = 0;
            boolean check = false;

            do {
                switch (orderedDistance.get(x)) {
                    case 0:
                        if (!(owner.moveNorth())) x++;
                        else check = true;
                        break;

                    case 1:
                        if (!(owner.moveNorthEast())) x++;
                        else check = true;
                        break;

                    case 2:
                        if (!(owner.moveEast())) x++;
                        else check = true;
                        break;

                    case 3:
                        if (!(owner.moveSouthEast())) x++;
                        else check = true;
                        break;

                    case 4:
                        if (!(owner.moveSouth())) x++;
                        else check = true;
                        break;

                    case 5:
                        if (!(owner.moveSouthWest())) x++;
                        else check = true;
                        break;

                    case 6:
                        if (!(owner.moveWest())) x++;
                        else check = true;
                        break;

                    case 7:
                        if (!(owner.moveNorthWest())) x++;
                        else check = true;
                        break;
                }
            } while (!check);
        }
    }

    int[] getDistanceSums(List<LivingCreature> threats, BoardObject.Location loc) {
        int[] distanceSums = new int[8];
        loc.moveNorth();
        distanceSums[0] = checkDistanceSum(threats, loc);
        loc.moveEast();
        distanceSums[1] = checkDistanceSum(threats, loc);
        loc.moveSouth();
        distanceSums[2] = checkDistanceSum(threats, loc);
        loc.moveSouth();
        distanceSums[3] = checkDistanceSum(threats, loc);
        loc.moveWest();
        distanceSums[4] = checkDistanceSum(threats, loc);
        loc.moveWest();
        distanceSums[5] = checkDistanceSum(threats, loc);
        loc.moveNorth();
        distanceSums[6] = checkDistanceSum(threats, loc);
        loc.moveNorth();
        distanceSums[7] = checkDistanceSum(threats, loc);

        return distanceSums;
    }

    int checkDistanceSum(List<LivingCreature> threats, BoardObject.Location loc) {
        int distanceSum = 0;
        for (int i = 0; i < threats.size(); i++) {
            distanceSum += threats.get(i).getLocation().getDistance(loc);
        }
        return distanceSum;
    }



    void notifyNextRound() {
        for (Memory memory: longTermMemory) {
            if (memory != null) memory.notifyNewRound();
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

        public Status() {
            setStatus(0);
        }

        private final String[] statuses = {
                "calm"
                , "grazing"
                , "attacking"
                , "searching group members"
                , "fleeing"
                , "hunting"
                , "reaching carrion"
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
