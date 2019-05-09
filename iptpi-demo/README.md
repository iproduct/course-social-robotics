# Reactive Java Robotics and IoT Demo

Demo presents ent-to-end reactive hot event stream processing of IPTPI robot sensor events using Spring Reactor library from server side and WebSocket with Angular 2 (TypeScript) and RxJS (RxNext) from client side. No web server needed - all resources as well as live WebSocket events are served by custom reactive HTTP endpoint using Reactor Net.

The main application component class AppComponent is in src/main/webapp/app folder, together with custom reactive WebSocket implementation - IPTRxWebSocketSubject component. IPTRxWebSocketSubject is exposing WebSocket as RxJS bidirectional Subject (by idea from RxDOM), plus reactive WebSocket open and close observers.

There are two types of clients - embedded client using Java Swing (whole screen mode) and mobile web client using Angular 2 (TypereScript) and RxJS:

The emebedded Swing client is in class RobotView (https://github.com/iproduct/jprime-demo/blob/master/iptpi-demo/src/main/java/org/iproduct/iptpi/demo/view/RobotView.java)

The web client (ng2 + RxJS) is in src/main/webapp folder (https://github.com/iproduct/jprime-demo/tree/master/iptpi-demo/src/main/webapp)

The main Angular 2 application component class AppComponent is in src/main/webapp/app folder, together with custom reactive WebSocket implementation - IPTRxWebSocketSubject component. IPTRxWebSocketSubject is exposing WebSocket as RxJS bidirectional subject (by idea from RxDOM), plus reactive WebSocket open and close observers.

Server side is implemented using Reactor (http://projectreactor.io/) project. Main main class is org.iproduct.iptpi.demo.IPTPIVoxxedDemo in src/main/java folder. The network communication is implemented in class org.iproduct.iptpi.demo.net.PositionsWsService. It serves all resources required by the client using Reactor Net (Netty), and could be started as console application - no webserver required. WebSocket bi-directional communication is implemented in a reactive (and compact) way in getWsHandler() method.
