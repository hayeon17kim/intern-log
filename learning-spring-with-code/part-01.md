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