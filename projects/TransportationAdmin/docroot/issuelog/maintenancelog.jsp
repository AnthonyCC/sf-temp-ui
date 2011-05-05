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
	}else if("M".equalsIgnoreCase(issueLog)) {
		pageTitle = "Maintenance Record";
	}

	int trucksInService =(Integer)request.getAttribute("trucksInService");
	int trucksOutOfService = (Integer)request.getAttribute("trucksOutOfService");

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
				
				<div class="subtab <% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog")) &&!"I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeT<% } %>">
					<div class="minwidth"><!-- --></div>
					<a href="virrecordlog.do" class="<% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog")) &&!"I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>MNM001<% } %>">VIR Record</a>
				</div>
				<div class="sub_tabright sub_tabR_MNM001 <% if(!"M".equalsIgnoreCase(request.getParameter("issueLog"))&&!"I".equalsIgnoreCase(request.getParameter("issueLog")) &&!"I".equalsIgnoreCase(request.getParameter("issueLog"))) { %>activeR<% } %>">&nbsp;</div>		
			
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
				<div class="scrTitle" style="float:left;padding:3px 0 0 6px;"><%=pageTitle%> </div>
			
					<div style="float:left;text-align:center;font-weight:bold;font-size:11px;">Service Status<br>&nbsp;
						<select id="serviceStatus" name="serviceStatus" style="font-size:11px;">
							<option value="">--Please Select</option>
							  <c:forEach var="serviceName" items="${serviceStatuses}">
								  <c:choose>
									<c:when test="${serviceStatus == serviceName}" > 
									  <option selected value="<c:out value="${serviceName}"/>"><c:out value="${serviceName}"/></option>
									</c:when>
									<c:otherwise> 
									  <option value="<c:out value="${serviceName}"/>"><c:out value="${serviceName}"/></option>
									</c:otherwise> 
								  </c:choose>
								</c:forEach>
					    </select>
					</div>
					<div style="float:left;text-align:center;font-weight:bold;font-size:11px;">Issue Status<br>&nbsp;
						 <select id="issueStatus" name="issueStatus" style="font-size:11px;">
							<option value="">--Please Select</option>
							  <c:forEach var="statusName" items="${issueStatuses}">
								  <c:choose>
									<c:when test="${issueStatus == statusName}" > 
									  <option selected value="<c:out value="${statusName}"/>"><c:out value="${statusName}"/></option>
									</c:when>
									<c:otherwise> 
									  <option value="<c:out value="${statusName}"/>"><c:out value="${statusName}"/></option>
									</c:otherwise> 
								  </c:choose>      
								</c:forEach>   
					    </select>
					</div>
					&nbsp;&nbsp;&nbsp;
					<div style="float:left;"><br/>
						<span>&nbsp;
							<input id="view_button" type="image" alt="View" src="./images/icons/view.gif" onclick="javascript:doMaintenanceIssueLink('issueStatus','serviceStatus','maintenancelog.do');" onmousedown="this.src='./images/icons/view_ON.gif'" />
						</span>
					</div>
					&nbsp;&nbsp;&nbsp;
					<div style="float:right;font-weight:bold;"><br/>
						<span><input type="image" src="./images/icons/o-icon.gif" />&nbsp;Open</span>
						<span><input type="image" src="./images/icons/v-icon.gif" />&nbsp;Verified</span>
						<span><input type="image" src="./images/icons/rv-icon.gif" />&nbsp;Re-Verified</span>
						<span><input type="image" src="./images/icons/r-icon.gif" />&nbsp;Resolved</span>
						<span><input type="image" src="./images/icons/i-icon.gif" />&nbsp;Rejected</span>&nbsp;&nbsp;&nbsp;
					</div>
					
	</div>
	<BR/><BR/><BR/>

	<div class="cont_topright">
			<div class="cont_row">
				
				<div class="cont_Litem">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<div style="align:right;font-weight:bold;padding-left:20px;" >
						<span class="orphanScenario">Trucks In-Service: <%= trucksInService %></span><br/><br/>
						<span class="defaultScenario">Trucks Out-Of-Service: <%= trucksOutOfService %></span>
					</div>
				</div>
				<div class="cont_Ritem">
			
	 			<form id="maintenanceRecordListForm" action="" method="post">  
				<ec:table items="maintenanceRecords" action="${pageContext.request.contextPath}/maintenancelog.do?issueLog=M"
					imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
					width="98%" filterable="true" showPagination="true" rowsDisplayed="25" view="fd" form="maintenanceRecordListForm" >
					
					<ec:exportPdf fileName="maintenancerecords.pdf" tooltip="Export PDF" 
							  headerTitle="Issue Type" />
					<ec:exportXls fileName="maintenancerecords.xls" tooltip="Export PDF" />
					<ec:exportCsv fileName="maintenancerecords.csv" tooltip="Export CSV" delimiter="|"/>
						
					<ec:row interceptor="obsoletemarker">
					  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="selectcol"
									property="id" />
					  <ec:column property="id" alias="maintenanceId" title="ID"/> 
					  <ec:column property="createdDate" title="Issue Date" />
					  <ec:column property="displayVerificationDate" title="Verification Date" />
					  <ec:column property="displayEstimatedRepairDate" title="Estimated Repair Date" />
					  <ec:column property="displayActualRepairDate" title="Actual Repair Date" />
					  <ec:column property="daysOpen" title="Days Open" />
					  <ec:column property="verifiedBy" title="Verified By" />
					  <ec:column property="repairedBy" title="Repaired By" />
					  <ec:column property="truckNumber" title="Truck" />
 					  <ec:column property="vendor" title="Vendor" />
  					  <ec:column property="issueType" title="Issue Type" />
   					  <ec:column property="issueSubType" title="Issue SubType" />
					  <ec:column property="damageLocation" title="Front/ Back" />
					  <ec:column property="issueSide" title="Driver/ Passenger" />
					  <ec:column property="comments" title="Comments" />
  					  <ec:column cell="bool" property="truckInService" title="In Service?" />
  					  <ec:column cell="issueStatusCell" property="issueStatus" title="Issue Status" />
  					  <ec:column property="createdBy" title="Entered By" />
					</ec:row>
				</ec:table>
				</form>
				</div>
			</div>
		</div>
	<script>
		<% if("TrnAdmin".equalsIgnoreCase(com.freshdirect.transadmin.security.SecurityManager.getUserRole(request))
				||	"TrnPlanning".equalsIgnoreCase(com.freshdirect.transadmin.security.SecurityManager.getUserRole(request))
				||  "TrnOperations".equalsIgnoreCase(com.freshdirect.transadmin.security.SecurityManager.getUserRole(request))){ 
			
		%> 
			addMultiParamRowHandlersFilter('ec_table', 'rowMouseOver', 'editmaintenancelog.do','id',0, 0, false, 'issueLog','<%=issueLog%>');

			function getFilterTestValue() {
	             var filters = getFilterValue(document.getElementById("maintenanceRecordListForm"),false);
	             var param1 = document.getElementById("serviceStatus").value;
	     		 var param2 = document.getElementById("issueStatus").value;
	             filters+="&issueStatus="+param2;
	             filters+="&serviceStatus="+param1;
	             return escape(filters);
	       }
		<%}%>
	</script>
	</tmpl:put> 

</tmpl:insert>