# REST 방식과 Ajax를 이용하는 댓글 처리 

## 서버 역할의 변화

- 과거에는 서버의 데이터를 소비하는 주체가 '브라우저'라는 특정한 애플리케이션으로 제한적이었다면, 모바일 시대가 되면서 웹은 서버에서 제공하는 데이터를 소비하게 된다. 
- 과거 서버는 브라우저라는 하나의 대상만을 상대로 데이터를 제공했기 때문에 아예 브라우저가 소화 가능한 HTML이라는 형태로 전달하고, 브라우저는 이를 화면에 보여주는 역할을 했다.
- 스마트폰에서는 앱을 이용해 데이터를 소비하게 되고, 보이는 화면 역시 자신만의 방식으로 서비스하게 된다. 앱에서 서버에게 기대하는 것은 완성된 HTML이 아니라 자신에게 필요한 순수한 데이터만을 요구하게 되었다. 이처럼 서버의 역할은 점점 더 순수하게 데이터에 대한 처리를 목적으로 하는 형태로 진화하고 있다. 또한, 브라우저와 앱은 서버에서 전달하는 데이터를 이용해서 앱 혹은 브라우저 내부에서 별도의 방식을 통해 이를 소비하는 형태로 전환하고 있다. 
- 웹의 URI 의미도 다르게 변화하기 시작했다. 과거 웹페이지의 경우 페이지를 이동하더라도 브라우저의 주소는 변화하지 않는 방식을 선호했다. (ex: 네이버 카페). 반면 최근 웹페이지들은 대부분 페이지를 이동하면서 브라우저 내의 주소 역시 같이 이동하는 방식을 사용한다.

> URL은 URI의 하위 개념이다. URL은 '이 곳에 가면 당신이 원하는 것을 찾을 수 잇습니다'와 같은 상징적인 의미가 좀 더 강하다면,  URI는 '당신이 원하는 곳의 주소는 여기입니다'와 같이 좀 더 현실적이고 구체적인 의미가 있다. URI의 I는 데이터베이스의 PK와 같다.

- REST는 Representational State Transfer의 약어로 하나의 URI는 하나의 고유한 리소스(Resource)를 대표하도록 설계된다는 개념에 전송방식을 결합해서 원하는 작업을 지정한다. 예를 들어 'board/123'은 게시물 중 123번이라는 고유한 의미를 가지도록 설계하고, 이에 대한 처리는 GET, POST 방식과 같이 추가적인 정보를 통해 결정한다. 
- REST 방식은 다음과 같이 구성된다.
- URI + GET/POST/PUT/DELETE...
- 스프링은 REST 방식의 데이터 처리를 위한 여러 종류의 어노테이션과 기능이 있다.
  - `@RestController`: Controller가 REST 방식을 처리하기 위한 것임을 명시한다.
  - `@ResponseBody`:  일반적인 JSP와 같은 뷰로 전달되는 게 아니라 데이터 자체를 전달하기 위한 용도
  - `@PathVariable`: URL 경로에 있는 값을 파라미터로 추출하려고 할 때 사용
  - `@CrossOrigin`: Ajax의 크로스 도메인 문제를 해결해주는 어노테이션
  - `@RequestBody`: JSON 데이터를 원하는 타입으로 바인딩 처리

### @RestController

- 스프링4부터 @RestController라는 어노테이션을 추가해서 해당 Controller의 모든 메서드의 리턴 타입을 기존과 다르게 처리한다는 것을 명시한다. 
- 이전에는 @Controller와 메서드 선언부에 @ResponseBody를 이용해서 동일한 결과를 만들 수 있었다. 
- @RestController는 메서드의 리턴 타입으로 사용자가 정의한 클래스 타입을 사용할 수 이쏙, 이를 JSON이나 XML로 자동으로 처리할 수 있다.

#### 예제 프로젝트 준비

- JSON 데이터를 처리하기 위한 jackson-databind라는 라이브러리를 pom.xml에 추가한다. 이 라이브러리는 나중에 브라우저에 객체를 JSON이라는 포맷의 문자열로 변환시켜 전송할 때 필요하다. 

> JSON: JavaScript Object Notation의 약자로 구조가 잇는 데이터를 '{}'로 묶고 키와 값으로 구성하는 경량의 데이터 포맷이다. 구**조를 표현한 문자열은 프로그래밍 언어에 관계 없이 사용**할 수 있기 때문에 XML과 더불어 가장 많이 사용되는 데이터의 표현 방식이다. 

- 테스트할 때는 직접 Java 인스턴스를 JSON 타입의 문자열로 변환해야 하는 일들도 있으므로 gson 라이브러리도 추가한다. 

```java
@RestController
@RequestMapping("/sample")
@Slf4j
public class SampleController {
    
    @GetMapping(value = "/getText", produces = "text/plain; charset=UTF-8")
    public String getText() {
        log.info("MIME TYPE: " + MediaType.TEXT_PLAIN_VALUE);
        return "안녕하세요";
    }
}
```

- GetMapping에 사용된 produces 속성은 해당 메서드가 생산하는 MIME 타입을 말한다. 문자열로 직접 지정할 수도 있고, 메서드 내의 MediaType이라는 클래스를 이용할 수도 있다. 

