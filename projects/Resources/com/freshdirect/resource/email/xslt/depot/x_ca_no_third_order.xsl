<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>

Dear <xsl:value-of select="customer/firstName"/>,
			
Now that you've tried FreshDirect, I'd like to find out personally about your experience.

I created FreshDirect to give New Yorker's the best food at the best possible price. I also believe in treating customers like my own family, so I want to know the good and the bad. Are you pleased with the food quality? Is our selection satisfactory? How were our prices?  Is there anything we can do better? Drop me an e-mail and let me know your honest thoughts.

Direct feedback is the most valuable thing I can think of to ensure that your FreshDirect food shopping experience is the best you've ever had.

Log onto www.freshdirect.com/ca  Access Code: CA321

You can also contact us via email at caservice@freshdirect.com

I look forward to hearing from you.

Joe Fedele
Creator and CEO
FreshDirect
It's all about the food.

<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
