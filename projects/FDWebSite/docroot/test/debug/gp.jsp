<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.common.customer.*'%>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.erp.ErpFactory'%>
<%@ page import='com.freshdirect.erp.model.ErpProductInfoModel '%>
<%@ page import='java.util.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.net.URLEncoder' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed='true' />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.freshdirect.fdstore.grp.FDGrpInfoManager"%>
<%@page import="com.freshdirect.common.pricing.MaterialPrice"%><html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">

		<title>Group Price</title>

		<script src="/assets/javascript/prototype.js" type="text/javascript" language="javascript"></script>
		<script src="/assets/javascript/scriptaculous.js?load=effects,builder" type="text/javascript" language="javascript"></script>
		<script  src="/assets/javascript/modalbox.js" type="text/javascript" language="javascript"></script>
		<script  src="/assets/javascript/FD_GiftCards.js" type="text/javascript" language="javascript"></script>

		<link href="/assets/css/pc_ie.css" rel="stylesheet" type="text/css" />
		<link href="/assets/css/giftcards.css" rel="stylesheet" type="text/css" />
		<link href="/assets/css/modalbox.css" rel="stylesheet" type="text/css" />

	</head>
<body>
<%
	FDSessionUser currentUser= (FDSessionUser)session.getAttribute( SessionName.USER );
	String zoneId=currentUser.getPricingZoneId();
	EnumServiceType serviceType=currentUser.getZPServiceType();
	String zipCode=currentUser.getZipCode(); 
	String skuCode=request.getParameter("sku");
	if(skuCode!=null) {
		//FDProduct product=FDCachedFactory.getProduct(skuCode);
		FDProductInfo prodInfo=FDCachedFactory.getProductInfo(skuCode);
		ZonePriceInfoListing zLis=prodInfo.getZonePriceInfoList();
		Collection zoneList=zLis.getZonePriceInfos();
		Iterator ite=zoneList.iterator();
	%>
		<table border='1' width='50%' cellpadding="3" cellspacing="0">
		<tr>
			<th align="center" style="font-size: 14px;" width="150">ZONE PRICING (SKU)</th>
			<td>
			<% if (zoneId!=null && !"".equals(zoneId)) { %>
				<table border="0" cellpadding="3" cellspacing="0" width="100%">
				<tr>
					<td align="right">SERVICE TYPE :</td>
					<td><%=serviceType.getName()%></td>
				</tr>
				<tr>
					<td align="right">ZIP CODE :</td>
					<td><%=zipCode%></td>
				</tr>
				<tr>
					<td align="right">ZONE ID :</td>
					<td><%=zoneId%></td>
				</tr>
				</table>
			<% } %>
			</td>
		</tr>
		<tr>
			<td align="right">Zone Price Info Model :</td>
			<td>
				<table>
				<%
				while(ite.hasNext()) {
				ZonePriceInfoModel model=(ZonePriceInfoModel)ite.next();
				%>
					<tr>
						<td align="right">SKU :</td><td><%=skuCode%></td>
					</tr>
					<tr>
						<td align="right">ZONE ID :</td><td><%=model.getSapZoneId()%></td>
					</tr>
					<tr>
						<td align="right">PRICE :</td><td><%=model.getDefaultPrice()%></td>
					</tr>
					<tr>
						<td align="right">DEFAULT UNIT :</td><td><%=model.getDefaultPriceUnit()%></td>
					</tr>
					<tr>
						<td align="right">DEAL PERCENTAGE :</td><td><%=model.getDealPercentage()%></td>
					</tr>
					<% if (ite.hasNext()) { %>
						<tr>
							<td colspan="2"><hr /></td>
						</tr>
					<% } %>
				<% } %>
				</table>
			</td>
		</tr>
		</table>
	<% }

	String grpId=request.getParameter("grpId");
	String ver=request.getParameter("version");
	int version = 0;
	if(ver != null){
		version = Integer.parseInt(ver);
	} else {
		version = FDGrpInfoManager.getLatestVersionNumber(grpId);
	}
	if(grpId != null) {
		%>
			<table border='1' width='50%' cellpadding="3" cellspacing="0">
			<tr>
				<th align="center" style="font-size: 14px;" width="150">GROUP PRICING</th>
				<td>
				<% if (zoneId!=null && !"".equals(zoneId)) { %>
					<table border="0" cellpadding="3" cellspacing="0" width="100%">
					<tr>
						<td align="right">SERVICE TYPE :</td>
						<td><%=serviceType.getName()%></td>
					</tr>
					<tr>
						<td align="right">ZIP CODE :</td>
						<td><%=zipCode%></td>
					</tr>
					<tr>
						<td align="right">ZONE ID :</td>
						<td><%=zoneId%></td>
					</tr>
					</table>
				<% } %>
				</td>
			</tr>
		<%
		GroupScalePricing model1 = null;
		try {
			model1=FDCachedFactory.getGrpInfo(new FDGroup(grpId, version));
		}catch(FDGroupNotFoundException fe){
			//do nothing
		} 
		if (model1 != null) {

		%>
			<tr>
				<td align="right">GroupId :</td><td><%=model1.getGroupId()%></td>
			</tr>
			<tr>
				<td align="right"> GRP SHORT DESC :</td><td><%=model1.getShortDesc()%></td>
			</tr>
			<tr>
				<td align="right"> GRP LONG DESC :</td><td><%=model1.getLongDesc()%></td>
			</tr>
			<tr>
				<td align="right"> ACTIVE :</td><td><%=model1.isActive()%></td>
			</tr>
			<tr>
				<td align="right"> Version :</td><td><%=model1.getVersion()%></td>
			</tr>
			<tr>
				<td align="right"> GrpZonePriceModel :</td><td>
					<table>
					<%
						Collection<GrpZonePriceModel> gpm = model1.getGrpZonePriceList().getGrpZonePrices();
						Iterator<GrpZonePriceModel> gpmI=gpm.iterator();
						while(gpmI.hasNext()){
							GrpZonePriceModel gpModel=(GrpZonePriceModel)gpmI.next();
							%>
							<tr>
								<td align="right">ZONE ID :</td>
								<td><%=gpModel.getSapZoneId()%></td>
							</tr>
							<%
								MaterialPrice[] matPrices = gpModel.getMaterialPrices();
								for(int i = 0; i < matPrices.length; i++) {
							%>
							
							<tr>
								<td align="right">QTY :</td>
								<td><%=matPrices[i].getScaleLowerBound()%></td>
							</tr>
							<tr>
								<td align="right">SCALE UNIT :</td>
								<td><%=matPrices[i].getPricingUnit()%></td>
							</tr>
							<tr>
								<td align="right">PRICE :</td>
								<td><%=matPrices[i].getPrice()%></td>
							</tr>
							</tr>
							<tr>
								<td align="right">SELLING UNIT :</td>
								<td><%=matPrices[i].getScaleUnit()%></td>
							</tr>		
							<% } %>	
							<% if (gpmI.hasNext()){ %>
								<tr>
									<td colspan="2"><hr /></td>
								</tr>
							<% } %>
						<% } %>
					</table>
				</td>
			</tr>
			<%Set<String> mList = model1.getMatList(); %>
			<tr>
				<td align="right"> Material List :</td><td>
					<table>
					<tr>
						<td valign="top" align="right" width="100">SAP ID (Sku) :</td><td>
					<%
						Iterator mI=mList.iterator();
						while(mI.hasNext()) {
							Object gpmModel=mI.next();
							%><%=gpmModel%><% if (mI.hasNext()){ %>,<br /><% } %>
						<% } %>
						</td>
					</tr>
					</table>
				</td>
			</tr>
			
			<tr>
				<td align="right"> Material List :<br />(with SKUs)</td><td>
					<table>
					<tr>
						<td valign="top" align="right" width="100">SAP ID (Sku) :</td><td>
					<%
						Iterator<String> mI_skus=mList.iterator();
						ErpFactory factory = ErpFactory.getInstance();
						while(mI_skus.hasNext()) {
							Object gpmModel=mI_skus.next();
							%><%=gpmModel%><%
								Collection<ErpProductInfoModel> searchResults = factory.findProductsBySapId(gpmModel.toString());
								Iterator<ErpProductInfoModel> srI=searchResults.iterator();
								if(srI.hasNext()){
									ErpProductInfoModel pim = (ErpProductInfoModel)srI.next();
									%><%= " (Sku:"+pim.getSkuCode()+")"%><% } %><% if (mI_skus.hasNext()){ %>,<br /><% } %>
						<% } %>
						</td>
					</tr>
					</table>
				</td>
			</tr>
		<%
		}else{
		%>
			<tr>
				<td align="right">GroupId: </td><td class="error"><%=grpId%> is null!</td> 
			</tr>
		<%
		}
		%>
			</table>
		<%
	}
	
	String matId=request.getParameter("matId");
	if(matId != null && !"".equals(matId)) {
		//Display the groups attached to this material
	%>
		<table border='1' width='50%' cellpadding="3" cellspacing="0">
			<tr>
				<th align="center" style="font-size: 14px;" width="150">GROUPS ATTACHED TO MATERIAL</th>
				<td>
				<% if (zoneId!=null && !"".equals(zoneId)) { %>
					<table border="0" cellpadding="3" cellspacing="0" width="100%">
					<tr>
						<td align="right">SERVICE TYPE :</td>
						<td><%=serviceType.getName()%></td>
					</tr>
					<tr>
						<td align="right">ZIP CODE :</td>
						<td><%=zipCode%></td>
					</tr>
					<tr>
						<td align="right">ZONE ID :</td>
						<td><%=zoneId%></td>
					</tr>					
					</table>
				<% } %>
				</td>
			</tr>
			<tr>
						<td align="right">MATERIAL ID</td>
						<td><%=matId%></td>
					</tr>
				
	<%
		ErpFactory factory = ErpFactory.getInstance();
		Collection<FDGroup> groups = null;
		try {
			groups = factory.findGrpsForMaterial(matId);
		} catch (Exception e) {
		%>
			<tr>
				<td align="right"><b>Groups </b></td>
				<td><b>No groups found for this SKU</b></td>
			</tr>
		<%
			return;
		}
		%>
					<tr>
						<td align="right"><b>No. of groups this material is attached to </b></td>
						<td><b><%=groups.size()%></b></td>
					</tr>
		<%
		Iterator<FDGroup> gI = groups.iterator();
		while(gI.hasNext()) {		
			FDGroup group = (FDGroup) gI.next();
			GroupScalePricing model1 = null;
			try {
				model1=FDCachedFactory.getGrpInfo(new FDGroup(group.getGroupId(), group.getVersion()));
			}catch(FDGroupNotFoundException fe){
				//do nothing
			} 
			if (model1 != null) {

			%>
			<table border='4' width='50%' cellpadding="3" cellspacing="0">
				<tr>
					<td align="right">GroupId :</td><td><%=model1.getGroupId()%></td>
				</tr>
				<tr>
					<td align="right"> GRP SHORT DESC :</td><td><%=model1.getShortDesc()%></td>
				</tr>
				<tr>
					<td align="right"> GRP LONG DESC :</td><td><%=model1.getLongDesc()%></td>
				</tr>
				<tr>
					<td align="right"> ACTIVE :</td><td><%=model1.isActive()%></td>
				</tr>
				<tr>
					<td align="right"> Version :</td><td><%=model1.getVersion()%></td>
				</tr>
				<tr>
					<td align="right"> GrpZonePriceModel :</td><td>
						<table>
						<%
							Collection<GrpZonePriceModel> gpm = model1.getGrpZonePriceList().getGrpZonePrices();
							Iterator<GrpZonePriceModel> gpmI=gpm.iterator();
							while(gpmI.hasNext()){
								GrpZonePriceModel gpModel=(GrpZonePriceModel)gpmI.next();
								%>
									<tr>
										<td align="right">ZONE ID :</td>
											<td><%=gpModel.getSapZoneId()%></td>
									</tr>
									<%
										MaterialPrice[] matPrices = gpModel.getMaterialPrices();
										for(int i = 0; i < matPrices.length; i++) {
									%>
									
									<tr>
										<td align="right">QTY :</td>
										<td><%=matPrices[i].getScaleLowerBound()%></td>
									</tr>
									<tr>
										<td align="right">SCALE UNIT :</td>
										<td><%=matPrices[i].getPricingUnit()%></td>
									</tr>
									<tr>
										<td align="right">PRICE :</td>
										<td><%=matPrices[i].getPrice()%></td>
									</tr>
									</tr>
									<tr>
										<td align="right">SELLING UNIT :</td>
										<td><%=matPrices[i].getScaleUnit()%></td>
									</tr>		
									<% } %>	
									<% if (gpmI.hasNext()){ %>
										<tr>
											<td colspan="2"><hr /></td>
										</tr>
									<% } %>
								<% } %>
							</table>
						</td>
					</tr>
					<%Set<String> mList = model1.getMatList(); %>					
					<tr>
						<td align="right"> Material List :<br />(with SKUs)</td><td>
							<table>
							<tr>
								<td valign="top" align="right" width="100">SAP ID (Sku) :</td><td>
							<%
								Iterator<String> mI_skus=mList.iterator();
								while(mI_skus.hasNext()) {
									Object gpmModel=mI_skus.next();
									%><%=gpmModel%><%
										Collection<ErpProductInfoModel> searchResults = factory.findProductsBySapId(gpmModel.toString());
										Iterator<ErpProductInfoModel> srI=searchResults.iterator();
										if(srI.hasNext()){
											ErpProductInfoModel pim = (ErpProductInfoModel)srI.next();
											FDProductInfo prodInfo=FDCachedFactory.getProductInfo(pim.getSkuCode());
											%><%= " (Sku:"+pim.getSkuCode()+")"%>
											<%	double price = pim.getMaterialPrices()[0].getPrice();
												boolean redText = false;
												gpmI = gpm.iterator();
												while(gpmI.hasNext()){
													GrpZonePriceModel gpModel=(GrpZonePriceModel)gpmI.next();
													MaterialPrice[] matPrices = gpModel.getMaterialPrices();
													for(int i = 0; i < matPrices.length; i++) {
														if(price < matPrices[i].getPrice()) {
															redText = true;
															break;
														}
													}
												}
												if(redText) {
											%>
												<span style="background:red; color: white;">
												<%= "(Material Price: "  + pim.getMaterialPrices()[0].getPrice() + " ) (Promo Price: " + pim.getMaterialPrices()[0].getPromoPrice()  + " )"%>
												</span>
											<% } else { %>
												<%= "(Material Price: "  + pim.getMaterialPrices()[0].getPrice() + " ) (Promo Price: " + pim.getMaterialPrices()[0].getPromoPrice()  + " )"%>
											<% } } %> 											
											<% if (mI_skus.hasNext()){ %>,<br /><% } %>
								<% } %>
								</td>
							</tr>
							</table>
						</td>
					</tr>
				<%
				}else{
				%>
					<tr>
						<td align="right">GroupId: </td><td class="error"><%=grpId%> is null!</td> 
					</tr>
				<%
				}
				
				%>
					</table>
				<%
				
			}
			%>
					</table>
			<%
		}
	
%>

<div id="error" style="clear: both;"></div>

</body>
</html>