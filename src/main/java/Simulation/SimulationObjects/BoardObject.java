package Simulation.SimulationObjects;

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

        public BoardObject.Location move(String direction) {
            if (direction.contains("north")) moveNorth();
            if (direction.contains("south")) moveSouth();
            if (direction.contains("west")) moveWest();
            if (direction.contains("east")) moveEast();
            return this;
        }

        // returns a string with "north", "south", "west" or "east" depending on how to get to Loc
        // null if both are the same
        public String wayTo(Location destination) {
            if (destination.x > x && destination.y > y) {
                return "southeast";
            } else if (destination.x > x && destination.y < y) {
                return "northeast";
            } else if (destination.x < x && destination.y > y) {
                return "southwest";
            } else if (destination.x < x && destination.y < y) {
                return "northwest";
            } else if (destination.x < x) {
                return "west";
            } else if (destination.x > x) {
                return "east";
            } else if (destination.y < y) {
                return "north";
            } else if (destination.y > y) {
                return "south";
            }
            return null;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Location)) return false;
            Location loc = (Location) obj;
            return (loc.x == x && loc.y == y);
        }

        @Override
        public Object clone() {
            return new BoardObject.Location(x, y);
        }
    }
}
