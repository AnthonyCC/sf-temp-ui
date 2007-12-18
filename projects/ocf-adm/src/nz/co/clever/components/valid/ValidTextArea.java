package nz.co.clever.components.valid;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.TextArea;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.ValidatorException;

/**
 *  NOTE: this component is known to be broken!
 *  
 * @author Peter Butler
 */
public abstract class ValidTextArea extends TextArea {
	WriterErrorDecorator myWriter = new WriterErrorDecorator(this);
	
	public ValidTextArea() {
		super();
	}
	
	public abstract IValidator getValidator();

	public abstract String getDisplayName();
	
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		IForm form = getForm();
		IValidationDelegate delegate = form.getDelegate();

		if (delegate == null)
			throw new ApplicationRuntimeException(Tapestry.format("ValidTextArea.no-delegate", getExtendedId(), getForm().getExtendedId()), this, null, null);

		IValidator validator = getValidator();

		if (validator == null)
			throw Tapestry.createRequiredParameterException(this, "validator");

		boolean rendering = !cycle.isRewinding();
		
		IMarkupWriter superWriter;
		if (rendering) {
			delegate.writePrefix(writer, cycle, this, validator);
			myWriter.init(writer, delegate, validator, cycle);
			superWriter = myWriter;
		} else {
			superWriter = writer;
		}
		
		super.renderComponent(superWriter, cycle);

		String name = getName();

		if (!rendering && !isDisabled()) {
			String value = cycle.getRequestContext().getParameter(name);
			updateValue(value);
		}

		if (rendering) {
			delegate.writeSuffix(writer, cycle, this, validator);
			myWriter.clear();
		}
	}
	
	protected void updateValue(String value) {
		Object objectValue = null;
		IValidationDelegate delegate = getForm().getDelegate();

		delegate.recordFieldInputValue(value);

		try {
			objectValue = getValidator().toObject(this, value);
		} catch (ValidatorException ex) {
			delegate.record(ex);
			return;
		}

		setValue((String)objectValue);
	}	
}
