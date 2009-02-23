<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<%    
  pageContext.setAttribute("HAS_ADDBUTTON", "false");
  pageContext.setAttribute("HAS_DELETEBUTTON", "false");
  String type = request.getParameter("type");
  String pageTitle = "Zone Boundary";
	if(!"zone".equalsIgnoreCase(type)) { 
		pageTitle = "Geo Restriction Boundary";
	}
%>
<tmpl:insert template='/common/sitelayout.jsp'>

<tmpl:put name='yui-lib'>
	<%@ include file='/common/i_yui.jspf'%>
</tmpl:put>	

<tmpl:put name='gmap-lib'>
	<%@ include file='/common/i_gmap.jspf'%>
</tmpl:put>	

<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>	

<tmpl:put name='title' direct='true'>Operations : GMaps : <%=pageTitle%></tmpl:put>

<tmpl:put name='hasSubs' direct='true'>subs</tmpl:put>

  <tmpl:put name='content' direct='true'>
  <c:if test="${not empty messages}">
		<div class="err_messages">
			<jsp:include page='/common/messages.jsp'/>
		</div>
	</c:if> 
  <script>
      function handleType(type) {
      		location.href = "gmap.do?type="+ type;
      }
      
    </script>  
  <div align="center"><br/>
  		<b>Please Select a Boundary Type:&nbsp;&nbsp;&nbsp;</b>
		<input onclick="javascript:handleType('zone');" type="radio" id="gtype" value="zone" <%= "zone".equalsIgnoreCase(type) ? "checked" : "" %> /> <b>Zone</b>&nbsp;&nbsp;&nbsp;
		<input onclick="javascript:handleType('georestriction');" type="radio" id="gtype" value="georestriction" <%= "georestriction".equalsIgnoreCase(type) ? "checked" : "" %> /> <b>Geo Restriction</b><br/>
	</div>
	
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
					<form id="defaultForm" action="" method="post">
					<% if("georestriction".equalsIgnoreCase(type)) 	{ %>
							<ec:table items="boundaries"   action="${pageContext.request.contextPath}/gmap.do?type=georestriction"
								imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""
								width="98%"  view="fd" form="defaultForm" autoIncludeParameters="false" rowsDisplayed="25">
								<ec:exportPdf fileName="transportationgeorestriction.pdf" tooltip="Export PDF" 
										  headerTitle="Transportation Geo Restriction" />
								  <ec:exportXls fileName="transportationgeorestriction.xls" tooltip="Export PDF" />
								  <ec:exportCsv fileName="transportationgeorestriction.csv" tooltip="Export CSV" delimiter="|"/>               
								  <ec:row interceptor="obsoletemarker">
								    
								  <ec:column alias="trnBCode" property="code" title="Code"/>
								  <ec:column property="name" title="Name"/>
								  
								</ec:row>
							  </ec:table>
							  <script>
								function loadData(jsonrpcClient, showBoundaryCallback, boundaryKey) {
						     		return jsonrpcClient.AsyncGeographyProvider.getGeoRestrictionBoundary(showBoundaryCallback
						    	  																, boundaryKey); 
	     						}
							</script>	
						<% }else{ %>
							<ec:table items="boundaries"   action="${pageContext.request.contextPath}/gmap.do?type=zone"
								imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""
								width="98%"  view="fd" form="defaultForm" autoIncludeParameters="false" rowsDisplayed="25">
								<ec:exportPdf fileName="transportationzones.pdf" tooltip="Export PDF" 
										  headerTitle="Transportation Zones" />
								  <ec:exportXls fileName="transportationzones.xls" tooltip="Export PDF" />
								  <ec:exportCsv fileName="transportationzones.csv" tooltip="Export CSV" delimiter="|"/>               
								  <ec:row interceptor="obsoletemarker">
								    
								  <ec:column alias="trnZoneCode" property="zoneCode" title="Code"/>
								  <ec:column property="name" title="Name"/>
								  
								</ec:row>
							  </ec:table>
							  <script>
								function loadData(jsonrpcClient, showBoundaryCallback, boundaryKey) {
						     		return jsonrpcClient.AsyncGeographyProvider.getZoneBoundary(showBoundaryCallback
						    	  																, boundaryKey); 
	     						}
							</script>	
						<% } %>
				</form>	 
				<%@ include file='/common/i_gmapviewer.jspf'%>
		
				</div>
			</div>
		</div>
	</div>
		<script>
		function addAsyncHandler(tableId, rowClassName, columnIndex) {
	
				var previousClass = null;
			    var table = document.getElementById(tableId);
			    
			    if(table != null) {
				    var rows = table.tBodies[0].getElementsByTagName("tr");	 	       
				    for (i = 0; i < rows.length; i++) {	    	
				        var cells = rows[i].getElementsByTagName("td");
				        
				        for (j = 0; j < cells.length; j++) {
				        	
				            cells[j].onmouseover = function () {
				            	previousClass = this.parentNode.className;
				            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
				            };
				        
				            cells[j].onmouseout = function () {
				              	this.parentNode.className = previousClass;
				            };
				        
				           cells[j].onclick = function () {			    		
							      		var cell = this.parentNode.getElementsByTagName("td")[columnIndex];							      		
							      		showBoundary(cell.innerHTML);
							      			      		
						   };
					    		    	
				        }
				    }
				}
			}
			addAsyncHandler('ec_table', 'rowMouseOver', 0);		
		</script>
  </tmpl:put>
</tmpl:insert>
