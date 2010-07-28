<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.webapp.taglib.promotion.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.management.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter" %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@ page import="com.freshdirect.webapp.util.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionStatus"%>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionType"%>
<%
	PromoFilterCriteria  promoFilter =  (PromoFilterCriteria)request.getSession().getAttribute("filter");
	String offerType = "";
	String customerType ="";
	String promoStatus = "";
	String createdBy = "";
	String modifiedBy = "";
	String keyword = "";
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
	
	if(null!=offerType && !"".equalsIgnoreCase(offerType)){
		if(EnumPromotionType.HEADER.getName().equalsIgnoreCase(offerType)){
			offerType = "Header - Generic";
		}else if(EnumPromotionType.GIFT_CARD.getName().equalsIgnoreCase(offerType)){
			offerType = "Header - "+EnumPromotionType.GIFT_CARD.getDescription();
		}else if(EnumPromotionType.WINDOW_STEERING.getName().equalsIgnoreCase(offerType)){
			offerType = "Header - "+EnumPromotionType.WINDOW_STEERING.getDescription();
		}else if(EnumPromotionType.WAIVE_CHARGE.getName().equalsIgnoreCase(offerType)){
			offerType = "Header - "+EnumPromotionType.WAIVE_CHARGE.getDescription();
		}else if(EnumPromotionType.DP_EXTENSION.getName().equalsIgnoreCase(offerType)){
			offerType = "Header - "+EnumPromotionType.DP_EXTENSION.getDescription();
		}else if(EnumPromotionType.LINE_ITEM.getName().equalsIgnoreCase(offerType)){
			offerType = EnumPromotionType.LINE_ITEM.getDescription();
		}else if(EnumPromotionType.SAMPLE.getName().equalsIgnoreCase(offerType)){
			offerType = EnumPromotionType.SAMPLE.getDescription();
		}
	}
	
	if(null != customerType && !"".equalsIgnoreCase(customerType)){
		if("ChefsTable".equalsIgnoreCase(customerType)){
			customerType = "Chef's Table";
		}else if("New".equalsIgnoreCase(customerType)){
			customerType = "New";
		}else if("COSPilot".equalsIgnoreCase(customerType)){
			customerType = "COS";
		}else if("COSNew".equalsIgnoreCase(customerType)){
			customerType = "COS New";
		}else if("DeliveryPass".equalsIgnoreCase(customerType)){
			customerType = "Active/RTU DP";
		}else if("MarketingPromo".equalsIgnoreCase(customerType)){
			customerType = "Marketing";
		}
	}
	
	final EnumPromotionStatus[] statuses;
	if (FDStoreProperties.isPromoPublishNodeMaster()) {
		statuses = new EnumPromotionStatus[]{
			EnumPromotionStatus.DRAFT, EnumPromotionStatus.APPROVE, EnumPromotionStatus.PROGRESS, EnumPromotionStatus.TEST, EnumPromotionStatus.PUBLISHED, EnumPromotionStatus.EXPIRED, EnumPromotionStatus.CANCELLING, EnumPromotionStatus.CANCELLED
		};
	} else {
		statuses = new EnumPromotionStatus[]{
				EnumPromotionStatus.LIVE, EnumPromotionStatus.CANCELLED
		};
	}


	for (EnumPromotionStatus s : statuses) {
		if(s.getName().equalsIgnoreCase(promoStatus)){
			promoStatus = s.getDescription();
		}
	}
	%>
	<table border="1">
				<tr>
					<th >&nbsp;</th>
					<th >Offer Type:<%= null!=offerType && !"".equalsIgnoreCase(offerType) ? offerType:"ALL" %></th>
					<th >Customer Type:<%= null!=customerType && !"".equalsIgnoreCase(customerType) ? customerType:"ALL" %></th>
					<th >Status:<%= null!=promoStatus && !"".equalsIgnoreCase(promoStatus) ? promoStatus:"ALL" %></th>
					<th >Created By:<%= null!=createdBy && !"".equalsIgnoreCase(createdBy) ? createdBy:"ALL" %></th>
					<th >Last modified by:<%= null!=modifiedBy && !"".equalsIgnoreCase(modifiedBy) ? modifiedBy:"ALL" %></th>
					<th >Keyword:<%= null!=keyword && !"".equalsIgnoreCase(keyword) ? keyword:" " %></th>
					<th >&nbsp;</th>
				</tr>
				<tr>
				</tr>
	</table>
	<table width="100%" cellspacing="0" cellpadding="0" border="1">
		<%    
	JspTableSorter sort = new JspTableSorter(request);
%>
		<tbody>
			<tr>
				<th width="14px" align="center">&nbsp;</th>
				<th width="193px">Name</th>
				<th width="143px">Redemption Code</th>
				<th width="90px">Type</th>
				<th width="280px">Description</th>
				<th width="110px">Start</th>
				<th width="125px">Expire</th>
				<th width="90px">Status</th>
				<th width="150px">Created</th>
				<th width="150px">Modified</th>				
				
			</tr>
		</tbody>
		</table>
	
	<fd:GetAllPromotionsNew id = "promoRows" filter="<%= promoFilter %>">
	<table width="100%" cellspacing="0" cellpadding="0" border="1" >

	
