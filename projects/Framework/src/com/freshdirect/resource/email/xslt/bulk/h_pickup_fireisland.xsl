<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"
	xmlns:str="http://xsltsl.org/string"
	extension-element-prefixes="doc str">
	<xsl:output method="html"/>
	<xsl:include href='h_header_v1.xsl'/>
    <xsl:include href='h_optout_footer.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:include href='string.xsl' />
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
		<title>FreshDirect announces Fire Island dockside deliveries.</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333" marginwidth="20" marginheight="10" leftmargin="20" topmargin="10">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<xsl:comment>

www.FRESHDIRECT.com
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

SUMMER DOCKSIDE DELIVERY TO FIRE ISLAND
( http://www.freshdirect.com/help/delivery_fire_island.jsp?trk=fi_email )
Our trucks are taking to the water this summer, delivering the best food in New York to Fire Island in partnership with Fire Island Ferries. Order on the Web today and let us custom-prepare everything, from the thickness of your steaks to the grind of your coffee. All summer long, we'll be making deliveries every Friday evening to Fair Harbor and Ocean Beach, direct to the dock in our refrigerated trucks.

HOW IT WORKS
Just browse our site for your favorite summertime foods, and complete Checkout to place your order. You may place your order up to one week before delivery and as late as the night before (order early because delivery slots fill up quickly). When you check out, select from one of our Fire Island locations. 

Every Friday, we'll be at: 
Fair Harbor dock from 4:00 pm to 6:00 pm
Ocean Beach dock from 6:15 pm to 9:30 pm

We'll bring your order on our hard-to-miss floating truck. Bring a wagon (or a boat) for your boxes and a cooler for frozen items if you have a long way to go. Your food will be kept cool in our refrigerated truck until you pick it up. We'll ask to see an ID but shirt and shoes are not required.

Learn more at: http://www.freshdirect.com/help/delivery_fire_island.jsp?trk=fi_email

Fire Island Ferry schedules: http://www.pagelinx.com/fififerry/index.shtml

======
When you signed up with FreshDirect, you indicated an interest in receiving newsletters and updates. You may change this preference online in Your Account or REPLY via email and write UNSUBSCRIBE in the subject line.
======

QUICK LINKS:

Go to FreshDirect
http://www.freshdirect.com

Contact Us
http://www.freshdirect.com/help/contact_fd.jsp

Frequently Asked Questions
http://www.freshdirect.com/help/faq_home.jsp

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
(c) 2002, 2003 FRESHDIRECT. All Rights Reserved.

</xsl:comment>

<table cellpadding="0" cellspacing="0" width="554" align="center">
<tr>
    <td colspan="2" align="center"><a href="http://www.freshdirect.com/help/delivery_fire_island.jsp?trk=fi_email"><img src="http://www.freshdirect.com/media_stat/images/template/email/pickup/fi_hdr.gif" width="552" height="110" border="0" alt="FreshDirect in Partnership with Fire Island Ferries" /><br/>
    <img src="http://www.freshdirect.com/media_stat/images/template/email/pickup/fi_floating_food.jpg" width="552" height="230" border="0" vspace="8" alt="Now Delivering to Fire Island!" /><br/>
    <img src="http://www.freshdirect.com/media_stat/images/template/email/pickup/fi_betterfood.gif" width="554" height="17" border="0" alt="Better Food. Better Prices. Delivered to Fair Harbor &amp; Ocean Beach" /></a><br/></td>
    </tr>            
    <tr valign="top">
        <td width="450"><img src="http://www.freshdirect.com/media_stat/images/template/email/pickup/fi_delivery.gif" width="256" height="9" border="0" alt="SUMMER DOCKSIDE DELIVERY TO FIRE ISLAND" /><br/>
        Our trucks are taking to the water this summer, delivering the best food in New York to Fire Island in partnership with Fire Island Ferries. Order on the Web today and let us <b>custom-prepare everything</b>, from the thickness of your steaks to the grind of your coffee. All summer long, we'll be making deliveries every <b>Friday evening to Fair Harbor and Ocean Beach, direct to the dock in our refrigerated trucks</b>.
        <br/><br/>
        <img src="http://www.freshdirect.com/media_stat/images/template/email/pickup/fi_how.gif" width="87" height="9" border="0" alt="HOW IT WORKS" /><br/>
        Just browse our site for your favorite summertime foods, and complete Checkout to place your order. You may place your order up to one week before delivery and as late as the night before (order early because delivery slots fill up quickly). When you check out, select from one of our Fire Island locations.
        <br/><br/>
        <b>Every Friday, we'll be at:<br/> 
Fair Harbor dock from 4:00 pm to 6:00 pm<br/>
Ocean Beach dock from 6:15 pm to 9:30 pm</b>
        <br/><br/>
We'll bring your order on our hard-to-miss floating truck. Bring a wagon (or a boat) for your boxes and a cooler for frozen items if you have a long way to go. Your food will be kept cool in our refrigerated truck until you pick it up. We'll ask to see an ID but shirt and shoes are not required.
        <br/><br/>
        <a href="http://www.freshdirect.com/help/delivery_fire_island.jsp?trk=fi_email"><b>Click here to find out more</b></a>.
        <br/><br/>
		</td>
        <td width="105" align="right"><a href="http://www.pagelinx.com/fififerry/index.shtml" target="fi_sched"><img src="http://www.freshdirect.com/media_stat/images/template/email/pickup/fi_boat.jpg" width="93" height="60" border="0" hspace="1" /><br/><img src="http://www.freshdirect.com/media_stat/images/template/email/pickup/fi_schedules.gif" width="93" height="87" border="0" hspace="1" /></a></td>
	</tr>
<tr><td colspan="2"><p><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></p></td></tr>
</table>

</xsl:template>

</xsl:stylesheet>