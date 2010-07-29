<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%@ page import= 'com.freshdirect.framework.util.DateUtil' %>
<%@ page import= 'java.util.Calendar' %>

<tmpl:insert template='/common/sitelayout.jsp'>
<%    
  String fromdateRangeVal = request.getParameter("fromdaterange") != null ? request.getParameter("fromdaterange") : "";
  String todateRangeVal = request.getParameter("todaterange") != null ? request.getParameter("todaterange") : ""; 
%>
<% 
	String pageTitle = "Scenario";
%>

  <tmpl:put name='title' direct='true'>Routing : <%=pageTitle%></tmpl:put>
  
  <tmpl:put name='content' direct='true'>
	<script language="javascript">     	 
	    function doCompositeLink(compId1,compId2, url) 
	    {
	    	var param1 = document.getElementById(compId1).value;         
	        var param2 = document.getElementById(compId2).value;
	        var param3="";         
	        location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+ param2;
		} 
    </script>
	<div class="contentroot">

	<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem">
					<span class="scrTitle">
						<%=pageTitle%>
					</span>
					<span>
					
                  <span style="font-size: 12px;font-weight: plain;">From:</span><input style="width:80px;" maxlength="12" name="fromdaterange" id="fromdaterange" value="<%= fromdateRangeVal %>" />
                  <span>
						<a href="#" id="trigger_Date" style="font-size: 9px;">
                        <img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a>
                        <script language="javascript">     	 
		      				Calendar.setup(
				                       {
				                        showsTime : false,
				                        electric : false,
				                        inputField : "fromdaterange",
				                        ifFormat : "%m/%d/%Y",
				                        singleClick: true,
				                        button : "trigger_Date" 
				                       }
		                    	  );
	    				</script>
                    </span>
                  <span style="font-size: 12px;font-weight: plain;">To:</span><input style="width:80px;" maxlength="12" name="todaterange" id="todaterange" value="<%= todateRangeVal %>" />
                  						
	    			</span>
					<span>
						<a href="#" id="trigger_scheduleDate" style="font-size: 9px;">
                        <img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a>
                        <script language="javascript">     	 
		      				Calendar.setup(
				                       {
				                        showsTime : false,
				                        electric : false,
				                        inputField : "todaterange",
				                        ifFormat : "%m/%d/%Y",
				                        singleClick: true,
				                        button : "trigger_scheduleDate" 
				                       }
		                    	  );
	    				</script>
                    </span>
                    &nbsp;&nbsp;				
					<span><input id="view_button" height="18"  type="button" value="View"   onclick="javascript:doCompositeLink('fromdaterange','todaterange','dlvservicetimescenariodisplay.do')" onmousedown="this.src='./images/icons/view_ON.gif'" /></span>
					<span><input id="clear_button" height="18"  type="button" value="Clear" onclick="document.getElementById('fromdaterange').value='';document.getElementById('todaterange').value='';" /></span>
					<span><input id="delete_button" height="18" type="button" value="Delete" onclick="javascript:deleteScenarioHandlers();" /></span>
					&nbsp;&nbsp;&nbsp;
					<span><div style="width:80px;float:right;" class="orphanScenario">Orphan</div></span>
					&nbsp;&nbsp;&nbsp;
					<span><div style="width:80px;float:right;" class="defaultScenario">Default</div></span>
				</div>				
			</div>
			<div>
				<span class="screenmessages"><jsp:include page='/common/messages.jsp'/></span>
			</div>
		</div>

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
				<form id="dlvServiceTimeScenarioForm" action="" method="post">
        <ec:table items="dlvservicetimescenariolist"   action="${pageContext.request.contextPath}/dlvservicetimescenario.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
            width="98%"  view="fd" form="dlvServiceTimeScenarioForm" autoIncludeParameters="false" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="transportationservicetimescenarios.pdf" tooltip="Export PDF" 
                      headerTitle="Transportation Service Time Scenarios" />
              <ec:exportXls fileName="transportationservicetimescenarios.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationservicetimescenarios.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">
              	<ec:column title=" " width="5px" filterable="false" sortable="false" cell="selectcol" property="scenariodayId" />  
		          <ec:column width="7px" alias="scenariodate" property="normalDate" title="Date"/>    
		          <ec:column property="dayOfWeekInText" title="Day"/>
		          <ec:column width="5px" alias="scenariocode" property="scenario.code" title="Code"/>    
		          <ec:column property="scenario.description" title="Description"/>
		      	  <ec:column property="scenario.serviceTimeFactorFormula" title="Service Time Factor Formula"/>
		          <ec:column property="scenario.serviceTimeFormula" title="Service Time Formula"/>
		          <ec:column property="scenario.defaultCartonCount" width="5px" title="Carton Count"/>
		          <ec:column property="scenario.defaultCaseCount" width="5px" title="Case Count"/>
		          <ec:column property="scenario.defaultFreezerCount" width="5px" title="Freezer Count"/>
		          <ec:column property="scenario.orderSizeFormula" title="Order Size Formula"/>                
		          <ec:column property="scenario.needsLoadBalance" width="5px" title="Load Balance"/> 
		          <ec:column property="scenario.loadBalanceFactor" width="5px" title="Balance Factor"/>
		          <ec:column format="com.freshdirect.routing.constants.EnumBalanceBy" cell="enumcol" property="scenario.balanceBy" title="Balance By"/>                         
		          <ec:column property="scenario.lateDeliveryFactor" width="10px" title="Late Delivery Factor"/> 
          </ec:row>
          </ec:table>
       </form>
				</div>
			</div>
		</div>
	</div> 
	 <script>
	 rowHandlers('ec_table', 'rowMouseOver', 'editdlvservicetimescenario.do','id',0, 0,false,3);
      
      function deleteScenarioHandlers() {
    		
    	  var table = document.getElementById('ec_table');
    	  
    	    var checkboxList = table.getElementsByTagName("input");    
    	    var paramValues = null;
    	    for (i = 0; i < checkboxList.length; i++) {
    	    	if (checkboxList[i].type=="checkbox" && checkboxList[i].checked) {
    	    		var rowFld = checkboxList[i].parentNode.parentNode.getElementsByTagName("td")[3];  		
    	    		if (paramValues != null) {
    	    			paramValues = paramValues+","+rowFld.innerHTML;
    	    		} else {
    	    			paramValues = rowFld.innerHTML;
    	    		}
    	    	}
    	    }
    	    if(paramValues == null || paramValues.trim().length == 0) {
    	    	alert("Select a Service Time Scenario!");
      		} else {
	  		   
	  		  	var confirmed=confirm('Are you sure you want to  delete Scenario.');
         	 	if(confirmed) {
         	 		var jsonrpcClient = new JSONRpcClient("dispatchprovider.ax");             	 	
             	 	var result = jsonrpcClient.AsyncDispatchProvider.deleteServiceTimeScenario(deleteScenarioFormCallback,paramValues);
             	 	location.href = location.href;
         	 	}
      	    }  
     }
     function deleteScenarioFormCallback(result, exception) {
      	  
          if(exception) {               
                alert('Unable to connect to host system. Please contact system administrator!');               
                return;
          }
          if(result == 0) {
              	alert('Please delete Zones or ScenarioDays associated with Scenario before deleting the scenario.');
          }                           
 	}  
				
  	
    </script>   
  </tmpl:put>
</tmpl:insert>
