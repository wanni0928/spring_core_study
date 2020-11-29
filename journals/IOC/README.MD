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
    - 이 경우를 해결하기 위한 방법.
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
