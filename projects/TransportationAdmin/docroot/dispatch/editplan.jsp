<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.web.model.WebPlanInfo' %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<% 
	boolean hasErrors = session.getAttribute("apperrors") != null;
%>
<script src="js/jsonrpc.js" language="javascript" type="text/javascript"></script>
<script src="js/activeZone.js" language="javascript" type="text/javascript"></script>
<script src="js/resourceedit.js" language="javascript" type="text/javascript"></script>
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
		
	.time_picker_div {
		padding:5px;
            border:solid #999999 1px;
        background:#ffffff;
	}
      </style>
    
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Add/Edit Plan</tmpl:put>
	<tmpl:put name='content' direct='true'>
	<br/> 
	<div align="center">
		<form:form commandName = "planForm" method="post">
		<form:hidden path="planId"/>
		<form:hidden path="referenceContextId"/>
		<form:hidden path="isTeamOverride"/>
		<form:hidden path="ignoreErrors"/>
		<form:hidden path="errorDate"/>
		<form:hidden path="zoneModified"/>
		<form:hidden path="dispatchGroupModified"/>		
		<form:hidden path="destFacilityModified"/>
		
		<table width="100%" height="75%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="screentitle">Add/Edit Plan</td>
			</tr>
			<tr><td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td></tr>
			<tr>
				<td class="screencontent">
					<table class="forms1" style="height:100%;width:95%;border:1px dotted;background-color:#F7F7F7;">
						<tr>
							<td align="center" valign="top">
								<table>  
									<tr>
							<td>Date</td>
							<td>
								<form:input maxlength="50" size="24" path="planDate" onchange="javascript:getActiveZoneInfo(this.value,planForm.zoneCode)" />&nbsp;
								<a href="#" id="trigger_planDate" style="font-size: 9px;">
									<img src="images/calendar.gif" style="border: 0;" alt=">>" />
								</a>
							</td>   
										<td><form:errors path="planDate" />&nbsp;</td>
									</tr>
						<tr>
									<td>Origin Facility</td>
									<td>
											<form:select path="originFacility" onChange="showZoneSelection(this, planForm.destinationFacility)">
												<form:option value="" label="--Please Select Origin Facility"/>
												<form:options items="${trnFacilitys}" itemLabel="name" itemValue="facilityId" />
											</form:select> 
									</td>
									<td><form:errors path="originFacility" />&nbsp;</td>
						</tr>  
						<tr>
									<td>Destination Facility</td>
									<td> 
											<form:select path="destinationFacility" onChange="showZoneSelection(planForm.originFacility, this)">
												<form:option value="" label="--Please Select Destination Facility"/>
												<form:options items="${trnFacilitys}" itemLabel="name" itemValue="facilityId" />
											</form:select> 
									</td>
									<td><form:errors path="destinationFacility" />&nbsp;</td>
						</tr>
						<tr>
							<td>Zone</td>
							<td> 
								<c:if test="${!empty planForm.zoneCode }">
									<c:set var="hasZone" value="true"/>
								</c:if>
											<c:if test="${(planForm.isBullpen eq 'Y') || (planForm.destinationFacility.trnFacilityType.name ne 'SIT' and planForm.destinationFacility.trnFacilityType.name ne 'DPT')}">
												<c:set var="_disableZone" value="true"/>
								</c:if>
											<form:select path="zoneCode" disabled="${_disableZone}" onChange="zoneChanged()">
									<form:option value="null" label="--Please Select Zone"/>
									<form:options items="${zones}" itemLabel="displayName" itemValue="zoneCode" />
								</form:select>
							</td>
							<td><form:errors path="zoneCode" />&nbsp;</td>
						</tr>
						<tr>
							<td>Equipment Type</td>
							<td>
								<form:select path="equipmentTypeS" disabled="${_disableZone}">
									<form:option value="" label="--Please Select Equipment Type"/>
									<form:options items="${planForm.equipmentTypes}" itemLabel="id" itemValue="id" />
								</form:select>
							</td>
							<td><form:errors path="equipmentTypeS" />&nbsp;</td>
						</tr>
						<tr>
							<td>Region</td>
							<td> 
								<form:select path="regionCode" disabled="${hasZone}">
												<form:option value="" label="--Please Select Region"/>
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
							<td>Dispatch Group&nbsp;Time</td>
							<td>
								<c:if test='${!empty planForm.planId}'>
									<c:set var="hasPlan" value="true"/>
								</c:if>
								<c:choose>          
									<c:when test='${hasPlan}'>     
										<form:select path="dispatchGroupS">
											<form:option value="" label="--Please Select Dispatch Group"/>
											<form:options items="${dispatchGroups}" itemLabel="name" itemValue="groupTime" />
										</form:select>            
									</c:when>
									<c:otherwise>
										<form:select path="dispatchGroupS" onChange="dispatchGroupChanged();">
											<form:option value="" label="--Please Select Dispatch Group" />
											<form:options items="${dispatchGroups}" itemLabel="name" itemValue="groupTime" />
										</form:select>
									</c:otherwise>
								</c:choose>
							</td>
							<td><form:errors path="dispatchGroupS" />&nbsp;</td>                 
						</tr>
						
						<tr>
							<td>Truck Dispatch&nbsp;Time</td>
							<td>         
								<form:input maxlength="50" size="24" path="startTime" onblur="this.value=time(this.value);" /> 
							</td>
							<td><form:errors path="startTime" />&nbsp;</td>                 
						</tr> 
						<tr>
							<td><a id="timeLastDlv_toggler">Truck End&nbsp;Time</a></td>
							<td>
								<form:input maxlength="50" size="24" path="endTime" onblur="this.value=time(this.value);"/>
							</td>
							<td><form:errors path="endTime" />&nbsp;</td>                 
						</tr>
						<tr>
							<td><a id="maxTime_toggler">Max Time</a></td>
							<td>     
								<form:input maxlength="50" size="24" path="maxTime" onblur="this.value=time(this.value);" />             
							</td>
							<td><form:errors path="maxTime" />&nbsp;</td>                 
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
					</table>
				</td>
				<td align="center" valign="top">
					<table>
						<tr>
							<td colspan="2">&nbsp;</td>
							<td align="right">Adjust Time</td>
						</tr>
						<tr>
							<td>Drivers (Req:<spring:bind path="planForm.driverReq"><c:out value="${planForm.driverReq}"/></spring:bind> Max: <spring:bind path="planForm.driverMax"><c:out value="${planForm.driverMax}"/></spring:bind>)</td>
							<td nowrap> 
								<div class="fleft">
									<c:forEach items="${planForm.drivers}" var="driver" varStatus="gridRow">
										<div class="dipatch_AddEdit_row">
											<spring:bind path="planForm.drivers[${gridRow.index}].employeeId">
												<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>"  onchange="enableAdjustmentTime('drivers[<c:out value="${gridRow.index}"/>]', this);">
													<option value="">Select Drivers</option>          
													<c:forEach var="driverEmp" items="${drivers}">
														<option <c:if test='${status.value == driverEmp.employeeId}'> SELECTED </c:if> value="<c:out value="${driverEmp.employeeId}"/>"><c:out value="${driverEmp.name}"/></option>                         
													</c:forEach>
												</SELECT>												
											</spring:bind>
											<input type="text" maxlength="10" size="7" name="drivers[<c:out value="${gridRow.index}"/>].adjustmentTimeS" value="<c:out value="${driver.adjustmentTimeS}"/>" onblur="this.value=time(this.value);" /> 
										</div>
									</c:forEach>
								</div>
							</td>
							<td><form:errors path="drivers" />&nbsp;</td>
						</tr>
						<tr>
							<td>Helpers (Req:<spring:bind path="planForm.helperReq"><c:out value="${planForm.helperReq}"/></spring:bind> Max: <spring:bind path="planForm.helperMax"><c:out value="${planForm.helperMax}"/></spring:bind>)</td>
							<td nowrap>
								<div class="fleft">	
									<c:forEach items="${planForm.helpers}" var="helper" varStatus="gridRow">
										<div class="dipatch_AddEdit_row">
											<spring:bind path="planForm.helpers[${gridRow.index}].employeeId">
												<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>"  onchange="enableAdjustmentTime('helpers[<c:out value="${gridRow.index}"/>]', this);">
													<OPTION value="">Select Helpers</OPTION>          
													<c:forEach var="helperEmp" items="${helpers}">
														<option <c:if test='${status.value == helperEmp.employeeId}'> SELECTED </c:if> value="<c:out value="${helperEmp.employeeId}"/>"><c:out value="${helperEmp.name}"/></option>                         
													</c:forEach>
												</SELECT>
											</spring:bind>
											<input type="text" maxlength="10" size="7" name="helpers[<c:out value="${gridRow.index}"/>].adjustmentTimeS" value="<c:out value="${helper.adjustmentTimeS}"/>" onblur="this.value=time(this.value);" /> 
										</div>
									</c:forEach>
								</div>
							</td>
							<td><form:errors path="helpers" />&nbsp;</td>
						</tr>               
						<tr>
							<td>Runners (Req:<spring:bind path="planForm.runnerReq"><c:out value="${planForm.runnerReq}"/></spring:bind> Max: <spring:bind path="planForm.runnerMax"><c:out value="${planForm.runnerMax}"/></spring:bind>)</td>
							<td nowrap> 	
								<div class="fleft">	
									<c:forEach items="${planForm.runners}" var="helper" varStatus="gridRow">
										<div class="dipatch_AddEdit_row">
											<spring:bind path="planForm.runners[${gridRow.index}].employeeId">
												<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" onchange="enableAdjustmentTime('runners[<c:out value="${gridRow.index}"/>]', this);">
													<OPTION value="">Select Runners</OPTION>          
													<c:forEach var="runnerEmp" items="${runners}">
														<option <c:if test='${status.value == runnerEmp.employeeId}'> SELECTED </c:if> value="<c:out value="${runnerEmp.employeeId}"/>"><c:out value="${runnerEmp.name}"/></option>
													</c:forEach>
												</SELECT>
											</spring:bind>
											<input type="text" maxlength="10" size="7" name="runners[<c:out value="${gridRow.index}"/>].adjustmentTimeS" value="<c:out value="${helper.adjustmentTimeS}"/>" onblur="this.value=time(this.value);" /> 
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
							<td>HandOff&nbsp;Time</td>
							<td> 
								<form:select path="cutOffTime">
									<form:option value="" label="--Please Select HandOff"/>
									<form:options items="${cutoffs}" itemLabel="name" itemValue="cutOffTimeEx" />
								</form:select>
							</td>
							<td><form:errors path="cutOffTime" />&nbsp;</td>
						</tr>    
									</table>
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
								<input type = "button" onclick="validateForm();" value="&nbsp;Save&nbsp;" />
								<input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:back();" />
							</td> 
						<% } %>   
						</tr>
					</table>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
		</table>
	</form:form>
	</div>
     
	<script language="javascript">             
		var jsonrpcClient = new JSONRpcClient("dispatchprovider.ax");

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
		};

		function validateForm(){
			var _starttime = document.getElementById('startTime').value;
	     	var _endtime = document.getElementById('endTime').value;
	     			
			if(_starttime.trim().length > 0 && _endtime.trim().length > 0 && checkTime(_endtime, _starttime)) 
					if(!confirm("Truck end time is before truck dispatch time. The end time will be considered past midnight.")){
						return;
					}
			submitData();
		}
		
		function showZoneSelection(originRefVar, destRefVar) {
			
			var originRef = originRefVar.value || '';
			var destRef = destRefVar.value || '';
			
			var result = jsonrpcClient.AsyncDispatchProvider.getFacilityInfo(sendFormCallback, originRef, destRef); 
			
			function sendFormCallback(result, exception) {
			  if(exception) {
				  alert('Unable to connect to host system. Please contact system administrator!');               
				  return;
			  }

			  if( result[0] === 'SIT'){
				  alert('Origin facility cannot be delivery zone.');
				  originRefVar.selectedIndex = 0; return;
			  } else if( result[1] === 'PLANT'){
				  alert('Destination facility cannot be main plant.');
				  destRefVar.selectedIndex = 0; return;
			  } else if((result[1] === result[0]) && (originRef != '' && destRef != '')){
				  alert('Both origin & desination facility cannot be same.');
				  originRefVar.selectedIndex = 0;
				  destRefVar.selectedIndex = 0; return;
			  } else {
					if( result[1] === 'SIT' ||  result[1] === 'DPT'  ){
						document.getElementById("zoneCode").disabled=false;

						if(document.getElementById("isBullpen1").checked){
							document.getElementById("regionCode").disabled=false;
							document.getElementById("zoneCode").disabled=true;
							document.getElementById("zoneCode").selectedIndex=0;
						}
					}else{
						document.getElementById("zoneCode").disabled=true;
						document.getElementById("regionCode").disabled=false;
						document.getElementById("regionCode").selectedIndex=0;
						document.getElementById("zoneCode").selectedIndex=0;
					}
			  }
			  document.getElementById("destFacilityModified").value = "true";
			  submitData();
			}
		}
		
		function zoneChanged() {
			document.getElementById("zoneModified").value = "true";
			submitData();
		}
		function  dispatchGroupChanged() {
			document.getElementById("dispatchGroupModified").value = "true";
			submitData();
		}
		function submitData() {
			document.getElementById("ignoreErrors").value = "true";
			document.getElementById("planForm").submit();
		}
		function bullpen() {
			
			if(document.getElementById("isBullpen1").checked) {
				$('#zoneCode').attr('disabled', true);
				$('#regionCode').attr('disabled', false);
				$('#destinationFacility')[0].selectedIndex = 0;
				$('#originFacility')[0].selectedIndex = 0;
				
				var regionCode = $('#regionCode').val() || '';
				jsonrpcClient.AsyncDispatchProvider.getRegionFacility(regionFacilityCallback, regionCode);
				
			} else {
				$('#zoneCode').attr('disabled', false);
				$('#regionCode').attr('disabled', true);
			}
		}
		
		function regionFacilityCallback(result, exception) {
			if(exception) {
				alert('Unable to connect to host system. Please contact system administrator!');               
				return;
			}			
			
			$("#originFacility").val( result ).prop('selected',true);
			
			$('#zoneModified').val('true');
			$('#ignoreErrors').val('true');
			$('#planForm').submit();
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
	    function enableAdjustmentTime(target, src)
		{
			var f=document.forms["planForm"];	
			var value=eval("f['"+target+".employeeId']");
			if(value!=null)			
			{
				if(value.value!='')
				{
					eval("f['"+target+".adjustmentTimeS'].disabled=false");
				}
				else
				{
					eval("f['"+target+".adjustmentTimeS'].disabled=true");
					eval("f['"+target+".adjustmentTimeS'].value=''");
				}
			}
			resoureChangeEvent(src, 'P', document.getElementById('planDate'), document.getElementById('planId'));	
		}
	    function disableAdjustmentTime()
		{
			var f=document.forms["planForm"];	
			for(var i=0;i<15;i++)
			{
				var value=eval("f['drivers["+i+"].employeeId']");
				if(value!=null&&value.value=='')
				{
					eval("f['drivers["+i+"].adjustmentTimeS'].disabled=true");
				}
			}	
			for(var i=0;i<15;i++)
			{
				var value=eval("f['helpers["+i+"].employeeId']");
				if(value!=null&&value.value=='')
				{
					eval("f['helpers["+i+"].adjustmentTimeS'].disabled=true");
				}
			}	
			for(var i=0;i<15;i++)
			{
				var value=eval("f['runners["+i+"].employeeId']");
				if(value!=null&&value.value=='')
				{
					eval("f['runners["+i+"].adjustmentTimeS'].disabled=true");
				}
			}			
		}
		disableAdjustmentTime();
	</script>
  </tmpl:put>
</tmpl:insert>
<form name="plan" action="plan.do" method="post">  </form>