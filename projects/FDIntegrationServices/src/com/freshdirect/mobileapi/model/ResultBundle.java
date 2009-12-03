package com.freshdirect.mobileapi.model;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.model.tagwrapper.HttpContextWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.TagWrapper;

public class ResultBundle {

    private ActionResult actionResult;

    private Map<String, Object> extraData = new HashMap<String, Object>();

    public ResultBundle() {
    }

    public ResultBundle(ActionResult actionResult, TagWrapper tagWrapper) {
        this.actionResult = actionResult;
        tagWrapper.setParams(this);
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    public void setWrapper(HttpContextWrapper wrapper) {
        wrapper.setParams(this);
    }

    public void setActionResult(ActionResult actionResult) {
        this.actionResult = actionResult;
    }

    public Object getExtraData(String key) {
        return this.extraData.get(key);
    }

    public void addExtraData(String key, Object value) {
        this.extraData.put(key, value);
    }

}
