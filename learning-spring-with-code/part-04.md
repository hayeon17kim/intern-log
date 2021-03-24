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

```java
@RunWith(SpringJUnit4ClassRunner.class)
// Test for Controller
@WebAppConfiguration
@ContextConfiguration(classes = {
        com.hayeon.demo.config.RootConfig.class,
        com.hayeon.demo.config.ServletConfig.class
})
@Slf4j
public class SampleControllerTests {

    @Setter(onMethod_ = {@Autowired})
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testConvert() throws Exception {

        Ticket ticket = new Ticket();
        ticket.setTno(123);
        ticket.setOwner("admin");
        ticket.setGrade("AAA");

        String jsonStr = new Gson().toJson(ticket);
        
        log.info(jsonStr);

        mockMvc.perform(post("/sample/ticket")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonStr))
            .andExpect(status().is(200));
    }
}
```

- `contentType()`: SampleController의 convert()는 JSON으로 전달되는 데이터를 받아서 Ticket 타입으로 변환한다. 이를 위해서 해당 데이터가 JSON이라는 것을 명시해줘야 한다. MockMvc는 contentType()을 이용해서 전달하는 데이터가 무엇인지 알려준다.
- 이처럼 JUnit을 사용하면 Tomcat을 구동하지 않고도 컨트롤러를 구동해볼 수 있다.

#### 기타 도구

- JUnit 외에도 톰캣을 구동한다면 REST 방식을 테스트할 수 있는 여러 방법이 있다.
- Mac이나 Linux의 경우 curl 같은 도구를 이용할 수 있다.
- Chrome 확장 프로그램 중 REST Client를 검색하면 도구를 확인할 수 있다. (Restlet Client 등)

### 다양한 전송방식

REST 방식은 URI와 결합하므로 회원이라는 자원을 대상으로 전송방식을 결합하면 다음과 같은 형태가 된다.

| 작업 | 전송방식 | URI                                   |
| ---- | -------- | ------------------------------------- |
| 등록 | POST     | /member/new                           |
| 조회 | GET      | /member/{id}                          |
| 수정 | PUT      | /members/{id} + body (json 데이터 등) |
| 삭제 | DELETE   | /member/{id}                          |

## Ajax 댓글 처리

### ReplyController의 설계

#### 등록 작업

```java
@PostMapping(value = "/new",
            consumes="application/json",
            produces = {MediaType.TEXT_PLAIN_VALUE})
public ResponseEntity<String> create(@RequestBody ReplyVO vo) {
  log.info("Reply VO: "+ vo);
  int insertCount = service.register(vo);
  log.info("Reply INSERT COUNT: " + insertCount);
  return insertCount == 1
    ? new ResponseEntity<>("success", HttpStatus.OK)
    : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
}
```

- consumes와 produces 를 이용해서 JSON 방식의 데이터만 처리하도록 하고, 문자열을 반환하도록 설계한다.
- 댓글이 추가된 숫자를 확인해서 브라우저에서 200 OK 혹은 500 Internal Server Error 를 반환하도록 한다.

#### 특정 게시물의 댓글 목록 확인

```java
@GetMapping(value = "/pages/{bno}/{page}",
           produces = {
             MediaType.APPLICATION_XML_VALUE,
             MediaType.APPLICATION_JSON_UTF8_VALUE })
public ResponseEntity<List<ReplyVO>> getList(
	@PathVariable("page") int page,
	@PathVariable("bno") Long bno) {
	log.info("getList........");
  Criteria cri = new Criteria(page, 10);
  log.info(cri);
  return new ResponseEntity<>(service.getList(cri, bno), HttpStatus.OK);
} 
```

ReplyController의 getList()는 Criteria를 이용해서 파라미터를 수집하는데 '/{bno}/{page}'의 'page' 값은 Criteria를 생성해서 직접 처리해야 한다. 

#### 댓글 삭제/조회

```java
@GetMapping(value = "/{rno}",
           produces = {
             MediaType.APPLICATION_XML_VALUE,
             MediaType.APPLICATION_JSON_UTF8_VALUE })
public ResponseEntity<ReplyVO> get(
	@PathVariable("rno") Long rno) {
	log.info("get " + rno);
  return new ResponseEntity<>(service.get(rno), HttpStatus.OK);
} 

@DeleteMapping(value = "/{rno}",
           produces = {
             MediaType.TEXT_PLAIN_VALUE })
public ResponseEntity<String> delete(
	@PathVariable("rno") Long rno) {
	log.info("remove " + rno);
 	return service.remove(rno) == 1
    ? new ResponseEntity<>("success", HttpStatus.OK)
    : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_OK);
} 
```

