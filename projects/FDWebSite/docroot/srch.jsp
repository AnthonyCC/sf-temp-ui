<%@ page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page
	import="java.util.HashMap, java.util.ArrayList, java.util.Iterator,  java.util.Map,  java.util.Set"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr"%>
<%@ taglib uri="https://developers.google.com/closure/templates"
	prefix="soy"%>

<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='bean' prefix='bean'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>

<%@ taglib uri="fd-data-potatoes" prefix="potato"%>
<%@ taglib uri="unbxd" prefix='unbxd'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<fd:OptionalParameterValidator parameter="pageSize" parameterType="<%=Integer.class.getSimpleName()%>"/>
<fd:OptionalParameterValidator parameter="activePage" parameterType="<%=Integer.class.getSimpleName()%>"/>

<fd:CheckLoginStatus id="user" guestAllowed='true'
	recognizedAllowed='true' />

<c:choose>
	<c:when test="${not empty param.pageType}"><c:set var="pageId" value="${param.pageType}" /></c:when>
	<c:otherwise><c:set var="pageId" value="search" /></c:otherwise>
</c:choose>
<%
	// [APPDEV-3953] Special rule for DDPP Preview Mode
	// Redirect to site access for getting zip code first
	if (null != request.getParameter("ppPreviewId")) {
		/* from category.jsp, for being forwarded here */

		//disable linking
		// disableLinks = true;
		if (request.getParameter("redirected") == null) {
			StringBuffer redirBuf = new StringBuffer();
			//redirBuf.append("/site_access/site_access_lite.jsp?successPage="+request.getRequestURI());

			redirBuf.append("/about/index.jsp?siteAccessPage=aboutus&successPage=" + request.getRequestURI());

			String requestQryString = request.getQueryString();

			if ((requestQryString != null) && (requestQryString.trim().length() > 0)) {
				redirBuf.append(URLEncoder.encode("?" + requestQryString));
			}
			redirBuf.append("&redirected=true");
			response.sendRedirect(redirBuf.toString());
			return;
		}
	}

	String template = "/common/template/browse_template.jsp";

	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user)
			&& JspMethods.isMobile(request.getHeader("User-Agent"));
	if (mobWeb) {
		template = "/common/template/mobileWeb.jsp"; //mobWeb template
		request.setAttribute("sitePage", "www.freshdirect.com/mobileweb/browse.jsp"); //change for OAS
	}
%>
<fd:BrowsePartialRolloutRedirector user="<%=user%>" id="${param.id}" />

<%-- TODO search --%>
<potato:browse />

<%-- OAS variables --%>
<c:set var="sitePage" scope="request"
	value="${empty browsePotato.descriptiveContent.oasSitePage ? 'www.freshdirect.com/search.jsp' : browsePotato.descriptiveContent.oasSitePage }" />

<c:choose>
	<c:when test="${browsePotato.searchParams.pageType == 'STAFF_PICKS'}">
		<c:set var="listPos" scope="request"
			value="SystemMessage,LittleRandy,CategoryNote,PPHeader,PPHeader2,PPSuperBuy,SPBottom1,SPBottom2,SPBottom3,PPSearchContent" />
	</c:when>
	<c:otherwise>
		<c:set var="listPos" scope="request"
			value="SystemMessage,LittleRandy,CategoryNote,PPHeader,PPHeader2,PPSuperBuy,PPLeftBottom,PPMidBottom,PPRightBottom,PPSearchContent" />
	</c:otherwise>
</c:choose>

