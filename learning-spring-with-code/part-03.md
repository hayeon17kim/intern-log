## 스프링 MVC 프로젝트의 기본 구성

- 웹 프로젝트는 3 tier 방식으로 구성한다.
- Presentation Tier: 화면에 보여주는 기술을 사용하는 영역이다. 프로젝트 성격에 맞춰 앱으로 제작하거나, CS로 구성되는 경우도 있다. Servlet/JSP나 스프링 MVC가 담당하는 영역이다.
- Business Tier: 순수한 비즈니스 로직을 담고 있는 영역이다. 고객이 원하는 요구사항을 반영하는 계층이기 때문에 중요하다. 영역의 설계는 고객의 요구 사항과 일치해야 한다. 메서드의 이름은 고객들이 사용하는 용어를 그대로 사용한다.
- Persistance Tier: 데이터를 어떤 방식으로 보관하고, 사용하는가에 대한 설계가 들어간다. 
- 각 영역은 별도의 설정을 가지는 단위로 볼 수 있다.
- 스프링 Core 영역은 흔히 POJO의 영역이다. 스프링의 의존성 주입을 이용해서 객체 간의 연관구조를 완성해서 사용한다. 

**3 tier로 구성하는 이유**

유지보수 때문에 3 tier로 구성한다. 각 영역은 독립적으로 설계되어 나중에 특정한 기술이 변하더라도 필요한 부분을 전자제품의 부품처럼 쉽게 교환할 수 있다. 따라서 각 영역은 설계 당시부터 영역을 구분하고, 해당 연결 부위는 인터페이스를 이용해서 설계한다.

### 각 영역의 Naming Convention

- xxxController
- xxxService, xxxServiceImpl
- xxxDAO, xxxRepository: DAO(Data-Acess-Object)나 Repository(저장소)라는 이름으로 영역을 따로 구성하는 것이 보편적이다. 다만 여기서는 별도의 DAO를 구성하지 않고 MyBatis의 Mapper 인터페이스를 활용한다.
- **VO, DTO: VO와 DTO는 데이터를 담고 있는 객체를 의미한다는 공통점이 있다. 다만, VO의 경우 Read Only의 목적이 강하고, 데이터 자체도 Immutable하게 설계하는 것이 정석이다. DTO는 주로 데이터 수집의 용도가 좀 더 강하다. 웹 화면에서 로그인하는 정보를 DAO로 처리한다.** 

### 패키지의 Naming Convention

프로젝트의 규모가 커져서 많은 Service 클래스와 Controller 들이 혼재한다면 비즈니스를 단위별로 구분하고 다시 내부에 Controller, Service 등으로 나눈다. 이 방식은 담당자가 명확해지고, 독립적인 설정을 가지는 형태로 개발하기 때문에 큰 규모에 적합하다.

```
org.zerok.config
org.zerok.controller
org.zerok.service
org.zerok.domain
org.zerok.persistance
org.zerok.exception
org.zerok.aop
org.zerok.security
org.zerok.util
```

### 프로젝트를 위한 요구사항

- 초기 버전에는 최대한 단순하고 눈에 보이는 결과를 만들어내는 형태로 진행하는 것이 좋다.
- 요구사항은 온전한 문장으로 정리하는 것이 좋다. 주어는 '고객'이고 목적어는 '대상'이 된다. 여기서 '대상'은 데이터베이스 설계와 시스템 설계에서 가장 중요한 용어가 된다 (대상을 도메인이라고 하기도 한다)
- 예시
  - 고객은 새로운 게시물을 등록할 수 있어야 한다.
  - 고객은 특정한 게시물을 조회할 수 있어야 한다.
  - 고객은 작성한 게시물을 삭제할 수 있어야 한다.
- 대상은 게시물이 되고, 게시물이라는 용어가 필요하게 되고, 게시물의 구조를 판단해서 데이터베이스의 테이블을 설계하게 된다. 
- 각 화면을 설계하는 단계에서는 사용자가 입력해야 하는 값과 함게 전체 페이지의 흐름이 설계된다. 이 화면의 흐름을 URL로 구성하게 되는데 이 경우 GET/POST 방식에 대해서 같이 언급해두는 것이 좋다. 

**테이블 생성과 Dummy 데이터 생성**

- 게시글마다 고유의 번호가 필요한데, 오라클의 경우 시퀀스를 이용해서 작업을 처리한다.
- 시퀀스를 생성할 때는 데이터베이스의 다른 오브젝트들과 구분하기 위해 ``seq_`로 시작하는 것이 일반적이다. 테이블을 생성할 때는 `tbl_`이나 `t_` 같이 구분이 가능한 단어를 앞에 붙여놓는 것이 좋다. 
- 테이블을 설계할 때는 가능하면 레코드의 생성 시간(`regdate`)과 최종 수정 시간(`updatedate`)을 같이 기록하는 것이 좋다.
- 오라클 데이터베이스의 경우 MySQL과 달리 데이터에 대한 가공 작업 후 반드시 commit을 수동으로 처리해야 한다.

### 영속/비즈니스 계층의 CRUD 구현

영속 계층의 작업은 다음과 같은 순서로 진행한다.

- 테이블의 칼럼 구조를 반영하는 VO(Value Object) 클래스의 생성
- MyBatis의 Mapper 인터페이스의 작성
- 작성한 Mapper 인터페이스의 테스트

```java
@Data
public class BoardVO {
  private Long bno;
  private String title;
  private String content;
  private String writer;
  private Date regDate;
  private Date updateDate;
}
```

```java
package org.zerock.mapper;

public interface BoardMapper {
  
  @Select("select * from tbl_board where bno > 0")
  public List<BoardVO> getList();
}
```

- 필요한  SQL을 어노테이션의 속성값으로 처리한다.
- SQL을 작성할 때 `;`이 없도록 작성해야 한다. 

```java
package org.zerock.mapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {org.zerock.config.RootConfig.class})
@Log4j
public class BoardMapperTests {
  
  @Setter(onMethod_ = @Autowired)
  private BoardMapper mapper;
  
  @Test
  public void testGetList() {
    mapper.getList().forEach(board -> log.info(board));
  }
}
```

- BoardMapperTests 클래스는 스프링을 이용해서 BoardMapper 인터페이스의 구현체를 주입받아서 동작하게 된다.
- Java 설정 시에는 RootConfig 클래스를 이용해서 스프링의 설정을 이용하고 있음을 명시한다.

Mapper XML 파일

```xml
<mapper namespace="org.zerock.mapper.BoardMapper">
  <select id="getList" resultType="org.zerock.domain.BoardVO">
    <! [CDATA[
    select * from tbl_board where bno > 0
    ]]>
  </select>
</mapper>
```

- XML을 작성할 대는 반드시 `<mapper>`의 namespace 의 속성 값을 Mapper 인터페이스와 동일한 이름을 준다.
- XML에 SQL이 처리되었으니 BoardMapper 인터페이스에 SQL은 제거한다.



**영속 영역의 CRUD 구현**

MyBatis는 내부적으로 JDBC의 PreparedStatement를 활용하고 필요한 파라미터를 처리하는 `?`에 대한 치환은 `#{속성}`을 이용해서 처리한다.

```java
public interface BoardMapper {
  //..
  public void insert(BoardVO board);
  public void insertSelectKey(BoardVO board);
}
```

```xml
<!-- ...-->
<insert id="insert">
  insert into tbl_board(bno,title,content,writer)
  values (seq_board.nextval, #{title}, #{content}, #{writer})
</insert>

<insert id="insertSelectKey">
  <selectKey keyProperty="bno" order="BEFORE"
             resultType="long">
    select seq_board.nextval from dual
  </selectKey>
  insert into tbl_board(bno,title,content,writer)
  values (#{bno}, #{title}, #{content}, #{writer})
</insert>
```

- `insert()`: 단순히 시퀀스의 다음 값을 구해서 insert할 때 사용한다. 추가된 데이터의 PK의 값을 알 수는 없지만, 1번의 SQL 처리만으로 작업이완료되는 장점이 있다.
- ` insertSelectKey()`: @SelectKey라는 MyBatis의 어노테이션을 이용한다. @SelectKey는 주로 PK값을 미리(before) SQL을 통해 처리해 두고 특정한 이름으로 결과를 보관하는 방식이다. @Insert할 때 SQL문을 보면 `#{bno}`와 같이 이미 처리된 결과를 이용하는 것을 볼 수 있다. 즉, SQL을 한 번 더 실행해야 한다는 부담이 있지만 자동으로 추가되는 PK값을 확인해야 하는 상황에서는 유용하게 사용할 수 있다.

**read(select) 처리**

```java
public interface BoardMapper {
  //..
  public BoardVO read(Long bno);
}
```

```xml
<select id="read" resultType="org.zerock.domain.BoardVO">
  select * from tbl_board where bno=#{bno}
</select>
```

MyBatis는 bno라는 칼럼이 존재하면 인스턴스의 `setBno()`를 호출하게 된다.  MyBatis는 모든 파라미터와 리턴 타입의 처리는 get파라미터명(), set칼럼명()의 규칙으로 호출된다. 

**update 처리**

``` java
public int update(BoardVO board);
```

```xml
<update id="update">
  update tbl_board
  set title= #{title},
  content= #{content},
  writer= #{writer},
  updateDate= sysdate
  where bno = #{bno}
</update>
```

### 비즈니스 계층

- 영속 계층은 데이터베이스를 기준으로 설계를 나눠 구현하지만, 비즈니스 계층은 로직을 기준으로 해서 처리하게 된다.
- '쇼핑몰에서 상품을 구매한다'고 가정할때, 해당 쇼핑몰의 로직이 '물건을 구매한 회원에게는 포인트를 올려준다'고 하면 영속 계층의 설계는 '상품'과 '회원'으로 나누어서 설계하게 된다. 반면 비즈니스 계층은 상품 영역과 회원 영역을 동시에 사용해서 하나의 로직으로 처리하게 된다.

