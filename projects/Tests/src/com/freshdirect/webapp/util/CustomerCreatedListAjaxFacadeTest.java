package com.freshdirect.webapp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.AssertionFailedError;

import org.easymock.MockControl;
import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.MethodPatternPointcut;
import org.mockejb.interceptor.Pointcut;

import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.customer.FDCustomerManagerTestSupport;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerListInfo;
import com.freshdirect.fdstore.lists.FDCustomerListExistsException;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.CustomerCreatedListAjaxFacade.CustomerListNames;
import com.freshdirect.webapp.util.CustomerCreatedListAjaxFacade.NameEmpty;
import com.freshdirect.webapp.util.CustomerCreatedListAjaxFacade.NameExists;
import com.mockrunner.mock.web.MockHttpSession;

/**
 * Test case for the availability of ConfiguredProducts.
 */
public class CustomerCreatedListAjaxFacadeTest extends FDCustomerManagerTestSupport {
	
	private static final Date MOD_DATE_1 = 	new GregorianCalendar(2004, 06, 27, 0, 0, 0).getTime();
	private static final Date MOD_DATE_2 = 	new GregorianCalendar(2004, 06, 28, 0, 0, 0).getTime();

	public CustomerCreatedListAjaxFacadeTest(String name) {
		super(name);
	}

	protected MockHttpSession session = null;
	protected MockControl userC = null;
	protected FDUserI user = null;
	
	// Dynamic stub control fields
	protected Exception throwException = null;
	protected Object returnObj;

	
	protected CustomerCreatedListAjaxFacade facade = null;
	
	protected List stubLog = null;
	protected Aspect createList = null;
	protected Aspect deleteList = null;
	protected Aspect getListInfos = null;
	protected Aspect getSOListInfos = null;
	protected Aspect getCurrSO = null;
	protected Aspect renameList = null;
	protected Aspect getList = null;
	protected Aspect storeList = null;
	
	public void setUp() throws Exception {
		super.setUp();
		// Let's get a session
		session = new MockHttpSession();
		
		// Let's get a FDUserI
		userC = MockControl.createControl(FDUserI.class);
		user = (FDUserI) userC.getMock();
		
		session.setAttribute(SessionName.USER, user);
		
		// Set up logging of stub calls
		stubLog = new ArrayList();
		// Set up SessionBean stub
		createList = new CreateListStub();
		aspectSystem.add(createList);
		
		deleteList = new DeleteListStub();
		aspectSystem.add(deleteList);
		
		getListInfos = new GetListInfosStub();
		aspectSystem.add(getListInfos);

		getSOListInfos = new GetSOListInfosStub();
		aspectSystem.add(getSOListInfos);

		getCurrSO = new GetCurrentStandingOrderStub();
		aspectSystem.add(getCurrSO);

		renameList = new RenameListStub();
		aspectSystem.add(renameList);

		getList = new GetListStub();
		aspectSystem.add(getList);

		storeList = new StoreListStub();
		aspectSystem.add(storeList);

		// Create our tested object
		facade = new CustomerCreatedListAjaxFacade();
	}
	
	/**
	 * In response to JIRA CCL-85 (Resolved by CR-5881).
	 */
	public void testCreateAndRenameListCalledRecipes() throws Exception {
		
		// CREATE LIST CALLED "Recipes"
		//		 Set up FDUserI mock object
		user.getLevel();
		userC.setReturnValue(FDUserI.RECOGNIZED);
		user.getIdentity();
		userC.setReturnValue((FDIdentity)null); // Invalid value, but our createList stub catches it anyways 
		user.invalidateCache();
		userC.replay();
				
		facade.createList(session, "Recipes");		
		assertLatestStubCall(createList, new Object[] {(FDIdentity)null, "Recipes"});
		
		// RENAME "Recipes" to "Recipes2"
		resetState();
		user.getLevel();
		userC.setReturnValue(FDUserI.SIGNED_IN);
		user.getIdentity();
		userC.setReturnValue((FDIdentity)null); // Invalid value, but our createList stub catches it anyways 
		user.invalidateCache();
		userC.replay();
				
		facade.renameList(session, "Recipes", "Recipes2");		
		assertLatestStubCall(renameList, new Object[] {(FDIdentity)null, "Recipes", "Recipes2"});
		
		// RENAME "Recipes2" to "Recipes"
		resetState();
		user.getLevel();
		userC.setReturnValue(FDUserI.SIGNED_IN);
		user.getIdentity();
		userC.setReturnValue((FDIdentity)null); // Invalid value, but our createList stub catches it anyways 
		user.invalidateCache();
		userC.replay();
				
		facade.renameList(session, "Recipes2", "Recipes");		
		assertLatestStubCall(renameList, new Object[] {(FDIdentity)null, "Recipes2", "Recipes"});
		
	}
	
