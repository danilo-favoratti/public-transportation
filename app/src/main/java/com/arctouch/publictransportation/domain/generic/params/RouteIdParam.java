package com.arctouch.publictransportation.domain.generic.params;

import com.arctouch.publictransportation.domain.RouteId;

public class RouteIdParam extends GenericParam<RouteId> {

    public RouteIdParam(int routeId) {
        super.setParams(new RouteId(new Integer(routeId)));
    }

}
