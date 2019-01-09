package javacode.Simulation.AI;

import javacode.Simulation.SimulationObjects.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HunterAI extends AI {


    public HunterAI(LivingCreature owner) {
        super(owner);
        longTermMemory = new Memory[3];
        status.setStatus(1);
    }


    @Override
    public void react() {
        notifyNextRound();
        List<BoardObject> thingsSeen = owner.see();
        List<HunterMemory> shortTermMemory = getShortTermMemory(thingsSeen);
        filterToLongTermMemory(shortTermMemory);
        System.out.println("memory for hunter finished");
        // first 4 finished -> MemoryManagement done

        // What to do:
        // 1. Prio: Am I near a Prey that would kill me
        // 2. Prio: Am I near a Prey that I could kill
        // 3. Prio: Does somebody want to group with me
        // 4. Prio: Is there a big Prey and a Hunter nearby? Search for group
        // 5. Prio:
    }


    private List<HunterMemory> getShortTermMemory(List<BoardObject> thingsSeen) {

        List<HunterMemory> shortTermMemory = new ArrayList<>();
        for (BoardObject thing: thingsSeen) {
            shortTermMemory.add(new HunterMemory(thing));
        }
        Collections.sort(shortTermMemory);
        return shortTermMemory;
    }


    private void filterToLongTermMemory(List<HunterMemory> shortTermMemory) {
        // highest priority memory: Prey smaller me
        // second highest priority memory: Hunter near me
        // third highest priority memory: Prey bigger then me
        // memory gets filled, first with highest priority memories, then second and then third
        for (int i = 0; (i<shortTermMemory.size()) && (i<longTermMemory.length); i++) {
            HunterMemory shortTerm = shortTermMemory.get(i);

            for (Memory longTerm: longTermMemory) {
                if((longTerm == null) || (shortTerm.getPriority() > ((HunterMemory) longTerm).getPriority())) {
                    longTermMemory[i] = shortTerm;
                    break;
                }
            }
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
            else if (thing instanceof Hunter && ((Hunter)thing).getStrength() > owner.getStrength()) {
                prio = 3;
            }
            else if (thing instanceof Hunter) {
                prio = 2;
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
            if (prio > otherPrio) return -1;
            else if (prio < otherPrio) return 1;
            return 0;
        }
    }
}
