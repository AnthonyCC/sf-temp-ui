package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.GetGSProductsTag;
import com.freshdirect.webapp.taglib.fdstore.QuickShopControllerTag;

/**
 * Wrapper for {@see com.freshdirect.webapp.taglib.fdstore.QuickShopControllerTag} 
 * @author fgarcia
 *
 */
public class GetGSProductsTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName, SessionParamName {
    public final static String SKU_LIST_ID = "skuListId";
    public final static String PRODUCT_LIST_ID = "productListId";
    public GetGSProductsTagWrapper(SessionUser user) {
        super(new GetGSProductsTag(), user);
    }

    public ResultBundle getGSSKUList(String grpId, String version) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_USER },new String[]{}); //gets,sets
        addExpectedRequestValues(new String[] {REQ_PARAM_SKU_CODE, SKU_LIST_ID}, new String[] { REQ_PARAM_GRP_QTY, REQ_PARAM_GRP_TOTAL_PRICE,
        		REQ_PARAM_GRP_SHORT_DESC,REQ_PARAM_GRP_LONG_DESC, SKU_LIST_ID, PRODUCT_LIST_ID });//gets,sets
        
        ((GetGSProductsTag) wrapTarget).setGroupId(grpId);
        ((GetGSProductsTag) wrapTarget).setVersion(version);
        ((GetGSProductsTag) wrapTarget).setProductList(PRODUCT_LIST_ID);
        ((GetGSProductsTag) wrapTarget).setSkuModelList(SKU_LIST_ID);
        

        ResultBundle result = new ResultBundle(executeTagLogic(), this);

        result.addExtraData(SKU_LIST_ID, getResult());

        return result;
    }

    @Override
    protected void setResult() {
        // Nothing
    }

    @Override
    protected Object getResult() throws FDException {
        return this.pageContext.getAttribute(SKU_LIST_ID);
    }
}
