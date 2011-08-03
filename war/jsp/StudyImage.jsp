<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Изображение</title>
</head>

<body>

<jsp:include page="/jsp/Header.jsp"></jsp:include>

<% 
	
	String dcmId = request.getPathInfo().replaceFirst("/", "");
%>


<br>

<img src="../../images/<%=dcmId%>.fullsize" border='0'> </img> 

</body>
</html>




