package com.freshdirect;

import org.jmock.Mockery;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.lib.legacy.ClassImposteriser;

public class Fixture extends MockObjectTestCase {

	protected Mockery context;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context = new Mockery() {
			{
				setImposteriser(ClassImposteriser.INSTANCE);
			}
		};
	}
}
