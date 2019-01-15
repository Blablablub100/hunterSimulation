package javacode.Simulation.AI;

import javacode.Simulation.SimulationObjects.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HunterAI extends AI {

    private List<HunterMemory> shortTermMemory;

    public HunterAI(LivingCreature owner) {
        super(owner);
        longTermMemory = new Memory[3];
        status.setStatus(1);
    }


    @Override
    public void react() {
        notifyNextRound();
        List<BoardObject> thingsSeen = owner.see();
        shortTermMemory = getShortTermMemory(thingsSeen);
        filterToLongTermMemory();

        // first 4 finished -> MemoryManagement done

        // What to do:
        // 5. Prio: Am I near  a Prey that would kill me
        //     -> flee, walk on the other side of large preys
        // 4. Prio: Am I near a Prey that I could kill
        //     -> hunt, walk in the direction of the small prey
        // 3. Prio: Does somebody want to group with me
        //     -> group with him, let
        // 2. Prio: Big Prey nearby
        //     -> search for group
        // 1. Prop: Nothing to do
        //     -> Turn around 90 degree and walk


        // check if I have to flee
        List<LivingCreature> threats = identifyThreats();
        for (LivingCreature threat: threats) {
            if (threat.getLocation().getDistance(owner.getLocation()) < 3) {
                status.setStatus("fleeing");
                flee(threats);
            }
        }

        // check if I am able to hunt
        Prey toHunt = identifyPreyToHunt(threats);
        if (toHunt != null) hunt(toHunt);

        // group logic going here



        // nothing to do
        moveRandomly();
    }



    private List<HunterMemory> getShortTermMemory(List<BoardObject> thingsSeen) {

        List<HunterMemory> shortTermMemory = new ArrayList<>();
        for (BoardObject thing: thingsSeen) {
            shortTermMemory.add(new HunterMemory(thing));
        }
        Collections.sort(shortTermMemory);
        return shortTermMemory;
    }


    private void filterToLongTermMemory() {
        // highest priority memory: Prey smaller me
        // second highest priority memory: Hunter near me
        // third highest priority memory: Prey bigger then me
        //   gets filled, first with highest priority memories, then second and then third
        for (int i = 0; (i<shortTermMemory.size()) && (i<longTermMemory.length); i++) {
            HunterMemory shortTerm = shortTermMemory.get(i);

            for (Memory longTerm: longTermMemory) {
                if((longTerm == null)
                        || (shortTerm.getPriority() > ((HunterMemory) longTerm).getPriority())
                        || longTerm.getThingMemorized() == shortTerm.getThingMemorized()) {
                    longTermMemory[i] = shortTerm;
                    break;
                }
            }
        }
    }


    private Prey identifyPreyToHunt(List<LivingCreature> threats) {
        // prioritize what to hunt:
        // 1. Prio: no Threat nearby
        // 2. Prio: nearest Prey
        // 3. Prio: biggest Prey
        List<Prey> huntablePrey = identifyHuntablePrey();
        Prey toHunt = null;
        for (int i = 0; i < huntablePrey.size(); i++) {
            Prey curr = huntablePrey.get(i);

            // no huntable is set
            if (toHunt == null) {
                if (isHuntable(curr, threats)) toHunt = curr;
            }
            // if current Prey is nearer then toHunt
            else if (curr.getLocation().getDistance(owner.getLocation())
                    < toHunt.getLocation().getDistance(owner.getLocation())) {

                if (isHuntable(curr, threats)) toHunt = curr;
            }
            // if current Prey is larger then toHunt and not more far away
            else if (curr.getLocation().getDistance(owner.getLocation())
                    == toHunt.getLocation().getDistance(owner.getLocation())
                    && curr.getStrength() > toHunt.getStrength()) {

                if (isHuntable(curr, threats)) toHunt = curr;
            }
        }
        return toHunt;
    }


    private boolean isHuntable(Prey p, List<LivingCreature> threats) {
        BoardObject.Location myLoc = owner.getLocation();
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


    private List<Prey> identifyHuntablePrey() {
        List<Prey> huntablePrey = new ArrayList<Prey>();
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


    private List<LivingCreature> identifyThreats() {
        List<LivingCreature> threats = new ArrayList<LivingCreature>();
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


    private void hunt(Prey p) {
        status.setStatus("hunting");
        owner.move(p.getLocation());
        if (owner.getLocation().getDistance(p.getLocation()) == 1 && owner.getPossibleSteps() >= 1.0) {
            owner.attack(p);
        }
    }


    private void moveRandomly() {
        int direction = owner.getRandom(1, 4);
        switch (direction) {
            case 1:
                owner.moveNorth();
                break;
            case 2:
                owner.moveSouth();
                break;
            case 3:
                owner.moveWest();
                break;
            case 4:
                owner.moveEast();
                break;
        }
    }


    public class HunterMemory extends AI.Memory implements Comparable {
        private int prio;

        public HunterMemory(BoardObject thing) {
            super(thing);
            prio = calcPriority();
        }

        public int getPriority() {
            return prio;
        }

        private int calcPriority() {
            BoardObject thing = getThingMemorized();
            int prio = 0;

            if (thing instanceof Prey && ((Prey)thing).getStrength() >= owner.getStrength()) {
                prio = 5;
            }
            else if (thing instanceof Prey && ((Prey)thing).getStrength() < owner.getStrength()) {
                prio = 4;
            }
            else if (false/*somebody wants to group with me*/) {
                prio = 3;
            }
            else if (thing instanceof Hunter && ((Hunter)thing).getStrength() > owner.getStrength()) {
                prio = 2;
            }
            else if (thing instanceof Hunter) {
                prio = 2;
            }
            return prio;
        }

        public void remove() {
            for (int i = 0; i < longTermMemory.length; i++) {
                if (longTermMemory[i] == this) longTermMemory[i] = null;
            }
        }

        @Override
        public void notifyNewRound() {
            super.notifyNewRound();
            if (getTime() > 5) {
                this.remove();
            }
        }

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
