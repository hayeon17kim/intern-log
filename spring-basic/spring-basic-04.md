# Section 4. 스프링 컨테이너와 스프링 빈

## 스프링 컨테이너의 생성

```java
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
```

- `ApplicationContext`를 스프링 컨테이너라 하고, 이는 인터페이스이다.
- 스프링 컨테이너를 생성할 때는 구성 정보를 지정해 줘야 한다.스프링 컨테이너는 XML 혹은 애노테이션 기반의 자바 설정 클래스로 만들 수 있다.
- 위 코드에서는 `AppConfig`를 기반으로 스프링 컨테이너(`ApplicationContext`)를 만든 것이다
- `AnnotationApplicationContext`는 `ApplicationContext`의 구현체이다.

> 스프링 컨테이너를 더 정확히는 `BeanFactory`, `ApplicationContext`로 구분할 수 있다. 최상의 `BeanFactory`를 직접 사용하는 경우는 거의 없으므로 일반적으로 `ApplicationContext`를 스프링 컨테이너라고 부른다.

**스프링 컨테이너의 생성 과정**

1. 스프링 컨테이너 생성: AppConfig으로 스프링 컨테이너(ApplicationContext)를 생성한다. 

   ```java
   public class OrderApp {
   
       public static void main(String[] args) {
   //        AppConfig appConfig = new AppConfig();
   //        MemberService memberService = appConfig.memberService();
   //        OrderService orderService = appConfig.orderService();
           ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
   
           MemberService memberService = applicationContext
               .getBean("memberService", MemberService.class);
           OrderService orderService = applicationContext.getBean("orderService", OrderService.class);
   ```

2. 스프링 빈 등록: 파라미터로 넘어온 설정 클래스 정보를 사용해서 빈을 등록한다.  `@Bean` 붙은 것을 죄다 호출한다. 

   > 빈 이름은 메서드 이름을 사용하고, 직접 부여할 수도 있다. (`@Bean(name="memberService2")`) 단, 빈 이름은 **항상 다른 이름을 부여**해야 한다. **같은 이름을 부여하면, 다른 빈이 무시되거나, 기존 빈을 덮어버리거나 설정에 따라 오류가 발생한다(최근 스프링).** 

   > 실무에서는 간단하고 명확하게 개발한다. 그냥 빈 이름을 애초에 같게 만들지 말라! 

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

3. 스프링 빈 의존관계 설정 - 준비

4. 스프링 빈 의존관계 설정 - 완료: 스프링 컨테이너는 설정 정보를 참고해서 의존관계를 주입한다. 단순히 자바 코드를 호출하는 것 같지만, 차이가 있다. => 싱글톤 컨테이너

> **스프링은 빈을 생성하고, 의존관계를 주입하는 단계가 나누어져 있다.** 그런데 이렇게 자바 코드로 스프링 빈을 등록하면 생성자를 호출하면서 의존관계 주입도 한번에 처리된다. => 의존관계 자동 주

## 컨테이너에 등록된 모든 빈 조회

스프링 컨테이너에 실제 스프링 빈들이 잘 등록되었는지 확인해본다.

> JUnit5 부터는 `public` 설정을 하지 않아도 된다.

```java
public class ApplicationContextInfoTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    // JUnit5 부터는 public 설정을 하지 않아도 된다.
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("name = " + beanDefinitionName + " object = " + bean);
        }
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            // ROLE_APPLICATION: 직접 등록한 애플리케이션 빈
            // ROLE_INFRASTRUCTURE: 스프링 내부에서 사용하는 빈
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name = " + beanDefinitionName + " object = " + bean);
            }
        }
    }

}
```

> beanDefinition: bean에 대한 메타데이터 정보. 

애플리케이션 빈 출력 결과

```console
name = appConfig object = hello.core.AppConfig$$EnhancerBySpringCGLIB$$f03b4cae@24d09c1
name = memberService object = hello.core.member.MemberServiceImpl@54c62d71
name = memberRepository object = hello.core.member.MemoryMemberRepository@65045a87
name = orderService object = hello.core.order.OrderServiceImpl@47f4e407
name = discountPolicy object = hello.core.discount.RateDiscountPolicy@2d1dee39
```

- 모든 빈 출력하기
  - 스프링에 등록된 모든 빈 정보를 출력할 수 있다.
  - `ac.getBeanDefinitionNames()`: 스프링에 등록된 모든 빈 이름을 조회한다.
  - `ac.getBean()`: 빈 이름으로 빈 객체(인스턴스)를 조회한다.