<% EnumPromotionType lastPromoType = null; %>
<% if(promoRows.isEmpty()){ %>
<tr valign="top" style="color:#CC0000; font-weight: bold;"><td colspan="11" align="center">No matching promotions found.</td></tr>
<%} %>
<logic:iterate id="p" collection="<%= promoRows %>" type="com.freshdirect.webapp.taglib.promotion.PromoNewRow" indexId="idx">
<% 
	EnumPromotionType ptype = p.getType();
	if (lastPromoType==null || !lastPromoType.equals(ptype)) {
       		lastPromoType = ptype;
%>
     		<!-- <tr bgcolor="#DDDDDD" class="list_content"><td colspan="10">&nbsp;<a name="<%=ptype.getName().toLowerCase()%>"></a><i>Promotion Type:</i> <b><%=ptype.getName()%></b></td></tr> --> 
<%
    	}
	EnumPromotionStatus pStatus = p.getStatus();
%>
<% 
	String linkURL = "#";
	/*if(currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))) { 
		linkURL = "/promotion/edit_promotion.jsp?promoId="+p.getCode();
	} else {
		linkURL = "/main/view_promotion.jsp?promoId="+p.getCode();
	}*/	
	linkURL = "/main/view_promotion.jsp?promoId="+p.getCode();

		  	String rCode = p.getRedemptionCode();
			Date expire = p.getExpire();
		%>
		
		<tr valign="top">
		
			<td >&nbsp;</td>
			<td  style="font-weight: normal"><div style="width:180px;"><%=p.getName()%></div>
			<% boolean isChef = p.isChef();
			   boolean isNewCust = p.isNewCust();
			   boolean isCos= p.isCos();
			   boolean isDp = p.isDp();
			   boolean isMktg = p.isMktg();
			   boolean isCosNew = p.isCosNew();
			   StringBuffer custBuff = new StringBuffer();
			   if(isChef || isNewCust || isCos || isDp || isMktg || isCosNew){ 
				  	custBuff.append("(");
					if(isChef){ 
	                	custBuff.append("Chef's Table");               
	                }
	                if(isNewCust){
	                	if(custBuff.length()>1){
	                		custBuff.append(",");
	                	}
	                	custBuff.append(" New");
					}
	                if(isCos){
	                	if(custBuff.length()>1){
	                		custBuff.append(",");
	                	}
	                	custBuff.append(" COS");
					}
	                if(isCosNew){
	                	if(custBuff.length()>1){
	                		custBuff.append(",");
	                	}
	                	custBuff.append(" COS New");
	                }
	                if(isDp){
	                	if(custBuff.length()>1){
	                		custBuff.append(",");
	                	}
	                	custBuff.append(" Active/RTU DP");				
					}
	                if(isMktg){
	                	if(custBuff.length()>1){
	                		custBuff.append(",");
	                	}
	                	custBuff.append(" Marketing");
					}
	                custBuff.append(" )");
				} %><%= custBuff.toString() %>
				</td>
			<td  style="font-weight: normal"><%=(rCode != null && !rCode.equals(""))? rCode.toUpperCase() : "N/A" %></td>
			<td  style="font-weight: normal"><%=ptype.getName()%></td>
			<td  style="font-weight: normal"><%=p.getDescription() %></td>
			<td  style="font-weight: normal"><%=CCFormatter.formatDate(p.getStart())%></td>
			<td  style="font-weight: normal"><%= expire != null ? CCFormatter.formatDate(expire) : ""%></td>
			<td  style="font-weight: normal"><%= null!=pStatus ? pStatus.getDescription():" " %></td>
			<td  style="font-weight: normal"><%= (null!=p.getCreatedBy()?p.getCreatedBy():"") %></td>			
			<td  style="font-weight: normal"><%= (null !=p.getModifiedBy()?p.getModifiedBy():"") %></td>
			
		</tr>

</logic:iterate>

	<%-- Filler --%>
	<%-- for (int i=0; i<= 50; i++) { %>
		<tr>
			<td>&nbsp;</td>
			<td>Name</td>
			<td>Redemption Code</td>
			<td>Type</td>
			<td>Description</td>
			<td>Start</td>
			<td>Expire</td>
			<td>Status</td>
			<td>Created Modified</td>
		<td><a href="/promotion/promo_details.jsp">Details</a></td>
			<td>&nbsp;</td>
		</tr>
		<%-- exclude line after last item --%>
		<%-- if (i<50) { %>
			<tr>
				<td colspan="11" style="background-color: #ccc;"><img width="1" height="1" src="/media_stat/crm/images/clear.gif"></td>
			</tr>
		<% } %>
	<% } --%>
	<!-- <div class="promo_ico_cont fleft"><img width="16" height="16" src="/media_stat/crm/images/purple_star.gif" /></div>
			<div class="promo_ico_cont fleft"><img width="16" height="16" src="/media_stat/crm/images/orange_o.gif" /></div>
			<div class="promo_ico_cont fleft"><img width="16" height="16" src="/media_stat/crm/images/blue_building.gif" /></div>
			<div class="promo_ico_cont fleft"><img width="16" height="16" src="/media_stat/crm/images/green_truck.gif" /></div>
			<div class="promo_ico_cont fleft"><img width="16" height="16" src="/media_stat/crm/images/pink_heart.gif" /></div>  -->
</table>
</fd:GetAllPromotionsNew>
	
