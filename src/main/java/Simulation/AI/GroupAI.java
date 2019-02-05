package Simulation.AI;

import Simulation.SimulationObjects.BoardObject;
import Simulation.SimulationObjects.DeadCorpse;
import Simulation.SimulationObjects.Hunter;
import Simulation.SimulationObjects.Prey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Provides the ability for hunter to group together to group hunt strong prey.
 * Works like this:
 *     1. check if already called this step
 *     2. if not calculate Locations and let everybody move towards it
 *     3. When target is reached cadaver is spawned
 *     4. Every hunter eats a fair share
 */
public class GroupAI {

    /**
     * List containing all members of the group.
     */
    private List<HunterAI> members;
    /**
     * An object of the target the gorup is going to hunter.
     */
    private Prey target;
    /**
     * An object of the leader of the group. The leader will determine where each hunter has to go.
     */
    private HunterAI leader;
    /**
     * Counter of how long a formed group is already acting together. This attribute is used to ungroup an unsuccessful
     * group.
     */
    private int time;
    /**
     * Bool value that represents whether the group is hunting or not.
     */
    private boolean hunting = false;
    /**
     * HashMap of all group members with their corresponding goal locations.
     */
    private HashMap<HunterAI, BoardObject.Location> goals;
    /**
     * Object of the carrion of the dead target.
     * Null if target is still alive.
     */
    private DeadCorpse corpse;

    /**
     * The only constructor to create a new GroupAI.
     * @param leader the leader of the newly created Group.
     * @param target the target the group is going to hunt.
     */
    GroupAI(HunterAI leader, Prey target) {
        this.leader = leader;
        this.target = target;
        this.time = 0;
        members = new ArrayList<>();
        members.add(leader);
    }

    /**
     * This method called every iteration by each hunter that is a group member.
     * It decides what each group member should do and it will increment the time counter if the group leader calls this
     * method.
     * @param h the hunter that calls this method.
     */
    void steerGroup(HunterAI h) {
        if (time > 20 && getGroupStrength() < target.getStrength()) {
            if (corpse != null) corpse.rot();
            ungroup();
            return;
        }
        if (h == leader)  {

            if (getGroupStrength() > target.getStrength()) {
                calcGoals();
                h.getBody().move(goals.get(h));
                hunting = true;
            } else {
                hunting = false;
            }
            time++;
        }
        steer(h);
    }

    /**
     * This method will control a specific hunter. It will change its status and telling the hunter where to move, when
     * to attack etc.
     * @param h Object of the hunter that is going to be controlled.
     */
    private void steer(HunterAI h) {
        if (corpse != null) {
            h.getBody().move(corpse.getLocation());
            h.getStatus().setStatus("reaching carrion");
            if (h.getBody().getLocation().getDistance(corpse.getLocation()) == 1) {
                h.getBody().eat(corpse.eat());
                h.leaveGroup();
                h.getStatus().setStatus("calm");
            } else {
                h.moveRandomly();
            }
        } else if (!h.getBody().getBoard().getPreys().contains(target)) {
            ungroup();
        } else if (hunting && goals.containsKey(h)) {
            h.getStatus().setStatus("hunting");
            h.getBody().move(goals.get(h));
            if (h.getBody().getLocation().getDistance(target.getLocation()) == 1
                    && (isMemberNearMe(h) || twoMembersNearTarget())) {
                DeadCorpse tmp = new DeadCorpse(
                        h.getBody().getBoard()
                        , target.getLocation()
                        , target.getStrength()
                        , members.size());
                target.die();
                h.getBody().getBoard().getStats().incPreyKilledByHunter();
                time = 15;
                h.getBody().getBoard().spawn(tmp);
                calcGoals();
                corpse = tmp;
            }
        } else {
            h.getStatus().setStatus("searching group members");
            h.searchForGroupMembers();
        }
    }

    /**
     * This method checks whether there are two group members near a target. This is important because the target prey
     * can only die if there are at least two group mebers near it.
     * @return returns true if there are at least two group members near the target.
     */
    private boolean twoMembersNearTarget() {
        boolean oneNear = false;
        BoardObject.Location targetLoc = target.getLocation();
        for (HunterAI member: members) {
            if (member.getBody().getLocation().getDistance(targetLoc) == 1) {
                if (oneNear) return true;
                oneNear = true;
            }
        }
        return false;
    }
    /**
     * This method checks if a group member is near a target.
     * @param h group member that is going to be checked.
     * @return true if the group member is near the target.
     */
    private boolean isMemberNearMe(HunterAI h) {
        return (isMemberNearby(h.getBody().getLocation()));
    }
    /**
     * This method checks if any group member is near a location.
     * @param startLoc the location that is going to be checked.
     * @return true if there is a group member near startloc.
     */
    private boolean isMemberNearby(BoardObject.Location startLoc) {
        for (HunterAI member: members) {
            if (member.getBody().getLocation().getDistance(startLoc) == 1) return true;
        }
        return false;
    }

    /**
     * This method saves the goal for every group member and saves it into the hashset. This is important so that the
     * group can make a formation to circle the target.
     */
    private void calcGoals() {
        goals = new HashMap<>();
        HunterAI nearest = getNearestMember();

        BoardObject.Location nearestGoal = getNearestLocByTarget(nearest, target.getLocation());
        BoardObject.Location[] goalLocations = calcGoals(nearestGoal);

        for (HunterAI member : members) {
            if (member == nearest) goals.put(nearest, nearestGoal);
            else {
                BoardObject.Location currGoal = getNearestLocation(member, goalLocations);
                if (currGoal != null) {
                    goals.put(member, (BoardObject.Location) currGoal.clone());
                    int index = Arrays.asList(goalLocations).indexOf(currGoal);
                    goalLocations[index] = null;
                } else goals.put(member, (BoardObject.Location) nearestGoal.clone());
            }
        }
    }

