package some.domain.handler;

import some.domain.model.Client;
import some.domain.model.ClientRequest;
import some.domain.model.ServerStatistics;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Pages {
    public static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm";
    public static final int LIMIT_PROCESSED_CONNECTIONS = 16;

    public static final String HELLO_TEMPLATE = "Hello, World!";
    public static final String NOT_FOUND_TEMPLATE = "404. Not found.";
    public static final String INDEX_PAGE = "Welcome!";
    public static final String HELLO_PAGE = "hello";
    public static final String INFO_PAGE = "About app";
    public static final String STATISTICS_PAGE = "Server statistics";
    public static final String NOT_FOUND_PAGE = "404. Not Found";


    public static String htmlTemplate(String pageName){
        return (new StringBuilder("<!DOCTYPE html><html lang=\"en\">\n<head>\n")
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
                .append("> Github profile.</a>\n")
                .append("<br/>")
                .append("</div>\n</div></html>\\n")
        ).toString();
    }

    public static String getIndexPage() {
        StringBuilder result = new StringBuilder("<h1>Welcome to simple http-server!</h1><br/>\n")
                .append("<div class=\"list-group\"><b>Go to... </b>")
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

                //.append("</div>\n")
                ;


        return result.toString();
    }

    public static String getInfoPage() {
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
                .append("</div>");
        return result.toString();
    }

    public static String getStatisticTable() {
        ServerStatistics statistics = ServerStatistics.getInstance();

        StringBuilder result = new StringBuilder("<h1>Server Statistics</h1><br/>\n")
                .append("<br/>\n")
                .append("<p><b>Server starts on: </b>\n")
                .append(statistics.getStartOn().toLocalDateTime().toString(DATE_TIME_PATTERN))
                .append("</p>\n")
//                .append("<hr>\n")
        /* - ����� ���������� ��������*/
                .append("<p><b>Total request`s count: </b>\n")
                .append(statistics.getRequests().size())
                .append("</p>")/* - ���������� ���������� �������� (�� ������ �� IP)*/
                .append("<p><b>Total unique request`s count: </b>\n")
                .append(statistics.getUniqueRequests().size())
                .append("</p>\n")
//                .append("<hr>\n")
        /* - ������� �������� �� ������ IP � ���� ������� � �������� � IP,
             ���-�� ��������, ����� ���������� �������*/
                .append("<p><b>Request`s details table: </b>\n")
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
                    .append(client.getLastDateTime().toString(DATE_TIME_PATTERN))//5
                    .append("</td>\n</tr>");
        }
        result.append("</table>\n")
//                .append("<hr>\n")
        /* - ���������� ������������� �� url'��  � ���� �������, � ���������
            url, ���-�� �������������*/
                .append("<p><b>Redirect`s details table: </b>\n")
                .append("</p>\n")     //--1---------------2---------------3------------------4------------
                .append("<table class=\"table table-bordered table-hover table-striped\"><tr><th>#</th><th>redirect to</th><th>count</th><th>last date</th></tr>\n")
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
                    .append(redirect.getDateTime().toLocalDateTime().toString(DATE_TIME_PATTERN))//4
                    .append("</td>\n</tr>");
        }
        result.append("</table>\n")
//                .append("<hr>\n")
        /* - ���������� ����������, �������� � ������ ������*/
                .append("<p><b>Open connections: </b>\n")
                .append(statistics.getOpenConnections().size())//???
                .append("</p>\n");
//                .append("<hr>\n");
        /* - � ���� ������� ��� �� 16 ��������� ������������ ����������, �������
            src_ip, URI, timestamp,  sent_bytes, received_bytes, speed (bytes/sec)        */
        result.append("<p><b>Redirect`s details table: </b>\n")
                .append("</p>\n")     //--1----------2----------3------------------4------------
                .append("<table class=\"table table-bordered table-hover table-striped\"><tr><th>#</th><th>ip</th><th>uri</th><th>date-time</th>")
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
                    .append(request.getDateTime().toString(DATE_TIME_PATTERN))//4
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

        result.append("</table>\n");

        return result.toString();

    }
}