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

        public void moveRight() {
            x++;
        }

        public void moveLeft() {
            x--;
        }

        public void moveUp() {
            y--;
        }

        public void moveDown() {
            y++;
        }

        private int x;
        private int y;

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Location)) return false;
            Location loc = (Location) obj;
            if (!(loc.x == x && loc.y == y)) return false;
            return true;
        }
    }
}
