<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.content.util.*' %>
<%@ page import='java.util.*'%>
<%@ page import='java.net.*'%>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%!
    java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>

<tmpl:insert template='/common/template/dnav.jsp'>
	<tmpl:put name='title' direct='true'>Don't Know Beans</tmpl:put>
		<tmpl:put name='content' direct='true'>
<fd:Department id='coffeeDepartment' departmentId='<%= request.getParameter("deptId") %>'/>
<fd:ItemGrabber category='<%= (ContentNodeModel)coffeeDepartment %>' id='coffeeCollection'  
     depth='10'
    ignoreShowChildren='true'  returnHiddenFolders='false' >
<% 
// remove all the folders from this collection.

int prodCounter=0;
String bgcolor = null;
String sortCol=request.getParameter("sortBy");
String regionValue = null;
String roastValue = null;
String bodyValue = null;
String acidityValue=null;
String organicValue = null;
String decafValue = null;
String decafImg = null;
String organicImg = null;
String bodyImg = null;
String acidityImg = null;
String tasteValue = null;
String typeValue = null;
String goodForEspresso = null;
String cellColor = null;
MultiAttribute prodRatingDomVals = null;
List ratingValues = null;
StringBuffer criteriaFound = new StringBuffer();

String question1 = request.getParameter("question1");
String question2 = request.getParameter("question2");
String question3 = null; //request.getParameter("question3");
String question4 = request.getParameter("question4");
String question5 = request.getParameter("question5");
String question6 = request.getParameter("question6");
StringBuffer questionsParams = new StringBuffer();

if (question1!=null) questionsParams.append("&question1="+question1);
if (question2!=null) questionsParams.append("&question2="+question2);
if (question3!=null) questionsParams.append("&question3="+question3);
if (question4!=null) questionsParams.append("&question4="+question4);
if (question5!=null) questionsParams.append("&question5="+question5);
if (question6!=null) questionsParams.append("&question6="+question6);

ArrayList questions=new ArrayList();
if (question4!=null && "Yes".equalsIgnoreCase(question4)) questions.add("question4");
if (question5!=null && "Yes".equalsIgnoreCase(question5)) questions.add("question5");
if (question6!=null && "Espresso".equalsIgnoreCase(question6)) questions.add("question6");
if (question1!=null) questions.add("question1");
if (question2!=null) questions.add("question2");
if (question3!=null) questions.add("question3");

