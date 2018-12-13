package javacode.Simulation.SimulationObjects;

import java.util.ArrayList;
import java.util.List;

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
    }


    // spawn at specific position
    public void spawnHunter(BoardObject.Location loc) {
        hunters.add(new Hunter(loc));
    }

    public void spawnPrey(BoardObject.Location loc) {
        preys.add(new Prey(loc));
    }

    public void spawnObstacle(BoardObject.Location loc) {
        obstacles.add(new Obstacle(loc));
    }


    // spawn at random positions
    private void spawnHunter() {
        hunters.add(new Hunter(generateRandomLoc()));
    }

    private void spawnPrey() {
        preys.add(new Prey(generateRandomLoc()));
    }

    private void spawnObstacle() {
        obstacles.add(new Obstacle(generateRandomLoc()));
    }


    // remove Object
    void removeFromBoard(BoardObject b) {
        if (b instanceof Hunter) {
            removeHunter((Hunter) b);
        } else if (b instanceof Prey) {
            removePrey((Prey) b);
        } else if (b instanceof Obstacle) {
            removeObstacle((Obstacle) b);
        }
    }

    void removeHunter(Hunter h) {
        hunters.remove(h);
    }

    void removePrey(Prey p) {
        preys.remove(p);
    }

    void removeObstacle(Obstacle o) {
        obstacles.remove(o);
    }


    // getters
    public List<Hunter> getHunters() {
        return hunters;
    }

    public List<Prey> getPreys() {
        return preys;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<BoardObject> getBoardObjects() {
        List<BoardObject> res = new ArrayList<>();
        res.addAll((List<BoardObject>)(List<?>) hunters);
        res.addAll((List<BoardObject>)(List<?>) preys);
        res.addAll((List<BoardObject>)(List<?>) obstacles);
        return res;
    }

    // returns null if there is no object at certain location
    public BoardObject getObjectAtLocation(BoardObject.Location loc) {
        List<BoardObject> boardObjects = getBoardObjects();
        for (BoardObject curr: boardObjects) {
            if (curr.getLocation().equals(loc)) return curr;
        }
        return null;
    }


    // helping methods
    private BoardObject.Location generateRandomLoc() {
        BoardObject.Location res = null;
        do {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            res = new BoardObject.Location(x, y);
        } while (!isEmpty(res));
        return res;
    }

    public boolean isEmpty(BoardObject.Location loc) {
        List<BoardObject> temp = getBoardObjects();
        for (BoardObject curr: temp) {
            if (curr.getLocation().equals(loc)) return false;
        }
        return true;
    }

}
