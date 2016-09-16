package com.arctouch.publictransportation.volley.endpoints;

import android.content.Context;

import com.arctouch.publictransportation.domain.generic.params.RouteIdParam;
import com.arctouch.publictransportation.domain.generic.returns.DeparturesReturn;
import com.arctouch.publictransportation.interfaces.VolleyListener;
import com.arctouch.publictransportation.volley.VolleyBase;

public class DeparturesVolley extends VolleyBase<RouteIdParam, DeparturesReturn> {

    private static final String ENDPOINT = "findDeparturesByRouteId/run";

    public DeparturesVolley(final Context context, RouteIdParam routeIdParam,
                            VolleyListener<DeparturesReturn> volleyListener) {
        super(context, routeIdParam, volleyListener, DeparturesReturn.class, ENDPOINT);
    }

}