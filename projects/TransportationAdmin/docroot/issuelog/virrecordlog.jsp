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
	<tmpl:put name='title' direct='true'> Operations : Maintenance Log : <%=pageTitle%></tmpl:put>
	
	<tmpl:put name='hasSubs' direct='true'>subs</tmpl:put>

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
	
	<div class="contentroot">
				<div class="scrTitle" style="float:left;padding:3px 0 0 6px;"> <%= pageType == null ? pageTitle : "" %> &nbsp;&nbsp;</div>
				<% if(pageType == null && !"M".equalsIgnoreCase(request.getParameter("recordType"))) { %>
					
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
								singleClick: true,
								button : "trigger_createDate" 
							   }
							  );
						  </script>
					</div>
					<div style="float:left;text-align:center;font-weight:bold">Entered By<br>&nbsp;
						 <select id="employee" name="employee" style="width:200px;">
							  <option value=""></option> 
							  <c:forEach var="e" items="${employees}">
								  <c:choose>
									<c:when test="${employee == e.employeeId}" > 
									  <option selected value="<c:out value="${e.employeeId}"/>"><c:out value="${e.name}"/></option>
									</c:when>
									<c:otherwise> 
									  <option value="<c:out value="${e.employeeId}"/>"><c:out value="${e.name}"/></option>
									</c:otherwise> 
								  </c:choose>      
								</c:forEach>   
					   </select>
					</div>
					<div style="float:left;text-align:center;font-weight:bold">Truck Number<br>&nbsp;
						<input maxlength="40" size="20" name="truckNumber" id="truckNumber" value="<%= truckNumberVal %>" style="width:100px" />
					</div>&nbsp;&nbsp;&nbsp;
					<div style="float:left;"><br>
						<span>&nbsp;<input id="view_button" type="image" alt="View" src="./images/icons/view.gif" onclick="javascript:doVIRRecordLink('createDate','employee','truckNumber','virrecordlog.do');" onmousedown="this.src='./images/icons/view_ON.gif'" />
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
	
	<%if(recordType == null && pageType == null){%>
		
		
				<ec:table items="virRecords"   action="${pageContext.request.contextPath}/virrecordlog.do"
					imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
					width="98%" filterable="true" showPagination="true" rowsDisplayed="25" view="fd" >
					
					<ec:exportPdf fileName="virrecords.pdf" tooltip="Export PDF" 
							  headerTitle="Issue Type" />
					<ec:exportXls fileName="virrecords.xls" tooltip="Export PDF" />
					<ec:exportCsv fileName="virrecords.csv" tooltip="Export CSV" delimiter="|"/>

					<ec:row interceptor="obsoletemarker">
					  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="selectcol"
									property="id" />
					  <ec:column property="id" alias="virId" title="VIR ID"/> 
					  <ec:column property="truckNumber" title="Truck" />
					  <ec:column property="vendor" title="Vendor" />
					  <ec:column property="createdDate" title="Created Date" />
					  <ec:column property="issueType.issueTypeName" title="Issue Type" />
					  <ec:column property="issueSubType.issueSubTypeName" title="Issue SubType" />
					  <ec:column property="damageLocation" title="Front/ Back" />
					  <ec:column property="issueSide" title="Driver/ Passenger" />
					  <ec:column property="comments" title="Comments" />
  					  <ec:column property="maintenanceIssue.id" title="Maintenance IssueID" />
    				  <ec:column cell="bool" property="maintenanceIssue.truckInService" title="In Service?" />
  					  <!--<ec:column cell="issueStatusCell" property="maintenanceIssue.issueStatus" title="Issue Status" />-->
  					  <ec:column property="driver" title="Reporting Driver" />
  					  <ec:column property="createdBy" title="Entered By" />
					</ec:row>
				</ec:table>
	<%}%>
	
	
	
	<%if("I".equalsIgnoreCase(request.getParameter("pageType"))){%>
		
		
				<ec:table items="issueTypes"   action="${pageContext.request.contextPath}/maintenancelog.do?pageType=I"
					imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Issue Types"
					width="98%" filterable="true" showPagination="true" rowsDisplayed="25" view="fd" >
					
					<ec:exportPdf fileName="issuetype.pdf" tooltip="Export PDF" 
							  headerTitle="Issue Type" />
					<ec:exportXls fileName="issuetype.xls" tooltip="Export PDF" />
					<ec:exportCsv fileName="issuetype.csv" tooltip="Export CSV" delimiter="|"/>
						
					<ec:row interceptor="obsoletemarker">                
					  <ec:column title=" " width="4px" 
		                    filterable="false" sortable="false" cell="selectcol"
				            property="issueTypeId" />
					  <ec:column property="issueTypeName"  title="Issue Name"/> 
					  <ec:column property="issueTypeDescription" title="Issue Type Description" />
					  <ec:column cell="bool" property="status" title="isActive" />
					  <ec:column property="createdDateDisplay" title="Created Date" />
					  <ec:column property="createdBy" title="Created By" />
					</ec:row>
				</ec:table>
	<%}%>
	<%if("S".equalsIgnoreCase(request.getParameter("pageType"))){%>
		
				<ec:table items="issueSubTypes"   action="${pageContext.request.contextPath}/maintenancelog.do?pageType=S"
					imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Issue Sub Types"
					width="98%" filterable="true" showPagination="true" rowsDisplayed="25" view="fd" >
					
					<ec:exportPdf fileName="issuesubtype.pdf" tooltip="Export PDF" 
							  headerTitle="Issue Type" />
					<ec:exportXls fileName="issuesubtype.xls" tooltip="Export PDF" />
					<ec:exportCsv fileName="issuesubtype.csv" tooltip="Export CSV" delimiter="|"/>
						
					<ec:row interceptor="obsoletemarker">                
					  <ec:column title=" " width="4px" 
		                    filterable="false" sortable="false" cell="selectcol"
				            property="issueSubTypeId" />
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
	
	</tmpl:put> 

</tmpl:insert>