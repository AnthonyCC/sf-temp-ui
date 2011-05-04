<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">


<xsl:call-template name="x_header"/>

Dear <xsl:value-of select="customer/firstName"/>,

<xsl:if test="string-length(complaint/customerEmail/customMessage) &gt; 0">
<xsl:value-of select='complaint/customerEmail/customMessage'/><xsl:text>
</xsl:text> 
</xsl:if>

Thank you for allowing us to serve you better. We have issued you the following credits for order #<xsl:value-of select="saleId"/>:

$<xsl:value-of select='format-number(complaint/cashBackAmount, "###,##0.00", "USD")'/> from the FreshDirect 
<xsl:if test="departmentCode = 'GDW'">
<xsl:text>Customer Service</xsl:text> 
</xsl:if>
<xsl:if test="not(departmentCode='GDW')">
<xsl:value-of select="departmentName"/>
</xsl:if> store credited back to your credit card.

<xsl:if test="complaint/storeCreditAmount &gt; 0">
<xsl:for-each select="complaint/complaintLinesAggregated/complaintLinesAggregated">
<xsl:if test="not(number(amount)=0)">
<xsl:if test="method = 'FDC'">
$<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/><xsl:text>  </xsl:text> from the 
<xsl:if test="departmentCode = 'GDW'">
<xsl:text>Customer Service</xsl:text> 
</xsl:if>
<xsl:if test="not(departmentCode='GDW')">
<xsl:value-of select="departmentName"/>
</xsl:if>
Department in store credit
</xsl:if>
</xsl:if>
</xsl:for-each>

$<xsl:value-of select='format-number(complaint/storeCreditAmount, "###,##0.00", "USD")'/> total store credit

<xsl:if test="complaint/cashBackAmount &gt; 0">
plus...

</xsl:if>
</xsl:if>

<xsl:if test="complaint/cashBackAmount &gt; 0">

Certificate # Card Type Amount

<xsl:for-each select="complaint/complaintLines/complaintLines">
<xsl:value-of select="certificateNumber"/>
<xsl:value-of select="templateId"/>
$<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/>


</xsl:for-each>	

<!--<xsl:if test="not(number(amount)=0)">
<xsl:if test="method = 'CSH'">
$<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/><xsl:text>  </xsl:text> 
store credited back to your credit card.
</xsl:if>
</xsl:if>-->

<!--$<xsl:value-of select='format-number(complaint/cashBackAmount, "###,##0.00", "USD")'/> total charged back to your credit card<br/>-->

</xsl:if>

$<xsl:value-of select='format-number(complaint/amount, "###,##0.00", "USD")'/> TOTAL CREDIT
<xsl:if test="complaint/storeCreditAmount &gt; 0">Please note it takes approximately two business days for your store credit to become available. Once it is available we'll automatically subtract these store credits from your next order total at the last stage of checkout.</xsl:if>
<xsl:if test="complaint/cashBackAmount &gt; 0">Please note that this credit should reflect on your credit card within five business days.</xsl:if>

I'd like to thank you for letting us know what occurred and giving us the opportunity to help solve your problem. If, for any reason you're dissatisfied with how we resolved your problem, or need further assistance, please feel free to e-mail us directly by clicking on the contact us link on the website or call us at 212-796-8002.

Your satisfaction is our number one priority!

Sincerely,
<xsl:if test="string-length(complaint/customerEmail/signature) &gt; 0">
<xsl:value-of select="complaint/customerEmail/signature"/>
</xsl:if>

FreshDirect
<xsl:choose><xsl:when test="order/deliveryType != 'C'">Customer Service Group</xsl:when><xsl:otherwise>Corporate Services Group</xsl:otherwise></xsl:choose>

View this order (and get printable versions of your gifts) in Your Account: 
http://www.freshdirect.com/your_account/manage_account.jsp
<xsl:call-template name="x_footer_v1"/>
</xsl:template>

</xsl:stylesheet>