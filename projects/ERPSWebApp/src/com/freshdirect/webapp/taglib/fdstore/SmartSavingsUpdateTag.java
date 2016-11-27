package com.freshdirect.webapp.taglib.fdstore;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.PromoVariantHelper;
import com.freshdirect.framework.webapp.BodyTagSupport;

public class SmartSavingsUpdateTag extends BodyTagSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    
    boolean promoConflictMode = false;
    
    boolean justCheckSavingVariantId = false;
    
    public void setPromoConflictMode(boolean promoConflictMode) {
        this.promoConflictMode = promoConflictMode;
    }
    
    public void setJustCheckSavingVariantId(boolean flag) {
        this.justCheckSavingVariantId = flag;
    }
    
    
    @Override
    public int doStartTag() throws JspException {
        HttpSession session = pageContext.getSession();
        FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
        ServletRequest request = pageContext.getRequest();
        
        if (user == null) {
            return EVAL_BODY_BUFFERED;
        }
        
        Map savingsLookupTable = (Map) session.getAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE);
        if(savingsLookupTable == null){
            savingsLookupTable = new HashMap();
        }
        PromoVariantHelper.updateSavingsVariant(user, savingsLookupTable);
        String usrVariant = user.getSavingsVariantId();
        if(usrVariant != null && usrVariant.length() > 0) {
            PromoVariantHelper.updateSavingsVariantFound(user, 5, request);
        } else {
            user.setSavingsVariantFound(false);
        }
        session.setAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE, savingsLookupTable);
        
        
        pageContext.setAttribute("smartSavingVariantId", usrVariant);
        
        if (justCheckSavingVariantId) {
            // we dont want the confusing calculations to happen ...
            
            return EVAL_BODY_BUFFERED;
        }
        
        String savingsVariant =  (String) session.getAttribute(SessionName.PREV_SAVINGS_VARIANT);
        Boolean prevVariantFound = (Boolean) session.getAttribute(SessionName.PREV_VARIANT_FOUND);
        boolean usrVariantFound = user.isSavingsVariantFound();
        if((usrVariant != null && !usrVariant.equals(savingsVariant)) || (prevVariantFound != null && usrVariantFound != prevVariantFound.booleanValue())) {
            if (promoConflictMode) {
                //This flag need to be enabled to perform post conflict resolution.
                user.setPostPromoConflictEnabled(true);
            }
            //If current savings variant is different from previous savings variant
            user.updateUserState();
            session.setAttribute(SessionName.PREV_SAVINGS_VARIANT, usrVariant);
            session.setAttribute(SessionName.PREV_VARIANT_FOUND, Boolean.valueOf(usrVariantFound));
        }

        return EVAL_BODY_BUFFERED;
    }
}
