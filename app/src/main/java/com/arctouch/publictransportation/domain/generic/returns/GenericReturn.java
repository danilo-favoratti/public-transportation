package com.arctouch.publictransportation.domain.generic.returns;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericReturn<T> {
    private List<T> rows = new ArrayList<>();

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}