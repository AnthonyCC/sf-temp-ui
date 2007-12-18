%@ taglib uri='template' prefix='tmpl' %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<tmpl:insert template='site.jsp'>

    <tmpl:put name='title' direct='true'>Competitor Form Page</tmpl:put>

	<tmpl:put name='content' direct='true'>

<body>

<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td width="100%"><img src="images/urban-highway2.jpg" width="129" height="82" border="0" alt="Urban"><img src="images/logo_sm.gif" width="130" height="32" border="0" alt="FreshDirect" HSPACE="10" VSPACE="2"></TD>
		</tr>
		<tr>
			<td bgcolor="c00cc3d" width="100%"><font class="space2pix">&nbsp;</font></td>
		</tr>
</table>

<table width="100%" border="1">
  <tr>
    <th scope="col">Competitor View </th>
  </tr>
</table>
<p>&nbsp;</p>

<table width="100%" border="0">
  <tr align="left">
    <th width="70%" scope="col"><h3>
        <label><center>Existing Competitor Information</center></label>
    &nbsp;</h3></th>
    <th width="30%" scope="col"><h3>&nbsp;</h3></th>
  </tr>
  <tr>
    <th scope="col">&nbsp;</th>
    <th scope="col">&nbsp;</th>
  </tr>
</table>
<table width="80%">
<tr>
    <th class="competitorHeader" width="68" scope="col"><label class="competitorHeader">Id</label>&nbsp;</th>
    <th class="competitorHeader" width="163" scope="col"><label>Competitor Name</label>&nbsp;</th>
    <th class="competitorHeader" width="82" scope="col"><label>Competitor Address</label>&nbsp;</th>
    <th class="competitorHeader" width="82" scope="col"><label>Competitor Status</label>&nbsp;</th>
    <th class="competitorHeader" width="94" scope="col">&nbsp; <label>Delete</label></th>
  </tr>
  <c:forEach items="${competitors}" var="competitor">
  <tr align="center"  style="border:solid;">
    <td><a href="editCompetitor.do?competitorId=<c:out value="${competitor.id}"/>"><c:out value="${competitor.id}"/></a>  </TD>
    <td><c:out value="${competitor.companyName}"/></a>  </TD>
    <td><c:out value="${competitor.address1}"/></a>  </TD>
    <td><c:out value="${competitor.status.name}"/></a>  </TD>
    <td><a href="deleteCompetitor.do?competitorId=<c:out value="${competitor.id}"/>">delete</a></td>    
    <td>&nbsp;</td>
  </tr>
  </c:forEach>
</table>
<p>&nbsp;</p>
<br>
<a href="addCompetitor.do">Add Competitor Information</a> 
</body>


</tmpl:put>
</tmpl:insert>%>
