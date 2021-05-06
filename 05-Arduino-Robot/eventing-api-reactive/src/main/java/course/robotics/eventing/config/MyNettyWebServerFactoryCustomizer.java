package course.robotics.eventing.config;

import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import reactor.netty.ChannelPipelineConfigurer;
import reactor.netty.ConnectionObserver;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.server.HttpServer;
import reactor.util.annotation.Nullable;

import java.net.*;
import java.time.Duration;

@Component
@Slf4j
public class MyNettyWebServerFactoryCustomizer implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    @SneakyThrows
    @Override
    public void customize(NettyReactiveWebServerFactory serverFactory) {
//        serverFactory.setLifecycleTimeout(Duration.ofMillis(5000));
        serverFactory.addServerCustomizers(new EventLoopNettyCustomizer());
    }

    private static class EventLoopNettyCustomizer implements NettyServerCustomizer {

        @Override
        public HttpServer apply(HttpServer httpServer) {
//            return httpServer
//                    .idleTimeout(Duration.ofMillis(5000))
//                    .bindAddress(() -> {
//                        try {
//                            return new InetSocketAddress(Inet4Address.getByAddress(
//                                    new byte[]{(byte)192, (byte)168, 1, 101}), 8080);
//                        } catch (UnknownHostException e) {
//                            e.printStackTrace();
//                        }
//                        return null;
//                    })
//                    .protocol(HttpProtocol.HTTP11)
////                    .channelGroup(new DefaultChannelGroup(GlobalEventExecutor.INSTANCE).)
//                    .wiretap("MyNettyHttpServer", LogLevel.INFO);
            return httpServer.tcpConfiguration(tcpServer -> tcpServer
                    .doOnBound(conf -> {
                        log.info("!!!!! Server bound to: " + conf.address());
                    }));
//                    .wiretap("MyNettyHttpServer", LogLevel.INFO));
//                    .doOnChannelInit((ConnectionObserver connectionObserver, Channel channel, @Nullable SocketAddress remoteAddress) -> {
//                        log.info("!!! Connected client: " + remoteAddress);
//                        channel.config().setOption(ChannelOption.SO_TIMEOUT, 10000);
//                    }));
//                    .currentContext().group(parentGroup, childGroup)
//                    .channel(NioServerSocketChannel.class)));
        }
    }
}
