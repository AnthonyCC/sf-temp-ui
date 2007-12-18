package nz.co.clever.components.valid;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.NestedMarkupWriter;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.IValidator;

/**
 * @author Peter Butler
 */
public class WriterErrorDecorator implements IMarkupWriter {
	private IMarkupWriter actual;
	private IValidationDelegate delegate;
	private IValidator validator;
	private IRequestCycle cycle;
	private IFormComponent field;

	public WriterErrorDecorator(IFormComponent field) {
		this.field = field;
	}
	
	public void init(IMarkupWriter actual, IValidationDelegate delegate, IValidator validator, IRequestCycle cycle) {
		this.actual = actual;
		this.delegate = delegate;
		this.validator = validator;
		this.cycle = cycle;
	}
	
	public void clear() {
		this.actual = null;
		this.delegate = null;
		this.validator = null;
		this.cycle = null;
	}

	public void attribute(String name, int value) {
		actual.attribute(name, value);
	}

	public void attribute(String name, boolean value) {
		actual.attribute(name, value);
	}

	public void attribute(String name, String value) {
		actual.attribute(name, value);
	}

	public void attributeRaw(String name, String value) {
		actual.attributeRaw(name, value);
	}

	public void begin(String name) {
		actual.begin(name);
		validator.renderValidatorContribution(field, this, cycle);
		delegate.writeAttributes(this, cycle, field, validator);
	}

	public void beginEmpty(String name) {
		actual.beginEmpty(name);
		validator.renderValidatorContribution(field, this, cycle);
		delegate.writeAttributes(this, cycle, field, validator);
	}

	public boolean checkError() {
		return actual.checkError();
	}

	public void close() {
		actual.close();
	}

	public void closeTag() {
		actual.closeTag();
	}

	public void comment(String value) {
		actual.comment(value);
	}

	public void end() {
		actual.end();
	}

	public void end(String name) {
		actual.end(name);
	}

	public void flush() {
		actual.flush();
	}

	public void print(char[] data, int offset, int length) {
		actual.print(data, offset, length);
	}

	public void print(char value) {
		actual.print(value);
	}

	public void print(int value) {
		actual.print(value);
	}

	public void print(String value) {
		actual.print(value);
	}

	public void println() {
		actual.println();
	}

	public void printRaw(char[] buffer, int offset, int length) {
		actual.printRaw(buffer, offset, length);
	}

	public void printRaw(String value) {
		actual.printRaw(value);
	}

	public String getContentType() {
		return actual.getContentType();
	}

	public NestedMarkupWriter getNestedWriter() {
		return actual.getNestedWriter();
	}

	public void print(String value, boolean raw) {
		actual.print(value, raw);
	}

	public void print(char[] data, int offset, int length, boolean raw) {
		actual.print(data, offset, length, raw);
	}
}
