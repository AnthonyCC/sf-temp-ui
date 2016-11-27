<%@page import="com.freshdirect.transadmin.util.TransportationAdminProperties"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>

<%  
	pageContext.setAttribute("HAS_ADDBUTTON", "true"); 
  	pageContext.setAttribute("HAS_CONFIRMBUTTON_NEW", "true");
  	pageContext.setAttribute("HAS_CLONEBUTTON", "true");
   	String dateRangeVal = request.getParameter("dispDate") != null ? request.getParameter("dispDate") : "";
   	if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
   	double muniMetermaxValue = TransportationAdminProperties.getMuniMeterMaxValue();
  %>
  
  <link rel="stylesheet" href="css/transportation.css" type="text/css" />		
  <link rel="stylesheet" href="css/airclic.css" type="text/css" />
  
  
  <% boolean airclic_msg = false; 
  
  if(dateRangeVal.equals(TransStringUtil.getCurrentDate())) airclic_msg = true;
  %>
  
<tmpl:insert template='/common/sitelayout.jsp'>
 <link rel="stylesheet" href="css/muni_meter_dialog.css" type="text/css" />  
    <tmpl:put name='title' direct='true'>Dispatch Sheet</tmpl:put>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>	
	<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>
	
  <tmpl:put name='content' direct='true'>
    <br/> <div id="overlay" style="display: none;"> </div>
    
    <div class="contentroot">               
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <c:if test="${not empty messages}">
          <tr>
            <td class="screenmessages">
            	<jsp:include page='/common/messages.jsp'/>            	
            </td>
          </tr>
          </c:if>         
          <tr>
            <td>			 
              <table border = "0">
                <tr>
                <td> 
                    <span style="font-size: 14px;font-weight: bold;">Dispatch</span>
                </td>                
                  <td> 
                     <input style="width:85px;" maxlength="10" size="8" name="dispDate" id="dispDate" value='<c:out value="${dispDate}"/>' />
                     <a href="#" id="trigger_dispatchDate" style="font-size: 9px;">
                        		<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date">
                     </a>
                    
                  <script language="javascript">
                     var jsonrpcClient = new JSONRpcClient("dispatchprovider.ax");
                     var errColor = "#FF0000";
               	     var msgColor = "#0000FF";
               	     
                     Calendar.setup(
	                      {
	                        showsTime : false,
	                        electric : false,
	                        inputField : "dispDate",
	                        ifFormat : "%m/%d/%Y",
	                        singleClick: true,
	                        button : "trigger_dispatchDate" 
	                       }
	                      );
	                      
                     function doDelete(tableId, url) {    
                        sendRequest(tableId, url, "Do you want to delete the selected records?");                       
                     }
                    
                     function doConfirm(tableId, url) {
                    	 if(confirm('You are about to update the selected records. Do you want to continue?')){
                    		 doDispatchStatus(tableId);
                    	 }                      
                     }
                    
                    function refreshRoute() {
                        var hasConfirmed = confirm ("This action can overwrite existing Route/Truck Assignment. Are you sure you want to perform this operation?");
                        if(hasConfirmed) {
                           location.href = "<c:out value="${pageContext.request.contextPath}"/>/refreshRoute.do?dispDate=<%= dateRangeVal %>";
                        }
                    }

                    function doUnassignedEmployees() {
                     	 
        	            javascript:pop('unassignedactiveemployees.do', 400,600);
                    }

                    function sendRequest(tableId, url, message, action) {
                      var table = document.getElementById(tableId);
                        var checkboxList = table.getElementsByTagName("input");                        
                        var dateField = document.getElementById("dispDate").value;    
                        var paramValues = null;
                        for (i = 0; i < checkboxList.length; i++) {
                          if (checkboxList[i].type=="checkbox" && checkboxList[i].checked && !checkboxList[i].disabled&&checkboxList[i].name.indexOf("_")==-1) {
                            
                            if (paramValues != null) {
                              paramValues = paramValues+","+checkboxList[i].name//+"$"+dateField;
                            } else {
                              paramValues = checkboxList[i].name//+"$"+dateField;
                            }
                          }
                        }
                        if (paramValues != null) {
                          var hasConfirmed = confirm (message);
                        if (hasConfirmed) {
                            location.href = url+"?id="+ paramValues+"&dispDate="+dateField+"&"+getFilterValue(document.getElementById("ec"),false);
                        } 
                        } else {
                          alert('Please Select a Row!');
                        }
                    }
                    
                   function sendRequestNew(tableId, url, message, action) {
                      var table = document.getElementById(tableId);
                        var checkboxList = table.getElementsByTagName("input");                        
                        var dateField = document.getElementById("dispDate").value;    
                        var checked="";                        
                        for (i = 0; i < checkboxList.length; i++) 
                        {                        
                          if (checkboxList[i].type=="checkbox" && !checkboxList[i].disabled && checkboxList[i].name.indexOf("_")!=-1) 
                          {
                          	if(checkboxList[i].checked)
                          	{
                          		checked+=checkboxList[i].name+",";
                          	}                          	
                          }
                        }
                        checked=checked.substring(0,checked.length-1);                        
                        if(checked.length==0){
                         	alert('Please Select a Row!');
                        } else {
                        	var newForm=document.forms["newSubmit"];
                        	newForm.action=url;
                        	newForm.id.value=checked;
                        	newForm.dispDate.value=dateField;
                        	setFilter(document.getElementById("ec"),newForm);                    	
                        	newForm.submit();
                        }
                    } 
                  
                   function doDispatchStatus(tableId){
                	   addSysMessage('', false);
                	   var table = document.getElementById(tableId);
                       var checkboxList = table.getElementsByTagName("input");

                       var myJSONObject = {"dispatch" :[]};
                       var status; 
                       var prevRow = 0;
                       var currentRow = 0;
                       var dispatchObj; var j = 0;
                       for (var i=0;i < checkboxList.length; i++) 
                       {                    	   
                    	   if (checkboxList[i].type == "checkbox" && !checkboxList[i].disabled 
                    			   && checkboxList[i].name.indexOf("_")!=-1 && checkboxList[i].checked) 
                           {                    		   
                    		   currentRow = checkboxList[i].name.split(/_/)[0];
                    		   status = checkboxList[i].name.split(/_/)[1];
                           	   if(currentRow != prevRow){
                           		   dispatchObj = {
		                   				  		 "javaClass" : "com.freshdirect.transadmin.web.model.DispatchStatus",
                           				   		 "dispatchId" : currentRow,
		                   						 "isKeysReady" : false,
		                   						 "phoneAssigned" : false,
		                   						 "isDispatched" : false,
		                   						 "isCheckedIn" : false,
		                   						 "isKeysIn" : false
		                   				  		};
	                           		
                           		   myJSONObject.dispatch[j++] = dispatchObj;  
                           	   }
                           	  if(status=='keysReady')
                       			dispatchObj.isKeysReady = true;
                       		  else if(status=='phoneAssigned')
                       			dispatchObj.phoneAssigned = true; 
                       		  else if(status=='dispatched')
                       			dispatchObj.isDispatched = true;
                       		  else if(status=='checkedIn')
                         			dispatchObj.isCheckedIn = true;
                       		else if(status=='keysIn')
                     			dispatchObj.isKeysIn = true;
                           }
                    	   prevRow = currentRow;
                       } 
                       
                       if(myJSONObject.dispatch.length==0){
                    	    addSysMessage('Please Select a Row!', true); 
                       } else {
                    	    var result = jsonrpcClient.AsyncDispatchProvider.updateDispatchStatus(updateStatusCallBack, myJSONObject, '<%= com.freshdirect.transadmin.security.SecurityManager.getUserName(request)%>');
                       }
                   }

                   function updateStatusCallBack(result, exception) {                	   
                	   if(result != null && result) {
                		   alert('Records updated successfully');   
                	   } else {
                		   alert('Error updating dispatch status');                		   
                	   }
                	   var newForm = document.forms["newSubmit"];
                	   var dateField = document.getElementById("dispDate").value;
                       newForm.action=location.href;
                       newForm.dispDate.value=dateField;
                       setFilter(document.getElementById("ec"),newForm);                    	
                       newForm.submit();
                   }
                  
                  function addSysMessage(msg, isError) {
                 		var errContObj = YAHOO.util.Dom.get("errContainer");
	           		    if(isError) {
	           		    	errContObj.style.color = errColor;
	           	      	} else {
	           	      		errContObj.style.color = msgColor;
	           	      	}
           	      	    errContObj.style.fontWeight="bold";
                 		YAHOO.util.Dom.get("errContainer").innerHTML = msg;
                  }
                   		
                    function directions(tableId, url, columnIndex) {
                      var table = document.getElementById(tableId);
                       var checkboxList = table.getElementsByTagName("input");
                        
                        var dateField = document.getElementById("dispDate").value;    
                        var paramValues = null;
                        for (i = 0; i < checkboxList.length; i++) {
                          if (checkboxList[i].type=="checkbox" && checkboxList[i].checked&& !checkboxList[i].disabled&&checkboxList[i].name.indexOf("_")==-1) {
                            var routeId = checkboxList[i].parentNode.parentNode.getElementsByTagName("td")[columnIndex].innerHTML;
                            if(routeId != null && routeId.length > 0) {
	                            if (paramValues != null) {
	                              paramValues = paramValues+","+routeId;
	                            } else {
	                              paramValues = routeId;
	                            }
	                         }
                          }
                        }
                        if (paramValues != null) {
                          pop(url+"?routeId="+ paramValues+"&rdate="+dateField,600,800);
                        } else {
                          alert('Please Select a Row!');
                        }
                    }

                    function loadGps(tableId, url, columnIndex) {
                        var table = document.getElementById(tableId);
                         var checkboxList = table.getElementsByTagName("input");
                          
                          var dateField = document.getElementById("dispDate").value;    
                          var paramValues = null;
                          for (i = 0; i < checkboxList.length; i++) {
                            if (checkboxList[i].type=="checkbox" && checkboxList[i].checked&& !checkboxList[i].disabled&&checkboxList[i].name.indexOf("_")==-1) {
                              var routeId = checkboxList[i].parentNode.parentNode.getElementsByTagName("td")[columnIndex].innerHTML
                              if(routeId != null && routeId.length > 0) {
  	                            if (paramValues != null) {
  	                              paramValues = paramValues+","+routeId;
  	                            } else {
  	                              paramValues = routeId;
  	                            }
  	                         }
                            }
                          }
                          if (paramValues != null) {
                            pop(url+"?routeId="+ paramValues+"&rdate="+dateField,500,500);
                          } else {
                            alert('Please Select a Row!');
                          }
                      }

                    function loadGpsEx(tableId, url, columnIndex) {
                        var table = document.getElementById(tableId);
                         var checkboxList = table.getElementsByTagName("input");
                          
                          var dateField = document.getElementById("dispDate").value;    
                          var paramValues = null;
                          for (i = 0; i < checkboxList.length; i++) {
                            if (checkboxList[i].type=="checkbox" && checkboxList[i].checked&& !checkboxList[i].disabled&&checkboxList[i].name.indexOf("_")==-1) {
                              var routeId = checkboxList[i].parentNode.parentNode.getElementsByTagName("td")[columnIndex].innerHTML
                              if(routeId != null && routeId.length > 0) {
  	                            if (paramValues != null) {
  	                              paramValues = paramValues+","+routeId;
  	                            } else {
  	                              paramValues = routeId;
  	                            }
  	                         }
                            }
                          }
                          if (paramValues != null) {
                        	  location.href = url+"?routeId="+ paramValues+"&rdate="+dateField;
                          } else {
                            alert('Please Select a Row!');
                          }
        			}
                  </script>
                </td>
                <td>
                  &nbsp;<form:errors path="dispDate" />
                </td>
                <td> 
                  <select id="zone" width="40" name="zone">
                      <option value="">Select Zone</option> 
                      <c:forEach var="zone" items="${zones}">                             
                          <c:choose>
                            <c:when test="${param.zone == zone.zoneCode}" > 
                              <option selected value="<c:out value="${zone.zoneCode}"/>"><c:out value="${zone.displayName}"/></option>
                            </c:when>
                            <c:otherwise> 
                              <option value="<c:out value="${zone.zoneCode}"/>"><c:out value="${zone.displayName}"/></option>
                            </c:otherwise> 
                          </c:choose>      
                        </c:forEach>   
                   </select>
                
                </td>
                <td align="center"><span style="font-size: 9px;">OR</span></td>
                <td> 
                    <select id="region" width="50" name="region">
                      <option value="">Select Region</option> 
                      <c:forEach var="region" items="${regions}">                             
                          <c:choose>
                            <c:when test="${param.region == region.code}" > 
                              <option selected value="<c:out value="${region.code}"/>"><c:out value="${region.name}"/></option>
                            </c:when>
                            <c:otherwise> 
                              <option value="<c:out value="${region.code}"/>"><c:out value="${region.name}"/></option>
                            </c:otherwise> 
                          </c:choose>      
                        </c:forEach>   
                   </select>
            
                  </td>
                   <td>
                     <input style="font-size:11px" type = "button" value="View" onclick="javascript:doCompositeLink('dispDate','zone','region','dispatch.do')" />
                  </td>  
                  <td>
                     <input style="font-size:11px" type = "button" value="Refresh Route" onclick="javascript:refreshRoute()" />
                  </td>
                  <td>
                     <input style="font-size:11px" type = "button" value="U/E" onclick="javascript:doUnassignedEmployees()" />
                  </td>
                  <td>
                     <input style="font-size:11px" type = "button" value="Unassigned Routes" onclick="javascript:doUnassignedRoutes('dispDate')" />
                  </td> 
                  <%if(com.freshdirect.transadmin.security.SecurityManager.isUserAdmin(request)){%> 
                  <td>
                     <input style="font-size:11px" type = "button" value="Act.Log" onclick="javascript:doActivityLog('dispDate')" />
                  </td>
                  <td>
                     <input style="font-size:11px" type = "button" value="Reason Code"  border="0" onclick="javascript:doReasonCode()" />
                  </td>
                  <%} %>
                  <td>
                    <a href="javascript:directions('ec_table','drivingdirection.do', 12)">
                  		<img src="./images/driving-directions.gif" width="90" height="25" border="0" alt="Driving Directions" title="Driving Directions" />
                  	</a>
                  <td>
                 
                  
                  <td>
                    <a href="javascript:loadGps('ec_table','gpsadmin.do', 12)">
                  		<img src="./images/gpsadmin.gif" border="0" alt="Garmin" title="GPS Admin" />
                  	</a>
                  </td>
                <!--  
                This functionality is not required for Copilot as the driving directions will be available from Airclic
                  <td>
	                    <a href="javascript:loadGpsEx('ec_table','gpsadminex.do', 11)">
	                  		<img src="./images/copilot.png" border="0" alt="CoPilot" title="CoPilot" />
	                  	</a>
	              </td>-->  
	              <td>	
						<% 
						if(airclic_msg && TransportationAdminProperties.isAirclicEnabled())
						{%>
						<%@ include file="/airclic/airclic_msg.jspf%>
						<% 	
						}
						%>
				</td>
              </tr>              
              </table>        
              <div align="center" id="errContainer"></div></td>
            </td>
          </tr>               
        </table> 
        
       <script language="javascript">
       var dispId, assignedValue;
         function doCompositeLink(compId1,compId2, compId3, url) {
          var param1 = document.getElementById(compId1).value;
          var param2 = document.getElementById(compId2).value;
          var param3 = document.getElementById(compId3).value;
          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2+"&"+compId3+"="+param3;
        } 
        
        function doUnassignedRoutes(compId1) {
        	 var param1 = document.getElementById(compId1).value;
        	javascript:pop('unassignedroute.do?routeDate='+param1, 400,600);
        }        
		function doActivityLog(compId1) {
        	 var param1 = document.getElementById(compId1).value;
        	 showForm(param1, 'D');
        }
		function doReasonCode() 
		{
			document.getElementById("result").innerHTML="";
	       	var panel=init("panel-2","Override Reason Code");
	       	panel.render(document.body);
	        panel.show();
       }
      </script>      
      </div>
      <input name="muniMetermaxValue" id="muniMetermaxValue" type="hidden" value="<%=muniMetermaxValue%>">
      <div id="dialog-dispatch" style="display: none">
	
  	<div class="muni" style="text-align:center;">Muni Meter Card</div>
 	<div class ="validateTips"> </div>
	  
	    <table>
	     
		<tr><td >
	      Card Value $</td><td valign="bottom"  colspan="2"> <input type="text" name="dispcardvalue" id="dispcardvalue"  maxlength="6" size="6" class="text ">
		  </td></tr><tr><td colspan="2" >
	      Card Not Assigned</td><td><input type="checkbox" name="cardnotassigned" id="cardnotassigned" value="X">
		  </td></tr>
	      </table>
	  
	</div>
	<div id="dialog-checkin" style="display: none" >
	
  	<div class="muni" style="text-align:center;">Muni Meter Card</div>
 	<div class ="validateTips"> </div>
	  
	    <table>
	    
		<tr><td >
	      Card Value $</td><td valign="bottom"  colspan="2"> <input type="text" name="chkincardvalue" id="chkincardvalue"  maxlength="6" size="6" class="text ">
		  </td></tr><tr><td colspan="2" >
	      Card Not returned</td><td><input type="checkbox" name="cardnotreturned" id="cardnotreturned" value="X">
		  </td></tr>
	      </table>
	  
	</div>
    
    <div align="center">
      <ec:table items="dispatchInfos"   action="${pageContext.request.contextPath}/dispatch.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
            width="98%"  rowsDisplayed="25" view="fd" >
            
            <ec:exportPdf fileName="dispatchschedule.pdf" tooltip="Export PDF" 
                      headerTitle="Dispatch Schedule" />
              <ec:exportXls fileName="dispatchschedule.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="dispatchschedule.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="dispatchobsoletemarker"> 
              <ec:column viewsAllowed="fd" title="Row" width="5px"  filterable="false" sortable="false" cell="selectcol"  property="dispatchId" /> 
              <ec:column viewsAllowed="fd" title="Nextel" width="5px"  filterable="false" sortable="false" cell="selectsplcol"  property="phoneAssigned" />
              <ec:column viewsAllowed="fd" title="Keys Out" width="5px"  filterable="false" sortable="false" cell="selectsplcol"  property="keysReady" />   
              <ec:column viewsAllowed="fd" title="Dispatched" width="5px"  filterable="false" sortable="false" cell="selectsplcol"  property="dispatched" />
              <ec:column viewsAllowed="fd" title="Keys In" width="5px"  filterable="false" sortable="false" cell="selectsplcol"  property="keysIn" /> 
              <ec:column viewsAllowed="fd" title="ChIn" width="5px"  filterable="false" sortable="false" cell="selectsplcol"  property="checkedIn" />  
              <ec:column alias="trnStatus" property="dispatchStatus"  title="Status"/> 
			  <ec:column property="facilityInfoEx" sortable="true" title="ORF-DTF"/>
              <ec:column alias="trnZoneRegion" property="regionZone" title="Region - Zone" />             
              <ec:column property="supervisorEx"   title="Sup" cell="tooltip"  />
              <ec:column cell="date" format="hh:mm aaa"  property="dispatchGroup" title="Group Time"/>
              <ec:column cell="date" format="hh:mm aaa"  property="startTimeEx" title="Truck Dispatch Time"/>
              <ec:column alias="trnRouterouteNumber" property="route"  width="10" title="Route"/>
              <ec:column alias="trnTrucktruckNumber" property="truck" width="10"  title="Truck"/>
              <ec:column alias="trnTruckLocation" property="location" width="10"  title="Location"/>
              <ec:column property="drivers"  cell="dispatchResCell" title="Driver"  filterable="true" alias="drivers"/>
              <ec:column property="helpers"  cell="dispatchResCell" title="Helper"  filterable="true" alias="helpers"/>
              <ec:column property="runners"  cell="dispatchResCell" title="Runner"  filterable="true" alias="runners"/>
              <ec:column cell="dispatchExtCell" property="extras" width="10"  title="Extras"/>
              <ec:column cell="dispatchAssetScanCell" property="scanAssets" width="10" title="Asset Status"/>
              <ec:column alias="dispatchTime"  property="dispatchTimeEx" title="Actual Dispatch Time"  cell="date" format="hh:mm aaa"/>
              <ec:column property="override"  title="Override Dispatch"/>
              <ec:column alias="trnComments" filterable="false" property="comments"  title="Comments"/> 
              
            </ec:row>
          </ec:table>
    </div>
    <script>
    
    function doClone(tableId, url) 
    {  
  	    var table = document.getElementById(tableId);
  	    var checkboxList = table.getElementsByTagName("input");    
  	    var rowSelCnt = 0;
  	    for (i = 0; i < checkboxList.length; i++) {
  	    	if (checkboxList[i].type=="checkbox" && checkboxList[i].checked && !checkboxList[i].disabled && checkboxList[i].name.indexOf("_") == -1) {
  	    		rowSelCnt++;  	    		
  	    	}
  	    }
  	    
  	    if(rowSelCnt === 0) {
  	    	alert('Please select a Row!');
  	    } else if(rowSelCnt > 1){
  	    	alert('Please select only one Row!');
  	    } else {
  	    	var paramValues = getParamList(tableId, url);
  		    if (paramValues != null) {
  		    	var paramArray = new Array();
  		    	paramArray = paramValues.split(",");
  		    	var hasConfirmed = confirm ("You are about to clone the selected dispatch entry. Do you want to continue?")
  		    	if (hasConfirmed) {
  		    		location.href = url+"?cloneId="+ paramArray[0]+"&filter="+getFilterTestValue();
  				} 
  		    }	
  	    }
	  }    
    
      addMultiRowHandlersColumnFilter('ec_table', 'rowMouseOver', 'editdispatch.do','id',0, 5,'dispDate');

      function getFilterTestValue()
      {
	      	var filters=getFilterValue(document.getElementById("ec"),false);
	      	filters+="&dispDate="+document.getElementById("dispDate").value;
	      	filters+="&zone="+document.getElementById("zone").value;
	      	filters+="&region="+document.getElementById("region").value;
	      	return escape(filters);
      }
      
    </script>
    <%@ include file='i_activityLog.jspf'%> 
     <%@ include file='i_ReasonCode.jspf'%> 
    <form name="newSubmit" action="dispatch.do" method="post">
    <input type=hidden name=id><input type=hidden name=dispDate>
    </form>
    <form name="edit"  method="get">   
    </form>
    <script type="text/javascript" src="js/munimeter.js"></script>
  </tmpl:put>
  
  
</tmpl:insert>
