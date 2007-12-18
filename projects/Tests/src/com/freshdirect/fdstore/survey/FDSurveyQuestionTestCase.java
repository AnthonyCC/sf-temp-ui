package com.freshdirect.fdstore.survey;

import com.freshdirect.fdstore.survey.FDSurveyAnswer;
import com.freshdirect.fdstore.survey.FDSurveyQuestion;

import junit.framework.TestCase;

public class FDSurveyQuestionTestCase extends TestCase {

	public FDSurveyQuestionTestCase(String arg0) {
		super(arg0);
	}

	public void testValidateOptionalSingle() {
		// setup
		FDSurveyQuestion q = new FDSurveyQuestion("q", "Question", false, false);
		q.addAnswer( new FDSurveyAnswer("foo", "Foo"));
		q.addAnswer( new FDSurveyAnswer("bar", "Bar"));
	
		// execute & verify
		assertTrue(q.isValidAnswer(new String[0]));
		assertFalse(q.isValidAnswer(new String[] { "zzz" }));
		assertFalse(q.isValidAnswer(new String[] { "foo", "foo" }));		
		assertFalse(q.isValidAnswer(new String[] { "foo", "bar" }));
		assertTrue(q.isValidAnswer(new String[] { "foo" }));
		assertTrue(q.isValidAnswer(new String[] { "bar" }));
	}

	public void testValidateOptionalMulti() {
		// setup
		FDSurveyQuestion q = new FDSurveyQuestion("q", "Question", false, true);
		q.addAnswer( new FDSurveyAnswer("foo", "Foo"));
		q.addAnswer( new FDSurveyAnswer("bar", "Bar"));

		// execute & verify
		assertTrue(q.isValidAnswer(new String[0]));
		assertFalse(q.isValidAnswer(new String[] { "zzz" }));
		assertFalse(q.isValidAnswer(new String[] { "foo", "foo" }));
		assertTrue(q.isValidAnswer(new String[] { "foo", "bar" }));
		assertTrue(q.isValidAnswer(new String[] { "foo" }));
		assertTrue(q.isValidAnswer(new String[] { "bar" }));
	}

	public void testValidateRequiredSingle() {
		// setup
		FDSurveyQuestion q = new FDSurveyQuestion("q", "Question", true, false);
		q.addAnswer( new FDSurveyAnswer("foo", "Foo"));
		q.addAnswer( new FDSurveyAnswer("bar", "Bar"));

		// execute & verify
		assertFalse(q.isValidAnswer(new String[0]));
		assertFalse(q.isValidAnswer(new String[] { "zzz" }));
		assertFalse(q.isValidAnswer(new String[] { "foo", "foo" }));
		assertFalse(q.isValidAnswer(new String[] { "foo", "bar" }));
		assertTrue(q.isValidAnswer(new String[] { "foo" }));
		assertTrue(q.isValidAnswer(new String[] { "bar" }));
	}

	public void testValidateRequiredMulti() {
		// setup
		FDSurveyQuestion q = new FDSurveyQuestion("q", "Question", true, true);
		q.addAnswer( new FDSurveyAnswer("foo", "Foo"));
		q.addAnswer( new FDSurveyAnswer("bar", "Bar"));

		// execute & verify
		assertFalse(q.isValidAnswer(new String[0]));
		assertFalse(q.isValidAnswer(new String[] { "zzz" }));
		assertFalse(q.isValidAnswer(new String[] { "foo", "foo" }));
		assertTrue(q.isValidAnswer(new String[] { "foo", "bar" }));
		assertTrue(q.isValidAnswer(new String[] { "foo" }));
		assertTrue(q.isValidAnswer(new String[] { "bar" }));
	}

}
