<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
A SPECIAL OFFER FOR FRESHDIRECT CUSTOMERS

In our effort to bring our customers the best culinary experiences, FreshDirect has partnered with Chef's Theater, a delicious new kind of entertainment experience. Chef's Theater, staged at the elegant Supper Club, combines the best of fine dining with the best of Broadway... for a one-of-a-kind night on the town. Some of the culinary stars of the show include: 

Todd English, Olives  March 30 - April 4 
Tom Valenti, 'Cesca, Ouest  April 6 - April 11 
Food Network's Tyler Florence  April 13 - April 18 
Mary Sue Milliken and Susan Feniger (formerly Too Hot  Tamales), Border Grill April 20 - April 25 
Michael Lomonaco, Noche  April 27 - May 2 
Michael Romano, Union Square Café  May 4 - May 9 

FreshDirect and Chef's Theater are proud to provide an exclusive delicious deal for FreshDirect customers: 

	Buy two dinner tickets, save $30* 
	Buy two brunch tickets, save $20* 
	Buy two dessert tickets, save $10* 

Orders must be placed by April 13, 2004. Offer good for all performances through June 27, 2004. 

To take advantage of this special offer, visit www.broadwayoffers.com and enter code CFFD459, or call Telecharge.com at 212-947-8844 and mention code CFFD459. 

*Offer subject to availability. Offer may be revoked at any time. Tickets must be ordered by 4/13/04. Offer valid through 6/27/04. Ticket prices do not include gratuity. 
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
