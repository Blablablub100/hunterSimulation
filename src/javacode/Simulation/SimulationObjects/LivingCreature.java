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
    double stepsTaken;

    public abstract void react();

    public abstract List<BoardObject> see();

    public abstract boolean attack(LivingCreature opponent);

    void die() {
        myBoard.removeFromBoard(this);
    }

    void eat(int calories) {
        energy = energy + calories;
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
            else if (stepsNorth >= 1 && stepsWest >= 1 && possibleSteps >= 1.5 && moveNorthWest()) {
                stepsNorth--;
                stepsWest--;
            }
            // move down + right
            else if (stepsSouth >= 1 && stepsEast >= 1 && possibleSteps >= 1.5 && moveSouthEast()) {
                stepsSouth--;
                stepsEast--;
            }
            // move down + left
            else if (stepsSouth >= 1 && stepsWest >= 1 && possibleSteps >= 1.5 && moveSouthWest()) {
                stepsSouth--;
                stepsWest--;
            }
            // move straight up
            else if (stepsNorth >= 1 && moveNorth()) {
                stepsNorth--;
            }
            // move straight down
            else if (stepsSouth >= 1 && moveSouth()) {
                stepsSouth--;
            }
            // move move straight right
            else if (stepsEast >= 1 && moveEast()) {
                stepsEast--;
            }
            // move straight left
            else if (stepsWest >= 1 && moveWest()) {
                stepsWest--;
            } else {
                break;
            }
            possibleSteps = maxMovementSpeed - stepsTaken;
        }
        return false;
    }

    private boolean moveNorthEast() {
        Location tmp = ((Location)getLocation().clone()).moveNorth().moveEast();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.5) return false;
        getLocation().moveNorth().moveEast();
        direction = "north";
        stepsTaken += 1.5;
        return true;
    }

    private boolean moveNorthWest() {
        Location tmp = ((Location)getLocation().clone()).moveNorth().moveWest();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.5) return false;
        getLocation().moveNorth().moveWest();
        direction = "north";
        stepsTaken += 1.5;
        return true;
    }

    private boolean moveSouthEast() {
        Location tmp = ((Location)getLocation().clone()).moveSouth().moveEast();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.5) return false;
        getLocation().moveSouth().moveEast();
        direction = "south";
        stepsTaken += 1.5;
        return true;
    }

     private boolean moveSouthWest() {
        Location tmp = ((Location)getLocation().clone()).moveSouth().moveWest();
        if (!myBoard.isEmpty(tmp)) return false;
         if (getPossibleSteps() < 1.5) return false;
        getLocation().moveSouth().moveWest();
        direction = "south";
         stepsTaken += 1.5;
         return true;
    }

    private boolean moveSouth() {
        Location tmp = ((Location)getLocation().clone()).moveSouth();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.0) return false;
        getLocation().moveSouth();
        direction = "south";
        stepsTaken += 1.0;
        return true;
    }

    private boolean moveNorth() {
        Location tmp = ((Location)getLocation().clone()).moveNorth();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.0) return false;
        getLocation().moveNorth();
        direction = "north";
        stepsTaken += 1.0;
        return true;
    }

    private boolean moveEast() {
        Location tmp = ((Location)getLocation().clone()).moveEast();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.0) return false;
        getLocation().moveEast();
        direction = "east";
        stepsTaken += 1.0;
        return true;
    }

    private boolean moveWest() {
        Location tmp = ((Location)getLocation().clone()).moveWest();
        if (!myBoard.isEmpty(tmp)) return false;
        if (getPossibleSteps() < 1.0) return false;
        getLocation().moveWest();
        direction = "west";
        stepsTaken += 1.0;
        return true;
    }

    public int getStrength() {
        return strength;
    }

    public double getPossibleSteps() {
        return maxMovementSpeed-stepsTaken;
    }

    int getRandom(int lowerBound, int upperBound) {
        int randomNumber = lowerBound + (int)(Math.random() * ((upperBound - lowerBound) + 1));
        System.out.println(randomNumber);
        return randomNumber;
    }
}
