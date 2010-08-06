<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.webapp.taglib.promotion.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter" %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@ page import="com.freshdirect.webapp.util.*" %>
<script type="text/javascript" language="javascript" src="/assets/javascript/promo.js"></script>
<tmpl:insert template='/template/top_nav.jsp'>
<%
	PromoFilterCriteria  promoFilter =  (PromoFilterCriteria)request.getSession().getAttribute("filter");
	String offerType = "";
	String customerType ="";
	String promoStatus = "LIVE";
	String createdBy = "";
	String modifiedBy = "";
	String keyword = "";
	if(null == promoFilter || promoFilter.isEmpty()){
		promoFilter = new PromoFilterCriteria(offerType,customerType,promoStatus,createdBy,modifiedBy,keyword);
	}
	if(null !=request.getParameter("promo_filter_submit")){
		offerType =request.getParameter("offerType");
		customerType =request.getParameter("customerType");
		promoStatus =request.getParameter("promoStatus");
		createdBy =request.getParameter("createdBy");
		modifiedBy =request.getParameter("modifiedBy");
		keyword =request.getParameter("keyword");
		promoFilter = new PromoFilterCriteria(offerType,customerType,promoStatus,createdBy,modifiedBy,keyword);
	}else if(null != promoFilter && !promoFilter.isEmpty()){
		offerType = promoFilter.getOfferType();
		customerType = promoFilter.getCustomerType();
		promoStatus = promoFilter.getPromoStatus();
		createdBy = promoFilter.getCreatedBy();
		modifiedBy = promoFilter.getModifiedBy();
		keyword = promoFilter.getKeyword();		
	}
	List<String> createdUsers = FDPromotionNewModelFactory.getInstance().getPromotionsCreatedUsers();//(List)request.getAttribute("createdUsers");
	List<String> modifiedUsers = FDPromotionNewModelFactory.getInstance().getPromotionsModifiedUsers();//(List)request.getAttribute("modifiedUsers");
	String promoId = null;
	FDPromotionNewModel promotion = new FDPromotionNewModel();
	%>
	<tmpl:put name='title' direct='true'>View Promotions</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
		<%@ include file="/includes/promotions/i_promo_nav.jspf" %>
		<%-- Top-Row, page specific nav --%>
		<div class="promo_page_header">
		<form method='POST' name="frmPromoList" id="frmPromoList" action="/main/promo_home.jsp">
			<table>
				<tr>
					<td class="grayIta10pt">&nbsp;</td>
					<td class="grayIta10pt">Offer Type</td>
					<td class="grayIta10pt">Customer Type</td>
					<td class="grayIta10pt">Status</td>
					<td class="grayIta10pt">Created By</td>
					<td class="grayIta10pt">Last modified by</td>
					<td class="grayIta10pt">Keyword</td>
					<td class="grayIta10pt">&nbsp;</td>
				</tr>
				<tr>
					<td class="promo_page_header_text">View Promotions&nbsp;</td>
					<td>
						<select id="offerType" name="offerType" class="promo_filter">
							<option value="">ALL</option>
							<option value="<%= EnumOfferType.GENERIC.getName() %>" <%= EnumOfferType.GENERIC.getName().equalsIgnoreCase(offerType)?"selected":""%>>Header - Generic</option>
							<option value="<%= EnumOfferType.GIFT_CARD.getName() %>" <%= EnumOfferType.GIFT_CARD.getName().equalsIgnoreCase(offerType)?"selected":""%>>Header - <%= EnumOfferType.GIFT_CARD.getDescription() %></option>
							<option value="<%= EnumOfferType.WINDOW_STEERING.getName() %>" <%= EnumOfferType.WINDOW_STEERING.getName().equalsIgnoreCase(offerType)?"selected":""%>>Header - <%= EnumOfferType.WINDOW_STEERING.getDescription() %></option>
							<option value="<%= EnumOfferType.WAIVE_DLV_CHARGE.getName() %>" <%= EnumOfferType.WAIVE_DLV_CHARGE.getName().equalsIgnoreCase(offerType)?"selected":""%>>Header - <%= EnumOfferType.WAIVE_DLV_CHARGE.getDescription() %></option>
							<option value="<%= EnumOfferType.DP_EXTN.getName() %>" <%= EnumOfferType.DP_EXTN.getName().equalsIgnoreCase(offerType)?"selected":""%>>Header - <%= EnumOfferType.DP_EXTN.getDescription() %></option>
							<option value="<%= EnumOfferType.LINE_ITEM.getName() %>" <%= EnumOfferType.LINE_ITEM.getName().equalsIgnoreCase(offerType)?"selected":""%>><%= EnumOfferType.LINE_ITEM.getDescription() %></option>
							<option value="<%= EnumOfferType.SAMPLE.getName() %>" <%= EnumOfferType.SAMPLE.getName().equalsIgnoreCase(offerType)?"selected":""%>><%= EnumPromotionType.SAMPLE.getDescription() %></option>
						</select>
					</td>
					<td>
						<select id="customerType" name="customerType" class="promo_filter">
							<option value="" selected="selected">ALL</option>
							<option value="ChefsTable" <%= "ChefsTable".equalsIgnoreCase(customerType)?"selected":"" %>>Chef's Table</option>
							<option value="New" <%= "New".equalsIgnoreCase(customerType)?"selected":"" %>>New</option>
							<option value="COSPilot" <%= "COSPilot".equalsIgnoreCase(customerType)?"selected":"" %>>COS</option>							
							<option value="COSNew" <%= "COSNew".equalsIgnoreCase(customerType)?"selected":"" %>>COS New</option>
							<option value="DeliveryPass" <%= "DeliveryPass".equalsIgnoreCase(customerType)?"selected":"" %>>Active/RTU DP</option>
							<option value="MarketingPromo" <%= "MarketingPromo".equalsIgnoreCase(customerType)?"selected":"" %>>Marketing</option>
						</select>
					</td>
					<td>
						<select id="promoStatus" name="promoStatus" class="promo_filter">
							<option value="">ALL</option>
							<option value="LIVE" <%= "LIVE".equalsIgnoreCase(promoStatus)?"selected":"" %>>Live</option>
							<!--<option value="EXPIRED_CANCELLED" <%= "EXPIRED_CANCELLED".equalsIgnoreCase(promoStatus)?"selected":"" %>>EXPIRED/CANCELLED</option>
							<option value="DRAFT_TEST_PROGRESS" <%= "DRAFT_TEST_PROGRESS".equalsIgnoreCase(promoStatus)?"selected":"" %>>DRAFT/TEST/IN PROGRESS</option>-->
						<%
							final EnumPromotionStatus[] statuses;
							if (FDStoreProperties.isPromoPublishNodeMaster()) {
								statuses = new EnumPromotionStatus[]{
									EnumPromotionStatus.DRAFT, EnumPromotionStatus.APPROVE, EnumPromotionStatus.PROGRESS, EnumPromotionStatus.TEST, EnumPromotionStatus.PUBLISHED, EnumPromotionStatus.EXPIRED, EnumPromotionStatus.CANCELLING, EnumPromotionStatus.CANCELLED
								};
							} else {
								statuses = new EnumPromotionStatus[]{
										EnumPromotionStatus.CANCELLED,EnumPromotionStatus.EXPIRED
								};
							}


						for (EnumPromotionStatus s : statuses) { %>
							<option value="<%= s.getName() %>" <%= s.getName().equalsIgnoreCase(promoStatus)?"selected":"" %>><%= s.getDescription() %></option>
						<% } %>
						</select>
					</td>
					<td>
						<select id="createdBy" name="createdBy" class="promo_filter">
						<option value="" selected="selected">ALL</option>
						<logic:iterate id="createdUser" collection="<%= createdUsers %>" type="java.lang.String" indexId="idx">
							<option value="<%= createdUser %>" <%= createdUser.equalsIgnoreCase(createdBy)?"selected":"" %>><%= createdUser %></option>
						</logic:iterate>
						</select>
						
					</td>
					<td>
						<select id="modifiedBy" name="modifiedBy" class="promo_filter">
						<option value="" selected="selected">ALL</option>
						<logic:iterate id="modifiedUser" collection="<%= modifiedUsers %>" type="java.lang.String" indexId="idx">
							<option value="<%= modifiedUser %>" <%= modifiedUser.equalsIgnoreCase(modifiedBy)?"selected":"" %>><%= modifiedUser %></option>
						</logic:iterate>
						</select>
					</td>
					<td><input type="text" id="keyword" name="keyword" class="promo_filter" value="<%= keyword %>"/></td>
					<td><input type="submit" value="FILTER" onclick="" id="promo_filter_submit" name="promo_filter_submit" class="promo_btn_grn" /></td>
					<td><input type="submit" value="REFRESH" onclick="" id="promo_refresh_submit" name="promo_refresh_submit" class="promo_btn_grn" /></td>
				</tr>
			</table>
			</form>
		</div>
		<%-- content row --%>
		<table width="100%" cellspacing="0" cellpadding="0" class="promo_table1" border="0">
		<%    
	JspTableSorter sort = new JspTableSorter(request);
