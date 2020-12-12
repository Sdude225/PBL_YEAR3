package io.javabrains.springsecurityjwt.models;

import java.io.Serializable;

public class Coordinates implements Serializable {

    private Float[] cor;

    //need default constructor for JSON Parsing
    public Coordinates()
    {

    }

    public Float[] getCor() {
        return cor;
    }

    public void setCor(Float[] cor) {
        this.cor = cor;
    }

    public Coordinates(Float[] cor) {
        this.cor = cor;
    }
}
