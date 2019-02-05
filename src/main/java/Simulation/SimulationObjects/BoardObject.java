package Simulation.SimulationObjects;

/**
 * Interface describing what any BoardObject must at least have.
 */
public interface BoardObject {

    /**
     * There must be a way for any BoardObject to return a Location it is at.
     * @return Location of the BoardObject.
     */
    Location getLocation();

    /**
     * Location helper class for efficiently working with different locations on the simulation board. Every BoardObject
     * needs this kind of Location.
     */
    class Location {

        /**
         * Integer representing an x value on the board.
         */
        private int x;
        /**
         * Integer representing an y value on the board.
         */
        private int y;

        /**
         * The only constructor of a Location.
         * @param x x value of the location.
         * @param y y value of the location.
         */
        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Gets x value.
         * @return x value.
         */
        public int getX() {
            return x;
        }

        /**
         * Gets y value.
         * @return y value.
         */
        public int getY() {
            return y;
        }

        /**
         * Changes the location one time to the east.
         * @return The return of the object itself is used for concatenation.
         */
        public Location moveEast() {
            x++;
            return this;
        }
        /**
         * Changes the location one time to the west.
         * @return The return of the object itself is used for concatenation.
         */
        public Location moveWest() {
            x--;
            return this;
        }
        /**
         * Changes the location one time to the north.
         * @return The return of the object itself is used for concatenation.
         */
        public Location moveNorth() {
            y--;
            return this;
        }
        /**
         * Changes the location one time to the south.
         * @return The return of the object itself is used for concatenation.
         */
        public Location moveSouth() {
            y++;
            return this;
        }

        /**
         * Calculates the distance between this location and another location.
         * @param goalLocation second location for calculating the distance.
         * @return distance to the goalLocation as an integer.
         */
        public int getDistance(Location goalLocation) {
            int distance = Math.abs(this.x - goalLocation.x);
            distance = distance + Math.abs(this.y - goalLocation.y);
            return distance;
        }

        /**
         * Moves in a ceratain direction based on string input.
         * @param direction can be north, south, west or east.
         * @return Returns itself for concatenation.
         */
        public BoardObject.Location move(String direction) {
            if (direction.contains("north")) moveNorth();
            if (direction.contains("south")) moveSouth();
            if (direction.contains("west")) moveWest();
            if (direction.contains("east")) moveEast();
            return this;
        }

        /**
         * Gets a String saying which direction is the best to go to a certain destination.
         * @param destination goal destination.
         * @return either a direction String like north, south etc, or null if destinations are the same.
         */
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

        /**
         * Checks whether this object and another object are the same.
         * @param obj object this object is going to be compared to.
         * @return true if objects are the same objects.
         */
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Location)) return false;
            Location loc = (Location) obj;
            return (loc.x == x && loc.y == y);
        }

        /**
         * Creates a copy of the current object.
         * @return The copy of the current object.
         */
        @Override
        public Object clone() {
            return new BoardObject.Location(x, y);
        }
    }
}