if (sortCol==null) sortCol="coffee_region";
int itemMatches = 0;
HashMap itemsToShow = new HashMap();
// remove all the folders from this collection and unavailable items,  and
//count how many items match the specified criteria
int q1Counter = 0;
int q2Counter = 0;
int q3Counter = 0;
int q4Counter = 0;
int q5Counter = 0;
int q6Counter = 0;
int matchCounter = 0;
for(Iterator itr= coffeeCollection.iterator();itr.hasNext() && matchCounter<15;){
    ContentNodeModel item = (ContentNodeModel)itr.next();
    if (!(item instanceof ProductModel)) {
        itr.remove();
        continue;
     } else if (((ProductModel)item).isUnavailable()) continue;
     
     //initialize each time
     tasteValue = "~~";
     acidityValue = "~~";
     roastValue = "~~";
     typeValue = "~~";
     decafValue = "~~";
     goodForEspresso = "~~";

     ProductModel coffeeProduct = (ProductModel)item;
     StringBuffer matchNames=new StringBuffer();
     boolean matched = true;
     prodRatingDomVals = (MultiAttribute)coffeeProduct.getAttribute("RATING");
     ratingValues = prodRatingDomVals==null?new ArrayList():prodRatingDomVals.getValues();

     // get the values for the attributes that participate in the criteria. from the Rating and Usage MultiAttribute)
    for(Iterator dvItr = ratingValues.iterator();dvItr.hasNext() ;) {
        Object obj = dvItr.next();
        if (!(obj instanceof DomainValueRef)) continue;
        DomainValue dmv = ((DomainValueRef)obj).getDomainValue(); 
        Domain dom = dmv.getDomain();
        if (dom.getName().equalsIgnoreCase("coffee_type")) {
            typeValue = dmv.getValue();
        } else  if (dom.getName().equalsIgnoreCase("coffee_roast")) {
            roastValue = dmv.getValue();
        } else  if (dom.getName().equalsIgnoreCase("body")) {
            bodyValue = dmv.getValue();
        } else  if (dom.getName().equalsIgnoreCase("acidity")) {
            acidityValue = dmv.getValue();
        } else  if (dom.getName().equalsIgnoreCase("decaffeinated")) {
            decafValue = dmv.getValue();
        } else if (dom.getName().equalsIgnoreCase("goodforespresso")) {
            goodForEspresso = dmv.getValue();
        }

    }

    itemsToShow.put(coffeeProduct,matchNames);
    matched = true;
    //ok. great.  we have the values of the attributes that participate in the criteria..check-em.
    if (question1!=null && question1.toLowerCase().indexOf(roastValue.toLowerCase())==-1){ matched=false; //roast type
    }else if (question1!=null && question1.toLowerCase().indexOf(roastValue.toLowerCase())!=-1 && q1Counter <5){
        matchNames.append("question1 ");
        q1Counter++;
        if (q1Counter==1) criteriaFound.append("question1 ");
    }

    if (question2!=null && question2.indexOf(acidityValue)==-1) {matched=false; ;  //acidity (sharper/smoother)
    }else if (question2!=null && question2.indexOf(acidityValue)!=-1 && q2Counter <5){
        matchNames.append("question2 ");
        q2Counter++;
        if (q2Counter==1) criteriaFound.append("question2 ");
    }

   //if (question3!=nulll && question3.toLowerCase().indexOf(roastValue.toLowerCase())==-1) continue;
    if ("no".equalsIgnoreCase(question4) && "flavored".equalsIgnoreCase(typeValue)) {matched=false; ;  // drinks flavored coffeds
    }else if (question4!=null && "flavored".equalsIgnoreCase(typeValue) && q4Counter <5){
        matchNames.append("question4 ");
        q4Counter++;
        if (q4Counter==1) criteriaFound.append("question4 ");
    }

    if ("no".equalsIgnoreCase(question5) && "true".equalsIgnoreCase(decafValue)) {matched=false; ; // does not drink decaf..dont return decaf items
    }else if (question5!=null && "true".equalsIgnoreCase(decafValue) && q5Counter <5){
        matchNames.append("question5 ");
        q5Counter++;
        if (q5Counter==1) criteriaFound.append("question5 ");
    }

    if ("espresso".equalsIgnoreCase(question6) && !"true".equalsIgnoreCase(goodForEspresso)) {matched=false; ;
    }else if (question6!=null && "true".equalsIgnoreCase(goodForEspresso) && q6Counter <5){
        matchNames.append("question6 ");
        q6Counter++;
        if (q6Counter==1) criteriaFound.append("question6 ");
    }

    //this item is good, add it to the hash.
    if (matched) {
        matchNames.append("matched");
        matchCounter++;
    }
    // remove the item from the hash if it did not match any of the criterias
    if (matchNames.length()==0) itemsToShow.remove(coffeeProduct);

}
boolean noItemsFound = (matchCounter==0);
%>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="700">
<TR VALIGN="TOP">
	<TD WIDTH="700">
		<FONT CLASS="title16">Recommendations for You</FONT><BR>
<%
if (!noItemsFound) { %>
		Here are the coffees we recommend based on your responses. Enjoy!<BR>
<%} else {%>
                We're sorry.  We couldn't find the bean of your dreams.  But based on your tastes, we think you'll enjoy one of the coffees below.
<%}%>
		<font class="space4pix"><br></font>
	</TD>
