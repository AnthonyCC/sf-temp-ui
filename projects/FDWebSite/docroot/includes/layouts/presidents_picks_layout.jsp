<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='java.net.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='java.io.UnsupportedEncodingException'%>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import="com.freshdirect.webapp.util.ConfigurationContext"%>
<%@ page import="com.freshdirect.webapp.util.ConfigurationStrategy"%>
<%@ page import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy"%>
<%@ page import="com.freshdirect.fdstore.customer.EnumQuickbuyStatus" %>
<%@ page import='com.freshdirect.cms.util.*' %>
<%@ page import="com.freshdirect.fdstore.content.util.QueryParameterCollection"%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<%

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
		<script type="text/javascript" src="/assets/javascript/rounded_corners.inc.js"></script>
		<%
		} else {
			// production JS libs
		%>
		<script type="text/javascript" src="/assets/javascript/rounded_corners-min.js"></script>
		<%
		}
%>
<display:InitLayout/>
<%

	//needed for transactional
	List<ProductImpression> impressions = new ArrayList<ProductImpression>();
	ProductImpression pi = null;

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
		if ("".equals(trkCode)) { sort = "ddpp"; }
		
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
	
	String ppPreviewId = request.getParameter("ppPreviewId");
	boolean isPpPreview = (null ==((com.freshdirect.fdstore.content.CategoryModel)currentFolder).getProductPromotionType()|| null==ppPreviewId)?false:true;
	
	if(!isPpPreview){
		promotionProducts = ((com.freshdirect.fdstore.content.CategoryModel)currentFolder).getProducts();
	}else{
		promotionProducts = ((com.freshdirect.fdstore.content.CategoryModel)currentFolder).getPromotionPageProductsForPreview(ppPreviewId);
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
	<fd:javascript src="/assets/yui-2.9.0/selector/selector-min.js" />
	<fd:javascript src="/assets/javascript/statusupdate.js" />
		<script type="text/javascript">

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
		
		<div class="ddpp">
			<div class="socialMedia">
				<iframe src="http://www.facebook.com/plugins/like.php?href=http%3A%2F%2Fwww.freshdirect.com%2F&amp;send=false&amp;layout=button_count&amp;width=150&amp;show_faces=false&amp;action=like&amp;colorscheme=light&amp;font=tahoma&amp;height=21" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width: 100px; height:21px;" allowTransparency="true"></iframe>
				<a href="http://twitter.com/share" class="twitter-share-button" data-count="horizontal">Tweet</a><script type="text/javascript" src="http://platform.twitter.com/widgets.js"></script>
			</div>
			
			<div class="PPHeader">
				<script type="text/javascript">
						OAS_AD('PPHeader');
				</script>
			</div>
			
			<div class="PPHeader2">
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
					<div class="ddpp_feat_prod_cont" id="ddpp_feat_prod_cont">
				
					<%
						int curLeftPos = 0;
						isFeatProd = true;
						String prodContStyle = "";
					%>
					
						<logic:iterate id="contentNode" collection="<%= featProds %>" type="java.lang.Object" indexId="idx">
							<%
								seqDDPP = idx; //use a seperate var for include
								ProductModelPromotionAdapter pm = (ProductModelPromotionAdapter) contentNode;
								String actionURI = FDURLUtil.getProductURI( pm, trkCode );
															
								pi = confStrat.configure((ProductModel) pm, confContext);
								impressions.add(pi);
		
								prodContStyle = "left: "+curLeftPos+"px;";
							%>
							<%@ include file="/includes/product/i_product_box_ddpp.jspf" %>
							<% 
								/* break out if we have more than three products in the featured setup */
								if (seqDDPP == 3) { break; }
								curLeftPos = curLeftPos + 255; /* take from css, cont width + 14 (gutter) */
							%>
						</logic:iterate>
						<div class="PPSuperBuy">
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
			<div class="toolbar container <%= qc.getParameterValue("view","grid")+"-view" %>">
				<div class="toolbar-content">
					<div id="sorter" class="span-10">
						<span class="label">Sort:</span>
						<display:SortBar defaultSort="ourFaves" sortItems="<%= new SearchSortType[] {SearchSortType.BY_OURFAVES,SearchSortType.BY_PRICE, SearchSortType.BY_DEPARTMENT} %>">
							<a href="<%= currentUrl %>" class="sortitem <%= isSelected ? "sortitem-selected" : ""%> <%= currentIndex==1 ? "nodot" : ""%>"><%= currentText%></a>
						</display:SortBar>					
					</div>
					<% QueryParameterCollection qvc = QueryParameterCollection.decode(request.getQueryString()); %>				
					<div id="viewswitcher" class="prepend-10 span-4 last">Views:<% qvc.setParameterValue("view","grid"); %><a id="viewswitcher-grid" href="<%=uri %>?<%= qvc.getEncoded() %>" class="viewswitcher-sprite"></a><% qvc.setParameterValue("view","list"); %><a id="viewswitcher-list" href="<%=uri %>?<%= qvc.getEncoded() %>" class="viewswitcher-sprite"></a></div>
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
			<div class="PPLeftBottom">
				<script type="text/javascript">
						OAS_AD('PPLeftBottom');
				</script>
			</div>
		</td>
		<td align="center" class="ddppBotAdsSep">
			<!-- sep -->
		</td>
		<td align="center">
			<div class="PPMidBottom">
				<script type="text/javascript">
						OAS_AD('PPMidBottom');
				</script>
			</div>
		</td>
		<td align="center" class="ddppBotAdsSep">
			<!-- sep -->
		</td>
		<td align="center">
			<div class="PPRightBottom">
				<script type="text/javascript">
						OAS_AD('PPRightBottom');
				</script>
			</div>
		</td>
	</tr>
</table>
