<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*,com.freshdirect.fdstore.content.util.SortStrategyElement' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute, com.freshdirect.fdstore.content.util.EnumWineSortType' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%@ page import='com.freshdirect.content.attributes.EnumAttributeName' %>
<%@ page import='com.freshdirect.fdstore.FDCachedFactory' %>
<%@ page import='com.freshdirect.fdstore.FDProductInfo' %>
<%@ page import='com.freshdirect.fdstore.FDResourceException' %>
<%@ page import='com.freshdirect.fdstore.FDSkuNotFoundException' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
<%@ page import='com.freshdirect.fdstore.content.CategoryModel' %>
<%@ page import='com.freshdirect.fdstore.content.CategoryRef' %>
<%@ page import='com.freshdirect.fdstore.content.ContentFactory' %>
<%@ page import='com.freshdirect.fdstore.content.ContentNodeI' %>
<%@ page import='com.freshdirect.fdstore.content.ContentNodeModel' %>
<%@ page import='com.freshdirect.fdstore.content.ContentRef' %>
<%@ page import='com.freshdirect.fdstore.content.DepartmentModel' %>
<%@ page import='com.freshdirect.fdstore.content.Domain' %>
<%@ page import='com.freshdirect.fdstore.content.DomainRef' %>
<%@ page import='com.freshdirect.fdstore.content.DomainValue' %>
<%@ page import='com.freshdirect.fdstore.content.DomainValueRef' %>
<%@ page import='com.freshdirect.fdstore.content.Image' %>
<%@ page import='com.freshdirect.fdstore.content.ProductModel' %>
<%@ page import='com.freshdirect.fdstore.content.ProductRef' %>
<%@ page import='com.freshdirect.fdstore.content.SkuModel' %>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI' %>


<%
    String catId = request.getParameter("catId"); 
    String deptId = request.getParameter("deptId"); 
    
       ContentNodeModel currentFolder = null;
    if(deptId!=null) {
	    currentFolder=ContentFactory.getInstance().getContentNodeByName(deptId);
    } else {
    	currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
    }
 %>   
<%@ include file="/includes/wine/i_wine_method_params.jspf" %> 
<%!
public Map getRatingImagePathMap(ProductModel displayProduct){

        Map ratingMap=null;

		List list1=displayProduct.getWineRating1();
	    List list2=displayProduct.getWineRating2();
	    List list3=displayProduct.getWineRating3();
	    
	    DomainValue ratingDvalue1=null;
	    DomainValue ratingDvalue2=null;
	    DomainValue ratingDvalue3=null;
	    
	    if(list1.size()>0){
	    	ratingDvalue1=(DomainValue)list1.get(0);
	    }
	    if(list2.size()>0){
	    	ratingDvalue2=(DomainValue)list2.get(0);
	    }
	    if(list3.size()>0){
	    	ratingDvalue3=(DomainValue)list3.get(0);
	    }
	    
	    
	    String rating1Str=null;
	    String rating2Str=null;
	    String rating3Str=null;
	    
        String rating1CoStr="";
        String rating2CoStr="";
        String rating3CoStr="";
        
	    if(ratingDvalue1!=null){
	    	rating1Str=	ratingDvalue1.getValue();
            Domain domain1=ratingDvalue1.getDomain();
            rating1CoStr=getLastStringToken(domain1.getName(),"_");            
	    }
	    if(ratingDvalue2!=null){
	    	rating2Str=	ratingDvalue2.getValue();
            Domain domain2=ratingDvalue2.getDomain();
            rating2CoStr=getLastStringToken(domain2.getName(),"_");
	    }
	    if(ratingDvalue3!=null){
	    	rating3Str=	ratingDvalue3.getValue();
            Domain domain3=ratingDvalue3.getDomain();
            rating3CoStr=getLastStringToken(domain3.getName(),"_");
	    }
        
	    int rating1=0;
	    int rating2=0;
	    int rating3=0;
       
	    
	    if(rating1Str!=null){
	    	rating1=Integer.parseInt(rating1Str);
	    }
	    if(rating2Str!=null){
	    	rating2=Integer.parseInt(rating2Str);
	    }
	    if(rating3Str!=null){
	    	rating3=Integer.parseInt(rating3Str);
	    }
	    
	    int largeRating=0;
	    String largeRatCo=null;
	    
	    if(largeRating<rating1){
	    	largeRating=rating1;	    	
	    	largeRatCo=rating1CoStr;
	    }if(largeRating<rating2){
	    	largeRating=rating2;            	    	
	    	largeRatCo=rating2CoStr;
	    }if(largeRating<rating3){	    	
	    	largeRating=rating3;	    
	    	largeRatCo=rating3CoStr;
	    }

        if(largeRating==0){
        	return null;
        }
        
        String imageRootPath="/media/editorial/win_usq/icons/rating/";                
        StringBuffer imagePathStr=new StringBuffer(imageRootPath).append(largeRatCo).append("_").append(largeRating).append(".gif");
        
        
        
        StringBuffer ratingStr=new StringBuffer("");
        if(rating1!=0){
            ratingStr.append("<B><FONT color='red'>").append(rating1CoStr.toUpperCase()+"</FONT> "+rating1).append("</B>   ");
        }
        if(rating2!=0){
            ratingStr.append("<B><FONT color='red'>").append(rating2CoStr.toUpperCase()+"</FONT> "+rating2).append("</B>   ");
        }
        if(rating3!=0){
            ratingStr.append("<B><FONT color='red'>").append(rating3CoStr.toUpperCase()+"</FONT> "+rating3).append("</B>   ");
        }

        
        ratingMap=new HashMap();
        ratingMap.put("RATING_STRING",ratingStr.toString());
        ratingMap.put("RATING_IMAGE_PATH",imagePathStr.toString());
    
        return ratingMap;
        
    }

    public  String getLastStringToken(String str,String separator){
		String token=str.substring(str.lastIndexOf(separator)+1);
		return token;
	}