</TR>
</TABLE>
<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="1" WIDTH="700">
<tr><td colspan="8"><IMG SRC="/media_stat/images/layout/999966.gif" ALT="" WIDTH="700" HEIGHT="1"></td></tr>
<tr><td colspan="8"><IMG SRC="/media_stat/images/layout/clear.gif" ALT="" WIDTH="1" HEIGHT="1"></td></tr>
<TR VALIGN="MIDDLE" BGCOLOR="#DDDDDD">
	<TD WIDTH="140" ALIGN="CENTER" CLASS="text11bold"><%="name".equalsIgnoreCase(sortCol)?"Name":"<A HREF=\"coffee_beans_results.jsp?sortBy=region&deptId=cof&catId=cof_dkbns"+questionsParams+"\">Name</a>"%><BR></TD>
	<TD WIDTH="125" ALIGN="CENTER" CLASS="text11bold"><%="coffee_Region".equalsIgnoreCase(sortCol)?"Region":"<A HREF=\"coffee_beans_results.jsp?sortBy=coffee_region&deptId=cof&catId=cof_dkbns"+questionsParams+"\">Region</A>"%><BR></TD>
	<TD WIDTH="85" ALIGN="CENTER" CLASS="text11bold"><%="coffee_Roast".equalsIgnoreCase(sortCol)?"Roast":"<A HREF=\"coffee_beans_results.jsp?sortBy=coffee_roast&deptId=cof&catId=cof_dkbns"+questionsParams+"\">Roast</A>"%><BR></TD>
	<TD WIDTH="65" ALIGN="CENTER" CLASS="text11bold">- <%="Body".equalsIgnoreCase(sortCol)?"Body":"<A HREF=\"coffee_beans_results.jsp?sortBy=body&deptId=cof&catId=cof_dkbns"+questionsParams+"\">Body</A>"%> +<BR></TD>
	<TD WIDTH="80" ALIGN="CENTER" CLASS="text11bold">- <%="Acidity".equalsIgnoreCase(sortCol)?"Acidity":"<A HREF=\"coffee_beans_results.jsp?sortBy=Acidity&deptId=cof&catId=cof_dkbns"+questionsParams+"\">Acidity</A>"%> +<BR></TD>
<%-- 	<TD WIDTH="80" ALIGN="CENTER" CLASS="text11bold"><%//="Espresso".equalsIgnoreCase(sortCol)?"Great For<BR> Espresso":"<A HREF=\"javascript:soon()">Great For<BR> Espresso</A>"%><BR></TD> --%>
	<TD WIDTH="50" ALIGN="CENTER" CLASS="text11bold"><%="Organic".equalsIgnoreCase(sortCol)?"Organic":"<A HREF=\"coffee_beans_results.jsp?sortBy=organic&deptId=cof&catId=cof_dkbns"+questionsParams+"\">Organic</A>"%><BR></TD>
	<TD WIDTH="40" ALIGN="CENTER" CLASS="text11bold"><%="Decaffeinated".equalsIgnoreCase(sortCol)?"Decaf":"<A HREF=\"coffee_beans_results.jsp?sortBy=decaffeinated&deptId=cof&catId=cof_dkbns"+questionsParams+"\">Decaf</A>"%><BR></TD>
	<TD WIDTH="100" ALIGN="CENTER" CLASS="text11bold"><%="Price".equalsIgnoreCase(sortCol)?"Price":"<A HREF=\"coffee_beans_results.jsp?sortBy=price&deptId=cof&catId=cof_dkbns"+questionsParams+"\">Price</A>"%><BR></TD>

</TR>

<%
//=======================  Ok now.. Display the stuff.  ====================
boolean sortInReverseOrder = false;
if ("acidity,body,organic,decaffeinated".indexOf(sortCol.toLowerCase())!=-1) {
    sortInReverseOrder=true;
}

// if the itemsToShow hash is empty then figure out how maany times to loop
int loopCounter = 1;
int prodsMax = 15;

