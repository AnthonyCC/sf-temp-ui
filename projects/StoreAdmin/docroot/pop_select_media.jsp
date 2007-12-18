<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import ="com.freshdirect.framework.webapp.*" %>
<%@ page import ="com.freshdirect.fdstore.content.*"%>
<%@ page import ="com.freshdirect.fdstore.attributes.*"%>
<%@ page import ="java.text.*"%>
<%
    String servletContext = request.getContextPath();
	String expandParam = request.getParameter("expand");

    String expandLink = request.getRequestURI();
	String expansionImg = " + ";
    String action = request.getParameter("action");
    if (action==null) action="select";
%>
<sa:MediaSelection action='<%=action%>' id='theAttribute' result='result'>
<%
	List mediaItems;
	MediaModel media=null;
	String mediaTitle = null;
	String mediaPopupSize=null;
	String articleSource=null;
	String articleBlurb = null;
	String sArticleDate = null;
	String mediaDim=null;
	if (!isMultiAttribute.booleanValue()) {
		mediaItems = new ArrayList();
		mediaItems.add(theAttribute.getValue());
	} else { // multiattribute, just show the path of the files
		mediaItems = ((MultiAttribute)theAttribute).getValues();
	}
%>
<tmpl:insert template='/common/template/pop_right_usrnav.jsp'>
<% if (result.isSuccess() && "refreshParent".equalsIgnoreCase(request.getParameter("action"))) { %>
<tmpl:put name="header">
    <script>
    	window.opener.location='<%=(String)session.getAttribute("parentURI")%>?action=updateAttribute';
    	window.close();
    </script>
</tmpl:put>
<%} else {  //not closing window...so show stuff %>
<tmpl:put name="heading">
<FONT CLASS="title13or">FD Content Manager: Select Media</FONT><BR>
<img src="images/CCCCCC.gif" WIDTH="380" HEIGHT="1"><BR>
</tmpl:put>
<tmpl:put name="header">
<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<fd:ErrorHandler id='errMsg' result='<%=result%>' name='Priority'>
    	<tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
	</fd:ErrorHandler>
	<fd:ErrorHandler id='errMsg' result='<%=result%>' name='duplicateItem'>
    	<tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
	</fd:ErrorHandler>
	<fd:ErrorHandler id='errMsg' result='<%=result%>' name='Error'>
    	<tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
	</fd:ErrorHandler>
</table>
</tmpl:put>
 <tmpl:put name="contentHeader">
<TABLE WIDTH="380" CELLSPACING="2" CELLPADDING="0" BORDER="0">
 <TR>
  <TD WIDTH="380" ALIGN="RIGHT"><FONT CLASS="space4pix"><BR></FONT>
 </TR>
</TABLE>
<TABLE WIDTH="380" CELLSPACING="2" CELLPADDING="0" BORDER="0">
  <TR VALIGN="top">
   <TD WIDTH="150" CLASS="text11bold">Property/Attribute</TD><TD WIDTH="230"><%=theAttribute.getKey()%></TD>
  </TR>
</TABLE>
<img src="images/CCCCCC.gif" WIDTH="380" HEIGHT="1"><BR>
<FONT CLASS="space4pix"><BR></FONT>
<FONT CLASS="text11bold">CURRENT MEDIA</FONT><BR>
<FONT CLASS="space4pix"><BR></FONT>
</tmpl:put>
<tmpl:put name="content">
<TABLE WIDTH="380" CELLSPACING="2" CELLPADDING="0" BORDER="0">
<form name="mediaForm" action="<%=expandLink%>" method="post">
<input type="hidden" name="action" value="refreshParent">
<%
	for (Iterator mediaItr=mediaItems.iterator(); mediaItr.hasNext(); ) {
		media=null;
		mediaTitle = null;
		mediaPopupSize=null;
		articleSource=null;
		articleBlurb = null;
		sArticleDate = null;
		mediaDim="n/a";

		MediaI mediaItem = (MediaI)mediaItr.next();
                if (mediaItem==null) continue;
                
		if (mediaItem.getMediaType().equals(MediaI.TYPE_TITLEDMEDIA) || mediaItem.getMediaType().equals(MediaI.TYPE_ARTICLEMEDIA) ) {
			media = ((TitledMedia)mediaItem).getMedia();
			mediaTitle=((TitledMedia)mediaItem).getMediaTitle();
			if (mediaTitle==null) mediaTitle="";
			if (mediaItem.getMediaType().equals(MediaI.TYPE_TITLEDMEDIA)) {
				mediaPopupSize=((TitledMedia)mediaItem).getPopupSize();
				if (mediaPopupSize==null) mediaPopupSize = "";
			} else {
				articleSource = ((ArticleMedia)mediaItem).getSource();
				articleBlurb = ((ArticleMedia)mediaItem).getBlurb();
				if (((ArticleMedia)mediaItem).getDate()!=null) {
					Calendar articleDate = ((ArticleMedia)mediaItem).getDate();
            		sArticleDate = articleDate.get(Calendar.MONTH)+"/"+articleDate.get(Calendar.DATE)+"/"+articleDate.get(Calendar.YEAR);
        		}
			}
		} else {
			media = (MediaModel)mediaItem;
		}
		if (media.getMediaType().equals(MediaI.TYPE_IMAGE)) {
			mediaDim = media.getWidth()+"w x "+media.getHeight()+"h";
		}
%>
<%
		if (!isMultiAttribute.booleanValue()) {
         // %>
			<TR VALIGN="TOP">
			<TD WIDTH="20%" CLASS="text11bold">Path</TD>
			<TD WIDTH="80%"><%=media.getPath() %></TD>
			</TR>
<%		if (media.getMediaType().equalsIgnoreCase(MediaI.TYPE_IMAGE)) { // must be an image  %>
			<TR VALIGN="TOP">
			<TD WIDTH="20%" CLASS="text11bold">Dimensions</TD>
			<TD WIDTH="80"><%=mediaDim%></TD>
			</TR>
<%		}
     	if (mediaTitle!=null) { %>
				<TR VALIGN="TOP">
				<TD WIDTH="20%" CLASS="text11bold">Title</TD>
				<TD WIDTH="80%"><input type="text" name="mediaTitle" value="<%=mediaTitle%>"></TD>
				</TR>
<%		}
		if (mediaPopupSize!=null) {  %>
				<TR VALIGN="TOP">
				<TD WIDTH="20%" CLASS="text11bold">Popup Size</TD>
				<TD WIDTH="80%"><input type="text" name="popupSize" value="<%=mediaPopupSize%>"></TD>
				</TR>
<%		}
		if (articleSource!=null) {	%>
				<TR VALIGN="TOP">
				<TD WIDTH="20%" CLASS="text11bold">Source</TD>
				<TD WIDTH="80%"><input type="text" name="articleSource" value="<%=articleSource%>"></TD>
				</TR>
<%		}	%>
			<TR VALIGN="TOP">
			<TD WIDTH="20%" CLASS="text11bold">Size</TD>
			<TD WIDTH="80%">10k</TD>
			</TR>
			<TR VALIGN="TOP">
			<TD WIDTH="20%" CLASS="text11bold">Loaded</TD>
			<TD WIDTH="80%">mm/dd/yy H:MM pm</TD>
			</TR>
			<tr><td colspan="2" width="100%">
<%        if (media.getPK()!=null) {     %>
			 
<%			if (media.getMediaType().equalsIgnoreCase(MediaI.TYPE_IMAGE)) { // must be an image  %>
				<img src="<%=media.getPath()%>" width="<%=media.getWidth()%>" height="<%=media.getHeight()%>" border="0" alt="">
<% 			} else { %>
				<fd:IncludeMedia name='<%=media.getPath()%>'/>
<%			}  %>

<%        }  %>
			</td></tr>
<%		} else { %>
<tr><td colspan="2">
<%=media.getPath()%>&nbsp;<%=mediaDim%>&nbsp;<%=mediaPopupSize%>&nbsp;<%=mediaTitle%><br>
</td></tr>
<%		}
	} %>
</form>
</TABLE>
</tmpl:put>
<tmpl:put name='buttons'>
    <table width="45">
        <tr>
          <td colspan="3"><img src="<%= servletContext %>/images/clear.gif" width="1" height="5"></td>
        </tr>
		<tr>
		  <td width="20" bgcolor="#CC0000" class="button"><A HREF="javascript:window.close();" class="button">&nbsp;CLOSE&nbsp;</a></td>
		  <td><img src="<%= servletContext %>/images/clear.gif" width="5" height="1"></td>
		  <td width="20" bgcolor="#006600" class="button"><a HREF="javascript:mediaForm.submit();" class="button">&nbsp;SAVE&nbsp;</a></td>
		</tr>
    </table>
</tmpl:put>

<tmpl:put name='navHeader'>
<FONT CLASS="title13or">BROWSE ASSETS</FONT><BR>
<FONT CLASS="text12">(click to preview)</FONT><BR>
<FONT CLASS="space4pix"><BR></FONT>
</tmpl:put>
<tmpl:put name='navBody'>
<sa:Directory id="node" checkForFileInDb="true">
<%
//System.out.println(" node = "+path );
 if (node.isDirectory()) {
	String expandPath=path;
	if (expandParam!=null && expandParam.startsWith(path)) {
		expansionImg= " - ";
	    expandPath = path.substring(0,path.lastIndexOf(File.separator));
	    if ("".equals(expandPath)) {
           expandPath=File.separator;
        }
	} else {
		expansionImg = " + ";
	}

%>
    <tr valign="top">
		<td><img src="<%= request.getContextPath() %>/images/clear.gif" height="1" width="<%= 10 * (depth.intValue()-1) %>">
		<a href="<%=expandLink%>?expand=<%= expandPath %>"><%=expansionImg%><%= path.substring(path.lastIndexOf(java.io.File.separator)+1) %></a>
    </td></tr>
<%  } else if (!node.isDirectory() && isInDatabase.booleanValue() && !"NO_MEDIA_ID".equalsIgnoreCase(mediaId) ) {
%>
    <tr valign="top">
		<td><img src="<%= request.getContextPath() %>/images/clear.gif" height="1" width="<%= 10 * (depth.intValue()-1) %>">
		<a href="<%=expandLink%>?action=select&mediaId=<%=mediaId%>"><%= path.substring(path.lastIndexOf(java.io.File.separator)+1) %></a>
    </td></tr>
<%  } %>
</sa:Directory>
</tmpl:put>

<% } // End of check to see if we are closing the window %>
</tmpl:insert>
</sa:MediaSelection>