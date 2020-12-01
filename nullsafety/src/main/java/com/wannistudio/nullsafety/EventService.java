package com.wannistudio.nullsafety;

import org.springframework.lang.NonNull;

public class EventService {

    @NonNull
    public String createEvent(@NonNull String name) {
        return "hello " + name;
//        return null;
    }

}