#### 댓글 수정

```java
@RequestMapping( method = { RequestMethod.PUT, RequestMethod.PATCH },
                value = "/{rno}",
                consumes = "application/json"
                produces = { MediaType.TEXT_PLAIN_VALUE })
public ResponseEntity<String> modify(
  @RequestBody ReplyVO vo,
	@PathVariable("rno") Long rno) {
	vo.setRno(rno);
  log.info("rno: " + rno);

 	return service.modify(rno) == 1
    ? new ResponseEntity<>("success", HttpStatus.OK)
    : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_OK);
} 
```

- **댓글 수정은 'PUT' 방식이나 'PATCH' 방식을 이용한다.**
- @RequestBody로 처리되는 데이터는 일반 파라미터나 @PathVariable 파라미터로 처리할 수 없기 때문에 직접 처리해주는 부분을 주의해야 한다. 

### JavaScript 준비

#### JavaScript의 모듈화

- 화면 내에서 JavaScript 처리를 하다 보면 어느 순간 이벤트 처리와 DOM 처리, Ajax 처리 등이 마구 섞여서 유지보수 하기 힘든 코드를 만드는 경우가 많다. 
- 이 경우를 대비해 JavaScript 를 하나의 모듈처럼 구성하는 방식을 이용한다.
- JavaScript에서 가장 많이 사용하는 패턴은 모듈 패턴이다. 
- 모듈 패턴은 관련 있는 함수들을 하나의 모듈처럼 묶음으로 구성하는 것을 의미한다. 
- javaScript의 클로저를 이용하는 대표적인 방법이다. 

webapp/resources/js/reply.js 생성

reply.js는 게시물의 조회 페이지에서 사용하기 위해 작성된 것으로, views/board/get.jsp 파일에 추가한다.

```jsp
<script type="text/javascript" src="/resources/js/reply.js"></script>
```

브라우저에서는 개발자 도구를 이용해서 reply.js가 아무 문제 없이 로딩되고 있는지 확인한다. 

**모듈 구성하기**

- 모듈 패턴은 Java의 클래스처럼 JavaScript 를 이용해서 메서드를 가지는 객체를 구성한다. 
- 모듈 패턴은 JavaScript의 즉시 실행함수와 '{}'를 이용해서 객체를 구성한다.

```js
console.log("Reply Module..........");

var replyService = (function() {
  return {name: "AAAA"};
})();
```

- JavaScript의 즉시 실행함수는 () 안에 함수를 선언하고 바깥쪽에서 실행해버린다.
- 즉시 실행함수는 함수의 실행결과가 바깥쪽에 선언된 변수에 할당된다.
- replyService라는 변수에 name이라는 속성에 'AAAA'라는 속성값을 가진 객체가 할당된다. 
- replyService의 확인은 reply.js를 사용하는 get.jsp를 이용해서 확인이 가능하다.

```jsp
<script type="text/javascript" src="/resources/js/reply.js"></script>

<script type="text/javascript">
  $(document).ready(function() {
    console.log(replyService);
  });
</script>
```

jQuery의 $(document).ready()는 한 페이지 내에서 여러 번 나와도 상관 없기 때문에 기존의 JavaScript코드를 수정하지 않으려면 위와 같이 `<script>` 태그로 분리해도 무방하다. 

개발자 도구 결과

```js
Reply Module..........
{name: "AAAA"}
```

#### reply.js 등록 처리

```js
console.log("Reply Module..........");

var replyService = (function() {
  
  function add(reply, callback, error) {
    console.log("reply..........");
    
    $.ajax({
      type: 'post',
      url; '/replies/new',
      data: JSON.stringify(reply),
      contentType: "application/json; charset=utf-8",
      success: function(result, status, xhr) {
        if (error) {
          error(er);
        }
      }
  	})
  }
  
  return {add: add};
})();
```

