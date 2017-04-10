package org.iproduct.cocktails;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.iproduct.cocktails.controller.CocktailMachineController;
import org.iproduct.cocktails.flowmeter.FlowMeterFlux;
import org.iproduct.cocktails.net.RobotWSService;
import org.iproduct.cocktails.pump.PumpMotorSubscriber;
import org.iproduct.cocktails.view.CoctailMachineView;
import org.iproduct.iptpi.domain.arduino.ArduinoCommandSubscriber;
import org.iproduct.iptpi.domain.arduino.ArduinoData;
import org.iproduct.iptpi.domain.arduino.ArduinoFactory;
import org.iproduct.iptpi.domain.audio.AudioFactory;
import org.iproduct.iptpi.domain.audio.AudioPlayer;
import org.iproduct.iptpi.domain.movement.MovementCommandSubscriber;
import org.iproduct.iptpi.domain.movement.MovementFactory;
import org.iproduct.iptpi.domain.position.PositionFactory;
import org.iproduct.iptpi.domain.position.PositionsFlux;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.Gpio;
import static com.pi4j.io.gpio.RaspiPin.*;

import reactor.core.publisher.Flux;
import reactor.util.Loggers;
import reactor.util.function.Tuple2;

import static com.pi4j.io.gpio.PinState.*;

public class CocktailMachine {
	private static Pin[] PUMP_PINS = { GPIO_00, GPIO_02, GPIO_03 };
	private static Pin[] FLOWMETER_PINS = { GPIO_06, GPIO_05, GPIO_04 };
	private static long PUMP_TIMEOUT_MS = 10000;
	
	private final GpioController gpio; 
	private final FlowMeterFlux[] flowMeterFluxes = new FlowMeterFlux[3]; 
	private final PumpMotorSubscriber[] pumpSubscribers = new PumpMotorSubscriber[3]; 
	
//	private RobotController controller;
//	private RobotView view;
//	private ArduinoData arduinoData;
//	private ArduinoCommandSubscriber arduinoCommandsSub;
//	private PositionsFlux positionsFlux;
//	private MovementCommandSubscriber movementSub, movementSub2;
//	private List<JComponent> presentationViews = new ArrayList<>();
//	private RobotWSService remoteClientService;
//	private AudioPlayer audio;
	
	public CocktailMachine() throws IOException {
		gpio = GpioFactory.getInstance();
		//receive FlowMeter 1 data readings
		for(int i = 0; i < FLOWMETER_PINS.length; i++){
			flowMeterFluxes[i] = new FlowMeterFlux(gpio, FLOWMETER_PINS[i]);
			Flux.combineLatest(flowMeterFluxes[i], Flux.just(i), (r, n) -> n + " --> " + r )
				.subscribe(r -> System.out.println(r));
		}
		for(int i = 0; i < PUMP_PINS.length; i++)
			pumpSubscribers[i] = new PumpMotorSubscriber(gpio, PUMP_PINS[i]);
		
		
		Flux.just(HIGH, LOW)
			.zipWith(Flux.intervalMillis(0,30000))
			.map(Tuple2::getT1)
			.subscribe(pumpSubscribers[2]);
		
		
//		//calculate robot positions
//		positionsFlux = PositionFactory.createPositionFlux(arduinoData.getEncoderReadingsFlux());
//		presentationViews.add(PositionFactory.createPositionPanel(positionsFlux));
//		
//		//enable sending commands to Arduino
//		arduinoCommandsSub = ArduinoFactory.getInstance().createArduinoCommandSubscriber();
//		
//		//Audio player - added @jPrime 2016 Hackergarten 
//		audio = AudioFactory.createAudioPlayer();
//		
//		//wire robot main controller with services
//		movementSub = MovementFactory.createMovementCommandSubscriber(positionsFlux, arduinoData.getLineReadingsFlux());
//		controller = new RobotController(this::tearDown, movementSub, arduinoCommandsSub, audio);
//		
//		//create view with controller and delegate material views from query services
//		view = new RobotView("IPTPI Reactive Robotics Demo", controller, presentationViews);
//		
//		//expose as WS service
//		movementSub2 = MovementFactory.createMovementCommandSubscriber(positionsFlux, arduinoData.getLineReadingsFlux());
//		remoteClientService = new RobotWSService(positionsFlux, movementSub2);	
				
	}
	
//	public void tearDown(Integer exitStatus) {
//		reactor.util.Logger log = Loggers.getLogger(this.getClass().getName());
//		log.info("Tearing down services and exiting the system");
//		try {
//			remoteClientService.teardown();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		System.exit(exitStatus);
//	}
	
	public static void main(String[] args) throws InterruptedException {
		// initialize wiringPi library
//		Gpio.wiringPiSetupGpio();
//		
//		// US distance sensor GPIO output pin - switch US off
//		Gpio.pinMode(14, Gpio.OUTPUT);
//		Gpio.digitalWrite(14, false);
		
		try {
			CocktailMachine machine = new CocktailMachine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread.sleep(40000);
	}
}
