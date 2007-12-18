<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus guestAllowed="true" />
<%--fd:Department id='department' departmentId='<%= request.getParameter("deptId") %>'/--%>

<tmpl:insert template='/common/template/right_nav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Kosher - Coming Soon!<%--= department.getFullName() --%></tmpl:put>
    
	<tmpl:put name='content' direct='true'>
	
	<table width="550" cellpadding="0" cellspacing="6" border="0">
		<tr>
			<td align="center">
			<br>
			<img src="/media_stat/images/template/kosher/ksoon_coming_soon.gif" width="175" height="17"><br>
			<img src="/media_stat/images/template/kosher/ksoon_stars.gif" width="540" height="23" vspace="6"><br>
			<img src="/media_stat/images/template/kosher/ksoon_kosher.gif" width="536" height="119"><br>
			<img src="/media_stat/images/template/kosher/ksoon_stars.gif" width="540" height="23" vspace="6"><br>
			<font class="title18">You ask, we listen.</font>
			</td>
		</tr>
		<tr>
			<td width="540">
			FreshDirect is preparing a full line of custom-cut Glatt Kosher meat as well as the freshest Kosher seafood. We've partnered with Rubashkin's &#151; home of Aaron's Best Glatt Kosher meat to bring you and your family the finest. Hand-cut just for your order, all of our Kosher meat is certified under the watchful eyes of OU and KAJ supervision.
			</td>
		</tr>
		<tr>
			<td align="center">
			<img src="/media_stat/images/template/kosher/ksoon_icons.jpg" width="543" height="58" vspace="4">
			<img src="/media_stat/images/template/kosher/ksoon_stars.gif" width="540" height="23" vspace="2">
			</td>
		</tr>
	</table>

    </tmpl:put>

</tmpl:insert>