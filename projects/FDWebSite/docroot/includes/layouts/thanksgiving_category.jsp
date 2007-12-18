<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.util.*'%>
<%@ page import='java.net.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus guestAllowed="true" />
<%
String catId = request.getParameter("catId");
boolean isGroceryVirtual=false;
ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNodeByName(catId);
boolean allSoldOut=true;
boolean previewMode = ContentFactory.getInstance().getPreviewMode();
//on Nov 28, 12am They want to see the holiday meals coming soon content, so use the cut-off logic
Calendar cutOffPoint = new GregorianCalendar(2004,Calendar.NOVEMBER,27,23,59,59);
Calendar rightNow = Calendar.getInstance();
boolean pastCutOff = !previewMode && !rightNow.before(cutOffPoint);
Attribute middleMedia = currentFolder.getAttribute("MIDDLE_MEDIA");
String midMediaPath=null;
if (middleMedia!=null) {
    midMediaPath = ((MediaI)middleMedia.getValue()).getPath();
}
midMediaPath="/media_stat/images/thanksgiving/thanks_warning_main.txt";

Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();
        for(Iterator itr=sortedColl.iterator(); itr.hasNext() && allSoldOut;) {
            ContentNodeModel cn = (ContentNodeModel) itr.next();
            if (!cn.getContentType().equals(ContentNodeI.TYPE_PRODUCT)) continue;
            if ( !((ProductModel)cn).isUnavailable()) {
               allSoldOut=false;
            }
        }

%>

<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
<TABLE border=0 cellPadding=0 cellSpacing=0 width=540>
<%
  if (!allSoldOut && !pastCutOff) {  %>
<TR><TD colspan="3">
<IMG src="/media_stat/images/thanksgiving/thanksgiving_hdr.gif" WIDTH="534" HEIGHT="60" BORDER="0" alt="THANKSGIVING Turkey DINNER">
<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="1"></td>
</TR>
<tr>
    <td valign="top">
        Your turn to cook this year?  We have you covered -
        from our gorgeous roasted turkey to spiced pumpkin pie, with every glorious morsel in between.
        Our chefs will be preparing every dish using top quality ingredients, family recipes, and professional know-how.
        When we bring it to your door, <b>all you'll have to do is heat and serve</b>
        <BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10"><BR>
        <% if (midMediaPath!=null) {  %>
 	<fd:IncludeMedia name='<%= midMediaPath %>' />
        <% } %>
        <img src="/media_stat/images/thanksgiving/choose_size_info.gif" width="206" height="10" border="0">
<%
        for(Iterator itr=sortedColl.iterator(); itr.hasNext();) {
            ContentNodeModel cn = (ContentNodeModel) itr.next();
            if (!cn.getContentType().equals(ContentNodeI.TYPE_PRODUCT)) continue;

            ProductModel pm = (ProductModel)cn;
            DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,pm.getParentNode().getContentName(),pm,"full",true);
            boolean prodUnavailable = pm.isUnavailable();
            SkuModel defaultSku = !prodUnavailable?pm.getDefaultSku():(SkuModel)pm.getSkus().get(0);
            String suLabel = "".equals(pm.getAttribute("SALES_UNIT_LABEL",""))
                ? ""
                : " (Serves " + pm.getAttribute("SALES_UNIT_LABEL","")+")";
%>
            <fd:FDProductInfo id="productInfo" skuCode="<%= defaultSku.getSkuCode() %>">
<%
               FDProduct fdProduct = null;

               try {
                    fdProduct = FDCachedFactory.getProduct(productInfo);
                    FDSalesUnit[] salesUnits = fdProduct.getSalesUnits();
                         if (salesUnits.length>0) {
                             String salesUnitDescription = " ("+salesUnits[0].getDescription()+")";
                             String salesUnitName = salesUnits[0].getName();
                         //salesUnitNames.add(salesUnitName);
                         }
               } catch (FDSkuNotFoundException fdsnf){
                    JspLogger.PRODUCT.warn("ThnkGivMenu.jsp: catching FDSkuNotFoundException and Continuing:\n FDProductInfo:="+productInfo+"\nException message:= "+fdsnf.getMessage());
               }    %>
</fd:FDProductInfo>
              <font class="space4pix"><br><br></font>
<%            if (pm.isUnavailable()) {     %>
                 <font color="#cccccc"><b><%=pm.getFullName()%></b></a><%=suLabel %> - </font><font color="#ff0000"><b>SOLD OUT</b></font>
<%            } else {   %>
                <a href="/product.jsp?catId=<%=pm.getParentNode()%>&productId=<%=pm%>"><b><%=pm.getFullName()%></b></a><%= suLabel %> - <b><%=displayObj.getPrice()%></b>
<%            }
        }   %>
        <BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10"><BR>
        <b>All dinners include:</b>
        <br>Roasted Turkey
        <br>Gravy
        <br>Choice of Stuffing
        <br>Choice of Four Sides
        <br>Cranberry Sauce
        <br>Dinner rolls
        <br>Zucchini Bread
        <br>Choice of Dessert
