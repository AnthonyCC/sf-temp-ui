package com.freshdirect.webapp.util.json;

import junit.framework.TestCase;

import com.freshdirect.webapp.util.json.FDConfigurationJSONSerializer;
import com.freshdirect.webapp.util.json.FDCustomerCreatedListJSONSerializer;


import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;

import java.util.TreeMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Vector;

import com.metaparadigm.jsonrpc.JSONSerializer;


public class JSONSerializerTest extends TestCase {
	
	private static class TestLineItem extends FDCustomerProductListLineItem {


		private static final long serialVersionUID = -2008713156338061774L;

		public TestLineItem(String skuCode, FDConfiguration configuration) {
			super(skuCode, configuration);
		}
		
		public String getFullName() { 
			return "mici macko";
		}
		
		public String getCategoryId() {
			return "dormogo domotor";
		}
		
		public String getProductId() {
			return "futrinka utca";
		}
		
	}
	
	private FDConfiguration simpleConf = new FDConfiguration(2.3,"EA02");
	private FDConfiguration complicatedConf = null;
	
	private JSONSerializer ser = new JSONSerializer();
	
	
	public void setUp() throws Exception {
		super.setUp();
		
		Map opt = new TreeMap();
		opt.put("X","XVALUE");
		opt.put("Y","YVALUE");
		opt.put("Z","ZVALUE");
		
		complicatedConf = new FDConfiguration(0.5,"LBS04",opt);
		ser.registerSerializer(FDConfigurationJSONSerializer.getInstance());
		ser.registerSerializer(FDCustomerCreatedListJSONSerializer.getInstance());
	}
	
	
	
	public void testFDConfigurationSerialization() throws Exception {
		String jsSimple = ser.toJSON(simpleConf);
		FDConfiguration simpleDup = (FDConfiguration)ser.fromJSON(jsSimple);
	
		assertTrue(simpleConf.equals(simpleDup));
	
		String jsComplicated = ser.toJSON(complicatedConf);
		FDConfiguration complicatedDup = (FDConfiguration)ser.fromJSON(jsComplicated);
		
		assertTrue(complicatedConf.equals(complicatedDup));
	}
	
	public void testFDCustomerCreatedListSerialization() throws Exception {
	    FDCustomerCreatedList lineItems = new FDCustomerCreatedList();
	    
	    Vector lineItemVec= new Vector();
	    for(int i = 0; i< 25; ++i) {
	    	String sku = "SKU_" + i;
	    	FDCustomerProductListLineItem li = new TestLineItem(sku,i % 2 == 0? simpleConf : complicatedConf);
	    	li.setRecipeSourceId("123");
	    	lineItems.addLineItem(li);
	    	lineItemVec.add(li);
	    }
	    
	    String jsItems = ser.toJSON(lineItems);
	    System.out.println(jsItems);
	    FDCustomerCreatedList lineItemsDup = (FDCustomerCreatedList)ser.fromJSON(jsItems);
	   
	    assertTrue(lineItemsDup.getLineItems().size() == lineItems.getLineItems().size());
	    for(Iterator i = lineItemsDup.getLineItems().iterator(); i.hasNext(); ) {
	    	FDCustomerProductListLineItem item = (FDCustomerProductListLineItem)i.next();
	    	int p = Integer.parseInt(item.getSkuCode().substring("SKU_".length()));
	        assertTrue(lineItemVec.get(p).equals(item));	
	    }
	}


}
