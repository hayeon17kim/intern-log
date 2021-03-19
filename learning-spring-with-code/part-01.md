#  Part 1 스프링 개발 환경 구축

## Chapter 1. 개발을 위한 준비

> Lombok 라이브러리
>
> - Java 개발 시 자주 사용하는 getter, setter, toString(), 생성자 등을 자동으로 생성
> - 약간의 코드만으로 필요한 클래스를 설계할 때 유용
> - 프로젝트가 아니라 에디터에 적용해야 함

## Chapter 2. 스프링 특징과 의존성 주입

### 2.1 스프링 프레임워크의 간략한 역사

**프레임워크란?**

뼈대나 근간을 이루는 코드들의 묶음이다.

**프레임워크의 장점**

- 개발에 필요한 구조를 이미 코드로 만들어 놓았기 때문에 실력이 부족한 개발자라 하더라도 반쯤 완성한 상태에서 필요한 부분을 조립하는 형태의 개발이 가능하다.
- 회사 입장: 일정한 품질 보장
- 개발자 입장: 완성된 구조에 자신이 맡은 코드를 개발해서 넣어주는 형태이므로 개발 시간 단축

**스프링이 나온 배경**

- 2000년대 초반부터 시작된 엔터프라이즈급의 개발은 안정된 품질의 개발이 절실했고, 그 결과 많은 프레임워크의 전성기였다.
- 스프링은 비교적 그 시작이 늦었지만 성공적인 경량 프레임워크다.

> 경량 프레임워크(light-weight Framework): 90년대 말에 복잡한 구동 환경과 하드웨어적인 구성이 필요한 프레임워크와 반대되는 개념으로 등장하였다. 과거  J2EE 기술은 너무 복잡하고 방대했기 때문에, 그 전체를 이해하고 개발하기 어려워서 특정 기능을 위주로 간단한 jar 파일 등을 이용해서 모든 개발이 가능하도록 구성된 프레임워크이다.

**스프링이 살아남은 이유**

- 복잡하지 않음

  일반적인 Java 클래스와 인터페이스를 이용하는 구조여서 진입장벽이 높지 않고, EJB와 같이 복잡한 프레임워크에 비해 가볍기 때문에 빠르게 엔터프라이즈급의 시스템을 작성할 수 있다.

- 프로젝트 전체 구조를 설계할 때 유용

  다른 프레임워크들과 달리 스프링은 어느 한 분야에 집중하지 않고, 전체를 설계하는 용도로 사용될 수 있다. 

- 다른 프레임워크들의 포용

  기본 뼈대를 흔들지 않고, 여러 종류의 프레임워크를 혼용해서 사용할 수 있다. 다른 프레임워크들과의 통합을 지원해 최소한의 수정이 가능하다. 특정 프레임워크를 채택했을 때 해당 영역 전체를 수정해야 하는 고질적인 문제를 갖고 있는 다른 프레임워크들과는 다르다.

- 개발 생산성과 개발 도구의 지원

  코드의 양이 줄어들고, 유지 보수에 있어서도 XML설정 등을 이용한다. 또한 STS, Eclipse, IntelliJ 등의 플러그인 지원 역시 다른 프레임워크들에 비해서 빠른 업데이트가 되었기 대문에 별도의 새로운 개발도구에 대한 적응이 없이도 개발이 가능하다.

**스프링의 중요한 변화**

- Spring 2.5: 어노테이션 활용 설정을 도입하면서 편리한 설정과 개발이 가능하도록 지원
- Spring 3.0: 별도의 설정 없이도 Java 클래스 만으로 설정 파일을 대신할 수 있게 지원
- Spring 4.0: 모바일 환경과 웹 환경에서 많이 사용되는 REST 방식의 컨트롤러 지원
- Spring 5.0: Refactor를 이용한 Reactive 스타일의 개발 환경 지원



### 2.1.1. 스프링의 주요 특징

**POJO 기반의 구성**

객체 간의 관계를 구성할 때 별도의 API 등을 사용하지 않는 POJO의 구성만으로 가능하도록 제작되어 잇다. 즉 코드를 개발할 때 개발자가 특정한 라이브러리나 컨테이너의 기술에 종속적이지 않다. 가장 일반적인 형태로 코드를 작성하고 실행할 수 있기 때문에 생산성에서도 유리하고, 코드에 대한 테스트 작업 역시 유연하게 할 수 있다.

