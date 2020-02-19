package hello;

public class GoSquareBehavior implements Behavior {
	private double side = 10;
	
	public GoSquareBehavior(double side) {
		this.side = side;
	}

	@Override
	public MovementStatus execute(Robot robot) {
		for (int i = 0; i < 4; i++) {
			robot.moveForward(side);
			robot.turnRight(90);
		}
		return MovementStatus.OK;
	}

}
