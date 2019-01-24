package Simulation.AI;

import Simulation.SimulationObjects.Board;
import Simulation.SimulationObjects.BoardObject;
import Simulation.SimulationObjects.Hunter;
import Simulation.SimulationObjects.LivingCreature;

import java.util.*;

public class PreyAI extends AI {

    public PreyAI(LivingCreature owner) {
        super(owner);
        longTermMemory = new Memory[3];
        status.setStatus(1);
    }


    @Override
    public void react() {
        notifyNextRound();
        /*
        1. see
        2. filter memory (Hunter bigger me -> longterm)
        3. if hunter bigger me nearby -> flee
        4. else if hunter smaller nearer 3 -> attack
        5. else grass
        */
        List<BoardObject> thingsSeen = owner.see();
        filterToMemory(thingsSeen);
        List<LivingCreature> threats = getNearbyThreats(thingsSeen);
        Hunter weakHunter = getNearestWeakHunter(thingsSeen);

        if (hasToFlee(threats)) flee(threats);
        else if (weakHunter != null) attackWeakHunter(weakHunter);
        else grass();

    }

    private void attackWeakHunter(Hunter h) {
        owner.move(h.getLocation());
        owner.attack(h);
    }

    private Hunter getNearestWeakHunter(List<BoardObject> thingsSeen) {
        Hunter h = null;
        int hDis = Integer.MAX_VALUE;
        for (BoardObject thing: thingsSeen) {
            int tmpDis = thing.getLocation().getDistance(owner.getLocation());
            if (thing instanceof Hunter
                    && ((Hunter)thing).getStrength() < owner.getStrength()
                    && tmpDis < 3
                    && (h == null || tmpDis < hDis)) {
                h = (Hunter) thing;
                hDis = tmpDis;
            }
        }
        return h;
    }

    private void grass() {
        owner.eat(2);
    }

    private boolean hasToFlee(List<LivingCreature> threats) {
        for (LivingCreature threat: threats) {
            if (threat.getLocation().getDistance(owner.getLocation()) < 5) return true;
        }
        return false;
    }

    private List<LivingCreature> getNearbyThreats(List<BoardObject> thingsSeen) {
        Set<LivingCreature> threats = new HashSet<>();

        for (BoardObject thing: thingsSeen) {
            if (isThread(thing)) threats.add((LivingCreature) thing);
        }
        for (Memory mem: longTermMemory) {
            if (mem != null &&
                    isThread(mem.getThingMemorized())) threats.add((LivingCreature) mem.getThingMemorized());
        }
        return (new ArrayList<>(threats));
    }

    private boolean isThread(BoardObject thing) {
        return (thing instanceof Hunter
                && ((Hunter) thing).getStrength() > owner.getStrength());
    }

    private void filterToMemory(List<BoardObject> thingsSeen) {
        for (BoardObject thing: thingsSeen) {
            if (thing instanceof Hunter
                    && ((Hunter) thing).getStrength() > owner.getStrength()) {
                saveToMemory((Hunter) thing);
            }
        }
    }

    private void saveToMemory(Hunter h) {
        if (Arrays.asList(longTermMemory).contains(h)) return;

        for (int i = 0; i < longTermMemory.length; i++) {
            Memory mem = longTermMemory[i];
            if (mem == null) longTermMemory[i] = new Memory(h);

            int currDist = mem.getThingMemorized().getLocation().getDistance(owner.getLocation());
            int newDist = h.getLocation().getDistance(owner.getLocation());

            if (newDist < currDist || mem.getTime() > 20) {
                longTermMemory[i] = new Memory(h);
                return;
            }
        }
    }
}
