# Part 2 기본적인 기능의 게시물 관리

## Chapter 10 검색 처리와 동적 SQL

- 검색 처리는 페이징 처리 기능에 추가로 조회나 페이지 이동 시 유지해야 하는 데이터가 포함된다.
- 검색 처리에 가장 어려운 부분은 SQL문의 처리 영역이고 이 방법을 처리하기 위한 두 가지 방법
  - 이를 처리하기 위해서 MyBatis의 동적 SQL(Dynamic SQL)을 이용
  - @SelectProvider를 이용하는 방법 (어노테이션으로 처리하는 경우에만 사용)



- 개발 순서
  - 화면과 관련된 컨트롤러, JSP 처리 -> MyBatis의 처리



### 10.1 검색에 필요한 데이터와 SearchCriteria

- 필수 사전정보
  - 현재 페이지 번호(page)
  - 페이지당 보여지는 데이터의 수(perPageNum)
  - 검색의 종류(searchType)
  - 검색의 키워드(keyword)


- SearchCriteria

```java
public class SearchCriteria extends Criteria {

	private String searchType;
	private String keyword;

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return "SearchCriteria [searchType=" + searchType + ", keyword=" + keyword + "]";
	}
}
```

### 10.2 SearchBoardController의 작성

- SearchBoardController

```java
@Controller
@RequestMapping("/sboard/*")
public class SearchBoardController {

	private static final Logger logger = LoggerFactory.getLogger(SearchBoardController.class);
	
	@Inject
	private BoardService service;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void listPage(@ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception {
		logger.info(cri.toString());
		
		model.addAttribute("list", service.listCriteria(cri));
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		
		pageMaker.setTotalCount(service.listCountCriteria(cri));
		model.addAttribute("pageMaker", pageMaker);
	}
}
```



#### 10.2.1 JSP 페이지의 준비

- listPage.jsp 복사해서 list로 사용

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	                            <td><a href='/board/readPage${pageMaker.makeQuery(pageMaker.cri.page)}&bno=${boardVO.bno}'>${boardVO.title}</a></td>
	                            <td>${boardVO.writer}</td>
	                            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${boardVO.regdate}" /></td>
	                            <td><span class="badge bg-red">${boardVO.viewcnt}</span></td>
	                        </tr>
	
	                    </c:forEach>
	
	                    </table>
	
					</div>
					<!-- /.box-body -->
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
				</div>
			</div>
			<!--/.col (left) -->
		</div>
		<!-- /.row -->
	</section>
	<!-- /.contents -->
</div>
<!-- /.contents-wrapper -->

<form id="jobForm">
    <input type='hidden' name="page" value=${pageMaker.cri.page}>
    <input type='hidden' name="perPageNum" value=${pageMaker.cri.perPageNum}>
</form>

<script>
    var result = '${msg}';
    var pagePaker = '${pageMaker}'
	if(result === 'SUCCESS'){
		alert("처리가 완료되었습니다.");
    }
    console.log(pagePaker);
    /*$(".pagination li a").on("click", function(event){
        event.preventDefault();
        var targetPage = $(this).attr("href");
        var jobForm = $("#jobForm");
        jobForm.find("[name='page']").val(targetPage);
        jobForm.attr("action","/board/listPage").attr("method", "get");
        jobForm.submit();
    });*/
</script>

<%@include file="../include/footer.jsp"%>
```





### 10.3 검색에 필요한 JSP 수정

- list.jsp

```jsp
<div class = "box">
  <div class="box-header with-border">
    <h3 class="box-title">Board List</h3>
  </div>
  <div class="box-body">
    <select name="searchType">
      <option value = "n"
              <c:out value="${cri.searchType == null ? 'selected' : ''}"/>>---				
      </option>
      <option value = "t"
              <c:out value="${cri.searchType eq 't' ? 'selected' : ''}"/>>Title				
      </option>
      <option value = "c"
              <c:out value="${cri.searchType eq 'c' ? 'selected' : ''}"/>>Content			
      </option>
      <option value = "w"
              <c:out value="${cri.searchType eq 'w' ? 'selected' : ''}"/>>Writer			
      </option>
      <option value = "tc"
              <c:out value="${cri.searchType eq 'tc' ? 'selected' : ''}"/>>Title OR Content	
      </option>
      <option value = "cw"
              <c:out value="${cri.searchType eq 'cw' ? 'selected' : ''}"/>>Content OR Writer
      </option>
      <option value = "tcw"
              <c:out value="${cri.searchType eq 'tcw' ? 'selected' : ''}"/>>Title OR Content OR Writer
      </option>
  	</select>
  <input type="text" name="keyword" id="keywordInput" value="${cri.keyword }"/>
  <button id="searchBtn">Search</button>
  <button id="newBtn">New Board</button>
  </div>	