**의존성 주입(DI)을 통한 객체 간의 관계 구성**

- 의존성(Dependency): 하나의 객체가 다른 객체 없이 제대로 된 역할을 할 수 없다. 
- 주입(Injection): 외부에서 '밀어 넣는 것'
- 의존성 주입: 어떤 객체가 필요한 객체를 외부에서 밀어 넣는다.
- 주입을 받는 입장에서는 어떤 객체인지 신경 쓸 필요가 없다. 
- 어떤 객체에 의존하든 자신의 역할은 변하지 않는다. 
- 스프링에서는 ApplicationContext가 필요한 객체를 생성하고, 주입한다. 
- 여기서 ApplicationContext가 관리하는 객체들을 Bean이라고 한다. 
- Bean과 Bean사이의 의존관계를 처리하는 방식으로 XML 설정, Annotation 설정, Java 설정 방식을 이용할 수 있다.

AOP(Aspect-Oriented-Programming) 지원

- 반복적인 코드를 줄이고, 핵심 비즈니스 로직에만 집중할 수 있도록 한다.
- 횡단 관심사(cross-concern): 대부분의 시스템이 공통으로 가지고 있는 보안이나 로그, 트랜잭션 등 비즈니스 로직은 아니지만, 반드시 처리가 필요한 부분
- AOP(Aspect-Oriented Programming): 횡단 관심사를 모듈로 분리하는 프로그래밍 패러다임
- 장점
  - 핵심 비즈니스 로직에만 집중해서 코드를 개발할 수 있다.
  - 각 프로젝트마다 다른 관심사를 적용할 때 코드의 수정을 최소화시킬 수 있다.
  - 원하는 관심사의 유지 보수가 수월한 코드를 구성할 수 있다. 

**트랜잭션 지원**

스프링은 트랜잭션의 관리를 어노테이션이나 XML로 설정할 수 있기 때문에 개발자가 매번 상황에 맞는 코드를 작성할 필요가 없도록 설계되었다.

**편리한 MVC 구조**

**WAS에 종속적이지 않은 개발 환경**

### 2.2. 의존성 주입 테스트

**의존성 주입 방법**

- 생성자를 이용한 주입
- setter를 이용한 주입

### 2.2.1. 예제 클래스 생성

```java
@Component
@Data
public class Chef {
  
}
```

- `@Data`: Lombok의 setter를 생성하는 기능과 생성자, toString()등을 자동으로 생성하도록 만드는 애노테이션

```java
@Component
@Data
public class Restaurant {
  @Setter(onMethod_ = @Autowired)
  private Chef chef;
}
```

- `@Component`: 스프링에게 해당 클래스가 스프링에서 관리해야 하는 대상임을 표시
- `@Setter`:  자동으로 setChef()를 컴파일 시 생성
- `onMethod`: setChef()에 `Autowired` 어노테이션을 추가하도록 만든다.

### 2.2.2. XML을 이용하는 의존성 주입 설정

`root-content.xml` 의 일부

```xml
<conext:component-scan base-package="com.hayeon.demo">
</conext:component-scan>
```

### Java 설정을 이용하는 의존성 주입

```java
@Configuration
@ConponentScan(basePackage={"com.haeyeon.demo"})
public class RootConfig {
  
}
```

### 2.3. 스프링이 동작하면서 생기는 일 

- 스프링 프레임워크가 시작되면 먼저 스프링이 사용하는 메모리 영역을 만들게 된다. 이를 컨텍스트(Context)라고 한다. 스프링에서는 ApplicationContext라는 이름의 객체가 만들어진다.
- 스프링은 자신이 객체를 생성하고 관리해야 하는 객체들에 대한 설정이 필요하다. 이에 대한 설정이 root-context.xml이다.
- root-context.xml 에 설정되어 있는 `context:component-scan` 태그의 내용을 통해서 `com.hayeon.demo` 패키지를 스캔하기 시작한다.
- 해당 페이지에 있는 클래스들 중에서 스프링이 사용하는 `Component`라는 어노테이션이 존재하는 클래스의 인스턴스를 생성한다.
- Restaurant 객체는 Chef 객체가 필요하다는 어노테이션(`@Autowired`) 설정이 있으므로, 스프링은 Chef 객체의 레퍼런스를 Restaurant 객체에 주입한다.

