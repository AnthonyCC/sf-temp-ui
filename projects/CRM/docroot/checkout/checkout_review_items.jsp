<%@ page import="com.freshdirect.common.pricing.Discount" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.customer.ErpDiscountLineModel" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionReason" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.deliverypass.DeliveryPassUtil" %>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionType" %>
<%@ page import="com.freshdirect.fdstore.promotion.HeaderDiscountRule" %>
<%@ page import="com.freshdirect.fdstore.promotion.PromotionFactory" %>
<%@ page import="com.freshdirect.fdstore.promotion.PromotionI" %>
<%@ page import="com.freshdirect.fdstore.promotion.SignupDiscountRule" %>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ page import="com.freshdirect.framework.util.TimeOfDay" %>
<%@ page import="com.freshdirect.framework.webapp.ActionError" %>
<%@ page import="com.freshdirect.framework.webapp.ActionResult" %>
<%@ page import="com.freshdirect.webapp.taglib.callcenter.ComplaintUtil" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="com.freshdirect.webapp.util.RestrictionUtil" %>
<%@ page import='java.text.*, java.util.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Checkout > Review Items</tmpl:put>
<tmpl:put name='content' direct='true'>

	<%
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		FDIdentity identity = user.getIdentity();
	%>

	<jsp:include page='/includes/order_header.jsp'/>

	<%
		String redemptionCode = request.getParameter("redemptionCode");
		String giftCardCode = request.getParameter("giftCardCode");
		String actionName = "updateQuantities";
		if(request.getMethod().equalsIgnoreCase("POST") && (("nextPage".equals(request.getParameter("action1"))))){
			actionName="nextPage";
		}

		if ((request.getMethod().equalsIgnoreCase("POST") && request.getParameter("redemptionCodeSubmit.x") != null) || (redemptionCode != null && !"".equals(redemptionCode))) {
			actionName = "redeemCode";
		}
		String [] lineReason = request.getParameterValues("ol_credit_reason");

		
		List mg_reason = ComplaintUtil.getReasonsForDepartment("Makegood");
		boolean makegood = "true".equals(request.getParameter("makegood")) ? true : false;
		String referencedOrder = request.getParameter("orig_sale_id");

		//Reset the DCPD eligiblity map to re-calculate the promo if in case promotion was modified.
		user.getDCPDPromoProductCache().clear();
	%>	
	<fd:FDShoppingCart id='cart' result='result' action='<%= actionName%>'>
	<fd:RedemptionCodeController actionName="<%=actionName%>" result="redemptionResult">

	<%
		int minQuantity = 1; // will have to make this smarter at some point
		//String lastDept = null;
	%>

	<fd:ErrorHandler result="<%=redemptionResult%>" name="redemption_error" id="errorMsg">
		<%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%=result%>' name='pass_expired' id='errorMsg'>
		<br /><%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%=result%>' name='pass_cancelled' id='errorMsg'>
		<%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%=result%>' name='system' id='errorMsg'>
		<%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>

		<table width="90%" cellpadding="2" cellspacing="0" border="0" align="center" class="checkout_header<%= (user.isActive()) ? "" : "_warning" %>">
			<tr valign="top">
				<td width="80%">&nbsp;Review Items in Cart before proceeding<% if (!user.isActive()) { %>&nbsp;&nbsp;&nbsp;!!! Checkout prevented until account is <a href="<%= response.encodeURL("/customer_account/deactivate_account.jsp?successPage="+request.getRequestURI()) %>" class="new">REACTIVATED</a><% } %></td>
				<td align="right"><a href="javascript:nextPage()" class="checkout">CONTINUE CHECKOUT >></a></td>
			</tr>
		</table>

		<%@ include file="/includes/i_modifyorder.jspf" %>

		<div class="content_scroll" style="height: 72%;">

		<% /* Gift Card Additions */ %>
			<script type="text/javascript">
			<!--
				var shown = false;
				var waiting = false;

				function slideOut(e) {
					if (!waiting) {
						waiting = true;
						new Effect.Move(e, {
							x: 268, y: -165, mode: 'relative',
							transition: Effect.Transitions.sinoidal,
							duration: 0.75,
							afterFinish: function() { waiting = false; shown = true; }
						});
					}
				}
				function slideIn(e) {
					if (!waiting) {
						waiting = true;
						new Effect.Move(e, {
							x: -268, y: 165, mode: 'relative',
							transition: Effect.Transitions.sinoidal,
							duration: 0.5,
							afterFinish: function() { waiting = false; shown = false; }
						});
					}
				}

				function slider(e) {
					(shown)?slideIn(e):slideOut(e);
				}

			//-->
			</script>
			<div id="gc_CO_cont" class="gc_CO_cont">
				<div id="gc_CO_title" class="gc_CO_title">
					<a href="#" onclick="slider('gc_CO_usersCards'); this.blur(); return false;" class="gc_CO_titleLink">
						G<br />
						i<br />
						f<br />
						t<br />
						c<br />
						a<br />
						r<br />
						d<br />
						s
					</a>
				</div>
				<div id="gc_CO_usersCards" class="gc_CO_usersCards">
					
					<table class="gc_table1 bordBlack" cellspacing="0" cellpadding="0" width="100%">
						<tr>
							<th width="16px" align="center">&nbsp;</th>
							<th width="100px" align="right">Certificate #</th>
							<th width="70px" align="right">Balance</th>
						</tr>
					</table>

					<div class="gc_CO_usersCardsContainer">
						<input type="hidden" name="showAllReceived" id="showAllReceived" value="true">
						<fd:GetGiftCardReceived id='giftcards' showAll='true'>
							<% Integer availCardsCount = 0; %>
							<table class="gc_table1" cellspacing="0" cellpadding="0" width="100%" border="0">
							<logic:iterate id="giftcard" collection="<%= giftcards %>" type="com.freshdirect.fdstore.giftcard.FDGiftCardModel" indexId="gcRemCounter1">
								<%
									Integer counterTmp = (Integer) pageContext.getAttribute("gcRemCounter1");
									if (giftcard.isRedeemable() && giftcard.getBalance() > 0 ) {
								%>

									<tr valign="top" <%if(counterTmp.intValue()%2==0){%>class="list_odd_row"<%}%>>
										<td style="vertical-align: middle; text-align: center; width: 16px;">
											<div style="background-color: #fff; padding: 0px; margin: 0px; border: 0px;">

												<% if(!giftcard.isSelected()) { %>
													<a href="<%= request.getRequestURI() %>?action=applyGiftCard&certNum=<%= giftcard.getCertificateNumber() %>&value=true" class="note"><img width="16" height="16" alt="hide" src="/media_stat/crm/images/icon_minus.gif" id="m<%=counterTmp%>" class="gcIcon" style="display: block; border: 0px;" /></a>
												<% } else {%>
													<a href="<%= request.getRequestURI() %>?action=applyGiftCard&certNum=<%= giftcard.getCertificateNumber() %>&value=false" class="note"><img width="16" height="16" alt="show" src="/media_stat/crm/images/icon_plus.gif" id="p<%=counterTmp%>" style="display: block; border: 0px;" /></a>
												<% } %>

											</div>
										</td>
										<td align="right"><%= giftcard.getCertificateNumber() %></td>
										<td align="right" style="padding-right: 6px;">$<%= giftcard.getFormattedBalance() %></td>							
									</tr>
								
								<%
									availCardsCount++;
									} %>
							</logic:iterate>
								<% if (availCardsCount ==0) { %>
									<tr><td>No Gift Cards Available.</td></tr>
								<% } %>
							</table>
						</fd:GetGiftCardReceived>
					</div>
				</div>
			</div>
		<% /* Gift Card Additions */ %>

		<%--  using standard view  --%>
			<table width="90%" cellspacing="0" cellpadding="0" align="center" class="order" border="0">
			
			<form name="viewcart" method="post" style="margin: 0px; padding: 0px;">
				<input type="hidden" name="cartLineRemove" value="-1" />
				<input type="hidden" name="action" value="updateQuantities" />
				<input type="hidden" name="action1" value="" />

				<% if(makegood) { %>
					<input type="hidden" name="makeGoodOrder" value="true" />
					<input type="hidden" name="referencedOrder" value="<%=referencedOrder%>" />
				<% } %>

			<logic:iterate id="view" collection="<%= cart.getOrderViews() %>" type="com.freshdirect.fdstore.customer.WebOrderViewI">
				<tr>
					<td colspan="10">
						<% if (!"".equals(view.getDescription())) {	%>
							<br />
							<div class="orderViewHeader"><%= view.getDescription().toUpperCase() %></div>
						<% } %>
						<% 	//if (!view.isDisplayDepartment()) {
							// % ><br />< %
							//} %>
					</td>
				</tr>
				<%
					List orderLines = view.getOrderLines();
					String lastDept = null;
					String lastDeptImgName = "";
					boolean hadKosherRestriction = false;
					boolean hadPlatterRestriction = false;
					boolean firstRecipe = true;
					int j = 0;
				%>
				<logic:iterate id="cartLine" collection="<%= orderLines %>" type="com.freshdirect.fdstore.customer.FDCartLineI" indexId="viewIndex">
					<%
						int idx = cart.getOrderLineIndex(cartLine.getRandomId());
						
						ProductModel productNode = cartLine.lookupProduct();
						FDProduct fdProduct = cartLine.lookupFDProduct();
						
						FDCartLineI nextLine = viewIndex.intValue() + 1 == orderLines.size() ? null : (FDCartLineI)orderLines.get(viewIndex.intValue() + 1);
						boolean lastItemInDept = nextLine==null || !nextLine.getDepartmentDesc().equals(cartLine.getDepartmentDesc());
					
						if (lastDept==null || !lastDept.equalsIgnoreCase(cartLine.getDepartmentDesc())) {
					
							lastDept = cartLine.getDepartmentDesc() ;

							if (lastDept.startsWith("Recipe: ")) {
								if (firstRecipe) {
					%>
									<tr>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td colspan="3"></td>
										<td colspan="7" style="color: #f93;"><b>RECIPES</b></td>
									</tr>
								<%
									firstRecipe = false;
								}

								String recipeName = lastDept.substring("Recipe: ".length());
							%>
								<tr>
									<td colspan="3"></td>
									<td colspan="7" class="color: #f93;"><%=recipeName%></td>
								</tr>
						<% 	    
							} else {

								String deptName = ContentFactory.getInstance().getProductByName( cartLine.getCategoryName(), cartLine.getProductName() ).getDepartment().getContentName();

								//if (view.isDisplayDepartment()) {
							%>
								<tr>
									<td colspan="3"></td>
									<td colspan="7" style="color: #f93;"><a href="/department.jsp?deptId=<%=deptName%>&trk=cart"><b><%=lastDept.toUpperCase()%></b></a></td>
								</tr>
						<%
								//}
							}
						}

						if (cartLine.getApplicableRestrictions().contains(EnumDlvRestrictionReason.PLATTER)) {
							hadPlatterRestriction = true;
						}

						if (cartLine.getApplicableRestrictions().contains(EnumDlvRestrictionReason.KOSHER)) {
							hadKosherRestriction = true;
						}
					%>
		
						<tr valign="middle">
						<% if (cartLine.isSoldBySalesUnits()) {	%>
							<td colspan="3" width="78">
								<select name="salesUnit_<%=idx%>" class="text10">
									<option value="">0</option>
									
									<logic:iterate id="salesUnit" collection="<%= fdProduct.getSalesUnits() %>" type="com.freshdirect.fdstore.FDSalesUnit">
										<%
											String salesUnitDescr = salesUnit.getDescription();
											// clean parenthesis
											int ppos = salesUnitDescr.indexOf("(");
											if (ppos > -1) {
												salesUnitDescr = salesUnitDescr.substring(0, ppos).trim();
											}
										%>
										<option value="<%= salesUnit.getName() %>"<%= cartLine.getSalesUnit().equals( salesUnit.getName() ) ? " SELECTED" : ""%>><%= salesUnitDescr %></option>
									</logic:iterate>
								</select>
								<input name="rnd_<%=idx%>" type="hidden" value="<%= cartLine.getRandomId() %>" />
							</td>
						<% } else {	%>
							<td align="right">
								<script language="javascript" type="text/javascript">
									function chgQty<%=idx%>(delta) {
										val = document.viewcart.quantity_<%=idx%>.value;
										qty = parseFloat(val) + delta;
										
										if ((delta > 0) && (val == "")) {
											if (delta < <%= productNode.getQuantityMinimum() %>) {
												delta = <%= productNode.getQuantityMinimum() %>;
											}
										
											qty = delta;
										} else if (isNaN(qty) || (qty < 0)) {
											document.viewcart.quantity_<%=idx%>.value = "0";
											return;
										} else if ((qty > 0) && (qty < <%= productNode.getQuantityMinimum() %>) && (delta < 0)) {
											document.viewcart.quantity_<%=idx%>.value = "0";
											return;
										} else if ((qty > 0) && (qty < <%= productNode.getQuantityMinimum() %>) && (delta >= 0)) {
											qty = <%= productNode.getQuantityMinimum() %>;
										} else if (qty >= <%= user.getQuantityMaximum(productNode) %>) {
											qty = <%= user.getQuantityMaximum(productNode) %>;
										}
										
										qty = Math.floor( (qty-<%= productNode.getQuantityMinimum() %>)/<%=productNode.getQuantityIncrement()%> )*<%=productNode.getQuantityIncrement()%>  + <%= productNode.getQuantityMinimum() %>;
										
										document.viewcart.quantity_<%=idx%>.value = qty;
									}
								</script>
								
								<input name="quantity_<%=idx%>" type="text" size="5" MAXLENGTH="5" CLASS="text10" VALUE="<%= CCFormatter.formatQuantity(cartLine.getQuantity()) %>" onBlur="chgQty<%=idx%>(0);" />
								<input name="rnd_<%=idx%>" type="hidden" value="<%= cartLine.getRandomId() %>"/>
							</td>
							<td><a href="javascript:chgQty<%=idx%>(<%= productNode.getQuantityIncrement() %>);"><img src="/media_stat/images/layout/grn_arrow_up.gif" width="10" height="9" border="0" vspace="1" alt="Increase quantity"></a><br /><a href="javascript:chgQty<%=idx%>(<%= -productNode.getQuantityIncrement() %>);"><img src="/media_stat/images/layout/grn_arrow_down.gif" width="10" height="9" border="0" vspace="1" alt="Decrease quantity" /></a></td>
							<td>&nbsp;<%=cartLine.getLabel()%></td>
						<% }
							
							String earliestAvailability = productNode.getSku(cartLine.getSkuCode()).getEarliestAvailabilityMessage();
							boolean displayShortTermUnavailability = fdProduct.getMaterial().getBlockedDays().isEmpty();
							boolean platterDlvInfo = false;
							if (productNode.isPlatter()) {
								platterDlvInfo = true;
							}
						%>
			
							<td colspan="2"><div style="margin-left:16px; text-indent:-8px;"><span class="text10bold"><a href="/product_modify.jsp?cartLine=<%= cartLine.getRandomId() %>&trk=cart"><%= cartLine.getDescription() %></a></span>&nbsp;<%=!cartLine.getConfigurationDesc().equals("")?"("+cartLine.getConfigurationDesc()+")":""%>
							<%= fdProduct.getKosherInfo().isKosherProduction() ? " <span class=\"kosher\">**</span>":""  %>
							<%= cartLine.getApplicableRestrictions().contains(EnumDlvRestrictionReason.PLATTER) ? " <font color=\"#FF9933\">**</font>":""  %>
							<% if ((cart instanceof FDModifyCartModel) && !(cartLine instanceof FDModifyCartLineI)) { %><span class="text10rbold">(new)</span><% } %>
							<% if (displayShortTermUnavailability && earliestAvailability != null && !(cartLine instanceof FDModifyCartLineI)) { %><br />&nbsp;&nbsp;<span class="text10rbold">Earliest Delivery <%=earliestAvailability%></span><% }%></div>
							</td>
							<td align="right">(<%= cartLine.getUnitPrice() %>) </td>
							
							<% if (makegood) { %>
								<td align="right">
									<input type="hidden" name="orderlineId" value="<%= ((FDCartLineI)cartLine).getOrderLineId() %>" />
									<select name="ol_credit_reason" class="pulldown_detail">
										<option value="">select make good reason</option>
										<logic:iterate id="reason" collection="<%= mg_reason %>" type="com.freshdirect.customer.ErpComplaintReason">
											<option value="<%= reason.getId() %>" <%= ( lineReason != null && reason.getId().equals(lineReason[j]) ) ? "SELECTED" : "" %>><%= reason.getReason() %></option>
										</logic:iterate>
									</select>
								</td>
							<% } %>

							<td align="right"><span class="text10bold"><%= CCFormatter.formatCurrency(cartLine.getPrice()) %></span></td>
							<td><%= cartLine.isEstimatedPrice() ? "*" : "" %><strong><%=cartLine.hasTax() ? "&nbsp;T" : ""%></strong></td>
							<td><strong><%= cartLine.hasScaledPricing() ? "&nbsp;S" : "" %><%= cartLine.hasDepositValue() ? "&nbsp;D" : "" %></strong></td>

							<% if (!makegood){ %>
								<td align="right">&nbsp;<a href="<%= request.getRequestURI() %>?remove=1&cartLine=<%= cartLine.getRandomId() %>" class="note">Remove</a></td>
							<% } %>   
						</tr>
	
					<%
						if (lastItemInDept && hadPlatterRestriction) {
							TimeOfDay platterTime = RestrictionUtil.getPlatterRestrictionStartTime();
							
							if (platterTime != null) {
								String platterCutoffTime;
								
								if (new TimeOfDay("12:00 PM").equals(platterTime)) {
									platterCutoffTime = "12 Noon";
								} else {
									SimpleDateFormat tf = new SimpleDateFormat("ha");
									platterCutoffTime = tf.format(platterTime.getAsDate());	 
								}
					%>
								<tr>
									<td colspan="3">&nbsp;</td>
									<td colspan="2" style="padding-left:8px;">
										<img src="/media_stat/images/layout/ff9933.gif" width="412" height="1" vspace="3">
										<br /><font color="#FF9933">**</font>
										This item requires an advance cutoff time. You must <b>complete checkout by <%=platterCutoffTime%></b> to order this item for delivery tomorrow.
									</td>
									<td colspan="5">&nbsp;</td>
								</tr>
						<% 
							}
							
							hadPlatterRestriction = false;
						}
		
						if (lastItemInDept && hadKosherRestriction) {
					%>
							<tr>
								<td colspan="3">&nbsp;</td>
								<td colspan="2" style="padding-left: 8px;">
									<img src="/media_stat/images/layout/6699cc.gif" width="412" height="1" vspace="3">
									<br /><span class="kosher">**</span> Not available for delivery on Friday, Saturday, or Sunday morning.
									<fd:GetDlvRestrictions id="kosherRestrictions" reason="<%=EnumDlvRestrictionReason.KOSHER%>" withinHorizon="true">
									<% if (kosherRestrictions.size() > 0) { %>Also unavailable during
										<logic:iterate indexId='i' collection="<%= kosherRestrictions %>" id="restriction" type="com.freshdirect.delivery.restriction.RestrictionI">
										<b><%=restriction.getName()%></b>, <%=restriction.getDisplayDate()%><% if (i.intValue() < kosherRestrictions.size() -1) {%>; <% } else { %>.<% } %>
										</logic:iterate>
									<% } %>
									</fd:GetDlvRestrictions>
									<a href="javascript:popup('/shared/departments/kosher/delivery_info.jsp','small')">Learn More</a>

								</td>
								<td colspan="5">&nbsp;</td>
							</tr>
					<% 
							hadKosherRestriction = false;
						}
					%>
					
					<% j++; %>
				</logic:iterate>

				<tr>
					<td colspan="10">
						<div class="orderViewSeparator" />
					</td>
				</tr>
				<tr class="orderViewSummary">
					<td colspan="6" align="right"><%= view.getDescription() %><% if (view.isEstimatedPrice()) { %> Estimated<% } %> Subtotal:</td>
					<td colspan="1" align="right"><b><%=CCFormatter.formatCurrency(view.getSubtotal())%></b></td>
					<td colspan="1"><% if (view.isEstimatedPrice()) { %>*<% } %></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<%
					if (view.getTax() > 0) {
						%>
						<tr class="orderViewSummary">
							<td colspan="6" align="right">Tax:</td>
							<td colspan="1" align="right"><%=CCFormatter.formatCurrency(view.getTax())%></td>
							<td colspan="3">&nbsp;</td>
						</tr>
						<%
					}

					if (view.getDepositValue() != 0) {
						%>
						<tr class="orderViewSummary">
							<td colspan="6" align="right">Bottle Deposit:</td>
							<td colspan="1" align="right"><%=CCFormatter.formatCurrency(view.getDepositValue())%></td>
							<td colspan="3">&nbsp;</td>
						</tr>
						<% 
					}
				%>
		
			</logic:iterate>

			<tr>
				<td colspan="10">
					<span class="orderSeparator" />
				</td>
			</tr>
			
			<%if (!makegood) { %>	
				<tr>
					<td valign="top" colspan="3" rowspan="18" class="note">
						<input type="image" name="update_quantities" src="/media_stat/images/buttons/update_quantities.gif" width="113" height="16" border="0" alt="UPDATE QUANTITIES" VSPACE="2" HSPACE="0" /><br />
						After changing a<br />
						quantity, click to<br />
						update prices.
					</td>
					<td colspan="7"></td>
				</tr>
			<% } else { %>
				<tr>
					<td valign="top" colspan="3" rowspan="18" class="note"><!--  --></td>
					<td colspan="7"><!--  --></td>
				</tr>
			<% } %>

			<tr valign="top" class="orderSummary">
				<td colspan="3" align="right">Order Subtotal:</td>
				<td colspan="1" align="right"><%=CCFormatter.formatCurrency(cart.getSubTotal())%></td>
				<td><%= cart.isEstimatedPrice() ? "*":"" %></td>
				<td colspan="2">&nbsp;</td>
			</tr>

			<%
				if (cart.getTaxValue() > 0) {
			%>
					<tr valign="top" class="orderSummary">
						<td colspan="3" align="right">Total Tax:</td>
						<td colspan="1" align="right"><%=CCFormatter.formatCurrency(cart.getTaxValue())%></td>
						<td colspan="3"></td>
					</tr>
			<%
				}

				if (cart.getDepositValue() > 0) {
			%>
					<tr valign="top" class="orderSummary">
						<td colspan="3" align="right">Bottle Deposit:</td>
						<td colspan="1" align="right"><%=CCFormatter.formatCurrency(cart.getDepositValue())%></td>
						<td colspan="3"></td>
					</tr>
			<%
				}
	
				if (cart.isDlvPassApplied()) {
			%>
					<tr valign="top" class="orderSummary">
						<td colspan="3" align="right">Delivery Charge:</td>
						<td colspan="1" align="right"><%= DeliveryPassUtil.getDlvPassAppliedMessage(user) %></td>
						<td colspan="3">&nbsp;</td>
					</tr>
			<%
				} else if (cart.getDeliverySurcharge() > 0) {
			%>
					<tr valign="top" class="orderSummary">
						<td colspan="3" align="right">Delivery Charge<%if(cart.isDeliveryChargeWaived()){%> (waived)<%}%>:</td>
						<td colspan="1" align="right"><%if(cart.isDeliveryChargeWaived()){%>$0.00<%}else{%><%=CCFormatter.formatCurrency(cart.getDeliverySurcharge())%><%}%></td>
						<td><%= cart.isDeliveryChargeTaxable() && !cart.isDeliveryChargeWaived() ? "&nbsp;<strong>T</strong>":""%></td>
						<td colspan="2"></td>
					</tr>
			<%
				}

				if (cart.getMiscellaneousCharge() > 0.0) {%>
					<tr valign="top" class="orderSummary">
						<td colspan="3" align="right"><a href="javascript:popup('/shared/fee_info.jsp?type=fuel','large');">Fuel Surcharge</a><%if(cart.isMiscellaneousChargeWaived()){%> (waived)<%}%>:</td>
						<td align="right"><%if(cart.isMiscellaneousChargeWaived()){%>$0.00<%}else{%><%= CCFormatter.formatCurrency(cart.getMiscellaneousCharge()) %><%}%></td>
						<td><%= cart.isMiscellaneousChargeTaxable() && !cart.isMiscellaneousChargeWaived() ? "&nbsp;<strong>T</strong>":""%></td>
						<td colspan="2"></td>
					</tr>
			<%
				}

				// TODO: this conditional branch is not part of i_viewcart.jspf
				if (cart.getPhoneCharge() > 0.0 || ((cart.getPhoneCharge() == 0.0) && cart.isPhoneChargeWaived())) { 
			%>
					<tr valign="top" class="orderSummary">
						<td colspan="3" align="right">Phone Charge<%if(cart.isPhoneChargeWaived()){%> (waived)<%}%>:</td>
						<td align="right"><%if(cart.isPhoneChargeWaived()){%>$0.00<%}else{%><%= CCFormatter.formatCurrency(cart.getPhoneCharge()) %><%}%></td>
						<td><%= cart.isPhoneChargeTaxable() && !cart.isPhoneChargeWaived() ? "&nbsp;<strong>T</strong>":""%></td>
						<td colspan="2"></td>
					</tr>
			<% 
				}

				double maxPromotion = user.getMaxSignupPromotion();
				PromotionI redemptionPromo = user.getRedeemedPromotion();
				boolean isRedemptionApplied = (redemptionPromo != null && user.getPromotionEligibility().isApplied(redemptionPromo.getPromotionCode()) );

				List discounts = cart.getDiscounts();
	
				for (Iterator iter = discounts.iterator(); iter.hasNext();) {
					ErpDiscountLineModel discountLine = (ErpDiscountLineModel) iter.next();
					Discount discount = discountLine.getDiscount();							
					
					if (user.isEligibleForSignupPromotion() && cart.getTotalDiscountValue() >= 0.01) {
				%>
						<tr valign="top" class="orderSummary">
							<td colspan="3" align="right"><b><a href="javascript:popup('/shared/promotion_popup.jsp?promoCode=signup','large')">FREE FOOD</a></b>:</td>
							<td colspan="1" align="right">-<%=CCFormatter.formatCurrency(discount.getAmount())%></td>
							<td colspan="3"></td>	   
						</tr>	   
				<%
					} else if (isRedemptionApplied && redemptionPromo.getPromotionCode().equalsIgnoreCase(discount.getPromotionCode())) { 
				%>
						<tr valign="top" class="orderSummary">
							<td colspan="3" align="right"><b><a href="javascript:popup('/shared/promotion_popup.jsp?promoCode=<%= redemptionPromo.getPromotionCode()%>','small')"><%= redemptionPromo.getDescription()%></a></b>:</td>
							<td align="right"><%= redemptionPromo.isSampleItem() ? "<b>FREE!</b>" : "-" + CCFormatter.formatCurrency(discount.getAmount()) %></td>
							<td><!--  --></td>
							<td colspan="3">&nbsp;<a href="<%= request.getRequestURI() %>?action=removeCode" class="note">Remove</a></td>
						</tr>	   
				<%
					} // TODO: missing else branch.
				}
	
				if (cart.getTotalDiscountValue() > 0) { 
					discounts = cart.getDiscounts();
					for (Iterator iter = discounts.iterator(); iter.hasNext();) {
						ErpDiscountLineModel discountLine = (ErpDiscountLineModel) iter.next();
						Discount discount = discountLine.getDiscount();
						PromotionI promotion = PromotionFactory.getInstance().getPromotion(discount.getPromotionCode());
						String desc = EnumPromotionType.SIGNUP.equals(promotion.getPromotionType()) ? "Free Food" : promotion.getDescription();
					%>
						<tr valign="top" class="orderSummary">
							<td colspan="3" align="right"><%= desc %>:</td>
							<td align="right">-<%= CCFormatter.formatCurrency(discount.getAmount()) %></td>
							<td colspan="3"></td>	   
						</tr>
					<%
					}
				}

				if (isRedemptionApplied) {
				    if (redemptionPromo.isSampleItem()) {
				%>
						<tr valign="top" class="orderSummary">
							<td colspan="3" align="right"><b><a href="javascript:popup('/shared/promotion_popup.jsp?promoCode=<%=redemptionPromo.getPromotionCode()%>','small')"><%=redemptionPromo.getDescription()%></a></b></td>
							<td colspan="1" align="right"><b>FREE!</b></td>
							<td colspan="1"></td>
							<td colspan="3">&nbsp;<a href="<%= request.getRequestURI() %>?action=removeCode" class="note">Remove</a></td>
						</tr>
				<%
					} else if (redemptionPromo.isWaiveCharge()) {
				%>
						<tr valign="top" class="orderSummary">
							<td colspan="3" align="right"><b><a href="javascript:popup('/shared/promotion_popup.jsp?promoCode=<%=redemptionPromo.getPromotionCode()%>','small')"><%=redemptionPromo.getDescription()%></a></b></td>
							<td colspan="1" align="right"><b>$0.00</b></td>
							<td colspan="1"></td>
							<td colspan="3">&nbsp;<a href="<%= request.getRequestURI() %>?action=removeCode" class="note">Remove</a></td>
						</tr>
				<%
					}
				}

				if (cart.getCustomerCreditsValue() > 0) {
				%>
					<tr valign="top" class="orderSummary">
						<td colspan="3" align="right">Credit Applied:</td>
						<td colspan="1" align="right">-<%=CCFormatter.formatCurrency(cart.getCustomerCreditsValue())%></td>
						<td colspan="3"></td>
					</tr>		   
				<%
				}
			%>
			<tr valign="top" class="orderTotal">
				<td colspan="3" align="right"><b><% if (cart.isEstimatedPrice()) { %>ESTIMATED TOTAL<% } else { %>ORDER TOTAL<%}%></b>:</td>
				<td colspan="1" align="right"><b><%= CCFormatter.formatCurrency(cart.getTotal()) %></b></td>
				<td colspan="1"><% if (cart.isEstimatedPrice()) { %>*<% } %></td>
				<td colspan="2"></td>
			</tr>
			<%
				if (isRedemptionApplied && redemptionPromo.isHeaderDiscount()) {
					if (request.getAttribute("redeem_override_msg") != null) {
				%>
					<%--
						<script languagE='JavaScript'>
							var msg = '<%= request.getAttribute("redeem_override_msg") %>';
							alert(msg);
						</script>
					--%>	
						<tr align="right">
							<td colspan="7"><span class="text11rbold"><%=request.getAttribute("redeem_override_msg")%></span></td>
						</tr>

					<%
					}
				}

				if ( ((redemptionPromo == null && maxPromotion <= 0.0) || (redemptionPromo != null && !isRedemptionApplied)) && makegood != true ) {
			%>
					<tr bgcolor="FFFFCE">
						<td colspan="3" align="right" style="padding: 4px;"><b>Enter promotion code: </b></td>
						<td align="right"><input type="text" size="10" maxlength="16" class="text10" name="redemptionCode" value=""></td>
						<td colspan="3" align="right"><input type="image" name="redemptionCodeSubmit" src="/media_stat/images/buttons/apply.gif" border="0" alt="APPLY" vspace="0"></td>
					</tr>
					<tr align="right">
						<td colspan="7"><fd:ErrorHandler result="<%=redemptionResult%>" name="redemption_error" id="errorMsg"><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
					</tr>
			<%
				}

				if (redemptionPromo != null && user.getPromotionEligibility().isEligible(redemptionPromo.getPromotionCode()) && 
						( !user.getPromotionEligibility().isApplied(redemptionPromo.getPromotionCode())
							|| (redemptionPromo.getHeaderDiscountRules()!=null && !(redemptionPromo.getHeaderDiscountTotal()==0) && cart.getTotalDiscountValue() == 0) 
						)
					) {
		
					// TODO: some of these error handlers are already written in RedemptionCodeControllerTag
					//   so error display duplication may occur! 
					double redemptionAmt = 0;
					String warningMessage = "";
					
					if (cart.getSubTotal() < redemptionPromo.getMinSubtotal()) {
						List headerDiscountRules = redemptionPromo.getHeaderDiscountRules();
						
						if (headerDiscountRules!=null) {
							HeaderDiscountRule rule = (HeaderDiscountRule) headerDiscountRules.get(0);
							redemptionAmt = rule.getMaxAmount();
						}
						
						warningMessage = MessageFormat.format(SystemMessageList.MSG_REDEMPTION_MIN_NOT_MET, new Object[] { new Double(redemptionPromo.getMinSubtotal()) } );
					} else if (redemptionPromo.isCategoryDiscount()) {
						warningMessage = SystemMessageList.MSG_REDEMPTION_NO_ELIGIBLE_CARTLINES;
					} else if (redemptionPromo.isSampleItem()) {
						warningMessage = SystemMessageList.MSG_REDEMPTION_PRODUCT_UNAVAILABLE;
					}
				%>
					<tr valign="top">
						<td colspan="3"><b><a href="javascript:popup('/shared/promotion_popup.jsp?promoCode=<%=redemptionPromo.getPromotionCode()%>','small')"><%=redemptionPromo.getDescription()%></a></b></td>
						<td colspan="1" align="right"><%= redemptionPromo.isSampleItem() ? "<b>FREE!</b>" : "-" + CCFormatter.formatCurrency(redemptionAmt) %></td>
						<td colspan="1"></td>
						<td colspan="2">&nbsp;<a href="<%= request.getRequestURI() %>?action=removeCode" class="note">Remove</a></td>
					</tr>
				<%
					// MNT-113 Quick fix: to avoid duplicated error displays we check
					//   whether the generated warning message is displayed already by ErrorHandlerTag 
					ActionError rerr = redemptionResult.getError("redemption_error");
					String rerrMsg = (rerr != null ? rerr.getDescription() : null);
	
					// display if message is not empty and does not match the previously displayed error message (if exists)
					if (!"".equalsIgnoreCase(warningMessage) && !warningMessage.equalsIgnoreCase(rerrMsg)) {
				%>
						<tr align="right">
							<td colspan="7"><span class="text11rbold"><%= warningMessage %></span></td>
						</tr>
				<%
					}
				}
			%>
			<tr>
				<td colspan="7" align="right">
				<%
					if (identity == null || user.isEligibleForSignupPromotion()) {
						if (user.isEligibleForSignupPromotion()) {

							PromotionI signupPromo = user.getEligibleSignupPromotion();					
							SignupDiscountRule discountRule = user.getSignupDiscountRule();
							int minSubtotal = (int)discountRule.getMinSubtotal();
							int maxAmount   = (int)discountRule.getMaxAmount();
							%>Spend $<%=minSubtotal%> on this order to get $<%=maxAmount%> free, fresh food. <a href="javascript:popup('/shared/promotion_popup.jsp','large')">Click for details</a>.
						<%
							if (cart.getSubTotal() < minSubtotal) {
								%><br /><b>This order does not yet qualify for $<%=maxAmount%> offer. Subtotal must be $<%=minSubtotal%> or more.</b><%
							}
							
							%><br />Applies to home delivery orders only.<%
						}
				
						if (user.isPickupOnly()) {
							%><br /><font color="#CC0000"><b>Pickup orders are not eligible for our free food promotion.</b></font><%
						}
					}
				%>
				</td>
			</tr>
	
			<% 
				if (user!=null && user.getIdentity() !=null) {
					%><fd:CustomerCreditHistoryGetterTag id='customerCreditHistory'><%
						if (customerCreditHistory.getRemainingAmount()>0.00) {
					%>
							<tr>
								<td colspan="7" align="right">
									<span class="text11">You have <%= CCFormatter.formatCurrency(customerCreditHistory.getRemainingAmount()) %> credit. Credits will be applied during check out.</span><br />
								</td>
							</tr>
					<%
						}
					%></fd:CustomerCreditHistoryGetterTag><%
				}
			%>
	</form>

	<% if (!makegood) { 
		if (user.getGiftcardBalance() > 0) { %>
			<tr valign="top" style="background-color: #FF9933;line-height: 20px;">
				<td colspan="3" align="right" style="color: white;font-size: 13px;"><b>Gift Card Balance</b>:</td>
				<td colspan="1" align="right" style="color: white;font-size: 13px;"><b><%= CCFormatter.formatCurrency(user.getGiftcardBalance()) %></b></td>
				<td colspan="1"></td>
				<td colspan="2">&nbsp;<a href="<%= request.getRequestURI() %>?action=removeGiftCard" class="note">Remove</a></td>
			</tr>
		<% } %>     
<%-- 
		<fd:GiftCardController actionName='applyGiftCard' result='result' successPage='/checkout/checkout_review_items.jsp'>
			<form method="post">
				<tr bgcolor="white">
					<td colspan="3" align="right" style="border-top: orange thin dashed; border-left: orange thin dashed; border-bottom: orange thin dashed"><b>Add Gift Card, enter code: </b></td>
					<td align="right" style="border-top: orange thin dashed; border-bottom: orange thin dashed"><input type="text" size="10" maxlength="22" class="text10" name="givexNum" value=""></td>
					<td style="border-top: orange thin dashed; border-right: orange thin dashed; border-bottom: orange thin dashed" colspan="3" align="right"><input type="image" name="giftCardCodeSubmit" src="/media_stat/images/giftcards/your_account/apply_btn.gif" border="0" alt="APPLY" vspace="0"></td>
				</tr>
				<tr align="right">
					<td colspan="7">
						<fd:ErrorHandler result='<%=result%>' name='invalid_card' id='errorMsg'>
							<%@ include file="/includes/i_error_messages.jspf" %>   
						</fd:ErrorHandler>
						<fd:ErrorHandler result="<%=result%>" name="card_in_use" id="errorMsg">
							<%@ include file="/includes/i_error_messages.jspf" %>   
						</fd:ErrorHandler>
					</td>
				</tr>
			</form>

			<% if (user.getGiftcardBalance() > 0) { %>
				<tr bgcolor="white">
					<td colspan="3" align="right" style="border-left: orange thin dashed">Order total with Gift Cards Applied (total at 125% if estimated):</td>
					<td align="right" ><%= CCFormatter.formatCurrency(cart.getTotal()) %></td>
					<td style="BORDER-RIGHT: orange thin dashed" colspan="3" align="right"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr bgcolor="white">
					<td colspan="3" align="right" style="border-left: orange thin dashed; border-bottom: orange thin dashed">Remaining Gift Card balance:</td>
					<td align="right" style="border-bottom: orange thin dashed"><b><%= CCFormatter.formatCurrency(user.getGiftcardBalance()) %></b></td>
					<td style="border-right: orange thin dashed; border-bottom: orange thin dashed" colspan="3" align="right"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
			<% } %> 
		</fd:GiftCardController>--%>
	<% } %>

			<tr><td colspan="7" align="right"><br /><strong>T</strong> = Taxable Item&nbsp;&nbsp;&nbsp;&nbsp;<strong>S</strong> = Special Price&nbsp;&nbsp;&nbsp;&nbsp;<strong>D</strong> = Bottle Deposit</td></tr>
		</table>


		<br clear="both" />
	</div>

<%-- end view iterate new stuff  --%>


<script language="javascript" type="text/javascript">
	<!--
	function setCartLineRemoveID(id) {
    	document.viewcart["cartLineRemove"].value = id;
	    setSubmitAction("removeCartLine");
    	return true;
	}
	function nextPage(){		
	    setSubmitAction("nextPage");	 
    	document.viewcart.submit();
	}

	function submitRemove(){
	    //perform a form submission on the viewCart form.
    	document.viewcart.submit();
	}

	function setSubmitAction(action){
    	document.viewcart["action1"].value = action;
	}
	setSubmitAction("nothing");

	// Use this method to change Quantity field values. 
	//Takes the field index and the change quantity as params
	function chgCartQty(formElement, delta) {
		qty = parseFloat(formElement.value) + delta;
		if (qty < <%= minQuantity %>) return;
		formElement.value = qty;
	}
	// -->
</script>


</fd:RedemptionCodeController>
</fd:FDShoppingCart>

</tmpl:put>
</tmpl:insert>