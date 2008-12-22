<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<%    
  pageContext.setAttribute("HAS_ADDBUTTON", "false");
  pageContext.setAttribute("HAS_DELETEBUTTON", "false");
%>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Transportation Employee</tmpl:put>

  <tmpl:put name='content' direct='true'>

    <div class="MNM001 subsub">
		<div class="subs_left">	
			<br>
			<div class="sub_tableft sub_tabL_MNM001">&nbsp;</div>
			<div class="subtab">
				<div class="minwidth"><!-- --></div>
				<a href="employee.do" class="MNM001">Active</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001">&nbsp;</div>		
		
			<div class="sub_tableft sub_tabL_MNM001 activeL">&nbsp;</div>
			<div class="subtab activeT">
				<div class="minwidth"><!-- --></div>
				<a href="xEmployee.do">Terminated</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 activeR">&nbsp;</div>
		</div>
	</div>
	<div class="cont_row"><!--  --></div>

    <div align="center">
      <form id="employeeListForm" action="" method="post">  
        <ec:table items="employees"   action="${pageContext.request.contextPath}/employee.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Transportation Employees11"
            width="98%"  view="fd" form="employeeListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="transportationemployee.pdf" tooltip="Export PDF" 
                      headerTitle="Transportation Employees" />
              <ec:exportXls fileName="transportationemployee.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationemployee.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">                     
              <ec:column property="firstName" title="First Name"/>
              <ec:column property="lastName" title="Last Name"/>
              <ec:column alias="kronosId" property="employeeId" title="KronosID"/>                                  
              <ec:column property="hireDate" title="Hire Date"/>
              <ec:column property="jobType" title="JobType"/>              
              <ec:column property="supervisorFirstName" title="supervisor"/>
              <ec:column property="employeeRoleType" title="roles"/>
              
            </ec:row>
          </ec:table>
       </form>  
     </div>
	<% if("T".equalsIgnoreCase(request.getParameter("empstatus"))) { %>
		<script>
			addRowHandlers('ec_table', 'rowMouseOver', 'editemployee.do','id',0, 0);
		</script>
	<% } %>
  </tmpl:put>
</tmpl:insert>