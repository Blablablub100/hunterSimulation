package Simulation.AI;

import Simulation.SimulationObjects.LivingCreature;
import Simulation.SimulationObjects.BoardObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The abstract AI class provides some basic guidelines aboout how the Prey and Hunter AI are implemented.
 * It also has some methods already implemented.
 */
public abstract class AI {

    /**
     * This Array is used to save everything important the Living Creature has seen.
     */
    Memory[] longTermMemory;
    /**
     * The status object represents the current status the AI is in. It is used to show if the Creature is calm or
     * attacking for example.
     */
    Status status = new Status();
    /**
     * Represents the body the brain (AI) has control over.
     */
    LivingCreature body;

    /**
     * Is called in every new simulated steps. This Method provides the entry point for every AI.
     */
    public void react() {
        notifyNextRound();
        // call notifyNextRound() at beginning!
        // 1. see
        // 2. does seen stuff change status?
        // 3. act according to status
        // --> see -> think -> react
    }

    /**
     * The only Constructor to create a new AI. A body is needed, without it the AI would have nothing to control.
     * @param body body of the LivingCreature the AI will control.
     */
    AI(LivingCreature body) {
        this.body = body;
    }

    /**
     * Provides the ability to flee from a List of threats. This method will allow the LivingCreature to go to the
     * Location with the most possible distance to all threats.
     * @param threats List of threats to flee from.
     */
    void flee(List<LivingCreature> threats) {
        BoardObject.Location loc = (BoardObject.Location) body.getLocation().clone();

        while(body.getPossibleSteps() > 1) {
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
                max = distanceSums[0];
                orderedDistance.add(picked);
                picked = 0;
            }

            int x = 0;
            boolean check = false;

            do {
                if(x < 8) {
                    switch (orderedDistance.get(x)) {
                        case 0:
                            if (!(body.moveNorth())) x++;
                            else check = true;
                            break;

                        case 1:
                            if (!(body.moveNorthEast())) x++;
                            else check = true;
                            break;

                        case 2:
                            if (!(body.moveEast())) x++;
                            else check = true;
                            break;

                        case 3:
                            if (!(body.moveSouthEast())) x++;
                            else check = true;
                            break;

                        case 4:
                            if (!(body.moveSouth())) x++;
                            else check = true;
                            break;

                        case 5:
                            if (!(body.moveSouthWest())) x++;
                            else check = true;
                            break;

                        case 6:
                            if (!(body.moveWest())) x++;
                            else check = true;
                            break;

                        case 7:
                            if (!(body.moveNorthWest())) x++;
                            else check = true;
                            break;
                    }
                } else return;
            } while (!check);
        }
    }

    /**
     * This method is only used by flee. It creates an array which rates the possible steps from the current Location.
     * A Location that is more far away from the the threats will have an higher rating then a location which is close
     * to a threat.
     * @param threats A list of threats. Their Location is used to calculate the rating.
     * @param loc The current Location of the body.
     * @return an array with each possible step and a rating. The index corresponds to the step.
     */
    private int[] getDistanceSums(List<LivingCreature> threats, BoardObject.Location loc) {
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

    /**
     * Rates a single location based on a list of threats. Only used by the flee() method.
     * @param threats list of threats. Their location is used to calculate the rating.
     * @param loc location to get the rating off.
     * @return Rating of loc.
     */
    private int checkDistanceSum(List<LivingCreature> threats, BoardObject.Location loc) {
        int distanceSum = 0;
        for (int i = 0; i < threats.size(); i++) {
            distanceSum += threats.get(i).getLocation().getDistance(loc);
        }
        return distanceSum;
    }

    /**
     * Must be called every iteration. This method notifies the Memory and the status that a new round has begun.
     */
    void notifyNextRound() {
        for (Memory memory: longTermMemory) {
            if (memory != null) memory.notifyNewRound();
        }
        status.notifyNextRound();
    }

    /**
     * Generic getter for longTermMemory.
     * @return longTermMemory attribute. It is used to store things the AI has to be remember.
     */
    public Memory[] getLongTermMemory() {
        return longTermMemory;
    }

    /**
     * Provides a String representation of the current status.
     * @return String representation of the current AIs status.
     */
    public String getStatusString() {
        return status.getStatus();
    }

    /**
     * Provides the ability to save any BoardObject as a Memory and keep track of how long this BoardObject is already
     * saved.
     */
    public class Memory {

        private BoardObject thingMemorized;
        private int time;

        /**
         * The only constructor to create a new memory.
         * @param memory represents the thing to be memorized.
         */
        Memory(BoardObject memory) {
            thingMemorized = memory;
            time = 0;
        }

        /**
         * Generic getter to get the BoardObject that is saved in this memory.
         * @return BoardObject saved in the memory.
         */
        public BoardObject getThingMemorized() {
            return thingMemorized;
        }

        /**
         * Provides the ability to get how long a Memory is already saved.
         * @return Amount of steps the Memory is saved.
         */
        public int getTime() {
            return time;
        }

        /**
         * Increments the time-counter.
         */
        public void notifyNewRound() {
            time++;
        }
    }


    /**
     * Provides the ability to save the AIs status with the corresponding time how long the AI has been in this status.
     */
    public class Status {

        /**
         * The only constructor to create a new Status. It initializes the AIs status as calm.
         */
        Status() {
            setStatus(0);
        }

        /**
         * Saves all the possible statuses.
         */
        private final String[] statuses = {
                "calm"
                , "grazing"
                , "attacking"
                , "searching group members"
                , "fleeing"
                , "hunting"
                , "reaching carrion"
        };
        /**
         * The index of the current status.
         */
        private int currentStatus;
        /**
         * A counter that saves how long the AI has been in the same status.
         */
        private int time;

        /**
         * Changes the status.
         * @param newStatus index of the status the AI will be in.
         * @return true if index exists.
         */
        boolean setStatus(int newStatus) {
            if (!this.contains(newStatus)) return false;
            if (currentStatus != newStatus) time = 0;
            currentStatus = newStatus;
            time = 0;
            return true;
        }

        /**
         * Changes the status.
         * @param newStatus String of the status the AI will be in.
         * @return true if String exists in Statuses variable.
         */
        public boolean setStatus(String newStatus) {
            int i = search(newStatus);
            if (i == -1) return false;
            if (!getStatus().equals(newStatus)) time = 0;
            currentStatus = i;
            time = 0;
            return true;
        }

        /**
         * Generic getter that provides a String representation of the current status.
         * @return String representation of the current status.
         */
        String getStatus() {
            return statuses[currentStatus];
        }

        /**
         * Generic getter to get the Time the AI has been in one status.
         * @return time the AI is in the same status.
         */
        public int getTime() {
            return time;
        }

        /**
         * Increments the time counter.
         */
        void notifyNextRound() {
            time++;
        }

        /**
         * Helping method to check whether an index exists.
         */
        private boolean contains(int i) {
            return (i >= 0 && i < statuses.length);
        }

        /**
         * Returns the index of status string.
         * @param s status string to get the index of.
         * @return -1 if string is not a status.
         */
        private int search(String s) {
            for (int i = 0; i < statuses.length; i++) {
                if (statuses[i].equals(s)) return i;
            }
            return -1;
        }
    }
}
