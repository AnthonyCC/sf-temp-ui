<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear Symbol Technologies Corporate Depot Customer,

For some time now, Freshdirect has been delivering fresh food and groceries to Long Island corporate locations. Early feedback was that everyone loved shopping online and having their groceries delivered to the trunk of their car. Not only was it a time-saver, it was a great new way to receive better quality foods, at better prices, to bring home to your family.

Recent customer satisfaction surveys show that everyone still loves the quality of food, everyday low prices, convenience and overall lifestyle benefit provided by having extra time to do other things. Unfortunately, the survey results also address the overriding concern about the limited delivery days and time slots. We understand your view and have come to agree that providing a limited service is no substitute for a full service. It makes it difficult for you, as well as us, to plan accordingly. 

In the near future, we do plan to expand our service to include home deliveries to Long Island. We will provide delivery schedules that address the daily needs and lifestyles of the consumer market at large.

So now, after much discussion and thought, we regret to inform you, effective week ending January 16, 2004 we will no longer be delivering to your location. We truly want to thank you for allowing us to serve you in your place of work. And to those of you who utilized Freshdirect on a regular basis we sincerely apologize. We would appreciate the opportunity of serving you again.

Customers who have accounts with FreshDirect can still log in to view their account details (https://www.freshdirect.com/your_account/manage_account.jsp).

Anyone in the Tri-State area (including existing depot and home delivery customers) can place an order online for pickup at our facility, located just outside the Midtown Tunnel in Long Island City, Queens.

Start shopping: http://www.freshdirect.com

FreshDirect
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>