<tmpl:insert template='<%=template%>'>
	<tmpl:put name='eventsource' direct='true'>BROWSE</tmpl:put>

	<tmpl:put name='soypackage' direct='true'>
		<soy:import packageName="browse" />
		<soy:import packageName="srch" />
	</tmpl:put>

	<tmpl:put name='extraCss' direct='true'>
		<jwr:style src="/quickshop.css" media="all" />
		<jwr:style src="/browse.css" media="all" />
		<jwr:style src="/srch.css" media="all" />
	</tmpl:put>

	<tmpl:put name='containerExtraClass' direct='true'>srch ${empty browsePotato.menuBoxes.menuBoxes ? 'emptymenu' : ''}</tmpl:put>
	<c:choose>
		<c:when test="${empty pageId or 'search' eq pageId}">
			<tmpl:put name="seoMetaTag" direct="true">
				<fd:SEOMetaTag title="${browsePotato.descriptiveContent.pageTitle}"
					metaDescription="${browsePotato.descriptiveContent.metaDescription}"></fd:SEOMetaTag>
			</tmpl:put>
		</c:when>
		<c:otherwise>
			<tmpl:put name="seoMetaTag" direct="true">
				<fd:SEOMetaTag pageId="${pageId}"></fd:SEOMetaTag>
			</tmpl:put>
		</c:otherwise>
	</c:choose>
	<tmpl:put name='pageType'>${browsePotato.searchParams.pageType}</tmpl:put>

	<tmpl:put name='deptnav' direct='true'>
		<div class="srch-header NOMOBWEB">
			<soy:render template="srch.header"
				data="${browsePotato.searchParams}" />
		</div>
	</tmpl:put>

	<tmpl:put name='tabs' direct='true'>
		<soy:render template="srch.noResultsNoSuggestions" data="${browsePotato.searchParams}" />
		
		<div id="listsearch" class="NOMOBWEB">
			<soy:render template="srch.listSearch"
				data="${browsePotato.searchParams}" />
		</div>
		<section class="page-type NOMOBWEB">
			<soy:render template="browse.pageType"
				data="${browsePotato.searchParams}" />
		</section>

		<c:choose>
			<c:when test="${browsePotato.searchParams.pageType == 'NEWPRODUCTS'}">

				<%
				    String moduleContainerId = FDStoreProperties.getNewProductsPageCarouselContainerContentKey();
					pageContext.setAttribute("moduleContainerId", moduleContainerId);
					boolean isModuleEnabled = FDStoreProperties.isNewProductsPageCarouselEnabled();
					pageContext.setAttribute("isModuleEnabled", isModuleEnabled);
				%>

				<c:choose>
					<c:when test="${isModuleEnabled}">
						<tmpl:put name='new products recommender' direct='true'>
							<div class="new-products-carousel-module">
								<potato:modulehandling name="newProductsModule"
									moduleContainerId="${moduleContainerId}" />
								<soy:render template="common.contentModules"
									data="${newProductsModule}" />
							</div>
						</tmpl:put>
					</c:when>
				</c:choose>
			</c:when>
			<c:otherwise>
				<section class="srch-ddpp">
					<%-- this does the featured items --%>
					<%
					    if (mobWeb) {
					%>
					<div class="isHookLogic-false">
						<div class="browse-sections transactional">
							<div class="browseContent">
								<section class="">
									<div class="sectionContent">
										<soy:render template="common.simpleFixedProductList"
											data="${browsePotato.ddppproducts}" />

										<div class="oas-cnt PPSuperBuy" id="oas_b_PPSuperBuy">
											<script type="text/javascript">
								      OAS_AD('PPSuperBuy');
								  </script>
										</div>

									</div>
								</section>
							</div>
						</div>
					</div>
					<%
					    } else {
					%>
					<soy:render template="srch.ddppWrapper"
						data="${browsePotato.ddppproducts}" />
					<%
					    }
					%>
				</section>
			</c:otherwise>
		</c:choose>



		<c:choose>
			<c:when test="${browsePotato.searchParams.pageType == 'STAFF_PICKS'}">
				<%-- DEBUG: this should switch to new type --%>
				<%--do nothing --%>
			</c:when>
			<c:otherwise>
				<section class="ddpp-oas NOMOBWEB">
					<div class="oas-cnt PPSuperBuy" id="oas_PPSuperBuy">
						<script type="text/javascript">
			        OAS_AD('PPSuperBuy');
			    </script>
					</div>
				</section>
			</c:otherwise>
		</c:choose>
		<%--
		<nav class="tabs NOMOBWEB" role="tablist">
			<soy:render template="srch.searchTabs"
				data="${browsePotato.searchParams}" />
		</nav>
		--%>
		<%--
		<div class="search-input NOMOBWEB">
			<soy:render template="srch.searchParams"
				data="${browsePotato.searchParams}" />
		</div>
		--%>
	</tmpl:put>

	<tmpl:put name='leftnav' direct='true'>
		<div id="leftnav">
			<soy:render template="browse.menu" data="${browsePotato.menuBoxes}" />
		</div>
	</tmpl:put>

	<tmpl:put name='content' direct='true'>
    	<div id="searchPanel" role="tabpanel" tabindex="0">
		    <div class="oas-cnt" ad-fixed-size="true" ad-size-height="95" ad-size-width="774" id="oas_PPSearchContent">
				<script type="text/javascript">
					OAS_AD('PPSearchContent');
				</script>
			</div>
		
	    	<div class="itemcount-cont">
				<section class="itemcount">
					<soy:render template="srch.searchSuggestions"
						data="${browsePotato.searchParams}" />
				</section>
				<script>
					$jq(document).on('ready', function() {
						if ((document.referrer).indexOf('expresssearch.jsp') !== -1) {
							$jq('.itemcount-cont').append('<button class="right cssbutton green transparent icon-arrow-left2-before back">Back to Express Search</button><br style="clear:both;" />');
							$jq('.srch .itemcount').addClass('wBackButton');
						}
					});
				</script>
			</div>
    	

			<soy:render template="browse.topMedia" data="${browsePotato.descriptiveContent}" />
			<%-- remove top pagination
			    <div class="pager-holder top">
			      <c:if test="${not empty browsePotato.pager}">
			        <soy:render template="browse.pager" data="${browsePotato.pager}" />
			      </c:if>
			    </div>
	     	--%>

			<c:choose>
				<c:when test="${browsePotato.searchParams.pageType != ''}">
					<div id="sorter">
						<soy:render template="browse.sortBar" data="${browsePotato.sortOptions}" />
					</div>
				</c:when>
			</c:choose>

			<div class="browse-filtertags">
				<soy:render template="browse.filterTags" data="${browsePotato.filterLabels}" />
			</div>

			<div class="isHookLogic-false browse-sections-top-cont">
				<c:choose>
					<c:when test="${browsePotato.searchParams.pageType == 'SEARCH'}">
						<div class="browse-sections-top transactional">
							<soy:render template="srch.topContent"
								data="${browsePotato.sections}" />
						</div>
						<%
						    if (!mobWeb) {
						%>
						<div class="srch-carousel"></div>

						<div class="browse-sections-bottom transactional">
							<soy:render template="srch.bottomContent"
								data="${browsePotato.sections}" />
						</div>
						
						<%
						    }
						%>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when
								test="${browsePotato.searchParams.pageType == 'STAFF_PICKS'}">
								<div class="browse-sections transactional">
									<%-- this does the main prod grid --%>
									<soy:render template="srch.staffPicksContent"
										data="${browsePotato.assortProducts}" />
								</div>
							</c:when>
							<c:otherwise>
								<div class="browse-sections transactional">
									<%-- this does the main prod grid --%>
									<soy:render template="browse.content"
										data="${browsePotato.sections}" />
								</div>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>

			<div class="pager-holder bottom">
				<c:if test="${not empty browsePotato.pager}">
					<soy:render template="browse.pager" data="${browsePotato.pager}" />
				</c:if>
			</div>
		</div>
		
		<script>
			window.FreshDirect = window.FreshDirect || {};
			window.FreshDirect.browse = window.FreshDirect.browse || {};
			window.FreshDirect.globalnav = window.FreshDirect.globalnav || {};
	
			window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>
			window.FreshDirect.globalnav.data = <fd:ToJSON object="${globalnav}" noHeaders="true"/>
			window.FreshDirect.browse.data.searchParams = window.FreshDirect.browse.data.searchParams || {};
			window.FreshDirect.browse.data.isAutosuggest = window.FreshDirect.browse.data.isAutosuggest || false;
	
			/* APPDEV-5920 Staff picks sort bar implementation */
			window.FreshDirect.browse.data.pageType = window.FreshDirect.browse.data.pageType || <c:choose><c:when test="${browsePotato.searchParams.pageType == 'STAFF_PICKS'}">'STAFF_PICKS'</c:when><c:otherwise> {} </c:otherwise> </c:choose>;
			window.FreshDirect.browse.data.sortOptions = window.FreshDirect.browse.data.sortOptions || {};
	    </script>
	</tmpl:put>

	<tmpl:put name='bottom' direct='true'>
		<c:choose>
			<c:when test="${browsePotato.searchParams.pageType == 'PRES_PICKS'}">
				<div class="srch-carousel pres-picks-carousel"/>
			</c:when>
			<c:when
				test="${browsePotato.searchParams.pageType != 'SEARCH'}">
				<div class="srch-carousel">
					<soy:render template="srch.carouselWrapper"
						data="${browsePotato.carousels}" />
				</div>
			</c:when>
		</c:choose>

		<div class="ddpp-bottom">
			<hr class="ddpp-hr top" />
			<table class="ddppBotAds">
				<tr>
					<td align="center" colspan="3" class='ddppBotAd-width'></td>
				</tr>
				<c:choose>
					<c:when
						test="${browsePotato.searchParams.pageType == 'STAFF_PICKS'}">
						<tr>
							<td align="center">
								<div class="oas-cnt SPBottom1" id="oas_SPBottom1">
									<script type="text/javascript">
		                                    OAS_AD('SPBottom1');
		                            </script>
								</div>
							</td>
							<td align="center" class="">
								<div class="oas-cnt SPBottom2" id="oas_SPBottom2">
									<script type="text/javascript">
		                                    OAS_AD('SPBottom2');
		                            </script>
								</div>
							</td>
							<td align="center">
								<div class="oas-cnt SPBottom3" id="oas_SPBottom3">
									<script type="text/javascript">
		                                    OAS_AD('SPBottom3');
		                            </script>
								</div>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
							<td align="center">
								<div class="oas-cnt PPLeftBottom" id="oas_PPLeftBottom">
									<script type='text/javascript'>
                               OAS_AD('PPLeftBottom');
                            </script>
								</div>
							</td>
							<td align="center" class="ddppBotAd-sep">
								<div class="oas-cnt PPMidBottom" id="oas_PPMidBottom">
									<script type='text/javascript'>
                                 OAS_AD('PPMidBottom');
                              </script>
								</div>
							</td>
							<td align="center">
								<div class="oas-cnt PPRightBottom" id="oas_PPRightBottom"
									ad-size-width='186' ad-size-height='216'>
									<script type='text/javascript'>
                                 OAS_AD('PPRightBottom');
                              </script>
								</div>
							</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>
			<hr class="ddpp-hr bottom" />
			<center class="legaltext">
				New Fresh Deals items are available weekly. Prices and the selection
				of items are subject to change without notice. Sorry, no rain
				checks. Offers expire each Wednesday at 11:59 p.m. and promotional
				pricing will be removed from orders after that time. Offer is
				nontransferable.<br> <br> Wines and spirits sold by
				FreshDirect Wines &amp; Spirits. Alcohol cannot be delivered outside
				of New York state. Beer, wine and spirits will be removed from your
				cart during checkout if an out of state address is selected for
				delivery. The person receiving your delivery must have
				identification proving they are over the age of 21 and will be asked
				for their signature.
			</center>
		</div>
	</tmpl:put>

	<%
	    if (mobWeb) {
	%>
	<tmpl:put name="jsmodules">
		<%@ include file="/common/template/includes/i_jsmodules.jspf"%>
	</tmpl:put>
	<%
	    }
	%>

	<tmpl:put name='extraJsModules'>
		<jwr:script src="/browse.js" useRandomParam="false" />
		<jwr:script src="/srch.js" useRandomParam="false" />
	</tmpl:put>
</tmpl:insert>
