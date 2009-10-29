<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
        
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.freshdirect.fdstore.promotion.PromoVariantModel"%>    
<%@page import="com.freshdirect.fdstore.promotion.*"%>   
<%@page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@page import="java.util.Collection"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>VARIANT CONFIGURATIONS PAGE</title>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style type="text/css">

body{margin:20px 60px;color:#333333;background-color:#fff;}
input{font-weight:normal;}
p{margin:0px;padding:0px 0px 15px;}
p.head{padding:10px 0px 20px;}
a{color:#336600;}.test-page a:VISITED{color:#336600;}
table{border-collapse:collapse;border-spacing:0px;width:100%;}
table.t1{width:auto;margin-bottom:20px; border: 3px solid black;}
table.t1 td{border:1px solid black;padding:4px 8px;}
table.t1 td.sf{background-color:#ccc;}
table.t1 td.space{border-width:0px 0px;}
table.t1 td.expander{padding:15px 0px 0px;}
table.t1 td.in-use{border-width:0px;padding:10px 0px 4px;}
.info{color:red}
.no-use{color:#999 !important;}
td.no-use{border-color:#999 !important;}
.faulty{border-color:red !important;}
.erring{color:red; !important}
.warning{color:#FF6633; !important}
.unused{color:green; !important}
.valid{font-weight:bold;}
.overridden{text-decoration: underline;}
.default{font-style: italic;}
.hidden{display: none;}
.visible{display: block;}
.hand{cursor: pointer;}
	</style>
</head>
<body>



<%

URLGenerator urlG = new URLGenerator(request);

PromoVariantCache cache=PromoVariantCache.getInstance();
if (urlG.get("refresh") != null) {
	PromoVariantCache.getInstance().refreshAll();
	urlG.remove("refresh");
	urlG.set("reloaded", "1");
	String newURL = urlG.build();
	response.sendRedirect(StringEscapeUtils.unescapeHtml(newURL));
	return;
} 


java.util.Map promoMap=cache.getPromoVariantMap();
Collection promoList=cache.getPromoVariantIds();

%>
<b2> Total number of PromoVariants <%=promoList.size()%></b2>
<%
if(promoList!=null){
           
    for(Iterator iter = promoList.iterator(); iter.hasNext();) {
    
     String variantId=(String)iter.next();     
     java.util.List pvlist = (java.util.List) cache.getAllPromoVariants(variantId);          
     
     if(pvlist==null || pvlist.size()==0) continue;
  
     for(int j=0;j<pvlist.size();j++){     
       PromoVariantModel pv = (PromoVariantModel)pvlist.get(j);
      
 %>    
 
 <div style="padding: 10px 0px;" title="Site Feature">  
 
<table class="t1" border="1" >
        <tr>
    		<td class="text13bold space" colspan="2">
               VariantId : <%=pv.getVariantId()%>
    		</td>
    	</tr>
        <tr>
            <td class="text13bold space" colspan="2">
               promoCode: <%=pv.getAssignedPromotion().getPromotionCode()%>
    		</td>
    	</tr>
        <tr>
    		<td class="text13bold space" colspan="2">
               Promo Priority : <%=pv.getPromoPriority()%>
    		</td>
    	</tr>
        <tr>
    		<td class="text13bold space" colspan="2">
               Variant Priority : <%=pv.getVariantPriority()%>
    		</td>
    	</tr>        
        <tr>
            <td class="text13bold space" colspan="2">
               Site Feature: <%=pv.getSiteFeature().getTitle()%>
    		</td>
    	</tr>

</table>
 
<%
     }
 %>
   <br>
 <%
   }
}
%>


</div>

</body>
</html>