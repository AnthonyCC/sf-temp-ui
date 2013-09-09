<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import='com.freshdirect.transadmin.security.SecurityManager' %>
<%@ page import='com.freshdirect.transadmin.model.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%		
	pageContext.setAttribute("HAS_ADDBUTTON", "false");
	pageContext.setAttribute("HAS_DELETEBUTTON", "false");		
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>/ Customers At Building  /</title>
	<link rel="stylesheet" href="css/transportation.css" type="text/css" />		
	<link rel="stylesheet" href="css/extremecomponents.css" type="text/css" />
	<link rel="stylesheet" href="css/jscalendar-1.0/calendar-system.css" type="text/css" />
	<script src="js/RowHandlers.js" language="javascript" type="text/javascript"></script>
	<script src="js/action.js" language="javascript" type="text/javascript"></script>
		
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/calendar.js"></script>
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/lang/calendar-en.js"></script>
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/calendar-setup.js"></script>
    
</head>
 <body marginwidth="0" marginheight="0" border="0">
	<table class="appframe" width="100%" cellpadding="0" cellspacing="0" border="0">
			
			<tr>
				<td class="navlist" colspan="3" bgcolor="c00cc3d">
				<table class="navtbl" border="0" width="100%">
					<tr>
						<td>
						
				<form id="customerInfoForm" action="" method="post">	
				<ec:table items="customerlist" 
				    imagePath="${pageContext.request.contextPath}/images/table/*.gif" action="${pageContext.request.contextPath}/customerinfo.do" title="&nbsp;"
				    width="98%"  view="fd" form="customerInfoForm" autoIncludeParameters="true" rowsDisplayed="25"  >
				    
				        
				    <ec:row interceptor="obsoletemarker">				    				    	
				      <ec:column property="customerId" title="Customer ID" alias="customerId" width="10px"/>
				     <ec:column property="firstName" title="First Name" alias="firstName" width="10px"/>
				     <ec:column property="lastName" title="Last Name" alias="lastName" width="10px"/>
				    </ec:row>
				  </ec:table>
				  </form>
		</td>				
					</tr>
					</table>
				</td>
			</tr>
			
	</table>

</body>
</html>

