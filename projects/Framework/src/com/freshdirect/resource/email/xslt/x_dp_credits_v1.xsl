<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
    <xsl:import href='stdlib.xsl'/>
    <xsl:include href='x_common_functions_v1.xsl'/>
    <xsl:include href='x_footer_v1.xsl'/>
    <xsl:output method="text"/>
    <xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
Dear <xsl:value-of select="customer/firstName"/>,


We apologize for any inconvenience you may have experienced with your order #<xsl:value-of select="saleId"/>.
We have issued you the following credit:
 

<xsl:if test="dpName='UNLIMITED'">
	<xsl:if test="number(creditCount)=1">
	Your DeliveryPass has been extended by one additional week. 
      </xsl:if>
	<xsl:if test="number(creditCount)=2">
     	Your DeliveryPass has been extended by two additional weeks. 
	</xsl:if>
	<xsl:if test="creditCount=3">
      Your DeliveryPass has been extended by three additional weeks. 
	</xsl:if>
</xsl:if>
<xsl:if test="dpName='BSGS'">
	<xsl:if test="number(creditCount)=1">
      One free delivery has been added to your DeliveryPass.
      </xsl:if>
	<xsl:if test="number(creditCount)=2">
      Two free deliveries have been added to your DeliveryPass.
      </xsl:if>
	<xsl:if test="creditCount=3">
      Three free deliveries have been added to your DeliveryPass.
      </xsl:if>
</xsl:if>
As always, We invite you to let us know how we're doing.
http://www.freshdirect.com/help/contact_fd.jsp

We greatly appreciate your business and hope you'll visit us again soon.

Sincerely,

Your Customer Service Team at FreshDirect

<xsl:call-template name="x_footer_v1"/>

</xsl:template>

</xsl:stylesheet>