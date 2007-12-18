<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Thank you for your order with FreshDirect.

At FreshDirect we are very conscious of the quality of the products we receive in our facility from our partners around the world. We find that today's shipment of  whole milk (quarts) was unacceptable and did not meet our fresh standards. We know all of our customers appreciate fresh milk, so we returned today's shipment to our suppliers. We apologize for the milk shortage in your delivery today and the inconvenience, but we will only sell products we feel to be of the freshest quality. We have already removed the item from your credit card, but we wanted you to know why the milk was missing from your delivery.

You may, however, order our new shipment of milk arriving tomorrow.

If you have any other questions, please call us toll-free at 1-866 2UFRESH (1-866 283-7374). We're here Sunday-Friday 9am-midnight and Saturdays 9am-9pm. Or, you may email us 24 hours a day at service@freshdirect.com

Sincerely,
FreshDirect Customer Service
www.freshdirect.com 
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
