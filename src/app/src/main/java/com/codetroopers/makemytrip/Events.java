package com.codetroopers.makemytrip;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Srujan Jha on 9/25/2016.
 */
public class Events {
    public String Id,EventName,Description,Source,Destination,Attendees;
    public long StartDate,EndDate;
    public boolean HotelsBooked;
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Id", Id);
        result.put("EventName", EventName);
        result.put("Description", Description);
        result.put("Source", Source);
        result.put("Destination", Destination);
        result.put("Attendees", Attendees);
        result.put("StartDate", StartDate);
        result.put("EndDate", EndDate);
        result.put("HotelsBooked", HotelsBooked);

        return result;
    }

}
