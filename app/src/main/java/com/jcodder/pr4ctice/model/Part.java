package com.jcodder.pr4ctice.model;

import java.util.ArrayList;
import java.util.List;

public class Part {
    private String id;
    private List<Measure> measures;

    public Part() {
        measures = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }
}
