<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:str="http://xsltsl.org/string"
                xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"
                extension-element-prefixes="doc str">

<xsl:include href='h_footer_v1.xsl'/>
<xsl:include href='string.xsl' />
<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:output method="html"/>

<xsl:template match="fdemail">
<xsl:if test="not(preview) or preview = 'false'"> 
<html>
<head>
	<base href="http://www.freshdirect.com/"/>
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	<style>
		.pageTitle {
			text-transform: uppercase;
			font-weight: normal;
			font-size: 11pt;
			font-family: Arial, sans-serif;			
		}
		
		#main, #main td {
			font-size: 8pt;
		}
		h2 {
			margin-bottom: 0;
		}
		h3 {
			margin-bottom: 0;
			color: #f93;
			text-transform: uppercase;
			font-weight: normal;
			font-size: 13pt;
			font-family: Arial, sans-serif;
			margin-top: 1em;
		}
		hr {
			border: 0;
			color: #ccc;
			background-color: #ccc;
			height: 1px
		}
		div.source {
			margin-bottom: 1em;
		}
		.ingredients {
			margin: 1em;
		}
		.retailers {
			margin-bottom: 1em;
		}
		.retailers .title {
			margin-bottom: 1em;
		}
		.retailers .retailer {
			margin: 1em 1em 1em 1em;
		}
		a.recipe {
			font-size: 10pt;
			font-weight: bold;
			display: block;
			margin: 1em 0;
		}
		.photo {
			float: left;
			margin: 1em;
		}
	</style>
</head>
<body bgcolor="#ffffff">

	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
			<td valign="bottom"><a href="http://www.freshdirect.com"><img src="http://www.freshdirect.com/media_stat/images/logos/fd_logo_md.gif" width="216" height="42" alt="FreshDirect" border="0"/></a></td>
			<td align="right" valign="bottom" class="pageTitle">RECIPE</td>
		</tr>
	</table>
	
	<hr/>
	<br/>
	
    <xsl:call-template name="mail_body" />
	
	<xsl:call-template name="h_footer_v1"/>

</body>
</html>
</xsl:if>

<xsl:if test="preview = 'true'">
	<xsl:call-template name="mail_body" />
</xsl:if>
</xsl:template>

