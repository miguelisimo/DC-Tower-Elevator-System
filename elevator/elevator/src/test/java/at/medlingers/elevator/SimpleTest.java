package at.medlingers.elevator;

import at.medlinger.elevator.ElevatorManager;
import org.junit.Before;
import org.junit.Test;

public class SimpleTest {

	private static final int BACK_TO_GROUND_FLOOR = 0;
	private static final int IBM_CIC = 35;

	static {
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%3$s] %5$s %n");

	}

	@Before
	public void beforeEachTestMethod() {
		System.out.println("Invoked before each test method");
	}

	
	@Test
	public void dcTowerElevatorSimulation() {
		/* This test simulates DC Tower Elevator System in a real world scenario. */
		System.out.println(
				"Intro: This Java program simulates the DC Tower elevator system, which handles incoming elevator requests.");
		System.out.println(
				"The DC Tower access on the ground floor, as well as the return trip from the floor to the ground floor,"
						+ "\n"
						+ "can be an inquiry. After the system receives a request, it searches for the next free elevator."
						+ "\n"
						+ "If no one is immediately free, the system should schedule the next one to pick up the passengers.");
		
		ElevatorManager elevatorManager = new ElevatorManager(7);
		
		elevatorManager.request(5);
		elevatorManager.request(7);
		elevatorManager.request(44);
		elevatorManager.request(12);
		elevatorManager.request(8);
		elevatorManager.request(IBM_CIC);
		elevatorManager.request(55);
		elevatorManager.request(38);
		elevatorManager.request(BACK_TO_GROUND_FLOOR);
		elevatorManager.request(4);
		
		/* Now all the elevators are going back to the ground floor */
		
		elevatorManager.request(BACK_TO_GROUND_FLOOR);
		elevatorManager.request(BACK_TO_GROUND_FLOOR);
		elevatorManager.request(BACK_TO_GROUND_FLOOR);
		elevatorManager.request(BACK_TO_GROUND_FLOOR);
		elevatorManager.request(BACK_TO_GROUND_FLOOR);
		elevatorManager.request(BACK_TO_GROUND_FLOOR);
		elevatorManager.request(BACK_TO_GROUND_FLOOR);
		elevatorManager.request(BACK_TO_GROUND_FLOOR);
		elevatorManager.request(BACK_TO_GROUND_FLOOR);
		
		elevatorManager.waitForShutdown();
	}

	@Test
	public void simpleTestScenario() {
		/* This test is there to demonstrate that the code works according to the requirements of the task. */
		System.out.println(
				"Intro: In this test scenario, the elevator system is tested to assign a request to the next free elevator as soon as it becomes free.");
		System.out.println(
				"To demonstrate this, the system is provided with two elevators, which are supposed to process three inquiries at the same time. "
						+ "\n" + "The latter being assigned to the first elevator available again.");
		
		ElevatorManager elevatorManager = new ElevatorManager(2);
		
		elevatorManager.request(7);
		elevatorManager.request(5);
		elevatorManager.request(3);

		elevatorManager.waitForShutdown();
	}

}