	public void testCreateList() throws Exception {
		// Set up FDUserI mock object
		user.getLevel();
		userC.setReturnValue(FDUserI.RECOGNIZED);
		user.getIdentity();
		userC.setReturnValue((FDIdentity)null); // Invalid value, but our createList stub catches it anyways 
		user.invalidateCache();
		userC.replay();
				
		facade.createList(session, "list1");		
		assertLatestStubCall(createList, new Object[] {(FDIdentity)null, "list1"});
		
		resetState(); // Reset mock state
		
		user.getLevel();
		userC.setReturnValue(FDUserI.RECOGNIZED);
		userC.replay();
		
		try {
			facade.createList(session, "      ");
			fail();
		} catch (NameEmpty e) {
			// Pass
		}
		assertEquals(0, stubLog.size());	
		
		resetState(); // Reset mock state
		
		user.getLevel();
		userC.setReturnValue(FDUserI.RECOGNIZED);
		user.getIdentity();
		userC.setReturnValue((FDIdentity)null); // Invalid value, but our createList stub catches it anyways 
		user.invalidateCache();
		userC.replay();
		throwException = new FDCustomerListExistsException();
		
		try {
			facade.createList(session, "list1");
			fail();
		} catch (NameExists e) {
			// Pass
		}
		assertLatestStubCall(createList, new Object[] {(FDIdentity)null, "list1"});		
	}
	
	public void testDeleteList() throws Exception {
		// Set up FDUserI mock object
		user.getLevel();
		userC.setReturnValue(FDUserI.SIGNED_IN);
		user.getIdentity();
		userC.setReturnValue((FDIdentity)null); // Invalid value, but our createList stub catches it anyways 
		user.invalidateCache();
		userC.replay();
				
		facade.deleteList(session, "list1");		
		assertLatestStubCall(deleteList, new Object[] {(FDIdentity)null, "list1"});
		
		resetState(); // Reset mock state
		
		// Set up FDUserI mock object
		user.getLevel();
		userC.setReturnValue(FDUserI.SIGNED_IN);
		user.getIdentity();
		userC.setReturnValue((FDIdentity)null); // Invalid value, but our createList stub catches it anyways 
		user.invalidateCache();
		userC.replay();
				
		facade.deleteList(session, "    ");		
		assertLatestStubCall(deleteList, new Object[] {(FDIdentity)null, "    "});
	}

	public void testGetListNames() throws Exception {
		// Set up FDUserI mock object
		user.getLevel();
		userC.setReturnValue(FDUserI.RECOGNIZED);
		user.invalidateCache();
				
		
		List<FDCustomerListInfo> lists = new ArrayList<FDCustomerListInfo>();
		FDCustomerListInfo ccl = new FDCustomerListInfo();
		ccl.setName("list1");
		ccl.setType(EnumCustomerListType.CC_LIST);
		lists.add(ccl);
		
		ccl = new FDCustomerListInfo();
		ccl.setName("list2");
		ccl.setType(EnumCustomerListType.CC_LIST);
		lists.add(ccl);
		
		user.getCustomerCreatedListInfos();
		userC.setReturnValue(lists);
		userC.replay();
		
		String[] result = facade.getListNames(session);	
		assertEquals(2, result.length);
		assertEquals("list1", result[0]);
		assertEquals("list2", result[1]);
		
		resetState(); // Reset mock state
	}
	
	public void testGetListNamesWithItemCount() throws Exception {
		// Set up FDUserI mock object
		user.getLevel();
		userC.setReturnValue(FDUserI.RECOGNIZED);
		user.invalidateCache();
				
		
		List<FDCustomerListInfo> lists = new ArrayList<FDCustomerListInfo>();
		FDCustomerListInfo ccl = new FDCustomerListInfo();
		ccl.setName("blist");
		ccl.setCount(3);
		ccl.setModificationDate(MOD_DATE_1);
		ccl.setType(EnumCustomerListType.CC_LIST);
		lists.add(ccl);
		
		ccl = new FDCustomerListInfo();
		ccl.setName("alist");
		ccl.setCount(4);
		ccl.setModificationDate(MOD_DATE_2);
		ccl.setType(EnumCustomerListType.CC_LIST);
		lists.add(ccl);

		user.getCustomerCreatedListInfos();
		userC.setReturnValue(lists);

		user.getCurrentStandingOrder();
		userC.setReturnValue(null);
		
		user.getStandingOrderListInfos();
		userC.setReturnValue(Collections.EMPTY_LIST);
		
		userC.replay();
		
		CustomerListNames result = facade.getListNamesWithItemCount(session);	
		assertEquals(2, result.getListNames().length);
		assertEquals("alist", result.getListNames()[0][0]);
		assertEquals("4", result.getListNames()[0][1]);
		assertEquals("blist", result.getListNames()[1][0]);
		assertEquals("3", result.getListNames()[1][1]);
		assertEquals("alist", result.getMostRecentList());
		
		resetState(); // Reset mock state
	}

