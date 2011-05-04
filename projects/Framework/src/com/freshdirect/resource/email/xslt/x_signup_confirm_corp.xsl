<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
 <p><b>Hello <xsl:value-of select="customer/firstName"/>,</b></p>
               
               <p>Thank you for signing up with FreshDirect At The Office.</p>
               
               <p>We've gathered a terrific selection of products at low prices to make shopping for the office a breeze. Our expanded product and service offerings specially tailored for corporate clients include:</p>
		
		<ul>
			<li>One-stop shopping for all your office needs (save time and streamline your work!) </li>
			<li>Everyday low prices on popular brands of snacks, beverages and pantry-stocking items (save money and look like a cost-cutting superstar!) </li>
			<li>The convenience of easy online ordering and next-day delivery </li>
			<li>Exceptional corporate customer service and our freshness guarantee </li>
	
		</ul>
              
	       <p>We have everything from milk, coffee and fresh fruit to cleaning supplies and bulk beverages! Enjoy fresher food, convenient deliveries and fuss-free customer care, all for up to 35% less than most vendors.</p>
               
 <p>See you at the office!<br/>
			   <br/>
			    Sincerely,<br/>
			   <br/>
				FreshDirect At The Office</p>
<xsl:call-template name="x_footer_v1"/>

</xsl:template>

</xsl:stylesheet>