- 외부에서는 replyService.add(객체, 콜백)를 전달하는 형태로 호출할 수 있는데, Ajax 호출은 감춰져 있기 때문에 코드를 좀 더 깔끔하게 작성할 수 있다.
- reply.js 내의 add 함수는 Ajax 를 이용해서 POST 방식으로 호출하는 코드를 작성한다.

- add()
  - 데이터 전송 타입: 'application/json; charset=utf-8' 
  - 파라미터로 callback과 error를 함수로 받을 것이다.
  - 만일 Ajax 호출이 성공하고, callback 값으로 적절한 함수가 존재한다면 해당 함수를 호출해서 결과를 반영한다.
  - JavaScript는 함수의 파라미터 개수를 일치시킬 필요가 없기 때문에 callback이나 error와 같은 파라미터는 필요에 따라 작성할 수 있다.

```jsp
<script type="text/javascript" src="/resources/js/reply.js"></script>

<script type="text/javascript">
  console.log("============");
  console.log("JS TEST");
  
  var bnoValue = '<c:out value="${board.bno}"/>'
  
  // for replyService add test
  replyService.add(
  {reply: "JS TEST", replyer:"tester", bno:bnoValue}
  ,
  function(result){
    alert("RESULT: " + result);
  }
  );
</script>
```

get.jsp 내부에서는 Ajax 호출은 replyService 라는 이름의 객체에 감춰져 있으므로 필요한 파라미터들만 전달하는 형태로 간결해진다. replyService의 add()에 던져야 하는 파라미터는 JavaScript의 객체 타입으로 만들어서 전송해주고, Ajax 전송 결과를 처리하는 함수를 파라미터로 같이 전달한다.

#### 댓글의 목록 처리

reply.js

```js
console.log("Reply Module..........");

var replyService = (function() {
  
  function add(reply, callback, error) {
    console.log("reply..........");
    //..
  }
  
  function getList(param, callback, error) {
    
    var bno = param.bno;
    
    var page = param.page || 1;
    
    $.getJSON("/replies/pages/" + bno + "/" + page + ".json",
         funciton(data) {
            if (callback) {
      				callback(data);
    				}
          }).fail(funciton(xhr, status, err) {
        if (error) {
          error();
        }
      });
		}
  
  return {
    add: add,
    getList: getList
  };
})();
```

- getList()는 param이라는 객체를 통해 필요한 파라미터를 전달받아 JSON 목록을 호출한다.
- JSON 형태가 필요하므로 URL 호출 시 확장자를 '.json'으로 요구한다.
- 댓글 등록과 마찬가지로 get.jsp에서는 해당 게시물의 모든 댓글을 가져오는지 확인하는 코드를 작성한다.

```jsp
<script type="text/javascript" src="/resources/js/reply.js"></script>

<script type="text/javascript">
  console.log("============");
  console.log("JS TEST");
  
  var bnoValue = '<c:out value="${board.bno}"/>'
  
  replyService.getList({bno:bnoValue, page:1}, function(list) {
    
    for (var i = 0; len = list.length||0; i < len; i++) {
      console.log(list[i]);
    }
  });
  
</script>
```

#### 댓글 삭제와 갱신

```js
console.log("Reply Module..........");

var replyService = (function() {
  
  function add(reply, callback, error) {
    console.log("reply..........");
    //..
  }
  
  function getList(param, callback, error) {
    
    //..
	}
  
  function remove(rno, callback, error) {
    $.ajax({
      type: 'delete',
      url: '/replies/' + rno,
      success: function(deleteResult, status, xhr) {
        if (callback) {
          callback(deleteResult);
        }
      },
      error: function(xhr, status, er) {
        if (error) {
          error(er);
        }
      }
    });
  }
  
  return {
    add: add,
    getList: getList,
    remove: remove
  };
})();
```

remove는 DELETE 방식으로 데이터를 전달하므로 $.ajax를 이용해서 구체적으로 type 속성으로 'delete'를 지정한다. board/get.jsp에서는 반드시 실제 데이터베이스에 있는 댓글 번호를 이용해서 정상적으로 댓글이 삭제되는지를 확인한다. 

