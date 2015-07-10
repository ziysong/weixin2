<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="script/jquery-1.11.2.js"> </script>
<script type="text/javascript" src="script/ajaxfileupload.js"> </script>

<title>How Old Are You?</title>
</head>
<body>
	<div id="img">
		<img  id="imgvalue" src="/face/3.jpg"/>
	</div>

	<form id="urlform" method="post">
		<input type="text" name="url-field" id="url-field" style="width:400px;height:30px" placeholder="URL"/>
		<input type="button" id="urlbutton" style="height:30px" value="How Old?"/> <br>
	</form>
																	
	<form id="postform" action="" method="post" enctype="multipart/form-data">
		<input type="file" name="post-field" id="post-field" style="width:403px;height:30px"/>
		<input type="button" id="postbutton" style="height:30px" value="How Old?"/> <br>
	</form>  

</body>

<script type="text/javascript">
	$("#urlbutton").click(function(){
		$.post("howOldServlet", $("#urlform").serializeArray(), function(data){
			
			document.getElementById("imgvalue").setAttribute("src", "/face/"+data);
		},
		"text"); /*当输出的data为图片名时类型要设置为text而不是html*/
	});
	
	$("#postbutton").click(function(){
		 $.ajaxFileUpload({
            url: 'howOldServlet', 
            type: 'post',
            secureuri: false, 
            fileElementId: 'post-field', // 上传文件的id、name属性名
            dataType: 'text', //返回值类型
            success: function(data, status){  
            	document.getElementById("imgvalue").setAttribute("src", "/face/"+data);
            },
            error: function(data, status, e){ 
                alert(e);
            }
	     });
	});
</script>

<!-- <script type="text/javascript">
	function doSubmit(){
		/* var imgName = '<c:out value="${imgName}" />';
		alert(imgName); */
		var textValue = document.getElementById("url-field").value;
		alert(textValue);
		int index = textValue.indexOf("/");
		imgName = textValue.substring(index);
		alert(imgName);
	}	
</script> -->


</html>