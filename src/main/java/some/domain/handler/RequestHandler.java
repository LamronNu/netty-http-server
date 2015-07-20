package some.domain.handler;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import some.domain.model.Client;
import some.domain.model.ClientRequest;
import some.domain.model.ServerStatistics;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class RequestHandler {
    private static final Logger log = Logger.getLogger(RequestHandler.class);
    public static final int LIMIT_PROCESSED_CONNECTIONS = 16;
    private String ip;

    public static final String HTML_TEMPLATE = "<html><body>%body%</body></html>";
    public static final String HELLO_TEMPLATE = "Hello, World!";
    public static final String NOT_FOUND_TEMPLATE = "404. Not found.";
    public static final String RESPONSE_IS = "response is: ";
    public static final int COUNT_MS = 10;

    public RequestHandler(String ip) {
        this.ip = ip;
    }

    public FullHttpResponse getResponse(String url, int receiveBytes) throws InterruptedException {
        log.info("uri=" + url);
        //prepare request obj
        ClientRequest request = new ClientRequest(url);
        request.setReceivedBytes(receiveBytes);

        FullHttpResponse response = null;
        String extractUri = url.indexOf("?") > 0 ? url.substring(0, url.indexOf("?")) : url;
        log.info("extractUri=" + extractUri);

        switch (extractUri){
            case"/hello":
            case"/helloworld":
                response = getHelloWorldResponse();
                break;
            case "/redirect":///redirect?url=<url>
                String redirectTo = "http://" + url.replace("/redirect?url=", "");
                request.setRedirectTo(redirectTo);
                response = getRedirectResponse(redirectTo);
                break;
            case "/status"://statistics
                response = getStatisticResponse();
                break;
            default:
                response = getNotFoundResponse();
        }

        request.setSendBytes(response == null ? 0 : response.content().toString().length());//???
        ServerStatistics.getInstance().addRequestInfo(ip, request);
        return response;
    }

    private FullHttpResponse getStatisticResponse() {
        String table = getHtmlResponse(getStatisticTable());
        log.info("response is table of statistics" );
        log.info("--------------------");

        return new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(table, CharsetUtil.US_ASCII));
    }

    private String getStatisticTable() {
        ServerStatistics statistics = ServerStatistics.getInstance();

        StringBuilder result = new StringBuilder("<h1>Server Statistics</h1><br>/")
                .append("<br/>")
                .append("<p><b>Server starts on: </b>")
                .append(statistics.getStartOn())
                .append("</p>")
        /* - общее количество запросов*/
                .append("<p><b>Total request`s count: </b>")
                .append(statistics.getRequests().size())
                .append("</p>")/* - количество уникальных запросов (по одному на IP)*/
                .append("<p><b>Total unique request`s count: </b>")
                .append(statistics.getUniqueRequests().size())
                .append("</p>")
        /* - счетчик запросов на каждый IP в виде таблицы с колонкам и IP,
             кол-во запросов, время последнего запроса*/
                .append("<p><b>Request`s details table: </b>")
                .append("</p>")     //--1---------2-------------3------------------4-------------------5-----
                .append("<table><tr><th>#</th><th>IP</th><th>requests</th><th>unique requests</th><th>last date</th></tr>")
                ;
        int i = 0;
        for (Client client : statistics.getClients()){
            i++;
            result.append("<tr><td>")
                    .append(i)              //1
                    .append("</td><td>")
                    .append(client.getIp()) //2
                    .append("</td><td>")
                    .append(client.getRequestsCount())  //3
                    .append("</td><td>")
                    .append(client.getUniqueRequestsCount())//4
                    .append("</td><td>")
                    .append(client.getLastDateTime())//5
                    .append("</td></tr>");
        }
        result.append("</table>")
        /* - количество переадресаций по url'ам  в виде таблицы, с колонками
            url, кол-во переадресация*/
                .append("<p><b>Redirect`s details table: </b>")
                .append("</p>")     //--1---------------2---------------3------------------4------------
                .append("<table><tr><th>#</th><th>redirect to</th><th>count</th><th>last date</th></tr>")
        ;
        i = 0;
        for (ClientRequest redirect : statistics.getUniqueRedirects().keySet()){
            i++;
            result.append("<tr><td>")
                    .append(i)              //1
                    .append("</td><td>")
                    .append(redirect.getRedirectTo()) //2
                    .append("</td><td>")
                    .append(statistics.getUniqueRedirects().get(redirect))  //3
                    .append("</td><td>")
                    .append(redirect.getDateTime())//4
                    .append("</td></tr>");
        }
        result.append("</table>")
        /* - количество соединений, открытых в данный момент*/
                .append("<p><b>Open connections: </b>")
                .append(statistics.getOpenConnections().size())//???
                .append("</p>");
        /* - в виде таблицы лог из 16 последних обработанных соединений, колонки
            src_ip, URI, timestamp,  sent_bytes, received_bytes, speed (bytes/sec)        */
        result.append("<p><b>Redirect`s details table: </b>")
                .append("</p>")     //--1----------2----------3------------------4------------
                .append("<table><tr><th>#</th><th>ip</th><th>uri</th><th>date-time</th>")
                .append("<th>send bytes</th><th>receive bytes</th><th>speed</th></tr>")
        ;                       //--5---------------6-------------------7------------
        i = 0;
        List<ClientRequest> reverseRequests = new LinkedList<>(statistics.getRequests());
        Collections.reverse(reverseRequests);
        for (ClientRequest request : reverseRequests ){
            i++;
            result.append("<tr><td>")
                    .append(i)              //1
                    .append("</td><td>")
                    .append(request.getClient().getIp()) //2
                    .append("</td><td>")
                    .append(request.getFullUrl())  //3
                    .append("</td><td>")
                    .append(request.getDateTime())//4
                    .append("</td><td>")
                    .append(request.getSendBytes()) //5
                    .append("</td><td>")
                    .append(request.getReceivedBytes())  //6
                    .append("</td><td>")
                    .append(request.getSpeed())//7
                    .append("</td></tr>");
            if (i >= LIMIT_PROCESSED_CONNECTIONS)
                break;
        }

        result.append("</table>");

        return result.toString();

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
