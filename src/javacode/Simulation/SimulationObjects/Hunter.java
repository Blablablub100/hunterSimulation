package javacode.Simulation.SimulationObjects;

import java.util.ArrayList;
import java.util.List;

public class Hunter extends LivingCreature {

    Hunter(Location loc, Board myBoard) {
        this.loc = loc;
        this.myBoard = myBoard;
        // TODO randomize rest
    }


    @Override
    public void react() {
        List<BoardObject> tmp = see();
        System.out.println("A");
    }


    @Override
    List<BoardObject> see() {
        sightDistance = 5;
        // things that the hunter is able to see
        List<BoardObject> thingsSeen = new ArrayList<>();

        int viewWidth = 1;
        for (int i = 1; i < sightDistance; i++) {
            int currY = loc.getY() + i;

            for (int j = viewWidth*(-1); j < viewWidth; j++) {
                int currX = loc.getX() + j;
                BoardObject tmp =
                        myBoard.getObjectAtLocation(new BoardObject.Location(currX, currY));
                if (tmp != null) thingsSeen.add(tmp);
            }

            viewWidth++;
        }
        return thingsSeen;
    }

    @Override
    void eat() {
        BoardObject tmp = myBoard.getObjectAtLocation(getLocation());
        if (!(tmp instanceof Prey)) return;
        Prey toEat = (Prey) tmp;
        attack(toEat);

    }

    @Override
    void attack(LivingCreature opponent) {
        if (!getLocation().equals(opponent.getLocation())) return;
        if (opponent.getStrength() < getStrength()) return;
        if (!(opponent instanceof Prey)) return;
        opponent.die();
        eat();
    }
}
