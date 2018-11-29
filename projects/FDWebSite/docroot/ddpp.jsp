<%@ page import='com.freshdirect.FDCouponProperties'
%><%@ page import='com.freshdirect.storeapi.*'
%><%@ page import='com.freshdirect.storeapi.fdstore.FDContentTypes'
%><%@ page import='com.freshdirect.fdstore.*'
%><%@ page import='com.freshdirect.storeapi.content.*'
%><%@ page import='com.freshdirect.storeapi.content.DomainValue'
%><%@ page import='com.freshdirect.fdstore.content.util.QueryParameterCollection'
%><%@ page import='com.freshdirect.fdstore.customer.EnumQuickbuyStatus'
%><%@ page import='com.freshdirect.fdstore.util.FilteringNavigator'
%><%@ page import='com.freshdirect.fdstore.util.URLGenerator'
%><%@ page import='com.freshdirect.framework.util.NVL'
%><%@ page import='com.freshdirect.framework.util.log.LoggerFactory'
%><%@ page import='com.freshdirect.framework.webapp.*'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil'
%><%@ page import='com.freshdirect.webapp.util.ConfigurationContext'
%><%@ page import='com.freshdirect.webapp.util.ConfigurationStrategy'
%><%@ page import='com.freshdirect.webapp.util.FDURLUtil'
%><%@ page import='com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy'
%><%@ page import='java.text.DecimalFormat'
%><%@ page import='java.text.SimpleDateFormat'
%><%@ page import='org.apache.commons.lang.StringEscapeUtils'
%><%@ page import='com.freshdirect.storeapi.util.*'
%><%@ page import='com.freshdirect.storeapi.attributes.*'
%><%@ page import='com.freshdirect.content.attributes.*'
%><%@ page import='com.freshdirect.fdstore.*'
%><%@ page import='com.freshdirect.fdstore.content.*'
%><%@ page import='com.freshdirect.storeapi.attributes.*'
%><%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*'
%><%@ page import='com.freshdirect.fdstore.customer.*'
%><%@ page import='com.freshdirect.framework.util.*'
%><%@ page import='com.freshdirect.framework.webapp.*'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.*'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.*'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName'
%><%@ page import='com.freshdirect.customer.EnumATCContext'
%><%@ page import='java.io.UnsupportedEncodingException'
%><%@ page import='java.net.*'
%><%@ page import='java.net.URLEncoder'
%><%@ page import='java.util.*'
%><%@ taglib uri='template' prefix='tmpl'
%><%@ taglib uri='bean' prefix='bean'
%><%@ taglib uri='logic' prefix='logic'
%><%@ taglib uri='freshdirect' prefix='fd'
%><%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'
%><% //expanded page dimension
final int W_INDEX_TOTAL = 970;
final int W_INDEX_CENTER_PADDING = 20;
final int W_INDEX_RIGHT_CENTER = W_INDEX_TOTAL - 228 - W_INDEX_CENTER_PADDING;

boolean disableLinks = false;
if (null !=request.getParameter("ppPreviewId")) {
	/* from category.jsp, for being forwarded here */

	//disable linking
	disableLinks = true;
	if ( request.getParameter("redirected")==null ) {
		 StringBuffer redirBuf = new StringBuffer();
         //redirBuf.append("/site_access/site_access_lite.jsp?successPage="+request.getRequestURI());

         redirBuf.append("/about/index.jsp?siteAccessPage=aboutus&successPage=" +
             request.getRequestURI());

         String requestQryString = request.getQueryString();

         if ((requestQryString != null) &&
                 (requestQryString.trim().length() > 0)) {
             redirBuf.append(URLEncoder.encode("?" +
                     request.getQueryString()));
         }
        redirBuf.append("&redirected=true");
		response.sendRedirect( redirBuf.toString());
		return;
	}
}

String catId = request.getParameter("catId");
String deptId = null;
// it should be CategoryModel ...
ContentNodeModel currentFolder = PopulatorUtil.getContentNode(catId);
final ProductContainer productContainer = (currentFolder instanceof ProductContainer) ? (ProductContainer) currentFolder : null;
final CategoryModel categoryModel = (currentFolder instanceof CategoryModel) ? (CategoryModel) currentFolder : null;
if (categoryModel != null) {
	deptId = (((CategoryModel)currentFolder).getDepartment() != null) ? ((CategoryModel)currentFolder).getDepartment().getContentName() : "";
}
%><fd:CheckLoginStatus id="user" /><%

