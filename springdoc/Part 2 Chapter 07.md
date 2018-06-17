# Part 2 기본적인 기능의 게시물 관리

## Chapter 07 예외 처리

### 7.1 예외 처리에 대한 팁

- @ExceptionHandler 어노테이션을 이용한 처리
- @ControllerAdvice를 이용한 처리
  - 클래스에 @ControllerAdvice라는 어노테이션 처리
  - 각 메소드에 @ExceptionHandler를 이용해서 적절한 타입의 Exception을 처리
- @ResponseStatus를 이용한 Http 상태 코드 처리



- CommonExceptionAdvice.java

```java
@ControllerAdvice
public class CommonExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(CommonExceptionAdvice.class);

    @ExceptionHandler(Exception.class)
    public String common(Exception e) {

        logger.info(e.toString());

        return "error_common";
    }
}
```

#### 7.1.1 Exception을 화면으로 전달하기

- CommonExceptionAdvice.java

```java
@ControllerAdvice
public class CommonExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(CommonExceptionAdvice.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView errorModelAndView(Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error_common");
        modelAndView.addObject("exception", ex);

        return modelAndView;
    }
}
```

- error_common.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Common Error</title>
</head>
<body>
    <h4>${exception.getMessage()}</h4>
    <ul>
        <c:forEach items="${exception.getStackTrace()}" var="stack">
            <li>${stack.toString()}</li>
        </c:forEach>
    </ul>
</body>
</html>
```

- error_common.jsp

![error_common](C:\Users\SeokRae Kim\Desktop\error_common.png)