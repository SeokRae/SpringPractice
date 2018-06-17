# Part 2 기본적인 기능의 게시물 관리

## Chapter 05 조회 구현

- 조회 기능을 위한 BoardDAO의 처리 - 이미 처리된 완료
- BoardService(BoardServiceImpl 포함), BoardController의 처리 - 이미 처리된 완료
- 조회 페이지의 작성
- 수정, 삭제 링크 처리

### 5.1 BoardController의 기능 추가와 뷰 처리

- BoardController.java

```java
@RequestMapping(value = "/read", method = RequestMethod.GET)
public void read(@RequestParam("bno") int bno, Model model) throws Exception{
	model.addAttribute(service.read(bno));
}
```

#### 5.1.1 조회용 페이지 작성

- read.jsp

```java
<form role="form" method="post">
	<input type="hidden" name="bno" value="${boardVO.bno}" />
</form>
<div class="box-body">
	<div class="form-group">
	<label for="exampleInputEmail1">Title</label>
	<input type="text" name="title" class="form-control" value="${boardVO.title}" readonly="readonly" />
</div>
	<div class="form-group">
		<label for="exampleInputPassword1">Contents</label>
		<textarea name="contents" class="form-control" rows="3" readonly="readonly">${boardVO.contents}</textarea>
	</div>
	<div class="form-group">
		<label for="exampleInputEmail1">Writer</label>
		<input type="text" name="writer" class="form-control" value="${boardVO.writer}" readonly="readonly" />
	</div>
</div>
<div class="box-footer">
	<button type="submit" class="btn btn-warning">Modify</button>
	<button type="submit" class="btn btn-danger">Remove</button>
	<button type="submit" class="btn btn-primary">LIST ALL</button>
</div>
```



- 페이지 이동 테스트

![readPage](C:\Users\SeokRae Kim\Desktop\readPage.png)

### 5.2 수정, 삭제로의 링크 처리

- read.jsp

```jsp
<script>
    $(document).ready(function () {
        var formObj = $("form[role='form']");
        console.log(formObj);
        $(".btn-warning").on("click", function () {
           formObj.attr("action", "/board/modify");
           formObj.attr("method", "get");
           formObj.submit();
        });
        $(".btn-danger").on("click", function () {
           formObj.attr("action", "/board/remove");
           formObj.submit();
        });
        $(".btn-primary").on("click", function () {
            self.location = "/board/listAll";
        });
    });
</script>
```