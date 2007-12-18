package com.freshdirect.webapp.taglib.content;

import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;

import com.freshdirect.content.nutrition.*;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.fdstore.FDResourceException;

public class NutritionControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport {
    
    private String feedback;
    
    private String userMessage = null;
    
    private String successPage = null;
    
    public String getUserMessage() {
        return this.userMessage;
    }
    
    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
    
    public String getSuccesssPage() {
        return this.successPage;
    }
    
    public void setSuccessPage(String sp) {
        this.successPage = sp;
    }
    
    private ErpNutritionModel nutrition;
    
    public ErpNutritionModel getNutrition() {
        return (this.nutrition);
    }
    
    public void setNutrition(ErpNutritionModel nutrition) {
        this.nutrition = nutrition;
    }
    
    public int doStartTag() throws JspException {
        //
        // get the user's session, current request, and intended action
        //
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            
            String action = request.getParameter("action");
            boolean didSomething = false;
            if ("update".equalsIgnoreCase(action)) {
                updateNutrition(request);
                didSomething = true;
            } else if ("update_components".equalsIgnoreCase(action)) {
                updateNutritionComponents(request);
                didSomething = true;
            } else if ("ingredients".equalsIgnoreCase(action)) {
                updateIngredients(request);
                didSomething = true;
            } else if ("hidden_ingredients".equalsIgnoreCase(action)) {
                updateHiddenIngredients(request);
                didSomething = true;
            }  else if ("heating".equalsIgnoreCase(action)) {
                updateHeatingInstructions(request);
                didSomething = true;
            }  else if ("kosher".equalsIgnoreCase(action)) {
                updateKosherInformation(request);
                didSomething = true;
            } else if ("claims".equalsIgnoreCase(action)) {
                updateClaims(request);
                didSomething = true;
            }
            
            if (didSomething) {
                try {
                    ErpFactory.getInstance().saveNutrition(nutrition);
                    //
                    // set the success message
                    //
                    feedback = "Nutrition Information saved";
                } catch (FDResourceException fdre) {
                    feedback = "Unable to save attributes: " + fdre.getMessage();
                }
                
                if (successPage != null) {
                    HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
                    try {
                        response.sendRedirect(response.encodeRedirectURL(successPage));
                        JspWriter writer = pageContext.getOut();
                        writer.close();
                        return SKIP_BODY;
                    } catch (java.io.IOException ioe) {
                        throw new JspException("Error redirecting " + ioe.getMessage());
                    }
                }
                
            }
            
        }
        
        return EVAL_BODY_BUFFERED;
    }
    
    private void updateNutrition(HttpServletRequest request) throws JspException {
        
        for (Iterator i = ErpNutritionType.getTypesIterator(); i.hasNext();) {
            String name = (String) i.next();
            String value = (String) request.getParameter(name);
            String uom = (String) request.getParameter(name + "_UOM");
            String lt = (String) request.getParameter(name + "_LT");
            if (value == null)
                value = "";
            if (uom == null)
                uom = "";
            if ("".equals(value) && "".equals(uom)) continue;
            if (value.equals("")) {
                value = "0";
            }
            double val = 0.0;
            try {
                val = Double.parseDouble(value);
            } catch (NumberFormatException ne) {
                if (value.indexOf("/") > -1) {
                    // it's a fraction...
                    val = Double.parseDouble(value.substring(0, value.indexOf("/"))) / Double.parseDouble(value.substring(value.indexOf("/")+1, value.length()));
                } else {
                    val = 0.0;
                }
            }
            if (lt != null) {
                val *= -1.0;
            }
            nutrition.setValueFor(name, val);
            nutrition.setUomFor(name, uom);
        }
        
    }
    
    private void updateNutritionComponents(HttpServletRequest request)
    throws JspException {
        for (Iterator i = ErpNutritionType.getTypesIterator(); i.hasNext();) {
            String name = (String) i.next();
            String checkbox = (String) request.getParameter(name);
            if ("on".equalsIgnoreCase(checkbox)
            && !nutrition.containsNutritionInfoFor(name)) {
                nutrition.setValueFor(name, 0);
                nutrition.setUomFor(name, ErpNutritionType.getType(name).getUom());
            }
            if (checkbox == null && nutrition.containsNutritionInfoFor(name)) {
                nutrition.removeNutritionInfoFor(name);
            }
        }
    }
    
    private void updateIngredients(HttpServletRequest request)
    throws JspException {
        String ingr = request.getParameter("ingredients");
        if (ingr != null) {
            nutrition.setIngredients(ingr);
        }
    }
    
    private void updateHiddenIngredients(HttpServletRequest request)
    throws JspException {
        String ingr = request.getParameter("hidden_ingredients");
        if (ingr != null) {
            nutrition.setHiddenIngredients(ingr);
        }
    }
    
    private void updateHeatingInstructions(HttpServletRequest request)
    throws JspException {
        String heat = request.getParameter("heating");
        if (heat != null) {
            nutrition.setHeatingInstructions(heat);
        }
    }
    
    private void updateKosherInformation(HttpServletRequest request)
    throws JspException {
        String ksym = request.getParameter("kosher_symbol");
        if (ksym != null) {
            nutrition.setKosherSymbol(EnumKosherSymbolValue.getValueForCode(ksym));
        }
        String ktyp = request.getParameter("kosher_type");
        if (ktyp != null) {
            nutrition.setKosherType(EnumKosherTypeValue.getValueForCode(ktyp));
        }
    }
    
    private void updateClaims(HttpServletRequest request)  throws JspException {
       
        nutrition.setClaims(collectValues(request, "claim", EnumClaimValue.getValues()));
        nutrition.setAllergens(collectValues(request, "allergen", EnumAllergenValue.getValues()));
        nutrition.setOrganicStatements(collectValues(request, "organic", EnumOrganicValue.getValues()));
        
    }
    
    private List collectValues(HttpServletRequest request, String formElementName, List values) {
        String[] formValues = request.getParameterValues(formElementName);
        List newValues = new ArrayList();
        if (formValues != null) {
            for (int i=0; i<formValues.length; i++) {
                for (Iterator enumIter = values.iterator(); enumIter.hasNext(); ) {
                    NutritionValueEnum nv = (NutritionValueEnum)enumIter.next();
                    if ((nv != null) && formValues[i].equals(nv.getCode())) {
                        newValues.add(nv);
                        break;
                    }
                }
            }
        }
        return newValues;
    }
    
}