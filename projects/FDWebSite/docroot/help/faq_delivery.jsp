<%
String qNumber ="";
if(request.getParameter("qNumber") != null){
	qNumber = request.getParameter("qNumber");
}
%>
<html lang="en-US" xml:lang="en-US">
  <head>
    <title>FreshDirect - Help - Delivery</title>
    <%@ include file="/common/template/includes/seo_canonical.jspf" %>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
  </head>
<FRAMESET ROWS="47,*" BORDER="0" FRAMEBORDER="0" FRAMESPACING="0" BORDERCOLOR="#FFFFFF">
<FRAME MARGINWIDTH="0" 
  	   MARGINHEIGHT="0" 
       NAME="top_nav" 
       SRC="/help/header.jsp" 
       SCROLLING=NO>


<FRAMESET COLS="150,*" BORDER="0" FRAMEBORDER="0" FRAMESPACING="0" BORDERCOLOR="#FFFFFF"> 
<FRAME MARGINWIDTH="0" 
  	   MARGINHEIGHT="0" 
       NAME="left_nav" 
       SRC="/help/nav_delivery.jsp" 
       SCROLLING=NO>
				
<FRAME MARGINWIDTH="0" 
       MARGINHEIGHT="0" 
       NAME="right_content" 
       SRC="/help/delivery.jsp#<%=qNumber%>" 
       SCROLLING="auto">		
</FRAMESET>
</FRAMESET>
</html>
