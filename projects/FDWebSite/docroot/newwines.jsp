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
<fd:CheckLoginStatus />

<%!
    final static int DAYS_1 = 14;
    final static int DAYS_2 = 21;
    final static int DAYS_3 = 28;
    final static int DEFAULT_DAYS = DAYS_2;
    final static Map DAYS = new HashMap();
    static {
            DAYS.put( "1", new Integer(DAYS_1) );
            DAYS.put( "2", new Integer(DAYS_2) );
            DAYS.put( "3", new Integer(DAYS_3) );
    }


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
String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 
Integer daysInt = (Integer)DAYS.get( request.getParameter("days") );
int days = daysInt==null ? DEFAULT_DAYS : daysInt.intValue();
ContentNodeModel currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
String daysIndx=null;

for(Iterator kItr = DAYS.keySet().iterator(); kItr.hasNext() && daysIndx==null; ) {
    String keyValue = (String) kItr.next();
    if (((Integer) DAYS.get(keyValue)).intValue()==days) {
      daysIndx = keyValue;
    }
}

Attribute hasAlcohol=currentFolder.getAttribute("CONTAINS_BEER");
FDSessionUser yser = (FDSessionUser)session.getAttribute(SessionName.USER);
if(hasAlcohol != null && Boolean.TRUE.equals(hasAlcohol.getValue()) && !yser.isHealthWarningAcknowledged()){
	String redirectURL = "/health_warning.jsp?successPage=/newwines.jsp"+URLEncoder.encode("?"+request.getQueryString());
	response.sendRedirect(response.encodeRedirectURL(redirectURL));
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

List displayList = new ArrayList();
String pagingLinks = "";
%>
<tmpl:insert template='/common/template/bestcellars/all_navs.jsp'>
 <tmpl:put name='title' direct='true'>FreshDirect - <%= currentFolder.getFullName() %></tmpl:put>
  <tmpl:put name='content' direct='true'>
   <oscache:cache time="3600" key='<%= "newwines/" + days %>'>
<%
try {
%>
    <fd:GetNewProducts id='products' days='<%=days%>' department='<%=deptId%>'>
<%
//List products = new ArrayList();
for(Iterator availItr = products.iterator();availItr.hasNext();) {
    Object availObject = availItr.next();
    if (availObject instanceof ProductModel && ((ProductModel)availObject).isUnavailable()) {
       continue;
    } else {
        //throw away hidden folders
        if (availObject instanceof CategoryModel && ((CategoryModel)availObject).getShowSelf()==false) continue;
        displayList.add(availObject);
    }
} 

if (pageNumber > ((displayList.size()/itemsToDisplay)+(displayList.size() % itemsToDisplay)) ) {
    pageNumber = 1;
}

pagingLinks = getPageNumbers(request,response,pageNumber,itemsToDisplay,currentFolder,displayList.size());
Integer offset = new Integer((pageNumber-1)*itemsToDisplay);
Integer len = new Integer(itemsToDisplay);
%>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
<tr>
  <td valign="top" align="left" CLASS="text10"><img src="/media_stat/images/layout/clear.gif" width="12"><b>Page: </b><%=pagingLinks%>&nbsp;(<%=displayList.size()%> items)</td>
  <td align="right" CLASS="text10" valign="top">&nbsp;See last: 
<%if (DAYS_1==days){%><b><%= DAYS_1 %> days</b><%}else{%><a href="/newwines.jsp?deptId=win&catId=win_new&days=1"><%= DAYS_1 %> days</a><%}%> | 
<%if (DAYS_2==days){%><b><%= DAYS_2 %> days</b><%}else{%><a href="/newwines.jsp?deptId=win&catId=win_new&days=2"><%= DAYS_2 %> days</a><%}%> | 
<%if (DAYS_3==days){%><b><%= DAYS_3 %> days</b><%}else{%><a href="/newwines.jsp?deptId=win&catId=win_new&days=3"><%= DAYS_3 %> days</a><%}%>&nbsp;
   <br><img src="/media_stat/images/layout/clear.gif" height="24">
  </td>
</tr>
</table>
<%
if (displayList.size()!=0) { 
%>
  <%@ include file="/includes/layouts/i_generic_body.jspf"%>
<%
} else {   %>
    <table cellspacing="0" cellpadding="0"  border="0" width="100%"><tr><td>
      <img src="/media_stat/images/layout/clear.gif" height="54"><br>&nbsp;&nbsp;<b>No new wines within the last <%=days%> days.<b><br>
      <img src="/media_stat/images/layout/clear.gif" height="24">
    </td></tr></table>
<%
} %>
     </fd:GetNewProducts>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
<tr><td COLSPAN="2" BGCOLOR="#CCCCCC" width="100%"></td></tr>
<tr valign="top"><td CLASS="text10" align="left" width="50%">
<img src="/media_stat/images/layout/clear.gif" width="12"><b><%=currentFolder.getFullName()%></b>&nbsp;(<%=displayList.size()%> items)</td>
<td CLASS="text10" ALIGN="RIGHT" width="50%">Display <%
if (itemsToDisplay == 12) {
%>
<B>12</B> |
<A HREF="<%= response.encodeURL("/newwines.jsp?deptId=win&catId=win_new&days="+daysIndx + buildOtherParams(20,-1)+"&trk=numb") %>">20</A> |
<A HREF="<%= response.encodeURL("/newwines.jsp?deptId=win&catId=win_new&days="+daysIndx + buildOtherParams(28,-1)+"&trk=numb") %>">28</A>
<%
} else if (itemsToDisplay == 20) {
%>
<A HREF="<%= response.encodeURL("/newwines.jsp?deptId=win&catId=win_new&days="+daysIndx  + buildOtherParams(12,-1)+"&trk=numb") %>">12</A> |
<B>20</B> |
<A HREF="<%= response.encodeURL("/newwines.jsp?deptId=win&catId=win_new&days="+daysIndx + buildOtherParams(28,-1)+"&trk=numb") %>">28</A>
<%
} else {
%>
<A HREF="<%= response.encodeURL("/newwines.jsp?deptId=win&catId=win_new&days="+daysIndx + buildOtherParams(12,-1)+"&trk=numb") %>">12</A> |
<A HREF="<%= response.encodeURL("/newwines.jsp?deptId=win&catId=win_new&days="+daysIndx + buildOtherParams(20,-1)+"&trk=numb") %>">20</A> |
<B>28</B><%
}
%> per page<img src="/media_stat/images/layout/clear.gif" width="12"></td>
</tr>
<tr><td CLASS="text10" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="12"><b>Page: </b><%=pagingLinks%></td></tr>
</table>
<% } catch (Exception ex) {
		ex.printStackTrace();
%>
<oscache:usecached />
<% } %>
</oscache:cache>
 </tmpl:put>
</tmpl:insert>
