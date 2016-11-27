<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v1.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
Hello <xsl:value-of select="customer/firstName"/>,

We just wanted to confirm that you've successfully made a change to a delivery address associated with your account:

							<xsl:value-of select="erpAddressModel/firstName"/><xsl:text> </xsl:text> 
                            <xsl:value-of select="erpAddressModel/lastName"/>
                            <xsl:if test="erpAddressModel/companyName != ''">
   								<br/><xsl:value-of select="erpAddressModel/companyName"/><br/>
   							</xsl:if>
   							  <xsl:if test="erpAddressModel/companyName = ''">
   								<br/>
   							</xsl:if>
                            <xsl:value-of select="erpAddressModel/address1"/>,
   							<xsl:if test="erpAddressModel/serviceType/name = 'CORPORATE'">
   								Floor/Suite
   							</xsl:if>
   							<xsl:if test="erpAddressModel/serviceType/name != 'CORPORATE'">
   								Apt.
   							</xsl:if>
   							<xsl:value-of select="erpAddressModel/apartment"/> 
   						
   							<xsl:if test="erpAddressModel/address2 != ''">
   							<br/><xsl:value-of select="erpAddressModel/address2"/><br/>
   							</xsl:if>
   							<xsl:if test="erpAddressModel/address2 = ''">
   							<br/>
   							</xsl:if>   							  
							<xsl:value-of select="erpAddressModel/city"/>,
							<xsl:value-of select="erpAddressModel/state"/><xsl:text> </xsl:text>
							<xsl:value-of select="erpAddressModel/zipCode"/><br/><br/>
							
							<xsl:value-of select="phone"/><br/>
							<xsl:value-of select="alternatePhone"/><br/>

	<p>If you didn't make this change or have any related questions, please call us at 1-866-283-7374.</p>

	<p>Happy Shopping,<br/>
	<br/>
	FreshDirect<br/>
	Customer Service Group</p>

<xsl:call-template name="x_footer_v1"/>
</xsl:template>

</xsl:stylesheet>