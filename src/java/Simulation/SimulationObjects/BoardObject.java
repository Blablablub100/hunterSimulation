package java.Simulation.SimulationObjects;

public interface BoardObject {

    Location location = new Location(-1, -1);

    class Location {
        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
        int x;
        int y;
    }
}
