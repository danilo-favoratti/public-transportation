package com.arctouch.publictransportation.domain.generic.params;

import com.arctouch.publictransportation.domain.Search;

public class StopNameParam extends GenericParam<Search> {

    public StopNameParam(String stopName) {
        super.setParams(new Search(stopName));
    }

}
