<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<% 
	String pageTitle = "";
	String recordType = request.getParameter("recordType");
	String pageType = request.getParameter("pageType");
	if((recordType == null) || ("V".equalsIgnoreCase(recordType))) {
		pageTitle = "VIR Record";
	}else if("M".equalsIgnoreCase(recordType)) {
		pageTitle = "Maintenance Record";
	}

	String truckNumberVal = request.getParameter("truckNumber") != null ? request.getParameter("truckNumber") : "";
%>

<tmpl:insert template='/common/sitelayout.jsp'>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>	
	</tmpl:put>
	

<tmpl:put name='title' direct='true'>Create VIR Record</tmpl:put>

	<tmpl:put name='content' direct='true'>
		
		<script src="js/jsonrpc.js" language="javascript" type="text/javascript"></script>
		<script src="js/maintenancelog.js" language="javascript" type="text/javascript"></script>

		<div class="MNM001 subsub or_999">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM001 <% if(pageType == null && !"M".equalsIgnoreCase(request.getParameter("recordType"))&&!"C".equalsIgnoreCase(request.getParameter("recordType"))) { %>activeL<% } %>">&nbsp;</div>
			
			<div class="subtab <% if(pageType == null && !"M".equalsIgnoreCase(request.getParameter("recordType"))&&!"C".equalsIgnoreCase(request.getParameter("recordType"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="virrecordlog.do" class="<% if(pageType == null && !"M".equalsIgnoreCase(request.getParameter("recordType"))&&!"C".equalsIgnoreCase(request.getParameter("recordType"))) { %>MNM001<% } %>">VIR Record</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if(pageType == null && !"M".equalsIgnoreCase(request.getParameter("recordType"))&&!"C".equalsIgnoreCase(request.getParameter("recordType"))) { %>activeR<% } %>">&nbsp;</div>		
		
			<div class="sub_tableft sub_tabL_MNM001 <% if(pageType == null && "M".equalsIgnoreCase(request.getParameter("recordType"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if(pageType == null && "M".equalsIgnoreCase(request.getParameter("recordType"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="maintenancelog.do?recordType=M" class="<% if(pageType == null && "M".equalsIgnoreCase(request.getParameter("recordType"))) { %>MNM001<% } %>">Maintenance Record</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if(pageType == null && "M".equalsIgnoreCase(request.getParameter("recordType"))) { %>activeR<% } %>">&nbsp;</div>
			
		</div>
		<div class="subs_right">
			<div class="sub_tableft sub_tabL_MNM001 <% if("I".equalsIgnoreCase(request.getParameter("pageType"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("I".equalsIgnoreCase(request.getParameter("pageType"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="virrecordlog.do?pageType=I" class="<% if("I".equalsIgnoreCase(request.getParameter("pageType"))) { %>MNM001<% } %>">Issue Type</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("I".equalsIgnoreCase(request.getParameter("pageType"))) { %>activeR<% } %>">&nbsp;</div>

			<div class="sub_tableft sub_tabL_MNM001 <% if("S".equalsIgnoreCase(request.getParameter("pageType"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("S".equalsIgnoreCase(request.getParameter("pageType"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="virrecordlog.do?pageType=S" class="<% if("S".equalsIgnoreCase(request.getParameter("pageType"))) { %>MNM001<% } %>">Issue SubType</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("S".equalsIgnoreCase(request.getParameter("pageType"))) { %>activeR<% } %>">&nbsp;</div>
		</div>
	</div>
	<div class="cont_row_bottomline"><!--  --></div>
		<br/> 
		<div>
		  <form:form commandName = "virRecordForm" method="post">
				 <form:hidden path="subTypeId" />
		 		<table width="100%" cellpadding="0" cellspacing="0" border="0">
				  <tr>
					<td class="screentitle">
					  Create VIR Record
					</td>
				  </tr>
		          <tr>
				    <td class="screenmessages">
						<jsp:include page='/common/messages.jsp'/>
					</td>
				 </tr>
                 <tr>
					 <td class="screencontent">
						  <table class="forms1" style="width:40%;align:center;border:1px solid;background-color:#F7F7F7;">
							 
							<tr>
								<td>Create Date</td>
								<td>
									<input maxlength="10" size="24" id="createDate" name="createDate" value='<c:out value="${currentDate}"/>' disabled="true"/>&nbsp;
								</td>
								<td>
									<form:errors path="createDate" />&nbsp;
								</td>
							</tr>
							 <tr>
								  <td>Truck Number</td>
								  <td>                  
									<form:select path="truckNumber">
							                <form:option value="" label="--Please Select Truck"/>
										    <form:options items="${truckAssets}" itemLabel="assetNo" itemValue="assetNo" />
					                </form:select>
								  </td>
								  <td>
									  &nbsp;<form:errors path="truckNumber" />
								  </td>
							 </tr>
							 <tr>
								  <td>Vendor</td>
								  <td>                  
									<form:select path="vendor" disabled="false">
							                <form:option value="" label="--Please Select Vendor"/>
										    <form:options items="${vendors}" />
					                </form:select>
								  </td>
								  <td>
									  &nbsp;<form:errors path="vendor" />
								  </td>
							 </tr>
							  <tr>
								  <td>Issue Type</td>
								  <td>                  
									<form:select path="issueTypeId" onChange="javascript:getIssueSubTypes();">
							                <form:option value="" label="--Please Select IssueType"/>
										    <form:options items="${issueTypes}" itemLabel="issueTypeName" itemValue="issueTypeId" />
					                </form:select>
								  </td>
								  <td>
									  &nbsp;<form:errors path="issueTypeId" />
								  </td>
							 </tr>
							  <tr>
								  <td>Issue SubType</td>
								  <td>
										<form:select path="issueSubTypeId"></form:select>
								  </td>
								  <td>
									  &nbsp;<form:errors path="issueSubTypeId" />
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
								  <td>Reporting Driver</td>
								  <td>                  
										<form:select path="driver">
							                <form:option value="N/A" label="--N/A--"/>
										    <form:options items="${drivers}" itemLabel="name" itemValue="employeeId" />
					                   </form:select>
								  </td>
								  <td>
									  &nbsp;<form:errors path="driver" />
								  </td>
							 </tr>
							 <tr>
								<td>Entered By</td>
								<td>
									<input maxlength="50" size="30" id="createdBy" name="createdBy" value='<c:out value="${userId}"/>' disabled="true"/>
								</td>
								<td>
									<form:errors path="createdBy" />&nbsp;
								</td>
							</tr>
							<tr><td colspan="3">&nbsp;</td></tr>
							<tr>
									<td colspan="3" align="center">
									   <input type = "submit" value="&nbsp;Submit&nbsp;"  />&nbsp;&nbsp;
   									   <input type = "button" value="&nbsp;Add Another Issue&nbsp;"  onclick="javascript:addNewVIRRecord();" />
									</td>
							  </tr>
							  <tr><td colspan="3">&nbsp;</td></tr>
						  </table>
					</td>
			 </tr>
		 </table>
		  </form:form>
		</div>
    
  </tmpl:put>
</tmpl:insert>
