<%@ taglib uri='template' prefix='tmpl' %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<tmpl:insert template='site.jsp'>

    <tmpl:put name='title' direct='true'>Competitor Form Page</tmpl:put>

	<tmpl:put name='content' direct='true'>

<br>
<table width="80%" border="0">
  <tr>
    <td width="80%" align="center">
<H2><c:if test="${command.new}">New </c:if>Competitor Information</H2>
<spring:bind path="command">
  <FONT color="red">
    <B><c:out value="${status.errorMessage}"/></B>
  </FONT>
</spring:bind>
</th>
  </tr>
</table>

<FORM method="POST">  

<table width="80%" border="0">
  <tr valign="top">
    <td align="right" valign="top"><label><B>Competitor Name:</B></label>&nbsp;</td>
    <td valign="top"> 
<spring:bind path="command.companyName">
  <FONT color="red">  
    <B><c:out value="${status.errorMessage}"/></B>
  </FONT>
  <INPUT type="text" maxlength="50" size="30" name="companyName" value="<c:out value="${status.value}"/>" >
</spring:bind>
 </td>
 </tr>
 <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
 <tr valign="top"> <td align="right" valign="top"><label><B>Competitor Address:</B></label>&nbsp;</td>
<td valign="top">
<spring:bind path="command.address1">
  <FONT color="red">
    <B><c:out value="${status.errorMessage} "/></B>
  </FONT>
  <INPUT type="text" maxlength="55" size="30" name="address1" value="<c:out value="${status.value}"/>" >
</spring:bind>
</td>
</tr>
<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr valign="top">
    <td align="right"><label><B>Competitor City:</B></label>&nbsp;</td>
<td>
<spring:bind path="command.city">
  <FONT color="red">
    <B><c:out value="${status.errorMessage}"/></B>
  </FONT>
  <INPUT type="text" maxlength="55" size="30" name="city" value="<c:out value="${status.value}"/>" >
</spring:bind>
</td>
</tr>
<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr valign="top">
    <td align="right"><label><B>Competitor State:</B></label>&nbsp;</td>
<td>
<spring:bind path="command.state">
  <FONT color="red">
    <B><c:out value="${status.errorMessage}"/></B>
  </FONT>
  <INPUT type="text" maxlength="2" size="2" name="state" value="<c:out value="${status.value}"/>" >
</spring:bind>
</td>
</tr>
<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr valign="top">
    <td align="right"><label><B>Competitor Zip Code:</B></label>&nbsp;</td>
<td>
<spring:bind path="command.zipCode">
  <FONT color="red">
    <B><c:out value="${status.errorMessage}"/></B>
  </FONT>
  <INPUT type="text" maxlength="10" size="30" name="zipCode" value="<c:out value="${status.value}"/>" >
</spring:bind>
</td>
</tr>
<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr valign="top">
    <td align="right"><label><B>Competitor Type:</B></label>&nbsp;</td>
<td>
    <spring:bind path="command.competitorType">
       <FONT color="red">
       <B><c:out value="${status.errorMessage}"/></B>
      </FONT>
      
      <SELECT name="competitorType">
       <c:forEach var="competitorTypeVar" items="${competitorTypes}">       
        <c:if test="${command.competitorType.name == competitorTypeVar.name}">
          <OPTION selected="<c:out value="${competitorTypeVar.name}"/>" value="<c:out value="${competitorTypeVar.name}"/>"><c:out value="${competitorTypeVar.description}"/></OPTION>
        </c:if>
        <c:if test="${command.competitorType.name != competitorTypeVar.name}">
          <OPTION value="<c:out value="${competitorTypeVar.name}"/>"><c:out value="${competitorTypeVar.description}"/></OPTION>                    
        </c:if>                
        </c:forEach>
      </SELECT>
    </spring:bind>
</td>
</tr>
<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<tr valign="top">
    <td align="right">&nbsp;&nbsp;</label>&nbsp;</td>
<td align="left">
    <c:if test="${command.new}">
    <INPUT type = "submit" value="Add Competitor"  />
  </c:if>
  <c:if test="${!command.new}">
    <INPUT type = "submit" value="Update Competitor"  />
  </c:if>
</td>

</tr>
</table>

</FORM>

<BR>

</tmpl:put>
</tmpl:insert>