</div>
```

- 검색 리스트, 키워드 항목 확인

![searchTypeList](C:\Users\SeokRae Kim\Desktop\searchTypeList.png)



#### 10.3.1 searchType과 keyword 링크 처리

- PageMaker

```java
public String makeSearch(int page) {
  UriComponents uriComponents = UriComponentsBuilder.newInstance()
    .queryParam("page", page)
    .queryParam("perPageNum", cri.getPerPageNum())
    .queryParam("searchType", ((SearchCriteria) cri).getSearchType())
    .queryParam("keyword", encoding(((SearchCriteria) cri).getKeyword())).build();
  return uriComponents.toUriString();
}

private String encoding(String keyword) {
  if(keyword == null || keyword.trim().length() == 0) {
    return "";
  }
  try {
    return URLEncoder.encode(keyword, "UTF-8");
  }catch(UnsupportedEncodingException e) {
    return "";
  }
}
```



- list.jsp

```jsp
<ul class="pagination">
  <c:if test="${pageMaker.prev}">
    <li><a href="listPage${pageMaker.makeSearch(pageMaker.startPage - 1)}">&laquo;</a> </li>
  </c:if>

  <c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="idx">
    <li <c:out value="${pageMaker.cri.page == idx? 'class = active':''}"/>>
    <a href="listPage${pageMaker.makeSearch(idx)}">${idx}</a>
    </li>
  </c:forEach>

  <c:if test="${pageMaker.next && pageMaker.endPage > 0}">
    <li><a href="listPage${pageMaker.makeSearch(pageMaker.endPage + 1)}">&raquo;</a> </li>
  </c:if>
</ul>
```



##### 10.3.1.1 브라우저를 통한 검색과 페이징 확인 

- URL에 searchType과 keyword를 작성하여 결과 페이지 키워드 영역에 keyword에 입력한 값이 출력되었는지 확인
- list.jsp

```text
http://localhost:8090/sboard/list?page=3&perPageNum=10&searchType=t&keyword=새
```

![SearchTest](C:\Users\SeokRae Kim\Desktop\SearchTest.png)



- 조회 페이지로의 이동 경로 처리 list
- readPage 작성후 확인 가능

```jsp
<c:forEach items="${list}" var="boardVO">
  <tr>
    <td>${boardVO.bno}</td>
    <td><a href='/board/readPage${pageMaker.makeSearch(pageMaker.cri.page)}&bno=${boardVO.bno}'>${boardVO.title}</a></td>
    <td>${boardVO.writer}</td>
    <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${boardVO.regdate}" /></td>
    <td><span class="badge bg-red">${boardVO.viewcnt}</span></td>
  </tr>
</c:forEach>
```



#### 10.3.2 검색 버튼의 동작 처리

- list.jsp

``` jsp
<script>
  $(document).ready(
    function() {
      $('#searchBtn').on("click"
                         , function (event) {
        self.location = "list"
          + '${pageMaker.makeQuery(1)}'
          + "&searchType="
          + $("select option:selected").val()
          + "&keyword=" + encodeURIComponent($('#keywordInput').val());
        alert("되니");
      });
      $('#newBtn').on("click", function(evt) {
        self.location = "register";
      });
    });
