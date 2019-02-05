package Simulation.SimulationObjects;

import Simulation.AI.PreyAI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class representing a prey in a simulation. Provides core functionality of a prey.

 */
public class Prey extends LivingCreature {

    /**
     * Constructor for creating a new prey. Every attribute despite of the location and the board will be assigned
     * randomly.
     * @param loc location where the prey is going to start.
     * @param myBoard board the prey will be upon.
     */
    Prey(Location loc, Board myBoard) {
        brain = new PreyAI(this);
        this.loc = loc;
        this.myBoard = myBoard;
        int random = ThreadLocalRandom.current().nextInt(1, 4 + 1);
        if(random == 1){
            direction = "north";
        } else if(random == 2){
            direction = "east";
        } else if(random == 3){
            direction = "south";
        } else if(random == 4){
            direction = "west";
        }
        maxMovementSpeed = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        strength = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        strength = 10; // TODO remove just for testing
        energy = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        sightDistance = ThreadLocalRandom.current().nextInt(1, 10 + 1);
    }
    /**
     * Constructor for creating a new prey.
     * @param loc location where the prey is going to start.
     * @param myBoard board the prey will be upon.
     * @param speed the speed corresponds to the maximum amount of steps a prey can make in one iteration.
     * @param strength the strength of the prey.
     * @param sight the sight corresponds to the viewing distance of the prey.
     * @param energy initial energy of the prey. If this value is lower then zero the hunter will starve immediately.
     */
    Prey(Location loc, Board myBoard, int speed, int strength, int sight, int energy) {
        this.loc = loc;
        this.myBoard = myBoard;
        int random = ThreadLocalRandom.current().nextInt(1, 4 + 1);
        if(random == 1){
            direction = "north";
        } else if(random == 2){
            direction = "east";
        } else if(random == 3){
            direction = "south";
        } else if(random == 4){
            direction = "west";
        }
        maxMovementSpeed = speed;
        this.strength = strength;
        this.energy = energy;
        this.sightDistance = sight;
        brain = new PreyAI(this);
    }

    /**
     * Reacts to the current situation. This method is only called by the SimulationController.
     */
    @Override
    public void react() {
        if (energy <= 0) {
            die();
            myBoard.getStats().incPreyStarved();
            return;
        }
        stepsTaken = 0.0;
        brain.react();
    }

    /**
     * This method calculates a list of things the prey is able to see.
     * @return List of BoardObjects the prey can see.
     */
    @Override
    public List<BoardObject> see() {

        List<BoardObject> objectsSeen = new ArrayList<>();

        switch (direction) {
            case "north":
                seeNorth(objectsSeen);
                break;

            case "south":
                seeSouth(objectsSeen);
                break;

            case "west":
                seeWest(objectsSeen);
                break;

            case "east":
                seeEast(objectsSeen);
                break;
        }
        return objectsSeen;
    }
    /**
     * Used for scanning area north of prey.
     */
    private void seeNorth(List<BoardObject> objectsSeen){
        // in viewing direction
        int viewWidth = 1;

        for (int i = -1; i >= (-(sightDistance)); i--) {
            int currY = loc.getY() + i;

            northAndSouthScanOfForwardAndBackwards(objectsSeen, viewWidth, currY);
            viewWidth++;
        }

        // opposite direction of viewing direction
        viewWidth = 1;
        int sightDistanceTmp = sightDistance;
        if(sightDistanceTmp < 4) sightDistanceTmp = 4;

        for (int i = 1; i <= (sightDistanceTmp / 4); i++){
            int currY = loc.getY() + i;

            northAndSouthScanOfForwardAndBackwards(objectsSeen, viewWidth, currY);
            viewWidth++;
        }

        // left side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if(sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = -1; i >= (-(sightDistanceTmp / 2)); i--){
            int currX = loc.getX() + i;

            northAndSouthScanOfLeftAndRight(objectsSeen, viewWidth, currX);
            viewWidth++;
        }

        // right side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if(sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = 1; i <= (sightDistanceTmp / 2); i++){
            int currX = loc.getX() + i;

