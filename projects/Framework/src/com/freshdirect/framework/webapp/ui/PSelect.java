package com.freshdirect.framework.webapp.ui;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.framework.webapp.*;

public class PSelect extends PElement {

	public static class Option implements PPrintableI {
		private final String value;
		private final String description;
		public Option(String value, String description) {
			this.value = value;
			this.description = description;
		}
		public String getValue() {
			return this.value;	
		}
		public String getDescription() {
			return this.description;	
		}
		public void print(JspWriter out, String options) throws IOException {
			out.print("<OPTION VALUE='");
			out.print(this.value);
			out.print("'");
			if (options!=null) {
				out.print(" ");
				out.print(options);
			}
			out.print(">");
			out.print(this.description);
			out.println("</OPTION>");
		}
	}

	private final PSelect.Option[] options;
	private final boolean required;

	private PSelect.Option selected;

	public PSelect(String name, PSelect.Option[] options) {
		this(name, options, false);
	}

	public PSelect(String name, PSelect.Option[] options, boolean required) {
		super(name);
		this.options = options;
		this.required = required;
	}

	public void initialize(HttpServletRequest request) {
		String val = request.getParameter( this.getName() );
		if (val!=null) {
			for (int i=this.options.length; --i>=0; ) {
				if (val.equals( this.options[i].getValue() )) {
					// found it
					this.selected = this.options[i];
					break;	
				}
			}
		}
	}

	public void validate(ActionResult result) {
		// check required
		if (this.required && (this.selected==null || this.selected.getValue().length()==0)) {
			result.addError( new ActionError(this.getName(), "Required field") );
			return;
		}
	}

	public PSelect.Option getSelected() {
		return this.selected;	
	}
	
	public String getSelectedValue() {
		return this.selected==null ? null : this.selected.getValue();
	}
	
	public String[] getValues() {
		return this.selected==null ? new String[0] : new String[] { this.selected.getValue() };
	}

	public void print(JspWriter out, String options) throws IOException {
		out.print("<SELECT NAME='");
		out.print(this.getName());
		out.print("'");
		if (options!=null) {
			out.print(" ");
			out.print(options);
		}
		out.println(">");
		for (int i=0; i<this.options.length; i++) {
			this.options[i].print(out, (this.selected==this.options[i]) ? "SELECTED" : null);
		}
		out.println("</select>");
	}

}

