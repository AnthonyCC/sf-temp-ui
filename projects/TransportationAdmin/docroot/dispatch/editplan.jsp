<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<% boolean hasErrors = session.getAttribute("apperrors") != null; %>

<tmpl:insert template='/common/site.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Plan</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form:form commandName = "planForm" method="post">
			<form:hidden path="planId"/>
			
			<form:hidden path="ignoreErrors"/>
			<form:hidden path="errorDate"/>
			
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
							    <td>Date</td>
							    <td>
							    	<form:input maxlength="50" size="24" path="planDate" />
							    
							    &nbsp;<a href="#" id="trigger_planDate" style="font-size: 9px;">
							  	 			<img src="images/calendar.gif"  style="border:0"  alt=">>" />
							  	 		  </a>
							  	 	 <script language="javascript">									
									    Calendar.setup(
									    {
										    showsTime : false,
										    electric : false,
										    inputField : "planDate",
										    ifFormat : "%Y-%m-%d",
										    singleClick: true,  	                                        
										    button : "trigger_planDate",
										    onUpdate : updateDate										     
										 }
									    );
									    function updateDate(cal) {
									    		var selIndex = cal.date.getDay();
									    		if(selIndex == 0) selIndex = 7;
										    	document.getElementById('dispatchDay').selectedIndex =  selIndex;
										    };
									  </script>
								</td>	  
							 	<td>
							 		&nbsp;<form:errors path="planDate" />
							 	</td>
							 </tr>		  	
							  <tr>
							    <td>Day</td>
							    <td>
							    <form:select path="dispatchDay" disabled="true">
							  	  		<form:option value="null" label="--Please Select Day"/>
										<form:options items="${days}" />
							     </form:select>
							     </td>
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
							    <% if(hasErrors) { %>
							    <td align="center">
								   <input type= "button" align="center" value="&nbsp;Continue&nbsp;" 
								   			onclick="submitData()" />
								</td>
								<td align="center">
								   <input type = "button" value="&nbsp;Cancel&nbsp;" onclick="javascript:location.href ='plan.do'" />
								</td>
								<td/>
								<% } else { %>
								<td colspan="3" align="center">
								   <input type = "submit" value="&nbsp;Save&nbsp;"  />
								</td>	
								<% } %>		
							</tr>
							</table>				
							
						</td>
					</tr>								
				</table>
				<script language="javascript">									
			   		function submitData() {
			   			document.getElementById("ignoreErrors").value = "true";
			   			document.getElementById("planForm").submit();
			   		}
			  </script>
			</form:form>
		 </div>
		 
	</tmpl:put>
</tmpl:insert>