/*
	// copied from the search.jsp:

	final String categoryId = FDCouponProperties.getCouponCMSCategory(); // tracking code


	// storing the view settings in the session
	FilteringNavigator nav = new FilteringNavigator(request,16);

  	request.setAttribute("filternav", nav);

	final int hideAfter = 8;
	boolean otherFilters=false;

	// default page size
	final int defaultPageSize = nav.getDefaultPageSize();
	QueryParameterCollection qc = QueryParameterCollection.decode(request.getQueryString());
	if ( qc.getParameter("pageSize") == null ) {
		nav.setPageSize(defaultPageSize);
	}
	*/
%>
<%

	//things needed for ddpp

    //needed for transactional
	List<ProductImpression> impressions = new ArrayList<ProductImpression>();
	ProductImpression pi = null;
	pageContext.setAttribute("ATCCONTEXT",EnumATCContext.DDPP.getName());
	//FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	ConfigurationContext confContext = new ConfigurationContext();
	confContext.setFDUser(user);
	ConfigurationStrategy confStrat = new DefaultProductConfigurationStrategy();

	String successPage = request.getRequestURI()+(request.getQueryString() == null ? "" : "?" + request.getQueryString());

	//String catId = NVL.apply(request.getParameter("catId"), ""); //category id
	String ppId = request.getParameter("pp_id");

	String TX_FORM_NAME        = catId+"_form"; // impression form name
	String TX_JS_NAMESPACE     = catId+"_JSnamespace"; // impression javascript namespace

	int transProdIndex = 0;
	int totalProds = 0;

	DecimalFormat df = new DecimalFormat("0");
	QueryStringBuilder queryString = new QueryStringBuilder();

	String view = NVL.apply(request.getParameter("view"), "grid"); //switch view param, if not "list", then use default ("grid")
		if ("".equals(view)) { view = "grid"; }
	String sort = NVL.apply(request.getParameter("sort"), "ourFaves"); //switch sort param, if not "price", "priceDesc" or "dept", then use default ("ourFaves")
		if ("".equals(sort)) { sort = "ourFaves"; }
	String order = NVL.apply(request.getParameter("order"), "asc"); //switch sort param, if not "price", "priceDesc" or "dept", then use default ("ourFaves")
		if ("".equals(order)) { order = "asc"; }
	String trkCode =  NVL.apply(request.getParameter("trkCode"), "ddpp");
		if ("".equals(trkCode)) { trkCode = "ddpp"; }

	boolean isFeatProd = true;
	int tempCounter = 0;
	int seqDDPP = 0;
	String trk = "ddpp";

	Set hideBursts = new HashSet();
		hideBursts.add(EnumBurstType.DEAL);
		hideBursts.add(EnumBurstType.YOUR_FAVE);
		hideBursts.add(EnumBurstType.NEW);
		hideBursts.add(EnumBurstType.BACK_IN_STOCK);

	String checkHtml = "<img src=\"/media/images/buttons/checkmark_green_15x10.png\" alt=\"\" />"; //escape single quotes in here
	List<ProductModel> globalUniqueProducts = new ArrayList<ProductModel>();

	String sp = request.getQueryString();
	String pairs[] = sp.replace("?", "&").split("&");

	for (String pair : pairs) {
		 String name = null;
		 String value = null;
		 int pos = pair.indexOf("=");
		 if (pos == -1) {
			continue; //not a valid key=val pair, ignore
		 } else {
			try {
				name = URLDecoder.decode(pair.substring(0, pos), "UTF-8");
				value = URLDecoder.decode(pair.substring(pos+1, pair.length()), "UTF-8");
				queryString.addParam(name, value);
			} catch (UnsupportedEncodingException e) {
				// Not really possible, throw unchecked
			    throw new IllegalStateException("ddpp.jsp: No UTF-8");
			}
		}
	}

	if (queryString.existsParam("ddpp_nfp")) { queryString.removeParam("ddpp_nfp"); }
	if (queryString.existsParam("ddpp_fp")) { queryString.removeParam("ddpp_fp"); }

    //--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", currentFolder.getPath());
	request.setAttribute("listPos", "PPSuperBuy,PPHeader,PPHeader2,PPLeftBottom,PPMidBottom,PPRightBottom");

	Map<String, List<ProductModel>> promoProducts = new HashMap<String, List<ProductModel>>();
	List<ProductModel> promotionProducts = new ArrayList<ProductModel>();

	CategoryModel categoryNode = (CategoryModel)currentFolder;

	String ppPreviewId = request.getParameter("ppPreviewId");
	boolean isPpPreview = (null ==(categoryNode).getProductPromotionType()|| null==ppPreviewId)?false:true;

	if(!isPpPreview){
		promotionProducts = categoryNode.getProducts();
	}else{
		promotionProducts = categoryNode.getPromotionPageProductsForPreview(ppPreviewId);
	}

	List<ProductModel> featProds = ProductPromotionUtil.getFeaturedProducts(promotionProducts,isPpPreview);
	List<ProductModel> nonfeatProds = new ArrayList<ProductModel>();

	//get sorted list by sort type
	if (SearchSortType.BY_PRICE.getLabel().equalsIgnoreCase(sort)) {
		if("asc".equalsIgnoreCase(order)) {
			nonfeatProds = ProductPromotionUtil.getNonFeaturedProducts(promotionProducts,ProductPromotionData.SORT_BY_PRICE_VIEW,isPpPreview);
		} else {
			nonfeatProds = ProductPromotionUtil.getNonFeaturedProducts(promotionProducts,ProductPromotionData.SORT_BY_PRICE_VIEW_INVERSE,isPpPreview);
		}
	} else if (SearchSortType.BY_DEPARTMENT.getLabel().equalsIgnoreCase(sort)) {
		nonfeatProds = ProductPromotionUtil.getNonFeaturedProducts(promotionProducts,ProductPromotionData.SORT_BY_DEPT_VIEW,isPpPreview);
	}else{
		nonfeatProds = ProductPromotionUtil.getNonFeaturedProducts(promotionProducts,isPpPreview);
	}


	//things "search" needs

	//turn non-feat items into a search results obj
	List<FilteringSortingItem<ProductModel>> searchProductResults = new ArrayList<FilteringSortingItem<ProductModel>>();
	if(null !=nonfeatProds){
		for (ProductModel productModel : nonfeatProds) {
			FilteringSortingItem<ProductModel> item = new FilteringSortingItem<ProductModel>(productModel);
			item.putSortingValue(EnumSortingValue.PHRASE, 1);
			searchProductResults.add(item);
		}
	}
	SearchResults search = new SearchResults(searchProductResults, Collections.<FilteringSortingItem<Recipe>> emptyList(), Collections.<FilteringSortingItem<CategoryModel>> emptyList(), "", true);


	// storing the view settings in the session
	FilteringNavigator nav = new FilteringNavigator(request,16);
	nav.setSortBy(SearchSortType.NATURAL_SORT);
	nav.saveState();

  	request.setAttribute("filternav", nav);

	// default page size
	final int defaultPageSize = nav.getDefaultPageSize();
	QueryParameterCollection qc = QueryParameterCollection.decode(request.getQueryString());
	if ( qc.getParameter("pageSize") == null ) {
		nav.setPageSize(defaultPageSize);
	}
	String title = "FreshDirect - " + currentFolder.getFullName();
