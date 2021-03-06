<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

<% 
	String pageTitle = "Dispatch Group Time";
%>

    <tmpl:put name='title' direct='true'> Routing : <%=pageTitle%></tmpl:put>

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
	
					  <form id="dispatchGroupForm" action="" method="post">  
						<ec:table items="dispatchGroups"   action="${pageContext.request.contextPath}/dispatchgroup.do"
							imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
							width="98%"  view="fd" form="dispatchGroupForm" autoIncludeParameters="false" rowsDisplayed="25"  >
							
							<ec:exportPdf fileName="transportationcutoffs.pdf" tooltip="Export PDF" 
									  headerTitle="Transportation CutOff" />
							  <ec:exportXls fileName="transportationcutoffs.xls" tooltip="Export PDF" />
							  <ec:exportCsv fileName="transportationcutoffs.csv" tooltip="Export CSV" delimiter="|"/>
								
							<ec:row interceptor="obsoletemarker">
							  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="selectcol"
									property="dispatchGroupId" />             
							  <ec:column property="name" title="Name"/>  
							  <ec:column property="groupTime" title="Dispatch Group Time"/>            
							</ec:row>
						  </ec:table>
					 </form>
				</div>
			</div>
		</div>
	</div>
     <script>
      addRowHandlers('ec_table', 'rowMouseOver', 'editdispatchgroup.do','id',0, 0);
    </script>   
  </tmpl:put>
</tmpl:insert>
