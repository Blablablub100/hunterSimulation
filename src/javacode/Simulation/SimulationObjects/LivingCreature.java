package javacode.Simulation.SimulationObjects;

import javacode.Simulation.AI.AI;

import java.util.List;

public abstract class LivingCreature implements BoardObject {

    AI brain;
    Board myBoard;
    Location loc;
    int maxMovementSpeed;
    int strength;
    int sightDistance;
    int energy;
    String direction; // can be north, south, east, west

    public abstract void react();

    abstract List<BoardObject> see();

    abstract void eat();

    abstract boolean attack(LivingCreature opponent);

    void die() {
        myBoard.removeFromBoard(this);
    }

    public Location getLocation() {
        return loc;
    }

    public boolean move(BoardObject.Location goalDestination) {
        // TODO MAKE MORE READABLE
        if (!myBoard.isOnBoard(goalDestination)) return false;

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


        double stepsTaken = 0.0;
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
                stepsTaken = stepsTaken + 1.5;
            }
            // move up + left
            else if (stepsNorth >= 1 && stepsWest >= 1 && possibleSteps >= 1.5 && moveNorthWest()) {
                stepsNorth--;
                stepsWest--;
                stepsTaken = stepsTaken + 1.5;
            }
            // move down + right
            else if (stepsSouth >= 1 && stepsEast >= 1 && possibleSteps >= 1.5 && moveSouthEast()) {
                stepsSouth--;
                stepsEast--;
                stepsTaken = stepsTaken + 1.5;
            }
            // move down + left
            else if (stepsSouth >= 1 && stepsWest >= 1 && possibleSteps >= 1.5 && moveSouthWest()) {
                stepsSouth--;
                stepsWest--;
                stepsTaken = stepsTaken + 1.5;
            }
            // move straight up
            else if (stepsNorth >= 1 && moveNorth()) {
                stepsNorth--;
                stepsTaken = stepsTaken + 1.0;
            }
            // move straight down
            else if (stepsSouth >= 1 && moveSouth()) {
                stepsSouth--;
                stepsTaken = stepsTaken + 1.0;
            }
            // move move straight right
            else if (stepsEast >= 1 && moveEast()) {
                stepsEast--;
                stepsTaken = stepsTaken + 1.0;
            }
            // move straight left
            else if (stepsWest >= 1 && moveWest()) {
                stepsWest--;
                stepsTaken = stepsTaken + 1.0;
            } else {
                // no movement possible -> stop trying
                stepsTaken = maxMovementSpeed;
            }
            possibleSteps = maxMovementSpeed - stepsTaken;
        }
        return false;
    }

    private boolean moveNorthEast() {
        Location tmp = ((Location)getLocation().clone()).moveNorth().moveEast();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveNorth().moveEast();
        direction = "north";
        return true;
    }

    private boolean moveNorthWest() {
        Location tmp = ((Location)getLocation().clone()).moveNorth().moveWest();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveNorth().moveWest();
        direction = "north";
        return true;
    }

    private boolean moveSouthEast() {
        Location tmp = ((Location)getLocation().clone()).moveSouth().moveEast();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveSouth().moveEast();
        direction = "south";
        return true;
    }

     private boolean moveSouthWest() {
        Location tmp = ((Location)getLocation().clone()).moveSouth().moveWest();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveSouth().moveWest();
        direction = "south";
        return true;
    }

    private boolean moveSouth() {
        Location tmp = ((Location)getLocation().clone()).moveSouth();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveSouth();
        direction = "south";
        return true;
    }

    private boolean moveNorth() {
        Location tmp = ((Location)getLocation().clone()).moveNorth();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveNorth();
        direction = "north";
        return true;
    }

    private boolean moveEast() {
        Location tmp = ((Location)getLocation().clone()).moveEast();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveEast();
        direction = "east";
        return true;
    }

    private boolean moveWest() {
        Location tmp = ((Location)getLocation().clone()).moveWest();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveWest();
        direction = "west";
        return true;
    }

    int getStrength() {
        return strength;
    }
}
