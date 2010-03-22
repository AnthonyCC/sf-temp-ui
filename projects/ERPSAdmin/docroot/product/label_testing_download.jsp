<%@ page contentType='text/plain' import='com.freshdirect.erp.model.*,com.freshdirect.fdstore.*,com.freshdirect.fdstore.content.*,java.util.*' %><%@ taglib uri='template' prefix='tmpl' %><%@ taglib uri='logic' prefix='logic' %><%@ taglib uri='freshdirect' prefix='fd' %><fd:ProductSearch results='searchResults' searchtype='<%= request.getParameter("searchtype") %>' searchterm='<%= request.getParameter("searchterm") %>'><% /* File format */
/*
Data element			Data type		Length	
SAP Material#			CHAR			18
Record Identifier		CHAR			4
Data					CHAR			max allowed of the three text IDs ZL01, ZL02 and ZL03

ZL01 = description
ZL02 = configuration description
ZL03 = ingredients

SAMPLE:
SAP Material#		Record Identifier		Data		   
000000000300300443		ZL01			BEEF PORTERHOUSE STEAK|USDA PRIME
000000000300300443		ZL02			2.0", USDA Prime, Vacuum Pack
000000000300700045		ZL01			Large Rotisserie Chicken
000000000300700045		ZL02			Antibiotic Free, Traditional Seasoning
000000000300700045		ZL03			Chicken, Marinade (Paprika, Salt, Sugar, Garlic Powder, Dextrose, Onion Powder, 
*/%><%

%><logic:iterate id="productInfo" collection="<%= searchResults %>" type="com.freshdirect.erp.model.ErpProductInfoModel"><fd:Nutrition id="nutrition" skuCode='<%= productInfo.getSkuCode() %>'><logic:iterate id="materialInfo" collection="<%= productInfo.getMaterialSapIds() %>" type="java.lang.String"><%

    /* Print out material description */
    out.print(materialInfo + "ZL01");
    String fullName = "", descr = "", label="";
    try {
        ProductModel pm = ContentFactory.getInstance().getProduct(productInfo.getSkuCode());
    	FDProductInfo fdinfo = FDCachedFactory.getProductInfo(productInfo.getSkuCode());
    	FDProduct fdp = FDCachedFactory.getProduct(fdinfo);
    	label = fdp.getAttribute("label_name", "");
    	if( label.trim().equals("") )
	        fullName = pm.getFullName();
	    else
	    	fullName = label;
    } catch (FDSkuNotFoundException fdsnfe) {
        descr = productInfo.getDescription();
    }
    
    if(fullName.length() > descr.length())
        out.print(fullName);
    else
    	out.print(descr);
    out.println();
    
    /* Print out longest configuration - stolen from OrderBot */
	FDProductInfo prodInf = FDCachedFactory.getProductInfo(productInfo.getSkuCode());
    FDProduct product = FDCachedFactory.getProduct(prodInf);
    FDVariation[] variations = product.getVariations();

    // find the largest sales unit info:
    FDSalesUnit[] units = product.getSalesUnits();
    String unit = "";
    for(int i = 0; i< units.length; i++) {
    	if(units[i].getDescription() != null) {
	       	if(units[i].getDescription().length() >= unit.length()) {
	       		unit = units[i].getDescription();
	       	}
        }
	}
	
	if(unit.equals("nm")) {
	    unit = "";
	}
    // System.out.println("Max Unit: " + unit + ".");

    // find the largest option for each variation
    String confDescr = "";
    HashMap optionMap = new HashMap();
    for (int i=0; i<variations.length; i++) {
        FDVariation variation = variations[i];
        String option = "";
        FDVariationOption[] options = variation.getVariationOptions();
        for(int j = 0; j < options.length; j++) {
        	if(options[j].getDescription() != null) {
        		if(options[j].getDescription().length() >= option.length()) {
        			option = options[j].getDescription();
        		}
        	}
        }
        if(i > 0)
           confDescr += ", ";
        confDescr += option;
    }
    
    if(unit.length() > 0 && confDescr.length() > 0)
    	unit += ", ";
    
    confDescr = unit + confDescr;

    if(confDescr.length() > 0) {
	    out.print(materialInfo + "ZL02");
	    out.print(confDescr);
	    out.println();   
    }
    
    /* Print out ingredients */
    if(nutrition != null && nutrition.getIngredients() != null && nutrition.getIngredients().length() > 0) {
	    out.print(materialInfo + "ZL03");
	    out.print(nutrition.getIngredients());
	    out.println();
    }
%></logic:iterate></fd:Nutrition></logic:iterate></fd:ProductSearch>