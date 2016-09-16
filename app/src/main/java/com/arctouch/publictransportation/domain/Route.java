package com.arctouch.publictransportation.domain;

import java.io.Serializable;

public class Route implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String shortName;
    private String longName;
    private String lastModifiedDate;
    private Integer agencyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Integer agencyId) {
        this.agencyId = agencyId;
    }

    @Override
    public String toString() {
        return shortName;
    }
}
