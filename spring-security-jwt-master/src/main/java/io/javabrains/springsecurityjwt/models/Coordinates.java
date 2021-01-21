package io.javabrains.springsecurityjwt.models;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private float corX;
    private float corY;

    public Coordinates(float corX, float corY) {
        this.corX = corX;
        this.corY = corY;
    }

    public float getCorX() {
        return corX;
    }

    public void setCorX(float corX) {
        this.corX = corX;
    }

    public float getCorY() {
        return corY;
    }

    public void setCorY(float corY) {
        this.corY = corY;
    }

    //need default constructor for JSON Parsing
    public Coordinates()
    {

    }


}

