<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

<% 
	String pageTitle = "Area";
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
					  <form id="areaForm" action="" method="post">  
						<ec:table items="areas"   action="${pageContext.request.contextPath}/area.do"
							imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
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
							  <ec:column property="deliveryRate" title="Orders/Hour" width="18px"/>				  							  
							  <ec:column property="needsLoadBalance" title="Load Balance" width="18px"/> 
							  <ec:column property="loadBalanceFactor" title="Balance Factor" width="18px"/>
							  <ec:column format="com.freshdirect.routing.constants.EnumBalanceBy" width="18px" cell="enumcol" property="balanceBy" title="Balance By"/>                        
							  <ec:column property="active" title="Route w/ UPS" width="18px"/>
							  <ec:column property="isDepot" title="Depot" width="5px"/>
							  <ec:column property="deliveryModel" title="Delivery Model" width="18px"/> 
							   <ec:column property="prefix" title="Prefix" width="10px"/>
							</ec:row>
						  </ec:table>
					   </form> 
				</div>
			</div>
		</div>
	</div>
     <script>
      addRowHandlers('ec_table', 'rowMouseOver', 'editarea.do','id',0, 0);
    </script>   
  </tmpl:put>
</tmpl:insert>
