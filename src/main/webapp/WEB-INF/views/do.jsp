<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>do something</title>
</head>
<body>

	<%
		request.setCharacterEncoding("utf-8");
	%>

	<h3>chose the thing you want do</h3>

	<form action="../getUserInfo" method="get">
		<input type="submit" value="Obtain user info" />
	</form>
	<br>
	<form action="../forwardMsg" method="post">
		message: <input type="text" name="msg" /> <input type="submit"
			value="Forward" />
	</form>

</body>
</html>