<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<%    
  pageContext.setAttribute("HAS_ADDBUTTON", "false");
  pageContext.setAttribute("HAS_DELETEBUTTON", "false");
%>

<% 
	String pageTitle = "Truck Preference";
%>
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'> Operations : Employee : <%=pageTitle%></tmpl:put>
		
	<tmpl:put name='hasSubs' direct='true'>subs</tmpl:put>

  <tmpl:put name='content' direct='true'>
  
	<div class="MNM001 subsub or_999">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM001 <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))&&!"S".equalsIgnoreCase(request.getParameter("empstatus")) && !"P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))&&!"S".equalsIgnoreCase(request.getParameter("empstatus")) && !"P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do" class="<% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))&&!"S".equalsIgnoreCase(request.getParameter("empstatus")) && !"P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Active</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))&&!"S".equalsIgnoreCase(request.getParameter("empstatus")) && !"P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>		
		
			<div class="sub_tableft sub_tabL_MNM001 <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=T" class="<% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Terminated</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>
			
			
			<div class="sub_tableft sub_tabL_MNM001 <% if("S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=S" class="<% if("S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Schedule</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>
			
			<div class="sub_tableft sub_tabL_MNM001 <% if("C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=C" class="<% if("C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Team</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>

			<div class="sub_tableft sub_tabL_MNM001 <% if("P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=P" class="<% if("P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Preference</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("P".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>
		</div>
	</div>
	<div class="cont_row_bottomline"><!--  --></div>

	<div class="contentroot">
		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem">
					<span class="scrTitle">
						<%=pageTitle%>
					</span>
				</div>
			</div>
		</div>

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
					<form id="employeeTruckPrefListForm" action="" method="post">
					 	<ec:table items="empTruckPrefs" action="${pageContext.request.contextPath}/employee.do?empstatus=P"
						imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""		
						width="98%"  view="fd" form="employeeTruckPrefListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
							<ec:exportPdf fileName="transportationemployee.pdf" tooltip="Export PDF"	headerTitle="" />
							<ec:exportXls fileName="transportationemployee.xls" tooltip="Export PDF" />
							<ec:exportCsv fileName="transportationemployee.csv" tooltip="Export CSV" delimiter="|"/>
								
							<ec:row interceptor="obsoletemarker">
								
								<ec:column title=" " width="5px" 
										filterable="false" sortable="false" cell="selectcol"
										property="employeeId" />  
								
								<ec:column property="trnStatus1" title="Status" width="5px"/>	
								<ec:column property="firstName" title="First Name"/>
								<ec:column property="lastName" title="Last Name"/>
								<ec:column alias="kronosId" property="employeeId" title="KronosID" width="5px"/>
								<ec:column property="jobType" title="JobType"/>
								<ec:column property="employeeRoleType" title="Roles"/>
								<ec:column property="leadInfoEx.name" title="Lead"/>
								<ec:column property="truckPref01" title="TRUCK 1"/>
								<ec:column property="truckPref02" title="TRUCK 2"/>
								<ec:column property="truckPref03" title="TRUCK 3"/>
								<ec:column property="truckPref04" title="TRUCK 4"/>
								<ec:column property="truckPref05" title="TRUCK 5"/>
							</ec:row>
						</ec:table>
					</form>
				</div>
			</div>
		</div>
	</div>

	<script>
		addMultiParamRowHandlersFilter('ec_table', 'rowMouseOver', 'editemployeetruckpreference.do','id',0, 0, false, 'empstatus','<%=request.getParameter("empstatus")%>');

		function getFilterTestValue() {
	             var filters = getFilterValue(document.getElementById("employeeTruckPrefListForm"),false);
	             return escape(filters);
       }
	</script>

  </tmpl:put>
</tmpl:insert>
