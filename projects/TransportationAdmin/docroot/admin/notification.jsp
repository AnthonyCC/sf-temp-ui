<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

<% 
	String pageTitle = "Routing Notification";
	pageContext.setAttribute("HAS_ADDBUTTON", "false");
	pageContext.setAttribute("HAS_DELETEBUTTON", "false");
%>
    <tmpl:put name='title' direct='true'> Admin : <%=pageTitle%></tmpl:put>

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
					<form id="notificationForm" action="" method="post"> 
					<ec:table items="notifications"   action="${pageContext.request.contextPath}/notification.do"
						imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
						width="98%"  view="fd" form="notificationForm" autoIncludeParameters="false" rowsDisplayed="25"  >
						
						<ec:exportPdf fileName="routingnotifications.pdf" tooltip="Export PDF" 
								  headerTitle="Routing Notifications" />
						  <ec:exportXls fileName="routingnotifications.xls" tooltip="Export PDF" />
						  <ec:exportCsv fileName="routingnotifications.csv" tooltip="Export CSV" delimiter="|"/>
							
						<ec:row interceptor="obsoletemarker">							
						  <ec:column property="notificationId" title="ID"/>
						  <ec:column property="orderNumber" title="Reservation ID"/>
						  <ec:column property="schedulerId.regionId" title="Region"/>
						  <ec:column property="schedulerId.deliveryDate" title="Delivery Date" cell="date"/>
						  <ec:column property="schedulerId.area.areaCode" title="Area"/>
						  <ec:column property="notificationType" title="Type"/>						  						  
						</ec:row>
					  </ec:table>
					</form> 
				</div> 
			</div> 
		</div>
     </div>    
  </tmpl:put>
</tmpl:insert>