            northAndSouthScanOfLeftAndRight(objectsSeen, viewWidth, currX);
            viewWidth++;
        }
    }
    /**
     * Used for scanning area south of prey.
     */
    private void seeSouth(List<BoardObject> objectsSeen){
        // in viewing direction
        int viewWidth = 1;

        for (int i = 1; i <= sightDistance; i++) {
            int currY = loc.getY() + i;

            northAndSouthScanOfForwardAndBackwards(objectsSeen, viewWidth, currY);
            viewWidth++;
        }

        // opposite direction of viewing direction
        viewWidth = 1;
        int sightDistanceTmp = sightDistance;
        if(sightDistanceTmp < 4) sightDistanceTmp = 4;

        for (int i = -1; i >= (-(sightDistanceTmp / 4)); i--){
            int currY = loc.getY() + i;

            northAndSouthScanOfForwardAndBackwards(objectsSeen, viewWidth, currY);
            viewWidth++;
        }

        // right side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if(sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = -1; i >= (-(sightDistanceTmp / 2)); i--){
            int currX = loc.getX() + i;

            northAndSouthScanOfLeftAndRight(objectsSeen, viewWidth, currX);
            viewWidth++;
        }

        // left side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if(sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = 1; i <= (sightDistanceTmp / 2); i++){
            int currX = loc.getX() + i;

            northAndSouthScanOfLeftAndRight(objectsSeen, viewWidth, currX);
            viewWidth++;
        }
    }

    /**
     * Used for scanning area west of prey.
     */
    private void seeWest(List<BoardObject> objectsSeen){
        // in viewing direction
        int viewWidth = 1;

        for (int i = -1; i >= (-(sightDistance)); i--) {
            int currX = loc.getX() + i;

            westAndEastScanOfForwardAndBackwards(objectsSeen, viewWidth, currX);
            viewWidth++;
        }

        // opposite direction of viewing direction
        viewWidth = 1;
        int sightDistanceTmp = sightDistance;
        if (sightDistanceTmp < 4) sightDistanceTmp = 4;

        for (int i = 1; i <= (sightDistanceTmp / 4); i++) {
            int currX = loc.getX() + i;

            westAndEastScanOfForwardAndBackwards(objectsSeen, viewWidth, currX);
            viewWidth++;
        }

        // right side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if (sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = -1; i >= (-(sightDistanceTmp / 2)); i--) {
            int currY = loc.getY() + i;

            westAndEastScanOfLeftAndRight(objectsSeen, viewWidth, currY);
            viewWidth++;
        }

        // left side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if (sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = 1; i <= (sightDistanceTmp / 2); i++) {
            int currY = loc.getY() + i;

            westAndEastScanOfLeftAndRight(objectsSeen, viewWidth, currY);
            viewWidth++;
        }
    }
    /**
     * Used for scanning area east of hunter.
     */
    private void seeEast(List<BoardObject> objectsSeen){
        // in viewing direction
        int viewWidth = 1;

        for (int i = 1; i <= sightDistance; i++) {
            int currX = loc.getX() + i;

            westAndEastScanOfForwardAndBackwards(objectsSeen, viewWidth, currX);
            viewWidth++;
        }

        // opposite direction of viewing direction
        viewWidth = 1;
        int sightDistanceTmp = sightDistance;
        if (sightDistanceTmp < 4) sightDistanceTmp = 4;

        for (int i = -1; i >= (-(sightDistanceTmp / 4)); i--) {
            int currX = loc.getX() + i;

            westAndEastScanOfForwardAndBackwards(objectsSeen, viewWidth, currX);
            viewWidth++;
        }

        // left side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if (sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = -1; i >= (-(sightDistanceTmp / 2)); i--) {
            int currY = loc.getY() + i;

            westAndEastScanOfLeftAndRight(objectsSeen, viewWidth, currY);
            viewWidth++;
        }

        // right side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if (sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = 1; i <= (sightDistanceTmp / 2); i++) {
            int currY = loc.getY() + i;

            westAndEastScanOfLeftAndRight(objectsSeen, viewWidth, currY);
            viewWidth++;
        }
    }
    /**
     * Helping method for seeing.
     * @param objectsSeen objects that are seen.
     * @param viewWidth width of how wide a prey is able to see.
     * @param currY current y location.
     */
    private void northAndSouthScanOfForwardAndBackwards(List<BoardObject> objectsSeen, int viewWidth, int currY) {
        for (int j = viewWidth * (-1); j <= viewWidth; j++) {
            int currX = loc.getX() + j;
            BoardObject tmp = myBoard.getObjectAtLocation(new Location(currX, currY));
            if (tmp != null) objectsSeen.add(tmp);
        }
    }
    /**
     * Helping method for seeing.
     * @param objectsSeen objects that are seen.
     * @param viewWidth width of how wide a prey is able to see.
     * @param currX current x location.
     */
    private void northAndSouthScanOfLeftAndRight(List<BoardObject> objectsSeen, int viewWidth, int currX) {
        if(viewWidth == 0){
            int currY = loc.getY();
            BoardObject tmp = myBoard.getObjectAtLocation(new Location(currX, currY));
            if(tmp != null) objectsSeen.add(tmp);
        } else {
            for (int j = viewWidth * (-1); j <= viewWidth; j++) {
                int currY = loc.getY() + j;
                BoardObject tmp = myBoard.getObjectAtLocation(new Location(currX, currY));
                if (tmp != null) objectsSeen.add(tmp);
            }
        }
    }
    /**
     * Helping method for seeing.
     * @param objectsSeen objects that are seen.
     * @param viewWidth width of how wide a prey is able to see.
     * @param currX current x location.
     */
    private void westAndEastScanOfForwardAndBackwards(List<BoardObject> objectsSeen, int viewWidth, int currX) {
        for (int j = viewWidth * (-1); j <= viewWidth; j++) {
            int currY;
            currY = loc.getY() + j;
            BoardObject tmp = myBoard.getObjectAtLocation(new Location(currX, currY));
            if (tmp != null) objectsSeen.add(tmp);
        }
    }
    /**
     * Helping method for seeing.
     * @param objectsSeen objects that are seen.
     * @param viewWidth width of how wide a prey is able to see.
     * @param currY current y location.
     */
    private void westAndEastScanOfLeftAndRight(List<BoardObject> objectsSeen, int viewWidth, int currY) {
        if(viewWidth == 0){
            int currX = loc.getX();
            BoardObject tmp = myBoard.getObjectAtLocation(new Location(currX, currY));
            if(tmp != null) objectsSeen.add(tmp);
        } else {
            for (int j = viewWidth * (-1); j <= viewWidth; j++) {
                int currX;
                currX = loc.getX() + j;
                BoardObject tmp = myBoard.getObjectAtLocation(new Location(currX, currY));
                if (tmp != null) objectsSeen.add(tmp);
            }
        }
    }

    /**
     * Attacks another LivingCreature and eats it if the attack was successful.
     * @param opponent LivingCreature that is going to be attack.
     * @return true if the attack was successful.
     */
    @Override
    public boolean attack(LivingCreature opponent) {
        if (getLocation().getDistance(opponent.getLocation()) > 1) return false;
        if (opponent.getStrength() > getStrength()) return false;
        if (!(opponent instanceof Hunter)) return false;
        Location tmp = opponent.getLocation();
        opponent.die();
        move(tmp);
        myBoard.getStats().incHunterKilledByPrey();
        return true;
    }
}
