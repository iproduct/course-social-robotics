package org.iproduct.iptpi.domain.movement;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.cbrt;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.signum;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;
import static org.iproduct.iptpi.demo.robot.RobotParametrs.MAIN_AXE_LENGTH;
import static org.iproduct.iptpi.demo.robot.RobotParametrs.MAX_ROBOT_ANGULAR_ACCELERATION;
import static org.iproduct.iptpi.demo.robot.RobotParametrs.MAX_ROBOT_LINEAR_ACCELERATION;
import static org.iproduct.iptpi.demo.robot.RobotParametrs.MAX_ROBOT_LINEAR_VELOCITY;
import static org.iproduct.iptpi.demo.robot.RobotParametrs.ROBOT_STOPPING_DECCELERATION;
import static org.iproduct.iptpi.demo.robot.RobotParametrs.WHEEL_RADIUS;
import static org.iproduct.iptpi.domain.CommandName.STOP;
import static org.iproduct.iptpi.domain.CommandName.VOID;

import org.iproduct.iptpi.domain.Command;
import org.iproduct.iptpi.domain.arduino.LineReadings;
import org.iproduct.iptpi.domain.audio.AudioPlayer;
import org.iproduct.iptpi.domain.position.Position;
import org.iproduct.iptpi.domain.position.PositionsFlux;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.pi4j.wiringpi.Gpio;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuples;

public class MovementCommandSubscriber implements Subscriber<Command>  {
	public static final int MAX_SPEED = 1024;
	public static final int CLOCK_DIVISOR = 2;
	public static final double LANDING_CURVE_PARAMETER = 0.000000005;
	
	public static final MotorsCommand STOP_COMMAND = new MotorsCommand(0, 0, 0, 0, 0);
	
	private Subscription subscription;
	private PositionsFlux positions;
	private Flux<LineReadings> lineReadings;
//	private SchedulerGroup eventLoops = SchedulerGroup.async();
	
	//Create movement command broadcaster
	private EmitterProcessor<Command> commandFlux = EmitterProcessor.create();
	
	
	public MovementCommandSubscriber(PositionsFlux positions, Flux<LineReadings> lineReadings) {
		this.positions = positions;
		this.lineReadings = lineReadings;
	}

	@Override
	public void onNext(Command command) {

		setupGpioForMovement();
		
		switch (command.getName()) {
		case MOVE_FORWARD : moveForward(command); break;
			
		case FOLLOW_LINE : followLine(command);	break;
		
		case MOVE_RELATIVE : moveRelative(command);	break;
			
		case STOP : 
			System.out.println("STOPPING THE ROBOT");
			runMotors(STOP_COMMAND);
			break;
			
		default:
			break;
		}

	}

