package Simulation.SimulationObjects;

import Simulation.AI.AI;

import java.util.List;

/**
 * This abstract class provides the basis all LivingCreatures are based upon. Namely this abstract class is the basis
 * for the hunter and the prey.
 */
public abstract class LivingCreature implements BoardObject {

    /**
     * The AI that is going to control the LivingCreature.
     */
    AI brain;
    /**
     * The board the LivingCreature is upon.
     */
    Board myBoard;
    /**
     * The location the LivingCreature is at.
     */
    Location loc;
    /**
     * The maximum amount of step a LivingCreature is able to walk in one iteration.
     */
    int maxMovementSpeed;
    /**
     * The strength a LivingCreature has.
     */
    int strength;
    /**
     * The maximum view distance.
     */
    int sightDistance;
    /**
     * The amount of energy a LivingCreature has left. If it is zero, the LivingCreature will starve.
     */
    int energy;
    /**
     * The direction in which the LivingCreature is looking (north, south, east or west)
     */
    String direction; // can be north, south, east, west
    /**
     * The steps a LivingCreature has already taken in one iteration. This resets itself every iteration.
     */
    double stepsTaken;

    /**
     * Defines how a LivingCreature will react to a given situation.
     */
    public abstract void react();

    /**
     * Will return everything in sight.
     * @return List of things the LivingCreature can see.
     */
    public abstract List<BoardObject> see();

    /**
     * This method will enable the LivingCreature to attack an opponent.
     * @param opponent Other LivingCreature that is going to be attacked.
     * @return true if success.
     */
    public abstract boolean attack(LivingCreature opponent);

    /**
     * Removes the LivingCreature from this simulation.
     */
    public void die() {
        myBoard.removeFromBoard(this);
    }

    /**
     * Enables the LivingCreature to eat for refilling its energy.
     * @param calories amount of energy that is going to be restored.
     */
    public void eat(int calories) {
        if (this instanceof Prey) {
            myBoard.getStats().addFoodGainPrey(calories);
        } else if (this instanceof Hunter) {
            myBoard.getStats().addFoodGainHunter(calories);
        }
        energy = energy + calories;
    }

    /**
     * Allows the LivingCreature to move as close as possible to a given goal location.
     * @param goalDestination location this method will try to move the LivingCreature to.
     * @return true if goal has been reached.
     */
    public boolean move(BoardObject.Location goalDestination) {
        if (!myBoard.isOnBoard(goalDestination)) {
            int x = goalDestination.getX();
            int y = goalDestination.getY();
            if (goalDestination.getX() < 0) x = 0;
            if (goalDestination.getY() < 0) y = 0;
            if (goalDestination.getX() >= myBoard.getWidth()) x = myBoard.getWidth()-1;
            if (goalDestination.getY() >= myBoard.getHeight()) y = myBoard.getHeight()-1;
            goalDestination = new Location(x,y);
            if (!myBoard.isOnBoard(goalDestination)) System.out.println("WTF"+ x + "   "+ y);
        }

        // only for calculating how many steps to go up, down, left, right
        int stepsX = goalDestination.getX() - loc.getX();
        int stepsY = goalDestination.getY() - loc.getY();
        // needed for loop
        int stepsNorth = 0;
        int stepsSouth = 0;
        int stepsWest = 0;
        int stepsEast = 0;

        if (stepsY < 0) stepsNorth = stepsY * (-1);
        else if (stepsY > 0) stepsSouth = stepsY;
        if (stepsX < 0) stepsWest = stepsX * (-1);
        else if (stepsX > 0) stepsEast = stepsX;


        double possibleSteps = maxMovementSpeed - stepsTaken;

        while (possibleSteps >= 1) {
            if (stepsNorth == 0
                    && stepsSouth == 0
                    && stepsWest == 0
                    && stepsEast == 0) return true;

            // move up + right
            if (stepsNorth >= 1 && stepsEast >= 1 && possibleSteps >= 1.5 && moveNorthEast()) {
                stepsNorth--;
                stepsEast--;
            }
            // move up + left
            if (stepsNorth >= 1 && stepsWest >= 1 && possibleSteps >= 1.5 && moveNorthWest()) {
                stepsNorth--;
                stepsWest--;
            }
            // move down + right
            if (stepsSouth >= 1 && stepsEast >= 1 && possibleSteps >= 1.5 && moveSouthEast()) {
                stepsSouth--;
                stepsEast--;
            }
            // move down + left
            if (stepsSouth >= 1 && stepsWest >= 1 && possibleSteps >= 1.5 && moveSouthWest()) {
                stepsSouth--;
                stepsWest--;
            }
            // move straight up
            if (stepsNorth >= 1 && moveNorth()) {
                stepsNorth--;
            }
            // move straight down
            if (stepsSouth >= 1 && moveSouth()) {
                stepsSouth--;
            }
            // move move straight right
            if (stepsEast >= 1 && moveEast()) {
                stepsEast--;
            }
            // move straight left
            if (stepsWest >= 1 && moveWest()) {
                stepsWest--;
            } else {
                break;
            }
            possibleSteps = maxMovementSpeed - stepsTaken;
        }
        return false;
    }

