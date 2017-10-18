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
	<table>
		<!-- 关键词没有匹配的情况 -->
		<c:if test="${empty requestScope.list }">
			<tr>
				<td>没有匹配到该关键词</td>
			</tr>
		</c:if>
		<c:if test="${!empty requestScope.list }">
			<tr>
				<td>头像</td>
				<td>昵称</td>
				<td>性别</td>
				<td>家乡</td>
				<td>职业</td>
				<td>详情</td>
			</tr>
			<c:forEach items="${requestScope.list }" var="resultMap">
				<tr>
					<td>
						<c:if test="${empty resultMap['user_pic_group'] }">尚未设置头像</c:if>
						<c:if test="${!empty resultMap['user_pic_group'] }">
							<img src="http://192.168.162.242/${resultMap['user_pic_group'] }/${resultMap['user_pic_filename'] }"
							height="400" width="600"/>
						</c:if>
					</td>
					<td>${resultMap['user_nick'] }</td>
					<td>${resultMap['user_gender'] }</td>
					<td>${resultMap['user_job'] }</td>
					<td>${resultMap['user_hometown'] }</td>
					<td>
						<a href="demo/user/search_detail/${resultMap['id'] }">详情</a>
					</td>
				</tr>
			</c:forEach>
			
		</c:if>		
	</table>
	
</body>
</html>