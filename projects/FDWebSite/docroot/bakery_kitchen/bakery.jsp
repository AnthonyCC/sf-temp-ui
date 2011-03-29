<%	
	String pageNo;
	int intPageNo;
	String nextPage = "";
	int intNextPageNo;
	String previousPage = "";
	int intPrePageNo;
	
	pageNo = request.getParameter("pageNo");
	intPageNo =  Integer.parseInt(pageNo);
	
	intNextPageNo = intPageNo + 1;
	intPrePageNo = intPageNo - 1;
	%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>FreshDirect - Kitchen</title>

	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<BODY BGCOLOR="White" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10" leftmargin=0 topmargin=0>
<CENTER>
<table width="525" border="0" cellspacing="0" cellpadding="0">
<tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" border="0"></td></tr>
<tr>
	<td>
	 <table border="0" cellspacing="0" cellpadding="0">
	 <tr>
	 	<td align="left"><img src="/media_stat/images/template/kitchen/kb_pop_logo.gif" alt="" border="0"></td>
		<td valign="bottom" align="right"><a href="javascript:self.close();">close window</a></td>
	 </tr>
	 <tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
	 <tr>
	 	<td colspan=2 bgcolor="#669933" ><img src="/media_stat/images/layout/669933.gif" width="525" height="2" alt="" border="0"></td>
	 </tr>
	 <tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
	 <tr>
	 	<td align="left"><img src="/media_stat/images/template/kitchen/kb_pop_preview_hdr.gif" alt="" border="0"></td>
		<td align="right">
		<img src="/media_stat/images/layout/clear.gif" width="200" height="1" alt="" border="0"><br>
		<%@ include file="/bakery_kitchen/includes/bakery_nav.jspf" %>
		</td>
	 </tr>	 
	 </table>

	</td>
</tr>
<tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="25" alt="" border="0"></td></tr>
<tr>
	<td>

	<% if (pageNo.equals("1")){
		nextPage = "Our Kitchen";
	%>
	
		<%@ include file="/bakery_kitchen/includes/i_bakery_1.jspf" %>
	<%
	}
	else if(pageNo.equals("2")){
		previousPage = "Preview our Kitchen and Bakery";
		nextPage = "Sample Menu";
	%>
		<%@ include file="/bakery_kitchen/includes/i_bakery_2.jspf" %>
	<%} 
	else if(pageNo.equals("3")){
		previousPage = "Our Kitchen";
		nextPage = "Sample Entr&eacute;e";
	%>
		<%@ include file="/bakery_kitchen/includes/i_bakery_3.jspf" %>
	<%} 
	else if(pageNo.equals("4")){
		previousPage = "Sample Menu";
		nextPage = "Our Pasta Bar";
	%>
		<%@ include file="/bakery_kitchen/includes/i_bakery_4.jspf" %>
	<%} 
	else if(pageNo.equals("5")){
		previousPage = "Sample Entr&eacute;e";
		nextPage = "";	
	%>
		<%@ include file="/bakery_kitchen/includes/i_bakery_5.jspf" %>
	<%} 
	else{
		previousPage ="";
		nextPage = "";	
	%>
	
		<%@ include file="/about/index.jsp" %>
	
	<%} 

%>				


	</td>
</tr>
<tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" border="0"></td></tr>
<tr>
	<td>
	 <table border="0" cellspacing="0" cellpadding="0">
	 <tr>
	 	<td align="left"></td>
		<td valign="bottom" align="right"><a href="javascript:self.close();">close window</a></td>
	 </tr>
	 <tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
	 <tr>
	 	<td colspan=2 bgcolor="#669933"><img src="/media_stat/images/layout/669933.gif" width="525" height="1" alt="" border="0"></td>
	 </tr>
	 <tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>
	 <tr>
	 	<td align="left"></td>
		<td align="center">

		
			<table cellspacing="0" cellpadding="0" border="0">
			<tr>
	<% 	if (!pageNo.equals("1")){%>
				<td rowspan="2">
					<a href="/bakery_kitchen/bakery.jsp?pageNo=<%=intPrePageNo%>"><img src="/media_stat/images/template/kitchen/kb_pop_arrow_l.gif" width="27" height="27" alt="" border="0"></a>
				</td>
		        <td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>				
			    <td width=200 align="left"><a href="/bakery_kitchen/bakery.jsp?pageNo=<%=intPrePageNo%>"><img src="/media_stat/images/template/kitchen/kb_pop_previous.gif" width="52" height="8" alt="" border="0"></a></td>
	<%}
		else{%>
	<td rowspan="2"></td><td rowspan="2"></td><td width=250><img src="/media_stat/images/layout/clear.gif" width="250" height="1" alt="" border="0"></td>
	<%}%>				
				<td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="50" height="1" alt="" border="0"></a>
	
	<% 	if (!pageNo.equals("5")){%> 	    
				<td width=200 align="right"><a href="/bakery_kitchen/bakery.jsp?pageNo=<%=intNextPageNo%>"><img src="/media_stat/images/template/kitchen/kb_pop_next.gif" width="27" height="9" alt="" border="0"></a></td>
			    <td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
				<td rowspan="2">
					<a href="/bakery_kitchen/bakery.jsp?pageNo=<%=intNextPageNo%>"><img src="/media_stat/images/template/kitchen/kb_pop_arrow_r.gif" width="27" height="27" alt="" border="0"></a>
				</td>
	<%}
		else{%>
				<td width=250><img src="/media_stat/images/layout/clear.gif" width="250" height="1" alt="" border="0"></td><td rowspan="2"></td><td rowspan="2"></td>
	<%}%>			
			</tr>
			<tr>
			    <td align="left"><%=previousPage%></td>
				<td align="right"><%=nextPage%></td>
			</tr>
			</table>
			
		</td>
	 </tr>	 
	 </table>

	</td>
</tr>
<tr><td colspan=2><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" border="0"></td></tr>
</table>
</CENTER>
</BODY>
</HTML>

