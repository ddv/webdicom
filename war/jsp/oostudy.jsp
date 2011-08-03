<%@ page language="java" contentType="text/plain; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<jsp:useBean id="util" scope="page" class="org.psystems.dicom.browser.server.Util" />
<%
	
	String width = (String)request.getParameter("width");
	String height = (String)request.getParameter("height");
	String path = request.getPathInfo().replaceFirst("/", "");
	Connection connection = util.getConnection("main",getServletContext());
	PreparedStatement psSelect = null;
	psSelect = connection.prepareStatement("SELECT * FROM WEBDICOM.STUDY WHERE ID = ? ");
	psSelect.setLong(1, Long.valueOf(path).longValue());
	ResultSet rs = psSelect.executeQuery();
	while (rs.next()) {
		
%>
###ID###
<%=rs.getLong("ID") %>
###00200010###
<%=rs.getString("STUDY_ID") %>
###0020000D###
<%=rs.getString("STUDY_UID") %>
###0020000E###
<%=rs.getString("STUDY_UID") %>.<%=new java.util.Date().getTime() %>
###00080060###
<%=rs.getString("STUDY_MODALITY") %>
###STUDY_TYPE###
<%=rs.getString("STUDY_TYPE") %>
###00081030###
<%=rs.getString("STUDY_DESCRIPTION") %>
###00080020###
<%=rs.getString("STUDY_DATE") %>
###STUDY_MANUFACTURER_UID###
<%=rs.getString("STUDY_MANUFACTURER_UID") %>
###00081090###
<%=rs.getString("STUDY_MANUFACTURER_MODEL_NAME") %>
###00080090###
<%=rs.getString("STUDY_DOCTOR") %>
###00081070###
<%=rs.getString("STUDY_OPERATOR") %>
###00324000###
<%=rs.getString("STUDY_VIEW_PROTOCOL") %>
###00321050###
<%=rs.getString("STUDY_VIEW_PROTOCOL_DATE") %>
###00102000###
<%=rs.getString("STUDY_RESULT") %>
###DATE_MODIFY###
<%=rs.getString("DATE_MODIFY") %>
###00100021###
<%=rs.getString("PATIENT_ID") %>
###00100010###
<%=rs.getString("PATIENT_NAME") %>
###PATIENT_SHORTNAME###
<%=rs.getString("PATIENT_SHORTNAME") %>
###00100040###
<%=rs.getString("PATIENT_SEX") %>
###00100030###
<%=rs.getString("PATIENT_BIRTH_DATE")%>
###00000000###
<%}%>