```jsp
<script type="text/javascript" src="/resources/js/reply.js"></script>

<script type="text/javascript">
  console.log("============");
  console.log("JS TEST");
  
  var bnoValue = '<c:out value="${board.bno}"/>'
  
  // for replyService add test
  //.. 생략
  
  // reply List Test
  // .. 생략
  
  // 23번 댓글 삭제 테스트
  replyService.remove( 23, function(count) {
    console.log(count);
    
    if (count === "success") {
      alert("REMOVED");
    }
    
  }, function(error) {
    alert('ERROR...');
  });
</script>
```

#### 댓글 수정

```js
console.log("Reply Module..........");

var replyService = (function() {
  
  function add(reply, callback, error) {
    console.log("reply..........");
    //..
  }
  
  function getList(param, callback, error) {
    
    //..
	}
  
  function remove(rno, callback, error) {
		//..
  }
  
  function update(reply, callback, error) {
    console.log("RNO: " + reply.rno);
    
    $.ajax({
      type: 'put',
      url: '/replies/' + reply.rno,
      data: JSON.stringify(reply),
      contentType: "application/json; charset=utf-8",
      success: function(result, status, xhr) {
        if (callback) {
          callback(result);
        }
      },
      error: function(xhr, status, er) {
        if (error) {
          error(er);
        }
      }
    });
  }
  
  return {
    add: add,
    getList: getList,
    remove: remove,
    update: update
  };
})();
```

댓글은 수정하는 내용이 댓글의 내용밖에 없지만 JS 객체로 처리하는 방식을 이용한다.

```jsp
<script type="text/javascript" src="/resources/js/reply.js"></script>

<script type="text/javascript">
  console.log("============");
  console.log("JS TEST");
  
  var bnoValue = '<c:out value="${board.bno}"/>'
  
  //.. 생략

  // 22번 댓글 수정
  replyService.update({
    rno: 22,
    bno: bnoValue,
    reply: "Modified Reply..."
  }, function(result) {
    console.log("수정 완료...");
    
  }, function(error) {
    alert('ERROR...');
  });
</script>
```

#### 댓글 조회 처리

```js
console.log("Reply Module..........");

var replyService = (function() {
  
  function add(reply, callback, error) {
    console.log("reply..........");
    //..
  }
  
  function getList(param, callback, error) {
    //..
	}
  
  function remove(rno, callback, error) {
		//..
  }
  
  function update(reply, callback, error) {
    //..
  }
  
  function get(rno, callback, error) {
    $.get("/replies/" + rno + ".json", function(result) {
      if (callback) {
        callback(result);
      }
    }).fail(function(xhr, status, error) {
      if (error) {
        error();
      }
    });
  }
  
  return {
    add: add,
    getList: getList,
    remove: remove,
    update: update,
    get: get
  };
})();
```

get.jsp에는 단순히 댓글의 번호만을 전달한다.

```js
replyService.get(10, function(data) {
  console.log(data);
});
```

### 이벤트 처리와 HTML 처리

게시글의 조회 페이지가 열리면 자동으로 댓글 목록을 가져와서 `<li>` 태그를 구성해야 한다. 이에 대한 처리는 `$(document).ready()` 내에서 이루어 지도록 한다. 

```jsp
<script type="text/javascript" src="/resources/js/reply.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
    
    var bnoValue = '<c:out value="${board.bno}"/>';
    var reply = $(".chat");
    
    showList(1);
    
    function showList(page) {
      
      replyService.getList({bno: bnoValue, page: page || 1}, function(list) {
        
        var str = "";
        
        if (list == null || list.length == 0) {
          replyUL.html("");
          return;
        }
        
        for (var i = 0; len = list.length || 0; i < len; i++) {
          str += "<li class='left clearfix' data-rno='"+list[i].rno+"'>";
          str += "	<div><div class='header'><strong class='primary-font'>"+list[i].replyer+"</strong>";
          str += "		<small class='pull-right text-muted'>" + list[i].replyDate+"</small></div>";
          str += "		<p>"+list[i].reply+"</p></div></li>";
        }
        replyUL.html(str);
      }); // end function
    } // end showList
  });
</script>
```

- showList()는 페이지 번호를 파라미터로 받도록 설계하고, 만일 파라미터가 없는 경우에는 자동으로 1페이지가 되도록 설계한다.
- 브라우저에서 DOM 처리가 끝나면 자동으로 showList()가 호출되면서 `<ul>`태그 내에 내용으로 처리된다. 만일 1페이지가 아닌 경우 기존 `<ul>`에 `<li>` 들이 추가되는 형태이다. 