%>
<%
    boolean sortDescending = "true".equalsIgnoreCase(sortDesc);
    String sortNameAttrib = currentFolder.getAttribute("LIST_AS", "full");
    if (!sortNameAttrib.equalsIgnoreCase(SortStrategyElement.SORTNAME_GLANCE) && !sortNameAttrib.equalsIgnoreCase(SortStrategyElement.SORTNAME_NAV))
        sortNameAttrib = SortStrategyElement.SORTNAME_FULL;
    Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
    FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
    String successPage="/cart_confirm.jsp?catId="+request.getParameter("catId");
    if(request.getParameter("productId")!=null)
    {
        successPage=successPage+"&productId="+request.getParameter("productId");
    }
    request.setAttribute("successPage",successPage); 

%>

<fd:FDShoppingCart id='cart' action='addToCart' result='result' successPage='<%=successPage %>'>

<%
   request.removeAttribute("successPage");
%>


<%
    if (sortedColl==null) sortedColl = new ArrayList();
    List displayList = getDisplayList(sortedColl);
    //This list is to hold the list of products by clicking on the sidenav referenced by i_wine_category_top.jspf
    List contextList = new ArrayList(displayList);
    int itemsToDisplay = getItemsToDisplay(displayList, request);
    int pageNumber = getPageNumber(displayList, itemsToDisplay, request);
    Integer offset = new Integer((pageNumber-1)*itemsToDisplay);
    Integer len = new Integer(itemsToDisplay);
%>



	
<!-- choosing selected view based on query string -->

<script language="javascript">


function chgQty(qtyFldName,delta,min,max) {

var qty = parseFloat(document.wine_detail_form[qtyFldName].value);
	if (isNaN(qty)) qty=0;
	qty = qty + delta;

	if (qty <= 0 || (qty < min && delta < 0) ){ 
		qty=0;
	} else if(qty < min && delta >=0) {
		qty=min;
	} else if (qty > max) {
		qty=max;
	}

	if (qty<=0) {
		document.wine_detail_form[qtyFldName].value='';
	} else {
		document.wine_detail_form[qtyFldName].value = qty;
	}

}

function sendForm(productIdVar,catIdVar,quantityVar,skuVar){

  //alert("productIdVar:"+document.wine_detail_form.productId.value);  
  document.wine_detail_form.quantity.value=document.wine_detail_form[quantityVar].value;
  document.wine_detail_form.productId.value=productIdVar;
  document.wine_detail_form.wineCatId.value=catIdVar;
  document.wine_detail_form.skuCode.value=skuVar;
  //alert("submitting the formasda");
  //document.wine_detail_form.submit();
  //alert("shoudent be here");
  return false;
}


</script>

<%@ include file="/includes/wine/i_wine_category_top.jspf" %> 
<!-- include 15 items -->
<%
SkuModel dfltSku = null;
 String thisProdBrandLabel=null;
 String price="";
 String salesUnitDesc="";
 String imagePath="";
 String productDescPath="";
 int index=0;
 
%>


<input type="hidden" name="quantity" value="1" />
<input type="hidden" name="productId" value="" />
<input type="hidden" name="wineCatId" value="" />
<input type="hidden" name="skuCode" value="" />
<logic:iterate indexId="idx" id="displayThing" length="<%= len.toString() %>" offset="<%= offset.toString() %>" collection="<%= displayList %>" type="com.freshdirect.fdstore.content.ContentNodeModel">
<%
index++;

