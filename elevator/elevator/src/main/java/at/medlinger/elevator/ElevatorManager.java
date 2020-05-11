package at.medlinger.elevator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class ElevatorManager {

    private final Elevator[] elevators;
    private boolean shutdown = false;
    private final Logger logger = Logger.getLogger("El Manager");

    
    public ElevatorManager(int elevators) {
    	// tells the elevator manager how many elevators to create
        this.elevators = IntStream.range(0, elevators).mapToObj(i -> new Elevator(i)).toArray(Elevator[]::new);
        Arrays.stream(this.elevators).forEach(e -> e.startup());
    }

    public void request(int floor) {
        // assign an elevator to move to floor - assign it to elevator with min command queue size
        Arrays.stream(this.elevators).min(Comparator.comparingInt(Elevator::getQueueSize)).ifPresent( e -> {
            info("assigning request to floor " + floor + " to elevator " + e.getElevatorId());
            e.request(new ElevatorMoveCommand(floor));
        });
    }

    public void shutdown() {
    	// shutdowns all elevator instances
        Arrays.stream(this.elevators).forEach(e -> e.shutdown());
        this.shutdown = true;
    }

    public void waitForShutdown() {
    	// delays the shutdown until the elevator threads are shut down
        while (!shutdown) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void info(String message) {
    	// logs the system data about elevator manager and elevator states - e.g. that the elevator manager assigns a request, or 
    	// that an elevator got the command to move to a target floor and if it has reached it, etc.
        this.logger.log(Level.INFO, message);
    }
}
