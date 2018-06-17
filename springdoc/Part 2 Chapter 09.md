# Part 2 기본적인 기능의 게시물 관리

## Chapter 09 페이징 처리 - 컨트롤러와 프레젠테이션 계층

- BoardController

```java
@RequestMapping(value = "/listCri", method = RequestMethod.GET)
public void listAll(Criteria cri, Model model) throws Exception{
  logger.info("show list Page with Criteria.....");
  model.addAttribute("list", service.listCriteria(cri));
}
```



### 9.1 1차 화면 테스트

- listCri.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="false"%>

<%@include file="../include/header.jsp"%>

	<!-- Main contents -->
	<section class="content">
		<div class="row">
			<!-- left column -->
			<div class="col-md-12">
				<!-- general form elements -->
	
				<div class="box">
					<div class="box-header with-border">
						<h3 class="box-title">LIST ALL PAGE</h3>
					</div>
					<div class="box-body">
					
	                    <table class="table table-bordered">
	                        <tr>
	                            <th style="width: 10px">BNO</th>
	                            <th>TITLE</th>
	                            <th>WRITER</th>
	                            <th>REGDATE</th>
	                            <th style="width: 40px">VIEWCNT</th>
	                        </tr>
	
	                    <c:forEach items="${list}" var="boardVO">
	
	                        <tr>
	                            <td>${boardVO.bno}</td>
	                            <td><a href='/board/read?bno=${boardVO.bno}'>${boardVO.title}</a></td>
	                            <td>${boardVO.writer}</td>
	                            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${boardVO.regdate}" /></td>
	                            <td><span class="badge bg-red">${boardVO.viewcnt}</span></td>
	                        </tr>
	
	                    </c:forEach>
	
	                    </table>
	
					</div>
					<!-- /.box-body -->
					<div class="box-footer">Footer</div>
					<!-- /.box-footer-->
				</div>
			</div>
			<!--/.col (left) -->
	
		</div>
		<!-- /.row -->
	</section>
	<!-- /.contents -->
</div>
<!-- /.contents-wrapper -->

<script>
    var result = '${msg}';
	if(result === 'SUCCESS'){
		alert("처리가 완료되었습니다.");
    }
    
    </script>

<%@include file="../include/footer.jsp"%>
```

- /board/listCri, /board/listCri?page=3
  - 파라미터가 없을 때는 Criteria의 초기화된 값인 page=1, perPageNum=10을 기본으로 출력

![listCri_listCri-Page=3](C:\Users\SeokRae Kim\Desktop\listCri_listCri-Page=3.png)

- /board/listCri?page=3&perPageNum=20
  - perPageNum의 값을 가변적으로도 줄 수 있다.

![listCriParameterTwo](C:\Users\SeokRae Kim\Desktop\listCriParameterTwo.png)



### 9.2 화면 하단의 페이징 처리

- 페이징 처리시 필요한 데이터
  - 시작 페이지 번호
    - 현재 페이지가 1에서 10의 사이에 있는 번호라면 시작 페이지는 1
      만일 시작 페이지의 번호가 1이 아니라면 '이전(prev)'으로 갈 수 있는 링크 제공
  - 끝 페이지 번호
    - 시작 페이지 번호부터 몇 개의 번호를 보여줘야 하는지를 결정해야 한다.
  - 전체 데이터의 개수
    - 끝 페이지 번호를 계산할 때 전체 데이터의 개수를 이용해서 최종적으로 끝 페이지의 번호가 결정
    - 만일 끝 페이지 번호보다 많은 양의 데이터가 존재 한다면 '뒤로(next)' 갈 수 있는 링크가 추가로 붙는다.
  - 이전 페이지 링크
    - 맨 앞의 페이지 번호가 1이 아니라면 화면상에 링크를 통해서 이전 페이지를 조회할 수 있어야 한다.
  - 이후 페이지 링크
    - 맨 뒤의 페이지 이후에 더 많은 데이터가 존재하는 경우 이동이 가능하도록 링크를 제공



#### 9.2.1 endPage 구하기

```java
endPage = (int) (Math.ceil(cri.getPage() / (double) displayPageNum) * displayPageNum);
```



#### 9.2.2 startPage 구하기

```java
startPage = (endPage - displayPageNum) + 1;
```



#### 9.2.3 totalCount와 endPage의 재계산

```java
int tempEndPage = (int) (Math.ceil(totalCount / (double) cri.getPerPageNum()));
	if (endPage > tempEndPage) {
  		endPage = tempEndPage;
	}
