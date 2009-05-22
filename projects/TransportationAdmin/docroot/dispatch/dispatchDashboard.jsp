<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%@ page import= 'com.freshdirect.transadmin.util.TransportationAdminProperties' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import='com.freshdirect.transadmin.security.*' %>
<%@ page import='java.util.*' %>

<%  pageContext.setAttribute("HAS_ADDBUTTON", "false"); 
  pageContext.setAttribute("HAS_CONFIRMBUTTON", "false"); 
  pageContext.setAttribute("HAS_DELETEBUTTON", "false"); 
   String dateRangeVal = request.getParameter("dispDate") != null ? request.getParameter("dispDate") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
  %>
  
 	


	


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <title>/ FreshDirect Transportation Admin : Dashboard /</title>
	<link rel="stylesheet" href="css/transportation.css" type="text/css" />		
	<link rel="stylesheet" href="css/extremecomponents_tv.css" type="text/css" />
	<link rel="stylesheet" href="css/jscalendar-1.0/calendar-system.css" type="text/css" />
	<script src="js/RowHandlers.js" language="javascript" type="text/javascript"></script>
	<script src="js/action.js" language="javascript" type="text/javascript"></script>
		
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/calendar.js"></script>
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/lang/calendar-en.js"></script>
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/calendar-setup.js"></script>
	<script language="javascript" src="js/time.js"></script>
	<script type="text/javascript">
		/* allow lower resolutions to display */
		if (screen.height < 768 && screen.width <= 1024) {
			var newCSS;
			var headID = document.getElementsByTagName("head")[0];

			newCSS = document.createElement('link');
			newCSS.type = 'text/css';
			newCSS.rel = 'stylesheet';
			newCSS.href = 'css/lowres.css';
			newCSS.media = 'screen';
			headID.appendChild(newCSS);
		}
	</script>	
<META HTTP-EQUIV="Refresh" CONTENT="<%=request.getParameter("refreshtime")%>">
</head>
 <body  marginwidth="0" marginheight="0" border="0">	
	
	<table width="100%" border=0 height="30"><tr><td width="100" align="left"><img width="100" height="30" src="images/TransAppLogo.gif"></td><td align="center" class="tv_header">DISPATCH</td><td width="180" align="right" class="tv_time" nowrap>Last Refresh Time:<br><span class="tv_time1"><%=request.getAttribute("lastTime")%></span></td></tr></table>
	<table width="100%" ><tr><td>
	<div id="dispatchDiv" align="center" valign="top" border="0" >
      <ec:table items="dispatchInfos"   action="${pageContext.request.contextPath}/dispatchDashboard.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
            width="98%"  rowsDisplayed="1000" view="flattable" filterable="false">      
           
            <ec:row interceptor="obsoletemarker">
                                         
              
              <ec:column alias="trnZoneRegion" property="regionZone" title="Region-Zone" filterable="false" sortable="false"/>              
              <ec:column  alias="trnTimeslotslotName"  property="startTime" title="Start Time" filterable="false" sortable="false"/>
              
              <ec:column alias="trnRouterouteNumber" property="route"  width="10" title="Route" filterable="false" sortable="false"/>
              <ec:column alias="trnTrucktruckNumber" property="truck" width="10"  title="Truck" filterable="false" sortable="false"/>              
              <ec:column alias="trnTruckLocation" property="location" width="10"  title="Loc" filterable="false" sortable="false"/>
              <ec:column alias="trnTruckStops" property="noOfStops" width="10"  title="Stops" filterable="false" sortable="false"/>
               <ec:column alias="trnStatus" property="dispatchStatus"  title="Status" filterable="false" sortable="false"/>
              <ec:column property="drivers"  cell="com.freshdirect.transadmin.web.ui.FDDispatchSummaryResourceCell" title="Driver"  filterable="true" alias="drivers" filterable="false" sortable="false"/>
              <ec:column property="helpers"  cell="com.freshdirect.transadmin.web.ui.FDDispatchSummaryResourceCell" title="Helper"  filterable="true" alias="helpers" filterable="false" sortable="false"/>             
             <ec:column  alias="dispatchTime"  property="dispatchTimeEx" title="Dispatch Time" filterable="false" sortable="false"  cell="date" format="hh:mm aaa"/>
            </ec:row>
          </ec:table>
    </div>
</td></tr></table>
 
   </body>
   </html>
<script>

var c=0;
var t;
var k=0;
var tmpLast = 0;
var grpSize = <%=request.getParameter("pagesize")%>;


function paginator() {

var rows = document.getElementById("ec_table").tBodies[0].rows;

      t=setTimeout("paginator()",<%=request.getParameter("pagerefreshtime")%>*1000);
      var currentPage = 0;
      var countGrp = 0;

      for (i = 0; i < rows.length; i++) {
            if(rows[i].name != "local") {
                  if(countGrp == grpSize) {
                        countGrp = 0;
                        currentPage++;
                  }
                  countGrp++;
                  if(currentPage == k+1 ) {
                        rows[i].style.display = '';
      
                        if(i == rows.length-1) {
                              tmpLast = -1;
                        } else {
                              tmpLast = currentPage;
                        }
                  } else {
                        rows[i].style.display = 'none';
                  }
            }
      }
      k = tmpLast;

}



function stopCount() {
      clearTimeout(t);
}
paginator();


</script>
