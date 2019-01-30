package Simulation.SimulationObjects;

import Simulation.SimulationController;
import Simulation.Statistics;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private int width;
    private int height;
    private List<Hunter> hunters = new ArrayList<>();
    private List<Prey> preys = new ArrayList<>();
    private List<BoardObject> boardObjects = new ArrayList<>();
    private SimulationController sim;

    public Board(int width, int height, int hunterCount, int preyCount, int obstacleCount, SimulationController sim) {
        this.sim = sim;
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

    // spawn with attributes
    public void spawnHunter(int speed, int strength, int sight, int energy) {
        hunters.add(new Hunter(generateRandomLoc(), this, speed, strength, sight, energy));
    }

    public void spawnPrey(int speed, int strength, int sight, int energy) {
        preys.add(new Prey(generateRandomLoc(), this, speed, strength, sight, energy));
    }

    public void spawnObstacle(BoardObject.Location loc) {
        boardObjects.add(new Obstacle(loc));
    }

    public void spawn(BoardObject boardObject) {
        if (boardObject instanceof Hunter) {
            hunters.add((Hunter) boardObject);
        } else if (boardObject instanceof Prey) {
            preys.add((Prey) boardObject);
        } else {
            boardObjects.add(boardObject);
        }
    }


    // spawn at random positions
    private void spawnHunter() {
        hunters.add(new Hunter(generateRandomLoc(), this));
    }

    private void spawnPrey() {
        preys.add(new Prey(generateRandomLoc(), this));
    }

    private void spawnObstacle() {
        boardObjects.add(new Obstacle(generateRandomLoc()));
    }


    // remove Object
    public void removeFromBoard(BoardObject b) {
        if (b instanceof Hunter) {
            removeHunter((Hunter) b);
        } else if (b instanceof Prey) {
            removePrey((Prey) b);
        } else {
            removeObstacle((BoardObject) b);
        }
    }

    void removeHunter(Hunter h) {
        hunters.remove(h);
    }

    void removePrey(Prey p) {
        preys.remove(p);
    }

    void removeObstacle(BoardObject b) {
        boardObjects.remove(b);
    }


    // getters
    public Statistics getStats() {
        return sim.getStats();
    }

    public List<Hunter> getHunters() {
        return hunters;
    }

    public List<Prey> getPreys() {
        return preys;
    }

    public List<BoardObject> getNonLivingBoardObjects() {
        return boardObjects;
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
        res.addAll((List<BoardObject>)(List<?>) boardObjects);
        return res;
    }

    public List<LivingCreature> getLivingCreatues() {
        List<LivingCreature> res = new ArrayList<>();
        res.addAll((List<LivingCreature>)(List<?>) hunters);
        res.addAll((List<LivingCreature>)(List<?>) preys);
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
        BoardObject.Location res;
        do {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            res = new BoardObject.Location(x, y);
        } while (!isEmpty(res));
        return res;
    }

    public boolean isEmpty(BoardObject.Location loc) {
        if (!isOnBoard(loc)) return false;
        List<BoardObject> temp = getBoardObjects();
        for (BoardObject curr: temp) {
            if (curr.getLocation().equals(loc)) return false;
        }
        return true;
    }

    public boolean isOnBoard(BoardObject.Location loc) {
        if (loc == null) return false;
        int x = loc.getX();
        int y = loc.getY();
        return ((x>=0 && x<width)  &&  (y>=0 && y<height));
    }

}