%>
		<tbody>
			<tr>
				<th width="14px" align="center">&nbsp;</th>
				<th width="193px"><a href="?<%= sort.getFieldParams("name") %>" >Name</a></th>
				<th width="143px"><a href="?<%= sort.getFieldParams("redemptionCode") %>" >Redemption Code</a></th>
				<th width="90px"><a href="?<%= sort.getFieldParams("type") %>" >Type</a></th>
				<th width="275px"><a href="?<%= sort.getFieldParams("description") %>" >Description</a></th>
				<th width="110px"><a href="?<%= sort.getFieldParams("start") %>" >Start</a></th>
				<th width="125px"><a href="?<%= sort.getFieldParams("expire") %>" >Expire</a></th>
				<th width="90px"><a href="?<%= sort.getFieldParams("status") %>" >Status</a></th>
				<th width="75px"><a href="?<%= sort.getFieldParams("createdBy") %>" >Created</a></th>
				<th width="75px"><a href="?<%= sort.getFieldParams("modifiedBy") %>" >Modified</a></th>
				<th><img width="35" height="1" src="/media_stat/crm/images/clear.gif"></th>
				<th><img width="12" height="1" src="/media_stat/crm/images/clear.gif"></th>
			</tr>
		</tbody>
		</table>

		<div class="promo_page_content-view_content5 noBorderLR">
			<%@ include file="/includes/promotions/i_promo_view_promoList.jspf" %>
		</div>
		<%-- key/quick filter row --%>
		<div class="promo_page_content-view_footer">
			<table width="100%" cellspacing="4" cellpadding="0" border="0" class="promo_tableBody">
				<tr>
					<td width="50%" class="gray8pt">
						
						<div class="promo_ico_cont fleft"><img width="16" height="16" src="/media_stat/crm/images/purple_star.gif" /></div>
						<div class="promo_ico_text_cont fleft">CHEF</div>
						
						<div class="promo_ico_cont fleft"><img width="16" height="16" src="/media_stat/crm/images/orange_o.gif" /></div>
						<div class="promo_ico_text_cont fleft">NEW</div>
						
						<div class="promo_ico_cont fleft"><img width="16" height="16" src="/media_stat/crm/images/blue_building.gif" /></div>
						<div class="promo_ico_text_cont fleft">COS</div>
						
						<div class="promo_ico_cont fleft"><img width="16" height="16" src="/media_stat/crm/images/green_truck.gif" /></div>
						<div class="promo_ico_text_cont fleft">DP</div>
						
						<div class="promo_ico_cont fleft"><img width="16" height="16" src="/media_stat/crm/images/pink_heart.gif" /></div>
						<div class="promo_ico_text_cont fleft">MKTG</div>
					</td>
					<td width="50%" class="gray8pt" align="right">
						<%-- float right, reverse order - text then ico. also, pair order is reversed --%>
						
						<div class="promo_ico_text_cont fright">DRAFT/TEST/IN PROGRESS</div>
						<div class="promo_ico_cont fright"><img width="11" height="11" src="/media_stat/crm/images/clear.gif" class="BG_test blackBord1px" /></div>
						
						<div class="promo_ico_text_cont fright">EXPIRED/CANCELLED </div>
						<div class="promo_ico_cont fright"><img width="11" height="11" src="/media_stat/crm/images/clear.gif" class="BG_exp blackBord1px" /></div>

						<div class="promo_ico_text_cont fright">LIVE</div>
						<div class="promo_ico_cont fright"><img width="11" height="11" src="/media_stat/crm/images/clear.gif" class="BG_live blackBord1px" /></div>
					</td>
				</tr>
			</table>
		</div>

	
	</crm:GetCurrentAgent>
	</tmpl:put>
</tmpl:insert>