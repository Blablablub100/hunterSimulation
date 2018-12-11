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

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Location)) return false;
            Location loc = (Location) obj;
            if (!(loc.x == x && loc.y == y)) return false;
            return true;
        }
    }
}
