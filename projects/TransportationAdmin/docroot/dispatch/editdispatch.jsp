<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/site.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Dispatch</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form:form commandName = "dispatchForm" method="post">
			<form:hidden path="id.planId"/>
			<form:hidden path="id.dispatchDate"/>
			<form:hidden path="zone"/>
						
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Add Dispatch</td>
					</tr>
					<tr>
						<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
					</tr>
					
					<tr>
						<td class="screencontent">
							<table class="forms1">	 								  							
							 <tr>
							    <td>Time Slot</td>
							    <td> 
								  <form:select path="timeslot">
							  	  		<form:option value="null" label="--Please Select Time Slot"/>
										<form:options items="${timeslots}" itemLabel="slotName" itemValue="slotId" />
							     </form:select>
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="timeslot" />
							 	</td>
							 </tr>
							  <tr>
							    <td>Route</td>
							    <td> 
								  <form:select path="route">
							  	  		<form:option value="null" label="--Please Select Route"/>
										<form:options items="${routes}" itemLabel="routeNumber" itemValue="routeId" />
							     </form:select>
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="route" />
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
							    <td>Truck</td>
							    <td> 
								  <form:select path="truck">
							  	  		<form:option value="null" label="--Please Select Truck"/>
										<form:options items="${trucks}" itemLabel="truckNumber" itemValue="truckId" />
							     </form:select>
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="truck" />
							 	</td>
							 </tr>
							 <tr>
							    <td>Driver</td>
							    <td> 
								  <form:select path="driver">
							  	  		<form:option value="null" label="--Please Select Driver"/>
										<form:options items="${drivers}" itemLabel="name" itemValue="employeeId" />
							     </form:select>
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="driver" />
							 	</td>
							 </tr>
							 
							 <tr>
							    <td>Primary Helper</td>
							    <td> 
								  <form:select path="primaryHelper">
							  	  		<form:option value="null" label="--Please Select Helper1"/>
										<form:options items="${helpers}" itemLabel="name" itemValue="employeeId" />
							     </form:select>
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="primaryHelper" />
							 	</td>
							 </tr>
							 
							 <tr>
							    <td>Secondary Helper</td>
							    <td> 
								  <form:select path="secondaryHelper">
							  	  		<form:option value="null" label="--Please Select Helper2"/>
										<form:options items="${helpers}" itemLabel="name" itemValue="employeeId" />
							     </form:select>
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="secondaryHelper" />
							 	</td>
							 </tr>
							<tr>
							    <td>Nextel</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="nextelId" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="nextelId" />
							 	</td>
							 </tr>
							 <tr>
							    <td>Comments</td>
							    <td> 								  
							  	 	<form:textarea path="comments" rows="5" cols="45" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="comments" />
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