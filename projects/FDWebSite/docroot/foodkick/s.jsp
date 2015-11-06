<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="image/svg+xml; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="includes/fk_core_settings.jspf"%>
<%
String fn = request.getParameter("f");
if( fn.endsWith(".svg") ){
	//String filePath = FKAPP_DIR + "/" + IMAGES_DIR + "/" + fn.replace("./", "");
	String filePath = IMAGES_DIR + "/" + fn.replace("./", "");
	
	//out.println("foo " + filePath);

	if(null != application.getResource( filePath )){
%>
	<jsp:include page="<%= filePath %>" flush="true" />
<%
	}
}
%>