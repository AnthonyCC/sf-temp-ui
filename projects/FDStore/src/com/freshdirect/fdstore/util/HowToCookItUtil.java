package com.freshdirect.fdstore.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.fdstore.attributes.Attribute;
import com.freshdirect.fdstore.attributes.MultiAttribute;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategoryRef;
import com.freshdirect.fdstore.content.Domain;
import com.freshdirect.fdstore.content.DomainRef;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.DomainValueRef;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.view.WebHowToCookIt;

public class HowToCookItUtil {

	public static List getHowToCookIt(ProductModel productNode) {
		List howToCookIt = new ArrayList();
		MultiAttribute prodMAttrib = (MultiAttribute) productNode.getAttribute("RATING");

		List prodRatingValues = null;
		List htciFolderRefs = null;
		String htciCatId = null;

		if (prodMAttrib != null) {
			prodRatingValues = (List) prodMAttrib.getValues();
		} else {
			prodRatingValues = new ArrayList();
		}

		Attribute usageList = productNode.getAttribute("USAGE_LIST");
		Attribute htciFolders = productNode.getAttribute("HOWTOCOOKIT_FOLDERS");
		if (htciFolders != null) {
			htciFolderRefs = (List) htciFolders.getValue();
		}

		if (htciFolderRefs != null && usageList != null && prodRatingValues.size() > 0) {
			List usageDomains = new ArrayList((List) usageList.getValue());
			if (usageDomains != null && usageDomains.size() > 0) {
				DomainNameComparator domainNameComparator = new DomainNameComparator();
				//Sort the list of domains using the domainNameComparator in the JspMethods class
				Collections.sort(usageDomains,domainNameComparator);

				// get a domain then check for the value being set to true in the prodRatingValues list
				for (Iterator htciI = usageDomains.iterator(); htciI.hasNext();) {
					Domain htciDomain = ((DomainRef) htciI.next()).getDomain();
					String prodDomainValue = null;
					// get the matching domainvalue off the prod for this Domain.
					prodDomainValue = getProductDomainValue(prodRatingValues, htciDomain, prodDomainValue);

					if (prodDomainValue == null || !prodDomainValue.equalsIgnoreCase("true")) {
						//skip this item
						continue;
					}
					htciCatId = null;
					//ok now find the htci folder that has this domain on it
					htciCatId = getHowToCookCatId(htciFolderRefs, htciCatId, htciDomain);
					if (htciCatId == null) {
						// did not find a how to cook it folder for cooking method..skip it
						continue;
					}
					String linkParams = "catId="+htciCatId+"&trk=prod";
					WebHowToCookIt webHowToCookIt = new WebHowToCookIt(htciDomain.getLabel(), linkParams);
					howToCookIt.add(webHowToCookIt);
				}
			}
		}
		
		return howToCookIt;
	}

	private static String getProductDomainValue(List prodRatingValues, Domain htciDomain, String prodDomainValue) {
		for (Iterator dvItr = prodRatingValues.iterator(); dvItr.hasNext();) {
			Object obj = dvItr.next();
			if (!(obj instanceof DomainValueRef)) {
				continue;
			}
			DomainValue dmv = ((DomainValueRef) obj).getDomainValue();
			//dvItr.next();
			Domain dom = dmv.getDomain();
			if (htciDomain.getContentName().equals(dom.getContentName())) {
				prodDomainValue = dmv.getValue();
				break;
			}
		}
		return prodDomainValue;
	}

	private static String getHowToCookCatId(List htciFolderRefs, String htciCatId, Domain htciDomain) {
		boolean foundFolder = false;
		for (Iterator htciFI = htciFolderRefs.iterator(); htciFI.hasNext();) {
			CategoryModel htciCat = ((CategoryRef) htciFI.next()).getCategory();
			if (htciCat.isHidden()) {
				continue;
			}
			MultiAttribute catMAttrib = (MultiAttribute) htciCat.getAttribute("RATING");
			if (catMAttrib == null) {
				continue;
			}
			List catRatingValues = (List) catMAttrib.getValues();
			for (Iterator dItr = catRatingValues.iterator(); dItr.hasNext();) {
				Object dObj = dItr.next();
				if (dObj == null || !(dObj instanceof DomainRef)) {
					continue;
				}
				Domain dom = ((DomainRef) dObj).getDomain();
				if (dom != null && htciDomain.getContentName().equals(dom.getContentName())) {
					foundFolder = true;
					htciCatId = htciCat.getContentName();
					break;
				}
			}
			if (!foundFolder) {
				continue;
			}
			//ok got one..but is it hidden
		}
		return htciCatId;
	}
	
	public static class DomainNameComparator implements Comparator {
        // handles Domains or DomainRefs, DomainValue and DomainValueRefs
        public int compare (Object obj1, Object obj2) {
            String name1 = null;
            String name2 = null;
            if(obj1 instanceof Domain) {
                name1 = ((Domain)obj1).getName();
            }
            if(obj1 instanceof DomainRef) {
                name1 = ((DomainRef)obj1).getDomainName();
            }
            if(obj1 instanceof DomainValue) {
                name1 = ((DomainValue)obj1).getDomain().getName();
            }
            if(obj1 instanceof DomainValueRef) {
                name1 = ((DomainValueRef)obj1).getDomainName();
            }

            if(obj2 instanceof Domain) {
                name2 = ((Domain)obj1).getName();
            }
            if(obj2 instanceof DomainRef) {
                name2 = ((DomainRef)obj2).getDomainName();
            }
            if(obj2 instanceof DomainValue) {
                name2 = ((DomainValue)obj2).getDomain().getName();
            }
            if(obj2 instanceof DomainValueRef) {
                name2 = ((DomainValueRef)obj2).getDomainName();
            }
            
            if (name1==null || name2==null) return 0;
            return name1.compareToIgnoreCase(name2);
        }
    }
}
