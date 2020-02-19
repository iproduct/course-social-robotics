package hello;

public class ObstacleAvoidanceBehavior implements Behavior {
	
	public static final double MINIMAL_DISTANCE_TRESHOLD = 0.1; // in centimetertes

	private double distance = 40;

	public ObstacleAvoidanceBehavior() {
	}
	
	public ObstacleAvoidanceBehavior(double distance) {
		this.distance = distance;
	}

	@Override
	public MovementStatus execute(Robot robot) {
		MovementStatus status = MovementStatus.OK;
		double distanceTravelled = 0;
		do {
			status = robot.moveForwardWhileTableAndNoObstacle(distance - distanceTravelled);
			distanceTravelled += robot.getState().getLastTravelledDistance();
			if (status.equals(MovementStatus.STOP_COLOR)) {
				robot.moveBackward(5);
				distanceTravelled += robot.getState().getLastTravelledDistance();
				robot.turnRight(90);
				distanceTravelled += robot.getState().getLastTravelledDistance();
			} else if (status.equals(MovementStatus.OBSTACLE)) {
				robot.moveBackward(5);
				distanceTravelled += robot.getState().getLastTravelledDistance();
				robot.turnRight(90);
				distanceTravelled += robot.getState().getLastTravelledDistance();
			}
			robot.getLcd().drawString("D:" + distanceTravelled, 0, 0);
			robot.getLcd().drawString("LD:" + robot.getState().getLastTravelledDistance(), 0, 1);
		} while (distanceTravelled < distance - MINIMAL_DISTANCE_TRESHOLD);
		return status;
	}

}
