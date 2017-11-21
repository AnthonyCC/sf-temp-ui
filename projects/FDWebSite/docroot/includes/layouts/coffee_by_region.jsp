<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_COFFEE_BY_REGION_TOTAL = 765;
final int W_COFFEE_BY_REGION_IMG = 123;
final int W_COFFEE_BY_REGION_COLS = 642;
final int W_COFFEE_BY_REGION_COL = 150-1;
final int W_COFFEE_BY_REGION_COL_PAD_HALF = 7;
%>

<%
//********** Start of Stuff to let JSPF's become JSP's **************

String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 
boolean isDepartment = false;

ContentNodeModel currentFolder = null;
if(deptId!=null) {
	currentFolder=ContentFactory.getInstance().getContentNode(deptId);
	isDepartment = true;
} else {
	currentFolder=ContentFactory.getInstance().getContentNode(catId);
}


boolean onlyOneProduct = false;
ProductModel theOnlyProduct = null;
//Siva-Changed Tracking Code Retrieval
String trkCode = (String)request.getAttribute("trk");

Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();
%>

<%!
public class ByRegionObject {
    private StringBuffer col_one = new StringBuffer();
    private StringBuffer col_two = new StringBuffer();
    private StringBuffer col_three = new StringBuffer();
    private StringBuffer col_four = new StringBuffer();

    public void clearCols() {
        this.col_one.setLength(0);
        this.col_two.setLength(0);
        this.col_three.setLength(0);
        this.col_four.setLength(0);
    }

    public long getColLength(int colnumber) {
        long colLength = 0;
        switch (colnumber) {
            case 1: 
                colLength=(long)this.col_one.length();
                break;
            case 2: 
                colLength=(long)this.col_two.length();
                break;
            case 3: 
                colLength=(long)this.col_three.length();
                break;
            case 4: 
                colLength=(long)this.col_four.length();
                break;
        }
        return colLength;
    }

    public String getColValue(int colnumber) {
        String colStuff = "";
        switch( colnumber ){
            case 1:
                colStuff=this.col_one.toString();
                break;
            case 2:
                colStuff=this.col_two.toString();
                break;
            case 3:
                colStuff=this.col_three.toString();
                break;
            case 4:
                colStuff=this.col_four.toString();
                break;
        }
        return colStuff;
    }

    public void append(int colnumber,String appendString) {
        String colStuff = "";
        switch( colnumber ){
            case 1:
                this.col_one.append(appendString);
                break;
            case 2:
                this.col_two.append(appendString);
                break;
            case 3:
                this.col_three.append(appendString);
                break;
            case 4:
                this.col_four.append(appendString);
                break;
        }
    }
}

public ByRegionObject bldByRegionCols(Collection prodCollection,HttpServletResponse response, String trkCode) throws JspException {
        ByRegionObject byRegionObject = new ByRegionObject();
        
        byRegionObject.clearCols();
        String availFontStart = "";
        String availFontEnd = "";
        int colNum = 1;
        //boolean processDecaf = false;
        int pSize = prodCollection.size();
        int col1 = pSize/4;
        int col2 = col1;
        int col3 = col1;
        int col4 = col1;
        int tmp = pSize%4;

        //if not evenly divisible by 3, then distribute the remaining to col-1 or col1 & col 2
        if(tmp > 0 && tmp == 1){
                col1++;
        }
        if(tmp > 0 && tmp ==2){
                col1++;
                col2++;
        }
        if(tmp > 0 && tmp ==3){
            col1++;
            col2++;
            col3++;
    	}
        int counter = 0;
        for(Iterator prodItr = prodCollection.iterator();prodItr.hasNext();counter++) {
            availFontStart = "";
            availFontEnd = "";
            ProductModel product = (ProductModel)prodItr.next();

            if (product.isUnavailable()){
                availFontStart = "<font color=\"#999999\">";
                availFontEnd = "</font>";
            }

            CategoryModel innerFolder = (CategoryModel)product.getParentNode();
            String url = response.encodeURL("product.jsp?productId="+product+"&catId="+innerFolder+"&trk="+trkCode);
            String name = product.getNavName();

            if(counter >= 0 && counter< col1 )
                    colNum = 1;
            if(counter >= col1 && counter < col1+col2)
                    colNum = 2;
            if(counter >= col1+col2 && counter<col1+col2+col3)
                    colNum = 3;
            if(counter >= col1+col2+col3 && counter<col1+col2+col3+col4)
                colNum = 4;

            byRegionObject.append(colNum,"<a href=\"");
            byRegionObject.append(colNum,url);
            byRegionObject.append(colNum,"\"");
				if (product.isUnavailable()){
                byRegionObject.append(colNum," style=\"color:#999999\"");
            	}
			byRegionObject.append(colNum,">");
            byRegionObject.append(colNum,availFontStart);
            byRegionObject.append(colNum,name);
            byRegionObject.append(colNum,availFontEnd);
            byRegionObject.append(colNum,"</a>&nbsp;<BR>");
        }
        return byRegionObject;
}