### 2.3.1. 테스트 코드를 통한 확인

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class SampleTests {
  
  @Setter(onMethod_ = { @AutoWired })
  private Restaurant restaurant;
  
  @Test
  public void testExists() {
    assertNotNull(restaurant);
    
    log.info(restaurant);
    log.info("----------------");
    log.info(restaurant.getChef());
  }
}
```

- `@ContextConfiguration`: 지정된 클래스나 문자열을 이용해서 필요한 객체들을 스프링  내에 객체로 등록한다.
- `@Log4j`: Lombok을 이용해 로그를 기록하는 Logger를 변수로 생성한다. 별도의 Logger 객체 선언 없이도 Log4j 라이브러리와 설정이 존재한다면 바로 사용할 수 있다. 
- `@Test`:JUnit에서 Test 대상을 표시하는 어노테이션



- 스프링은 관리가 필요한 객체(Bean)를 어노테이션 등을 이용해 객체를 생성하고 관리하는 일종의 "컨테이너"나 "팩토리"의 기능을 가지고 있다. 
- Lombok은 자동으로 getter/setter 등을 만들어 주는데 스프링은 생성자 주입 혹은 setter 주입을 이용해 동작한다. Lombok을 통해 getter/setter 를 자동으로 생성하고 'onMethod' 속성을 이용해 작성된 setter에 @Autowired 옵션을 붙인다.
- 스프링은 @Autowired와 같은 어노테이션을 이용해서 개발자가 직접 객체들과의 관계를 관리하지 않고, 자동으로 관리되도록 한다. 

**어노테이션**

- Lombok 어노테이션: @Setter, @Data, @Log4j

  - @Setter

    - value: 접근 제한 속성
    - onMethod: setter 메서드 생성 시 메서드에 추가할 어노테이션 지정
    - onParam: setter 메서드의 파라미터에 어노테이션 사용

  - @Data

    @ToString, @EqualsAndHashCode, @Getter/@Setter, @RequiredArgsConstructor 를 결합한 형태

  - @Log4j

    로그 객체를 생성한다. Log4j 가 존재하지 않을 경우 @Log를 이용한다. (@Log를 이용하면 내부적으로 static final로 Logger객체가 생성된다)

  - @AllArgsConstructor: 인스턴스 변수로 선언 된 모든 것을 파라미터로 받는 생성자를 작성

- Spring 어노테이션: @Component, @Autowired

  - @Autowired

    스프링 내부에서 자신이 특정한 객체에 의존적이므로 자신에게 해당 타입의 빈을 주입해달라는 표시

- 테스트 관련 어노테이션: @RunWith, @ContextConfiguration @Test

  - @ContextConfiguration:  스프링이 실행되면서 어떤 설정 정보를 읽어 들여야 하는지 명시. 
  - @RunWith: 테스트 시 필요한 클래스를 지정



### 2.4. 스프링 4.3 이후 단일 생성자의 묵시적 자동 주입

- 생성자 주입의 경우 객체 생성 시 의존성 주입이 필요하므로 좀 더 엄격하게 의존성 주입을 체크하는 장점이 있다. 
- 기존에 스프링에서는 생성자 주입을 하기 위해 생성자를 정의하고, @Autowired와 같은 어노테이션을 추가해야만 생성자 주입이 이루어 졌지만 스프링 4.3 이후에는 묵시적으로 생성자 주입이 가능하다.

 ```java
@Component
@ToString
@Getter
public class SampleHotel {
  private Chef chef;
  
