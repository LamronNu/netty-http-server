package some.domain.handler;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import some.domain.model.Client;
import some.domain.model.ClientRequest;
import some.domain.model.ServerStatistics;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Pages {
    private static final Logger log = Logger.getLogger(Pages.class);

    public static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm";
    public static final int LIMIT_PROCESSED_CONNECTIONS = 16;
    //pages
    public static final String INDEX_PAGE = "Welcome!";
    public static final String HELLO_PAGE = "hello";
    public static final String INFO_PAGE = "About server";
    public static final String STATISTICS_PAGE = "Server statistics";
    public static final String NOT_FOUND_PAGE = "404. Not Found";
    //templates
    public static final String HELLO_TEMPLATE = "Hello, World!";
    public static final String NOT_FOUND_TEMPLATE = "404. Not found.";
    public static final String INFO_TEMPLATE = getInfoPage();
    public static final String INDEX_TEMPLATE = getIndexPage();



    public static String htmlTemplate(String pageName){
        return (new StringBuilder("<!DOCTYPE html><html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n")
                .append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css\">\n")
                .append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css\">\n")
                .append("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js\"></script>\n")
                .append("<title>")
        //title
                .append(pageName)
                .append("</title>\n")
                .append("</head>\n")
                        //body-header
                .append("<body>\n<div class=\"container\">\n <nav class=\"navbar navbar-default\"><div class=\"container-fluid\">\n")
                .append("            <div class=\"navbar-header\">\n")
                .append("                <button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" data-target=\"#navbar\"\n")
                .append("                        aria-expanded=\"false\" aria-controls=\"navbar\">\n")
                .append("                    <span class=\"sr-only\">Toggle navigation</span>\n")
                .append("                    <span class=\"icon-bar\"></span>\n")
                .append("                    <span class=\"icon-bar\"></span>\n")
                .append("                    <span class=\"icon-bar\"></span>\n")
                .append("                </button>\n")
                .append("                <a class=\"navbar-brand\" href=\"/\">HttpServer</a>\n")
                .append("            </div>\n")
                .append("            <div id=\"navbar\" class=\"navbar-collapse collapse\">\n" )
                .append("                <ul class=\"nav navbar-nav\">\n")
        //index, home
                .append("                    <li class=\"")
                .append(INDEX_PAGE.equals(pageName) ? "active" : "")
                .append("\">\n")
                .append("                        <a href=\"/\">Home</a></li>\n")
        //hello
                .append("                    <li class=\"")
                .append(HELLO_PAGE.equals(pageName) ? "active" : "")
                .append("\">\n")
                .append("                        <a href=\"/hello\">Hello</a></li>\n")
        //info
                .append("                    <li class=\"")
                .append(INFO_PAGE.equals(pageName) ? "active" : "")
                .append("\">\n")
                .append("                        <a href=\"/info\">About</a></li>\n")
        //statistics
                .append("                    <li class=\"")
                .append(STATISTICS_PAGE.equals(pageName) ? "active" : "")
                .append("\">\n")
                .append("                        <a href=\"/status\">Statistics</a></li>\n")
                .append("\n")
                .append("                </ul>\n")
                .append("            </div>\n" )
                .append("        </div>" )
        //body-content
                .append( "</nav>%body%\n</body>\n")
                .append("<br/>\n")
                        //footer
                .append("<div style=\"text-align: center;\"><b>&copy; Olga. </b>See my ")
//                .append("<br/>")
                .append("<a href=/redirect?url=")
                .append("www.github.com/LamronNu")
                .append(" target=_blank> Github profile </a>\n")
                .append("<br/>")
                .append("</div>\n</div></html>\\n")
        ).toString();
    }

    public static String getIndexPage() {
        return (new StringBuilder("<h1>Welcome to the simple http-server!</h1><br/>\n")
                .append("<div class=\"list-group\"><b>Go to... </b>")
                        //links
                .append("<a href=\"/info\" class=\"list-group-item\">Info page</a>")
                .append("<a href=\"/hello\" class=\"list-group-item\">Helloworld page</a>")
                .append("<a href=\"/status\" class=\"list-group-item\">Statistics page</a>")
                .append("</div>")
                .append("<br/>")
                        //redirect
                .append("<b>Redirect to...</b>")
                .append("<div class=\"list-group\">\n")
                .append("<a href=\"/redirect?url=www.github.com/LamronNu/netty-http-server\" class=\"list-group-item\" target=_blank>Github source</a>")
                .append("<a href=\"/redirect?url=www.github.com/LamronNu\"\" class=\"list-group-item\" target=_blank>Github profile</a>")
                .append("<a href=\"/redirect?url=www.linkedin.com/profile/view?id=226275019&locale=en_US\" class=\"list-group-item\" target=_blank>LinkedIn profile</a>")
                .append("<a href=\"/redirect?url=www.hamstercoders.com/\" class=\"list-group-item\" target=_blank>HamsterCoders</a>")
                .append("</div>")
                .append("<br/>\n")
        ).toString();
    }

    public static String getInfoPage()  {

        String result = (new StringBuilder("<h1>About this http-server</h1><br/>")
                .append("<div class=\"container\">\n")
                .append("<h3>Task</h3>\n")
                .append("    <p align=\"justify\">Необходимо реализовать http-сервер на фреймворке\n")
                .append("        <a href=\"redirect?url=www.netty.io/\" target=\"_blank\">Netty</a>, со следующим функционалом:</p>\n")
                .append("    <ol>\n")
                .append("        <li>По запросу на <i>http://somedomain/hello</i> отдает «Hello World» через 10 секунд</li>\n")
                .append("        <li>По запросу на <i>http://somedomain/redirect?url=[url]</i> происходит переадресация на указанный url</li>\n")
                .append("        <li>По запросу на <i>http://somedomain/status</i> выдается статистика\n")
                .append("            <ul>\n")
                .append("                <li> общее количество запросов  </li>\n")
                .append("                <li>количество уникальных запросов (по одному на IP)</li>\n")
                .append("                <li>счетчик запросов на каждый IP в виде таблицы с колонкам и IP,\n")
                .append("                    кол-во запросов, время последнего запроса</li>\n")
                .append("                <li>количество переадресаций по url'ам  в виде таблицы, с колонками\n")
                .append("                    url, кол-во переадресация</li>\n")
                .append("                <li>количество соединений, открытых в данный момент</li>\n")
                .append("                <li>в виде таблицы лог из 16 последних обработанных соединений, колонки\n")
                .append("                    src_ip, URI, timestamp,  sent_bytes, received_bytes, speed (bytes/sec)</li>\n")
                .append("            </ul>\n")
                .append("        </li>\n")
                .append("    </ol>\n")
                .append("    <p>\n")
                .append("        Все это (вместе с особенностями имплементации в текстовом виде)\n")
                .append("        выложить на github, приложить к этому:\n")
                .append("        <ul>\n")
                .append("            <li>скриншоты как выглядят станицы /status в рабочем приложении</li>\n")
                .append("            <li>скриншот результата выполнения команды <b>ab – c 100 – n 10000\n")
                .append("                http://somedomain/status</b> (примечaние:\n")
                .append("                <a href=\"redirect?url=httpd.apache.org/docs/2.2/programs/ab.html\" target=\"_blank\">ab</a>)</li>\n")
                .append("            <li>еще один скриншот станицы /status, но уже после выполнение команды\n")
                .append("                <b>ab</b> из предыдущего пункта</li>\n")
                .append("        </ul>\n")
                .append("    </p>\n")
                .append("    <p>\n")
                .append("        Комментарии:\n")
                .append("        <ul>\n")
                .append("            <li>использовать самую последнюю стабильную версию netty</li>\n")
                .append("            <li>обратить внимание на многопоточность</li>\n")
                .append("            <li>разобраться в EventLoop’ами netty</li>\n")
                .append("            <li>приложение должно собираться Maven'ом</li>\n")
                .append("            <li>все файлы должны быть в UTF8, перенос строки \\n</li>\n")
                .append("        </ul>\n")
                .append("    </p>\n")
                .append("    <h3>Реализация</h3>\n")
                .append("    <p>\n")
                .append("        Описана в wiki \n")
                .append("        <a href=\"/redirect?url=www.github.com/LamronNu/netty-http-server\" target=\"_blank\">исходного кода</a> на гитхабе: \n")
                .append("        <a href=\"/redirect?url=www.github.com/LamronNu/netty-http-server/wiki/2.-Implementation\" target=\"_blank\">тут</a> (реализация)\n")
                .append("        и <a href=\"/redirect?url=www.github.com/LamronNu/netty-http-server/wiki/3.-Screenshots\" target=\"_blank\">тут</a> (скриншоты).\n")
                .append("    </p>\n")
                .append("</div>")
        )
                    .toString();
        return result;
    }

    public static String getStatisticTable() {
        ServerStatistics statistics = ServerStatistics.getInstance();

        StringBuilder result = new StringBuilder("<h1>Server Statistics</h1><br/>\n")
                .append("<br/>\n")
                .append("<p><b>Server starts on: </b>\n")
                .append(formatDateTime(statistics.getStartOn()))
                .append("</p>\n")
        /* - общее количество запросов*/
                .append("<p><b>Total request`s count: </b>\n")
                .append(statistics.getRequests().size())
                .append("</p>")/* - количество уникальных запросов (по одному на IP)*/
                .append("<p><b>Total unique request`s count: </b>\n")
                .append(statistics.getUniqueRequests().size())
                .append("</p>\n")
        /* - количество соединений, открытых в данный момент*/
                .append("<p><b>Open connections: </b>\n")
                .append(statistics.getOpenConnections())//.size())//???
                .append("</p>\n")
        /* - счетчик запросов на каждый IP в виде таблицы с колонкам и IP,
             кол-во запросов, время последнего запроса*/
                .append("<p>Table 1. <b>Request`s details table: </b>\n")
                .append("</p>\n")
                .append("<table class=\"table table-bordered table-hover table-striped\"><tr>")
                .append("<th>#</th><th>IP</th><th>requests</th><th>unique requests</th><th>last date</th></tr>\n")
                ;       //---1---------2-------------3------------------4-------------------5-----
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
                    .append(formatDateTime(client.getLastDateTime()))//5
                    .append("</td>\n</tr>");
        }
        result.append("</table>\n")
        /* - количество переадресаций по url'ам  в виде таблицы, с колонками
            url, кол-во переадресация*/
                .append("<p>Table 2. <b>Redirect`s details table: </b>\n")
                .append("</p>\n")
                .append("<table class=\"table table-bordered table-hover table-striped\"><tr>")
                .append("<th>#</th><th>redirect to</th><th>count</th><th>last date</th></tr>\n")
        ;                //--1---------------2---------------3------------------4------------
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
                    .append(formatDateTime(redirect.getDateTime()))//4
                    .append("</td>\n</tr>");
        }
        result.append("</table>\n")

        /* - в виде таблицы лог из 16 последних обработанных соединений, колонки
            src_ip, URI, timestamp,  sent_bytes, received_bytes, speed (bytes/sec)        */
                .append("<p>Table 3. <b>Log of last 16 requests: </b>\n")
                .append("</p>\n")                                                           //--1----------2----------3------------------4------------
                .append("<table class=\"table table-bordered table-hover table-striped\"><tr><th>#</th><th>ip</th><th>uri</th><th>date-time</th>")
                .append("<th>response</th><th>send bytes</th><th>receive bytes</th><th>time spent, sec</th><th>speed, bytes/sec</th></tr>\n")
        ;                    //--5---------------6-------------------7----------------------8------------------------9----------
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
                    .append(formatDateTime(request.getDateTime()))//4
                    .append("</td>\n<td>")
                    .append(request.getResponse()) //5
                    .append("</td>\n<td>")
                    .append(request.getSendBytes()) //6
                    .append("</td>\n<td>")
                    .append(request.getReceivedBytes())  //7
                    .append("</td>\n<td>")
                    .append(formatTime(request.getTimeSpent()))//8
                    .append("</td>\n<td>")
                    .append(String.format("%.2f", request.getSpeed()))//9
                    .append("</td>\n</tr>");

            if (i >= LIMIT_PROCESSED_CONNECTIONS)
                break;
        }

        result.append("</table>\n");

        return result.toString();

    }

    private static String formatTime(Long timeSpent) {
        return String.format("%.3f",(Double) (timeSpent / 1000.));
    }

    private static String formatDateTime(DateTime dateTime) {
        return dateTime.toDateTime(DateTimeZone.forOffsetHours(3)).toString(DATE_TIME_PATTERN);
    }
}
