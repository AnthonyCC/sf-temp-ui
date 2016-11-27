<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%@ page import= 'com.freshdirect.transadmin.util.TransportationAdminProperties' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import='com.freshdirect.transadmin.security.*' %>
<%@ page import='java.util.*' %>

<% 
	pageContext.setAttribute("HAS_ADDBUTTON", "false"); 
  pageContext.setAttribute("HAS_CONFIRMBUTTON", "false"); 
  pageContext.setAttribute("HAS_DELETEBUTTON", "false"); 
   String dateRangeVal = request.getParameter("dispDate") != null ? request.getParameter("dispDate") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
  %>
  
<%
String modeStr="";
if("1".equals(request.getParameter("mode"))) modeStr=" - READY";
else if("2".equals(request.getParameter("mode"))) modeStr=" - WAITING";
else if("3".equals(request.getParameter("mode"))) modeStr=" - N/R";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <title>/ FreshDirect Transportation Admin : Dashboard /</title>
    <link rel="stylesheet" href="css/extremecomponents_tv_view.css" type="text/css" />
	<link rel="stylesheet" href="css/transportation.css" type="text/css" />			
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
	<style type="text/css">
	.eXtremeTable .tableHeader 
	{
		background-color: #4C4C4C;
		color: white;
		<% if("1".equals(request.getParameter("mode"))){%>
		font-family: <%=TransportationAdminProperties.getCSSPropertyValue("READY","PAGE_HEADER",null)%>;
		font-size: <%=TransportationAdminProperties.getCSSPropertyValue("READY","PAGE_HEADER","SIZE")%>px;	
		<%}%>
		<% if("2".equals(request.getParameter("mode"))){%>
		font-family: <%=TransportationAdminProperties.getCSSPropertyValue("WAITING","PAGE_HEADER",null)%>;
		font-size: <%=TransportationAdminProperties.getCSSPropertyValue("WAITING","PAGE_HEADER","SIZE")%>px;	
		<%}%>
		font-weight: bold;
		text-align: center;
		padding-right: 3px;
		padding-left: 3px;
		padding-top: 3px;
		padding-bottom: 3px;
		margin: 0px;
		border-right-style: solid;
		border-right-width: 1px;
		border-color: white;
		border: none 2px white;	
	}
	.eXtremeTable .odd td, .eXtremeTable .even td, .eXtremeTable .obsoleteRow td , .eXtremeTable .confirmedRow td
	{
		padding-top: 2px;
		padding-right: 3px;
		padding-bottom: 2px;
		padding-left: 3px;
		vertical-align: middle;
		<% if("1".equals(request.getParameter("mode"))){%>
		font-family: <%=TransportationAdminProperties.getCSSPropertyValue("READY","PAGE",null)%>;
		font-size: <%=TransportationAdminProperties.getCSSPropertyValue("READY","PAGE","SIZE")%>px;	
		<%}%>
		<% if("2".equals(request.getParameter("mode"))){%>
		font-family: <%=TransportationAdminProperties.getCSSPropertyValue("WAITING","PAGE",null)%>;
		font-size: <%=TransportationAdminProperties.getCSSPropertyValue("WAITING","PAGE","SIZE")%>px;	
		<%}%>	
		border: none 1px white;
		font-weight: bold;
		text-align: center;
		height:25px;
	}
	.tv_header
	{
		background-color: green;
		color: white;
		<% if("1".equals(request.getParameter("mode"))){%>
		font-family: <%=TransportationAdminProperties.getCSSPropertyValue("READY","HEADER",null)%>;
		font-size: <%=TransportationAdminProperties.getCSSPropertyValue("READY","HEADER","SIZE")%>px;	
		<%}%>
		<% if("2".equals(request.getParameter("mode"))){%>
		font-family: <%=TransportationAdminProperties.getCSSPropertyValue("WAITING","HEADER",null)%>;
		font-size: <%=TransportationAdminProperties.getCSSPropertyValue("WAITING","HEADER","SIZE")%>px;	
		<%}%>	
		font-weight: bold;
	}	
	</style>
