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
  <tmpl:put name='title' direct='true'> Routing : HandOff </tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/>     
    <div align="center">     
      <form method="post" enctype="multipart/form-data">       
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">HandOff Management Console</td>
          </tr>         
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>            
          </tr>
          
          <tr>
            <td class="screencontent">
              <table class="forms1">          
                <tr>
                  <td>Delivery Date</td>
                  <td>  
                  	<spring:bind path="command.deliveryDate">                
                   <input maxlength="10" size="10" name='<c:out value="${status.expression}"/>' id='<c:out value="${status.expression}"/>' 
                   		value='<c:out value="${status.value}"/>' />
                    <a href="#" id="trigger_deliveryDate" style="font-size: 9px;">
                        <img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date">
                    </a>
                     <script language="javascript">                 
                      Calendar.setup(
                      {
                        showsTime : false,
                        electric : false,
                        inputField : "deliveryDate",
                        ifFormat : "%m/%d/%Y",
                        singleClick: true,
                        button : "trigger_deliveryDate",
                        onUpdate : handleDateChangeEvt 
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
                                    &nbsp;<span id="deliveryDate.errors"><c:out value="${error}"/></span>
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
                              <option value="">--Please Select Cut Off</option> 
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
               <td>Scenario</td>
                        <td>
                          <spring:bind path="command.serviceTimeScenario">
                            <select id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>">
                              <option value="">--Please Select Scenario</option> 
                              <c:forEach var="cutOffRow" items="${scenarios}">                             
                                  <c:choose>
                                    <c:when test="${status.value == cutOffRow.code}" > 
                                      <option selected value="<c:out value="${cutOffRow.code}"/>"><c:out value="${cutOffRow.description}"/></option>
                                    </c:when>
                                    <c:otherwise> 
                                      <option value="<c:out value="${cutOffRow.code}"/>"><c:out value="${cutOffRow.description}"/></option>
                                    </c:otherwise> 
                                  </c:choose>      
                                </c:forEach>   
                          </select>
                          <c:forEach items="${status.errorMessages}" var="error">
                                    &nbsp;<span id="serviceTimeScenario.errors"><c:out value="${error}"/></span>
                                </c:forEach>
                          </spring:bind>
                         </td>
              <td>&nbsp;</td>
                      </tr>                 
             	
              <tr>
			    <td colspan="3" align="center">
				 	<input type = "submit" value="&nbsp;Start&nbsp;"  onclick="return confirm('You are about to start a new handoff batch session. Do you want to continue?')" />
				 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				 	<input type = "button" value="&nbsp;Clear&nbsp;" onclick="javascript:document.getElementById('deliveryDate').value='';"  />
				 	&nbsp;&nbsp;&nbsp;<input type = "button" id="refreshgrid" value="&nbsp;Refresh&nbsp;" onclick="javascript:refreshTable();"  />	
				 	&nbsp;&nbsp;&nbsp;<input type="checkbox" name="autoRefresh" onclick="doAutoRefresh(this)" />&nbsp;Auto Refresh	
				 	&nbsp;&nbsp;&nbsp;<div style="font-weight:bold;float:right;color:green;margin:5px" id="lastUpdateTime"></div>		
			    </td>   
			  </tr>
								
              </table>        
              
            </td>
          </tr>               
        </table>
         </form>
     </div>
     <br/>
 	<div style="height:475px;overflow-y:auto;background-color:#F2F2F2;border:1px solid #000;margin:5px" align="center">
 		<div id="contbatch">
 		</div>
	</div>  
	<script type="text/javascript" src="js/dataTable.js"></script> 
 	<script>
      var jsonrpcClient = new JSONRpcClient("handoff.ax");
      var sDataSource; 
      var myCallback;
      var sDataTable;
     					  
      function refreshTable() {
      		sDataTable.getDataSource().sendRequest('', myCallback);      		
      }   
      
      function showTable() {
         
         var dataIncrementer = function() {         
         	var result = jsonrpcClient.AsyncHandOffProvider.getHandOffBatch
         												(document.getElementById("deliveryDate").value);
            updateRefreshTimestamp();
            return result.list;
         };
          
          sDataSource = new YAHOO.util.DataSource(dataIncrementer);
         <% if(TransportationAdminProperties.isAutoDispatchEnabled()) {%>
			  var sColumns =  [ 
					{key:"batchId", label:"Batch ID",sortable:false, width: 50, className:"forms1"}, 
					{key:"deliveryDate", label:"Delivery Date",sortable:false, width: 80,className:"forms1"},			    
					{key:"creationInfo", label:"Process Audit Details",sortable:false, width: 142,className:"forms1"},
					{key:"status", label:"Status",sortable:false, width: 60,className:"forms1"},
					{key:"sessionInfo", label:"Session",sortable:false, width: 305,className:"forms1"},
					{key:"systemMessage", label:"System Message", width: 200,sortable:false,className:"forms1"},
					
					{key:"action", label:"Action", sortable:false, width: 125, className:"forms1",
									formatter:"dropdown", dropdownOptions:["","ROUTEIN","ROUTEOUT","COMMIT","AUTODISPATCH","CANCEL"] },

					{key:"report", label:"Download", sortable:false, width: 150, className:"forms1",
									formatter:"dropdown", dropdownOptions:["","CutOff Report","Community Report","SAP Upload Files"] }
					
			 ];
		<%}else{%>
				var sColumns =  [ 
					{key:"batchId", label:"Batch ID",sortable:false, width: 50, className:"forms1"}, 
					{key:"deliveryDate", label:"Delivery Date",sortable:false, width: 80,className:"forms1"},			    
					{key:"creationInfo", label:"Process Audit Details",sortable:false, width: 142,className:"forms1"},
					{key:"status", label:"Status",sortable:false, width: 60,className:"forms1"},
					{key:"sessionInfo", label:"Session",sortable:false, width: 305,className:"forms1"},
					{key:"systemMessage", label:"System Message", width: 200,sortable:false,className:"forms1"},
					
					{key:"action", label:"Action", sortable:false, width: 125, className:"forms1",
									formatter:"dropdown", dropdownOptions:["","ROUTEIN","ROUTEOUT","COMMIT","CANCEL"] },

					{key:"report", label:"Download", sortable:false, width: 150, className:"forms1",
									formatter:"dropdown", dropdownOptions:["","CutOff Report","Community Report","SAP Upload Files"] }
					
			 ];
		<%}%>
		  var sMyConfigs = { 
			    paginator : new YAHOO.widget.Paginator({ 
			        rowsPerPage    : 5
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
                											, oRecord.getData("batchId"));
                									
                	} else {
                		
                		doReport(elDropdown, elDropdown.options[elDropdown.selectedIndex].value
                										, oRecord.getData("status")
                											, oRecord.getData("batchId"));
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
      	 'NEW':{STOP:0},
	     'PRC':{STOP:0},
	     'RTC': {ROUTEIN:0,ROUTEOUT:0,CANCEL:0},
	     'RGC': {ROUTEIN:0,ROUTEOUT:0,COMMIT:0,CANCEL:0},
	     'STD': {ROUTEIN:0,CANCEL:0},
	     'NEF': {ROUTEIN:0,CANCEL:0},
	     'RTF': {ROUTEIN:0,CANCEL:0},
	     'RGF': {ROUTEIN:0,ROUTEOUT:0,CANCEL:0},
	     'CPF': {ROUTEIN:0,ROUTEOUT:0,COMMIT:0,CANCEL:0}, 
		 'CAN': {},
	     'CPD/ADC': {AUTODISPATCH:0},
		 'CPD/ADF': {AUTODISPATCH:0},
		 'CPD': {AUTODISPATCH:0}
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
				try  {
					var checkExceptionResult = jsonrpcClient.AsyncHandOffProvider.doHandOffCommit(currentBatchId, false, true);
				
      				if(checkExceptionResult != null) {
      					if(!confirm(checkExceptionResult)) {
      						elDropdown.value = '';
      						return;
      					}
      				}
      			} catch(rpcException) {
      				alert("There was a problem in communication to the server. Please try to refresh the browser window!\n"+e);
      			}
				
      			if(actionType == 'CutOff Report') {
      				location.href = 'cutoffreport.do?handOffBatchId='+currentBatchId;
      			} else if(actionType == 'SAP Upload Files') {
      				location.href = 'sapupload.do?handOffBatchId='+currentBatchId;
      			} else if(actionType == 'Community Report') {
      				location.href = 'communityreport.do?handOffBatchId='+currentBatchId;
      			} else {
      				alert("Feature not available");
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
      					if(actionType == 'ROUTEOUT') {
      						showDepotTable(currentBatchId);
      					
      					} else if(actionType == 'ROUTEIN') {
      						jsonrpcClient.AsyncHandOffProvider.doRoutingIn(currentBatchId);
      					} else if(actionType == 'CANCEL') {
      						jsonrpcClient.AsyncHandOffProvider.doHandOffCancel(currentBatchId);
      					} else if(actionType == 'STOP') {
      						//jsonrpcClient.AsyncHandOffProvider.doHandOffStop(currentBatchId);
      						alert("Feature not available");
      					} else if(actionType == 'COMMIT') {
      						var result = jsonrpcClient.AsyncHandOffProvider.doHandOffCommit(currentBatchId, false, false);
      						if(result != null) {
      							if(confirm(result)) {
      								result = jsonrpcClient.AsyncHandOffProvider.doHandOffCommit(currentBatchId, true, false);
      								if(result != null) {
      									alert('Unable to force commit. Contact AppSupport!');
      								}
      							}
      						}
      					} else if(actionType == 'AUTODISPATCH') {
      						jsonrpcClient.AsyncHandOffProvider.doHandOffAutoDispatch(currentBatchId, true);
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
      </script>
      <%@ include file='i_handoffdepotinfo.jspf'%>  
  </tmpl:put>
</tmpl:insert>