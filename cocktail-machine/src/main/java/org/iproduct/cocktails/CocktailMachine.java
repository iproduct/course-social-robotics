package org.iproduct.cocktails;

import static com.pi4j.io.gpio.PinState.HIGH;
import static com.pi4j.io.gpio.PinState.LOW;
import static com.pi4j.io.gpio.RaspiPin.GPIO_00;
import static com.pi4j.io.gpio.RaspiPin.GPIO_02;
import static com.pi4j.io.gpio.RaspiPin.GPIO_03;
import static com.pi4j.io.gpio.RaspiPin.GPIO_04;
import static com.pi4j.io.gpio.RaspiPin.GPIO_05;
import static com.pi4j.io.gpio.RaspiPin.GPIO_06;
import static org.iproduct.cocktails.model.CommandName.*;

import java.io.IOException;

import org.iproduct.cocktails.flowmeter.FlowMeterFlux;
import org.iproduct.cocktails.model.Command;
import org.iproduct.cocktails.model.IngredientPercentage;
import org.iproduct.cocktails.model.Cocktail;
import org.iproduct.cocktails.model.CocktailRecipe;
import org.iproduct.cocktails.pump.PumpMotorSubscriber;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuple8;
import reactor.util.function.Tuples;

public class CocktailMachine {
	private static final Pin[] PUMP_PINS = { GPIO_00, GPIO_02, GPIO_03 };
	private static final Pin[] FLOWMETER_PINS = { GPIO_06, GPIO_05, GPIO_04 };
	private static final double[] PUMP_STOP_DELTA_MILILITERS = {3.2, 2.1, 2.3} ; // how early to stop before reacing desired quantity
	private static long PUMP_TIMEOUT_MS = 20000;

	private final GpioController gpio;
	private final FlowMeterFlux[] flowMeterFluxes = new FlowMeterFlux[3];
	private final PumpMotorSubscriber[] pumpSubscribers = new PumpMotorSubscriber[3];
	private final CommandFlux commands = new CommandFlux();

	// private RobotController controller;
	// private RobotView view;
	// private ArduinoData arduinoData;
	// private ArduinoCommandSubscriber arduinoCommandsSub;
	// private PositionsFlux positionsFlux;
	// private MovementCommandSubscriber movementSub, movementSub2;
	// private List<JComponent> presentationViews = new ArrayList<>();
	// private RobotWSService remoteClientService;
	// private AudioPlayer audio;

	public CocktailMachine() throws IOException {
		gpio = GpioFactory.getInstance();
		// receive FlowMeter data readings
		for (int i = 0; i < FLOWMETER_PINS.length; i++) {
			flowMeterFluxes[i] = new FlowMeterFlux(gpio, FLOWMETER_PINS[i]);
			Flux.combineLatest(flowMeterFluxes[i], Flux.just(i), (r, n) -> n + " --> " + r)
					.subscribe(System.out::println);
		}
		for (int i = 0; i < PUMP_PINS.length; i++)
			pumpSubscribers[i] = new PumpMotorSubscriber(gpio, PUMP_PINS[i]);

		// Flux.just(HIGH, LOW)
		// .zipWith(Flux.intervalMillis(0,5000))
		// .map(Tuple2::getT1)
		// .subscribe(pumpSubscribers[2]);
		
		Flux<Tuple3<Double, Double, Double>> flowMeteresFlux = 
			Flux.combineLatest(
				(fmReadings) -> Tuples.of((Double) fmReadings[0], (Double)fmReadings[1], (Double)fmReadings[2]),
				flowMeterFluxes
			);
		
		Flux<Tuple2<Command, Tuple3<Double, Double, Double>>> inDataFlux = 
			commands.withLatestFrom(
				flowMeteresFlux, 
				(c, fmr3) -> Tuples.of(c, fmr3) 
			);
		
		// CocktailMachine state consists of:
		// 1-3 pump motor pin state (LOW or HIGH)
		// 4-6 start flow-meter quantities at command start time
		// 7-9 target ingredient quantities
		// 10 command executed currently
		Flux<Tuple4< Tuple3<PinState, PinState, PinState>, Tuple3<Double, Double, Double>, 
		Tuple3<Double, Double, Double>, Command>> stateFlux = 
			inDataFlux.scan(
				Tuples.of(
					Tuples.of(LOW, LOW, LOW)),   //pump motor pin states
					Tuples.of(0.0, 0.0, 0.0),    // target ingredient quantities
					Tuples.of(0.0, 0.0, 0.0),    // start flow-meter quantities
					new Command(VOID, null)),    // last executed command
				
				(state, eventTpl) -> {
				Command command = eventTpl.getT1();
				switch (command.getName()) {
					case MAKE_COCKTAIL : 
						Cocktail cocktail = (Cocktail) command.getData();
						double quantity = cocktail.getQuantity();
						CocktailRecipe recipe = cocktail.getRecipe();
						IngredientPercentage[] ingredients = recipe.getIngredientPercentages();
						double q1 = ingredients[0].getPercentage() * quantity;
						double q2 = ingredients[1].getPercentage() * quantity;
						double q3 = ingredients[2].getPercentage() * quantity;
						PinState ps1 = q1 > 0 ? HIGH : LOW;
						PinState ps2 = q2 > 0 ? HIGH : LOW;
						PinState ps3 = q3 > 0 ? HIGH : LOW;
						return Tuples.of(Tuples.of(ps1, ps2, ps3), Tuples.of(q1, q2, q3), 
								Tuples.of(sq1, sq2, sq3), command);
				}
				return Tuples.of(
						new PinState[] {LOW, LOW, LOW}, //pump motor pin states
						new Double[]{0.0, 0.0, 0.0},    // target ingredient quantities
						new Double[]{0.0, 0.0, 0.0},    // start flow-meter quantities
						command);
			}).doOnNext(System.out::println);
			
		
		
