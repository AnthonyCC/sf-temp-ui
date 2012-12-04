<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus/>
<tmpl:insert template='<%="/common/template/blank.jsp" %>'>
<tmpl:put name='title' direct='true'>FreshDirect - Alcohol Information</tmpl:put>
<tmpl:put name='content' direct='true'>
<% String successPage = request.getParameter("successPage");
	String instant = request.getParameter("instant");
	String decorate = request.getParameter("decorate");
    request.setAttribute("listPos", "SystemMessage,SideCartBottom");
%>
<fd:HealthWarningController successPage="<%=successPage%>" result="result">
<table width="765" cellpadding="0" cellspacing="0" border="0">
	<form name="see_beer" method="POST">
	<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td></tr>
	<tr valign="bottom">
		<td colspan="2" align="center" valign="bottom">
			<img src="/media_stat/images/layout/993333.gif" width="100%" height="1"><BR>
		</td>
	</tr>
	
	<tr valign="top">
		<td width="202" align="left">
		<div class="space2pix"><br/></div>
			<img src="/media/editorial/site_pages/health_warning_usq_wine.gif" alt="Wine and Spirits Provided by Union Square Wines" width="202" height="80" border="0"><br/>
		</td>
		<td align="left">
		<div class="space2pix"><br/></div>
			<img src="/media/editorial/site_pages/health_warning_no_nj.gif" width="248" height="33" border="0" alt="Alcohol cannot be delivered outside of New York State"><br/>
		<div style="margin-left:1px; margin-right:16px; font-size:11px;">
			Beer and wine will be removed from your cart during checkout
			if an out-of-state address is selected for delivery.<br>
		</div>
		</td>
	</tr>
	</table>
	
	
	<table width="765" cellpadding="0" cellspacing="0" border="0">
	
	
	<tr valign="top">
		<td colspan="2" align="center">
			<img src="/media_stat/images/layout/993333.gif" width="100%" height="1"><br>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="text12">
						<br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br/>
						<b>Please note:</b><DIV CLASS="space2pix"><BR></DIV>
					</td>
				 </tr>
				 <tr>
					<td class="text12">
						<ul>
						<li>The person receiving your delivery must have identification proving they are over the age of 21, and they will be asked for their signature.<DIV CLASS="space4pix"><BR></DIV></li>
						<li>FreshDirect does not deliver alcohol to certain buildings.<DIV CLASS="space4pix"><BR></DIV></li>
						<li>Your invoice will show wine under the heading "Union Square Wine &amp; Spirits."<DIV CLASS="space4pix"><BR></DIV></li>
						<li>Union Square and FreshDirect are separate businesses and are not affiliated with each other.<DIV CLASS="space4pix"><BR></DIV></li>
	<!-- 					<li>Your credit card will be charged only once for your entire order, by FreshDirect.</li> -->
						<li>For orders containing both food and wine, your credit card or checking account will be charged separately by FreshDirect and Union Square.<DIV CLASS="space4pix"><BR></DIV></li>
						<li>You will receive your entire order in one delivery and be charged a single delivery fee (if applicable).</li>
						</ul>
					</td>
				 </tr>
				 
	     <tr><td><div style="text-align:center;"><a href="#" onclick="window.parent.FreshDirect.USQLegalWarning.blockSubmit(location.search); return false;"><img name="dont_see_beer_button" src="/media_stat/images/wine/decline.gif" border="0" alt="Click here to decline"></a>
	     <%if (instant != null && !"".equals(instant)) { %>
		     <a href="#" onclick="if(window.parent.FreshDirect.USQLegalWarning.checkHealthCondition('freshdirect.healthwarning','1')==false) {window.parent.FreshDirect.USQLegalWarning.setCookie('freshdirect.healthwarning','1')};<%=decorate%><%=instant%>;window.parent.FreshDirect.USQLegalWarning.blockSubmit(location.search); return false;"><img name="see_beer_button" src="/media_stat/images/wine/click_here_to_accept.gif" border="0" alt="Click here to accept"></a><br><br><br>
	     <%} else { %>
		     <a href="#" onclick="if(window.parent.FreshDirect.USQLegalWarning.checkHealthCondition('freshdirect.healthwarning','1')==false) {window.parent.FreshDirect.USQLegalWarning.setCookie('freshdirect.healthwarning','1')};<%=decorate%>window.parent.FreshDirect.USQLegalWarning.doNormalSubmit(location.search); return false;"><img name="see_beer_button" src="/media_stat/images/wine/click_here_to_accept.gif" border="0" alt="Click here to accept"></a><br><br><br>
	     <%}  %></div>
	     <font class="text12"><b>GOVERNMENT WARNING: </b></font><br>
	     ACCORDING TO THE SURGEON GENERAL, WOMEN SHOULD NOT DRINK ALCOHOLIC BEVERAGES DURING PREGNANCY BECAUSE OF THE RISK OF BIRTH DEFECTS.
	     <br><br>
	     <font class="text12"><b>WARNING</b><br>
	     No person shall sell or give away any alcoholic beverages to:</font><br><br>
	     </td></tr>
	     <tr><td class="text12"> 
	     <ol><li>Any person under the age of twenty-one years; or</li><li>Any visibly intoxicated person.</li></ol>
	
	     <span class="text11">IT IS A VIOLATION PUNISHABLE UNDER LAW FOR ANY PERSON UNDER THE AGE OF TWENTY-ONE TO PRESENT ANY WRITTEN EVIDENCE OF AGE WHICH IS FALSE, FRAUDULENT OR NOT ACTUALLY HIS OWN FOR THE PURPOSE OF ATTEMPTING TO PURCHASE ANY ALCOHOLIC BEVERAGE.</span>
	      <br><br>
	     </td></tr>
	     </table>
	</td>
	</tr>
	</form>
</table>
</fd:HealthWarningController>
<script type="text/javascript">
	YAHOO.util.Event.onDOMReady(function() {

<%-- 		var f = window.parent.document.getElementById('usq_legal_warning_choice_frame_<%= request.getParameter("formId") %>'); --%>
<%-- 		var wdh = getFrameWidth('usq_legal_warning_choice_frame_<%= request.getParameter("formId") %>'); --%>
<%-- 		var hgt = getFrameHeight('usq_legal_warning_choice_frame_<%= request.getParameter("formId") %>'); --%>
// 		f.style.width = (wdh+20)+"px";
// 		f.style.height = (hgt+20)+"px";
		window.parent.FreshDirect.USQLegalWarning.refreshPanel(location.search);
		//resize trick to make the scroll bars disappear
	});
</script>
</tmpl:put>

</tmpl:insert>

