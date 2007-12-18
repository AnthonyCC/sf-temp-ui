package com.freshdirect.fdstore.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.attributes.Attribute;
import com.freshdirect.fdstore.attributes.MultiAttribute;
import com.freshdirect.fdstore.content.*;
import com.freshdirect.fdstore.content.view.ProductRating;
import com.freshdirect.fdstore.content.view.WebProductRating;

public class RatingUtil {
	
	public static WebProductRating getRatings(ProductModel productNode) throws FDResourceException {
		
		WebProductRating webRating = null;
		List prodPageRatingNames = new ArrayList();
                List prodPageTextRatingNames = new ArrayList();
		if (productNode.getAttribute("PROD_PAGE_RATINGS")!=null) {
                    StringTokenizer stRN=new StringTokenizer((String)productNode.getAttribute("PROD_PAGE_RATINGS").getValue(),",");
                    while (stRN.hasMoreTokens()) {
                        prodPageRatingNames.add(stRN.nextToken().toLowerCase());
                    }
		}
		if (productNode.getAttribute("PROD_PAGE_TEXT_RATINGS")!=null) {
                    StringTokenizer stRN=new StringTokenizer((String)productNode.getAttribute("PROD_PAGE_TEXT_RATINGS").getValue(),",");
                    while (stRN.hasMoreTokens()) {
                        prodPageTextRatingNames.add(stRN.nextToken().toLowerCase());
                    }
		}

                MultiAttribute rating = (MultiAttribute)productNode.getAttribute("RATING");
		CategoryModel currentFolder = (CategoryModel)productNode.getParentNode();
		String rrFolderLabel= null; //"Compare "+currentFolder.getFullName();
		String rrFolderId = null;   //"catId="+currentFolder.getContentName();

		Attribute tmpAttribute = currentFolder.getAttribute("RATING_HOME");
		if (tmpAttribute!=null) {
                    ContentRef cr = (ContentRef)tmpAttribute.getValue();
                    if (cr instanceof DepartmentRef) {
                            rrFolderLabel = "Compare "+((DepartmentRef)cr).getDepartment().getFullName();
                            rrFolderId = "deptId="+cr.getRefName();
                    } else if (cr instanceof CategoryRef) {
                            rrFolderLabel = "Compare "+((CategoryRef)cr).getCategory().getFullName();
                            rrFolderId = "catId="+cr.getRefName();
                    } else rrFolderLabel = null;
		}

		tmpAttribute = currentFolder.getAttribute("RATING_GROUP_NAMES");
		StringBuffer rateNRankLinks =null;

		if (tmpAttribute !=null  && rrFolderLabel !=null) {
                    rateNRankLinks = new StringBuffer();
                    StringTokenizer stRRNames = new StringTokenizer((String)tmpAttribute.getValue(),",");
                    String rrName = stRRNames.nextToken().toUpperCase();
                    String ordrBy = "&orderBy=name";
                    // go find the attribute with that name and it's label
                    tmpAttribute = currentFolder.getAttribute(rrName);
                    if (tmpAttribute !=null) {
                        tmpAttribute = currentFolder.getAttribute(rrName);
                        List ra = (List)tmpAttribute.getValue();
                        if (ra.size() > 0) {
                            Domain raDMV = ((DomainRef)ra.get(0)).getDomain();
                            ordrBy = "&orderBy="+raDMV.getName().toLowerCase();
                        }
                    }
                    rateNRankLinks.append(rrFolderId + "&productId="+productNode+"&ratingGroupName="+rrName+ordrBy);

		}

		if (rating!=null && (prodPageRatingNames.size()>0 || prodPageTextRatingNames.size()>0) ) {
                    String ratingLabel = "";
                    if (productNode.getAttribute("RATING_PROD_NAME")!=null) {
    			ratingLabel = (String)productNode.getAttribute("RATING_PROD_NAME").getValue();
                    }
                    List webRatings = new ArrayList();
                    List webTextRatings = new ArrayList();
                
                    List ratingValues = rating.getValues();
                    for(int i = 0, size = ratingValues.size(); i < size; i++){
                    	if (!(ratingValues.get(i) instanceof DomainValueRef) ) continue;
                        DomainValueRef valueRef = (DomainValueRef)ratingValues.get(i);
						DomainValue domainValue = valueRef.getDomainValue();
    					String ratingName = valueRef.getDomain().getName();
    					String ratingValueLabel = valueRef.getDomain().getLabel();
                        
        				if (prodPageRatingNames.contains(ratingName.toLowerCase())){
                            ProductRating productRating = new ProductRating(ratingValueLabel, domainValue.getValue());
                            webRatings.add(productRating);				
                        }
                        //check to see if this is one of the text ratings
        				if (prodPageTextRatingNames.contains(ratingName.toLowerCase())){
                            ProductRating productRating = new ProductRating(ratingValueLabel, domainValue.getValue());
                            webTextRatings.add(productRating);				
	   					}
                    }
                    String sRateNRankLinks = rateNRankLinks!=null?rateNRankLinks.toString():null;
                    webRating = new WebProductRating(ratingLabel, webRatings, webTextRatings,rrFolderLabel, sRateNRankLinks);
		}
		return webRating;                                
	}
}
