# Part 2 기본적인 기능의 게시물 관리

## Chapter 04 전체 목록 구현

### 4.1 컨트롤러의 완성 및 JSP의 완성

- BoardController와 BoardService와의 연결 작업

```java
@RequestMapping(value = "/listAll", method = RequestMethod.GET)
public void listAll(Model model) throws Exception {
	logger.info("show all list .....");
	model.addAttribute("list", service.listAll());
}
```

- listAll.jsp

```jsp
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
```

```jsp
<table class="table table-bordered">
  <tr>
    <th style="width: 10px">BNO</th>
    <th>TITLE</th>
    <th>WRITER</th>
    <th>REGDATE</th>
    <th style="width: 40px">VIEWCNT</th>
  </tr>
  <!-- 214 page -->
  <c:forEach items="${list}" var="boardVO">
    <tr>
      <td>${boardVO.bno}</td>
      <td><a href=''>${boardVO.title}</a></td>
      <td>${boardVO.writer}</td>
      <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${boardVO.regdate}" /></td>
      <td><span class="badge bg-red">${boardVO.viewcnt }</span></td>
    </tr>
  </c:forEach>
</table>
```



#### 4.1.1 각 목록에 링크 처리하기

```jsp
<c:forEach items="${list}" var="boardVO">
    <tr>
      <td>${boardVO.bno}</td>
      <td><a href='/board/read?bno=${boardVO.bno}'>${boardVO.title}</a></td>
      <td>${boardVO.writer}</td>
      <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${boardVO.regdate}" /></td>
      <td><span class="badge bg-red">${boardVO.viewcnt }</span></td>
    </tr>
  </c:forEach>
```



### 4.2 목록에 추가로 구현해야 하는 사항들

- 페이징 처리
  - 일반 웹에서는 페이지 번호를 이용한 처리를 해주어야 하고, 모바일의 경우에는 무한 스크롤과 같은 기능을 구현해 주어야 한다.
- 검색 기능
  - 모든 페이지 처리는 검색 기능과 함께 이루어져야 한다.