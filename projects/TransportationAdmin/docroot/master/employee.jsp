<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Transportation Employee</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form id="employeeListForm" action="" method="post">  
        <ec:table items="employees"   action="${pageContext.request.contextPath}/employee.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Transportation Employees"
            width="98%"  view="fd" form="employeeListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="transportationemployee.pdf" tooltip="Export PDF" 
                      headerTitle="Transportation Employees" />
              <ec:exportXls fileName="transportationemployee.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationemployee.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">
              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="employeeId" />              
              <ec:column property="name" title="Name"/>
              <ec:column property="employeenumber" title="KronosID"/>
              <ec:column alias="trnSupervisorname" property="trnSupervisor.name" title="Supervisor"/>
              <ec:column alias="trnEmployeeJobTypejobTypeName" property="trnEmployeeJobType.jobTypeName" title="Job Type"/>
              <ec:column alias="trnEmployeeJobTypehireDate" cell="date" property="hireDate" title="Hire Date"/>
            </ec:row>
          </ec:table>
       </form>  
     </div>
     <script>
      addRowHandlers('ec_table', 'rowMouseOver', 'editemployee.do','id',0, 0);
    </script>   
  </tmpl:put>
</tmpl:insert>