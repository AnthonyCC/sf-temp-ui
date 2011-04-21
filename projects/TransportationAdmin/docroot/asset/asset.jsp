<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>

<tmpl:insert template='/common/sitelayout.jsp'>

<% 
	String pageTitle = "Asset";
	pageContext.setAttribute("HAS_DELETEBUTTON", "false");
%>
    <tmpl:put name='title' direct='true'> Admin : <%=pageTitle%></tmpl:put>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>	
	<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>
	
  <tmpl:put name='content' direct='true'>
		<div class="subs MNM004">
			<div class="subs_left">	
				<div class="sub_tableft sub_tabL_MNM004 <% if(request.getParameter("tAssetType")== null) { %>activeL<% } %>">&nbsp;</div>
				<div class="subtab <%if(request.getParameter("tAssetType")== null) { %>activeT<% } %>">
					<div class="minwidth"><!-- --></div>
					<a href="asset.do?pAssetType=GPS" class="<% if(request.getParameter("tAssetType")== null) { %>MNM004<% } %>">Asset</a>
				</div>
				<div class="sub_tabright sub_tabR_MNM004 <% if(request.getParameter("tAssetType")== null) { %>activeR<% } %>">&nbsp;</div>
		
				<div class="sub_tableft sub_tabL_MNM004 <% if(request.getParameter("tAssetType")!= null) { %>activeL<% } %>">&nbsp;</div>
				<div class="subtab <%if(request.getParameter("tAssetType")!= null) { %>activeT<% } %>">
					<div class="minwidth"><!-- --></div>
					<a href="assettemplate.do?tAssetType=GPS" class="<% if(request.getParameter("tAssetType")!= null) { %>MNM004<% } %>">Asset Template</a>
				</div>
				<div class="sub_tabright sub_tabR_MNM004 <% if(request.getParameter("tAssetType")!= null) { %>activeR<% } %>">&nbsp;</div>
			</div>
		</div>

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
					<select id="assetType" name="assetType" >                       	
                    	<c:forEach var="assetType" items="${assetTypes}">                             
                          <c:choose>
                            <c:when test="${param.pAssetType == assetType.code}" > 
                              <option selected value="<c:out value="${assetType.code}"/>"><c:out value="${assetType.code}"/></option>
                            </c:when>
                            <c:otherwise> 
                              <option value="<c:out value="${assetType.code}"/>"><c:out value="${assetType.code}"/></option>
                            </c:otherwise> 
                          </c:choose>      
                        </c:forEach>  
                    </select>				
					<span><input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink()" onmousedown="this.src='./images/icons/view_ON.gif'" /></span>
				</div>
			</div>
		</div>

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
					  <form id="assetForm" action="" method="post">  
						<ec:table items="assets"   action="${pageContext.request.contextPath}/asset.do?pAssetType=${param.pAssetType}"
							imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
							width="98%"  view="fd" form="assetForm" autoIncludeParameters="false" rowsDisplayed="25"  >
							
							<ec:exportPdf fileName="assets.pdf" tooltip="Export PDF" 
									  headerTitle="Transportation Assets" />
							  <ec:exportXls fileName="assets.xls" tooltip="Export PDF" />
							  <ec:exportCsv fileName="assets.csv" tooltip="Export CSV" delimiter="|"/>
								
							<ec:row interceptor="obsoletemarker">
							  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="selectcol"
									property="assetId" />
							           
							  <ec:column property="assetNo" title="Asset No"/>
							  <ec:column property="assetDescription" title="Description"/>	
							  <ec:column property="assetType.code" title="Asset Type"/>
							  <ec:column property="assetStatus" title="Status" />
							  <ec:column property="assetTemplate.assetTemplateName" title="Asset Template" />
							</ec:row>
						  </ec:table>
					   </form> 
				</div>
			</div>
		</div>
	</div>
     <script>
	  addMultiRowHandlersColumn('ec_table', 'rowMouseOver', 'editasset.do','id',0,0,'assetType');

      function doCompositeLink() {
    	  location.href = "asset.do?pAssetType="+document.getElementById('assetType').value;
      }
    </script>   
  </tmpl:put>
</tmpl:insert>
