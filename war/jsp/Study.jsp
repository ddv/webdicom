<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Исследования</title>
</head>

<body>

<jsp:include page="/Header.jsp"></jsp:include>

<jsp:useBean id="util" scope="page"
	class="org.psystems.dicom.browser.server.Util" />


<script language="javascript">


function openwindow (linka,name,_left,_top,_width,_height) {

	var width = _width;
	var height = _height;
	var left=(screen.width-width)/2;
	var top=(screen.height-height)/2;
	window.open(linka,name,'left='+left+',top='+top+',width='+width+',height='+height+',toolbar=0,location=0,directories=0,menubar=0,status=0,resizable=1,scrollbars=1');
	
}

</script>

<table border='0' cellpadding='10'><tr>

<%
	
	String width = (String)request.getParameter("width");
	String height = (String)request.getParameter("height");
	String path = request.getPathInfo().replaceFirst("/", "");
	Connection connection = util.getConnection("main",getServletContext());
	PreparedStatement psSelect = null;
	psSelect = connection.prepareStatement("SELECT * FROM WEBDICOM.DCMFILE WHERE FID_STUDY = ? ");
	psSelect.setLong(1, Long.valueOf(path).longValue());
	ResultSet rs = psSelect.executeQuery();
	while (rs.next()) {
		String file = rs.getString("DCM_FILE_NAME");
		long dcmId = rs.getLong("ID");
		/*String href = "../images/"+dcmId+".fullsize";*/
		String href = "../study/image/"+dcmId;
		
		if(rs.getLong("IMAGE_FILE_SIZE")>0 || 
				(rs.getString("MIME_TYPE")!=null && rs.getString("MIME_TYPE").equals("image/jpg") ) )  {
%>


<td>
		
<a href='<%=href%>' onclick="openwindow('<%=href%>','name2',0,0,<%=width %>,<%=height %>); return false"> 
<img src="../images/<%=dcmId%>.100x100" border='0'> </img> 
</a>

</td>


<%		}
		
		if( rs.getString("MIME_TYPE")!=null && rs.getString("MIME_TYPE").equals("application/pdf") && rs.getLong("DOCUMENT_SIZE") > 0 )  {
			href = "../dcmpdf/"+dcmId+".pdf";
		%> 
		
		<td>
		<a href='<%=href%>' onclick="openwindow('<%=href%>','name2',0,0,<%=width %>,<%=height %>); return false"> 
		<img src="../logoPDF.png" border='0'> </img> 
		</a>
		</td>
		<% 
		}
	}

%>

</tr></table>



</body>
</html>




