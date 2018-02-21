<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
		<style>
			.fileDrop {
				width: 100%;
				height: 200px;
				border: 1px dotted blue;
			}
			
			small {
				margin-left: 3px;
				font-weight: bold;
				color: gray;
			}
		</style>
	</head>
	<body>
		<h3>Ajax File Upload</h3>
		<div class='fileDrop'></div>
	
		<div class='uploadedList'></div>
		
		<script>
			$(".fileDrop").on("dragenter dragover", function(event) {
				event.preventDefault();
			});
			
			$(".fileDrop").on("drop", function(event) {
				event.preventDefault();
				
				var files = event.originalEvent.dataTransfer.files;
				var file = files[0];
				console.log(file);
				var formData = new FormData();
				formData.append("file", file);
				
				$.ajax({
					url: "/uploadAjax"
					, data: formData
					, dataType: "text"
					, processData: false
					, contentType: false
					, type: "POST"
					, success: function(data) {
// 						alert(data);
						var str = "";
						
						console.log(data);
						console.log(checkImageType(data));
						
						if(checkImageType(data)){
							str = "<div>"
								+ "<a href='displayFile?fileName=" + getImageLink(data) + "'>"
								+ "<img src='displayFile?fileName=" + data + "'/>"
								+ getImageLink(data) + "</a></div>";
						} else {
							str ="<div><a href='displayFile?fileName=" + data + "'>"
								+ getOriginalName(data)
								+ "</a></div>";
						}
						$(".uploadedList").append(str);
					}
				});
			});
			
			function checkImageType(fileName) {
				var pattern = /jpg$|gif$|png$|jpg$/i;
				return fileName.match(pattern);
			}
			
			function getOriginalName(fileName) {
				if(checkImageType(fileName)) {
					return;
				}
				var idx = fileName.indexOf("_") + 1;
				return fileName.substr(idx);
			}
			
			function getImageLink(fileName) {
				if(!checkImageType(fileName)) {
					return;
				}
				var front = fileName.substr(0, 12);
				var end = fileName.substr(14);
				return front + end;
			}
		</script>
	</body>
</html>