```



#### 9.2.4 prev와 next의 계산

```java
prev = (startPage == 1) ? false : true;
```

```java
next = endPage * cri.getPerPageNum() >= totalCount ? false : true;
```



### 9.3 페이징 처리용 클래스 설계하기

- 클래스의 작성을 위해서 필요한 데이터 점검
  - 외부에서 입력되는 데이터(Criteria)
    - page: 현재 조회하는 페이지의 번호
    - perPageNum: 한 페이지당 출력하는 데이터의 개수
  - DB에서 계산되는 데이터
    - totalCount: SQL의 결과로 나온 데이터의 전체 개수
  - 계산을 통해서 만들어지는 데이터
    - startPage
    - endPage
    - prev
    - next

```java
package org.zerock.domain;

public class PageMaker {

	private int totalCount;
	private int startPage;
	private int endPage;
	private boolean prev;
	private boolean next;

	private int displayPageNum = 10;

	private Criteria cri;

	public void setCri(Criteria cri) {
		this.cri = cri;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		calcData();
	}

	private void calcData() {
		endPage = (int) (Math.ceil(cri.getPage() / (double) displayPageNum) * displayPageNum);
		startPage = (endPage - displayPageNum) + 1;
		int tempEndPage = (int) (Math.ceil(totalCount / (double) cri.getPerPageNum()));
		if (endPage > tempEndPage) {
			endPage = tempEndPage;
		}
		prev = (startPage == 1) ? false : true;
		next = endPage * cri.getPerPageNum() >= totalCount ? false : true;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public int getDisplayPageNum() {
		return displayPageNum;
	}

	public void setDisplayPageNum(int displayPageNum) {
		this.displayPageNum = displayPageNum;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public Criteria getCri() {
		return cri;
	}

	@Override
	public String toString() {
		return "PageMaker [totalCount=" + totalCount + ", startPage=" + startPage + ", endPage=" + endPage + ", prev="
				+ prev + ", next=" + next + ", displayPageNum=" + displayPageNum + ", cri=" + cri + "]";
	}	
}
```



### 9.4 BoardController와 뷰 처리

- BoardController

```java 
@RequestMapping(value ="/listPage", method = RequestMethod.GET)
public void listPage(Criteria cri, Model model) throws Exception {
  logger.info(cri.toString());
  model.addAttribute("list", service.listCriteria(cri));
  PageMaker pageMaker = new PageMaker();
  pageMaker.setCri(cri);
  pageMaker.setTotalCount(131);

  model.addAttribute("pageMaker", pageMaker);
}
```



#### 9.4.1 listPage.jsp의 처리

- listPage.jsp

```jsp
<div class="box-footer">
  <div class="text-center">
    <ul class="pagination">
      <c:if test="${pageMaker.prev}">
        <li><a href="listPage?page=${pageMaker.startPage - 1}">&laquo;</a> </li>
      </c:if>
      <c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="idx">
        <li <c:out value="${pageMaker.cri.page == idx? 'class = active':''}"/>>
        <a href="listPage?page=${idx}">${idx}</a>
        </li>
      </c:forEach>

    <c:if test="${pageMaker.next && pageMaker.endPage > 0}">
      <li><a href="listPage?page=${pageMaker.endPage + 1}">&raquo;</a> </li>
    </c:if>
    </ul>
</div>
</div>
<!-- /.box-footer-->
```

![paging](C:\Users\SeokRae Kim\Desktop\paging.png)



### 9.5 페이징을 위한 SQL문의 처리

#### 9.5.1 BoardDAO의 수정

```java
public int countPaging(Criteria cri) throws Exception;
```



#### 9.5.2 XML Mapper의 수정

```xml
<select id="countPaging" resultType="int">
  <![CDATA[
 SELECT
  count(bno)
 FROM
  tbl_board
 WHERE
  bno > 0
 ]]>
</select>
```

#### 9.5.3  BoardDAOImpl의 수정

- BoardDAOImpl

```java
@Override
public int countPaging(Criteria cri) throws Exception {
  return session.selectOne(namespace + ".countPaging", cri);
}
```



#### 9.5.4 BoardService/BoardServiceImpl의 수정

- BoardService

```java
public int listCountCriteria(Criteria cri) throws Exception;
```

- BoardServiceImpl

```java
@Override
public int listCountCriteria(Criteria cri) throws Exception {
  return dao.countPaging(cri);
}
```



#### 9.5.5 BoardController의 수정

```java
@RequestMapping(value ="/listPage", method = RequestMethod.GET)
public void listPage(Criteria cri, Model model) throws Exception {
  logger.info(cri.toString());
  model.addAttribute("list", service.listCriteria(cri));
  PageMaker pageMaker = new PageMaker();
  pageMaker.setCri(cri);
  // pageMaker.setTotalCount(131);
  pageMaker.setTotalCount(service.listCountCriteria(cri));

  model.addAttribute("pageMaker", pageMaker);
}
```



### 9.6 페이징 처리의 개선을 위한 TIP

- page 파라미터만으로  처리 하기 때문에 화면에 10개 이상의 데이터를 제대로 전달 할 수 없다.
- 이와 같은 처리하기 위해서
  - 직접 JSP 내에서 수정하는 방법
  - PageMaker에서 필요한 링크를 생성
  - JavaScript를 이용해서 처리



#### 9.6.1 스프링 MVC의 UriComponentsBuilder를 이용하는 방식

- org.springframework.web.util 패키지의 UriComponentsBuilder, UriComponents클래스를 사용

```java
@Test
public void testURI() throws Exception {
  UriComponents uriComponents = UriComponentsBuilder.newInstance()
    .path("/board/read")
    .queryParam("bno", 12)
    .queryParam("perPageNum", 20)
    .build();
  logger.info("/board/read?bno=12&perPageNum=20");
  logger.info(uriComponents.toString());
}
```

![log](C:\Users\SeokRae Kim\Desktop\log.png)



- 특정 URI를 먼저 지정하고 작업하는 방식

```java
public void testURI2() throws Exception {
  UriComponents uriComponents = 
    UriComponentsBuilder.newInstance()
    .path("/{module}/{page}")
    .queryParam("bno", 12)
    .queryParam("perPageNum", 20)
    .build()
    .expand("board", "read")
    .encode();
  logger.info("/board/read?bno=12&perPageNum=20");
  logger.info(uriComponents.toString());
}
```

![log2](C:\Users\SeokRae Kim\Desktop\log2.png)



- PageMaker 메서드 추가

```java
public String makeQuery(int page) {
  UriComponents uriComponents =
    UriComponentsBuilder.newInstance()
    .queryParam("page", page)
    .queryParam("perPageNum", cri.getPerPageNum())
    .build();
  return uriComponents.toUriString();
}
```



- listPage.jsp 수정

```jsp
<c:forEach items="${list}" var="boardVO">

  <tr>
    <td>${boardVO.bno}</td>
    <td><a href='/board/readPage${pageMaker.makeQuery(pageMaker.cri.page)}&bno=${boardVO.bno}'>${boardVO.title}</a></td>
    <td>${boardVO.writer}</td>
    <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${boardVO.regdate}" /></td>
    <td><span class="badge bg-red">${boardVO.viewcnt}</span></td>
  </tr>

</c:forEach>
```



- listPage 수정 (페이지 정보 유지)

```jsp
<div class="box-footer">
  <div class="text-center">
    <ul class="pagination">
      <c:if test="${pageMaker.prev}">
        <li><a href="listPage${pageMaker.makeQuery(pageMaker.startPage - 1)}">&laquo;</a> </li>
      </c:if>
      <c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="idx">
        <li <c:out value="${pageMaker.cri.page == idx? 'class = active':''}"/>>
        <a href="listPage${pageMaker.makeQuery(idx)}">${idx}</a>
        </li>
      </c:forEach>

    <c:if test="${pageMaker.next && pageMaker.endPage > 0}">
      <li><a href="listPage${pageMaker.makeQuery(pageMaker.endPage + 1)}">&raquo;</a> </li>
    </c:if>
    </ul>
</div>
</div>
<!-- /.box-footer-->
```



#### 9.6.2 JavaScript를 이용하는 링크 처리

- 위 방식과 겹치므로 한가지만 사용
- 페이지 번호가 클릭되면 event.preventDefault()를 이용해서 실제 화면의 이동을 막고, <a> 태그에 있는 페이지 번호를 찾아서 <form> 태그를 전송하는 방식

```jsp
<form id="jobForm">
    <input type='hidden' name="page" value=${pageMaker.cri.page}>
    <input type='hidden' name="perPageNum" value=${pageMaker.cri.perPageNum}>
</form>
```

```javascript
$(".pagination li a").on("click", function(event){
  event.preventDefault();
  var targetPage = $(this).attr("href");
  var jobForm = $("#jobForm");
  jobForm.find("[name='page']").val(targetPage);
  jobForm.attr("action","/board/listPage").attr("method", "get");
  jobForm.submit();
});
```



### 9.7 목록 페이지와 정보 유지하기

- BoardController의 조회 처리
  - 페이징 처리 후, 조회 페이지는 다시 목록 페이지로 돌아가기 위해서 다음과 같은 정보를 필요로 한다.
    - 현재 목록 페이지의 페이지 번호(page)
    - 현재 목록 페이지의 페이지당 데이터 수(perPageNum)
    - 현재 조회하는 게시물의 번호(bno)

```java
@RequestMapping(value = "/read", method = RequestMethod.GET)
public void read(@RequestParam("bno") int bno, Model model) throws Exception {
  model.addAttribute(service.read(bno));
}
```



#### 9.7.1 BoardController 수정

- BoardController

```java
@RequestMapping(value = "/readPage", method = RequestMethod.GET)
public void read(@RequestParam("bno") int bno,@ModelAttribute("cri") Criteria cri, Model model) throws Exception {
  model.addAttribute(service.read(bno));
}
```



#### 9.7.2 readPage.jsp의 작성

- readPage.jsp 수정

```jsp
<div class="box-footer">
  <button type="submit" class="btn btn-warning modifyBtn">Modify</button>
  <button type="submit" class="btn btn-danger removeBtn">Remove</button>
  <button type="submit" class="btn btn-primary goListBtn">LIST ALL</button>
</div>
<form role="form" action="modifyPage" method="post">
  <input type="hidden" name="bno" 		value="${boardVO.bno}" />
  <input type="hidden" name="page" 		value="${cri.page}" />
  <input type="hidden" name="perPageNum" 	value="${cri.perPageNum}" />
</form>
```

- script

```javascript
$(".goListBtn").on("click", function () {
   formObj.attr("method", "get");
   formObj.attr("action", "/board/listPage");
   formObj.submit();
});
```



#### 9.7.3 수정 페이지와 삭제 페이지의 처리

##### 9.7.3.1 삭제 처리 

- 조회 페이지 -> 삭제 -> BoardController -> 목록 페이지
- BoardController

```java
@RequestMapping(value = "/removePage", method = RequestMethod.POST)
public String remove(@RequestParam("bno") int bno, Criteria cri, RedirectAttributes rttr) throws Exception {
  service.remove(bno);

  rttr.addAttribute("page", cri.getPage());
  rttr.addAttribute("perPageNum", cri.getPerPageNum());
  rttr.addFlashAttribute("msg", "SUCCESS");

  return "redirect:/board/listPage";
}
```



- readPage.jsp

```jsp
$(".removeBtn").on("click", function () {
formObj.attr("action", "/board/removePage");
formOjb.submit();
});
```



##### 9.7.3.2 수정 처리

- 조회 페이지 -> BoardController -> 수정 페이지 -> BoardController -> 목록 페이지
- readPage.jsp

```jsp
$(".modifyBtn").on("click", function () {
  formObj.attr("action", "/board/modifyPage");
  formObj.attr("method", "get");
  formObj.submit();
});
```



- BoardController 

```java
@RequestMapping(value ="/modifyPage", method = RequestMethod.GET)
public void modifyPagingGET(@RequestParam("bno") int bno, @ModelAttribute("cri") Criteria cri, Model model) throws Exception {
  model.addAttribute(service.read(bno));
}
```



- modifyPage.jsp

```jsp
<form role="form" action="modifyPage" method="post">
  <input type ="hidden" name ="page" value = "${cri.page}">
  <input type = "hidden" name ="perPageNum" value ="${cri.perPageNum }">
  <div class="box-body">
    <div class="form-group">
      <label for="exampleInputEmail1">BNO</label>
      <input type="text" name='bno' class="form-control" value="${boardVO.bno}" readonly="readonly">
    </div>
    <div class="form-group">
      <label for="exampleInputEmail1">Title</label>
      <input type="text" name="title" class="form-control" value="${boardVO.title}" />
    </div>
    <div class="form-group">
      <label for="exampleInputPassword1">Contents</label>
      <textarea name="contents" class="form-control" rows="3">${boardVO.content}</textarea>
    </div>
    <div class="form-group">
      <label for="exampleInputEmail1">Writer</label>
      <input type="text" name="writer" class="form-control" value="${boardVO.writer}" readonly="readonly" />
    </div>
  </div>
</form>
```



- modifyPage - script

```javascript
<script>
    $(document).ready(function () {
        var formObj = $("form[role='form']");
        console.log(formObj);
        $(".btn-warning").on("click", function () {
           self.location = "/board/listPage?page=${cri.page}&perPageNum=${cri.perPageNum}";
        });
        $(".btn-primary").on("click", function () {
            formObj.submit();
        });
    });
</script>
```



- BoardController

```java
@RequestMapping(value = "/modifyPage", method = RequestMethod.POST)
public String modifyPagingGET(BoardVO board, Criteria cri, RedirectAttributes rttr) throws Exception {
  service.modify(board);

  rttr.addAttribute("page", cri.getPage());
  rttr.addAttribute("perPageNum", cri.getPerPageNum());
  rttr.addFlashAttribute("msg", "SUCCESS");

  return "redirect:/board/listPage";
}
```

- 목록 페이지 -> 조회 페이지 -> 수정 페이지 -> 목록 페이지 확인하기

![flow](C:\Users\SeokRae Kim\Desktop\flow.png)