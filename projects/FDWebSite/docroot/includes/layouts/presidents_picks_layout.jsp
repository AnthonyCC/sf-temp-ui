<%@page import="com.freshdirect.customer.EnumATCContext"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='java.net.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='java.io.UnsupportedEncodingException'%>
<%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.storeapi.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import="com.freshdirect.webapp.util.ConfigurationContext"%>
<%@ page import="com.freshdirect.webapp.util.ConfigurationStrategy"%>
<%@ page import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy"%>
<%@ page import="com.freshdirect.fdstore.customer.EnumQuickbuyStatus" %>
<%@ page import='com.freshdirect.storeapi.util.*' %>
<%@ page import="com.freshdirect.fdstore.content.util.QueryParameterCollection"%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%

	// Query string
	QueryParameterCollection qv = QueryParameterCollection.decode(request.getQueryString());

	String isPreviewMode = NVL.apply(request.getParameter("ppPreviewId"), "false");
	boolean disableLinks = false;

	if (!"false".equalsIgnoreCase(isPreviewMode)) {
		/* manipulate layout for preview mode */

		//disable linking
		disableLinks = true;

	%>
<fd:CheckLoginStatus/><% } else { %>
	<fd:CheckLoginStatus   />
	<% }

	if (FDStoreProperties.isCclAjaxDebugClient()) {
		// debug JS libs
		%>
		<jwr:script src="/roundedcorners.js" useRandomParam="false" />
		<%
		} else {
			// production JS libs
		%>
		<%-- no longer in use
		<script type="text/javascript" src="/assets/javascript/rounded_corners-min.js"></script>
		 --%>
		<%
		}
%>
<display:InitLayout/>
<%

	//needed for transactional
	List<ProductImpression> impressions = new ArrayList<ProductImpression>();
	ProductImpression pi = null;
	pageContext.setAttribute("ATCCONTEXT",EnumATCContext.DDPP.getName());
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	ConfigurationContext confContext = new ConfigurationContext();
	confContext.setFDUser(user);
	ConfigurationStrategy confStrat = new DefaultProductConfigurationStrategy();

	String successPage = request.getRequestURI()+(request.getQueryString() == null ? "" : "?" + request.getQueryString());

	String catId = NVL.apply(request.getParameter("catId"), ""); //category id
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

	/* fix action url so tracking params aren't duplicated */
/*
	Map<String, String[]> reqMap = request.getParameterMap();

	for (Iterator reqIter = reqMap.entrySet().iterator(); reqIter.hasNext();) {
		Map.Entry e = (Map.Entry) reqIter.next();
		String key = (String) e.getKey();
		String[] values = (String[]) e.getValue();
		String value = "";
		for (int valIdx=0; valIdx<values.length; valIdx++) {
			value += values[valIdx].toString();
			if (valIdx != values.length) { value += ""; }
		}
		queryString.addParam(key, value);
	}
*/
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
			    throw new IllegalStateException("presidents_picks_layout.jsp: No UTF-8");
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
	if ((null !=promotionProducts && !promotionProducts.isEmpty())) {

		//make products unique on page (promo products should be coming back unique from SAP, but we need this for the BND rows)
		globalUniqueProducts.addAll(promotionProducts);

		%>
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
				$jq('.ddpp_feat_prod').corner('round 4px');
				$jq('#ddpp_feat_prod_cont').corner('round 4px');
				$jq('#sortSep').corner('round 4px');
				$jq('#ddpp_BNDgro_viewAll').corner('round 4px');
				$jq('#ddpp_BNDfro_viewAll').corner('round 4px');
				$jq('#ddpp_BNDdai_viewAll').corner('round 4px');

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
		</script>
		<style>
			.product-grid { width: 967px; }
			.items { width: 971px; }
			.grid-view .grid-item-container { width: 242px; }
		</style>
		<script>
		FD_QuickBuy.style = {
				closeButton:'quickbuy-noheader-close',
				header:'quickbuy-noheader'
		};
		</script>

		<div class="ddpp clearfix">
			<div class="PPHeader" id='oas_PPHeader'>
				<script type="text/javascript">
						OAS_AD('PPHeader');
				</script>
			</div>

			<div class="PPHeader2" id='oas_PPHeader'>
				<script type="text/javascript">
						OAS_AD('PPHeader2');
				</script>
			</div>
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
						<div class="PPSuperBuy" id='oas_PPSuperBuy'>
							<script type="text/javascript">
									OAS_AD('PPSuperBuy');
							</script>
						</div>
					</div>
				</div>
			</fd:FDShoppingCart>
		</div>
		<br style="clear:both" />
<!--[if lt IE 7]><div class="ie ie6"><![endif]-->
<!--[if IE 7]><div class="ie ie7"> <![endif]-->
<!--[if IE 8]><div class="ie ie8"> <![endif]-->
<!--[if gt IE 8]><!--><div><!--<![endif]-->

<%		QueryParameterCollection qc = QueryParameterCollection.decode(request.getQueryString());
		String uri = request.getRequestURI();
%>
	<fd:CmElement wrapIntoScriptTag="true" elementCategory="president_picks_sort" queryParamCollection="<%= qc %>" />

			<div class="toolbar container <%= qc.getParameterValue("view","grid")+"-view" %>">
				<div class="toolbar-content">
					<div id="sorter" class="span-10">
						<span class="label">Sort:</span>
						<display:SortBar defaultSort="ourFaves" sortItems="<%= new SearchSortType[] {SearchSortType.BY_OURFAVES,SearchSortType.BY_PRICE, SearchSortType.BY_DEPARTMENT} %>">
							<a href="<%= currentUrl %>" class="sortitem <%= isSelected ? "sortitem-selected" : ""%> <%= currentIndex==1 ? "nodot" : ""%>"><%= currentText%></a>
						</display:SortBar>
					</div>
					<% String viewswitcherClass = "prepend-10 span-4 last"; // class names for the #viewswitcher div %>
				</div>
			</div>
			<div class="product-grid <%= qc.getParameterValue("view","grid")+"-view" %>">
				<div class="items"><%
					for (Iterator<ProductModel> it=nonfeatProds.iterator() ; it.hasNext();) {
						pi = confStrat.configure(it.next(), confContext);
						%><div class="grid-item-container"><% if(disableLinks) { %><%@
							include file="/includes/product/i_product_box_preview.jspf" %><%
						} else { %><%@
							include file="/includes/product/i_product_box.jspf" %><%
						} %></div><%
					}
				%><div class="clear"></div>
				</div>
			</div>
		</div>
		<%
	}
%>

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
