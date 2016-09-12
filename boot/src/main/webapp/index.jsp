<%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<h2>Hello World!</h2>

<form action="login" method="post">
  <input name="username" value="u1"><br>
  <input name="password" value="p1"><br>
  <input type="submit" value="submit">
</form>

<P>${message }</P>  

<a href="authorize?client_id=ci1&response_type=code&redirect_uri=http://localhost:8580/boot/oauth2/client/login">授权登录</a>
</body>
</html>
