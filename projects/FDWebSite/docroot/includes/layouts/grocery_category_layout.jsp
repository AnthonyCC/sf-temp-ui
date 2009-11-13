<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%!

public void produceBrandsAndTypes(Set brands, List typesList, Collection sortedColl) {
	boolean checkParent = false;
	ContentNodeModel lastNode = null;

	if (sortedColl == null || sortedColl.size() == 0)
		return;

	Iterator itr = sortedColl.iterator();
	while (itr.hasNext()) {
	    ContentNodeModel item = (ContentNodeModel)itr.next();

	    if (checkParent) {
	        checkParent = false;
	        //if the next item is not a product or not a folder that is a descend
	        if(!(item instanceof ProductModel) && !item.getParentNode().getContentName().equals(lastNode.getContentName()) ) {
	            typesList.remove(typesList.size()-1);
	        }
	    }

	    if (item instanceof CategoryModel) {
	        // check to see if there is any children of this category (prod or cat)
	        typesList.add(item);
	        checkParent = true;
	        lastNode = item;
	    } else {
	        List prodBrands = ((ProductModel)item).getBrands();
	        if (prodBrands!=null && prodBrands.size()>0) {
	            for (Iterator brandItr = prodBrands.iterator();brandItr.hasNext();) {
	                brands.add((BrandModel)brandItr.next());
	            }
	        }
	    }
	}	
}

%>
<%

//********** Start of Stuff to let JSPF's become JSP's **************

String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 
boolean isDepartment = false;

ContentNodeModel currentFolder = null;
CategoryModel currentCategory = null;
if (deptId != null) {
    currentFolder=ContentFactory.getInstance().getContentNode(deptId);
    isDepartment = true;
} else if (catId != null) {
    currentFolder=ContentFactory.getInstance().getContentNode(catId);
    if (currentFolder != null)
        currentCategory = (CategoryModel) currentFolder;
}

//Siva-Changed Tracking Code Retrieval
String trkCode = (String)request.getAttribute("trk");
if (trkCode!=null && !"".equals(trkCode.trim()) ) {
    trkCode = "&trk="+trkCode.trim();
} else {
    trkCode = "";
}

// DO render Editorial (if exists)
// [APPREQ-92] skip Editorial on Brand and on Virtual All pages
boolean doRenderEditorialPartial = (request.getParameter("brandValue") == null && !"All".equals(request.getParameter("groceryVirtual")));


Set brands = new TreeSet(ContentNodeModel.FULL_NAME_COMPARATOR);
List typesList = new ArrayList();

produceBrandsAndTypes(brands, typesList, (Collection) request.getAttribute("itemGrabberResult"));

int typesCount = typesList.size();


%>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="550" BORDER="0">
    <TR VALIGN="TOP">
        <TD WIDTH="550">
<%
if (currentCategory != null) {
    //
    // Category Label (Image)
    //
    if (currentCategory.getCategoryLabel() != null) {
        String categoryLabelPath = currentCategory.getCategoryLabel().getPath() /* ((Image)anAttib.getValue()).getPath() */;
%>
            <IMG SRC="<%= categoryLabelPath %>" border="0" ALT="<%= currentCategory.getFullName() %>"><br>
<%
    }
    
    //
    // Render Editorial (partial HTML)
    //
    Html editorialMedia = currentFolder.getEditorial();
    if (doRenderEditorialPartial &&
    		editorialMedia != null &&
    		currentCategory.getCategoryLabel() != null &&
    		!editorialMedia.isBlank()) {
%>
            <img src="/media_stat/images/layout/cccccc.gif" height="1" width="100%" vspace="5"><br>
            <fd:IncludeMedia name='<%= editorialMedia.getPath() %>'/><br>
<%
    }
}
%>
            <IMG SRC="media/images/layout/clear.gif" WIDTH="1" HEIGHT="8">
        </TD>
    </TR>
</TABLE>
<%


	//
	// Favorite Products
	//
