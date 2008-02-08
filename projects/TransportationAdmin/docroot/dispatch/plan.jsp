<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<%	pageContext.setAttribute("HAS_COPYBUTTON", "true");  %>
	
<tmpl:insert template='/common/site.jsp'>

    <tmpl:put name='title' direct='true'>Transportation Planning</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form id="planListForm" action="" method="post">	
				<ec:table items="planlist"  filterRowsCallback="exactMatch" action="${pageContext.request.contextPath}/plan.do"
				    imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Transportation Planning"
				    width="98%"  view="fd" form="planListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
				    
				    <ec:exportPdf fileName="transportationplan.pdf" tooltip="Export PDF" 
				        			headerTitle="Transportation Plan" />
		       		<ec:exportXls fileName="transportationplan.xls" tooltip="Export PDF" />
		       		<ec:exportCsv fileName="transportationplan.csv" tooltip="Export CSV" delimiter="|"/>
				        
				    <ec:row interceptor="obsoletemarker">
				    	<ec:column title=" " width="5px" 
					          filterable="false" sortable="false" cell="selectcol"
					          property="planId" />				    	
				      <ec:column cell="date" property="planDate" sortable="true" title="Date"/>
				      <ec:column property="dispatchDay" sortable="true" title="Day"/>
				      <ec:column alias="trnZonezoneNumber" property="trnZone.zoneNumber" title="Zone"/>
				      <ec:column alias="trnTimeslotslotName" property="trnTimeslot.slotName" title="Timeslot"/>
				      <ec:column alias="trnDrivername" property="trnDriver.name" title="Driver"/>
				      <ec:column alias="trnPrimaryHelpername" property="trnPrimaryHelper.name" title="Helper1"/>
				      <ec:column alias="trnSecondaryHelpername" property="trnSecondaryHelper.name" title="Helper2"/>
				    </ec:row>
				  </ec:table>
			 </form> 	
		 </div>
		 <script>
			addRowHandlers('ec_table', 'rowMouseOver', 'editplan.do','id',0, 0);
		</script> 	
	</tmpl:put>
</tmpl:insert>