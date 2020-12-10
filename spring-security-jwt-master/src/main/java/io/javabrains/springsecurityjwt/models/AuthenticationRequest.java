package io.javabrains.springsecurityjwt.models;

import java.io.Serializable;

public class AuthenticationRequest implements Serializable {


    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //need default constructor for JSON Parsing
    public AuthenticationRequest()
    {

    }

    public AuthenticationRequest(String token) {
        this.token = token;
    }
}
