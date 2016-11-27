<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>

<tmpl:insert template='/common/sitelayout.jsp'>

<% 
	String pageTitle = "Asset Template";
	pageContext.setAttribute("HAS_DELETEBUTTON", "false");
%>
    <tmpl:put name='title' direct='true'> Admin : <%=pageTitle%></tmpl:put>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>	
	<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>
	
  <tmpl:put name='content' direct='true'>
		<div class="MNM002 subsub or_999">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM002 <% if(request.getParameter("pAssetType")!= null) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <%if(request.getParameter("pAssetType")!= null) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="asset.do?pAssetType=TRUCK" class="<% if(request.getParameter("pAssetType")!= null) { %>MNM002<% } %>">Asset</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM002 <% if(request.getParameter("pAssetType")!= null) { %>activeR<% } %>">&nbsp;</div>
		
			<div class="sub_tableft sub_tabL_MNM002 <% if(request.getParameter("tAssetType")!= null) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <%if(request.getParameter("tAssetType")!= null) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="assettemplate.do?tAssetType=TRUCK" class="<% if(request.getParameter("tAssetType")!= null) { %>MNM002<% } %>">Asset Template</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM002 <% if(request.getParameter("tAssetType")!= null) { %>activeR<% } %>">&nbsp;</div>
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
					<select id="assetType" name="assetType">
                    	<c:forEach var="assetType" items="${assetTypes}">
                          <c:choose>
                            <c:when test="${param.tAssetType == assetType.code}" > 
                              <option selected value="<c:out value="${assetType.code}"/>"><c:out value="${assetType.code}"/></option>
                            </c:when>
                            <c:otherwise> 
                              <option value="<c:out value="${assetType.code}"/>"><c:out value="${assetType.code}"/></option>
                            </c:otherwise> 
                          </c:choose>      
                        </c:forEach>  
                    </select>				
					<span>
						<input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink()" onmousedown="this.src='./images/icons/view_ON.gif'" />
					</span>
				</div>
			</div>
		</div>

		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
					  <form id="assetTemplateForm" action="" method="post">  
						<ec:table items="assetTemplates"   action="${pageContext.request.contextPath}/assettemplate.do?tAssetType=${param.tAssetType}"
							imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title=""
							width="98%"  view="fd" form="assetForm" autoIncludeParameters="false" rowsDisplayed="25"  >
							
							<ec:exportPdf fileName="assetTemplates.pdf" tooltip="Export PDF" 
									  headerTitle="Transportation Assets" />
							  <ec:exportXls fileName="assetTemplates.xls" tooltip="Export PDF" />
							  <ec:exportCsv fileName="assetTemplates.csv" tooltip="Export CSV" delimiter="|"/>
								
							<ec:row interceptor="obsoletemarker">
							  <ec:column title=" " width="5px" 
									filterable="false" sortable="false" cell="selectcol"
									property="assetTemplateId" />
							  <ec:column property="assetTemplateName" title="Template Name"/>	
							  <ec:column property="assetType.code" title="Asset Type"/>
							</ec:row>
						  </ec:table>
					   </form> 
				</div>
			</div>
		</div>
	</div>
     <script>
	  addMultiRowHandlersColumn('ec_table', 'rowMouseOver', 'editassettemplate.do','id',0,0,'tAssetType','assetType');
      function doCompositeLink() {
    	  location.href = "assettemplate.do?tAssetType="+document.getElementById('assetType').value;
      }
    </script>   

  </tmpl:put>
</tmpl:insert>