</script>
```



- 검색 조건과 키워드가 페이지 이동 시 같이 적용되는지 확인

![SearchTest2](C:\Users\SeokRae Kim\Desktop\SearchTest2.png)



### 10.4 MyBatis 동적 SQL

- 동적 SQL 작성 단계
  - 동적 SQL의 적용이 필요한 메서드의 설정
  - XML Mapper를 이용한 SQL 문 처리
  - 동적 SQL 문의 생성 확인 및 테스트



#### 10.4.1 BoardDAO의 수정

- BoardDAO
- SearchCriteria를 사용하기 위한 메서드 추가
  - page(Criteria에서 상속된 속성)
  - perPageNum(Criteria에서 상속된 속성)
  - searchType(검색 조건)
  - keyword(검색 키워드)

```java
public List<BoardVO> listSearch(SearchCriteria cri) throws Exception;
	
public int listSearchCount(SearchCriteria cri) throws Exception;
```



#### 10.4.2 XML Mapper 수정

- boardMapper.xml

```xml
<select id="listSearch" resultType="BoardVO">
<![CDATA[
 SELECT
  *
 FROM
  tbl_board
 WHERE
  bno > 0
 ORDER BY
  bno DESC
 LIMIT #{pageStart}, #{perPageNum}
]]>
</select>
<select id="listSearchCount" resultType="int">
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



#### 10.4.3 BoardDAOImpl의 수정

- BoardDAOImpl

```java
@Override
public List<BoardVO> listCriteria(Criteria cri) throws Exception {
  return dao.listCriteria(cri);
}

@Override
public int listCountCriteria(Criteria cri) throws Exception {
  return dao.countPaging(cri);
}
```



#### 10.4.4 BoardDAO의 테스트

- BoardDAOTest

```java
@Test
public void testDynamic1() throws Exception {
  SearchCriteria cri = new SearchCriteria();
  cri.setPage(1);
  cri.setKeyword("글");
  cri.setSearchType("t");
  logger.info("================================");

  List<BoardVO> list = dao.listSearch(cri);
  for(BoardVO boardVO : list) {
    logger.info(boardVO.getBno() + ":" + boardVO.getTitle());
  }
  logger.info("================================");
  logger.info("COUNT : " + dao.listSearchCount(cri));
}
```



- Test 결과

