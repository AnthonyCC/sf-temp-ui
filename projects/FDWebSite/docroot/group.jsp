<%@ page import='com.freshdirect.common.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.erp.ErpFactory' %>
<%@ page import='com.freshdirect.erp.model.ErpProductInfoModel '%>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.FDSkuNotFoundException' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.framework.util.NVL' %>
<%@ page import='com.freshdirect.framework.util.log.LoggerFactory' %>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.ProductImpression' %>
<%@ page import='java.net.URLEncoder' %>
<%@ page import='java.util.*' %>
<%@ page import='org.apache.log4j.Category' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%@page import="com.freshdirect.common.pricing.MaterialPrice"%>

<%!
private static Category  LOGGER = LoggerFactory.getInstance("group.jsp");
%>

<%@page import="com.freshdirect.webapp.util.prodconf.ProductConfigurationStrategy"%><fd:CheckLoginStatus guestAllowed='true' />
<%

	String templatePath = "/common/template/both_dnav_manual_left.jsp"; //the default
	String trkCode= NVL.apply(request.getParameter("trk"), "trkCode");
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	StringBuffer leftNavBuf = new StringBuffer(10000);
	String grpId=NVL.apply(request.getParameter("grpId"), "");
	String version=NVL.apply(request.getParameter("version"), "");
	/*  general */
	String catId=NVL.apply(request.getParameter("catId"), ""); //category id
		request.setAttribute("catId", catId);
	String deptId=NVL.apply(request.getParameter("deptId"), ""); //department id (not used)
		request.setAttribute("deptId", deptId);
	int displayedRows = 1;
	List impressions = new ArrayList();

