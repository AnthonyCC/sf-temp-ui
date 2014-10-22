<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<%    
  pageContext.setAttribute("HAS_ADDBUTTON", "false");
  pageContext.setAttribute("HAS_DELETEBUTTON", "false");
%>



<% 
	String pageTitle = "Active";
	if("Active".equalsIgnoreCase(request.getParameter("zoneType"))) 
		{ 
			pageTitle = "Active";
		}else{
			pageTitle = "All"; 
		}
%>
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Operations : Zones : <%=pageTitle%></tmpl:put>

<tmpl:put name='hasSubs' direct='true'>subs</tmpl:put>

  <tmpl:put name='content' direct='true'>
  
	<div class="MNM004 subsub or_3c3">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM004 <% if("Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="zone.do?zoneType=Active" class="<% if("Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>MNM004<% } %>">Active</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM004 <% if("Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>activeR<% } %>">&nbsp;</div>

			<div class="sub_tableft sub_tabL_MNM004 <% if(!"Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if(!"Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="zone.do" class="<% if(!"Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>MNM004<% } %>">All</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM004 <% if(!"Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>activeR<% } %>">&nbsp;</div>		
		
		</div>
	</div>


	<div class="contentroot">

		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem">
					<span class="scrTitle">
						<%=pageTitle%>
				</div>
			</div>
		</div>

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
			<form id="zoneListForm" action="" method="post">  
	<% if("Active".equalsIgnoreCase(request.getParameter("zoneType"))) 
		{ %>
						<ec:table items="zones"   action="${pageContext.request.contextPath}/zone.do?zoneType=Active"
							imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""
							width="98%"  view="fd" form="zoneListForm" autoIncludeParameters="false" rowsDisplayed="25">
							<ec:exportPdf fileName="transportationzones.pdf" tooltip="Export PDF" 
									  headerTitle="Transportation Zones" />
							  <ec:exportXls fileName="transportationzones.xls" tooltip="Export PDF" />
							  <ec:exportCsv fileName="transportationzones.csv" tooltip="Export CSV" delimiter="|"/>               
							<ec:row interceptor="obsoletemarker">
							    
							  <ec:column alias="trnZoneCode" property="zoneCode" title="Code"/>
							  <ec:column property="name" title="Zone Name"/>
							  <ec:column alias="trnZoneType" property="trnZoneType.name" title="Zone Type"/>
							  <ec:column alias="area" property="area.name" title="Area"/>
							  <ec:column alias="region" property="region.name" title="Zone Region"/>
							  <ec:column alias="serviceTimeType" property="defaultServiceTimeType.name" title="ServiceTime Type"/>
							  <ec:column alias="unattended" property="unattended" title="Unattended"/>
                              <ec:column  filterable="true" property="amZoneSupervisors"  cell="com.freshdirect.transadmin.web.ui.FDZoneSupervisorCell" title="AM Supervisors" alias="amSupervisors"/>
            				  <ec:column  filterable="true" property="pmZoneSupervisors"  cell="com.freshdirect.transadmin.web.ui.FDZoneSupervisorCell" title="PM Supervisors" alias="pmSupervisors"/>
                              <ec:column alias="priority" property="priority" title="Priority"/>
                              <ec:column alias="enableCOS" property="cosEnabled" title="COS Enabled"/>
                              <ec:column alias="preTrip" property="preTripTime" title="Pre-Trip (Mins)"/>
							  <ec:column alias="postTrip" property="postTripTime" title="Post-Trip (Mins)"/>
							  <ec:column alias="loadingPriority" property="loadingPriority" title="Loading Priority"/>
							  <ec:column property="ecoFriendly" title="Eco Friendly Radius"/>
							  <ec:column property="steeringRadius" title="Steering Radius"/>
							  <ec:column property="svcAdjReductionFactor" title="Service Adjustment Reduction Factor"/>
							  <ec:column property="ETAInterval" title="ETA Interval"/>
							  <ec:column property="manifestETAEnabled" title="Manifest ETAEnabled"/>
							  <ec:column property="emailETAEnabled" title="Email ETAEnabled"/>
							  <ec:column property="smsETAEnabled" title="SMS ETAEnabled"/>
							  <ec:column property="nextStopSmsEnabled" title="Next Stop SMS"/>
							  <ec:column property="unattendedSmsEnabled" title="Unatteded/Doorman delivery SMS"/>
							  <ec:column property="dlvAttemptedSmsEnabled" title="Delivery Attempted SMS"/>
							  <ec:column property="dlvWindowReminder" title="Delivery Window Reminder"/>
							</ec:row>
						  </ec:table>
		<% }else{ %>
						<ec:table items="zones"   action="${pageContext.request.contextPath}/zone.do"
							imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""
							width="98%"  view="fd" form="zoneListForm" autoIncludeParameters="false" rowsDisplayed="25">
							<ec:exportPdf fileName="transportationzones.pdf" tooltip="Export PDF" 
									  headerTitle="Transportation Zones" />
							  <ec:exportXls fileName="transportationzones.xls" tooltip="Export PDF" />
							  <ec:exportCsv fileName="transportationzones.csv" tooltip="Export CSV" delimiter="|"/>               
							<ec:row interceptor="obsoletemarker">
							  <ec:column alias="trnZoneCode" property="zoneCode" title="Code"/>
							  <ec:column property="name" title="Zone Name"/>
							  <ec:column alias="trnZoneType" property="trnZoneType.name" title="Zone Type"/>
							  <ec:column alias="area" property="area.name" title="Area"/>
							  <ec:column alias="region" property="region.name" title="Region"/>
                              <ec:column alias="serviceTimeType" property="defaultServiceTimeType.name" title="ServiceTime Type"/>
                              <ec:column alias="priority" property="priority" title="Priority"/>
                              <ec:column alias="enableCOS" property="cosEnabled" title="COS Enabled"/>
							  <ec:column alias="unattended" property="unattended" title="Unattended"/>
							  <ec:column alias="preTrip" property="preTripTime" title="Pre-Trip (Mins)"/>
							  <ec:column alias="postTrip" property="postTripTime" title="Post-Trip (Mins)"/>						  
							  <ec:column alias="loadingPriority" property="loadingPriority" title="Loading Priority"/>
							  <ec:column property="ecoFriendly" title="Eco Friendly Radius"/>
							  <ec:column property="steeringRadius" title="Steering Radius"/>
							  <ec:column property="svcAdjReductionFactor" title="Service Adjustment Reduction Factor"/>
							  <ec:column property="ETAInterval" title="ETA Interval"/>							  
							  <ec:column property="manifestETAEnabled" title="Manifest ETAEnabled"/>
							  <ec:column property="emailETAEnabled" title="Email ETAEnabled"/>
							  <ec:column property="smsETAEnabled" title="SMS ETAEnabled"/>
							  <ec:column property="nextStopSmsEnabled" title="Next Stop SMS Enabled"/>
							  <ec:column property="unattendedSmsEnabled" title="Unatteded/Doorman delivery SMS Enabled"/>
							  <ec:column property="dlvAttemptedSmsEnabled" title="Delivery Attempted SMS Enabled"/>
							  <ec:column property="dlvWindowReminder" title="Delivery Window Reminder"/>
							</ec:row>
						  </ec:table>
		<% } %>
					 </form>
				</div>
			</div>
		</div>
	</div>
	<% if("Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>
		<script>
			
			addZoneHandlers('ec_table', 'rowMouseOver', 'editzone.do','id',0, 0);
			
			function getFilterTestValue() {
	             var filters = getFilterValue(document.getElementById("zoneListForm"), false);	                                                                       
	             return escape(filters);
	       }
		</script>
	<% } %>
  </tmpl:put>
</tmpl:insert>
