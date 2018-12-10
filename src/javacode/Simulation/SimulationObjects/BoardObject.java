package javacode.Simulation.SimulationObjects;

public interface BoardObject {

    Location loc = null;

    class Location {
        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
        int x;
        int y;
    }
}
