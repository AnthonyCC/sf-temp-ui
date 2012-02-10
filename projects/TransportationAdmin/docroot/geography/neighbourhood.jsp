<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%@ page import= 'com.freshdirect.transadmin.util.TransportationAdminProperties' %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import='java.util.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>	
		
	<tmpl:put name='title' direct='true'>Geography : Neighborhood  </tmpl:put>
	<tmpl:put name='content' direct='true'>
	 <br/> 
    <div class="contentroot">
		<c:if test="${not empty messages}">
			<div class="err_messages">
				<jsp:include page='/common/messages.jsp'/>
			</div>
		</c:if>
		
		<div class="contentroot">

		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem" id="page_neighbourhood">
						<div class="scrTitle" style="float:left;padding-top:3px">Neighbourhood</div>
				  <div style="float:left;">
					  <span>&nbsp; <select id="nbhood" width="40" name="zone">
						  <option value="">--Select Neighbourhood</option>
						  <c:forEach var="neighbourhood" items="${neighbourhoods}">
							  <c:choose>
								<c:when test="${param.neighbourhood == neighbourhood.name}" > 
								  <option selected value="<c:out value="${neighbourhood.name}"/>"><c:out value="${neighbourhood.name}"/></option>
								</c:when>
								<c:otherwise>
								  <option value="<c:out value="${neighbourhood.name}"/>"><c:out value="${neighbourhood.name}"/></option>
								</c:otherwise> 
							  </c:choose>
							</c:forEach>
					   </select> 
					   </span>
					   <span>&nbsp;
						 <input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink('nbhood','neighbourhood.do');" onmousedown="this.src='./images/icons/view_ON.gif'" />
						<input style="font-size:11px" type = "button" height="18" value="Manage Neighbourhood" onclick="javascript:showNeighbourhood();" /> </span>
					</div>
				</div>
				</div>
			</div>
		</div>
		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">

				  <form id="neighbourhoodListForm" action="" method="post">  
					<ec:table items="neighbourhoodZipInfo" filterRowsCallback="exactMatch" action="${pageContext.request.contextPath}/neighbourhood.do"
						imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
						width="98%"  view="fd" form="neighbourhoodListForm" autoIncludeParameters="true" rowsDisplayed="25"  >
						
						<ec:exportPdf fileName="neighbourhood.pdf" tooltip="Export PDF" 
								  headerTitle="Transportation Plan" />
						  <ec:exportXls fileName="neighbourhood.xls" tooltip="Export PDF" />
						  <ec:exportCsv fileName="neighbourhood.csv" tooltip="Export CSV" delimiter="|"/>
							
						<ec:row interceptor="obsoletemarker">
						  <ec:column title=" " width="5px" filterable="false" sortable="false" cell="selectcol"
								property="zipcode" alias="nZipcode"/>
						  <ec:column property="zipcode" sortable="true" title="Zip Code"/>
						  <ec:column property="county" sortable="true" title="County"/> 
						  <ec:column property="state" sortable="true" title="State"/> 
						  <ec:column property="neighborhood.name" title="Neighbourhood Name"/>
						  <ec:column property="neighborhood.description" title="Neighbourhood Description"/>
						  <ec:column property="neighborhood.active" title="Active"/>
						 </ec:row>
					  </ec:table>
				   </form>
			</div>
		</div>
		</div>
     </div>
	 <%@ include file='i_manageneighbourhood.jspf'%>
	 <script>
		var geographyRpcClient = new JSONRpcClient("geographyprovider.ax");
		var errColor = "#FF0000";
		var msgColor = "#00FF00";
		function doCompositeLink(compId1, url) {
          var param1 = document.getElementById(compId1).value;          
          location.href = url+"?"+compId1+"="+ param1;
		}		
		
		function addNeighbourhoodHandlers(tableId, rowClassName, url, paramName, columnIndex, checkCol, needKeyPress) {
	
				var previousClass = null;
			    var table = document.getElementById(tableId);
			    
			    if(table != null) {
				    var rows = table.tBodies[0].getElementsByTagName("tr");	 	       
				    for (i = 0; i < rows.length; i++) {	    	
				        var cells = rows[i].getElementsByTagName("td");
				        
				        for (j = 1; j < cells.length; j++) {
				        	
				            cells[j].onmouseover = function () {
				            	previousClass = this.parentNode.className;
				            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
				            };
				        
				            cells[j].onmouseout = function () {
				              	this.parentNode.className = previousClass;
				            };
				        
				            if(checkCol == -1 || checkCol != j ) {
								if(!(needKeyPress && (j == (cells.length-1)))) {	            
							    	cells[j].onclick = function () {			    		
							      		var cell = this.parentNode.getElementsByTagName("td")[columnIndex];							      		
							      		location.href = url+"?"+ paramName + "=" + cell.innerHTML;			      		
							    	};
							    }
					    	}	    	
				        }
				    }
				}
			}
			addNeighbourhoodHandlers('ec_table', 'rowMouseOver', 'editneighbourhood.do','id',1, 0);

			function getFilterTestValue() {
				 var filters = getFilterValue(document.getElementById("neighbourhoodListForm"),false);
				 var param1 = document.getElementById("nbhood").value;
				 filters+="&nbhood="+param1;
				 return escape(filters);
			}

	 </script>
	</tmpl:put>
</tmpl:insert>