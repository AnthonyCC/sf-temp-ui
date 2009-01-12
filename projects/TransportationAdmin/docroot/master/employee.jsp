<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<%    
  pageContext.setAttribute("HAS_ADDBUTTON", "false");
  pageContext.setAttribute("HAS_DELETEBUTTON", "false");
%>

<% 
	String pageTitle = "";
	if("T".equalsIgnoreCase(request.getParameter("empstatus"))) 
		{ 
			pageTitle = "Terminated Employees"; 
		}else{
			pageTitle = "Active Employees"; 
		}
%>
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Operations : Employee : <%=pageTitle%></tmpl:put>

<tmpl:put name='hasSubs' direct='true'>subs</tmpl:put>

  <tmpl:put name='content' direct='true'>
  
	<div class="MNM001 subsub or_999">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM001 <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do" class="<% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Active</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>		
		
			<div class="sub_tableft sub_tabL_MNM001 <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do?empstatus=T" class="<% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Terminated</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>
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
					<form id="employeeListForm" action="" method="post">
					
					<% 
						if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { 
					%>

						<ec:table items="employees"   action="${pageContext.request.contextPath}/employee.do?empstatus=T"
						imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""		
						width="98%"  view="fd" form="employeeListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
<ec:exportPdf fileName="transportationemployee.pdf" tooltip="Export PDF" 
									  headerTitle="" />
							  <ec:exportXls fileName="transportationemployee.xls" tooltip="Export PDF" />
							  <ec:exportCsv fileName="transportationemployee.csv" tooltip="Export CSV" delimiter="|"/>
								
							<ec:row interceptor="obsoletemarker">            
								<% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>
								<ec:column title=" " width="5px" 
										filterable="false" sortable="false" cell="selectcol"
										property="employeeId" />  
								<% } %>		
								<ec:column property="firstName" title="First Name"/>
								<ec:column property="lastName" title="Last Name"/>
								<ec:column alias="kronosId" property="employeeId" title="KronosID"/>                                  
								<ec:column property="hireDate" title="Hire Date"/>
								<ec:column property="jobType" title="JobType"/>              
								<ec:column property="supervisorFirstName" title="supervisor"/>
								<ec:column property="employeeRoleType" title="roles"/>
                                <ec:column property="terminationDate" title="Terminated"/>
							</ec:row>
						</ec:table>
                     <%
						 }
					     else
						 { 
					  %>
                    
					 	<ec:table items="employees"   action="${pageContext.request.contextPath}/employee.do"
						imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""		
						width="98%"  view="fd" form="employeeListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
<ec:exportPdf fileName="transportationemployee.pdf" tooltip="Export PDF" 
									  headerTitle="" />
							  <ec:exportXls fileName="transportationemployee.xls" tooltip="Export PDF" />
							  <ec:exportCsv fileName="transportationemployee.csv" tooltip="Export CSV" delimiter="|"/>
								
							<ec:row interceptor="obsoletemarker">            
								<% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>
								<ec:column title=" " width="5px" 
										filterable="false" sortable="false" cell="selectcol"
										property="employeeId" />  
								<% } %>		
								<ec:column property="firstName" title="First Name"/>
								<ec:column property="lastName" title="Last Name"/>
								<ec:column alias="kronosId" property="employeeId" title="KronosID"/>                                  
								<ec:column property="hireDate" title="Hire Date"/>
								<ec:column property="jobType" title="JobType"/>              
								<ec:column property="supervisorFirstName" title="supervisor"/>
								<ec:column property="employeeRoleType" title="roles"/>
                           
							</ec:row>
						</ec:table>
                      <% } %>
							
					</form>
				</div>
			</div>
		</div>
	</div>
	<% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>
		<script>
			addRowHandlers('ec_table', 'rowMouseOver', 'editemployee.do','id',0, 0);
		</script>
	<% } %>
  </tmpl:put>
</tmpl:insert>
