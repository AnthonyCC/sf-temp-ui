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
    <title>/ FreshDirect Transportation Admin : Unassigned Routes/</title>
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
<form id="routeListForm" action="" method="post"> 
	<ec:table items="routes"   action="${pageContext.request.contextPath}/unassignedroute.do"
		imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
		 view="fd" form="routeListForm" autoIncludeParameters="true" rowsDisplayed="25"  >
		
		<ec:exportPdf fileName="transportationunassignedroutes.pdf" tooltip="Export PDF" 	headerTitle="Transportation Routes" />
		  <ec:exportXls fileName="transportationunassignedroutes.xls" tooltip="Export PDF" />
		  <ec:exportCsv fileName="transportationunassignedroutes.csv" tooltip="Export CSV" delimiter="|"/>
		<ec:row interceptor="obsoletemarker">
		  <ec:column property="routeNumber" title="Route Number"/>
		  <ec:column property="zoneNumber" title="Zone Number"/>
		  <ec:column property="routeTime" title="AM/PM"/>
		  <ec:column property="numberOfStops" title="Stops"/>
          <ec:column property="firstDlvTime" title="First Dlv Time"/>                                      
		</ec:row>
	  </ec:table>
</form> 

</body>
</html>