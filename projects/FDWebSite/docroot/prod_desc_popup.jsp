<%@ page import='com.freshdirect.storeapi.content.*'  %>
<%@ page import='com.freshdirect.storeapi.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<%
String catId = request.getParameter("catId");
String prodId = request.getParameter("prodId");
String show = request.getParameter("show");

ContentFactory cf = ContentFactory.getInstance();
ProductModel prod =  cf.getProductByName(catId,prodId);
String title = prod.getFullName();
Image prodImg = prod.getCategoryImage();
boolean prodUnavailable = prod.isUnavailable();

Html productDesc = prod.getProductDescription(); //for about
SkuModel defaultSku = !prodUnavailable?prod.getDefaultSku():(SkuModel)prod.getSkus().get(0); //for nutri/ingr/heat
FDProduct fdprd = prodUnavailable? null:defaultSku.getProduct(); //for nutri/ingr/heat

String selfLink = request.getRequestURI() + "?" + "catId=" + catId + "&prodId=" + prodId;

boolean showAbout = false;
     if ( show == null || "".equalsIgnoreCase(show) || "about".equalsIgnoreCase(show) ) {
          showAbout = true;
     }
boolean showNutrition = false;
     if ( "nutrition".equalsIgnoreCase(show) ) {
          showNutrition = true;
     }
boolean showIngredients = false;
     if( "ingredients".equalsIgnoreCase(show) ) {
          showIngredients = true;
     }

List<SkuModel> skus = new ArrayList<SkuModel>(); 
boolean multiple = false;
boolean sameFirstVar = true;
boolean sameSecondVar = true;
String lastFirst = null;
String lastSecond = null;
if (prod.isNutritionMultiple()) {
    multiple = true;
    skus = prod.getSkus();

    for (Iterator<SkuModel> sIter = skus.iterator(); sIter.hasNext(); ) {
        SkuModel sm = sIter.next();
        if (sm.isUnavailable()) {
            sIter.remove();
            continue;
        }
        List<DomainValue> dvals = sm.getVariationMatrix();
        if (dvals != null && dvals.size()>0) {
            DomainValue dv = ((DomainValue) dvals.get(0));
            if (lastFirst == null) {
                lastFirst = dv.getLabel();
            } else if (!dv.getLabel().equals(lastFirst)) {
                lastFirst = dv.getLabel();
                sameFirstVar = false;
            }
            if (dvals.size() > 1) {
                dv = ((DomainValue) dvals.get(1));
                if (lastSecond == null) {
                    lastSecond = dv.getLabel();
                } else if (!dv.getLabel().equals(lastSecond)) {
                    lastSecond = dv.getLabel();
                    sameSecondVar = false;
                }
            }
          }
       }
}
title = "FreshDirect - " + title;
%>

<tmpl:insert template='/common/template/small_pop.jsp'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="<%=title%>"/>
    </tmpl:put>
