<%@ page import='java.util.*'
%><%@ page import='com.freshdirect.fdstore.customer.*'
%><%@ page import="com.freshdirect.fdstore.*"
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.*'
%><%@ page import='com.freshdirect.fdstore.content.*'
%><%@ page import='com.freshdirect.fdstore.promotion.*'
%><%@ page import='com.freshdirect.cms.fdstore.FDContentTypes'
%><%@ page import="com.freshdirect.webapp.util.FDEventUtil"
%><%@ page import="com.freshdirect.smartstore.fdstore.Recommendations"
%><%@ page import="com.freshdirect.smartstore.fdstore.FDStoreRecommender"
%><%@ page import="com.freshdirect.smartstore.Trigger"
%><%@ page import="com.freshdirect.fdstore.util.EnumSiteFeature"
%><%@ page import="com.freshdirect.webapp.util.ConfigurationStrategyFactory"
%><%@ page import="com.freshdirect.webapp.util.ConfigurationStrategy"
%><%@ page import="com.freshdirect.webapp.util.ConfigurationContext"
%><%@ page import="com.freshdirect.webapp.util.ProductImpression"
%><%@ page import="com.freshdirect.webapp.util.TransactionalProductImpression"
%><%@ page import="com.freshdirect.webapp.util.FDURLUtil"
%><%@ page import="com.freshdirect.fdstore.content.Image"
%><%@ page import="java.net.URLEncoder"
%><%@ taglib uri='template' prefix='tmpl'
%><%@ taglib uri='logic' prefix='logic'
%><%@ taglib uri='freshdirect' prefix='fd'
%><tmpl:insert template='/common/template/no_shell.jsp'>
	<tmpl:put name='title' direct='true'>Welcome to FreshDirect</tmpl:put>
	<tmpl:put name='content' direct='true'>
<!-- pip test -->
<%
// SMARTSTORE - DYF Template
// -------------------------
//
// @author segabor
FDUserI user = (FDUserI) session.getAttribute("fd.user");

// boolean skipDYFCheck = (val instanceof Boolean && ((Boolean)val).booleanValue() );
boolean skipDYFCheck = true;

// Transactional
final java.text.DecimalFormat QUANTITY_FORMATTER = new java.text.DecimalFormat("0.##");
final java.text.NumberFormat CURRENCY_FORMATTER = java.text.NumberFormat.getCurrencyInstance(Locale.US);



// transactional pricing logic - 'template' parameters
String tx_pricing_FormName = "dyfform"; // impression form name
String tx_pricing_JSNameSpace = "DYF";