```restructuredtext
INFO : org.zerock.test.BoardDAOTest - ================================
INFO : jdbc.connection - 1. Connection opened
INFO : jdbc.audit - 1. Connection.new Connection returned 
INFO : jdbc.audit - 1. Connection.getAutoCommit() returned true
INFO : jdbc.audit - 1. PreparedStatement.new PreparedStatement returned 
INFO : jdbc.audit - 1. Connection.prepareStatement(SELECT
		*
	FROM
		tbl_board
	WHERE
		bno > 0
	ORDER BY
		bno DESC
	LIMIT ?, ?) returned net.sf.log4jdbc.sql.jdbcapi.PreparedStatementSpy@242b836
INFO : jdbc.audit - 1. PreparedStatement.setInt(1, 0) returned 
INFO : jdbc.audit - 1. PreparedStatement.setInt(2, 10) returned 
INFO : jdbc.sqlonly - SELECT * FROM tbl_board WHERE bno > 0 ORDER BY bno DESC LIMIT 0, 10 

INFO : jdbc.sqltiming - SELECT * FROM tbl_board WHERE bno > 0 ORDER BY bno DESC LIMIT 0, 10 
 {executed in 3 msec}
INFO : jdbc.audit - 1. PreparedStatement.execute() returned true
INFO : jdbc.resultset - 1. ResultSet.new ResultSet returned 
INFO : jdbc.audit - 1. PreparedStatement.getResultSet() returned net.sf.log4jdbc.sql.jdbcapi.ResultSetSpy@5e600dd5
INFO : jdbc.resultset - 1. ResultSet.getMetaData() returned com.mysql.jdbc.ResultSetMetaData@2e3967ea - Field level information: 
	com.mysql.jdbc.Field@70e9c95d[catalog=sdba,tableName=tbl_board,originalTableName=tbl_board,columnName=bno,originalColumnName=bno,mysqlType=3(FIELD_TYPE_LONG),flags= AUTO_INCREMENT PRIMARY_KEY, charsetIndex=63, charsetName=US-ASCII]
	com.mysql.jdbc.Field@4d5650ae[catalog=sdba,tableName=tbl_board,originalTableName=tbl_board,columnName=title,originalColumnName=title,mysqlType=253(FIELD_TYPE_VAR_STRING),flags=, charsetIndex=33, charsetName=UTF-8]
	com.mysql.jdbc.Field@a38c7fe[catalog=sdba,tableName=tbl_board,originalTableName=tbl_board,columnName=content,originalColumnName=content,mysqlType=253(FIELD_TYPE_VAR_STRING),flags= BLOB, charsetIndex=33, charsetName=UTF-8]
	com.mysql.jdbc.Field@6fdbe764[catalog=sdba,tableName=tbl_board,originalTableName=tbl_board,columnName=writer,originalColumnName=writer,mysqlType=253(FIELD_TYPE_VAR_STRING),flags=, charsetIndex=33, charsetName=UTF-8]
	com.mysql.jdbc.Field@51c668e3[catalog=sdba,tableName=tbl_board,originalTableName=tbl_board,columnName=regdate,originalColumnName=regdate,mysqlType=7(FIELD_TYPE_TIMESTAMP),flags= BINARY, charsetIndex=63, charsetName=US-ASCII]
	com.mysql.jdbc.Field@2e6a8155[catalog=sdba,tableName=tbl_board,originalTableName=tbl_board,columnName=viewcnt,originalColumnName=viewcnt,mysqlType=3(FIELD_TYPE_LONG),flags=, charsetIndex=63, charsetName=US-ASCII]
INFO : jdbc.resultset - 1. ResultSet.getType() returned 1003
INFO : jdbc.resultset - 1. ResultSet.next() returned true
INFO : jdbc.resultset - 1. ResultSet.getInt(bno) returned 720873
INFO : jdbc.resultset - 1. ResultSet.wasNull() returned false
... 중략
INFO : jdbc.resultset - 1. ResultSet.wasNull() returned false
INFO : jdbc.resultsettable - 
|-------|-------|---------|-------|----------------------|--------|
|bno    |title  |content  |writer |regdate               |viewcnt |
|-------|-------|---------|-------|----------------------|--------|
|720873 |새글을 작성 |[unread] |user00 |2018-02-02 18:02:24.0 |0       |
|720872 |새글을 작성 |[unread] |user00 |2018-02-02 18:02:24.0 |0       |
|720871 |[null] |[unread] |user00 |2018-02-02 18:02:24.0 |0       |
|720870 |새글 타이틀 |[unread] |user00 |2018-02-02 18:02:24.0 |0       |
|720869 |새글을 작성 |[unread] |user00 |2018-02-02 18:02:24.0 |0       |
|720868 |새글을 작성 |[unread] |user00 |2018-02-02 18:02:24.0 |0       |
|720867 |[null] |[unread] |user00 |2018-02-02 18:02:24.0 |0       |
|720866 |새글 타이틀 |[unread] |user00 |2018-02-02 18:02:24.0 |0       |
|720865 |새글을 작성 |[unread] |user00 |2018-02-02 18:02:24.0 |0       |
|720864 |새글을 작성 |[unread] |user00 |2018-02-02 18:02:24.0 |0       |
|-------|-------|---------|-------|----------------------|--------|

INFO : jdbc.resultset - 1. ResultSet.next() returned false
INFO : jdbc.resultset - 1. ResultSet.close() returned void
INFO : jdbc.audit - 1. PreparedStatement.getConnection() returned net.sf.log4jdbc.sql.jdbcapi.ConnectionSpy@1e6a3214
INFO : jdbc.audit - 1. Connection.getMetaData() returned com.mysql.jdbc.JDBC4DatabaseMetaData@368247b9
INFO : jdbc.audit - 1. PreparedStatement.getMoreResults() returned false
INFO : jdbc.audit - 1. PreparedStatement.getUpdateCount() returned -1
INFO : jdbc.audit - 1. PreparedStatement.close() returned 
INFO : jdbc.connection - 1. Connection closed
INFO : jdbc.audit - 1. Connection.close() returned 
INFO : org.zerock.test.BoardDAOTest - 720873:새글을 작성
INFO : org.zerock.test.BoardDAOTest - 720872:새글을 작성
INFO : org.zerock.test.BoardDAOTest - 720871:새 타이틀
INFO : org.zerock.test.BoardDAOTest - 720870:새글 타이틀
INFO : org.zerock.test.BoardDAOTest - 720869:새글을 작성
INFO : org.zerock.test.BoardDAOTest - 720868:새글을 작성
INFO : org.zerock.test.BoardDAOTest - 720867:새 타이틀
INFO : org.zerock.test.BoardDAOTest - 720866:새글 타이틀
INFO : org.zerock.test.BoardDAOTest - 720865:새글을 작성
INFO : org.zerock.test.BoardDAOTest - 720864:새글을 작성
INFO : org.zerock.test.BoardDAOTest - ================================
```