  public SampleHotel(Chef Chef) {
    this.chef = chef;
  }
}
 ```

- Autowired 없이 처리되고 있다. 

## 3. 스프링과 Oracle Database 연동

### 커넥션 풀

일반적으로 여러 명의 사용자를 동시에 처리해야 하는 웹 애플리케이션의 경우 데이터베이스 연결을 이용할 때는 커넥션 풀(Connection Pool)을 이용한다. Java에서는 DataSource라는 인터페이스를 통해 커넥션 풀을 사용한다. DataSource를 통해 매번 데이터베이스와 연결하는 것이 아닌, 미리 연결을 맺어주고 반환하는 구조를 이용하여 성능 향상을 꾀한다. 

### 4. MyBatis와 스프링 연동

MyBatis는 SQL 매핑 프레임워크이다. 

| 전통적인 JDBC 프로그램                                       | MyBatis                                                      |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| 직접 Connection을 맺고 마지막에 close()                      | 자동으로 Connection close() 가능                             |
| PreparedStatement 직접 생성 및 처리                          | MyBatis 내부적으로 PreparedStatement 처리                    |
| PreparedStatement의 setXXX() 등에 대한 모든 작업을 개발자가 처리 | #{prop}와 같이 속성을 지정하면 내부적으로 자동 처리          |
| SELECT 의 경우 직접 ResultSet 처리                           | 리턴 타입을 지정하는 경우 자동으로 객체 생성 및 ResultSet 처리 |

- MyBatis는 기존의 SQL을 그대로 활용할 수 있다는 장점이 있고, 진입장벽이 낮은 편이다.
- MyBatis는 mybatis-spring이라는 라이브러리를 통해 쉽게 연동작업을 할 수 잇다.

**SQLSessionFactory**

- SQLSession을 만들어낸다.
- 개발에서는 SQLSession을 통해서 Connection을 생성하거나 원하는 SQL을 전달하고, 결과를 리턴받는 구조로 작성한다. 

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {RootConfig.class})
@Log4j
public class DataSourceTests {
  @Setter(onMethod_ = {@Autowired} )
  private DataSource datatSource;
  
  @Setter(onMethod_ = {@Autowired})
  private SqlSessionFactoåry sqlSessionFactory;
  
  @Test
  public void testMyBatis() {
    try (SqlSession session = sqlSessionFactory.openSession();
        Connection con = session.getConnection();) {
      log.info(session);
      log.info(con);
    } catch (Exception e) {
      fail(e.getMessage)l
    }
  }
}
```

### 4.2. 스프링과의 연동 처리

- SQLSessionFactory를 이용해서 코드를 작성해도 직접 Connection을 얻어서 JDBC 코딩이 가능하다.
- MyBatis의 Mapper를 사용하면 SQL과 그에 대한 처리를 별도의 설정을 분리해주고, 자동으로 처리되도록 할 수 있다. 

```java
public interface TimeMapper {
  @Select("SELECT sysdate FROM dual")
  public String getTime();
}
```

- Mapper 를 작성해주었다면 MyBatis가 동작할 때 Mapper를 인식할 수 있도록 root-context.xml 혹은 RootConfig에 추가적인 설정을 한다. 

```java
@Configuration
@ComponentScan(basePackage = {"com.hayeon.sample"} )
@MapperScan(basePackage = {"com.hayeon.mapper"} )
public class RootConfig {
  
}
```

- Mapper 테스트를 작성한다.

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {com.hayeon.config.RootConfig.class})
@Log4j
public class TimeMapperTests {
  @Setter(onMethod_ = {@Autowired} )
  private TimeMapper timeMapper;
  
