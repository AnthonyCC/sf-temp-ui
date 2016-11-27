<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.*' %>

<%
	if(com.freshdirect.transadmin.security.SecurityManager.isUserAdminOrPlanning(request)){
		pageContext.setAttribute("HAS_ADDBUTTON", "true");	
		pageContext.setAttribute("HAS_DELETEBUTTON", "true");
		pageContext.setAttribute("HAS_COPYBUTTON", "true");	
		pageContext.setAttribute("HAS_CLONEBUTTON", "true");
	} else {
		pageContext.setAttribute("HAS_ADDBUTTON", "false");	
		pageContext.setAttribute("HAS_DELETEBUTTON", "false");
		pageContext.setAttribute("HAS_COPYBUTTON", "false");	
		pageContext.setAttribute("HAS_CLONEBUTTON", "false");
	}
	
  String dateRangeVal = request.getParameter("daterange") != null ? request.getParameter("daterange") : "";
  String zoneVal = request.getParameter("zone") != null ? request.getParameter("zone") : "";
  if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
%>
<% 
	String pageTitle = "Scrib";
%>

<tmpl:insert template='/common/sitelayout.jsp'>
<tmpl:put name='yui-lib'>
	<%@ include file='/common/i_yui.jspf'%>	
</tmpl:put>

	<tmpl:put name='title' direct='true'> Routing : <%=pageTitle%></tmpl:put>

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
					<div style="float:left;">
						<div style="float:left;">
							<span class="scrTitle"><%=pageTitle%></span>
						</div>
						<div style="margin-top:4px;float:left;">Week Of:</div>
						<div style="float:left;">
							<input style="width:85px;" maxlength="40" name="daterange" id="daterange" value='<c:out value="${weekDate}"/>' />
							<span><a href="#" id="trigger_scribDate" style="font-size: 9px;">
	                        	<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a></span>
	                    	 <select id="scribDay" name="scribDay" >
	                          	<option value="All">--All Days</option>
	                      		<option value="2">Monday</option><option value="3">Tuesday</option><option value="4">Wednesday</option><option value="5">Thurdsay</option><option value="6">Friday</option><option value="7">Saturday</option><option value="8">Sunday</option>
	                    	 </select>				
							<span><input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink('daterange','scribDay','scrib.do')" onmousedown="this.src='./images/icons/view_ON.gif'" /></span>
							<% if(com.freshdirect.transadmin.security.SecurityManager.isUserAdminOrPlanning(request)) { %>
							<span> <input style="font-size:11px" type = "button" height="18" value="Generate Plan" onclick="javascript:showGeneratePlanForm();" /></span>
							<span> <input style="font-size:11px" type = "button" height="18"  value="Add Label" onclick="javascript:showLabelForm();" /></span>
							<span> <input style="font-size:11px" type = "button" height="18"  value="View Labels" onclick="javascript:showViewLabelsForm();" /></span>
							<span> <input style="font-size:11px" type = "button" height="18"  value="Publish" onclick="javascript:doPublish();" /></span>
							<span> <input style="font-size:11px" type = "button" height="18"  id="lockWaveSyncBtn" value="Lock WaveSync" /></span>
							<span> <input style="font-size:11px" type = "button" height="18"  value="Upload" onclick="javascript:uploadScribs();" /></span>							
							<% } %>
						</div>
					</div>			
				</div>
			</div>
		</div> 
		

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">

      <form id="scribListForm" action="" method="post">  
        <ec:table items="scriblist"  filterRowsCallback="exactMatch" action="${pageContext.request.contextPath}/scrib.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
            width="98%"  view="fd" form="scribListForm" autoIncludeParameters="true" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="transportationScrib.pdf" tooltip="Export PDF" 
                      headerTitle="Transportation Scrib" />
              <ec:exportXls fileName="transportationscrib.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationscrib.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">
              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="scribId" />              
              <ec:column cell="date" property="scribDate" sortable="true" title="Date"/>
              <ec:column property="facilityInfoEx" sortable="true" title="ORF-DTF"/>
              <ec:column property="zoneS" sortable="true" title="Zone"/>
              <ec:column property="regionS" sortable="true" title="Region" /> 
              <ec:column property="supervisorName" sortable="true" title="Sup" />
              <ec:column cell="date" format="hh:mm aaa"  property="dispatchGroup" title="Dispatch Group Time"/>
              <ec:column cell="date" format="hh:mm aaa"  property="startTime" title="Truck Dispatch Time"/>
              <ec:column cell="date" format="hh:mm aaa"  property="endTime" title="Truck End Time"/>
              <ec:column cell="date" format="HH:mm"      property="preTripTime" title="Pre-Trip Time"/>
              <ec:column cell="date" format="HH:mm"      property="postTripTime" title="Post-Trip Time"/>
			  <ec:column cell="date" format="hh:mm aaa"  property="maxReturnTime" title="Max Return"/>  
              <ec:column property="truckCnt" sortable="true" title="No of Trucks/CD Trailers"/> 
              <ec:column property="equipmentTypeS" sortable="true" title="Equipment Type"/> 
              <ec:column property="handTruckCnt" sortable="true" title="No of HandTrucks"/> 
              <ec:column cell="date" format="hh:mm aaa"  property="waveStart" title="*Wave Start"/>     
			  <ec:column cell="date" format="HH:mm"  property="prefRunTime" title="*Pref Time"/>  
			  <ec:column cell="date" format="HH:mm"  property="maxRunTime" title="*Max Time"/> 
			  <ec:column cell="date" format="hh:mm aaa"  property="cutOffTime" title="Handoff Time"/>			   
            </ec:row>
          </ec:table>
       </form>
	   
					</div></div>
				</div>
     </div>   
     <script>
	    function uploadScribs(){
			 var confirmed = confirm ("You are about to upload scrib data. Do you want to continue?");
			 if(confirmed){
				javascript:window.open('uploadschedules.do?processType=SCR','upload','height=250,width=400,resizable=no');
			 }
		}	
     	function doPublish(){
     		var weekOff = document.getElementById('daterange').value;
     		var dayOff = document.getElementById('scribDay').value;
     		if(weekOff == null || weekOff.trim().length == 0 
     	     		|| dayOff == null || dayOff.trim().length == 0 || "All" == dayOff) {
         		alert("Please select valid Week Of and Day!");
     		} else {
     			var dispatchRpcClient = new JSONRpcClient("dispatchprovider.ax");
     			var result = dispatchRpcClient.AsyncDispatchProvider.canPublishWave(weekOff, dayOff);
     			if(result != null && result.hasPerviousPublish) {
         			var source = "Plan";
         			if(result.previousPublishScrib) {
         				source = "Scrib";
         			}
         			if(source == "Plan") {
	     				alert("No Scrib Publish allowed after Plan Publish!");
	     				return;
         			} else {
         				if (!confirm ("Wave has already been publish from "+source)) {
	         				return;
	     				}
         			}
     			}
     			try {
     				document.getElementById('ajaxBusy').style.display = 'block';
     				dispatchRpcClient.AsyncDispatchProvider.publishWave(weekOff, dayOff);
     				document.getElementById('ajaxBusy').style.display = 'none';
     				alert("Publish completed successfully");
     			} catch(rpcException) {
     				document.getElementById('ajaxBusy').style.display = 'none';
      				alert("Unable to publish wave. Please try to refresh the browser window!\n"+rpcException);
      			}         			
     		}
		}
		
		function showViewLabelsForm(){
			javascript:window.open('viewscriblabel.do','y','height=450,width=400');
		}
          	 
     	 Calendar.setup(
                      {
                        showsTime : false,
                        electric : false,
                        inputField : "daterange",
                        ifFormat : "%m/%d/%Y",
                        singleClick: true,
                        button : "trigger_scribDate" 
                       }
                      );

        function doCompositeLink(compId1,compId2, url,generatePlan) {
        	var param1 = document.getElementById(compId1).value;         
	          var param2 = document.getElementById(compId2).value;
	          var param3="";         
	          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+ param2;
        } 

        <%if(com.freshdirect.transadmin.security.SecurityManager.isUserAdminOrPlanning(request)){%>
       		addRowHandlersFilterTest('ec_table', 'rowMouseOver', 'editscrib.do','scribId',0, 0);
       	<% } %>
       	
      	document.getElementById("scribDay").value='<%=request.getAttribute("scribDay")%>';

        function getFilterTestValue()
      	{
	      	var filters=getFilterValue(document.getElementById("scribListForm"),false);
	      	filters+="&daterange="+document.getElementById("daterange").value;
	      	filters+="&scribDay="+document.getElementById("scribDay").value;
	      	return escape(filters);
        }

        function doDelete(tableId, url) 
        {    
  		    var paramValues = getParamList(tableId, url);
  		    if (paramValues != null) {
  		    	var hasConfirmed = confirm ("Do you want to delete the selected records?")
  		    	if (hasConfirmed) 
  				{
  		    		var param1 = document.getElementById("daterange").value;
  		     		var param2 = document.getElementById("scribDay").value;
  		            var filters="&daterange="+param1+"&scribDay="+param2+"&"+getFilterValue(document.getElementById("scribListForm"),false)
  				  	location.href = url+"?id="+ paramValues+filters;
  				} 
  		    } else {
  		    	alert('Please Select a Row!');
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
      		    	var hasConfirmed = confirm ("You are about to clone the selected scrib entry. Do you want to continue?")
      		    	if (hasConfirmed) {
      		    		location.href = url+"?scribRefId="+ paramValues+"&filter="+getFilterTestValue();
      				} 
      		    }	
      	    }
  	  }
    </script>
     <%@ include file='i_generateplan.jspf'%>
     <%@ include file='i_addlabel.jspf'%>
     <%@ include file='/common/i_wavesynclock.jspf'%>
  </tmpl:put>
</tmpl:insert>
