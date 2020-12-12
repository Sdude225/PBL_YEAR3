package io.javabrains.springsecurityjwt.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CorResponse implements Serializable {
    private Float[] cor;
    private String lastSeen;

    //need default constructor for JSON Parsing
    public CorResponse()
    {

    }

    public Float[] getCor() {
        return cor;
    }

    public void setCor(Float[] cor) {
        this.cor = cor;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public CorResponse(Float[] cor, String lastSeen) {
        this.cor = cor;
        this.lastSeen = lastSeen;
    }
}
