package javacode.Simulation.SimulationObjects;

public interface BoardObject {

    void setLocation(Location loc);

    Location getLocation();

    class Location {
        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        int x;
        int y;
    }
}
