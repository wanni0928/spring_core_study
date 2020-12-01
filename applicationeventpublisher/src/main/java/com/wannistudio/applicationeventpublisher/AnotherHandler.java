package com.wannistudio.applicationeventpublisher;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Async
public class AnotherHandler {
    @EventListener
    public void handle(MyEvent myEvent) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("Another : " + myEvent.getData());
    }
}