**시간에 대한 처리**

XML이나 JSON 형태로 데이터를 받을 때는 순수하게 숫자로 표현되는 시간 값이 나오게 되어 있으므로, 화면에서는 이를 변환하여 사용하는 것이 좋다. 

```js
console.log("Reply Module..........");

var replyService = (function() {
  
  function add(reply, callback, error) {
    console.log("reply..........");
    //..
  }
  
  function getList(param, callback, error) {
    //..
	}
  
  function remove(rno, callback, error) {
		//..
  }
  
  function update(reply, callback, error) {
    //..
  }
  
  function get(rno, callback, error) {
		//..
  }
  
  function displayTime(timeValue) {
    var today = new Date();
    var gap = today.getTime() - timeValue;
    var dateObj = new Date(timeValue);
    var str = "";
    
    if (gap < (1000 * 60 * 60 * 24)) {
      var hh = dateObj.getHours();
      var mi = dateObj.getMinutes();
      var ss = dateObj.getSeconds();
      
      return [ (hh > 9 ? '' : '0'), ":", (mi > 9 ? '' : '0') + mi, ':',
             (ss > 9 ? '' : '0') + ss ].join('');
    } else {
      var yy = dateObj.getFullYear();
      var mm = dateObj.getMonth(); // getMonth() is zero-based
      var dd = dateObj.getDate();
      
      return [ yy, '/', (mm > 9 ? '' : '0') + mm, '/', (dd > 9 ? '' : '0') + dd].join();
    }
  }
  
  return {
    add: add,
    getList: getList,
    remove: remove,
    update: update,
    get: get,
    displayTime: displayTime
  };
})();
```

displayTime()은 Ajax에서 데이터를 가져와서 HTML을 만들어 주는 부분에  `replyServie.displayTime(list[i].replyDate)`의 형태로 적용한다. 이를 사용하면 24시간이 지난 댓글은 날짜만 표시되고, 24시간 이내의 글은 시간으로 표시된다. 

### 댓글의 페이징 처리

댓글의 숫자가 많다면 데이터베이스에서 많은 양의 데이터를 가져와야 하고, 이는 성능상의 문제를 가져올 수 있다. 이를 페이징 처리를 이용해서 처리한다. 

#### 데이터베이스의 인덱스 설계

- tbl_reply테이블에 접근할 때 댓글 번호가 중심이 아니라 게시물의 번호가 중심이 된다.
- 만일 bno 값이 100번인 게시물의 댓글들을 보면 `PK_REPLY` 를 이요해서 검색을 하다 보니 중간에 있는 다른 게시물의 번호들은 건너 뛰어 가면서 특정 게시물의 댓글들을 찾아야만 한다. 만일 데이터가 많아진다면 성능에 문제가 생길 수 있다. 
- 효율을 높이고 싶다면 게시물의 번호에 맞게 댓글들을 모아서 빠르게 찾을 수 있는 구조로 만드는 것이 좋다.

