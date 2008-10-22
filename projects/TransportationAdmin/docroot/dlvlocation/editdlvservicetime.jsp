<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/sitelocation.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Service Time</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form:form commandName = "dlvServiceTimeForm" method="post">
						
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Add/Edit Service Time</td>
					</tr>
					<tr>
						<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
					</tr>
					
					<tr>
						<td class="screencontent">
							<table class="forms1">			  	
							 <tr>
							    <td>Service Time Type</td>
							    <td> 
								  <form:select path="serviceTimeType">
							  	  		<form:option value="null" label="--Please Select Service Time Type"/>
										<form:options items="${servicetimetypes}" itemLabel="name" itemValue="code" />
							     </form:select>
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="serviceTimeType" />
							 	</td>
							 </tr>
							 <tr>
							    <td>Zone Type</td>
							    <td> 
								  <form:select path="zoneType">
							  	  		<form:option value="null" label="--Please Select Zone Type"/>
										<form:options items="${zonetypes}"  itemLabel="name" itemValue="zoneTypeId" />
							     </form:select>
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="zoneType" />
							 	</td>
							 </tr>
							 
							  <tr>
							    <td>Fixed Service Time</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="fixedServiceTime" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="fixedServiceTime" />
							 	</td>
							 </tr>
							 
							  <tr>
							    <td>Variable Service Time</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="variableServiceTime" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="variableServiceTime" />
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