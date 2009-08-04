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
        <script language="javascript">         
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
                var hasConfirmed = confirm ("Are you sure you want to flag/unflag it as a BullPen? You may loose your exisitng dispatch information.");
                if(hasConfirmed)
                    dispatchForm.submit();
                else{
                    chxbox.checked = !(chxbox.checked);
                }                
        }
       function checkRouteInfo()
       {
       if(!dispatchForm.confirmed.checked)getRouteInfo();
       }
       function getRouteInfo()
        {
         if(!dispatchForm.confirmed.checked)
         {
        	var jsonrpcClient = new JSONRpcClient("dispatchprovider.ax");
        	var dispatchDate=dispatchForm.dispatchDate.value;
        	var zoneCode=dispatchForm.zoneCode.value;
        	jsonrpcClient.AsyncDispatchProvider.getActiveRoute(getRouteInfoCallback,dispatchDate,zoneCode);
         }
        }
        function getRouteInfoCallback(result, exception) 
        {
      	  
          if(exception) {               
              alert('Unable to connect to host system. Please contact system administrator!');               
              return;
          }
         
		 	  
		  for(var i=dispatchForm.route.options.length-1;i>=1;i--)
		  {
				dispatchForm.route.remove(i);
		  }		 
		  var selected=false;
		  var results=result.list;
		  for(var i=0;i<results.length;i++)
		  {
			  if( results[i].length>0)
			  {
			  	var optn = document.createElement("OPTION");
			  	optn.text = results[i];
	          	optn.value = results[i];
	          	if(optn.text==dispatchForm.selectedroute.value)
	          	{
	          		optn.selected=true;
	          		selected=true;
	          	}
	          	dispatchForm.route.options.add(optn);
	          	
	          }
          }
  /*        //testing
          var optn = document.createElement("OPTION");
		  optn.text = dispatchForm.selectedroute.value;
	      optn.value = dispatchForm.selectedroute.value;
	      dispatchForm.route.options.add(optn);
	      */
	      
          if(!selected)     
          dispatchForm.route.options[0].selected=true;
                                
      }        
      
      function back()
      {
      	var filters=unescape(getParameter("filter"));      	
      	var params=filters.split("&");
      	var dispatchForm=document.forms["dispatch"];
      	for(var i=0;i<params.length;i++)
      	{
      		var param=params[i].split("=");         				
      		add_input(dispatchForm,"hidden",param[0],param[1]);
      	}     	      	
      	dispatchForm.submit();
      }
  
      </script>
      <style>
        .time_picker_div {padding:5px;
            border:solid #999999 1px;
            background:#ffffff;}
      </style>      
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'> Operations : Dispatch : Add/Edit Dispatch</tmpl:put>
  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form:form commandName = "dispatchForm" method="post">
      <form:hidden path="dispatchId" />
      <input type=hidden name="routeNo" value="" />
      <input type=hidden name="zoneId" value="" />
      <input type=hidden name="dispDate" value="<%=dispDate %>" />

    
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td class="screentitle">Add/Edit Dispatch</td>
		</tr>
		<tr>
			<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
		</tr>
          
		<tr>
			<td class="screencontent">
			<table ><tr><td valign="top">
				<table class="forms1" border="0">
					<tr>
						<td>Date</td>
						<td>
								<span><form:input maxlength="50" size="24" path="dispatchDate" onChange="javascript:getRouteInfo();"/></span>
							<span><a href="#" id="trigger_dispatchDate" style="font-size: 9px;">
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
								 // document.getElementById('dispatchDay').selectedIndex =  selIndex;
								};
							</script>
						</td>   
						<td>
							<form:errors path="dispatchDate" />&nbsp;
						</td>
					</tr>                                   
					<tr>
						<td>Zone</td>
						<td colspan="2">         
					  
							<spring:bind path="dispatchForm.zoneCode">   
								<c:choose>                    
								<c:when test='${status.value == ""}'> 
								  <spring:bind path="dispatchForm.isBullpen">   
									<form:select path="zoneCode" onChange="javascript:getResourcesInfo();" disabled="${status.value}">
										<form:option value="" label="Select Zone"/>
										<form:options items="${zones}" itemLabel="displayName" itemValue="zoneCode" />
									</form:select>
								   </spring:bind>
								</c:when>
								 <c:otherwise> 
									   <form:input maxlength="50" size="10" path="zoneCode" readOnly="true" cssClass="noborder"/>
								</c:otherwise> 
								 </c:choose>
							 </spring:bind>&nbsp;
						</td>
                         <td>
                          &nbsp;<form:errors path="zoneCode" />
                        </td>                        
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
						<td>Region</td>
						<td colspan="2">       
							<c:choose>                    
								<c:when test='${dispatchForm.isBullpen == "true"}'> 
									<form:select path="regionCode">
										<form:option value="" label="Select Region"/>
										<form:options items="${regions}" itemLabel="name" itemValue="code" />
									</form:select>
								</c:when>
								<c:otherwise> 
									<form:select path="regionCode" disabled="true">
										<form:option value="" label="Select Region"/>
										<form:options items="${regions}" itemLabel="name" itemValue="code" />
									</form:select>
								</c:otherwise> 
							</c:choose>
						</td>
                        <td>
                          &nbsp;<form:errors path="regionCode" />
                        </td>  
					</tr>       
					<tr>
						<td>Supervisor</td>
						<td>      
							<spring:bind path="dispatchForm.confirmed"> 
								<c:choose>                    
									<c:when test='${status.value == "false"}'> 
										<form:select path="supervisorCode" disabled='<c:out value="${status.value}" />'>
											<form:option value="" label="Select Supervisor"/>
											<form:options items="${supervisors}" itemLabel="name" itemValue="employeeId" />
										</form:select>
									</c:when>
									<c:otherwise> 
										<form:input maxlength="50" size="30" path="supervisorName" readOnly="true" />
									</c:otherwise> 
								</c:choose>
							</spring:bind>  
						</td>   
						<td>
							<form:errors path="supervisorCode" />&nbsp;
						</td>
					</tr> 
					<tr>
						<td><a id="timeStart_toggler">Start&nbsp;Time</a></td>
						<td>  
							<spring:bind path="dispatchForm.confirmed"> 
								<c:choose>                    
								<c:when test='${status.value == "false"}'> 
									<form:input maxlength="50" size="8" path="startTime" onblur="this.value=time(this.value);" />
									<!-- <div id="timeStart_picker" class="time_picker_div"></div> -->
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
						<td><a id="timeFirstDlv_toggler">First Dlv.&nbsp;Time</a></td>
						<td>  
							 <spring:bind path="dispatchForm.confirmed"> 
								<c:choose>                    
								<c:when test='${status.value == "false"}'> 
									<form:input maxlength="50" size="8" path="firstDeliveryTime" onblur="this.value=time(this.value);" />
									<!-- <div id="timeFirstDlv_picker" class="time_picker_div"></div> -->
								</c:when>
								 <c:otherwise> 
								 <form:input maxlength="50" size="8" path="firstDeliveryTime" readOnly="true"/>
								</c:otherwise> 
								 </c:choose>
							 </spring:bind> 
						</td>
						<td>
							<form:errors path="firstDeliveryTime" />&nbsp;
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
                <td>
                  &nbsp;<form:errors path="route" />
                </td>                  
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
						<td> 
							<div class="fleft">
								<c:forEach items="${dispatchForm.drivers}" var="driver" varStatus="gridRow">
									<div class="dipatch_AddEdit_row">
									<spring:bind path="dispatchForm.drivers[${gridRow.index}].employeeId">
									 <c:choose>                    
									   <c:when test='${dispatchForm.confirmed == "false"}'> 
										<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" >
											<OPTION value="">Select Drivers</OPTION>          
											<c:forEach var="driverEmp" items="${drivers}">
												<option <c:if test='${status.value == driverEmp.employeeId}'> SELECTED </c:if> 
											value="<c:out value="${driverEmp.employeeId}"/>"><c:out value="${driverEmp.name}"/></option>                         
											</c:forEach>
										</SELECT>
									   </c:when>
									   <c:otherwise> 
										<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" disabled="true">
											<OPTION value="">Select Drivers</OPTION>          
											<c:forEach var="driverEmp" items="${drivers}">
												<option <c:if test='${status.value == driverEmp.employeeId}'> SELECTED </c:if> 
											value="<c:out value="${driverEmp.employeeId}"/>"><c:out value="${driverEmp.name}"/></option>                         
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
									<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" >
										<OPTION value="">Select Helpers</OPTION>          
										<c:forEach var="helperEmp" items="${helpers}">
											<option <c:if test='${status.value == helperEmp.employeeId}'> SELECTED </c:if> 
										value="<c:out value="${helperEmp.employeeId}"/>"><c:out value="${helperEmp.name}"/></option>                         
										</c:forEach>
										</SELECT>
									   </c:when>
									   <c:otherwise> 
										<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" disabled="true">
											<OPTION value="">Select Helpers</OPTION>          
											<c:forEach var="helperEmp" items="${helpers}">
												<option <c:if test='${status.value == helperEmp.employeeId}'> SELECTED </c:if> 
											value="<c:out value="${helperEmp.employeeId}"/>"><c:out value="${helperEmp.name}"/></option>                         
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
											value="<c:out value="${runnerEmp.employeeId}"/>"><c:out value="${runnerEmp.name}"/></option>                         
											</c:forEach>
											</SELECT>
										   </c:when>
										   <c:otherwise> 
											<SELECT id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" disabled="true">
												<OPTION value="">Select Runners</OPTION>          
												<c:forEach var="runnerEmp" items="${runners}">
													<option <c:if test='${status.value == runnerEmp.employeeId}'> SELECTED </c:if> 
												value="<c:out value="${runnerEmp.employeeId}"/>"><c:out value="${runnerEmp.name}"/></option>                         
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
					<td valign="top">
					<table class="forms1" border="0">
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
						<td>GPS No</td>
						<td colspan="2"> 
							<form:input maxlength="50" size="8" path="gpsNumber" />
						</td>
					</tr>
					<tr>
						<td>EzPass No</td>
						<td colspan="2"> 
							<form:input maxlength="50" size="8" path="ezpassNumber" />
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
						<td>Comments</td>
						<td>                  
							<form:textarea path="comments" rows="5" cols="45" cssClass="large" />
						</td>
						<td>
							<form:errors path="comments" />&nbsp;
						</td>
					</tr>                
					<tr>
						<td colspan="3">&nbsp;</td>
					</tr>
					</table></td></tr>
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
	</table>
      </form:form>
     </div>
     
  </tmpl:put>
</tmpl:insert>
<script>checkRouteInfo();</script>
<form name="dispatch" action="dispatch.do" method="post">  </form>