%>
<fd:FeaturedItemsRecommendations id="recommendations" facility="cat_feat_items"  currentNode="<%= currentFolder %>" itemCount="5"><%
	if (recommendations != null && recommendations.getProducts().size() > 0) {
		request.setAttribute("recommendationsRendered","true");
	
		List products = recommendations.getProducts();
		int ord=1;
%>
		<table cellpadding="0" cellspacing="0" width="550" border="0">
			<tr>
				<td class="title14" colspan="<%= products.size()*2 %>"><%= recommendations.getVariant().getServiceConfig().getFILabel() %><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td>
			</tr>
			<tr valign="bottom">
<logic:iterate id='contentNode' collection="<%= products %>" type="com.freshdirect.fdstore.content.ProductModel"><%
		ProductModel productNode = contentNode;
		ProductLabeling pl = new ProductLabeling((FDUserI) session.getAttribute(SessionName.USER), productNode, recommendations.getVariant().getHideBursts());

	String actionURI = FDURLUtil.getProductURI(productNode, recommendations.getVariant().getId(), "feat", pl.getTrackingCode(), ord, recommendations.getImpressionId(productNode));
%><%-- display a product --%>
		<td align="center" width="105" valign="bottom">
			<display:ProductImage product="<%=productNode%>" action="<%=actionURI%>" hideBursts="<%= recommendations.getVariant().getHideBursts() %>"/>
		</td>
		<td width="10">
			<img src="/media/images/layout/clear.gif" width="8" height="1">
		</td><%
			++ord;
		%></logic:iterate>
	</tr>
	<tr valign="top"><%
		ord=1;
	%><logic:iterate id='contentNode' collection="<%=products%>" type="com.freshdirect.fdstore.content.ProductModel"><%
		ProductModel productNode = contentNode;
			ProductLabeling pl = new ProductLabeling((FDUserI) session.getAttribute(SessionName.USER), productNode, recommendations.getVariant().getHideBursts());
			String fiProdPrice = null;
			String fiProdBasePrice=null;
			boolean fiIsDeal=false;	// is deal?
	%><fd:FDProductInfo id="productInfo" skuCode="<%=productNode.getDefaultSku().getSkuCode()%>"><%
		fiProdPrice = JspMethods.currencyFormatter.format(productInfo.getDefaultPrice()) /** +"/"+productInfo.getDisplayableDefaultPriceUnit().toLowerCase() **/;
			
			fiIsDeal=productInfo.hasWasPrice();
			if (fiIsDeal) {
		fiProdBasePrice	= JspMethods.currencyFormatter.format(productInfo.getBasePrice()); //+"/"+ productInfo.getBasePriceUnit().toLowerCase();
			}
	%></fd:FDProductInfo><%
String actionURI = FDURLUtil.getProductURI(productNode, recommendations.getVariant().getId(), "feat", pl.getTrackingCode(), ord, recommendations.getImpressionId(productNode));
%><%-- display a product --%>
		<td align="center" WIDTH="105">
			<display:ProductRating product="<%= productNode %>"/>
<%		if (productNode.isDisplayable()) { %>
			<div><display:ProductName product="<%= productNode %>" action="<%= actionURI %>"/></div>
<%		} else { %>
			<div style="color: #999999;"><display:ProductName product="<%= productNode %>" action="<%= actionURI %>" style="color: #999999;"/></div>
<%		}
		if (fiIsDeal) {
%>			<div style="FONT-WEIGHT: bold; FONT-SIZE: 8pt; COLOR: #c00"><%= fiProdPrice %></div>
			<div style="FONT-SIZE: 7pt; COLOR: #888">(was <%= fiProdBasePrice %>)</div>
<%		} else {
%>			<div class="favoritePrice"><%= fiProdPrice %></div>
<%		} %>
		</td>
		<td width="10">
			<img src="/media/images/layout/clear.gif" width="8" height="1">
		</td><%
		++ord;
%></logic:iterate>
	</tr>
</table>
<%
	}
%>
</fd:FeaturedItemsRecommendations>
<font class="space4pix"><BR></font>
<%



	int typeSpan = (currentCategory != null ? currentCategory.getColumnSpan() : 2);
	int brandSpan = 4 - typeSpan;

%>
<BR><BR>
    
<IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="550" HEIGHT="1"><BR>
<FONT CLASS="space4pix"><BR></FONT>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="550">
<tr VALIGN="TOP">
    <td WIDTH="550" colspan="6"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="5"></td>
