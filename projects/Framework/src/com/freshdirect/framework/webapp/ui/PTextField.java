package com.freshdirect.framework.webapp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

import com.freshdirect.framework.webapp.*;


public class PTextField extends PElement {

	protected final int maxLength;
	protected final boolean required;

	protected String value;

	public PTextField(String name) {
		this(name, false);
	}

	public PTextField(String name, boolean required) {
		this(name, required, 0);
	}

	public PTextField(String name, boolean required, int maxLength) {
		super(name);
		this.required = required;
		this.maxLength = maxLength;
	}

	public void initialize(HttpServletRequest request) {
		this.value = request.getParameter(this.getName());
	}

	public void validate(ActionResult result) {
		// check required
		if (this.required && (this.value==null || this.value.length()==0)) {
			result.addError( new ActionError(this.getName(), "Required field") );
			return;
		}
		// check max length
		if (this.maxLength!=0 && this.value!=null && this.value.length()>this.maxLength) {
			result.addError( new ActionError(this.getName(), "Field too long") );
			return;
		}
	}

	public String getValue() {
		return this.value;
	}

	public String[] getValues() {
		return new String[] { this.value };
	}

	public void print(JspWriter out, String options) throws IOException {
		out.print("<INPUT TYPE='text' NAME='");
		out.print(this.getName());
		out.print("'");
		if (this.maxLength!=0) {
			out.print(" MAXLENGTH='");
			out.print(this.maxLength);
			out.print("'");
		}
		if (this.value!=null) {
			out.print("	VALUE='");
			out.print(this.value);
			out.print("'");
		}
		if (options!=null) {
			out.print(" ");
			out.print(options);	
		}
		out.print(">");
	}

}

