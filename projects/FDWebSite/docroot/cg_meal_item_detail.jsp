<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='java.net.URLEncoder' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
FDCartLineI templateLine = null ;
//m for meal ids
String mcatId = request.getParameter("mcatId");
String mproductId = request.getParameter("mproductId");
String mskuCode  = request.getParameter("mskuCode");

//for shown product
String catId = request.getParameter("catId");
String productId = request.getParameter("productId");
String skuCode  = request.getParameter("skuCode");

// recipe-related info
String variantId        = request.getParameter("variantId");
String recipeId         = request.getParameter("recipeId");


ProductModel  product = null;
RecipeVariant variant = null;
Recipe        recipe  = null;


int            maxWidth = 320;
ContentFactory cf       = ContentFactory.getInstance();
List           skus     = new ArrayList();

if (mcatId != null && mproductId != null) {
    product =  ContentFactory.getInstance().getProductByName(mcatId,mproductId);

} else if (variantId != null && variantId.length() != 0) {
	variant = (RecipeVariant) ContentFactory.getInstance().getContentNode(variantId);
	recipe = (Recipe) variant.getParentNode();

} else if (recipeId !=null) {
	recipe = (Recipe) ContentFactory.getInstance().getContentNode(recipeId);
	variant = recipe.getDefaultVariant();
}
// TODO: handle lack of mproductId or recipeId


//accomodate claims include
ProductModel    productNode        = null;
CategoryModel   parentCat          = null;
String          prodNameAttribute  = null;
ContentFactory  contentFactory     = null;
SkuModel        defaultSku         = null;
Image           productImage       = null;
List            prodSkus           = null;
List            compositeGroups    = null;
Map             variations         = new HashMap();

FDProductInfo   defaultProductInfo = null;
FDProduct       defaultProduct     = null;
FDVariation[]   fdVariations       = null;
Map             fdVarOptDesc       = new HashMap();

if (product != null) {
    productNode       = product;
    parentCat         = (CategoryModel) productNode.getParentNode();
    prodNameAttribute = JspMethods.getProductNameToUse(parentCat);
    contentFactory    = ContentFactory.getInstance();
    defaultSku        = product.getDefaultSku();
    productImage      = product.getDetailImage();
    prodSkus          = productNode.getSkus();
} else if (variant != null) {
    prodSkus          = new ArrayList(variant.getDistinctSkus());
}

Map availOptSkuMap = new HashMap();
    
boolean hasSingleSku = (prodSkus.size() == 1);

if (hasSingleSku) {
	defaultSku = (SkuModel)prodSkus.get(0);
} else if (productNode != null) {
	if (mskuCode!=null) {
		// locate the proper sku based on request
		defaultSku = productNode.getSku(mskuCode);
	}
	if (defaultSku==null) {
		// no sku from request: default is the one with lowest price
		defaultSku = productNode.getDefaultSku();
	}
}

if (productNode != null) {

    compositeGroups = (List)productNode.getComponentGroups();

    try {
        for(int i=0; i<compositeGroups.size(); i++) {
         variations.putAll(((ComponentGroupModel)compositeGroups.get(i)).getVariationOptions() );
        }
    } catch (FDResourceException fde) {
             //* EAT EM UP, mmmmmmm  
    }

    defaultProductInfo = FDCachedFactory.getProductInfo(defaultSku.getSkuCode());
    defaultProduct     = FDCachedFactory.getProduct( defaultProductInfo);
    fdVariations       = defaultProduct.getVariations();
    fdVarOptDesc       = new HashMap();
    for (int varIdx = 0; varIdx<fdVariations.length;varIdx++) {
        fdVarOptDesc.put(fdVariations[varIdx].getName(),fdVariations[varIdx].getDescription());
    }

    //* check to see if any of the mandatory variations are all unavailable.  (rule: platter unavailable if all comp of a var is unavail)
    //* while we're at it, save skucode and productModel for future use.
    boolean aVariationUnavailable = false;
    for (int cvIdx = 0; cvIdx < fdVariations.length && !aVariationUnavailable; cvIdx++) {
        FDVariationOption[] varOpts = fdVariations[cvIdx].getVariationOptions(); 
        if (fdVariations[cvIdx].isOptional()) continue;

        int unAvailCount=0;
        for (int voIdx = 0; voIdx < varOpts.length;voIdx++) {
            String optSkuCode=varOpts[voIdx].getAttribute(EnumAttributeName.SKUCODE);
            if (optSkuCode==null) {
                unAvailCount++;
                continue;
            }
            try {
                ProductModel pm =cf.getProduct(optSkuCode);
                if (pm!=null ) {
                   if ( !pm.isUnavailable()   && availOptSkuMap.get(optSkuCode)==null) {
                    availOptSkuMap.put(optSkuCode,pm);
                   } else if ( pm.isUnavailable() ) {
                        unAvailCount++;
                   }
                } else {
                    unAvailCount++;
                }
            } catch (Exception e) {
                    JspLogger.PRODUCT.warn("party_platter_cat.jsp: catching FDSkuNotFoundException for SkuCode: "+optSkuCode+" and Continuing:\nException message:= "+e.getMessage());
                    unAvailCount++;
            }

        }
        if (unAvailCount==varOpts.length && unAvailCount!=0) aVariationUnavailable=true;
    }

} else if (variant != null) {
    // TODO: select a default SKU properly
    if (prodSkus != null && prodSkus.size() > 0) {
	    defaultSku = (SkuModel) prodSkus.get(0);
    }
}

