package com.freshdirect.webapp.ajax.expresscheckout.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;

import com.freshdirect.Fixture;
import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.Constraint;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;

public class ConstraintValidatorTest extends Fixture {

	private static final String ERROR_MESSAGE = "error message";
	private static final String VALUE = "value";
	private static final String KEY = "key";
	private static final String OTHER_KEY = "otherkey";
	private static final String OTHER_VALUE = "othervalue";

	private Validator validator;
	private Constraint<String> constraint;

	@SuppressWarnings("unchecked")
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		constraint = context.mock(Constraint.class);

		validator = ConstraintValidator.defaultValidator();
	}

	public void testValidateEmptyDataWithEmptyConstraintReturnSuccess() {
		final Map<String, String> datas = createValidatedDatas();
		final Map<String, Constraint<String>> constraints = createConstraints();

		List<ValidationError> validationErrors = validator.validate(datas, constraints);

		assertTrue(validationErrors.isEmpty());
	}

	public void testValidateDataWithEmptyConstraintReturnSuccess() {
		final Map<String, String> datas = createValidatedDatas();
		datas.put(KEY, VALUE);
		final Map<String, Constraint<String>> constraints = createConstraints();

		List<ValidationError> validationErrors = validator.validate(datas, constraints);

		assertTrue(validationErrors.isEmpty());
	}

	public void testValidateEmptyDataWithMultiConstraintReturnSuccess() {
		final Map<String, String> datas = createValidatedDatas();
		final Map<String, Constraint<String>> constraints = createConstraints();
		constraints.put(KEY, constraint);

		List<ValidationError> validationErrors = validator.validate(datas, constraints);

		assertTrue(validationErrors.isEmpty());
	}

	public void testValidateValidDataWithMatchingConstraintReturnErrorCollectionEmpty() {
		final Map<String, String> datas = createValidatedDatas();
		datas.put(KEY, VALUE);

		final Map<String, Constraint<String>> constraints = createConstraints();
		constraints.put(KEY, constraint);

		expectConstraintIsValidMethod(VALUE, true);

		List<ValidationError> validationErrors = validator.validate(datas, constraints);

		assertTrue(validationErrors.isEmpty());
	}

	public void testValidateNotValidDataWithMatchingConstraintReturnErrorMessage() {
		final Map<String, String> datas = createValidatedDatas();
		datas.put(KEY, VALUE);

		final Map<String, Constraint<String>> constraints = createConstraints();
		constraints.put(KEY, constraint);

		expectConstraintIsValidMethod(VALUE, false);
		expectConstraintGetErrorMessageMethod(ERROR_MESSAGE);

		List<ValidationError> validationErrors = validator.validate(datas, constraints);

		assertEquals(1, validationErrors.size());
		assertEquals(ERROR_MESSAGE, validationErrors.get(0).getError());
		assertEquals(KEY, validationErrors.get(0).getName());
	}

	public void testValidateValidAndNotValidDataWithMatchingConstraintReturnErrorMessage() {
		final Map<String, String> datas = createValidatedDatas();
		datas.put(KEY, VALUE);
		datas.put(OTHER_KEY, OTHER_VALUE);

		final Map<String, Constraint<String>> constraints = createConstraints();
		constraints.put(KEY, constraint);
		constraints.put(OTHER_KEY, constraint);

		expectConstraintIsValidMethod(VALUE, true);
		expectConstraintIsValidMethod(OTHER_VALUE, false);
		expectConstraintGetErrorMessageMethod(ERROR_MESSAGE);

		List<ValidationError> validationErrors = validator.validate(datas, constraints);

		assertEquals(1, validationErrors.size());
		assertEquals(ERROR_MESSAGE, validationErrors.get(0).getError());
		assertEquals(OTHER_KEY, validationErrors.get(0).getName());
	}

	private Map<String, Constraint<String>> createConstraints() {
		return new HashMap<String, Constraint<String>>();
	}

	private Map<String, String> createValidatedDatas() {
		return new HashMap<String, String>();
	}

	private void expectConstraintIsValidMethod(final String validatedValue, final boolean returnValue) {
		expectConstraintIsValidMethod(validatedValue, returnValue, 1);
	}

	private void expectConstraintIsValidMethod(final String validatedValue, final boolean returnValue, final int count) {
		context.checking(new Expectations() {
			{
				exactly(count).of(constraint).isValid(validatedValue);
				will(returnValue(returnValue));
			}
		});
	}

	private void expectConstraintGetErrorMessageMethod(final String errorMessage) {
		expectConstraintGetErrorMessageMethod(errorMessage, 1);
	}

	private void expectConstraintGetErrorMessageMethod(final String errorMessage, final int count) {
		context.checking(new Expectations() {
			{
				exactly(count).of(constraint).getErrorMessage();
				will(returnValue(errorMessage));
			}
		});
	}
}
