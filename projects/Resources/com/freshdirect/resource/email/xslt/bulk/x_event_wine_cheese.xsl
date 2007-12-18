<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
LEARN ABOUT WINE &amp; CHEESE FROM THE EXPERTS 
An Evening with Josh Wesson and Max McCalman! 

We're proud to bring together the Artisanal Cheese Center and Best Cellars to promote a night of exquisite cheese and wine tasting education hosted by some of the country's leading experts. 

This special tasting will be the first of several sponsored by FreshDirect, and will feature a standout offering from each of Best Cellars eight taste categories (FIZZY, FRESH, SOFT, LUSCIOUS, JUICY, SMOOTH, BIG, and SWEET) accompanied by an equal number of fine cheeses from Artisanal's unique selection. Moving beyond the limitations imposed by predetermined pairings, the class will explore a broader and simpler taste-based approach to the art of matching wine with cheese. 

The seminar instructors will include Josh Wesson, award winning sommelier and co-owner of Best Cellars; 3-star Chef Terrance Brennan, of the Artisanal Cheese Center, and restaurants Artisanal and Picholine; and Max McCalman, Ma&#238;tre Fromager and author of The Cheese Plate, the definitive book on wine and cheese pairing.

Event: An Evening with Josh Wesson and Max McCalman 
When: Tuesday, May 25, 2004, Doors open at 7:00pm 
Where: Artisanal Cheese Center, 500 West 37th St. (10th Ave), 2nd Floor, NYC 
Price: Tickets cost $75.00 

SEATING IS LIMITED! 
TO ORDER TICKETS TO THIS SPECIAL EVENT CONTACT THE ARTISANAL CHEESE CENTER 
Phone: 212-239-1200 or online at: 
http://www.artisanalcheese.com/artisanal/education/classes_tastings.cfm 

<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
