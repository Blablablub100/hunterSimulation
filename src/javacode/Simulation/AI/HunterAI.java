package javacode.Simulation.AI;

import javacode.Simulation.SimulationObjects.BoardObject;
import javacode.Simulation.SimulationObjects.LivingCreature;

import java.util.List;

public class HunterAI extends AI {

    public HunterAI(LivingCreature owner) {
        super(owner);

    }

    @Override
    public void react() {
        notifyNextRound();
    }
}