	protected void moveRelative(Command command) {	
		RelativeMovement relMove = (RelativeMovement) command.getData();
		
		// start moving - and think later as it comes :)
		int directionL, directionR;
		if(relMove.getVelocity() < 0) {
			directionL = directionR = -1;
		} else {
			directionL = directionR = 1;
		}
		
		double targetVelocity = abs(relMove.getVelocity());
		int velocity = (int)(MAX_SPEED * targetVelocity / MAX_ROBOT_LINEAR_VELOCITY); // 50 mm/s max
		MotorsCommand initialCommand = new MotorsCommand(directionL, directionR, velocity, velocity, Long.MAX_VALUE); //distance still unknown
		System.out.println(initialCommand);
		runMotors(initialCommand);
	
		Position startPos = positions.elementAt(1).block();
							
		double targetDeltaX = relMove.getDeltaX();
		double targetDeltaY = relMove.getDeltaY();
		double targetX = startPos.getX() + targetDeltaX;
		double targetY = startPos.getY() + targetDeltaY;
	
		double distance = hypot(targetDeltaX, targetDeltaY);
		
		System.out.println("$$$$$$$$$$$$$$ TargetX=" + targetX );
		System.out.println("$$$$$$$$$$$$$$ TargetY=" + targetY );
		System.out.println("$$$$$$$$$$$$$$ Target Distance=" + distance);
	
		double targetHeading, targetDeltaHeading, targetCurvature, h = 0;
		if(relMove.getDeltaHeading() == 0 ) {
			targetCurvature = targetDeltaHeading = 0;
			targetHeading = startPos.getHeading();					
		} else {
			targetDeltaHeading = relMove.getDeltaHeading();
			targetHeading = startPos.getHeading() + targetDeltaHeading ;
			targetCurvature = (2 * sin(targetDeltaHeading / 2) ) / distance ;
			h = sqrt( 1/(targetCurvature * targetCurvature) - 0.25 * distance * distance );
		}
	
		double xC, yC; //circle center coordinates
		double r = hypot(distance/2, h);
		if(targetCurvature != 0) {
			double q = hypot( targetX - startPos.getX(), targetY - startPos.getY() ),
				x3 = (targetX + startPos.getX()) /2,
				y3 = (targetY + startPos.getY()) /2;
			if(targetCurvature > 0) {
				xC = x3 + sqrt(r*r - (q*q/4)) * (startPos.getY() - targetY)/q;
				yC = y3 + sqrt(r*r - (q*q/4)) * (targetX - startPos.getX() )/q;
			} else {
				xC = x3 - sqrt(r*r - (q*q/4)) * (startPos.getY() - targetY)/q;
				yC = y3 - sqrt(r*r - (q*q/4)) * (targetX - startPos.getX() )/q;					
			}
		} else {
			xC = (targetX + startPos.getX()) /2;
			yC = (targetY + startPos.getY()) /2;
		}
		
		System.out.println("$$$$$$$$$$$$$$ TargetHeading=" + targetHeading );
		System.out.println("$$$$$$$$$$$$$$ TargetCurvature=" + targetCurvature );
		
		double targetAngularVelocity;
		if (targetDeltaHeading != 0 && relMove.getAngularVelocity() == 0)
			targetAngularVelocity = targetVelocity * targetCurvature;
		else
			targetAngularVelocity = relMove.getAngularVelocity();
		
					
		double startH = startPos.getHeading();
		System.out.println("START POSITION: " + startPos);
		
		Flux<Position> skip1 = positions.skip(1);
		Flux.zip(positions, skip1)
			.scan(initialCommand, (last, tupple) -> {
				Position prevPos = ((Position)tupple.getT1());
				Position currPos = ((Position)tupple.getT2());
				float prevX = prevPos.getX();
				float prevY = prevPos.getY();
				double prevH = prevPos.getHeading();
				float currX = currPos.getX();
				float currY = currPos.getY();
				double currH = currPos.getHeading();
				System.out.println(currPos + " - " + prevPos);
				double dt = (currPos.getTimestamp() - prevPos.getTimestamp()) / 1000.0; //delta time in seconds between position redings
			
				if(dt <= 0)	return last; // if invalid sequence do nothing
				
				double time = (currPos.getTimestamp() - startPos.getTimestamp()) /1000.0;
				
				// calculating the ideal trajectory position
				double tarX, tarY, tarH, remainingPathLength;
				if(targetCurvature == 0) {
					tarX = startPos.getX() + targetVelocity * time * cos(targetHeading);
					tarY = startPos.getY() + targetVelocity * time * sin(targetHeading);
					remainingPathLength = hypot(targetX - currX, targetY - currY) ;
					tarH = targetHeading;
				} else {
					double deltaHeading = targetAngularVelocity * time;
					double startAng = atan((startPos.getY() - yC) / (startPos.getX() - xC));
					double angle = startAng + deltaHeading;
					if(signum(angle) != (startPos.getY() - yC))
						angle -= PI;
					tarX = cos(angle) * r + xC;
					tarY = sin(angle) * r + yC;
					tarH = startPos.getHeading() + deltaHeading;
					remainingPathLength = (targetDeltaHeading - deltaHeading ) / targetCurvature;
//					System.out.println("   -----> tarX=" + tarX + ", tarY=" + tarY  + ", tarH=" + tarH + ", deltaHeading=" + deltaHeading + ", startAng=" + startAng + ", angle=" + angle);
//					System.out.println("   -----> r=" + r + ", xC=" + xC  + ", yC=" + yC );
				}
				
				//calculating current trajectory parameters
				float dX = currX - prevX;
				float dY = currY - prevY;
				double currDist = hypot(dX, dY);
				double currV = currDist / dt; // current velocity [mm/s]
				double currAngV = (currH - prevH) / dt;
				
				//calculating errors
				double errX = (tarX - currX) * cos(tarH) + (tarY - currY) + sin(tarH);
				double errY = (tarX - currX) * sin(tarH) + (tarY - currY) + cos(tarH);
				double errH = tarH - currH;
				
				//calculating landing curve
				double Cx = LANDING_CURVE_PARAMETER;
				double dlandY = 3 * Cx * pow(cbrt(abs(errY) / Cx), 2) * signum(errY);
				double landH = tarH + atan(dlandY);
				double dErrY = -targetAngularVelocity * errX + currV * sin (errH); 
				double landAngV = targetAngularVelocity + (2 * (1 / cbrt(abs(errY) / Cx)) * dErrY) /
						(1 + tan(landH - tarH) * tan(landH - tarH));
				
				//calculating the corrected trajectory control parameters
				double switchAngV = landAngV - currAngV +
						sqrt(2 * MAX_ROBOT_ANGULAR_ACCELERATION * abs(landH - currH)) 
							* signum(landH - currH) * 0.2;
				double switchAngA = min(abs(switchAngV / dt), MAX_ROBOT_ANGULAR_ACCELERATION) * signum(switchAngV);
				double newAngV = currAngV + switchAngA * dt;
				
				
				//calculating new velocity
				double dErrX = targetVelocity - currV * cos(errH) + targetAngularVelocity * errY;
				double switchV = dErrX + sqrt( 2 * MAX_ROBOT_LINEAR_ACCELERATION * abs(errX)) * signum(errX);
				double switchA = min(abs(switchV / dt), MAX_ROBOT_LINEAR_ACCELERATION) * signum(switchV);
	
				//calculating delta motor speed control values
				double k = 0.1;
				double newDeltaLR = k* MAX_SPEED * MAIN_AXE_LENGTH * dt * switchAngA / (2 * WHEEL_RADIUS);
	
				//calculating new motor speed control values
				int newVL = (int) (last.getVelocityL() + switchA * dt / WHEEL_RADIUS - newDeltaLR * last.getDirL());
				int newVR = (int) (last.getVelocityR() + switchA * dt / WHEEL_RADIUS + newDeltaLR * last.getDirL());
	
				
				
				System.out.println("--> errH=" + errH + ", targetHeading=" + targetHeading + ", currH=" + currH + ", dist=" + currDist 
						);
//				System.out.println("!!! landH=" + landH + ", dErrY=" + dErrY
//						+ ", currAngV=" + currAngV + ", landAngV=" + landAngV + ", switchAngV=" + switchAngV 
//						+ ", switchAngA=" + switchAngA + ", newAngV=" + newAngV );
//				System.out.println("!!! remainingPathLength=" + remainingPathLength + ", dErrX=" + dErrX + ", switchV=" + switchV + ", switchA=" + switchA );
//				System.out.println("!!! newDeltaV=" + switchA * dt / WHEEL_RADIUS + ", newDelatLR=" + newDeltaLR + ", newVL=" + newVL + ", newVR=" + newVR);
	
					
				double remainingDeltaHeading = targetHeading - currH;
				if(remainingPathLength < last.getRemainingPath() 
						&& remainingPathLength > currV * currV / ROBOT_STOPPING_DECCELERATION
					|| targetDeltaHeading > 0.01 
						&& abs(remainingDeltaHeading) > 0.05 && remainingDeltaHeading * targetDeltaHeading > 0 ) { //drive until minimum distance to target
					return new MotorsCommand(last.getDirL(), last.getDirR(),  newVL,  newVR, (float) remainingPathLength);
				} else {
					System.out.println("FINAL POSITION: " + currPos);
					return STOP_COMMAND;
				}				
			}).map((MotorsCommand motorsCommand) -> {
				runMotors(motorsCommand);
				return motorsCommand;
			})
			.takeUntil((MotorsCommand motorsCommand) -> motorsCommand.equals(STOP_COMMAND) ) 
			.subscribe( (MotorsCommand motorsCommand) -> {
				System.out.println(motorsCommand);
			});
		
	}

