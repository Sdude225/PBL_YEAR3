package io.javabrains.springsecurityjwt.models;

import java.io.Serializable;

public class RegisterRequest implements Serializable {
    private String token;
    private boolean monitor;
    private String linkedEmail;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isMonitor() {
        return monitor;
    }

    public void setMonitor(boolean monitor) {
        this.monitor = monitor;
    }

    public String getLinkedEmail() {
        return linkedEmail;
    }

    public void setLinkedEmail(String linkedEmail) {
        this.linkedEmail = linkedEmail;
    }

    //need default constructor for JSON Parsing
    public RegisterRequest()
    {

    }

    public RegisterRequest(String token, boolean monitor, String linkedEmail) {
        this.token = token;
        this.monitor = monitor;
        this.linkedEmail = linkedEmail;
    }
}