<xsl:template name="mail_body">

	<div id="main">
    <table cellpadding="0" cellspacing="0" border="0" width="600">
        <xsl:if test="tellAFriendEmail = 'true'">
            <tr valign="top">
                <td width="95%">
                    Dear <xsl:value-of select="mailInfo/friendName" />,
                    <br /><br/>
                    <xsl:value-of select="mailInfo/emailText" />
                    <br /><br/>
                    - <xsl:value-of select="mailInfo/customerFirstName" /><br/><br/>
                </td>
            </tr>
        </xsl:if>
        <tr valign="top">
            <td width="95%">
                <!-- start the recipe name and author -->
                <h2><xsl:value-of select="/fdemail/recipeName"/></h2>
				<div class="source">
					From "<xsl:value-of select="/fdemail/sourceName"/>"
					<xsl:value-of select="concat(' ', /fdemail/authorNames)"/>
				</div>
                <!-- end the recipe name and author -->

                <!-- start the recipe description -->
                <xsl:choose>
                    <xsl:when test="count(/fdemail/recipeDescription) = 0">
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="/fdemail/recipeDescription"
                                      disable-output-escaping = "yes"
                        />	
                        <br/>
                    </xsl:otherwise>
                </xsl:choose>
                <!-- end the recipe description -->

                <!-- start the recipe ingredients -->
                <xsl:choose>
                    <xsl:when test="count(/fdemail/ingredientsMedia) = 0">
                    </xsl:when>
                    <xsl:otherwise>
                        <img src="http://www.freshdirect.com/media_stat/recipe/rec_hdr_ingredients.gif" width="92" height="10"/>
						<div class="ingredients">
							<xsl:value-of select="/fdemail/ingredientsMedia"
										  disable-output-escaping = "yes"
							/>
						</div>
                    </xsl:otherwise>
                </xsl:choose>
                <!-- end the recipe ingredients -->
            </td>
        </tr>
		<tr>
			<td>
				<a class="recipe" href="http://www.freshdirect.com/recipe.jsp?recipeId={recipeId}&amp;trk={//trackingCode}">Click here to buy!</a>
			</td>
		</tr>
        <tr>
            <td>
                <!-- start the recipe preparation -->
                <xsl:choose>
                    <xsl:when test="count(/fdemail/preparationMedia) = 0">
                    </xsl:when>
                    <xsl:otherwise>
						<br/>
                        <img src="http://www.freshdirect.com/media_stat/recipe/rec_hdr_preparation.gif" width="93" height="10"/>
                        <br/>
                        <img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/>
                        <br/>
                        <xsl:value-of select="/fdemail/preparationMedia"
                                      disable-output-escaping = "yes"
                        />
                        <br/>
                    </xsl:otherwise>
                </xsl:choose>
                <!-- end the recipe preparation -->

                <!-- start the copyright message -->
                <xsl:choose>
                    <xsl:when test="count(/fdemail/copyrightMedia) = 0">
                    </xsl:when>
                    <xsl:otherwise>
						<small>
                        <xsl:value-of select="/fdemail/copyrightMedia"
                                      disable-output-escaping = "yes"
                        />
						</small>
                    </xsl:otherwise>
                </xsl:choose>
                <!-- end the copyright message -->
            </td>
        </tr>
		<tr>
			<td>
				<a class="recipe" href="http://www.freshdirect.com/recipe.jsp?recipeId={recipeId}&amp;trk={//trackingCode}">Click here to view this recipe at FreshDirect.com</a>
			</td>
		</tr>
		</table>

    <xsl:if test="not(tellAFriendEmail) or (tellAFriendEmail = 'false')">
        <!-- only show recipe book promo if this is not a tell-a-friend mail -->
        <xsl:if test="count(/fdemail/sourceId) &gt; 0">
            <hr/>

            <table cellpadding="0" cellspacing="0" border="0" width="600">
            <tr>
                <td>
                    <xsl:if test="count(/fdemail/photoUrl) &gt; 0">
                        <img src="http://www.freshdirect.com/{photoUrl}" height="{photoHeight}" width="{photoWidth}" border="0" class="photo"/>
                    </xsl:if>

                    <!-- start source book retailers -->
                    <xsl:if test="count(/fdemail/retailer/retailer) &gt; 0">
                    
                        <div class="retailers">
                            <div class="title">
                                <h3>YOU MIGHT LIKE...</h3>
                                <b>"<xsl:value-of select="/fdemail/sourceName"/>"</b>
                                <br/>
                                <xsl:value-of select="/fdemail/authorNames"/>
                            </div>
                            <br/>
                            GET IT ONLINE:
                            <br/><br/>
                            <xsl:for-each select="//retailer/retailer">
                                <span class="retailer">
                                    <a href="http://www.freshdirect.com/redirect_bookretailer.jsp?recipeSourceId={//sourceId}&amp;bookRetailerId={id}&amp;trk={//trackingCode}"><img src="http://www.freshdirect.com/{logo}" alt="{name}" border="0" width="{logoWidth}" height="{logoHeight}"/></a>
                                </span>
                            </xsl:for-each>
                        </div>
                        
                    </xsl:if>
                    <!-- end source book retailers -->
                </td>
            </tr>
        </table>
        </xsl:if>
    </xsl:if>
		
	</div>

</xsl:template>

<xsl:template match="*">
	<xsl:copy>
		<xsl:for-each select="@*">
			<xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
		</xsl:for-each>
		<xsl:apply-templates />
	</xsl:copy>
</xsl:template>

</xsl:stylesheet>
