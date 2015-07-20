package some.domain;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.apache.log4j.Logger;
import some.domain.handler.ServerHandler;

public class HttpServer  extends ChannelInitializer<SocketChannel> {
    private static final Logger log = Logger.getLogger(HttpServer.class);


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        String ip = socketChannel.remoteAddress().getHostString();
        log.info("ip address is: " + ip);

        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("handler", new ServerHandler(ip));
    }
}
