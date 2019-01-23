package Simulation.AI;

import Simulation.SimulationObjects.LivingCreature;
import Simulation.SimulationObjects.BoardObject;

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
        /*System.out.println("fleeing");
        // TODO NIKO mach das hier bitte
        // Hier wird eine Liste übergeben an BaordObject die der LivingCreature gefährlich werden
        // -> Du sollst einen algo schireben, der sich möglichst weit von den threads wegbewegt

        int smallestDistance;
        int smallestDistanceCache;
        int distanceSum;
        int distanceSumCheck;

        LivingCreature ownerCopy = (LivingCreature)owner.getLocation().clone();
        int random = owner.getRandom(1, 8);
        int randomCopy = random;
        int caseSave;
        boolean check;
        boolean steps;      // steps true straight      false diagonally
        boolean stepsCheck = true;
        double stepsLeft = owner.getPossibleSteps();

        while(stepsLeft >= 1) {
            randomCopy = random;
            check = true;
            caseSave = 0;
            smallestDistanceCache = 9999;
            distanceSumCheck = 0;


            do {
                switch (random) {
                    case 1:
                        ownerCopy().moveNorth;
                        distanceSum = 0;
                        smallestDistance = 9999;
                        steps = true;

                        // calculates the distanceSum to all threats
                        for (int i = 0; i < threats.size(); i++) {
                            distanceSum += threats.get(i).getLocation().getDistance(ownerCopy);

                            // saves the smallest distance
                            if (smallestDistance > threats.get(i).getLocation().getDistance(ownerCopy)) {
                                smallestDistance = threats.get(i).getLocation().getDistance(ownerCopy);
                            }
                        }

                        // marks this move as excecutable
                        if (distanceSum > distanceSumCheck) {
                            distanceSumCheck = distanceSum;
                            smallestDistanceCache = smallestDistance;
                            caseSave = random;
                            stepsCheck = steps;

                        } else if (distanceSum == distanceSumCheck && smallestDistanceCache > smallestDistance &&
                                smallestDistance != 1 && !stepsCheck) {
                            smallestDistanceCache = smallestDistance;
                            caseSave = random;
                            stepsCheck = steps;
                        }

                        ownerCopy.moveSouth();


                    case 2:
                        ownerCopy.moveNorth();
                        ownerCopy.moveEast();
                        distanceSum = 0;
                        smallestDistance = 9999;
                        steps = false;

                        // excludes this move if too less steps  are available
                        if(stepsLeft < 1.5) break;

                        for (int i = 0; i < threats.size(); i++) {
                            distanceSum += threats.get(i).getLocation().getDistance(ownerCopy);

                            if (smallestDistance > threats.get(i).getLocation().getDistance(ownerCopy)) {
                                smallestDistance = threats.get(i).getLocation().getDistance(ownerCopy);
                            }
                        }
                        if (distanceSum > distanceSumCheck) {
                            distanceSumCheck = distanceSum;
                            smallestDistanceCache = smallestDistance;
                            caseSave = random;

                        } else if (distanceSum == distanceSumCheck && smallestDistanceCache > smallestDistance &&
                                smallestDistance != 1) {
                            smallestDistanceCache = smallestDistance;
                            caseSave = random;
                        }

                        ownerCopy.moveSouth();
                        ownerCopy.moveWest();

                    case 3:
                        ownerCopy.moveEast();
                        distanceSum = 0;
                        smallestDistance = 9999;
                        steps = true;



                        ownerCopy.moveWest();

                    case 4:
                        ownerCopy.moveEast();
                        ownerCopy.moveSouth();
                        distanceSum = 0;
                        smallestDistance = 9999;
                        steps = false;

                        if(stepsLeft < 1.5) break;



                        ownerCopy.moveWest();
                        ownerCopy.moveNorth();

                    case 5:
                        ownerCopy.moveSouth();
                        distanceSum = 0;
                        smallestDistance = 9999;
                        steps = true;



                        ownerCopy.moveNorth();

                    case 6:
                        ownerCopy.moveSouth();
                        ownerCopy.moveWest();
                        distanceSum = 0;
                        smallestDistance = 9999;
                        steps = false;

                        if(stepsLeft < 1.5) break;



                        ownerCopy.moveNorth();
                        ownerCopy.moveEast();

                    case 7:
                        ownerCopy.moveWest();
                        distanceSum = 0;
                        smallestDistance = 9999;
                        steps = true;



                        ownerCopy.moveEast();

                    case 8:
                        ownerCopy.moveWest();
                        ownerCopy.moveNorth();
                        distanceSum = 0;
                        smallestDistance = 9999;
                        steps = false;

                        if(stepsLeft < 1.5) break;



                        ownerCopy.moveEast();
                        ownerCopy.moveSouth();
                }

                if (random == randomCopy) {
                    if (check) check = false;
                    else random = 10;
                }

                if (random < 8) random++;
                else random = 1;

            } while (random != 10);

            switch (caseSave) {
                case 1:
                    owner.getLocation().moveNorth();
                    stepsLeft -= 1.0;
                case 2:
                    owner.getLocation().moveNorth();
                    owner.getLocation().moveEast();
                    stepsLeft -= 1.5;
                case 3:
                    owner.getLocation().moveEast();
                    stepsLeft -= 1.0;
                case 4:
                    owner.getLocation().moveEast();
                    owner.getLocation().moveSouth();
                    stepsLeft -= 1.5;
                case 5:
                    owner.getLocation().moveSouth();
                    stepsLeft -= 1.0;
                case 6:
                    owner.getLocation().moveSouth();
                    owner.getLocation().moveWest();
                    stepsLeft -= 1.5;
                case 7:
                    owner.getLocation().moveWest();
                    stepsLeft -= 1.0;
                case 8:
                    owner.getLocation().moveWest();
                    owner.getLocation().moveNorth();
                    stepsLeft -= 1.5;
            }
        }
        */
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
                , "grazing"         //attacking -> grazing weil attack == hunt
                , "alarmed"
                , "searching group"
                , "fleeing"
                , "hunting"
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
