<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
                xmlns:CMS="http://www.freshdirect.com/xsd/CMS"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fn="http://www.w3.org/2005/02/xpath-functions">

<!--
    XML Style Sheet Transformation to create SQL INSERT statements
    from an XML Content Type Definition, that will populate
    the DB content type definition tables, describing the very same
    content type definitions.

    The resulting SQL script will clear the tables first;
-->

<xsl:output method="text"/>

<xsl:template match="CMS:CMSDef">

<!--
<xsl:call-template name="drop_views"/>
<xsl:text>

</xsl:text>
-->

<xsl:call-template name="delete_contents"/>
<xsl:text>

</xsl:text>

<xsl:call-template name="insert_contenttypes"/>
<xsl:text>

</xsl:text>

<xsl:call-template name="insert_attributedefinitions"/>
<xsl:text>

</xsl:text>

<xsl:call-template name="insert_lookuptypes"/>
<xsl:text>

</xsl:text>

<xsl:call-template name="insert_lookups"/>
<xsl:text>

</xsl:text>

<xsl:call-template name="insert_enumdefinitions"/>
<xsl:text>

</xsl:text>


<xsl:call-template name="insert_relationshipdefinitions"/>
<xsl:text>

</xsl:text>

<xsl:call-template name="insert_destinationdefs"/>
<xsl:text>

</xsl:text>

<!--
<xsl:call-template name="create_views"/>
<xsl:text>

</xsl:text>
-->

</xsl:template>


<xsl:template name="delete_contents">
<xsl:text>
delete from cms.attributedefinition;
delete from cms.relationshipdestination;
delete from cms.relationshipdefinition;
delete from cms.lookup;
delete from cms.lookuptype;
delete from cms.contenttype;
</xsl:text>
</xsl:template>


<xsl:template name="drop_views">
<xsl:text>
drop materialized view log on cms.relationship;
drop materialized view log on cms.relationshipdefinition;
drop materialized view cms.all_nodes;
</xsl:text>
</xsl:template>


<xsl:template name="create_views">
<xsl:text>
create materialized view log on cms.relationship
 with primary key (def_name, def_contenttype);

create materialized view log on cms.relationshipdefinition
 with primary key (name, contenttype_id, navigable);

create materialized view cms.all_nodes
  build immediate 
  refresh fast on commit 
  as 
select * from cms.relationship r
where exists
 (select 'x' from cms.relationshipdefinition rd
  where rd.name = r.def_name
   and rd.navigable = 'T');
</xsl:text>
</xsl:template>


<xsl:template name="insert_attributedefinitions">
<xsl:for-each select="//CMS:AttributeDef">
    <!-- the insert header -->
    <xsl:text>INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES (</xsl:text>
    <!-- name -->
    <xsl:value-of select="concat(&quot;'&quot;, @name, &quot;',&quot;)"/>
    
    <!-- id, which is: contenttype_id.name -->
    <xsl:value-of select="concat(&quot;'&quot;, ../../@name, '.', @name, &quot;',&quot;)"/>
    
    <!-- contenttype_id -->
    <xsl:value-of select="concat(&quot;'&quot;, ../../@name, &quot;',&quot;)"/>

    <!-- attributetype_code -->
    <xsl:value-of select="concat(&quot;'&quot;, @type, &quot;',&quot;)"/>

    <!-- inheritable -->
    <xsl:choose>
        <xsl:when test="@inheritable='true'">
            <xsl:text>'T',</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>'F',</xsl:text>
        </xsl:otherwise>
    </xsl:choose>

    <!-- required -->
    <xsl:choose>
        <xsl:when test="@required='true'">
            <xsl:text>'T',</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>'F',</xsl:text>
        </xsl:otherwise>
    </xsl:choose>

    <!-- label -->
    <xsl:value-of select="concat(&quot;'&quot;, @label, &quot;',&quot;)"/>

    <!-- cardinality -->
    <xsl:choose>
        <xsl:when test="@cardinality='Many'">
            <xsl:text>'Many',</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>'One',</xsl:text>
        </xsl:otherwise>
    </xsl:choose>

    <!-- lookup code -->
    <xsl:text>NULL</xsl:text>
    
    <!-- the end of the statement, including a linefeed -->
    <xsl:text>);