		int i = 0;
		long timeout = PUMP_TIMEOUT_MS;
		double quantity = 20;
		
		makeCocktail(i, timeout, quantity);

		// //calculate robot positions
		// positionsFlux =
		// PositionFactory.createPositionFlux(arduinoData.getEncoderReadingsFlux());
		// presentationViews.add(PositionFactory.createPositionPanel(positionsFlux));
		//
		// //enable sending commands to Arduino
		// arduinoCommandsSub =
		// ArduinoFactory.getInstance().createArduinoCommandSubscriber();
		//
		// //Audio player - added @jPrime 2016 Hackergarten
		// audio = AudioFactory.createAudioPlayer();
		//
		// //wire robot main controller with services
		// movementSub =
		// MovementFactory.createMovementCommandSubscriber(positionsFlux,
		// arduinoData.getLineReadingsFlux());
		// controller = new RobotController(this::tearDown, movementSub,
		// arduinoCommandsSub, audio);
		//
		// //create view with controller and delegate material views from query
		// services
		// view = new RobotView("IPTPI Reactive Robotics Demo", controller,
		// presentationViews);
		//
		// //expose as WS service
		// movementSub2 =
		// MovementFactory.createMovementCommandSubscriber(positionsFlux,
		// arduinoData.getLineReadingsFlux());
		// remoteClientService = new RobotWSService(positionsFlux,
		// movementSub2);

	}

	protected void makeCocktail(int i, long timeout, double targetQuantity) {
		// Shedule
//		Flux<PinState> commands = Flux.just(HIGH, LOW).zipWith(Flux.intervalMillis(0, timeout))
//				.map(Tuple2::getT1);
//
//		Flux.combineLatest(Flux.just(0.0).mergeWith(flowMeterFluxes[i]), commands, (f, c) -> Tuples.of(f, c))
		
		Flux.just(0.0).mergeWith(flowMeterFluxes[i])
		.skipUntil(qty -> qty >= targetQuantity - PUMP_STOP_DELTA_MILILITERS[i] )
		.timeoutMillis(timeout, Mono.just(0.0))
		.doOnNext(q -> System.out.println("q1 - > " + q))
		.next()
		.thenMany(Flux.just(LOW))
		.startWith(HIGH)
		.doOnNext(c -> System.out.println("command - > " + c))
		.subscribe(pumpSubscribers[i]);
	}

	// public void tearDown(Integer exitStatus) {
	// reactor.util.Logger log = Loggers.getLogger(this.getClass().getName());
	// log.info("Tearing down services and exiting the system");
	// try {
	// remoteClientService.teardown();
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// System.exit(exitStatus);
	// }

	public static void main(String[] args) throws InterruptedException {
		// initialize wiringPi library
		// Gpio.wiringPiSetupGpio();
		//
		// // US distance sensor GPIO output pin - switch US off
		// Gpio.pinMode(14, Gpio.OUTPUT);
		// Gpio.digitalWrite(14, false);

		try {
			CocktailMachine machine = new CocktailMachine();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Thread.sleep(5000);
	}
}
