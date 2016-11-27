<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

<% 
	String pageTitle = "Snapshot Locations"; 
pageContext.setAttribute("HAS_DELETEBUTTON", "true");
pageContext.setAttribute("HAS_ADDBUTTON", "false");
%>
    <tmpl:put name='title' direct='true'> Admin : <%=pageTitle%></tmpl:put>

	<tmpl:put name='content' direct='true'>

	
	<c:if test="${not empty messages}">
		<div class="err_messages">
			<jsp:include page='/common/messages.jsp'/>
		</div>
	</c:if> 
  <div class="contentroot">

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
				
	      <ec:table items="snapshotLocations"  action="${pageContext.request.contextPath}/capacitySnapshot.do"
	            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
	            width="100%" showPagination="false" sortable="false" 
	            tableId="snapshot" rowsDisplayed="100" view="fd" >
			            
	            <ec:row interceptor="obsoletemarker">	
	             <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="id" />               
				  <ec:column property="srubbedStreet"  title="Scrubbed Address"/>
				  <ec:column property="city" title="City" />
				  <ec:column property="state" title="State" />
				  <ec:column property="zip" title="Zip" />
				  <ec:column property="country" title="Country" />
				  <ec:column property="servicetype" title="Service Type" />
	            </ec:row>
	       </ec:table>
	  	
	   
				</div>
			</div>
		</div>
	</div>
		
	</tmpl:put>
</tmpl:insert>
