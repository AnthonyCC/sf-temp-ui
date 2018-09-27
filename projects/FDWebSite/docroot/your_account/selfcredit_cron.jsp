<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri="fd-data-potatoes" prefix="potato"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="fd-features" prefix="features"%>

<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' />
<features:isActive name="selfcredit" featureName="backOfficeSelfCredit" />
<features:redirectUnauthorizedFeature featureActive="${selfcredit}"
	featureName='backOfficeSelfCredit' />

<fd:SelfCreditCronTag />

<tmpl:insert template='/common/template/dnav.jsp'>

	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="FreshDirect - Your Profile" pageId="credit"></fd:SEOMetaTag>
	</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<h2>Credit auto-approval cron finished.</h2>
		<tr VALIGN="TOP">
			<TD WIDTH="35"><a href="/index.jsp"><img
					src="/media_stat/images/buttons/arrow_green_left.gif" border="0"
					alt="" ALIGN="LEFT"> CONTINUE SHOPPING <BR>from <FONT
					CLASS="text11bold">Home Page</A></FONT><BR> <IMG
				src="/media_stat/images/layout/clear.gif" alt="" WIDTH="340"
				HEIGHT="1" BORDER="0"></TD>
		</tr>

	</tmpl:put>
</tmpl:insert>