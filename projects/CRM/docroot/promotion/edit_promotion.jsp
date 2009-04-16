<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ page import="com.freshdirect.fdstore.promotion.management.FDPromotionModel"%>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionType"%>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import="com.freshdirect.framework.util.EnumMonth"%>
<%@ page import="com.freshdirect.framework.core.PrimaryKey"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>


<% String promoId = request.getParameter("promoId");%>
<fd:GetPromotion id="promotion" promotionId="<%=promoId%>">
<%
  String successPage = "update_promotion.jsp?promoId="+promoId; 
  boolean forPrint = "print".equalsIgnoreCase(request.getParameter("for"));
  boolean isViewOnly = false;
  boolean isVariantExist=false;
  if(promotion.getPromoVariants()!=null && promotion.getPromoVariants().size()>0){ isVariantExist=true; }
  
  String tmpl = "/template/" + (forPrint ? "print" : "top_nav") + ".jsp";
  TreeMap zipRestrictionMap = promotion.getZipRestrictions();
  Map profileAttributeNames = FDCustomerManager.loadProfileAttributeNames();
  
  String actionName="edit_promotion";
  
  if(isVariantExist) actionName="edit_variant_promotion";
  
  System.out.println("isVariantExist :"+isVariantExist);
  
%>
<fd:PromotionController promotion="<%=promotion%>" actionName="<%=actionName%>" result="result" successPage="<%=successPage%>" zipRestrictionMap="<%=zipRestrictionMap%>">  
<tmpl:insert template='<%=tmpl%>'>
    <tmpl:put name='title' direct='true'>Edit Promotion</tmpl:put>
    <tmpl:put name='content' direct='true'>
	<div style="background: #FFFFFF;">
    <%
       System.out.println("forPrint :"+forPrint);
    %>
    
		<%if(!forPrint){%>
            <div class="sub_nav" style="text-align: left;">
            <table cellpadding="0" cellspacing="0" border="0" width="99%">
            <form  name="editPromotions" method="POST">
            <tr><td width="35%"><span class="sub_nav_title">Edit Promotion</span>&nbsp;&nbsp; <span class="note">* Required</span></td>
            <%if(result.isFailure()) { %>	
			<td nowrap align="left">&nbsp;&nbsp;<span class="error">* Promotion has errors</span>&nbsp;&nbsp;</td>
			<%} %>
            <td width="30%"><input type="reset" value="CLEAR" class="clear"><input type="submit" value="SUBMIT PROMOTION" class="submit"></td>
            <td width="20%"align="right">(<a href="javascript:pop('/promotion/edit_promotion.jsp?promoId=<%= promoId %>&for=print','680','800')">Print Version</a>)&nbsp;&nbsp;<a href="/main/available_promotions.jsp">View All Promotions &raquo;</a></td>
            </tr>
            </table>
            </div>  
           <%  if(isVariantExist){  %>
                <%@ include file="/includes/edit_variant_promotion.jspf" %>
           <% }else{  %>
           <%@ include file="/includes/promotion_fields.jspf" %>
           <%  }  %>             
        <%}%>
        <%if(forPrint) {%>
            <%@ include file="/includes/view_promotion_data.jspf" %>
        <%}%>
	</div>
    </form>
</tmpl:put>
</tmpl:insert>
</fd:PromotionController>
</fd:GetPromotion>