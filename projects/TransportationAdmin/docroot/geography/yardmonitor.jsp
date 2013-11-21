<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%@ page import= 'com.freshdirect.transadmin.util.TransportationAdminProperties' %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import='java.util.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>	
	
	<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>
	<tmpl:put name='title' direct='true'>Routing : Yard Monitor </tmpl:put>
		<tmpl:put name='content' direct='true'>
		  
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<div>&nbsp;</div>
				<tr>
            		<td class="screentitle">Yard Monitor</td>
          		</tr>
			</table>
			<table width="100%" cellpadding="0" cellspacing="0" border="0" align="right">
				<tr>&nbsp;</tr>          		
				<tr>
					<td align="left">
						 &nbsp;&nbsp;&nbsp;<input style="font-size:11px" type = "button" value="Force Refresh" onclick="javascript:showYardMonitorData();" />
					</td>
					<td>&nbsp;&nbsp;&nbsp;<div id="errExceptionContainer" style="float:left;"></div></td>
				<% if(com.freshdirect.transadmin.security.SecurityManager.isUserAdmin(request)) { %>
            		<td align="right">
					 <input style="font-size:11px" type = "button" value="Manage Locations" onclick="javascript:showParkingLocation();" />
					 <input style="font-size:11px" type = "button" value="View Slots" onclick="javascript:viewSlots();" />
					 <input style="font-size:11px" type = "button" value="Manage Slots" onclick="javascript:showParkingSlotForm();" />&nbsp;&nbsp;&nbsp;
					</td>
				<% } %>
          		</tr>				
			 </table>
			 <div>&nbsp;</div>
			 <table align="center" class="yardMonitorTable">
				<tr>
					<td valign="top" rowspan="2">
						<table width="100%" border=0 height="30">
							<tr>
								<td align="right" class="tv_header"> Parking Location / Slots</td>
								<td align="right" class="tv_time" nowrap>Last Refresh Time:<br>
									<span id="lastLocSlotRefreshTime" style="font-weight:bold;"></span>
								</td>
							</tr>
						</table>
						<div id="locSlotsTemp"></div>
							<ec:table items="parkingLocSlots" action="${pageContext.request.contextPath}/yardmonitor.do"
									imagePath="${pageContext.request.contextPath}/images/table/*.gif" title="&nbsp;" width="100%"
									rowsDisplayed="100" view="flattable" filterable="false" >
									<ec:row interceptor="obsoletemarker">	                      
										<ec:column property="location" title="Parking Location" filterable="false" sortable="false"/>
										<ec:column property="availbleSlotCount"   title="Available Slots" filterable="false" sortable="false"/>
										<ec:column property="usedSlotCount"   title="Used Slots" filterable="false" sortable="false"/>
									</ec:row>
							</ec:table>  
						  <div id="noparkinglocslotdata" style="">
          						<img  height="650" width="100%" src="images/no-data.gif">
						  </div>
					</td>
					<td valign="top" height="400">
						<table width="100%" border=0 height="30">
							<tr>
								<td align="right" class="tv_header"> Parking Location / Trucks</td>
								<td align="right" class="tv_time" nowrap>Last Refresh Time:<br>
										<span id="lastLocTruckRefreshTime" style="font-weight:bold;"></span>
								</td>
							</tr>
						</table>
							<ec:table items="parkingLocTrucks" tableId="ec_locationtrucks" action="${pageContext.request.contextPath}/yardmonitor.do"
								imagePath="${pageContext.request.contextPath}/images/table/*.gif" title="&nbsp;" width="100%"
								rowsDisplayed="100" view="flattable" filterable="false" >
								<ec:row interceptor="obsoletemarker">
									<ec:column  property="location" title="Parking Location" filterable="false" sortable="false"/>
									<ec:column  property="usedSlotCount"   title="No. of Trucks" filterable="false" sortable="false"/>
								</ec:row>
							</ec:table>
						 <div id="noparkingloctruckdata" style="">
          						<img  height="100%" width="100%" src="images/no-data.gif">
						  </div>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<div>&nbsp;</div>
						<div class="search_header">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Search Route / Truck</div>
						<table align="center" border="0">
							<tr class="noBorder">
								<td class="searchTable">Enter Route / Truck</td>
								<td>
									<input id="routeNumber" maxlength="12"/>
									<input type="button" id="add" style="margin-top:6px" value="&nbsp;Go&nbsp;" onclick="javascript:getRouteSummary(document.getElementById('routeNumber').value);" />
									<div id="errRouteSearchContainer"></div>
								</td>
							</tr>
						</table>
						<div class="search_header">Search Results:</div>
						<table class="searchresult_table">
						
							<tr class="noBorder">
								<td>Open / Loaded : </td>
								<td id="loadStatus" align="left">&nbsp;</td>
							</tr>
							<tr class="noBorder">
								<td>Service status : </td>
								<td id="serviceStatus" align="left">&nbsp;</td>
							</tr>
							<tr class="noBorder">
								<td>Current Location : </td>
								<td id="currentLocation" align="left">&nbsp;</td>
							</tr>
						</table>
					</td>
				</tr>
			 </table>
			<%@ include file='i_manageparkingarea.jspf'%> 
			<%@ include file='i_manageparkingslot.jspf'%> 

	<script type="text/javascript">
		var yardMngRpcClient = new JSONRpcClient("yardmonitor.ax");
		
		var errColor = "#FF0000";
		var msgColor = "#00FF00";
		
		function viewSlots(){
			javascript:pop('parkingslot.do',400, 680);
		}

		function showYardMonitorData(){
			YAHOO.util.Dom.get("errExceptionContainer").value = "";
			try {
				var locationSummary = yardMngRpcClient.AsyncYardProvider.getYardMonitorSummary();
				if(locationSummary != null && (locationSummary.locationSlotSummary != null || locationSummary.locationTruckSummary != null)){
					var locationSlotSummary = locationSummary.locationSlotSummary.list;
					var locationTruckSummary =  locationSummary.locationTruckSummary.list;
					   
					if(locationSlotSummary.length == 0) {
						document.getElementById("noparkinglocslotdata").style.display = '';
					} else {
						document.getElementById("noparkinglocslotdata").style.display = 'none';
					}
					var locSlotTable = document.getElementById('ec_table');
					if(locSlotTable.tBodies[0])
					{
						locSlotTable.removeChild(locSlotTable.tBodies[0]);
					}
					var oddRow= true;
					var slotBody = document.createElement("tbody");
					slotBody.className='tableBody';
					locSlotTable.appendChild(slotBody);
					for(var i=0;i < locationSlotSummary.length; i++){
						var locSlotRow = document.createElement("tr");
						if(oddRow){
							locSlotRow.className='odd';
						}else{
							locSlotRow.className='even';
						}
						var td1=document.createElement("td");
						var td2=document.createElement("td");
						var td3=document.createElement("td");
						td1.innerHTML = locationSlotSummary[i].location;
						td2.innerHTML = locationSlotSummary[i].availableSlots;
						td3.innerHTML = locationSlotSummary[i].usedSlots;
						locSlotRow.appendChild(td1);locSlotRow.appendChild(td2);locSlotRow.appendChild(td3);
						locSlotTable.tBodies[0].appendChild(locSlotRow);
						
						oddRow = !oddRow;
					}

					if(locationTruckSummary.length == 0) {
						document.getElementById("noparkingloctruckdata").style.display = '';
					} else {
						document.getElementById("noparkingloctruckdata").style.display = 'none';
					}
					var locTruckTable = document.getElementById('ec_locationtrucks_table');
					if(locTruckTable.tBodies[0])
					{
						locTruckTable.removeChild(locTruckTable.tBodies[0]);
					}
					oddRow = true;
					var truckBody = document.createElement("tbody");
					truckBody.className='tableBody';
					locTruckTable.appendChild(truckBody);
					
					for(var k = 0;k < locationTruckSummary.length; k++){
						var locTruckRow = document.createElement("tr");

						if(oddRow){
							locTruckRow.className='odd';
						}else{
							locTruckRow.className='even';
						}
						var locName = document.createElement("td");
						var noTrucks = document.createElement("td");
					
						locName.innerHTML = locationTruckSummary[k].location;
						noTrucks.innerHTML = locationTruckSummary[k].noOfTrucks;
						locTruckRow.appendChild(locName);
						locTruckRow.appendChild(noTrucks);
						locTruckTable.tBodies[0].appendChild(locTruckRow);

						oddRow = !oddRow;
					}
					updateRefreshTimestamp();
				}
			} catch(rpcException) {
				showSysExceptionMsg("There was a problem in communication to the server. Please try to refresh the browser window!",true);
			}
		}
		
		YAHOO.util.Event.addListener(window, "load", 
													function(){ 
														showYardMonitorData(); 
														setInterval("showYardMonitorData()",  <%= TransportationAdminProperties.getYardMonitorRefreshTime() %> )
													});

		function updateRefreshTimestamp() {
			var d = new Date();
			if (d.getHours() < 10){
				d.getHours() = "0" + d.getHours();
			}
			if (d.getMinutes() < 10){
				d.getMinutes() = "0" + d.getMinutes();
			}

			document.getElementById("lastLocSlotRefreshTime").innerHTML = d.getHours() + ':' + d.getMinutes();
			document.getElementById("lastLocTruckRefreshTime").innerHTML = d.getHours() + ':' + d.getMinutes();
		}
		
		updateRefreshTimestamp();

		function getRouteSummary(routeNo){
				addRouteSysMessage('', false);
				document.getElementById("loadStatus").innerHTML = '';
				document.getElementById("serviceStatus").innerHTML = '';
				document.getElementById("currentLocation").innerHTML = '';

				if(routeNo && routeNo.length > 0){
					var routeSummary = yardMngRpcClient.AsyncYardProvider.getRouteStatusInfo(routeNo);
					if(routeSummary != null){
						document.getElementById("loadStatus").innerHTML = routeSummary.loadStatus;
						document.getElementById("serviceStatus").innerHTML = routeSummary.serviceStatus;
						document.getElementById("currentLocation").innerHTML = routeSummary.currentLocation;
						//document.getElementById("fueledStatus").innerHTML = routeSummary.fueled;
					}else{
						addRouteSysMessage('No matching route/truck found', true);
						document.getElementById("loadStatus").innerHTML = '';
						document.getElementById("serviceStatus").innerHTML = '';
						document.getElementById("currentLocation").innerHTML = '';
					}
				}
		}
		function addRouteSysMessage(msg, isError) {
			var errContObj1 = YAHOO.util.Dom.get("errRouteSearchContainer");
					if(isError) {
						errContObj1.style.color = errColor;
					} else {
						errContObj1.style.color = msgColor;
					}
					errContObj1.style.fontWeight="bold";
					YAHOO.util.Dom.get("errRouteSearchContainer").innerHTML = msg;
		}
		function showSysExceptionMsg(msg, isError) {
			var errContObj1 = YAHOO.util.Dom.get("errExceptionContainer");
					if(isError) {
						errContObj1.style.color = errColor;
					} else {
						errContObj1.style.color = msgColor;
					}
					errContObj1.style.fontWeight="bold";
					YAHOO.util.Dom.get("errExceptionContainer").innerHTML = msg;
		}
	 </script>