<META HTTP-EQUIV="Refresh" CONTENT="<%=request.getParameter("refreshtime")%>">
</head>
 <body marginwidth="0" marginheight="0">	
	<table width="100%" border=0 height="30">
		<tr>
			<td width="100" align="left"><img width="100" height="30" src="images/TransAppLogo.gif"></td>
			<td align="center" class="tv_header">DISPATCH<%=modeStr%></td><td width="180" align="right" class="tv_time" nowrap>Last Refresh Time:<br>
				<span class="tv_time1"><%=request.getAttribute("lastTime")%></span>
			</td>
		</tr>
	</table>
    <ec:table items="dispatchInfos" action="${pageContext.request.contextPath}/dispatchDashboardScreen.do"
    		imagePath="${pageContext.request.contextPath}/images/table/*.gif" title="&nbsp;" width="100%"
            rowsDisplayed="1000" view="dispatchdbtable" filterable="false" >
    	<ec:row interceptor="obsoletemarker">	                      
	        <ec:column alias="trnTimeslotslotName" property="startTime" title="Truck Dispatch Time" filterable="false" sortable="false"/>
	        <ec:column alias="trnRouterouteNumber" property="route"  width="10" title="Route" filterable="false" sortable="false"/>
	        <ec:column alias="trnTrucktruckNumber" property="truck" width="10"  title="Truck" filterable="false" sortable="false"/>              
	        <ec:column alias="trnTruckLocation" property="location" width="10"  title="Parking Loc" filterable="false" sortable="false"/>	
	        <ec:column alias="trnTruckStops" property="noOfStops" width="10"  title="Stops" filterable="false" sortable="false"/>       
	        <ec:column property="drivers" cell="dispatchDashBoardResCell" title="Driver"  filterable="false" alias="drivers" sortable="false"/>
	        <ec:column property="helpers" cell="dispatchDashBoardResCell" title="Helper"  filterable="false" alias="helpers" sortable="false"/>             
	        <ec:column alias="trnStatus" property="dispatchStatus"  title="Status" filterable="false" sortable="false"/>
        </ec:row>
    </ec:table>  
          <div id="nodata" style="display:none;">
          		<img  height="650" width="100%" src="images/no-data.gif">
          </div>  
   </body>
   </html> 
<script>

var t;
var pageParam;

var grpSize = <%=request.getParameter("pagesize")%>;
var pending = 0;
var pageTableId = "ec_table";

function initPagination() {
	var tabSrc = document.getElementById(pageTableId);
	
	pageParam = new Array(tabSrc.tBodies.length);
	var totalRowCnt = 0;
	for(p=0; p < pageParam.length; p++) {
		var rcCnt = tabSrc.tBodies[p].rows.length;
		totalRowCnt = totalRowCnt + rcCnt;
		pageParam[p] = new Array(2);
		pageParam[p][0] = Math.floor(grpSize/tabSrc.tBodies.length) + pending;
		pageParam[p][1] = -1;
		
		if(rcCnt < pageParam[p][0]) {
			pending = pageParam[p][0] - rcCnt;
			pageParam[p][0] = rcCnt;
			if(p == pageParam.length -1 && p > 0) {
				pageParam[p-1][0] = pageParam[p-1][0] + pending;
			}
		}
		
	}
	if(totalRowCnt == 0) {
		document.getElementById("nodata").style.display = ''
	}
	paginator();	
}

function paginator() {
	
	for(p=0; p < pageParam.length; p++) {
		pageParam[p][1] = paginateTable(pageTableId, pageParam[p][1], pageParam[p][0], p);
	}
          
	t=setTimeout("paginator()",<%= Integer.parseInt(request.getParameter("pagerefreshtime")) * 1000 %>);
}

function paginateTable(tabId, k , tmpGrpSize, cntGrp) {
	 var rows = document.getElementById(tabId).tBodies[cntGrp].rows;
	 var currentPage = 0;
     var countGrp = 0;
     var tmpLast = 0;

      for (i = 0; i < rows.length; i++) {
            if(countGrp == tmpGrpSize) {
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
      //k = tmpLast;
      return tmpLast;
}



function stopCount() {
    clearTimeout(t);
}

initPagination();

</script>
