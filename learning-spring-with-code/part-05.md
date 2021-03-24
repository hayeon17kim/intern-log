# AOP와 트랜잭션

## AOP라는 패러다임

- 관점(Aspect): 개발자에게는 관심사(concern)라는 말로 통용된다.
- 관심사는 개발 시 필요한 고민이나 염두에 두어야 하는 일이다.
  - 파라미터가 올바르게 들어왔을까?
  - 이 작업을 하는 사용자가 적절한 권한을 가진 사용자인가?
  - 이 작업에서 발생할 수 있는 모든 예외는 어떻게 처리해야 하는가?
- 이 고민은 핵심 로직은 아니지만, 코드를 온전하게 만들기 위해서 필요한 고민이다.
- AOP가 추구하는 것은 관심사의 분리(separate concerns)이다.
- AOP는 개발자가 염두에 두어야 하는 일을 별도의 관심사로 분리하고, 핵심 비즈니스 로직만을 작성할 것을 권장한다. 
- 예) 나눗셈 구현 시 핵심 로직은 두 개의 숫자를 나누는 것이지만, 주변 로직은 0을 나누는 것이 아닌지 체크하는 것이다.
- AOP는 관심사와 비즈니스 로직을 별도의 코드로 작성하고, 실행할 때 이를 결합하는 방식으로 접근한다.;
- 실제 실행은 결합된 상태의 코드가 실행되기 때문에 개발자들은 핵심 비즈니스 로직에만 근거해서 코드를 작성하고, 나머지는 어떤 관심사들과 결합할 것인지를 설정하는 것 만으로 모든 개발을 마칠 수 있게 된다.
- 별도의 복잡한 설정이나 제약 없이 스프링 내에서 간편하게 AOP의 기능을 구현할 수 있다.

### AOP 용어들

- 개발자 입장에서 AOP를 적용한다는 것은 기존의 코드를 수정하지 않고도 원하는 관심사(cross-concern)을 엮을 수 잇다는 점이다.
- Target: 개발자가 작성한 핵심 비즈니스 로직을 가지는 객체이다. 순수 비즈니스 로직을 의미하고, 어떠한 관심사와도 관계를 맺지 않는다. 순수한 코어이다.
- Proxy: Target을 전체적으로 감싸고 있는 존재이다. Proxy는 내부적으로 Target을 호출하지만, 중간에 필요한 관심사들을 거쳐서 Target을 호출하도록 자동 혹은 수동으로 작성된다.  Proxy의 존재는 직접 코드를 통해서 구현하기도 하지만, 대부분의 경우 스프링 AOP 기능을 이용해 자동으로 생성되는(auto-proxy) 방식을 이용한다.
- JoinPoint: Target 객체가 가진 메서드이다. 외부에서의 호출은 Proxy 객체를 통해서 Target 객체의 JoinPoint를 호출하는 방식이다. Target에는 여러 메서드가 존재하기 때문에 어떤 메서드에 관심사를 결합할 것인지를 결정해야 하는데, 이 결정을 Pointcut이라고 한다.
- PointCut: 관심사와 비즈니스 로직이 결합되는 지점을 결정하는 것이다. Proxy는 결합이 완성된 상태이므로 메서드를 호출하게 되면 자동으로 관심사가 결합된 상태로 동작하게 된다. 
- Advice: 실제 걱정거리를 분리해 놓은 코드를 의미한다. 관심사는 Aspect와 Advice로 표현되어 있는데, Aspect는 조금 추상적인 개념을 의미한다면 Advice는 Aspect를 구현한 코드라고 볼 수 있다. Advice는 동작 위치에 따라 다음과 같이 구분된다.
  - Before Advice: Target과 JoinPont를 호출하기 전에 실행되는 코드이다. 코드의 실행 자체에는 관여할 수 없다.
  - After Returning Advice: 모든 실행이 정상적으로 이루어진 후에 동작하는 코드이다.
  - After Throwing Advice: 예외가 발생한 뒤에 동작하는 코드이다.
  - After Advice: 정상적으로 실행되거나 예외가 발생했을 때 구분 없이 실행되는 코드이다.
  - Around Advice: 메서드의 실행 자체를 제어할 수 있는 가장 강력한 코드이다. 직접 대상 메서드를 호출하고 결과나 예외를 처리할 수 있다. 



- Advice는 과거 별도의 인터페이스로 구현되고, 이를 클래스로 구현하는 방식으로 제작했으나 스프링 버전 3부터는 어노테이션만으로도 모든 설정이 가능하다. 
- Target에 어떤 Advice를 적용할 것인지는 XML 과 어노테이션 방식이 있다.
- Pointcut 은 Advice를 어떤 JoinPoint에 결합할 것인지를 결정하는 설정이다. 
- Pointcut은 다양한 형태로 선언해서 사용할 수 있는데 주로 사용되는 설정은 다음과 같다.
  - execution(@execution): 메서드를 기준으로 Pointcut을 설정한다.
  - within(@within): 특정한 타입(클래스)을 기준으로 Pointcut 을 설정한다.
  - this: 주어진 인터페이스를 구현한 객체를 대상으로 Pointcut을 설정한다. 
  - args(@args): 특정한 파라미터를 가지는 대상들만을  Pointcut으로 설정한다.
  - @annotation: 특정한 어노테이션이 적용된 대상들만을 Pointcut으로 설정한다.

