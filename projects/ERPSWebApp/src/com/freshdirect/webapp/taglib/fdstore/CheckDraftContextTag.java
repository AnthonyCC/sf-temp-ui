package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.webapp.util.DraftUtil;

public class CheckDraftContextTag extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException {
        PageContext ctx = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
        HttpServletResponse response = (HttpServletResponse) ctx.getResponse();
        DraftUtil.draft(request, response);
    }
}