    /**
     * Moves the LivingCreature to the north east.
     * @return true if successful
     */
    public boolean moveNorthEast() {
        Location tmp = ((Location)getLocation().clone()).moveNorth().moveEast();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.5) return false;
        getLocation().moveNorth().moveEast();
        direction = "north";
        stepsTaken += 1.5;
        energy -= 1.5;
        return true;
    }
    /**
     * Moves the LivingCreature to the north west.
     * @return true if successful
     */
    public boolean moveNorthWest() {
        Location tmp = ((Location)getLocation().clone()).moveNorth().moveWest();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.5) return false;
        getLocation().moveNorth().moveWest();
        direction = "north";
        stepsTaken += 1.5;
        energy -= 1.5;
        return true;
    }
    /**
     * Moves the LivingCreature to the south east.
     * @return true if successful
     */
    public boolean moveSouthEast() {
        Location tmp = ((Location)getLocation().clone()).moveSouth().moveEast();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.5) return false;
        getLocation().moveSouth().moveEast();
        direction = "south";
        stepsTaken += 1.5;
        energy -= 1.5;
        return true;
    }
    /**
     * Moves the LivingCreature to the south west.
     * @return true if successful
     */
    public boolean moveSouthWest() {
        Location tmp = ((Location)getLocation().clone()).moveSouth().moveWest();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.5) return false;
        getLocation().moveSouth().moveWest();
        direction = "south";
        stepsTaken += 1.5;
        energy -= 1.5;
        return true;
    }
    /**
     * Moves the LivingCreature to the south.
     * @return true if successful
     */
    public boolean moveSouth() {
        Location tmp = ((Location)getLocation().clone()).moveSouth();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.0) return false;
        getLocation().moveSouth();
        direction = "south";
        stepsTaken += 1.0;
        energy--;
        return true;
    }
    /**
     * Moves the LivingCreature to the north.
     * @return true if successful
     */
    public boolean moveNorth() {
        Location tmp = ((Location)getLocation().clone()).moveNorth();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.0) return false;
        getLocation().moveNorth();
        direction = "north";
        stepsTaken += 1.0;
        energy--;
        return true;
    }
    /**
     * Moves the LivingCreature to the east.
     * @return true if successful
     */
    public boolean moveEast() {
        Location tmp = ((Location)getLocation().clone()).moveEast();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.0) return false;
        getLocation().moveEast();
        direction = "east";
        stepsTaken += 1.0;
        energy--;
        return true;
    }
    /**
     * Moves the LivingCreature to the west.
     * @return true if successful
     */
    public boolean moveWest() {
        Location tmp = ((Location)getLocation().clone()).moveWest();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.0) return false;
        getLocation().moveWest();
        direction = "west";
        stepsTaken += 1.0;
        energy--;
        return true;
    }

    /**
     * Generic getter for getting the current location.
     * @return current location.
     */
    public Location getLocation() {
        return loc;
    }

    /**
     * Generic getter for getting the memory.
     * @return the LivingCreatures memory.
     */
    public AI.Memory[] getLongTermMemory() {
        return brain.getLongTermMemory();
    }

    /**
     * Generic getter for getting the LivingCreatures strength.
     * @return strength.
     */
    public int getStrength() {
        return strength;
    }
    /**
     * Generic getter for getting the LivingCreatures maximum movement speed.
     * @return maxMovementSpeed.
     */
    public int getMaxMovementSpeed() {
        return maxMovementSpeed;
    }
    /**
     * Calculates how many steps a LivingCreature is still able to make this iteration.
     * @return amount of possible steps.
     */
    public double getPossibleSteps() {
        return maxMovementSpeed-stepsTaken;
    }
    /**
     * Generic getter for getting the LivingCreatures energy.
     * @return energy.
     */
    public int getEnergy() {
        return energy;
    }
    /**
     * Generic getter for getting the LivingCreatures sight distance.
     * @return sight distance.
     */
    public int getSightDistance() {
        return sightDistance;
    }
    /**
     * Generic getter for getting the LivingCreatures status.
     * @return status.
     */
    public String getStatus() {
        return brain.getStatusString();
    }
}