%>	
	<fd:GetGSProducts skuModelList="skuModelList" productList="pmList" groupId='<%= grpId %>' version='<%= version %>'>
	<%
		boolean showNewText     = true; //show NEW! on new products (text)
		boolean showDescrips    = true; //set by "descrips" sub attribute
		int piCnIdx = 0;
		ContentNodeModel currentFolder;
		String topCatLink="";

		List<String> linksList = new ArrayList<String>();
		Boolean allowDuplicateLinks = false; //show duplicate links in left nav (multi products under the same category)
		
		currentFolder = null;
		if (!"".equals(catId)) {
			currentFolder = ContentFactory.getInstance().getContentNode(catId);
		}else{
			LOGGER.debug("no catId from URI");
			//we don't have a catId passed from the URI, try getting it from the first product in group
			if (pmList != null && pmList.size() > 0) {
				ProductModel pmTemp = (ProductModel)pmList.get(0);
				//put into request attribute
				request.setAttribute("defaultPM", pmTemp);
				LOGGER.debug("pmList.get(0) "+pmTemp);
				if (pmTemp != null && pmTemp.getParentNode() instanceof CategoryModel) {
					catId = pmTemp.getParentNode().toString();
					LOGGER.debug("catId "+catId);
				}
				
				if (!"".equals(catId)) {
					currentFolder = ContentFactory.getInstance().getContentNode(catId);
					request.setAttribute("catId", catId);
				}
			}
		}
		if (currentFolder != null) {
			request.setAttribute("currentFolder", currentFolder);
		}else{
			LOGGER.debug("currentFolder is null");
		}

		boolean prodsShowParentLinks = true; //instead of products, show their parent
		ContentNodeModel contentRef = null;
		ContentNodeModel contentRefPmCheck = null;
		CategoryModel catRef = null;
		CategoryModel topCategory = null;

		String showInContextOf  = null;
		
		/* Transactional stuff */

			ProductImpression pi = null;
			int nConfProd = 0;
			//ConfigurationContext confContext = new ConfigurationContext();
			//confContext.setFDUser(user);
			//ConfigurationStrategy cUtil = new ProductConfigurationStrategy();
			String successPage = "/grocery_cart_confirm.jsp?catId="+catId; //successPage when adding to cart

			String base_url = request.getParameter("base_url");
			if (base_url == null) {
				base_url = request.getRequestURI()+(request.getQueryString() == null ? "" : "?" + request.getQueryString());
			}
			int txCount = 0;

			if (currentFolder==null) {
			LOGGER.debug("Error, currentFolder is null! currentFolder:"+currentFolder);
			return;
		}

		request.setAttribute("smList", skuModelList); //** expose result of group to the layout **
	%>
	<logic:iterate id="sku" collection="<%= skuModelList %>" type="com.freshdirect.fdstore.content.SkuModel" indexId="idx">
	<%
		LOGGER.debug("Left Nav Index "+idx);
		/*
		 *	This will create the left nav for rec, cat, dept, or prods.
		 */
		//contentRef = ContentFactory.getInstance().getContentNode(contentNode.toString());
		//if (contentRef instanceof SkuModel) {
			//SkuModel skuRef = (SkuModel)contentNode;
			//contentRef = skuRef.getProductModel();
		//}
		contentRef = sku.getProductModel();
		contentRefPmCheck = ContentFactory.getInstance().getContentNode(contentRef.toString());
		if (contentRef instanceof ProductModel && prodsShowParentLinks) {
			contentRef = contentRef.getParentNode();
		}
		if (contentRef instanceof CategoryModel) {
			//if no catId is set, use this first ref
			if ("".equals(catId)) {
				catId = contentRef.toString();
			}
			catRef = (CategoryModel)contentRef;
			ContentNodeModel aliasNode = catRef.getAlias();

			topCategory = catRef.getTopCategory();

			if (aliasNode !=null ) {
				if (aliasNode instanceof ProductModel) {
					topCatLink="/product.jsp?catId="+aliasNode.getParentNode()+"&productId="+aliasNode;
				} else if(aliasNode instanceof CategoryModel) {
					topCatLink="/category.jsp?catId="+aliasNode;
				} else if(aliasNode instanceof DepartmentModel){
					topCatLink="/department.jsp?deptId="+aliasNode;
				}
			} else {
				topCatLink = "/category.jsp?catId="+contentRef;
			}
		}else{
			if (contentRef instanceof ProductModel) {
				topCatLink="/product.jsp?catId="+contentRef.getParentNode()+"&productId="+contentRef;
			} else if(contentRef instanceof CategoryModel) {
				topCatLink="/category.jsp?catId="+contentRef;
			} else if(contentRef instanceof DepartmentModel){
				topCatLink="/department.jsp?deptId="+contentRef;
			} else if(contentRef instanceof RecipeCategory){
				topCatLink="/recipe_cat.jsp?catId="+contentRef;
			} else if(contentRef instanceof RecipeSubcategory){
				topCatLink="/recipe_subcat.jsp?catId="+contentRef.getParentNode()+"&subCatId="+contentRef;
			}else{
				topCatLink="/category.jsp?catId="+contentRef;
			}
		}

		/* Transactional */
		if (contentRefPmCheck instanceof ProductModel) {
			ProductModel productModel = (ProductModel) contentRefPmCheck;
			//pi = cUtil.configure((ProductModel) pm, confContext);
			if (productModel.isAutoconfigurable()) {
				pi =  new TransactionalProductImpression(
					productModel,
					sku.getSkuCode(),
					productModel.getAutoconfiguration());
			} else {
				pi = new ProductImpression(productModel);
			}			
			impressions.add(pi);
			
			if (pi.isTransactional()) {
				++nConfProd;
			}
		}
		topCatLink += "&trk="+trkCode;

		if (topCategory != null && displayedRows == 1) {
			Image catImage = topCategory.getCategoryTitle();
			if (catImage!=null) {
				leftNavBuf.append("<tr><td><a href=\"");
				leftNavBuf.append("/category.jsp?catId="+topCategory);
				leftNavBuf.append("&trk=snav\">");

				leftNavBuf.append("<img src=\"").append(catImage.getPath());
				leftNavBuf.append("\" width=\"").append(catImage.getWidth());
				leftNavBuf.append("\" height=\"").append(catImage.getHeight());
				leftNavBuf.append("\" border=\"0\" alt=\"\" />");
				leftNavBuf.append("</a><br>&nbsp;</td></tr>");

				displayedRows++;
			}
		}
		String navText = (contentRef.getNavName()!=null)?contentRef.getNavName():contentRef.getFullName();
		
		if (!linksList.contains(navText)) {
			if (!allowDuplicateLinks) {
				linksList.add(navText);
			}

			leftNavBuf.append("<tr><td width=\"120\"><div style=\"margin-left:");
			leftNavBuf.append(9);
			leftNavBuf.append("px; text-indent: -8px;\">");
				leftNavBuf.append("<b>");
					leftNavBuf.append("<a href=\"");
					leftNavBuf.append(topCatLink);
					leftNavBuf.append("\"");
					leftNavBuf.append(">");
					leftNavBuf.append(navText);
					leftNavBuf.append("</a>");
				leftNavBuf.append("</b>");
			leftNavBuf.append("</td></tr>");
		}

		displayedRows++;
	%>
	</logic:iterate>
