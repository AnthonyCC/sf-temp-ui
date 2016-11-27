<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%@ page import= 'com.freshdirect.transadmin.util.TransportationAdminProperties' %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import='java.util.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

<tmpl:put name='title' direct='true'>Geography : Sector  </tmpl:put>
	<tmpl:put name='content' direct='true'>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>	
	</tmpl:put>
	 <br/> 
    <div class="contentroot">
		<c:if test="${not empty messages}">
			<div class="screenmessages">
            	<jsp:include page='/common/messages.jsp'/>            	
            </div>
		</c:if>
		
		<div class="contentroot">

		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem" id="page_sector">
						<div class="scrTitle" style="float:left;padding-top:3px">Sector</div>
				  <div style="float:left;">
					  <span>&nbsp; <select id="sector" width="40" name="zone">
						  <option value="">--Please select Sector</option>
						  <c:forEach var="sector" items="${sectors}">
							  <c:choose>
								<c:when test="${param.sector == sector.name}" > 
								  <option selected value="<c:out value="${sector.name}"/>"><c:out value="${sector.name}"/></option>
								</c:when>
								<c:otherwise>
								  <option value="<c:out value="${sector.name}"/>"><c:out value="${sector.name}"/></option>
								</c:otherwise> 
							  </c:choose>
							</c:forEach>
					   </select> 
					   </span>
					   <span>&nbsp;
						 <input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink('sector','sector.do');" onmousedown="this.src='./images/icons/view_ON.gif'" />
						<input style="font-size:11px" type = "button" height="18" value="Manage Sector" onclick="javascript:showSector();" /> </span>
					</div>
				</div>
				</div>
			</div>
		</div>
		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">

				  <form id="sectorListForm" action="" method="post">  
					<ec:table items="sectorZipInfo" filterRowsCallback="exactMatch" action="${pageContext.request.contextPath}/sector.do"
						imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
						width="98%"  view="fd" form="sectorListForm" autoIncludeParameters="true" rowsDisplayed="25"  >
						
						<ec:exportPdf fileName="sector.pdf" tooltip="Export PDF" 
								  headerTitle="Transportation Plan" />
						  <ec:exportXls fileName="sector.xls" tooltip="Export PDF" />
						  <ec:exportCsv fileName="sector.csv" tooltip="Export CSV" delimiter="|"/>
							
						<ec:row interceptor="obsoletemarker">
						  <ec:column title=" " width="5px" filterable="false" sortable="false" cell="selectcol"
								property="zipcode" alias="nZipcode"/>
						  <ec:column property="zipcode" sortable="true" title="Zip Code"/>
						  <ec:column property="county" sortable="true" title="County"/> 
						  <ec:column property="state" sortable="true" title="State"/> 
						  <ec:column property="sector.name" sortable="true" title="Sector Name"/>
						  <ec:column property="sector.description" sortable="true" title="Sector Description"/>
						  <ec:column property="sector.active" sortable="true" title="Active"/>
						 </ec:row>
					  </ec:table>
				   </form>
			</div>
		</div>
		</div>
     </div>
	 <%@ include file='i_managesector.jspf'%>
	 <script>
		var geographyRpcClient = new JSONRpcClient("geographyprovider.ax");

		var errColor = "#FF0000";
		var msgColor = "#00FF00";
		function doCompositeLink(compId1, url) {
          var param1 = document.getElementById(compId1).value;          
          location.href = url+"?"+compId1+"="+ param1;
		}		
		
		function addSectorHandlers(tableId, rowClassName, url, paramName, columnIndex, checkCol, needKeyPress) {
	
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
			addSectorHandlers('ec_table', 'rowMouseOver', 'editsector.do','id',1, 0);

			function getFilterTestValue() {
				 var filters = getFilterValue(document.getElementById("sectorListForm"),false);
				 var param1 = document.getElementById("sector").value;
				 filters+="&sector="+param1;
				 return escape(filters);
			}

	 </script>
	</tmpl:put>
</tmpl:insert>