	public void testGetListNamesWithItemCountSrcName() throws Exception {
		// Set up FDUserI mock object
		user.getLevel();
		userC.setReturnValue(FDUserI.RECOGNIZED);
		user.invalidateCache();
				
		List<FDCustomerListInfo> lists = new ArrayList<FDCustomerListInfo>();
		FDCustomerListInfo ccl = new FDCustomerListInfo();
		ccl.setName("blist");
		ccl.setCount(3);
		ccl.setModificationDate(MOD_DATE_1);
		ccl.setType(EnumCustomerListType.CC_LIST);
		lists.add(ccl);
		
		ccl = new FDCustomerListInfo();
		ccl.setName("alist");
		ccl.setCount(4);
		ccl.setModificationDate(MOD_DATE_2);
		ccl.setType(EnumCustomerListType.CC_LIST);
		lists.add(ccl);

		ccl = new FDCustomerListInfo();
		ccl.setName("clist");
		ccl.setCount(5);
		ccl.setModificationDate(MOD_DATE_1);
		lists.add(ccl);

		user.getCustomerCreatedListInfos();
		userC.setReturnValue(lists);
		
		user.getCurrentStandingOrder();
		userC.setReturnValue(null);
		
		user.getStandingOrderListInfos();
		userC.setReturnValue(Collections.EMPTY_LIST);

		userC.replay();
		
		CustomerListNames result = facade.getListNamesWithItemCount(session, "clist");	
		assertEquals(2, result.getListNames().length);
		assertEquals("alist", result.getListNames()[0][0]);
		assertEquals("4", result.getListNames()[0][1]);
		assertEquals("blist", result.getListNames()[1][0]);
		assertEquals("3", result.getListNames()[1][1]);
		assertEquals("alist", result.getMostRecentList());
		
		resetState(); // Reset mock state
	}
	
	public void testGetListNamesWithItemCountSrcNameNull() throws Exception {
		// Set up FDUserI mock object
		user.getLevel();
		userC.setReturnValue(FDUserI.RECOGNIZED);
		userC.setReturnValue(FDUserI.RECOGNIZED);
		user.invalidateCache();
				
		List<FDCustomerListInfo> lists = new ArrayList<FDCustomerListInfo>();
		FDCustomerListInfo ccl = new FDCustomerListInfo();
		ccl.setName("blist");
		ccl.setCount(3);
		ccl.setModificationDate(MOD_DATE_1);
		ccl.setType(EnumCustomerListType.CC_LIST);
		lists.add(ccl);
		
		ccl = new FDCustomerListInfo();
		ccl.setName("alist");
		ccl.setCount(4);
		ccl.setModificationDate(MOD_DATE_2);
		ccl.setType(EnumCustomerListType.CC_LIST);
		lists.add(ccl);

		ccl = new FDCustomerListInfo();
		ccl.setName("clist");
		ccl.setCount(5);
		ccl.setModificationDate(MOD_DATE_1);
		ccl.setType(EnumCustomerListType.CC_LIST);
		lists.add(ccl);

		user.getCustomerCreatedListInfos();
		userC.setReturnValue(lists);
		
		user.getCurrentStandingOrder();
		userC.setReturnValue(null);
		
		user.getStandingOrderListInfos();
		userC.setReturnValue(Collections.EMPTY_LIST);

		userC.replay();
		
		CustomerListNames result = facade.getListNamesWithItemCount(session, null);	
		assertEquals(3, result.getListNames().length);
		assertEquals("alist", result.getListNames()[0][0]);
		assertEquals("4", result.getListNames()[0][1]);
		assertEquals("blist", result.getListNames()[1][0]);
		assertEquals("3", result.getListNames()[1][1]);
		assertEquals("clist", result.getListNames()[2][0]);
		assertEquals("5", result.getListNames()[2][1]);
		assertEquals("alist", result.getMostRecentList());
		
		resetState(); // Reset mock state
	}

