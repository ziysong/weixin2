<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8"> 	
	<title>登录</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"> 
	<link rel="stylesheet" type="text/css" href="style/query.css">
</head>
<body>


<div id="top">
	<img src="image/loginLogo.jpg" width="235px" height="120px">
</div>

<form id="form" action="${pageContext.request.contextPath }/markQueryServlet" method="post">
	<div id="set">
		<img src="image/username.png" width="50px" height="25px"/>
		<input type="text" id="username" placeholder="学号" name="username"/>
	</div>
	<hr class="bott" style="border: 1px solid #ccc;"/>
	<div id="set">
		<img src="image/password.png" width="50px" height="25px"/>
		<input type="password" id="password" placeholder="密码" name="password"/>
	</div>
	<label><input type="submit" id="login1" class="btn" value="登录" name="login"/></label>
	<label><input type="submit" id="login2" class="btn" value="登录并绑定" name="loginbind"/></label>
</form>
	

</body>
</html>