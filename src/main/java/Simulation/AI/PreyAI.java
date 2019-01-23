package Simulation.AI;

import Simulation.SimulationObjects.LivingCreature;

public class PreyAI extends AI {

    //public List<PreyMemory> shortTermMemory;

    public PreyAI(LivingCreature owner) {
        super(owner);
        longTermMemory = new Memory[3];
        status.setStatus(1);
    }


    @Override
    public void react() {
        //notifyNextRound();
        //List<BoardObject> thingsSeen = owner.see();
        //shortTermMemory = getShortTermMemory(thingsSeen);
        //filterToLongTermMemory();
    }

/*

    private List<PreyAI.PreyMemory> getShortTermMemory(List<BoardObject> thingsSeen) {

        List<PreyAI.PreyMemory> shortTermMemory = new ArrayList<>();
        for (BoardObject thing: thingsSeen) {
            shortTermMemory.add(new PreyAI.PreyMemory(thing));
        }
        Collections.sort(shortTermMemory);
        return shortTermMemory;
    }


    private void filterToLongTermMemory() {
        // highest priority memory: Hunter bigger than me
        // second highest priority memory: Hunter smaller than me
        // third highest priority memory: Hunter with same strenght
        //   gets filled, first with highest priority memories, then second and then third
        for (int i = 0; (i<shortTermMemory.size()) && (i<longTermMemory.length); i++) {
            PreyAI.PreyMemory shortTerm = shortTermMemory.get(i);

            for (Memory longTerm: longTermMemory) {
                if((longTerm == null)
                        || (shortTerm.getPriority() > ((PreyAI.PreyMemory) longTerm).getPriority())
                        || longTerm.getThingMemorized() == shortTerm.getThingMemorized()) {
                    longTermMemory[i] = shortTerm;
                    break;
                }
            }
        }
    }


    private Hunter identifyHunterToHunt(List<LivingCreature> threats) {
        // prioritize what to hunt:
        // 1. Prio: no Threat nearby
        // 2. Prio: nearest Hunter
        // 3. Prio: biggest Hunter
        List<Hunter> huntableHunter = identifyHuntableHunter();
        Hunter toHunt = null;
        for (int i = 0; i < huntableHunter.size(); i++) {
            Hunter curr = huntableHunter.get(i);

            // no huntable is set
            if (toHunt == null && isHuntable(curr, threats)) {
                toHunt = curr;
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


    private boolean isHuntable(Hunter h, List<LivingCreature> threats) {
        BoardObject.Location myLoc = owner.getLocation();
        BoardObject.Location hLoc = h.getLocation();
        for (LivingCreature threat: threats) {
            BoardObject.Location tLoc = threat.getLocation();
            int minClearance = 2;
            // is it between the two on the x axis
            if ( ((tLoc.getX() > myLoc.getX())  &&  (tLoc.getX() < hLoc.getX()+minClearance))
                    || ((tLoc.getX() < myLoc.getX()) && (tLoc.getX() > hLoc.getX()-minClearance)) ) {
                return false;
            }
            // is it between the  two on the y axis
            if ( ((tLoc.getY() > myLoc.getY())  &&  (tLoc.getY() < hLoc.getY()+minClearance))
                    || ((tLoc.getY() < myLoc.getY()) && (tLoc.getY() > hLoc.getY()-minClearance)) ) {
                return false;
            }
        }
        return true;
    }


    private List<Hunter> identifyHuntableHunter() {
        List<Hunter> huntableHunter = new ArrayList<>();
        for (Memory longTerm: longTermMemory) {
            if (longTerm != null && ((PreyAI.PreyMemory) longTerm).getPriority() == 4)
                huntableHunter.add((Hunter) longTerm.getThingMemorized());
        }
        for (PreyAI.PreyMemory shortTerm : shortTermMemory) {
            if (shortTerm.getPriority() == 4)
                huntableHunter.add((Hunter)shortTerm.getThingMemorized());
        }
        return huntableHunter;
    }


    private List<LivingCreature> identifyThreats() {
        List<LivingCreature> threats = new ArrayList<>();
        for (Memory longTerm: longTermMemory) {
            if (longTerm != null && ((PreyAI.PreyMemory) longTerm).getPriority() == 5)
                threats.add((LivingCreature) longTerm.getThingMemorized());
        }
        for (PreyAI.PreyMemory shortTerm : shortTermMemory) {
            if (shortTerm.getPriority() == 5)
                threats.add((LivingCreature) shortTerm.getThingMemorized());
        }
        return threats;
    }


    private void hunt(Hunter h) {
        status.setStatus("hunting");
        owner.move(h.getLocation());
        if (owner.getLocation().getDistance(h.getLocation()) == 1 && owner.getPossibleSteps() >= 1.0) {
            owner.attack(h);
        }
    }

    public class PreyMemory extends AI.Memory implements Comparable {
        private int prio;

        public PreyMemory(BoardObject thing) {
            super(thing);
            prio = calcPriority();
        }

        public int getPriority() {
            return prio;
        }

        private int calcPriority() {
            BoardObject thing = getThingMemorized();
            int prio = 0;

            if (thing instanceof Hunter && ((Hunter)thing).getStrength() >= owner.getStrength()) {
                prio = 5;
            }
            else if (thing instanceof Hunter && ((((Hunter)thing).getStrength()) <= (owner.getStrength()))
                    && ((Hunter))thing.getLocation().getDistance(owner.getLocation()) <= 3) {
                prio = 4;
            }
            else if (thing instanceof Hunter && ((Hunter)thing).getStrength() ) {
                prio = 2;
            }
            else if (!(thing instanceof Hunter) && owner.getEnergy() <= 3) {
                prio = 1;
            }
            else (!(thing instanceof Hunter)) {
                prio = 0;
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
            if (!(o instanceof PreyAI.PreyMemory)) return 0;
            int otherPrio = ((PreyAI.PreyMemory) o).getPriority();
            if (prio > otherPrio) return -1;
            else if (prio < otherPrio) return 1;
            return 0;
        }
    }
    */
}
