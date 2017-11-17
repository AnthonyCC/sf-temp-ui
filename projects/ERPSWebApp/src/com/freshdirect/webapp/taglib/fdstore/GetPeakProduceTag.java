/*
 * 
 * GetOrderByStatusTag.java
 * Date: Nov 13, 2002 Time: 12:42:09 PM
 */
package com.freshdirect.webapp.taglib.fdstore;

/**
 * 
 * @author knadeem
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.freshdirect.common.context.UserContext;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.Image;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SkuModel;
import com.freshdirect.storeapi.util.ProductInfoUtil;
import com.freshdirect.webapp.taglib.AbstractGetterTag;


public class GetPeakProduceTag extends AbstractGetterTag {
	
	private static final int MAX_PEAK_PRODUCE_COUNT=6;
	private static final int MIN_PEAK_PRODUCE_COUNT=3;
	private static final int SPARE_WIDTH=50;
	
	String deptId = null;

	String getGlobalPeakProduceSku="false";
	boolean useMinCount = true; //allow ignoring of min value in Tag
	
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	public void setUseMinCount(boolean useMinCount) {
		this.useMinCount = useMinCount;
	}
	
	public void setGlobalPeakProduceSku(String b){
		this.getGlobalPeakProduceSku=b;
	}

	protected Object getResult() throws FDResourceException {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		FDIdentity identity = user.getIdentity();
		
		Collection peakProduce = null;
		ContentNodeModel node=ContentFactory.getInstance().getContentNode(deptId);
		if(node instanceof DepartmentModel) {
			if ("true".equalsIgnoreCase(getGlobalPeakProduceSku)) {
				peakProduce = getAllPeakProduceForDept((DepartmentModel) node,user.getUserContext());
			} else {
				peakProduce = getPeakProduce((DepartmentModel) node, MAX_PEAK_PRODUCE_COUNT,user.getUserContext());
			}
		}

		pageContext.setAttribute("tableWidth", peakProduce == null ? 0 : countTableWidth(peakProduce));
		return peakProduce;
	}
	
	private Collection getAllPeakProduceForDept(DepartmentModel dept,UserContext userCtx) throws FDResourceException {
		//String plantID=userCtx.getFulfillmentContext().getPlantId();
	    List products=new ArrayList();
		List deptList=new ArrayList();
		//System.out.println("|=== dept.getContentKey().getId()  :"+dept.getContentKey().getId());
		deptList.add(dept.getContentKey().getId());
		List _products=FDCachedFactory.findPeakProduceSKUsByDepartment(deptList);
		//System.out.println("||===== getAllPeakProduceForDept after query: "+_products);
        
		
		if(_products!=null && _products.size()!=0) {
			products=new ArrayList(_products.size());
			FDProductInfo productInfo=null;
			String sku="";
			for (Iterator i = _products.iterator(); i.hasNext();) {
				sku=i.next().toString();
				try {
					productInfo=FDCachedFactory.getProductInfo(sku);
					if(productInfo.isAvailable(userCtx.getPricingContext().getZoneInfo().getSalesOrg(),userCtx.getPricingContext().getZoneInfo().getDistributionChanel()) && 
							isPeakProduce(productInfo.getRating(ProductInfoUtil.getPickingPlantId(productInfo)))) {
						
						try {
							   ProductModel sm=ContentFactory.getInstance().getProduct(sku);
							   if(!sm.isUnavailable())
								   products.add(sm);
					     } catch(Exception e) {
					     }
					}
				} catch (FDSkuNotFoundException e) {
					throw new FDResourceException(e);
				}
			}
		}
		
		
		//System.out.println("||||=== getAllPeakProduceForDept before remove duplicates: "+products);
        //products=removeDuplicates(products);        
        //System.out.println("Peak produce after remove duplicates:"+products);	
		return products;
	}
	
	public Collection getPeakProduce(DepartmentModel dept, int maxPeakProduceCount,UserContext userCtx) throws FDResourceException {
		String plantID=userCtx.getFulfillmentContext().getPlantId();
		List products=new ArrayList(10);
		List categories=dept.getCategories();
		Iterator it=categories.iterator();
		CategoryModel catModel=null;
		List _products=null;
		while (it.hasNext()) {
			_products=new ArrayList();
			catModel=(CategoryModel)it.next();
			setPeakProduce(catModel,_products,plantID);
			if(_products!=null) {
				products.addAll(_products);
			}
		}

	      //System.out.println("-=--=-=--=-==-==-=BEFORE Peak produce :"+products);
	      //System.out.println("-=--=-=--=-==-==-=BEFORE Peak produce :"+products);
        products=removeDuplicates(products);
      //System.out.println("-=--=-=--=-==-==-=AFTER Peak produce :"+products);
		if(products.size()<MIN_PEAK_PRODUCE_COUNT) {
			if (this.useMinCount) {
				//if true (default) return an empty list
				return new ArrayList();
			}else{
				//else retrun whatever we have
				return products;
			}
		} 
		else if(products.size()>maxPeakProduceCount) {
			Random random=new Random();
			Set randomProducts=new HashSet(maxPeakProduceCount);
			int index=0;
			while(randomProducts.size()<maxPeakProduceCount) {
				index=random.nextInt(products.size());
				randomProducts.add(products.get(index));
			}
			//System.out.println("Chose Peak produce :"+randomProducts);
			return randomProducts;
		}
		else {
			return products;
		}
		

	}
	
	private int countTableWidth(Collection products)  throws FDResourceException {
		
		Iterator it = products.iterator();
		
		int tableWidth=0;

		while (it.hasNext()) {
			Object o=it.next();
			
			if(o instanceof SkuModel){
				SkuModel model=(SkuModel) o;
				ProductModel pm=model.getProductModel();
				Image itemImage = pm.getAlternateImage();
				
				if (itemImage == null) {
					itemImage = pm.getCategoryImage();
				}
				
				tableWidth+=itemImage.getWidth()+SPARE_WIDTH;
				
			}
		}
		return tableWidth;
	}
	
	private List removeDuplicates(List products) throws FDResourceException {
		
		Set set = new HashSet();
		set.addAll(products);
		if(set.size() < products.size()) {
			products.clear();
			products.addAll(set);
		}
//		 agb begin
		//return products;		

		//System.out.println("===start=== removeDuplicates:"+products);
		Map m = new HashMap();
		Iterator it = set.iterator();

		while (it.hasNext()) {
			boolean isBrand=false;
			Object obj=it.next();
			if(obj instanceof SkuModel)
			{

				//System.out.println("=== instanceof SkuModel:");
				SkuModel sku = (SkuModel)obj; 
				try {
					FDProductInfo prodInfo = sku.getProductInfo();
					com.freshdirect.fdstore.FDProduct prod = sku.getProduct();
					String materialNumber= prod.getMaterial().getMaterialNumber();
					//System.out.println("materialNumber= "+ materialNumber);
					final int MAX_DISPLAY_BRANDS=1;
					List list = sku.getProductModel().getDisplayableBrands(MAX_DISPLAY_BRANDS);			
					if(!list.isEmpty()){
						isBrand=true;
					}
					if( m.containsKey(materialNumber)){
						if(isBrand) {
							m.put(materialNumber, sku);
						}
					}
					else{
						m.put(materialNumber, sku);
					}
				}
				catch (FDSkuNotFoundException e) { 
					throw new FDResourceException(e);
				}
			}else if(obj instanceof ProductModel){

				//System.out.println("=== instanceof ProductModel:");
				ProductModel product = (ProductModel)obj; 								
				List skus=product.getSkus();
				String rating=null;
				SkuModel skuTemp=null;
				for(int i=0;i<skus.size();i++) {
					skuTemp=(SkuModel)skus.get(i);

					//System.out.println("=== skuTemp: "+skuTemp);
					if(skuTemp.isDiscontinued() || skuTemp.isOutOfSeason() || skuTemp.isTempUnavailable() ||(!skuTemp.isAvailableWithin(2))) {//sku is available tomorrow.
						continue;
					}				   											
					try {
						com.freshdirect.fdstore.FDProduct prod = skuTemp.getProduct();
						String materialNumber= prod.getMaterial().getMaterialNumber();
						//System.out.println("materialNumber= "+ materialNumber);
						final int MAX_DISPLAY_BRANDS=1;
						List list = skuTemp.getProductModel().getDisplayableBrands(MAX_DISPLAY_BRANDS);			
						if(!list.isEmpty()){
							isBrand=true;
						}
						if( m.containsKey(materialNumber)){
							if(isBrand) {
								m.put(materialNumber, product);
							}
							//System.out.println("=== DOES === m.containsKey(materialNumber): ");
						}
						else{
							m.put(materialNumber, product);
							//System.out.println("=== DOES NOT === m.containsKey(materialNumber): ");
						}
					}
					catch (FDSkuNotFoundException e) { 
						throw new FDResourceException(e);
					}
				}	
			}
		}

		Collection c = m.values();

		//System.out.println("===========Peak produce after remove duplicates:"+products);
		return new ArrayList(c);  //agb end	
	}

	
	private void setPeakProduce(CategoryModel category, List products,String plantID) throws FDResourceException {


	      //System.out.println("-===|||||||---- setPeakProduce :"+products);
	      //System.out.println("-===|||||||---- setPeakProduce :"+products);
		List peakProduce=getPeakProduce(category.getProducts(),plantID);
		if(peakProduce!=null && peakProduce.size()>0)
			products.addAll(peakProduce);
		List subCats=category.getSubcategories();
		for (int i=0;i<subCats.size();i++) {
			setPeakProduce((CategoryModel)subCats.get(i),products,plantID);
		}
		
	}
	
	private List getPeakProduce(Collection products,String plantID) throws FDResourceException {
		
		
		if(products==null || products.size()==0) {
			return null;
		}

	      //System.out.println("-===__________---- setPeakProduce :"+products);
		
		List peakProduce=new ArrayList(10);
		Iterator it=products.iterator();
		SkuModel sku=null;
		while(it.hasNext()) {
			ProductModel prod=(ProductModel)it.next();
			List skus=prod.getSkus();
			EnumOrderLineRating rating=null;
			for(int i=0;i<skus.size();i++) {
				sku=(SkuModel)skus.get(i);
				if(sku.isDiscontinued() || sku.isOutOfSeason() || sku.isTempUnavailable() ||(!sku.isAvailableWithin(2))) {//sku is available tomorrow.
					continue;
				}
				try {
					rating=sku.getProductInfo().getRating(ProductInfoUtil.getPickingPlantId(sku.getProductInfo()));
				} catch (FDSkuNotFoundException e) {
					throw new FDResourceException(e);
				}
				if(PopulatorUtil.isShowRatings(sku.getSkuCode())&&isPeakProduce(rating)) {
					peakProduce.add(sku);
					break;
				}
			}
		}
		return peakProduce;
	}
	
	private boolean isPeakProduce(EnumOrderLineRating rating) {
		
		if ( EnumOrderLineRating.PEAK_PRODUCE_10.equals(rating)||
			 EnumOrderLineRating.PEAK_PRODUCE_9.equals(rating)||
			 EnumOrderLineRating.PEAK_PRODUCE_8.equals(rating) ) {
			return true;
		}
		return false;
				
		
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.Collection";
		}

	}

}

