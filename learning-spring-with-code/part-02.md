## Part 2. 스프링 MVC 설정

- Servlet/JSP 를 이용하는 개발에 비해 간단하고, 빠른 개발이 가능하다.

### 스프링 MVC의 기본 구조

- 스프링 MVC 프로젝트를 구성해서 사용한다는 의미: root-context.xml로 사용하는 일반 Java 영역(흔히 POJO)과 servlet-context.xml로 설정하는 Web 관련 영역을 같이 연동해서 사용하게 된다. 

![Bhqyg4sAQ2KqhCOaO8n7IA_thumb_a4f9](https://user-images.githubusercontent.com/50407047/111818611-b85e1900-8922-11eb-9a21-9a119dbef439.jpg)

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

  ![o09awz2SQWmj3xFWf1zYvg_thumb_a4fa](https://user-images.githubusercontent.com/50407047/111818624-bb590980-8922-11eb-9883-60c19771a78f.jpg)

- root-context.xml이 처리된 후에는 스프링 MVC에서 사용하는 DispatcherServlet이라는 서블릿과 관련된 설정이 동작한다.

- DispatcherSerlvet 클래스는 스프링 MVC 구조에서 가장 핵심적인 역할을 한다. 내부적으로 웹 관련 처리의 준비작업을 진행하는데 이 때 사용하는 파일이 servlet-context.xml  이다. DispatcherServlet은 XmlWebApplicationContext를 이용해서 servlet-context.xml을 로딩하고 해석한다. 이 과정에서 등록된 객체들은 기존에 만들어진 객체들과 같이 연동된다.

  ![BN4ZCsz7RgyEKXy2+ek%zg_thumb_a4fb](https://user-images.githubusercontent.com/50407047/111818638-beec9080-8922-11eb-9973-48d09b927be3.jpg)

  



### 스프링 MVC의 기본 사상

![MFZQa+S2TG6pIF+%Zx2oyA_thumb_a4f7](https://user-images.githubusercontent.com/50407047/111818649-c1e78100-8922-11eb-9e77-9696f14f383a.jpg)

- Servlet/JSP에서는 HttpServletRequest/HttpServletResponse라는 타입의 객체를 이용해 브라우저에서 전송한 정보를 처리한다.
- 스프링 MVC의 경우 이 위에 하나의 계층을 더한 형태가 된다. 
- 따라서 스프링 MVC를 이용하게 되면 개발자들은 직접적으로 HttpServletRequest / HttpServletResponse와 같은 Servlet/JSP의 API를 사용할 필요성이 현저하게 줄어든다. 스프링은 중간에 연결 역할을 하기 때문에 이러한 코드를 작성하지 않고도 원하는 기능을 구현할 수 있게 된다.



### 모델2와 스프링 MVC

**모델2**

![Vne+q9+xQnKAd3o5Lz7kSw_thumb_a4fc](https://user-images.githubusercontent.com/50407047/111818663-c6139e80-8922-11eb-8723-cc4cf706dbaf.jpg)

- 모델2 방식은 로직과 화면을 분리하는 스타일의 개발방식이다. 
- Request는 먼저 Controller를 호출한다. 나중에 View를 교체하더라도 사용자가 호출하는 URL 자체에 변화가 없게 만들어주기 때문이다.
- 컨트롤러는 데이터를 처리하는 존재를 이용해서 데이터(Model)을 처리하고 Response할 때 필요한 데이터(Model)를 View 쪽으로 전달한다. 
- Servlet을 이용하는 경우 개발자들은 Servlet API의 RequestDispatcher 등을 이용해서 이를 직접 처리해왔지만 스프링 MVC는 내부에서 이러한 처리를 하고, 개발자들은 스프링 MVC의 API를 이용해서 코드를 작성하게 된다. 

**스프링 MVC의 기본 구조**

![zz0Gn6wLSnKmxwJCv0R2Hw_thumb_a4f6](https://user-images.githubusercontent.com/50407047/111818673-c875f880-8922-11eb-9821-c1609944f418.jpg)

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
-  @RequestMapping은 현재 클래스의 모든 메서드들의 기본적인 URL 경로가 된다.  클래스의 선언과 메서드 선언에 사용할 수 있다.
-  @Log4j는 @Log가 java.util.Logging을 이용하는 데 반해 Log4j 라이브러리를 활용한다. 

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
- @ModelAttribute가 걸린 파라미터는 타입에 관계없이 무조건 Model에 담아서 전달되므로, 파라미터로 전달된 데이터를 다시 화면에서 사용해야 하는 경우에 사용한다.

```java
@GetMapping("/ex04")
public String ex04(SampleDTO dto, @ModelAttribute("page") int page) {
  log.info("dto: " + dto);
  log.info("page: " + page);
  return "/sample/ex04";
}
```

**RedirectAttributes**

RedirectAttributes는 일회성으로 데이터를 전달하는 용도로 사용한다. 기존에 Servlet에서는 response.sendRedirect()를 사용할 때와 동일한 용도로 사용된다.

```java
response.sendRedirect("/home?name=aaa&age=10")
```

스프링 MVC를 이용하는 경우

```java
rttr.addFlashAttribute("name", "AAA");
rttr.addFlashAttribute("age", 10);
return "redirect:/";
```

### Controller의 리턴 타입

스프링 MVC로 바뀌면서 가장 큰 변화 중 하나는 리턴 타입이 자유로워졌다는 것이다.

**Controller의 메서드가 사용할 수 있는 리턴 타입**

- String

  - jsp를 이용하는 경우 jsp 파일의 경로와 파일 이름을 나타내기 위해서 사용한다.
  - 특히 상황에 따라 다른 화면을 보여줄 필요가 있을 때 많이 사용한다. (if  else 같은 처리가 필요할 때)
  - redirect(리다이렉트 방식으로 처리), forward(포워드 방식으로 처리)와 같은 특별한 키워드를 붙여 사용할 수 있다.

- void

  - 호출하는 URL과 동일한 이름의 jsp를 의미한다.

- VO, DTO 타입

  - 주로 JSON 타입의 데이터를 만들어서 반환하는 용도로 사용한다.
  - 이를 위해서는 jackson-databind 라이브러리를 사용한다. 만일 이 라이브러리가 포함되지 않으면 "500: no converter found" 에러 화면이 나온다. 

- ResponseEntity 타입

  ```java
  @GetMapping("/ex07")
  public ResponseEntity<String> ex07() {
    log.info("/ex07.......");
    
    String msg = "{\"name\": \"홍길동\"}";
    
    HttpHeaders headers = new HttpHeaders();
    header.add("Content-Type", "application/json;charset=UTF-8");
    
    return new ResponseEntity<>(msg, header, HttpStatus.OK);
  }
  ```

  - HTTP 프로토콜의 헤더를 다루는 경우 사용한다.
  - HttpServletRequest나 HttpServletResponse를 직접 핸들링하지 않아도 ResponseEntity를 통해 원하는 헤더 정보나 데이터를 전달할 수 있다. 
  - ResponseEntity는 HttpHeaders 객체를 같이 전달할 수 있고, 이를 통해서 원하는 HTTP 헤더 메시지를 가공하는 것이 가능하다.

- Model, ModelAndView: Model로 데이터를 반환하거나 화면까지 같이 지정하는 경우에 사용한다. (많이 사용하지는 않는 편)

- HttpHeaders: 응답에 내용 없이 Http 헤더 메시지만 전달하는 용도로 사용한다. 

### Controller의 Exception 처리

- @ExceptionHandler와 @ControllerAdvice를 이용한 처리
- @ResponseEntity를 이용한 예외 메시지 구성

**@ControllerAdvice**

- AOP를 이용하는 방식이다. AOP는 핵심적인 로직은 아니지만 프로그램에서 필요한 공통적인 관심사(cross-concern)을 분리하자는 개념이다.
- 메서드의 모든 예외사항을 전부 핸들링해야 한다면 중복적이고 많은 양의 코드를 작성해야 하지만, AOP 방식을 이용하면 공통적인 예외사항에 대해서는 별도로 @ControllerAdvice를 이용해서 분리하는 방식이다.

```java
package org.zerock.exception;

@ControllerAdvice
@Log4j
public class CommonExceptionAdvice {
  @ExceptionHandler(Exception.class)
  public String except(Exception ex, Model model) {
    log.error("Exception......." + ex.getMessage());
    model.addAttribute("exception", ex);
    log.error(model);
    return "error_page";
  }
}
```

- @ControllerAdvice: 해당 객체가 스프링의 컨트롤러에서 발생하는 예외를 처리하는 존재임을 명시하는 용도
- @ExceptionHandler: 해당 메서드가 () 들어가는 예외타입을 처리한다는 것을 의미한다. 이 속성의 자리에는  Exception 클래스 타입을 지정할 수 있다. 여기서는 Exception.classs 를 지정하였으므로 모든 예외에 대한 처리가 except()만을 이용해서 처리할 수 있다.
- 여기서 org.zerock.exception 패키지는 servlet-context.xml에서 인식하지 않기 때문에 `<component-scan>`을 이용해서 해당 패키지의 내용을 조사하도록 해야 한다.

**404 에러 페이지**

- WAS의 구동 중 가장 흔한 HTTP 상태 코드는 404와 505에러이다.
- 505: Internal Server Error: @ExceptionHandler를 이용해서 처리된다.
- 404: 잘못된 URL을 호출할 때 보이는 메시지

스프링 MVC의 모든 요청은 DispatcherServlet을 이용해서 처리되므로 404 에러도 같이 처리할 수 있도록 webConfg을 수정한다.

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
  @Override
  protected void customizeRegistration(ServletRegistration.Dynamicregistration) {
    registration.setInitParameter("throwExceptionIfNoHandlerFound", true);
  }
}
```

org.zerock.exception.CommonExceptionAdvice에는 다음과 같이 메서드를 추가한다.

```java
@ExceptionHandler(NoHandlerFoundException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
public String handle404(NoHandlerFoundException ex) {
  return "custom404";
}
```

custom.jsp

```jsp
<body>
  <h1>
    해당 URL은 존재하지 않습니다.
  </h1>
</body>
```