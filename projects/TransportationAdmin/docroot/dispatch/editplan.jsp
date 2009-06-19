<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.web.model.WebPlanInfo' %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<% boolean hasErrors = session.getAttribute("apperrors") != null; 
 
%>
<style>
	* {font-family:Arial, Helvetica, sans-serif;
		font-size:9pt;}
		
	/* table list */
	.table_list {border-collapse:collapse;
		border:solid #cccccc 1px;
		width:100%;}
	
	.table_list td {padding:5px;
		border:solid #efefef 1px;}
	
	.table_list th {background:#75b2d1;
		padding:5px;
		color:#ffffff;}
	
	.table_list tr.odd {background:#e1eff5;}
	
	.time_picker_div {padding:5px;
		border:solid #999999 1px;
		background:#ffffff;}
		
</style>
<style>
        .time_picker_div {padding:5px;
            border:solid #999999 1px;
            background:#ffffff;}
      </style>
    

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Plan</tmpl:put>

	<tmpl:put name='content' direct='true'>
	<br/> 
	<div align="center">
		<form:form commandName = "planForm" method="post">
		<form:hidden path="planId"/>
		<form:hidden path="ignoreErrors"/>
		<form:hidden path="errorDate"/>
		<form:hidden path="zoneModified"/>
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="screentitle">Add/Edit Plan</td>
			</tr>
			<tr>
				<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
			</tr>      
			<tr>
				<td class="screencontent">
					<table class="forms1" border="0">  
						<tr>
							<td>Date</td>
							<td>
								<form:input maxlength="50" size="24" path="planDate" />&nbsp;
								<a href="#" id="trigger_planDate" style="font-size: 9px;">
									<img src="images/calendar.gif" style="border: 0;" alt=">>" />
								</a>
                     
							</td>   
							<td>
								<form:errors path="planDate" />&nbsp;
							</td>
						</tr>  
						<tr>
							<td>Zone</td>
							<td> 
								<c:if test="${!empty planForm.zoneCode }">
									<c:set var="hasZone" value="true"/>
								</c:if>
								<c:if test="${planForm.isBullpen eq 'Y' }">
									<c:set var="_isBullpen" value="true"/>
								</c:if>
								<form:select path="zoneCode" disabled="${_isBullpen}" onChange="zoneChanged()">
									<form:option value="null" label="--Please Select Zone"/>
									<form:options items="${zones}" itemLabel="displayName" itemValue="zoneCode" />
								</form:select>
							</td>
							<td><form:errors path="zoneCode" />&nbsp;</td>
						</tr>
						<tr>
							<td>Region</td>
							<td> 
								<form:select path="regionCode" disabled="${hasZone}">
									<form:option value="null" label="--Please Select Region"/>
									<form:options items="${regions}" itemLabel="code" itemValue="code" />
								</form:select>
							</td>
							<td><form:errors path="regionCode" />&nbsp;</td>
						</tr>  
						<tr>
							<td>Bullpen</td>         
							<td colspan="2"><form:checkbox path="isBullpen" value="Y" onclick="bullpen()"/></td>
						</tr>
						<tr>
							<td>Supervisor</td>
							<td> 
								<form:select path="supervisorCode">
									<form:option value="null" label="--Please Select Supervisor"/>
									<form:options items="${supervisors}" itemLabel="name" itemValue="employeeId" />
								</form:select>
							</td>
							<td><form:errors path="supervisorCode" />&nbsp;</td>
						</tr>
						<tr>
							<td><a id="timeStart_toggler">Start&nbsp;Time</a></td>
							<td>         
								<form:input maxlength="50" size="24" path="startTime" onblur="this.value=time(this.value);" /> 
							</td>
							<td><form:errors path="startTime" />&nbsp;</td>                 
						</tr>   
						<tr>
							<td><a id="timeFirstDlv_toggler">First Dlv.&nbsp;Time</a></td>
							<td>     
								<form:input maxlength="50" size="24" path="firstDeliveryTime" onblur="this.value=time(this.value);" />             
							</td>
							<td><form:errors path="firstDeliveryTime" />&nbsp;</td>                 
						</tr>
						<tr>
							<td>Drivers (Req:<spring:bind path="planForm.driverReq"><c:out value="${planForm.driverReq}"/></spring:bind> Max: <spring:bind path="planForm.driverMax"><c:out value="${planForm.driverMax}"/></spring:bind>)</td>
							<td> 
								<div class="fleft">
									<c:forEach items="${planForm.drivers}" var="driver" varStatus="gridRow">
										<div class="dipatch_AddEdit_row">
											<spring:bind path="planForm.drivers[${gridRow.index}].employeeId">
												<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" >
													<option value="">Select Drivers</option>          
													<c:forEach var="driverEmp" items="${drivers}">
														<option <c:if test='${status.value == driverEmp.employeeId}'> SELECTED </c:if> value="<c:out value="${driverEmp.employeeId}"/>"><c:out value="${driverEmp.name}"/></option>                         
													</c:forEach>
												</SELECT>
											</spring:bind>
										</div>
									</c:forEach>
								</div>
							</td>
							<td><form:errors path="drivers" />&nbsp;</td>
						</tr>
						<tr>
							<td>Helpers (Req:<spring:bind path="planForm.helperReq"><c:out value="${planForm.helperReq}"/></spring:bind> Max: <spring:bind path="planForm.helperMax"><c:out value="${planForm.helperMax}"/></spring:bind>)</td>
							<td>
								<div class="fleft">	
									<c:forEach items="${planForm.helpers}" var="helper" varStatus="gridRow">
										<div class="dipatch_AddEdit_row">
											<spring:bind path="planForm.helpers[${gridRow.index}].employeeId">
												<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" >
													<OPTION value="">Select Helpers</OPTION>          
													<c:forEach var="helperEmp" items="${helpers}">
														<option <c:if test='${status.value == helperEmp.employeeId}'> SELECTED </c:if> value="<c:out value="${helperEmp.employeeId}"/>"><c:out value="${helperEmp.name}"/></option>                         
													</c:forEach>
												</SELECT>
											</spring:bind>
										</div>
									</c:forEach>
								</div>
							</td>
							<td><form:errors path="helpers" />&nbsp;</td>
						</tr>               
						<tr>
							<td>Runners (Req:<spring:bind path="planForm.runnerReq"><c:out value="${planForm.runnerReq}"/></spring:bind> Max: <spring:bind path="planForm.runnerMax"><c:out value="${planForm.runnerMax}"/></spring:bind>)</td>
							<td> 	
								<div class="fleft">	
									<c:forEach items="${planForm.runners}" var="helper" varStatus="gridRow">
										<div class="dipatch_AddEdit_row">
											<spring:bind path="planForm.runners[${gridRow.index}].employeeId">
												<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" >
													<OPTION value="">Select Runners</OPTION>          
													<c:forEach var="runnerEmp" items="${runners}">
														<option <c:if test='${status.value == runnerEmp.employeeId}'> SELECTED </c:if> value="<c:out value="${runnerEmp.employeeId}"/>"><c:out value="${runnerEmp.name}"/></option>
													</c:forEach>
												</SELECT>
											</spring:bind>
										</div>
									</c:forEach>
								</div>
							</td>
							<td><form:errors path="runners" />&nbsp;</td>
						</tr>   
						<tr><td colspan="3">&nbsp;</td></tr>
						<tr>
							<td>Sequence</td>
							<td> 								  
								<form:input maxlength="50" size="30" path="sequence" />
							</td>
							<td>
								<form:errors path="sequence" />&nbsp;
							</td>
						</tr>                                           
						<tr>
						<% if(hasErrors) { %>
							<td colspan="3" align="center">
								<input type= "button" align="center" value="&nbsp;Continue&nbsp;" onclick="submitData()" />
								&nbsp;
								<input type = "button" value="&nbsp;Cancel&nbsp;" onclick="javascript:back();" />
							</td>
						<% } else { %>
							<td colspan="3" align="center">
								<input type = "submit" value="&nbsp;Save&nbsp;" />
								<input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:back();" />
							</td> 
						<% } %>   
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form:form>
	</div>
     
	<script language="javascript">             
		Calendar.setup(
			{
				showsTime : false,
				electric : false,
				inputField : "planDate",
				ifFormat : "%m/%d/%Y",
				singleClick: true,                                            
				button : "trigger_planDate",
				onUpdate : updateDate                        
			}
		);
		function updateDate(cal) {
			var selIndex = cal.date.getDay();
			if(selIndex == 0) selIndex = 7;
			// document.getElementById('dispatchDay').selectedIndex =  selIndex;
		};
		function zoneChanged() {
			document.getElementById("zoneModified").value = "true";
			submitData();
		} 
		function submitData() {
			document.getElementById("ignoreErrors").value = "true";
			document.getElementById("planForm").submit();
		}
		function bullpen() {
			if(document.getElementById("isBullpen1").checked) {
				document.getElementById("zoneCode").disabled=true;
				document.getElementById("regionCode").disabled=false;
			} else {
				document.getElementById("zoneCode").disabled=false;
				document.getElementById("regionCode").disabled=true;
			}
			document.getElementById("zoneModified").value = "true";
			document.getElementById("ignoreErrors").value = "true";
			document.getElementById("planForm").submit();
		}
		function back()
	    {
	      	var filters=unescape(getParameter("filter"));      	
	      	var params=filters.split("&");
	      	var planForm=document.forms["plan"];
	      	for(var i=0;i<params.length;i++)
	      	{
	      		var param=params[i].split("=");         				
	      		add_input(planForm,"hidden",param[0],param[1]);
	      	}     	      	
	      	planForm.submit();
	    }
	</script>
  </tmpl:put>
</tmpl:insert>
<form name="plan" action="plan.do" method="post">  </form>