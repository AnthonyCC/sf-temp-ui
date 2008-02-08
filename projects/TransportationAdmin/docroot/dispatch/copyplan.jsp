<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<% boolean hasErrors = session.getAttribute("apperrors") != null; %>

<tmpl:insert template='/common/site.jsp'>

    <tmpl:put name='title' direct='true'>Copy Planning Data</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form:form commandName = "copyPlanForm" method="post">
			<form:hidden path="ignoreErrors"/>	
			<form:hidden path="errorSourceDate"/>
			<form:hidden path="errorDestinationDate"/>		
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Copy Planning Data</td>
					</tr>
					<tr>
						<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
					</tr>
					
					<tr>
						<td class="screencontent">
							<table class="forms1">	
							  <tr>
							    <td>Source Week Of</td>
							    <td>
							    <form:input maxlength="50" size="30" path="sourceDate" />
							    &nbsp;<a href="#" id="trigger_sourceDate" style="font-size: 9px;">
							  	 			<img src="images/calendar.gif"  style="border:0"  alt=">>" />
							  	 		  </a>
							  	 	 <script language="javascript">									
									    Calendar.setup(
									    {
										    showsTime : false,
										    electric : false,
										    inputField : "sourceDate",
										    ifFormat : "%Y-%m-%d",
										    singleClick: true,  	                                        
										    button : "trigger_sourceDate" 
										   }
									    );
									   </script> 
								</td>	   
							 	<td>
							 		&nbsp;<form:errors path="sourceDate" />
							 	</td>
							 </tr>
							 			  	
							  <tr>
							    <td>Source Day</td>
							    <td>
								    <form:select path="dispatchDay">
								  	  		<form:option value="null" label="--Please Select Day"/>
											<form:options items="${days}" />
								     </form:select>
							     </td>
							 	<td>
							 		&nbsp;<form:errors path="dispatchDay" />
							 	</td>
							 </tr>
							 
							 <tr>
							    <td>Destination Week Of</td>
							    <td>
							    <form:input maxlength="50" size="30" path="destinationDate" />
							    &nbsp;<a href="#" id="trigger_destinationDate" style="font-size: 9px;">
							  	 			<img src="images/calendar.gif"  style="border:0"  alt=">>" />
							  	 		  </a>
							  	 	 <script language="javascript">									
									    Calendar.setup(
									    {
										    showsTime : false,
										    electric : false,
										    inputField : "destinationDate",
										    ifFormat : "%Y-%m-%d",
										    singleClick: true,  	                                        
										    button : "trigger_destinationDate" 
										   }
									    );
									  </script>
								</td>	  
							 	<td>
							 		&nbsp;<form:errors path="destinationDate" />
							 	</td>
							 </tr>
							
							<tr>
							    <td>Include Employees</td>
							    <td>
							    <form:checkbox path="includeEmployees" value="true"/>
							    </td>
							 	<td>
							 		&nbsp;<form:errors path="includeEmployees" />
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
							<script language="javascript">									
			   		function submitData() {
			   			document.getElementById("ignoreErrors").value = "true";
			   			document.getElementById("copyPlanForm").submit();
			   		}
			  </script>
						</td>
					</tr>								
				</table>
			
			</form:form>
		 </div>
		 
	</tmpl:put>
</tmpl:insert>