<%@ page import='java.util.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>
<%@ taglib uri="freshdirect" prefix='fd'%>
<%@ page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@ page import="com.freshdirect.fdstore.content.ProductContainer"%>


<div class="popsubmenu fourdeps">
	<div class="psmcontent container">
		<div class="column span-20 dottedborder">
			<c:forEach var="globalMenuSection" items="${globalMenuItem.subSections}" end="3" varStatus="varStat">

				<c:set var="linkedProductContainer" value="${globalMenuSection.linkedProductContainer}"/>
				<c:set var="columnStyle" value="dottedborder"/>
				<c:if test="${varStat.last}">
					<c:set var="columnStyle" value="last"/>
				</c:if>

				<div class="column span-5 <c:out value="${columnStyle}"/>">
					<div class="psmhead span-5 last">
						<h3>
							<c:out value="${linkedProductContainer.globalMenuTitleLabel}" />
						</h3>

						<jsp:useBean id="linkedProductContainer" type="ProductContainer" />
						<display:ProductContainerLink productContainer="<%=linkedProductContainer%>" trackingCode="showAll" >
							<c:out value="${linkedProductContainer.globalMenuLinkLabel}" />
							<jsp:useBean id="linkedProductContainer" type="ProductContainer" />
							<display:GlobalMenuIcon productContainer="<%=linkedProductContainer%>" large="true"/>
						</display:ProductContainerLink>
					</div>
					<ul class="span-5 last stand">
						<c:forEach var="category" items="${globalMenuSection.allSubCategoryItems}">
							<li>
								<jsp:useBean id="category" type="CategoryModel" />
								<display:ProductContainerLink productContainer="<%=category%>" trackingCode="home" >
									<c:out value="${category.fullName}" />
								</display:ProductContainerLink>
							</li>
						</c:forEach>
					</ul>
				</div>
			</c:forEach>
		</div>
		<div class="column span-4 last">menu ad</div>
	</div>
</div>

