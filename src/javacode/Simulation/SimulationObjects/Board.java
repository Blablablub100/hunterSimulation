package javacode.Simulation.SimulationObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

    private int width;
    private int height;
    private List<Hunter> hunters = new ArrayList<>();
    private List<Prey> preys = new ArrayList<>();
    private List<Obstacle> obstacles = new ArrayList<>();

    public Board(int width, int height, int hunterCount, int preyCount, int obstacleCount) {
        this.width = width;
        this.height = height;
        for (int i = 0; i < hunterCount; i++) {
            spawnHunter();
        }
        for (int i = 0; i < preyCount; i++) {
            spawnPrey();
        }
        for (int i = 0; i < obstacleCount; i++) {
            spawnObstacle();
        }
        // TODO ÜBERLAGERUNG BEACHTEN
    }

    public List<Hunter> getHunters() {
        return hunters;
    }

    public List<Prey> getPreys() {
        return preys;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    protected void spawnHunter() {
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
