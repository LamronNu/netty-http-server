package some.domain.handler;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import some.domain.model.ClientRequest;
import some.domain.model.ServerStatistics;

import java.nio.charset.Charset;

import static some.domain.handler.Pages.*;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class RequestHandler {
    private static final Logger log = Logger.getLogger(RequestHandler.class);
    public static final Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;
    public static final Charset CYRILLIC_CHARSET = Charset.forName("Cp1251");
    private String ip;
    public static final int COUNT_MS = 10;

    public RequestHandler(String ip) {
        this.ip = ip;
    }

    public FullHttpResponse getResponse(String url, int receiveBytes) throws InterruptedException {
        //prepare request obj
        ClientRequest request = new ClientRequest(url);
        log.info("uri=" + url);
        request.setReceivedBytes(receiveBytes);

        FullHttpResponse response = null;
        String extractUri = url.indexOf("?") > 0 ? url.substring(0, url.indexOf("?")) : url;
        log.info("extractUri=" + extractUri);

        switch (extractUri){
            case"/":
            case "/index":
                response = getIndexPageResponse();
                request.setResponse(INDEX_PAGE);
                break;
            case "/info":
                response = getInfoPageResponse();
                request.setResponse(INFO_PAGE);
                break;
            case"/hello":
                response = getHelloWorldResponse();
                break;
            case "/redirect":///redirect?url=<url>
                String redirectTo = "http://" + url.replace("/redirect?url=", "");
                request.setRedirectTo(redirectTo);
                response = getRedirectResponse(redirectTo);
                request.setResponse("redirect");
                break;
            case "/status"://statistics
                response = getStatisticResponse();
                request.setResponse(STATISTICS_PAGE);
                break;
            default:
                response = getNotFoundResponse();
                request.setResponse(NOT_FOUND_PAGE);
        }

        request.setSendBytes(response == null ? 0 : response.content().toString().length());//???
        ServerStatistics.getInstance().addRequestInfo(ip, request);
        return response;
    }

    private FullHttpResponse getInfoPageResponse() {
        String table = getHtmlResponse(getInfoPage(), INFO_PAGE);
        log.info("response is: info page" );
        log.info("--------------------");

        return new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(table, CYRILLIC_CHARSET));
    }

    private FullHttpResponse getIndexPageResponse() {
        String table = getHtmlResponse(getIndexPage(), INDEX_PAGE);
        log.info("response is: index page" );
        log.info("--------------------");

        return new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(table, DEFAULT_CHARSET));
    }

    private FullHttpResponse getStatisticResponse() {
        String table = getHtmlResponse(getStatisticTable(), STATISTICS_PAGE);
        log.info("response is: table of statistics" );
        log.info("--------------------");

        return new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(table, DEFAULT_CHARSET));
    }

    private String getHtmlResponse(String body, String pageName) {
        return htmlTemplate(pageName).replace("%body%", body);
    }

    private FullHttpResponse getHelloWorldResponse() throws InterruptedException {
        String hello = getHtmlResponse(HELLO_TEMPLATE, HELLO_PAGE);
        log.info("response is "+ HELLO_TEMPLATE );
        log.info("sleep during " + COUNT_MS + " sec ...");

        Thread.sleep(COUNT_MS * 1000);
        log.info("--------------------");

        return new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(hello, DEFAULT_CHARSET));
    }

    private FullHttpResponse getNotFoundResponse() {
        String hello = getHtmlResponse(NOT_FOUND_TEMPLATE, NOT_FOUND_PAGE);
        log.info("response is "+ NOT_FOUND_TEMPLATE );
        log.info("--------------------");

        return new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND,
                Unpooled.copiedBuffer(hello, DEFAULT_CHARSET));
    }

    private FullHttpResponse getRedirectResponse(String url) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        response.headers().set(HttpHeaders.Names.LOCATION, url);
        log.info("redirect to... " + url );
        log.info("--------------------");

        return response;

    }


}
