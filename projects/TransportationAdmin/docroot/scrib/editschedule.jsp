<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import='com.freshdirect.transadmin.web.model.*' %>
<%@ page import='com.freshdirect.transadmin.util.*' %>
<%@ page import='com.freshdirect.framework.util.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>
<tmpl:put name='yui-lib'>
	<%@ include file='/common/i_yui.jspf'%>	
</tmpl:put>	

<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>
<% 
	String pageTitle = "Edit Schedule";
	String copyWeekOf = "";
%>
    <tmpl:put name='title' direct='true'> Operations : Schedule : <%=pageTitle%></tmpl:put>

	<tmpl:put name='content' direct='true'>

	<div class="MNM001 subsub>
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
			
			<div class="sub_tableft sub_tabL_MNM001 ">&nbsp;</div>
			<div class="subtab ">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=C" class="">Team</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 ">&nbsp;</div>

			<div class="sub_tableft sub_tabL_MNM001 ">&nbsp;</div>
			<div class="subtab ">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=P" class="">Preference</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 ">&nbsp;</div>
		</div>
	</div>

	<div class="contentroot">		
		<div class="cont_row">
		<br/>
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
					<td class="screentitle">Add/Edit Schedule</td>
			</tr>
		</table>
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr>
		<td width="25%" style="vertical-align: top;">
			<div id="treediv" style="margin:10px">
				<div id="parentTreeDiv" style="height:240px;overflow-y:auto;background-color:#F2F2F2;border:1px solid #000;">
	 				<div id="treecontainer"></div>
				</div>	
			</div>
		</td>
		
		<td width="50%" align="center">		
		
			<form:form commandName = "scheduleForm" method="post">
			
			<% 
				boolean isMassEdit = false;
				WebSchedule _tmpSchedule = (WebSchedule)request.getAttribute("scheduleForm");
				if(_tmpSchedule != null) {
					copyWeekOf = TransStringUtil.getDate(_tmpSchedule.getWeekOf());
					String[] employeeIds = StringUtil.decodeStrings(_tmpSchedule.getEmployeeIds());
					if(employeeIds != null && employeeIds.length > 1) {
						isMassEdit = true;
					}
				}
			 %>
			<table width="90%" cellpadding="0" cellspacing="0" border="0">					
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
									<tr><td width="60">&nbsp;</td><td>Region</td><td>Time</td><td>Depot Facility*</td></tr>
									<tr>
										<td width="60">Mon</td>
										<td>
										<form:hidden path="mon.scheduleId"/>
										<form:hidden path="mon.weekOf"/>
										<form:select path="mon.regionS" cssStyle="width:100px" onchange="javascript:disableTimeFacility('mon')">
											<form:option value="" label="Select"/>
											<form:option value="OFF" label="OFF"/>
											<form:options items="${region}" itemLabel="name" itemValue="code" />
										</form:select>
										</td>
										<td>
											<form:select path="mon.dispatchGroupS" cssStyle="width:100px" onchange="javascript:disableTimeFacility('mon')">
												<form:option value="" label="Select Dispatch Group"/>
												<form:options items="${dispatchGroups}" itemLabel="name" itemValue="groupTime" />
											</form:select>
										</td>
										<td>
										<form:select path="mon.depotFacility" cssStyle="width:180px">
												<form:option value="" label="Select"/>
												<form:options items="${depotFacility}" itemLabel="name" itemValue="facilityId" />
										</form:select>
										</td>
										<td><form:errors path="mon.regionS" />&nbsp&nbsp;&nbsp;<form:errors path="mon.dispatchGroupS" />&nbsp&nbsp;&nbsp;<form:errors path="mon.depotFacility" /></td>
									</tr>
									<tr>
										<td width="60">Tue</td>
										<td>
										<form:hidden path="tue.scheduleId"/>
										<form:hidden path="tue.weekOf"/>
										<form:select path="tue.regionS" cssStyle="width:100px" onchange="javascript:disableTimeFacility('tue')">
											<form:option value="" label="Select"/>
											<form:option value="OFF" label="OFF"/>
											<form:options items="${region}" itemLabel="name" itemValue="code" />
										</form:select>
										</td>
										<td>
											<form:select path="tue.dispatchGroupS" cssStyle="width:100px" onchange="javascript:disableTimeFacility('tue')">
												<form:option value="" label="Select Dispatch Group"/>												
												<form:options items="${dispatchGroups}" itemLabel="name" itemValue="groupTime" />
											</form:select>
										</td>
										<td>
											<form:select path="tue.depotFacility" cssStyle="width:180px">
													<form:option value="" label="Select"/>
													<form:options items="${depotFacility}" itemLabel="name" itemValue="facilityId" />
											</form:select>
										</td>
										<td><form:errors path="tue.regionS" />&nbsp&nbsp;&nbsp;<form:errors path="tue.dispatchGroupS" />&nbsp&nbsp;&nbsp;<form:errors path="tue.depotFacility" /></td>

									</tr>
									<tr>
										<td width="60">Wed</td>
										<td>
										<form:hidden path="wed.scheduleId"/>
										<form:hidden path="wed.weekOf"/>
										<form:select path="wed.regionS" cssStyle="width:100px" onchange="javascript:disableTimeFacility('wed')">
											<form:option value="" label="Select"/>
											<form:option value="OFF" label="OFF"/>
											<form:options items="${region}" itemLabel="name" itemValue="code" />
										</form:select>
										</td>
										<td>
											<form:select path="wed.dispatchGroupS" cssStyle="width:100px" onchange="javascript:disableTimeFacility('wed')">
												<form:option value="" label="Select Dispatch Group"/>												
												<form:options items="${dispatchGroups}" itemLabel="name" itemValue="groupTime" />
											</form:select>
										</td>
										<td>
											<form:select path="wed.depotFacility" cssStyle="width:180px">
													<form:option value="" label="Select"/>
													<form:options items="${depotFacility}" itemLabel="name" itemValue="facilityId" />
											</form:select>
										</td>
										<td><form:errors path="wed.regionS" />&nbsp&nbsp;&nbsp;<form:errors path="wed.dispatchGroupS" />&nbsp&nbsp;&nbsp;<form:errors path="wed.depotFacility" /></td>
									</tr>
									<tr>
										<td width="60">Thu</td>
										<td>
										<form:hidden path="thu.scheduleId"/>
										<form:hidden path="thu.weekOf"/>
										<form:select path="thu.regionS" cssStyle="width:100px" onchange="javascript:disableTimeFacility('thu')">
											<form:option value="" label="Select"/>
											<form:option value="OFF" label="OFF"/>
											<form:options items="${region}" itemLabel="name" itemValue="code" />
										</form:select>
										</td>
										<td>
											<form:select path="thu.dispatchGroupS" cssStyle="width:100px" onchange="javascript:disableTimeFacility('thu')">
												<form:option value="" label="Select Dispatch Group"/>												
												<form:options items="${dispatchGroups}" itemLabel="name" itemValue="groupTime" />
											</form:select>
										</td>
										<td>
											<form:select path="thu.depotFacility" cssStyle="width:180px">
													<form:option value="" label="Select"/>
													<form:options items="${depotFacility}" itemLabel="name" itemValue="facilityId" />
											</form:select>
										</td>
										<td><form:errors path="thu.regionS" />&nbsp&nbsp;&nbsp;<form:errors path="thu.dispatchGroupS" />&nbsp&nbsp;&nbsp;<form:errors path="thu.depotFacility" /></td>
									</tr>
									<tr>
										<td width="60">Fri</td>
										<td>
										<form:hidden path="fri.scheduleId"/>
										<form:hidden path="fri.weekOf"/>
										<form:select path="fri.regionS" cssStyle="width:100px" onchange="javascript:disableTimeFacility('fri')">
											<form:option value="" label="Select"/>
											<form:option value="OFF" label="OFF"/>
											<form:options items="${region}" itemLabel="name" itemValue="code" />
										</form:select>
										</td>
										<td>
											<form:select path="fri.dispatchGroupS" cssStyle="width:100px" onchange="javascript:disableTimeFacility('fri')">
												<form:option value="" label="Select Dispatch Group"/>												
												<form:options items="${dispatchGroups}" itemLabel="name" itemValue="groupTime" />
											</form:select>
										</td>
										<td>
											<form:select path="fri.depotFacility" cssStyle="width:180px">
													<form:option value="" label="Select"/>
													<form:options items="${depotFacility}" itemLabel="name" itemValue="facilityId" />
											</form:select>
										</td>
										<td><form:errors path="fri.regionS" />&nbsp&nbsp;&nbsp;<form:errors path="fri.dispatchGroupS" />&nbsp&nbsp;&nbsp;<form:errors path="fri.depotFacility" /></td>

									</tr>
									<tr>
										<td width="60">Sat</td>
										<td>
										<form:hidden path="sat.scheduleId"/>
										<form:hidden path="sat.weekOf"/>
										<form:select path="sat.regionS" cssStyle="width:100px" onchange="javascript:disableTimeFacility('sat')">
											<form:option value="" label="Select"/>
											<form:option value="OFF" label="OFF"/>
											<form:options items="${region}" itemLabel="name" itemValue="code" />
										</form:select>
										</td>
										<td>
											<form:select path="sat.dispatchGroupS" cssStyle="width:100px" onchange="javascript:disableTimeFacility('sat')">
												<form:option value="" label="Select Dispatch Group"/>												
												<form:options items="${dispatchGroups}" itemLabel="name" itemValue="groupTime" />
											</form:select>
										</td>
										<td>
											<form:select path="sat.depotFacility" cssStyle="width:180px">
													<form:option value="" label="Select"/>
													<form:options items="${depotFacility}" itemLabel="name" itemValue="facilityId" />
											</form:select>
										</td>
										<td><form:errors path="sat.regionS" />&nbsp&nbsp;&nbsp;<form:errors path="sat.dispatchGroupS" />&nbsp&nbsp;&nbsp;<form:errors path="sat.depotFacility" /></td>

									</tr>
									<tr>
										<td width="60">Sun</td>
										<td>
										<form:hidden path="sun.scheduleId"/>
										<form:hidden path="sun.weekOf"/>
										<form:select path="sun.regionS" cssStyle="width:100px" onchange="javascript:disableTimeFacility('sun')">
											<form:option value="" label="Select"/>
											<form:option value="OFF" label="OFF"/>
											<form:options items="${region}" itemLabel="name" itemValue="code" />
										</form:select>
										</td>
										<td>
											<form:select path="sun.dispatchGroupS" cssStyle="width:100px" onchange="javascript:disableTimeFacility('sun')">
												<form:option value="" label="Select Dispatch Group"/>												
												<form:options items="${dispatchGroups}" itemLabel="name" itemValue="groupTime" />
											</form:select>
										</td>
										<td>
											<form:select path="sun.depotFacility" cssStyle="width:180px">
													<form:option value="" label="Select"/>
													<form:options items="${depotFacility}" itemLabel="name" itemValue="facilityId" />
											</form:select>
										</td>
										<td><form:errors path="sun.regionS" />&nbsp&nbsp;&nbsp;<form:errors path="sun.dispatchGroupS" />&nbsp&nbsp;&nbsp;<form:errors path="sun.depotFacility" /></td>

									</tr>
									
									</table>
							 </td>
							 </tr>	
							 <tr>
							    <td colspan="3" align="center">
								   <input type = "submit" value="&nbsp;Save&nbsp;"  />										
								
							   <input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:back();" />
							    
							   </td>   
							</tr>
							 </table>
							 </td>
					</tr>
													
				</table>
			</form:form>
		
		</td>
		<td width="25%" style="vertical-align: top;padding-right:10px;">		
		<div style="background-color:#F2F2F2; border:1px solid #000;height:240px;overflow-y:auto;">
				<br/>
				
				<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screencontent" colspan="3">
							Copy <b> <%= copyWeekOf %> </b>  
						</td>
					</tr>
					<tr>
						<td class="screencontent" colspan="3">
							<select id="fromTo" name="fromTo" >
                          		<option value="FROM">From</option>
                          		<option value="TO">To</option>                      			
                      		</select>		  
						</td>
					</tr>	
					<tr>
						<td class="screencontent" colspan="3">
							<span ><input maxlength="10" size="10" name="cpScheduleDate" id="cpScheduleDate" />
                    			<a href="#" id="trigger_cpScheduleDate" style="font-size: 9px;">
                        			<img src="./images/icons/calendar.gif" width="15" height="15" border="0" alt="Select Date" title="Select Date" />
                    			</a>
                     			<script language="javascript">                 
                     		 		Calendar.setup(
                      				{
                        				showsTime : false,
                       					electric : false,
                        				inputField : "cpScheduleDate",
                        				ifFormat : "%m/%d/%Y",
                        				singleClick: true,
                        				button : "trigger_cpScheduleDate" 
                       				}
                      				);
                      			</script>
                  			</span>
                  			&nbsp;&nbsp;<select id="scribDay" name="scribDay" >
                          			<option value="All">--All Days</option>
                      				<option value="MON">Monday</option>
                      				<option value="TUE">Tuesday</option>
                      				<option value="WED">Wednesday</option>
                      				<option value="THU">Thurdsay</option>
                      				<option value="FRI">Friday</option>
                      				<option value="SAT">Saturday</option>
                      				<option value="SUN">Sunday</option>
                    	 	</select>
                  		</td>
                 	</tr> 
                 <tr><td colspan="3">&nbsp;</td></tr> 
                 <tr> 
                  	<td colspan="3" align="center"><input type = "button" value="Execute" onclick="javascript:copyWeek();" /></td>
				 </tr>
				
				 <tr>
						<td class="screencontent" style="border-bottom:1px solid #000" colspan="3">
							&nbsp; 
						</td>
				</tr>
				<tr>
						<td class="screencontent" colspan="3">
							Copy <b> <%= copyWeekOf %> </b>  
						</td>
					</tr>
					<tr>
						<td class="screencontent" colspan="3">
							<select id="fromToMaster" name="fromToMaster" >
                          		<option value="FROM">From</option>
                          		<option value="TO">To</option>                      			
                      		</select>		  
						</td>
					</tr>	
				 <tr>
						<td class="screencontent" colspan="3">
							&nbsp;&nbsp;&nbsp;&nbsp;<b>Master(01/01/1900)</b> 
							&nbsp;&nbsp;<select id="scribDayMaster" name="scribDayMaster" >
                          		<option value="All">--All Days</option>
                      			<option value="MON">Monday</option>
                      			<option value="TUE">Tuesday</option>
                      			<option value="WED">Wednesday</option>
                      			<option value="THU">Thurdsay</option>
                      			<option value="FRI">Friday</option>
                      			<option value="SAT">Saturday</option>
                      			<option value="SUN">Sunday</option>
                    	 	</select>
						</td>
				</tr>
				<tr>
						<td colspan="3">&nbsp;</td>
                 </tr> 
				 <tr> 
                  		<td colspan="3" align="center"><input type = "button" value="Execute" onclick="javascript:copyMaster();" /></td>             	
				  </tr>
				</table>	
			</div>
			</td>
			</tr>
			</table>
		 </div>
		 </div>
		 <%@ include file='i_schedulecopy.jspf'%>
		 <%@ include file='i_teamtree.jspf'%>

		 <script>

				function disableTimeFacility(day) {
					var f = document.forms["scheduleForm"];
					var value = eval("f['" + day + ".regionS'].value");
					if (value == 'OFF') {
						eval("f['" + day + ".dispatchGroupS'].disabled=true");
						eval("f['" + day + ".dispatchGroupS'].value=''");
						eval("f['" + day + ".depotFacility'].disabled=true");
						eval("f['" + day + ".depotFacility'].value=''");
					} else {
						eval("f['" + day + ".dispatchGroupS'].disabled=false");
						if (value == 'Depot') {
							eval("f['" + day
									+ ".depotFacility'].disabled=false");
						} else {
							eval("f['" + day + ".depotFacility'].value=''");
							eval("f['" + day
									+ ".depotFacility'].disabled=true");
						}
					}
				}
				disableTimeFacility("mon");
				disableTimeFacility("tue");
				disableTimeFacility("wed");
				disableTimeFacility("thu");
				disableTimeFacility("fri");
				disableTimeFacility("sat");
				disableTimeFacility("sun");

				function back() {
					var filters = unescape(getParameter("filter"));
					var params = filters.split("&");
					var planForm = document.forms["employee"];
					for ( var i = 0; i < params.length; i++) {
						var param = params[i].split("=");
						if (param[0] != 'ec_f_employeeId') {
							add_input(planForm, "hidden", param[0], param[1]);
						}
					}
					planForm.submit();
				}
				showTeamTree();
			</script>
	<form name="employee" action="employee.do?empstatus=S" method="post">  </form>
	</tmpl:put>
	 
</tmpl:insert>
