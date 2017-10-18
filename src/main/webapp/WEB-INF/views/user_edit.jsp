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
<h1>更新用户信息页面</h1>

	<form action="demo/user/update" method="post" enctype="multipart/form-data">
	
		<input type="hidden" name="userId" value="${sessionScope.loginUser.userId}" />
		<table>
			<tr>
				<td>昵称：<input type="text" name="userNick" value="${sessionScope.loginUser.userNick }"/></td>
				<td rowspan="6">
					<c:if test="${!empty sessionScope.loginUser.userPicGroup }">
						<img src="http://192.168.162.242/${sessionScope.loginUser.userPicGroup }/${sessionScope.loginUser.userPicFilename }"
						height="400" width="600"/>
					</c:if>
					<c:if test="${empty sessionScope.loginUser.userPicGroup }">
						尚未上传头像
					</c:if>
				</td>
			</tr>
			<tr>
				<td>性别：
					<input type="radio" name="userGender" value="0" 
						<c:if test="${sessionScope.loginUser.userGender==0}">checked="checked"</c:if>
					/>男
					<input type="radio" name="userGender" value="1"
						<c:if test="${sessionScope.loginUser.userGender==1}">checked="checked"</c:if>
					/>女
				</td>
				
			</tr>
			<tr>
				<td>职业：<input type="text" name="userJob" value="${sessionScope.loginUser.userJob }"/></td>
				
			</tr>
			<tr>
				<td>家乡：<input type="text" name="userHometown" value="${sessionScope.loginUser.userHometown }"/></td>
				
			</tr>
			<tr>
				<td>
					自我介绍：<br/>
					<textarea name="userDesc" >${sessionScope.loginUser.userDesc }</textarea>
				</td>
				
			</tr>
			<tr>
				<td>头像：<input type="file" name="headPicture" /></td>
				
			</tr>
		</table>
		
			<input type="submit" value="修改" />
			<input type="button" value="取消" />
	</form>
		<a href="index.jsp">返回首页</a>
</body>
</html>