package at.medlinger.elevator;

public class ElevatorMoveCommand implements ElevatorCommand {

    private final int floor;

    public ElevatorMoveCommand(int floor) {
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }
}