- 애플리케이션 빈 출력하기
  - 스프링이 내부에서 사용하는 빈은 제외하고, 내가 등록한 빈만 출력한다.
  - 스프링이 내부에서 사용하는 빈은 `getRole()`로 구분할 수 있다.
    - `ROLE_APPLICATION`: 일반적으로 사용자가 정의한 빈
    - `ROLE_INFRASTRUCTURE`: 스프링이 내부에서 사용하는 빈

## 스프링 빈 조회 - 기본

스프링 컨테이너에서 스프링 빈을 찾는 가장 기본적인 방법

- `ac.getBean(빈 이름, 타입)`
- `ac.getBean(타입)`

조회 대상 스프링 빈이 없으면 예외가 발생한다.

```java
NoSuchBeanDefinitionException: No bean named 'xxxx' available
```

```java
public class ApplicationContextBasicFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName() {
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("이름 없이 타입만으로 조회")
    void findBeanByType() {
        MemberService memberService = ac.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanByName2() {
        MemberService memberService = ac.getBean(MemberServiceImpl.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("빈 이름으로 조회X")
    void findBeanByNameX() {
        //ac.getBean(MemberServiceImpl.class);
      
      // 두번째 파라미터를 실행하면 첫번째 파라미터 예외가 발생해야 한다는 의미.
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () ->
            ac.getBean("xxxx", MemberService.class));
    }
}
```

구체 타입으로 조회하면 변경 시 유연성이 떨어진다. 추상에 의존하는 것이 아니라 구체에 의존하고 있기 때문에 만약 구현체를 다른 구현체로 대체한다면 테스트 코드도 변경해줘야 한다. 

## 스프링 빈 조회 - 동일한 타입이 둘 이상

- 타입으로 조회 시 같은 타입의 스프링 빈이 둘 이상이면 오류가 발생한다. 이때는 빈 이름을 지정하자.
- `ac.getBeansOfType()`을 사용하면 해당 타입의 모든 빈을 조회할 수 있다.

```java
public class ApplicationContextSameBeanFindTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SameBeanConfig.class);

    @Test
    @DisplayName("타입으로 조회 시 같은 타입이 둘 이상 있으면, 중복 오류가 발생한다.")
    void findBeanByTypeDuplicate() {
        assertThrows(NoUniqueBeanDefinitionException.class,
            () -> ac.getBean(MemberRepository.class));
    }

    @Test
    @DisplayName("타입으로 조회 시 같은 타입이 둘 이상 있으면, 빈 이름을 지정하면 된다.")
    void findBeanByName() {
        MemberRepository memberRepository = ac
            .getBean("memberRepository1", MemberRepository.class);
        assertThat(memberRepository).isInstanceOf(MemberRepository.class);
    }

    @Test
    @DisplayName("특정 타입을 모두 조회하기")
    void findAllBeanByType() {
        Map<String, MemberRepository> beansOfType = ac.getBeansOfType(MemberRepository.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType.get(key));
        }
        System.out.println("beansOfType = " + beansOfType);
        assertThat(beansOfType.size()).isEqualTo(2);
    }

    @Configuration
    static class SameBeanConfig {
        @Bean
        public MemberRepository memberRepository1() {
            return new MemoryMemberRepository();
        }
        @Bean
        public MemberRepository memberRepository2() {
            return new MemoryMemberRepository();
        }
    }

}
```

한번에 조회하는 기능을 설명하는 이유는, 나중에 @Autowired 설명할 때 자동 주입에 이런 것들이 다 사용이 되기 때문이다. 

## 스프링 빈 조회 - 상속관계

- **부모 타입으로 조회하면, 자식 타입도 함께 조회한다.**
- 그래서 모든 자바 객체의 최고 부모인 `Object` 타입으로 조회하면, 모든 스프링 빈을 조회한다.

> 나중에 자동 의존관계 주입할 때 또 나옴!

