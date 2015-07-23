package some.domain.model;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class ClientRequest {
    private static final Logger log = Logger.getLogger(ClientRequest.class);

    private String fullUrl;
    private DateTime dateTime;
    private String redirectTo;
    private Integer receivedBytes;
    private Integer sendBytes;
    private Double speed;
    private long timeSpent;
    private Client client;



    private String response;

    public ClientRequest(String fullUrl) {
        if (fullUrl == null || "".equals(fullUrl))
            throw new IllegalArgumentException("url can't be empty!");
        init(fullUrl);
    }

    private void init(String fullUrl) {
        this.fullUrl = fullUrl;
        this.dateTime = new DateTime();//current time
        this.redirectTo = "";
        this.response = "";
        this.receivedBytes = 0;
        this.sendBytes = 0;
        this.speed = 0.;
        this.timeSpent = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientRequest that = (ClientRequest) o;
        return fullUrl.equals(that.fullUrl);

    }

    @Override
    public int hashCode() {
        return fullUrl.hashCode();
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
        speed = receivedBytes + sendBytes / (timeSpent / 1000.);
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

    public Long getTimeSpent() {
        return timeSpent;
    }

    public void fixTimeSpent(){
        long diff = new DateTime().getMillis() - dateTime.getMillis();
        this.timeSpent = diff == 0 ? 1 : diff;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