#### 10.4.5 동적 SQL 문의 추가

- boardMapper.xml  (listSearch수정)

```xml
<select id="listSearch" resultType="BoardVO">
  <![CDATA[
 SELECT
  *
 FROM
  tbl_board
 WHERE
  bno > 0
 ]]>
  <if test="searchType != null" > 
    <if test="searchType == 't'.toString()">
      and title like CONCAT('%', #{keyword}, '%')
    </if>
    <if test="searchType == 'c'.toString()">
      and content like CONCAT('%', #{keyword}, '%')
    </if>
    <if test="searchType == 'w'.toString()">
      and writer like CONCAT('%', #{keyword}, '%')
    </if>     
    <if test="searchType == 'tc'.toString()">
      and ( title like CONCAT('%', #{keyword}, '%') OR content like CONCAT('%', #{keyword}, '%'))
    </if>        
    <if test="searchType == 'cw'.toString()">
      and ( content like CONCAT('%', #{keyword}, '%') OR writer like CONCAT('%', #{keyword}, '%'))
    </if>        
    <if test="searchType == 'tcw'.toString()">
      and (   title like CONCAT('%', #{keyword}, '%') 
      OR 
      content like CONCAT('%', #{keyword}, '%') 
      OR 
      writer like CONCAT('%', #{keyword}, '%'))
    </if>              
  </if>  
  <![CDATA[
 ORDER BY
  bno DESC
 LIMIT #{pageStart}, #{perPageNum}
 ]]>
</select>
```

- 결과

```java
INFO : jdbc.sqlonly - SELECT * FROM tbl_board WHERE bno > 0 and title like CONCAT('%', '글', '%') ORDER BY bno DESC 
LIMIT 0, 10 
```



##### 10.4.5.1 <include>와 <sql>

- <sql> 태그를 이용해서 동일한 SQL 구문을 재사용하는 방법


- boardMapper.xml 

```xml
<sql id="search">
  <if test="searchType != null" > 
    <if test="searchType == 't'.toString()">
      and title like CONCAT('%', #{keyword}, '%')
    </if>
    <if test="searchType == 'c'.toString()">
      and content like CONCAT('%', #{keyword}, '%')
    </if>
    <if test="searchType == 'w'.toString()">
      and writer like CONCAT('%', #{keyword}, '%')
    </if>     
    <if test="searchType == 'tc'.toString()">
      and ( title like CONCAT('%', #{keyword}, '%') OR content like CONCAT('%', #{keyword}, '%'))
    </if>        
    <if test="searchType == 'cw'.toString()">
      and ( content like CONCAT('%', #{keyword}, '%') OR writer like CONCAT('%', #{keyword}, '%'))
    </if>        
    <if test="searchType == 'tcw'.toString()">
      and (   title like CONCAT('%', #{keyword}, '%') 
      OR 
      content like CONCAT('%', #{keyword}, '%') 
      OR 
      writer like CONCAT('%', #{keyword}, '%'))
    </if>              
  </if>  
</sql>
```

- boardMapper.xml

```xml
<select id="listSearch" resultType="BoardVO">
  <![CDATA[
 SELECT
  *
 FROM
  tbl_board
 WHERE
  bno > 0
 ]]>
  <include refid="search"></include>
  <![CDATA[
 ORDER BY
  bno DESC
 LIMIT #{pageStart}, #{perPageNum}
 ]]>
</select>
<select id="listSearchCount" resultType="int">
  <![CDATA[
 SELECT
  count(bno)
 FROM
  tbl_board
 WHERE
  bno > 0
 ]]>
  <include refid="search"></include>
</select>
```



