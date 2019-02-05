package Simulation.SimulationObjects;

import Simulation.AI.GroupAI;
import Simulation.AI.HunterAI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class representing a hunter in a simulation. Provides core functionality of a hunter.
 */
public class Hunter extends LivingCreature {

    /**
     * Constructor for creating a new Hunter. Every attribute despite of the location and the board will be assigned
     * randomly.
     * @param loc location where the hunter is going to start.
     * @param myBoard board the hunter will be upon.
     */
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

    /**
     * Constructor for creating a new Hunter.
     * @param loc location where the hunter is going to start.
     * @param myBoard board the hunter will be upon.
     * @param speed the speed corresponds to the maximum amount of steps a hunter can make in one iteration.
     * @param strength the strength of the hunter.
     * @param sight the sight corresponds to the viewing distance of the hunter.
     * @param energy initial energy of the hunter. If this value is lower then zero the hunter will starve immediately.
     */
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

    /**
     * Reacts to the current situation. This method is only called by the SimulationController.
     */
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

    /**
     * Attacks another LivingCreature and eats it if the attack was successful.
     * @param opponent LivingCreature that is going to be attack.
     * @return true if the attack was successful.
     */
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

    /**
     * Get the strength of this specific individual hunter.
     * @return strength value of this hunter.
     */
    public int getIndividualStrength() {
        return super.getStrength();
    }

    /**
     * checks whether the hunter is a group member.
     * @return true if hunter is a group member.
     */
    public boolean isGroupMember() {
        return ((HunterAI) brain).isGroupmember();
    }

    /**
     * Gets the current strength of the hunter. If the hunter is a group member the gorup strength will be returned.
     * @return strength or group strength of the hunter.
     */
    @Override
    public int getStrength() {
        if (((HunterAI) brain).isGroupmember()) {
            return ((HunterAI) brain).getGroupStrength();
        }
        return super.getStrength();
    }

    /**
     * Will remove the hunter from the simulation.
     */
    @Override
    public void die() {
        ((HunterAI)brain).leaveGroup();
        super.die();
    }

    /**
     * Generic 
     * @return
     */
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
