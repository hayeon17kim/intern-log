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

> **스프링은 빈을 생성하고, 의존관계를 주입하는 단계가 나누어져 있다.** 그런데 이렇게 자바 코드로 스프링 빈을 등록하면 생성자를 호출하면서 의존관계 주입도 한번에 처리된다. => 의존관계 자동 주입

## 컨테이너에 등록된 모든 빈 조회

> JUnit5 부터는 `public` 설정을 하지 않아도 된다.