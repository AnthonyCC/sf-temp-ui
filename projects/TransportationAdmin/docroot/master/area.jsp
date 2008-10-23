<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/site.jsp'>

    <tmpl:put name='title' direct='true'>Transportation Areas</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form id="areaForm" action="" method="post">	
				<ec:table items="areas"   action="${pageContext.request.contextPath}/area.do"
				    imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Transportation Areas"
				    width="98%"  view="fd" form="areaForm" autoIncludeParameters="false" rowsDisplayed="25"  >
				    
				    <ec:exportPdf fileName="transportationareas.pdf" tooltip="Export PDF" 
				        			headerTitle="Transportation Area" />
		       		<ec:exportXls fileName="transportationareas.xls" tooltip="Export PDF" />
		       		<ec:exportCsv fileName="transportationareas.csv" tooltip="Export CSV" delimiter="|"/>
				        
				    <ec:row interceptor="obsoletemarker">
				    	<ec:column title=" " width="5px" 
					          filterable="false" sortable="false" cell="selectcol"
					          property="code" />
					   <ec:column alias="areacode" property="code" title="Area Code"/>       				    	
				      <ec:column property="name" title="Area Name"/>
				      <ec:column property="description" title="Description"/>
				      <ec:column property="prefix" title="Prefix"/>	
				      <ec:column property="deliveryModel" title="Delivery Model"/>			      
				    </ec:row>
				  </ec:table>
			 </form> 	
		 </div>
		 <script>
			addRowHandlers('ec_table', 'rowMouseOver', 'editarea.do','id',0, 0);
		</script> 	
	</tmpl:put>
</tmpl:insert>
