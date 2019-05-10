package hello;

public class LineFollowBehavior implements Behavior {
	private double distance = 10;

	public LineFollowBehavior(double distance) {
		this.distance = distance;
	}

	@Override
	public MovementStatus execute(Robot robot) {
		double distanceTraveled = 0;
		robot.getMotorB().setSpeed(150);
		robot.getMotorC().setSpeed(150);
		do {
			double angle = 15;
			MovementStatus status;

			// find red
			System.out.println("Turn Left " + angle);
			status = robot.turnLeftWhileNotColor(angle, Color.RED);
			distanceTraveled += robot.getState().getLastTravelledDistance();

			// try harder
			while (status != MovementStatus.STOP_COLOR && angle <= 180)
			{
				angle += 30;

				System.out.println("Turn Right " + angle);
				status = robot.turnRightWhileNotColor(angle, Color.RED);
				distanceTraveled += robot.getState().getLastTravelledDistance();
				if (status == MovementStatus.STOP_COLOR)
					break;

				angle += 30;
				System.out.println("Turn Left " + angle);
				status = robot.turnLeftWhileNotColor(angle, Color.RED);
				distanceTraveled += robot.getState().getLastTravelledDistance();
				if (status == MovementStatus.STOP_COLOR)
					break;
			}

			if (status != MovementStatus.STOP_COLOR) {
				System.out.println("Exit with ERROR");
				return MovementStatus.ERROR;
			}
			System.out.println("Move Forward");
			robot.moveForwardWhileTableAndNoObstacle(8);
			distanceTraveled += robot.getState().getLastTravelledDistance();
//				robot.moveForwardWhileNoColorAndNoObstacle(10, Color.OTHER);
//				distanceTraveled += robot.getState().getLastTravelledDistance();

		} while (distanceTraveled < distance);
		return MovementStatus.OK;
	}

}
