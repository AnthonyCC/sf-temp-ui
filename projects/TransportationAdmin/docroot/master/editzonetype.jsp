<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Zone Type</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form:form commandName = "zoneTypeForm" method="post">
			<form:hidden path="zoneTypeId"/>
			
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Add/Edit Zone Type</td>
					</tr>
					<tr>
						<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
					</tr>
					
					<tr>
						<td class="screencontent">
                        <table class="forms1">			  	
							  <tr>
                              <td>Zone Name</td>
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
							 <tr>
							    <td>Driver Max.</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="driverMax" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="driverMax" />
							 	</td>
							 </tr>
                             <tr>
							    <td>Driver Req.</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="driverReq" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="driverReq" />
							 	</td>
							 </tr>
                             <tr>
							    <td>Helper Max.</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="helperMax" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="helperMax" />
							 	</td>
							 </tr>
                               <tr>
							    <td>Helper Req.</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="helperReq" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="helperReq" />
							 	</td>
							 </tr>
                             <tr>
							    <td>Runner Max.</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="runnerMax" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="runnerMax" />
							 	</td>
							 </tr>
                               <tr>
							    <td>Runner Req.</td>
							    <td> 								  
							  	 	<form:input maxlength="50" size="30" path="runnerReq" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="runnerReq" />
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
