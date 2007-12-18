<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
    <xsl:import href='stdlib.xsl'/>
    <xsl:include href='x_common_functions_v1.xsl'/>
    <xsl:include href='x_footer_v1.xsl'/>
    <xsl:output method="text"/>
    <xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
Dear <xsl:value-of select="customer/firstName"/>,

Thank you for helping us to serve you better. We have issued you the following credits for order #<xsl:value-of select="saleId"/>:

<xsl:if test="complaint/storeCreditAmount &gt; 0">
<xsl:for-each select="complaint/complaintLines/complaintLines">
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
<xsl:for-each select="complaint/complaintLines/complaintLines">
<xsl:if test="not(number(amount)=0)">
<xsl:if test="method = 'CSH'">
    <xsl:variable name="deptName">
	    <xsl:if test="departmentCode = 'GDW'">Customer Service</xsl:if>
	    <xsl:if test="not(departmentCode='GDW')"><xsl:value-of select="departmentName"/></xsl:if>
   </xsl:variable>
$<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/><xsl:text> </xsl:text>from the <xsl:value-of select="$deptName"/> Department charged back to your payment account
<br/>
</xsl:if></xsl:if>
</xsl:for-each>
$<xsl:value-of select='format-number(complaint/cashBackAmount, "###,##0.00", "USD")'/> total charged back to your payment account
</xsl:if>

$<xsl:value-of select='format-number(complaint/amount, "###,##0.00", "USD")'/> TOTAL CREDIT

<xsl:if test="complaint/storeCreditAmount &gt; 0">We'll automatically subtract these store credits from your next order total.</xsl:if>

We invite you to let us know how we're doing.
http://www.freshdirect.com/help/contact_fd.jsp

Thank you for shopping with us. We greatly appreciate your business and hope you'll come back soon.

Sincerely,

FreshDirect
Customer Service Group

<xsl:call-template name="x_footer_v1"/>

</xsl:template>

</xsl:stylesheet>