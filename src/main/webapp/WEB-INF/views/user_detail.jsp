<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<base href="http://${pageContext.request.serverName }:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
<style type="text/css">
	body {
		margin: 0px auto 0px auto;
		width: 70%;
	}
</style>
</head>
<body>
	<h1>用户个人信息中心</h1>
	<table>
		<tr>
			<td>昵称:</td>
			<td>${sessionScope.loginUser.userNick }</td>
			
			<td rowspan="4">
				<c:if test="${!empty sessionScope.loginUser.userPicGroup }">
					<img src="http://192.168.162.242/${sessionScope.loginUser.userPicGroup }/${sessionScope.loginUser.userPicFilename}"
					height="400" width="600"/>
				</c:if>
				<c:if test="${empty sessionScope.loginUser.userPicGroup }">
					尚未上传头像
				</c:if>
			</td>
		</tr>
		
		<tr>
			<td>性别:</td>
			<td>
				<c:if test="${sessionScope.loginUser.userGender == 0 }">男</c:if>
				<c:if test="${sessionScope.loginUser.userGender == 1 }">女</c:if>
			</td>
		</tr>
		
		<tr>
			<td>职业:</td>
			<td>${empty sessionScope.loginUser.userJob ?"未认证":sessionScope.loginUser.userJob }</td>
		</tr>
		
		<tr>
			<td>家乡:</td>
			<td>${empty sessionScope.loginUser.userHometown ?"未认证":sessionScope.loginUser.userHometown }</td>
		</tr>
		
		<tr>
			<td>自我介绍:</td>
			<td>${empty sessionScope.loginUser.userDesc ?"未认证":sessionScope.loginUser.userDesc }</td>
		</tr>
		
		<tr>
			<td colspan="3" align="center">
				<h3><a href="demo/user/toEditUI">更新</a></h3>
			</td>
		</tr>
	</table>
	
	
	
</body>
</html>