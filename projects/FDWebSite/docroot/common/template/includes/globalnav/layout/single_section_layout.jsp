<%@ page import='java.util.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<%@ taglib uri="freshdirect" prefix='fd'%>
<%@ page import="com.freshdirect.storeapi.content.CategoryModel"%>
<%@ page import="com.freshdirect.storeapi.content.ProductContainer"%>
<%@ page import="com.freshdirect.storeapi.content.GlobalMenuSectionModel"%>

<c:set var="globalMenuSection" value="${globalMenuItem.subSections[0]}"/>
<c:set var="linkedProductContainer"	value="${globalMenuSection.linkedProductContainer}" />

<div class="popsubmenu onedep">
	<div class="psmcontent container">
		<div class="column span-20 dottedborder">
			<div class="psmhead column span-20 last">
				<h3>
					<c:out value="${linkedProductContainer.globalMenuTitleLabel}" />
				</h3>

				<jsp:useBean id="linkedProductContainer" type="ProductContainer" />
				<display:ProductContainerLink productContainer="<%=linkedProductContainer%>" trackingCode="showAll" >
					<c:out value="${linkedProductContainer.globalMenuLinkLabel}" />
				</display:ProductContainerLink>
			</div>

			<c:set var="subCats" value="${globalMenuSection.allSubCategoryItems}" />
			<%pageContext.setAttribute("subCatColSize", Math.ceil(((Collection) pageContext.getAttribute("subCats")).size() / 3d));%>

			<c:forEach varStatus="colCntStatus" begin="0" end="2">
				<ul class="column span-5 stand dottedborder">
					<c:forEach var="category" items="${subCats}" begin="${colCntStatus.index*subCatColSize}" end="${(colCntStatus.index+1)*subCatColSize - 1}">
						<li>
							<jsp:useBean id="category" type="CategoryModel" /> 
							<display:ProductContainerLink productContainer="<%=category%>" trackingCode="home" >
								<jsp:useBean id="category" type="CategoryModel" />
								<display:GlobalMenuIcon productContainer="<%=category%>" /> 
								<c:out value="${category.fullName}" />
							</display:ProductContainerLink>
						</li>
					</c:forEach>
				</ul>
			</c:forEach>

			<div class="column span-5 last shopby">
				<jsp:useBean id="globalMenuSection" type="GlobalMenuSectionModel" />
				<fd:IncludeHtml html="<%=globalMenuSection.getEditorial()%>" />
			</div>
		</div>

		<%--ad--%>
		<div class="column span-4 last">menu ad</div>
	</div>
</div>