%>

<%
//**************************************************************
//***          the Coffee By Region/Roast Pattern            ***
//**************************************************************

    Object[] foldersAndProds = sortedColl.toArray();
    CategoryModel subFolder = null;
    Image catImage = null;
    Image lblImage = null;
    StringBuffer map = new StringBuffer();
    String imagePhoto= null;
    String folderLabelImage = null;
    String folderURL = null;
    String productURL = null;
    String fldrDescription = null;
    String outerFolderName = "";
    int folderShownCount = 0;
    List unAvailableList = new ArrayList();
    for(int i = 0; i < foldersAndProds.length; i++){
        int nextIndex = i;
        //note we are expecting a folder at this point...so if none...that'll be strange.
        ContentNodeModel arrayItem= (ContentNodeModel)foldersAndProds[i];
        if (arrayItem instanceof CategoryModel) {
            nextIndex = i+1;
            subFolder = (CategoryModel)arrayItem;
        } else if (currentFolder instanceof CategoryModel){
            subFolder = (CategoryModel)currentFolder;  //use current folder expected object is not a folder
        } else {
            continue;  //subFolder = new CategoryModel();
        }

        if (subFolder.getShowSelf()!=true 
             && subFolder.getFullName().toLowerCase().indexOf("decaf")!=-1) { %>

<FONT CLASS="space4pix"><br></FONT><FONT CLASS="text10bold"><%=subFolder.getFullName()%>:</FONT><BR>
<%      } else if (subFolder.getShowSelf()==true) {
    		catImage = subFolder.getCategoryPhotoNotNull(); 

    		lblImage = subFolder.getCategoryLabelNotNull();
            folderURL=  response.encodeURL("category.jsp?catId="+subFolder+"&trk="+trkCode);
            fldrDescription = subFolder.getBlurb();

            //since there is no Category_description attribute as yet, use the intro copy attribute for now

if(folderShownCount>0){
%>
</TD></TR>
<TR VALIGN="TOP">
<TD WIDTH="<%=W_COFFEE_BY_REGION_IMG%>">&nbsp;</TD>
<TD WIDTH="<%=W_COFFEE_BY_REGION_COLS%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="12" BORDER="0"><BR><IMG src="/media_stat/images/layout/999966.gif" ALT="" WIDTH="<%=W_COFFEE_BY_REGION_COLS%>" HEIGHT="1" BORDER="0"><BR><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="12" BORDER="0"></TD>
</TR></table>
<%}%>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=W_COFFEE_BY_REGION_TOTAL%>">
<TR VALIGN="TOP"><TD WIDTH="<%=W_COFFEE_BY_REGION_IMG%>"><A HREF="<%=folderURL%>"><img src="<%=catImage.getPath()%>" <%=JspMethods.getImageDimensions(catImage)%> border="0" alt="<%=subFolder.getFullName()%>"></A></TD>
<TD WIDTH="<%=W_COFFEE_BY_REGION_COLS%>"><A HREF="<%=folderURL%>"><img src="<%=lblImage.getPath()%>" <%=JspMethods.getImageDimensions(lblImage)%> border="0" alt="<%=subFolder.getFullName()%>"></A><BR>
<%
if (fldrDescription!=null && fldrDescription.trim().length() >=1) { %> 
<font class="text13">
<%=fldrDescription%>

<%}%>
<A HREF="<%=folderURL%>">Learn more &gt;&gt;</A>
</font>
<BR><BR>
<%     folderShownCount++;
        outerFolderName = subFolder.getContentName();

     }
        // go figure out how many products are here for the folder..(till next folder or till end of array)
        List availableStuff = new ArrayList();
        int prodCount=0;
        boolean folderFound= false;
        for (int j=nextIndex; j<foldersAndProds.length && !folderFound;j++) {
            if ((ContentNodeModel)foldersAndProds[j] instanceof ProductModel) {
                prodCount++;
                if (((ProductModel)foldersAndProds[j]).isUnavailable()) {
                    unAvailableList.add(foldersAndProds[j]);
                } else {																													
                    availableStuff.add(foldersAndProds[j]);
                }
            } else folderFound=true;
        }
        ByRegionObject regionProdCols = bldByRegionCols(availableStuff,response,trkCode);
        i+=prodCount;
%>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" width="<%=W_COFFEE_BY_REGION_COLS-1%>">
<TR VALIGN="TOP"><TD width="<%=W_COFFEE_BY_REGION_COL%>"><%=regionProdCols.getColValue(1)%></TD>
<TD WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>" HEIGHT="1" BORDER="0"></TD>
<%if(regionProdCols.getColLength(2) > 0){%>
<TD WIDTH="1" BGCOLOR="#CCCCCC"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="1" BORDER="0"></TD>
<%}%>
<TD WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>" HEIGHT="1" BORDER="0"></TD><TD width="<%=W_COFFEE_BY_REGION_COL%>"><%=regionProdCols.getColValue(2)%></TD>
<TD WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>" HEIGHT="1" BORDER="0"></TD>
<%if(regionProdCols.getColLength(3) > 0){%>
<TD WIDTH="1" BGCOLOR="#CCCCCC"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="1" BORDER="0"></TD>
<%}%>
<TD WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>" HEIGHT="1" BORDER="0"></TD><TD width="<%=W_COFFEE_BY_REGION_COL%>"><%=regionProdCols.getColValue(3)%></TD>
<TD WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>" HEIGHT="1" BORDER="0"></TD>
<%if(regionProdCols.getColLength(4) > 0){%>
<TD WIDTH="1" BGCOLOR="#CCCCCC"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="1" BORDER="0"></TD>
<%}%>
<TD WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>" HEIGHT="1" BORDER="0"></TD><TD width="<%=W_COFFEE_BY_REGION_COL%>">
<%=regionProdCols.getColValue(4)%></TD></TR></TABLE><BR><FONT CLASS="space4pix"><BR></FONT>
<%
    // if the next folder is a not A child of the outerFolder..then display the unavailable stuff
    if (unAvailableList.size()>0 && (!folderFound || (!outerFolderName.equals( ((CategoryModel)foldersAndProds[i+1]).getParentNode().getContentName() )) )  ) {
        regionProdCols = bldByRegionCols(unAvailableList,response,trkCode);
        unAvailableList.clear();
%>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" width="<%=W_COFFEE_BY_REGION_COLS-1%>">
<TR VALIGN="TOP"><TD colspan="13" width="<%=W_COFFEE_BY_REGION_COLS-1%>"><br><font color="#999999"><b>Currently Unavailable</b></font><br></TD></tr>

<TR VALIGN="TOP"><TD width="<%=W_COFFEE_BY_REGION_COL%>"><%=regionProdCols.getColValue(1)%></TD>
<TD WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>" HEIGHT="1" BORDER="0"></TD>
<%if(regionProdCols.getColLength(2) > 0){%>
<TD WIDTH="1" BGCOLOR="#CCCCCC"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="1" BORDER="0"></TD>
<%}%>
<TD WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>" HEIGHT="1" BORDER="0"></TD><TD width="<%=W_COFFEE_BY_REGION_COL%>"><%=regionProdCols.getColValue(2)%></TD>
<TD WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>" HEIGHT="1" BORDER="0"></TD>
<%if(regionProdCols.getColLength(3) > 0){%>
<TD WIDTH="1" BGCOLOR="#CCCCCC"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="1" BORDER="0"></TD>
<%}%>
<TD WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>" HEIGHT="1" BORDER="0"></TD><TD width="<%=W_COFFEE_BY_REGION_COL%>"><%=regionProdCols.getColValue(3)%></TD>
<TD WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>" HEIGHT="1" BORDER="0"></TD>
<%if(regionProdCols.getColLength(4) > 0){%>
<TD WIDTH="1" BGCOLOR="#CCCCCC"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="1" BORDER="0"></TD>
<%}%>
<TD WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_COFFEE_BY_REGION_COL_PAD_HALF%>" HEIGHT="1" BORDER="0"></TD><TD width="<%=W_COFFEE_BY_REGION_COL%>">
<%=regionProdCols.getColValue(4)%></TD></TR></TABLE><BR><FONT CLASS="space4pix"><BR></FONT>

<%
    }
regionProdCols.clearCols();
}%>
&nbsp;<p><p>
</TABLE>