	public void testRenameList() throws Exception {
		// Set up FDUserI mock object
		user.getLevel();
		userC.setReturnValue(FDUserI.SIGNED_IN);
		user.getIdentity();
		userC.setReturnValue((FDIdentity)null); // Invalid value, but our createList stub catches it anyways 
		user.invalidateCache();
		userC.replay();
				
		facade.renameList(session, "list1", "list2");		
		assertLatestStubCall(renameList, new Object[] {(FDIdentity)null, "list1", "list2"});
		
		resetState(); // Reset mock state
		
		user.getLevel();
		userC.setReturnValue(FDUserI.SIGNED_IN);
		userC.replay();
		
		try {
			facade.renameList(session, "  blabla    ", "    ");
			fail();
		} catch (NameEmpty e) {
			// Pass
		}
		assertEquals(0, stubLog.size());	
		
		resetState(); // Reset mock state
		
		user.getLevel();
		userC.setReturnValue(FDUserI.SIGNED_IN);
		userC.replay();
		
		try {
			facade.renameList(session, "      ", " blublu   ");
			fail();
		} catch (NameEmpty e) {
			// Pass
		}
		assertEquals(0, stubLog.size());	

		resetState(); // Reset mock state

		user.getLevel();
		userC.setReturnValue(FDUserI.SIGNED_IN);
		user.getIdentity();
		userC.setReturnValue((FDIdentity)null); // Invalid value, but our renameList stub catches it anyways 
		userC.replay();
		throwException = new FDCustomerListExistsException();
		
		try {
			facade.renameList(session, "list1", "list2");
			fail();
		} catch (NameExists e) {
			// Pass
		}
		assertEquals(1, stubLog.size());			
		assertLatestStubCall(renameList, new Object[] {(FDIdentity)null, "list1", "list2"});
	}
	
	public void testAddItemsToList() throws Exception {
		// Set up FDUserI mock object
		user.getLevel();
		userC.setReturnValue(FDUserI.SIGNED_IN);
		user.getIdentity();
		userC.setReturnValue((FDIdentity)null); // Invalid value, but our createList stub catches it anyways 
		user.invalidateCache();
		userC.replay();

		
		FDCustomerCreatedList items = new FDCustomerCreatedList();
		FDCustomerProductListLineItem li1 = new FDCustomerProductListLineItem(
			"VEG0010950",
			new FDConfiguration(50, "EA")
		);
        li1.setFrequency(5);
        li1.setFirstPurchase(MOD_DATE_1);
        li1.setLastPurchase(MOD_DATE_1);
        li1.setDeleted(null);
        li1.setRecipeSourceId(null);
        items.addLineItem(li1);
        
        Map configMap = new HashMap();
        configMap.put("C_SF_MAR", "LHR");
        FDCustomerProductListLineItem li2 = new FDCustomerProductListLineItem(
        		"SEA0064611",
        		new FDConfiguration(1, "H05", configMap)
        );
        
        li2.setFrequency(2);
        li2.setFirstPurchase(MOD_DATE_1);
        li2.setLastPurchase(MOD_DATE_1);
        li2.setDeleted(null);
        items.addLineItem(li2);

        FDCustomerProductListLineItem li3 = new FDCustomerProductListLineItem(
        	"VEG0010951",
        	new FDConfiguration(33, "EA")
        );
        li3.setFrequency(5);
        li3.setFirstPurchase(MOD_DATE_1);
        li3.setLastPurchase(MOD_DATE_1);
        li3.setDeleted(MOD_DATE_2);
        items.addLineItem(li3);

        try {
        	facade.addItemsToList(session, "blabla", EnumCustomerListType.CC_LIST.getName(), items);
        	fail();
        } catch (NameEmpty e) {
        	// Pass
        }

		resetState(); // Reset mock state

		user.getLevel();
		userC.setReturnValue(FDUserI.SIGNED_IN);
		user.getIdentity();
		userC.setReturnValue((FDIdentity)null); // Invalid value, but our createList stub catches it anyways 
		user.getIdentity();
		userC.setReturnValue((FDIdentity)null); // Invalid value, but our createList stub catches it anyways 
		user.invalidateCache();
		userC.replay();

		returnObj = new FDCustomerCreatedList();
		((FDCustomerCreatedList) returnObj).setName("blabla");
		((FDCustomerCreatedList) returnObj).setId("L1");
		facade.addItemsToList(session, "blabla", EnumCustomerListType.CC_LIST.getName(), items);
		StubLogEntry gList = ((StubLogEntry)stubLog.get(stubLog.size()-3));
		StubLogEntry sList = ((StubLogEntry)stubLog.get(stubLog.size()-2));
		StubLogEntry gList2 = ((StubLogEntry)stubLog.get(stubLog.size()-1));
		assertEquals(getList, gList.stub);
		assertEquals(storeList, sList.stub);
		assertEquals(getList, gList2.stub);
		FDCustomerCreatedList l = (FDCustomerCreatedList) sList.params[0];
		assertEquals(l.getLineItems().get(0), li1);
		assertEquals(l.getLineItems().get(1), li2);
		assertEquals(l.getLineItems().get(2), li3);
		
		resetState(); // Reset mock state		
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
	}

