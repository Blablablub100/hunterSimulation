package javacode.Simulation.SimulationObjects;

public interface BoardObject {

    void setLocation(Location loc);

    Location getLocation(Location loc);

    class Location {
        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
        int x;
        int y;
    }
}
