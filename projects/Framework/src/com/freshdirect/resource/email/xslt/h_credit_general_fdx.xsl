<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
    <!ENTITY mdash  "&#8212;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='h_header_fdx.xsl'/>
	<xsl:include href='h_footer_fdx.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html>
<head>
	<title>We've issued your credits</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</head>
	<body bgcolor="#eeeeee" text="#333333">
		<xsl:call-template name="mail_body" />
	</body>
</html>
</xsl:template>
		
<xsl:template name="mail_body">
	
	<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
		<div style="margin: 0 0 15px 0;"><xsl:call-template name="h_header_fdx" /></div>
		
		<div style="color: #732484; font-size: 36px; font-weight: bold; margin: 15px 0;">Hey <xsl:value-of select="customer/firstName"/>,</div>
		
		<xsl:if test="string-length(complaint/customerEmail/customMessage) &gt; 0">
			<div style="margin: 15px 0; font-size: 16px;">
				<xsl:value-of disable-output-escaping="yes" select='complaint/customerEmail/customMessageHTML'/>
			</div>
		</xsl:if>
		
		<div style="margin: 15px 0; font-size: 16px;">
			We heard something wasn't right with your order #<xsl:value-of select="saleId"/>, so we've issued you the following credits:
		</div>
		
		<div style="margin-left: 20px; font-size: 16px;">
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
							Department in store credit<br />
						</xsl:if>
					</xsl:if>
				</xsl:for-each>
				$<xsl:value-of select='format-number(complaint/storeCreditAmount, "###,##0.00", "USD")'/> total store credit<br />
				<br />
				<xsl:if test="complaint/cashBackAmount &gt; 0">
					plus...<br />
					<br />
				</xsl:if>
			</xsl:if>
			<xsl:if test="complaint/cashBackAmount &gt; 0">
				<xsl:for-each select="complaint/complaintLinesAggregated/complaintLinesAggregated">
					<xsl:if test="not(number(amount)=0)">
						<xsl:if test="method = 'CSH'">
							$<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/><xsl:text>  </xsl:text> from the 
							<xsl:if test="departmentCode = 'GDW'">
								<xsl:text>Customer Service</xsl:text> 
							</xsl:if>
							<xsl:if test="not(departmentCode='GDW')">
								<xsl:value-of select="departmentName"/>
							</xsl:if>
							Department charged back to your credit card<br />
						</xsl:if>
					</xsl:if>
				</xsl:for-each>
				$<xsl:value-of select='format-number(complaint/cashBackAmount, "###,##0.00", "USD")'/> total charged back to your credit card<br />
				<br />
			</xsl:if>
			
			$<xsl:value-of select='format-number(complaint/amount, "###,##0.00", "USD")'/> total credit
		</div>
		
		<div style="margin: 15px 0; font-size: 16px;">
			Hang tight, it takes about 2 business days for your store credit to become available. Once it's loaded to your account, we'll automatically subtract the store credit from your next order total at the last stage of checkout. Hooray!
		</div>
		
		<div style="margin: 15px 0; font-size: 16px;">
			Thanks again for giving us a heads up about the issue &mdash; we'll always try to make things right. If you need further assistance, please feel free to chat with one of our SideKicks.
		</div>
		
		<div style="margin: 15px 0; font-size: 16px;">
			We heart you. Mean it.<br />
			xx,<br />			
			Your FoodKick SideKicks
		</div>
		
	</div>
	<xsl:call-template name="h_footer_fdx"/>
	
	
	<table cellpadding="0" cellspacing="0">
		<tr>
			<td width="20"><img src="/images/clear.gif" width="20" height="1" border="0" alt="" /></td>
			<td>
				
				<table cellpadding="0" cellspacing="0" width="90%">
					<tr>
						<td>
							
							<br />
							<xsl:if test="complaint/storeCreditAmount &gt; 0">Please note it takes approximately two business days for your store credit to become available. Once it is available we'll automatically subtract these store credits from your next order total at the last stage of checkout.</xsl:if>
							<xsl:if test="complaint/cashBackAmount &gt; 0">Please note that this credit should reflect on your credit card within five business days.</xsl:if>
							<br />
							<br />
							I'd like to thank you for letting us know what occurred and giving us the opportunity to help solve your problem. If, for any reason you're dissatisfied with how we resolved your problem, or need further assistance, please feel free to e-mail us directly by clicking on the contact us link on the website or call us at 212-796-8002.<br />
							<br />
							Your satisfaction is our number one priority!<br />
							<br />
							Sincerely,<br />
							<br />
	                          <xsl:if test="string-length(complaint/customerEmail/signature) &gt; 0">
	                            <xsl:value-of select="complaint/customerEmail/signature"/><br />
	                          </xsl:if>
							Your FreshDirect Customer Service Team<br />
							Keep in mind that we are here for you:<br />
							<br />
							<xsl:for-each select="contactHours/contactHours">
								<xsl:value-of select="daysDisplay"/><xsl:value-of select="hoursDisplay"/><br />
							</xsl:for-each>
	
							<p><xsl:call-template name="h_footer_fdx"/></p>
	
						</td>
					</tr>
				</table>
			</td>
			<td width="20"><img src="/images/clear.gif" width="20" height="1" border="0" alt="" /></td>
		</tr>
	</table>
</xsl:template>
</xsl:stylesheet>