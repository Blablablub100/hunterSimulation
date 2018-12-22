package javacode.Simulation.SimulationObjects;

import java.util.ArrayList;
import java.util.List;

public class Prey extends LivingCreature {

    Prey(Location loc, Board myBoard) {
        this.loc = loc;
        this.myBoard = myBoard;
        direction = "east";
        // TODO randomize rest
    }

    // methods from LivingCreature
    @Override
    public void react() {
        see();
    }

    @Override
    public List<BoardObject> see() {

        sightDistance = 5;
        List<BoardObject> objectsSeen = new ArrayList<>();

        switch (direction) {
            case "north":
                seeNorth(objectsSeen);
                break;

            case "south":
                seeSouth(objectsSeen);
                break;

            case "west":
                seeWest(objectsSeen);
                break;

            case "east":
                seeEast(objectsSeen);
                break;
        }
        return objectsSeen;
    }

    private void seeNorth(List<BoardObject> objectsSeen){
        // in viewing direction
        int viewWidth = 1;

        for (int i = -1; i >= (-(sightDistance)); i--) {
            int currY = loc.getY() + i;

            northAndSouthScanOfForwardAndBackwards(objectsSeen, viewWidth, currY);
            viewWidth++;
        }

        // opposite direction of viewing direction
        viewWidth = 1;
        int sightDistanceTmp = sightDistance;
        if(sightDistanceTmp < 4) sightDistanceTmp = 4;

        for (int i = 1; i <= (sightDistanceTmp / 4); i++){
            int currY = loc.getY() + i;

            northAndSouthScanOfForwardAndBackwards(objectsSeen, viewWidth, currY);
            viewWidth++;
        }

        // left side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if(sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = -1; i >= (-(sightDistanceTmp / 2)); i--){
            int currX = loc.getX() + i;

            northAndSouthScanOfLeftAndRight(objectsSeen, viewWidth, currX);
            viewWidth++;
        }

        // right side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if(sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = 1; i <= (sightDistanceTmp / 2); i++){
            int currX = loc.getX() + i;