### AOP 실습

- AOP 기능은 일반적인 Java API를 이용하는 클래스(POJO)에 적용한다.
- Controller의 경우 인터셉터나 필터 등을 이용한다.
- 예제에서는 서비스 계층에 AOP를 적용한다.
  - 서비스 계층의 메서드 호출 시 모든 파라미터들을 로그로 기록하고,
  - 메서드 실행 시간을 기록한다,

#### 예제 프로젝트 생성

- 스프링의 AOP는 AspectJ라는 라이브러리의 도움을 받는다.
- 스프링은 AOP 처리가 된 객체를 생성할 때 AspectJ Weaver 라이브러리의 도움을 받으므로 pom.xml에 추가한다.

```java
package org.zerock.aop;

@Aspect
@Log4j
@Component
public class LogAdvice {
  @Before("execution(* org.zerock.service.SampleService*.*(...))")
  public void logBefore() {
    log.info("================");
  }
}
```

- @Aspect: 해당 클래스의 객체가 Aspect를 구현한 것임을 나타내기 위해서 사용한다.
- @Component: AOP와는 관계 없지만 스프링에서 빈으로 인식하기 위해서 사용한다.
- @Before: BeforeAdvice를 구현한 메서드에 추가한다.
- Advice와 관련된 어노테이션들은 내부적으로 Pointcut을 지정한다. Pointcut은 별도의  @Pointcut으로 지정해서 사용할 수도 잇다.
- @Before 내부의 'ececution...': AspectJ의 표현식이다. execution의 경우 접근제한자와 특정 클래스의 메서드를 지정할 수 있다. 맨 앞의 `*`는 접근제한자를 의미하고, 맨 뒤의 `*`는 클래스의 이름과 메서드를 의미한다.



### AOP 설정

- Proxy  객체를 만들어주는 설정을 추가한다.
- 프로젝트의 root-context.xml을 선택해서 네임스페이스에 aop와 context를 추가한다. 
- root-context.xml에서는 component-scan을 이용해서 org.zerock.service와 org.zerock.aop를 스캔한다. 이 과정에서 SampleServiceImpl 클래스와 LogAdvice는 스프링의 빈으로 등록될 것이고, LogAdvice에 설정한 @Before가 동작하게 된다.

Java 설정

```java
@Configuration
@ComponentScan(basepackages={"org.zerock.service"})
@ComponentScan(basepackages={"org.zerock.service"})
@EnableAspectJAutoProxy
@MapperScan(basepackages={"org.zerock.mapper"})
public class RootConfig {
  //..
}
```

### AOP 테스트

- 테스트에서 가장 먼저 작성해야 하는 코드는 AOP 설정을 한 Target에 대해서 Proxy 객체가 정상적으로 만들어져 잇는지를 확인하는 것이다.
- `<aop:aspectj-autoproxy>`가 정상적으로 모든 동작을 하고, LogAdvice에 설정 문제가 없다면 service  변수의 클래스는 단순히 org.zerock.service.SampleService.Impl의 인스턴스가 아닌 생성된 Proxy 클래스의 인스턴스가 된다. 

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
@Log4j
public class SampleControllerTests {

  @Setter(onMethod_ = {@Autowired})
  private SampleService service;
  
