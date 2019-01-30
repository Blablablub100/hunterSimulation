package UI;

import java.util.concurrent.ThreadLocalRandom;

public class FullUserInput extends UserInput {

    private LivingCreatureArguments hunterVals;
    private LivingCreatureArguments preyVals;

    public FullUserInput(int boardWidth
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

    public int getHunterSpeed() {
        return hunterVals.getSpeed();
    }

    public int getHunterStrength() {
        return hunterVals.getStrength();
    }

    public int getHunterSight() {
        return hunterVals.getSight();
    }

    public int getHunterEnergy() {
        return hunterVals.getEnergy();
    }

    public int getPreySpeed() {
        return preyVals.getSpeed();
    }

    public int getPreyStrength() {
        return preyVals.getStrength();
    }

    public int getPreySight() {
        return preyVals.getSight();
    }

    public int getPreyEnergy() {
        return preyVals.getEnergy();
    }

    private class Range {
        int min;
        int max;

        Range(int min, int max) {
            this.min =  min;
            this.max = max;
        }

        int getValInRange() {
            return ThreadLocalRandom.current().nextInt(min, max + 1);
        }
    }

    private class LivingCreatureArguments {
        FullUserInput.Range speed;
        FullUserInput.Range strength;
        FullUserInput.Range sight;
        FullUserInput.Range energy;

        LivingCreatureArguments(int... vals) {
            if (vals.length != 8) return;
            speed = new FullUserInput.Range(vals[0], vals[1]);
            strength = new FullUserInput.Range(vals[2], vals[3]);
            sight = new FullUserInput.Range(vals[4], vals[5]);
            energy = new FullUserInput.Range(vals[6], vals[7]);
        }

        public int getSpeed() {
            return speed.getValInRange();
        }

        public int getStrength() {
            return strength.getValInRange();
        }

        public int getSight() {
            return sight.getValInRange();
        }

        public int getEnergy() {
            return energy.getValInRange();
        }
    }
}
