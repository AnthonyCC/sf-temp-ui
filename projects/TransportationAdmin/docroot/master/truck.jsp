<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/site.jsp'>

    <tmpl:put name='title' direct='true'>Transportation Trucks</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form id="truckListForm" action="" method="post">	
				<ec:table items="trucks"   action="${pageContext.request.contextPath}/truck.do"
				    imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Transportation Trucks"
				    width="98%"  view="fd" form="truckListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
				    
				    <ec:exportPdf fileName="transportationtrucks.pdf" tooltip="Export PDF" 
				        			headerTitle="Transportation Trucks" />
		       		<ec:exportXls fileName="transportationtrucks.xls" tooltip="Export PDF" />
		       		<ec:exportCsv fileName="transportationtrucks.csv" tooltip="Export CSV" delimiter="|"/>
				        
				    <ec:row>
				    	<ec:column title=" " width="5px" 
					          filterable="false" sortable="false" cell="selectcol"
					          property="truckId" />				    	
				      <ec:column property="truckNumber" title="Truck Number"/>
				      <ec:column property="truckType" title="Truck Type"/>
				      <ec:column property="licensePlate" title="License Plate"/>
				    </ec:row>
				  </ec:table>
			 </form> 	
		 </div>
		 <script>
			addRowHandlers('ec_table', 'rowMouseOver', 'edittruck.do','id',0, 0);
		</script> 	
	</tmpl:put>
</tmpl:insert>
