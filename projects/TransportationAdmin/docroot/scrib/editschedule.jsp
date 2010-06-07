<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import='com.freshdirect.transadmin.web.model.*' %>
<%@ page import='com.freshdirect.framework.util.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>
<tmpl:put name='yui-lib'>
	<%@ include file='/common/i_yui.jspf'%>	
</tmpl:put>	

<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>
<% 
	String pageTitle = "Edit Schedule";
%>
    <tmpl:put name='title' direct='true'> Operations : Schedule : <%=pageTitle%></tmpl:put>

	<tmpl:put name='content' direct='true'>

	<div class="MNM001 subsub or_999">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM001 ">&nbsp;</div>
			<div class="subtab ">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do" class="">Active</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 ">&nbsp;</div>		
		
			<div class="sub_tableft sub_tabL_MNM001 ">&nbsp;</div>
			<div class="subtab ">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=T" class="">Terminated</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 ">&nbsp;</div>
			
			<div class="sub_tableft sub_tabL_MNM001 activeL">&nbsp;</div>
			<div class="subtab activeT">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=S" class="MNM001">Schedule</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 activeR">&nbsp;</div>
		</div>
	</div>

	<div class="contentroot">		
		<div class="cont_row">

		<br/>	
		<div align="center">
			<form:form commandName = "scheduleForm" method="post">
			
			<% 
				boolean isMassEdit = false;
				WebSchedule _tmpSchedule = (WebSchedule)request.getAttribute("scheduleForm");
				if(_tmpSchedule != null) {
					String[] employeeIds = StringUtil.decodeStrings(_tmpSchedule.getEmployeeIds());
					if(employeeIds != null && employeeIds.length > 1) {
						isMassEdit = true;
					}
				}
			 %>
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle"><span><a href="javascript:showTeamTree()">
                        	<img src="./images/info.gif" border="0" alt="Team" title="Team" />
                        </a></span>Add/Edit Schedule</td>
					</tr>
					<tr>
						<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
					</tr>
					<tr>
						<td class="screencontent">
							<form:hidden path="employeeIds"/>
							
							<table class="forms1">
							  <tr>
							    <td>Week Of</td>
							    <td> 
								  	 <form:input maxlength="50" size="30" path="weekOf" readOnly="true" />
							 	</td>
							 	<td>
							 		&nbsp;<form:errors path="empInfo.lastName" />
							 	</td>
							 </tr>
							 <c:choose>	
							<c:when test="${scheduleForm.isMultiEdit}">
							 	
							 	<form:hidden path="empInfo.employeeId"/>
							 	<form:hidden path="empInfo.firstName"/>
							 	<form:hidden path="empInfo.lastName"/>		
							 </c:when>
							 
							 	<c:otherwise>	  	
								  <tr>
								    <td>Kronos ID</td>
								    <td> 								  
								  	 	<form:input maxlength="50" size="30" path="empInfo.employeeId" readOnly="true" />
								 	</td>
								 	<td>
								 		&nbsp;<form:errors path="empInfo.employeeId" />
								 	</td>
								 </tr>
								 <tr valign="top">
								    <td>First Name</td>
								    <td> 
									   <form:input maxlength="50" size="30" path="empInfo.firstName" readOnly="true" />
								 	</td>
								 	<td>
								 		&nbsp;<form:errors path="empInfo.firstName" />
								 	</td>
								 </tr>
								 <tr>
								    <td>Last Name</td>
								    <td> 
									  	 <form:input maxlength="50" size="30" path="empInfo.lastName" readOnly="true" />
								 	</td>
								 	<td>
								 		&nbsp;<form:errors path="empInfo.lastName" />
								 	</td>
								 </tr>	
								 </c:otherwise>
							 </c:choose>	 
							 <tr>
							 
							 <td colspan="3">
									<table>
									<tr><td width="60">&nbsp;</td><td>Region</td><td>Time</td><td>Depot Zone*</td></tr>
									<tr>
										<td width="60">Mon</td>
										<td>
										<form:hidden path="mon.scheduleId"/>
										<form:hidden path="mon.weekOf"/>
										<form:select path="mon.regionS" cssStyle="width:100px" onchange="javascript:disableTimeZone('mon')">
											<form:option value="" label="Select"/>
											<form:option value="OFF" label="OFF"/>
											<form:options items="${region}" itemLabel="name" itemValue="code" />
										</form:select>
										</td><td><form:input maxlength="10" size="6" path="mon.timeS" onblur="this.value=time(this.value);" /></td>
										<td>
										<form:select path="mon.depotZoneS" cssStyle="width:180px">
												<form:option value="" label="Select"/>
												<form:options items="${zone}" itemLabel="displayName" itemValue="zoneCode" />
										</form:select>
										</td>
										<td><form:errors path="mon.regionS" />&nbsp&nbsp;&nbsp;<form:errors path="mon.timeS" />&nbsp&nbsp;&nbsp;<form:errors path="mon.depotZoneS" /></td>
									</tr>
									<tr>
										<td width="60">Tue</td>
										<td>
										<form:hidden path="tue.scheduleId"/>
										<form:hidden path="tue.weekOf"/>
										<form:select path="tue.regionS" cssStyle="width:100px" onchange="javascript:disableTimeZone('tue')">
											<form:option value="" label="Select"/>
											<form:option value="OFF" label="OFF"/>
											<form:options items="${region}" itemLabel="name" itemValue="code" />
										</form:select>
										</td><td><form:input maxlength="10" size="6" path="tue.timeS" onblur="this.value=time(this.value);"/></td>
										<td>
										<form:select path="tue.depotZoneS" cssStyle="width:180px">
												<form:option value="" label="Select"/>
												<form:options items="${zone}" itemLabel="displayName" itemValue="zoneCode" />
										</form:select>
										</td>
										<td><form:errors path="tue.regionS" />&nbsp&nbsp;&nbsp;<form:errors path="tue.timeS" />&nbsp&nbsp;&nbsp;<form:errors path="tue.depotZoneS" /></td>
									</tr>
									<tr>
										<td width="60">Wed</td>
										<td>
										<form:hidden path="wed.scheduleId"/>
										<form:hidden path="wed.weekOf"/>
										<form:select path="wed.regionS" cssStyle="width:100px" onchange="javascript:disableTimeZone('wed')">
											<form:option value="" label="Select"/>
											<form:option value="OFF" label="OFF"/>
											<form:options items="${region}" itemLabel="name" itemValue="code" />
										</form:select>
										</td><td><form:input maxlength="10" size="6" path="wed.timeS" onblur="this.value=time(this.value);"/></td>
										<td>
										<form:select path="wed.depotZoneS" cssStyle="width:180px">
												<form:option value="" label="Select"/>
												<form:options items="${zone}" itemLabel="displayName" itemValue="zoneCode" />
										</form:select>
										</td>
										<td><form:errors path="wed.regionS" />&nbsp&nbsp;&nbsp;<form:errors path="wed.timeS" />&nbsp&nbsp;&nbsp;<form:errors path="wed.depotZoneS" /></td>
									</tr>
									<tr>
										<td width="60">Thu</td>
										<td>
										<form:hidden path="thu.scheduleId"/>
										<form:hidden path="thu.weekOf"/>
										<form:select path="thu.regionS" cssStyle="width:100px" onchange="javascript:disableTimeZone('thu')">
											<form:option value="" label="Select"/>
											<form:option value="OFF" label="OFF"/>
											<form:options items="${region}" itemLabel="name" itemValue="code" />
										</form:select>
										</td><td><form:input maxlength="10" size="6" path="thu.timeS" onblur="this.value=time(this.value);"/></td>
										<td>
										<form:select path="thu.depotZoneS" cssStyle="width:180px">
												<form:option value="" label="Select"/>
												<form:options items="${zone}" itemLabel="displayName" itemValue="zoneCode" />
										</form:select>
										</td>
										<td><form:errors path="thu.regionS" />&nbsp&nbsp;&nbsp;<form:errors path="thu.timeS" />&nbsp&nbsp;&nbsp;<form:errors path="thu.depotZoneS" /></td>
									</tr>
									<tr>
										<td width="60">Fri</td>
										<td>
										<form:hidden path="fri.scheduleId"/>
										<form:hidden path="fri.weekOf"/>
										<form:select path="fri.regionS" cssStyle="width:100px" onchange="javascript:disableTimeZone('fri')">
											<form:option value="" label="Select"/>
											<form:option value="OFF" label="OFF"/>
											<form:options items="${region}" itemLabel="name" itemValue="code" />
										</form:select>
										</td><td><form:input maxlength="10" size="6" path="fri.timeS" onblur="this.value=time(this.value);"/></td>
										<td>
										<form:select path="fri.depotZoneS" cssStyle="width:180px">
												<form:option value="" label="Select"/>
												<form:options items="${zone}" itemLabel="displayName" itemValue="zoneCode" />
										</form:select>
										</td>
										<td><form:errors path="fri.regionS" />&nbsp&nbsp;&nbsp;<form:errors path="fri.timeS" />&nbsp&nbsp;&nbsp;<form:errors path="fri.depotZoneS" /></td>
									</tr>
									<tr>
										<td width="60">Sat</td>
										<td>
										<form:hidden path="sat.scheduleId"/>
										<form:hidden path="sat.weekOf"/>
										<form:select path="sat.regionS" cssStyle="width:100px" onchange="javascript:disableTimeZone('sat')">
											<form:option value="" label="Select"/>
											<form:option value="OFF" label="OFF"/>
											<form:options items="${region}" itemLabel="name" itemValue="code" />
										</form:select>
										</td><td><form:input maxlength="10" size="6" path="sat.timeS" onblur="this.value=time(this.value);"/></td>
										<td>
										<form:select path="sat.depotZoneS" cssStyle="width:180px">
												<form:option value="" label="Select"/>
												<form:options items="${zone}" itemLabel="displayName" itemValue="zoneCode" />
										</form:select>
										</td>
										<td><form:errors path="sat.regionS" />&nbsp&nbsp;&nbsp;<form:errors path="sat.timeS" />&nbsp&nbsp;&nbsp;<form:errors path="sat.depotZoneS" /></td>
									</tr>
									<tr>
										<td width="60">Sun</td>
										<td>
										<form:hidden path="sun.scheduleId"/>
										<form:hidden path="sun.weekOf"/>
										<form:select path="sun.regionS" cssStyle="width:100px" onchange="javascript:disableTimeZone('sun')">
											<form:option value="" label="Select"/>
											<form:option value="OFF" label="OFF"/>
											<form:options items="${region}" itemLabel="name" itemValue="code" />
										</form:select>
										</td><td><form:input maxlength="10" size="6" path="sun.timeS" onblur="this.value=time(this.value);"/></td>
										<td>
										<form:select path="sun.depotZoneS" cssStyle="width:180px">
												<form:option value="" label="Select"/>
												<form:options items="${zone}" itemLabel="displayName" itemValue="zoneCode" />
										</form:select>
										</td>
										<td><form:errors path="sun.regionS" />&nbsp&nbsp;&nbsp;<form:errors path="sun.timeS" />&nbsp&nbsp;&nbsp;<form:errors path="sun.depotZoneS" /></td>
									</tr>
									
									</table>
							 </td>
							 </tr>	
							 <tr>
							    <td colspan="3" align="center">
								   <input type = "submit" value="&nbsp;Save&nbsp;"  />										
								
							   <input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:back();" />
							   <input type = "button" value="Copy From Master" onclick="javascript:copyFromMaster();" />
							   <input type = "button" value="Copy To Master" onclick="javascript:copyToMaster();" /> 
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
		 <%@ include file='i_schedulecopy.jspf'%>
		 <%@ include file='i_teamtree.jspf'%>
	</tmpl:put>
	 
