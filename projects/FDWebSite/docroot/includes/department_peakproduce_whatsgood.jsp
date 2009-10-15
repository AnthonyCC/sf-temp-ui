<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Comparator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Collections"%>
<%@ page import="java.util.Iterator"%>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<fd:ProduceRatingCheck>

<%
  //var defines
  int ppTarget = 6;
  int ppVeg = 0;
  int ppFru = 0;

  Collection vegCol = null;
  Collection fruCol = null;

	rowColl = new ArrayList();
  
  catId = "picks_pres";
  //request.getParameter("catId");
  currentFolder = ContentFactory.getInstance().getContentNodeByName(catId);
  isDept = (currentFolder instanceof DepartmentModel);
  isCat = (currentFolder instanceof CategoryModel);
  trkCode= "";
  //** pass the code that should be used as the tracking code **/
  if (isDept) {
    trkCode = "dpage";
    request.setAttribute("trk","dpage");
  } else if(isCat) {
    trkCode = "cpage";
    request.setAttribute("trk","cpage");
  }

%>



  
  <fd:GetPeakProduce deptId='fru' id='peakProduces' useMinCount='false'>
    <% fruCol = peakProduces; %>
  </fd:GetPeakProduce>

  <fd:GetPeakProduce deptId='veg' id='peakProduces' useMinCount='false'>
    <% vegCol = peakProduces; %>
  </fd:GetPeakProduce>

  <%
    //iterate over vegCol and remove dupes contained in fruCol or globalColl
    Iterator iteratorVeg = vegCol.iterator();
    while (iteratorVeg.hasNext()) {
      Object element = iteratorVeg.next();
      if (fruCol.contains(element) || globalColl.contains(element)) {
        iteratorVeg.remove();
      }
    }
	//now iterate over fruCol and remove dupes contained in globalColl
    Iterator iteratorFru = fruCol.iterator();
    while (iteratorFru.hasNext()) {
      Object element = iteratorFru.next();
      if (globalColl.contains(element)) {
        iteratorFru.remove();
      }
    }
    //set sizes
    ppVeg = vegCol.size();
    ppFru = fruCol.size();

    //logic to determine products from each dept to show
    if (ppVeg > 3) {
      //veg = 3
      ppVeg = 3;
      if (ppFru > 3){
        //veg = 3, fru = 3
        ppVeg = 3;
        ppFru = 3;
        //done
      }else{
        //fru<3
        //veg = tar-fru
        ppVeg = ppTarget-ppFru;
      }
    }else{
      //fru = tar - veg
      ppFru = ppTarget-ppVeg;
    }
    %>

    <% if (ppFru > 0 || ppVeg > 0) { // min to require to display section %>
      <fd:IncludeMedia name="/media/editorial/whats_good/whats_good_line.html" />
      <fd:IncludeMedia name= "/media/editorial/whats_good/whats_good_GRN_above.html"/>
      <table cellpadding="0" cellspacing="0" border="0">
      <tr valign="bottom">
        <logic:iterate id="peakProduce" collection="<%= fruCol %>" type="com.freshdirect.fdstore.content.SkuModel" indexId="idx">
        <% if (idx.intValue() >= ppFru || globalColl.contains(peakProduce.getProductModel())) { continue; }
          String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
          DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response, 
              peakProduce.getProductModel().getParentNode().getContentName(), 
              peakProduce.getProductModel(), prodNameAttribute, 
              true);
          int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
          String actionUrl = FDURLUtil.getProductURI( peakProduce.getProductModel(), trkCode );

			//add to global collection for dupe check
			globalColl.add(peakProduce.getProductModel());
			//add to pageColl for prices ref
			rowColl.add(peakProduce.getProductModel());
        %>
          
          <td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
            <display:ProductImage product="<%= peakProduce.getProductModel() %>" hideDeals="false" showRolloverImage="false" action="<%= actionUrl %>"/>
          </td>
        </logic:iterate>
        <logic:iterate id="peakProduce" collection="<%= vegCol %>" type="com.freshdirect.fdstore.content.SkuModel" indexId="idx">
        <% if (idx.intValue() >= ppVeg || globalColl.contains(peakProduce.getProductModel())) { continue; }
          String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
          DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response, 
              peakProduce.getProductModel().getParentNode().getContentName(), 
              peakProduce.getProductModel(), prodNameAttribute, 
              true);
          int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
          String actionUrl = FDURLUtil.getProductURI( peakProduce.getProductModel(), trkCode );

			//add to global collection for dupe check
			globalColl.add(peakProduce.getProductModel());
			//add to pageColl for prices ref
			rowColl.add(peakProduce.getProductModel());
        %>
          
          <td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
            <display:ProductImage product="<%= peakProduce.getProductModel() %>" hideDeals="true" showRolloverImage="false" action="<%= actionUrl %>"/>
          </td>
        </logic:iterate>
      </tr>
      <tr valign="top">
        <logic:iterate id="peakProduce" collection="<%= fruCol %>" type="com.freshdirect.fdstore.content.SkuModel" indexId="idx">
        
        <% if (idx.intValue() >= ppFru || !rowColl.contains(peakProduce.getProductModel())) { continue; }
          String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
		  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,peakProduce.getProductModel().getParentNode().getContentName(), peakProduce.getProductModel(), prodNameAttribute, true);
          int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
          String actionUrl = FDURLUtil.getProductURI( peakProduce.getProductModel(), trkCode );
        %>
          
          <td valign="top" width="<%=adjustedImgWidth%>" align="center" style="padding-left:5px; padding-right:5px;padding-bottom:10px;">
            <div class="WG_deals">
	            <display:ProductRating product="<%= peakProduce.getProductModel() %>" action="<%= actionUrl %>"/>
				<display:ProductName product="<%= peakProduce.getProductModel() %>" action="<%= actionUrl %>"/>
				<display:ProductPrice impression="<%= new ProductImpression(peakProduce.getProductModel()) %>" showDescription="false"/>
            </div>
          </td>
        </logic:iterate>



        <logic:iterate id="peakProduce" collection="<%= vegCol %>" type="com.freshdirect.fdstore.content.SkuModel" indexId="idx">
        <% if (idx.intValue() >= ppVeg || !rowColl.contains(peakProduce.getProductModel())) { continue; }
          String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
		  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,peakProduce.getProductModel().getParentNode().getContentName(), peakProduce.getProductModel(), prodNameAttribute, true);
          int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
          String actionUrl = FDURLUtil.getProductURI( peakProduce.getProductModel(), trkCode );
        %>
          
          <td valign="top" width="<%=adjustedImgWidth%>" align="center" style="padding-left:5px; padding-right:5px;padding-bottom:10px;">
            

            <div style="font-weight: normal; font-size: 8pt; width: 100px; text-align: center">
            	<display:ProductRating product="<%= peakProduce.getProductModel() %>" action="<%= actionUrl %>"/>
				<display:ProductName product="<%= peakProduce.getProductModel() %>" action="<%= actionUrl %>"/>
				<display:ProductPrice impression="<%= new ProductImpression(peakProduce.getProductModel()) %>" showDescription="false"/>
            </div>

          </td>
        </logic:iterate>
      </tr>
      </table>
    <%
	} 
	
%>
</fd:ProduceRatingCheck>