%>
<bean:define id="activeTabVal" value='<%= (!search.getProducts().isEmpty() && request.getParameter("recipes")==null) || (search.getProducts().isEmpty() && search.getRecipes().isEmpty()) ? "products" : "recipes" %>' />

<tmpl:insert template='/common/template/ddpp_optimized.jsp'>
	<tmpl:put name="customCss">
		<style type="text/css">
			.product-grid { width:803px; }
			.items { width:804px; }
			.grid-view .grid-item-container { width:201px; }
			.see { font-size: 10px; }
		</style>
	</tmpl:put>
	<tmpl:put name="customJs">
		<script type="text/javascript">
			<%--
				Override the QB show panel function to make it look and act like how we want for ddpp
			--%>
			FD_QuickBuy.showPanelDDPP = function(deptId, catId, prdId, iatcNamespace, tracking) {
				return function() {
					var elementId= prdId+'_'+FD_QuickBuy._randomId(16), oStyle;
					var ctPanel = new YAHOO.widget.Panel(elementId, {
						fixedcenter: true,
						constraintoviewport: true,
						underlay: "matte",
						close: true,
						visible: false,
						modal: true,
						monitorresize: true,
						draggable: false,
						zIndex: '10'
					});
					var isWineDept = ("<%= JspMethods.getWineAssociateId().toLowerCase() %>" == deptId);

					if(isWineDept) {
						oStyle={
							closeButton:'container-close_wine',
							header:'hd_bg_wine'
						};
					} else {
						oStyle=FD_QuickBuy.style;
					}

					ctPanel.setHeader( "&nbsp;" );

					var winTitle = document.title.substring(14);

					var uri = '/quickbuy/product.jsp?catId='+encodeURIComponent(catId)+'&amp;productId='+encodeURIComponent(prdId)+'&amp;fdsc.source=quickbuy&amp;refTitle='+escape(winTitle)+'&amp;referer='+escape(window.location.href)+'&amp;uid='+elementId;

					if (tracking && tracking.source)
						uri += "&amp;fdsc.source="+encodeURIComponent(tracking.source);

					// store DOM ID
					uri += '&amp;uid='+encodeURIComponent(elementId);

					// include various codes for tracking purposes
					uri = FD_QuickBuy._includeTrackingCodes(uri, tracking);
					uri += '&amp;trkd=qb';

					// store master page URL and title for back-reference
					uri += '&amp;refTitle='+encodeURIComponent(winTitle)+'&amp;referer='+encodeURIComponent(window.location.href);

					var content = "";

					if (iatcNamespace)
						uri += '&amp;iatcNamespace='+escape(iatcNamespace);
					content += '<div id="'+elementId+'_ctnt">\n';
					content += '  <div id="'+elementId+'_overbox" class="overbox">\n';
					content += '    <div id="'+elementId+'_nfeat" class="nfeat roundedbox"></div>\n';
					content += '    <div id="'+elementId+'_errors" class="alerts roundedbox"></div>\n';
					content += '  </div>\n';
					content += '<div class="quickbuy-loading">Loading product...</div>\n';
					content += '<iframe id="'+elementId+'_frame" frameborder="0" src="'+uri+'" class="prodframe" style="height:1px"></iframe>';
					content += '</div>\n';

					ctPanel.setBody( content );

					ctPanel.render(document.body);

					// override .yui-panel hidden setting
					YAHOO.util.Dom.get(elementId).style.overflow = "visible";

					YAHOO.util.Dom.addClass(elementId+'_c','quickbuy-dialog');

					YAHOO.util.Dom.addClass( ctPanel.header, oStyle.header );
					YAHOO.util.Dom.addClass( FD_QuickBuy._getCloseButton(ctPanel.body), oStyle.closeButton );


					ctPanel.hideEvent.subscribe(function(e){
						YAHOO.util.Dom.get(elementId+'_overbox').style.visibility = "hidden";

						setTimeout(function() {
							ctPanel.destroy();
							if (document.quickbuyPanel) { document.quickbuyPanel = {}; }
						}, 0);
						/* on panel close, it's either ATC or a simple close */
						var statusElem = $jq('#'+iatcNamespace);
						var prodAddCont = statusElem.closest('.prodAddFP');
						var CTAButton = null;
						//check for a closed panel (no ATC)
						if (prodAddCont.length == 1) { //FP
							CTAButton = prodAddCont.find('.CTAButtonFP');
							if (statusElem.html() == '') {
								prodAddCont.animate({ height: '0', top: '200'}, 'fast');
								CTAButton.data('usedCTA', false);
							} else if (statusElem.html().indexOf('<%= EnumQuickbuyStatus.ADDED_TO_CART.getMessage() %>') != -1) {
								//added in customize
								//change to succss look and msg
								statusElem.removeClass('prodATCStatusError').addClass('prodATCStatusSuccess').prepend('<%=checkHtml%>');
								//change button look
								CTAButton.attr('src', '/media/images/buttons/customize_blue_s.png');
								//kick off timer for close effect
								setTimeout(function () {
									statusElem.fadeOut('400', function() {
										setTimeout(function () {
											prodAddCont.animate({ height: '0', top: '200'}, 'fast');
											//remove add to cart text, and show div again
											statusElem.empty().show();
										}, 500);
										CTAButton.data('usedCTA', false);
									});
								}, 2000);
							}
						} else {
							prodAddCont = statusElem.closest('.prodAdd');
							if (prodAddCont.length == 1) { //NFP
								CTAButton = prodAddCont.find('.CTAButtonNFP');
								if (statusElem.html() == '') {
									CTAButton.data('usedCTA', false);
								} else if (statusElem.html().indexOf('<%= EnumQuickbuyStatus.ADDED_TO_CART.getMessage() %>') != -1) {
									//added in customize
									//change to succss look and msg
									statusElem.removeClass('prodATCStatusError').addClass('prodATCStatusSuccess').prepend('<%=checkHtml%>');
									//change button look
									CTAButton.attr('src', '/media/images/buttons/customize_blue_s.png');
									//kick off timer for close effect
									setTimeout(function () {
										statusElem.fadeOut('400', function() {
											statusElem.empty().show();
											CTAButton.data('usedCTA', false);
										});
									}, 2000);
								}
							}
						}
					});

					document.quickbuyPanel = ctPanel;

					// show panel
					ctPanel.center();
					ctPanel.show();

					// Load New Feature popup content
					FD_QuickBuy.loadNewFeatureInner(elementId);
				};
			};

			$jq(document).ready(function(){

				$jq('.ddpp_feat_prod').hover(function() {
					if (!$jq(this).find('.CTAButtonFP').data('usedCTA')) {
						$jq(this).stop().find('.prodAddFP').animate({ height: '+=115', top: '85'}, 'fast');
					}
				}, function() {
					if (!$jq(this).find('.CTAButtonFP').data('usedCTA')) {
						$jq(this).stop().find('.prodAddFP').animate({ height: '0', top: '200'}, 'fast');
					}
				});


				$jq('.CTAButtonFP').on('click', function() {
					$jq(this).data('usedCTA', true);
				});
				$jq('.CTAButtonFP,.CTAButtonNFP').hover(function() {
					var buttonSrc = $jq(this).attr('src');
					if (buttonSrc.indexOf('_s.png') > -1) {
						$jq(this).attr('src', buttonSrc.replace('_s.png', '_sr.png'));
					} else if (buttonSrc.indexOf('_s.png') == -1) {
						$jq(this).attr('src', buttonSrc.replace('.png', '_r.png'));
					}
				}, function () {
					var buttonSrc = $jq(this).attr('src');
					if (buttonSrc.indexOf('_sr.png') > -1) {
						$jq(this).attr('src', buttonSrc.replace('_sr.png', '_s.png'));
					} else if (buttonSrc.indexOf('_s.png') == -1) {
						$jq(this).attr('src', buttonSrc.replace('_r.png', '.png'));
					}
				});
				<%-- make gutters the height of the product rows dynamically --%>
				var parHeight = 0;
				$jq('.gutter').each(function(index, elem) {
				    jqElem = $jq(elem);
				    if (index % 3 == 0) {
				        parHeight = jqElem.closest('.ddpp_grid_row').innerHeight()-25;
				    }
				    jqElem.css({ height: parHeight });
				});

				fixGridFeatRowHeights('.ddpp_feat_prod_cont', 'div.featurebox', '.grid-item'); <%-- moved to common_javascript --%>

			});
			FD_QuickBuy.style = {
					closeButton:'quickbuy-noheader-close',
					header:'quickbuy-noheader'
			};
		</script>
	</tmpl:put>
	<tmpl:put name="customJsBottom">
	</tmpl:put>

    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="<%= title %>"/>
    </tmpl:put>
