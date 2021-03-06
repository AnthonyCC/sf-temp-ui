<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<% 
	String pageTitle = "";
	String issueLog = request.getParameter("issueLog");
	if((issueLog == null) || ("V".equalsIgnoreCase(issueLog))) {
		pageTitle = "VIR Record";
	}else if("M".equalsIgnoreCase(issueLog)) {
		pageTitle = "Maintenance Record";
	}
	String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(request); 
%>

<tmpl:insert template='/common/sitelayout.jsp'>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>	
	</tmpl:put>
	
	<tmpl:put name='title' direct='true'>Create VIR Record</tmpl:put>

	<tmpl:put name='content' direct='true'>

		<script src="js/jsonrpc.js" language="javascript" type="text/javascript"></script>
		<script src="js/maintenancelog.js" language="javascript" type="text/javascript"></script>

<div class="MNM002 subsub or_999">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM002 <% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog"))
			&&!"S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeL<% } %>">&nbsp;</div>
			
			<div class="subtab <% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog")) &&!"S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="virrecordlog.do" class="<% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog")) &&!"S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>MNM002<% } %>">VIR Record</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM002 <% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog")) &&!"S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeR<% } %>">&nbsp;</div>		
		
			<div class="sub_tableft sub_tabL_MNM002 <% if("M".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("M".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="maintenancelog.do?issueLog=M" class="<% if("M".equalsIgnoreCase(request.getParameter("issueLog"))) { %>MNM002<% } %>">Maintenance Record</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM002 <% if("M".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeR<% } %>">&nbsp;</div>
			
			<div class="sub_tableft sub_tabL_MNM002 <% if("I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="virrecordlog.do?issueLog=I" class="<% if("I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>MNM002<% } %>">Issue Type</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM002 <% if("I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeR<% } %>">&nbsp;</div>

			<div class="sub_tableft sub_tabL_MNM002 <% if("S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="virrecordlog.do?issueLog=S" class="<% if("S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>MNM002<% } %>">Issue SubType</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM002 <% if("S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeR<% } %>">&nbsp;</div>
		</div>
	</div>
	<div class="cont_row_bottomline"><!--  --></div>
		<br/> 
		<div>
		 <form:form commandName = "maintenanceRecordForm" method="post">
		 <form:hidden path="issueId" /> <form:hidden path="subTypeId" /> 
		 <table width="100%" cellpadding="0" cellspacing="0" border="0">
			  <tr>
				<td class="screentitle">
				  Create Maintenance Record
				</td>
			  </tr>
			  <tr>
				<td class="screenmessages">
					<jsp:include page='/common/messages.jsp'/>
					<div id="errContainer"></div>
				</td>
			 </tr>
			 <tr>
				 <td class="screencontent">
					  <table class="forms1" style="height:660px;width:98%;align:center;border:1px dotted;background-color:#F7F7F7;">
						<tr>
							<td align="center" colspan="2" valign="top">
						   <table>
								<tr>
								<td>Created Date</td>
								<td>
									<c:if test="${!empty maintenanceRecordForm.id }">
										<c:set var="hasId" value="true"/>
									</c:if>
									<c:if test="${maintenanceRecordForm.issueStatus eq 'Open' }">
										<c:set var="isOpen" value="true"/>
									</c:if>
									<c:if test="${maintenanceRecordForm.issueStatus eq 'Verified' }">
										<c:set var="isVerified" value="true"/>
									</c:if>
									<c:if test="${maintenanceRecordForm.issueStatus eq 'Re-Verified' }">
										<c:set var="ReVerified" value="true"/>
									</c:if>

									<form:input maxlength="10" size="28" path="createDate" disabled="true"/>&nbsp;
								</td>
								<td>
									<form:errors path="createDate" />&nbsp;
								</td>
							</tr>
							 <tr>
								  <td>Truck Number</td>
								  <td>                  
									<form:select path="truckNumber" disabled="${hasId}" onChange="javascript:getVendorInfo();">
											<form:option value="" label="--Please Select Truck"/>
											<form:options items="${truckAssets}" itemLabel="assetNo" itemValue="assetNo" />
									</form:select>
								  </td>
								  <td>
									  &nbsp;<form:errors path="truckNumber" />
								  </td>
							 </tr>
							 <tr>
								  <td>Truck Location</td>
								  <td>
								  	<c:choose>
								  		<c:when test='${isOpen}'>
											<form:select path="truckLocation">
													<form:option value="N/A" label="--N/A--"/>
													<form:options items="${truckLocations}" />
											</form:select>
										</c:when>
										<c:when test='${isVerified}'>
											<form:select path="truckLocation">
													<form:option value="N/A" label="--N/A--"/>
													<form:options items="${truckLocations}" />
											</form:select>
										</c:when>
										<c:when test='${ReVerified}'>
											<form:select path="truckLocation">
													<form:option value="N/A" label="--N/A--"/>
													<form:options items="${truckLocations}" />
											</form:select>
										</c:when>
										<c:otherwise> 
											<form:select path="truckLocation" disabled="true">
													<form:option value="N/A" label="--N/A--"/>
													<form:options items="${truckLocations}" />
											</form:select>
										</c:otherwise>
									</c:choose>
								  </td>
								  <td>
									  &nbsp;<form:errors path="truckLocation" />
								  </td>
							 </tr>
							 <tr>
								  <td>Vendor</td>
								  <td>
									<form:input maxlength="50" size="28" path="vendor" readOnly="true"></form:input>
								  </td>
								  <td>
									  &nbsp;<form:errors path="vendor" />
								  </td>
							 </tr>
							
							   <tr>
								  <td>Issue Type</td>
								  <td>                  
									<form:select path="issueType" onChange="javascript:getIssueSubTypes();" >
											<form:option value="" label="--Please Select IssueType"/>
											<form:options items="${issueTypes}" itemLabel="issueTypeName" itemValue="issueTypeName" />
									</form:select>
								  </td>
								  <td>
									  &nbsp;<form:errors path="issueType" />
								  </td>
							 </tr>
							  <tr>
								  <td>Issue SubType</td>
								  <td>
										<form:select path="issueSubType"></form:select>
								  </td>
								  <td>
									  &nbsp;<form:errors path="issueSubType" />
								  </td>
							 </tr>
							  <tr>
								<td>Entered By</td>
								<td>
									<form:input maxlength="50" size="28" path="createdBy" disabled="true"></form:input>
								</td>
								<td>
									<form:errors path="createdBy" />&nbsp;
								</td>
							</tr>
						</table>
						</td>
				   </tr>
				   <tr>
					 <td align="center" valign="top">
						<table>
							<tr>
								<td>Verification Date</td>
								<td>
									<form:input maxlength="10" size="28" path="verificationDate" disabled="true"/>&nbsp;
								</td>
								<td>
									<form:errors path="verificationDate" />&nbsp;
								</td>
							</tr>
							
							<tr>
								  <td>In/Out Service</td>
								  <td>                  
									<form:select path="serviceStatus">
											<form:option value="" label="--Please Select ServiceStatus"/>
											<form:options items="${serviceStatuses}" />
									</form:select>
								  </td>
								  <td>
									  &nbsp;<form:errors path="serviceStatus" />
								  </td>
							</tr>
							<tr>
								  <td>Comments</td>
								  <td>
									  <form:textarea rows="4" cols="45" path="comments" />
								  </td>
								 <td>
									 &nbsp;<form:errors path="comments" />
								 </td>
							 </tr>
							 <tr>
								<td>Days Open</td>
								<td>
									<form:input maxlength="50" size="30" path="daysOpen" disabled="true"></form:input>
								</td>
								<td>
									<form:errors path="daysOpen" />&nbsp;
								</td>
							</tr>
							 <tr>
								<td>Verified By</td>
								<td>
									<form:input maxlength="50" size="30" path="verifiedBy" disabled="true"></form:input>
								</td>
								<td>
									<form:errors path="verifiedBy" />&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="3" align="center">
								<c:choose>
									<c:when test='${isOpen}'>
										<input type = "submit" value="&nbsp;Verify&nbsp;" />
									</c:when>
									<c:when test='${isVerified}'>
										<input type = "submit" value="&nbsp;Re-Verify&nbsp;" name="reverify"/>
									</c:when>
									<c:when test="${maintenanceRecordForm.issueStatus eq 'Rejected' }">
										<input type = "submit" value="&nbsp;Verify&nbsp;" disabled="true"/>
									</c:when>
									<c:otherwise> 
										<input type = "submit" value="&nbsp;Verify&nbsp;" disabled="true"/>
									</c:otherwise>
								</c:choose>
									
								</td>
							</tr>
						</table>
						</td>
						<td align="center" valign="top">
						<table>
						<tr>
							<td>Estimated Repair Date</td>
							<td>
								<c:choose>
									<c:when test='${isOpen}'>
										<form:input maxlength="10" size="28" path="estimatedRepairDate" />
										<a href="#" id="trigger_date" style="font-size: 9px;">
                        					<img src="./images/icons/calendar.gif" width="16" height="16" border="0">
				                    	</a>
									</c:when>
									<c:when test='${isVerified}'>
										<form:input maxlength="10" size="28" path="estimatedRepairDate" />
										<a href="#" id="trigger_date" style="font-size: 9px;">
                        					<img src="./images/icons/calendar.gif" width="16" height="16" border="0">
				                    	</a>
									</c:when>
									<c:otherwise> 
										<form:input maxlength="10" size="28" path="estimatedRepairDate" disabled="true"/>
									</c:otherwise>
								</c:choose>
								
								 <script language="javascript">
									  Calendar.setup(
									   {
										showsTime : false,
										electric : false,
										inputField : "estimatedRepairDate",
										ifFormat : "%m/%d/%Y",
										singleClick: true,
										button : "trigger_date" 
									   }
									  );
								  </script>
							</td>
							<td>
								<form:errors path="estimatedRepairDate" />&nbsp;
							</td>
						</tr>
						<tr>
							<td>Actual Repair Date</td>
							<td>
								<c:choose>
									<c:when test='${isVerified}'>
										<form:input maxlength="10" size="28" path="actualRepairDate" />
										<a href="#" id="trigger_actualRepairDate" style="font-size: 9px;">
                        					<img src="./images/icons/calendar.gif" width="16" height="16" border="0">
				                    	</a>
									</c:when>
									<c:when test='${ReVerified}'>
										<form:input maxlength="10" size="28" path="actualRepairDate" />
										<a href="#" id="trigger_actualRepairDate" style="font-size: 9px;">
                        					<img src="./images/icons/calendar.gif" width="16" height="16" border="0">
				                    	</a>
									</c:when>
									<c:otherwise> 
										<form:input maxlength="10" size="28" path="actualRepairDate" disabled="true"/>
									</c:otherwise>
								</c:choose>
								
								 <script language="javascript">
									  Calendar.setup(
									  {
										showsTime : false,
										electric : false,
										inputField : "actualRepairDate",
										ifFormat : "%m/%d/%Y",
										singleClick: true,
										button : "trigger_actualRepairDate"
									   }
									  );
								  </script>
							</td>
							<td>
								<form:errors path="actualRepairDate" />&nbsp;
							</td>
						</tr>
						<tr>
							  <td>Front/Back</td>
							  <td>                  
									<form:select path="damageLocation">
										<form:option value="N/A" label="--N/A--"/>
										<form:options items="${damageLocations}" />
								   </form:select>
							  </td>
							  <td>
								  &nbsp;<form:errors path="damageLocation" />
							  </td>
						</tr>
						<tr>
							  <td>Driver/Passenger</td>
							  <td>                  
									<form:select path="issueSide">
										<form:option value="N/A" label="--N/A--"/>
										<form:options items="${issueSides}"/>
								   </form:select>
							  </td>
							  <td>
								  &nbsp;<form:errors path="issueSide" />
							  </td>
						</tr>
						<tr>
							<td>Repaired By</td>
							<td>
								<form:input maxlength="50" size="28" path="repairedBy" disabled="true"/>
							</td>
							<td>
								<form:errors path="repairedBy" />&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="3" align="center">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="3" align="center">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="3" align="center">
								<c:choose>
									<c:when test='${isVerified}'>
										<input type = "submit" value="&nbsp;Repair&nbsp;" />
									</c:when>
									<c:when test='${ReVerified}'>
										<input type = "submit" value="&nbsp;Repair&nbsp;" />
									</c:when>
									<c:otherwise> 
										<input type = "submit" value="&nbsp;Repair&nbsp;" disabled="true" />
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						</table>
						</td>
					 </tr>
				 <tr>
					<td colspan="2" valign="top" align="center">
								<c:choose>
									<c:when test='${hasId}'>
										<input type = "submit" value="&nbsp;Save&nbsp;" disabled="true"/>
									</c:when>
									<c:when test="${maintenanceRecordForm.issueStatus eq 'Rejected' }">
										<input type = "submit" value="&nbsp;Save&nbsp;" disabled="true"/>
									</c:when>
									<c:otherwise> 
										<input type = "submit" value="&nbsp;Save&nbsp;"/>
									</c:otherwise>
								</c:choose>
						
						<input type = "button" value="&nbsp;Back&nbsp;" onclick="javascript:doBack();" />
						<c:choose>
							<c:when test='${hasId}'>
								<input type = "button" value="&nbsp;Reject&nbsp;" onclick="javascript:rejectIssue('<%=userId%>');" />
							</c:when>
							<c:otherwise> 
								<input type = "button" value="&nbsp;Reject&nbsp;" disabled="true" onclick="javascript:rejectIssue('<%=userId%>');" />
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
		
		</table>
		  </form:form>
		</div>	
		<script>
			window.onload = function(){
				var issueTypes = document.getElementById("issueType");
				// get selected issueType from dropdown list  
				var issueTypeId = issueTypes.options[issueTypes.selectedIndex].value;
				if(issueTypeId.length != 0){
					getIssueSubTypes();
				}

				if(document.getElementById("subTypeId")){
					subTypeId = document.getElementById("subTypeId").value;
				}
			}
		</script>
  </tmpl:put>
</tmpl:insert>
<form name="maintenancelog" action="maintenancelog.do?issueLog=M" method="post">  </form>