</tr>
<TR VALIGN="TOP">
    <TD WIDTH="10"><BR></TD>
    <TD ALIGN="CENTER">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td colspan="<%=typeSpan+(typeSpan-1)%>">
                <IMG SRC="/media/images/navigation/department/grocery/gro_choose_type.gif" WIDTH="107" HEIGHT="10" ALT="CHOOSE A TYPE"><BR>
                <IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4" >
                </td>
            </tr>
            <tr VALIGN="TOP"><%
    //
    // "CHOOSE A TYPE" COLUMNS
    //
                

    int[] columnRows = new int[4];
    boolean makeFakeAllLink = (currentCategory != null ? currentCategory.getFakeAllFolders() : false);
    if (typesCount > 1 && makeFakeAllLink) {
         typesCount++;
    }

    int visitingParentCount = 0;    
    int currentRow = 0;

%>
                <TD WIDTH="125"><%
    
    if (makeFakeAllLink) {
    	%><div style="margin-left: 8px; text-indent: -8px;">
    	   <a href="/category.jsp?catId=<%= currentFolder.getContentName() %>&groceryVirtual=All<%= trkCode %>"><b>All <%= currentFolder.getFullName() %></b></a>
    	   <br/>
        </div>
<%
        currentRow = 1;
        visitingParentCount++;
    }

    int currentColumn = 0;

    int numInColsDesired = 0;
    for(int h=0; h<typeSpan;h++){
        numInColsDesired += columnRows[h];
    }

    int typeProdsCount = 0;
    CategoryModel type = null;
    //skip through the product
    typeProdsCount = 0;

    boolean unlink =  false;
    boolean indent =  false;
    boolean canBreak = true;
    int skipIdx = 0;
    int typesShown=0;
    CategoryModel lastFolder = null;
    int[] breakPoints = new int[typeSpan];
    int breakPoint = 0;
    for (int i = 0; i<typeSpan;i++) {
        breakPoints[i]=typesCount/typeSpan;
    }

    //adjust numbers if not evenly divisible by the typeSpan
    for (int i = 0; i<(typesCount % typeSpan);i++) {
        breakPoints[i]+=1;
    }
    

    //assign first breakpoint
    breakPoint = breakPoints[currentColumn];
    for (int idx = 0; idx < typesList.size();idx++) {
        ContentNodeModel contentItem = (ContentNodeModel) typesList.get(idx);
        type = (CategoryModel) contentItem;
        typesShown++;
        typeProdsCount = 0;
        unlink =  false;
        indent =  false;
        canBreak = false;
        currentRow++;

        if (type.getParentNode() != null && lastFolder != null &&
        	type.getParentNode().getContentName().equals(lastFolder.getContentName())) {
            indent = true;
        } else if (currentRow > breakPoint) {
            canBreak = true;
        }

        if (canBreak) {
        	%></TD>
        	<TD WIDTH="8"><IMG SRC="media/images/layout/clear.gif" HEIGHT="1" WIDTH="8"></TD>
<%
            if (idx != typesCount) {
            	%>
            	<TD WIDTH="125">
<%
            }
            currentRow = 0;
            breakPoint = breakPoints[++currentColumn];
        }


        // display type entry
        if (indent) {
        	if (unlink) {
        		// indented text
        		%><div style="margin-left: 12px; text-indent: -12px;">&nbsp;&nbsp;<%= type.getFullName() %><br/>
        		</div>
<%
        	} else {
        		// indented link
                %><div style="margin-left: 12px; text-indent: -12px;">&nbsp;&nbsp;<a href="/category.jsp?catId=<%= type.getContentName() %>&trk=cpage"><%= type.getFullName() %></a><br/>
                </div>
<%
        	}
        } else {
            if (unlink) {
                // unindented text
                %><div style="margin-left: 8px; text-indent: -8px;"><b><%= type.getFullName() %></b><br/>
                </div>
<%
            } else {
                // unindented link
                %><div style="margin-left: 8px; text-indent: -8px;"><a href="/category.jsp?catId=<%= type.getContentName() %>&trk=cpage"><b><%= type.getFullName() %></b></a><br/>
                </div>
<%
            }
        }



        if (lastFolder==null ||
        		(type.getParentNode() != null && lastFolder != null && !type.getParentNode().getContentName().equals(lastFolder.getContentName()) )) {
            lastFolder = type;
        }
    }
    // if we dont use the other col, when the span is > 1 then assign the rest to the brand span
    if (currentColumn<(typeSpan-1)) {
        brandSpan += (typeSpan-1)-currentColumn;
    }
