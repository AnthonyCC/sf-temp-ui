<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<%    
  pageContext.setAttribute("HAS_ADDBUTTON", "false");
  pageContext.setAttribute("HAS_DELETEBUTTON", "false");
%>

<% 
	String pageTitle = "Schedule Team";	
%>
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'> Operations : Employee : <%=pageTitle%></tmpl:put>

<tmpl:put name='hasSubs' direct='true'>subs</tmpl:put>

  <tmpl:put name='content' direct='true'>
  
	<div class="MNM001 subsub or_999">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM001 <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus")) 
					&& !"S".equalsIgnoreCase(request.getParameter("empstatus"))
						&& !"C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))
						&&!"S".equalsIgnoreCase(request.getParameter("empstatus"))
							&&!"C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do" class="<% if(!"T".equalsIgnoreCase(request.getParameter("empstatus"))&&!"S".equalsIgnoreCase(request.getParameter("empstatus"))) { %>MNM001<% } %>">Active</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if(!"T".equalsIgnoreCase(request.getParameter("empstatus")) 
					&& !"S".equalsIgnoreCase(request.getParameter("empstatus"))
						&& !"C".equalsIgnoreCase(request.getParameter("empstatus"))) { %>activeR<% } %>">&nbsp;</div>		
		
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
		</div>
	</div>
	<div class="cont_row_bottomline"><!--  --></div>

	<div class="contentroot">
		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem">
					<span class="scrTitle">
						Team Schedule Status
					</span>
					
				</div>
			</div>
		</div>

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
					<form id="employeeListForm" action="" method="post">
					
                      <input type="hidden" name="status" value="<%=request.getAttribute("status")%>"/>
           
						<ec:table items="employees"   action="${pageContext.request.contextPath}/employee.do?empstatus=C"
						imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""		
						width="98%"  view="fd" form="employeeListForm" autoIncludeParameters="true" rowsDisplayed="25"  >
									<ec:exportPdf fileName="transportationTeamSchedule.pdf" tooltip="Export PDF" 
									  headerTitle="" />
							  <ec:exportXls fileName="transportationteamschedule.xls" tooltip="Export PDF" />
							  <ec:exportCsv fileName="transportationteamschedule.csv" tooltip="Export CSV" delimiter="|"/>
								
							<ec:row interceptor="obsoletemarker">           								
								
								<ec:column property="lead" title="Lead"/>						
								<ec:column property="members" title="Members" cell="employee"/>
								<ec:column property="isMasterMatching" title="Master Schedule Match" cell="bool" filterCell="droplist"/>
								<ec:column property="isCurrentMatching" title="Current Schedule Match" cell="bool" filterCell="droplist"/>								                                 
							</ec:row>
						</ec:table>
    
					</form>
				</div>
			</div>
		</div>
	</div>
  </tmpl:put>
</tmpl:insert>
