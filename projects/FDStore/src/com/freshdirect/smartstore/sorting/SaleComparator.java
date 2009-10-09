package com.freshdirect.smartstore.sorting;

import java.util.List;

import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;

/**
 * Sale Sort Comparator
 *   based on Popularity comparator
 * [APPREQ-234]
 * 
 * @author segabor
 */
public class SaleComparator extends PopularityComparator {
    SaleComparator(boolean inverse, boolean hideUnavailable, List<ContentNodeModel> products) {
        super(inverse, hideUnavailable, products);
    }

    public SaleComparator(boolean inverse, List<ContentNodeModel> products) {
        super(inverse, products);
    }

    public int compare(ProductModel c1, ProductModel c2) {
        try {
            // Stage 0 -- sort out non-display cases
            //
            {
                boolean h1 = isDisplayable(c1.getContentKey()) && c1.getDefaultSku()!=null;
                boolean h2 = isDisplayable(c2.getContentKey()) && c2.getDefaultSku()!=null;
                if (!h1 && h2) {
                    return 1;
                }
    
                if (h1 && !h2) {
                    return -1;
                }
                if (!h1 && !h2) {
                	// unavailable products sorted by popularity ...
                	return super.compare(c1, c2);
                }
            }

            FDProductInfo i1 = c1.getDefaultSku().getProductInfo();
            FDProductInfo i2 = c2.getDefaultSku().getProductInfo();

            // Stage 1 -- Compare highest deal percentages
            //

            int p1 = i1.getHighestDealPercentage();
            int p2 = i2.getHighestDealPercentage();

            // greater percentage is better
            int sc = p1 > p2 ? -1 : (p1 < p2 ? 1 : 0);

            // same prices -> sort by popularity
            if (sc != 0)
            	return inverse ? -sc : sc;

            // Stage 2 -- base prices
            //

            double defp1 = i1.getDefaultPrice();
            double defp2 = i2.getDefaultPrice();

            if (defp1 != defp2) {

                // cheaper price is better
                sc = Double.compare(defp1, defp2);
                if (sc == 0)
                    return super.compare(c1, c2);
                return inverse ? -sc : sc;
            }

            // Stage 4 -- equal prices --> compare by popularity
            //
            return super.compare(c1, c2);
        } catch (FDSkuNotFoundException noSkuExc) {
            return 0;
        } catch (FDResourceException se) {
            return 0;
        }
    }
}
