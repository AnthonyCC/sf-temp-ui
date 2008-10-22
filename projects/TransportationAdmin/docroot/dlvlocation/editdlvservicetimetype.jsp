<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/sitelocation.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Delivery Service Time Type</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form:form commandName = "dlvServiceTimeTypeForm" method="post">
			<form:hidden path="isNew"/>
			<c:if test="${!empty dlvServiceTimeTypeForm.code  && empty dlvServiceTimeTypeForm.isNew}">
  						<c:set var="hasPK" value="true"/>
			</c:if>		
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Add/Edit Delivery Service Time Type</td>
					</tr>
					<tr>
						<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
					</tr>
					
					<tr>
						<td class="screencontent">
							<table class="forms1">			  	
							  <tr>
							    <td>Code</td>
							    <td> 								  
							  	 	<form:input readonly="${hasPK}" maxlength="50" size="30" path="code" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="code" />
							 	</td>
							 </tr>
							 
							 <tr>
							    <td>Name</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="name" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="name" />
							 	</td>
							 </tr>
							 
							 <tr>
							    <td>Description</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="description" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="description" />
							 	</td>
							 </tr>
							 							  
							 
							<tr><td colspan="3">&nbsp;</td></tr>
							<tr>
							    <td colspan="3" align="center">
								   <input type = "submit" value="&nbsp;Save&nbsp;"  />
								</td>			
							</tr>
							</table>				
							
						</td>
					</tr>								
				</table>
			
			</form:form>
		 </div>
		 
	</tmpl:put>
</tmpl:insert>