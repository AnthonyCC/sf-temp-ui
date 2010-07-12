<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>

<tmpl:insert template='/common/sitelayout.jsp'>
<%    
  String dateRangeVal = request.getParameter("daterange") != null ? request.getParameter("daterange") : "";
  if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
%>
<% 
	String pageTitle = "Service Time Scenario";
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
	    document.getElementById("scenarioDay").value='<%=request.getParameter("scenarioDay")==null?"0":request.getParameter("scenarioDay")%>';
    
    </script>
	<div class="contentroot">

	<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem">
					<span class="scrTitle">
						<%=pageTitle%>
					</span>
					<span><input maxlength="40" name="daterange" id="daterange" value="<%= dateRangeVal %>" />						
	    			</span>
					<span>
						<a href="#" id="trigger_scheduleDate" style="font-size: 9px;">
                        <img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a>
                        <script language="javascript">     	 
		      				Calendar.setup(
				                       {
				                        showsTime : false,
				                        electric : false,
				                        inputField : "daterange",
				                        ifFormat : "%m/%d/%Y",
				                        singleClick: true,
				                        button : "trigger_scheduleDate" 
				                       }
		                    	  );
	    				</script>
                    </span>
                    	 <span>OR</span>
                    	 <select id="scenarioDay" name="scenarioDay" >
                          	<option value="0"></option>
                      		<option value="2">Mon</option><option value="3">Tue</option><option value="4">Wed</option><option value="5">Thu</option><option value="6">Fri</option><option value="7">Sat</option><option value="1">Sun</option>
                    	 </select>				
					<span><input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink('daterange','scenarioDay','dlvservicetimescenariodisplay.do')" onmousedown="this.src='./images/icons/view_ON.gif'" /></span>
				</div>
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
              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="scenario.code" />  
          <ec:column width="5px" alias="scenariodate" property="normalDate" title="Date"/>    
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
      addRowHandlers('ec_table', 'rowMouseOver', 'editdlvservicetimescenario.do','id',0, 0);
    </script>   
  </tmpl:put>
</tmpl:insert>
