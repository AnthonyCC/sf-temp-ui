package com.freshdirect.dataloader.productfamily;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.productfamily.FDProductFamilyJcoServer;
import com.freshdirect.dataloader.productfamily.FDProductFamilyJcoServer.FDConnectionHandler;
import com.freshdirect.dataloader.response.FDJcoServerResult;
import com.freshdirect.dataloader.sap.jco.server.param.ProductFamilyParameter;
public class sampletest{
	
	public static void main(String args[]){
		ArrayList<String> ar1 = new ArrayList<String>();
		sampletest sm = new sampletest();
		FDProductFamilyJcoServer jco = new FDProductFamilyJcoServer();
		FDConnectionHandler fDConnectionHandler = jco.new FDConnectionHandler();
		ar1.add("beverage");
		ar1.add("1111111");
		ar1.add("I");
		ar1.add(" ");
		ar1.add(" ");
		ar1.add("successfully updated");
		System.out.println("ar1%%%%%"+ar1.size());	
		//jco.storeScaleGroups(ar1);   // for inserting
		//jco.deleteScaleGroups(ar1);    // for update product family
		
		Map<String, List<ProductFamilyParameter>> productFamilyRecordMap = new HashMap();
		FDJcoServerResult result = null;
		
		productFamilyRecordMap.put("beverage", getFamilyProducts("beverage"));
		productFamilyRecordMap.put("water", getFamilyProducts("water"));
		productFamilyRecordMap.put("wine", getFamilyProducts("wine"));
		
		try {
			String ss =  fDConnectionHandler.validatMaterials(productFamilyRecordMap,result);
			System.out.println("ar1%%%%%"+ss);	
		} catch (LoaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private static List<ProductFamilyParameter> getFamilyProducts(String familyID) {
		List<ProductFamilyParameter> list = new ArrayList<ProductFamilyParameter>();
		for (int i =0;i<10;i++){
		ProductFamilyParameter pp =new ProductFamilyParameter();
		pp.setAction(random());
		pp.setGroupId(familyID);
		pp.setMaterialID("11111"+i);
		pp.setDeletegroup(randomGropuDel());
		list.add(pp);
		}
		return list;
	}

	private static String random() {
	    Random r = new Random();

	    String alphabet = "ID ";
		alphabet.charAt(r.nextInt(alphabet.length()));
		return alphabet.charAt(r.nextInt(alphabet.length()))+"";
	}
	
	private static String randomGropuDel() {
	    Random r = new Random();

	    String alphabet = "X ";
		alphabet.charAt(r.nextInt(alphabet.length()));
		return alphabet.charAt(r.nextInt(alphabet.length()))+"";
	}
	
}