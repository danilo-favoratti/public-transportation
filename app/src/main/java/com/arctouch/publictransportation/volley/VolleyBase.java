package com.arctouch.publictransportation.volley;


import android.content.Context;
import android.util.Base64;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arctouch.publictransportation.R;
import com.arctouch.publictransportation.domain.generic.params.GenericParam;
import com.arctouch.publictransportation.domain.generic.returns.GenericReturn;
import com.arctouch.publictransportation.interfaces.VolleyListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public abstract class VolleyBase<T extends GenericParam, U extends GenericReturn> {

    private static final String SERVER = "https://api.appglu.com/v1/queries/";
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENVIRONMENT = "staging";
    private static final String USER = "WKD4N7YMA1uiM8V";
    private static final String PASSWORD = "DtdTtzMLQlA0hk2C1Yi5pLyVIlAQ68";

    private Context mContext;
    private GsonRequest<T> mRequest;
    private VolleyListener<U> mListener;
    private Class<U> mClass;

    public VolleyBase(final Context context, T requestBody,
                      final VolleyListener<U> listener, Class<U> clazz, String endpoint) {
        this.mContext = context;
        this.mListener = listener;
        this.mClass = clazz;

        String url = SERVER + endpoint;

        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("Content-Type", CONTENT_TYPE);
        headerParams.put("X-AppGlu-Environment", ENVIRONMENT);
        headerParams.put("Authorization", getAuthorizationData());

        String body = new Gson().toJson(requestBody);

        this.mRequest = new GsonRequest(url, mClass, headerParams, body,
                successListener, errorListener);
    }

    private String getAuthorizationData() {
        String credentials = String.format("%s:%s", USER, PASSWORD);
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
    }

    private Response.Listener<U> successListener = new Response.Listener<U>() {
        @Override
        public void onResponse(U response) {
            mListener.onVolleySuccess(response);
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mListener.onVolleyError(mContext.getString(R.string.exception_retrieve_values_message));
        }
    };

    public GsonRequest<T> getmRequest() {
        return mRequest;
    }

}