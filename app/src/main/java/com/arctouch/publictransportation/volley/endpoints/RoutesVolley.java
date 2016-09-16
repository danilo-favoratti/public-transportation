package com.arctouch.publictransportation.volley.endpoints;

import android.content.Context;

import com.arctouch.publictransportation.domain.generic.params.StopNameParam;
import com.arctouch.publictransportation.domain.generic.returns.RoutesReturn;
import com.arctouch.publictransportation.interfaces.VolleyListener;
import com.arctouch.publictransportation.volley.VolleyBase;

public class RoutesVolley extends VolleyBase<StopNameParam, RoutesReturn> {

    private static final String ENDPOINT = "findRoutesByStopName/run";

    public RoutesVolley(final Context context, StopNameParam stopNameParam,
                        VolleyListener volleyListener) {
        super(context, stopNameParam, volleyListener, RoutesReturn.class, ENDPOINT);
    }

}