%>

<tmpl:insert template='/common/template/large_long_pop.jsp'>
<%
    if (productNode != null) {
%>
        <tmpl:put name='title' direct='true'>FreshDirect - <%=productNode.getFullName()%> Details</tmpl:put>
<%
    } else if (variant != null) {
%>
        <tmpl:put name='title' direct='true'>FreshDirect - <%=recipe.getFullName()%> Details</tmpl:put>
<%
    }
%>
	<tmpl:put name='content' direct='true'>
<table border="0" cellpadding="0" cellspacing="0" width="520">
<tr valign="top">
<td style="border-right:solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="120" height="1"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="400" height="1"></td>
</tr>
<tr valign="top">
<td align="right" style="border-right:solid 1px #999966; padding-right: 6px;">
<%
    if (productNode != null) {
%>
<%-- START NAVIGATION --%>
<a href="?mcatId=<%=mcatId%>&mproductId=<%=mproductId%>&mskuCode=<%=mskuCode%>&catId=<%=mcatId%>&productId=<%=mproductId%>&skuCode=<%=mskuCode%>"><img src="/media_stat/images/headers/about_the_meal.gif" border="0" width="82" height="32"></a>
<br><br><a href="?mcatId=<%=mcatId%>&mproductId=<%=mproductId%>&mskuCode=<%=mskuCode%>&catId=<%=mcatId%>&productId=<%=mproductId%>&skuCode=<%=mskuCode%>" class="text12"><b><%=productNode.getFullName()%></b></a><br><br><br>

<%
int prodCount = 0;%>
<%
	int numProducts=0;
	int catIdx = 0;
	
	for (Iterator cgItr = compositeGroups.iterator(); cgItr.hasNext();) {
		ComponentGroupModel compGroup = (ComponentGroupModel) cgItr.next();
		List matCharNames=compGroup.getCharacteristicNames();
		List prodList = new ArrayList();
		
		//show ComponentGroup  Header Image and Editorial if there are characteristics names
		if (matCharNames.size()>0) { 	%>
		   	<span class="space8pix"><br></span><b><%=compGroup.getFullName()%></b><br>
	<%		for (Iterator optionsItr=matCharNames.iterator(); optionsItr.hasNext();) {
				String matCharName = (String)optionsItr.next();
				FDVariationOption[] varOpts = (FDVariationOption[]) variations.get(matCharName);
				if (varOpts==null) continue;
				for (int optIdx=0;optIdx<varOpts.length;optIdx++ ){
					String optSkuCode=varOpts[optIdx].getAttribute(EnumAttributeName.SKUCODE);

					ProductModel pm =(ProductModel)cf.getProduct(optSkuCode);
					if (pm.getSku(optSkuCode).isUnavailable()) continue;
					if (pm!=null && !prodList.contains(pm)) {
						prodList.add(pm);
					boolean currentSelection = optSkuCode.equals(skuCode) ; 	%>
			       <%= currentSelection ? "<i><b>":""%><a href="?mcatId=<%=mcatId%>&mproductId=<%=mproductId%>&mskuCode=<%=mskuCode%>&catId=<%=pm.getParentNode()%>&productId=<%=pm%>&skuCode=<%=optSkuCode%>"><%=JspMethods.getDisplayName(pm,prodNameAttribute)%></a><%= currentSelection ? "</b></i>":""%>
					<br>
			<%	 	}  %>
				  
	<%			}
			} %><br><%
		}
	}
	
	//===========================================================//
%>
	<br><br><br>
<%-- END NAVIGATION --%>
<%
    } else if (variant != null) {
%>
<%-- START NAVIGATION --%>
<a href="?recipeId=<%=recipeId%>&variantId=<%=variantId%>"><img src="/media_stat/images/headers/rec_ing_pop_catnav.gif" border="0" vspace="3" width="112" height="36"></a>
<br><br><a href="?recipeId=<%=recipeId%>&variantId=<%=variantId%>" class="text12"><b><%=recipe.getFullName()%></b></a><br><br><br>

<%
int prodCount = 0;%>
<%
	int numProducts=0;
	int catIdx = 0;

    for (Iterator it = variant.getSections().iterator(); it.hasNext(); ) {
        RecipeSection section = (RecipeSection) it.next();
	
	if (section.getName() != null && section.getName().length() > 0) {
%>
        <span class="space8pix"><br></span><b><%=section.getName()%></b><br>
<%
	}
        for (Iterator itt = section.getIngredients().iterator(); itt.hasNext();) {
            ConfiguredProduct cp       = (ConfiguredProduct) itt.next();

            String cpSkuCode = cp.getSkuCode();
            if (cpSkuCode == null) {
                continue;
            }
            boolean currentSelection = cpSkuCode.equals(skuCode) ; 	%>

           <%= currentSelection ? "<i><b>":""%><a href="?recipeId=<%=recipeId%>&variantId=<%=variantId%>&productId=<%=cp%>&skuCode=<%=cpSkuCode%>"><%=JspMethods.getDisplayName(cp,prodNameAttribute)%></a><%= currentSelection ? "</b></i>":""%>
           <span class="space2pix"><br><br></span>
    <%
        }
    }
	
	//===========================================================//
%>
	<br><br><br>
<%-- END NAVIGATION --%>

<%
    }
