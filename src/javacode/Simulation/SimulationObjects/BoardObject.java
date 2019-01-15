package javacode.Simulation.SimulationObjects;

public interface BoardObject {

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

        public Location moveEast() {
            x++;
            return this;
        }

        public Location moveWest() {
            x--;
            return this;
        }

        public Location moveNorth() {
            y--;
            return this;
        }

        public Location moveSouth() {
            y++;
            return this;
        }

        private int x;
        private int y;

        public int getDistance(Location goalLocation) {
            int distance = Math.abs(this.x - goalLocation.x);
            distance = distance + Math.abs(this.y - goalLocation.y);
            return distance;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Location)) return false;
            Location loc = (Location) obj;
            if (!(loc.x == x && loc.y == y)) return false;
            return true;
        }

        @Override
        public Object clone() {
            return new BoardObject.Location(x, y);
        }
    }
}
