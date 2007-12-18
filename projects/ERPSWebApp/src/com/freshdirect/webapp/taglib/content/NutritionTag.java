package com.freshdirect.webapp.taglib.content;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.fdstore.FDResourceException;

public class NutritionTag extends com.freshdirect.framework.webapp.BodyTagSupport {
       
    private String id;
    public String getId() {
        return (this.id);
    }
    public void setId(String id) {
        this.id = id;
    }
    
    private String skuCode;
    public String getSkuCode() {
        return (this.skuCode);
    }
    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }
    
    private String sessionName = "freshdirect.content.nutrition";
    
    private ErpNutritionModel nutrition = null;
    
    public void doInitBody() throws JspException {
        
        HttpSession session = pageContext.getSession();
        
        if (skuCode != null) {
            try {
                
                nutrition = ErpFactory.getInstance().getNutrition(skuCode);
                session.setAttribute(sessionName, nutrition);
                
            } catch (FDResourceException fdre) {
                throw new JspException(fdre.getMessage());
            }
        } else {
            
            nutrition = (ErpNutritionModel) session.getAttribute(sessionName);
            
        }
        
        if (nutrition == null) {
            session.removeAttribute(sessionName);
            throw new JspException("No NutritionModel found");
        }
        
        pageContext.setAttribute(id, nutrition);
        
    }
    
    
    
}
