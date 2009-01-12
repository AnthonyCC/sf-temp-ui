<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>


<%    
	pageContext.setAttribute("HAS_ADDBUTTON", "false");
	pageContext.setAttribute("HAS_DELETEBUTTON", "false");
%>


<tmpl:insert template='/common/sitelayout.jsp'>

<% 
	String pageTitle = "Truck";
%>

    <tmpl:put name='title' direct='true'> Operations : Truck</tmpl:put>

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
					<span><a href="./truck.do?refresh=true" class="refresh_link"><input id="refresh_button" type="image" alt="Refresh" src="./images/icons/refresh.gif" onmousedown="this.src='./images/icons/refresh_ON.gif'" /></a> <a href="./truck.do?refresh=true" class="refresh_link">Refresh</a></span>
				</div>
			</div>
		</div>

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
	
					<form id="truckListForm" action="" method="post">	
						<ec:table items="trucks"   action="${pageContext.request.contextPath}/truck.do"
							imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
							width="98%"  view="fd" form="truckListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
							
							<ec:exportPdf fileName="transportationtrucks.pdf" tooltip="Export PDF" 
											headerTitle="Transportation Trucks" />
							<ec:exportXls fileName="transportationtrucks.xls" tooltip="Export PDF" />
							<ec:exportCsv fileName="transportationtrucks.csv" tooltip="Export CSV" delimiter="|"/>
								
							<ec:row>
							  <ec:column property="truckNumber" title="Truck Number"/>
							  <ec:column property="truckType" title="Truck Type"/>
							  <ec:column property="truckLicenceNumber" title="License Plate"/>
							  <ec:column property="location" title="Location"/>
							  
							</ec:row>
						  </ec:table>
					 </form>
				</div>
			</div>
		</div>
	</div>
		 <script>
			//addRowHandlers('ec_table', 'rowMouseOver', 'edittruck.do','id',0, 0);
		</script> 	
	</tmpl:put>


</tmpl:insert>
