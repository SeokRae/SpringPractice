<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@include file="../include/header.jsp" %>

	<!-- Main contents -->
	<section class="contents">
	    <div class="row">
	        <!-- left column -->
	        <div class="col-md-12">
	            <!-- general form elements -->
	            <div class="box box-primary">
	                <div class="box-header">
	                    <h3 class="box-title">REGISTER BOARD</h3>
	                </div>
	                <!-- /.box-header -->
					
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
	                <div class="box-footer">
	                    <button type="submit" class="btn btn-primary">SAVE</button>
	                    <button type="submit" class="btn btn-warning">CANCEL</button>
	                </div>
	            </div>
	            <!-- /.box -->
	        </div>
	        <!--/.col (left) -->
	
	    </div>
	    <!-- /.row -->
	</section>
	<!-- /.contents -->
</div>
<!-- /.contents-wrapper -->

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

<%@include file="../include/footer.jsp" %>