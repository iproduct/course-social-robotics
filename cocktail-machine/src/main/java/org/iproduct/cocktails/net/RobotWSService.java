package org.iproduct.cocktails.net;

import static org.iproduct.iptpi.domain.CommandName.MOVE_RELATIVE;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.function.Function;

import org.iproduct.iptpi.domain.Command;
import org.iproduct.iptpi.domain.movement.MovementCommandSubscriber;
import org.iproduct.iptpi.domain.movement.RelativeMovement;
import org.iproduct.iptpi.domain.position.PositionsFlux;
import org.reactivestreams.Publisher;

import com.google.gson.Gson;

import reactor.core.publisher.Mono;
import reactor.core.publisher.TopicProcessor;
import reactor.ipc.netty.config.ServerOptions;
import reactor.ipc.netty.http.HttpChannel;
import reactor.ipc.netty.http.HttpServer;
import reactor.util.Logger;
import reactor.util.Loggers;

public class RobotWSService {
	private HttpServer httpServer;
	private TopicProcessor<Command> movementCommands;
	private PositionsFlux positions;
	private MovementCommandSubscriber movements;
	private Gson gson = new Gson();
	private String ipAddress = "192.168.0.108";

	private static final Charset UTF_8 = Charset.forName("utf-8");
	private static final String GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
	private static final String DEFAULT_IP_ADDRESS = "192.168.0.108";
	private static final int PORT = 80;
	private static final String IP_V4_REGEX = "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
	private Logger log = Loggers.getLogger("RobotWSService");

	public RobotWSService(PositionsFlux positions, MovementCommandSubscriber movementSubscriber)
			throws UnknownHostException {
		this.positions = positions;
		this.movements = movementSubscriber;

		try {
			ipAddress = findMyInetAddress();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		try {
			setup();
			movementCommands = TopicProcessor.create();
			movementCommands.subscribe(movementSubscriber);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public void setup() throws InterruptedException {
		setupServer();
		
	}

	private void setupServer() throws InterruptedException {
		// EventLoopGroup workerGroup = new NioEventLoopGroup(6);

		ServerOptions hso = ServerOptions.on(ipAddress, PORT).timeoutMillis(5000);
		// .keepAlive(false)
		// .eventLoopGroup(workerGroup);
		httpServer = HttpServer.create(hso);
		httpServer.start(getResourceHandler()).subscribe();
	}

	public void teardown() throws InterruptedException {
		httpServer.shutdown().subscribe(System.out::println);
	}

	private Function<? super HttpChannel, ? extends Publisher<Void>> getResourceHandler() {
		return channel -> {
			System.out.println("REQUESTED: " + channel.uri());
			String uri = channel.uri();
			
			if(uri.equals("/ws")) {
				return getWebSocketHandler(channel);
			} else {
				return getStaticResourceHandler(channel);
			}
		};
	}

	private Publisher<Void> getStaticResourceHandler(HttpChannel channel) {
						
		String uri = channel.uri();
		
		// enable HTML 5 History API 
		if (uri.equals("/") || uri.indexOf('.') == -1)
			uri = "/index.html";

		String contentType;
		// resolve content type by file extension
		switch (uri.substring(uri.lastIndexOf('.') + 1)) { 
		case "html":
			contentType = "text/html;charset=utf-8";
			break;
		case "css":
			contentType = "text/css";
			break;
		case "js":
			contentType = "application/javascript";
			break;
		case "map":
			contentType = "application/json";
			break;
		case "jpg":
			contentType = "image/jpeg";
			break;
		case "png":
			contentType = "image/png";
			break;
		case "gif":
			contentType = "image/gif";
			break;
		case "ico":
			contentType = "image/x-icon";
			break;
		default:
			contentType = "text/plain";
		}

		String path = "/home/pi/.launchpi_projects/iptpi-demo/webapp/dist" + uri; // on Pi
		String encodings = channel.headers().get("Accept-Encoding");
		File responseFile;
		
		// try to send compresses js and css if available 
		if(( contentType.equals("application/javascript") || contentType.equals("text/css") )
				&& encodings != null && encodings.contains("gzip")) {
			responseFile = new File(path + ".gz");
			if(responseFile.exists()) {
				channel.addResponseHeader("Content-Encoding", "gzip") ;
			} else {
				responseFile = new File(path);
				if(!responseFile.exists()) {
					return sendErrorNotFound(channel);
				}
			}
		} else {
			responseFile = new File(path);
			if(!responseFile.exists()) {
				return sendErrorNotFound(channel);
			}
		}
		
		Mono<Void> result = channel.status(200)
				.addResponseHeader("Content-Type", contentType)
				.addResponseHeader("Connection", "close").removeTransferEncodingChunked()
				// .send(Mono.just(responseBuffer));
				.sendFile(responseFile);
		return result;
	}

	private  Publisher<Void> sendErrorNotFound(HttpChannel channel) {
		 log.info("Resource not found: " + channel.uri());
		 return channel.status(404).sendString(Mono.just("Client Error 404: Resource not found: " 
				 + channel.uri()), Charset.forName("UTF-8"));
	}
	
	private Publisher<Void> getWebSocketHandler(HttpChannel channel) {
		System.out.println("Connected a websocket client: " + channel.remoteAddress());
		
		return channel.flushEach().upgradeToWebsocket()
			.then(() -> {
				channel.receiveString().doOnNext(System.out::println)
				.subscribe(	json -> {
					RelativeMovement relativeMovement = gson.fromJson(json, RelativeMovement.class);
					Command command = new Command(MOVE_RELATIVE, relativeMovement);
					System.out.printf(">>>>>>>>>>>>> %s WS Command Received: %s%n", Thread.currentThread(), command);				
					movementCommands.onNext(command);
				});

				return positions
					.doOnNext(System.out::println)
					.map( position -> gson.toJson(position) )
					.as(positionString -> 
						channel.sendString(positionString, UTF_8)
					);
			});
	}
	
	protected String findMyInetAddress() throws SocketException {
		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		while(netInterfaces.hasMoreElements())
		{
		    NetworkInterface n = netInterfaces.nextElement();
		    Enumeration<InetAddress> ee = n.getInetAddresses();
		    while (ee.hasMoreElements())
		    {
		        InetAddress ip = ee.nextElement();
		        if(ip.getCanonicalHostName().matches(IP_V4_REGEX)) {
		        	System.out.println(ip.getHostAddress() + " : " + ip.getCanonicalHostName());
		        	return ip.getHostAddress();
		        }
		    }
		}
		return DEFAULT_IP_ADDRESS;
	}


}