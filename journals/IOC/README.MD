# IOC (Inversion Of Control) : 제어의 역전
> 의존 관계 주입(Dependency Injection) 이라고도 한다. 특정 객체가 사용하는 '의존 객체'를 직접 만들어 사용하는 것이 아니라, 주입을 받아 사용하는 방법을 말한다. (흔히 구현과 역할을 분리해서 사용하기도 한다.)

## 스프링 IOC 컨테이너
- BeanFactory
- 애플리케이션 컴포넌트의 중앙 저장소
- '빈 설정 소스' 를 통해 '빈 정의'를 읽고, 빈을 구성하고, 제공한다.

## 빈(Bean)
- '스프링 IOC 컨테이너'가 관리하는 객체
- 장점
    - 의존성 관리
    - 스코프
        - 싱글톤 : 하나
        - 프로토타입 : 매번 다른 객체
    -  생명주기 사이클

## ApplicationContext
- BeanFactory
- 메세지 소스 처리기능(i18n)
- 이벤트 발행 기능
- 리소스 로딩 기능

## ApplicationContext와 다양한 빈 설정 방법.

```java
    
    Bean configuration ------> Spring IoC container

```
## Spring Ioc container의 역할
- 빈 인스턴스 생성
- 의존 관계 설정
- 빈 제공

> 위 세가지 기능으로 인해, 개발자는 객체간의 '역할' 과 '구현'을 분리해서, 애플리케이션 설계를 진행할 수 있고, 이로 인해, 높은 응집도 + 낮은 결합도의 애플리케이션을 구현할 수 있게 된다.
> 이러한 구조의 애플리케이션은, 협업의 효율을 높일 수 있으며, 유지보수로 인해 발생되는 비용을 최소화할 수 있게된다.

## ApplicationContext
- ClassPathXmlApplicationContext (XML)
- AnnotationConfigApplicationContext (Java)

## 컴포넌트 스캔
- 설정 방법
    - xml 설정에서는 context:conponent-scan
    - 자바 설정에서는 @ComponentScan
- 특정 패키지 이하의 모든 클래스 중에 @Component 애노테이션을 사용한 클래스를 빈으로 자동등록을 해준다.

## Autowired
- required: Default로는 true. (false로 할 경우, 의존관계가 꼬여도, 애플리케이션은 작동한다. 하지만, 기능은 보장 못한다.)

## Autowired 사용위치
- 생성자
- setter
- field

## 사용할 경우 접할 수 있는 경우의 수
1. 해당 타입의 빈이 없는 경우 : 에러가 뜰 것이다. 대부분의 경우 에러로그를 통해, 의존관계가 꼬였는 빈이 어떤 것인지 알려준다.(주 키워드 : bean Definition)
2. 해당 타입의 빈이 하나인 경우 : 비즈니스 로직이 제대로 구성되어있다면, 오류가 날 일은 없을 것이다.
3. 해당 타입의 빈이 여러 개인 경우 : 역시 에러가 뜬다. 해결법은 다음과 같다. (에러 키워드 : NoUniqueBeanDefinitionException)
    1. @Primary : 해당 어노테이션이 선언되어 있는 빈에 의존관계를 주입한다.
        ```java
        package com.wannistudio.autowire;

        @Repository
        @Primary
        public class WanniBookRepository implements BookRepository {
        }

        @Repository
        public class MyBookRepository implements BookRepository {
        }

        @Component
        public class MyRunner implements ApplicationRunner {
            @Autowired
            ApplicationContext applicationContext;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                BookRepository bean = applicationContext.getBean(BookRepository.class);
                System.out.println(bean);
            }
        }

        /**
        * result
        * com.wannistudio.autowire.WanniBookRepository@3f4f9acd
        */
        ```
    2. @Qualifier : 의존관계 주입하는 코드에 추가한다. (빈 이름으로 주입)
        ```java
        package com.wannistudio.autowire;

        @Service
        public class BookService {
            @Autowired @Qualifier("wanniBookRepository")
            BookRepository bookRepository;

            @PostConstruct
            public void setup() {
                System.out.println("@postConstruct : " + bookRepository.getClass());
            }
        }

        /**
        * result
        * com.wannistudio.autowire.WanniBookRepository
        */
        ```

    3. 해당 타입의 빈 모두 주입 받기
        ```java
        @Service
        public class BookService {
            @Autowired
            List<BookRepository> bookRepositories;

            public void printBookRepository() {
                bookRepositories.forEach(bookRepository -> System.out.println(bookRepository.getClass()));
            }

            @PostConstruct
            public void setup() {
                System.out.println("@postConstruct : " + bookRepository.getClass());
            }

            public BookService(BookRepository bookRepository) {
                this.bookRepository = bookRepository;
            }
            @Autowired(required = false) // Autowired가 있기 때문에 의존성 주입을 시도하려고 한다.
            public void setBookRepository(BookRepository bookRepository) {
                this.bookRepository = bookRepository;
            }
        }

        /**
        * result
        * [com.wannistudio.autowire.MyBookRepository, com.wannistudio.autowire.WanniBookRepository]
        */
        ```
