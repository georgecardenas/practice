package com.jcodder.pr4ctice.model;

public class Note {
    private Pitch pitch;
    private String type;
    private String beam;
    private boolean dotted;
    private boolean rest;

    public Pitch getPitch() {
        return pitch;
    }

    public void setPitch(Pitch pitch) {
        this.pitch = pitch;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBeam() {
        return beam;
    }

    public void setBeam(String beam) {
        this.beam = beam;
    }

    public boolean isDotted() {
        return dotted;
    }

    public void setDotted(boolean dot) {
        this.dotted = dot;
    }

    public boolean isRest() {
        return rest;
    }

    public void setRest(boolean rest) {
        this.rest = rest;
    }
}
