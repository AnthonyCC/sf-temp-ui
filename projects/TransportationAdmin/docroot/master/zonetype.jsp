<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

<% 
	String pageTitle = "Zone Type";
%>
    <tmpl:put name='title' direct='true'> Geography : <%=pageTitle%></tmpl:put>

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
				</div>
			</div>
		</div>

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
			<form id="zoneTypeListForm" action="" method="post">	
				<ec:table items="zonetypes"   action="${pageContext.request.contextPath}/zonetype.do"
				    imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
				    width="98%"  view="fd" form="zoneTypeListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
				    
				    <ec:exportPdf fileName="transportationzonetypes.pdf" tooltip="Export PDF" 
				        			headerTitle="Transportation Zone Type" />
		       		<ec:exportXls fileName="transportationzonetypes.xls" tooltip="Export PDF" />
		       		<ec:exportCsv fileName="transportationzonetypes.csv" tooltip="Export CSV" delimiter="|"/>
				        
				    <ec:row interceptor="obsoletemarker">
				    	<ec:column title=" " width="5px" 
					          filterable="false" sortable="false" cell="selectcol"
					          property="zoneTypeId" />				    	
				      <ec:column property="nameEx" title="Zone Type Name" cell="tooltip" />
				      <ec:column property="description" title="Description"/>	
                                            
                      <ec:column filterable="false" property="zonetypeResources" headerCell="com.freshdirect.transadmin.web.ui.FDCompositeResourceHeaderCell" cell="resource" title="Driver" alias="001"/>
                      <ec:column filterable="false" property="zonetypeResources" headerCell="com.freshdirect.transadmin.web.ui.FDCompositeResourceHeaderCell" cell="resource" title="Helper" alias="002"/>
                      <ec:column filterable="false" property="zonetypeResources" headerCell="com.freshdirect.transadmin.web.ui.FDCompositeResourceHeaderCell" cell="resource" title="Runner" alias="003"/>                      
                      
                                         
				    </ec:row>
				  </ec:table>
			 </form> 
				</div>
			</div>
		</div>
	</div>
		 <script>
			addRowHandlers('ec_table', 'rowMouseOver', 'editzonetype.do','id',0, 0);
		</script> 	
	</tmpl:put>
</tmpl:insert>
