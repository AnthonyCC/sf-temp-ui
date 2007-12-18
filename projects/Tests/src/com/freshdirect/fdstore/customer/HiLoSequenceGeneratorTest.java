package com.freshdirect.fdstore.customer;

import java.util.HashSet;
import java.util.Set;

import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.MethodPatternPointcut;
import org.mockejb.interceptor.Pointcut;

import com.freshdirect.fdstore.FDResourceException;

public class HiLoSequenceGeneratorTest extends FDCustomerManagerTestSupport {

	private int sequenceVar = 0;
	
	private HiLoGenerator ID_GENERATOR = new HiLoGenerator("CUST","FAKER");
	
	public HiLoSequenceGeneratorTest(String name) {
		super(name);
	}

	protected String[] getAffectedTables() {
		return null;
	}

	protected String getSchema() {
		return null;
	}

	public void setUp() throws Exception {
		super.setUp();
		// stub out DB sequence generator
		aspectSystem.add(new GetNextIdStub());		
	}
	
	public void testUniqueNewId() throws FDResourceException {
		Set ids = new HashSet();
		
		for (int i=0; i<100000; ++i) {
			String id = ID_GENERATOR.getNextId();
			// ensure key-length is as expected
			assertEquals(sequenceVar / 10 + 5, id.length());
			// ensure key is unique
			assertTrue(ids.add(id));
		}
		// there should have been exactly 10 calls to getNextId
		assertEquals(10, sequenceVar);
	}
	
	public class GetNextIdStub implements Aspect {
		
		public Pointcut getPointcut() {
			return new MethodPatternPointcut("FDCustomerManagerSessionBean\\.getNextId");
		}

		public void intercept(InvocationContext invocationContext) throws Exception {
			sequenceVar++;
			System.out.println("GetNextIdStub.intercept() "+sequenceVar);
			invocationContext.setReturnObject(String.valueOf(sequenceVar));
		}
		
	}
}
