<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<tmpl:insert template='/common/sitelayout.jsp'>
<tmpl:put name='hasSubs' direct='true'>subs</tmpl:put>

<% 
	String pageTitle = "Transportation Employees";
	if("R".equalsIgnoreCase(request.getParameter("routetype"))) 
		{ 
			pageTitle = "Active Routes"; 
		}else{
			pageTitle = "Ad Hoc Routes"; 
		}
%>

<% if("R".equalsIgnoreCase(request.getParameter("routetype"))) { %>
	<tmpl:put name='title' direct='true'> Operations : Route : Active Routes</tmpl:put><%    
		pageContext.setAttribute("HAS_ADDBUTTON", "false");
		pageContext.setAttribute("HAS_DELETEBUTTON", "false");
	%>
	
<% }else{ %>
	
	<tmpl:put name='title' direct='true'> Operations : Route : Ad Hoc Routes</tmpl:put>
<% } %>
	

  <tmpl:put name='content' direct='true'>

			<% try { %>
	<div class="MNM001 subsub or_999">

		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM001 <% if("R".equalsIgnoreCase(request.getParameter("routetype"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("R".equalsIgnoreCase(request.getParameter("routetype"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="route.do?routetype=R" class="<% if("R".equalsIgnoreCase(request.getParameter("routetype"))) { %>MNM001<% } %>">Active Routes</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("R".equalsIgnoreCase(request.getParameter("routetype"))) { %>activeR<% } %>">&nbsp;</div>		
		
			<div class="sub_tableft sub_tabL_MNM001 <% if(!"R".equalsIgnoreCase(request.getParameter("routetype"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if(!"R".equalsIgnoreCase(request.getParameter("routetype"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="route.do" class="<% if(!"R".equalsIgnoreCase(request.getParameter("routetype"))) { %>MNM001<% } %>">Ad Hoc Routes</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if(!"R".equalsIgnoreCase(request.getParameter("routetype"))) { %>activeR<% } %>">&nbsp;</div>
		</div>
	</div>
				<% } catch(Exception e) {
					e.printStackTrace();
				} %>


	<div class="contentroot">

		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem">
					<span class="scrTitle">
						<%=pageTitle%>
					</span>
						<% if("R".equalsIgnoreCase(request.getParameter("routetype"))){ %>
							<span><input maxlength="10" size="10" name="routeDate" id="routeDate" value="<c:out value="${routeDate}"/>" /></span>
							<span><a href="#" id="trigger_routeDate" style="font-size: 9px;"><img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a></span>
							<span><input type="image" src="./images/icons/view.gif" alt="View"   onclick="javascript:doLink('routeDate','route.do')" /></span>
						<% } %>
				</div>
			</div>
		</div>

		<div class="cont_topright">
			<div class="cont_row"><br></div>
			<div class="cont_row">
				<div class="cont_Ritem">
					<div class="eXor"><div class="eXor<% if(!"R".equalsIgnoreCase(request.getParameter("routetype"))) { %>_AdHoc<% }else{ %>_Active<% } %>">
						<% if(!"R".equalsIgnoreCase(request.getParameter("routetype"))) { %>
							<form id="routeListForm" action="" method="post"> 
								<ec:table items="routes" action="${pageContext.request.contextPath}/route.do"
								imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
								  view="fd" form="routeListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
								
								<ec:exportPdf fileName="transportationroutes.pdf" tooltip="Export PDF" 
										  headerTitle="Transportation Routes" />
								  <ec:exportXls fileName="transportationroutes.xls" tooltip="Export PDF" />
								  <ec:exportCsv fileName="transportationroutes.csv" tooltip="Export CSV" delimiter="|"/>
								<ec:row interceptor="obsoletemarker">
										<ec:column title=" " width="5px" 
										filterable="false" sortable="false" cell="selectcol"
										property="routeId" />
								  <ec:column property="routeNumber" title="Route number1"/>
								  <ec:column property="description" title="description"/>
								  <ec:column property="routeAmPm" title="AM/PM"/>
								</ec:row>
							  </ec:table>
							</form> 
							<script language="javascript">
									

									function doDelete(tableId, url) {                       
										sendRequest(tableId, url, "Do you want to delete the selected records?");                       
									}

									function doConfirm(tableId, url) {
										sendRequest(tableId, url, "Do you want to confirm/deconfirm the selected records?");                      
									}

									function sendRequest(tableId, url, message) {
										var table = document.getElementById(tableId);
										var checkboxList = table.getElementsByTagName("input");
										var dateField = document.getElementById("routeDate").value;
										var paramValues = null;
										for (i = 0; i < checkboxList.length; i++) {
											if (checkboxList[i].type=="checkbox" && checkboxList[i].checked) {
												if (paramValues != null) {
													paramValues = paramValues+","+checkboxList[i].name+"$"+dateField;
												} else {
													paramValues = checkboxList[i].name+"$"+dateField;
												}
											}
										}
										if (paramValues != null) {
											var hasConfirmed = confirm (message);
											if (hasConfirmed) {
												location.href = url+"?id="+ paramValues;
											} 
										} else {
											alert('Please Select a Row!');
										}
									}
								</script>
							 <script>
							  addRowHandlers('ec_table', 'rowMouseOver', 'editAdHocRoute.do','id',0, 0);
							</script>  
						<% }else{ %>
							<form id="routeListForm" action="" method="post"> 
								<ec:table items="routes"   action="${pageContext.request.contextPath}/route.do?routetype=R"
									imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
									 view="fd" form="routeListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
									
									<ec:exportPdf fileName="transportationroutes.pdf" tooltip="Export PDF" 					  headerTitle="Transportation Routes" />
									  <ec:exportXls fileName="transportationroutes.xls" tooltip="Export PDF" />
									  <ec:exportCsv fileName="transportationroutes.csv" tooltip="Export CSV" delimiter="|"/>
									<ec:row interceptor="obsoletemarker">
									  <ec:column property="routeNumber" title="Route number1"/>
									  <ec:column property="zoneNumber" title="Zone Number"/>
									  <ec:column property="routeTime" title="AM/PM"/>
									  <ec:column property="numberOfStops" title="Stops"/>
                                      <ec:column property="firstDlvTime" title="FirstDlvTime"/>                                      
									</ec:row>
								  </ec:table>
							</form>  
							<script language="javascript">
							 Calendar.setup(
										{
											showsTime : false,
											electric : false,
											inputField : "routeDate",
											ifFormat : "%m/%d/%Y",
											singleClick: true,
											button : "trigger_routeDate" 
										}
									);
							</script>		
						<% } %>

					</div></div>
				</div>
			</div>
		</div>

	</div>  
  </tmpl:put>
</tmpl:insert>
