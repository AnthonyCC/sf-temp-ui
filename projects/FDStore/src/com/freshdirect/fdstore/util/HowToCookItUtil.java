package com.freshdirect.fdstore.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.Domain;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.view.WebHowToCookIt;

public class HowToCookItUtil {

	public static List<WebHowToCookIt> getHowToCookIt(ProductModel productNode) {
		List<WebHowToCookIt> howToCookIt = new ArrayList<WebHowToCookIt>();
		
		List<DomainValue> prodRatingValues = productNode.getRating();
		String htciCatId = null;

		List<Domain> usageList = productNode.getUsageList();
		List<CategoryModel> htciFolders = productNode.getHowtoCookitFolders();

		if (htciFolders != null && htciFolders.size() > 0 && usageList != null && usageList.size() > 0 && prodRatingValues != null && prodRatingValues.size() > 0) {
		    List<Domain> usageDomains = new ArrayList<Domain>(usageList);
                    DomainNameComparator domainNameComparator = new DomainNameComparator();
                    // Sort the list of domains using the domainNameComparator in the
                    // JspMethods class
                    Collections.sort(usageDomains, domainNameComparator);
        
                    // get a domain then check for the value being set to true in the
                    // prodRatingValues list
                    for (Iterator htciI = usageDomains.iterator(); htciI.hasNext();) {
                        Domain htciDomain = (Domain) htciI.next();
                        String prodDomainValue = null;
                        // get the matching domainvalue off the prod for this Domain.
                        prodDomainValue = getProductDomainValue(prodRatingValues, htciDomain.getContentKey().getId(), prodDomainValue);
        
                        if (prodDomainValue == null || !prodDomainValue.equalsIgnoreCase("true")) {
                            // skip this item
                            continue;
                        }
                        htciCatId = null;
                        // ok now find the htci folder that has this domain on it
                        htciCatId = getHowToCookCatId(htciFolders, htciCatId, htciDomain);
                        if (htciCatId == null) {
                            // did not find a how to cook it folder for cooking
                            // method..skip it
                            continue;
                        }
                        String linkParams = "catId=" + htciCatId + "&trk=prod";
                        WebHowToCookIt webHowToCookIt = new WebHowToCookIt(htciDomain.getLabel(), linkParams, htciCatId);
                        howToCookIt.add(webHowToCookIt);
                    }
		}
		
		return howToCookIt;
	}

        public static String getProductDomainValue(List<DomainValue> domainValues, String domainId, String defaultDomainValue) {
            for (DomainValue dmv : domainValues) {
                ContentKey dom = dmv.getDomainContentKey();
                if (domainId.equals(dom.getId())) {
                    return dmv.getValue();
                }
            }
            return defaultDomainValue;
    }

	
        private static String getHowToCookCatId(List<CategoryModel> htciFolderRefs, String htciCatId, Domain htciDomain) {
            boolean foundFolder = false;
            for (CategoryModel htciCat : htciFolderRefs) {
                if (htciCat.isHidden()) {
                    continue;
                }
                List<Domain> catRatingValues = htciCat.getRating();
                for (Domain dom : catRatingValues) {
                    if (dom != null && htciDomain.getContentName().equals(dom.getContentName())) {
                        foundFolder = true;
                        htciCatId = htciCat.getContentName();
                        break;
                    }
                }
                if (!foundFolder) {
                    continue;
                }
                // ok got one..but is it hidden
            }
            return htciCatId;
        }
	
	public static class DomainNameComparator implements Comparator {
        // handles Domains or DomainValues
        public int compare (Object obj1, Object obj2) {
            String name1 = null;
            String name2 = null;
            if(obj1 instanceof Domain) {
                name1 = ((Domain)obj1).getName();
            }
            if(obj1 instanceof DomainValue) {
                name1 = ((DomainValue)obj1).getDomain().getName();
            }

            if(obj2 instanceof Domain) {
                name2 = ((Domain)obj2).getName();
            }
            if(obj2 instanceof DomainValue) {
                name2 = ((DomainValue)obj2).getDomain().getName();
            }
            
            if (name1==null || name2==null) return 0;
            return name1.compareToIgnoreCase(name2);
        }
    }
}
