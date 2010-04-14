<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<%    
  pageContext.setAttribute("HAS_ADDBUTTON", "false");
  pageContext.setAttribute("HAS_DELETEBUTTON", "false");
%>

<% 
	String pageTitle = "Schedule Employees";	
%>
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'> Operations : Employee : <%=pageTitle%></tmpl:put>

<tmpl:put name='hasSubs' direct='true'>subs</tmpl:put>

  <tmpl:put name='content' direct='true'>
  
	<div class="MNM001 subsub or_999">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM001 <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))&&!"S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))&&!"S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do" class="<% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))&&!"S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Active</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))&&!"S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>		
		
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
					<span>
					Status : <select id="statusFilter" onchange="javascript:doFilter()"><option value="a">TransApp Active</option>
													   <option value="i">TransApp Inactive</option>
													   <option value="ae">All Employees</option>
													   </select>
					</span>
				</div>
			</div>
		</div>

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
					<form id="employeeListForm" action="" method="post">
					
                      <input type="hidden" name="status" value="<%=request.getAttribute("status")%>"/>
           
						<ec:table items="employees"   action="${pageContext.request.contextPath}/employee.do?empstatus=S"
						imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""		
						width="98%"  view="fd" form="employeeListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
									<ec:exportPdf fileName="transportationSchedule.pdf" tooltip="Export PDF" 
									  headerTitle="" />
							  <ec:exportXls fileName="transportationemployee.xls" tooltip="Export PDF" />
							  <ec:exportCsv fileName="transportationemployee.csv" tooltip="Export CSV" delimiter="|"/>
								
							<ec:row interceptor="obsoletemarker">            								
								<ec:column title=" " width="5px" 
										filterable="false" sortable="false" cell="selectcol"
										property="employeeId" />
								<ec:column property="status" title="Status"/>						
								<ec:column property="firstName" title="First Name"/>
								<ec:column property="lastName" title="Last Name"/>
								<ec:column alias="kronosId" property="employeeId" title="KronosID"/>   
								<ec:column  property="employeeRoleType" title="Role"/>                                  								
                                <ec:column property="mon" title="MON"/>
                                <ec:column property="tue" title="TUE"/>
                                <ec:column property="wed" title="WED"/>
                                <ec:column property="thu" title="THU"/>
                                <ec:column property="fri" title="FRI"/>
                                <ec:column property="sat" title="SAT"/>
                                <ec:column property="sun" title="SUN"/>
                                 
							</ec:row>
						</ec:table>
    
					</form>
				</div>
			</div>
		</div>
	</div>	
		<script>
		   addRowHandlersFilterTest('ec_table', 'rowMouseOver', 'editschedule.do','id',0, 0);
		     function getFilterTestValue()
		      {
		      	var filters=getFilterValue(document.getElementById("employeeListForm"),false);		      
		      	return escape(filters)
		      }
		     function doFilter() 
		     {
		          var param1 = document.getElementById("statusFilter").value;
		          location.href = "employee.do?empstatus=S&status="+param1;    
		     }
		     document.getElementById("statusFilter").value='<%=request.getAttribute("status")%>'
		</script>	
  </tmpl:put>
</tmpl:insert>
