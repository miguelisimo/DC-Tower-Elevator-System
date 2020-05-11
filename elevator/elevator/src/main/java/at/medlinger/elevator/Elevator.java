package at.medlinger.elevator;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Elevator implements Runnable {

    private static final int MILLIS_PER_FLOOR = 1000;
    private final int elevatorId;
    private final Logger logger;
    private Thread thread;
    private final AtomicBoolean shutdownTrigger = new AtomicBoolean(false);
    private final AtomicInteger currentFloor = new AtomicInteger(0);
    private int targetFloor = 0;
    private AtomicBoolean busy = new AtomicBoolean( false );
    private final LinkedBlockingQueue<ElevatorCommand> commandQueue = new LinkedBlockingQueue<>();

    public Elevator( int elevatorId ) {
        this.elevatorId = elevatorId;
        this.logger =  Logger.getLogger("Elevator " + this.elevatorId);
    }

    @Override
    public void run() {
    	// starting up the elevator thread - keeps the elevator thread running while shutdown isn't triggered
        info("starting up");
        while(!this.shutdownTrigger.get()) {
            info("waiting for command");
            ElevatorCommand command = null;
            try {
            	// retrieves and remove the element from the head of the command queue - blocks if queue is empty
                command = commandQueue.take(); // 
            } catch (InterruptedException e) {
            }
           // if an incoming command is a move command for the elevator - assigns elevator to move to the requested floor 
            if ( command instanceof ElevatorMoveCommand ) {
                ElevatorMoveCommand elevatorMoveCommand = (ElevatorMoveCommand) command; //downcasting 
                info("got command 'move to floor " + elevatorMoveCommand.getFloor() + "' - currently at floor " + currentFloor.get());
                moveToFloor( elevatorMoveCommand.getFloor() );
            }
            if ( this.shutdownTrigger.get() ) break;
            if ( commandQueue.isEmpty() ) this.busy.set(false);
        }

        info("shutting down");
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public boolean isBusy() {
        return busy.get();
    }

    public void request( ElevatorCommand elevatorCommand ) {
        this.busy.set(true);
        this.commandQueue.add( elevatorCommand );
    }

    private void moveToFloor( int targetFloor ) {
    	// assigns elevator to move (step down) / (step up)  a floor each second - till the elevator's current floor equals the target floor
        info("moving to floor " + targetFloor + ", currently at floor " + currentFloor.get());
        this.targetFloor = targetFloor;
        while (currentFloor.get() != targetFloor) {
            try {
                Thread.sleep( MILLIS_PER_FLOOR );
                if ( currentFloor.get() < targetFloor ) {
                    int curFloor = this.currentFloor.incrementAndGet();
                    info("moved up to floor " + curFloor);
                } else {
                    int curFloor = this.currentFloor.decrementAndGet();
                    info("moved down to floor " + curFloor);
                }

            } catch (InterruptedException e) {
            }
            if (this.shutdownTrigger.get()) {
                this.busy.set(false);
                return;
            }
        }
        info("reached floor " + targetFloor);
    }

    private void info( String message ) {
        this.logger.log( Level.INFO,message );
    }

    public int getCurrentFloor() {
        return currentFloor.get();
    }

    public int getQueueSize() {
        return this.commandQueue.size();
    }

    public void startup() {
        this.thread = new Thread( this );
        this.thread.start();
    }

    public void shutdown() {
        this.shutdownTrigger.set( true );
        this.thread.interrupt();
    }
}