#### 객체의 반환

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleVO {
    private Integer mno;
    private String firstName;
    private String lastName;
}
```

SampleVO 클래스는 비어 있는 생성자를 만들기 위한 @NoArgsConstructor와 모든 속성을 사용하는 생성자를 위한 @AllArgsConstructor 어노테이션을 이용한다.

SampleController

```java
@GetMapping(value = "/getSample")
public SampleVO getSample() {
  return new SampleVO(112, "스타", "로드");
}
```

결과는 다음과 같다.

```json
{
  mno: 112,
  firstName: "스타",
  lastName: "로드"
}
```

#### 컬렉션 타입의 객체 반환

배열, 리스트, 맵 타입의 객체들을 전송하는 경우이다.

```java
@GetMapping(value = "/getList")
public List<SampleVO> getList() {
  return IntStream.range(1, 10).mapToObj(i -> new SampleVO(i, i + "First", i + "Last"))
    .collect(Collectors.toList());
}
```

결과는 다음과 같다.

```json
[
  {
    mno: 1,
    firstName: "1First",
    lastName: "1Last"
  },
  {
    mno: 2,
    firstName: "2First",
    lastName: "2Last"
  },
  {
    mno: 3,
    firstName: "3First",
    lastName: "3Last"
  },
  //....
  {
    mno: 9,
    firstName: "9First",
    lastName: "9Last"
  }
]
```

맵의 경우는 키와 값을 가지는 하나의 객체로 간주된다.

```java
@GetMapping(value= "/getMap")
public Map<String, SampleVO>  getMap() {
  Map<String, SampleVO> map = new HashMap<>();
  map.put("First", new SampleVO(111, "그루트", "주니어"));
  return map;
}
```

결과는 다음과 같다.

#### ResponseEntity 타입

REST 방식으로 호출하는 경우 화면 자체가 아니라 데이터 자체를 전송하는 방식으로 처리되기 때문에, 데이터를 요청한 쪽에는 **정상적인 데이터인지 비정상적인 데이터인지** 를 구분할 수 있는 확실한 방법을 제공해야 한다.

ResponseEntity는 **데이터와 함께 HTTP헤더의 상태 메시지 등을 같이 전달**하는 용도로 사용한다. HTTP의 상태 코드와 에러 메시지 등을 함께 데이터를 전달할 수 있기 때문에 **받는 입장에서는 확실하게 결과를 알 수 있다.** 

```java
@GetMapping(value="/check", params = {"height", "weight"})
public ResponseEntity<SampleVO> check(Double height, Double weight) {

  SampleVO vo = new SampleVO(0, "" + height, "" + weight);

  ResponseEntity<SampleVO> result = null;

  if (height < 150) {
    result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
  } else {
    result = ResponseEntity.status(HttpStatus.OK).body(vo);
  }

  return result;
}
```

- `check()`은 반드시 'height'와 'weight'를 파라미터로 전달받는다.
- 만일 'height'값이 50보다 작다면 502(bad gateway) 상태 코드와 데이터를 전달하고, 그렇지 않다면 200(ok)를 전달한다.

### @RestController에서 파라미터

@RestController는 기존 @Controller에 추가로 몇 가지 어노테이션을 이용한다.

- @PathVariable: 일반 컨트롤러에서도 사용이 가능하지만 REST 방식에서 자주 사용한다. URL 경로의 일부를 파라미터로 사용할 때 이용
- @RequestBody: JSON 데이터를 원하는 타입의 객체로 변환해야 하는 경우에 주로 사용

#### @PathVariable

REST 방식에서는 URL 자체에 4데이터를 식별할 수 있는 정보들을 표현하는 경우가 많으므로 다양한 방식으로 @PathVariable이 사용된다.

```java
@GetMapping("/product/{cat}/{pid}")
public String[] getPath(
  @PathVariable("cat") String cat,
  @PathVariable("pid") Integer pid
) {
  return new String[] { "category: " + cat, "produceId: " + pid };
}
```

- @PathVariable을 적용하고 싶은 경우에는 `{}`을 이용해서 변수명을 지정하고, @PathVariable을 이용해서 지정된 이름의 변숫값을 얻을 수 있다.
- **값을 얻을 때에 원시타입은 사용할 수 없다.** 

다음은 `/sample/product/bags/123`으로 요청 시 결과이다.

```json
[
"category: bags",
"produceId: 123"
]
```

#### @RequestBody

- @RequestBody는 전달된 요청의 내용을 이용해서 파라미터의 타입으로 변환을 요구한다. 
- 내부적으로 HttpMessageConverter 타입의 객체들을 이용해서 다양한 포맷의 입력 데이터를 변환할 수 있다.
- 대부분 JSON 데이터를 서버에 보내서 원하는 타입의 객체로 변환하는 용도로 사용된다.
- 경우에 따라서 원하는 포맷의 데이터를 보내고, 이를 해석해서 원하는 타입으로 사용하기도 한다. 

```java
@PostMapping("/ticket")
public Ticket convert(@RequestBody Ticket ticket) {
  log.info("convert..........ticket" + ticket);
  return ticket;
}
```

다른 메서드와 달리 @PostMapping이 사용되었는데, @RequestBody가 말 그대로 요청한 내용(body)를 처리하기 때문에 일반적인 파라미터 전달방식을 사용할 수 없기 때문이다.

### REST 방식의 테스트

