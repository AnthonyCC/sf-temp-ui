<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
  pageContext.setAttribute("HAS_ADDBUTTON", "true");
  pageContext.setAttribute("HAS_DELETEBUTTON", "false");
%>

<% 
	String pageTitle = "";
	String issueLog = request.getParameter("issueLog");
	if((issueLog == null) || ("V".equalsIgnoreCase(issueLog))) {
		pageTitle = "VIR Record";
	}else if("I".equalsIgnoreCase(issueLog)) {
		pageTitle = "Issue Types";
	}else if("S".equalsIgnoreCase(issueLog)) {
		pageTitle = "Issue Sub Types";
	}
	String truckNumberVal = request.getParameter("truckNumber") != null ? request.getParameter("truckNumber") : "";
%>

<tmpl:insert template='/common/sitelayout.jsp'>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>	
	</tmpl:put>	
	<tmpl:put name='title' direct='true'> Operations : Maintenance Log : <%=pageTitle%></tmpl:put>
	
	<tmpl:put name='hasSubs' direct='true'>subs</tmpl:put>

	<tmpl:put name='content' direct='true'>

		<script src="js/jsonrpc.js" language="javascript" type="text/javascript"></script>
		<script src="js/maintenancelog.js" language="javascript" type="text/javascript"></script>

		<div class="MNM001 subsub or_999">
		<div class="subs_left">
			<div class="sub_tableft sub_tabL_MNM001 <% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog"))
			&&!"S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeL<% } %>">&nbsp;</div>
			
			<div class="subtab <% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog")) &&!"S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="virrecordlog.do" class="<% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog")) &&!"S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>MNM001<% } %>">VIR Record</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog")) &&!"S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeR<% } %>">&nbsp;</div>		
		
			<div class="sub_tableft sub_tabL_MNM001 <% if("M".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("M".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="maintenancelog.do?issueLog=M" class="<% if("M".equalsIgnoreCase(request.getParameter("issueLog"))) { %>MNM001<% } %>">Maintenance Record</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("M".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeR<% } %>">&nbsp;</div>
			
			<div class="sub_tableft sub_tabL_MNM001 <% if("I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="virrecordlog.do?issueLog=I" class="<% if("I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>MNM001<% } %>">Issue Type</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeR<% } %>">&nbsp;</div>

			<div class="sub_tableft sub_tabL_MNM001 <% if("S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="virrecordlog.do?issueLog=S" class="<% if("S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>MNM001<% } %>">Issue SubType</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeR<% } %>">&nbsp;</div>		
		</div>
	</div>
	<div class="cont_row_bottomline"><!--  --></div>
	
	<div class="contentroot">
				<div class="scrTitle" style="float:left;padding:3px 0 0 6px;"> <%= pageTitle %> &nbsp;&nbsp;</div>
				<% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog"))	&&!"S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>
					
					<div style="float:left;text-align:center;font-weight:bold">Create Date<br>
						<input maxlength="10" size="10" name="createDate" id="createDate" style="width:75px" value='<c:out value="${createDate}"/>' />
                    	<a href="#" id="trigger_createDate" style="font-size: 9px;">
                        	<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date">
                    	</a>
						 <script language="javascript">
							  Calendar.setup(
							  {
								showsTime : false,
								electric : false,
								inputField : "createDate",
								ifFormat : "%m/%d/%Y",
								singleIlick: true,
								button : "trigger_createDate" 
							   }
							  );
						  </script>
					</div>
					<div style="float:left;text-align:center;font-weight:bold">Truck Number<br>&nbsp;
						<input maxlength="40" size="20" name="truckNumber" id="truckNumber" value="<%= truckNumberVal %>" style="width:100px" />
					</div>&nbsp;&nbsp;&nbsp;
					<div style="float:left;"><br>
						<span>&nbsp;<input id="view_button" type="image" alt="View" src="./images/icons/view.gif" onclick="javascript:doVIRRecordLink('createDate','truckNumber','virrecordlog.do');" onmousedown="this.src='./images/icons/view_ON.gif'" />
						</span>
						<input style="font-size:11px" type = "button" height="18" value="Add Issue Type" onclick="javascript:showIssueTypeForm();" <%= (com.freshdirect.transadmin.security.SecurityManager.isUserAdmin(request) ?  " " : " disabled=\"disabled\"") %> />
						<input style="font-size:11px" type = "button" height="18" value="Add Issue SubType" onclick="javascript:showissueSubTypeForm();" <%= (com.freshdirect.transadmin.security.SecurityManager.isUserAdmin(request) ?  " " : " disabled=\"disabled\"") %> />
					</div>
					<%
						String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(request);
						String userRole = com.freshdirect.transadmin.security.SecurityManager.getUserRole(request);
					%>
				<% } %>
	
	<%@ include file='i_addissuetype.jspf'%>
	<%@ include file='i_addissuesubtype.jspf'%>
	</div>
	<BR/><BR/><BR/>&nbsp;
	
	<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
	
	<%if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog"))	&&!"S".equalsIgnoreCase(request.getParameter("issueLog"))) { %>
				<ec:table items="virRecords"   action="${pageContext.request.contextPath}/virrecordlog.do"
					imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
					width="98%" filterable="true" showPagination="true" rowsDisplayed="25" view="fd" >
					
					<ec:exportPdf fileName="virrecords.pdf" tooltip="Export PDF" 
							  headerTitle="Issue Type" />
					<ec:exportXls fileName="virrecords.xls" tooltip="Export PDF" />
					<ec:exportCsv fileName="virrecords.csv" tooltip="Export ISV" delimiter="|"/>

					<ec:row interceptor="obsoletemarker">
					  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="selectcol"
									property="id" />
					  <ec:column width="15px" property="id" alias="virId" title="VIR ID"/> 
					  <ec:column width="10px" property="truckNumber" title="Truck" />
					  <ec:column width="15px" property="vendor" title="Vendor" />
					  <ec:column width="6px" property="createdDate" title="Created Date" />
					  <ec:column width="250px" filterable="true" property="virRecordIssues" cell="issueLogCell" title="Issues&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Maintenance Issue&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;DamageLocation&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;IssueSide&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Comments"/>
  					  <ec:column width="10px" property="reportingDriver.name" title="Reporting Driver" />
  					  <ec:column width="10px" property="createdBy" title="Entered By" />
					</ec:row>
				</ec:table>
	<%}%>
	
	<%if("I".equalsIgnoreCase(request.getParameter("issueLog"))){%>
		
		
				<ec:table items="issueTypes"   action="${pageContext.request.contextPath}/maintenancelog.do?issueLog=I"
					imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
					width="98%" filterable="false" showPagination="false" rowsDisplayed="25" view="fd" >

					<ec:row interceptor="obsoletemarker">                
					  <ec:column property="issueTypeName"  title="Issue Name"/> 
					  <ec:column property="issueTypeDescription" title="Issue Type Description" />
					  <ec:column cell="bool" property="status" title="isActive" />
					  <ec:column property="createdDateDisplay" title="Created Date" />
					  <ec:column property="createdBy" title="Created By" />
					</ec:row>
				</ec:table>
	<%}%>
	<%if("S".equalsIgnoreCase(request.getParameter("issueLog"))){%>
		
				<ec:table items="issueSubTypes"   action="${pageContext.request.contextPath}/maintenancelog.do?issueLog=S"
					imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
					width="98%" filterable="false" showPagination="false" rowsDisplayed="25" view="fd" >
					
					
						
					<ec:row interceptor="obsoletemarker">                
					  <ec:column property="issueSubTypeName"  title="Issue Subtype Name"/> 
					  <ec:column property="issueSubTypeDescription" title="Issue SubType Description" />
					  <ec:column property="issueType.issueTypeName" title="Issue Type" />
					  <ec:column cell="bool" property="status" title="isActive" />
					  <ec:column property="createdDateDisplay" title="Created Date" />
					  <ec:column property="createdBy" title="Created By" />
					</ec:row>
				</ec:table>
			
	<%}%>
				</div>
			</div>
		</div>

<style>
.eXtremeTable .tableHeader {
	text-align:center;
}

.eXtremeTable .tableHeaderSort {
	text-align:center;
}

#ec_table td table td, td.tableHeader table {
    width:200px;
}

#ec_table td table td.employee_on, #ec_table td table td.employee_off,#ec_table td table td.damageLocation,#ec_table td table td.issueSide, td.tableHeader table {
    width:80px;
}

#ec_table td table td.issues, td.tableHeader table {
    width:200px;
}

#ec_table td table td.noMaintenanceIssue, td.tableHeader table {
    width:100px;
}
</style>

	</tmpl:put> 
</tmpl:insert>