</fd:GetGSProducts>	
	<%
	String TX_FORM_NAME        = "groupScale_form"; // impression form name
	String TX_JS_NAMESPACE     = "groupScale_JSnamespace"; // impression javascript namespace
	 %>
<tmpl:insert template='<%= templatePath %>'>
	<tmpl:put name='title' direct='true'>FreshDirect - Group Scale Pricing</tmpl:put>
	<tmpl:put name='left_nav_manual' direct='true'>
		<table border="0" cellspacing="0" cellpadding="0" align="center" width="125">
			<tr>
				<td width="1" rowspan="<%= displayedRows %>"><%-- spacer 1x1 --%></td>
				<td width="120"><%-- spacer 120x1 --%></td>
				<td width="4" rowspan="<%= displayedRows %>"><%-- spacer 4x1 --%></td>
			</tr>
			<%= leftNavBuf.toString() %>
		</table><br>
		<%-- spacer 130x20 --%>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<%-- PRICING PART --%>
			<%-- javascript required for transactional --%>
			<script type="text/javascript" src="/assets/javascript/pricing.js"></script>
			<script type="text/javascript">
				<%-- javascript required for "grocery" behavior --%>
				function selectProduct(locationURL,qtyFldName) {
					if (locationURL==null) return false;
					if (qtyFldName!=null) {
						var qty;
						if (qty>=1) {
							(document.<%= TX_FORM_NAME %>[qtyFldName])
								?qty = parseFloat(document.<%= TX_FORM_NAME %>[qtyFldName].value)
								:qty=0;
							if (isNaN(qty)) qty=0;
							locationURL=locationURL+"&typedQuantity="+qty;
						}
					}
					window.location=locationURL;
				}
			</script>
		<fd:TxProductPricingSupport formName="<%= TX_FORM_NAME %>" namespace="<%= TX_JS_NAMESPACE %>" customer="<%= user %>" impressions="<%= impressions %>"/>
			<script type="text/javascript">
				<%-- turn on "group scale" behavior --%>
				<%= TX_JS_NAMESPACE %>.useGroupScalePricing = true;
			</script>
		<% request.setAttribute("TX_FORM_NAME", TX_FORM_NAME); %>
		<% request.setAttribute("TX_JS_NAMESPACE", TX_JS_NAMESPACE); %>
		<% request.setAttribute("impressions", impressions); %>
		
		<fd:FDShoppingCart id='cart' action='addMultipleToCart' result='result' successPage='<%= "/grocery_cart_confirm.jsp?catId="+catId %>' source='<%= request.getParameter("fdsc.source")%>'>
			<%
				{
					// there are errors..Display them
					Collection myErrs=((ActionResult)result).getErrors();
					%>
					<table border="0" cellspacing="0" cellpadding="0" width="425">
					<%
						for (Iterator errItr = myErrs.iterator();errItr.hasNext(); ) {
							String errDesc = ((ActionError)errItr.next()).getDescription();
						%>
							<tr valign="top">
								<td width="350" valign="middle">
									<div id="error_descriptions">   <FONT class="text12bold" color="#CC3300"><%=errDesc%></FONT></div>
								</td>
							</tr>
						<%
						}
						%>
					</table>
					<%
				}
			%>
			<jsp:include page="/includes/layouts/groupScale.jsp" flush="false"/>
		</fd:FDShoppingCart>
	</tmpl:put>
</tmpl:insert>