package com.wannistudio.springapplicationcontext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//@SpringBootApplication
public class SpringapplicationcontextApplication {

    public static void main(String[] args) {
//        SpringApplication.run(SpringapplicationcontextApplication.class, args);

/**1. xml 설정 파일로 IOC 불러오기*/
//        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
//        String[] beanDefinitionNames = context.getBeanDefinitionNames();
//        for (String beanDefinitionName : beanDefinitionNames) {
//            System.out.println(beanDefinitionName);
//        }
/**2. @Configuration으로 IOC 불러오기*/
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        BookService bookService = (BookService) context.getBean("bookService");
        System.out.println(bookService.bookRepository != null);
    }

}
