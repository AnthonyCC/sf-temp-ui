<%@page import="java.util.*"%>
<%@page contentType="text/html"%>
<%@page import="com.freshdirect.fdstore.*"%>
<%@page import="com.freshdirect.storeapi.content.*"%>
<%@page import="com.freshdirect.fdstore.customer.*"%>
<%@page import="com.freshdirect.fdstore.ZonePriceListing"%>
<%@ page import="com.freshdirect.common.pricing.ZoneInfo"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>

<%
    if (request.getMethod().equalsIgnoreCase("POST")) {
        String orderSkus = request.getParameter("orderSkus");
        boolean mult = request.getParameter("orderStyle").equalsIgnoreCase("multiple");
        FDUserI user=((FDUserI) request.getSession(false).getAttribute("freshdirect.user"));
        FDCartModel cart = user.getShoppingCart();
        ArrayList skus = new ArrayList();
        for (StringTokenizer stoke = new StringTokenizer(orderSkus, "\n"); stoke.hasMoreTokens();) {
            String skuCode = stoke.nextToken().trim().toUpperCase();
            if ((skuCode != null) && (!"".equals(skuCode))) {
                cart.addOrderLines(makeOrderLines(skuCode, mult,user));
            }
        }
        response.sendRedirect("/view_cart.jsp");
    }
%>
<html lang="en-US" xml:lang="en-US">
<head><title>Order Maker</title></head>
<body>

<form method=post>
<table border=1>
<tr>
    <td><input type='radio' name='orderStyle' value='single' checked></td><td>One order line per sku</td>
</tr><tr>
    <td><input type='radio' name='orderStyle' value='multiple'></td><td>Multiple order lines per sku</td>
</tr><tr>
    <td colspan=2 align=center><textarea name='orderSkus' cols='10' rows='20'>paste skus in here, one per line</textarea></td>
</tr></tr>
    <td colspan=2 align=center><input type=submit value='...add order lines to cart...'></td>
</tr>
</table>
</form>

</body>
</html>