	protected void followLine(Command command) {
		{
			ForwardMovement forwardMove = (ForwardMovement) command.getData();
			
			// start moving - and think later as it comes :)
			int directionL, directionR;
			if(forwardMove.getVelocity() < 0) {
				directionL = directionR = -1;
			} else {
				directionL = directionR = 1;
			}
			
			double targetVelocity = abs(forwardMove.getVelocity());
			int velocity = (int)(MAX_SPEED * targetVelocity / MAX_ROBOT_LINEAR_VELOCITY); // 50 mm/s max
			MotorsCommand initialCommand = new MotorsCommand(directionL, directionR, velocity, velocity, Long.MAX_VALUE); //distance still unknown
			System.out.println(initialCommand);
			runMotors(initialCommand);

			Position startPos = positions.elementAt(1).block();
								
			double distance = forwardMove.getDistance();
			double targetHeading = startPos.getHeading();
			double targetDeltaX = distance * cos(targetHeading);
			double targetDeltaY = distance * sin(targetHeading);
			double targetX = startPos.getX() + targetDeltaX;
			double targetY = startPos.getY() + targetDeltaY;

			System.out.println("$$$$$$$$$$$$$$ TargetX=" + targetX );
			System.out.println("$$$$$$$$$$$$$$ TargetY=" + targetY );
			System.out.println("$$$$$$$$$$$$$$ Target Distance=" + distance);
     		System.out.println("$$$$$$$$$$$$$$ TargetHeading=" + targetHeading );
			
			double startH = startPos.getHeading();
			System.out.println("START POSITION: " + startPos);
			
			Flux<Position> skip1 = positions.skip(1);
			Flux<Tuple2<Position, Position>> lastTwoPositionsFlux = Flux.zip(positions, skip1);
			
			Flux<Tuple4<Position, Position, LineReadings, Command>> flux = 
				Flux.combineLatest(
					lastTwoPositionsFlux, 
					lineReadings,
					commandFlux.startWith(new Command(VOID, null)),
					(Object[] args) -> 
						Tuples.of(((Tuple2<Position, Position>)args[0]).getT1(), 
								((Tuple2<Position, Position>)args[0]).getT2(), 
								(LineReadings)args[1], 
								(Command)args[2])						
				);
			
			flux.scan(initialCommand, (last, tuple4) -> {
					System.out.println("########## NEW EVENT !!!!!!!!!!!");
					Position prevPos = tuple4.getT1();
					Position currPos = tuple4.getT2();
					LineReadings lastReadings = tuple4.getT3();
					Command lastCommand = tuple4.getT4();
					float prevX = prevPos.getX();
					float prevY = prevPos.getY();
					double prevH = prevPos.getHeading();
					float currX = currPos.getX();
					float currY = currPos.getY();
					double currH = currPos.getHeading();
					System.out.println(currPos + " - " + prevPos);
					double dt = (currPos.getTimestamp() - prevPos.getTimestamp()) / 1000.0; //delta time in seconds between position redings
				
					if(dt <= 0)	return last; // if invalid sequence do nothing
					
					double time = (currPos.getTimestamp() - startPos.getTimestamp()) /1000.0;
					
					// calculating the ideal trajectory position
					double tarX, tarY, tarH, remainingPathLength;
					tarX = startPos.getX() + targetVelocity * time * cos(targetHeading);
					tarY = startPos.getY() + targetVelocity * time * sin(targetHeading);
					remainingPathLength = hypot(targetX - currX, targetY - currY) ;
					tarH = targetHeading;
					
					//calculating current trajectory parameters
					float dX = currX - prevX;
					float dY = currY - prevY;
					double currDist = hypot(dX, dY);
					double currV = currDist / dt; // current velocity [mm/s]
					double currAngV = (currH - prevH) / dt;
					
					//calculating errors
					double errX = (tarX - currX) * cos(tarH) + (tarY - currY) + sin(tarH);
					double errY = (tarX - currX) * sin(tarH) + (tarY - currY) + cos(tarH);
					double errH = tarH - currH;
					
					//calculating landing curve
					double Cx = LANDING_CURVE_PARAMETER;
					double dlandY = 3 * Cx * pow(cbrt(abs(errY) / Cx), 2) * signum(errY);
					double landH = tarH + atan(dlandY);
					double dErrY = currV * sin (errH); 
					double landAngV = (2 * (1 / cbrt(abs(errY) / Cx)) * dErrY) /
							(1 + tan(landH - tarH) * tan(landH - tarH));
					
					//calculating the corrected trajectory control parameters
					double switchAngV = landAngV - currAngV +
							sqrt(2 * MAX_ROBOT_ANGULAR_ACCELERATION * abs(landH - currH)) 
								* signum(landH - currH) * 0.2;
					double switchAngA = min(abs(switchAngV / dt), MAX_ROBOT_ANGULAR_ACCELERATION) * signum(switchAngV);
					double newAngV = currAngV + switchAngA * dt;
					
					
					//calculating new velocity
					double dErrX = targetVelocity - currV * cos(errH);
					double switchV = dErrX + sqrt( 2 * MAX_ROBOT_LINEAR_ACCELERATION * abs(errX)) * signum(errX);
					double switchA = min(abs(switchV / dt), MAX_ROBOT_LINEAR_ACCELERATION) * signum(switchV);
//					double newV = currV + switchA * dt;

					//calculating delta motor speed control values
					double k = 0.1;
					double newDeltaLR = k* MAX_SPEED * MAIN_AXE_LENGTH * dt * switchAngA / (2 * WHEEL_RADIUS);

					//calculating new motor speed control values
					int newVL = (int) (last.getVelocityL() + switchA * dt / WHEEL_RADIUS - newDeltaLR * last.getDirL());
					int newVR = (int) (last.getVelocityR() + switchA * dt / WHEEL_RADIUS + newDeltaLR * last.getDirL());

					
					
					System.out.println("!!! time=" + time + ", dt=" + dt  + ", tarX=" + tarX + ", tarY=" + tarY 
							+ ", startH=" + startH + ", errH=" + errH + ", targetX=" + targetX + ", targetY=" + targetY + ", targetHeading=" + targetHeading 
							+ ", errX=" + errX + ", errY=" + errY + ", dlandY=" + dlandY + ", currV=" + currV + ", dist=" + currDist 
							+ ", switchAngV/dt=" + switchAngV / dt );
					System.out.println("!!! remainingPathLength=" + remainingPathLength + ", dErrX=" + dErrX + ", switchV=" + switchV + ", switchA=" + switchA );
					
					if(lastCommand.getName() != STOP && remainingPathLength < last.getRemainingPath() 
							&& remainingPathLength > currV * currV / ROBOT_STOPPING_DECCELERATION ) { //drive until minimum distance to target
						return new MotorsCommand(last.getDirL(), last.getDirR(),  newVL,  newVR, (float) remainingPathLength);
					} else {
						System.out.println("FINAL POSITION: " + currPos);
						return STOP_COMMAND;
					}				
				}).map((MotorsCommand motorsCommand) -> {
					runMotors(motorsCommand);
					return motorsCommand;
				})
				.takeUntil((MotorsCommand motorsCommand) -> motorsCommand.equals(STOP_COMMAND) ) 
				.subscribe( (MotorsCommand motorsCommand) -> {
					System.out.println(motorsCommand);
				});
		}
	}

