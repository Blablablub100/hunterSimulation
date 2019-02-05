package Simulation.AI;

import Simulation.SimulationObjects.BoardObject;
import Simulation.SimulationObjects.Hunter;
import Simulation.SimulationObjects.LivingCreature;
import Simulation.SimulationObjects.Prey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class provides the AI for any hunter object.
 */
public class HunterAI extends AI {

    /**
     * This list provides the short term memory for the hunter. It gets renewed every iteration.
     */
    private List<HunterMemory> shortTermMemory;
    /**
     * This attribute saves the current group. Null if the Hunter is not a group member.
     */
    private GroupAI groupIntelligence = null;

    /**
     * The only constructor for creating a new HunterAI.
     * @param body the body this AI is going to control.
     */
    public HunterAI(LivingCreature body) {
        super(body);
        longTermMemory = new Memory[3];
        status.setStatus(1);
    }


    /**
     * Reacts to the current situation based on prios:
     * What to do:<br>
     * 6. Prio: Group member<br>
     *  -> do what the group tells me to do.<br>
     * 5. Prio: Am I near  a Prey that would kill me<br>
     *  -> flee, walk on the other side of large preys<br>
     * 4. Prio: Am I near a Prey that I could kill<br>
     *  -> hunt, walk in the direction of the small prey<br>
     * 3. Prio: Does somebody want to group with me<br>
     *  -> group with <br>
     * 2. Prio: Big Prey nearby<br>
     *  -> search for group<br>
     * 1. Prio: Nothing to do<br>
     *  -> Move randomly around<br>
     */
    @Override
    public void react() {
        notifyNextRound();
        List<BoardObject> thingsSeen = body.see();
        shortTermMemory = getShortTermMemory(thingsSeen);
        filterToLongTermMemory();
        if (isGroupmember()) {
            groupIntelligence.steerGroup(this);
            return;
        }

        status.setStatus("calm");

        // check if I have to flee
        List<LivingCreature> threats = identifyThreats();
        for (LivingCreature threat: threats) {
            if (threat.getLocation().getDistance(body.getLocation()) < 3) {
                status.setStatus("fleeing");
                flee(threats);
            }
        }

        // check if I am able to hunt
        Prey toHunt = identifyPreyToHunt(threats);
        if (toHunt != null) {
            hunt(toHunt);
            status.setStatus("hunting");
        }

        // check if there is  a big Prey I can groupHunt
        Prey biggest = getBiggestPrey();
        if (biggest != null && !isGroupmember()) {
            groupIntelligence = new GroupAI(this, biggest);
            searchForGroupMembers();
            status.setStatus("searching group members");
        } else if (toHunt == null && !status.getStatus().equals("fleeing")) {
            // nothing to do
            moveRandomly();
        }
    }


    /**
     * Creates the hunters short term memory out of a list of seen things.
     * @param thingsSeen BoardObjects the hunter has seen.
     * @return short term memory.
     */
    private List<HunterMemory> getShortTermMemory(List<BoardObject> thingsSeen) {

        List<HunterMemory> shortTermMemory = new ArrayList<>();
        for (BoardObject thing: thingsSeen) {
            shortTermMemory.add(new HunterMemory(thing));
        }
        Collections.sort(shortTermMemory);
        return shortTermMemory;
    }

    /**
     * filters the short term memory and saves the important stuff to long term memory.<br>
     * highest priority memory: Prey smaller me<br>
     * second highest priority memory: Hunter near me<br>
     * third highest priority memory: Prey bigger then me<br>
     * gets filled, first with highest priority memories, then second and then third<br>
     */
    private void filterToLongTermMemory() {

        for (int i = 0; (i<shortTermMemory.size()) && (i<longTermMemory.length); i++) {
            HunterMemory shortTerm = shortTermMemory.get(i);

            for (Memory longTerm: longTermMemory) {
                if((longTerm == null)
                        || (shortTerm.getPriority() > ((HunterMemory) longTerm).getPriority())
                        || longTerm.getThingMemorized() == shortTerm.getThingMemorized()) {
                    // check if longTermMemory already contains it
                    for (int j = 0; j < longTermMemory.length; j++) {
                        if (longTermMemory[j] == shortTerm) break;
                    }
                    longTermMemory[i] = shortTerm;
                    break;
                }
            }
        }
    }

    /**
     * Identifies which prey out of possible preys to hunter out of these criteria:<br>
     * 1. Prio: no Threat nearby<br>
     * 2. Prio: nearest Prey<br>
     * 3. Prio: biggest Prey<br>
     * @param threats List of threats that are dangerous to the hunter.
     * @return best possible prey to hunt.
     */
    private Prey identifyPreyToHunt(List<LivingCreature> threats) {
        List<Prey> huntablePrey = identifyHuntablePrey();
        Prey toHunt = null;
        for (Prey curr : huntablePrey) {
            // no huntable is set
            if (toHunt == null) {
                if (isHuntable(curr, threats)) toHunt = curr;
            }
            // if current Prey is nearer then toHunt
            else if (curr.getLocation().getDistance(body.getLocation())
                    < toHunt.getLocation().getDistance(body.getLocation())) {

                if (isHuntable(curr, threats)) toHunt = curr;
            }
            // if current Prey is larger then toHunt and not more far away
            else if (curr.getLocation().getDistance(body.getLocation())
                    == toHunt.getLocation().getDistance(body.getLocation())
                    && curr.getStrength() > toHunt.getStrength()) {

                if (isHuntable(curr, threats)) toHunt = curr;
            }
        }
        return toHunt;
    }