<%-- 	<tmpl:put name='title' direct='true'><%=title%></tmpl:put> --%>
          <tmpl:put name='content' direct='true'>
          
               <table border="0" cellpadding="0" cellspacing="0" width="330">
               <tr valign="top">
                    <td width="100" align="center">
                    	<div style="margin-bottom: 5px">
                    		<img src="<%=prodImg.getPath()%>" width="<%=prodImg.getWidth()%>" height="<%=prodImg.getHeight()%>" border="0" alt="<%= prod.getFullName() %>">
                    	</div>
                    	<div>
	                    	<display:WineRating product="<%= prod %>"/>
                    	</div>
                    </td>
                    <td width="5"><img src="media_stat/images/layout/clear.gif" width="5" height="1"></td>
                    <td width="225" class="text11">
                    	<div class="text12"><b><%=title%></b></div>
                    	<img src="media_stat/images/layout/cccccc.gif" alt="" width="100%" height="1" vspace="5"><br>
                    <% if ( showAbout ) {%><b>About</b><%} else {%><a href="<%=selfLink%>&show=about">About</a><%}%><% if (fdprd!=null && fdprd.hasNutritionFacts()) {%> | <% if ( showNutrition ) {%><b>Nutrition</b><%} else {%><a href="<%=selfLink%>&show=nutrition">Nutrition</a><%}%><%}%><% if ( fdprd!=null && fdprd.hasIngredients() ) {%> | <% if ( showIngredients ) {%><b>Ingredients</b><%} else {%><a href="<%=selfLink%>&show=ingredients">Ingredients</a><%}%><%}%>
                    <br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8"><br>
                    <% if ( showAbout ) {%>
                         <%@ include file="/shared/includes/product/i_product_about.jspf" %>
                    <% } else if ( showNutrition ) { 
							if (multiple) {
                                if (skus.size() > 1) {
                                    String skuCode = request.getParameter("skuCode");
                                    if (skuCode == null) {
                                        skuCode = prod.getDefaultSku().getSkuCode();
                                    }
                                %>
                                <script language="JavaScript">

                                    function viewChoice(skucode) {
                                        window.location.href = "<%=selfLink%>" + "&show=nutrition&skuCode=" + skucode;
                                    }

                                </script>
                                <form>
                                <select name="skuCode" onChange="javascript:viewChoice(this.options[this.selectedIndex].value)">
                                <%  for (SkuModel s : skus) {
                                        List<DomainValue> dvals = s.getVariationMatrix();
                                        out.print("<option value=\"" + s.getSkuCode() + "\"");
                                        if (s.getSkuCode().equals(skuCode)) {
                                            out.print(" SELECTED");
                                            fdprd = s.getProduct();
                                        }
                                        out.print(" >");
                                        if (!sameFirstVar) out.print(((DomainValue) dvals.get(0)).getLabel());
                                        if (!sameFirstVar && ! sameSecondVar) out.print(" - ");
                                        if (!sameSecondVar) out.print(((DomainValue) dvals.get(1)).getLabel());
										out.print("</option>");
                                    } %>
                                </select>
                                </form>
						<% 		} 
							} %>
							
                         <fd:NutritionPanel skuCode="<%=fdprd.getSkuCode()%>"/>
                         
                         <img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5"><br>
                         <a href="/shared/product_nutrition_note.jsp">An important note about our nutrition and ingredients information.</a><br>
                    <% } else if( showIngredients ) {
						if (multiple) {
                                if (skus.size() > 1) {
                                    String skuCode = request.getParameter("skuCode");
                                    if (skuCode == null) {
                                        skuCode = prod.getDefaultSku().getSkuCode();
                                    }
                                %>
                                <script language="JavaScript">

                                    function viewChoice(skucode) {
                                        window.location.href = "<%=selfLink%>&show=ingredients&skuCode=" + skucode;
                                    }

                                </script>
                                <form>
                                <select name="skuCode" onChange="javascript:viewChoice(this.options[this.selectedIndex].value)">
                                <%  for (SkuModel s : skus) {
                                        List<DomainValue> dvals = s.getVariationMatrix();
                                        out.print("<option value=\"" + s.getSkuCode() + "\"");
                                        if (s.getSkuCode().equals(skuCode)) {
                                            out.print(" SELECTED");
                                            fdprd = s.getProduct();
                                        }
                                        out.print(" >");
                                        if (!sameFirstVar) out.print(((DomainValue) dvals.get(0)).getLabel());
                                        if (!sameFirstVar && ! sameSecondVar) out.print(" - ");
                                        if (!sameSecondVar) out.print(((DomainValue) dvals.get(1)).getLabel());
                                    } %>
                                </select>
                                </form>
                        <%      }
                            }   %>
                         <table border="0" cellpadding="0" cellspacing="0" width="100%">
          	          <tr>
              	          <td>
                  	     <font CLASS="title18">Ingredients</font><br>
                         <img src="/media_stat/images/layout/330000.gif" width="100%" height="6" vspace="2"><br>
                         <%= fdprd.getIngredients() %><br>
                         <img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="5"><br>
                         <a href="/shared/product_nutrition_note.jsp">An important note about our nutrition and ingredients information.</a>
                         </td>
                         </tr>
                         </table>
                    <% } %>
                    <br>
                    <%@ include file="/includes/product/i_heating_instructions.jspf" %>
                    </td>
               <tr>
               </table>
                        
          </tmpl:put>
</tmpl:insert>
