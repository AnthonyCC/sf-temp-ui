<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/site.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Plan</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form:form commandName = "planForm" method="post">
			<form:hidden path="planId"/>
			
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Add/Edit Plan</td>
					</tr>
					<tr>
						<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
					</tr>
					
					<tr>
						<td class="screencontent">
							<table class="forms1">			  	
							  <tr>
							    <td>Day</td>
							    <td>
							    <form:select path="dispatchDay">
							  	  		<form:option value="null" label="--Please Select Day"/>
										<form:options items="${days}" />
							     </form:select>
							 	<td>
							 		&nbsp;<form:errors path="dispatchDay" />
							 	</td>
							 </tr>
							 <tr>
							    <td>Zone Number</td>
							    <td> 
								  <form:select path="zone">
							  	  		<form:option value="null" label="--Please Select Zone"/>
										<form:options items="${zones}" itemLabel="zoneNumber" itemValue="zoneId" />
							     </form:select>
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="zone" />
							 	</td>
							 </tr>
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