	protected String[] getAffectedTables() {
		return null;
	}

	protected String getSchema() {
		return null;
	}
	
	public abstract class AbstractAspect implements Aspect {
		public void intercept(InvocationContext invocationContext) throws Exception {
			// Log call
			enterLog(this, invocationContext.getParamVals());
			// Throw errors as requested
			throwIfRequested();
		}
		
		public Pointcut getPointcut() {
			return new MethodPatternPointcut("FDListManagerSessionBean\\." + this.getMethodName());
		}

		protected abstract String getMethodName();
		
		public String toString() {
			return "Aspect<FDListManagerSessionBean." + getMethodName() + ">";
		}
	}
	
	public class CreateListStub extends AbstractAspect {
		
		protected String getMethodName() {
			return "createCustomerCreatedList";
		}

	}

	public class DeleteListStub extends AbstractAspect {
		
		protected String getMethodName() {
			return "deleteCustomerCreatedList";
		}

	}

	public class GetListInfosStub extends AbstractAspect {
		
		protected String getMethodName() {
			return "getCustomerCreatedListInfos";
		}

		public void intercept(InvocationContext invocationContext) throws Exception {
			super.intercept(invocationContext);
			invocationContext.setReturnObject(returnObj);
		}		
	}

	public class GetSOListInfosStub extends AbstractAspect {
		
		protected String getMethodName() {
			return "getStandingOrderListInfos";
		}

		public void intercept(InvocationContext invocationContext) throws Exception {
			super.intercept(invocationContext);
			invocationContext.setReturnObject(returnObj);
		}		
	}

	public class GetCurrentStandingOrderStub extends AbstractAspect {
		
		protected String getMethodName() {
			return "getCurrentStandingOrder";
		}

		public void intercept(InvocationContext invocationContext) throws Exception {
			super.intercept(invocationContext);
			invocationContext.setReturnObject(returnObj);
		}		
	}

	public class RenameListStub extends AbstractAspect {
		
		protected String getMethodName() {
			return "renameCustomerCreatedList";
		}
	
	}

	public class GetListStub extends AbstractAspect {
		
		protected String getMethodName() {
			return "getCustomerList";
		}

		public void intercept(InvocationContext invocationContext) throws Exception {
			super.intercept(invocationContext);
			invocationContext.setReturnObject(returnObj);
		}		
	}

	public class StoreListStub extends AbstractAspect {
		
		protected String getMethodName() {
			return "storeCustomerList";
		}
	
	}
	
	public void throwIfRequested() throws Exception {
		if (throwException != null) {
			throw throwException;
		}
	}
	
	public static class StubLogEntry {
		public StubLogEntry(Aspect stub, Object[] params) {
			this.stub = stub;
			this.params = params;
		}
		public Aspect stub;
		public Object[] params;
		public String toString() {
			return "StubLogEntry[" + stub.toString() + "]";
		}
	}
	
	protected void enterLog(Aspect stub, Object[] params) {
		StubLogEntry e = new StubLogEntry(stub, params);
		stubLog.add(e);
	}
	
	public void assertLatestStubCall(Aspect stub, Object[] params) {
		assertStubCall(stubLog.size()-1, stub, params);
	}
	
	public void assertStubCall(int index, Aspect stub, Object[] params) {
		StubLogEntry e = (StubLogEntry) stubLog.get(index);
		if (e.stub != stub) {
			throw new AssertionFailedError("Assertion failed, wrong stub called: " + stub);
		}
		if (!Arrays.equals(params, e.params)) {
			throw new AssertionFailedError("Assertion failed, stub called with wrong parameters: " + params);
		}
	}
	
	public void flushStubCalls() {
		stubLog.clear();
	}
	
	protected void resetState() {
		userC.reset();
		flushStubCalls();
		throwException = null;
		returnObj = null;
	}
}