<style type="text/css">

	.yardMonitorTable {
		height:700px;
		width:98%;
		border:1px dotted;
		background-color:#F7F7F7;
	}

	.yardMonitorTable td {
		color: #222222;
		font-size: 11px;
		font-weight: normal;
		padding: 1px 3px 2px;
		text-decoration: none;
		border:1px dotted;
		width:50%;
	}
	
	.eXtremeTable .tableHeader 
	{
		color: white;
		font-family: verdana, arial, helvetica, sans-serif;
		font-size: 12px;
		font-weight: bold;
		text-align: center;
		padding: 3px 3px 3px 3px;
		margin: 0px;
		border-right-style: solid;
		border-right-width: 1px;
		border-color: white;
		border: none 2px white;
	}
	.eXtremeTable .odd td, .eXtremeTable .even td, .eXtremeTable .obsoleteRow td , .eXtremeTable .confirmedRow td
	{
		padding: 2px 3px 2px 3px;
		vertical-align: middle;
		font-family: verdana, arial, helvetica, sans-serif;
		font-size:11px;
		border: none 1px white;
		font-weight: bold;
		text-align: center;
		height:35px;
	}
	td.tv_header
	{
		width:300px;
		color: #4C4C4C;
		font-family: verdana, arial, helvetica, sans-serif;
		font-size: 14px;
		font-weight: bold;
		border: none;
		background-color:transparent;
		text-align: center;
	}

	td.tv_time {
		background-color:transparent;
		border: none;
		text-align:center;
		padding: 0px 3px 2px 0px;
		width:100px;
	}

	td.tv_header
	{
		width:300px;
		color: #4C4C4C;
		font-family: verdana, arial, helvetica, sans-serif;
		font-size: 14px;
		font-weight: bold;
		border: none;
		background-color:transparent;
		text-align: center;
	}

	.search_header
	{
		width:300px;
		font-family: verdana, arial, helvetica, sans-serif;
		font-weight: bold;
		border: none;
		background-color:transparent;
		text-align: center;
		padding: 15px 0 0 20px;
		
	}
	.noBorder, .noBorder td {
		border: none;
	}

	.searchTable {
		text-align:right;
	}

	.searchresult_table  {
		text-align:right;
		border: none;
		width:600px;
	}
	</style>

		 

	</tmpl:put>
</tmpl:insert>
