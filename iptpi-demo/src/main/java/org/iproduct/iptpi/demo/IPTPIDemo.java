package org.iproduct.iptpi.demo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.iproduct.iptpi.demo.controller.RobotController;
import org.iproduct.iptpi.demo.net.RobotWSService;
import org.iproduct.iptpi.demo.view.RobotView;
import org.iproduct.iptpi.domain.arduino.ArduinoCommandSubscriber;
import org.iproduct.iptpi.domain.arduino.ArduinoData;
import org.iproduct.iptpi.domain.arduino.ArduinoFactory;
import org.iproduct.iptpi.domain.audio.AudioFactory;
import org.iproduct.iptpi.domain.audio.AudioPlayer;
import org.iproduct.iptpi.domain.movement.MovementCommandSubscriber;
import org.iproduct.iptpi.domain.movement.MovementFactory;
import org.iproduct.iptpi.domain.position.PositionFactory;
import org.iproduct.iptpi.domain.position.PositionsFlux;

import com.pi4j.wiringpi.Gpio;

import reactor.util.Loggers;

public class IPTPIDemo {
	private RobotController controller;
	private RobotView view;
	private ArduinoData arduinoData;
	private ArduinoCommandSubscriber arduinoCommandsSub;
	private PositionsFlux positionsFlux;
	private MovementCommandSubscriber movementSub, movementSub2;
	private List<JComponent> presentationViews = new ArrayList<>();
	private RobotWSService remoteClientService;
	private AudioPlayer audio;
	
	public IPTPIDemo() throws IOException {
		//receive Arduino data readings
		arduinoData = ArduinoFactory.getInstance().createArduinoData(); 
		
		//calculate robot positions
		positionsFlux = PositionFactory.createPositionFlux(arduinoData.getEncoderReadingsFlux());
		presentationViews.add(PositionFactory.createPositionPanel(positionsFlux));
		
		//enable sending commands to Arduino
		arduinoCommandsSub = ArduinoFactory.getInstance().createArduinoCommandSubscriber();
		
		//Audio player - added @jPrime 2016 Hackergarten 
		audio = AudioFactory.createAudioPlayer();
		
		//wire robot main controller with services
		movementSub = MovementFactory.createMovementCommandSubscriber(positionsFlux, arduinoData.getLineReadingsFlux());
		controller = new RobotController(this::tearDown, movementSub, arduinoCommandsSub, audio);
		
		//create view with controller and delegate material views from query services
		view = new RobotView("IPTPI Reactive Robotics Demo", controller, presentationViews);
		
		//expose as WS service
		movementSub2 = MovementFactory.createMovementCommandSubscriber(positionsFlux, arduinoData.getLineReadingsFlux());
		remoteClientService = new RobotWSService(positionsFlux, movementSub2);	
				
	}
	
	public void tearDown(Integer exitStatus) {
		reactor.util.Logger log = Loggers.getLogger(this.getClass().getName());
		log.info("Tearing down services and exiting the system");
		try {
			remoteClientService.teardown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(exitStatus);
	}
	
	public static void main(String[] args) {
		// initialize wiringPi library
		Gpio.wiringPiSetupGpio();
		
		// US distance sensor GPIO output pin - switch US off
		Gpio.pinMode(14, Gpio.OUTPUT);
		Gpio.digitalWrite(14, false);
		
		try {
			IPTPIDemo demo = new IPTPIDemo();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
