package com.wannistudio.proxyspringaop;

import org.springframework.stereotype.Service;

@Service
public class SimpleEventService implements EventService {
    @Override
    @PerfLogging
    public void createEvent() {
//        long begin = System.currentTimeMillis();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("create Event");

//        System.out.println(System.currentTimeMillis() - begin);
    }

    @Override
    @PerfLogging
    public void publishEvent() {
//        long begin = System.currentTimeMillis();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("publish Event");

//        System.out.println(System.currentTimeMillis() - begin);
    }
    @Override
    public void deleteEvent() {
        System.out.println("Delete an event");
    }
}
