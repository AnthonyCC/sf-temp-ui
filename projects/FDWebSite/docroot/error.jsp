<%@ page isErrorPage="true" %>
<%@ page import='com.freshdirect.webapp.util.JspLogger' %>
<%@ taglib uri='template' prefix='tmpl' %>
<% response.setStatus(500); %> 
<tmpl:insert template='/common/template/no_space_border.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect</tmpl:put>
		<tmpl:put name='content' direct='true'>
<center>
<table border="0" cellpadding="0" cellspacing="0" width="600">
<tr>
	<td align="center">
	<!-- 
		<img src="/media_stat/images/template/error/error_01.gif" alt="ERR" border="0"><img src="/media_stat/images/template/error/error_02.jpg"  alt="O" border="0"><img src="/media_stat/images/template/error/error_03.gif"  alt="R" border="0">
	 -->
	</td>
</tr>
		<tr>
		    <TD>

					<br><br>
					<table cellspacing="0" cellpadding="0" border="0" width="100%">
					<tr>
					    <td rowspan="5" width="20"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
					    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_lft_crnr.gif" width="18" height="5" border="0"></td>
					    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
					    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_rt_crnr.gif" width="6" height="5" border="0"></td>
					    <td rowspan="5"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
					</tr>
					<tr>
					    <td rowspan="3"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
					    <td width="100%"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
					</tr>
					<tr>
					    <td width="18" bgcolor="#CC3300"><img src="/media_stat/images/template/system_msgs/exclaim_CC3300.gif" width="18" height="22" border="0" alt="!"></td>
					    <td class="text11rbold">
														
								Sorry, the Web site is unable to process that request. Please click your browser's back button and try again. If that does not work, contact FreshDirect customer service at 1-212-796-8002. We apologize for any inconvenience.
								<br><br>
								Reference: 
		    	<% if (exception!=null) { 
				%>
					<%=String.valueOf(exception)%>
					<% JspLogger.GENERIC.error("Got an error page", exception); %>
				<% } %>
				<br><br>



								<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
						
						</td>
					    <td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
					    <td bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
					</tr>
					<tr>
					    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_lft_crnr.gif" width="18" height="5" border="0"></td>
					    <td><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
					    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_rt_crnr.gif" width="6" height="5" border="0"></td>
					</tr>
					<tr>
					    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
					</tr>
					</table>
					<br>			
					<br><br>

			</TD>
		</tr>
		<tr>
		    <TD align="center">

				<a href="/index.jsp" onmouseover="swapImage('home_img','/media_stat/images/template/help/help_home_r.gif')" onmouseout="swapImage('home_img','/media_stat/images/template/help/help_home.gif')"><img src="/media_stat/images/template/help/help_home.gif" name="home_img" width="71" height="26" alt="" border="0"></a>
				<br>Continue shopping from <a href="/index.jsp">Home Page</a>
				<br><br>
			</TD>		
		</tr>
</table>
</center>
</tmpl:put>
</tmpl:insert>
