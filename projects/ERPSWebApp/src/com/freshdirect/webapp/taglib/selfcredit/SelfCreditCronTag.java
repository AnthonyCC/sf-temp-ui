package com.freshdirect.webapp.taglib.selfcredit;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.dataloader.payment.AutoCreditApprovalCron;

public class SelfCreditCronTag extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        try {
            AutoCreditApprovalCron.main(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