### 10.5 BoardService와 SearchBoardController의 수정

- BoardService

```java
public List<BoardVO> listSearchCriteria(SearchCriteria cri) throws Exception;

public int listSearchCount(SearchCriteria cri) throws Exception;
```

- BoardServiceImpl

```java
@Override
public List<BoardVO> listSearchCriteria(SearchCriteria cri) throws Exception {
  return dao.listSearch(cri);
}

@Override
public int listSearchCount(SearchCriteria cri) throws Exception {
  return dao.listSearchCount(cri);
}
```

- SearchBoardController

```java
@RequestMapping(value = "/list", method = RequestMethod.GET)
public void listPage(@ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception {
  logger.info(cri.toString());

  // model.addAttribute("list", service.listCriteria(cri));
  model.addAttribute("list", service.listSearchCriteria(cri));
  PageMaker pageMaker = new PageMaker();
  pageMaker.setCri(cri);

  // pageMaker.setTotalCount(service.listCountCriteria(cri));
  pageMaker.setTotalCount(service.listSearchCount(cri));
  model.addAttribute("pageMaker", pageMaker);
}
```



### 10.6 조회, 수정, 삭제 페이지의 처리

#### 10.6.1 게시물의 조회 처리

- SearchBoardController

```java
@RequestMapping(value = "/readPage", method = RequestMethod.GET)
public void read(@RequestParam("bno") int bno, @ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception {
  model.addAttribute(service.read(bno));
}
```



- readPage.jsp

```jsp
<form role="form" action="modifyPage" method="post">
  <input type="hidden" name="bno" 		value="${boardVO.bno}" />
  <input type="hidden" name="page" 		value="${cri.page}" />
  <input type="hidden" name="perPageNum" 	value="${cri.perPageNum}" />
  <input type="hidden" name="searchType" 	value="${cri.searchType}" />
  <input type="hidden" name="keyword" 	value="${cri.keyword}" />
</form>
```



- readPage.jsp

```jsp
<script>
    $(document).ready(function () {
        var formObj = $("form[role='form']");
        console.log(formObj);
        $(".btn-warning").on("click", function () {
           formObj.attr("action", "/sboard/modify");
           formObj.attr("method", "get");
           formObj.submit();
        });
        $(".btn-danger").on("click", function () {
           formObj.attr("action", "/sboard/remove");
           formObj.submit();
        });
        $(".btn-primary").on("click", function () {
            self.location = "/sboard/listAll";
        });
        $(".goListBtn").on("click", function () {
        	formObj.attr("method", "get");
        	formObj.attr("action", "/sboard/listPage");
        	formObj.submit();
        });
        $(".removeBtn").on("click", function () {
        	formObj.attr("action", "/sboard/removePage");
        	formObj.submit();
        });
        $(".modifyBtn").on("click", function () {
        	formObj.attr("action", "/sboard/modifyPage");
        	formObj.attr("method", "get");
        	formObj.submit();
        });
    });
</script>
```



#### 10.6.2 게시물의 삭제 처리

- SearchBoardController

```java
@RequestMapping(value = "/removePage", method = RequestMethod.POST) 
public String remove(@RequestParam("bno") int bno, SearchCriteria cri, RedirectAttributes rttr) throws Exception{
  service.remove(bno);

  rttr.addAttribute("page", cri.getPage());
  rttr.addAttribute("perPageNum", cri.getPerPageNum());
  rttr.addAttribute("searchType", cri.getSearchType());
  rttr.addAttribute("keyword", cri.getKeyword());

  rttr.addFlashAttribute("msg", "SUCCESS");

  return "redirect:/sboard/list";
}
```



#### 10.6.3 게시물의 수정 처리

- SearchBoardController

