package com.freshdirect.mobileapi.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.webapp.util.MediaUtils;

public class Variation {

    public enum VariationType {
        MULTIPLE_CHOICE, SINGLE_CHOICE, ONLY_ONE_OPTION
    }

    private VariationType type;

    private FDVariation variation;

    private List<VariationOption> options = new ArrayList<VariationOption>();

    private boolean optional;

    private String underLabel;

    private String name;

	private String description;
    
    private String helpNote;

    public void removeUnavailableOptions() {
        for (Iterator<VariationOption> it = options.iterator(); it.hasNext();) {
            VariationOption option = (VariationOption) it.next();
            if (option.isUnAvailable()) {
                it.remove();
            }
        }
    }
    
    public static Variation wrap(FDVariation variation, Product product) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        Variation result = new Variation();
        result.variation = variation;

        FDVariationOption[] options = variation.getVariationOptions();
        Pricing defaultPricing = product.getDefaultProduct().getPricing();

        for (FDVariationOption fdOption : options) {
            VariationOption option = new VariationOption(fdOption);
            try	{
            	ProductModel pm = ContentFactory.getInstance().getProduct(fdOption.getSkuCode());
            	option.setId(pm.getContentName());
            	option.setIncludedProducts(pm.getIncludeProducts());
            } catch(Exception e){
            	
            }
            result.options.add(option);
            double cvprice = 0.0;

            CharacteristicValuePrice cvp = defaultPricing.findCharacteristicValuePrice(variation.getName(), fdOption.getName(),product.pricingContext);
            if (cvp != null) {
                cvprice = cvp.getPrice();
            }

            if (cvprice > 0.0) {
                option.setCharacteristicValuePrice(currencyFormatter.format(cvp.getPrice()) + "/" + cvp.getPricingUnit());
            } else {
                option.setCharacteristicValuePrice(" - no charge ");
            }

        }

        if (options.length == 1) {
            result.type = VariationType.ONLY_ONE_OPTION;
        } else {

            if (!"checkbox".equals(variation.getDisplayFormat())) {
                result.type = VariationType.SINGLE_CHOICE;

            } else { // "dropdown"
                result.type = VariationType.MULTIPLE_CHOICE;
            }
        }
        
        if ("checkbox".equals(variation.getDisplayFormat()) && variation.isOptional() == true)
        {
        	result.optional = false;
        }
        else
        {
        	result.optional = variation.isOptional();
        }
        
        result.underLabel = variation.getUnderLabel();
        result.name = variation.getName();
        result.description = variation.getDescription();
        result.helpNote="";
        
        
        try {
        String charFileName = "media/editorial/fd_defs/characteristics/"+variation.getName().toLowerCase()+ ".html";
    	URL url = MediaUtils.resolve( FDStoreProperties.getMediaPath(), charFileName );
    	result.helpNote = readContent(url);
        }
    	
    	catch ( Exception ex ) { 
			
			System.out.println(ex);
		} 
        

        return result;
    }
    
    
    public static String readContent(URL url) throws IOException {
        //LOG.debug("Reading content from: " + url.toString());
        Writer out = new StringWriter();
        InputStream in = null;
        in = url.openStream();

        if (in == null) {
            throw new FileNotFoundException();
        }

        byte[] buf = new byte[4096];
        int i;
        while ((i = in.read(buf)) != -1) {
            out.write(new String(buf, 0, i));
        }
        in.close();
        return out.toString();
    }

    

    public boolean isOptional() {
        return optional;
    }

    public String getUnderLabel() {
        return underLabel;
    }

    public String getName() {
        return name;
    }

    /**
     * For dropdown options (i_product_jspf)
     * @return
     */
    public String getDescription() {
        return description;
    }

    public VariationType getType() {
        return type;
    }

    public List<VariationOption> getOptions() {
        return options;
    }

    public FDVariation getVariation() {
        return variation;
    }

    public void setVariation(FDVariation variation) {
        this.variation = variation;
    }

    public void setType(VariationType type) {
        this.type = type;
    }

    public void setOptions(List<VariationOption> options) {
        this.options = options;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public void setUnderLabel(String underLabel) {
        this.underLabel = underLabel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getHelpNote() {
		return helpNote;
	}

	public void setHelpNote(String helpNote) {
		this.helpNote = helpNote;
	}

}