![Ny102p4WTLKIbLfoNPlpgg_thumb_a508](https://user-images.githubusercontent.com/50407047/111905042-19096500-8a8d-11eb-9e6d-389550121f7c.jpg)설계를 할 때 각 계층 간의 연결은 인터페이스를 이용해서 느슨한(loose) 연결(결합)을 한다. 

```java
package org.zerock.service;

public interface BoardService {
  
  public void register(BoardVO board);
  public BoardVO get(Long bno);
  public boolean modify(BoardVO board);
  public boolean remove(Long bno);
  public List<BoardVO> getList();
}
```

- Service 메서드를 설계할 때 메서드 이름은 현실적인 로직의 이름을 붙이는 것이 관례이다. 

```java
@Log4j
@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {
  
  private BoardMapper mapper;
  
  @Override
  public void register(BoardVO board) {
    //..
  }
}
```

- 스프링 4.3부터는 단일 파라미터를 받는 생성자의 경우 필요한 파라미터를 자동으로 주입할 수 있다. **@AllArgsConstructor는 모든 파라미터를 이용하는 생성자를 만든다.** 

생성자가 없다면(@AllArgsConstructor를 떼고) 다음과 같이 @Autowired를 사용할 수 있다.

```java
@Setter(onMethod_ ={@Autowired})
private BoardMapeer mapper;
```

BoardService 객체가 제대로 주입이 가능한지 확인하는 테스트. 

```java
package org.zerock.service;

@RunWith(SpringJUnitClassRunner.class)
@ContextConfiguration(classes = {org.zerock.config.RootConfig.class})
@Log4j
public class BoardServiceTests {
  @Setter(onMethod_ = {@Autowired})
  private BoardService service;;
  
  @Test
  public void testExist() {
    log.info(service);
    assertNotNull(service);
  }
}
```

등록 작업의 구현

```java
@Override
public void register(BoardVO board) {
  log.info("register....." + board);
  mapper.insertSelectKey(board);
}
```

테스트

```java
@Test
public void testRegister() {
  BoardVO board = new BoardVO();
  board.setTitle("새로 작성하는 글");
  board.setContent("새로 작성하는 내용");
  board.setWriter("newbie");
  
  service.register(board);
  log.info("생성된 게시물의 번호: " + board.getBno());
}
```

목록 작업의 구현

```java
@Override
public List<BoardVO> getList() {
  log.info("getList.......");
  return mapper.getList();
}
```

테스트

```java
@Test
public void testGetList() {
  service.getList().forEach(board -> log.info(board));
}
```

조회 작업의 구현

```java
@Override
public BoardVO get(Long bno) {
  log.info("get...." + bno);
  return mapper.read(bno);
}
```

테스트

```java
@Test
public void testGet() {
  log.info(service.get(1L))
}
```

삭제/수정 구현

```java
@Override
public boolean modify(BoardVO board) {
  log.info("modify.........." + board);
  return mapper.update(board) == 1;
}

@Override
public boolean remove(Long bno) {
  log.info("remove.........." + bno);
  return mapper.delete(bno) == 1;
}
```

테스트

``` java
 @Test
public void testDelete() {
  //게시물 번호의 존재 여부 테확인하고 테스트
  log.info("REMOVE RESULT: " + service.remove(2L));
}

@Test
public void testUpdate() {
  BoardVO board = service.get(1L);
  
  if(board == null) {
    return;
  }
  
  board.setTitle("제목 수정합니다.");
  log.info("MODIFY RESULT: " + service.modify(board));
}
```

### 프레젠테이션(웹) 계층의 CRUD 구현

- 과거 이 단계에서 Tomcat(WAAS)를 실행하고 웹 화면을 만들어서 겨로가를 확인하는 방식의 코드를 작성했다. 이 방식은 시간이 오래 걸리고 테스트를 자동화하기 어렵다.

**목록에 대한 처리와 테스트**

```java
@Controller
@Log4j
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {
  private BoardService service;
  
  @GetMapping("/list")
  public void list(Model model) {
    log.info("list");
    model.addAttribute("list", service.getList());
  }
}
```

- 스프링의 테스트 기능을 활용하면 개발 당시에 Tomcat(WAS)을 실행하지 않고도 스프링과 웹 URL을 테스트할 수 있다.

```java
package org.zerock.controller;

@RunWith(SpringJUnitClassRunner.class)
//Test for Controller
@WebApplicationConfiguration
@ContextConfiguration(classes = {
  org.zerock.config.RootConfig.class,
  org.zerock.config.ServletConfig.class})
@Log4j
public class BoardControllerTests {
  
  @Setter(onMethod_={@Autowired})
  private WebApplicationContext cs;
  private MockMvc mockMvc;
  
  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilder.webApplicationContextSetup(ctx).build();
  }
  
  @Test
  public void testList() throws Exception {
    log.info(
    mockMvc.perform(MockMvcRequestBuilders.get("/board/list"))
    .andReturn()
    .getModelAndView()
    .getModelMap());
  }
}
```

- @WebAppConfiguration: Servlet의 ServletContext를 이용하기 위해서인데, 스프링에서는 WebApplicationContext라는 존재를 이용하기 위해서이다.
- @Before이라는 어노테이션이 적용된 setUp()에서는 import할 때 JUnit을 이용해야 한다. @Before가 적용된 메서드는 모든 테스트 전에 매번 실행하는 메서드가 된다.
- MockMvc는 가짜 mvc이다. 가짜로 URL과 파라미터 등을 브라우저에서 사용하는 것처럼 만드러엇 Controller를 실행해볼 수 있다. testList()는 MockMvcRequestBuilders라는 존재를 이용해서 GET 방식의 호출을 한다. 이후에는 BoardController의 getList()에서 반환된 결과를 이용해 Model에 어떤 데이터들이 담겨 있는지 확인한다. Tomcat을 통해서 실행되는 방식이 아니므로 기존의 테스트 코드를 실행하는 것과 동일하게 실행한다. 
- testList()를 실행한 결과는 데이터베이스에 저장된 게시물들을 볼 수 있다. 

**등록 처리와 테스트**

POST방식으로 처리되는 register

```java
@PostMapping("/register")
public String register(BoardVO board, RedirectAttributes rttr) {
  log.info("register: " + board);
  service.register(board);
  rttr.addFlashAttribute("result", board.getBno());
  return "redirect:/board/list";
}
```

- 등록 작업이 끝난 후 다시 목록 화면으로 이동하기 위해 새롭게 등록된 게시물의 번호를 전달하기 위해  RedirectAttributes를 파라미터로 지정한다. 
- 리턴 시 'redirect:' 접두어를 사용하면 스프링 MVC가 내부적으로 response.sendRedirect()를 처리해준다.

테스트

```java
@Test
public void testRegister() throws Exception {
  String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/register")
    .param("title", "테스트 새글 제목")
    .param("content", "테스트 새글 내용")
    .param("writer". "user00")
  ).andReturn().getModelAndView().getViewName();
  log.info(resultPage);
}
```

테스트할 대 MockMvcBuilder의 post()를 이용하면 POST 방식으로 데이터를 전달할 수 있고, param()을 이용해서 전달해야 하는 파라미터를 지정할 수 있다(`<input>`태그와 유사한 역할).

 **조회 처리와 테스트**

```java
@GetMapping("/get")
public void get(@ReqeustParam("bno") Long bno, Model model) {
  log.info("/get");
  model.addAttribute("board", service.get(bno));
}
```

bno 값을 좀 더 명시적으로 처리하기 위해 @RequestParam을 이용해서 지정한다. 여기서는 파라미터 이름과 변수 이름이 같으니 생략해도 괜찮다.

```java
@Test
public void testGet() throws Exception {
  log.info(mockMvc.perform(MockMvcRequestBuilders
                          .get("/board/get")
                          .param("bno", "2")
                          .andReturn()
                          .getModelAndView().getModelMap());
}
```

**수정 처리와 테스트**

```java
@PostMapping("/modify")
public String modify(BoardVO board, RedirectAttributes rttr) {
  log.info("modify:" + board);
  
  if (service.modify(board)) {
    rttr.addFlashAttribute("result", "success");
  }
  return "redirect:/board/list";
}
```

service.modify()는 수정 여부를 boolean으로 처리하므로 이를 이용해서 성공한 경우에만 RedirectAttributes에 추가한다.

```java
@Test
public void testModify() throws Exception {
  String resultPage = mockMvc
    .perform(MockMvcRequestBuilders.post("/board/modify")
            .param("bno", "1")
            .param("title", "수정된 테스트 새글 제목")
            .param("content", "수정된 테스트 새글 내용")
            .param("writer", "user00"))
    .andReturn().getModelAndView().getViewName();
  
  log.info(resultPage);
}
```

**삭제 처리와 테스트**

```java
@GetMapping("/remove")
public String remove(@RequestParam("bno") Long bno, RedirectAttributes rttr) {
  log.info("remove...." + bno);
  if (service.remove(bno)) {
    rttr.addFlashAttribute("result", "success");
  } 
  return "redirect:/board/list";
}
```

```java
@Test
public void testRemove() throws Exception {
  String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/remove")
                                     .param("bno", "25")
                                     ).andReturn().getModelAndView().getViewName();
  log.info(resultPage);
}
```

MockMvc를 이용해서 파라미터를 전달할 때에는 문자열로만 처리해야 한다. 

### 화면 처리

- 화면 설정은 ViewResolver라는 객체를 통해서 이루어지는데, '/WEB-INF/views' 폴더를 이용한다.
- '/WEB-INF' 경로는 브라우저에서 직접 접근할 수 없는 경로이브로 반드시 Controller를 이용하는 모델 2방식에서 기본적으로 사용한다. 

**한글 문제와 UTF-8 필터 처리**

새 게시물을 등록했을 때 한글 입력에 문제가 있다면

- 브라우저에서 한글이 깨져서 전송되는 지 확인한다.
- 문제가 없다면 스프링 MVC 쪽에서 한글을 처리하는 필터를 등록해야 한다. 

```java
@Override
protected Filter[] getServletFilters() {
  CharacterEncodingFilter characterEncodingFilter =
    new CharacterEncodingFilter();
  characterEncodingFilter.setEncoding("UTF-8");
  characterEncodingFilter.setForceEncoding(true);
  return new Filter[] {characterEncodingFilter};
}
```

**재전송 처리**

![vM9MCUPbSJ+%aKAk7MeQ%A_thumb_a509](https://user-images.githubusercontent.com/50407047/111905046-19a1fb80-8a8d-11eb-97c9-f1741bf0b67b.jpg)

- 위와 같이 재전송을 하지 않는다면 사용자는 브라우저의 '새로고침'을 통해서 동일한 내용을 계속 서버에 등록할 수 있는 문제가 있다. 브라우저에서는 이런 경우 경고창을 보여주기는 하지만 근본적으로 차단하지는 않는다. 
- 따라서 등록, 수정, 삭제 작업은 처리가 완료된 후 다시 동일한 내용을 전송할 수 없도록 아예 브라우저의 URL을 이동하는 방식을 이용한다.
- 이 과정에서 브라우저에 등록, 수정, 삭제의 결과를 바로 알 수 있게 경고창이나 모달창으로  피드백을 줘야 한다는 점을 기억해야 한다. 

```js
$(document).ready(function() {
  let result = '<c:out value="${result}"/>';
});
```

새로운 게시글이 동록된다면 다음과 같이 처리된다.

```js
$(document).ready(function() {
  let result = '15';
});
```

새로운 게시물의 번호는 addFlashAttribute()로 저장되었기 때문에 한 번도 사용된 적이 없다면 위와 같이 값을 만들어내지만 사용자가 '/board/list'를 호출하거나 '새로고침'을 통해서 호출하는 경우는 다음과 같이 아무런 내용이 없을 것이다. 

```js
$(document).ready(function() {
  let result = '';
});
```

addFlashAttribute()를 이용해서 일회성으로만 데이터를 사용할 수 있으므로 이를 이용해서 경고창이나 모달창 등을 보여주는 방식으로 처리할 수 있다.

**목록 페이지와 뒤로 가기 문제**

![TOenDGxJSxuY1ygi%RtACg_thumb_a50a](https://user-images.githubusercontent.com/50407047/111905047-1ad32880-8a8d-11eb-84d4-18ffa4d5196d.jpg)

웹페이지를 이용하다보면 뒤로가기 처리가 제대로 되지 않는 경우를 많이 볼 수 있다. 뒤로 가기를 하면 다시 다운로드를 시도하거나 경고창이 뜨는 경우가 여기에 해당한다.

조회 페이지를 이동하는 방식이 아니라 '새창'을 통해서 보고 싶다면 `<a>`태그의 속성으로 `target='_blank'`를 지정하면 된다.

등록 -> 목록 -> 조회 까지는 순조롭지만 브라우저의 뒤로가기를 선택하는 순간 다시 게시글의 등록 결과를 확인하는 방식으로 동작한다는 문제가 있다. 

이런 문제가 생기는 원인은 브라우저에서 '뒤로 가기'나 '앞으로 가기'를 하면 서버를 다시 호출하는 것이 아니라 과거에 자신이 가진 모든 데이터를 활용하기 때문이다. 브라우저에서 조회 페이지와 목록 페이지를 여러 번 앞으로 혹은 뒤로 이동해도 서버에서는 처음 호출을 제외하고 별다른 변화가 없다.

이 문제를 해결하기 위해서는 window의 history 객체를 이용해서 현재 페이지는 모달창을 띄울 필요가 없다고 표시를 해 두는 방식을 이용해야 한다. window의 history 객체는 stack 구조로 동작한다.

![vutLO15hRd6OXwtmrhpZTA_thumb_a50b](https://user-images.githubusercontent.com/50407047/111905048-1b6bbf00-8a8d-11eb-8fcf-3582f179f509.jpg)

그림 1은 사용자가 브라우저를 열고 '/board/list'를 최초로 호출한 것이다. history에 쌓으면서 현재 페이지는 모달창을 보여줄 필요가 없다는 표시를 해둔다.  

그림 2는 사용자가 '/board/register'를 호출한 경우이다. 스택 상단에 /'board/register'가 쌓이게 된다. 만일 이 상태에서 뒤로 가기를 실행하면 아래쪽의 board/list가 보여지는데 이때 심어둔 표시를 이용해서 모달창을 띄울 필요가 없다는 것을 확인할 수 있다.

그림3은 사용자가 등록을 완료하고 'board/list'가 호출되는 상황이다. 브라우저에서 앞으로 가기나 뒤로 가기로 이동한 것이 아니므로 스택의 상단에 추가된다. 등록 직후에 /board/list로 이동한 경우에는 모달창이 동작한다.

그림3에서 모달창을 보여준 후에는 스택의 상단에 모달창이 필요하지 않다는 표시를 해주어야 한다. 이후에 /board/register를 호출하면 그림4와 같이 된다. 

window.history 객체를 다음과 같이 조작해준다.

```js
$(document).ready(function() {
  
  var result = '<c:out value="${value}"/>';
  
  checkModal(result);
  
  history.replaceState({}, null, null);
  
  function checkModal(result) {
    
    if (result == '' || history.state ) {
      return;
    }
    
    if (parseInt(result) > 0) {
      $(".modal-body").html(
        "게시글" + parseInt(result) + "번이 등록되었습니다.");
    }
    
    $("#myModal").modal("show");
  }
  
  $("#regBtn").on("click", function() {
    self.location = "/board/register";
  });
});
```

**게시글의 수정/삭제 처리**

수정/삭제 페이지로 이동

```java
@GetMapping("/get", "modify")
public void get(@RequestParam("bno") Long bno, Modal modal) {
  log.info("/get or modify");
  model.addAttribute("board", service.get(bno));
}
```

- @GetMapping이나 @PostMapping 등에는 URL을 배열로 처리할 수 있으므로 하나의 메서드로 여러 URL을 처리할 수 있다. 

modify.jsp는 get.jsp와 같지만 수정이 가능한 제목이나 내용 등이 readonly 속성이 없도록 작성한다. POST 방식으로 처리하는 부분을 위해서는 `<form>`태그로 내용들을 감싸게 한다.

```html
<form role="form" action="/board/modify" method="post">
  <div class="form-group">
    <label>Bno</label>
    <input class="form-control" name='bno'
           value='<c:out value="${board.bno}"/>' readonly="readonly">
  </div>
  
  <div class="form-group">
    <label>RegDate</label>
    <input class="form-control" name='regDate'
           value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.regdate}"/>' readonly="readonly">
  </div>
  <!-- .... -->
  <button type="submit" data-oper='modify' class="btn btn-default">Modify</button>
  <button type="submit" data-oper='remove' class="btn btn-danger">Modify</button>
  <button type="submit" data-oper='list' class="btn btn-info">Modify</button>
</form>
```

JavaScript에서는 버튼에 따라 다른 동작을 할 수 있도록 수정한다.

```javascript
$(document).ready(function() {
  var formObj = $("form");
  
  $('button').on("click", function(e){
    
    e.preventDefault();
    
    var operation = $(this).data("oper");
    
    console.log(operation);
    
    if (operation === 'remove') {
      formObj.attr("action", "/board/remove");
    } else if (operation === 'list') {
      //move to list
      self.location = "/board/list";
      return;
    }
    formObj.submit();
  });
});
```

JavaScript에서는 `<button>` 태그의 `data-oper` 속성을 이용해서 원하는 기능을 동작하도록 처리한다. `<form>` 태그의 모든 버튼은 기본적으로 `submit`으로 처리하기 때문에 `e.preventDefault()`로 기본 동작을 막고 마지막에 직접 `submit()`을 수행한다. 

**조회 페이지에서 `<form>` 처리**

```jsp
<button data-oper='modify' class="btn btn-default">
  Modify
</button>
<button data-oper='list' class="btn btn-info">
  List
</button>

<form id='operForm' action="/board/modify" method="get">
  <input type='hidden' id='bno' name='bno' value='<c:out value="${board.bno}"/>'>
</form>
```

사용자가 버튼을 클릭하면 operForm이라는 id를 가진 `<form>` 태그를 전송해야 하므로 추가적인 JavaScript 처리가 필요하다.

```javascript
$(document).ready(function() {
  var operForm = $("#operForm");
  $("button[data-oper='modify']").on("click", function(e) {
    operForm.attr("action", "/board/modify").submit();
  });
  
  $("button[data-oper='list']").on("click", function(e) {
    operForm.find("#bno").remove();
    operForm.attr("action", "/board/list");
    operForm.submit();
  });
});
```

**수정 페이지에서 링크 처리**

```js
$(document).ready(function() {
  var formObj = $("form");
  
  $('button').on("click", function(e){
    
    e.preventDefault();
    
    var operation = $(this).data("oper");
    
    console.log(operation);
    
    if (operation === 'remove') {
      formObj.attr("action", "/board/remove");
    } else if (operation === 'list') {
      //move to list
      formObj.attr("action", "/board/list").attr("method", "get");
      formObj.empty();
    }
    formObj.submit();
  });
});
```

수정된 내용은 클릭한 버튼이 List인 경우 action 속성과 method 속성을 변경한다. `/board/list`로의 이동은 아무런 파라미터가 없기 때문에 `<form>` 태그의 모든 내용은 삭제한 상태에서 `submit()`을 진행한다. 이후에 코드는 실행되지 않도록 return 을 통해 제어한다.


### 오라클 데이터베이스 페이징 처리

- 수많은 데이터를 한 페이지에서 보여주면 처리 성능에 영향을 미친다. 또한, 브라우저에서도 데이터의 양이나 처리 속도에 문제를 일으키게 된다.
- 페이징 처리는 번호를 이용하거나 '계속 보기'의 형태로 구현된다.
- '계속 보기'는 Ajax와 앱이 등장한 이후에 '무한 스크롤'이나 '더 보기'와 같은 형태로 구현된다. 
- 오라클에서 페이징 처리하는 것은 MySQL에 비해서 추가적인 지식이 필요하므로 이에 대한 학습을 선행해야 한다.

#### order by의 문제

데터베이스를 이용할 때 애플리케이션에 가장 신경 쓰는 부분은

- 빠르게 처리되는 것
- 필요한 양만큼만 데이터를 가져오는 것

이다. 

거의 모든 페이지에서 페이징을 하는 이유는 최소한의 필요한 데이터만을 가져와서 빠르게 화면에 보여주기 위함이다. 

빠르게 동작하는 SQL을 위해서는 먼저 order by 를 이용하는 작업을 가능하면 하지 말아야 한다. order by는 데이터가 많은 경우 엄청난 성능의 저하를 가져오기 때문에 데이터가 적은 경우와, 정렬을 빠르게 할 수 있는 방법이 있는 경우가 아니라면 order by는 주의해야 한다.

**실행 계획과 order by**

- 실행 계획(execution plan): SQL을 데이터베이스에서 어떻게 처리할 것인가?에 대한 것이다. 
- SQL이 데이터베이스에 전달되면 데이터베이스는 여러 단계를 거쳐서 해당 SQL을 어떤 순서와 방식으로 처리할 것인지 계획을 세운다.  

![YL2W1sCkT6yAMDz9Zmf7fg_thumb_a50c](https://user-images.githubusercontent.com/50407047/111913197-65b26780-8ab0-11eb-899a-94f8cd69343c.jpg)

- SQL 파싱 단계: SQL 구문에 오류가 있는지 SQL을 실행해야 하는 대상 객체(테이블, 제약 조건, 권한 등)가 존재하는지를 검사한다. 
- SQL 최적화 단계: SQL이 실행되는데 필요한 비용(cost)을 계산한다. 이 계산된 값을 기초로 해서 어떤 방식으로 실행하는 것이 가장 좋다는 것을 판단하는 '실행 계획(execution plan)'을 세우게 된다.
- SQL 실행 단계: 세워진 실행 계획을 통해서 메모리상에서 데이터를 읽거나 물리적인 공간에서 데이터를 로딩하는 등의 작업을 하게 된다. 



개발자들은 도구를 이용하거나 SQL Plus 등을 이용해서 특정한 SQL에 대한 실행 계획을 알아볼 수 있다. SQL Developer에서는 간단히 버튼을 클릭해서 실행 계획을 확인할 수 있다. 

SQL 튜닝: 실행 계획을 보면 트리 구조로 방금 전 실행한 SQL이 어떻게 처리된 것인지를 알려준다. 이를 보고 어떤 방식이 더 효과적인지 알려준다. 

실행계획은 **안쪽에서 바깥쪽으로, 위에서 아래로** 보면 된다.

실행 계획을 세우는 것은 데이터베이스에서 하는 역할이기 때문에 데이터의 양이나 제약 조건 등의 여러 상황에 따라서 데이터베이스는 실행계획을 다르게 작성한다.

```sql
select * from tbl_board order by bno + 1 desc;
```

위의 실행 계획을 보면 TBL_BOARD를 FULL로 스캔했고, 바깥쪽으로 가면서 'SORT'가 일어난다. 책에서는 8.27초가 걸렸다고 한다. 이때 가장 많은 시간을 소모하는 작업은 정렬하는 작업이다. 

```sql
select * from tbl_board order by bno desc;
```

연산을 하고 위와 같이 실행했을 때는 거의 0초만에 실행되엇다. 실행 계획을 확인하면 첫번째 실행계획에는 있던 정렬 과정이 여기서는 없다는 것을 발견할 수 있다. 이를 이해하기 위해서는 데이터베이스의 인덱스(index)에 대해서 알아야 한다.

#### order by 보다는 인덱스

- 데이터가 많은 상태에서 정렬 작업이 문제가 된다면 이를 인덱스로 해결해야 한다. 
- '인덱스'라는 존재가 이미 정렬된 구조이므로 이를 이용해서 별도의 정렬을 하지 않는 방법
- 데이터베이스에서 PK는 '식별자'의 의미와 '인덱스'의 의미를 갖는다.
- 인덱스는 색인이다. 색인을 이용하면 사용자들은 책 전체를 살펴볼 필요 없이 색인을 통해 자신이 원하는 내용이 책의 어디에 있는지 알 수 있다.
- 데이터베이스의 테이블을 하나의 책이라고 생각하고 어떻게 데이터를 찾거나 정렬하는지 생각해보자. 색인은 사람들이 쉽게 찾아볼 수 있게 알파벳 순서나 한글 순서로 정렬한다. 이를 통해서 원하는 내용을 위에서부터 혹은 반대로 찾아나간다. 이를 스캔이라고 한다.
- 데이터베이스에 테이블을 만들 때 PK를 부여하면 인덱스라는 것이 만들어진다. 데이터베이스를 만들 때 PK를 지정하는 이유는 '식별'이라는 의미가 있지만, **구조상으로는 '인덱스'라는 존재(객체)가 만들어지는 것을 의미한다.**

![oJc3IKRtRxG2ssTeUua0+A_thumb_a510](https://user-images.githubusercontent.com/50407047/111936818-d20d8500-8b09-11eb-9cee-7537c578e44d.jpg)

- 테이블은 마치 책장에 책을 막 넣은 것처럼 중간에 순서가 섞여 있는 경우가 대부분이다. 
- 인덱스와 실제 테이블을 연결하는 고리는 ROWID라는 존재이다. ROWID는 데이터베이스 내의 주소에 해당하는데 모든 데이터는 자신만의 주소를 가지고 있다. 
- 'where bno=100'같은 조건을 주었을 때 데이터베이스의 입장에서는 tbl_board 라는 책에서 bno 값이 100인 데이터를 찾아야 한다. 만일 책이 얇아서 내용이 많지 않다면 전체를 살펴보는 것이 더 빠를 것이다. 이를 FULL SCAN이라고 표현한다. 하지만 내용이 많고, 색인이 존재한다면 색인을 찾고 색인에서 주소를 찾아서 접근하는 방식을 이용할 것이다.  

#### 인덱스를 이용하는 정렬

- 인덱스는 '정렬이 되어 있다.' 
- 따라서 데이터를 찾아내 이들을 SORT하는 과정을 생랽할 수 있다.
- 데이터의 양이 많고 정렬이 필요한 상황이라면 우선적으로 인덱스를 작성하는 것을 생각해봐야 한다.

**인덱스와 오라클 힌트(hint)**

- 오라클은 select 문을 전달할 때 힌트를 사용한다. 
- 힌트는 데이터베이스에 '지금 내가 전달한 select문을 이렇게 실행해주면 좋겠다'라는 힌트이다. 
- 힌트 구문에서 에러가 나도 SQL 실행에 지장을 주지 않는다. 
- 따라서 힌트를 이용한 select 문을 작성한 후에는 실행 계획을 통해 개발자가 원하는 대로 SQL문이 실행되는지를 확인해야 한다. 

```sql
select * from btl_board order by bno desc;

select /*+INDEX_DESC (tbl_board pk_board) */*
from tbl_board;
```

- 위의 두 쿼리문은 동일한 결과를 보여준다.
- 두 번째 select문은 order by 조건이 없어도 동일한 결과가 나왔다. select문에서 `tbl_board` 테이블에 `pk_board` 인덱스를 역순으로 이용해줄 것이라는 힌트를 부여했기 때문이다. 실행 계획에서 이를 활용고 있다.

**힌트 사용 문법**

힌트는 잘못 작성되어도 실행할 때는 무시되기만 하고 별도의 에러는 발생하지 않는다.

```sql
SELECT 
/* Hint name (param...) */ column name, ...
FROM
  table name
```

- `FULL` 힌트

  ```sql
  select /* + FULL(tbl_board) */ * from tbl_board order by bno desc;
  ```

  `TBL_BOARD` 를 FULL로 접근하고 다시 SORT 가 적용된다. 

- `INDEX_ASC`, `INDEX_DESC` 힌트

  인덱스를 순서대로 이용할 것인지 역순으로 이용할 것인지를 지정한다. 주로 `order by` 를 위해서 사용한다. 인덱스 자체가 정렬을 해준 상태이므로 이를 통해서 SORT 과정을 생략하기 위한 용도이다.

  ```sql
  select /* INDEX_ASC(tbl_board pk_board) */ * from tbl_board where bno > 0;
  ```

  이 힌트를 이요할 때는 동일한 조건의 order by 구문을 작성하지 않아도 된다. 

#### ROWNUM과 인라인뷰

- 전체가 아닌 필요한 만큼의 데이터를 가져오는 방식
- 데이터베이스는 페이지 처리를 위해서 ROWNUM이라는 특별한 키워드를 사용해서 데이터에 순번을 붙여 사용한다.
- ROWNUM은 SQL이 실행된 결과에 넘버링을 해준다. 모든 SELECT 문에는 ROWNUM이라는 변수를 이용해서 해당 데이터가 몇 번째로 나오는지 알아낼 수 있다. 
- ROWNUM은 실제 데이터가 아니라 테이블에서 데이터를 추출한 후에 처리되는 변수이므로 상황에 따라서 그 값이 매번 달라질 수 있다. 
- ROWNUM은 테이블에는 존재하지 않고, 테이블에서 가져온 데이터를 이용해서 번호를 매기는 방식이다. 
- 이때 번호는 데이터는 현재 데이터베이스의 상황에 따라서 저장된 데이터를 로딩하는 것이므로 실습 환경에 따라 다른 값이 나온다.
- ROWNUM이라는 것은 데이터를 가져올 때 적용되는 것이고, **이후에 정렬되는 과정에서는 ROWNUM이 변경되지 않는다.** 즉 정렬은 나중에 처리된다.

**인덱스를 이용한 접근 시 ROWNUM**

만일 `PK_BOARD` 인덱스를 통해서 접근한다면 다음과 같은 과정으로 접근한다.

- `PK_BOARD` 인덱슬르 통해서 테이블에 접근
- 접근한 데이터에 ROWNUM 부여

**페이지 번호 1, 2의 데이터**

페이지 1

```sql
select /*+INDEX_DESC(tbl_board pk_board) */
	rownum rn, bno, title, content
from
	tbl_board
where rownum <= 10;
```

- 가장 높은 번호의 게시물 10개만이 출력된다. 
- 이때 `PK_BOARD` 인덱스를 역순으로 접근한다. 
- `WHERE` 조건에서 특이하게 ROWNUM 조건은 테이블을 접근할 때 필터링 조건으로 적용된다.

페이지 2

```sql
select /*+INDEX_DESC(tbl_board pk_board) */
	rownum rn, bno, title, content
from
	tbl_board
where rownum > 10 and rownum <= 20;
```

위의 쿼리문은 아무 결과가 나오지 않는다. 왤까?

실행계획은 우선 ROWNUM > 10 데이터를 찾게 된다. 문제는 `TBL_BOARD` 에  처처음으로 나오는 ROWNUM의 값이 1이라는 것이다. `TBL_BOARD`에서 데이터를 찾고 ROWNUM이 1이 된 데이터는 where 조건에 의해 무효화된다. 이후에 다시 다른 데이터를 가져오면 새로운 데이터가 첫 번째 데이터가 되므로 다시 ROWNUM은 1이 된다. 이 과정이 반복되면 ROWNUM 값은 항상 1로 만들어지고 없어지는 과정이 반복되므로 테이블의 모든 데이터를 찾아내지만 결과는 아무것도 나오지 않게 된다. 이러한 이유로 SQL을 작성할 때 ROWNUM 조건은 반드시 1이 포함되어야 한다.

'...WHERE ROWNUM=10'을 요구한다면 이 조건을 만족하는 ROW는 결코 추출될 수 없다. 원래의 조건을 모두 만족하여 ROWNUM이 1이 될 수 있었다 하더라도 조건에 있는 ROWNUM=10에 의해 무시되어서 ROWNUM은 아직 '1이 되지도 못'했다. 그러므로 ROWNUM이 1이 되지 않고서는 영원히 ROWNUM은 10이 될 수 없으므로 이러한 조건을 만족하는 ROW는 결코 찾을 수 없다. 

SQL에 ROWNUM 조건이 1을 포함하도록 다음과 같이 수정해보면 결과가 나온다.

```sql
select /*+INDEX_DESC(tbl_board pk_board) */
	rownum rn, bno, title, content
from
	tbl_board
where rownum <= 20;
```

이러면 역순으로 데이터를 20개 가져온다.

**인라인뷰(in-line view) 처리**

- 인라인뷰: 'SELECT문 안쪽 FROM에 다시 SELECT문'
- 논리적으로는 어떤 결과를 구하는 SELECT문이 있고, 그 결과를 다시 대상으로 삼아서 SELECT를 하는 것이다.
- 뷰(View): 일종의 창문 같은 개념으로 복잡한 SELECT 처리를 하나의 뷰로 생성하고, 사용자들은 뷰를 통해서 복잡하게 만들어진 결과를 마치 하나의 테이블처럼 쉽게 조회한다는 개념이다.
- 인라인뷰는 이러한 뷰의 작성을 별도로 작성하지 않고 말 그대로 FROM 구문 안에 작성하는 형태이다. 

```sql
SELECT ...
FROM (
  SELECT ... /*인라인 뷰*/
  FROM ...
)
```

20개의 데이터를 가져오는 SQL을 하나의 테이블처럼 간주하고 바깥쪽에서 추가적인 처리를 하도록 만들어주자.

```sql
select
	bno, title, content
from
	(
    select /*+INDEX_DESC(tbl_board pk_board) */
    	rownum rn, bno, title, content
    from
    	tbl_board
    where rownum <= 20
  )
 where rn > 10;
```

이 과정을 정리하면 다음과 같다.

- 필요한 순서로 정렬된 데이터에 ROWNUM을 붙인다.
- 처음부터 해당 페이지의 데이터를 'ROWNUM <= 30'과 같은 조건을 이용해서 구한다.
- 구해놓은 데이터를 하나의 테이블처럼 간주하고 인라인뷰로 처리한다.
- 인라인뷰에서 필요한 데이터만을 남긴다. 

### MyBatis와 스프링에서 페이징 처리

MyBatis는 SQL을 그대로 사용할 수 있기 때문에 인라인뷰를 이용하는 SQL을 작성하고, 필요한 파라미터를 지정하는 방식으로 페이징 처리를 한다. 페이징 처리를 위해서는 SQL을 실행할 때는 다음과 같은 파라미터가 필요하다.

- 페이지 번호(pageNum)
- 한 페이지 당 몇 개의 데이터(amount)를 보여줄 것인가

이 데이터를 하나의 객체로 묶어서 전달하는 방식이 확장성이 좋다.

```java
package org.zerock.domain;

@Getter
@Setter
@ToString
public class Criteria {
  
  private int pageNum;
  private int amount;
  
  public Criteria() {
    this(1, 10);
  }
  
  public Criteria(int pageNum, int amount) {
    this.pageNum = pageNum;
    this.amount = amount;
  }
}
```

Criteria 클래스의 용도는 pageNum과 amount 값을 같이 전달하는 용도이지만 생성자를 통해서 기본값을 1페이지, 10개로 지정해서 처리한다.

#### MyBatis 처리와 테스트

```java
package org.zerock.mapper;

public interface BoardMapper {
  public List<BoardVO> getList();
  public List<BoardVO> getListWithPaging(Criteria cri);
  //..
}
```

`src/main/resources`

```xml
<select id ="getListWithPaging" resultType="org.zerock.domain.BoardVO">
  <![CDATA[
	select
		bno, title, content, writer, regdate, updatedate
	from
		(
		select /*+INDEX_DESC(tbl_board pk_board) */
			rownum rn, bno, title, content, writer, regdate, updatedate
		from
			tbl_board
		where rownum <= #{pageNum} * #{amount}
		)
	where rn > (#{pageNum} - 1) * #{amount}
]]>
</select>
```

인라인뷰에서는 BoardVO를 구성하는 데 필요한 모든 칼럼과 ROWNUM을 RN이라는 가명을 이용해서 만들어 주고 바깥쪽 SQL에서는 RN 칼럼을 조건으로 처리한다.

**페이징 테스트와 수정**

```java
@Test
public void testPaging() {
  Criteria cri = new Criteria();
  cri.setPageNum(3);
  cri.setAmount(10);
  List<BoardVO> list = mapper.getListWith(cri);
  list.forEach(board -> log.info(board));
}
```

BoardService

```java
public List<BoardVO> getList(Criteria cri);
```

BoardController

```java
@GetMapping("/list")
public void list(Criteria cri, Model model) {
  log.info("list: " + cri);
  model.addAttribute("list", service.getList(cri));
}
```

BoardControllerTests

```java
@Test
public void testListPaging() throws Exception {
  log.info(mockMvc.perform(
  MockMvcRequestBuilders.get("/board/list")
  .param("pageNum", "2")
  .param("amount", "50"))
           .getReturn().getModelAndView().getModelMap());
}
```

### 페이징 화면 처리 

- 브라우저 주소창에서 페이지 번호를 전달해서 결과를 확인하는 단계
- JSP에서 페이지 번호를 출력하는 단계
- 각 페이지 번호에 클릭 이벤트 처리
- 전체 데이터 개수를 반영해서 페이지 번호 조절

페이지 처리는 단순히 링크의 연결이기 때문에 어렵지는 않지만, **목록 페이지에서 조회 페이지, 수정 삭제 페이지까지 페이지 번호가 계속해서 유지되어야만 하기 때문에** 끝까지 신경써야 하는 부분이 많다.

#### 페이징 처리할 때 필요한 정보들

- 현재 페이지 번호(page)
- 이전과 다음으로 이동 가능한 링크의 표시 여부 (prev, next)
-  화면에서 보여지는 페이지의 시작 번호와 끝 번호(startPage, endPage)

**끝 페이지 번호와 시작 페이지 번호**

페이징 처리를 할 때 우선적으로 필요한 정보는 현재 페이지 정보이다. 사용자가 5페이지를 본다고 하면 화면의 페이지 번호는 1부터 시작하지만, 19페이지를 본다고 하면 11부터 시작해야 하기 때문이다.

페이지를 계산할 때는 끝 번호를 먼저 계산하는 것이 수월하다.

```java
this.endPage = (int)(Math.ceil(페이지번호 / 10.0)) * 10;
```

- 1페이지의 경우:  Math.ceil(0.1) * 10 = 10
- 10페이지의 경우: Math.ceil(1) * 10 = 10
- 11페이지의 경우: Math.ceil(1.1) * 10 = 20

끝 번호를 먼저 계산하는 이유는 시작 번호를 계산하기 수월하기 때문이다. 만일 화면에 10개씩 보여준다면 시작 번호는 무조건 끝 번호에서 9를 뺀 값이 된다.

```java
this.startPage = this.endPage - 9;
```

끝 번호는 전체 데이터 수(total)에 영향을 받는다. 예를 들어, 10개씩 보여주는 경우 전체 데이터 수(total)가 80개라고 가정한다면 끝 번호는(endPage)는 10이 아닌 8이 되어야 한다.

만일 끝 번호(endPage)와 한 페이지당 출력되는 데이터 수 (amount)의 곱이 전체 데이터 수(total)보다 크다면 끝 번호(endPage)는 다시 total을 이용해서 계산해야 한다.

```java
realEnd = (int) (Math.ceil((total * 1.0) / amount) );

if (realEnd < this.endPage) {
  this.endPage = realEnd;
}
```

먼저 전체 데이터 수(total)을 이용해서 진짜 끝 페이지(realEnd)가 몇 번까지 되는지를 계산한다. 만일 진짜 끝 페이지(realEnd)가 구해둔 끝 번호(endPage)보다 작다면 끝 번호는 작은 값이 되어야 한다. 

**이전(prev)과 다음(next)**

이전은 시작 번호가 1보다 큰 경우에만 존재한다.

```java
this.prev = this.startPage > 1;
```

다음으로 가는 링크의 경우 realEnd가 끝 번호(endPage)보다 큰 경우에만 존재한다.

```java
this.next = this.endPage < realEnd;
```

#### 페이징 처리를 위한 클래스 설계

```java
package org.zerock.domain;

@Getter
@Setter
public class PageDTO {
  private int startPage;
  private int endPage;
  private boolean prev, next;
  
  private int total;
  private Criteria cri;
  
  public PageDTO(Criteria cri, int total) {
    this.cri = cri;
    this.total = total;
    
    this.endPage = (int) (Math.ceil(cri.getPageNum() / 10.0) * 10);
    this.startPage = endPage - 9;
    
    int realEnd = (int) (Math.ceil((total * 1.0) / cri.getAmount()));
    
    if (realEnd < this.endPage) {
      this.endPage = realEnd;
    }
    
    this.prev = this.startPage > 1;
    this.next = this.endPage < realEnd;
  }
  
}
```

실제 페이지를 클릭하면 동작을 하는 부분은 별도의 form 태그를 이용해서 처리한다.

list.jsp

```jsp
<form id='actionForm' action="/board/list" method='get'>
  <input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum}'>
  <input type='hidden' name='amount' value='${pageMaker.cri.amount}'>
</form>
```

BoardController에서는 PageDTO를 사용할 수 있도록 Model에 담아서 화면에 전달해 줄 필요가 있다.

```java
@GetMapping("/list")
public void list(Criteria cri, Model model) {
  log.info("list: " + cri);
  model.addAttribute("list", service.getList(cri));
  // 전체 데이터 수가 필요한데 아직 처리가 이루어지지 않았으므로 임의의 값으로 123을 지정한다.
  model.addAttribute("pageMaker", new PageDTO(cri, 123));
}
```

#### JSP에서 페이지 번호 출력

```jsp
<div class='pull-right'>
  <ul class="pagination">
    <c:if test="${pageMaker.prev}">
      <li class="paginate_button previous"><a href="#">Previous</a></li>
    </c:if>
    
    <c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
      <li class="paginate_button"><a href="#">${num}</a></li>
    </c:forEach>
    
    <c:if test="${pageMaker.next}">
      <li class="paginate_button next"><a href="#">Next</a></li>
    </c:if>
  </ul>
</div>
```

a 태그의 href 속성을 이용해 페이지 번호를 클릭했을 대의 이벤트 처리를 할 수도 있지만, 직접 링크를 처리하는 방식의 경우 검색 조건이 붙고 난 후에 처리가 복잡하게 된다. 따라서 자바스크립트로 처리한다.

기존에 동작하던 JavaScript 부분은 아래와 같이 기존의 코드에 페이지 번호를 클릭하면 처리하는 부분이 추가된다.

```javascript
$(document).ready(function() {
  var result = '<c:out value="${result}"/>';
  
  checkModal(result);
  
  history.replaceState({}, null, null);
  
  function checkModal(result) {
    
    if (result === '' || history.state) {
      return;
    }
    
    if (parseInt(result) > 0) {
      $(".modal-body").html(
      "게시글 "+ parseInt(result) + " 번이 등록되었습니다.");
    }
    
    $("#mymodal").modal("show");
  } 
  
  $("#regBtn").on("click", function() {
    self.location = "/board/register";
  });
  
  var actionForm = $("#actionForm");
  
  $(".paginate_button a").on("click", function(e) {
    e.preventDefault();
    
    console.log('click');
    
    actionForm.find("input[name='pageNum']").val($(this).attr("href"));
    actionForm.submit(); // action폼 자체를 submit 시킨다.
  });
});
```

#### 조회 페이지로 이동

사용자가 3페이지에 있는 게시글을 클릭한 후 다시 목록으로 이동해 보면 무조건 1페이지 목록 페이지로 이동하는 증상이 있다. 이를 해결하기 위해서는 조회 페이지로 갈 때 현재 목록 페이지의 pageNum과 amount를 같이 전달해야 한다. 이런 경우 페이지 이동에 사용했던  `<from>` 태그에 추가로 게시물의 번호를 같이 전송하고, action 값을 조정해서 처리할 수 잇다. 

a 태그로 복잡한 링크를 생성하는 방식이 나쁘다고는 말할 수 없다. 검색엔진에서는 출력된 정보와 링크를 저장해서 사용하기 때문에 a 태그 내의 링크가 완전한 URL인 경우가 노출에 유리하다. 

직접 링크로 연결된 경로를 페이지 이동과 마찬가지로 `<form>`태그를 이용해서 처리할 것이므로 a 태그에는 이동하려는 게시물의 번호만을 가지게 수정한다.

```jsp
<td>
  <a class='move' href='<c:out value="${board.bno}"/>'>
  	<c:out value="${board.title}"/></a>
</td>
```

JS를 통해서 게시물의 제목을 클릭했을 때 이동하도록 이벤트 처리를 새로 작성한다.

```js
$(".move").on("click", function(e){
  e.preventDefault();
  actionForm.append("<input type='hidden' name='bno' value='" + $(this).attr("href")+"'>");
  actionForm.attr("action", "/board/get")l;
  actionForm.submit();
})
```

**조회 페이지에서 다시 목록 페이지로 이동 - 페이지 번호 유지**

BoardController에의 get() 메서드는 원래는 게시물의 번호만 받도록 처리되어 있었지만, 추가적인 파라미터가 붙으면서 Criteria를 파라미터로 추가해서 받고 전달한다.

```java
@GetMapping({"/get", "/modify"})
public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, Model model) {
  log.info("/get or modify");
  model.addAttribute("board", service.get(bno));
}
```

- **`@ModelAttribute` 는 자동으로 Model 에 데이터를 지정한 이름으로 담아준다.**
- **`@ModelAttribute`를 사용하지 않아도 Controller에서 화면으로 파라미터가 된 객체는 전달이 되지만**, 좀 더 명시적으로 이름을 지정하기 위해서 사용한다.

```jsp
<form id='operForm' action="/board/modify" method="get">
  <input type='hidden' id='bno' name='bno' value='<c:out value="${board.bno}"/>'>
  <input type='hidden' name='pageNum' value='<c:out value="${cri.pageNum}"/>'>
  <input type='hidden' name='amount' value='<c:out value="${cri.amount}"/>'>
</form>
```

**조회 페이지에서 수정/삭제 페이지로 이동**

BoardController에서는  get() 메서드에서 '/get'과 '/modify'를 같이 처리하므로 별도의 추가적인 처리 없이도 Criteria를 Model에 cri라는 이름으로 담아서 전달한다. 

modify.jsp에서는 `<form>` 태그를 이용해서 데이터를 처리한다. 

```jsp
<div class="panel-heading">Board Modify Page</div>
<div class="panel-body">
  <form role="form" action="/board/modify" method="post">
    <input type='hidden' name='pageNum' value='<c:out vlaue="${cri.pageNum}"/>'>
    <input type='hidden' name='amount' value='<c:out vlaue="${cri.amount}"/>'>
  </form>
</div>
```

POST 방식으로 진행하는 수정과 삭제 처리는 BoardController에서 각각의 메서드 형태로 구현되어 있으므로 페이지 관련 파라미터들을 처리하기 위해서는 변경할 필요가 있다.

```java
@PostMapping("/modify")
public String modify(BoardVO board, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
  log.info("modify: " + board);
  
  if (service.modify(board)) {
    rttr.addFlashAttribute("result", "success");
  }
  
  rttr.addAttribute("pageNum", cri.getPageNum());
  rttr.addAttribute("amount", cri.getAmount());
  
  return "redirect:/board/list";
}
```

삭제 처리

```java
@PostMapping("/remove")
public String remove(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
  log.info("remove: " + board);
  
  if (service.remove(board)) {
    rttr.addFlashAttribute("result", "success");
  }
  
  rttr.addAttribute("pageNum", cri.getPageNum());
  rttr.addAttribute("amount", cri.getAmount());
  
  return "redirect:/board/list";
}
```

**수정/삭제 페이지에서 목록 페이지로 이동**

수정/삭제를 취소하고 다시 목록 페이지를 이동할 때 다음과 같이 한다. 목록 페이지는 오직 pageNum과 amount만을 사용하므로 `<form>` 태그의 다른 내용은 삭제하고 필요한 내용만을 다시 추가하는 형태가 편리하다.

```js
$(document).ready(function(){
  
  var formObj = $("form");
  
  $('button').on("click", function(e) {
    
    e.preventDefault();
    
    var operation = $(this).data("oper");
    
    console.log(operation);
    
    if(operation === 'remove') {
      formObj.attr("action", "/board/remove");
    } else if (operation === 'list') {
      //move to list
      formObj.attr("action", "/board/list").attr("method", get);
      var pageNumTag = $("input[name='pageNum']").clone();
      var amountTag = $("input[name='amount']").clone();
      
      formObj.empty();
      formObj.append(pageNumTag);
      formObj.append(amountTag);
    }
    formObj.submit();
  });
});
```

만일 사용자가 'List' 버튼을 클릭한다면 `<form>` 태그에서 필요한 부분만 잠시 복사(clone)해서 보관해 두고 `<form>` 태그 내의 모든 내용은 지워버린다. 이후에 다시 필요한 태그만 추가해서 '/board/list'를 호출한다.

#### MyBatis에서 전체 데이터의 개수 처리

```java
public interface BoardMapper {
  //..
  public int getTotalCount(Criteria cri);
}
```

getTotalCount()는 Criteria를 파라미터로 전달받도록 하지 않아도 문제가 생기지는 않지만, 게시물의 목록과 전체 데이터 수를 구하는 작업은 일관성 있게 Criteria를 받는 것이 좋다. 이는 후에 검색에서 필욯다ㅏ.

```xml
<select id="getTotalCount" resultType="int">
  select count(*) from tbl_board where bno > 0
</select>
```

BoardService

```java
@Override
public int getTotal(Criteria cri) {
  log.info("get total count");
  return mapper.getTotalCount();
}
```

getTotal()에 굳이 Criteria는 파라미터로 전달될 필요가 없기는 하지만, 목록과 전체 데이터 개수는 항상 같이 동작하는 경우가 많기 때문에 파라미터로 지정한다.

BoardController

```java
@GetMapping("/list")
public void list(Criteria cri, Model model) {
  log.info("list: " + cri);
  model.addAttribute("list", service.getList(cri));
  
  int total = service.getTotal(cri);
  
  log.info("total: " + total);
  
  model.addAttribute("pageMaker", new PageDTO(cri, total));
}
```

### 검색 처리

- 검색 기능은 검색 조건과 키워드로 나누어 생각할 수 있다.
- 검색 조건은 일반 웹 사이트에서 일반 사용자의 경우 `<select>` 태그를 이용해서 작성하고, 관리자용이나 검색 기능이 강한 경우 `<checkbox>`를 이용하는 경우가 많다.

#### 검색 기능과 SQL

검색 기능은 다음과 같이 나눠진다.

- 제목/내용/작성자와 같이 단일 항목 검색
- 제목 or  내용, 제목 or 작성자, 내용 or 작성자, 제목 or 내용 or 작성자와 같은 다중 항목 검색

오라클은 페이징 처리에 인라인뷰를 이용하기 때문에 실제로 검색 조건에 대한 처리는 인라인뷰의 내부에서 이뤄져야 한다. 

단일 항목의 검색은 검색 조건에 따라서 칼럼이 달라지고, LIKE 처리를 통해 키워드를 사용한다. 

2페이지에 해당하는 데이터를 '제목'으로 검색하고, 키워드는 'Test'라고 한다면 다음과 같이 작성될 수 있다. 

```sql
select
*
from
	(
    select /*+INDEX_DESC(tbl_board pk_board) */
    	rownum rn, bno, title, content, writer, regdate, updatedate
    from
    	tbl_board
    where
    -- 변경부분
    title like '%Test%'
    
    and rownum <= 20
  )
where rn > 10;
```

인라인뷰 안쪽에서 필요한 데이터를 가져올 때 검색조건이 적용되어야 하기 때문에 WHERE문 뒤에 검색 조건이 추가되고, ROWNUM 조건이 뒤따르게 하면 문제가 없다. 

**다중 항목 검색**

제목이나 내용 중 'TEST'라는 문자열이 있는 게시물 검색 시 다음과 같이 작성될 것이라고 예상하곤 한다.   

```sql
select
*
from
	(
    select /*+ INDEX_DESC(tbl_board pk_board) */
    	rownum rn, bno, title, content, writer, regdate, updatedate
    from
    	tbl_board
    where
    	title like '%Test' or content like '%Test%'
    and rownum <= 20
  )
where rn > 10;
```

- 이 쿼리문을 실제로 동작시켜 보면 10개보다 더 많은 데이터가 검색된다.
- AND 연산자가 OR 연산자보다 우선순위가 높기 때문이다.
- ROWNUM이 20보다 작거나 같으면서(AND) 내용에 'Test'라는 문자열이 있거나(OR) 제목에 Test라는 문자열이 있는 게시글을 검색한다. 제목에 TEST라는 문자열이 있는 경우는 많기 때문에 많은 양의 데이터를 가져온다. 따라서 괄호로 묶어준다.

```sql
select
*
from
	(
    select /*+ INDEX_DESC(tbl_board pk_board) */
    	roawnum rn, bno, title, content, writer, regdate, updatedate
    from
    	tbl_board
    where
    	(title like '%Test' or content like '%Test%')
    and rownum <= 20
  )
where rn > 10;
```

#### MyBatis의 동적 SQL

검색 조건이 변하면 SQL의 내용 역시 변하기 때문에 XML이나 어노테이션 같이 고정된 문자열을 작성하는 방법으로는 제대로 처리할 수 없다.

MyBatis는 동적(Dynamic) 태그 기능을 통해 SQL을 파라미터들의 조건에 맞게 조정할 수 있는 기능을 제공한다. MyBatis의 동적 태그는 약간의 구문을 이용해서 전달되는 파라미터를 가공해서 경우에 따라 다른 SQL을 만들어서 실행할 수 있다.

**MyBatis의 동적 태그들**

```xml
select * from tbl_board
	<trim prefix="where (" suffix=")" prefixOverrides="OR">
    <forEach item="val" index="key" collection="map">
      <trim prefix="OR">
        <if test="type == 'T'.toString()">
  				(title like '%'||#{keyword}||'%')
        </if>
        <if test="type == 'C'.toString()">
          (content like '%'||#{keyword}||'%')
        </if>
        <if test="type == 'W'.toString()">
          (writer like '%'||#{keyword}||'%')
        </if>
      </trim>
    </forEach>
	</trim> 
```

- trim, where, set은 단독으로 사용되지 않고 `<if>`, `<choose>`와 같은 태그들을 내포하여 SQL을 연결해주고, 앞 뒤에 필요한 구문들(AND, OR, WHERE)를 추가하거나 생략하는 역할을 한다.
- 예를 들어 'where rownum <= 20'은 AND라는 키워드가 필요 없지만, 검색 조건이 추가되면 AND가 필요하다 (WHERE title='Test' AND ROWNUM <= 20). 이처럼 where, trim, set은 필요한 키워드를 붙이거나 빼는 상황에서 사용한다.

#### 검색 조건 처리를 위한 Criteria의 변화

페이징 처리에 사용했던 Criteria의 의도는 단순히 'pageNum'과 'amount'라는 파라미터를 수집하기 위해서였다. 그러나 페이징처리에 검색 조건 처리가 들어가면 Criteria 역시 변화가 필요하다. 검색 조건(type)과 검색에 사용하는 키워드가 필요하므로 Criteria를 확장한다. 확장 방법으로는 상속 방법을 이용하거나 직접 Criteria를 수정할 수 있다. 여기서는 수정하는 방식으로 하겠다. 

```java
package org.zerock.domain;

@Getter
@Setter
@ToString
public class Criteria {
  
  private int pageNum;
  private int amount;
  
  private String type;
  private String keyword;
  
  public Criteria() {
    this(1, 10);
  }
  
  public Criteria(int pageNum, int amount) {
    this.pageNum = pageNum;
    this.amount = amount;
  }
  
  public String[] getTypeArr() {
    return type == null? new String[] {} : type.split("");
  }
}
```

**BoardMapper.xml에서 Criteria 처리**

```xml
<select id ="getListWithPaging" resultType="org.zerock.domain.BoardVO">
  <![CDATA[
	select
		bno, title, content, writer, regdate, updatedate
	from
		(
		select /*+INDEX_DESC(tbl_board pk_board) */
			rownum rn, bno, title, content, writer, regdate, updatedate
		from
			tbl_board
		where 
	]]>
  <trim prefix="(" suffix=") AND " prefixOverrides="OR">
    <foreach item ='type' collection="typeArr">
      <trim prefix="OR">
        <choose>
          <when test="type == 'T'.toString()">
            title like '%'||#{keyword}||'%'
          </when>
          <when test="type == 'C'.toString()">
            content like '%'||#{keyword}||'%'
          </when>
          <when test="type == 'W'.toString()">
            writer like '%'||#{keyword}||'%'
          </when>
        </choose>
      </trim>
    </foreach>
  </trim>
<![CDATA[
rownum <= #{pageNum} * #{amount}
		)
	where rn > (#{pageNum} - 1) * #{amount}
]]>
</select>
```

MyBatis는 원하는 속성을 찾을 때 getTypeArr()과 같이 이름에 기반을 두어서 검색하기 때문에 Criteria에서 만들어둔 getTypeArr()의 결과인 문자열의 배열이 `<foreach>` 의 대상이 된다. (MyBatis는 엄격하게 Java Beans의 규칙을 따르지 않고, get/set 메서드만을 활용한다.

`<choose>` 안쪽의 동적 SQL은 'OR title... OR content ... OR writer..'와 같은 구문을 만들어낸다. 따라서 바깥쪽에는 `<trim>`을 이용해서 맨 앞에서 생성되는 'OR'을 없애준다. 

```java
@Test
public void testSearch() {
  Criteria cri = new Criteria();
  cri.setKeyword("새로");
  cri.setType("TC");
  List<BaordVO> list = mapper.getListWithPaging(cri);
  list.forEach(board -> log.info(board));
}
```

**`<sql>` `<include>`와 검색 데이터의 개수 처리**

```xml
<sql id="criteria">
  <trim prefix="(" suffix=") AND " prefixOverrides="OR">
    <foreach item ='type' collection="typeArr">
      <trim prefix="OR">
        <choose>
          <when test="type == 'T'.toString()">
            title like '%'||#{keyword}||'%'
          </when>
          <when test="type == 'C'.toString()">
            content like '%'||#{keyword}||'%'
          </when>
          <when test="type == 'W'.toString()">
            writer like '%'||#{keyword}||'%'
          </when>
        </choose>
      </trim>
    </foreach>
  </trim>
</sql>

<select id ="getListWithPaging" resultType="org.zerock.domain.BoardVO">
  <![CDATA[
	select
		bno, title, content, writer, regdate, updatedate
	from
		(
		select /*+INDEX_DESC(tbl_board pk_board) */
			rownum rn, bno, title, content, writer, regdate, updatedate
		from
			tbl_board
		where 
	]]>
	<include refid="criteria"></include>
<![CDATA[
rownum <= #{pageNum} * #{amount}
		)
	where rn > (#{pageNum} - 1) * #{amount}
]]>
</select>

<select id="getTotalCount" resultType="int">
  select count(*) from tbl_board
  where
  <include refid="criteria"></include>
  bn > 0
</select>
```

#### 화면에서 검색 조건 처리

화면에서 검색은 다음과 같은 사항을 주의해서 개발해야 한다.

- 페이지 번호가 파라미터로 유지되었던 것처럼 검색 조건과 키워드 역시 항상 화면 이동 시 같이 전송되어야 한다.
- 화면에서 검색 버튼을 클릭하면 새로 검색을 한다는 의미이므로 1페이지로 이동한다.
- 한글의 경우 GET 방식으로 이동하는 경우 문제가 생길 수 있으므로 주의해야 한다.

```jsp
<div class='row'>
  <div class="col-lg-12">
    <form id='searchForm' action="/board/list" method='get'>
      <select name='type'>
        <option value="">--</option>
        <option value="T">제목</option>
        <option value="C">내용</option>
        <option value="W">작성자</option>
        <option value="TC">제목 or 내용</option>
        <option value="TW">제목 or 작성자</option>
        <option value="TCW">제목 or 내용 or 작성자</option>
      </select>
      <input type='text' name='keyword'/>
      <input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum}'>
      <input type='hidden' name='amount' value='${pageMaker.cri.amount}'>
      <button class='btn btn-default'>
        Search
      </button>
    </form>
  </div>
</div>
```

여전히 다음과 같은 문제가 남아있다.

- 3페이지를 보다 검색을 하면 3페이지로 이동하는 문제
- 검색 후 페이지를 이동하면 검색 조건이 사라지는 문제
- 검색 후 화면에서는 어떤 검색 조건과 키워드를 이용했는지 알 수 없는 문제

**검색 버튼의 이벤트 처리**

검색 버튼을 클릭하면 검색은 1페이지를 하도록 수정하고, 화면에 검색 조건과 키워드가 보이게 한다.

```js
var searchForm = $("#searchForm");

$("#searchForm button").on("click", function(e) {
  if (!searchForm.find("option:selected").val()) {
    alert("검색종류를 선택하세요");
    return false;
  }
  if (!searchForm.find("input[name='keyword']").val()) {
    alert("키워드를 입력하세요");
    return false;
  }
  searchForm.find("input[name='pageNum']").val("1");
  e.preventDefault();
  
  searchForm.submit();

});
```

브라우저에서 검색 버튼을 클릭하면 `<form>` 태그의 전송은 막고, 페이지 번호는 1이 되도록 처리한다. 화면에서 키워드가 없다면 검색을 하지 않도록 제어한다.

검색 후에는 주소창에 검색 조건과 키워드가 같이 GET 방식으로 처리되므로 이를 이용해서 `<select>` 태그나 `<input>` 태그의 내용을 수정한다.

```jsp
<div class='row'>
  <div class="col-lg-12">
    <form id='searchForm' action="/board/list" method='get'>
      <select name='type'>
        <option value="" 
                <c:out value="${pageMaker.cri.type==null? 'selected':''}"/>>--</option>
        <option value="T" 
                <c:out value="${pageMaker.cri.type eq 'T'? 'selected':''}"/>>제목</option>
        <option value="C"
                <c:out value="${pageMaker.cri.type eq 'C'? 'selected':''}"/>>내용</option>
        <option value="W"
                <c:out value="${pageMaker.cri.type eq 'W'? 'selected':''}"/>>작성자</option>
        <option value="TC"
                <c:out value="${pageMaker.cri.type eq 'TC'? 'selected':''}"/>>제목 or 내용</option>
        <option value="TW"
                <c:out value="${pageMaker.cri.type eq 'TW'? 'selected':''}"/>>제목 or 작성자</option>
        <option value="TCW"
                <c:out value="${pageMaker.cri.type eq 'TCW'? 'selected':''}"/>>제목 or 내용 or 작성자</option>
      </select>
      <input type='text' name='keyword'/>
      <input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum}'>
      <input type='hidden' name='amount' value='${pageMaker.cri.amount}'>
      <button class='btn btn-default'>
        Search
      </button>
    </form>
  </div>
</div>
```

`<select>` 태그의 내부는 삼항 연산자를 이용해서 해당 조건으로 검색되었다면 'selected'라는 문자열을 출력하게 해서 화면에서 선택된 항목으로 보이도록 한다.

페이지 번호를 클릭해서 이동할 때도 검색 조건과 키워드는 같이 전달되어야 하므로 페이지 이동에 사용한 `<form>` 태그를 다음과 같이 수정한다.

```jsp
<form id='actionForm' action="/board/list" method='get'>
  <input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum}'>
  <input type='hidden' name='amount' value='${pageMaker.cri.amount}'>
  <input type='hidden' name='type' value='<c:out value="${pageMaker.cri.type}"/>'>
  <input type='hidden' name='keyword' value= '<c:out value="${pageMaker.cri.type}"/>'>
</form>
```

검색 조건과 키워드에 대한 처리가 되면 검색 후 페이지를 이동해서 동일한 검색 사항들이 계속 유지된다.

**조회 페이지에서 검색 처리**

```jsp
<form id='actionForm' action="/board/modify" method='get'>
  <input type='hidden' id='bno' name='bno' value='<c:out value="${board.bno}"/>'>
  <input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum}'>
  <input type='hidden' name='amount' value='${pageMaker.cri.amount}'>
  <input type='hidden' name='type' value='<c:out value="${pageMaker.cri.type}"/>'>
  <input type='hidden' name='keyword' value= '<c:out value="${pageMaker.cri.keyword}"/>'>
</form>
```

**수정/삭제 페이지에 검색 처리**

```jsp
<form id='actionForm' action="/board/modify" method='post'>
  <input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum}'>
  <input type='hidden' name='amount' value='${pageMaker.cri.amount}'>
  <input type='hidden' name='type' value='<c:out value="${pageMaker.cri.type}"/>'>
  <input type='hidden' name='keyword' value= '<c:out value="${pageMaker.cri.keyword}"/>'>
</form>
```

수정/삭제 처리는 BoardController에서 redirect 방식으로 동작하므로 type와 keyword 조건을 같이 리다이렉트 시에 포함시켜야 한다.

```java
@PostMapping("/modify")
public String modify(BoardVO board, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
  log.info("modify: " + board);
  
  if (service.modify(board)) {
    rttr.addFlashAttribute("result", "success");
  }
  
  rttr.addAttribute("pageNum", cri.getPageNum());
  rttr.addAttribute("amount", cri.getAmount());
  rttr.addAttribute("amount", cri.getType());
  rttr.addAttribute("amount", cri.getKeyword());
  
  return "redirect:/board/list";
}

@PostMapping("/remove")
public String remove(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
  log.info("remove: " + board);
  
  if (service.remove(board)) {
    rttr.addFlashAttribute("result", "success");
  }
  
  rttr.addAttribute("pageNum", cri.getPageNum());
  rttr.addAttribute("amount", cri.getAmount());
  rttr.addAttribute("amount", cri.getType());
  rttr.addAttribute("amount", cri.getKeyword());
  
  return "redirect:/board/list";
}
```

리다이렉트는 GET 방식으로 이루어지기 때문에 추가적인 파라미터를 처리해야 한다. modify.jsp에서는 다시 목록으로 이동하는 경우에 필요한 파라미터만 전송하기 위해서 `<form>`태그의 모든 내용을 지우고 다시 추가하는 방식을 이용했으므로 keyword와 type 역시 추가하도록 아래와 같이 관련된 JS 코드를 수정해야 한다.

```js
$(document).ready(function(){
  
  var formObj = $("form");
  
  $('button').on("click", function(e) {
    
    e.preventDefault();
    
    var operation = $(this).data("oper");
    
    console.log(operation);
    
    if(operation === 'remove') {
      formObj.attr("action", "/board/remove");
    } else if (operation === 'list') {
      //move to list
      formObj.attr("action", "/board/list").attr("method", get);
      var pageNumTag = $("input[name='pageNum']").clone();
      var amountTag = $("input[name='amount']").clone();
      var keywordTag = $("input[name='keyword']").clone();
      var typeTag = $("input[name='type']").clone();
      
      formObj.empty();
      formObj.append(pageNumTag);
      formObj.append(amountTag);
      formObj.append(keywordTag);
      formObj.append(typeTag);
    }
    formObj.submit();
  });
});
```

**UriComponentsBuilder를 이용하는 링크 생성**

- 웹페이지에서 매번 파라미터를 유지하는 일이 번거롭고 힘들다면 org.springframework.web.util.UriComponentsBuilder 클래스를 사용해볼 수 있다.
- 이 클래스는 여러 개의 파라미터를 연결해서 URL의 형태로 만들어주는 기능을 가지고 있다.
- URL을 만들어주면 리다이렉트를 하거나, `<form>` 태그를 사용하는 상황을 많이 줄일 수 있다. 검색 조건을 유지하는 org.zerock.domain.Criteria 클래스에 링크를 생성하는 기능을 추가한다.

Criteria

```java
public String getListLink() {
  UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
    .queryParam("pageNum", this.pageNum)
    .queryParam("amount", this.getAmount())
    .queryParam("type", this.getType())
    .queryParam("keyword", this.getKeyword);
  
  return builder.toUriString();
}
```

UriComponentsBuilder는 queryParam()이라는 메서드를 이용해 필요한 데이터들을 손쉽게 추가할 수 있다. getListLink()의 결과는 GET 방식에 적합한 URL인코딩된 결과로 만들어진다. 특히, 한글 처리에 신경쓰지 않아도 되어서 굉장히 편리하다. 

getListLink()를 이용하면 BoardControllerㅡ이 modify()와 remove()를 다음과 같이 간단하게 정리할 수 있다.

```java
@PostMapping("/modify")
public String modify(BoardVO board, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
  log.info("modify: " + board);
  
  if (service.modify(board)) {
    rttr.addFlashAttribute("result", "success");
  }
  
  return "redirect:/board/list" + cri.getListLink();
}

@PostMapping("/remove")
public String remove(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
  log.info("remove: " + board);
  
  if (service.remove(board)) {
    rttr.addFlashAttribute("result", "success");
  }
  
  rttr.addAttribute("pageNum", cri.getPageNum());
  rttr.addAttribute("amount", cri.getAmount());
  rttr.addAttribute("amount", cri.getType());
  rttr.addAttribute("amount", cri.getKeyword());
  
  return "redirect:/board/list" + cri.getListLink();
}
```

UriComponentsBuilder로 생성된 URL은 화면에서도 유용하게 사용될 수 있는데, 주로 JS를 사용할 수 없는 상황에서 링크를 처리해야 하는 상황에서 사용된다