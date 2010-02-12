<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.content.util.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus />
<%
String catId = request.getParameter("catId"); 
String filterValue = request.getParameter("filter");
ContentNodeModel currentFolder=ContentFactory.getInstance().getContentNode(catId);
final CategoryModel categoryModel = (currentFolder instanceof CategoryModel) ? (CategoryModel) currentFolder : null;

boolean hasAlcohol=categoryModel != null && categoryModel.isHavingBeer();
FDSessionUser yser = (FDSessionUser)session.getAttribute(SessionName.USER);
if(hasAlcohol && !yser.isHealthWarningAcknowledged()){
    String redirectURL = "/health_warning.jsp?successPage=/wine_region.jsp"+URLEncoder.encode("?"+request.getQueryString());
    response.sendRedirect(response.encodeRedirectURL(redirectURL));
    return;
}
List displayList = new ArrayList();
Domain wcDomain = ContentFactory.getInstance().getDomainById("wine_country");
List wcValues = new ArrayList();
if (wcDomain!=null) {
   for (Iterator dItr = wcDomain.getDomainValues().iterator(); dItr.hasNext();){
     wcValues.add(((DomainValue) dItr.next()).getValue());
   }
}
Collections.sort(wcValues);
%>
<fd:ItemGrabber category='<%=currentFolder %>' id='rtnColl'  
        depth='<%=99%>' ignoreShowChildren='<%=true%>' 
        filterDiscontinued='<%=true%>'
        returnHiddenFolders='<%=true%>'  
        ignoreDuplicateProducts='<%=true%>'
        returnSecondaryFolders='<%=true%>'>
<%
request.setAttribute("itemGrabberResult",rtnColl); //** expose result of item grabber to the layout **
Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();
for(Iterator availItr = sortedColl.iterator();availItr.hasNext();) {
    ContentNodeModel contentNode  = (ContentNodeModel)availItr.next();
    if (contentNode.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
      if (((ProductModel)contentNode).isUnavailable()) continue;
        displayList.add(contentNode);
    }
}
%>
    </fd:ItemGrabber> 
<%
List sortStrategy = new ArrayList();
sortStrategy.add(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY, false));
sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_WINE_COUNTRY, false));
sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, SortStrategyElement.SORTNAME_NAV, false));
%>
<fd:ItemSorter nodes='<%=displayList%>' strategy='<%=sortStrategy%>'/>

<tmpl:insert template='/common/template/bestcellars/no_rightnav.jsp'>
 <tmpl:put name='title' direct='true'>FreshDirect - <%= currentFolder.getFullName() %></tmpl:put>
  <tmpl:put name='content' direct='true'>
   <oscache:cache time="3600" key='<%="winebyregion/"+request.getQueryString() %>'>
  <script>
  <!--
      function doFilter(filterValue) {
          var winURL = "/wine_region.jsp?catId=<%=catId%>";
          if (filterValue!='') {
            winURL = winURL+"&filter="+escape(filterValue);
          }
         window.location=winURL;
      }
  // -->
  </script>
  <form name="filter_form" action="">
   <table border="0" cellspacing="0" cellpadding="0" width="100%">
    <tr>
     <td valign="bottom" align="left" style="padding-bottom:10px;"><img src="/media_stat/images/layout/show_only.gif" width="98" height="12">&nbsp;&nbsp;
      <select name="filter" onChange="doFilter(this.options[this.selectedIndex].value);">
        <option value="">All Regions</option>
<%
       for (Iterator wcItr = wcValues.iterator(); wcItr.hasNext();) {   
          String wcValue = (String)wcItr.next(); %>
         <option <%=(wcValue.equals(filterValue) ? "selected" :"" )%> value="<%=wcValue%>"><%=wcValue%></option>
<%     }     %>
      </select>
<%    if (filterValue!=null && !"".equals(filterValue)) { %>
        &nbsp;&nbsp;<a href="/wine_region.jsp?catId=<%=catId%>">show all</a>
<%    }  %>
     </td>
    </tr>
   </table>
  </form>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
<%
  String lastCatId ="";
  String lastFilterValue = null;
  boolean shownALabel=false;
  for(Iterator prodItr = displayList.iterator(); prodItr.hasNext();) { 
      ProductModel displayProd = (ProductModel)prodItr.next();
      CategoryModel parentCat = (CategoryModel)displayProd.getParentNode();
      String brandName = displayProd.getPrimaryBrandName();
      String prodName = displayProd.getFullName();
      String  wineRegion = displayProd.getWineRegion();
      DomainValue _wineCountry= displayProd.getWineCountry();
      String curFilterValue = _wineCountry==null ? "Other" 
        : _wineCountry.getLabel();
      //skip items that do not match the filtered value, if one is specified.
      if (filterValue!=null && !"".equals(filterValue)&& curFilterValue!=null && !filterValue.equalsIgnoreCase(curFilterValue)) {
          continue;
      }
      if (brandName!=null && brandName.length()>0 && displayProd.getFullName().startsWith(brandName)) {
                prodName=displayProd.getFullName().substring(brandName.length()).trim();
        }
        if (!lastCatId.equalsIgnoreCase(parentCat.getContentName())) {
          lastFilterValue=null;
          lastCatId = parentCat.getContentName();
          Image _img  = parentCat.getCategoryLabel();
          String imgPath = _img!=null ?  _img.getPath()  : "/media_stat/images/layout/clear.gif";
         
%>
        <tr><td><%if (shownALabel){%><br><%}%><img src="<%=imgPath%>"></td></tr>
<%        shownALabel = true;
      }
      if (lastFilterValue==null || !lastFilterValue.equalsIgnoreCase(curFilterValue)) {
          lastFilterValue = curFilterValue;
%>
           <tr><td><br><b><font color="#990099"><%=curFilterValue.toUpperCase()%><b></font><font class="space2pix"><br></font></td></tr>
<%
      }
        SkuModel sku = displayProd.getDefaultSku();
        String prodPrice = null;
%>
        <fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
<%      prodPrice = JspMethods.currencyFormatter.format(productInfo.getZonePriceInfo(yser.getPricingContext().getZoneId()).getDefaultPrice()); %>                      
        </fd:FDProductInfo>
      <tr><td><b><a href="/product.jsp?catId=<%=parentCat%>&productId=<%=displayProd%>&trk=cpage"><%=prodName%></a></b>&nbsp;<%=(wineRegion!=null ? "("+wineRegion+")":"")%>&nbsp;-&nbsp;<b><%=prodPrice%></b></td></tr>
<%
  }  
%>
</table>
</oscache:cache>
 </tmpl:put>
</tmpl:insert>
