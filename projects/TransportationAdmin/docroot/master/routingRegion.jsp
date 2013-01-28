<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

<% 
	String pageTitle = "Routing Region";
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
					  <form id="routingRegionForm" action="" method="post">  
						<ec:table items="routingRegions"   action="${pageContext.request.contextPath}/routingRegion.do"
							imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
							width="98%"  view="fd" form="routingRegionForm" autoIncludeParameters="false" rowsDisplayed="25"  >
							
							<ec:exportPdf fileName="transportationRoutingRegions.pdf" tooltip="Export PDF" 
									  headerTitle="Transportation Routing Regions" />
							  <ec:exportXls fileName="transportationRoutingRegions.xls" tooltip="Export PDF" />
							  <ec:exportCsv fileName="transportationRoutingRegions.csv" tooltip="Export CSV" delimiter="|"/>
								
							<ec:row interceptor="obsoletemarker">
							  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="selectcol"
									property="code" />
							 <ec:column alias="regioncode" property="code" title="Region Code"/>                    
							  <ec:column property="name" title="Region Name"/>
							  <ec:column property="description" title="Description"/>
							  <ec:column property="isDepot" title="Depot" width="5px"/>
							</ec:row>
						  </ec:table>
					   </form> 
				</div>
			</div>
		</div>
	</div>
     <script>
      addRowHandlers('ec_table', 'rowMouseOver', 'editRoutingRegion.do','id',0, 0);
      
      function getFilterTestValue() {
          var filters = getFilterValue(document.getElementById("routingRegionForm"), false);          
          return escape(filters);
     }
    </script>   
  </tmpl:put>
</tmpl:insert>
