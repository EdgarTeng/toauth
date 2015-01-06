<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<html>
<head>
<title>register</title>
</head>
<body>
	<form:form method="post" action="save" modelAttribute="bean">
		<table>
			<tr>
				<td><b>username:</b></td>
				<td><form:input path="username" /></td>
			</tr>
			<tr>
				<td><b>password:</b></td>
				<td><form:input path="password" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="submit"></td>
			</tr>
		</table>
	</form:form>
</body>
</html>