%><fd:DYFRecommendations id="recommendations" skipCheck="<%= skipDYFCheck %>"><%
	ConfigurationContext confContext = new ConfigurationContext();
	confContext.setErpCustomerId(user.getIdentity().getErpCustomerPK());

	if (recommendations != null && recommendations.getContentNodes().size() > 0) {
		ConfigurationStrategy cUtil = ConfigurationStrategyFactory.getConfigurationStrategy(recommendations.getVariant().getSiteFeature());
	
		List products = recommendations.getContentNodes();
		// Map impressions = new HashMap();
		List impressions = new ArrayList();
		
		// 'configure' products.
		for (Iterator it = products.iterator(); it.hasNext();) {
			ProductModel prd = (ProductModel) it.next();
			ProductImpression pi = cUtil.configure(prd, confContext);
			// impressions.put(prd.getContentKey(), pi);
			impressions.add(pi);
		}
%>
	<table style="width: 669px;">
	<fd:PIPLayout id="piRow" rowSize="3" impressions="<%= impressions %>" maxRowHeight="rowHeight">
		<tr>
		<fd:PIPRow id="pi" impressionRow="<%= piRow %>" isBlankCell="isBlankCell" productImage="prodImage">
<%
		if (isBlankCell.booleanValue()) {
			%><td>&nbsp;</td><%
		} else {
			ProductModel product = pi.getProductModel();

%>			<td class="txitem" style="width: 33%; text-align: center;" valign="top" >
				<!-- image -->
				<table align="center">
					<tr>
						<td style="padding: 0; height: <%= rowHeight.intValue() %>px; vertical-align: bottom; text-align: center">
							<a href="<%= FDURLUtil.getProductURI(product, recommendations.getVariant()) %>">
								<img src="<%= prodImage.getPath() %>" style="border: 0"/>
							</a>
						</td>
					</tr>
<%

			if (pi instanceof TransactionalProductImpression) {
				TransactionalProductImpression tpi = (TransactionalProductImpression) pi;
				
				if (product.isSoldBySalesUnits()) {
						// sales units editor
%>
					<tr>
						<td>
							<table align="center">
								<tr><td style="height: 28px">
									<select name="salesUnit_confProdCount" style="width: 60px" class="text10">
										<option value="" selected="selected"></option>
<%
					FDSalesUnit     salesUnits[] = tpi.getFDProduct().getSalesUnits();
                    for (int ii = 0; ii < salesUnits.length; ++ii) {
                        FDSalesUnit salesUnit      = salesUnits[ii];
                        String      salesUnitDescr = salesUnit.getDescription();

                        // clean parenthesis
                        int ppos = salesUnitDescr.indexOf("(");
                        if (ppos > -1) {
                            salesUnitDescr = salesUnitDescr.substring(0, ppos).trim();
                        }
%>
										<option value="<%= salesUnit.getName() %>"><%= salesUnitDescr %></option>
<%
	                    }
%>
									</select>
								</td></tr>
							</table>
						</td>
					</tr>
<%
					} else {
						// quantity editor
%>
					<tr>
						<td>
							<table align="center">
								<tr>
									<td style="height: 28px"><input type="text" class="text10" style="width: 36px" name="tx1" size="3" maxlength="4"/></td>
									<td style="height: 28px">
										<img src="/media_stat/images/template/quickshop/grn_arrow_up.gif" style="width: 10px; height: 9px; border: 0;  margin-bottom: 1px; margin-top: 1px"/><br/>
										<img src="/media_stat/images/template/quickshop/grn_arrow.gif" style="width: 10px; height: 9px; border: 0; margin-bottom: 1px; margin-top: 1px"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
<%
					}
				} else {
%>
					<tr>
						<td style="height: 28px; text-align: center; vertical-align: middle;">
							<span class="text8">(click name to buy)</span>
						</td>
					</tr>
<%
				} //!transactional
%>
				</table>
<%

				/*
				 * Product Name Section
				 */
				//
				// Display '<bold>product</bold>' OR '<bold>brand</bold>product'
				//
				String fullName = product.getFullName();
			    String brandName = product.getPrimaryBrandName();
			    String shortenedProductName = null;
	
			    if (brandName != null
					&& brandName.length() > 0
					&& (fullName.length() >= brandName.length())
					&& fullName.substring(0, brandName.length()).equalsIgnoreCase(brandName)) {
			
			        shortenedProductName = fullName.substring(brandName.length()).trim();
			    }
%>
				<!-- product name, etc -->
				<a href="<%= FDURLUtil.getProductURI(product, recommendations.getVariant()) %>">
					<font class="text11"><%
			if (shortenedProductName != null) {
				%><b><%= brandName %></b> <%= shortenedProductName %><%
			} else {
				%><b><%= fullName %></b><%
			} %>
					</font>
				</a><%

				if (product.getSizeDescription() != null) {
				%> (<%= product.getSizeDescription() %>)<%
			} %><br/>
<%
			// Display price
%><fd:FDProductInfo id="productInfo" skuCode="<%= pi.getSku().getSkuCode() %>">
				<b><%= CURRENCY_FORMATTER.format(productInfo.getDefaultPrice()) %>/<%= productInfo.getDisplayableDefaultPriceUnit().toLowerCase() %></b>
				<br/>
</fd:FDProductInfo>
<%				


			// Display "SAVE!" ... titles
            String[] ymalScales = pi.getFDProduct().getPricing().getScaleDisplay();
            if (ymalScales.length>0) {
%>				<span style="color: #FF9933; font-weight: bold;">Save!</span><br/>
<%
                for (int ymalSci = 0; ymalSci < ymalScales.length; ymalSci++) {
                	%><b><%= ymalScales[ymalSci] %></b><br/>
<%
                }
                
            }
%>
			</td>
<%
		} // !blank cell
%>
		</fd:PIPRow>
		</tr>
	</fd:PIPLayout>
	</table>
<%
	} // recommendations != null AND count of content nodes > 0
%>
</fd:DYFRecommendations>
</tmpl:put>
</tmpl:insert>