%>
	       </TD></tr>
    	</table>
    </TD>
    <TD WIDTH="10"><BR></TD>
    <TD BGCOLOR="#CCCCCC" WIDTH="1"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="1"></TD>
    <TD WIDTH="10"><BR></TD>
    <TD WIDTH="<%=125*brandSpan+((brandSpan+1)*6)%>">
    
		<table WIDTH="<%=125*brandSpan+((brandSpan+1)*6)%>" cellpadding="0" cellspacing="0" border="0">
		<tr>
		    <TD WIDTH="6"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="6" HEIGHT="1"></TD>
		    <td colspan="<%=(2*brandSpan)+1%>">
		        <IMG SRC="/media/images/navigation/department/grocery/gro_choose_brand.gif" WIDTH="123" HEIGHT="10" ALT="CHOOSE A BRAND" BORDER="0"><br>
		        <IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10">
		    </TD>
		</tr>
		<tr VALIGN="TOP"><%



	//
	// "CHOOSE A BRAND" COLUMN
	//
		
    columnRows = new int[4];
    int availableBrands = brands.size();
    int brandSize = availableBrands;
    int brandsRows = availableBrands / 3;
    if (availableBrands % 3 != 0) brandsRows++;
    columnRows[0] = brandsRows;

    availableBrands -= brandsRows;
    brandsRows = availableBrands / 2;
    if (availableBrands % 2 != 0) brandsRows++;
    columnRows[1] = brandsRows;
    columnRows[2] = availableBrands - brandsRows;
    %>
            <TD WIDTH="6"><IMG SRC="/media_stat/images/layout/clear.gif" WIDTH="6" HEIGHT="1"></TD>
		    <TD WIDTH="125">
<%
    currentColumn = 0;
    currentRow = 0;
    brandsRows = columnRows[0];

    numInColsDesired = 0;
    for(int h=0; h<brandSpan;h++){
        numInColsDesired += columnRows[h];
    }

    if (numInColsDesired != brandSize){
        int remainder = brandSize - numInColsDesired;
        switch (brandSpan){
            case 1:
                brandsRows = brandSize;
                break;
            case 2:
                int remainMod2 = remainder % 2;
                int remainDiv2 = remainder / 2;

                if(remainMod2 !=0){
                    brandsRows = columnRows[0] + (remainder - remainDiv2);
                    columnRows[1] += remainDiv2;
                }
                else{
                    brandsRows = columnRows[0] + remainDiv2;
                    columnRows[1] += remainDiv2;
                }
                break;
        }
    }
    StringBuffer brandLink = new StringBuffer();
    for (Iterator i = brands.iterator(); i.hasNext(); ) {
        BrandModel brand = (BrandModel) i.next();
        %>
                <div style="margin-left: 8px; text-indent: -8px;">
                    <a href="/category.jsp?catId=<%= currentFolder.getContentName() %>&brandValue=<%= brand.getContentName() %>&groceryVirtual=<%= currentFolder.getContentName() %>&trk=cpage"><%= brand.getFullName() %></a>
                    <br/>
                </div>
<%
            
            
        if (++currentRow == brandsRows) {
            	%>
            	</TD>
            	<TD WIDTH="6"><IMG SRC="/media_stat/images/layout/clear.gif" HEIGHT="1" WIDTH="6"></TD>
<%
            if (i.hasNext()) {
            	%>
            	<TD WIDTH="125"><%
            }
            currentRow = 0;
            brandsRows = columnRows[++currentColumn];
        }
    }
%>
                </td>
<%
    
    if (brands.size() > 0 ) {
        request.setAttribute("brandsList",brands);
    }
%>
            </tr>
        </table>
    </TD>
</TR>
<tr>
    <td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td>
</tr>
</TABLE>