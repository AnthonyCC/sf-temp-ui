<%@ taglib uri='template' prefix='tmpl' %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<tmpl:insert template='site.jsp'>

    <tmpl:put name='title' direct='true'>Success Page</tmpl:put>

	<tmpl:put name='content' direct='true'>
<body bgcolor="#ffffff" topmargin="0" leftmargin="0">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td width="100%"><img src="images/urban-highway2.jpg" width="129" height="82" border="0" alt="Urban"><img src="images/logo_sm.gif" width="130" height="32" border="0" alt="FreshDirect" HSPACE="10" VSPACE="2"></TD>
		</tr>
		<tr>
			<td bgcolor="c00cc3d" width="100%"><font class="space2pix">&nbsp;</font></td>
		</tr>
</table>

<% String msgKey=(String)request.getAttribute("success");
   if(msgKey!=null){
%> <FONT color="blue"><H3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="${success}"/></H3></font>
<%  }  %>

<table>
    <tr><td width="100"> </td>
    <td><input type="button" value="CLOSE WINDOW" onclick="window.close()">
    </td>
</table>    
</body>
</tmpl:put>
</tmpl:insert>
