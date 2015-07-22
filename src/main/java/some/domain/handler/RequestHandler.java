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

    public static final String HTML_TEMPLATE = "<html>\n<body>\n%body%\n</body>\n</html>\n";
    public static final String HELLO_TEMPLATE = "Hello, World!";
    public static final String NOT_FOUND_TEMPLATE = "404. Not found.";
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
            case"/":
            case "/index":
                response = getIndexPageResponse();
                break;
            case "/info":
                response = getInfoPageResponse();
                break;
            case"/hello":
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

    private FullHttpResponse getInfoPageResponse() {
        String table = getHtmlResponse(getInfoPage());
        log.info("response is: info page" );
        log.info("--------------------");

        return new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(table, CharsetUtil.US_ASCII));
    }

    private String getInfoPage() {
        StringBuilder result = new StringBuilder("<h1>About this http-server</h1><br/>")
                .append("<br/>")
                .append("<div><b>todo... </b>")
                .append("<br/>")
//                .append("<a href=\"/info\">Info page</a>")
//                .append("<br/>")
//                .append("<a href=\"/hello\">Helloworld page</a>")
//                .append("<br/>")
//                .append("<a href=\"/redirect\">Redirect page</a>")
//                .append("<br/>")
//                .append("<a href=\"/status\">Statistics page</a>")
                .append("</div>")
                .append(footer());
        return result.toString();
    }

    private FullHttpResponse getIndexPageResponse() {
        String table = getHtmlResponse(getIndexPage());
        log.info("response is: index page" );
        log.info("--------------------");

        return new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(table, CharsetUtil.US_ASCII));
    }

    private String getIndexPage() {
        StringBuilder result = new StringBuilder("<h1>Welcome to simple http-server!</h1><br/>\n")
                .append("<br/>")
                .append("<div><b>Go to... </b>")
                .append("<br/>\n")
                .append("<a href=\"/info\">Info page</a>")
                .append("<br/>\n")
                .append("<a href=\"/hello\">Helloworld page</a>")
                .append("<br/>\n")
                .append("<a href=\"/redirect\">Redirect page</a>")
                .append("<br/>\n")
                .append("<a href=\"/status\">Statistics page</a>")
                .append("</div>\n")
                .append(footer());

                /*<div id="main" ng-app>
    <!-- Всем переменным классов в меню навигации будет присвоено значение "active". Функция $event.preventDefault() выводит страницу, которая была открыта по ссылке. -->
    <nav class="{{active}}" ng-click="$event.preventDefault()">
        <!-- Когда пункт меню открыт по ссылке, мы устанавливаем активные переменные -->
        <a href="#" class="home" ng-click="active='home'">Home</a>
        <a href="#" class="projects" ng-click="active='projects'">Projects</a>
        <a href="#" class="services" ng-click="active='services'">Services</a>
        <a href="#" class="contact" ng-click="active='contact'">Contact</a>
    </nav>
    <!-- ng-show выводит элемент, если значение переменной в кавычках соответствует истине. ng- hide – скрывает элемент, если наоборот. Так как изначально  активная переменная не установлена, то сперва на экране будет виден следующий текст -->
    <p ng-hide="active">Please click a menu item</p>
    <p ng-show="active">You chose <b>{{active}}</b></p>
</div>*/
        return result.toString();
    }

    private String footer() {
        return (new StringBuilder("<hr><div id=\"footer\" align='bottom'")
                .append("<br/>\n")
                .append("<div><b>&copy; Olga. </b>See my ")
//                .append("<br/>")
                .append("<a href=/redirect?url=")
                .append("www.github.com/LamronNu")
                .append("> Github profile.</a>\n")
                .append("<br/>")
                .append("</div>\n")
        ).toString();
    }

    private FullHttpResponse getStatisticResponse() {
        String table = getHtmlResponse(getStatisticTable());
        log.info("response is: table of statistics" );
        log.info("--------------------");

        return new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(table, CharsetUtil.US_ASCII));
    }

    private String getStatisticTable() {
        ServerStatistics statistics = ServerStatistics.getInstance();

        StringBuilder result = new StringBuilder("<h1>Server Statistics</h1><br/>\n")
                .append("<br/>\n")
                .append("<p><b>Server starts on: </b>\n")
                .append(statistics.getStartOn())
                .append("</p>\n")
                .append("<hr>\n")
        /* - общее количество запросов*/
                .append("<p><b>Total request`s count: </b>\n")
                .append(statistics.getRequests().size())
                .append("</p>")/* - количество уникальных запросов (по одному на IP)*/
                .append("<p><b>Total unique request`s count: </b>\n")
                .append(statistics.getUniqueRequests().size())
                .append("</p>\n")
                .append("<hr>\n")
        /* - счетчик запросов на каждый IP в виде таблицы с колонкам и IP,
             кол-во запросов, время последнего запроса*/
                .append("<p><b>Request`s details table: </b>\n")
                .append("</p>\n")     //--1---------2-------------3------------------4-------------------5-----
                .append("<table><tr><th>#</th><th>IP</th><th>requests</th><th>unique requests</th><th>last date</th></tr>\n")
                ;
        int i = 0;
        for (Client client : statistics.getClients()){
            i++;
            result.append("<tr>\n<td>")
                    .append(i)              //1
                    .append("</td>\n<td>")
                    .append(client.getIp()) //2
                    .append("</td>\n<td>")
                    .append(client.getRequestsCount())  //3
                    .append("</td>\n<td>")
                    .append(client.getUniqueRequestsCount())//4
                    .append("</td>\n<td>")
                    .append(client.getLastDateTime())//5
                    .append("</td>\n</tr>");
        }
        result.append("</table>\n")
                .append("<hr>\n")
        /* - количество переадресаций по url'ам  в виде таблицы, с колонками
            url, кол-во переадресация*/
                .append("<p><b>Redirect`s details table: </b>\n")
                .append("</p>\n")     //--1---------------2---------------3------------------4------------
                .append("<table><tr><th>#</th><th>redirect to</th><th>count</th><th>last date</th></tr>\n")
        ;
        i = 0;
        for (ClientRequest redirect : statistics.getUniqueRedirects().keySet()){
            i++;
            result.append("<tr>\n<td>")
                    .append(i)              //1
                    .append("</td>\n<td>")
                    .append(redirect.getRedirectTo()) //2
                    .append("</td>\n<td>")
                    .append(statistics.getUniqueRedirects().get(redirect))  //3
                    .append("</td>\n<td>")
                    .append(redirect.getDateTime())//4
                    .append("</td>\n</tr>");
        }
        result.append("</table>\n")
                .append("<hr>\n")
        /* - количество соединений, открытых в данный момент*/
                .append("<p><b>Open connections: </b>\n")
                .append(statistics.getOpenConnections().size())//???
                .append("</p>\n")
                .append("<hr>\n");
        /* - в виде таблицы лог из 16 последних обработанных соединений, колонки
            src_ip, URI, timestamp,  sent_bytes, received_bytes, speed (bytes/sec)        */
        result.append("<p><b>Redirect`s details table: </b>\n")
                .append("</p>\n")     //--1----------2----------3------------------4------------
                .append("<table><tr><th>#</th><th>ip</th><th>uri</th><th>date-time</th>")
                .append("<th>send bytes</th><th>receive bytes</th><th>speed</th></tr>\n")
        ;                       //--5---------------6-------------------7------------
        i = 0;
        List<ClientRequest> reverseRequests = new LinkedList<>(statistics.getRequests());
        Collections.reverse(reverseRequests);
        for (ClientRequest request : reverseRequests ){
            i++;
            result.append("<tr>\n<td>")
                    .append(i)              //1
                    .append("</td>\n<td>")
                    .append(request.getClient().getIp()) //2
                    .append("</td>\n<td>")
                    .append(request.getFullUrl())  //3
                    .append("</td>\n<td>")
                    .append(request.getDateTime())//4
                    .append("</td>\n<td>")
                    .append(request.getSendBytes()) //5
                    .append("</td>\n<td>")
                    .append(request.getReceivedBytes())  //6
                    .append("</td>\n<td>")
                    .append(request.getSpeed())//7
                    .append("</td>\n</tr>");
            if (i >= LIMIT_PROCESSED_CONNECTIONS)
                break;
        }

        result.append("</table>\n")
                .append(footer());

        return result.toString();

     }

    private String getHtmlResponse(String body){
        return HTML_TEMPLATE.replaceAll("%body%", body);
    }

    private FullHttpResponse getHelloWorldResponse() throws InterruptedException {
        String hello = getHtmlResponse(HELLO_TEMPLATE + footer());
        log.info("response is "+ HELLO_TEMPLATE );
        log.info("sleep during " + COUNT_MS + " sec ...");

        Thread.sleep(COUNT_MS * 1000);
        log.info("--------------------");

        return new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(hello, CharsetUtil.US_ASCII));
    }

    private FullHttpResponse getNotFoundResponse() {
        String hello = getHtmlResponse(NOT_FOUND_TEMPLATE + footer());
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
