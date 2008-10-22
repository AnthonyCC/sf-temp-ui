<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/site.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Zone</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form:form commandName = "zoneForm" method="post">
			<form:hidden path="zoneId"/>
			
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Add/Edit Zone</td>
					</tr>
					<tr>
						<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
					</tr>
					
					<tr>
						<td class="screencontent">
							<table class="forms1">			  	
							  <tr>
							    <td>Zone Number</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="zoneNumber" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="zoneNumber" />
							 	</td>
							 </tr>
							 							 
							 <tr>
							    <td>Supervisor</td>
							    <td> 
								  <form:select path="supervisor">
							  	  		<form:option value="null" label="--Please Select Supervisor"/>
										<form:options items="${supervisors}" itemLabel="name" itemValue="employeeId" />
							     </form:select>
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="supervisor" />
							 	</td>
							 </tr>
							 
							 <tr>
							    <td>Zone Type</td>
							    <td> 
								  <form:select path="zoneType">
							  	  		<form:option value="null" label="--Please Select Zone Type"/>
										<form:options items="${zonetypes}" itemLabel="name" itemValue="zoneTypeId" />
							     </form:select>
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="zoneType" />
							 	</td>
							 </tr>
							 
							 <tr>
							    <td>Area</td>
							    <td> 
								  <form:select path="area">
							  	  		<form:option value="null" label="--Please Select Area"/>
										<form:options items="${areas}" itemLabel="name" itemValue="code" />
							     </form:select>
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="area" />
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