    /**
     * This method calculates the goal destination for every group member based on the location of the nearest group
     * member. This method basically calculated where each hunter has to corner the prey.
     * @param nearestGoal the nearest location near the target.
     * @return an array of goal locations.
     */
    private BoardObject.Location[] calcGoals(BoardObject.Location nearestGoal) {
        BoardObject.Location[] goals = new BoardObject.Location[members.size()-1];
        String direction = target.getLocation().wayTo(nearestGoal);
        String dir1;
        String dir2;

        if (direction.contains("north") || direction.contains("south")) {
            dir1 = "east";
            dir2 = "west";
        } else {
            dir1 = "north";
            dir2 = "south";
        }

        int steps = 1;
        for (int i = 0; i < goals.length; i++) {
            BoardObject.Location tmp = (BoardObject.Location) nearestGoal.clone();
            if ((i % 2) == 0) {

                for (int j = 0; j < steps; j++) {
                    tmp.move(dir1);
                }
            } else {

                for (int j = 0; j < steps; j++) {
                    tmp.move(dir2);
                }
                steps++;
            }
            goals[i] = tmp;
        }
        return goals;
    }

    /**
     * This method calculated the nearest location by the target for a specific group member.
     * @param h group member whose nearest location to the target is calculated.
     * @param target target location.
     * @return nearest location at the target for a hunter.
     */
    private BoardObject.Location getNearestLocByTarget(HunterAI h, BoardObject.Location target) {
        BoardObject.Location[] possibilities = new BoardObject.Location[4];
        BoardObject.Location targetC = (BoardObject.Location) target.clone();
        possibilities[0] = (BoardObject.Location) targetC.moveNorth().clone();
        possibilities[1] = (BoardObject.Location) targetC.moveEast().moveSouth().clone();
        possibilities[2] = (BoardObject.Location) targetC.moveSouth().moveWest().clone();
        possibilities[3] = (BoardObject.Location) targetC.moveWest().moveNorth().clone();
        return getNearestLocation(h, possibilities);
    }

    /**
     * Calculates the nearest location for a hunter out of a group of different possible locations.
     * @param h hunter whose location is going to be used.
     * @param locs locations the hunter locations is going to be tested upon.
     * @return the nearest location for h out of locs.
     */
    private BoardObject.Location getNearestLocation(HunterAI h, BoardObject.Location... locs) {
        if (locs == null || locs.length < 1) return null;
        BoardObject.Location nearest = null;
        int nearestDist = Integer.MAX_VALUE;

        for (BoardObject.Location loc : locs) {
            if (loc != null) {
                int tmpDist = getDistance(h, loc);
                if (tmpDist < nearestDist) {
                    nearest = loc;
                    nearestDist = tmpDist;
                }
            }
        }
        return nearest;
    }

    /**
     * Calculates the distance between a hunter and a location.
     * @param h hunter whose location is going to be used.
     * @param loc location that is going to be tested.
     * @return how many cells the hunter is away from the location.
     */
    private int getDistance(HunterAI h, BoardObject.Location loc) {
        return h.getBody().getLocation().getDistance(loc);
    }

    /**
     * Gets the nearest group member to the target.
     * @return nearest goup member to the target.
     */
    private HunterAI getNearestMember() {
        HunterAI nearest = null;
        for (HunterAI hunterBrain: members) {
            if (nearest == null) nearest = hunterBrain;
            else if (nearest.getBody().getLocation().getDistance(
                target.getLocation()
            ) > hunterBrain.getBody().getLocation().getDistance(
                    target.getLocation()
            )) {
                nearest = hunterBrain;
            }
        }
        return nearest;
    }

    /**
     * Adds a new hunter to the group.
     * @param h hunter that is going to be added.
     */
    void add(HunterAI h) {
        members.add(h);
        h.joinGroup(this);
    }

    /**
     * Removes a hunter from the group.
     * @param h hunter that is going to be removed.
     */
    void remove(HunterAI h) {
        if (!members.contains(h)) return;
        members.remove(h);
        if (members.size() == 0) {
            if (corpse != null) corpse.rot();
            return;
        }
        if (h == leader) leader = members.get(0);
        h.leaveGroup();
    }

    /**
     * Calculates the sum of all group members. This value corresponds to the group strength.
     * @return group strength.
     */
    int getGroupStrength() {
        int groupStrength = 0;
        for (HunterAI member : members) {
            int memberStrength = ((Hunter) member.getBody()).getIndividualStrength();
            groupStrength = groupStrength + memberStrength;
        }
        return groupStrength;
    }

    /**
     * Deletes the group.
     */
    private void ungroup() {
        for (int i = 0; i < members.size(); i++) {
            HunterAI tmp = members.get(i);
            if (tmp != null) {
                tmp.leaveGroup();
                remove(tmp);
            }
        }
        if (corpse != null) {
            corpse.rot();
        }
    }

    /**
     * Gets the amount of group members.
     * @return How many members the group has.
     */
    public int getGroupSize() {
        if (corpse != null) return Integer.MAX_VALUE;
        return members.size();
    }

    /**
     * Checks whether the group has members.
     * @return true if the group has members.
     */
    public boolean isAlive() {
        return members.size() != 0;
    }
}
