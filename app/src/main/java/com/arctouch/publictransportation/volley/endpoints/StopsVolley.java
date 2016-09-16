package com.arctouch.publictransportation.volley.endpoints;

import android.content.Context;

import com.arctouch.publictransportation.domain.generic.params.RouteIdParam;
import com.arctouch.publictransportation.domain.generic.returns.StopsReturn;
import com.arctouch.publictransportation.interfaces.VolleyListener;
import com.arctouch.publictransportation.volley.VolleyBase;

public class StopsVolley extends VolleyBase<RouteIdParam, StopsReturn> {

    private static final String ENDPOINT = "findStopsByRouteId/run";

    public StopsVolley(final Context context, RouteIdParam routeIdParam,
                       VolleyListener volleyListener) {
        super(context, routeIdParam, volleyListener, StopsReturn.class, ENDPOINT);
    }

}