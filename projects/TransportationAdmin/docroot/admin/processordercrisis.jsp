<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import='com.freshdirect.transadmin.util.TransportationAdminProperties' %>

<tmpl:insert template='/common/sitelayout.jsp'>

  <tmpl:put name='yui-lib'>
	<%@ include file='/common/i_yui.jspf'%>	
  </tmpl:put>	

  <tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>
  <tmpl:put name='title' direct='true'> Routing : Order Crisis </tmpl:put>

  <tmpl:put name='content' direct='true'>
   <form:form commandName = "command" method="post">
   <script type="text/javascript" language="javascript" src="js/SelectionHandlers.js"></script>
   	<script type="text/javascript" src="js/dataTable.js"></script> 
	<br/>     
    <div align="center">
    
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Crisis Management Console</td>
          </tr>         
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          
          <tr>
            <td class="screencontent">
              
			  <table class="forms1" style="height:100%;width:98%;border:1px dotted;background-color:#F7F7F7;">
						<tr>
							<td align="left" valign="top">
								  <table>
										<tr>
										  <td>Delivery Date</td>
										  <td>  
											<spring:bind path="command.selectedDate">
											   <input maxlength="10" size="10" name='<c:out value="${status.expression}"/>' id='<c:out value="${status.expression}"/>' 
													value='<c:out value="${status.value}"/>' />
												<a href="#" id="trigger_selectedDate" style="font-size: 9px;">
													<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date">
												</a>
													 <script language="javascript">
														  Calendar.setup(
														  {
															showsTime : false,
															electric : false,
															inputField : "selectedDate",
															ifFormat : "%m/%d/%Y",
															singleClick: true,
															button : "trigger_selectedDate",
															onUpdate : '' 
														   }
														  );
														   function handleDateChangeEvt(cal) {
																var jsonrpcScenarioClient = new JSONRpcClient("handoff.ax");
																var resultScenario = jsonrpcScenarioClient.AsyncHandOffProvider.getServiceTimeScenario
																							(document.getElementById("deliveryDate").value);
																
																if(resultScenario != null && resultScenario.trim().length > 0) {
																	document.getElementById("serviceTimeScenario").value = resultScenario;
																}
														  }
													  </script>
													<c:forEach items="${status.errorMessages}" var="error">
														&nbsp;<span id="selectedDate.errors"><c:out value="${error}"/></span>
													</c:forEach>
												  </spring:bind>
											</td>
											<td>&nbsp;</td>
									   </tr>

									   <tr>
										  <td>New Delivery Date</td>
										  <td>  
											<spring:bind path="command.destinationDate">
											   <input maxlength="10" size="10" name='<c:out value="${status.expression}"/>' id='<c:out value="${status.expression}"/>' 
													value='<c:out value="${status.value}"/>' />
												<a href="#" id="trigger_destinationDate" style="font-size: 9px;">
													<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date">
												</a>
												 <script language="javascript">
													  Calendar.setup(
													  {
														showsTime : false,
														electric : false,
														inputField : "destinationDate",
														ifFormat : "%m/%d/%Y",
														singleClick: true,
														button : "trigger_destinationDate",
														onUpdate : ''
													   }
													  );
												  </script>
														<c:forEach items="${status.errorMessages}" var="error">
															&nbsp;<span id="destinationDate.errors"><c:out value="${error}"/></span>
														</c:forEach>
											 </spring:bind>
										  </td>
										<td>&nbsp;</td>
									   </tr>
									   
									   <tr>
											<td>Cut Off</td>
											<td>
												  <spring:bind path="command.cutOff">
													<select id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
													  <option value="All">All</option> 
													  <c:forEach var="cutOffRow" items="${cutoffs}">
														  <c:choose>
															<c:when test="${status.value == cutOffRow.cutOffId}" > 
															  <option selected value="<c:out value="${cutOffRow.cutOffId}"/>"><c:out value="${cutOffRow.name}"/></option>
															</c:when>
															<c:otherwise> 
															  <option value="<c:out value="${cutOffRow.cutOffId}"/>"><c:out value="${cutOffRow.name}"/></option>
															</c:otherwise> 
														  </c:choose>      
														</c:forEach>   
												  </select>
												  <c:forEach items="${status.errorMessages}" var="error">
															&nbsp;<span id="cutOff.errors"><c:out value="${error}"/></span>
														</c:forEach>
												  </spring:bind>
												 </td>
												<td>&nbsp;</td>
										</tr>
										<tr>
						                  <td>Delivery Type</td>
										  <td>
											   <form:checkbox path="deliveryType" value="H"/>&nbsp;<font style="font-size:11px;">Residential</font>&nbsp;
											   <form:checkbox path="deliveryType" value="C"/>&nbsp;<font style="font-size:11px;">Corporate</font>&nbsp;
											   <form:checkbox path="deliveryType" value="P"/>&nbsp;<font style="font-size:11px;">Pick-Up</font>&nbsp;
										   </td>
										   <td>
											  &nbsp;<form:errors path="deliveryType" />
										   </td>
										</tr>
										<tr>
						                  <td>Include Standing Order</td>
										  <td>
											   <form:checkbox path="includeStandingOrder" value="X"/>
										   </td>
										   <td>
											  &nbsp;<form:errors path="includeStandingOrder" />
										   </td>
										</tr>
									  <tr>
									   <td>Customer Type</td>
												<td>
												  <spring:bind path="command.customerType">
													<select id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
													  <option value="All">All</option>
													  <c:forEach var="customerTypeRow" items="${customerTypes}"> 
														  <c:choose>
															<c:when test="${status.value == customerTypeRow}" > 
															  <option selected value="<c:out value="${customerTypeRow}"/>"><c:out value="${customerTypeRow}"/></option>
															</c:when>
															<c:otherwise> 
															  <option value="<c:out value="${customerTypeRow}"/>"><c:out value="${customerTypeRow}"/></option>
															</c:otherwise> 
														  </c:choose>      
														</c:forEach>   
												  </select>
														<c:forEach items="${status.errorMessages}" var="error">
															&nbsp;<span id="customerType.errors"><c:out value="${error}"/></span>
														</c:forEach>
												  </spring:bind>
												 </td>
												<td>&nbsp;</td>
										</tr>
										<tr>
										     <td>Timeslot Start Time</td>
											 <td>
												<spring:bind path="command.startTime">
													<c:choose>
														<c:when test='${status.value == startTime}'> 
															<form:input  size="8" path="startTime" onblur="this.value=time(this.value);"/>
														</c:when>
														<c:otherwise> 
															 <form:input size="8" path="startTime" onblur="this.value=time(this.value);"/>
														</c:otherwise> 
													 </c:choose>
												</spring:bind>
											</td> 
											<td>&nbsp;</td>	
										</tr>
										<tr>
											<td>Timeslot End Time</td> 
											 <td>
												<spring:bind path="command.endTime">
													<c:choose>
														<c:when test='${status.value == endTime}'> 
															<form:input  size="8" path="endTime" onblur="this.value=time(this.value);"/>
														</c:when>
														<c:otherwise> 
															 <form:input size="8" path="endTime" onblur="this.value=time(this.value);"/>
														</c:otherwise> 
													 </c:choose>
												</spring:bind>
												</td>
											<td>&nbsp;</td>	
										</tr>
									 
									  </table>
							</td>

							<td align="center" valign="top">
								<div>
									<INPUT TYPE="radio" checked="true" NAME="selectRadio" id="selectRadioZone" onclick="toggleDisplay(this.id)"> Zone
									<INPUT TYPE="radio" NAME="selectRadio" id="selectRadioRegion" onclick="toggleDisplay(this.id)"> Region
									<SCRIPT LANGUAGE="JavaScript">
										function toggleDisplay(idVar) {
											if (idVar === 'selectRadioZone') {
												document.getElementById('selectRadioZoneDisp').style.display = '';
												document.getElementById('selectRadioRegionDisp').style.display = 'none';
												document.getElementById('zones').disabled = false;
												document.getElementById('regions').disabled = true;
											}else if (idVar === 'selectRadioRegion') {
												document.getElementById('selectRadioZoneDisp').style.display = 'none';
												document.getElementById('selectRadioRegionDisp').style.display = '';
												document.getElementById('zones').disabled = true;
												document.getElementById('regions').disabled = false;
											}
										}
									</SCRIPT>
								</div>
								<div id="selectRadioZoneDisp">
									 <table>
										<tr>
											<td height="10px"><img width="0" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
											<td style="text-align:center;font-size:10px">Zones Available</td>
											<td><img width="0" height="" src="/media_stat/crm/images/clear.gif" alt="" /></td>
											<td style="text-align:center;font-size:9px">Zones Selected</td>
										</tr>
										<tr>
											<td><img width="0" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
											<td style="padding:8px;">
												<select id="dlvzones_available" name="dlvzones_available" style="height:160px;width:200px;" size="2" multiple>
														<c:forEach var="zone" items="${zones}">
															<option value="<c:out value="${zone.zoneCode}"/>"><c:out value="${zone.zoneCode}"/></option>
														</c:forEach>
												</select>
											</td>
											<td class="text-align:center;">
												<div>
													<input type="button" value="Add &gt;&gt;" onclick="remOpt('dlvzones_available', 'zones'); sortByText('dlvzones_available'); sortByText('zones'); return false;" style="font-size:9px;width: 60px;background-color:#F7F7F7;"/></div>
												<div>
													<img width="1" height="7" src="/media_stat/crm/images/clear.gif" alt=""/></div>
												<div>
													<input type="button" value="&lt;&lt; Remove" onclick="remOpt('zones', 'dlvzones_available'); sortByText('dlvzones_available'); sortByText('zones'); return false;" style="font-size:9px;width: 60px;background-color:#F7F7F7;"/></div>
											</td>
											
											<td style="padding:8px;">
												<input type="hidden" id="zone" name="zone"/>
												<select id="zones" name="zones" style="height:160px;width:200px;" size="2" multiple>
												</select>
											</td>
											
										</tr>
								  </table>
								</div>
								<div style='display:none;' id="selectRadioRegionDisp">
									 <table>
										<tr>
											<td height="10px"><img width="0" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
											<td style="text-align:center;font-size:10px">Regions Available</td>
											<td><img width="0" height="" src="/media_stat/crm/images/clear.gif" alt="" /></td>
											<td style="text-align:center;font-size:9px">Regions Selected</td>
										</tr>
										<tr>
											<td><img width="0" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
											<td style="padding:8px;">
												<select id="dlvregion_available" name="dlvregion_available" style="height:160px;width:200px;" size="2" multiple>
														<c:forEach var="region" items="${regions}">
															<option value="<c:out value="${region.code}"/>"><c:out value="${region.code}"/></option>
														</c:forEach>
												</select>
											</td>
											<td class="text-align:center;">
												<div>
													<input type="button" value="Add &gt;&gt;" onclick="remOpt('dlvregion_available', 'regions'); sortByText('dlvregion_available'); sortByText('regions'); return false;" style="font-size:9px;width: 60px;background-color:#F7F7F7;"/></div>
												<div>
													<img width="1" height="7" src="/media_stat/crm/images/clear.gif" alt=""/></div>
												<div>
													<input type="button" value="&lt;&lt; Remove" onclick="remOpt('regions', 'dlvregion_available'); sortByText('dlvregion_available'); sortByText('regions'); return false;" style="font-size:9px;width: 60px;background-color:#F7F7F7;"/></div>
											</td>
											
											<td style="padding:8px;">
												<input type="hidden" id="region" name="region"/>
												<spring:bind path="command.regions">
												<select id="regions" name="regions" style="height:160px;width:200px;" size="2" multiple>
												</select>
												</spring:bind>
											</td>
											
										</tr>
								  </table>
								</div>
							</td>					 
								  
							
						</tr>
						<tr><td colspan="2" align="center">&nbsp;</td></tr>
						 <tr>
							<td colspan="2" align="center">
									<input type = "submit" value="&nbsp;Start&nbsp;" onclick="javascript:startProcess();" />
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type = "button" value="&nbsp;Clear&nbsp;" onclick="javascript:clearInputData();" />
									&nbsp;&nbsp;&nbsp;<input type = "button" id="refreshgrid" value="&nbsp;Refresh&nbsp;" onclick="javascript:refreshTable();"  />
									&nbsp;&nbsp;&nbsp;<input type="checkbox" name="autoRefresh" onclick="doAutoRefresh(this)" />&nbsp;Auto Refresh
									&nbsp;&nbsp;&nbsp;<span style="font-weight:bold;color:green;margin:5px" id="lastUpdateTime"></span>
							</td>   
						</tr>
						 <tr><td>&nbsp;</td></tr>
			  </table>
              
            </td>
          </tr>
        </table>
         </form:form>
     </div>
     <br/>
 	
	 <div style='height:400px;width:98%;border:1px dotted;background-color:#F7F7F7;margin-left:9px' align="center">		
		<div id="contbatch">
		</div>  
    </div>

	<script>
        
      var jsonrpcClient = new JSONRpcClient("crisismanage.ax");
      var sDataSource; 
      var myCallback;
      var sDataTable;
     					  
      function refreshTable() {
      		sDataTable.getDataSource().sendRequest('', myCallback);  
      		showTable();
      }   
      
      function showTable() {
         
         var dataIncrementer = function() {         
         	var result = jsonrpcClient.AsyncCrisisMngProvider.getOrderCrisisBatch
         												(document.getElementById("selectedDate").value);
            updateRefreshTimestamp();
            return result.list;
         };
          
          sDataSource = new YAHOO.util.DataSource(dataIncrementer);
        
		  var sColumns =  [ 
					{key:"processId", label:"Process ID",sortable:false, className:"forms1"}, 
					{key:"deliveryDate", label:"Delivery Dates",sortable:false, width: 120,className:"forms1"},
					{key:"cutOffInfo", label:"Search Criteria",sortable:false, width: 145,className:"forms1"},
					{key:"creationInfo", label:"Process Audit Details",sortable:false, width: 142,className:"forms1"},
					{key:"status", label:"Status",sortable:false, width: 40,className:"forms1"},
					{key:"systemMessage", label:"System Message", width: 265,sortable:false,className:"forms1"},
					
					{key:"action", label:"Action", sortable:false, width: 150, className:"forms1",
									formatter:"dropdown", dropdownOptions:["","ORDERIN","CANCELORDER","CREATERSV","PLACEORDER","CANCEL"] },

					{key:"report", label:"Download", sortable:false, width: 125, className:"forms1",
									formatter:"dropdown", dropdownOptions:["","Mrk Report","VoiceShot Report"] }
					
			 ];
	
		  var sMyConfigs = { 
			    paginator : new YAHOO.widget.Paginator({ 
			        rowsPerPage    : 3
			    }) 
		  }; 
		 	 
  		sDataTable = new YAHOO.widget.DataTable("contbatch", sColumns, sDataSource, sMyConfigs);
  		
  		sDataTable.subscribe("dropdownChangeEvent", function(oArgs){
                var elDropdown = oArgs.target;   
                var _currCol =  this.getColumn(elDropdown);
                if(_currCol != null) {
                	var records = this.getRecordSet().getRecords();
               		var oRecord = records[this.getRecordIndex(elDropdown)];
                	if(_currCol.key == "action") {
                		
                		doBatchAction(elDropdown, elDropdown.options[elDropdown.selectedIndex].value
                										, oRecord.getData("status")
                											, oRecord.getData("processId"));
                									
                	} else {
                		
                		doReport(elDropdown, elDropdown.options[elDropdown.selectedIndex].value
                										, oRecord.getData("status")
                											, oRecord.getData("processId"));
                	}
                }
         });
            
  		 // Set up polling
        myCallback = {
            success: sDataTable.onDataReturnInitializeTable,
            failure: function() {
                YAHOO.log("Polling failure", "error");
            },
            scope: sDataTable
        };
      }
      
      var actionMapping = {
      	 'NEW': {CANCEL:0},
	     'PRC': {},
	     'DCC': {ORDERIN:0,CANCELORDER:0,CANCEL:0},
	     'DCF': {ORDERIN:0,CANCEL:0},
	     'OCF': {CANCELORDER:0},
		 'OCC': {CREATERSV:0},
	     'CRF': {CREATERSV:0},
	     'CRC': {PLACEORDER:0},
		 'POC': {PLACEORDER:0},
		 'CAN': {},
		 'CPD': {PLACEORDER:0}
      }
      
      function updateRefreshTimestamp() {
      	var d = new Date();
		document.getElementById("lastUpdateTime").innerHTML = d.getHours() + ':' + d.getMinutes()+ ':' + d.getSeconds();
      }
      
      function doReport(elDropdown, actionType, currentStatus, currentBatchId) {
      		
            if(actionType != "") {
      			if (!confirm ("You are about to perform "+actionType+" generation. Do you want to continue?")) {
					elDropdown.value = '';
					return;
				}
				if (currentStatus == 'OCC' || currentStatus == 'CRC' || currentStatus == 'CRF' || currentStatus == 'POC' || currentStatus == 'CPD'){
					if(actionType == 'Mrk Report') {
						showCrisisMngReportForm(currentBatchId, 'MKR');
					} else if(actionType == 'VoiceShot Report') {
						showCrisisMngReportForm(currentBatchId, 'VSR');
					} else {
						alert("Feature not available");
					}
				} else {
					alert(actionType+" is not allowed for batch status "+currentStatus);
				}
      		}
      		elDropdown.value = '';
      }
      
      function doBatchAction(elDropdown, actionType, currentStatus, currentBatchId) {
            if(actionType != "") {
      			if(actionMapping[currentStatus][actionType] == null) {
      				alert(actionType+" is not allowed for batch status "+currentStatus);
      				elDropdown.value = '';
      			} else {
      				
					if (!confirm ("You are about to perform "+actionType+" action. Do you want to continue?")) {
						elDropdown.value = '';
						return;
					}
					try {
      					if(actionType == 'ORDERIN') {
      						jsonrpcClient.AsyncCrisisMngProvider.doOrderCollectionIn(currentBatchId);
      					}else if(actionType == 'CANCELORDER') {
      						jsonrpcClient.AsyncCrisisMngProvider.doCancelOrder(currentBatchId, null);
      					} else if(actionType == 'CREATERSV') {
      						var result = jsonrpcClient.AsyncCrisisMngProvider.doCrisisMngCreateReservation(currentBatchId, false, null);
							if(result != null) {
      							if(confirm(result)) {
      								showExceptionTable(currentBatchId);
      							}
      						} else {
								jsonrpcClient.AsyncCrisisMngProvider.doCrisisMngCreateReservation(currentBatchId, true, null);
							}
      					} else if(actionType == 'PLACEORDER'){
							showStandingOrderTable(currentBatchId);
						}else if(actionType == 'CANCEL') {
      						jsonrpcClient.AsyncCrisisMngProvider.doCrisisMngBatchCancel(currentBatchId);
      					} else {
      						alert("Processing "+actionType+" on BATCH ID:"+currentBatchId);
      					} 
      				} catch(rpcException) {
      					alert("There was a problem in communication to the server. Please try to refresh the browser window!\n"+e);
      				}
      				sDataTable.destroy();
                	showTable();
      			}
      		}
      }
      
		  function doAutoRefresh(src) {
			if(src.checked) {
				sDataSource.setInterval(30000, null, myCallback);
			} else {
				sDataSource. clearAllIntervals();
			}
		  }      
		  showTable();

		  function clearInputData(){
			  document.getElementById('selectedDate').value='';
			  document.getElementById('destinationDate').value='';
			  document.getElementById('startTime').value='';
			  document.getElementById('endTime').value='';
			  document.getElementById('zones').value='';
			  document.getElementById('customerType').selectedIndex=0;
			  document.getElementById('cutOff').selectedIndex=0;
		 }

		function startProcess(){
			if(confirm('You are about to start a new Order Scenario batch session. Do you want to continue?')){
				document.getElementById('zone').value = getValues('zones');
				document.getElementById('region').value = getValues('regions');
			}
		}
      </script>
	 <%@ include file='i_standingorderdetails.jspf'%> 
	 <%@ include file='i_timeslotexception.jspf'%>
	  <%@ include file='i_processcrisismngreport.jspf'%>
	  <%@ include file='i_orderdetails.jspf'%> 
  </tmpl:put>
</tmpl:insert>