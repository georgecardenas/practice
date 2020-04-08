package com.jcodder.pr4ctice.model;

public class Attribute {
    private int divisions;
    private Key key;
    private Time time;
    private Clef clef;

    public int getDivisions() {
        return divisions;
    }

    public void setDivisions(int divisions) {
        this.divisions = divisions;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Clef getClef() {
        return clef;
    }

    public void setClef(Clef clef) {
        this.clef = clef;
    }
}