<%-- 	<tmpl:put name='title' direct='true'><%= title %></tmpl:put> --%>
	<tmpl:put name="activeView">grid<% //= nav.isListView() && !nav.isRecipes() ? "list" : "grid" %></tmpl:put>
	<tmpl:put name="noResult"><%= search.getProducts().isEmpty() && search.getRecipes().isEmpty() ? "noresult" : "hasresults" %></tmpl:put>
	<tmpl:put name="startPage">resultpage</tmpl:put>
	<tmpl:put name="productsOnly">products-only</tmpl:put>
	<tmpl:put name="oas-header-1">
		<div class="PPHeader" id='oas_PPHeader' ad-fixed-size="true" ad-size-height="100" ad-size-width="970">
			<script type="text/javascript">
					OAS_AD('PPHeader');
			</script>
		</div>
	</tmpl:put>
	<tmpl:put name="oas-header-2">
		<div class="PPHeader2" id='oas_PPHeader2'>
			<script type="text/javascript">
					OAS_AD('PPHeader2');
			</script>
		</div>
	</tmpl:put>

	<tmpl:put name="deal-carousel-grofrodai">

		<fd:Department id='tempDepartment' departmentId='gro'/>
		<%
			DepartmentModel featExtDept = (DepartmentModel) tempDepartment;
			String featExtIdPrefix = "ddpp_BND"+tempDepartment;
			int featExtNumItems = 6; //this is the same across all rows
			int featExtMinItems = 3; //this is the same across all rows
			int featExtMaxItems = 0; //this is the same across all rows
			String featExtTrackCode = "ddpp_BND"+tempDepartment;
			int featExtCarWidth = 850; //this is the same across all rows
			boolean featExtUseQuickBuy = !disableLinks; //this is the same across all rows
			boolean featExtDisableLinks = disableLinks; //this is the same across all rows
		%>
		<%@ include file="/includes/layouts/i_featured_products_external.jspf" %>

		<fd:Department id='tempDepartment' departmentId='fro'/>
		<%
			featExtDept = (DepartmentModel) tempDepartment;
			featExtIdPrefix = "ddpp_BND"+tempDepartment;
			featExtTrackCode = "ddpp_BND"+tempDepartment;
		%>
		<%@ include file="/includes/layouts/i_featured_products_external.jspf" %>

		<fd:Department id='tempDepartment' departmentId='dai'/>
		<%
			featExtDept = (DepartmentModel) tempDepartment;
			featExtIdPrefix = "ddpp_BND"+tempDepartment;
			featExtTrackCode = "ddpp_BND"+tempDepartment;
		%>
		<%@ include file="/includes/layouts/i_featured_products_external.jspf" %>
	</tmpl:put>


	<tmpl:put name="bottom-ads">
		<table class="ddppBotAds">
			<tr>
				<td align="center" colspan="5">
					<br /><img src="/media_stat/images/layout/cccccc.gif" width="970" height="1" border="0" alt="" /><br />
					<span class="space4pix"><br /><br /></span>
				</td>
			</tr>
			<tr>
				<td align="center">
					<div class="PPLeftBottom" id='oas_PPLeftBottom'>
						<script type="text/javascript">
								OAS_AD('PPLeftBottom');
						</script>
					</div>
				</td>
				<td align="center" class="ddppBotAdsSep">
					<!-- sep -->
				</td>
				<td align="center">
					<div class="PPMidBottom" id='oas_PPMidBottom'>
						<script type="text/javascript">
								OAS_AD('PPMidBottom');
						</script>
					</div>
				</td>
				<td align="center" class="ddppBotAdsSep">
					<!-- sep -->
				</td>
				<td align="center">
					<div class="PPRightBottom" id='oas_PPRightBottom'>
						<script type="text/javascript">
								OAS_AD('PPRightBottom');
						</script>
					</div>
				</td>
			</tr>
		</table>
	</tmpl:put>

	<tmpl:put name="bottom-media">
		<%
			if (categoryNode.getBottomMedia() != null) {
				String deptBotItm = null;

				for(Iterator<Html> deptBotItr = categoryNode.getBottomMedia().iterator(); deptBotItr.hasNext();) {
					Html piece = deptBotItr.next();
					if (piece != null) {
						deptBotItm = piece.getPath();
					}
				}
				if (deptBotItm != null) {
					%><fd:IncludeMedia name='<%= deptBotItm %>' /><%
				}
			}
		%>
	</tmpl:put>


