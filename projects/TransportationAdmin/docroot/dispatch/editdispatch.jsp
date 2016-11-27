<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%
 String id = request.getParameter("id") != null ? request.getParameter("id") : "";
 String dispDate = request.getParameter("dispDate") != null ? request.getParameter("dispDate") : "";
 %>
 <script src="js/jsonrpc.js" language="javascript" type="text/javascript"></script>
 <script src="js/activeZone.js" language="javascript" type="text/javascript"></script>
 <script src="js/resourceedit.js" language="javascript" type="text/javascript"></script>
        
	<style>
        .time_picker_div {
			padding:5px;
            border:solid #999999 1px;
            background:#ffffff;
            }
      	  
		* {font-family:Arial, Helvetica, sans-serif;
		font-size:9pt;}
    </style>
         
<tmpl:insert template='/common/sitelayout.jsp'>
    <tmpl:put name='title' direct='true'> Operations : Dispatch : Add/Edit Dispatch</tmpl:put>
  <tmpl:put name='content' direct='true'>
    <br/> 

    <div align="center">
      <form:form commandName = "dispatchForm" method="post">
      <form:hidden path="dispatchId" />
      <form:hidden path="referenceContextId"/>
      <form:hidden path="isTeamOverride"/>
      <input type=hidden name="routeNo" value="" />
      <input type=hidden name="zoneId" value="" />
      <input type=hidden name="dispDate" value="<%=dispDate %>" />
	  <form:hidden path="overrideUser" />
	  <form:hidden path="dispatchGroupModified"/>
      <form:hidden path="destFacilityModified"/>
      <form:hidden path="dispatchTypeModified"/>
      
      <input type=hidden id="curr_gpsNumber" value="" />
      <input type=hidden id="curr_ezpassNumber" value="" />
      <input type=hidden id="curr_motKitNumber" value="" />
      
      <table width="100%" height="80%" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td class="screentitle">Add/Edit Dispatch</td>
		</tr>
		<tr>
			<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
		</tr>
		<tr>
			<td class="screencontent">
				<table class="forms1" style="height:100%;width:95%;border:1px dotted;background-color:#F7F7F7;">
					<tr>
					<td align="center" valign="top" width="50%">
					<table>
						<tr>
							<td width="150">Date</td>
						<td>
								<span><form:input maxlength="50" size="24" path="dispatchDate" onChange="javascript:getRouteInfo();javascript:getActiveZoneInfo(this.value,dispatchForm.zoneCode)"/></span>
								<span>
									<a href="#" id="trigger_dispatchDate" style="font-size: 9px;">
								<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a>
							</span>

							 <script language="javascript">                 
							  Calendar.setup(
							  {
								showsTime : false,
								electric : false,
								inputField : "dispatchDate",
								ifFormat : "%m/%d/%Y",
								singleClick: true,                                            
								button : "trigger_dispatchDate",
								onUpdate : updateDate                        
							 }
							  );
							  function updateDate(cal) {
								  var selIndex = cal.date.getDay();
								  if(selIndex == 0) selIndex = 7;
								};
							</script>
						</td>   
							<td width="200"><form:errors path="dispatchDate" />&nbsp;</td>
						</tr>
						<tr>
								<td>Dispatch Type</td>
								<td> 
									<form:select path="dispatchType" onChange="populateRegion(this);">
										<%-- <form:option value="" label="--Please choose Dispatch Type"/> --%>
										<form:options items="${dispatchTypes}" itemLabel="desc" itemValue="name" />
									</form:select> 
								</td>
								<td><form:errors path="dispatchType" />&nbsp;</td>
						</tr>   
						<tr>
								<td>Origin Facility</td>
						<td>
									<form:select path="originFacility" onChange="showZoneSelection(this, dispatchForm.destinationFacility)">
										<form:option value="" label="--Please Select Origin Facility"/>
										<form:options items="${trnFacilitys}" itemLabel="name" itemValue="facilityId" />
									</form:select> 
						</td>
								<td><form:errors path="originFacility" />&nbsp;</td>
					</tr>                                   
					<tr>
								<td>Destination Facility</td>
								<td> 
									<form:select path="destinationFacility" onChange="showZoneSelection(dispatchForm.originFacility, this)">
										<form:option value="" label="--Please Select Destination Facility"/>
										<form:options items="${trnFacilitys}" itemLabel="name" itemValue="facilityId" />
									</form:select> 
								</td>
								<td><form:errors path="destinationFacility" />&nbsp;</td>
						</tr>
						<tr>
						<td>Zone</td>
							<td>
								<c:if test="${!empty dispatchForm.zoneCode }">
									<c:set var="hasZone" value="true"/>
								</c:if>
								<c:if test="${(dispatchForm.isBullpen eq 'true')|| (dispatchForm.destinationFacility.trnFacilityType.name ne 'SIT')}">
									<c:set var="disableZone" value="true"/>
								</c:if>
								<form:select path="zoneCode" disabled="${disableZone}" onChange="getResourcesInfo()">
									<form:option value="" label="--Please Select Zone"/>
										<form:options items="${zones}" itemLabel="displayName" itemValue="zoneCode" />
									</form:select>
							
						</td>
							 <td>&nbsp;<form:errors path="zoneCode"/></td>
					</tr>
					<tr>
						<td>Confirmed</td>
						<td colspan="2"> 
							<form:checkbox path="confirmed" disabled="true" />
						</td>
					</tr>
					<tr>
						<td>BullPen</td>
						<td colspan="2"> 
							<form:checkbox path="isBullpen" value="true" onclick="bullpen(this)"/>
						</td>
					</tr>
					<tr>
						<td>Override Resource Check</td>
						<td colspan="2"> 
							<form:checkbox path="isOverride" onclick="checkOverride(this)"/>
						</td>
					</tr>    
					<tr>
						<td>Override Reason:</td>						
						<td>
						<c:choose>                    
								<c:when test='${dispatchForm.isOverride == "true"}'> 
									<form:select path="overrideReasonCode" >							
										<form:options items="${reasons}" itemLabel="reason" itemValue="code" />
									</form:select>
								</c:when>
								<c:otherwise> 
									<form:select path="overrideReasonCode" disabled="true">							
										<form:options items="${reasons}" itemLabel="reason" itemValue="code" />
									</form:select>
								</c:otherwise> 
							</c:choose>							
						</td>
                        <td>
                          &nbsp;<form:errors path="overrideReasonCode" />
                        </td>  
					</tr>                   
					<tr>
						<td>Region</td>
							<td>
								<form:select path="regionCode" disabled="${hasZone}">
									<form:option value="null" label="--Please Select Region"/>
									<form:options items="${regions}" itemLabel="code" itemValue="code" />
								</form:select>
						</td>
                        <td>
                          &nbsp;<form:errors path="regionCode" />
                        </td>  
					</tr>
					<tr>
							<td>Dispatch Group&nbsp;Time</td>
							<td>
								<c:if test="${!empty dispatchForm.dispatchId }">
										<c:set var="hasDispatch" value="true"/>
								</c:if>
								<spring:bind path="dispatchForm.confirmed"> 
								<c:choose>          
									<c:when test='${hasDispatch}'>     
										<form:select path="dispatchGroupS" disabled="${hasDispatch}">
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
								</spring:bind>
							</td>
							<td><form:errors path="dispatchGroupS" />&nbsp;</td>                 
						</tr>
					<tr>
						<td><a id="timeStart_toggler">Truck Dispatch&nbsp;Time</a></td>
						<td>  
							<spring:bind path="dispatchForm.confirmed"> 
								<c:choose>                    
								<c:when test='${status.value == "false"}'> 
									<form:input maxlength="50" size="8" path="startTime" onblur="this.value=time(this.value);" />
								</c:when>
								 <c:otherwise> 
								 <form:input maxlength="50" size="8" path="startTime" readOnly="true"/>
								</c:otherwise> 
								 </c:choose>
							 </spring:bind>
						</td>
						<td>
							<form:errors path="startTime" />&nbsp;
						</td>                 
					</tr>
					<tr>
						<td>Supervisor</td>
						<td>      
							<spring:bind path="dispatchForm.confirmed">
								<c:choose>
									<c:when test='${status.value=="false"}'> 
										<form:select path="supervisorCode" disabled='<c:out value="${status.value}" />'>
											<form:option value="" label="Select Supervisor"/>
											<form:options items="${supervisors}" itemLabel="supervisorInfo" itemValue="employeeId" />
										</form:select>
									</c:when>
									<c:otherwise> 
										<form:input maxlength="50" size="30" path="supervisorName" readOnly="true" cssClass="noborder"/>
									</c:otherwise> 
								</c:choose>
							</spring:bind>  
						</td>   
						<td>
							<form:errors path="supervisorCode" />&nbsp;
						</td>
					</tr>  
					<input type="hidden" name = "selectedroute"  value="<c:out value="${dispatchForm.route}"/>" />
                <tr>
                  <td>Route Number</td>
                  <td>          
                    <spring:bind path="dispatchForm.confirmed"> 
                        <c:choose>                    
                        <c:when test='${status.value == "false"}'> 
                            <form:select path="route">
                                <form:option value="" label="Select Route"/>
                                <c:choose>
                                   <c:when test="${dispatchForm.destinationFacility.trnFacilityType eq 'CD'}">
                                    <c:forEach items="${routes}" var="route" varStatus="gridRow">
                                        <c:if test="${route.zonePrefix == dispatchForm.destinationFacility.routingCode}">
                                            <form:option label="${route.routeNumber}" value="${route.routeNumber}" />
                                        </c:if>
                                    </c:forEach>
                                   </c:when>
								  <c:when test='${dispatchForm.zoneCode != ""}'>                                 
                                    <c:forEach items="${routes}" var="route" varStatus="gridRow">
                                        <c:if test="${route.zonePrefix == dispatchForm.zoneCode}">
                                            <form:option label="${route.routeNumber}" value="${route.routeNumber}" />
                                        </c:if>
                                    </c:forEach>
                                   </c:when>
                                   <c:otherwise> 
                                    <c:forEach items="${routes}" var="route" varStatus="gridRow">
                                        <c:if test='${route.adHoc == "true"}' >
                                            <form:option label="${route.routeNumber}" value="${route.routeNumber}" />
                                        </c:if>
                                    </c:forEach>                                   
                                   </c:otherwise>
                                  </c:choose>                                
                             </form:select>
                           <c:forEach items="${routes}" var="route" varStatus="gridRow">
                            <c:if test="${route.zonePrefix == dispatchForm.zoneCode}">
                                <input type="hidden" id = "route<c:out value="${route.routeNumber}"/>" name="route<c:out value="${route.routeNumber}"/>" value="<c:out value="${route.adHoc}"/>" />
                            </c:if>    
                           </c:forEach>
                        </c:when>
                         <c:otherwise> 
                               <form:input maxlength="50" size="8" path="route" readOnly="true" cssClass="noborder"/>
                        </c:otherwise> 
                         </c:choose>
                     </spring:bind> 

                 </td>
						<td>&nbsp;<form:errors path="route" /></td>
                </tr>  
					<spring:bind path="dispatchForm.isBullpen">   
						<c:choose>                    
							<c:when test='${status.value == "true"}'>                 
					<tr>
						<td>Truck Number</td>
						<td>  
							<form:input maxlength="50" size="8" path="truck" readOnly="true" />&nbsp;&nbsp;
								<c:choose>                    
									<c:when test='${dispatchForm.confirmed == "false"}'> 
										<SELECT id="truckNum" name="truckNum" onChange="setTruckNumber(this.value);">
											<OPTION value="">Select Trucks</OPTION>          
												<c:forEach var="truck" items="${trucks}">
													<option value="<c:out value="${truck}"/>"><c:out value="${truck}"/></option>                         
												</c:forEach>
									</c:when>
								</c:choose>
						</td>
						<td>
							<form:errors path="truck" />&nbsp;
						</td>                  
					</tr>
							</c:when>
						 </c:choose>  
					</spring:bind> 
					<tr>
						<td>Drivers</td>
							<td width="375px"> 
							<div class="fleft">
								<c:forEach items="${dispatchForm.drivers}" var="driver" varStatus="gridRow">
									<div class="dipatch_AddEdit_row">
									<spring:bind path="dispatchForm.drivers[${gridRow.index}].employeeId">
									 <c:choose>                    
									   <c:when test='${dispatchForm.confirmed == "false"}'> 
										<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" onchange="handleResoureChangeEvent('drivers[<c:out value="${gridRow.index}"/>]', this);">
											<OPTION value="">Select Drivers</OPTION>          
											<c:forEach var="driverEmp" items="${drivers}">
												<option <c:if test='${status.value == driverEmp.employeeId}'> SELECTED </c:if> 
											value="<c:out value="${driverEmp.employeeId}"/>"><c:out value="${driverEmp.name}"/>-<c:out value="${driverEmp.employeeId}"/></option>                         
											</c:forEach>
										</SELECT>
									   </c:when>
									   <c:otherwise> 
										<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" disabled="true" onchange="handleResoureChangeEvent('drivers[<c:out value="${gridRow.index}"/>]', this);">
											<OPTION value="">Select Drivers</OPTION>          
											<c:forEach var="driverEmp" items="${drivers}">
												<option <c:if test='${status.value == driverEmp.employeeId}'> SELECTED </c:if> 
											value="<c:out value="${driverEmp.employeeId}"/>"><c:out value="${driverEmp.name}"/>-<c:out value="${driverEmp.employeeId}"/></option>                         
											</c:forEach>
										</SELECT>
									   </c:otherwise> 
									 </c:choose>    
									</spring:bind>
								</div>
								 </c:forEach>
							</div>

							<div class="fright">
								<c:forEach items="${dispatchForm.drivers}" var="driver" varStatus="gridRow">    
										<div class="dipatch_AddEdit_row">
									<spring:bind path="dispatchForm.drivers[${gridRow.index}].nextelNo">
									&nbsp;<img src="./images/icons/nextel.gif" width="16" height="16" border="0" alt="Nextel Number" title="Nextel Number" class="nextel_icon" />&nbsp;<input type="text" size="8" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" 
											value="<c:out value="${status.value}"/>" />
										
									</spring:bind>
									</div>
								</c:forEach>
							</div>&nbsp;
						</td>  
						<td>
							<form:errors path="drivers" />&nbsp;
						</td>
					</tr>
					<tr>
						<td>Helpers</td>
						<td> 
							<div class="fleft">
							<c:forEach items="${dispatchForm.helpers}" var="helper" varStatus="gridRow">
							<div class="dipatch_AddEdit_row">
								<spring:bind path="dispatchForm.helpers[${gridRow.index}].employeeId">
								 <c:choose>                    
								   <c:when test='${dispatchForm.confirmed == "false"}'> 
									<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" onchange="handleResoureChangeEvent('helpers[<c:out value="${gridRow.index}"/>]', this);">
										<OPTION value="">Select Helpers</OPTION>          
										<c:forEach var="helperEmp" items="${helpers}">
											<option <c:if test='${status.value == helperEmp.employeeId}'> SELECTED </c:if> 
										value="<c:out value="${helperEmp.employeeId}"/>"><c:out value="${helperEmp.name}"/>-<c:out value="${helperEmp.employeeId}"/></option>                         
										</c:forEach>
										</SELECT>
									   </c:when>
									   <c:otherwise> 
										<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" disabled="true" onchange="handleResoureChangeEvent('helpers[<c:out value="${gridRow.index}"/>]', this);">
											<OPTION value="">Select Helpers</OPTION>          
											<c:forEach var="helperEmp" items="${helpers}">
												<option <c:if test='${status.value == helperEmp.employeeId}'> SELECTED </c:if> 
											value="<c:out value="${helperEmp.employeeId}"/>"><c:out value="${helperEmp.name}"/>-<c:out value="${helperEmp.employeeId}"/></option>                         
											</c:forEach>
										 </SELECT>
									   </c:otherwise> 
									 </c:choose>
									</spring:bind>
								</div>
								</c:forEach>
							</div>
							
							<div class="fright">
								<c:forEach items="${dispatchForm.helpers}" var="helper" varStatus="gridRow">
								<div class="dipatch_AddEdit_row">
									<spring:bind path="dispatchForm.helpers[${gridRow.index}].nextelNo">
								   
										&nbsp;<img src="./images/icons/nextel.gif" width="16" height="16" border="0" alt="Nextel Number" title="Nextel Number" />&nbsp;<input type="text" size="8" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" 
											value="<c:out value="${status.value}"/>" />
											
									</spring:bind>
								</div>
								</c:forEach>
							</div>&nbsp;
						</td>
                        <td>
							<form:errors path="helpers" />&nbsp;
						</td>   
                    </tr>             
					<tr>
						<td>Runners</td>
						<td> 
							<div class="fleft">
							<c:forEach items="${dispatchForm.runners}" var="runner" varStatus="gridRow">
								<div class="dipatch_AddEdit_row">
									<spring:bind path="dispatchForm.runners[${gridRow.index}].employeeId">
									 <c:choose>                    
									   <c:when test='${dispatchForm.confirmed == "false"}'> 
										<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" >
											<OPTION value="">Select Runners</OPTION>          
											<c:forEach var="runnerEmp" items="${runners}">
												<option <c:if test='${status.value == runnerEmp.employeeId}'> SELECTED </c:if> 
											value="<c:out value="${runnerEmp.employeeId}"/>"><c:out value="${runnerEmp.name}"/>-<c:out value="${runnerEmp.employeeId}"/></option>                         
											</c:forEach>
											</SELECT>
										   </c:when>
										   <c:otherwise> 
											<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" disabled="true">
												<OPTION value="">Select Runners</OPTION>          
												<c:forEach var="runnerEmp" items="${runners}">
													<option <c:if test='${status.value == runnerEmp.employeeId}'> SELECTED </c:if> 
												value="<c:out value="${runnerEmp.employeeId}"/>"><c:out value="${runnerEmp.name}"/>-<c:out value="${runnerEmp.employeeId}"/></option>                         
												</c:forEach>
											 </SELECT><br />
										   </c:otherwise> 
										 </c:choose>
										</spring:bind>
								</div>
								</c:forEach>
							</div>
							<div class="fright">
								<c:forEach items="${dispatchForm.runners}" var="runner" varStatus="gridRow">
								<div class="dipatch_AddEdit_row">
									<spring:bind path="dispatchForm.runners[${gridRow.index}].nextelNo">
										&nbsp;<img src="./images/icons/nextel.gif" width="16" height="16" border="0" alt="Nextel Number" title="Nextel Number">&nbsp;<input type="text" size="8" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" 
											value="<c:out value="${status.value}"/>" />
									</spring:bind>
								</div>
							</c:forEach>
							</div>&nbsp;
						</td>   
						<td>
							<form:errors path="runners" />&nbsp;
						</td>                
					</tr>
					</table>
					</td>
					<!-- new table goes here -->
					<td align="center" valign="top">
							<table>
					<tr>
						<td>Status</td>
						<td colspan="2">                  
							<spring:bind path="dispatchForm.confirmed"> 
								<c:choose>                    
								<c:when test='${status.value == "true"}'> 
									<form:select path="status">
										<form:option value="null" label="Select Status"/>
										<form:options items="${statuses}" itemLabel="name" itemValue="code" />
								   </form:select>
								</c:when>
								 <c:otherwise> 
									<form:select path="status" disabled="true">
										<form:option value="null" label="Select Status"/>
										<form:options items="${statuses}" itemLabel="name" itemValue="code" />
								   </form:select>
								</c:otherwise> 
								 </c:choose>
							 </spring:bind>                   
						</td>
					</tr> 
					
					<tr>
									<td width="150">GPS No</td>
									<td>
							<form:select path="gpsNumber" onchange="return assetChangeEvent(this);">
										<form:option value="" label="Select GPS"/>
										<form:options items="${GPS}" itemLabel="assetNo" itemValue="assetId" />
									</form:select>
						</td>
									<td width="200">
                          &nbsp;<form:errors path="gpsNumber" />
                        </td>  
					</tr>   					
					<tr>
						<td>EzPass No</td>
									<td>       
							<form:select path="ezpassNumber" onchange="return assetChangeEvent(this);">
										<form:option value="" label="Select EZPass"/>
										<form:options items="${EZPASS}" itemLabel="assetNo" itemValue="assetId" />
									</form:select>
						</td>
                        <td>
                          &nbsp;<form:errors path="ezpassNumber" />
                        </td>  
					</tr>
					
					<tr>
						<td>MotKit No</td>
									<td>       
							<form:select path="motKitNumber" onchange="return assetChangeEvent(this);">
										<form:option value="" label="Select MotKit"/>
										<form:options items="${MOTKIT}" itemLabel="assetNo" itemValue="assetId" />
									</form:select>
						</td>
                        <td>
                          &nbsp;<form:errors path="motKitNumber" />
                        </td>  
					</tr>  
										
					<c:if test="${dispatchForm.today == true}">
					<c:if test="${dispatchForm.isBullpen == false}">
					<c:if test='${dispatchForm.dispatched == false}'>
					<c:if test='${dispatchForm.dispatchStatus == "Packet" || dispatchForm.dispatchStatus == "EmpReady"}'>
					<tr>
						<td>Phones Assigned</td>
						<td colspan="2"> 
							<form:checkbox path="phoneAssigned" />
						</td>
					</tr>
					
					<tr>
						<td>Keys Ready</td>
						<td colspan="2"> 
							<form:checkbox path="keysReady" />
						</td>
					</tr>
					</c:if>
					</c:if>
					<c:if test='${dispatchForm.checkedIn == false}'>
					<c:if test='${dispatchForm.dispatched == true}'>
					<tr>
						<td>Dispatched</td>
						<td colspan="2"> 
							<form:checkbox path="dispatched" />
						</td>
					</tr>
					</c:if>
					</c:if>
					
					<c:if test='${dispatchForm.checkedIn == true}'>
					<tr>
						<td>Checked In</td>
						<td colspan="2"> 
							<form:checkbox path="checkedIn" />
						</td>
					</tr>
					</c:if>
					</c:if>
					
					<c:if test="${dispatchForm.isBullpen == true}">	
					<c:if test='${dispatchForm.checkedIn == false}'>
					<c:if test='${dispatchForm.dispatched == true}'>				
					<tr>
						<td>Dispatched</td>
						<td colspan="2"> 
							<form:checkbox path="dispatched" />
						</td>
					</tr>					
					</c:if>
					</c:if>
					
					<c:if test='${dispatchForm.checkedIn == true}'>
					<tr>
						<td>Checked In</td>
						<td colspan="2"> 
							<form:checkbox path="checkedIn" />
						</td>
					</tr>
					</c:if>
					</c:if>
					
					</c:if>
					
					
					<c:if test="${dispatchForm.today == false}">					
					<c:if test='${dispatchForm.checkedIn == true}'>
					<tr>
						<td>Checked In</td>
						<td colspan="2"> 
							<form:checkbox path="checkedIn" />
						</td>
					</tr>
					</c:if>					
					</c:if>
					
					<tr>					
						<td>Additional Nextels</td>
						<td>                  
							<form:textarea path="additionalNextels" rows="5" cols="45" cssClass="large" />
						</td>
						<td>
							<form:errors path="additionalNextels" />&nbsp;
						</td>
					</tr>   
					
					<tr>					
						<td>Comments</td>
						<td>                  
							<form:textarea path="comments" rows="5" cols="45" cssClass="large" />
						</td>
						<td>
							<form:errors path="comments" />&nbsp;
						</td>
					</tr>
					<c:if test="${dispatchForm.muniMeterEnabled!=null }" >
					<tr>
					<td> Muni Meter </td>
					<td>
					<table>
					<tr>					
						<td>Card Value Assigned</td>
						<td>                  
							<form:input  path="muniMeterValueAssigned"  />
						</td>
						<td>
							<form:errors path="muniMeterValueAssigned" />&nbsp;
						</td>
					</tr>    
					<tr>					
						<td>Card Not Assigned</td>
						<td>                  
							<form:checkbox  path="muniMeterCardNotAssigned" value="X" />
						</td>
						<td>
							<form:errors path="muniMeterCardNotAssigned" />&nbsp;
						</td>
					</tr>     
					<tr>					
						<td>Card Value Returned</td>
						<td>                  
							<form:input  path="muniMeterValueReturned"  />
						</td>
						<td>
							<form:errors path="muniMeterValueReturned" />&nbsp;
						</td>
					</tr>    
					<tr>					
						<td>Card Not Returned</td>
						<td>                  
							<form:checkbox  path="muniMeterCardNotReturned" value="X" />
						</td>
						<td>
							<form:errors path="muniMeterCardNotReturned" />&nbsp;
						</td>
					</tr>
					</table>
					</td></tr> 
					</c:if>               
					<tr>
						<td colspan="3">&nbsp;</td>
					</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="3" align="center">
							<input type = "submit" value="&nbsp;Save&nbsp;"  />
							<input type = "button" value="&nbsp;Reset&nbsp;" onclick="javascript:resetDetails();" />
							<input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:back();" />
						</td> 
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
			  }
			  if( result[1] === 'PLANT'){
				  alert('Destination facility cannot be main plant.');
				  destRefVar.selectedIndex = 0; return;
			  } else if((result[1] === result[0]) && (originRef != '' && destRef != '')){
				  alert('Both origin & destination facility cannot be same.');
				  originRefVar.selectedIndex = 0;
				  destRefVar.selectedIndex = 0; return;
			  } 
			  document.getElementById("destFacilityModified").value = "true";
			  dispatchForm.submit();
			}
		}

	   function setTruckNumber(truckNo) {
            dispatchForm.truck.value = truckNo;
       }
       function resetDetails() {
             document.location.href='<c:out value="${pageContext.request.contextPath}" />/editdispatch.do?id=<%= id %>&dispDate=<%= dispDate %>';
        }
       function init_clock(){
            var tpStartTime = new TimePicker('timeStart_picker', 'startTime', 'timeStart_toggler', {imagesPath:"images"});
            var tpFirstDlvTime = new TimePicker('timeFirstDlv_picker', 'firstDeliveryTime', 'timeFirstDlv_toggler', {imagesPath:"images"});
	    }
        
        function $() {
            var elements = new Array();
            for (var i = 0; i < arguments.length; i++) {
                var element = arguments[i];
                if (typeof element == 'string')
                    element = document.getElementById(element);
                if (arguments.length == 1)
                    return element;
                elements.push(element);
            }
            return elements;
        }
        
			function getResourcesInfo() {
				dispatchForm.submit();
			}
			function bullpen(chxbox) {
				var hasConfirmed = confirm("Are you sure you want to flag/unflag it as a BullPen? You may loose your exisitng dispatch information.");
				if (hasConfirmed) {
					var regionCode = document.getElementById('regionCode').value;
					jsonrpcClient.AsyncDispatchProvider.getRegionFacility(regionFacilityCallback, regionCode);
				} else {
					chxbox.checked = !(chxbox.checked);
				}
			}
			
			function regionFacilityCallback(result, exception) {
				if(exception) {
					alert('Unable to connect to host system. Please contact system administrator!');               
					return;
				}
				document.getElementById('originFacility').value = result;
				dispatchForm.submit();
			}
			
			function checkRouteInfo() {
				if (!dispatchForm.confirmed.checked)
					getRouteInfo();
			}
			function getRouteInfo() {
				if (!dispatchForm.confirmed.checked) {
					var dispatchDate = dispatchForm.dispatchDate.value;
					var zoneCode = dispatchForm.zoneCode.value;
					var isTrailerRoute = false;
					if (dispatchForm.zoneCode.value == '') {
						zoneCode = '<c:out value="${dispatchForm.destinationFacility.routingCode}"/>';
						isTrailerRoute = true;
					}
					jsonrpcClient.AsyncDispatchProvider.getActiveRoute(
							getRouteInfoCallback, dispatchDate, zoneCode,
							isTrailerRoute);
				}
			}
			function getRouteInfoCallback(result, exception) {
				if (exception) {
					alert('Unable to connect to host system. Please contact system administrator!');
					return;
				}
				for ( var i = dispatchForm.route.options.length - 1; i >= 1; i--) {
					dispatchForm.route.remove(i);
				}
				var selected = false;
				var results = result.list;
				for ( var i = 0; i < results.length; i++) {
					if (results[i].length > 0) {
						var optn = document.createElement("OPTION");
						optn.text = results[i];
						optn.value = results[i];
						if (optn.text == dispatchForm.selectedroute.value) {
							optn.selected = true;
							selected = true;
						}
						dispatchForm.route.options.add(optn);
					}
				}				
				if (!selected)
					dispatchForm.route.options[0].selected = true;
			}

			function back() {
				var filters = unescape(getParameter("filter"));
				var params = filters.split("&");
				var dispatchForm = document.forms["dispatch"];
				for ( var i = 0; i < params.length; i++) {
					var param = params[i].split("=");
					add_input(dispatchForm, "hidden", param[0], param[1]);
				}
				dispatchForm.submit();
			}

			function checkOverride(chxbox) {
				if (chxbox.checked) {
					dispatchForm.overrideReasonCode.disabled = false;
				} else {
					dispatchForm.overrideReasonCode.disabled = true;
				}
			}

			function handleResoureChangeEvent(target, src) {
				resoureChangeEvent(src, 'D', document
						.getElementById('dispatchDate'), document
						.getElementById('dispatchId'));
			}

			function  dispatchGroupChanged() {
				document.getElementById("dispatchGroupModified").value = "true";
				dispatchForm.submit();
			}

			function initAssets() {
				document.getElementById("curr_gpsNumber").value = document
						.getElementById("gpsNumber").value;
				document.getElementById("curr_ezpassNumber").value = document
						.getElementById("ezpassNumber").value;
				document.getElementById("curr_motKitNumber").value = document
						.getElementById("motKitNumber").value;
			}
			function populateRegion(dispatchTypeVar) {
				document.getElementById('dispatchTypeModified').value = 'true';
				var dispatchType = dispatchTypeVar.value || '';
				if(dispatchType === 'LDD'){
					var hasConfirmed = confirm('You are about to flag/unflag the dispatch to Light duty dispatch.');
					if (hasConfirmed) {
						document.getElementById('destinationFacility').selectedIndex = 0;
						document.getElementById('originFacility').selectedIndex = 0;
						document.getElementById('isBullpen1').disabled = 'true';
						document.getElementById('isOverride1').disabled = 'true';
						dispatchForm.submit();
					}
				}			
								
			}
		</script>
  </tmpl:put>
</tmpl:insert>
<script>


checkRouteInfo();
initAssets();
</script>
<form name="dispatch" action="dispatch.do" method="post">  </form>