  @Test
  public void testClass() {
    log.info(service);
    log.info(service.getClass().getName());
  }
}
```

결과는 다음과 같다.

```java
INFO : org.zerock.service.SampleServiceTests - org.zerock.service.SampleServiceImpl@31ea9581
INFO : org.zerock.service.SampleServiceTests - com.sun.proxy.$Proxy20
```

## 스프링에서 트랜잭션 관리

트랜잭션의 성격을 ACID 원칙으로 설명하곤 한다.

- 원자성(ACID): 하나의 단위로 실행되어야 한다.
- 일관성(Consistency): 트랜잭션이 성공했다면 데이터베이스의 모든 데이터는 일관성을 유지해야만 한다.
- 격리(Isolation): 트랜잭션으로 처리되는 중간에 외부에서의 간섭은 없어야 한다.
- 용속성(Durability): 트랜잭션이 성공적으로 처리되면, 그 결과는 영속적으로 보관되어야 한다. 

### 데이터베이스 설계와 트랜잭션

- 정규화: 중복된 데이터를 제거해서 데이터 저장의 효율을 올린다.
  - 테이블은 늘어나고
  - 각 테이블의 데이터 양은 줄어든다.
- 정규화 시 칼럼으로 처리되지 않는 데이터
  - 시간이 흐르면 변경되는 데이터 (ex: 나이)
  - 계산이 가능한 데이터
  - 누구에게나 정해진 값 (ex: 201년 1월 1일은 '월요일' 이지만 동일한 시간을 모든 사람들에게 통용되기 때문에 기록 X)
- 정규화가 잘 되었거나, 위와 같은 규칙들이 반영된 데이터베이스의 설계에서는 트랜잭션이 많이 일어나지는 않는다. 
- 정규화가 진행될수록 테이블은 점점 더 순수한 형태가 되어가는데, 순수한 형태가 될수록 '트랜잭션 처리'의 대상에서 멀어진다.
- 정규화를 진행할 수록 테이블은 간결해지만 쿼리할 때는 불편해진다. 현재 상황을 알기 위해서는 조인이나 서브쿼리를 이용해서 처리해야 하기 때문이다.
- 조인이나 서브쿼리를 이용하면 다시 성능의 이슈가 발생한다. 이 경우 역정규화를 한다. 중복이나 계산되는 값을 데이터베이스 상에 보관하고, 대신 조인이나 서브쿼리의 사용을 줄인다.
- 보통 정규화의 규칙을 따른다면 게시물 테이블과 댓글 테이블에는 각 정보만으로 칼럼이 구성될 것이다. 그러나 문제가 있다. 보통 게시물의 목록에 페이지에서 댓글의 숫자도 같이 표시된다. 이런 상황에서는 board 테이블에 댓글의 숫자를 칼럼으로 처리한다. 그럼 게시글 목록을 가져올 경우 tbl_reply 테이블을 이용할 일이 없기 때문에 성능상으로 이득을 본다. 대신 댓글이 추가될 때 reply 테이블에 insert하고, 댓글의 숫자는 board 테이블에 update 시켜주는 작업이 필요하다. 두 작업은 하나의 트랜잭션으로 관리되어야 한다.
- 반정규화는 일처럼 중복이나 계산의 결과를 미리 보관해서 좀 더 빠른 결과를 얻기 위한 노력이다. 

 ### 트랜잭션 설정 실습

```java
@Transactional
@Override
public void addData(String value) {
  log.info("mapper1");
  mapper1.insertCol1(value);
  log.info("mapper2");
  mapper2.insertCol2(value);
}
```

- Transactional 이 추가된 후에는 실행 시 중간에 실패하면 rollback된다.

#### @Transactional 어노테이션 속성들

- Propagation
- Isolation
- Read-only: true인 경우 insert, update, delete 실행 시 예외 발생. 기본 설정은 false
- Rollback-for-예외: 특정 예외가 발생 시 강제로 rollback
- No-rollback-for-예외: 특정 예외의 발생 시 Rollback 처리되지 않음



#### @Transactional 적용 순서

- 메서드의 @Transactional 설정이 가장 우선시된다.
- 클래스의 @Transactional  설정이 메서드보다 우선순위가 낮다.
- 인터페이스의 @Transactional 설정이 가장 낮은 우선순위이다. 

인터페이스에는 가장 기준이 되는 @Transactional과 같은 설정을 지정하고, 클래스나 메서드에 필요한 어노테이션을 처리하는 것이 좋다. 



## 댓글과 댓글 수에 대한 처리

- board 테이블에 댓글의 수를 의미하는 replyCnt 칼럼을 추가한다.
- Board 도메인 객체에도 필드를 추가한다.

BoardMapper 인터페이스에 updateReplyCnt()를 추가한다.

```java
public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
```

해당 게시물의 번호인 bno와 증가나 감소를 의미하는 amount 변수에 파라미터를 받을 수 있또록 처리한다. 댓글이 등록되면 1이 증가하고, 댓글이 삭제되면 1이 감소하기 때문이다.

MyBatis의 SQL을 처리하기 위해서는 기본적으로 하나의 파라미터 타입을 사용하기 때문에 위와 같이 2개 이상의 데이터를 전달하려면 @Param이라는 어노테이션을 이용해서 처리할 수 있다.

```xml
<update id="updateReplyCnt">
  update tbl_board set replycnt = replycnt + #{amount} where bno = #{bno}
</update>
```

#### ReplyServiceImpl의 트랜잭션 처리

ReplyServiceImpl이 반정규화 처리가 되면서  ReplyMapper와 BoardMapper를 같이 이용해야 하는 상황이 생겼다. BoardServiceImpl에서 새로운 댓글이 추가되거나 삭제되는 상황이 되면 BoardMapper와 ReplyMapper를 같이 이용해서 처리하고, 이 작업은 트랜잭션으로 처리되어야 한다.

```java
@Transactional
@Override
public int register(ReplyVO vo) {
  log.info("register...." + vo);
  boardMapper.updateReplyCnt(vo.getBno(), 1);
  return mapper.insert(vo);
}

@Transactional
@Override
public int remove(ReplyVO vo) {
  log.info("register...." + vo);
  ReplyVO vo = mapper.read(rno);
  boardMapper.updateReplyCnt(vo.getBno(), -1);
  return mapper.delete(vo);
}
```

댓글의 삭제는 전달되는 파라미터가 댓글의 번호인 rno만을 받기 때문에 해당 댓글의 게시물을 알아내는 과정이 필요하다. 