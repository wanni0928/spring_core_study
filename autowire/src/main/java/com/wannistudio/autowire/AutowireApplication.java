package com.wannistudio.autowire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import out.MyService;

@SpringBootApplication
public class AutowireApplication {

    public static void main(String[] args) {
        // 빈 스코프가 singletone 일 경우, 모든 빈을 한번 씩 생성하고 실행하기 때문에, 초기 애플리케이션 실행시 성능을 잡아먹을 수도 있다.
        SpringApplication.run(AutowireApplication.class, args);

        // component 스캔을 포기하고 아래와 같은 방법으로도 실행이 가능하지만, xml으로 빈 설정을 했을 때의 단점과 문제점을 다시 겪게되는 경우라, 좋은 방법이라 할 수는 없다.
//        new SpringApplicationBuilder()
//                .sources(AutowireApplication.class)
//                .initializers((ApplicationContextInitializer<GenericApplicationContext>) ctx -> ctx.registerBean(MyService.class))
//                .run(args);
    }

}
