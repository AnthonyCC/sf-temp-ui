<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/site.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Truck</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form:form commandName = "truckForm" method="post">
			<form:hidden path="truckId"/>
			
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Add/Edit Truck</td>
					</tr>
					<tr>
						<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
					</tr>
					
					<tr>
						<td class="screencontent">
							<table class="forms1">			  	
							  <tr>
							    <td>Truck Number</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="truckNumber" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="truckNumber" />
							 	</td>
							 </tr>
							 							 
							 <tr>
							    <td>Truck Type</td>
							    <td> 
								  <form:select path="truckType">
							  	  		<form:option value="null" label="--Please Select Truck Type"/>
										<form:options items="${trucktypes}" />
							     </form:select>
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="truckType" />
							 	</td>
							 </tr>
							 
							 <tr>
							    <td>License Plate</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="licensePlate" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="licensePlate" />
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