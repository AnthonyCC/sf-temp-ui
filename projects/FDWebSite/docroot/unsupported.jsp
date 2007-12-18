<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus guestAllowed="true" />
<tmpl:insert template='/common/template/right_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Unsupported browser</tmpl:put>
<tmpl:put name='content' direct='true'>

<%
    String referer = request.getHeader("Referer") == null || request.getHeader("Referer").length() == 0
                   ? "/index.jsp"
                   : request.getHeader("Referer");
%>

<table width="450" cellpadding="0" cellspacing="0" border="0">
<tr>
<td colspan="2">
	<fd:IncludeMedia name="/media/editorial/site_pages/lists/unsupported.html">
        <font class="text12">
            <br/>
            <b>Unsupported Browser</b>
            <br/>
            <br/>
            We're sorry, the web browser you are using is not supported for this
            feature. You'll need to update your browser to the current version
            &#8212; or enable JavaScript &#8212; to use shopping lists. 
        </font>
    </fd:IncludeMedia>
</td></tr>
<tr><td>
<font class="space4pix"><br/><br/><br/></font>
</td></tr>
<tr valign="top">
<td width="35">
    <a href="<%= referer %>"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a>
</td>
<td width="640"  class="text11" >
    <a href="<%= referer %>"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
</td>
</tr>
</table>
</tmpl:put>

</tmpl:insert>