  @Test
  public void testGetTime() {
		log.info(timeMapper.getClass().getName());
    log.info(timeMapper.getTime());
  }
}
```

개발 시 인터페이스만 만들어 주었다. 그러나 스프링은 인터페이스를 이용해서 객체를 생성한다.

### log4jdbc-log4j2 설정

- MyBatis는 내부적으로JDBC의 PreparedStatement를 이용해서 SQL을 처리한다.
- SQL에 전달되는 파라미터는 JDBC에서와 같이 '?'로 치환되어 전달된다.
- 복잡한 SQL의 경우?로 나오는 값이 제대로 되었는지 확인하기가 쉽지 않고 실행된 SQL의 내용을 확인하기 쉽지 않다. 이를 해결하기 위해 PreparedStatement에서 사용된 ?가 어떤 값으로 처리되었는지 확인하는 기능을 추가한다. 



## Part 2. 스프링 MVC 설정

- Servlet/JSP 를 이용하는 개발에 비해 간단하고, 빠른 개발이 가능하다

### 스프링 MVC의 기본 구조

- 스프링 MVC 프로젝트를 구성해서 사용한다는 의미: root-context.xml로 사용하는 일반 Java 영역(흔히 POJO)과 servlet-context.xml로 설정하는 Web 관련 영역을 같이 연동해서 사용하게 된다. 

- WebApplicationContext라는 존재는 기존의 구조에 MVC 설정을 포함하는 구조로 만들어진다.

- Java설정을 이용하는 경우 servlet-context.xml이 없는 상태에서 프로젝트를 시작하게 되므로 WebConfig와 RootConfig, ServletConfig를 작성한다.

- RootConfig는 별다른 내용 없이 작성한다.

- WebConfig는 AbstractAnnotationConfigDispatcherServletInitializer를 상속한다. 

  ```java
  public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
      return new Class[] {RootConfig.class};
    }
    @Override
    protected Class<?>[] getServletConfigClasses() {
      return new Class[] {ServletConfig.class};
    }
    @Override
    protected String[] getServletMappings() {
      return new String[] {"/"};
    }
  }
  ```

- ServletConfig

  - @EnableWebMvc어노테이션과 WebMvcConfigurer를 구현하는 방식
  - @Configuration과 WebMvcConfiguratioSupport를 상속하는 방식: 일반 Configuration 우선 순위가 구분되지 않는 경우에 사용 

  ```java
  @EnableWebMvc
  @ComponentScan(basePackage= {"com.hayeon.controller"})
  public class ServletConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
      InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setViewClass(JstlView.class);
        bean.setPrefix("/WEB-INF/views/");
        bean.setSuffix(".jsp");
        registry.viewResolver(bean);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("/resources/**").addResourceLocation("/resources/");
    }
  }
  ```

### 프로젝트의 구동

- web.xml에서 시작된다.
- web.xml의 상단에는 가장 먼저 시작되는 Context Listener가 등록되어 잇다.
- root-context.xml이 처리되면 파일에 있는 Bean 설정들이 동작하게 된다. 
- root-context.xml에 정의된 객체(Bean)들은 스프링 영역 안에 생성되고, 객체들 간의 의존성이 처리된다.
- root-context.xml이 처리된 후에는 스프링 MVC에서 사용하는 DispatcherServlet이라는 서블릿과 관련된 설정이 동작한다.
- DispatcherSerlvet 클래스는 스프링 MVC 구조에서 가장 핵심적인 역할을 한다. 내부적으로 웹 관련 처리의 준비작업을 진행하는데 이 때 사용하는 파일이 servlet-context.xml  이다. DispatcherServlet은 XmlWebApplicationContext를 이용해서 servlet-context.xml을 로딩하고 해석한다. 이 과정에서 등록된 객체들은 기존에 만들어진 객체들과 같이 연동된다.



### 스프링 MVC의 기본 사상

- Servlet/JSP에서는 HttpServletRequest/HttpServletResponse라는 타입의 객체를 이용해 브라우저에서 전송한 정보를 처리한다.
- 스프링 MVC의 경우 이 위에 하나의 계층을 더한 형태가 된다. 
- 따라서 스프링 MVC를 이용하게 되면 개발자들은 직접적으로 HttpServletRequest / HttpServletResponse와 같은 Servlet/JSP의 API를 사용할 필요성이 현저하게 줄어든다. 스프링은 중간에 연결 역할을 하기 때문에 이러한 코드를 작성하지 않고도 원하는 기능을 구현할 수 있게 된다.



### 모델2와 스프링 MVC

**모델2**

- 모델2 방식은 로직과 화면을 분리하는 스타일의 개발방식이다. 
- Request는 먼저 Controller를 호출한다. 나중에 View를 교체하더라도 사용자가 호출하는 URL 자체에 변화가 없게 만들어주기 때문이다.
- 컨트롤러는 데이터를 처리하는 존재를 이용해서 데이터(Model)을 처리하고 Response할 때 필요한 데이터(Model)를 View 쪽으로 전달한다. 
- Servlet을 이용하는 경우 개발자들은 Servlet API의 RequestDispatcher 등을 이용해서 이를 직접 처리해왔지만 스프링 MVC는 내부에서 이러한 처리를 하고, 개발자들은 스프링 MVC의 API를 이용해서 코드를 작성하게 된다. 

**스프링 MVC의 기본 구조**

- 사용자의 Request는 Front-Controller인 DispatcherServlet을 통해 처리한다.
- HandlerMapping은 Request의 처리를 담당하는 컨트롤러를 찾기 위해서 존재한다. HandlerMapping을 구현한 여러 객체 중 RequestMappingHandlerMapping의 경우 개발자가 @RequestMapping 이 적용된 것을 기준으로 판단한다. 적절한 컨트롤러가 찾아졌다면 HandlerAdpator 를 이용해 해당 컨트롤러를 동작시킨다.
- Controller는 개발자가 작성하는 클래스로 실제 Request를 처리하는 로직을 작성한다. Controller는 다양한 타입의 결과를 반환하는데 이에 대한 처리는 ViweResolver를 이용한다. 
- ViewResolver는 Controller가 반환한 결과를 어떤 View를 통해 처리하는 것이 좋을 지 해석한다.
- View는 실제로 응답 보내야 하는 데이터를 JSP를 이용해서 생성하는 역할을 한다. 만들어진 응답은 DispatcherServlet을 통해서 전송된다. 

**Front-Controller 패턴**

- 모든 Request는 DispatcherServlet을 통하도록 설계된다. 이를 Front-Controller 패턴이라고 한다.
- Front-Controller 패턴을 이용하면 전체 흐름을 강제로 제한할 수 있다. 
- 모든 Request 처리에 대한 분배가 정해진 방식대로만 동작하기 때문에 좀 더 엄격한 구조를 만들어낼 수 있다. 



### 6. 스프링 MVC의 Controller

### Controller의 특징

- HttpServletRequest, HttpServletResponse를 거의 사용할 필요 없이 필요한 기능 구현
- 다양한 타입의 파라미터 처리, 다양한 타입의 리턴 타입 사용 가능
- GET 방식, POST 방식 등 전송 방식에 대한 처리를 어노테이션으로 처리 가능
- 상속/인터페이스 방식 대신 어노테이션만으로도 필요한 설정 가능

### @Controller, @RequestMapping

-  servlet-context.xml에 특정 패키지를 스캔할 때 @Controller 어노테이션을 갖고 있는 클래스를 파악하고 필요하면 이를 객체로 생성해서 관리하게 된다.
- @RequestMapping은 현재 클래스의 모든 메서드들의 기본적인 URL 경로가 된다.  클래스의 선언과 메서드 선언에 사용할 수 있다.
- @Log4j는 @Log가 java.util.Logging을 이용하는 데 반해 Log4j 라이브러리를 활용한다. 

### @RequestMapping의 변화

- @RequestMapping에는 method 속성을 사용하여 GET 방식, POST 방식을 구분한다.
- 스프링 4.3부터는 @GetMapping, @PostMapping으로 이를 줄여 표현할 수 있따.
- GET, POST 방식 모두 지원할 때는 배열로 처리한다. 

### Controller의 파라미터 수집

- Controller를 작성할 때 파라미터는 자동으로 수집된다.
- 매번 request.getParameter()를 이용하는 불편함이 사라진다. 
- 자동으로 setter 메서드가 동작하면서 파라미터를 수집한다. 
- 특히 **자동으로 타입을 변환해서 처리**한다

**파라미터 수집과 변환**

@RequestParam은 파라미터로 사용된 변수의 이름과 전달되는 파라미터의 이름이 다른 경우에 유용하게 사용된다. 

**리스트, 배열 처리**

- 동일한 이름의 파라미터가 여러 개 전달되는 경우 `ArrayList<>` 등을 이용해 처리 가능하다.
- 스프링은 **파라미터의 타입을 보고 객체를 생성** 하므로 파라미터의 타입은 `List<>` 와 같이 **인터페이스 타입이 아닌 실제적인 클래스 타입으로 지정** 해야 한다.

**객체 리스트**

전달하는 데이터가 객체 타입이고 여러 개를 처리해야 한다면 약간의 작업을 통해 한번에 처리를 할 수 있다. 

```java
@Data
public class SampleDTOList {
  private list<SampleDTO> list;
  
