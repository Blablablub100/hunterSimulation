package javacode.Simulation.SimulationObjects;

import java.util.List;
import java.util.Random;

public class Board {

    private int width;
    private int height;
    private List<Hunter> hunters;
    private List<Prey> preys;
    private List<Obstacle> obstacles;

    public Board(int width, int height, int hunterCount, int PreyCount, int obstacleCount) {
        this.width = width;
        this.height = height;
    }

    public void spawnHunter() {
        hunters.add(new Hunter(generateRandomLoc()));
    }

    public void spawnPrey() {
        preys.add(new Prey(generateRandomLoc()));
    }

    public void spawnObstacle() {
        obstacles.add(new Obstacle(generateRandomLoc()));
    }

    public void spawnHunter(BoardObject.Location loc) {
        hunters.add(new Hunter(loc));
    }

    public void spawnPrey(BoardObject.Location loc) {
        preys.add(new Prey(loc));
    }

    public void spawnObstacle(BoardObject.Location loc) {
        obstacles.add(new Obstacle(loc));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private BoardObject.Location generateRandomLoc() {
        int x = (int)(Math.random() * width);
        int y = (int)(Math.random() * height);
        return new BoardObject.Location(x, y);
    }
}
