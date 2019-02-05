package Simulation.SimulationObjects;

import Simulation.SimulationController;
import Simulation.Statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the ability to manage every BoardObject of the simulation.
 * BoardObjects of any kind can be removed or added.
 */
public class Board {
    /**
     * Width of the simulation board
     */
    private int width;
    /**
     * Height of the simulation board
     */
    private int height;
    /**
     * List of all hunters that are on the board.
     */
    private List<Hunter> hunters = new ArrayList<>();
    /**
     * List of all preys that are on the board.
     */
    private List<Prey> preys = new ArrayList<>();
    /**
     * List of all non-living-creature-BoardObjects that are on the board.
     */
    private List<BoardObject> boardObjects = new ArrayList<>();
    /**
     * Simulation Controller for this simulation. Mostly used for statistics.
     */
    private SimulationController sim;

    /**
     * The only constructor for creating a new board.
     * @param width width of the board.
     * @param height height of the board.
     * @param hunterCount amount of hunter on the board.
     * @param preyCount amount of prey on the board.
     * @param obstacleCount amount of obstacles on the board.
     * @param sim simulation controller for this simulation.
     */
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

    /**
     * spawns a hunter with the following attributes:
     * @param speed speed of the hunter.
     * @param strength strength of the hunter.
     * @param sight view distance of the hunter.
     * @param energy initial energy of the hunter.
     */
    public void spawnHunter(int speed, int strength, int sight, int energy) {
        hunters.add(new Hunter(generateRandomLoc(), this, speed, strength, sight, energy));
    }

    /**
     * spawns a prey with the following attributes:
     * @param speed speed of the prey.
     * @param strength strength of the prey.
     * @param sight view distance of the prey.
     * @param energy initial energy of the prey.
     */
    public void spawnPrey(int speed, int strength, int sight, int energy) {
        preys.add(new Prey(generateRandomLoc(), this, speed, strength, sight, energy));
    }

    /**
     * spawns a specific BoardObject.
     * @param boardObject object that is going to be spawned.
     */
    public void spawn(BoardObject boardObject) {
        if (boardObject instanceof Hunter) {
            hunters.add((Hunter) boardObject);
        } else if (boardObject instanceof Prey) {
            preys.add((Prey) boardObject);
        } else {
            boardObjects.add(boardObject);
        }
    }


    // Spawn at random positions
    /**
     * Spawns a hunter with random attributes at a random location.
     */
    private void spawnHunter() {
        hunters.add(new Hunter(generateRandomLoc(), this));
    }
    /**
     * Spawns a prey with random attributes at a random location.
     */
    private void spawnPrey() {
        preys.add(new Prey(generateRandomLoc(), this));
    }
    /**
     * Spawns an obstacle with random attributes at a random location.
     */
    private void spawnObstacle() {
        boardObjects.add(new Obstacle(generateRandomLoc()));
    }


    /**
     * Removes an BoardObject from the board.
     * @param b boardObject that is going to be removed.
     */
    void removeFromBoard(BoardObject b) {
        if (b instanceof Hunter) {
            removeHunter((Hunter) b);
        } else if (b instanceof Prey) {
            removePrey((Prey) b);
        } else {
            removeObstacle(b);
        }
    }

    /**
     * Removes an hunter from the board.
     * @param h hunter that is going to be removed.
     */
    private void removeHunter(Hunter h) {
        hunters.remove(h);
    }
    /**
     * Removes a prey from the board.
     * @param p prey that is going to be removed.
     */
    private void removePrey(Prey p) {
        preys.remove(p);
    }
    /**
     * Removes an obstacle from the board.
     * @param b obstacle that is going to be removed.
     */
    private void removeObstacle(BoardObject b) {
        boardObjects.remove(b);
    }


    /**
     * Gets the statistics of this simulation.
     * @return statistics object.
     */
    public Statistics getStats() {
        return sim.getStats();
    }

    /**
     * Gets all hunters of this simulation.
     * @return list of hunters.
     */
    public List<Hunter> getHunters() {
        return hunters;
    }

    /**
     * Gets all preys of this simulation.
     * @return list of preys.
     */
    public List<Prey> getPreys() {
        return preys;
    }

    /**
     * Gets all BoardObject that are non-living-creatures
     * @return list of board objects.
     */
    public List<BoardObject> getNonLivingBoardObjects() {
        return boardObjects;
    }

    /**
     * Gets width of the board.
     * @return width of the board.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets height of the board.
     * @return height of the board.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets a list of all board objects on the board.
     * @return List of all boardObject on the board.
     */
    public List<BoardObject> getBoardObjects() {
        List<BoardObject> res = new ArrayList<>();
        res.addAll((List<BoardObject>)(List<?>) hunters);
        res.addAll((List<BoardObject>)(List<?>) preys);
        res.addAll((List<BoardObject>)(List<?>) boardObjects);
        return res;
    }

    /**
     * Gets list of all LivingCreatures on the board.
     * @return list of all LivingCreatures on the board.
     */
    public List<LivingCreature> getLivingCreatures() {
        List<LivingCreature> res = new ArrayList<>();
        res.addAll((List<LivingCreature>)(List<?>) hunters);
        res.addAll((List<LivingCreature>)(List<?>) preys);
        return res;
    }

    /**
     * Returns BoardObject at a given location.
     * @param loc location that is going to be checked.
     * @return null if there is no object at that location.
     */
    public BoardObject getObjectAtLocation(BoardObject.Location loc) {
        List<BoardObject> boardObjects = getBoardObjects();
        for (BoardObject curr: boardObjects) {
            if (curr.getLocation().equals(loc)) return curr;
        }
        return null;
    }

    /**
     * Helping method for generating a random location on the board.
     * @return Random empty location on the board.
     */
    private BoardObject.Location generateRandomLoc() {
        BoardObject.Location res;
        do {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            res = new BoardObject.Location(x, y);
        } while (!isEmpty(res));
        return res;
    }

    /**
     * Checks whether a location is empty.
     * @param loc Location that is going to be checked.
     * @return true if empty.
     */
    boolean isEmpty(BoardObject.Location loc) {
        if (!isOnBoard(loc)) return false;
        List<BoardObject> temp = getBoardObjects();
        for (BoardObject curr: temp) {
            if (curr.getLocation().equals(loc)) return false;
        }
        return true;
    }

    /**
     * Checks if a given location is inside the boundaries of the board.
     * @param loc location that is going to be checked.
     * @return true if location is on the board.
     */
    boolean isOnBoard(BoardObject.Location loc) {
        if (loc == null) return false;
        int x = loc.getX();
        int y = loc.getY();
        return ((x>=0 && x<width)  &&  (y>=0 && y<height));
    }

}