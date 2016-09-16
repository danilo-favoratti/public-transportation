package com.arctouch.publictransportation.domain;

public class Search {
    private String stopName;

    public Search(String search) {
        this.stopName = String.format("%%%s%%", search);
    }

    public String getStopName() {
        return stopName;
    }

}