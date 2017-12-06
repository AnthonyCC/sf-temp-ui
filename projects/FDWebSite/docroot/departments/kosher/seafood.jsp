<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_KOSHER_SEAFOOD_TOTAL = 765;
%>

<fd:CheckLoginStatus guestAllowed="true" />

<tmpl:insert template='/common/template/right_dnav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Kosher Seafood - Coming Soon!"/>
  </tmpl:put>
<%--   <tmpl:put name='title'>FreshDirect - Kosher Seafood - Coming Soon!</tmpl:put> --%>

	<tmpl:put name='content' direct='true'>
	
	<table width="<%=W_KOSHER_SEAFOOD_TOTAL%>" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td align="center" colspan="3">
			<img src="/media_stat/images/template/kosher/ks_soon_stars_top.gif" width="765" height="31"><br>
			<img src="/media_stat/images/template/kosher/ks_soon_fishes.jpg" width="541" height="60" vspace="6"><br>
			<img src="/media_stat/images/template/kosher/ks_soon_kosher_seafood.gif" width="539" height="52" vspace="2"><br><br>
			</td>
		</tr>
		<tr valign="top">
			<td width="30"><img src="/media_stat/images/template/kosher/ks_soon_ou.gif" width="25" height="25" vspace="6"></td>
			<td width="<%=W_KOSHER_SEAFOOD_TOTAL-60%>" align="center">
			Get ready for the freshest Kosher seafood. We're preparing a full line of your favorite Kosher fish &#151; all hand-cut and wrapped to order under the watchful eye of OU and KAJ supervision.
			<br><b>Check back soon!</b>
			</td>
			<td width="30" align="right"><img src="/media_stat/images/template/kosher/ks_soon_kaj.gif" width="25" height="25" vspace="6"></td>
		</tr>
		<tr>
			<td align="center" colspan="3">
			<img src="/media_stat/images/template/kosher/ks_soon_stars_bot.gif" width="765" height="23" vspace="12"><br>
			</td>
		</tr>
	</table>

    </tmpl:put>

</tmpl:insert>
