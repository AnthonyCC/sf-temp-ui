<%@ page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<%@ taglib uri="freshdirect" prefix='fd'%>
<%@ page import="com.freshdirect.storeapi.content.CategoryModel"%>
<%@ page import="com.freshdirect.storeapi.content.ProductContainer"%>
<%@ page import="com.freshdirect.storeapi.content.GlobalMenuSectionModel"%>

<div class="popsubmenu twodeps">
	<div class="psmcontent container">

		<c:forEach var="globalMenuSection" items="${globalMenuItem.subSections}" end="1">
			<c:set var="linkedProductContainer" value="${globalMenuSection.linkedProductContainer}" />

			<div class="column span-10 dottedborder">
				<div class="psmhead column span-10 last">
					<h3>
						<c:out value="${linkedProductContainer.globalMenuTitleLabel}" />
					</h3>

					<jsp:useBean id="linkedProductContainer" type="ProductContainer" />
					<display:ProductContainerLink productContainer="<%=linkedProductContainer%>" trackingCode="showAll" >
						<c:out value="${linkedProductContainer.globalMenuLinkLabel}" />
					</display:ProductContainerLink>
				</div>

				<c:set var="subCats" value="${globalMenuSection.allSubCategoryItems}" />
				<%pageContext.setAttribute("subCatCnt", ((Collection) pageContext.getAttribute("subCats")).size());%>

				<ul class="column span-5 stand dottedborder">
					<c:forEach var="category" items="${subCats}" end="${(subCatCnt + 3) / 2 -1 }">
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

				<ul class="column span-5 stand last">
					<c:forEach var="category" items="${subCats}" begin="${(subCatCnt + 3) / 2}">
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

				<div class="column span-5 last shopby">
					<jsp:useBean id="globalMenuSection" type="GlobalMenuSectionModel" />
					<fd:IncludeHtml html="<%=globalMenuSection.getEditorial()%>" />
				</div>
			</div>

		</c:forEach>

		<div class="column span-4 last">menu ad</div>
	</div>
</div>

