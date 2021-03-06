<%@ page import="java.util.*"%>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_ROBINHOOD_LANDING_TOTAL = 970;
%>


<%@page import="com.freshdirect.framework.util.DateUtil"%><fd:CheckLoginStatus />
<tmpl:insert template='/common/template/robinhood.jsp'>
<%-- 	<tmpl:put name='title' direct='true'>FreshDirect - Donation</tmpl:put> --%>
 <tmpl:put name="seoMetaTag" direct="true">
    <fd:SEOMetaTag title="FreshDirect - Donation"></fd:SEOMetaTag>
  </tmpl:put>
	
	<tmpl:put name='content' direct='true'>

		<%
			String action_name ="checkout";
			
			FDProductInfo productInfo = FDCachedFactory.getProductInfo(FDStoreProperties.getRobinHoodSkucode());
			ProductModel productModel = ContentFactory.getInstance().getProduct(FDStoreProperties.getRobinHoodSkucode());
			/*
				check robin hood status to show appropriate messaging
					"BUY" which is available to buy (DEFAULT)
					"OOS" is out of stock
					"OUT" is sold out
			*/
			String rhStatus = FDStoreProperties.getRobinHoodStatus();
			String salesOrg=ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo().getSalesOrg();
			String distrChannel=ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo().getDistributionChanel();
			EnumAvailabilityStatus availabilityStatus = productInfo.getAvailabilityStatus(salesOrg,distrChannel);
			double availableQty = UserUtil.getRobinHoodAvailability(DateUtil.truncate(new Date()),productInfo);
			FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
			FDCartModel cart = user.getDonationCart();
			
			FDCartLineI cartLine = null;
			if(!cart.isEmpty()){
			cartLine = cart.getOrderLine(0);
			}
		%>

		<table border="0" cellspacing="0" cellpadding="0" width="<%=W_ROBINHOOD_LANDING_TOTAL%>">
			<tr>
				<td align="center">
					<img src="/media_stat/images/donation/robinhood/landing/robin_hood_logo_lg.gif" height="59" width="339" alt="Robin Hood" />
				</td>
			</tr>

		<%
			//if ("OUT".equalsIgnoreCase(rhStatus)) {
				if (EnumAvailabilityStatus.DISCONTINUED.equals(availabilityStatus)) {
				
				//show soldout page
		%>
			<tr>
				<td align="center"><fd:IncludeMedia name="/media/editorial/robin_hood/landing_sold_out.html" /></td>
			</tr>
		<%
			//} else if ("OOS".equalsIgnoreCase(rhStatus)) {
				} else if (EnumAvailabilityStatus.TEMP_UNAV.equals(availabilityStatus) || availableQty <=0) {
				//show out of stock page
		%>
			<tr>
				<td align="center"><fd:IncludeMedia name="/media/editorial/robin_hood/landing_out_of_stock.html" /></td>
			</tr>
		<%
			} else {
				// show buyable page
		%>
				<tr>
					<td align="center"><fd:IncludeMedia name="/media/editorial/robin_hood/landing_header.html" /></td>
				</tr>
				<tr>
					<td align="center">	
						<fd:RobinHoodController actionName='<%=action_name%>' resultName='result' successPage='/robin_hood/rh_submit_order.jsp'>
							<script language="javascript">
								function chgQty(delta) {
									qty = parseFloat(document.rh_form.quantity.value) + delta;
									if (isNaN(qty) || qty < 1) {
										qty = 1;
									} 
									qty = Math.floor( (qty-1.0)/1.0 )*1.0  + 1.0;
									document.rh_form.quantity.value = qty;
									document.rh_form.total_price.value = formatCurrency(qty * <%=productInfo.getZonePriceInfo(user.getPricingContext().getZoneInfo()).getDefaultPrice()%>);
								}
							</script>
						<table border="0" cellspacing="0" cellpadding="2" width="<%=W_ROBINHOOD_LANDING_TOTAL-190%>">
							<tr><td colspan="2" class="text11bold">How many meals will you donate?</td></tr>
							<tr><td colspan="2"><img src="/media_stat/images/layout/333333.gif" width="100%" height="1" border="0" vspace="10"></td></tr>
							<tr>
								<td align="left">
									<table cellpadding="0" cellspacing="0" border="0">
										<tr>
											<td class="text11">
												<form name="rh_form" id="rh_form" method="post">
													<input type="hidden" id="rhCost" name="rhCost" value="75">
													<% if(null !=cartLine && !cartLine.getOrderedQuantity().equals("0")){ %>
													<input type="text" class="text11" size="4" name="quantity" value="<%=cartLine.getOrderedQuantity()%>" onChange="javascript:chgQty(0);" onBlur="javascript:chgQty(0);"></td>
													<%} else{ %>
													<input type="text" class="text11" size="4" name="quantity" value="1" onChange="javascript:chgQty(0);" onBlur="javascript:chgQty(0);"></td>
													<%} %>
													
													

											<td width="12" align="right"><a href="javascript:chgQty(1.0);"><img src="/media_stat/images/layout/grn_arrow_up.gif" width="10" height="9" border="0" vspace="1" alt="Increase quantity"></a><br><a href="javascript:chgQty(-1.0);"><img src="/media_stat/images/layout/grn_arrow_down.gif" width="10" height="9" border="0" vspace="1" alt="Decrease quantity"></a></td>
											<td style="padding-left:4px;" class="text11bold"><a href="javascript:popup('/media/editorial/robin_hood/landing_about_meal.html','large')"><%=productModel.getFullName() %></a></td>
										</tr>
									</table>		
								</td>	
								<td>
									<table align="right">
									<tr>
										<td  align="right"  style="padding-left:4px;" class="text11bold"> <%= JspMethods.formatPrice(productInfo, user.getPricingContext()) %>	</td>
									</tr>
									</table>
								</td>
							</tr>
							<tr><td colspan="2"><img src="/media_stat/images/layout/333333.gif" width="100%" height="1" border="0" vspace="10"></td></tr>
							<tr>
								<td align="right" class="text11bold">Total Price:</td>
								<% if(null !=cartLine && !cartLine.getOrderedQuantity().equals("0")){ %>
								<td align="right"><input type="text" id="total_price" name="total_price" class="text11" size="8" value="<%= JspMethods.formatPrice(cartLine.getFixedPrice()) %>"/></td>
								<%} else{ %>
								<td align="right"><input type="text" id="total_price" name="total_price" class="text11" size="8" value="<%= JspMethods.formatDefaultPrice(productInfo, user.getPricingContext()) %>"/></td>
								<%} %>
								
							</tr>
							<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /></td></tr>
							<tr>
								<td align="right" colspan="2"><a href="">
									<input type="image" src="/media_stat/images/donation/robinhood/landing/btn_continue.gif" width="98" height="34" alt="Continue" name="rhLand_continue" id="rhLand_continue" border="0" onclick="$('rh_form').submit();return false;" />
									</form>
								</td>
							</tr>
						</table>
						</fd:RobinHoodController>
					</td>
				</tr>
				<tr>
					<td align="center"><fd:IncludeMedia name="/media/editorial/robin_hood/landing_footer.html" /></td>
				</tr>
		<%
			}
		%>
			<tr>
				<td align="center"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /></td>
			</tr>
			<tr>
				<td align="center"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /></td>
			</tr>

		</table>

	</tmpl:put>
</tmpl:insert>