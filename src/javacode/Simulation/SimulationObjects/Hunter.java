package javacode.Simulation.SimulationObjects;

import java.util.ArrayList;
import java.util.List;

public class Hunter extends LivingCreature {

    Hunter(Location loc, Board myBoard) {
        this.loc = loc;
        this.myBoard = myBoard;
        sightDistance = 5;
        direction = "east";
        maxMovementSpeed = 2;
        strength = 1;
        energy = 10;
        brain = null;
    }


    @Override
    public void react() {
        List<BoardObject> tmp = see();
    }

    @Override
    void eat() {
        BoardObject tmp = myBoard.getObjectAtLocation(getLocation());
        if (!(tmp instanceof Prey)) return;
        Prey toEat = (Prey) tmp;
        if (attack(toEat)) energy = energy + toEat.getStrength();
    }

    @Override
    boolean attack(LivingCreature opponent) {
        if (!getLocation().equals(opponent.getLocation())) return false;
        if (opponent.getStrength() < getStrength()) return false;
        if (!(opponent instanceof Prey)) return false;
        opponent.die();
        return true;
    }


    // seeing logic starting from here
    @Override
    public List<BoardObject> see() {
        // things that the hunter is able to see
        switch (direction) {
            case "north":
                return seeNorth();
            case "south":
                return seeSouth();
            case "west":
                return seeWest();
            case "east":
                return seeEast();
            default:
                direction = "north";
                return see();
        }
    }

    private List<BoardObject> seeNorth() {
        BoardObject.Location tmp = (BoardObject.Location) getLocation().clone();
        List<BoardObject.Location> toCheck = new ArrayList<>();

        int stepsNorthSeen = 1;
        while (stepsNorthSeen <= sightDistance) {
            tmp.moveNorth();
            scanViewWidth(toCheck, tmp, stepsNorthSeen);
            stepsNorthSeen++;
        }
        return scan(toCheck);
    }

    private List<BoardObject> seeSouth() {
        BoardObject.Location tmp = (BoardObject.Location) getLocation().clone();
        List<BoardObject.Location> toCheck = new ArrayList<>();

        int stepsSouthSeen = 1;
        while (stepsSouthSeen <= sightDistance) {
            tmp.moveSouth();
            scanViewWidth(toCheck, tmp, stepsSouthSeen);
            stepsSouthSeen++;
        }
        return scan(toCheck);
    }

    private void scanViewWidth(List<BoardObject.Location> toCheck, Location tmp, int stepsSeen) {
        int stepsWest = stepsSeen;
        while (stepsWest > 0) {
            tmp.moveWest();
            stepsWest--;
        }
        for (int i = 0; i < stepsSeen*2+1; i++) {
            toCheck.add((BoardObject.Location)tmp.clone());
            tmp.moveEast();
        }
        tmp.moveWest();
        stepsWest = stepsSeen;
        while (stepsWest > 0) {
            tmp.moveWest();
            stepsWest--;
        }
    }

    private List<BoardObject> seeWest() {
        BoardObject.Location tmp = (BoardObject.Location) getLocation().clone();
        List<BoardObject.Location> toCheck = new ArrayList<>();

        int stepsWestSeen = 1;
        while (stepsWestSeen <= sightDistance) {
            tmp.moveWest();
            scanViewHeight(toCheck, tmp, stepsWestSeen);
            stepsWestSeen++;
        }
        return scan(toCheck);
    }

    private List<BoardObject> seeEast() {
        BoardObject.Location tmp = (BoardObject.Location) getLocation().clone();
        List<BoardObject.Location> toCheck = new ArrayList<>();

        int stepsEastSeen = 1;
        while (stepsEastSeen <= sightDistance) {
            tmp.moveEast();
            scanViewHeight(toCheck, tmp, stepsEastSeen);
            stepsEastSeen++;
        }
        return scan(toCheck);
    }

    private void scanViewHeight(List<BoardObject.Location> toCheck, Location tmp, int stepsSeen) {
        int stepsNorth = stepsSeen;
        while (stepsNorth > 0) {
            tmp.moveNorth();
            stepsNorth--;
        }
        for (int i = 0; i < stepsSeen*2+1; i++) {
            toCheck.add((BoardObject.Location)tmp.clone());
            tmp.moveSouth();
        }
        tmp.moveNorth();
        stepsNorth = stepsSeen;
        while (stepsNorth > 0) {
            tmp.moveNorth();
            stepsNorth--;
        }
    }

    private List<BoardObject> scan(List<BoardObject.Location> toCheck) {
        List<BoardObject> res = new ArrayList<>();
        for (int i = 0; i < toCheck.size(); i++) {
            BoardObject tmp = myBoard.getObjectAtLocation(toCheck.get(i));
            if (tmp != null) res.add(tmp);
        }
        return res;
    }
}