![yRe1PAb6RR2Yq0PHrjwAcA_thumb_a522](https://user-images.githubusercontent.com/50407047/112316584-95e74980-8cee-11eb-99ae-8e094eac8b4d.jpg)

- 인덱스를 다음과 같이 생성한다.

```sql
create index idx_reply on tbl_reply (bno desc, rno asc);
```

#### 인덱스를 이용한 페이징 쿼리

인덱스를 이용하는 이유 중 하나는 정렬을 피할 수 있기 때문이다. 특정한 게시물의 rno의 순번대로 데이터를 조회하고 싶다면 다음과 같은 쿼리를 작성한다.

```sql
select /*+INDEX(tbl_reply idx_reply)*/
	rownum rn, bno, rno, reply, replyer, replyDate, updatedate
from tbl_rply
where bno = 3145745 --(게시물 번호)
and rno > 0
```

테이블에 접근해서 결과를 만들 때 생성되는 ROWNUM은 가장 낮은 rno 값을 가지는 데이터가 1번이 된다. ROWNUM이 원하는 순서대로 나오기 때문에 페이징 처리는 이전에 게시물 페이징과 동일한 형태로 작성할 수 있다. 예를 들어 10개씩 2페이지를 가져온다면 다음과 같은 쿼리를 작성한다.

```sql
select rno, bno, reply, replyer, replydate, updatedate
from
(
  select /*+INDEX(tbl_reply idx_reply)*/
    rownum rn, bno, rno, reply, replyer, replyDate, updatedate
  from tbl_rply
  where bno = 3145745 --(게시물 번호)
        and rno > 0
        and rownum <= 20
) where rn > 10
```

BoardMapper.xml

```xml
<select id="getListWithPaging" resultType="org.zerock.domain.ReplyVO">
    <![CDATA[
    select rno, bno, reply, replyer, replydate, updatedate
  from
  (
    select /*+INDEX(tbl_reply idx_reply)*/
      rownum rn, bno, rno, reply, replyer, replyDate, updatedate
    from tbl_rply
    where bno = #{bno}
          and rno > 0
          and rownum <= #{cri.pageNum} * #{cri.amount}
  ) where rn > (#{cri.pageNum} - 1) * #{cri.amount}
]]>
</select>
```

ReplyMapperTests

```java
@Test
public void testList2() {
  Criteria cri = new Criteria(2, 10);
  
  List<ReplyVO> replies = mapper.getListWithPaging(cri, 3145754L);
  
  replies.forEach(reply -> log.info(reply));
}
```

ReplyMapper

댓글을 페이징 처리하기 위해서는 해당 게시물의 전체 댓글의 숫자를 파악해서 화면에 보여줄 필요가 있다. 

```java
public interface ReplyMapper {
  public List<ReplyVO> getListWithPaging(
    @Param("cri") Criteria cri,
    @Param("bno") Long bno
  );
  
  public int getCountByBno(Long bno);
}
```

ReplyMapper.xml

```xml
<select id="getCountByBno" resultType="int">
  <![CDATA[
	select count(rno) from tbl_reply where bno = #{bno}
	]]>
</select>
```

#### ReplyServiceImpl 에서 댓글과 댓글 수 처리

ReplyPageDTO

단순히 댓글 전체를 보여주는 방식과 달리 댓글의 페이징 처리가 필요한 경우 댓글 목록과 함께 전체 댓글의 수를 같이 전달해야 한다. ReplyService 인터페이스와 구현 클래스인 ReplyServiceImpl 클래스는 `List<ReplyVO>` 와 댓글의 수를 같이 전달할 수 있는 구조로 변경한다. 

```java
@Data
@AllArgsConstructor
@Getter
public class ReplyPageDTO {
  private int replyCnt;
  private List<ReplyVO> list;
}
```

ReplyService인터페이스와  ReplyServiceImpl 클래스에는 ReplyPageDTO를 반환하는 메서드를 추가한다.

```java
@Service
@Log4j
public class ReplyServiceImpl implements ReplyService {
  
  @Setter(onMethod_ = @Autowired)
  private ReplyMapper mapper;
  
  @Override
  public ReplyPageDTO getListPage(Criteria cri, Long bno) {
    return new ReplyPageDTO(
      mapper.getCountByBno(bno),
      mapper.getListWithPaging(cri, bno)
    );
  }
}
```

#### ReplyController 수정

```java
@GetMapping(value = "/pages/{bno}/{page}")
public ResponseEntity<ReplyPageDTO> getList(@PathVariable("page") int page, @PathVariable("bno") Long bno) {
  
  Criteria cri = new Criteria(page, 10);
  log.info("get Reply List bno: " + bno);
  log.info("cri: " + cri);
  return new ResponseEntity<>(service.getListPage(cri, bno), HttpStatus.OK);
}
```

### 댓글 페이지의 화면 처리

reply.js

```js
function getList(param, callback, error) {

  var bno = param.bno;

  var page = param.page || 1;

  $.getJSON("/replies/pages/" + bno + "/" + page + ".json",
            funciton(data) {
            if (callback) {
    						//callback(data) // 댓글 목록만 가져오는 경우
    						callback(data.replyCnt, data.list); // 댓글 숫자와 목록을 가져오는 경우
  					}
}).fail(funciton(xhr, status, err) {
        if (error) {
  error();
}
```

get.jsp

```js
	$(document).ready(function() {
    
    var bnoValue = '<c:out value="${board.bno}"/>';
    var reply = $(".chat");
    
    showList(1);
    
    function showList(page) {
      
      replyService.getList({bno: bnoValue, page: page || 1}, function(replyCnt, list) {
        
        console.log("replyCnt: " + replyCnt);
        console.log("list: " + list);
        console.log("list");
        
        if (page == -1) {
          pageNum = Math.ceil(replyCnt/10.0);
          showList(pageNum);
          return;
        }
        
        var str = "";
        
        if (list == null || list.length == 0) {
          replyUL.html("");
          return;
        }
        
        for (var i = 0; len = list.length || 0; i < len; i++) {
          str += "<li class='left clearfix' data-rno='"+list[i].rno+"'>";
          str += "	<div><div class='header'><strong class='primary-font'>"+list[i].replyer+"</strong>";
          str += "		<small class='pull-right text-muted'>" + list[i].replyDate+"</small></div>";
          str += "		<p>"+list[i].reply+"</p></div></li>";
        }
        replyUL.html(str);
      }); // end function
    } // end showList
  });
```

- 만일 page 번호가 '-1'로 전달되면 마지막 페이지를 찾아서 다시 호출한다. 
- 사용자가 새로운 댓글을 추가하 showList(-1)을 호출하여 우선 전체 댓글의 숫자를 파악하게 한다. 
- 이후 다시 마지막 페이지를 호출해서 이동시키는 방식으로 동작시킨다.
- 이 방식은 여러 번 서버를 호출해야 하는 단점이 있기는 하지만, 댓글의 등록 행위가 댓글 조회나 페이징에 비해 적기 때문에 심각한 문제는 아니다.

```js
modalRegisterBtn.on("click", function(e) {
  
  var reply = {
    reply: modalInputReply.val(),
    replyer: modalInputReplyer.val(),
    bno: bnoValue
  };
  replyService.add(reply, function(result) {
    
    alert(result);
    
    modal.find("input").val("");
    modal.modal("hide");
    
    //showList(1);
    showList(-1);
  });
  
});
```

get.jsp의 일부

```js
var pageNum = 1;
var replyPageFooter = $(".panel-footer");

function showReplyPage(replyCnt) {
  
  var endNum = Math.ceil(pageNum / 10) * 10;
  var startNum = endNum - 9;
  
  var prev = startNum != 1;
  var next = false;
  
  if (endNum * 10 >= replyCnt) {
    endNum = Math.ceil(replyCnt/10.0);
  }
  
  if (endNum * 10 < replyCnt) {
    next = true;
  }
  
  var str = "<ul class='pgination pull-right'>";
  
  if (prev) {
    str += "<li class='page-item'><a class='page-link' href='"+(startNum - 1)+"'>Previous</a></li>";
  }
  
  if (next) {
    str += "<li class='page-item'><a class='page-link' href='"+(endNum + 1)"'>Next</a></li>";
  }
  
  str += "</ul></div>";
  
  console.log(str);
  
  replyPageFooter.html(str);
}
```

showList()의 마지막에 페이지를 출력한다.

```js
        for (var i = 0; len = list.length || 0; i < len; i++) {
          //..
        }
        replyUL.html(str);
        
        showReplyPage(replyCnt);
```

페이지의 번호를 클릭했을 때 새로운 댓글을 가져오도록 만든다.

get.jsp

```js
replyPageFooter.on("click", "li a", function(e) {
  e.preventDefault();
  console.log("page click");
  
  var targetPageNum = $(this).attr("href");
  
  console.log("targetPageNum: " + targetPageNum);
  
  pageNum = targetPageNum;
  
  showList(pageNum);
})
```

이벤트 처리에서는  a 태그의 기본 동작을 제한하고 댓글 페이지 번호를 변경한 후 해당 페이지 댓글을 가져온다.

#### 댓글의 수정과 삭제

댓글이 페이지 처리되면 댓글의 수정과 삭제 시에도 현재 댓글이 포함된 페이지로 이동하도록 수정한다.

```js
modalModBtn.on("click", function(e) {
  
  var reply = {rno:modal.data("rno"), reply: modalInputReply.val()};
  
  replyService.update(reply, function(result) {
    alert(result);
    modal.modal("hide");
    showList(pageNum);
  });
});

modalRemoveBtn.on("click", function(e) {
  
  var rno = modal.data("rno");
  
  replyService.remove(rno, function(result) {
    alert(result);
    modal.modal("hide");
    showList(pageNum);
  });
});

```

