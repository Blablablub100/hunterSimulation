package Simulation.AI;

import Simulation.SimulationObjects.BoardObject;
import Simulation.SimulationObjects.Hunter;
import Simulation.SimulationObjects.LivingCreature;

import java.util.*;

/**
 * This class provides the AI for any prey object.
 */
public class PreyAI extends AI {

    /**
     * The only constructor for creating a new PreyAI.
     * @param body the body this AI is going to control.
     */
    public PreyAI(LivingCreature body) {
        super(body);
        longTermMemory = new Memory[3];
        status.setStatus(1);
    }

    /**
     * Reacts to the current situation:<br>
     * 1. see<br>
     * 2. filter memory (Hunter bigger me -> longterm)<br>
     * 3. if hunter bigger me nearby -> flee<br>
     * 4. else if hunter smaller nearer 3 -> attack<br>
     * 5. else grass
     */
    @Override
    public void react() {
        notifyNextRound();
        List<BoardObject> thingsSeen = body.see();
        filterToMemory(thingsSeen);
        List<LivingCreature> threats = getNearbyThreats(thingsSeen);
        Hunter weakHunter = getNearestWeakHunter(thingsSeen);

        if (hasToFlee(threats)) {
            flee(threats);
            status.setStatus("fleeing");
        }
        else if (weakHunter != null){
            attackWeakHunter(weakHunter);
            status.setStatus("attacking");
        }
        else {
            grass();
            status.setStatus("grazing");
        }

    }

    /**
     * Attacks a specified hunter.
     * @param h Hunter that is going to be attacked.
     */
    private void attackWeakHunter(Hunter h) {
        body.move(h.getLocation());
        body.attack(h);
    }

    /**
     * Gets nearest hunter that is weaker then the prey this AI is controlling.
     * @param thingsSeen List of BoardObjects the prey is able to see.
     * @return nearest hunter weaker then the prey.
     */
    private Hunter getNearestWeakHunter(List<BoardObject> thingsSeen) {
        Hunter h = null;
        int hDis = Integer.MAX_VALUE;
        for (BoardObject thing: thingsSeen) {
            int tmpDis = thing.getLocation().getDistance(body.getLocation());
            if (thing instanceof Hunter
                    && ((Hunter)thing).getStrength() < body.getStrength()
                    && tmpDis < 3
                    && (h == null || tmpDis < hDis)) {
                h = (Hunter) thing;
                hDis = tmpDis;
            }
        }
        return h;
    }

    /**
     * Eats grass, one iteration of grass eating will get the prey 2 energy.
     */
    private void grass() {
        body.eat(2);
    }

    /**
     * Checks whether the prey has to flee from threats.
     * @param threats identified threats.
     * @return true if prey has to flee.
     */
    private boolean hasToFlee(List<LivingCreature> threats) {
        for (LivingCreature threat: threats) {
            if (threat.getLocation().getDistance(body.getLocation()) < 5) return true;
        }
        return false;
    }

    /**
     * Identifies all nearby thread out of the seen things.
     * @param thingsSeen things the prey is able to see.
     * @return List of every thread that is seen.
     */
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

    /**
     * checks if a board object is a thread.
     * @param thing Boardobject to check.
     * @return true if thing is dangerous to the prey.
     */
    private boolean isThread(BoardObject thing) {
        return (thing instanceof Hunter
                && ((Hunter) thing).getStrength() > body.getStrength());
    }

    /**
     * Filters the important things seen by the prey to the long term memory.
     * @param thingsSeen everything the prey has seen this iteration.
     */
    private void filterToMemory(List<BoardObject> thingsSeen) {
        for (BoardObject thing: thingsSeen) {
            if (thing instanceof Hunter
                    && ((Hunter) thing).getStrength() > body.getStrength()) {
                saveToMemory((Hunter) thing);
            }
        }
    }

    /**
     * Saves a hunter to long term memory.
     * @param h hunter that is going to be memorized.
     */
    private void saveToMemory(Hunter h) {
        for (int i = 0; i < longTermMemory.length; i++) {
            if (longTermMemory[i] != null && longTermMemory[i].getTime() > 10) longTermMemory[i] = null;
            if (longTermMemory[i] == null);
            else if (longTermMemory[i].getThingMemorized() == h) return;
        }

        for (int i = 0; i < longTermMemory.length; i++) {
            Memory mem = longTermMemory[i];
            if (mem == null) {
                longTermMemory[i] = new Memory(h);
                return;
            }

            int currDist = mem.getThingMemorized().getLocation().getDistance(body.getLocation());
            int newDist = h.getLocation().getDistance(body.getLocation());

            if (newDist < currDist || mem.getTime() > 10) {
                longTermMemory[i] = new Memory(h);
                return;
            }
        }
    }
}
