package com.freshdirect.framework.webapp.ui;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

import com.freshdirect.framework.webapp.*;

public class PCheckboxGroup extends PElement {

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
			out.print("<INPUT TYPE='CHECKBOX' VALUE='");
			out.print(this.value);
			out.print("'");
			if (options!=null) {
				out.print(" ");
				out.print(options);
			}
			out.print(">");
			out.print(this.description);
		}
	}

	private final boolean required;

	private final PCheckboxGroup.Option[] options;
	private PCheckboxGroup.Option[] selected = new PCheckboxGroup.Option[0];

	public PCheckboxGroup(String name, PCheckboxGroup.Option[] options) {
		this(name, options, false);
	}

	public PCheckboxGroup(String name, PCheckboxGroup.Option[] options, boolean required) {
		super(name);
		this.options = options;
		this.required = required;
	}

	public void initialize(HttpServletRequest request) {
		String values[] = request.getParameterValues( this.getName() );
		if(values != null){
			this.selected = new PCheckboxGroup.Option[values.length];
			for (int o=values.length; --o>=0; ) {
				for (int i=this.options.length; --i>=0; ) {
					if (values[o].equals( this.options[i].getValue() )) {
						// found it
						this.selected[o] = this.options[i];
						break;	
					}
				}
			}
		}
	} 

	public void validate(ActionResult result) {
		// check required
		if (this.required && this.selected.length==0) {
			result.addError( new ActionError(this.getName(), "Required field") );
			return;
		}
		for (int i=this.selected.length; --i>=0; ) {
			if (this.selected[i]==null) {
				result.addError( new ActionError(this.getName(), "Invalid selection") );
				return;
			}
		}
	}

	public PCheckboxGroup.Option[] getSelected() {
		return this.selected;	
	}
	
	public String[] getSelectedValues() {
		String[] values = new String[this.selected.length];
		for (int i=values.length; --i>=0; ) {
			values[i]=this.selected[i].getValue();
		}
		return values;
	}

	public String[] getValues() {
		return this.getSelectedValues();
	}
	
	public void print(JspWriter out, String options) throws IOException {
		for (int i=0; i<this.options.length; i++)	{
			boolean sel = false;
			for (int o=this.selected.length; --o>=0; ) {
				if (this.selected[o]==this.options[i]) {
					sel=true;
					break;
				}
			}
			this.options[i].print(out, "NAME='"+ this.getName() +"' "+ (sel ? options+" CHECKED" : options));
		}
	}
	

}

