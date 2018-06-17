# Part 2 기본적인 기능의 게시물 관리

## Chapter 06 삭제/수정 처리

### 6.1 삭제 처리

- BoardController

```java
@RequestMapping(value = "/remove", method = RequestMethod.POST)
public String remove(@RequestParam("bno")int bno, RedirectAttributes rttr) throws Exception {
	service.remove(bno);
	rttr.addFlashAttribute("msg", "SUCCESS");

	return "redirect:/board/listAll";
}
```



### 6.2 수정 처리

- BoardController

```java
@RequestMapping(value = "/modify", method = RequestMethod.GET)
public void modifyGET(int bno, Model model) throws Exception {
	model.addAttribute(service.read(bno));
}
	
@RequestMapping(value = "/modify", method = RequestMethod.POST)
public String modifyPOST(BoardVO board, RedirectAttributes rttr) throws Exception {
	logger.info("mod post .....");
	service.modify(board);
	rttr.addFlashAttribute("msg", "SUCCESS");
	return "redirect:/board/listAll";
}
```

- modify.jsp

```jsp
<form role="form" method="post">
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
      <textarea name="contents" class="form-control" rows="3">${boardVO.contents}</textarea>
    </div>
    <div class="form-group">
      <label for="exampleInputEmail1">Writer</label>
      <input type="text" name="writer" class="form-control" value="${boardVO.writer}" readonly="readonly" />
    </div>
  </div>
</form>
<div class="box-footer">
  <button type="submit" class="btn btn-primary">SAVE</button>
  <button type="submit" class="btn btn-warning">CANCEL</button>
</div>
```

```jsp
<script>
    $(document).ready(function () {
        var formObj = $("form[role='form']");
        console.log(formObj);
        $(".btn-warning").on("click", function () {
           self.location = "/board/listAll"
        });
        $(".btn-primary").on("click", function () {
            formObj.submit();
        });
    });
</script>
```

- 수정페이지 테스트

![modifyPage](C:\Users\SeokRae Kim\Desktop\modifyPage.png)