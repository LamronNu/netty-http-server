package some.domain.model;

import org.joda.time.DateTime;

import java.util.*;

public class ServerStatistics {


    private DateTime startOn;

    private Queue<ClientRequest> requests;
    private Set<Client> clients;
    private Set<ClientRequest> uniqueRequests;
    private Map<ClientRequest, Integer> uniqueRedirects;
    private static volatile int openConnections;

    private static ServerStatistics instance;

    private ServerStatistics() {
        this.startOn = new DateTime();
        this.requests = new LinkedList<>();
        this.clients = new HashSet<>();
        this.uniqueRequests = new HashSet<>();
        openConnections = 0;
        this.uniqueRedirects = new HashMap<>();
    }

    public static ServerStatistics getInstance() {
        synchronized (ServerStatistics.class){
            if (instance == null){
                instance = new ServerStatistics();
            }
        }
        return instance;
    }

    public DateTime getStartOn() {
        return startOn;
    }

    public Queue<ClientRequest> getRequests(){
        return requests;
    }

    public void addRequestInfo(String ip, ClientRequest request){
        Client client = getClientByIp(ip);
        client.addRequest(request);
        request.setClient(client);
        request.fixTimeSpent();
        requests.add(request);
        boolean isNewRequest = uniqueRequests.add(request);
        if (request.existsRedirect()) {
            Integer count = isNewRequest ? 1 : uniqueRedirects.get(request) + 1;
            uniqueRedirects.put(request, count);
        }
    }

    private Client getClientByIp(String ip) {
        for (Client client : clients){
            if (client.getIp().equals(ip)){
                return client;
            }
        }
        Client newClient = new Client(ip);
        clients.add(newClient);
        return newClient;
    }

    public Set<ClientRequest> getUniqueRequests() {
        return uniqueRequests;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public Map<ClientRequest, Integer> getUniqueRedirects() {
        return uniqueRedirects;
    }

    public synchronized int getOpenConnections() {
        return openConnections;
    }

    public static synchronized void incConn() {
        openConnections++;
    }

    public static synchronized void decrConn() {
        openConnections--;
    }
}