## 동작원리
- 빈 라이프사이클
- BeanPostProcessor
    - 새로 만든 빈 인스턴스를 수정할 수 있는 라이프 사이클 인터페이스
- AutowiredAnnotationBeanPostProcessor extends BeanPostProcessor
    - 스프링이 제공하는 @Autowired와 @Value 애노테이션 처리기.

## @Component와 컴포넌트 스캔

### 컴포넌트 스캔 주요 기능
- 스캔 위치 설정.
- 필터 : 어떤 애노테이션을 스캔 할지 또는 하지 않을지 결정.

### @Component
- @Repository
- @Service
- @Controller
- @Configuration

### 동작 원리
- @ComponentScan은 스캔할 패키지와 어노테이션에 대한 정보
- 실제 스캐닝은 ConfigurationClassPostProcessor라는 BeanFactoryPostProcessor에 의해 처리 됨.

## 빈의 스코프
### 스코프
- 싱글톤
- 프로토타입
    - Request
    - Session
    - WebSocket

- 프로토타입 빈이 싱글톤 빈을 참조할 경우 : 아무 문제 없다.
- 싱글톤 빈이 프로토타입 빈을 참조할 경우.
    - 프로토타입 빈이 업데이트가 안된다. 
    (싱글톤 빈은 애플리케이션 실행이후 한번 인스턴스화 되는데, 한번 인스턴스화 되는 빈이 다른 빈 하나를 참조하니, 코드상으로 몇번을 선언해도 결국 같은 빈만 참조하는 상황이 발생.)
    - 이 경우를 해결하기 위한 방법. (@Scope 에서 proxy 패턴을 구현한 proxyMode을 이용한다. )
        ```java
        @Component @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
        public class Proto {

            @Autowired
            Single single;
        }


        @Component
        public class Single {

            @Autowired
            private Proto proto;

            public Proto getProto() {
                return proto;
            }
        }

        @Component
        public class AppRunner implements ApplicationRunner {

            @Autowired
            ApplicationContext ctx;

            @Autowired
            BookRepository bookRepository;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                System.out.println("Proto");
                System.out.println(ctx.getBean(Proto.class));
                System.out.println(ctx.getBean(Proto.class));
                System.out.println(ctx.getBean(Proto.class));
                System.out.println("single");
                System.out.println(ctx.getBean(Single.class));
                System.out.println(ctx.getBean(Single.class));
                System.out.println(ctx.getBean(Single.class));
                System.out.println("proto by single");
                System.out.println(ctx.getBean(Single.class).getProto());
                System.out.println(ctx.getBean(Single.class).getProto());
                System.out.println(ctx.getBean(Single.class).getProto());

            }
        }
        ```

## IoC 컨테이너 : Enviroment 프로파일
> 프로파일과 프로퍼티를 다루는 인터페이스.

- ApplicationContext extends EnvironmentCapable
    - getEnvironment()
- 프로파일
    - 빈들의 그룹
    - Enviroment의 역할은 활성화할 프로파일 확인 및 설정.
- 프로파일을 쓰는 이유
    - 테스트 환경에서는 A라는 빈 사용.
    - 배포 환경에서는 B라는 빈 사용.
    - 환경에 따라 빈을 구분하여 사용하고 싶을 때 활용.
