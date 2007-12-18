<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<body bgcolor="#ffffff" topmargin="0" leftmargin="0">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td width="100%"><img src="images/urban-highway2.jpg" width="129" height="82" border="0" alt="Urban"><img src="images/logo_sm.gif" width="130" height="32" border="0" alt="FreshDirect" HSPACE="10" VSPACE="2"></TD>
		</tr>
		<tr>
			<td bgcolor="c00cc3d" width="100%"><font class="space2pix">&nbsp;</font></td>
		</tr>
</table>

<%
com.freshdirect.mktAdmin.exception.MktAdminSystemException ex=null;
Exception e = (Exception) request.getAttribute("exception");
if(e instanceof com.freshdirect.mktAdmin.exception.MktAdminSystemException){
   System.out.println(" it is com.freshdirect.mktAdmin.exception.MktAdminSystemException");
   ex=(com.freshdirect.mktAdmin.exception.MktAdminSystemException)e; 
}
if(ex!=null){
%>

<H2>System failure: <%= ex.getMessage() %></H2>
<P>
<!--
   <%=ex.getCausedException()%>
--> 
<% }else{ %>
<H2>System failure: <%= e.getMessage() %></H2>
<% } %>
 


</body>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>
