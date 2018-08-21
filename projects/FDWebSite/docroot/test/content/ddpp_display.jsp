<%@ page import="java.util.*"%>
<%@ page import='org.apache.log4j.Category' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings' %>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import='com.freshdirect.webapp.util.prodconf.SmartStoreConfigurationStrategy' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<fd:CheckLoginStatus guestAllowed="true" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US">
<head>
	<title>DDPP Test Page (Display)</title>

	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<script type="text/javascript" src="/assets/javascript/quickbuy.js"></script>
	<script type="text/javascript" src="/assets/javascript/pricing.js"></script>
	<style>
		.ddpp .qbLaunchButton {
			height: auto;
			width: auto;
		}
	</style>
</head>
<body class="ddpp">
<%@ include file="/shared/template/includes/yui.jspf" %>
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	String catId=NVL.apply(request.getParameter("catId"), ""); //category id
	ContentNodeModel currentFolder = null;
		if (!"".equals(catId)) { currentFolder = ContentFactory.getInstance().getContentNode(catId); }
	Collection sortedColl = null;
	List tmpList=new ArrayList();
	Settings layoutSettings = new Settings();
	String successPage = request.getRequestURI()+(request.getQueryString() == null ? "" : "?" + request.getQueryString());
	
	//needed for transactional
	List impressions = new ArrayList();
	ProductImpression pi = null;

	ConfigurationContext confContext = new ConfigurationContext();
	confContext.setFDUser(user);
	ConfigurationStrategy cUtil = SmartStoreConfigurationStrategy.getInstance();

	// added rowId here to keep these unique in case the same cat is used again
	String TX_FORM_NAME        = catId+"_form"; // impression form name
	String TX_JS_NAMESPACE     = catId+"_JSnamespace"; // impression javascript namespace
	
	int transProdIndex = 0;
	int totalProds = 0;

	if (currentFolder != null) {
		%>
		<fd:ItemGrabber
			category='<%=currentFolder %>' 
			id='rtnColl' 
			depth='<%=layoutSettings.getGrabberDepth()%>'
			ignoreShowChildren='<%=layoutSettings.isIgnoreShowChildren()%>' 
			filterDiscontinued='true'
			returnHiddenFolders='<%=layoutSettings.isReturnHiddenFolders()%>'
			ignoreDuplicateProducts='<%=layoutSettings.isIgnoreDuplicateProducts()%>'
			returnSecondaryFolders='<%=layoutSettings.isReturnSecondaryFolders()%>' 
			returnSkus='<%=layoutSettings.isReturnSkus()%>'
			workSet='<%=tmpList%>'
		>
			<%
				sortedColl = rtnColl;

				request.setAttribute("itemGrabberResult", sortedColl); //** expose result of item grabber to the layout **

			%>
		</fd:ItemGrabber>

		<%
			int perRowIdx = 1;
		%>
		<fd:FDShoppingCart id='cart' action='addMultipleToCart' result='result' successPage='<%= successPage %>' source='<%= request.getParameter("fdsc.source")%>'>
			<% //START error messaging %>
			<fd:ErrorHandler result='<%=result%>' name='quantity' id='errorMsg'>
				<img src="/media_stat/images/layout/clear.gif" width="20" height="12" alt="" border="0" />
				<%@ include file="/includes/i_error_messages.jspf" %>
			</fd:ErrorHandler>
			<% //END error messaging %>
			
			<form class="<%= TX_FORM_NAME %>" name="<%= TX_FORM_NAME %>" method="post" action="<%= successPage %>">
			
				<logic:iterate id="contentNode" collection="<%= sortedColl %>" type="java.lang.Object" indexId="idx">
					<%
						if (!(contentNode instanceof CategoryModel)) {

							
							ProductModel pm = (ProductModel) contentNode;
							if (!(pm.isUnavailable())) { 
							String trkCode = "";
							String actionURI = FDURLUtil.getProductURI( pm, trkCode );
							Set hideBursts = new HashSet();
							//hideBursts.add(EnumBurstType.DEAL);
							hideBursts.add(EnumBurstType.YOUR_FAVE);
							hideBursts.add(EnumBurstType.NEW);
							hideBursts.add(EnumBurstType.BACK_IN_STOCK);
							hideBursts.add(EnumBurstType.GOING_OUT_OF_STOCK);
													
							pi = cUtil.configure((ProductModel) pm, confContext);
							impressions.add(pi);
		
							//test vars
							int featProdCount = 9;
							int perRow = 4;
		
							if (idx < featProdCount+1) {
								%>	
									<div style="float: left; width: 200px;">
										<div class="ddpp_feat_prod" style="<%= (idx >= 4 && (idx-3 % 5 == 0 || idx == 4)) ? "clear: left;" : "" %> float: left; width: 200px; border: 1px solid #ccc; text-align: center;">
											<div class="topText"><display:ProductPrice impression="<%= new ProductImpression(pm) %>" showDescription="false" showAboutPrice="false" showRegularPrice="false" grpDisplayType="FEAT" dataDriven="true" /></div>
											<display:ProductImage 
												product="<%= pm %>" 
												showRolloverImage="false" 
												action="<%= actionURI %>" 
												hideBursts="<%= hideBursts %>" 
												prodImageType="PROD_IMAGE_ZOOM" 
												enableQuickBuy="false"
												quickBuyImage="/media_stat/images/quickbuy/close.gif"  
												bindToContainerSize="h=198,w=198" 
												burstOptions="size=lg"
											/>
											<div class="prodRating"><display:ProductRating product="<%= pm %>" action="<%= actionURI %>" /></div>
											<div class="prodName"><display:ProductName product="<%= pm %>" action="<%= actionURI %>" truncAt="80" useEllipsis="true" /></div>
											
											<display:ProductPrice impression="<%= pi %>" showDescription="true" />
											
											<div class="prodAdd">
												<% if (pi.isTransactional()) { %>													
														<fd:TxProductControl txNumber="<%= transProdIndex %>" namespace="<%= TX_JS_NAMESPACE %>" impression="<%= (TransactionalProductImpression) pi %>"/>
													
														<input type="image" name="addSingleToCart_<%= transProdIndex %>" src="/media/images/buttons/add_to_cart_no_icon.gif" width="82" height="25" border="0" alt="ADD SELECTED TO CART" />
														<input type="hidden" style="width: 60px" name="total_<%= transProdIndex %>" value="" size="6" class="text11bold" onChange="" onFocus="blur()" />
														<input type="hidden" name="source" value="cart_selection_<%= transProdIndex %>" />
														<% transProdIndex++; %>
												<% }else{ %>
													<a href="#" onclick="FD_QuickBuy.showPanel('<%= pm.getDepartment() %>', '<%= pm.getParentNode() %>', '<%= pm %>')(); return false;"><img src="/media/images/buttons/customize_blue.gif" height="28" width="89" alt="customize" border="0" /></a>
												<% } %>
											</div>
											
										</div>
									</div>
								<%
							} else {
									
							%>
								<div class="ddpp_normal_prod" style="<%= (idx >= 4 && (idx-3 % 5 == 0 || idx == 4)) ? "clear: left;" : "" %> float: left; width: 200px; border: 1px solid #ccc;">
									<div class="topText"><display:ProductPrice impression="<%= new ProductImpression(pm) %>" showDescription="false" showAboutPrice="false" showRegularPrice="false" grpDisplayType="NONFEAT" dataDriven="true" /></div>
									<display:ProductImage 
										product="<%= pm %>" 
										showRolloverImage="false" 
										action="<%= actionURI %>" 
										hideBursts="<%= hideBursts %>" 
										prodImageType="PROD_IMAGE_DETAIL" 
										bindToContainerSize="h=198,w=198" 
										burstOptions="size=lg"
									/>
									<div class="prodRating"><display:ProductRating product="<%= pm %>" action="<%= actionURI %>" /></div>
									<div class="prodName"><display:ProductName product="<%= pm %>" action="<%= actionURI %>" /></div>
									<display:ProductPrice impression="<%= new ProductImpression(pm) %>" showDescription="true" />
									<div class="prodAdd">
										<% if (pi.isTransactional()) { %>													
												<fd:TxProductControl txNumber="<%= transProdIndex %>" namespace="<%= TX_JS_NAMESPACE %>" impression="<%= (TransactionalProductImpression) pi %>"/>
											
												<input type="image" name="addSingleToCart_<%= transProdIndex %>" src="/media/images/buttons/add_to_cart_no_icon.gif" width="82" height="25" border="0" alt="ADD SELECTED TO CART" />
												<input type="hidden" style="width: 60px" name="total_<%= transProdIndex %>" value="" size="6" class="text11bold" onChange="" onFocus="blur()" />
												<input type="hidden" name="source" value="cart_selection_<%= transProdIndex %>" />
												<% transProdIndex++; %>
										<% }else{ %>
											<a href="#" onclick="FD_QuickBuy.showPanel('<%= pm.getDepartment() %>', '<%= pm.getParentNode() %>', '<%= pm %>')(); return false;"><img src="/media/images/buttons/customize_blue.gif" height="28" width="89" alt="customize" border="0" /></a>
										<% } %>
									</div>
								</div>
								<%
							}
		
							
							if (perRow == perRowIdx) {
								%><br style="clear:both" /><%
								perRowIdx = 0;
							}
							if (idx == featProdCount) {
								%><br style="clear:both" /><hr />above: "featured", below: "normal"<hr /><%
								perRowIdx = 0;
							}
							perRowIdx++;
						} }
						totalProds = idx;
					%>
				</logic:iterate>
				<input type="hidden" name="itemCount" value="<%= totalProds+1 %>" />
					
				</form>
			<fd:TxProductPricingSupport formName="<%= TX_FORM_NAME %>" namespace="<%= TX_JS_NAMESPACE %>" customer="<%= user %>" impressions="<%= impressions %>"/>
				
		</fd:FDShoppingCart>
	<% } else { %>
		No catId query param found, or invalid catId.
	<% } %>
</body>
</html>