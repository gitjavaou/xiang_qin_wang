<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
	<h1>首页</h1>
	
	<!-- 判断用户是否已经登录 -->
	<c:if test="${sessionScope.loginUser ==null }">
		<a href="demo/user/toLoginPage">登录</a> || 
		<a href="demo/user/toRegistPage">注册</a>
	</c:if>
	<c:if test="${sessionScope.loginUser !=null }">
		欢迎您：${sessionScope.loginUser.userNick } ||
		<a href="demo/user/showDetail">用户个人中心</a>||
		<a href="demo/user/logout">退出</a>
	</c:if>
	
	<form action="demo/user/search" method="post">
		<input type="text" name="keywords" />
		<input type="submit" value="搜索">
	</form>
</body>
</html>