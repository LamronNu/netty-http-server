package some.domain.model;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class ClientRequest {
    private static final Logger log = Logger.getLogger(ClientRequest.class);

    private String fullUrl;
    private DateTime dateTime;
    private String redirectTo;
    private Client client;



    //sent_bytes, received_bytes, speed (bytes/sec)
    private Integer receivedBytes;
    private Integer sendBytes;
    private Double speed;//???????

    public ClientRequest(String fullUrl) {
        if (fullUrl == null || "".equals(fullUrl))
            throw new IllegalArgumentException("url can't be empty!");
        init(fullUrl);
    }

    private void init(String fullUrl) {
        this.fullUrl = fullUrl;
        this.dateTime = new DateTime();//current time
        this.redirectTo = "";
        this.receivedBytes = 0;
        this.sendBytes = 0;
        this.speed = 0.;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientRequest that = (ClientRequest) o;

        //if (!dateTime.equals(that.dateTime)) return false;
        if (!fullUrl.equals(that.fullUrl)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fullUrl.hashCode();
        //result = 31 * result + dateTime.hashCode();
        return result;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    public Integer getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(Integer receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public Integer getSendBytes() {
        return sendBytes;
    }

    public void setSendBytes(Integer sendBytes) {
        this.sendBytes = sendBytes;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean existsRedirect() {
        return redirectTo != null && !"".equals(redirectTo);
    }
}