            northAndSouthScanOfLeftAndRight(objectsSeen, viewWidth, currX);
            viewWidth++;
        }
    }

    private void seeSouth(List<BoardObject> objectsSeen){
        // in viewing direction
        int viewWidth = 1;

        for (int i = 1; i <= sightDistance; i++) {
            int currY = loc.getY() + i;

            northAndSouthScanOfForwardAndBackwards(objectsSeen, viewWidth, currY);
            viewWidth++;
        }

        // opposite direction of viewing direction
        viewWidth = 1;
        int sightDistanceTmp = sightDistance;
        if(sightDistanceTmp < 4) sightDistanceTmp = 4;

        for (int i = -1; i >= (-(sightDistanceTmp / 4)); i--){
            int currY = loc.getY() + i;

            northAndSouthScanOfForwardAndBackwards(objectsSeen, viewWidth, currY);
            viewWidth++;
        }

        // right side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if(sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = -1; i >= (-(sightDistanceTmp / 2)); i--){
            int currX = loc.getX() + i;

            northAndSouthScanOfLeftAndRight(objectsSeen, viewWidth, currX);
            viewWidth++;
        }

        // left side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if(sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = 1; i <= (sightDistanceTmp / 2); i++){
            int currX = loc.getX() + i;

            northAndSouthScanOfLeftAndRight(objectsSeen, viewWidth, currX);
            viewWidth++;
        }
    }

    private void seeWest(List<BoardObject> objectsSeen){
        // in viewing direction
        int viewWidth = 1;

        for (int i = -1; i >= (-(sightDistance)); i--) {
            int currX = loc.getX() + i;

            westAndEastScanOfForwardAndBackwards(objectsSeen, viewWidth, currX);
            viewWidth++;
        }

        // opposite direction of viewing direction
        viewWidth = 1;
        int sightDistanceTmp = sightDistance;
        if (sightDistanceTmp < 4) sightDistanceTmp = 4;

        for (int i = 1; i <= (sightDistanceTmp / 4); i++) {
            int currX = loc.getX() + i;

            westAndEastScanOfForwardAndBackwards(objectsSeen, viewWidth, currX);
            viewWidth++;
        }

        // right side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if (sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = -1; i >= (-(sightDistanceTmp / 2)); i--) {
            int currY = loc.getY() + i;

            westAndEastScanOfLeftAndRight(objectsSeen, viewWidth, currY);
            viewWidth++;
        }

        // left side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if (sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = 1; i <= (sightDistanceTmp / 2); i++) {
            int currY = loc.getY() + i;

            westAndEastScanOfLeftAndRight(objectsSeen, viewWidth, currY);
            viewWidth++;
        }
    }

    private void seeEast(List<BoardObject> objectsSeen){
        // in viewing direction
        int viewWidth = 1;

        for (int i = 1; i <= sightDistance; i++) {
            int currX = loc.getX() + i;

            westAndEastScanOfForwardAndBackwards(objectsSeen, viewWidth, currX);
            viewWidth++;
        }

        // opposite direction of viewing direction
        viewWidth = 1;
        int sightDistanceTmp = sightDistance;
        if (sightDistanceTmp < 4) sightDistanceTmp = 4;

        for (int i = -1; i >= (-(sightDistanceTmp / 4)); i--) {
            int currX = loc.getX() + i;

            westAndEastScanOfForwardAndBackwards(objectsSeen, viewWidth, currX);
            viewWidth++;
        }

        // left side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if (sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = -1; i >= (-(sightDistanceTmp / 2)); i--) {
            int currY = loc.getY() + i;

            westAndEastScanOfLeftAndRight(objectsSeen, viewWidth, currY);
            viewWidth++;
        }

        // right side of viewing direction
        viewWidth = 0;
        sightDistanceTmp = sightDistance;
        if (sightDistanceTmp < 2) sightDistanceTmp = 2;

        for (int i = 1; i <= (sightDistanceTmp / 2); i++) {
            int currY = loc.getY() + i;

            westAndEastScanOfLeftAndRight(objectsSeen, viewWidth, currY);
            viewWidth++;
        }
    }

    private void northAndSouthScanOfForwardAndBackwards(List<BoardObject> objectsSeen, int viewWidth, int currY) {
        for (int j = viewWidth * (-1); j <= viewWidth; j++) {
            int currX = loc.getX() + j;
            BoardObject tmp = myBoard.getObjectAtLocation(new Location(currX, currY));
            if (tmp != null) objectsSeen.add(tmp);
        }
    }

    private void northAndSouthScanOfLeftAndRight(List<BoardObject> objectsSeen, int viewWidth, int currX) {
        if(viewWidth == 0){
            int currY = loc.getY();
            BoardObject tmp = myBoard.getObjectAtLocation(new Location(currX, currY));
            if(tmp != null) objectsSeen.add(tmp);
        } else {
            for (int j = viewWidth * (-1); j <= viewWidth; j++) {
                int currY = loc.getY() + j;
                BoardObject tmp = myBoard.getObjectAtLocation(new Location(currX, currY));
                if (tmp != null) objectsSeen.add(tmp);
            }
        }
    }

    private void westAndEastScanOfForwardAndBackwards(List<BoardObject> objectsSeen, int viewWidth, int currX) {
        for (int j = viewWidth * (-1); j <= viewWidth; j++) {
            int currY;
            currY = loc.getY() + j;
            BoardObject tmp = myBoard.getObjectAtLocation(new Location(currX, currY));
            if (tmp != null) objectsSeen.add(tmp);
        }
    }

    private void westAndEastScanOfLeftAndRight(List<BoardObject> objectsSeen, int viewWidth, int currY) {
        if(viewWidth == 0){
            int currX = loc.getX();
            BoardObject tmp = myBoard.getObjectAtLocation(new Location(currX, currY));
            if(tmp != null) objectsSeen.add(tmp);
        } else {
            for (int j = viewWidth * (-1); j <= viewWidth; j++) {
                int currX;
                currX = loc.getX() + j;
                BoardObject tmp = myBoard.getObjectAtLocation(new Location(currX, currY));
                if (tmp != null) objectsSeen.add(tmp);
            }
        }
    }

    @Override
    void eat() {

    }

    @Override
    boolean attack(LivingCreature opponent) {
        return false;
    }
}
