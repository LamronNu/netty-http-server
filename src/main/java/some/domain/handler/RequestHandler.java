package some.domain.handler;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class RequestHandler {
    private static final Logger log = Logger.getLogger(RequestHandler.class);
    public static final String HTML_TEMPLATE = "<html><body>%body%</body></html>";
    public static final String HELLO_TEMPLATE = "Hello, World!";
    public static final String NOT_FOUND_TEMPLATE = "404. Not found.";
    public static final String RESPONSE_IS = "response is: ";
    public static final int COUNT_MS = 10;



    public FullHttpResponse getResponse(String url) throws InterruptedException {
        log.info("uri=" + url);
        FullHttpResponse response = null;
        String extractUri = url.indexOf("?") > 0 ? url.substring(0, url.indexOf("?")) : url;
        log.info("extractUri=" + extractUri);

        switch (extractUri){
            case"/hello":
            case"/helloworld":
                response = getHelloWorldResponse();
                break;
            case "/redirect":///redirect?url=<url>
                response = getRedirectResponse("http://" + url.replace("/redirect?url=",""));
                break;
            default:
                response = getNotFoundResponse();
        }


        return response;
    }

    private String getHtmlResponse(String body){
        return HTML_TEMPLATE.replaceAll("%body%", body);
    }

    private FullHttpResponse getHelloWorldResponse() throws InterruptedException {
        String hello = getHtmlResponse(HELLO_TEMPLATE);
        log.info("response is "+ HELLO_TEMPLATE );
        log.info("sleep during " + COUNT_MS + " sec ...");

        Thread.sleep(COUNT_MS * 1000);
        log.info("--------------------");

        return new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(hello, CharsetUtil.US_ASCII));
    }

    private FullHttpResponse getNotFoundResponse() {
        String hello = getHtmlResponse(NOT_FOUND_TEMPLATE);
        log.info("response is "+ NOT_FOUND_TEMPLATE );
        log.info("--------------------");

        return new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND,
                Unpooled.copiedBuffer(hello, CharsetUtil.US_ASCII));
    }

    private FullHttpResponse getRedirectResponse(String url) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        response.headers().set(HttpHeaders.Names.LOCATION, url);
        log.info("redirect to... " + url );
        log.info("--------------------");

        return response;

    }


}