<fd:ProductsFilter results="<%= search %>" nav="<%= nav %>" domainsId="menus" itemsId="items" filteredItemCountId="itemCount">
	<fd:ProductsGroupingAndPaging items="<%= items %>" itemsId="products" nav="<%= nav %>">
		<tmpl:put name="productTabItemCount"><%= itemCount %></tmpl:put>

		<tmpl:put name="content-header">

			<div class="ddpp clearfix">

				<tmpl:get name="oas-header-1"/>
				<tmpl:get name="oas-header-2"/>

				<fd:FDShoppingCart id='cart' action='addMultipleToCart' result='result' source='<%= request.getParameter("fdsc.source")%>'>
					<% //START error messaging %>
					<fd:ErrorHandler result='<%=result%>' name='quantity' id='errorMsg'>
						<img src="/media_stat/images/layout/clear.gif" width="20" height="12" alt="" border="0" />
						<%@ include file="/includes/i_error_messages.jspf" %>
					</fd:ErrorHandler>
					<% //END error messaging %>
					<div style="text-align: left;"><%-- this fixes chrome, don't remove --%>
						<div class="ddpp_feat_prod_cont grid-view" id="ddpp_feat_prod_cont">

						<%
							int curLeftPos = 0;
							isFeatProd = true;
							String prodContStyle = "";
						%>
							<logic:iterate id="contentNode" collection="<%= featProds %>" type="java.lang.Object" indexId="idx">
								<%
									seqDDPP = idx; //use a seperate var for include

									if ( request.getParameter("featurebox") != null && request.getParameter("featurebox").equals("old") ) {

										ProductModelPromotionAdapter pm = (ProductModelPromotionAdapter) contentNode;
										String actionURI = FDURLUtil.getProductURI( pm, trkCode );

										pi = confStrat.configure((ProductModel) pm, confContext);
										impressions.add(pi);

										prodContStyle = "left: "+curLeftPos+"px;";

								%>

								<%@ include file="/includes/product/i_product_box_ddpp.jspf" %>
								<%
									}
									else {

										ProductModelPromotionAdapter productModelAdapter = (ProductModelPromotionAdapter) contentNode;
										String actionURI = FDURLUtil.getProductURI( productModelAdapter, trkCode );

										pi = confStrat.configure((ProductModel) productModelAdapter, confContext);
										impressions.add(pi);

										%>
										<div class="grid-item-container featurebox">
										<%@ include file="/includes/product/i_product_box_featured.jspf" %>
										</div>
										<%

									}
									/* break out if we have more than three products in the featured setup */
									if (seqDDPP == 3) { break; }
									curLeftPos = curLeftPos + 255; /* take from css, cont width + 14 (gutter) */
								%>
							</logic:iterate>
							<div class="PPSuperBuy" ad-fixed-size="true" ad-size-height="228" ad-size-width="275">
								<script type="text/javascript">
										OAS_AD('PPSuperBuy');
								</script>
							</div>
						</div>
					</div>
				</fd:FDShoppingCart>
			</div>
			<br style="clear:both" />
		</tmpl:put>

		<logic:equal name="activeTabVal" value="products">
			<% ArrayList selection = new ArrayList();  %>
			<tmpl:put name="activeTab">products</tmpl:put>
			<tmpl:put name="search-header">
				<% int productCount = search.getProducts().size(); %>
			</tmpl:put>

			<tmpl:put name="toolbar">
				<div id="sorter" class="span-18">
					<span class="label">Sort:</span>
					<display:SortBar defaultSort="ourFaves" sortItems="<%= new SearchSortType[] {SearchSortType.BY_OURFAVES,SearchSortType.BY_PRICE, SearchSortType.BY_DEPARTMENT} %>">
						<a href="<%= currentUrl %>" class="sortitem <%= isSelected ? "sortitem-selected" : ""%> <%= currentIndex==1 ? "nodot" : ""%>"><%= currentText%></a>
					</display:SortBar>
				</div>
			</tmpl:put>

			<tmpl:put name="viewAll">
				<% 	if( itemCount > defaultPageSize) {
						if(nav.getPageSize() == 0) {
							nav.resetState();
							nav.setPageSize(defaultPageSize);
				%>
						<a href="<%= nav.getLink() %>" class="button middle white bold view-all">Show <%= defaultPageSize %></a>
				<%		} else {
							nav.resetState();
							nav.setPageSize(0);
				%>
						<a href="<%= nav.getLink() %>" class="button middle white bold view-all">Show all</a>
				<%	}

						nav.resetState();
				}	%>
			</tmpl:put>

			<tmpl:put name="pagerTop">
				<div class="results">
					<span>Results: </span>
					<span class="results-current"><%= ((nav.getPageNumber())*nav.getPageSize())+1  %>-<%= nav.getPageSize()==0 ?
								itemCount : Math.min((nav.getPageNumber()+1)*nav.getPageSize(),itemCount)  %></span>
					<span>of</span>
					<span class="results-all"><%= itemCount %></span>
				</div>
				<div class="pager-content">
				<% if ( nav.getPageSize() > 0 && itemCount > nav.getPageSize()) { %>
					<display:Pager productsSize="<%= itemCount %>" nav="<%= nav %>"/>
				<% } %>
					<tmpl:get name="viewAll" />
				</div>
			</tmpl:put>

			<tmpl:put name="pagerBottom">
				<div class="pager-content">
				<% if ( nav.getPageSize() > 0 && itemCount > nav.getPageSize()) { %>
					<display:Pager productsSize="<%= itemCount %>" nav="<%= nav %>"/>
				<% } %>
					<tmpl:get name="viewAll" />
				</div>
				<div class="back-to-top"><a href="#content_top">back to top</a></div>
			</tmpl:put>

			<tmpl:put name="recommendations-content" direct="true">
				<%-- removed content --%>
			</tmpl:put>
			<%
			// RECOMMENDER for "view 20"
			if ( nav.getPageSize() != 0) { %>
			<tmpl:put name="recommendations" direct="true">
				<%-- removed content --%>
			</tmpl:put>
			<% } %>

			<tmpl:put name="content" direct="true">
				<%
				pageContext.setAttribute("ISONSEARCHPAGE",null);
				for (ListIterator<FilteringSortingItem <ProductModel>> it=products.listIterator() ; it.hasNext();) {
					{
					pi = confStrat.configure(it.next().getModel(), confContext);
					%><div class="grid-item-container"><%@ include file="/includes/product/i_product_box.jspf" %></div><%
					}

				}
				%>

			</tmpl:put>



		  <tmpl:put name='filterNavigator'>
		    <% request.setAttribute("filtermenus", menus); %>
		    <tmpl:insert template='/common/template/filter_navigator.jsp'>
		    </tmpl:insert>
		  </tmpl:put>

			<tmpl:put name="selection-header" direct="true">
			</tmpl:put>

			<tmpl:put name="selection-list" direct="true">
			</tmpl:put>

			<tmpl:put name="pagerTop">
				<% nav.resetState(); %>
				<div class="results">
					<span>Results: </span>
					<span class="results-current"><%= ((nav.getPageNumber())*nav.getPageSize())+1  %>-<%= nav.getPageSize()==0 ?
								itemCount : Math.min((nav.getPageNumber()+1)*nav.getPageSize(),itemCount)  %></span>
					<span>of</span>
					<span class="results-all"><%= itemCount %></span>
				</div>
				<div class="pager-content"><display:Pager productsSize="<%= itemCount %>" nav="<%= nav %>"/><tmpl:get name="viewAll" /></div>
			</tmpl:put>

			<tmpl:put name="pagerBottom">
				<% nav.resetState(); %>
				<div class="pager-content"><display:Pager productsSize="<%= itemCount %>" nav="<%= nav %>"/><tmpl:get name="viewAll" /></div>
				<div class="back-to-top"><a href="#content_top">back to top</a></div>
			</tmpl:put>

		</logic:equal>
	</fd:ProductsGroupingAndPaging>
</fd:ProductsFilter>


<% nav.resetState();nav.setPageSize(0); %>
<% nav.resetState(); %>

</tmpl:insert>