	protected void moveForward(Command command) {
		{
			ForwardMovement forwardMove = (ForwardMovement) command.getData();
			
			// start moving - and think later as it comes :)
			int directionL, directionR;
			if(forwardMove.getVelocity() < 0) {
				directionL = directionR = -1;
			} else {
				directionL = directionR = 1;
			}
			
			double targetVelocity = abs(forwardMove.getVelocity());
			int velocity = (int)(MAX_SPEED * targetVelocity / MAX_ROBOT_LINEAR_VELOCITY); // 50 mm/s max
			MotorsCommand initialCommand = new MotorsCommand(directionL, directionR, velocity, velocity, Long.MAX_VALUE); //distance still unknown
			System.out.println(initialCommand);
			runMotors(initialCommand);

			Position startPos = positions.elementAt(1).block();
								
			double distance = forwardMove.getDistance();
			double targetHeading = startPos.getHeading();
			double targetDeltaX = distance * cos(targetHeading);
			double targetDeltaY = distance * sin(targetHeading);
			double targetX = startPos.getX() + targetDeltaX;
			double targetY = startPos.getY() + targetDeltaY;

			System.out.println("$$$$$$$$$$$$$$ TargetX=" + targetX );
			System.out.println("$$$$$$$$$$$$$$ TargetY=" + targetY );
			System.out.println("$$$$$$$$$$$$$$ Target Distance=" + distance);
     		System.out.println("$$$$$$$$$$$$$$ TargetHeading=" + targetHeading );
			
			double startH = startPos.getHeading();
			System.out.println("START POSITION: " + startPos);
			
			Flux<Position> skip1 = positions.skip(1);
			Flux<Tuple2<Position, Position>> lastTwoPositionsFlux = Flux.zip(positions, skip1);
			
			Flux<Tuple3<Position, Position, Command>> flux = 
				Flux.combineLatest(
					lastTwoPositionsFlux, 
					commandFlux.startWith(new Command(VOID, null)),
					(tuple2, lastCommand) -> Tuples.of(tuple2.getT1(), tuple2.getT2(), lastCommand)						
			);
			
			flux.scan(initialCommand, (last, tuple3) -> {
					System.out.println("########## NEW EVENT !!!!!!!!!!!");
					Position prevPos = tuple3.getT1();
					Position currPos = tuple3.getT2();
					Command lastCommand = tuple3.getT3();
					float prevX = prevPos.getX();
					float prevY = prevPos.getY();
					double prevH = prevPos.getHeading();
					float currX = currPos.getX();
					float currY = currPos.getY();
					double currH = currPos.getHeading();
					System.out.println(currPos + " - " + prevPos);
					double dt = (currPos.getTimestamp() - prevPos.getTimestamp()) / 1000.0; //delta time in seconds between position redings
				
					if(dt <= 0)	return last; // if invalid sequence do nothing
					
					double time = (currPos.getTimestamp() - startPos.getTimestamp()) /1000.0;
					
					// calculating the ideal trajectory position
					double tarX, tarY, tarH, remainingPathLength;
					tarX = startPos.getX() + targetVelocity * time * cos(targetHeading);
					tarY = startPos.getY() + targetVelocity * time * sin(targetHeading);
					remainingPathLength = hypot(targetX - currX, targetY - currY) ;
					tarH = targetHeading;
					
					//calculating current trajectory parameters
					float dX = currX - prevX;
					float dY = currY - prevY;
					double currDist = hypot(dX, dY);
					double currV = currDist / dt; // current velocity [mm/s]
					double currAngV = (currH - prevH) / dt;
					
					//calculating errors
					double errX = (tarX - currX) * cos(tarH) + (tarY - currY) + sin(tarH);
					double errY = (tarX - currX) * sin(tarH) + (tarY - currY) + cos(tarH);
					double errH = tarH - currH;
					
					//calculating landing curve
					double Cx = LANDING_CURVE_PARAMETER;
					double dlandY = 3 * Cx * pow(cbrt(abs(errY) / Cx), 2) * signum(errY);
					double landH = tarH + atan(dlandY);
					double dErrY = currV * sin (errH); 
					double landAngV = (2 * (1 / cbrt(abs(errY) / Cx)) * dErrY) /
							(1 + tan(landH - tarH) * tan(landH - tarH));
					
					//calculating the corrected trajectory control parameters
					double switchAngV = landAngV - currAngV +
							sqrt(2 * MAX_ROBOT_ANGULAR_ACCELERATION * abs(landH - currH)) 
								* signum(landH - currH) * 0.2;
					double switchAngA = min(abs(switchAngV / dt), MAX_ROBOT_ANGULAR_ACCELERATION) * signum(switchAngV);
					double newAngV = currAngV + switchAngA * dt;
					
					
					//calculating new velocity
					double dErrX = targetVelocity - currV * cos(errH);
					double switchV = dErrX + sqrt( 2 * MAX_ROBOT_LINEAR_ACCELERATION * abs(errX)) * signum(errX);
					double switchA = min(abs(switchV / dt), MAX_ROBOT_LINEAR_ACCELERATION) * signum(switchV);
//					double newV = currV + switchA * dt;

					//calculating delta motor speed control values
					double k = 0.1;
					double newDeltaLR = k* MAX_SPEED * MAIN_AXE_LENGTH * dt * switchAngA / (2 * WHEEL_RADIUS);

					//calculating new motor speed control values
					int newVL = (int) (last.getVelocityL() + switchA * dt / WHEEL_RADIUS - newDeltaLR * last.getDirL());
					int newVR = (int) (last.getVelocityR() + switchA * dt / WHEEL_RADIUS + newDeltaLR * last.getDirL());

					
					
					System.out.println("!!! time=" + time + ", dt=" + dt  + ", tarX=" + tarX + ", tarY=" + tarY 
							+ ", startH=" + startH + ", errH=" + errH + ", targetX=" + targetX + ", targetY=" + targetY + ", targetHeading=" + targetHeading 
							+ ", errX=" + errX + ", errY=" + errY + ", dlandY=" + dlandY + ", currV=" + currV + ", dist=" + currDist 
							+ ", switchAngV/dt=" + switchAngV / dt );
					System.out.println("!!! remainingPathLength=" + remainingPathLength + ", dErrX=" + dErrX + ", switchV=" + switchV + ", switchA=" + switchA );
					
					if(lastCommand.getName() != STOP && remainingPathLength < last.getRemainingPath() 
							&& remainingPathLength > currV * currV / ROBOT_STOPPING_DECCELERATION ) { //drive until minimum distance to target
						return new MotorsCommand(last.getDirL(), last.getDirR(),  newVL,  newVR, (float) remainingPathLength);
					} else {
						System.out.println("FINAL POSITION: " + currPos);
						return STOP_COMMAND;
					}				
				}).map((MotorsCommand motorsCommand) -> {
					runMotors(motorsCommand);
					return motorsCommand;
				})
				.takeUntil((MotorsCommand motorsCommand) -> motorsCommand.equals(STOP_COMMAND) ) 
				.subscribe( (MotorsCommand motorsCommand) -> {
					System.out.println(motorsCommand);
				});
		}
	}