if (displayThing.getContentType().equals(ContentNodeI.TYPE_PRODUCT)) {
   ProductModel displayProduct = (ProductModel)displayThing;
   Image productImage=displayProduct.getAlternateImage();
   
   String vintage="";
   List wineVintage=displayProduct.getWineVintage();		
		if(wineVintage!=null && wineVintage.size()>0){									
          DomainValue dValue=(DomainValue)wineVintage.get(0);
          vintage=dValue.getValue();
         }
    

    String wineRegion="";
		List wineRegionList=displayProduct.getNewWineRegion();		
		if(wineRegionList!=null && wineRegionList.size()>0){										
		    DomainValue dValue=(DomainValue)wineRegionList.get(0);
            wineRegion=dValue.getValue();
        }
        
     String wineCity=displayProduct.getWineCity();
   
   if(productImage!=null){
        imagePath=productImage.getPath();   
   }
   
   Html htmlDesc=displayProduct.getProductAbout();
   if(htmlDesc!=null){
      productDescPath=htmlDesc.getPath();
   }
   dfltSku = displayProduct.getDefaultSku();
   
   try {
			 	if (dfltSku !=null) {
					  FDProductInfo pi = FDCachedFactory.getProductInfo( dfltSku.getSkuCode() );					  
					  price=currencyFormatter.format(pi.getDefaultPrice());
                       //String salesUnitDescr = displayProduct.getSalesUnits()[0].getDescription();

                      String salesUnitDescr =  FDCachedFactory.getProduct( pi).getSalesUnits()[0].getDescription();
                      if (!"nm".equalsIgnoreCase(salesUnitDescr) && !"ea".equalsIgnoreCase(salesUnitDescr) && !"".equalsIgnoreCase(salesUnitDescr)) { 
                      	salesUnitDesc=salesUnitDescr;
                      }
			 	}
			   thisProdBrandLabel = displayProduct.getFullName();
	} catch (FDResourceException fde) {
				  throw new JspException(fde);
	} catch (FDSkuNotFoundException sknf) {
				  throw new JspException(sknf);
	}
    
    String ratingString="";
    String ratingImagePath="";
    Map wineRatingMap=getRatingImagePathMap(displayProduct);
    if(wineRatingMap!=null){
        ratingString=(String)wineRatingMap.get("RATING_STRING");
        
    }
    
    Image descImage=displayProduct.getDescriptiveImage();
    if(descImage!=null)
         ratingImagePath=descImage.getPath();
         
     SkuModel skuModel=displayProduct.getSku(0);
     String skuCode=skuModel.getSkuCode();
%>



<table class="w100">
<tr>
	<td>
		<img src="<%=imagePath%>" width="41" height="100" border="0" alt="" title="">
	</td>
	<td style="width: 100%">
		<div class="center_prod_name"><a href="product.jsp?productId=<%=displayProduct%>&catId=<%=displayProduct.getParentNode().getPK().getId()%>" title="product detail"><%=thisProdBrandLabel%></a></div>
		<div class="bolder"> <%=wineRegion%> > <%=wineCity%> > <%=vintage%></div>
		<div class="center_prod_descrip"><fd:IncludeMedia name="<%=productDescPath%>" /><></div>
		<div class="top_spacer"></div>
		<div>        
        <table class="left">
            <tr valign="bottom padlr6">
                  <td><span class="center_prod_price"><%=price%></span><span class="center_prod_size"><%="/"+salesUnitDesc%></span</td>
                    <td class="padlr6"> <INPUT TYPE="text" NAME="quantity_big_<%=index%>" SIZE="2" MAXLENGTH="2" CLASS="text11" value="<%=Math.round(displayProduct.getQuantityMinimum()) %>" onChange="chgQty('quantity_big_<%=index%>',0,<%= displayProduct.getQuantityMinimum() %>,<%= user.getQuantityMaximum(displayProduct) %>);"></td>
                    <td width="14" valign="bottom"><A HREF="javascript:chgQty('quantity_big_<%=index%>',<%= displayProduct.getQuantityIncrement()%>,<%= displayProduct.getQuantityMinimum() %>,<%= user.getQuantityMaximum(displayProduct) %>);"><img src="/media_stat/images/layout/grn_arrow_up.gif" width="10" height="9" border="0" vspace="2" alt="lesser quantity"></A><br/>
                    <A HREF="javascript:chgQty('quantity_big_<%=index%>',-<%= displayProduct.getQuantityIncrement()%>,<%= displayProduct.getQuantityMinimum() %>,<%= user.getQuantityMaximum(displayProduct) %>);"><img src="/media_stat/images/layout/grn_arrow_down.gif" width="10" height="9" border="0" vspace="2" alt="lesser quantity"></A>
                    </td>
                    <td>
                    <a href="javascript:void(0)" onClick="javascript:sendForm('<%=displayProduct%>','<%=displayProduct.getParentNode().getPK().getId()%>','quantity_big_<%=index%>','<%=skuCode%>');">
                    <input type="image" name="addSingleToCart_big" src="/media_stat/images/buttons/add_to_cart.gif"  ALT="ADD THIS ITEM TO YOUR CART" width="93" height="20" HSPACE="2" VSPACE="2" border="0"/></a><br>
                    </td>
            </tr>
      </table>                                
      </div>        
	</td>
	<td>
		<div>
			<%=ratingString%>
		</div>
		<div>
			<img src="<%=ratingImagePath%>" width="90" height="90" border="0" alt="" title="">
		</div>
	</td>
</tr>
</table>


<div class="hr_space"></div>
<% } %>
</logic:iterate>



<%
int templateType=currentFolder.getAttribute("TEMPLATE_TYPE",1);
%>
<%@ include file="/includes/wine/i_wine_category_bottom.jspf" %> 

</fd:FDShoppingCart>

<!-- end spacing -->
<div class="center_spacer"></div>
<div class="center_spacer"></div>
