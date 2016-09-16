package com.arctouch.publictransportation.domain.generic.returns;

import com.arctouch.publictransportation.domain.Departure;

import java.util.ArrayList;
import java.util.List;

public class DeparturesReturn extends GenericReturn<Departure> {
    private static final String WEEKDAY = "WEEKDAY";
    private static final String SATURDAY = "SATURDAY";
    private static final String SUNDAY = "SUNDAY";

    public List<Departure> getRowsWeekday() {
        return filterByCalendar(WEEKDAY);
    }

    public List<Departure> getRowsSaturday() {
        return filterByCalendar(SATURDAY);
    }

    public List<Departure> getRowsSunday() {
        return filterByCalendar(SUNDAY);
    }

    private List<Departure> filterByCalendar(String calendar) {
        List<Departure> filteredDepartures = new ArrayList<>();
        for (Departure departure : super.getRows()) {
            if (departure.getCalendar().equalsIgnoreCase(calendar)) {
                filteredDepartures.add(departure);
            }
        }
        return filteredDepartures;
    }

}
