package com.jcodder.pr4ctice.model;

import java.util.ArrayList;
import java.util.List;

public class Measure {
    private List<Attribute> attributes;
    private List<Note> notes;
    private List<Direction> directions;

    public Measure() {
        attributes = new ArrayList<>();
        notes = new ArrayList<>();
        directions = new ArrayList<>();
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }
}
