<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.erp.model.*" %>
<%@ page import="com.freshdirect.erp.EnumFeaturedHeaderType" %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.storeapi.content.ProductModel" %>
<%@ page import="com.freshdirect.webapp.taglib.productpromotion.ErpProductPromotionUtil" %>
<%@ page import="com.freshdirect.customer.ErpZoneMasterInfo" %>
<%@ page import="com.freshdirect.fdstore.FDProductPromotionInfo" %>
<%@ page import="com.freshdirect.storeapi.content.SkuModel" %>
<%@ page import="com.freshdirect.storeapi.content.ContentFactory" %>
<%@ page import="com.freshdirect.cms.core.domain.ContentKey" %>
<%@ page import="com.freshdirect.storeapi.fdstore.FDContentTypes" %>
<%@ page import="com.freshdirect.storeapi.ProductModelPromotionAdapter" %>
<fd:CheckLoginStatus id="user" />
<%
	// DO NOT ALLOW customers here!
	if (user == null || user.getMasqueradeContext() == null) {
		// response.sendRedirect("/");
		// return;
	}
%>
<tmpl:insert template='/common/template/no_border_nonav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Email Products"/>
  </tmpl:put>
  <tmpl:put name='title'>FreshDirect - Email Products</tmpl:put>

<tmpl:put name='content' direct='true'>
	<%
			String ppId = request.getParameter("pp_id");
			String successPage = "";
		%>
	<fd:PPExportEmail id="zoneSkusMap1">
	<br/>
	<form>
	<table class="BG_live" width="100%" style="border-bottom:2px solid #000000;border-top:1px solid #000000;">
				<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
				<tr >
				<td class="promo_page_header_text" colspan='10'><b>Product Promotion Type:</b> President's Picks</td>	
				<td align="center"><input type="button" class="promo_btn_grn" id="exportEmail" name="exportEmail" value=" EXPORT " onclick="javascript:formSubmit();"/></td>			
				</tr>
				<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
				</table>
				<br/><br/>
	
	<table>	<tr><td><b>TAG</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   </td><td><b>Key(PRICING_ZONE)</b></td><td>&nbsp;&nbsp;&nbsp;<b>H</b></td></tr></table>
	<div id="div1" style="overflow: scroll; height: 70%;border:2px solid;">	<%=zoneSkusMap1 %></div>
	
	</form>
	</fd:PPExportEmail>
	
<script>
function formSubmit(){
	document.forms[0].action='/agent/ppicks_export_email_products.jsp?exportEmail=true';
	document.forms[0].submit();
}
</script>
	
	</tmpl:put>
</tmpl:insert>
	