<% //        <br><br><a href="javascript:popup('/departments/hmr/thanksgiving_instructions.jsp','large')">Click here for heating instructions.</a> %>
    </td>
    <td width="10"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="10" HEIGHT="1"></td>
    <td valign="top">
       <img src="/media_stat/images/thanksgiving/home_turkey.jpg" width="180" height="130" border="0">
    </td>
</tr>
<tr><td colspan="3"><br><br>Customers who have already ordered their Thanksgiving Dinner from FreshDirect can click here for heating instructions for:
  <a href="javascript:popup('/media_stat/pdf/freshdirect_12_14_lb_turkey_dinner.pdf','large')">12-14 lb turkey</a>,
  <a href="javascript:popup('/media_stat/pdf/freshdirect_18_20_lb_turkey_dinner.pdf','large')">18-20 lb turkey</a>, 
  <a href="javascript:popup('/media_stat/pdf/freshdirect_22_24_lb_turkey_dinner.pdf','large')">22-24 lb turkey</a>
</td></tr>
<% } else if (!pastCutOff) { //********* all items sold out, paint sold-out page *********   %>
<TR><TD colspan="3" align="center">
<IMG src="/media_stat/images/thanksgiving/thanksgiving_hdr_soldout.gif" WIDTH="394" HEIGHT="21" BORDER="0" alt="THANKSGIVING TURKEY DINNER">
<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10"></td>
</TR>
<tr><td bgcolor="#334455" colspan="3"></td></tr>
 <tr>
    <td valign="top">
       <font class="space4pix"><br></font>
        <b>Due to overwhelming demand we are sold out of our Thanksgiving turkey dinners.</b>
       <BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="30"><BR>
       <table cellpadding="0" cellspacing="0" border="0">
              <tr>
                <td valign="top"><img src="/media_stat/images/thanksgiving/mea_manager_chicken.jpg" border="0"></td>
                <td valign="top"><img src="/media_stat/images/thanksgiving/holiday_meats.gif" border="0"><font class="space4pix"><br></font>
                 Visit our <a href="/category.jsp?catId=mea_holiday&trk=thks">Holiday Selections</a> page in our Meat Department for turkeys, chickens, and roasts.
                  <font class="space4pix"><br><br><br></font>
                 <img src="/media_stat/images/thanksgiving/our_thanksgiving_picks.gif" border="0"><font class="space4pix"><br><br></font>
                  Our <a href="/department.jsp?deptId=our_picks&trk=thks">Thanksgiving Picks</a> page has everything you need to make your own Thanksgiving a spectacular one.

              </tr>
       </table>
     </td>
    <td width="10"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="10" HEIGHT="1"></td>
    <td valign="top">
       <font class="space4pix"><br></font>
       <img src="/media_stat/images/thanksgiving/home_turkey_sold_out.jpg" width="180" height="130" border="0">
    </td>
</tr>
<tr><td colspan="3">Customers who have already ordered their Thanksgiving Dinner from FreshDirect can click here for heating instructions for:
  <a href="javascript:popup('/media_stat/pdf/freshdirect_12_14_lb_turkey_dinner.pdf','large')">12-14 lb turkey</a>,
  <a href="javascript:popup('/media_stat/pdf/freshdirect_18_20_lb_turkey_dinner.pdf','large')">18-20 lb turkey</a>, 
  <a href="javascript:popup('/media_stat/pdf/freshdirect_22_24_lb_turkey_dinner.pdf','large')">22-24 lb turkey</a>
</td></tr>
<tr><td colspan="3">
   <BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="100" border="0"><BR>
</td></tr>
<% } else { /********* cut off point reached *************/%>
 <tr><td align="center">
    <img src="/media_stat/images/thanksgiving/hldy_comingsoon_hdr.gif" border="0"><br><br>
    <img src="/media_stat/images/thanksgiving/hldy_comingsoon_img.jpg" border="0"><br><br>
    Executive Chef Michael Stark and his team are creating special Holiday menus just in time for Hannukah, 
    Christmas and New Year's <B>Check back soon for details!</B>    
    </td>
</tr>
<% }%>
</TABLE>