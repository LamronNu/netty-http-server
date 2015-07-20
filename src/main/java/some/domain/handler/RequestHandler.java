package some.domain.handler;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class RequestHandler {
    private static final Logger log = Logger.getLogger(RequestHandler.class);
    public static final String HTML_TEMPLATE = "<html><body>%body%</body></html>";
    public static final String HELLO_TEMPLATE = "Hello, World!";

    private String getHtmlResponse(String body){
        return HTML_TEMPLATE.replaceAll("%body%", body);
    }

    public FullHttpResponse getResponse(String url) {
        String hello = getHtmlResponse(HELLO_TEMPLATE);

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(hello, CharsetUtil.US_ASCII));
        return response;
    }


}
