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
		</div>
	</c:if> 
  
  <div class="contentroot">

		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem">
					<span class="scrTitle">
						<%=pageTitle%>
					</span>
						<span><input maxlength="40" size="40" name="daterange" id="daterange" value="<%= dateRangeVal %>" /></span>

							<span><input maxlength="40" size="40" name="zone" id="zone" value="<%= zoneVal %>" /></span>
						<span><input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink('daterange','zone','plan.do')" onmousedown="this.src='./images/icons/view_ON.gif'" /></span>
						<span><input id="view_button" type="image" alt="Dispatch" src="./images/icons/dispatch.gif" onclick="javascript:doCompositeLink('daterange','zone','autoDispatch.do')" onmousedown="this.src='./images/icons/dispatch_ON.gif'" /> <a href="#"  onclick="javascript:doCompositeLink('daterange','zone','autoDispatch.do')" class="dispatch_link">Dispatch</a></span>
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
              <ec:column  cell="tooltip" alias="zoneCode" property="zoneNameEx" title="Zone"/>
              <ec:column alias="region" property="regionCode" title="Region"/>
              <ec:column property="supervisorEx"   title="Supervisor" cell="tooltip"  />
              <ec:column cell="date" format="hh:mm aaa" alias="startTime" property="startTime" title="Start Time"/>
              <ec:column cell="date" format="hh:mm aaa" alias="firstDeliveryTime" property="firstDeliveryTime" title="First Dlv"/>
              <ec:column  filterable="false" property="drivers"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Driver" alias="001"/>
              <ec:column  filterable="false" property="helpers"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Helper" alias="002"/>
              <ec:column  filterable="false" property="runners"  cell="com.freshdirect.transadmin.web.ui.FDPlanResourceCell" title="Runner" alias="003"/>
              <ec:column property="sequence"  title="Rank"/>
            <%--<ec:column property="zonetypeResources" headerCell="com.freshdirect.transadmin.web.ui.FDCompositeResourceHeaderCell" cell="resource" title="Driver" alias="001"/>--%>
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
    </script>   
  </tmpl:put>
</tmpl:insert>
