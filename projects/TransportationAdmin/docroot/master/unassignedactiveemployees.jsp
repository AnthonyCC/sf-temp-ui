<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <title>/ FreshDirect Transportation Admin : Unassigned Active Employees/</title>
	<link rel="stylesheet" href="css/transportation.css" type="text/css" />		
	<link rel="stylesheet" href="css/extremecomponents.css" type="text/css" />
	<script src="js/RowHandlers.js" language="javascript" type="text/javascript"></script>
	<script src="js/action.js" language="javascript" type="text/javascript"></script>	   
	
</head>
 <body marginwidth="0" marginheight="0" border="0">
  <%     
		pageContext.setAttribute("HAS_ADDBUTTON", "false");
		pageContext.setAttribute("HAS_DELETEBUTTON", "false");
  %>
<form id="activeListForm" action="" method="post"> 
	<ec:table items="unassignedEmployees"   action="${pageContext.request.contextPath}/unassignedactiveemployees.do"
		imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
		 view="fd" form="activeListForm" autoIncludeParameters="true" rowsDisplayed="25"  >
		
		<ec:exportPdf fileName="transportationunassignedactiveemployees.pdf" tooltip="Export PDF" 	headerTitle="Transportation Unassigned Employees" />
		  <ec:exportXls fileName="transportationunassignedactiveemployees.xls" tooltip="Export PDF" />
		  <ec:exportCsv fileName="transportationunassignedactiveemployees.csv" tooltip="Export CSV" delimiter="|"/>
		<ec:row interceptor="obsoletemarker">
								<ec:column property="firstName" title="First Name"/>
								<ec:column property="lastName" title="Last Name"/>
								<ec:column alias="kronosId" property="employeeId" title="KronosID"/>                                  
								<ec:column property="hireDate" title="Hire Date"/>
								<ec:column property="jobType" title="JobType"/>              
								<ec:column property="supervisorFirstName" title="supervisor"/>
								<ec:column property="employeeRoleType" title="roles"/>
								<ec:column property="regionS" title="Region"/>
		                                      
		</ec:row>
	  </ec:table>
</form> 

</body>
</html>