```java
@RequestMapping(value = "/modifyPage", method = RequestMethod.GET)
public void modifyPagingGET(int bno, @ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception {

  model.addAttribute(service.read(bno));
}

@RequestMapping(value = "/modifyPage", method = RequestMethod.POST)
public String modifyPagingPOST(BoardVO board, SearchCriteria cri, RedirectAttributes rttr) throws Exception {

  logger.info(cri.toString());
  service.modify(board);

  rttr.addAttribute("page", cri.getPage());
  rttr.addAttribute("perPageNum", cri.getPerPageNum());
  rttr.addAttribute("searchType", cri.getSearchType());
  rttr.addAttribute("keyword", cri.getKeyword());

  rttr.addFlashAttribute("msg", "SUCCESS");

  logger.info(rttr.toString());

  return "redirect:/sboard/list";
}
```



- modifyPage.jsp

```jsp
<form role="form" action="modifyPage" method="post">
  <input type="hidden" name="page" 		value="${cri.page}"> 
  <input type="hidden" name="perPageNum" 	value="${cri.perPageNum }"> 
  <input type="hidden" name="searchType" 	value="${cri.searchType}" /> 
  <input type="hidden" name="keyword" 	value="${cri.keyword}" />
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

- modifyPage.jsp

```javascript
<script>
    $(document).ready(function () {
        var formObj = $("form[role='form']");
        console.log(formObj);
        
        $(".btn-warning").on("click"
        		, function () {
           			self.location = 
           				"/sboard/list?page=${cri.page}&perPageNum=${cri.perPageNum}"
           				+ "&searchType=${cri.searchType}&keyword=${cri.keyword}";
        });
        
        $(".btn-primary").on("click"
        		, function () {
            		formObj.submit();
        });
        
    });
</script>
```



### 10.7 등록 페이지 처리

- SearchBoardController

```java
@RequestMapping(value = "/register", method = RequestMethod.GET)
public void registGET() throws Exception {

  logger.info("regist get ...........");
}

@RequestMapping(value = "/register", method = RequestMethod.POST)
public String registPOST(BoardVO board, RedirectAttributes rttr) throws Exception {

  logger.info("regist post ...........");
  logger.info(board.toString());

  service.regist(board);

  rttr.addFlashAttribute("msg", "SUCCESS");

  return "redirect:/sboard/list";
}
```

- board/register -> sboard/register 복사



### 10.8 최종적인 결과 확인

- 검색 조건 테스트
- 조회 페이지에서 검색 조건 유지 테스트
- 삭제 작업 이후 검색 조건 유지 테스트
- 수정 작업 이후 검색 조건 유지 테스트




- 소스 오류 수정

  - list.jsp

    ```jsp
    <div class="box-footer">
      <div class="text-center">
        <ul class="pagination">
          <c:if test="${pageMaker.prev}">
            <li><a href="list${pageMaker.makeSearch(pageMaker.startPage - 1)}">&laquo;</a> </li>
          </c:if>

          <c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="idx">
            <li <c:out value="${pageMaker.cri.page == idx? 'class = active':''}"/>>
            <a href="list${pageMaker.makeSearch(idx)}">${idx}</a>
            </li>
          </c:forEach>

        <c:if test="${pageMaker.next && pageMaker.endPage > 0}">
          <li><a href="list${pageMaker.makeSearch(pageMaker.endPage + 1)}">&raquo;</a> </li>
        </c:if>
        </ul>
    </div>
    </div>
    ```

    ​


  - readPage.jsp (script 경로 수정)

    ```javascript
     $(".btn-primary").on("click", function () {
       self.location = "/sboard/list";
     });
    $(".goListBtn").on("click", function () {
      formObj.attr("method", "get");
      formObj.attr("action", "/sboard/list");
      formObj.submit();
    });
    ```

    ​



### 10.9 정리

- 개발 환경의 준비 및 데이터베이스 준비
- 새로운 게시물을 등록 할 수 있는 기능 개발
- 전체 목록 기능 개발
- 조회 기능 개발
- 삭제 기능 개발
- 수정 기능 개발
- 페이징 처리
- 검색 처리



- 미흡한 부분
  - 예제를 단순화 하기 위해서 스프링의 Validation기능을 사용하지 못한 점
  - JAvaScript를 이용해서 엄격한 <form> 태그의 입력 항목들을 체크하지 못한 점
  - 트랜잭션 관리