if (noItemsFound) {
    loopCounter = questions.size();//(question1==null?0:1)+(question2==null?0:1)+(question3==null?0:1)+(question4==null?0:1)+(question5==null?0:1)+(question6==null?0:1);
    prodsMax=5;
}
List sortStrategy = new ArrayList();
//sortStrategy.add(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY,false));
if ("price".equalsIgnoreCase(sortCol)) {
  sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRICE,false));
} else {
   sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_ATTRIBUTE,"RATING",sortCol,sortInReverseOrder));
}
sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME,SortStrategyElement.SORTNAME_FULL,false));
%>
<fd:ItemSorter nodes='<%= (List)coffeeCollection %>' strategy='<%=sortStrategy%>'/>
<%
//List sortedStuff = JspMethods.sorter(coffeeCollection,sortCol,sortInReverseOrder,false);
for (int displayLoop=0;displayLoop<loopCounter;displayLoop++) {
    prodCounter = 0;
    String questionName=null;
    // if displaying our recommendation...figure out which question to print
    if(noItemsFound) {
        String displayHeading = "";
        questionName = (String)((ArrayList)questions).get(displayLoop);
        //if this question was not found then do not display the heading for it.
        if (criteriaFound.length() < 1 || criteriaFound.toString().indexOf(questionName)==-1) continue;

        if ("question1".equalsIgnoreCase(questionName)) {
            if ("medium".equals(question1)) {
                displayHeading = "Light, mild";
            }else if ("viennese".equals(question1)) {
                displayHeading="Not too light, not too dark";
            } else displayHeading = "Rich, bold";
        }
        if ("question2".equalsIgnoreCase(questionName)) {
            if ("4,5".equals(question2)) { 
                displayHeading = "Sharper Coffees";
            } else displayHeading = "Smoother Coffees";
        }
        if ("question3".equalsIgnoreCase(questionName)) {
            displayHeading = "blah 3";
        }
        if ("question4".equalsIgnoreCase(questionName)) {
            displayHeading="Flavored Coffees";
        }
        if ("question5".equalsIgnoreCase(questionName)) {
            displayHeading = "Decaffeinated Coffees";
        }
        if ("question6".equalsIgnoreCase(questionName)) {
            displayHeading = "Good For Espresso";
        }
%>
<tr><td colspan="8"><br><font class="text11bold"><%=displayHeading%></font><br><IMG SRC="/media_stat/images/layout/999966.gif" ALT="" WIDTH="700" HEIGHT="1"></td></tr>
<%  }

    for(Iterator itr= coffeeCollection.iterator();itr.hasNext() && prodCounter<prodsMax;){
        ContentNodeModel cnm = (ContentNodeModel)itr.next();
        if (!(cnm instanceof ProductModel)) continue;
        ProductModel coffeeProduct = (ProductModel)cnm;
        StringBuffer matchNames = (StringBuffer)itemsToShow.get(coffeeProduct);
        if (matchNames==null) continue;
        Comparator priceComp = new ProductModel.PriceComparator();

        List skus = coffeeProduct.getSkus(); 
        //if (prodParent==null || !(prodParent instanceof CategoryModel)) continue;
	for (ListIterator li=skus.listIterator(); li.hasNext(); ) {
		SkuModel sku = (SkuModel)li.next();
       	if ( sku.isUnavailable() ) {
			li.remove();
		}
	}
        int skuSize = skus.size();

        SkuModel sku = null;
        String prodPrice = null;
        if (skuSize==0) continue;  // skip this item..it has no skus.  Hmmm?
        if (skuSize==1) {
            sku = (SkuModel)skus.get(0);  // we only need one sku
        }
        else {
            sku = (SkuModel) Collections.min(skus, priceComp);
        }
%>

        <fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
<% 
        prodPrice = currencyFormatter.format(productInfo.getDefaultPrice())+"/"+ productInfo.getDisplayableDefaultPriceUnit().toLowerCase();
%>						
        </fd:FDProductInfo>
<%



        if (!noItemsFound) {
            if(matchNames.toString().indexOf("matched")==-1) continue;  // showing exact matches..this item is not one
       } else  if (matchNames.toString().indexOf(questionName)==-1) continue; // showing recommendations, this items not matching current qcriteria

        regionValue = "&nbsp;&#8212;&nbsp;";
        roastValue = "&nbsp;&#8212;&nbsp;";
        bodyValue = null;
        acidityValue=null;
        organicValue = null;
        decafValue = null;
        decafImg = "&nbsp;&#8212;&nbsp;";
        organicImg = "&nbsp;&#8212;&nbsp;";
        bodyImg = "&nbsp;&#8212;&nbsp;";
        acidityImg = "&nbsp;&#8212;&nbsp;";
        typeValue = null;
        prodRatingDomVals = (MultiAttribute)coffeeProduct.getAttribute("RATING");
        ratingValues = prodRatingDomVals==null?new ArrayList():prodRatingDomVals.getValues();
        
        for(Iterator dvItr = ratingValues.iterator();dvItr.hasNext() ;) {
            Object obj = dvItr.next();
            if (!(obj instanceof DomainValueRef)) continue;
            DomainValue dmv = ((DomainValueRef)obj).getDomainValue(); 
            Domain dom = dmv.getDomain();
            if (dom.getName().equalsIgnoreCase("coffee_type")) {
                typeValue = dmv.getValue();
            } else if (dom.getName().equalsIgnoreCase("coffee_region")) {
                regionValue = dmv.getValue();
            } else  if (dom.getName().equalsIgnoreCase("coffee_roast")) {
                roastValue = dmv.getValue();
            } else  if (dom.getName().equalsIgnoreCase("body")) {
                bodyValue = dmv.getValue();
            } else  if (dom.getName().equalsIgnoreCase("acidity")) {
                acidityValue = dmv.getValue();
            } else if (dom.getName().equalsIgnoreCase("organic")) {
                organicValue = dmv.getValue();
            } else  if (dom.getName().equalsIgnoreCase("decaffeinated")) {
                decafValue = dmv.getValue();
            }

        }

        prodCounter++;
        if(prodCounter%2 != 0){
                bgcolor = "#eeeeee";
        }else{
                bgcolor = "#ffffff";
        }
        cellColor = bgcolor;



        if (bodyValue!=null) {
            if (sortCol.equalsIgnoreCase("body")) {
                cellColor = "#dddddd";
            }
            bodyImg = "<img src=\""+"/media_stat/images/template/rating3_"+cellColor.substring(1,cellColor.length())+"_05_0"+bodyValue+".gif"+"\">";
        } 
        if (acidityValue!=null) {
            if (sortCol.equalsIgnoreCase("acidity")) {
                cellColor = "#dddddd";
            }
            acidityImg = "<img src=\""+"/media_stat/images/template/rating3_"+cellColor.substring(1,cellColor.length())+"_05_0"+acidityValue+".gif"+"\">";
        } 

        if (organicValue!=null) {
            if ("true".equalsIgnoreCase(organicValue)){ 
                organicImg = "<img src=\""+"/media_stat/images/layout/orangedot.gif"+"\">";
            } else {
                organicImg = "&nbsp;";
            }
        } 

        if (decafValue!=null) {
            if ("true".equalsIgnoreCase(decafValue)){ 
                decafImg = "<img src=\""+"/media_stat/images/layout/orangedot.gif"+"\">";
            } else {
                decafImg = "&nbsp;";
            }
        } 
        


        String prodURL = response.encodeURL("/product.jsp?catId="+(CategoryModel)coffeeProduct.getParentNode()+"&productId="+coffeeProduct+"&trk=cquiz");
//    if (coffeeProduct.isUnavailable) continue;
%>
<tr>
<TD WIDTH="140" ALIGN="LEFT" bgcolor="<%="name".equalsIgnoreCase(sortCol)?"#dddddd":bgcolor%>" CLASS="text11"><a href="<%=prodURL%>"><%=coffeeProduct.getFullName() %></a></TD>
<TD WIDTH="125" ALIGN="CENTER" bgcolor="<%="coffee_region".equalsIgnoreCase(sortCol)?"#dddddd":bgcolor%>" CLASS="text11"><%=regionValue%></TD>
<TD WIDTH="85" ALIGN="CENTER" bgcolor="<%="coffee_roast".equalsIgnoreCase(sortCol)?"#dddddd":bgcolor%>" CLASS="text11"><%=roastValue%></TD>
<TD WIDTH="65" ALIGN="CENTER" bgcolor="<%="body".equalsIgnoreCase(sortCol)?"#dddddd":bgcolor%>" CLASS="text11"><%=bodyImg%></TD>
<TD WIDTH="80" ALIGN="CENTER" bgcolor="<%="acidity".equalsIgnoreCase(sortCol)?"#dddddd":bgcolor%>" CLASS="text11"><%=acidityImg%></TD>
<!-- 	<TD WIDTH="80" ALIGN="CENTER" CLASS="text11bold"><A HREF="javascript:soon()">Great For<BR> Espresso</A><BR></TD> -->
<TD WIDTH="50" ALIGN="CENTER" bgcolor="<%="organic".equalsIgnoreCase(sortCol)?"#dddddd":bgcolor%>" CLASS="text11"><%=organicImg%></TD>
<TD WIDTH="40" ALIGN="CENTER" bgcolor="<%="decaffeinated".equalsIgnoreCase(sortCol)?"#dddddd":bgcolor%>" CLASS="text11"><%=decafImg%></TD>
<TD WIDTH="100" ALIGN="CENTER" bgcolor="<%="price".equalsIgnoreCase(sortCol)?"#dddddd":bgcolor%>" CLASS="text11"><%=prodPrice%></TD></tr>

<%
    }
}%>
</table>
</fd:ItemGrabber>
</tmpl:put>
</tmpl:insert>