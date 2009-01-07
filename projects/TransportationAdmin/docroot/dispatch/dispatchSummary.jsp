<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%  pageContext.setAttribute("HAS_ADDBUTTON", "false"); 
  pageContext.setAttribute("HAS_CONFIRMBUTTON", "false"); 
  pageContext.setAttribute("HAS_DELETEBUTTON", "false"); 
   String dateRangeVal = request.getParameter("dispDate") != null ? request.getParameter("dispDate") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
  %>
  <input type=hidden name="dispDate" value="" />

<% 
	String pageTitle = "Dispatch Summary";
%>
	
<tmpl:insert template='/common/sitelayout.jsp'>

<tmpl:put name='title' direct='true'> Operations : <%=pageTitle%></tmpl:put>

  <tmpl:put name='content' direct='true'>

	<c:if test="${not empty messages}">
		<div class="err_messages">
			<jsp:include page='/common/messages.jsp'/>
		</div>
	</c:if> 


	<div class="contentroot">
		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem">
					<span class="scrTitle">
						Today: <%= TransStringUtil.getFullMonthDate(new Date()) %>
					</span>
					
					
					<span><input id="refresh_button" type="image" alt="Refresh" src="./images/icons/refresh.gif" onclick="javascript:refreshRoute()"  onmousedown="this.src='./images/icons/refresh_ON.gif'" /></span>
				</div>
			</div>
		</div> 

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
				<ec:table items="dispatchInfos"   action="${pageContext.request.contextPath}/dispatchSummary.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif" title="&nbsp;"
            width="98%"  rowsDisplayed="25" view="fd" >
            
            <ec:exportPdf fileName="dispatchschedule.pdf" tooltip="Export PDF" 
                      headerTitle="Dispatch Schedule" />
              <ec:exportXls fileName="dispatchschedule.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="dispatchschedule.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="dispatchobsoletemarker">
              <ec:column title=" "width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="dispatchId" />               
              <ec:column alias="trnConfirm" width="5" cell="confirmcol" property="confirmed" title="C"  />            
              <ec:column alias="trnZonezoneNumber" width="12" property="zoneCode" title="Zone" />
              <ec:column alias="trnZoneRegion" property="regionName" title="Region" />
              <ec:column  alias="trnTimeslotslotName"  property="startTime" title="Start Time"/>              
              <ec:column alias="trnRouterouteNumber" property="route"  width="10" title="Route"/>
              <ec:column alias="trnTrucktruckNumber" property="truck" width="10"  title="Truck"/>
              <ec:column alias="trnNoOfStops" property="noOfStops" width="10"  title="Stops"/>
              <ec:column alias="trnStatus" property="statusName"  title="Status"/>              
              <ec:column property="drivers"  cell="com.freshdirect.transadmin.web.ui.FDDispatchResourceCell" title="Driver"  filterable="false" alias="001"/>
              <ec:column property="helpers"  cell="com.freshdirect.transadmin.web.ui.FDDispatchResourceCell" title="Helper"  filterable="false" alias="002"/>
              <ec:column property="runners"  cell="com.freshdirect.transadmin.web.ui.FDDispatchResourceCell" title="Runner"  filterable="false" alias="003"/>
              <ec:column alias="trnComments" property="comments"  title="Comments"/>              
            </ec:row>
          </ec:table>
				</div>
			</div>
		</div>
	</div>
    
    <script>
      addMultiRowHandlers('ec_table', 'rowMouseOver', 'editdispatch.do','id',0, 0,'dispDate');  
         function doCompositeLink(compId1,compId2, compId3, url) {
          var param1 = document.getElementById(compId1).value;
          var param2 = document.getElementById(compId2).value;
          var param3 = document.getElementById(compId3).value;
          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2+"&"+compId3+"="+param3;
        } 
		
					function refreshRoute() {
                        var hasConfirmed = confirm ("This action can overwrite existing Route/Truck Assignment. Are you sure you want to perform this operation?");
                        if(hasConfirmed) {
                           location.href = "<c:out value="${pageContext.request.contextPath}"/>/refreshRouteSummary.do?dispDate=<%= dateRangeVal %>&summary=true";
                        }
                    }
                    

                    function sendRequest(tableId, url, message) {
                      
                      var table = document.getElementById(tableId);
                        var checkboxList = table.getElementsByTagName("input");
                        var dateField = document.getElementById("dispDate").value;    
                        var paramValues = null;
                        for (i = 0; i < checkboxList.length; i++) {
                          if (checkboxList[i].type=="checkbox" && checkboxList[i].checked) {
                            
                            if (paramValues != null) {
                              paramValues = paramValues+","+checkboxList[i].name+"$"+dateField;
                            } else {
                              paramValues = checkboxList[i].name+"$"+dateField;
                            }
                          }
                        }
                        if (paramValues != null) {
                          var hasConfirmed = confirm (message);
                        if (hasConfirmed) {
                            location.href = url+"?id="+ paramValues;
                        } 
                        } else {
                          alert('Please Select a Row!');
                        }
                    }
    </script>   
  </tmpl:put>
</tmpl:insert>
