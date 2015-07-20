package some.domain.model;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.util.*;

public class Client {
    private static final Logger log = Logger.getLogger(Client.class);

    private String ip;
    private Queue<ClientRequest> requests;
    private final Set<ClientRequest> uniqueRequests;

    public Client(String ip) {
        this.ip = ip;
        this.requests = new LinkedList<>();
        this.uniqueRequests =  new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (!ip.equals(client.ip)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ip.hashCode();
    }

    public String getIp() {
        return ip;
    }

    public List<ClientRequest> getRequests() {
        return new LinkedList<>(requests);
    }

    public Set<ClientRequest> getUniqueRequests() {
        return new HashSet<>(uniqueRequests);
    }

    public int getRequestsCount(){
        return requests.size();
    }
    
    public int getUniqueRequestsCount(){
        return uniqueRequests.size();
    }
    
    public void addRequest(ClientRequest request){
        requests.add(request);
        uniqueRequests.add(request);
    }

    public DateTime getLastDateTime(){
        return requests.element().getDateTime();
    }

}
