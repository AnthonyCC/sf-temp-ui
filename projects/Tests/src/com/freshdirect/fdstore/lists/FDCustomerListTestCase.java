package com.freshdirect.fdstore.lists;

import java.util.ArrayList;
import java.util.Date;

import junit.framework.TestCase;

import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.framework.core.PrimaryKey;

public class FDCustomerListTestCase extends TestCase {

	static class FDTestCustomerList extends FDCustomerList {

		public EnumCustomerListType getType() {
			return EnumCustomerListType.CC_LIST;
		}
	}
	
	public void testModificationTracking() {
		FDTestCustomerList testList = new FDTestCustomerList();

		testList.setCreateDate(new Date());
		assertNotNull(testList.getModificationDate());

		testList.unmarkAsModified(null);
		assertNull(testList.getModificationDate());

		Date createDate = new Date(1);
		testList.setCreateDate(createDate);
		assertTrue(createDate.compareTo(testList.getModificationDate()) < 0);		
		testList.unmarkAsModified(null);
		
		assertNull(testList.getModificationDate());
		testList.setCustomerPk(new PrimaryKey("P1"));
		assertNotNull(testList.getModificationDate());
		testList.unmarkAsModified(null);
		
		assertNull(testList.getModificationDate());
		testList.setId("L1");
		assertNull(testList.getModificationDate());

		assertNull(testList.getModificationDate());
		testList.setLineItems(new ArrayList());
		assertNotNull(testList.getModificationDate());
		testList.unmarkAsModified(null);

		assertNull(testList.getModificationDate());
		testList.setName("Test List");
		assertNotNull(testList.getModificationDate());
		testList.unmarkAsModified(null);
		
		assertNull(testList.getModificationDate());
		testList.setPK(new PrimaryKey("X1"));
		assertNull(testList.getModificationDate());
	}
}
