<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<tmpl:insert template='/common/sitelayout.jsp'>

<% 
	String pageTitle = "Add/Edit Employee Truck Preference";
%>
    <tmpl:put name='title' direct='true'> Operations : Employee : <%=pageTitle%></tmpl:put>

	<tmpl:put name='content' direct='true'>

	<div class="MNM001 subsub or_999">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM001 <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus")) && !"P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))  && !"P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do" class="<% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))  && !"P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Active</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))  && !"P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>		
		
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
			<form:form commandName = "truckPreferenceForm" method="post">
			<form:hidden path="employeeId"/>
			
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Add/Edit Employee Truck Preference</td>
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
								<td>TRUCK 1</td>
								<td>
									<form:select path="truckPref01" onChange="checkDuplicateTruckPreference('', this);">
											<form:option value="" label="Select Truck"/>
											<form:options items="${trucks}" itemLabel="assetNo" itemValue="assetNo" />
									</form:select>
								</td>
								<td>&nbsp;<form:errors path="truckPref01" />&nbsp;</td>
							</tr>
							    <tr>
								<td>TRUCK 2</td>
								<td>
									<form:select path="truckPref02" onChange="checkDuplicateTruckPreference('', this);">
											<form:option value="" label="Select Truck"/>
											<form:options items="${trucks}" itemLabel="assetNo" itemValue="assetNo" />
									</form:select>
								</td>
								<td>&nbsp;<form:errors path="truckPref02" />&nbsp;</td>
							</tr>
							    <tr>
								<td>TRUCK 3</td>
								<td>
									<form:select path="truckPref03" onChange="checkDuplicateTruckPreference('', this);">
											<form:option value="" label="Select Truck"/>
											<form:options items="${trucks}" itemLabel="assetNo" itemValue="assetNo" />
									</form:select>
								</td>
								<td>&nbsp;<form:errors path="truckPref03" />&nbsp;</td>
							</tr>
							    <tr>
								<td>TRUCK 4</td>
								<td>
									<form:select path="truckPref04" onChange="checkDuplicateTruckPreference('', this);">
											<form:option value="" label="Select Truck"/>
											<form:options items="${trucks}" itemLabel="assetNo" itemValue="assetNo" />
									</form:select>
								</td>
								<td>&nbsp;<form:errors path="truckPref04" />&nbsp;</td>
							</tr>
							    <tr>
								<td>TRUCK 5</td>
								<td>
									<form:select path="truckPref05" onChange="checkDuplicateTruckPreference('', this);">
											<form:option value="" label="Select Truck"/>
											<form:options items="${trucks}" itemLabel="assetNo" itemValue="assetNo" />
									</form:select>
								</td>
								<td>&nbsp;<form:errors path="truckPref05" />&nbsp;</td>
							</tr>
							<tr>
								<td colspan="3" align="center">
									 <input type = "submit" value="&nbsp;Save Changes&nbsp;"  />
									 <input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:back();" />
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
	<script>
		function checkDuplicateTruckPreference(target,src){
			var tp1 = document.getElementById('truckPref01');
			var tp2 = document.getElementById('truckPref02');
			var tp3 = document.getElementById('truckPref03');
			var tp4 = document.getElementById('truckPref04');
			var tp5 = document.getElementById('truckPref05');


		}
	</script>
	</tmpl:put>
</tmpl:insert>
<form name="truckpreference" action="employee.do?empstatus=P" method="post">  </form>