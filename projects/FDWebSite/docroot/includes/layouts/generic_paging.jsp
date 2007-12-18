<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
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

<%!
public String getPageNumbers(HttpServletRequest requestObj, HttpServletResponse responseObj,int pageNumber, int itemsPerPage, ContentNodeModel currFolder, int itemsCount) {
	StringBuffer buf = new StringBuffer();
	String urlParams = buildOtherParams(itemsPerPage, -1);
	String urlStart = "/category.jsp?catId=" + currFolder +"&trk=trans";
	String fullURL = null;
	int startFrom = 1;

	if(pageNumber > 1)   {
		if (pageNumber/10 > 1) {
			startFrom = ((pageNumber/10) * 10) + 1;
			fullURL= responseObj.encodeURL(urlStart + urlParams + "&pageNumber=" + startFrom);

			buf.append("<A HREF=\"");
			buf.append(urlStart).append(urlParams);
			buf.append("\">previous</A> . ");
		}

		for (int i=startFrom; i<pageNumber; i++) {
			fullURL= responseObj.encodeURL(urlStart+urlParams+"&pageNumber="+i);
			buf.append("<A HREF=\"").append(fullURL).append("\">");
			buf.append(i).append("</A> . ");
		}
	}

	buf.append("<B>").append(pageNumber).append("</B>");

	if ( itemsCount >= (pageNumber * itemsPerPage) ) {
		// figure out how many additional pages to display
		int addToLoop = 0;
		if(itemsCount % itemsPerPage > 0) {
			addToLoop = 1;
		}
		for (int i=(pageNumber + 1); (i <= (itemsCount/itemsPerPage + addToLoop)); i++) {
			fullURL= responseObj.encodeURL(urlStart + urlParams + "&pageNumber=" + i);
			buf.append(" . <A HREF=\"");
			buf.append(fullURL);
			buf.append("\">");
			if (i%10 == 1) {
				buf.append("more");
				break;
			} else {
				 buf.append(i);
			}
			buf.append("</A>");
		}
	}
	return buf.toString();
}
public String buildOtherParams(int displayPerPageSetting, int pageNumberValue) {

	StringBuffer buf = new StringBuffer();

	if (displayPerPageSetting  > 0 ) {
		buf.append("&displayPerPage=").append(displayPerPageSetting);
	}

	if (pageNumberValue > 0) {
		buf.append("&pageNumber=").append(pageNumberValue);
	}

	return buf.toString();
}
%>
<%
//**************************************************************
//***          the GENERIC_PagingLAYOUT Pattern  \           ***
//**************************************************************

//********** Start of Stuff to let JSPF's become JSP's **************

String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 
ContentNodeModel currentFolder = null;
if(deptId!=null) {
	currentFolder=ContentFactory.getInstance().getContentNodeByName(deptId);
} else {
	currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
}


int pageNumber = 1;
try {
    pageNumber = Integer.valueOf(request.getParameter("pageNumber")).intValue();
} catch (NumberFormatException nfe) {}

int itemsToDisplay = 20;
{
	String reqItemsToDisp = request.getParameter("displayPerPage");
	String sessItemsToDisp = (String)session.getAttribute("gp_itemsToDisplay");

	if ( reqItemsToDisp!=null && (sessItemsToDisp==null || !sessItemsToDisp.equals(reqItemsToDisp)) ) {
		// we have to update the session with the value from the request
		sessItemsToDisp = reqItemsToDisp;
		session.setAttribute("gp_itemsToDisplay", sessItemsToDisp);
	} else {
            reqItemsToDisp = (String) session.getAttribute("gp_itemsToDisplay");
        }
	try {
		if (reqItemsToDisp!=null) {
			itemsToDisplay = Integer.valueOf(reqItemsToDisp).intValue();
		} else if (sessItemsToDisp!=null) {
			itemsToDisplay = Integer.valueOf(sessItemsToDisp).intValue();
		}
		if (itemsToDisplay!=12 && itemsToDisplay!=20 && itemsToDisplay!=28) {
			itemsToDisplay = 20;
		}
	} catch (NumberFormatException nfe) {
		itemsToDisplay = 20;
	}

}

Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();


List displayList = new ArrayList();
for(Iterator availItr = sortedColl.iterator();availItr.hasNext();) {
    Object availObject = availItr.next();
    if (availObject instanceof ProductModel && ((ProductModel)availObject).isUnavailable()) {
       continue;
    } else {
        //throw away hidden folders
        if (availObject instanceof CategoryModel && ((CategoryModel)availObject).getShowSelf()==false) continue;
        displayList.add(availObject);
    }
} 

//cath bogus page number, and set to 1 
if (pageNumber > ((displayList.size()/itemsToDisplay)+(displayList.size() % itemsToDisplay)) ) {
    pageNumber = 1;
}

String pagingLinks = getPageNumbers(request,response,pageNumber,itemsToDisplay,currentFolder,displayList.size());
Integer offset = new Integer((pageNumber-1)*itemsToDisplay);
Integer len = new Integer(itemsToDisplay);
%>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
<tr><td CLASS="text10"><img src="/media_stat/images/layout/clear.gif" width="12"><b>Page: </b><%=pagingLinks%>&nbsp;(<%=displayList.size()%> items)
<br><img src="/media_stat/images/layout/clear.gif" height="24">
</td></tr>
</table>

<%@ include file="/includes/layouts/i_generic_body.jspf"%>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
<tr><td COLSPAN="2" BGCOLOR="#CCCCCC" width="100%"></td></tr>
<tr valign="top"><td CLASS="text10" align="left" width="50%">
<img src="/media_stat/images/layout/clear.gif" width="12"><b><%=currentFolder.getFullName()%></b>&nbsp;(<%=displayList.size()%> items)</td>
<td CLASS="text10" ALIGN="RIGHT" width="50%">Display <%

if (itemsToDisplay == 12) {
%>
<B>12</B> |
<A HREF="<%= response.encodeURL("/category.jsp?catId=" + currentFolder + buildOtherParams(20,-1)+"&trk=numb") %>">20</A> |
<A HREF="<%= response.encodeURL("/category.jsp?catId=" + currentFolder + buildOtherParams(28,-1)+"&trk=numb") %>">28</A>
<%
} else if (itemsToDisplay == 20) {
%>
<A HREF="<%= response.encodeURL("/category.jsp?catId=" + currentFolder + buildOtherParams(12,-1)+"&trk=numb") %>">12</A> |
<B>20</B> |
<A HREF="<%= response.encodeURL("/category.jsp?catId=" + currentFolder + buildOtherParams(28,-1)+"&trk=numb") %>">28</A>
<%
} else {
%>
<A HREF="<%= response.encodeURL("/category.jsp?catId=" + currentFolder + buildOtherParams(12,-1)+"&trk=numb") %>">12</A> |
<A HREF="<%= response.encodeURL("/category.jsp?catId=" + currentFolder + buildOtherParams(20,-1)+"&trk=numb") %>">20</A> |
<B>28</B><%
}
%> per page<img src="/media_stat/images/layout/clear.gif" width="12"></td>
</tr>
<tr><td CLASS="text10" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="12"><b>Page: </b><%=pagingLinks%></td></tr>
</table>
