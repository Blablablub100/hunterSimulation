package Simulation.SimulationObjects;

import Simulation.AI.AI;
import Simulation.AI.GroupAI;
import Simulation.AI.HunterAI;
import UI.FullUserInput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Hunter extends LivingCreature {

    Hunter(Location loc, Board myBoard) {
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
        energy = 490;
        sightDistance = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        brain = new HunterAI(this);
    }

    Hunter(Location loc, Board myBoard, int speed, int strength, int sight, int energy) {
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
        brain = new HunterAI(this);
    }


    @Override
    public void react() {
        if (energy <= 0) {
            ((HunterAI) brain).leaveGroup();
            die();
            myBoard.getStats().incHunterStarved();
            return;
        }
        stepsTaken = 0.0;
        brain.react();
    }


    @Override
    public boolean attack(LivingCreature opponent) {
        if (getLocation().getDistance(opponent.getLocation()) > 1) return false;
        if (opponent.getStrength() > getStrength()) return false;
        if (!(opponent instanceof Prey)) return false;
        if (!myBoard.getPreys().contains(opponent)) return false;
        Location tmp = opponent.getLocation();
        eat(opponent.getStrength());
        opponent.die();
        move(tmp);
        getBoard().getStats().incPreyKilledByHunter();
        return true;
    }

    public int getIndividualStrength() {
        return super.getStrength();
    }

    public boolean isGroupMember() {
        return ((HunterAI) brain).isGroupmember();
    }

    @Override
    public int getStrength() {
        if (((HunterAI) brain).isGroupmember()) {
            return ((HunterAI) brain).getGroupStrength();
        }
        return super.getStrength();
    }

    @Override
    public void die() {
        ((HunterAI)brain).leaveGroup();
        super.die();
    }

    public GroupAI getGroup() {
        return ((HunterAI) brain).getGroup();
    }

    public Board getBoard() {
        return myBoard;
    }

    public boolean receiveGroupInvitation(GroupAI group) {
        return ((HunterAI)brain).joinGroup(group);
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
        for (Location location : toCheck) {
            BoardObject tmp = myBoard.getObjectAtLocation(location);
            if (tmp != null) res.add(tmp);
        }
        return res;
    }
}
