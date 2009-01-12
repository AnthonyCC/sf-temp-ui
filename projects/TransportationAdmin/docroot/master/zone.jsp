<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<%    
  pageContext.setAttribute("HAS_ADDBUTTON", "false");
  pageContext.setAttribute("HAS_DELETEBUTTON", "false");
%>



<% 
	String pageTitle = "Active";
	if("Active".equalsIgnoreCase(request.getParameter("zoneType"))) 
		{ 
			pageTitle = "Active";
		}else{
			pageTitle = "All"; 
		}
%>
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Operations : Zones : <%=pageTitle%></tmpl:put>

<tmpl:put name='hasSubs' direct='true'>subs</tmpl:put>

  <tmpl:put name='content' direct='true'>
  
	<div class="MNM003 subsub or_3c3">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM003 <% if("Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="zone.do?zoneType=Active" class="<% if("Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>MNM003<% } %>">Active</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM003 <% if("Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>activeR<% } %>">&nbsp;</div>

			<div class="sub_tableft sub_tabL_MNM003 <% if(!"Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if(!"Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="zone.do" class="<% if(!"Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>MNM003<% } %>">All</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM003 <% if(!"Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>activeR<% } %>">&nbsp;</div>		
		
		</div>
	</div>


	<div class="contentroot">

		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem">
					<span class="scrTitle">
						<%=pageTitle%>
				</div>
			</div>
		</div>

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
			<form id="zoneListForm" action="" method="post">  
	<% if("Active".equalsIgnoreCase(request.getParameter("zoneType"))) 
		{ %>
						<ec:table items="zones"   action="${pageContext.request.contextPath}/zone.do?zoneType=Active"
							imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""
							width="98%"  view="fd" form="zoneListForm" autoIncludeParameters="false" rowsDisplayed="25">
							<ec:exportPdf fileName="transportationzones.pdf" tooltip="Export PDF" 
									  headerTitle="Transportation Zones" />
							  <ec:exportXls fileName="transportationzones.xls" tooltip="Export PDF" />
							  <ec:exportCsv fileName="transportationzones.csv" tooltip="Export CSV" delimiter="|"/>               
							<ec:row interceptor="obsoletemarker">
							  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="selectcol"
									property="zoneCode" />  
							  <ec:column alias="trnZoneCode" property="zoneCode" title="Code"/>
							  <ec:column property="name" title="Zone Name"/>
							  <ec:column alias="trnZoneType" property="trnZoneType.name" title="Zone Type"/>
							  <ec:column alias="area" property="area.name" title="Area"/>
							  <ec:column alias="region" property="region.name" title="Region"/>
							  <ec:column alias="unattended" property="unattended" title="Unattended"/>
							</ec:row>
						  </ec:table>
		<% }else{ %>
						<ec:table items="zones"   action="${pageContext.request.contextPath}/zone.do"
							imagePath="${pageContext.request.contextPath}/images/table/*.gif" title=""
							width="98%"  view="fd" form="zoneListForm" autoIncludeParameters="false" rowsDisplayed="25">
							<ec:exportPdf fileName="transportationzones.pdf" tooltip="Export PDF" 
									  headerTitle="Transportation Zones" />
							  <ec:exportXls fileName="transportationzones.xls" tooltip="Export PDF" />
							  <ec:exportCsv fileName="transportationzones.csv" tooltip="Export CSV" delimiter="|"/>               
							<ec:row interceptor="obsoletemarker">
							  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="selectcol"
									property="zoneCode" />  
							  <ec:column alias="trnZoneCode" property="zoneCode" title="Code"/>
							  <ec:column property="name" title="Zone Name"/>
							  <ec:column alias="trnZoneType" property="trnZoneType.name" title="Zone Type"/>
							  <ec:column alias="area" property="area.name" title="Area"/>
							  <ec:column alias="region" property="region.name" title="Region"/>
							  <ec:column alias="unattended" property="unattended" title="Unattended"/>
							</ec:row>
						  </ec:table>
		<% } %>
					 </form>
				</div>
			</div>
		</div>
	</div>
	<% if("Active".equalsIgnoreCase(request.getParameter("zoneType"))) { %>
		<script>
			addRowHandlers('ec_table', 'rowMouseOver', 'editzone.do','id',0, 0);
		</script>
	<% } %>
  </tmpl:put>
</tmpl:insert>
