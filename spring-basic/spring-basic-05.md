# Section 5. 웹 애플리케이션과 싱글톤

## 웹 애플리케이션과 싱글톤

- 스프링은 기업용 온라인 서비스 기술을 지원하기 위해 탄생했다. 

  > 애플리케이션의 종류
  >
  > - 웹 애플리케이션
  > - 데몬 애플리케이션
  > - 배치 애플리케이션

- 대부분의 스프링 애플리케이션은 웹 애플리케이션이다. **웹이 아닌 애플리케이션 개발**(데몬, 배치)도 얼마든지 개발할 수 있다.
- **웹 애플리케이션**은 보통 **여러 고객이 동시에 요청**한다.

![image](https://user-images.githubusercontent.com/50407047/113119716-b7df4f80-924b-11eb-8a2b-4ea319a2da5a.png)

위와 같이 요청이 올 때마다 계속 객체를 만들어내고 있다. 이를 확인하기 위해 다음과 같이 테스트를 해본다.

**스프링 없는 순수한 DI 컨테이너 테스트**

```java
// 스프링 없는 순수한 DI 컨테이너의 문제점을 알아보자!
@Test
@DisplayName("스프링 없는 순수한 DI 컨테이너")
void pureContainer() {
  AppConfig appConfig = new AppConfig();

  // 1. 조회: 호출할 때마다 객체를 생성
  MemberService memberService1 = appConfig.memberService();

  // 2. 조회: 호출할 때마다 객체를 생성
  MemberService memberService2 = appConfig.memberService();

        // memberService1 != memberService2
 Assertions.assertThat(memberService1).isNotSameAs(memberService2);
}
```

스프링 없는 순수한 DI 컨테이너인 AppConfig는 요청을 할 때마다 객체를 새로 생성한다. 고객 트래픽이 초당 100이 나오면 초당 100개 객체가 생성되고 소멸된다!  => 메모리 낭비가 심하다.

해당 객체를 1개만 생성되고, 공유하도록 설계하면 된다. => 싱글톤 패턴

## 싱글톤 패턴

싱글톤은 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴이다.

그래서 객체 인스턴스를 2개 이상 생성하지 못하도록 막아야 한다. `private` 생성자를 사용해서 오치부에서 임의로 `new` 키워드를 사용하지 못하도록 막아야 한다.

```java
public class SingletonService {
    // 자기 자신을 내부에 private static 으로 갖는다.
    // static: 클래스 레벨에 올라가기 때문에 딱 하나만 존재한다.
    //
    private static final SingletonService instance = new SingletonService();

    // 인스턴스를 참조할 수 있는 방법은 이 메서드를 호출하는 방법밖에 없다.
    public static SingletonService getInstance() {
        return instance;
    }

    // 생성자가 private으로 선언해서 외부에서 new 키워드로 객체 인스턴스가 생성되는 것을 막는다. 
    private SingletonService() {

    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
```

- static 영역에 객체 instance를 미리 하나 생성해서 올려둔다.
- 이 객체 인스턴스가 필요하면 오직 `getInstance()` 메서드를 통해서만 조회할 수 있다. 이 메서드를 호출하면 항상 같은 인스턴스를 반환한다.
- 딱 1개의 객체 인스턴스만 존재해야 하므로, 생성자를 `private`으로 막아서 혹시라도 외부에서 `new` 키워드로 객체 인스턴스가 생성되는 것을 막는다. 

```java
@Test
@DisplayName("싱글톤 패턴을 적용한 객체 사용")
void singletonServiceTest() {
  //컴파일 오류 발생
  //new SingletonService();
  SingletonService singletonService1 = SingletonService.getInstance();
  SingletonService singletonService2 = SingletonService.getInstance();

  Assertions.assertThat(singletonService1).isSameAs(singletonService2);
}
```

- same: `==` 연산자로 비교
- equal: `equals()` 메서드로 비교

그럼 싱글톤 패턴을 적용하기 위해서는 AppConfig에서 객체를 static 필드로 선언하고 생성자를 `private`으로 막아두고 `getInstance()` 메서드를 통해 최초 한번 생성된 객체를 사용하도록 만들어야 할까? 그럴 필요가 없다.

**스프링 컨테이너가 기본적으로 객체를 싱글톤으로 만들어서 관리해준다.** 고객 요청이 아무리 많아도 있는 객체를 그대로 사용한다. 

**싱글톤 패턴의 문제점**

- 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
- 의존관계상 클라이언트가 구체 클래스에 의존한다. => DIP를 위반한다. (클라이언트가 `구체클래스.getInstance()`를 호출해야 한다)
- 클라이언트가 구체 클래스에 의존해서 OCP 원칙을 위반할 가능성이 높다.
- 테스트하기 어렵다.
- 내부 속성을 변경하거나 초기화하기 어렵다.
- `private` 생성자로 자식 클래스를 만들기 어렵다.
- 결국 유연성이 떨어진다.
- 안티패턴이라고도 부른다.

스프링 프레임워크는 위와 같은 단점을 모두 보완하면서 객체를 싱글톤으로 관리해준다.

### 싱글톤 컨테이너

스프링 컨테이너는 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤으로 관리한다.

**싱글톤 컨테이너**

- 스프링 컨테이너는 싱글턴 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리한다.
- 스프링 컨테이너는 싱글톤 컨테이너 역할을 한다. 이렇게 싱글톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리라고 한다.
- 스프링 컨테이너의 이런 기능 덕분에 싱글톤 패턴의 모든 단점을 해결하면서 객체를 싱글톤으로 유지할 수 있다.
  - 싱글톤 패턴을 위한 지저분한 코드가 들어가지 않아도 된다.
  - DIP, OCP, private 생성자로부터 자유롭게 싱글톤을 사용할 수 있다.

**싱글톤 컨테이너 적용 후**

![image](https://user-images.githubusercontent.com/50407047/113150860-78c2f580-926f-11eb-9104-60f60fdad9f6.png)

스프링 컨테이너 덕에 고객의 요청이 올 때마다 객체를 생성하는 것이 아니라 이미 만들어진 객체를 공유해서 효율적으로 재사용할 수 있다.

스프링 기본 빈 등록 방식은 싱글톤이지만, 싱글톤만 지원하는 것은 아니다. 요청할 때마다 새로운 객체를 생성해서 반환하는 기능도 제공한다. 

> HttpRequest 혹은 HttpSession의 Life Cycle에 맞추는 방식도 있다. 이것을 Scope이라고 하는데, 그것은 나중에 설명해 주실 것



## 싱글톤 방식의 주의점

싱글톤 패턴이든, 스프링 같은 싱글톤 컨테이너를 사용하든, 객체 인스턴스를 하나만 생성해서 공유하는 싱글톤 방식은 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문에 싱글톤 객체는 상태를 유지(stateful)하게 설계하면 안 된다. 

**무상태(stateless)로 설계**해야 한다.

- 특정 클라이언트에 의존적인 필드가 있으면 안 된다. => 특정 클라이언트가 값을 바꾸게 두면 안 된다. 
- 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안 된다.
- 가급적 읽기만 가능해야 한다.
- 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등을 사용해야 한다.

스프링 빈의 필드에 공유 값을 설정하면 정말 큰 장애가 발생할 수 있다.

```java
public class StatefulService {
    private int price; // 상태를 유지하는 필드

    public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        this.price = price; // 여기가 문제!
    }

    public int getPrice() {
        return price;
    }
}
```

```java
public class StatefulServiceTest {
    @Test
    void statefulServiceSingleton() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
            AppConfig.class);

        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA: A사용자 10000원 주문
        statefulService1.order("userA", 10000);
        // ThreadB: B사용자 20000원 주문
        statefulService2.order("userB", 20000);

        // ThreadA: 사용자A가 주문 금액 조회
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);

        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig {
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}
```

- ThreadA가 사용자A 코드를 호출하고 ThreadB가 사용자B 코드를 호출한다 가정하자.
-  `StatefulService`의 `price` 필드는 공유되는 필드인데, 특정 클라이언트가 값을 변경한다.
- 사용자A의 주문금액은 10000원이 되어야 한는데, 20000원이라는 결과가 나온다.
- 실에서 이로 인해 해결하기 어려운 문제들이 터진다.
- 공유필드는 정말 조심해야 한다! 스프링 빈은 항상 무상태(stateless)로 설계한다.

다음과 같이 변경한다.

```java
public class StatefulService {
    //private int price; // 상태를 유지하는 필드

    public int order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        //this.price = price; // 여기가 문제!
        return price;
    }

//    public int getPrice() {
//        return price;
//    }
}
```

```java
public class StatefulServiceTest {
    @Test
    void statefulServiceSingleton() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
            AppConfig.class);

        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA: A사용자 10000원 주문
        int userAPrice = statefulService1.order("userA", 10000);
        // ThreadB: B사용자 20000원 주문
        int userBPrice = statefulService2.order("userB", 20000);

        // ThreadA: 사용자A가 주문 금액 조회
        //int price = statefulService1.getPrice();
        System.out.println("user A price = " + userAPrice);
        System.out.println("user B price = " + userBPrice);
        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig {
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}

```

## @Configuration과 싱글톤

만약 memberService() 를 호출하면 memberRepository를 호출할 것이다. 그러면 new MemoryMemberRepository()를 호출할 것이다. 

```java
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }

}
```

- @Bean memberService -> new MemoryMemberRepository()
- @Bean orderService -> new MemoryMemberRepository()

결과적으로 각각 다른 2개의 MemoryMemberRepository가 생성되면서 싱글톤이 깨지는 것처럼 보인다. 

```java
public class ConfigurationSingletonTest {
    @Test
    void configurationTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
            AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = memberService.getMemberRepository();

        System.out.println("memberService -> memberRepository = " + memberRepository1);
        System.out.println("orderService -> memberRepository = " + memberRepository2);
        System.out.println("memberRepository = " + memberRepository);

        Assertions.assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        Assertions.assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
    }
}
```

확인해보면 memberRepository 인스턴스는 모두 같은 인스턴스가 공유되어 사용된다.  어떻게 이런 일이 일어나게 되는 걸까?

**AppConfig에 호출 로그 남김**

```java
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        System.out.println("call AppConfig.discountPolicy");
        return new RateDiscountPolicy();
    }

}
```

예상

```console
call AppConfig.memberService
call AppConfig.memberRepository
call AppConfig.memberRepository
call AppConfig.orderService
call AppConfig.memberRepository
call AppConfig.discountPolicy
```

 실제 호출 결과 

```console
AppConfig.memberService
AppConfig.memberRepository
AppConfig.orderService
AppConfig.discountPolicy
```

## @Configuration과 바이트코드 조작의 마법

> registry: 기재, 등기, 등기우편

```java
    @Test
    void configurationDeep() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
            AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);

        System.out.println("bean = " + bean.getClass());
    }
```

```java
bean = class hello.core.AppConfig$$EnhancerBySpringCGLIB$$58ae7df8
```

순수한 클래스라면 `class hello.core.AppConfig`라 나와야 한다. 예상과는 다르게 클래스 명에 xxxCGLIB가 붙으면서 복잡해졌다. 

내가 등록한 객체는 사라지고 그걸 상속받은 다른 애가 등록된다. 그 임의의 다른 클래스가 바로 싱글톤이 보장되도록 해준다. 다음과 같이 바이트 코드를 조작해서 작성되어 있을 것이다.

**AppConfig@CGLIB 예상 코드**

```java
@Bean
public MemberRepository memberRepository() {
  if (memoryMemberRepository가 이미 스프링 컨테이너에 등록되어 있으면?) {
    return 스프링 컨테이너에서 찾아 반환;
  } else {
    기존 로직을 호출해서 MemoryMemberRepository를 생성하고 스프링 컨테이너에 등록
    return 반환
  }
}
```

`@Bean`이 붙은 메서드마다 이미 스프링 빈이 존재하면 존재하는 빈을 찾아 반환하고, 스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 동적으로 만들어진다.

덕분에 싱글톤이 보장되는 것이다.

> **`AppConfig@CGLIB`는 `AppConfig`의 자식 타입이기 때문에, AppConfig 타입으로 조회할 수 있다.**

**`@Configuration`을 적용하지 않고, `@Bean`만 적용한다면 어떻게 될까?** 안 붙여도 Spring Container에 등록이 된다. 그러나 `@Configuration`을 붙이지 않으면 바이트 코드를 조작하는 CGLIB 기술 없이 순수한 AppConfig로 스프링 빈에 등록된다.

```java
//@Configuration 삭제
public class AppConfig {
  
}
```

실행결과

```console
bean = class hello.core.AppConfig
```

싱글톤이 깨져버린다. MemberRepository는 3번 호출이 되고, 

그리고 이제 MemberServiceImpl의 MemberRepository는 **스프링에서 관리해주는 스프링 빈이 아니다.** MemberServiceImpl에서 `new MemberRepository()`로 생성해준 코드와 마찬가지이다

> 스프링 빈이란?
>
> 스프링 컨테이너(Spring Container)에 의해서 자바 객체가 만들어지게 되면 이 객체를 스프링 빈이라고 한다.

결론

- @Bean만 사용해도 스프링빈으로 등록되지만, 싱글톤을 보장하지 않는다.
- `memberRepository()` 처럼 의존관계 주입이 필요해서 메서드를 직접 호출할 때 싱글톤을 보장하지 않는다.
- 스프링 설정 정보는 항상 `@Configuration`을 사용하도록 하자. 

