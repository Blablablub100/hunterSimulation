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
    int hunger;
    String direction; // can be left, right, up, down

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
        // TODO TESTING + MAKE MORE READABLE
        if (myBoard.isOnBoard(goalDestination)) return false;


        // only for calculating how many steps to go up, down, left, right
        int stepsX = goalDestination.getX() - loc.getX();
        int stepsY = goalDestination.getY() - loc.getY();
        // needed for loop
        int stepsUp = 0;
        int stepsDown = 0;
        int stepsLeft = 0;
        int stepsRight = 0;

        if (stepsY < 0) stepsUp = stepsY * (-1);
        else if (stepsY > 0) stepsDown = stepsY;
        else if (stepsX < 0) stepsLeft = stepsX * (-1);
        else if (stepsX > 0) stepsRight = stepsX;


        double stepsTaken = 0.0;
        double possibleSteps = maxMovementSpeed - stepsTaken;

        while (possibleSteps >= 1) {
            if (stepsUp == 0
                    && stepsDown == 0
                    && stepsLeft == 0
                    && stepsRight == 0) return true;

            // move up + right
            if (stepsUp > 1 && stepsRight > 1 && possibleSteps >= 1.5 && moveUpRight()) {
                stepsUp--;
                stepsRight--;
                stepsTaken = stepsTaken + 1.5;
            }
            // move up + left
            else if (stepsUp > 1 && stepsLeft > 1 && possibleSteps >= 1.5 && moveUpLeft()) {
                stepsUp--;
                stepsRight--;
                stepsTaken = stepsTaken + 1.5;
            }
            // move down + right
            else if (stepsDown > 1 && stepsRight > 1 && possibleSteps >= 1.5 && moveDownRight()) {
                stepsDown--;
                stepsRight--;
                stepsTaken = stepsTaken + 1.5;
            }
            // move down + left
            else if (stepsDown > 1 && stepsLeft > 1 && possibleSteps >= 1.5 && moveDownLeft()) {
                loc.moveDown();
                loc.moveLeft();
                stepsDown--;
                stepsLeft--;
                stepsTaken = stepsTaken + 1.5;
            }
            // move straight up
            else if (stepsUp > 1 && moveUp()) {
                loc.moveUp();
                stepsUp--;
                stepsTaken = stepsTaken + 1.0;
            }
            // move straight down
            else if (stepsDown > 1 && moveDown()) {
                loc.moveDown();
                stepsDown--;
                stepsTaken = stepsTaken + 1.0;
            }
            // move move straight right
            else if (stepsRight > 1 && moveRight()) {
                loc.moveRight();
                stepsRight--;
                stepsTaken = stepsTaken + 1.0;
            }
            // move straight left
            else if (stepsLeft > 1 && moveLeft()) {
                loc.moveLeft();
                stepsLeft--;
                stepsTaken = stepsTaken + 1.0;
            }
            possibleSteps = maxMovementSpeed - stepsTaken;
        }
        return false;
    }

    private boolean moveUpRight() {
        Location tmp = ((Location)getLocation().clone()).moveUp().moveRight();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveUp().moveRight();
        return true;
    }

    private boolean moveUpLeft() {
        Location tmp = ((Location)getLocation().clone()).moveUp().moveLeft();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveUp().moveLeft();
        return true;
    }

    private boolean moveDownRight() {
        Location tmp = ((Location)getLocation().clone()).moveDown().moveRight();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveDown().moveRight();
        return true;
    }

     private boolean moveDownLeft() {
        Location tmp = ((Location)getLocation().clone()).moveDown().moveLeft();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveDown().moveLeft();
        return true;
    }

    private boolean moveDown() {
        Location tmp = ((Location)getLocation().clone()).moveDown();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveDown();
        return true;
    }

    private boolean moveUp() {
        Location tmp = ((Location)getLocation().clone()).moveUp();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveUp();
        return true;
    }

    private boolean moveRight() {
        Location tmp = ((Location)getLocation().clone()).moveRight();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveRight();
        return true;
    }

    private boolean moveLeft() {
        Location tmp = ((Location)getLocation().clone()).moveLeft();
        if (!myBoard.isEmpty(tmp)) return false;
        getLocation().moveLeft();
        return true;    }

    int getStrength() {
        return strength;
    }
}
