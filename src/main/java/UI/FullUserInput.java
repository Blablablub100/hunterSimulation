package UI;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Provides the ability to save every possible parameter possible for a simulation.
 */
public class FullUserInput extends UserInput {

    /**
     * contains all values for hunters.
     */
    private LivingCreatureArguments hunterVals;
    /**
     * contains all values for preys.
     */
    private LivingCreatureArguments preyVals;

    /**
     * Constructor with all parameters possible for customizing the simulation.
     * @param boardWidth board width of the simulation.
     * @param boardHeight board height of the simulation.
     * @param initialHunterCount initial hunter count of the simulation.
     * @param initialPreyCount initial prey count of the simulation.
     * @param initialObstacleCount intial obstacle count of the simulation.
     * @param minHunterSpeed minimum movement speed for the hunters.
     * @param maxHunterSpeed maximum movement speed for the hunters.
     * @param minHunterStrength minimum strength for the hunters.
     * @param maxHunterStrength maximum strength for the hunters.
     * @param minHunterSight minimum view distance for the hunters.
     * @param maxHunterSight maximum view distance for the hunter.
     * @param minHunterEnergy minimum intial energy for the hunter.
     * @param maxHunterEnergy maximum initial energy for the hunter.
     * @param minPreySpeed minimum movement speed for the prey.
     * @param maxPreySpeed maximum movement speed for the prey.
     * @param minPreyStrength minimum strength for the prey.
     * @param maxPreyStrength maximum strength for the prey.
     * @param minPreySight minimum view distance for the prey.
     * @param maxPreySight maximum view distance for the prey.
     * @param minPreyEnergy minimum energy for the prey.
     * @param maxPreyEnergy maximum energy for the prey.
     * @throws WrongUserInputException if any of these inputs is not possible.
     */
    FullUserInput(int boardWidth
            , int boardHeight
            , int initialHunterCount
            , int initialPreyCount
            , int initialObstacleCount
            , int minHunterSpeed
            , int maxHunterSpeed
            , int minHunterStrength
            , int maxHunterStrength
            , int minHunterSight
            , int maxHunterSight
            , int minHunterEnergy
            , int maxHunterEnergy
            , int minPreySpeed
            , int maxPreySpeed
            , int minPreyStrength
            , int maxPreyStrength
            , int minPreySight
            , int maxPreySight
            , int minPreyEnergy
            , int maxPreyEnergy) throws WrongUserInputException {

        super(boardWidth, boardHeight, initialHunterCount, initialPreyCount, initialObstacleCount);

        hunterVals = new LivingCreatureArguments(
                minHunterSpeed, maxHunterSpeed, minHunterStrength,  maxHunterStrength
                , minHunterSight, maxHunterSight, minHunterEnergy, maxHunterEnergy
        );

        preyVals = new LivingCreatureArguments(
                minPreySpeed, maxPreySpeed, minPreyStrength,  maxPreyStrength
                , minPreySight, maxPreySight, minPreyEnergy, maxPreyEnergy 
        );
    }

    /**
     * Generic getter for the hunters movement speed.
     * @return max movement for the hunters.
     */
    public int getHunterSpeed() {
        return hunterVals.getSpeed();
    }

    /**
     * Generic getter for the hunters strength.
     * @return strength for hunters.
     */
    public int getHunterStrength() {
        return hunterVals.getStrength();
    }

    /**
     * Generic getter for hunters view distance.
     * @return hunters view distance.
     */
    public int getHunterSight() {
        return hunterVals.getSight();
    }

    /**
     * Generic getter for hunters energy.
     * @return hunters energy.
     */
    public int getHunterEnergy() {
        return hunterVals.getEnergy();
    }

    /**
     * Generic getter for preys movement speed.
     * @return preys movement speed.
     */
    public int getPreySpeed() {
        return preyVals.getSpeed();
    }

    /**
     * Generic getter for preys strength.
     * @return preys strength.
     */
    public int getPreyStrength() {
        return preyVals.getStrength();
    }

    /**
     * Generic getter for preys sight.
     * @return preys sight.
     */
    public int getPreySight() {
        return preyVals.getSight();
    }

    /**
     * Generic getter for getting preys energy.
     * @return preys energy.
     */
    public int getPreyEnergy() {
        return preyVals.getEnergy();
    }

    /**
     * Simple helping class for defining a range between two int and calculating random numbers inside it.
     */
    private class Range {
        /**
         * min value of the range.
         */
        int min;
        /**
         * max value of the range.
         */
        int max;

        /**
         * Constructor for creating a new range.
         * @param min min value.
         * @param max max value.
         */
        Range(int min, int max) {
            this.min =  min;
            this.max = max;
        }

        /**
         * Gets a random value in a given range.
         * @return random value larger min and smaller max.
         */
        int getValInRange() {
            return ThreadLocalRandom.current().nextInt(min, max + 1);
        }
    }

    /**
     * Helping class for saving together arguments used for describing LivingCreatures.
     */
    private class LivingCreatureArguments {
        /**
         * speed for LivingCreatures.
         */
        FullUserInput.Range speed;
        /**
         * strength for LivingCreatures.
         */
        FullUserInput.Range strength;
        /**
         * view distance for LivingCreatures.
         */
        FullUserInput.Range sight;
        /**
         * initial energy for LivingCreatures.
         */
        FullUserInput.Range energy;

        /**
         * Constructor for creating new LivingCreatureArguments.
         * @param vals Every value that is used for describing a LivingCreature.
         */
        LivingCreatureArguments(int... vals) {
            if (vals.length != 8) return;
            speed = new FullUserInput.Range(vals[0], vals[1]);
            strength = new FullUserInput.Range(vals[2], vals[3]);
            sight = new FullUserInput.Range(vals[4], vals[5]);
            energy = new FullUserInput.Range(vals[6], vals[7]);
        }

        /**
         * Simple getter for returning speed.
         * @return speed.
         */
        int getSpeed() {
            return speed.getValInRange();
        }

        /**
         * Simple getter for returning strength.
         * @return return strength.
         */
        int getStrength() {
            return strength.getValInRange();
        }

        /**
         * Simple getter for returning view distance.
         * @return view distance.
         */
        int getSight() {
            return sight.getValInRange();
        }

        /**
         * Simple getter for returning initial energy.
         * @return initial energy for a LivingCreature.
         */
        int getEnergy() {
            return energy.getValInRange();
        }
    }
}
