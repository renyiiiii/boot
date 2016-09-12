<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>oauthlogin</title>
</head>
<body>
<h2>Hello World!</h2>

<form action="${loginUrl }" method="post">
  <input name="username" value="u1"><br>
  <input name="password" value="p1"><br>
  <input type="submit" value="submit">
</form>

<P>${message }</P>  
</body>
</html>