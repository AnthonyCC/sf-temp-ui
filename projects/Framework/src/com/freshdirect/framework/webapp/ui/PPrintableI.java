package com.freshdirect.framework.webapp.ui;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public interface PPrintableI {

	public void print(JspWriter out, String options) throws IOException;

}