%>
</td>
<td style="padding-left:8px;">

<%
    if (productNode != null || (variant != null && skuCode != null)) {
        // if it's a specific product, display the details
%>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
	<% try {
		ProductModel compProduct = null;
		Image prodImg = null;
		String prodDescription = null;

		compProduct =  ContentFactory.getInstance().getProduct(skuCode);
        productNode = productNode == null ? compProduct : productNode;
		prodImg = compProduct.getDetailImage();
		prodDescription = ((Html)compProduct.getProductDescription()).getPath();	
        SkuModel selectSku = (SkuModel)compProduct.getSkus().get(0);
        FDProduct fdprd = null;
        try {
            fdprd = selectSku.getProduct();
        } catch (FDSkuNotFoundException fdsnfe) { throw fdsnfe; }
%>
		<img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>
		<img src="<%=prodImg.getPath()%>" width="<%=prodImg.getWidth()%>" height="<%=prodImg.getHeight()%>" alt="" border="0">
		
		<br><span class="space2pix"><br></span>
		<font class="title18"><%=compProduct.getFullName()%></font><br>
		
		<%if(prodDescription!=null){%>
			<span class="space2pix"><br></span><font class="text11"><fd:IncludeMedia name='<%=prodDescription%>'/><br></font>
		<%}%>
		
		<% if (fdprd != null && fdprd.hasNutritionInfo(ErpNutritionInfoType.HEATING)){ %>
			<br><%@ include file="/includes/product/i_heating_instructions.jspf"%>
		<% } %>

	<% 
		if (fdprd!=null && fdprd.hasIngredients()) {    %>
	       <br><%@ include file="/includes/product/i_product_ingredients.jspf"%>
	<%  }  %>
	
	<%@ include file="/includes/product/kosher.jspf" %>
	
	<%
	    if (fdprd != null && fdprd.hasNutritionFacts()) { %>
			<br>
	        <%@ include file="/includes/product/i_product_nutrition.jspf"%>
	        <br><br>
	<%  }
    %>
       
    <%
	 
	}
	catch (FDSkuNotFoundException ex){
		throw ex;
	}	%>
		
		</td>
	</tr>
	
</table>
<%
    } else if (variant != null) {
        // show the recipe details
        //
        RecipeSource source          = recipe.getSource();
        String       sourceName      = "";

        if (source != null) {
            sourceName      =  "From \"" + source.getName() + "\"";
        }

        MediaI recipeDesc = recipe.getDescription();
        MediaI recipePhoto = recipe.getTitleImage();
        MediaI recipeIngrdMedia = recipe.getIngredientsMedia();
%>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr valign="top">
		<td colspan="2"><span class="title16"><%=recipe.getName().toUpperCase()%></span><br>
        <span class="recipe_author"><%=sourceName%> <%=recipe.getAuthorNames()%></span><br><br></td>
    </tr>
	<tr valign="top">    
		<td><% if(recipeIngrdMedia!=null){ %><img src="/media_stat/recipe/rec_hdr_ingredients.gif" width="92" height="10"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br><fd:IncludeMedia name='<%= recipeIngrdMedia.getPath() %>' /><% } %>
		</td>
        <td style="padding-left:15px;" align="right"><% if(recipePhoto!=null){ %><img src=<%=recipePhoto.getPath()%> width="<%=recipePhoto.getWidth()%>" height="<%=recipePhoto.getHeight()%>" border="0"><% } %></td>
    </tr>
	<% if(recipeDesc!=null){ %>
		<tr><td colspan="2"><br></td></tr>
		<tr><td colspan="2" bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
		<tr>
			<td colspan="2"><br><fd:IncludeMedia name='<%= recipeDesc.getPath() %>' /><br></td>
		</tr>
	<% } %>
</table>
<%
    }
%>

</td>
</tr>				
</table>
	</tmpl:put>
</tmpl:insert>
