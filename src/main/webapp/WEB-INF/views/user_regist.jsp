<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<base href="http://${pageContext.request.serverName }:${pageContext.request.localPort}${pageContext.request.contextPath}/" />
<style type="text/css">
	body {
		margin:0px auto 0px auto;
		width: 20%;
	}
</style>
</head>
<body>
	<h1>注册用户页面</h1>
	
	<form action="demo/user/regist" method="post">
	
		<p>${requestScope.massage }</p>
	
		账号：<input type="text" name="userName" /><br/>
		密码：<input type="text" name="userPwd" /><br/>
		昵称：<input type="text" name="userNick"/><br/>
		<input type="submit" value="注册"/>
	</form>
	<br/>
	<a href="index.jsp">返回首页 </a>
	
</body>
</html>