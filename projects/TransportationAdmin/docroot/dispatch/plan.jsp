<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>

<%  pageContext.setAttribute("HAS_COPYBUTTON", "true");  
  String dateRangeVal = request.getParameter("daterange") != null ? request.getParameter("daterange") : "";
  String zoneVal = request.getParameter("zone") != null ? request.getParameter("zone") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
%>
<% 
	String pageTitle = "Planning";
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
						<%=pageTitle%>
					</span>
						<span><input maxlength="10" size="10" name="daterange" id="daterange" value="<%= dateRangeVal %>" /></span>
						<span><input id="trigger_daterange" type="image" alt="Calendar" src="./images/icons/calendar.gif" onmousedown="this.src='./images/icons/calendar_ON.gif'" onmouseout="this.src='./images/icons/calendar.gif';" onclick="javascript:doCompositeLink('daterange','zone','plan.do');" /></span>

							<span>  
						<select id="zone" name="zone">
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
						</select></span>
						<span><input id="view_button" type="image" alt="View" src="./images/icons/view.gif" onclick="javascript:doCompositeLink('daterange','zone','region','dispatch.do')" onmousedown="this.src='./images/icons/view_ON.gif'" /></span>
						<span><input id="view_button" type="image" alt="View" src="./images/icons/dispatch.gif" onclick="javascript:doCompositeLink('daterange','zone','region','autoDispatch.do')" onmousedown="this.src='./images/icons/dispatch_ON.gif'" /></span>
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
              <ec:column cell="date" property="planDate" sortable="true" title="Date"/>
              <ec:column property="planDay" sortable="true" title="Day"/>
              <ec:column alias="zoneCode" property="zoneName" title="Zone"/>
              <ec:column alias="region" property="regionCode" title="Region"/>
              <ec:column property="supervisorEx"   title="Supervisor" cell="tooltip"  />
              <ec:column cell="date" format="hh:mm aaa" alias="startTime" property="startTime" title="Start Time"/>
              <ec:column cell="date" format="hh:mm aaa" alias="firstDeliveryTime" property="firstDeliveryTime" title="First Dlv"/>

              <ec:column property="drivers"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Driver" alias="001"/>
              <ec:column property="helpers"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Helper" alias="002"/>
              <ec:column property="runners"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Runner" alias="003"/>
              <ec:column property="sequence"  title="Rank"/>
            <%--<ec:column property="zonetypeResources" headerCell="com.freshdirect.transadmin.web.ui.FDCompositeResourceHeaderCell" cell="resource" title="Driver" alias="001"/>
            <ec:column property="zonetypeResources" headerCell="com.freshdirect.transadmin.web.ui.FDCompositeResourceHeaderCell" cell="resource" title="Helper" alias="002"/>
            <ec:column property="zonetypeResources" headerCell="com.freshdirect.transadmin.web.ui.FDCompositeResourceHeaderCell" cell="resource" title="Runner" alias="003"/>--%>                      

              <%--<ec:column alias="supervisor" property="region.code" title="Supervisor"/>
              
              <ec:column alias="trnTimeslotslotName" sortable="true" property="trnTimeslot" title="Start Time"/>
              <ec:column alias="trnEndTimeslotslotName" property="trnEndTimeslot.slotName" title="End Time"/>
              <ec:column alias="trnDrivername" property="trnDriver.name" title="Driver"/>
              <ec:column alias="trnPrimaryHelpername" property="trnPrimaryHelper.name" title="Helper1"/>
              <ec:column alias="trnSecondaryHelpername" property="trnSecondaryHelper.name" title="Helper2"/>--%>
              
            </ec:row>
          </ec:table>
       </form>
	   
					</div></div>
				</div>
     </div>   
     <script>
         function doCompositeLink(compId1,compId2, url) {
          var param1 = document.getElementById(compId1).value;
          var param2 = document.getElementById(compId2).value;
          
          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2;
        } 
      addRowHandlers('ec_table', 'rowMouseOver', 'editplan.do','id',0, 0);
		 Calendar.setup(
					{
						showsTime : false,
						electric : false,
						inputField : "daterange",
						ifFormat : "%m/%d/%Y",
						singleClick: true,
						button : "trigger_daterange" 
					}
				);	
    </script>   
  </tmpl:put>
</tmpl:insert>
