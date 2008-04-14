<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/site.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Employee</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form:form commandName = "employeeForm" method="post">
			<form:hidden path="employeeId"/>
			
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Add/Edit Employee</td>
					</tr>
					<tr>
						<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
					</tr>
					
					<tr>
						<td class="screencontent">
							<table class="forms1">			  	
							  <tr>
							    <td>Kronos ID</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="employeenumber" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="employeenumber" />
							 	</td>
							 </tr>
							 <tr valign="top">
							    <td>First Name</td>
							    <td> 
								   <form:input maxlength="50" size="30" path="firstName" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="firstName" />
							 	</td>
							 </tr>
							 <tr>
							    <td>Last Name</td>
							    <td> 
								  	 <form:input maxlength="50" size="30" path="lastName" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="lastName" />
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
							    <td>Role</td>
							    <td> 
								  <form:select path="employeeJobType">
							  	  		<form:option value="null" label="--Please Select Job Type"/>
										<form:options items="${jobtypes}" itemLabel="jobTypeName" itemValue="jobTypeId" />
							      </form:select>
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="employeeJobType" />
							 	</td>
							 </tr>
							 <tr>
							    <td>Hire Date</td>
							    <td>
							    	<form:input maxlength="50" size="24" path="hireDate" />
							    
							    &nbsp;<a href="#" id="trigger_hireDate" style="font-size: 9px;">
							  	 			<img src="images/calendar.gif"  style="border:0"  alt=">>" />
							  	 		  </a>
							  	 	 <script language="javascript">									
									    Calendar.setup(
									    {
										    showsTime : false,
										    electric : false,
										    inputField : "hireDate",
										    ifFormat : "%m/%d/%Y",
										    singleClick: true,  	                                        
										    button : "trigger_hireDate"										     
										 }
									    );									    
									  </script>
								</td>	  
							 	<td>
							 		&nbsp;<form:errors path="hireDate" />
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