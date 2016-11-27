<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<tmpl:insert template='/common/sitelayout.jsp'>

<% 
	String pageTitle = "Add/Edit Employee";
%>
    <tmpl:put name='title' direct='true'> Operations : Employee : <%=pageTitle%></tmpl:put>

	<tmpl:put name='content' direct='true'>

	<div class="MNM001 subsub ">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM001 <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do" class="<% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Active</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>		
		
			<div class="sub_tableft sub_tabL_MNM001 <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=T" class="<% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Terminated</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>
			
			<div class="sub_tableft sub_tabL_MNM001 <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=S" class="<% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Schedule</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>
			
			<div class="sub_tableft sub_tabL_MNM001 <% if("C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=C" class="<% if("C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Team</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>

			<div class="sub_tableft sub_tabL_MNM001 <% if("P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=P" class="<% if("P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Preference</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>
		</div>
	</div>

	<div class="contentroot">		
		<div class="cont_row">

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
							  	 	<form:input maxlength="50" size="30" path="employeeId" readOnly="true" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="employeeId" />
							 	</td>
							 </tr>
							 <tr valign="top">
							    <td>First Name</td>
							    <td> 
								   <form:input maxlength="50" size="30" path="firstName" readOnly="true" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="firstName" />
							 	</td>
							 </tr>
							 <tr>
							    <td>Last Name</td>
							    <td> 
								  	 <form:input maxlength="50" size="30" path="lastName" readOnly="true" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="lastName" />
							 	</td>
							 </tr>
							 <tr>
							 	<td>Home Supervisor</td>
								<td> 
									<form:select path="homeSupervisorId">
										<form:option value="" label="--Please Select Supervisor"/>
										<form:options items="${supervisors}" itemLabel="name" itemValue="employeeId" />
									</form:select>
								</td>
								<td><form:errors path="homeSupervisorId" />&nbsp;</td>   
							</tr>
							<tr>
								<td>Home Region</td>
								<td> 
									<form:select path="empSupervisor.homeRegion">
										<form:option value="" label="--Please Select Region"/>
										<form:options items="${regions}" itemLabel="code" itemValue="code" />
									</form:select>
								</td>
								<td><form:errors path="empSupervisor.homeRegion" />&nbsp;</td>   
							</tr>
							<tr>
								<td>Home Shift</td>
								<td> 
									<form:select path="empSupervisor.homeShift">
										<form:option value="" label="--Please Select Shift"/>
										<form:options items="${shifts}" itemLabel="name" itemValue="name" />
									</form:select>
								</td>
								<td><form:errors path="empSupervisor.homeShift" />&nbsp;</td>   
							</tr>				                                                            
                             <tr>
							    <td>Sub-Role</td>
							    <td> 	
                              <spring:bind path="employeeForm.employeeRoleTypes">
                                   <SELECT name="employeeRoleTypes" >     
                                      <OPTION value="">Please select roles</OPTION>          
                                      <c:forEach  var="role" items="${roleTypes}"  >
                                       <c:set var="avail" value="true"/>
                                         <c:forEach var="irole" items="${employeeForm.employeeRoleTypes}">         
                                        <c:if test="${irole.code == role.code}">
                                          <OPTION selected="<c:out value="${role.code}"/>" value="<c:out value="${role.code}"/>"><c:out value="${role.name}"/></OPTION>
                                          <c:remove var="avail"/>
                                        </c:if>       
                                        </c:forEach>
                                        <c:if test="${avail}">
                                          <OPTION value="<c:out value="${role.code}"/>"><c:out value="${role.name}"/></OPTION>          
                                        </c:if>  
                                      </c:forEach>
                                    </SELECT>
                                  </spring:bind>                
                               </td>
							 	<td>
							 		&nbsp;<form:errors path="empRole" />
							 	</td>
							 </tr>							 
							 <tr>
							    <td>Team Lead</td>
							    <td> 
							    	 <form:select path="leadId">
                        				<form:option value="null" label="--Please Select Team Lead"/>
                    					<form:options items="${employees}" itemLabel="name" itemValue="employeeId" />
                   					</form:select>	
                                         
                               </td>
							 	<td>
							 		&nbsp;<form:errors path="leadId" />
							 	</td>
							 </tr>
							 
							 <tr>
							    <td>Status</td>
							    <td> 
								  	 <form:input maxlength="50" size="30" path="trnStatus1" readOnly="true" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="lastName" />
							 	</td>
							 </tr>								 	  	
							<tr><td colspan="3">&nbsp;</td></tr>
							<tr>
							<td colspan="3" align="center">
								 <input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:doBack('employee');" />
								 <input type = "submit" value="&nbsp;Save Changes&nbsp;"  />
							 <%if(com.freshdirect.transadmin.security.SecurityManager.isUserAdminOrPlanning(request)){%>
							
                             <input type = "submit" value="&nbsp;Change Status&nbsp;"  onclick="javascript:setStatus()"/>         
                             
							  <%}%>  
								
								</td>			
							</tr>
							</table>				
							
						</td>
					</tr>								
				</table>
			</form:form>
		</div>

			
		 </div>
		 </div>
		 <form name="employee" action="employee.do" method="post">  </form>
	</tmpl:put>
</tmpl:insert>
<script>
function setStatus()
{	
	new_element = document.createElement("input");
	new_element.setAttribute("type", "hidden");
	new_element.setAttribute("name", "toggle");	
	new_element.setAttribute("value", "true");
	document.forms['employeeForm'].appendChild(new_element);		
}
</script>