<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.content.util.*' %>

<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%!
	String getParentId(ContentNodeModel model) {
		CategoryModel primaryHome = model instanceof ProductModel ? ((ProductModel)model).getPrimaryHome() : null;
        return primaryHome == null ? model.getParentNode().getContentName():primaryHome.getContentName();
	}

%>
<%
List delicateMild       = new ArrayList();
List delicateModerate   = new ArrayList();
List delicateFull       = new ArrayList();
List flakyMild          = new ArrayList();
List flakyModerate      = new ArrayList();
List flakyFull          = new ArrayList();
List meatyMild          = new ArrayList();
List meatyModerate      = new ArrayList();
List meatyFull          = new ArrayList();

String catId = "sea_finder";
String selected = request.getParameter("sel");
ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(catId);

List strategy = new ArrayList();
strategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, SortStrategyElement.SORTNAME_GLANCE, false));
%>

<STYLE>
<!--
TD.bodycells{padding: 3px}
TD.textureLabels{padding-left:25px; padding-top:4px; padding-right:4px}

.selected{font-weight: bold}
.notSelected(font-weight: normal}
!-->
</STYLE>

<script language='javascript'>
<!--
    function goThere(url){
        window.opener.location = url;
        window.opener.focus();
    }
-->
</script>
<tmpl:insert template='/common/template/pop_lg.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Fish Finder</tmpl:put>
            <tmpl:put name='content' direct='true'>
                <fd:ItemGrabber category='<%= currentFolder %>' id='seafoodCollection'  
                    depth='10' ignoreShowChildren='true'  returnHiddenFolders='false'>
                
                <fd:ItemSorter nodes='<%=(List)seafoodCollection%>' strategy='<%=strategy%>'/>
            
                    <logic:iterate id="fish" collection="<%= seafoodCollection %>" type="com.freshdirect.fdstore.content.ContentNodeModel" indexId="idx">
                        <%      
                                if(!fish.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) continue;
                                    
                        		ProductModel fishProduct = (ProductModel)fish;
                                if(fishProduct.isUnavailable()){
                                    List alsoSoldAs = fishProduct.getAlsoSoldAs();
                                    
                                    for(Iterator i = alsoSoldAs.iterator();i.hasNext() && fishProduct.isUnavailable();){ 
                                        Object ref = i.next();
                                        if(ref instanceof ProductModel) {
	                                        ProductModel cm = (ProductModel) ref;
	                                        if(!cm.isUnavailable()){
	                                            fishProduct = cm;
	                                        }
                                        }
                                    }
                                    fish = fishProduct;
                                }
                                
                                
                                if(!fishProduct.isUnavailable()){
                                    
                                    int flavorRating = -1;
                                    int firmRating = -1;

                                    List<DomainValue> rating = fishProduct.getRating();
                                    for (Iterator<DomainValue> ai = rating.iterator(); ai.hasNext(); ) {
                                        DomainValue dv = ai.next();
                                        Domain d = dv.getDomain();
                                        
                                        if("firm".equals(d.getName())){ 
                                            firmRating=Integer.parseInt(dv.getValue());
                                        }
                                        
                                        if("flavorful".equals(d.getName())){ 
                                            flavorRating=Integer.parseInt(dv.getValue()); 
                                        }
                                    }
                                    
                                    if(firmRating > 0 && flavorRating > 0){
                                        if(firmRating < 3 ) {
                                            if(flavorRating < 3){
                                                delicateMild.add(fishProduct);
                                            } else if( flavorRating == 3 ){
                                                delicateModerate.add(fishProduct);
                                            } else if( flavorRating > 3 ){
                                                delicateFull.add(fishProduct);
                                            }
                                        } else if(firmRating == 3){
                                            if(flavorRating < 3){
                                                flakyMild.add(fishProduct);
                                            } else if( flavorRating == 3 ){
                                                flakyModerate.add(fishProduct);
                                            } else if( flavorRating > 3 ){
                                                flakyFull.add(fishProduct);                                
                                            }
                                        } else if(firmRating > 3){
                                            if(flavorRating < 3){
                                                meatyMild.add(fishProduct);
                                            } else if( flavorRating == 3 ){
                                                meatyModerate.add(fishProduct);
                                            } else if( flavorRating > 3 ){
                                                meatyFull.add(fishProduct);                                
                                            }
                                        }
                                    }
                                }
                        %>
                    </logic:iterate>
                    <%
                        String white = "#FFFFFF";
                        String orange = "#FF9933";
                        String green = "#669933";
                        String grey  = "#F5F5F5";
                    %>
                    <TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="535">
                        <TR VALIGN="TOP">
                            <TD WIDTH="85"><img src="/media_stat/images/template/fishfinder/fishes.gif" 
                                width="70" height="70" hspace="0" vspace="0" border="0" alt="Fishes"></TD>
                            <TD colspan='7' CLASS="text11"><FONT CLASS="title16">The Fish Finder</FONT><BR>
                                Want firm, full-flavored fish for chowder; flaky, mild fish for homemade 
                                fish sticks; or delicate, flavorful fish for saut&eacute;ing &#151; we've 
                                done the work &#151; just pick the flavor and texture you like!</TD>	
                        </TR>
                        <tr align='left' bgcolor='<%=orange%>'>
                            <td bgcolor='<%=white%>'></td>
                            <td class='bodycells' width='32%'><img src='/media_stat/images/template/fishfinder/flavor_mild.gif'></td>
                            <td WIDTH="1" BGCOLOR="<%=white%>"></td>
                            <td class='bodycells' width='32%'><img src='/media_stat/images/template/fishfinder/flavor_moderate.gif'></td>
                            <td WIDTH="1" BGCOLOR="<%=white%>"></td>
                            <td class='bodycells' width='32%'><img src='/media_stat/images/template/fishfinder/flavor_full.gif'></td>
                            <td WIDTH="1" BGCOLOR="<%=orange%>"></td>
                        </tr>
                        <tr valign='top'>
                            <td bgcolor='<%=green%>' class='textureLabels'>
                                <img src='/media_stat/images/template/fishfinder/texture_delicate.gif'><br>
                                <img src="/media_stat/images/layout/clear.gif" width="1" height="40">
                            </td>
                            <td class='bodycells'>
                                <logic:iterate id="fish" collection="<%= delicateMild %>" type="com.freshdirect.fdstore.content.ContentNodeModel" indexId="idx">
                                    <%
	                                    String phName = getParentId(fish);
                                    %>
                                    <a href="javascript:goThere('/product.jsp?productId=<%=fish.getContentName()%>&catId=<%=phName%>&trk=cpage')"><FONT class='<%=fish.getContentName().equals(selected)?"selected":"unSelected"%>'><%=fish.getGlanceName()%></font></a><%=(idx.intValue() != delicateMild.size() - 1)?",&nbsp;":""%>
                                </logic:iterate>
                            </td>
                            <td WIDTH="1" BGCOLOR="<%=green%>"><img src="/media_stat/images/layout/clear.gif" width="1"></td>
                            <td class='bodycells'>
                                <logic:iterate id="fish" collection="<%= delicateModerate %>" type="com.freshdirect.fdstore.content.ContentNodeModel" indexId="idx">
                                    <%
	                                    String phName = getParentId(fish);
                                    %>
                                    <a href="javascript:goThere('/product.jsp?productId=<%=fish.getContentName()%>&catId=<%=phName%>&trk=cpage')"><FONT class='<%=fish.getContentName().equals(selected)?"selected":"unSelected"%>'><%=fish.getGlanceName()%></font></a><%=(idx.intValue() != delicateModerate.size() - 1)?",&nbsp;":""%>
                                </logic:iterate>
                            </td>
                            <td WIDTH="1" BGCOLOR="<%=green%>"><img src="/media_stat/images/layout/clear.gif" width="1"></td>
                            <td class='bodycells'>
                                <logic:iterate id="fish" collection="<%= delicateFull %>" type="com.freshdirect.fdstore.content.ContentNodeModel" indexId="idx">
                                    <%
	                                    String phName = getParentId(fish);
                                    %>
                                    <a href="javascript:goThere('/product.jsp?productId=<%=fish.getContentName()%>&catId=<%=phName%>&trk=cpage')"><FONT class='<%=fish.getContentName().equals(selected)?"selected":"unSelected"%>'><%=fish.getGlanceName()%></font></a><%=(idx.intValue() != delicateFull.size() - 1)?",&nbsp;":""%>
                                </logic:iterate>
                            </td>
                            <td WIDTH="1" BGCOLOR="<%=green%>"><img src="/media_stat/images/layout/clear.gif" width="1"></td>
                        </tr>
                        <tr height="1">
                            <td BGCOLOR="<%=white%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                        </tr>
                        <tr valign='top' bgcolor='<%=grey%>'>
                            <td bgcolor='<%=green%>' class='textureLabels'>
                                <img src='/media_stat/images/template/fishfinder/texture_flaky.gif'><br>
                                <img src="/media_stat/images/layout/clear.gif" width="1" height="40">
                            </td>
                            <td class='bodycells'>
                                <logic:iterate id="fish" collection="<%= flakyMild %>" type="com.freshdirect.fdstore.content.ContentNodeModel" indexId="idx">
                                    <%
	                                    String phName = getParentId(fish);
                                    %>
                                    <a href="javascript:goThere('/product.jsp?productId=<%=fish.getContentName()%>&catId=<%=phName%>&trk=cpage')"><FONT class='<%=fish.getContentName().equals(selected)?"selected":"unSelected"%>'><%=fish.getGlanceName()%></font></a><%=(idx.intValue() != flakyMild.size() - 1)?",&nbsp;":""%>
                                </logic:iterate>
                            </td>
                            <td WIDTH="1" BGCOLOR="<%=green%>"><img src="/media_stat/images/layout/clear.gif" width="1"></td>
                            <td class='bodycells'>
                                <logic:iterate id="fish" collection="<%= flakyModerate %>" type="com.freshdirect.fdstore.content.ContentNodeModel" indexId="idx">
                                    <%
	                                    String phName = getParentId(fish);
                                    %>
                                    <a href="javascript:goThere('/product.jsp?productId=<%=fish.getContentName()%>&catId=<%=phName%>&trk=cpage')"><FONT class='<%=fish.getContentName().equals(selected)?"selected":"unSelected"%>'><%=fish.getGlanceName()%></font></a><%=(idx.intValue() != flakyModerate.size() - 1)?",&nbsp;":""%>
                                </logic:iterate>
                            </td>
                            <td WIDTH="1" BGCOLOR="<%=green%>"><img src="/media_stat/images/layout/clear.gif" width="1"></td>
                            <td class='bodycells'>
                                <logic:iterate id="fish" collection="<%= flakyFull %>" type="com.freshdirect.fdstore.content.ContentNodeModel" indexId="idx">
                                    <%
	                                    String phName = getParentId(fish);
                                    %>
                                    <a href="javascript:goThere('/product.jsp?productId=<%=fish.getContentName()%>&catId=<%=phName%>&trk=cpage')"><FONT class='<%=fish.getContentName().equals(selected)?"selected":"unSelected"%>'><%=fish.getGlanceName()%></font></a><%=(idx.intValue() != flakyFull.size() - 1)?",&nbsp;":""%>
                                </logic:iterate>
                            </td>
                            <td WIDTH="1" BGCOLOR="<%=green%>"><img src="/media_stat/images/layout/clear.gif" width="1"></td>
                        </tr>
                        <tr height="1">
                            <td BGCOLOR="<%=white%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                        </tr>
                        <tr valign='top'>
                            <td bgcolor='<%=green%>' class='textureLabels'>
                                <img src='/media_stat/images/template/fishfinder/texture_meaty.gif'><br>
                                <img src="/media_stat/images/layout/clear.gif" width="1" height="40">
                            </td>
                            <td class='bodycells'>
                                <logic:iterate id="fish" collection="<%= meatyMild %>" type="com.freshdirect.fdstore.content.ContentNodeModel" indexId="idx">
                                    <%
                                    String phName = getParentId(fish);
                                    %>
                                    <a href="javascript:goThere('/product.jsp?productId=<%=fish.getContentName()%>&catId=<%=phName%>&trk=cpage')"><FONT class='<%=fish.getContentName().equals(selected)?"selected":"unSelected"%>'><%=fish.getGlanceName()%></font></a><%=(idx.intValue() != meatyMild.size() - 1)?",&nbsp;":""%>
                                </logic:iterate>
                            </td>
                            <td WIDTH="1" BGCOLOR="<%=green%>"><img src="/media_stat/images/layout/clear.gif" width="1"></td>
                            <td class='bodycells'> 
                                <logic:iterate id="fish" collection="<%= meatyModerate %>" type="com.freshdirect.fdstore.content.ContentNodeModel" indexId="idx">
                                    <%
                                    String phName = getParentId(fish);
                                    %>
                                    <a href="javascript:goThere('/product.jsp?productId=<%=fish.getContentName()%>&catId=<%=phName%>&trk=cpage')"><FONT class='<%=fish.getContentName().equals(selected)?"selected":"unSelected"%>'><%=fish.getGlanceName()%></font></a><%=(idx.intValue() != meatyModerate.size() - 1)?",&nbsp;":""%>
                                </logic:iterate>
                            </td>
                            <td WIDTH="1" BGCOLOR="<%=green%>"><img src="/media_stat/images/layout/clear.gif" width="1"></td>
                            <td class='bodycells'>
                                <logic:iterate id="fish" collection="<%= meatyFull %>" type="com.freshdirect.fdstore.content.ContentNodeModel" indexId="idx">
                                    <%
                                    String phName = getParentId(fish);
                                    %>
                                    <a href="javascript:goThere('/product.jsp?productId=<%=fish.getContentName()%>&catId=<%=phName%>&trk=cpage')"><FONT class='<%=fish.getContentName().equals(selected)?"selected":"unSelected"%>'><%=fish.getGlanceName()%></font></a><%=(idx.intValue() != meatyFull.size() - 1)?",&nbsp;":""%>
                                </logic:iterate>
                            </td>
                            <td WIDTH="1" BGCOLOR="<%=green%>"><img src="/media_stat/images/layout/clear.gif" width="1"></td>
                        </tr>
                        <tr height="1">
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                            <td BGCOLOR="<%=green%>"></td>
                        </tr>
                        <tr>
                            <td><br></td>
                        </tr>
                    </table>
                </fd:ItemGrabber>
            </tmpl:put>
            
</tmpl:insert>