    /**
     * Checks whether a prey is huntable based on threats nearby.
     * @param p prey that is going to be checked.
     * @param threats threats that are dangerous to the hunter.
     * @return true if p is huntable without danger.
     */
    private boolean isHuntable(Prey p, List<LivingCreature> threats) {
        BoardObject.Location myLoc = body.getLocation();
        BoardObject.Location pLoc = p.getLocation();
        for (LivingCreature threat: threats) {
            BoardObject.Location tLoc = threat.getLocation();
            int minClearance = 2;
            // is it between the two on the x axis
            if ( ((tLoc.getX() > myLoc.getX())  &&  (tLoc.getX() < pLoc.getX()+minClearance))
                    || ((tLoc.getX() < myLoc.getX()) && (tLoc.getX() > pLoc.getX()-minClearance)) ) {
                return false;
            }
            // is it between the  two on the y axis
            if ( ((tLoc.getY() > myLoc.getY())  &&  (tLoc.getY() < pLoc.getY()+minClearance))
                    || ((tLoc.getY() < myLoc.getY()) && (tLoc.getY() > pLoc.getY()-minClearance)) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Identifies Prey that are huntable.
     * @return all preys that could be hunted.
     */
    private List<Prey> identifyHuntablePrey() {
        List<Prey> huntablePrey = new ArrayList<>();
        for (Memory longTerm: longTermMemory) {
            if (longTerm != null && ((HunterMemory) longTerm).getPriority() == 4)
                huntablePrey.add((Prey) longTerm.getThingMemorized());
        }
        for (HunterMemory shortTerm : shortTermMemory) {
            if (shortTerm.getPriority() == 4)
                huntablePrey.add((Prey) shortTerm.getThingMemorized());
        }
        return huntablePrey;
    }

    /**
     * Identifies everything seen that is dangerous for the hunter.
     * @return list of threats.
     */
    private List<LivingCreature> identifyThreats() {
        List<LivingCreature> threats = new ArrayList<>();
        for (Memory longTerm: longTermMemory) {
            if (longTerm != null && ((HunterMemory) longTerm).getPriority() == 5)
                threats.add((LivingCreature) longTerm.getThingMemorized());
        }
        for (HunterMemory shortTerm : shortTermMemory) {
            if (shortTerm.getPriority() == 5)
                threats.add((LivingCreature) shortTerm.getThingMemorized());
        }
        return threats;
    }

    /**
     * Gets biggest prey that is either seen or saved in long term memory.
     * @return biggest known prey.
     */
    private Prey getBiggestPrey() {
        Prey temp1 = getBiggest(longTermMemory);
        Prey temp2 = getBiggest(shortTermMemory.toArray(new Memory[0]));
        if (temp1 == null) return temp2;
        if (temp2 == null) return temp1;
        if (temp1.getStrength() > temp2.getStrength()) return temp1;
        if (temp2.getStrength() > temp1.getStrength()) return temp2;
        return temp1;
    }

    /**
     * Gets biggest prey out of an array of memories.
     * @param memories memories that are going to be checked.
     * @return biggest prey inside the memory array.
     */
    private Prey getBiggest(Memory[] memories) {
        Prey biggest = null;
        for (Memory memory: memories) {
            if (memory != null && memory.getThingMemorized() instanceof Prey) {
                Prey curr = (Prey)memory.getThingMemorized();
                if (biggest == null
                        || curr.getStrength() > biggest.getStrength()) {
                    biggest = curr;
                }
            }
        }
        return biggest;
    }

    /**
     * walks around and invites other hunters to the group. If the AI still remembers other hunter either from long or
     * short term memory. The hunter will go to the biggest possible hunter to invite to the group.
     */
    void searchForGroupMembers() {
        Hunter strongest = null;
        for (Memory shortTerm: shortTermMemory) {
            if (shortTerm.getThingMemorized() instanceof Hunter) {
                Hunter curr = (Hunter)(shortTerm.getThingMemorized());
                inviteToGroup(curr);
                if (strongest == null
                        || strongest.getStrength() < curr.getStrength()) {
                    strongest = curr;
                }
            }
        }
        for (Memory longTerm: longTermMemory) {
            if (longTerm != null && (longTerm.getThingMemorized() instanceof Hunter)) {
                Hunter curr = (Hunter)longTerm.getThingMemorized();
                if (strongest == null
                        || strongest.getStrength() < curr.getStrength()) {
                    strongest = curr;
                }
            }
        }
        if (strongest != null) {
            body.move(strongest.getLocation());
        }
        moveRandomly();
    }

    /**
     * Invites a new Hunter to the group.
     * @param h hunter that is going to be invited.
     * @return true if invitation is accepted.
     */
    private boolean inviteToGroup(Hunter h) {
        int groupRadius = 3;
        if (body.getLocation().getDistance(h.getLocation()) < groupRadius) {
            if (groupIntelligence == null) return false;
            return h.receiveGroupInvitation(groupIntelligence);
        }
        return false;
    }

    /**
     * hunts a specific prey.
     * @param p prey that is going to be hunted.
     */
    private void hunt(Prey p) {
        status.setStatus("hunting");
        body.move(p.getLocation());
        if (body.getLocation().getDistance(p.getLocation()) == 1 && body.getPossibleSteps() >= 1.0) {
            body.attack(p);
        } else if (body.getLocation().getDistance(p.getLocation()) != 1) {
            moveRandomly();
        }
    }

    /**
     * moves around randomly.
     */
    void moveRandomly() {
        int direction = ThreadLocalRandom.current().nextInt(1, 4 + 1);
        switch (direction) {
            case 1:
                body.moveNorth();
                break;
            case 2:
                body.moveSouth();
                break;
            case 3:
                body.moveWest();
                break;
            case 4:
                body.moveEast();
                break;
        }
    }

    /**
     * return current status.
     * @return current status object.
     */
    Status getStatus() {
        return status;
    }

    /**
     * joins a group.
     * @param group group that this hunter is going to join-
     * @return true if success.
     */
    public boolean joinGroup(GroupAI group) {
        if (group == groupIntelligence) return true;
        if (isGroupmember() && group.getGroupSize() < groupIntelligence.getGroupSize()) return false;
        else if (isGroupmember()) leaveGroup();
        this.groupIntelligence = group;
        groupIntelligence.add(this);
        return true;
    }

    /**
     * leaves the current group.
     */
    public void leaveGroup() {
        if (groupIntelligence == null) return;
        groupIntelligence.remove(this);
        groupIntelligence = null;
    }

    /**
     * returns whether this hunter is group member.
     * @return true if the hunter is a group member.
     */
    public boolean isGroupmember() {
        if (groupIntelligence != null && groupIntelligence.getGroupSize() == 1) return false;
        return (groupIntelligence != null);
    }

    /**
     * Gets current group.
     * @return current group object.
     */
    public GroupAI getGroup() {
        return groupIntelligence;
    }

    /**
     * Gets the group strength of the current group.
     * @return -1 if hunter is no group member.
     */
    public int getGroupStrength() {
        if (groupIntelligence == null) return -1;
        return groupIntelligence.getGroupStrength();
    }

    /**
     * Gets the body the AI is controlling.
     * @return Hunter object.
     */
    Hunter getBody() {
        return (Hunter) body;
    }

    /**
     * A special kind of memory that saves a priority with the memory. It extends on the normal AI memory.
     */
    public class HunterMemory extends AI.Memory implements Comparable {
        /**
         * Priority of the memory
         */
        private int prio;

        /**
         * The constructor to create a new hunter memory. The priority gets calculated automatically.
         * @param thing BoardObject that is going to be saved in the memory.
         */
        HunterMemory(BoardObject thing) {
            super(thing);
            prio = calcPriority();
        }

        /**
         * Gets the priority of the memory.
         * @return priority value of the memory.
         */
        int getPriority() {
            return prio;
        }

        /**
         * Calculates the priority of the memory.
         * @return int value that represents the priority of the memory.
         */
        private int calcPriority() {
            BoardObject thing = getThingMemorized();
            int prio = 0;

            if (thing instanceof Prey && ((Prey)thing).getStrength() >= body.getStrength()) {
                prio = 5;
            }
            else if (thing instanceof Prey && ((Prey)thing).getStrength() < body.getStrength()) {
                prio = 4;
            }
            else if (thing instanceof Hunter && ((Hunter)thing).getStrength() > body.getStrength()) {
                prio = 2;
            }
            else if (thing instanceof Hunter) {
                prio = 2;
            }
            return prio;
        }

        /**
         * Removes the memory out of long term memory.
         */
        void remove() {
            for (int i = 0; i < longTermMemory.length; i++) {
                if (longTermMemory[i] == this) longTermMemory[i] = null;
            }
        }

        /**
         * Overrides notifyNewRound so that the memory gets deleted once it is too old.
         */
        @Override
        public void notifyNewRound() {
            super.notifyNewRound();
            if (getTime() > 5) {
                this.remove();
            }
        }

        /**
         * Compares this memory two another memory. The one with the higher priority will be taller.
         * @param o object to compare to.
         * @return based on priority difference.
         */
        @Override
        public int compareTo(Object o) {
            if (!(o instanceof HunterMemory)) return 0;
            int otherPrio = ((HunterMemory) o).getPriority();
            if (prio > otherPrio) return -1;
            else if (prio < otherPrio) return 1;
            return 0;
        }
    }
}
