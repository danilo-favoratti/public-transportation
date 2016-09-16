package com.arctouch.publictransportation.domain.generic.params;

public abstract class GenericParam<T> {
    private T params;

    public T getParams() {
        return params;
    }

    protected void setParams(T params) {
        this.params = params;
    }
}