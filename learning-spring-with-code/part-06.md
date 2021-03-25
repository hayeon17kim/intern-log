# Spring Web Security를 이용한 로그인 처리

스프링 시큐리티를 이용하면 다음과 같은 작업을 간편히 처리할 수 있다.

- 로그인 처리와 CSRF 토큰 처리
- 암호화 처리
- 자동로그인
- JSP에서의 로그인 처리

## Spring Web Security 소개

![UNADJUSTEDNONRAW_thumb_a53f](https://user-images.githubusercontent.com/50407047/112523613-4a15cc80-8de2-11eb-969c-ce82fed3fd38.jpg)

- 스프링 시큐리티의 기본 동작은 서블릿의 여러 종류의 필터와 인터셉터를 이용해서 처리한다. 
- 인터셉터는 스프링에서 필터와 유사한 역할을 한다.
- 필터와 인터셉터는 서블릿이나 컨트롤러의 접근에 관여한다는 점에서 유사하다.
- 필터는 스프링과 무관하게 서블릿 자원이고, 인터셉터는 스프링의 빈으로 관리되면서 스프링의 컨텍스트 내에 속한다는 차이점이 있다.
- 스프링 시큐리티는 인터셉터와 필터를 이용하면서 별도의 컨텍스트를 생성해서 처리한다. 

![UNADJUSTEDNONRAW_thumb_a53d](https://user-images.githubusercontent.com/50407047/112523693-5b5ed900-8de2-11eb-9d11-d1e877628f40.jpg)

- 스프링 시큐리티는 현재 동작하는 스프링 컨텍스트 내에서 동작하기 때문에 이미 컨텍스트에 포함된 여러 빈들을 같이 이용해서 다양한 방식의 인증 처리가 가능하도록 설계한다.

### Spring Web Security 의 설정

#### security-context.xml 생성

스프링 시큐리티는 단독으로 설정할 수 있기 때문에 기존의 root-context.xml이나 servlet-context.xml과는 별도로 security-context.xml을 다로 작성한다.

#### web.xml 설정

스프링 시큐리티가 스프링 MVC에서 사용되기 위해서는 필터를 이용해 스프링 동작에 관여하도록 설정한다.

스프링 시큐리티가 동작하기 위해서는 Authentication Manager와 스프링 시큐리티의 시작 시점이 필요하다.

### 시큐리티가 필요한 URI 설계

- /sample/all
- /sample/member
- /sample/admin

### 인증(Authentication)과 권한부여(Authorization - 인가)

![UNADJUSTEDNONRAW_thumb_a53e](https://user-images.githubusercontent.com/50407047/112523815-7d585b80-8de2-11eb-8376-9ffc4faea6c6.jpg)

- 인증(Authentication): 자신을 증명하는 것이다. 자기 스스로가 무언가 자신을 증명할 만한 자료를 제시하는 것이다. 
- 권한부여(Authorization): 남에 의해서 자격이 부여된다.

스프링 시큐리티 내부에도 이와 비슷한 구조를 가지고 있다.

AuthenticationManager은 가장 중요한 역할을 한다. 다양한 방식의 인증을 처리할 수 있도록 다음과 같은 구조로 설계되어 있다. 

![image](https://user-images.githubusercontent.com/50407047/112492397-918d6000-8dc4-11eb-899a-73ec14669917.png)

ProviderManager는 인증에 대한 처리를 AuthenticationProvider라는 타입의 객체를 이용해서 처리를 위임한다. AuthenticationProvider(인증 제공자)는 실제 인증 작업을 처리한다. 이때 인증된 정보에는 권한에 대한 정보를 같이 전달하게 되는데 이 처리는 UserDetailService와 관련이 있다. 이 구현체는 실제로 사용자의 정보와 사용자가 가진 권한의 정보를 처리해서 반환한다. 

![image](https://user-images.githubusercontent.com/50407047/112492790-e9c46200-8dc4-11eb-8d2b-d472a06c548c.png)

개발자가 스프링 시큐리티를 커스터마이징 하는 방식은 AuthenticationProvider를 직접 구현하는 방식과 실제 처리를 담당하는 UserDetailService를 구현하는 방식으로 나눠진다. 대부분 UserDetailService를 구현하는 형태를 사용하는 것만으로도 충분하지만, 새로운 프로토콜이나 인증 구현 방식을 직접 구현하는 경우에는 AuthenticationProvider 인터페이스를 직접 구현해서 사용한다. 

### 로그인과 로그아웃 처리

```xml
<security:http>
  <security:intercept-url pattern="/sample/all" access="permitall"/>
  <security:intercept-url pattern="/sample/member" access="hasRole('ROLE_MEMBER')"/>
  <security:form-login/>
</security:http>
<security:authentication-manager>
</security:authentication-manager>
```

- `<security:intercept-url>` 
  - 특정 URI에 접근할 때 인터셉터를 이용해서 접근을 제한
  - `pattern`: URI의 패턴을 의미
  - `access`: 권한을 체크한다. 속성값으로 사용되는 문자열은 표현식과, 권한명을 의미하는 문자열을 이용할 수 있다. `<security:http>`의 기본 설정은 표현식을 이용하는 것이다. 단순 문자열만 이용할 때는 `use-expressions='false'` 를 지정한다. 

설정을 변경하면 'sample/member'에 접근했을 때 'sample/all'과는 달리 로그인 페이지로 강제로 이동한다. 이 로그인 페이지는 스프링 시큐리티가 기본으로 제공하는 페이지이다. 

### 단순 로그인 처리

- 아이디와 패스워드로 로그인이 가능하도록 설정을 추가한다.
- 일반 시스템에서 userid는 스프링 시큐리티에서 username에 해당한다. 일반적으로 사용자 이름을 username이라고 처리하는 것과 혼동하면 안 된다.
- 스프링 시큐리티에서 User는 인증 정보와 권한을 가진 객체로, 일반적인 경우에 사용하는 사용자 정보와는 다르다. 이를 구분하기 위해 MemberVO클래스를 이용할 수 있다.

```xml
<security:http>
  <security:intercept-url pattern="/sample/all" access="permitall"/>
  <security:intercept-url pattern="/sample/member" access="hasRole('ROLE_MEMBER')"/>
  <security:form-login/>
</security:http>
<security:authentication-manager>
  <security:authentication-provider>
    <security:user-service>
      <security:user name="member" password="member" authorities="ROLE_MEMBER"/>
    </security:user-service>
  </security:authentication-provider>
</security:authentication-manager>
```

- 위 설정으로 'member'라는 계정 정보를 가진 사용자가 로그인을 할 수 있도록 만든다. 
- 그럼에도 로그인을 하면 에러가 발생하는데, 이는 PasswordEncoder가 없기 때문이다. 스프링 시큐리티 5버전부터 반드시 PasswordEncoder를 이용하도록 변경되었다.
- 임시방편으로 스프링 시큐리티 5버전에는 포맷팅 처리를 지정해서 패스워드 인코딩 방식을 지정할 수 있다.
- 만일 패스워드의 인코딩 처리 없이 사용하고 싶다면 패스워드 앞에 `{noop}` 문자열을 추가한다.

#### 로그아웃 확인

- 로그아웃을 구현하기 위해서는 브라우저에서 유지하고 있는 세션과 관련된 정보를 삭제한다.
- 개발자 도구에서 Application 탭을 확인해보면 Cookies 항목에 JSESSIONID와 같이 세션을 유지하는 데 사용되는 세션 쿠키의 존재를 확인할 수 있다. (JSESSIONID는 Tomcat에서 발생하는 쿠키의 이름이다. WAS마다 다른 이름을 사용할 수 있다.)
- 로그아웃은 JSESSION 쿠키를 강제로 삭제해서 처리한다.

#### 여러 권한을 가지는 사용자 설정

- 'sample/admin'은 `ROLE_ADMIN`이라는 권한을 가진 사용자가 접근할 수 있도록 지정한다. 
- 이때 사용자는 `ROLE_ADMIN`과 `ROLD_MEMBER` 라라는  2개의 권한을 가지도록 지정한다.

```xml
<security:http>
  <security:intercept-url pattern="/sample/all" access="permitall"/>
  <security:intercept-url pattern="/sample/member" access="hasRole('ROLE_MEMBER')"/>
  <security:intercept-url pattern="/sample/member" access="hasRole('ROLE_ADMIN')"/>
  <security:form-login/>
</security:http>
<security:authentication-manager>
  <security:authentication-provider>
    <security:user-service>
      <security:user name="member" password="{noop}member" authorities="ROLE_MEMBER"/>
      <security:user name="admin" password="{noop}admin" authorities="ROLE_MEMBER, ROLE_ADMIN"/>
    </security:user-service>
  </security:authentication-provider>
</security:authentication-manager>
```

Admin 계정을 가진 사용자는 `/sample/member`와 `/sample/admin` 모두에 접근할 수 있다.

#### 접근 제한 메시지의 처리

- 로그인은 했지만 URI를 접근할 권한이 없는 상황에는 접근 제한 에러 메시지를 보게 된다. (403 Forbidden)
- 스프링 시큐리티에서는 접근 제한에 대해 AccessDeniedHandler를 직접 구현하거나 특정 URI를 지정할 수 있다.

```xml
<security:http auto-config="true" use-expressions="true">
  <security:intercept-url pattern="/sample/all" access="permitall"/>
  <security:intercept-url pattern="/sample/member" access="hasRole('ROLE_MEMBER')"/>
  <security:intercept-url pattern="/sample/member" access="hasRole('ROLE_ADMIN')"/>
  <security:form-login/>
  <security:access-denied-handler error-page="/accessError"/>
</security:http>
```

CommonController

```java
@Controller
@Log4j
public class CommonController {
  
  @GetMapping("/accessError")
  public void accessDenied(Authentication auth, Model model) {
    log.info("access Denied: " + auth);
    model.addAttribute("msg", "Access Denied");
  }
}
```

accessError.jsp

```jsp
<h1>Access Denied</h1>
<h2><c:out value="${SPRING_SECURITY_403_EXCEPTION.getMessage()}"/></h2>
<h2><c:out value="${msg}"/></h2>
```

Access Denied인 경우 403 에러 메시지가 발생한다. JSP에는 HttpServletRequest 안에 `SPRING_SECURITY_403_EXCEPTION`이라는 이름으로 AccessDeniedException 객체가 전달된다. 

#### AccessDeniedHandler 인터페이스를 구현하는 경우

- error-page만을 제공하는 경우에는 사용자가 접근했던 URI 자체의 변화는 없다. 
- 접근 제한이 된 경우 다양한 처리를 하고싶다면 AccessDeniedHandler 인터페이스를 구현하는 것이 좋다.
- 접근 제한이 되었을 때 쿠키나 세션에 특정한 작업을 하거나 HttpServletResponse에 특정한 헤더 정보를 추가하는 등의 행위를 할 경우에는 직접 구현하는 방식이 더 권장된다.

```java
package org.zerock.security;

@Log4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
  
  @Override
  public void handle(HttpServletRequest request,
                    HttpSEervletResponse response,
                    AccessDeniedException accessException) {
    log.error("Access Denied Handler");
    log.error("redirect");
    response.sendRedirect("/accessError");
  }
}
```

접근 제한이 걸리는 경우 리다이렉트하는 방식으로 동작되도록 지정되었다. security-context.xml에서는 error-page 속성 대신  CustomAccessDeniedHandler를 빈으로 등록해서 사용한다.

```xml
<bean id="customAccessDenied" class="org.zerock.security.CustomAccessDeniedHandler"></bean>
```

### 커스텀 로그인 페이지

대부분의 경우 기본 제공 로그인 페이지가 아닌 별도의 URI를 이용해서 로그인 페이지를 제작해 사용한다. 

```xml
<security:http>
  <security:intercept-url pattern="/sample/all" access="permitall"/>
  <security:intercept-url pattern="/sample/member" access="hasRole('ROLE_MEMBER')"/>
  <!--<security:form-login/>-->
  <security:form-login login-page="/customLogin"
</security:http>
```

login-page 속성의 URI는 반드시 GET방식으로 접근하는 URI를 지정한다.

```java
@Controller
@Log4j
public class CommonController {
  
  @GetMapping("/customLogin")
  public void loginInput(String error, String logout, Model model) {
    if (error != null) {
      model.addAttribute("error", "Login Error Check Your Account");
    }
    if (logout != null) {
      model.addAttribute("logout", "Logout!!!");
    }
  }
}
```

loginInput()은 GET 방식으로 접근하고, 에러 메시지와 로그아웃 메시지를 파라미터로 사용할 수 있다. 

### CSRF(Cross-site request forgery) 공격과 토큰

- 벼롣의 설정이 없다면 스프링 시큐리티가 적용된 사이트의 모든 POST 방식에는 CSRF 토큰이 사용된다.
- 사이트간 위조 방지를 목적으로 특정한 값의 토큰을 사용하는 방식이다.
- CSRF 공격은 사이트간 요청 위조이다. 서버에서 받아들이는 요청을 해석하고 처리할 때 어떤 출처에서 호출이 진행되었는지 따지지 않기 때문에 생기는 허점을 노리는 공격 방식이다. 
- 사이트간 요청 위조라고 하지만 현실적으로 한 사이트 내에서도 가능하다. 
- CSRF 는 `<img>` 태그 등의 URI 등을 이용할 수 있기 때문에 손쉽게 공격할 수 있다.
- CSRF 공격을 막기 위해서는 어떻게 해야 할까?
  - 사용자의 요청에 대한 출처를 의미하는 referer 헤더를 체크한다.
  - REST 방식에서 사용되는 PUT, DELETE 와 같은 방식을 사용한다.

#### CSRF 토큰

- 사용자가 임의로 변하는 특정하는 토큰값을 서버에서 체크하는 방식
- 서버에는 브라우저에 데이터를 전송할 때 CSRF 토큰을 같이 전달한다. 
- 사용자가 POST 방식 등으로 특정한 작업을 할 때는 브라우저에서 전송된 CSRF 토큰의 값과 서버가 보관하고 있는 토큰의 값을 비교한다.  만일 CSRF 토큰의 값이 다르다면 작업을 처리하지 않는다.
- 서버에서 생성하는 토큰은 난수를 생성해서 공격자가 패턴을 찾을 수 없도록 한다. 
- 공격자의 입장에서는 CSRF공격을 하려면 변경되는 CSRF토큰의 값을 알아야만 하기 때문에 고정된 내용의 `<form>` 태그나 `<img>` 태그 등의 값을 이용할 수 없게 된다.

#### 스프링 시큐리티의 CSRF 설정

- CSRF 토큰은 세션을 통해 보관하고, 브라우저에서 전송된 CSRF 토큰값을 검사하는 방식으로 처리한다.
- 스프링 시큐리티에서는 CSRF 토큰 생성을 비활성화하거나 CSRF토큰을 쿠키를 이용해서 처리하는 등의 설정을 지원한다.

```xml
<security:csrf disabled="true"/>
```

### 로그인 성공과 AuthenticationSuccessHandler

로그인 성공 시 특정 URI 로 이동하게 하거나 별도의 쿠키 등을 생성해서 처리하고 싶을 때와 같은 경우에 AuthenticationSuccessHandler를 구현해서 설정할 수 있다.

```java
package org.zerock.security;

@Log4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
  @Override
  public void onAuthenticationSucess(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Authentication auth) {
    log.warn("Login Success");
    List<String> roleNames = new ArrayList<>();
    
    auth.getAuthorities().forEach(authority -> {
      roleNames.add(authority.getAuthority());
    });
    
    log.warn("ROLE NAMES: " + roleNames);
    
    if (roleNames.contains("ROLE_ADMIN")) {
      response.sendRedirect("/sample/admin");
      return;
    }
    
    if (roleName.contains("ROLE_MEMBER")) {
      response.sendRedirect("/sample/member");
      return;
    }
    
    response.sendRedirect("/");
  }
}
```

CustomLoginSuccessHandler는 로그인 한 사용자에 부여된 권한 Authentication 객체를 이용해서 사용자가 가진 모든 권한을 문자열로 체크한다. 

security-context.xml에는 CustomLoginSuccessHandler를 빈으로 등록하고 로그인 성공 후 처리를 담당하는 핸들러로 지정한다.

```xml
<bean id="customAccessDenied" class="org.zerock.security.CustomAccessDeniedHandler"></bean>
<bean id="customLoginSuccess" class="org.zerock.security.CustomLoginSuccessHandler"></bean><security:http>
  <security:intercept-url pattern="/sample/all" access="permitall"/>
  <security:intercept-url pattern="/sample/member" access="hasRole('ROLE_MEMBER')"/>
  <security:intercept-url pattern="/sample/member" access="hasRole('ROLE_ADMIN')"/>
  <security:access-denied-handler error-page="/accessError"/>
  <security:form-login login-page="/customLogin" authentication-success-handler-ref="customLoginSuccess"/>
  <!--<security:csrf disabled="true"/> -->
</security:http>
```

### 로그아웃 처리와 LogoutSuccessHandler

security-context.xml

```xml
<security:logout logout-url="/customLogout" invalidate-session="true"/>
```

customLogout.jsp

```jsp
<form action="/customLogout" method="post">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
  <button>로그아웃</button>
</form>
```

POST 방식으로 처리되기 때문에 CSRF 토큰값을 같이 지정한다.

CommonController

```java
@GetMapping("/customLogout")
public void logoutGET() {
  log.info("custom logout");
}

@PostMapping("/customLogout"){
  log.info("custom logout");
}
```

## JDBC를 이용하는 간편 인증/권한 처리



