package com.freshdirect.framework.webapp.ui;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;

public class PTextArea extends PTextField {

	public PTextArea(String name) {
		super(name);
	}

	public PTextArea(String name, boolean required) {
		super(name, required);
	}

	public PTextArea(String name, boolean required, int maxLength) {
		super(name, required, maxLength);
	}

	public void print(JspWriter out, String options) throws IOException {
		
		out.print("<TEXTAREA NAME='");
		out.print(this.getName());
		out.print("'");
		if (options!=null) {
			out.print(" ");
			out.print(options);	
		}
		out.print(">");
		if (this.value!=null) {
			out.print(this.value);
		}
		out.print("</TEXTAREA>");

	}

}