	protected void setupGpioForMovement() {
		// Motor direction pins
		Gpio.pinMode(5, Gpio.OUTPUT);
		Gpio.pinMode(6, Gpio.OUTPUT);

		Gpio.pinMode(12, Gpio.PWM_OUTPUT);
		Gpio.pinMode(13, Gpio.PWM_OUTPUT);
		Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
		Gpio.pwmSetRange(MAX_SPEED);
		Gpio.pwmSetClock(CLOCK_DIVISOR);
	}

	private void runMotors(MotorsCommand mc) {
		//setting motor directions 
		Gpio.digitalWrite(5, mc.getDirR() > 0 ? 1 : 0);
		Gpio.digitalWrite(6, mc.getDirL() > 0 ? 1 : 0);
		//setting speed
		if(mc.getVelocityR() >= 0 && mc.getVelocityR() <= MAX_SPEED)
			Gpio.pwmWrite(12, mc.getVelocityR()); // speed up to MAX_SPEED
		if(mc.getVelocityL() >= 0 && mc.getVelocityL() <= MAX_SPEED)
			Gpio.pwmWrite(13, mc.getVelocityL());
		
		
	}

	@Override
	public void onSubscribe(Subscription s) {
		subscription = s;
		subscription.request(Long.MAX_VALUE);
	}

	@Override
	public void onError(Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		
	}

}
