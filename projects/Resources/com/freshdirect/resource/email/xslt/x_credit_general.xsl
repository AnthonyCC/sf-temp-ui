<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v1.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
Dear <xsl:value-of select="customer/firstName"/>,

<xsl:if test="string-length(complaint/customerEmail/customMessage) &gt; 0">
<xsl:value-of select='complaint/customerEmail/customMessage'/>
 
</xsl:if>
We have issued you the following credits for order #<xsl:value-of select="saleId"/>: 

<xsl:if test="complaint/storeCreditAmount &gt; 0">
<xsl:for-each select="complaint/complaintLinesAggregated/complaintLinesAggregated">
<xsl:if test="not(number(amount)=0)">
<xsl:if test="method = 'FDC'">
    <xsl:variable name="deptName">
	    <xsl:if test="departmentCode = 'GDW'">Customer Service</xsl:if>
	    <xsl:if test="not(departmentCode='GDW')"><xsl:value-of select="departmentName"/></xsl:if>
   </xsl:variable>
$<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/> from the <xsl:value-of select="$deptName"/> Department in store credit<xsl:text>
</xsl:text></xsl:if></xsl:if>
</xsl:for-each>
$<xsl:value-of select='format-number(complaint/storeCreditAmount, "###,##0.00", "USD")'/> total store credit<xsl:text>

</xsl:text>
<xsl:if test="complaint/cashBackAmount &gt; 0">
plus...<xsl:text>

</xsl:text></xsl:if>
</xsl:if>
<xsl:if test="complaint/cashBackAmount &gt; 0">
<xsl:for-each select="complaint/complaintLinesAggregated/complaintLinesAggregated">
<xsl:if test="not(number(amount)=0)">
<xsl:if test="method = 'CSH'">
<xsl:variable name="deptName">
    <xsl:if test="departmentCode = 'GDW'">Customer Service</xsl:if>
    <xsl:if test="not(departmentCode='GDW')"><xsl:value-of select="departmentName"/></xsl:if>
</xsl:variable>
$<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/><xsl:text> </xsl:text>from the <xsl:value-of select="$deptName"/> Department charged back to your credit card<xsl:text>
</xsl:text></xsl:if></xsl:if>
</xsl:for-each>
$<xsl:value-of select='format-number(complaint/cashBackAmount, "###,##0.00", "USD")'/> total charged back to your credit card 
</xsl:if>

$<xsl:value-of select='format-number(complaint/amount, "###,##0.00", "USD")'/> TOTAL CREDIT 

<xsl:if test="complaint/storeCreditAmount &gt; 0">Please note it takes approximately two business days for your store credit to become available. Once it is available we'll automatically subtract these store credits from your next order total at the last stage of checkout.</xsl:if>

<xsl:if test="complaint/cashBackAmount &gt; 0">Please note that this credit should reflect on your credit card within five business days.</xsl:if>
 <xsl:text>
 
</xsl:text>
I'd like to thank you for letting us know what occurred and giving us the opportunity to help solve your problem. If, for any reason you're dissatisfied with how we resolved your problem, or need further assistance, please feel free to e-mail us directly by clicking on the contact us link on the website or call us at 212-796-8002.<br/>

Your satisfaction is our number one priority!

Sincerely, <xsl:text>

</xsl:text>
<xsl:if test="string-length(complaint/customerEmail/signature) &gt; 0">
<xsl:value-of select="complaint/customerEmail/signature"/><br/>
</xsl:if><xsl:text>
</xsl:text>Your FreshDirect Customer Service Team
Keep in mind that we are here for you:

Monday-Thursday 6:30am-12am
Fridays 6:30am-11pm
Saturdays 7:30am-8pm
Sundays 7:30am-12am
 

<xsl:call-template name="x_footer_v1"/>

</xsl:template>

</xsl:stylesheet>