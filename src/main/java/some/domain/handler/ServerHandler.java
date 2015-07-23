package some.domain.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.log4j.Logger;
import some.domain.model.ServerStatistics;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = Logger.getLogger(ServerHandler.class);
    private final String ip;

    public ServerHandler(String ip) {
        this.ip = ip;
        log.info("--------------------");
        log.info("get request from ip: " + ip);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ServerStatistics.getInstance().incConn();

        if(!(msg instanceof HttpRequest))
            return;

        String uri = ((HttpRequest) msg).getUri();
        FullHttpResponse response = new RequestHandler(ip).getResponse(uri, msg.toString().length());

        if(response != null) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        }
        //
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        ServerStatistics.getInstance().decrConn();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.error("ex in handler!", cause);
        ctx.close();
        ServerStatistics.getInstance().decrConn();
    }
}