- 프로파일 정의하기
    - 클래스에 정의
        ```java
        @Repository
        @Profile("test")
        public class TestBookRepository implements BookRepository {
        }

        @Component
        public class AppRunner implements ApplicationRunner {

            @Autowired
            ApplicationContext ctx;

            @Autowired
            BookRepository bookRepository;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Environment environment = ctx.getEnvironment();
                String property = environment.getProperty("app.name");
                System.out.println(property);
                System.out.println(environment.getProperty("app.about"));

            }
        }

        @SpringBootApplication
        @PropertySource("classpath:/app.properties")
        public class BeanscopeApplication {

            public static void main(String[] args) {
                SpringApplication.run(BeanscopeApplication.class, args);
            }

        }
        ```
    - 메소드에 정의
        - @Bean @Profile("test")

    - 프로파일 표현식
        - !(not)
        - &(and)
        - |(or)

## MessageSource
> 국제화(i18n) 기능을 제공하는 인터페이스.

### ApplicationContext extends MessageSource
- getMessage(String code, Object[] args, String, default, Locale, loc)

### 스프링 부트를 사용한다면 별다른 설정 필요없이 message.properties를 사용할 수 있음.
- messages.properties
- messages_ko_kr.properties

### 릴로딩 기능이 있는 메시지 소스 사용하기
```java
@SpringBootApplication
public class MessagesourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessagesourceApplication.class, args);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3);
        return messageSource;
    }
}
```

## ApplicationEventPublisher
> 이벤트 프로그래밍에 필요한 인터페이스 제공. 옵저버 패턴 구현체.

### ApplicationContext extends ApplicationEventPublisher
- publishEvent(ApplicationEvent event)

### 이벤트 만들기
- ApplicationEvent 상속 (스프링 4.2 부터는 이 클래스를 상속받지 않아도 이벤트로 사용할 수 있다.)
    ```java
    public class MyEvent {

        private int data;
        private Object source;

        public MyEvent(Object source, int data) {
            this.source = source;
            this.data = data;
        }

        public Object getSource() {
            return source;
        }

        public int getData() {
            return data;
        }
    }

    @Component
    public class MyEventHandler {

        @EventListener
        @Order(Ordered.HIGHEST_PRECEDENCE)
        public void handle(MyEvent myEvent) {
            System.out.println(Thread.currentThread().toString());
            System.out.println("이벤트 받았다. 데이터 : " + myEvent.getData());
        }
    }

    @Component
    public class AppRunner implements ApplicationRunner { // 4.2 버전 이전에는 extends ApplicationEventPublisher를 선언해야 했다.

        @Autowired
        ApplicationContext publishEvent;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            publishEvent.publishEvent(new MyEvent(this, 100)); // event 발생
        }
    }
    ```

- 이벤트 처리하는 방법
    - ApplicationListener<이벤트> 구현한 클래스 만들어서 빈으로 등록하기.
    - 스프링 4.2 부터는 @EventListener를 사용해서 빈의 메소드에 사용할 수 있다.
    - 기본적으로는 synchronized.
    - 순서를 정하고 싶다면 @Order와 함께 사용.
    - 비동기적으로 실행하고 싶다면 @Async와 함께 사용.

- 스프링이 제공하는 기본 이벤트
    - ContextRefreshedEvent: ApplicationContext를 초기화 했더나 리프래시 했을 때 발생.
    - ContextStartedEvent: ApplicationContext를 start()하여 라이프사이클 빈들이 시작
    신호를 받은 시점에 발생.
    - ContextStoppedEvent: ApplicationContext를 stop()하여 라이프사이클 빈들이 정지
    신호를 받은 시점에 발생.
    - ContextClosedEvent: ApplicationContext를 close()하여 싱글톤 빈 소멸되는 시점에
    발생.
    - RequestHandledEvent: HTTP 요청을 처리했을 때 발생.

## ResourceLoader
> 리소스를 읽어오는 기능을 제공하는 인터페이스
### ApplicationContext extends ResourceLoader

### 리소스 읽어오기
- 파일 시스템에서 읽어오기
- 클래스패스에서 읽어오기
- URL로 읽어오기
- 상대/절대 경로롤 읽어오기

### Resource getResource(java.lang.String location)