</xsl:text>
</xsl:for-each>
</xsl:template>


<xsl:template name="insert_enumdefinitions">
<xsl:for-each select="//CMS:EnumDef">
    <!-- the insert header -->
    <xsl:text>INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES (</xsl:text>
    <!-- name -->
    <xsl:value-of select="concat(&quot;'&quot;, @name, &quot;',&quot;)"/>
    
    <!-- id, which is: contenttype_id.name -->
    <xsl:value-of select="concat(&quot;'&quot;, ../../@name, '.', @name, &quot;',&quot;)"/>
    
    <!-- contenttype_id -->
    <xsl:value-of select="concat(&quot;'&quot;, ../../@name, &quot;',&quot;)"/>

    <!-- attributetype_code -->
    <xsl:value-of select="concat(&quot;'&quot;, @type, &quot;',&quot;)"/>

    <!-- inheritable -->
    <xsl:choose>
        <xsl:when test="@inheritable='true'">
            <xsl:text>'T',</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>'F',</xsl:text>
        </xsl:otherwise>
    </xsl:choose>

    <!-- required -->
    <xsl:choose>
        <xsl:when test="@required='true'">
            <xsl:text>'T',</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>'F',</xsl:text>
        </xsl:otherwise>
    </xsl:choose>

    <!-- label -->
    <xsl:value-of select="concat(&quot;'&quot;, @label, &quot;',&quot;)"/>

    <!-- cardinality -->
    <xsl:choose>
        <xsl:when test="@cardinality='Many'">
            <xsl:text>'Many',</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>'One',</xsl:text>
        </xsl:otherwise>
    </xsl:choose>

    <!-- lookuptype_code, which is: contenttype_id.enum_name -->
    <xsl:value-of select="concat(&quot;'&quot;, ../../@name, '.', @name, &quot;'&quot;)"/>
    
    <!-- the end of the statement, including a linefeed -->
    <xsl:text>);
</xsl:text>
</xsl:for-each>
</xsl:template>



<xsl:template name="insert_contenttypes">
<xsl:for-each select="CMS:ContentTypeDef"><xsl:text>INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES (</xsl:text>
	<xsl:value-of select="concat(&quot;'&quot;, @name, &quot;',&quot;)"/>
	<xsl:value-of select="concat(&quot;'&quot;, @name, &quot;',&quot;)"/>
	<xsl:value-of select="concat(&quot;'Definition of type &quot;, @name, &quot;',&quot;)"/>
	<xsl:choose>
		 <xsl:when test="@inheritable='true'">
			 <xsl:text>'T'</xsl:text>
		 </xsl:when>
		 <xsl:otherwise>
			 <xsl:text>'F'</xsl:text>
		 </xsl:otherwise>
	 </xsl:choose>
	<xsl:text>);
</xsl:text>
</xsl:for-each>
</xsl:template>

<xsl:template name="insert_relationshipdefinitions">
<xsl:for-each select="//CMS:RelationshipDef">
    <!-- the insert header -->
    <xsl:text>INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES (</xsl:text>

    <!-- name -->
    <xsl:value-of select="concat(&quot;'&quot;, @name, &quot;',&quot;)"/>

    <!-- id, which is: contenttype_id.name -->
    <xsl:value-of select="concat(&quot;'&quot;, ../../@name, '.', @name, &quot;',&quot;)"/>

    <!-- contenttype_id -->
    <xsl:value-of select="concat(&quot;'&quot;, ../../@name, &quot;',&quot;)"/>

    <!-- inheritable -->
    <xsl:choose>
        <xsl:when test="@inheritable='true'">
            <xsl:text>'T',</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>'F',</xsl:text>
        </xsl:otherwise>
    </xsl:choose>

    <!-- required -->
    <xsl:choose>
        <xsl:when test="@required='true'">
            <xsl:text>'T',</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>'F',</xsl:text>
        </xsl:otherwise>
    </xsl:choose>

    <!-- navigable -->
    <xsl:choose>
        <xsl:when test="@navigable='true'">
            <xsl:text>'T',</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>'F',</xsl:text>
        </xsl:otherwise>
    </xsl:choose>

    <!-- navigable -->
    <xsl:choose>
        <xsl:when test="@readonly='true'">
            <xsl:text>'T',</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>'F',</xsl:text>
        </xsl:otherwise>
    </xsl:choose>

    <!-- label -->
    <xsl:value-of select="concat(&quot;'&quot;, @label, &quot;',&quot;)"/>

    <!-- cardinality -->
    <xsl:choose>
        <xsl:when test="@cardinality='Many'">
            <xsl:text>'Many'</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>'One'</xsl:text>
        </xsl:otherwise>
    </xsl:choose>

    <!-- the end of the statement, including a linefeed -->
    <xsl:text>);