<%!

    private List makeOrderLines(String skuCode, boolean multiple,FDUserI user) throws FDResourceException, FDSkuNotFoundException {

        ArrayList lines = new ArrayList();
        ContentFactory contentFactory = ContentFactory.getInstance();
        FDProductInfo productInfo = FDCachedFactory.getProductInfo(skuCode);
        FDProduct product = FDCachedFactory.getProduct(productInfo);
        ProductModel productmodel = contentFactory.getProduct(skuCode);
        SkuModel skumodel = null;
        for (Iterator sIter = productmodel.getSkus().iterator(); sIter.hasNext();) {
            SkuModel sku = (SkuModel) sIter.next();
            if (sku.getSkuCode().equalsIgnoreCase(skuCode)) {
                skumodel = sku;
            }
        }
        CategoryModel categorymodel = productmodel.getPrimaryHome();
        DepartmentModel deptmodel = categorymodel.getDepartment();

        // creates the minimum number of configured products to exercise
        // all of the options of all variations and all of the sales units
        // how many to make?
        // find the maximum of the # of sales units and the number of options in each varaition
        int max = product.getSalesUnits().length;
        FDVariation[] variations = product.getVariations();
        for (int i=0; i<variations.length; i++) {
            FDVariation variation = variations[i];
            max = Math.max(max, variation.getVariationOptions().length);
        }

        for (int n=0; ( (n < max) && ((!multiple && (n == 0)) || multiple)); n++) {

            // pick a sales unit
            FDSalesUnit[] units = product.getSalesUnits();
            FDSalesUnit salesUnit = units[n % units.length];
            // make a variation map
            HashMap optionMap = new HashMap();
            for (int i=0; i<variations.length; i++) {
                FDVariation variation = variations[i];
                FDVariationOption[] options = variation.getVariationOptions();
                FDVariationOption option = options[n % options.length];
                optionMap.put(variation.getName(), option.getName());
            }

            // make the configured product for this sales unit
            //
            // pick a random quantity between 5 and 10, except for...
            //
            int quantity = 5 + (int) (5.0 * Math.random());
            if (skuCode.startsWith("MEA")) {
                //
                // MEAT
                // quantity 1 -> 3 for meat
                //
                quantity = 1 + (int) (3.0 * Math.random());
            }
            else if (skuCode.startsWith("TEA") || skuCode.startsWith("COF")) {
                //
                // COFFEE & TEA
                // quantity 1
                //
                quantity = 1;
            } else if (skuCode.startsWith("SEA")) {
                //
                // SEAFOOD
                // quantity 1 -> 5
                //
                quantity = 1 + (int) (5.0 * Math.random());
            } else if (skuCode.startsWith("DEL")) {
                //
                // DELI
                // quantity 1
                //
                quantity = 1;
            } else if (skuCode.startsWith("FRU") || skuCode.startsWith("YEL")) {
                //
                // FRUIT
                // quantity 3
                //
                quantity = 3;
            } else if (skuCode.startsWith("VEG")) {
                //
                // VEGGIES
                // quantity 1 -> 5
                //
                quantity = 1 + (int) (5.0 * Math.random());
            } else if (skuCode.startsWith("CHE")) {
                //
                // CHEESE
                // quantity 1
                //
                quantity = 1;
            }
            
			FDConfiguration conf = new FDConfiguration(quantity, salesUnit.getName(), optionMap);

			FDCartLineModel cartLine = new FDCartLineModel(new FDSku(productInfo), productmodel, conf, null, user.getUserContext());

            lines.add(cartLine);
        }

        return lines;

    }

    private String createConfigurationDescription(String requestedUnit, Map configuration, SkuModel sku, FDProduct product) throws FDResourceException {
		StringBuffer confDescr = new StringBuffer();

		//
		// add sales unit description
		//
		FDSalesUnit[] units = product.getSalesUnits();
		for (int j=0; j<units.length; j++) {
			FDSalesUnit unit = units[j];
			if (unit.getName().equals(requestedUnit)) {
				String salesUnitDescr = unit.getDescription();

				// clean sales unit description
				if (salesUnitDescr!=null) {
					if (salesUnitDescr.indexOf("(") > -1) {
						salesUnitDescr = salesUnitDescr.substring(0, salesUnitDescr.indexOf("("));
					}
					// descriptions of "nm" should be ignored
					if ((!"".equalsIgnoreCase(salesUnitDescr.trim())) && (!"nm".equalsIgnoreCase(salesUnitDescr.trim()))) {
						confDescr.append(salesUnitDescr);
					}
				}

				break;
			}
		}

		//
		// add description of sku variations
		//
		List<DomainValue> varMatr = sku.getVariationMatrix();
		if (varMatr != null && varMatr.size() > 0) {
			for (Iterator varIter = varMatr.iterator(); varIter.hasNext();) {
				DomainValue dv = ((DomainValue) varIter.next());
				if (confDescr.length() > 0) confDescr.append(", ");
				confDescr.append( dv.getLabel() );
			}
		}
		List<DomainValue> variationOptions = sku.getVariationOptions();
		if (variationOptions != null && variationOptions.size()>0) {
			for (Iterator optIter = variationOptions.iterator(); optIter.hasNext();) {
				DomainValue dv = ((DomainValue) optIter.next());
				if (confDescr.length() > 0) confDescr.append(", ");
				confDescr.append( dv.getLabel() );
			}
		}


		//
		// add variation options
		//
		FDVariation[] variations = product.getVariations();
		for (int i=0; i<variations.length; i++) {
			FDVariation variation = variations[i];

			String optionName = (String)configuration.get( variation.getName() );
			if (optionName==null) continue;

			FDVariationOption[] options = variation.getVariationOptions();
			for (int j=0; j<options.length; j++) {
				if (options[j].getName().equals(optionName)) {
					String optDescr = options[j].getDescription();
					if ((!"None".equalsIgnoreCase(optDescr)) && (!"nm".equalsIgnoreCase(optDescr)) && (!"".equalsIgnoreCase(optDescr))) {
						if (confDescr.length() > 0) confDescr.append(", ");
						if (optDescr.indexOf("(") > -1) {
							confDescr.append( optDescr.substring(0, optDescr.indexOf("(")) );
						} else {
							confDescr.append( optDescr );
						}
					}
				}
			}		
		}

		return confDescr.toString();
	}


%>
