<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
<html>
<head>
<style>
body {
	background: white;
}
td {
	font-family: Verdana, sans-serif;
	font-size: 8pt;
}
.fold {
	
}
.open {
	background: #ccddcc;
	cursor: pointer; cursor: hand;
}
.closed {
	background: #ddcccc;
	cursor: pointer; cursor: hand;
}
</style>
<script>
function fold(src) {
	target = src;
	while (target &amp;&amp; target.className!='fold') {
		target = target.nextSibling ? target.nextSibling : target.parentNode;
	}
	if (target) {
		d = target.style.display=="none";
		target.style.display = d ? "" : "none";
		src.className = d ? "open" : "closed";
	}
}
</script>
</head>
<body bgcolor="#ffffff">
	<xsl:apply-templates />
</body>
</html>
</xsl:template>


<xsl:template match="*[count(*) &gt; 0]">
<table width="100%" border="0" cellspacing="1" cellpadding="0" bgcolor="#ffffff">
	<tr bgcolor="#cccccc" onclick="fold(this);" class="open">
		<td>
			<b><a  name="{@id}"><xsl:value-of select="name()"/></a></b>
		</td>
	</tr>
	<tr class="fold">
		<td>
			<table width="100%" cellspacing="1" cellpadding="0" border="0">
				<xsl:for-each select="*">
					<tr bgcolor="#f0f0f0">
						<td width="100" bgcolor="#dddddd" valign="top">
							<xsl:value-of select="name()"/>
						</td>
						<td>
							<xsl:apply-templates select="."/>
						</td>
					</tr>
				</xsl:for-each>
			</table>
		</td>
	</tr>
</table>
</xsl:template>

<xsl:template match="*[@refid]">
	<a href="#{@refid}">Reference</a>
</xsl:template>

</xsl:stylesheet>