</xsl:text>
</xsl:for-each>
</xsl:template>


<xsl:template name="insert_lookups">
<xsl:for-each select="//CMS:EnumDef">
    <xsl:for-each select="CMS:EnumValue">
        <!-- the insert header -->
        <xsl:text>INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES (</xsl:text>
        
        <!-- lookuptype_code, which is: contenttype_id.enum_name -->
        <xsl:value-of select="concat(&quot;'&quot;, ../../../@name, '.', ../@name, &quot;',&quot;)"/>

        <!-- value / code -->
        <xsl:value-of select="concat(&quot;'&quot;, @value, &quot;',&quot;)"/>

        <!-- label -->
        <xsl:value-of select="concat(&quot;'&quot;, @label, &quot;',&quot;)"/>

        <!-- description -->
        <xsl:value-of select="concat(&quot;'&quot;, @label, &quot;',&quot;)"/>
        
        <!-- ordinal -->
        <xsl:value-of select="position()"/>

        <!-- the end of the statement, including a linefeed -->
        <xsl:text>);
    </xsl:text>
    </xsl:for-each>
</xsl:for-each>
</xsl:template>


<xsl:template name="insert_lookuptypes">
<xsl:for-each select="//CMS:EnumDef">
    <!-- the insert header -->
    <xsl:text>INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) VALUES (</xsl:text>
    
    <!-- code, which is: contenttype_id.enum_name -->
    <xsl:value-of select="concat(&quot;'&quot;, ../../@name, '.', @name, &quot;',&quot;)"/>

    <!-- name, which is: contenttype_id.enum_name, same as above -->
    <xsl:value-of select="concat(&quot;'&quot;, ../../@name, '.', @name, &quot;',&quot;)"/>

    <!-- description, always NULL -->
    <xsl:text>NULL</xsl:text>

    <!-- the end of the statement, including a linefeed -->
    <xsl:text>);
</xsl:text>
</xsl:for-each>
</xsl:template>


<xsl:template name="insert_destinationdefs">
<xsl:for-each select="//CMS:DestinationDef">
    <!-- the insert header -->
    <xsl:text>INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES (</xsl:text>
    
    <!-- id, which is contenttype_id.relationshipname.contentType -->
    <xsl:value-of select="concat(&quot;'&quot;, ../../../@name, '.', ../@name, '.', @contentType, &quot;'&quot;)"/>

    <!-- relationshiddefinition id, which is:
         contenttype_id.relationshipname -->
    <xsl:value-of select="concat(',',&quot;'&quot;, ../../../@name, '.', ../@name, &quot;',&quot;)"/>

    <!-- target contentType -->
    <xsl:value-of select="concat(&quot;'&quot;, @contentType, &quot;',&quot;)"/>

    <!-- REVERSE_ATTRIBUTE_NAME -->
    <xsl:choose>
        <xsl:when test="@reverseAttributeName">
        	<xsl:value-of select="concat(&quot;'&quot;, @reverseAttributeName, &quot;',&quot;)"/>
        </xsl:when>
        <xsl:otherwise>
		    <xsl:text>NULL,</xsl:text>
        </xsl:otherwise>
    </xsl:choose>

    <!-- REVERSE_ATTRIBUTE_LABEL -->
    <xsl:choose>
        <xsl:when test="@reverseAttributeLabel">
        	<xsl:value-of select="concat(&quot;'&quot;, @reverseAttributeLabel, &quot;'&quot;)"/>
        </xsl:when>
        <xsl:otherwise>
		    <xsl:text>NULL</xsl:text>
        </xsl:otherwise>
    </xsl:choose>


    <!-- the end of the statement, including a linefeed -->
    <xsl:text>);
</xsl:text>
</xsl:for-each>
</xsl:template>

</xsl:stylesheet>

