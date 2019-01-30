package Simulation.AI;

import Simulation.SimulationObjects.BoardObject;
import Simulation.SimulationObjects.DeadCorpse;
import Simulation.SimulationObjects.Hunter;
import Simulation.SimulationObjects.Prey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GroupAI {

    private List<HunterAI> members;
    private Prey target;
    private HunterAI leader;
    private int time;
    private boolean hunting = false;
    private HashMap<HunterAI, BoardObject.Location> goals;
    private DeadCorpse corpse;

    /*
    1. check if already called this step
    2. if not calculate Locations and let everybody move towards it
    3. When target is reached cadaver is spawned
    4. Every hunter eats a fair share
     */

    GroupAI(HunterAI leader, Prey target) {
        this.leader = leader;
        this.target = target;
        this.time = 0;
        members = new ArrayList<>();
        members.add(leader);
    }

    void steerGroup(HunterAI h) {
        if (time > 1000) {
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


    private void steer(HunterAI h) {
        if (corpse != null) {
            h.getBody().move(corpse.getLocation());
            h.getStatus().setStatus("reaching carrion");
            if (h.getBody().getLocation().getDistance(corpse.getLocation()) == 1) {
                h.getBody().eat(corpse.eat());
                h.leaveGroup();
                h.getStatus().setStatus("calm");
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
                time = time - 5;
                h.getBody().getBoard().spawn(tmp);
                calcGoals();
                corpse = tmp;
            }
        } else {
            h.getStatus().setStatus("searching group members");
            h.searchForGroupMembers();
        }
    }

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
    private boolean isMemberNearMe(HunterAI h) {
        return (isMemberNeaby(h.getBody().getLocation()));
    }
    private boolean isMemberNeaby(BoardObject.Location startLoc) {
        for (HunterAI member: members) {
            if (member.getBody().getLocation().getDistance(startLoc) == 1) return true;
        }
        return false;
    }


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

    private BoardObject.Location getNearestLocByTarget(HunterAI h, BoardObject.Location target) {
        BoardObject.Location[] possibilities = new BoardObject.Location[4];
        BoardObject.Location targetC = (BoardObject.Location) target.clone();
        possibilities[0] = (BoardObject.Location) targetC.moveNorth().clone();
        possibilities[1] = (BoardObject.Location) targetC.moveEast().moveSouth().clone();
        possibilities[2] = (BoardObject.Location) targetC.moveSouth().moveWest().clone();
        possibilities[3] = (BoardObject.Location) targetC.moveWest().moveNorth().clone();
        return getNearestLocation(h, possibilities);
    }

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

    private int getDistance(HunterAI h, BoardObject.Location loc) {
        return h.getBody().getLocation().getDistance(loc);
    }

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


    void add(HunterAI h) {
        members.add(h);
        h.joinGroup(this);
    }

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

    int getGroupStrength() {
        int groupStrength = 0;
        for (HunterAI member : members) {
            int memberStrength = ((Hunter) member.owner).getIndividualStrength();
            groupStrength = groupStrength + memberStrength;
        }
        return groupStrength;
    }

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

    public int getGroupSize() {
        if (corpse != null) return Integer.MAX_VALUE;
        return members.size();
    }
}
