package javacode.Simulation.SimulationObjects;

import java.util.List;

public class Board {

    private int width;
    private int height;
    private List<Hunter> hunters;
    private List<Prey> preys;
    private List<Obstacle> obstacles;

    public void spawnHunter(BoardObject.Location loc) {
        hunters.add(new Hunter(loc));
    }

    public void spawnPrey() {

    }

    public void spawnObstacle() {

    }
}