</tmpl:insert>
<script>
		
	function disableTimeZone(day) {
		var f=document.forms["scheduleForm"];	
		var value=eval("f['"+day+".regionS'].value");
		if(value=='OFF') {	   
			eval("f['"+day+".timeS'].disabled=true");
			eval("f['"+day+".timeS'].value=''");
			eval("f['"+day+".depotZoneS'].disabled=true");
			eval("f['"+day+".depotZoneS'].value=''");
		}
		else {
			eval("f['"+day+".timeS'].disabled=false");	
			if(value=='Depot') {
				eval("f['"+day+".depotZoneS'].disabled=false");
			}
			else {
				eval("f['"+day+".depotZoneS'].value=''");
				eval("f['"+day+".depotZoneS'].disabled=true");
			}
		}
	}
	disableTimeZone("mon");
	disableTimeZone("tue");
	disableTimeZone("wed");
	disableTimeZone("thu");
	disableTimeZone("fri");
	disableTimeZone("sat");
	disableTimeZone("sun");
	
	function back() {
	  	var filters=unescape(getParameter("filter"));	  	    	
	  	var params=filters.split("&");
	  	var planForm=document.forms["employee"];
	  	for(var i=0;i<params.length;i++) {
	  		var param=params[i].split("="); 
	  		if(param[0] != 'ec_f_employeeId') {        				
	  			add_input(planForm,"hidden",param[0],param[1]);
	  		}
	  	}     	      	
	  	planForm.submit();
	}
</script>
<form name="employee" action="employee.do?empstatus=S" method="post">  </form>