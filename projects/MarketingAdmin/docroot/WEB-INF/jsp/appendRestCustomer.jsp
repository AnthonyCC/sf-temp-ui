<%@ taglib uri='template' prefix='tmpl' %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<tmpl:insert template='site.jsp'>

    <tmpl:put name='title' direct='true'>File Upload Page</tmpl:put>

	<tmpl:put name='content' direct='true'>
    
<body bgcolor="#ffffff" topmargin="0" leftmargin="0">

<% String msgKey=(String)request.getAttribute("success");
   if(msgKey!=null){
%> <FONT color="blue"><H3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="${success}"/></H3></font>
<%  }  %>
 <form method="post" action="appendRestriction.do" enctype="multipart/form-data">
<spring:bind path="command">
  <FONT color="red">
    <B><c:out value="${status.errorMessage}"/></B>
  </FONT>
</spring:bind>
<table>
    <tr>
    <td colspan="2" rowspan="3" align="right">Restrict to Customer(s):<br>
			<span class="field_note">comma separated<br>
			customer email IDs<br>
			no whitespaces</span></td>
    <td>
    Customer Ids :       
<spring:bind path="command.customerIds">
  <BR>&nbsp;<textarea name="customerIds" class="input" style="width: 300px;" wrap="virtual" rows="6"><c:out value="${status.value}"/></textarea>
  <BR>
    <spring:bind path="command.promotionCode">
    <input type="hidden" name="promotionCode" value="<c:out value="${status.value}"/>" >
    </spring:bind> </td>	                
  <FONT color="red">
  <c:forEach items="${status.errorMessages}" var="error">
    <li><c:out value="${error}"/></li>
  </c:forEach>    
  </FONT>
</spring:bind> 	            
	</td>    
    </tr>
    <tr>
    <td align="center" colspan="2">
    <BR><BR>  
    <input type="submit" value="Append Customer"/>	
    </td>
    </tr>
</table>
 </form>
</body>

</tmpl:put>
</tmpl:insert>