  public SampleDTOList() {
    list = new ArrayList<>();
  }
}
```

```java
@GetMapping("/ex02Bean")
public String ex02Bean(SampleDTOList list) {
  log.info("list dtos: " + list);
  return "ex02Bean";
}
```

 파라미터는 '[인덱스]'와 같은 형식으로 전달해서 처리할 수 있다.

```http
프로젝트경로/sample/ex02Bean?list[0].name=aaa&list[2].name=bbb
```

Tomcat은 버전에 따라서 위와 같은 문자열에서 `[]` 문자를 특수문자로 허용하지 않을 수 있다. 따라서 [는 '%5B'로, ]는 '%5D'로 변경한다. 

**@InitBinder**

- 파라미터의 수집을 다른 용어로는 'binding(바인딩)'이라고 한다. 
- 변환이 가능한 데이터는 자동으로 변환되지만 경우에 따라서는 파라미터를 변환해서 처리해야 하는 것도 존재한다. 
- 스프링 Controller에ㅓ는 파라미터를 바인딩할 때 자동으로 호출되는 `@InitBinder` 를 통해서 이러한 변화를 처리할 수 있다. 

```java
@Data
public class TodoDTO {
  private String title;
  private Date dueDate;
}
```

dueDate 변수 타입이 java.util.Date 타입이다. 따라서 사용자가 '2018-01-01'과 같이 들어오는 데이터를 변환하고자 할 때 문제가 발생하게 된다. 이러한 문제를 해결하기 위해서 @InitBinder를 사용한다.

```java
@Controller
@RequestMapping("/sample/*")
@Log4j
public class SampleController {
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    SimpleDateFormat dateFormat = new StimpleDateFormat("yyyy-MM-dd");
    binder.registerCustomEditor(java.util.Date, new CustomDateEditor(dateFormat, false));
  }
  
  //..
  @GetMapping("/ex03")
  public String ex03(TodoDTO todo) {
    log.info("todo: " + todo);
    return "ex03";
  }
}
```

만약 @InitBinder가 처리되지 않는다면 브라우저에서는 400 에러가 발생한다. (400 에러는 요청 구문이 잘못되었다는 의미이다.)

**@DateTimeFormat**

@InitBinder를 이용해서 날짜를 변환할 수도 있지만, 파라미터로 사용되는 인스턴스 변수에 @DateTimeFormat을 적용해도 변환이 가능하다. 이 경우 InitBinder는 필요하지 않다. 

```java
@Data
public class TodoDTO {
  private String title;
  
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private Date dueDate;
}
```

### Model이라는 데이터 전달자

- Model 객체는 JSP와 같은 View에 컨트롤러에서 생성된 데이터를 담아서 전달하는 역할을 한다. 
- 메서드의 파라미터에 Model 타입이 지정된 경우 스프링은 특별하게 Model 타입의 객체를 만들어서 메서드에 주입한다.
- request.setAttribute()와 유사한 역할을 한다. 

servlet에서 모델 2 방식으로 데이터를 전달하는 방식

```java
request.setAttribute("serverTime", new java.util.Date());

ReqeustDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/home.jsp");

dispatcher.forward(request, response);
```

Model을 이용해서 처리하기

```java
public String home(Model model) {
  model.addAttribute("serverTime", new java.util.Date());
  return "home";
}
```

- 메서드의 파라미터를 Model 타입으로 선언하게 되면 자동으로 스프링 MVC에서 Model 타입의 객체를 만들어 주기 때문에 개발자의 입장에서는 필요한 데이터를 담아 주는 작업만으로 모든 작업이 완료된다. 
- **Model은 주로 Controller에 전달된 데이터를 이용해서 추가적인 데이터를 가져와야 하는 상황에 사용한다.** 
  - 리스트 페이지 번호를 파라미터로 전달받고, 실제 데이터를 View로 전달해야 하는 경우
  - 파라미터들에 대한 처리 후 결과를 전달해야 하는 경우

**@ModelAttribute**

- **스프링 MVC의 Controller는 Java Beans 규칙에 맞는 객체는 다시 화면으로 객체를 전달한다.** 
- Java Beans 규칙: 생성자가 없거나 빈 생성자를 가져야 하며, getter/setter를 가진 클래스의 객체
- 전달될 때에는 클래스명의 앞글자는 소문자로 처리된다. 
- 반면 **기본 자료형의 경우 파라미터로 선언하더라도 기본적으로 화면까지 전달되지 않는다.** 
- @ModelAttribute는 강제로 전달받은 파라미터를 Model에 담아서 전달하도록 할 때 필요한 어노테이션이다. 