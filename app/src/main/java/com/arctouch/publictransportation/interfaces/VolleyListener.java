package com.arctouch.publictransportation.interfaces;

public interface VolleyListener<T> {
    void onVolleySuccess(T object);

    void onVolleyError(String error);
}
