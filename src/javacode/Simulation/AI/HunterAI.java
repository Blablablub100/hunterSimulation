package javacode.Simulation.AI;

import javacode.Simulation.SimulationObjects.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HunterAI extends AI {


    public HunterAI(LivingCreature owner) {
        super(owner);
        longTermMemory = new Memory[3];
    }


    @Override
    public void react() {
        notifyNextRound();
        List<BoardObject> thingsSeen = owner.see();
        List<HunterMemory> shortTermMemory = getShortTermMemory(thingsSeen);
        filterToLongTermMemory(shortTermMemory);
        // first 4 finished

    }


    private List<HunterMemory> getShortTermMemory(List<BoardObject> thingsSeen) {

        List<HunterMemory> shortTermMemory = new ArrayList<>();
        for (BoardObject thing: thingsSeen) {
            shortTermMemory.add(new HunterMemory(thing));
        }
        return shortTermMemory;
    }


    private void filterToLongTermMemory(List<HunterMemory> shortTermMemory) {
        // highest priority memory: Prey smaller me
        // second highest priority memory: Hunter near me
        // third highest priority memory: Prey bigger then me
        // memory gets filled, first with highest priority memories, then second and then third
        Collections.sort(shortTermMemory);
        for (HunterMemory shortTerm: shortTermMemory) {
            boolean run = true;
            for (int i = 0; i < longTermMemory.length; i++) {
                HunterMemory longTerm = (HunterMemory) longTermMemory[i];

                if(shortTerm.getPriority() > longTerm.getPriority()) {
                    longTermMemory[i] = shortTerm;
                    break;
                }
                if (i == longTermMemory.length-1) run = false;
            }
            if (!run) break;
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

            if (thing instanceof Prey && ((Prey)thing).getStrength() < owner.getStrength()) {
                prio = 1;
            }
            else if (thing instanceof Hunter) {
                prio = 2;
            }
            else if (thing instanceof Prey) {
                prio = 3;
            }
            return prio;
        }

        @Override
        public void notifyNewRound() {
            super.notifyNewRound();
            if (getTime() > 5) prio--;
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof HunterMemory)) return 0;
            int otherPrio = ((HunterMemory) o).getPriority();
            if (prio > otherPrio) return 1;
            else if (prio < otherPrio) return -1;
            return 0;
        }
    }
}
