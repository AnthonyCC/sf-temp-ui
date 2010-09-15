<%@ page import='com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings' %>
<%@ page import='com.freshdirect.fdstore.content.util.SortStrategyElement' %>
<%@ page import='com.freshdirect.fdstore.content.CategoryModel' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.display.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import="java.util.*"%>

<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<%
Collection sortedColl = null;
int favoritesToShow = 0;
int FAVS_PER_LINE		= 5; //number of products to show per line
int MAX_FAVSLINES2SHOW	= 1; //number of lines of featured products
int MIN_FAVS2SHOW		= 3; //minimum featured products required to show featured section
int	MAX_FAVS2SHOW		= FAVS_PER_LINE*MAX_FAVSLINES2SHOW; //to keep each row matching
String catId = request.getParameter("catId");
ContentNodeModel node = ContentFactory.getInstance().getContentNode(catId);
boolean isDept = (node instanceof DepartmentModel);
boolean isCat = (node instanceof CategoryModel);
String trkCode= "";
//** pass the code that should be used as the tracking code **/
if (isDept) {
	trkCode = "dpage";
	request.setAttribute("trk","dpage");
} else if(isCat) {
	trkCode = "cpage";
    request.setAttribute("trk","cpage");
}
boolean sortDescending = "true".equalsIgnoreCase(request.getParameter("sortDescending"));
    
Settings layoutSettings = new Settings();
layoutSettings.setGrabberDepth(99);
layoutSettings.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, false));

List tmpList=new ArrayList();
%>
<% //check if the category exists
	if(node != null && node instanceof CategoryModel){ %>

<fd:ItemGrabber
	category='<%=node %>' 
	id='rtnColl' 
	depth='<%=layoutSettings.getGrabberDepth()%>'
	ignoreShowChildren='true' 
	filterDiscontinued='<%= layoutSettings.isFilterDiscontinued() %>'
	returnHiddenFolders='<%=layoutSettings.isReturnHiddenFolders()%>'
	ignoreDuplicateProducts='<%=layoutSettings.isIgnoreDuplicateProducts()%>'
	returnSecondaryFolders='<%=layoutSettings.isReturnSecondaryFolders()%>' 
	returnSkus='<%=layoutSettings.isReturnSkus()%>'
	workSet='<%=tmpList%>'
>
<%
        sortedColl = rtnColl;
        //request.setAttribute("itemGrabberResult",sortedColl); //** expose result of item grabber to the layout **
%>
    </fd:ItemGrabber> 
 
    <fd:ItemSorter nodes='<%=(List)sortedColl%>' strategy='<%=layoutSettings.getSortStrategy()%>'/>

<% if(sortedColl.size() >= MIN_FAVS2SHOW){ %>
	<% String mediaPath = "/media/editorial/meat/meat_deals_"+catId+".html"; %>
	<fd:IncludeMedia name="<%= mediaPath %>" />
	<!--<div align="center"><a href="/category.jsp?catId=wgd_butchers_deals&trk=cpage"><img src="/media/images/navigation/department/meat/mea_block.jpg"></img></a></div>-->
	<br><br>
	<table cellpadding="0" cellspacing="0" border="0">
		<tr valign="bottom">
		<logic:iterate id="contentNode" collection="<%= sortedColl %>" type="java.lang.Object" indexId="idx">
			<%
				if (idx.intValue() >= MAX_FAVS2SHOW) { continue; }
				if(contentNode instanceof CategoryModel) { continue; }
				else if(contentNode instanceof ProductModel){
					if(((ProductModel)contentNode).isUnavailable()){ continue;}
					ProductModel pm = (ProductModel) contentNode;
					String prodNameAttribute = JspMethods.getProductNameToUse(pm);
					DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,catId,pm,prodNameAttribute,true);
					int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
					String actionUrl = FDURLUtil.getProductURI( pm, "dept" );
			%>
				<td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
					<display:ProductImage product="<%= pm %>" showRolloverImage="false" action="<%= actionUrl %>"/>
				</td>
				<% } %>
		</logic:iterate>
		</tr>
		<tr>
		<logic:iterate id="contentNode" collection="<%= sortedColl %>" type="java.lang.Object" indexId="idx">
			<%
				if (idx.intValue() >= MAX_FAVS2SHOW) { continue; } 
				if(contentNode instanceof CategoryModel) { continue; }
				else if(contentNode instanceof ProductModel){
					if(((ProductModel)contentNode).isUnavailable()){ continue;}
					ProductModel pm = (ProductModel) contentNode;
					String prodNameAttribute = JspMethods.getProductNameToUse(pm);
					DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,catId,pm,prodNameAttribute,true);
					int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
					String actionUrl = FDURLUtil.getProductURI( pm, "dept" );
			%>

				<td valign="top" width="<%=adjustedImgWidth%>" align="center" style="padding-left:5px; padding-right:5px;padding-bottom:10px;">
					<display:ProductName product="<%= pm %>" action="<%= actionUrl %>"/>								
					<display:ProductPrice impression="<%= new ProductImpression( pm ) %>" showAboutPrice="false" showDescription="false"/>
				</td>
				<% } %>
		</logic:iterate>
		</tr>
	</table>

<% } 
}//category instance %>
