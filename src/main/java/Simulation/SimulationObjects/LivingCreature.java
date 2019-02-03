package Simulation.SimulationObjects;

import Simulation.AI.AI;
import Simulation.AI.HunterAI;

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

    public void die() {
        myBoard.removeFromBoard(this);
    }

    public void eat(int calories) {
        if (this instanceof Prey) {
            myBoard.getStats().addFoodGainPrey(calories);
        } else if (this instanceof Hunter) {
            myBoard.getStats().addFoodGainHunter(calories);
        }
        energy = energy + calories;
    }

    public Location getLocation() {
        return loc;
    }

    public AI.Memory[] getLongTermMemory() {
        return brain.getLongTermMemory();
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

    public int getStrength() {
        return strength;
    }

    public int getMaxMovementSpeed() {
        return maxMovementSpeed;
    }

    public double getPossibleSteps() {
        return maxMovementSpeed-stepsTaken;
    }

    public int getEnergy() {
        return energy;
    }

    public boolean setDirection(int dir) {
        if (dir == 1) direction = "north";
        else if (dir == 2) direction = "east";
        else if (dir == 3) direction = "south";
        else if (dir == 4) direction = "west";
        else return false;
        return true;
    }

    public String getDirection() {
        return direction;
    }

    public int getDirectionNum() {
        switch (direction) {
            case "north":
                return 1;
            case "east":
                return 2;
            case "south":
                return 3;
            case "west":
                return 4;
        }
        return 0;
    }

    public int getSightDistance() {
        return sightDistance;
    }

    public String getStatus() {
        return brain.getStatusString();
    }
}
