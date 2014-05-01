<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%@ page import='com.freshdirect.transadmin.util.TransportationAdminProperties' %>

<%  
	pageContext.setAttribute("HAS_COPYBUTTON", "true");
	pageContext.setAttribute("HAS_CLONEBUTTON", "true");	
	pageContext.setAttribute("HAS_BULLPENBUTTON", "true");	
	String zoneVal = request.getParameter("zone") != null ? request.getParameter("zone") : "";	
%>
<% 
	String pageTitle = "Plan";
%>

<tmpl:insert template='/common/sitelayout.jsp'>

<tmpl:put name='title' direct='true'> Operations : <%=pageTitle%></tmpl:put>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>
  <tmpl:put name='content' direct='true'> 

	<c:if test="${not empty messages}">
		<div class="err_messages">
			<jsp:include page='/common/messages.jsp'/>
		</div>
	</c:if> 

  <div class="contentroot">

		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem" id="page_<%=pageTitle%>">						
						<div class="scrTitle" style="float:left;padding-top:3px"><%=pageTitle%></div>
						<div style="float:left;text-align:center;font-weight:bold">Week<br>
							<input maxlength="40" name="weekdate" id="weekdate" value='<c:out value="${weekDate}"/>' style="width:100px"/>
						 	<a href="#" id="trigger_planWDate" style="font-size: 9px;">
                        			<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a>
						</div>
						<div style="float:left;text-align:center;font-weight:bold">Date<br>&nbsp;
							<input maxlength="40" name="daterange" id="daterange" value='<c:out value="${dateRange}"/>' style="width:100px"/>
						 	<a href="#" id="trigger_planDate" style="font-size: 9px;">
                        			<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a>
						</div>&nbsp;
						<div style="float:left;text-align:center;font-weight:bold">Facility Location<br>&nbsp;		                  
		                  <select id="facilityLocation" name="facilityLocation">
		                      <option value="">All</option> 
		                      <c:forEach var="facilityLoc" items="${facilityLocations}">                             
		                          <c:choose>
		                            <c:when test="${param.facilityLocation == facilityLoc.code}" > 
		                              <option selected value="<c:out value="${facilityLoc.code}"/>"><c:out value="${facilityLoc.description}"/></option>
		                            </c:when>
		                            <c:otherwise> 
		                              <option value="<c:out value="${facilityLoc.code}"/>"><c:out value="${facilityLoc.description}"/></option>
		                            </c:otherwise>
		                          </c:choose>      
		                        </c:forEach>   
		                   </select>
						</div>&nbsp;
						<div style="float:left;text-align:center;font-weight:bold">Zone<br>&nbsp;
							<input maxlength="40" size="20" name="zone" id="zone" value="<%= zoneVal %>" style="width:100px" />
						</div>	&nbsp;					
						<div style="float:left;"><br>
	                   	  <span>&nbsp;<input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink('daterange','weekdate','zone','facilityLocation','plan.do');" onmousedown="this.src='./images/icons/view_ON.gif'" /></span>
	                      <input style="font-size:11px" type = "button" height="18" value=" U/A " onclick="javascript:doUnavailable('plan.do','weekdate','daterange','y')" />
	                      <input style="font-size:11px" type = "button" height="18" value="Kronos" onclick="javascript:doKronos('plan.do','weekdate','daterange','y','1')" />                  
	                      <% if(com.freshdirect.transadmin.security.SecurityManager.isUserAdminOrPlanning(request)) { %>							
	                      	<input style="font-size:11px" type = "button" height="18" value="Publish" onclick="javascript:doPublish();" />
	                      	<span> <input style="font-size:11px" type = "button" height="18"  id="lockWaveSyncBtn" value="Lock WaveSync" /></span>	                      	
	                      <%} %>
	                      <%if(com.freshdirect.transadmin.security.SecurityManager.isUserAdmin(request)){%> 
		                    <input style="font-size:11px" type = "button" value="Activity Log" onclick="javascript:doActivityLog('daterange')" />		                  
		                  <%} %>
	                   </div>  
				</div>
			</div>
		</div> 
		

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">

      <form id="planListForm" action="" method="post">  
        <ec:table items="planlist"  filterRowsCallback="exactMatch" action="${pageContext.request.contextPath}/plan.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
            width="98%"  view="fd" form="planListForm" autoIncludeParameters="true" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="transportationplan.pdf" tooltip="Export PDF" 
                      headerTitle="Transportation Plan" />
              <ec:exportXls fileName="transportationplan.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationplan.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">
              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="planId" />             
              <ec:column cell="date" property="weekDate" sortable="true" title="Week"/>
              <ec:column cell="date" property="planDate" sortable="true" title="Date"/>
              <ec:column property="planDay" sortable="true" title="Day"/>
			  <ec:column property="facilityInfoEx" sortable="true" title="ORF-DTF"/>
			  <ec:column property="equipmentTypeS" sortable="true" title="Equipment Type"/> 
              <ec:column cell="tooltip" alias="zoneCode" property="zoneNameEx" title="Zone"/>
              <ec:column alias="region" property="regionCode" title="Region"/>
              <ec:column property="supervisorEx"   title="Sup" cell="tooltip"  />
              <ec:column cell="date" format="hh:mm aaa"  property="dispatchGroup" title="Dispatch Group Time"/>
              <ec:column cell="date" format="hh:mm aaa"  property="startTimeEx" title="Truck Dispatch Time"/>
              <ec:column cell="date" format="hh:mm aaa"  property="endTimeEx" title="Truck End Time"/>
              <ec:column filterable="true" property="drivers"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Driver" alias="drivers"/>
              <ec:column filterable="true" property="helpers"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Helper" alias="helpers"/>
              <ec:column filterable="true" property="runners"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Runner" alias="runners"/>
              <ec:column property="sequence"  width="4px" title="Rank"/>
              <ec:column property="open"  width="4px" title="Open"/>
              <ec:column cell="date" format="hh:mm aaa" alias="cutOffTime" property="cutOffTimeEx" title="Handoff Time"/>
            </ec:row>
          </ec:table>
       </form>
	   
					</div></div>
				</div>
     </div>   
     <script>
     var jsonrpcClient = new JSONRpcClient("dispatchprovider.ax");
     function doPublish(){
  		var deliveryDate = document.getElementById('daterange').value;
  		if(deliveryDate == null || deliveryDate.trim().length == 0) {
      		alert("Please select valid Day!");
  		} else {
  			var result = jsonrpcClient.AsyncDispatchProvider.canPublishWave(deliveryDate);
  			if(result != null && result.hasPerviousPublish) {
     			var source = "Plan";
     			if(result.previousPublishScrib) {
     				source = "Scrib";
     			}
 				if (!confirm ("Wave has already been publish from "+source+". Do you want to continue?")) {
     				return;
 				}
 			}
 			try {
 				document.getElementById('ajaxBusy').style.display = 'block';
 				jsonrpcClient.AsyncDispatchProvider.publishWave(deliveryDate);
  				document.getElementById('ajaxBusy').style.display = 'none';
  				alert("Publish completed successfully");
 			} catch(rpcException) {
 				document.getElementById('ajaxBusy').style.display = 'none';
  				alert("Unable to publish wave. Please try to refresh the browser window!\n"+rpcException);
  			} 
  		}
	 }
	
      function doCompositeLink(compId1,compId2,compId3,compId4,url) {
          var param1 = document.getElementById(compId1).value;
          var param2 = document.getElementById(compId2).value;
          var param3 = document.getElementById(compId3).value;
          var param4 = document.getElementById(compId4).value;
          
          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2+"&"+compId3+"="+param3+"&"+compId4+"="+param4;
      } 

      addRowHandlersFilterTest('ec_table', 'rowMouseOver', 'editplan.do','id',0, 0);
      function getFilterTestValue() {
             var filters = getFilterValue(document.getElementById("planListForm"),false);
             var param1 = document.getElementById("weekdate").value;
     		 var param2 = document.getElementById("daterange").value;
     		 var param3 = document.getElementById("zone").value;
     		 var param4 = document.getElementById("facilityLocation").value;
             filters+="&weekdate="+param1;
             filters+="&daterange="+param2;
             filters+="&zone="+param3;
             filters+="&facilityLocation="+param4;   
             return escape(filters);
       }
 
      function doDelete(tableId, url) 
      {    
		    var paramValues = getParamList(tableId, url);
		    if (paramValues != null) {
		    	var hasConfirmed = confirm ("Do you want to delete the selected records?")
		    	if (hasConfirmed) 
				{
		    		var param1 = document.getElementById("weekdate").value;
		     		var param2 = document.getElementById("daterange").value;
		     		var param3 = document.getElementById("zone").value;
		            var filters="&weekdate="+param1+"&daterange="+param2+"&zone="+param3+"&"+getFilterValue(document.getElementById("planListForm"),false);				  	
		    		location.href = url+"?id="+ paramValues+filters;
				} 
		    } else {
		    	alert('Please Select a Row!');
		    }
	  }
      
      function doBullpen(tableId, url) 
      {    
    	    var table = document.getElementById(tableId);
	  	    var checkboxList = table.getElementsByTagName("input");    
	  	    var rowSelCnt = 0;
	  	    var bullpenColIndex = '6';
	  	  	var isBullpen = false;
	  	    for (i = 0; i < checkboxList.length; i++) {
	  	    	if (checkboxList[i].type=="checkbox" && checkboxList[i].checked) {
	  	    		rowSelCnt++;
	  	    		var bullpen = checkboxList[i].parentNode.parentNode.getElementsByTagName("td")[6].textContent;
	  	    		if(bullpen != null && bullpen.length > 0 && bullpen === 'Bullpen') {                    
	  	    			isBullpen = true;	                    
	                } 	    		
	  	    	}
	  	    }
	  	    
	  	    if(rowSelCnt === 0) {
	  	    	alert('Please select a Row!');
	  	    } else if(isBullpen){
	  	    	alert('You selected a Bullpen. Please select a truck to convert to bullpen!');
	  	    } else {
	    	  	var paramValues = getParamList(tableId, url);
			    if (paramValues != null) {
			    	var hasConfirmed = confirm ("Do you want to convert selected truck to bullpen?")
			    	if (hasConfirmed) {
			    		var param1 = document.getElementById("weekdate").value;
			     		var param2 = document.getElementById("daterange").value;
			     		var param3 = document.getElementById("zone").value;
			            var filters="&weekdate="+param1+"&daterange="+param2+"&zone="+param3+"&"+getFilterValue(document.getElementById("planListForm"),false);
			    		location.href = url+"?id="+ paramValues+filters;
					} 
			    } 
			 }
	  }
      
      function doClone(tableId, url) 
      {  
    	    var table = document.getElementById(tableId);
    	    var checkboxList = table.getElementsByTagName("input");    
    	    var rowSelCnt = 0;
    	    for (i = 0; i < checkboxList.length; i++) {
    	    	if (checkboxList[i].type=="checkbox" && checkboxList[i].checked) {
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
    		    	var hasConfirmed = confirm ("You are about to clone the selected plan entry. Do you want to continue?")
    		    	if (hasConfirmed) {
    		    		location.href = url+"?planRefId="+ paramValues+"&filter="+getFilterTestValue();
    				} 
    		    }	
    	    }
	  }
		
	  function doUnavailable(url,id1,id2,param3)
	  {
	     var param1 = document.getElementById(id1).value;
	     var param2 = document.getElementById(id2).value;
         var id3 = "unavailable";
          
         javascript:pop(url+"?"+id1+"="+ param1+"&"+id2+"="+param2+"&"+id3+"="+param3, 400,600);
	  }

	  function doKronos(url,id1,id2,param3,param4)
	  {
	  	var param1 = document.getElementById(id1).value;
	  	var param2 = document.getElementById(id2).value;
        var id3 = "kronos"; 
        var id4 = "file";          
        location.href =url+"?"+id1+"="+ param1+"&"+id2+"="+param2+"&"+id3+"="+param3+"&"+id4+"="+param3  ;
	  }
	  
	  function doActivityLog(compId1) {
     	 var param1 = document.getElementById(compId1).value;
     	 showForm(param1, 'P');
      }
	   Calendar.setup(
               {
                 showsTime : false,
                 electric : false,
                 inputField : "daterange",
                 ifFormat : "%m/%d/%Y",
                 singleClick: true,
                 button : "trigger_planDate" 
                }
               );
	   Calendar.setup(
               {
                 showsTime : false,
                 electric : false,
                 inputField : "weekdate",
                 ifFormat : "%m/%d/%Y",
                 singleClick: true,
                 button : "trigger_planWDate" 
                }
               );

    </script>   
    <%@ include file='i_activityLog.jspf'%> 
    <% if(com.freshdirect.transadmin.security.SecurityManager.isUserAdminOrPlanning(request)) { %>
    	<%@ include file='/common/i_wavesynclock.jspf'%>
    <% } %>>
  </tmpl:put>
</tmpl:insert>
