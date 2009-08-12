package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.freshdirect.customer.ErpCartonDetails;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.fdstore.customer.FDCartonDetail;
import com.freshdirect.fdstore.customer.FDCartonInfo;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.webapp.util.CartonComplaintsIterator;

public class CartonComplaintsIteratorTestCase extends TestCase {
	private int olCounter;
	
	protected void setUp() throws Exception {
		olCounter = 0;
	}


	public void testOneEmptyCarton() {
		// assemble a test content

		FDCartonInfo inf1 = createCartonInfo("O1", "CN1");

		List cartons = new ArrayList(1);
		cartons.add(inf1);


		FDOrderI anOrder = new TestOrderAdapter(cartons);
	
		CartonComplaintsIterator cit = new CartonComplaintsIterator(anOrder);
		assertFalse(cit.hasNext());
		assertNull(cit.nextDetail());
	}

	public void testTwoEmptyCartons() {
		// assemble a test content

		FDCartonInfo inf1 = createCartonInfo("O1", "CN1");
		FDCartonInfo inf2 = createCartonInfo("O2", "CN2");

		List cartons = new ArrayList(2);
		cartons.add(inf1);
		cartons.add(inf2);


		FDOrderI anOrder = new TestOrderAdapter(cartons);
	
		CartonComplaintsIterator cit = new CartonComplaintsIterator(anOrder);
		assertFalse(cit.hasNext());
		assertNull(cit.nextDetail());
	}

	public void testOneEmptyAndCartonWithNode() {
		// assemble a test content

		FDCartonInfo inf1 = createCartonInfo("O1", "CN1");
		FDCartonInfo inf2 = createCartonInfo("O2", "CN2");
		FDCartonDetail node1 = createCartonDetail(inf2);

		List cartons = new ArrayList(2);
		cartons.add(inf1);
		cartons.add(inf2);


		FDOrderI anOrder = new TestOrderAdapter(cartons);
	
		CartonComplaintsIterator cit = new CartonComplaintsIterator(anOrder);
		assertTrue(cit.hasNext());
		assertCartonDetail(cit.nextDetail(), node1);
		assertFalse(cit.hasNext());
	}

	public void testTwoEmptyAndCartonWithNode() {
		// assemble a test content

		FDCartonInfo inf1 = createCartonInfo("O1", "CN1");
		FDCartonInfo inf2 = createCartonInfo("O2", "CN2");
		FDCartonDetail node1 = createCartonDetail(inf2);
		FDCartonInfo inf3 = createCartonInfo("O3", "CN3");

		List cartons = new ArrayList(3);
		cartons.add(inf1);
		cartons.add(inf2);
		cartons.add(inf3);


		FDOrderI anOrder = new TestOrderAdapter(cartons);
	
		CartonComplaintsIterator cit = new CartonComplaintsIterator(anOrder);
		assertTrue(cit.hasNext());
		assertCartonDetail(cit.nextDetail(), node1);
		assertFalse(cit.hasNext());
	}

	public void testCartonWithOneNode() {
		// assemble a test content

		FDCartonInfo inf1 = createCartonInfo("O1", "CN1");
		FDCartonDetail node1 = createCartonDetail(inf1);

		List cartons = new ArrayList(1);
		cartons.add(inf1);


		FDOrderI anOrder = new TestOrderAdapter(cartons);
	
		CartonComplaintsIterator cit = new CartonComplaintsIterator(anOrder);
		assertTrue(cit.hasNext());
		assertCartonDetail(cit.nextDetail(), node1);

		assertFalse(cit.hasNext());
	}


	public void testCartonWithTwoNodes() {
		// assemble a test content

		FDCartonInfo inf1 = createCartonInfo("O1", "CN1");
		FDCartonDetail node1 = createCartonDetail(inf1);
		FDCartonDetail node2 = createCartonDetail(inf1);

		List cartons = new ArrayList(1);
		cartons.add(inf1);


		FDOrderI anOrder = new TestOrderAdapter(cartons);
	
		CartonComplaintsIterator cit = new CartonComplaintsIterator(anOrder);
		assertTrue(cit.hasNext());
		assertCartonDetail(cit.nextDetail(), node1);
		assertTrue(cit.hasNext());
		assertCartonDetail(cit.nextDetail(), node2);

		assertFalse(cit.hasNext());
	}


