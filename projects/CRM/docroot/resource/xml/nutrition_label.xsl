<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
	<xsl:output method="html" indent="yes"/>

    <xsl:template match="/nutrition">
		
        <table BORDER="0" CELLSPACING="0" CELLPADDING="1" WIDTH="220">
          <tr VALIGN="top">
            <td CLASS="title18" align="center">Nutrition Facts</td>
          </tr>
          <tr>
            <td CLASS="text9">Serv. Size&nbsp;<xsl:apply-templates select="nutrition[name='Serving Size']"/> (<xsl:apply-templates select="nutrition[name='Serving Weight']"/>)
            </td>
          </tr>
          <tr>
            <td CLASS="text9">
                Servings&nbsp;<xsl:apply-templates select="nutrition[name='Servings Per Container']"/>
            </td>
          </tr>
          <tr>
            <td> <img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="220" HEIGHT="6"/></td>
          </tr>
        </table>

        <table BORDER="0" CELLSPACING="0" CELLPADDING="1" WIDTH="220">
          <tr>
            <td COLSPAN="2" CLASS="text9">
                <b>Amount Per Serving</b> <br/>
                 <img SRC="media_stat/images/layout/330000.gif" ALT="" WIDTH="220" HEIGHT="1"/>
            </td>
          </tr>
          <tr>
            <td CLASS="text9"><b>Calories</b><xsl:text> </xsl:text>
            <xsl:choose>
                <xsl:when test="nutrition[name='Calories']">
                    <xsl:apply-templates select="nutrition[name='Calories']"/>
                </xsl:when>
                <xsl:otherwise>0</xsl:otherwise>
            </xsl:choose>
            </td>
            <td ALIGN="right" CLASS="text9">Calories From Fat
            <xsl:choose>
                <xsl:when test="nutrition[name='Calories from Fat']">
                    <xsl:apply-templates select="nutrition[name='Calories from Fat']"/>
                </xsl:when>
                <xsl:otherwise>0</xsl:otherwise>
            </xsl:choose>
            </td>
          </tr>
          <tr>
            <td COLSPAN="2"> <img SRC="media_stat/images/layout/330000.gif" ALT="" WIDTH="220" HEIGHT="4"/></td>
          </tr>
        </table>

        <table BORDER="0" CELLSPACING="0" CELLPADDING="1" WIDTH="220">
          <tr><td COLSPAN="2" ALIGN="right" CLASS="text8" WIDTH="220">% Daily Value *</td></tr>
          <tr><td COLSPAN="2"><img SRC="media_stat/images/layout/330000.gif" ALT="" WIDTH="220" HEIGHT="1"/></td></tr>
          <tr>
            <td CLASS="text9"><b>Total Fat</b><xsl:text> </xsl:text>
            <xsl:choose>
                <xsl:when test="nutrition[name='Total Fat quantity']">
                    <xsl:apply-templates select="nutrition[name='Total Fat quantity']"/>
                </xsl:when>
                <xsl:otherwise>0g</xsl:otherwise>
            </xsl:choose>
            </td>
            <td ALIGN="right" CLASS="text9"><b>
            <xsl:choose>
                <xsl:when test="nutrition[name='Total Fat value']">
                    <xsl:apply-templates select="nutrition[name='Total Fat value']"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="not(nutrition[name='Total Fat quantity'])">
                            <xsl:text>0%</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="format-number(number(nutrition[name='Total Fat quantity']/value) div 65, '##0%')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
            </b></td>
          </tr>
          <tr>
            <td COLSPAN="2" align="right"><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="210" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><img SRC="/media_stat/images/layout/clear.gif" ALT="" WIDTH="10" HEIGHT="9"/>Saturated Fat 
            <xsl:choose>
                <xsl:when test="nutrition[name='Saturated Fat quantity']">
                    <xsl:apply-templates select="nutrition[name='Saturated Fat quantity']"/>
                </xsl:when>
                <xsl:otherwise>0g</xsl:otherwise>
            </xsl:choose>
            </td>	
            <td ALIGN="right" CLASS="text9"><b>
            <xsl:choose>
                <xsl:when test="nutrition[name='Saturated Fat value']">
                    <xsl:apply-templates select="nutrition[name='Saturated Fat value']"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="not(nutrition[name='Saturated Fat quantity'])">
                            <xsl:text>0%</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="format-number(number(nutrition[name='Saturated Fat quantity']/value) div 20, '##0%')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
            </b></td>
          </tr>
          <xsl:if test="nutrition[name='Stearic Acid quantity']">
          <tr>
            <td COLSPAN="2" align="right"><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="210" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><img SRC="/media_stat/images/layout/clear.gif" ALT="" WIDTH="10" HEIGHT="9"/>Stearic Acid 
            <xsl:apply-templates select="nutrition[name='Stearic Acid quantity']"/>
            </td>
            <td> </td>
          </tr>
          </xsl:if>
          <xsl:if test="nutrition[name='Polyunsaturated Fat quantity']">
          <tr>
            <td COLSPAN="2" align="right"><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="210" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><img SRC="/media_stat/images/layout/clear.gif" ALT="" WIDTH="10" HEIGHT="9"/>Polyunsaturated Fat 
            <xsl:apply-templates select="nutrition[name='Polyunsaturated Fat quantity']"/>
            </td>
            <td> </td>
          </tr>
          </xsl:if>
          <xsl:if test="nutrition[name='Monounsaturated Fat quantity']">
          <tr>
            <td COLSPAN="2" align="right"><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="210" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><img SRC="/media_stat/images/layout/clear.gif" ALT="" WIDTH="10" HEIGHT="9"/>Monounsaturated Fat 
            <xsl:apply-templates select="nutrition[name='Monounsaturated Fat quantity']"/>
            </td>
            <td> </td>
          </tr>
          </xsl:if>
          <tr>
            <td COLSPAN="2"><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="220" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><b>Cholesterol</b><xsl:text> </xsl:text>
            <xsl:choose>
                <xsl:when test="nutrition[name='Cholesterol quantity']">
                    <xsl:apply-templates select="nutrition[name='Cholesterol quantity']"/>
                </xsl:when>
                <xsl:otherwise>0mg</xsl:otherwise>
            </xsl:choose>
            </td>
            <td ALIGN="right" CLASS="text9"><b>
            <xsl:choose>
                <xsl:when test="nutrition[name='Cholesterol value']">
                    <xsl:apply-templates select="nutrition[name='Cholesterol value']"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="not(nutrition[name='Cholesterol quantity'])">
                            <xsl:text>0%</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="format-number(number(nutrition[name='Cholesterol quantity']/value) div 300, '##0%')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
            </b></td>
          </tr>
          <tr>
            <td COLSPAN="2"><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="220" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><b>Sodium</b><xsl:text> </xsl:text>
            <xsl:choose>
                <xsl:when test="nutrition[name='Sodium quantity']">
                    <xsl:apply-templates select="nutrition[name='Sodium quantity']"/>
                </xsl:when>
                <xsl:otherwise>0mg</xsl:otherwise>
            </xsl:choose>
            </td>
            <td ALIGN="right" CLASS="text9"><b>
            <xsl:choose>
                <xsl:when test="nutrition[name='Sodium value']">
                    <xsl:apply-templates select="nutrition[name='Sodium value']"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="not(nutrition[name='Sodium quantity'])">
                            <xsl:text>0%</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="format-number(number(nutrition[name='Sodium quantity']/value) div 2400, '##0%')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
            </b></td>
          </tr>
          <xsl:if test="nutrition[name='Potassium quantity']">
          <tr>
            <td COLSPAN="2"><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="220" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><b>Potassium</b><xsl:text> </xsl:text>
            <xsl:apply-templates select="nutrition[name='Potassium quantity']"/>
            </td>
            <td ALIGN="right" CLASS="text9"><b>
            <xsl:choose>
                <xsl:when test="nutrition[name='Potassium value']">
                    <xsl:apply-templates select="nutrition[name='Potassium value']"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="format-number(number(nutrition[name='Potassium quantity']/value) div 3500, '##0%')"/>
                </xsl:otherwise>
            </xsl:choose>
            </b></td>
          </tr>
          </xsl:if>
          <tr>
            <td COLSPAN="2"><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="220" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><b>Total Carbohydrate</b><xsl:text> </xsl:text>
            <xsl:choose>
                <xsl:when test="nutrition[name='Total Carbohydrate quantity']">
                    <xsl:apply-templates select="nutrition[name='Total Carbohydrate quantity']"/>
                </xsl:when>
                <xsl:otherwise>0g</xsl:otherwise>
            </xsl:choose>
            </td>
            <td ALIGN="right" CLASS="text9"><b>
            <xsl:choose>
                <xsl:when test="nutrition[name='Total Carbohydrate value']">
                    <xsl:apply-templates select="nutrition[name='Total Carbohydrate value']"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="not(nutrition[name='Total Carbohydrate quantity'])">
                            <xsl:text>0%</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="format-number(number(nutrition[name='Total Carbohydrate quantity']/value) div 300, '##0%')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
            </b></td>
          </tr>
          <tr>
            <td COLSPAN="2"><img SRC="/media_stat/images/layout/330000.gif" align="right" ALT="" WIDTH="210" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><img SRC="/media_stat/images/layout/clear.gif" ALT="" WIDTH="10" HEIGHT="9"/>Dietary Fiber 
            <xsl:choose>
                <xsl:when test="nutrition[name='Dietary Fiber quantity']">
                    <xsl:apply-templates select="nutrition[name='Dietary Fiber quantity']"/>
                </xsl:when>
                <xsl:otherwise>0g</xsl:otherwise>
            </xsl:choose>
            </td>
            <td ALIGN="right" CLASS="text9"><b>
            <xsl:choose>
                <xsl:when test="nutrition[name='Dietary Fiber value']">
                    <xsl:apply-templates select="nutrition[name='Dietary Fiber value']"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="not(nutrition[name='Dietary Fiber quantity'])">
                            <xsl:text>0%</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="format-number(number(nutrition[name='Dietary Fiber quantity']/value) div 25, '##0%')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
            </b></td>
          </tr>
          <xsl:if test="nutrition[name='Soluble Fiber quantity']">
          <tr>
            <td COLSPAN="2"><img SRC="/media_stat/images/layout/330000.gif" align="right" ALT="" WIDTH="200" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><img SRC="/media_stat/images/layout/clear.gif" ALT="" WIDTH="20" HEIGHT="9"/>Soluble Fiber 
                <xsl:apply-templates select="nutrition[name='Soluble Fiber quantity']"/>
            </td>
            <td> </td>
          </tr>
          </xsl:if>
          <xsl:if test="nutrition[name='Insoluble Fiber quantity']">
          <tr>
            <td COLSPAN="2"><img SRC="/media_stat/images/layout/330000.gif" align="right" ALT="" WIDTH="200" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><img SRC="/media_stat/images/layout/clear.gif" ALT="" WIDTH="20" HEIGHT="9"/>Insoluble Fiber 
                <xsl:apply-templates select="nutrition[name='Insoluble Fiber quantity']"/>
            </td>
            <td> </td>
          </tr>
          </xsl:if>
          <tr>
            <td COLSPAN="2"><img SRC="/media_stat/images/layout/330000.gif" align="right" ALT="" WIDTH="210" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><img SRC="/media_stat/images/layout/clear.gif" ALT="" WIDTH="10" HEIGHT="9"/>Sugars
            <xsl:choose>
                <xsl:when test="nutrition[name='Sugars quantity']">
                    <xsl:apply-templates select="nutrition[name='Sugars quantity']"/>
                </xsl:when>
                <xsl:otherwise>0g</xsl:otherwise>
            </xsl:choose>
            </td>
            <td ALIGN="right" CLASS="text9"> </td>
          </tr>
          <xsl:if test="nutrition[name='Sugar Alcohol quantity']">
          <tr>
            <td COLSPAN="2"><img SRC="/media_stat/images/layout/330000.gif" align="right" ALT="" WIDTH="210" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><img SRC="/media_stat/images/layout/clear.gif" ALT="" WIDTH="10" HEIGHT="9"/>Sugar Alcohol
                <xsl:apply-templates select="nutrition[name='Sugar Alcohol quantity']"/>
            </td>
            <td> </td>
          </tr>
          </xsl:if>
          <xsl:if test="nutrition[name='Other Carbohydrates quantity']">
          <tr>
            <td COLSPAN="2"><img SRC="/media_stat/images/layout/330000.gif" align="right" ALT="" WIDTH="210" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><img SRC="/media_stat/images/layout/clear.gif" ALT="" WIDTH="10" HEIGHT="9"/>Other Carbohydrates
                <xsl:apply-templates select="nutrition[name='Other Carbohydrates quantity']"/>
            </td>
            <td> </td>
          </tr>
          </xsl:if>
          <tr>
            <td COLSPAN="2"><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="220" HEIGHT="1"/></td>
          </tr>
          <tr>
            <td CLASS="text9"><b>Protein</b><xsl:text> </xsl:text>
            <xsl:choose>
                <xsl:when test="nutrition[name='Protein quantity']">
                    <xsl:apply-templates select="nutrition[name='Protein quantity']"/>
                </xsl:when>
                <xsl:otherwise>0g</xsl:otherwise>
            </xsl:choose>
            </td>
            <td ALIGN="right" CLASS="text9"> </td>
          </tr>
          <tr>
            <td COLSPAN="2"><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="220" HEIGHT="6"/></td>
          </tr>
        </table>
        
        <table BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="220">
          <tr><td valign="top"><table BORDER="0" CELLSPACING="0" CELLPADDING="1" WIDTH="110">
              <tr><td CLASS="text9" WIDTH="110">Vitamin A
              <xsl:choose>
                <xsl:when test="nutrition[name='Vitamin A']">
                    <xsl:apply-templates select="nutrition[name='Vitamin A']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates>
                </xsl:when>
                <xsl:otherwise>0%</xsl:otherwise>
              </xsl:choose>
              </td></tr>
              <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
              <tr><td CLASS="text9" WIDTH="110">Calcium
              <xsl:choose>
                <xsl:when test="nutrition[name='Calcium']">
                    <xsl:apply-templates select="nutrition[name='Calcium']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates>
                </xsl:when>
                <xsl:otherwise>0%</xsl:otherwise>
              </xsl:choose>
              </td></tr>
              <xsl:if test="nutrition[name='Vitamin D']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Vitamin D <xsl:apply-templates select="nutrition[name='Vitamin D']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Vitamin K']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Vitamin K <xsl:apply-templates select="nutrition[name='Vitamin K']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Riboflavin']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Riboflavin <xsl:apply-templates select="nutrition[name='Riboflavin']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Vitamin B6']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Vitamin B6 <xsl:apply-templates select="nutrition[name='Vitamin B6']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Vitamin B12']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Vitamin B12 <xsl:apply-templates select="nutrition[name='Vitamin B12']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Pantothenic Acid']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Pantothenic Acid <xsl:apply-templates select="nutrition[name='Pantothenic Acid']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Iodine']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Iodine <xsl:apply-templates select="nutrition[name='Iodine']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Zinc']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Zinc <xsl:apply-templates select="nutrition[name='Zinc']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Copper']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Copper <xsl:apply-templates select="nutrition[name='Copper']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Chromium']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Chromium <xsl:apply-templates select="nutrition[name='Chromium']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Chloride']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Chloride <xsl:apply-templates select="nutrition[name='Chloride']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
            </table></td><td valign="top"><table BORDER="0" CELLSPACING="0" CELLPADDING="1" WIDTH="110">
              <tr><td CLASS="text9" WIDTH="110">Vitamin C
               <xsl:choose>
                <xsl:when test="nutrition[name='Vitamin C']">
                    <xsl:apply-templates select="nutrition[name='Vitamin C']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates>
                </xsl:when>
                <xsl:otherwise>0%</xsl:otherwise>
              </xsl:choose>
              </td></tr>
              <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
              <tr><td CLASS="text9" WIDTH="110">Iron
              <xsl:choose>
                <xsl:when test="nutrition[name='Iron']">
                    <xsl:apply-templates select="nutrition[name='Iron']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates>
                </xsl:when>
                <xsl:otherwise>0%</xsl:otherwise>
              </xsl:choose>
              </td></tr>
              <xsl:if test="nutrition[name='Vitamin E']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Vitamin E <xsl:apply-templates select="nutrition[name='Vitamin E']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Thiamin']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Thiamin <xsl:apply-templates select="nutrition[name='Thiamin']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Niacin']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Niacin <xsl:apply-templates select="nutrition[name='Niacin']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Folate']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Folate <xsl:apply-templates select="nutrition[name='Folate']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Biotin']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Biotin <xsl:apply-templates select="nutrition[name='Biotin']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Phosphorous']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Phosphorous <xsl:apply-templates select="nutrition[name='Phosphorous']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Magnesium']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Magnesium <xsl:apply-templates select="nutrition[name='Magnesium']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Selenium']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Selenium <xsl:apply-templates select="nutrition[name='Selenium']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Manganese']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Manganese <xsl:apply-templates select="nutrition[name='Manganese']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
              <xsl:if test="nutrition[name='Molybdenum']">
                  <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="110" HEIGHT="1"/></td></tr>
                  <tr><td CLASS="text9" WIDTH="110">Molybdenum <xsl:apply-templates select="nutrition[name='Molybdenum']">
                        <xsl:with-param name="vitamin">true</xsl:with-param>
                    </xsl:apply-templates></td></tr>
              </xsl:if>
            </table></td></tr>
        </table>
        <table BORDER="0" CELLSPACING="0" CELLPADDING="1" WIDTH="220">
          <tr>
            <img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="220" HEIGHT="1"/>
          </tr>
          <tr>
            <td CLASS="text9">** Contains less than 2 percent of the Daily Value of these nutrients.</td>
          </tr>
          <tr><td><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="220" HEIGHT="1"/></td></tr>
          <tr>
            <td CLASS="text9">* Percent Daily Values are based on a 2,000 calorie diet.  Your daily values may be
            higher or lower depending on your calorie needs:</td>
          </tr>
        </table>
        <table border="0" cellspacing="0" cellpadding="1" width="220">
        <tr>
            <td CLASS="text9"> </td>
            <td CLASS="text9">Calories:</td>
            <td CLASS="text9">2,000</td>
            <td CLASS="text9">2,500</td>
        </tr>
        <tr><td colspan="4"><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="220" HEIGHT="1"/></td></tr>
        <tr>
            <td CLASS="text9">Total&nbsp;Fat</td>
            <td CLASS="text9">Less&nbsp;than</td>
            <td CLASS="text9">65g</td>
            <td CLASS="text9">80g</td>
        </tr>
        <tr>
            <td CLASS="text9">Saturated&nbsp;Fat</td>
            <td CLASS="text9">Less&nbsp;than</td>
            <td CLASS="text9">20g</td>
            <td CLASS="text9">25g</td>
        </tr>
        <tr>
            <td CLASS="text9">Cholesterol</td>
            <td CLASS="text9">Less&nbsp;than</td>
            <td CLASS="text9">300mg</td>
            <td CLASS="text9">300mg</td>
        </tr>
        <tr>
            <td CLASS="text9">Sodium</td>
            <td CLASS="text9">Less&nbsp;than</td>
            <td CLASS="text9">2,400mg</td>
            <td CLASS="text9">2,400mg</td>
        </tr>
        <tr>
            <td CLASS="text9" colspan="2">Total&nbsp;Carbohydrate</td>
            <td CLASS="text9">300g</td>
            <td CLASS="text9">375g</td>
        </tr>
        <tr>
            <td CLASS="text9" colspan="2">Dietary&nbsp;Fiber</td>
            <td CLASS="text9">25g</td>
            <td CLASS="text9">30g</td>
        </tr>
        <tr><td colspan="4"><img SRC="/media_stat/images/layout/330000.gif" ALT="" WIDTH="220" HEIGHT="1"/></td></tr>
        <tr><td CLASS="text9" colspan="4">Calories per gram:</td></tr>
        <tr><td CLASS="text9" colspan="4" align="center">Fat 9 &dot; Carbohydrate 4 &dot; Protein 4</td></tr>
        </table>

    </xsl:template>
    
    <xsl:template match="nutrition">
        <xsl:param name="vitamin"/>
        <xsl:choose>
            <xsl:when test="$vitamin">
                <xsl:choose>
                    <xsl:when test="value &lt; 2">
                        **
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="format-number(value, '###0.#')"/>
                        <xsl:value-of select="unitOfMeasure"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="value &lt; 0">
                        less than <xsl:value-of select="format-number((-1 * value), '###0.#')"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="format-number(value, '###0.#')"/>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:value-of select="unitOfMeasure"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="nutrition[name='Servings Per Container']">
        <xsl:value-of select="unitOfMeasure"/>
        <xsl:if test="not(number(value)=0)">
            <xsl:text> </xsl:text>
            <xsl:value-of select="format-number(value, '###0.##')"/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="nutrition[name='Serving Size']">
        <xsl:choose>
            <xsl:when test="format-number(value, '#0.00')='0.80'">
                <xsl:text>4/5</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.75'">
                <xsl:text>3/4</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.67'">
                <xsl:text>2/3</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.60'">
                <xsl:text>3/5</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.50'">
                <xsl:text>1/2</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.40'">
                <xsl:text>2/5</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.38'">
                <xsl:text>3/8</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.33'">
                <xsl:text>1/3</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.25'">
                <xsl:text>1/4</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.20'">
                <xsl:text>1/5</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.17'">
                <xsl:text>1/6</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.14'">
                <xsl:text>1/7</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.13'">
                <xsl:text>1/8</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.11'">
                <xsl:text>1/9</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.10'">
                <xsl:text>1/10</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.09'">
                <xsl:text>1/11</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.08'">
                <xsl:text>1/12</xsl:text>
            </xsl:when>
            <xsl:when test="format-number(value, '#0.00')='0.07'">
                <xsl:text>1/15</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="format-number(value, '###0.##')"/>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text> </xsl:text><xsl:value-of select="unitOfMeasure"/>
    </xsl:template>

</xsl:stylesheet>
