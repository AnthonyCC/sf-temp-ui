<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/site.jsp'>

    <tmpl:put name='title' direct='true'>Transportation Routes</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form id="routeListForm" action="" method="post">	
				<ec:table items="routes"   action="${pageContext.request.contextPath}/route.do"
				    imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Transportation Routes"
				    width="98%"  view="fd" form="routeListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
				    
				    <ec:exportPdf fileName="transportationroutes.pdf" tooltip="Export PDF" 
				        			headerTitle="Transportation Routes" />
		       		<ec:exportXls fileName="transportationroutes.xls" tooltip="Export PDF" />
		       		<ec:exportCsv fileName="transportationroutes.csv" tooltip="Export CSV" delimiter="|"/>
				        
				    <ec:row interceptor="obsoletemarker">
				    	<ec:column title=" " width="5px" 
					          filterable="false" sortable="false" cell="selectcol"
					          property="routeId" />				    	
				      <ec:column property="routeNumber" title="Route number"/>
				      <ec:column alias="trnSupervisorname" property="trnSupervisor.name" title="Supervisor"/>
				      <ec:column alias="trnZonezoneNumber" property="trnZone.zoneNumber" title="Zone Number"/>
				      <ec:column property="routeAmPm" title="AM/PM"/>
				    </ec:row>
				  </ec:table>
			 </form> 	
		 </div>
		 <script>
			addRowHandlers('ec_table', 'rowMouseOver', 'editroute.do','id',0, 0);
		</script> 	
	</tmpl:put>
</tmpl:insert>