	public void testTwoCartonsWithTwoNodes() {
		// assemble a test content

		FDCartonInfo inf1 = createCartonInfo("O1", "CN1");
		FDCartonDetail node1 = createCartonDetail(inf1);
		FDCartonDetail node2 = createCartonDetail(inf1);


		FDCartonInfo inf2 = createCartonInfo("O2", "CN2");
		FDCartonDetail node3 = createCartonDetail(inf2);
		FDCartonDetail node4 = createCartonDetail(inf2);

		List cartons = new ArrayList(1);
		cartons.add(inf1);
		cartons.add(inf2);

		FDOrderI anOrder = new TestOrderAdapter(cartons);
	
		CartonComplaintsIterator cit = new CartonComplaintsIterator(anOrder);
		assertTrue(cit.hasNext());
		assertCartonDetail(cit.nextDetail(), node1);
		assertTrue(cit.hasNext());
		assertCartonDetail(cit.nextDetail(), node2);
		assertTrue(cit.hasNext());
		assertCartonDetail(cit.nextDetail(), node3);
		assertTrue(cit.hasNext());
		assertCartonDetail(cit.nextDetail(), node4);

		assertFalse(cit.hasNext());
	}


	public void testTwoCartonsWithTwoNodesGapped() {
		// assemble a test content

		FDCartonInfo inf1 = createCartonInfo("O1", "CN1");
		FDCartonDetail node1 = createCartonDetail(inf1);
		FDCartonDetail node2 = createCartonDetail(inf1);


		FDCartonInfo inf2 = createCartonInfo("O2", "CN2");
		FDCartonDetail node3 = createCartonDetail(inf2);
		FDCartonDetail node4 = createCartonDetail(inf2);

		FDCartonInfo inf3 = createCartonInfo("O3", "CN3");

		List cartons = new ArrayList(3);
		cartons.add(inf1);
		cartons.add(inf3);
		cartons.add(inf2);

		FDOrderI anOrder = new TestOrderAdapter(cartons);
	
		CartonComplaintsIterator cit = new CartonComplaintsIterator(anOrder);
		assertTrue(cit.hasNext());
		assertCartonDetail(cit.nextDetail(), node1);
		assertTrue(cit.hasNext());
		assertCartonDetail(cit.nextDetail(), node2);
		assertTrue(cit.hasNext());
		assertCartonDetail(cit.nextDetail(), node3);
		assertTrue(cit.hasNext());
		assertCartonDetail(cit.nextDetail(), node4);

		assertFalse(cit.hasNext());
	}

	private void assertCartonDetail(Object obj, FDCartonDetail other) {
		assertNotNull(obj);
		
		FDCartonDetail d = (FDCartonDetail)obj;
		assertEquals(other.getCartonDetail(), d.getCartonDetail());
		assertEquals(other.getCartonInfo(), d.getCartonInfo());
	}


	// 2nd level
	private FDCartonDetail createCartonDetail(FDCartonInfo parent) {
		String orderLineNumber = Integer.toString(olCounter);
		String matNumber = "MAT"+orderLineNumber;

		ErpCartonDetails ecd = new ErpCartonDetails(parent.getCartonInfo(), orderLineNumber, matNumber, "barcode", 1.0, 1.0, "EA");
		FDCartonDetail obj = new FDCartonDetail(parent, ecd, null /*cartline*/);

		olCounter++;

		// add to parent node
		parent.getCartonDetails().add(obj);
		
		return obj;
	}

	// 1st level
	private FDCartonInfo createCartonInfo(String orderNumber, String cartNum) {
		final ErpCartonInfo erpCartonInfo = new ErpCartonInfo(orderNumber, "SAP123", cartNum, ErpCartonInfo.CARTON_TYPE_REGULAR);
		final FDCartonInfo inf = new FDCartonInfo(erpCartonInfo, new ArrayList());

		return inf;
	}
}



class TestOrderAdapter extends FDOrderAdapter {
	private static final long serialVersionUID = 1L;

	public TestOrderAdapter(List cartonContents) {
		this.cartonInfo = cartonContents;
	}
}
