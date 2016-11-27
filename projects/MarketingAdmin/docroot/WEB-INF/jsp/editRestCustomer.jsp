<%@ taglib uri='template' prefix='tmpl' %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<tmpl:insert template='site.jsp'>

    <tmpl:put name='title' direct='true'>File Upload Page</tmpl:put>

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
%> <FONT color="blue"><H3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="success.fileupload"/></H3></font>
<%  }  %>
 <form method="post" action="upload.do" enctype="multipart/form-data">
<spring:bind path="command">
  <FONT color="red">
    <B><c:out value="${status.errorMessage}"/></B>
  </FONT>
</spring:bind>
<table>
    <tr><td width="100"> </td><td>
    Upload the File :       
<spring:bind path="command.file">
  <BR><input type="file" name="file" value="<c:out value="${status.value}"/>" >
  <BR>
  <FONT color="red">
  <c:forEach items="${status.errorMessages}" var="error">
    <li><c:out value="${error}"/></li>
  </c:forEach>    
  </FONT>
</spring:bind> 	            
	</td>    
    </tr>
    <tr>
    <td width="100"></td>
    <td> Action Type :<BR>
       <spring:bind path="command.fileContentType">
       <FONT color="red">
       <B><c:out value="${status.errorMessage}"/></B>
      </FONT>
      
      <SELECT name="fileContentType">
       <c:forEach var="contentTypeVar" items="${fileContentTypes}">       
        <c:if test="${command.fileContentType.name == contentTypeVar.name}">
          <OPTION selected="<c:out value="${contentTypeVar.name}"/>" value="<c:out value="${contentTypeVar.name}"/>"><c:out value="${contentTypeVar.name}"/></OPTION>
        </c:if>
        <c:if test="${command.fileContentType.name != contentTypeVar.name}">
          <OPTION value="<c:out value="${contentTypeVar.name}"/>"><c:out value="${contentTypeVar.name}"/></OPTION>                    
        </c:if>                
        </c:forEach>
      </SELECT>
    </spring:bind>         
    <BR><BR>  
    <input type="submit"/>	
    </td>
    </tr>
</table>
 </form>
</body>

</tmpl:put>
</tmpl:insert>