```java
public class ApplicationContextExtendsFindTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

    @Test
    @DisplayName("부모 타입으로 조회시, 자식이 둘 이상 있으면, 중복 오류가 발생한다.")
    void findBeanByParentTypeDuplicate() {
        DiscountPolicy bean = ac.getBean(DiscountPolicy.class);
        assertThrows(NoUniqueBeanDefinitionException.class, () -> ac.getBean(DiscountPolicy.class));
    }


    @Test
    @DisplayName("부모 타입으로 조회시, 자식이 둘 이상 있으면, 빈 이름을 지정하면 된다.")
    void findBeanByParentTypeBeanName() {
        DiscountPolicy rateDiscountPolicy = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);
        assertThrows(NoUniqueBeanDefinitionException.class, () -> ac.getBean(DiscountPolicy.class));
    }

    // 특정 하위 타입으로 조회하는 것은 좋지 않은 방법이다.
    @Test
    @DisplayName("특정 하위 타입으로 조회")
    void findBeanBySubType() {
        RateDiscountPolicy bean = ac.getBean(RateDiscountPolicy.class);
        assertThat(bean).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회하기")
    void findBeanByParentType() {
        Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
        assertThat(beansOfType.size()).isEqualTo(2);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType);
        }
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회하기 - Object")
    void findBeanByObjectType() {
        Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(Object.class);
        assertThat(beansOfType.size()).isEqualTo(2);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType);
        }
    }

    @Configuration
    static class TestConfig {
        // 인터페이스를 리턴타입으로 지정하는 이유?
        // 역할과 구현을 항상 쪼개야 한다.
        // 그래야 다른 데에서 의존관계를 주입할 때에도 리턴 타입만 보면 되기 때문이다.
        @Bean
        public DiscountPolicy rateDiscountPolicy() {
            return new RateDiscountPolicy();
        }
        @Bean
        public DiscountPolicy fixDiscountPolicy() {
            return new FixDiscountPolicy();
        }
    }
}
```

사실 개발자가 ApplicationContext에서 직접 getBean을 할 일은 거의 없다. 스프링 컨테이너가 자동 의존관계 주입해주는 것을 쓸 수도 있다. 다른 방법은 다음과 같이 객체에 의존 관계 주입에 대한 코드를 전혀 작성하지 않고, 의존 관계에 대한 코드는 Config에 작성을 하는 것이다. 그럼에도 불구하고 배운 이유는 이것이 기본 기능이기도 하고, 가끔 순수한 자바 애플리케이션에서 스프링 컨테이너를 생성해서 쓸 일이 있기도 하기 때문이다.

```java
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository,
        DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
```

### BeanFactory와 ApplicationContext

### BeanFactory

- 스프링 컨테이너의 **최상위 인터페이스**이다. 
- **스프링 빈을 관리하고 조회하는 역할**을 담당한다.
- `getBean()`을 제공한다.
- 지금까지 사용했던 대부분의 기능은 BeanFactory가 제공하는 기능이다.

### ApplicationContext

- BeanFactory 기능을 상속받는다.
- ApplicationContext는 빈 관리 기능 + 편리한 부가 기능을 제공한다.
- BeanFactory를 직접 사용할 일은 거의 없다. 부가기능이 포함된  ApplicationContext를 사용한다.
- BeanFactory나 ApplicationContext 를 스프링 컨테이너라 한다.

**ApplicationContext가 제공하는 부가기능**

- Message Source: 메시지소스를 활용한 국제화 기능
  - 한국에서 들어오면 한국어로, 영어권에서 들어오면 영어로 출력
- EnvironmentCapable: 환경 변수
  - 로컬, 개발 , 운영 데이터베이스 등을 구분해서 처리
- ApplicationEventPublisher: 애플리케이션 이벤트
  - 이벤트를 발행하고 구독하는 모델을 편리하게 지원
- ResourceLoader: 편리한 리소스 조회
  - 파일, 클래스패스, 외부 등에서 리소스를 편리하게 조회

ApplicationContext는 위 인터페이스들을 받고 있다.  





## 다양한 설정 지원 - 자바 코드, XML

스프링 컨테이너는 다양한 형식의 설정 정보를 받아들일 수 있게 유연하게 설계되어 있다. (자바 코드, XML, Groovy 등)

![image](https://user-images.githubusercontent.com/50407047/113030805-f122ab80-91c8-11eb-8f93-3ca1867df3b4.png)

- 애노테이션 기반 자바 코드 설정 사용
  - AnnotationConfigApplication 클래스를 사용하면서 자바 코드로 된 설정 정보를 넘기면 된다.
- XML 설정 사용
  - XML을 사용하면 **컴파일 없이 빈 설정 정보를 변경할 수 있다는 장점**이 있다.
  - `GenericXmlApplicationContext`를 사용하면서 `xml`설정 파일을 넘기면 된다. 