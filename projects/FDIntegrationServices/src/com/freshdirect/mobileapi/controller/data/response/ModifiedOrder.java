package com.freshdirect.mobileapi.controller.data.response;

import java.text.ParseException;
import java.util.Date;

/**
 * @author Rob
 *
 */
public class ModifiedOrder extends Cart {

    private Date modificationCutoffTime;

    public String getModificationCutoffTime() {
        return formatter.format(this.modificationCutoffTime);
    }

    public void setModificationCutoffTime(String modificationCutoffTime) {
        try {
            this.modificationCutoffTime = formatter.parse(modificationCutoffTime);
        } catch (ParseException e) {
            //Do nothing special.
            this.modificationCutoffTime = null;
        }
    }

    public void setModificationCutoffTime(Date modificationCutoffTime) {
        this.modificationCutoffTime = modificationCutoffTime;
    }

}
