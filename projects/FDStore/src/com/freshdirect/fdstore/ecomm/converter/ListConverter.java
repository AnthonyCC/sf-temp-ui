package com.freshdirect.fdstore.ecomm.converter;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.affiliate.ExternalAgency;
import com.freshdirect.common.context.StoreContext;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.common.pricing.ZoneInfo.PricingIndicator;
import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.ecomm.converter.FDActionInfoConverter;
import com.freshdirect.ecommerce.data.ecoupon.CrmAgentModelData;
import com.freshdirect.ecommerce.data.ecoupon.ErpOrderLineModelData;
import com.freshdirect.ecommerce.data.ecoupon.FDConfigurationData;
import com.freshdirect.ecommerce.data.erp.pricing.ZoneInfoData;
import com.freshdirect.ecommerce.data.fdstore.FDGroupData;
import com.freshdirect.ecommerce.data.fdstore.FDSkuData;
import com.freshdirect.ecommerce.data.list.CustomerCreatedListData;
import com.freshdirect.ecommerce.data.list.CustomerListRequest;
import com.freshdirect.ecommerce.data.list.CustomerProductListLineItemData;
import com.freshdirect.ecommerce.data.list.CustomerRecipeListLineItemData;
import com.freshdirect.ecommerce.data.list.FDActionInfoData;
import com.freshdirect.ecommerce.data.list.FDCustomerListData;
import com.freshdirect.ecommerce.data.list.FDCustomerListInfoData;
import com.freshdirect.ecommerce.data.list.FDCustomerListItemData;
import com.freshdirect.ecommerce.data.list.FDProductSelectionData;
import com.freshdirect.ecommerce.data.list.PricingContextData;
import com.freshdirect.ecommerce.data.list.RenameCustomerListData;
import com.freshdirect.ecommerce.data.list.RenameListData;
import com.freshdirect.ecommerce.data.list.SaleStatisticsData;
import com.freshdirect.ecommerce.data.list.UserContextData;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDProductSelection;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.SaleStatisticsI;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListInfo;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.lists.FDCustomerProductList;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDCustomerRecipeList;
import com.freshdirect.fdstore.lists.FDCustomerRecipeListLineItem;
import com.freshdirect.fdstore.lists.FDCustomerShoppingList;
import com.freshdirect.fdstore.lists.FDQsProductListLineItem;
import com.freshdirect.fdstore.lists.FDStandingOrderList;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;


public class ListConverter {

	public static CustomerCreatedListData buildCustomerCreatedData(FDIdentity identity, StoreContext storeContext, String listName) {
		CustomerCreatedListData customerCreatedList = new CustomerCreatedListData();
		customerCreatedList.setErpCustomerId(identity.getErpCustomerPK());
		if(storeContext.getEStoreId() != null)
		customerCreatedList.setEstoreId(storeContext.getEStoreId().getContentId());
		customerCreatedList.setListName(listName);
		return customerCreatedList;
	}

	public static CustomerListRequest buildCustomerListRequest(FDIdentity identity, EnumCustomerListType type, String listName, String listId) {
		CustomerListRequest customerListRequest = new CustomerListRequest();
		customerListRequest.setErpCustomerId(identity.getErpCustomerPK());
		customerListRequest.setFdcustomerId(identity.getFDCustomerPK());
		customerListRequest.setListName(listName);
		customerListRequest.setType(type.getName());
		customerListRequest.setListId(listId);
		return customerListRequest;
	}

	public static RenameCustomerListData buildRenameCustomerListData(FDActionInfo info, EnumCustomerListType type, String oldName,
			String newName) {
		RenameCustomerListData renameCustomerListData = new RenameCustomerListData();
		renameCustomerListData.setNewName(newName);
		renameCustomerListData.setOldName(oldName);
		renameCustomerListData.setType(type.getName());
		if(info != null){
			FDActionInfoData actionInfoData = FDActionInfoConverter.buildActionInfoData(info);
			renameCustomerListData.setActionInfoData(actionInfoData);
		}
		return renameCustomerListData;
	}

	public static RenameListData buildRenameShoppingListData(String listId, String newName) {
		RenameListData renameListData = new RenameListData();
		renameListData.setListId(listId);
		renameListData.setNewName(newName);
		return renameListData;

	}

	public static RenameListData buildRenameCustomerCreatedListData(FDIdentity identity, String oldName, String newName) {
		RenameListData renameListdata = new RenameListData();
		renameListdata.setErpCustomerId(identity.getErpCustomerPK());
		renameListdata.setOldName(oldName);
		renameListdata.setNewName(newName);
		return renameListdata;
	}

	public static FDCustomerList buildFDCustomerList(FDCustomerListData data) {
		if(data==null)
			return null;
		if(data.getReturnType().equals(FDCustomerProductList.class.getSimpleName())){
			FDCustomerList fdCustomerList = createListByType(EnumCustomerListType.getEnum(data.getListType()));
			fdCustomerList.setCreateDate(data.getCreateDate());
			if(data.getCustomerId() != null)
			fdCustomerList.setCustomerPk(new PrimaryKey(data.getCustomerId()));
			if(data.getCustomerListId() != null)
			fdCustomerList.setId(data.getCustomerListId());
			fdCustomerList.seteStoreType(data.geteStoreType());
			fdCustomerList.setLineItems(buildCustomerListLine(data.getLineItems()));
			fdCustomerList.setModificationDate(data.getModificationDate());
			fdCustomerList.setName(data.getName());
			fdCustomerList.setRecipeId(data.getRecipeId());
			fdCustomerList.setRecipeName(data.getRecipeName());
			return fdCustomerList;
		}
		else if(data.getReturnType().equals(FDCustomerRecipeList.class.getSimpleName())){
			FDCustomerList fdCustomerList = new FDCustomerRecipeList();
			fdCustomerList.setCreateDate(data.getCreateDate());
			if(data.getCustomerId() != null)
			fdCustomerList.setCustomerPk(new PrimaryKey(data.getCustomerId()));
			fdCustomerList.setId(data.getCustomerListId());
			fdCustomerList.seteStoreType(data.geteStoreType());
			fdCustomerList.setLineItems(buildCustomerListLine(data.getLineItems()));
			fdCustomerList.setModificationDate(data.getModificationDate());
			fdCustomerList.setName(data.getName());
			fdCustomerList.setRecipeId(data.getRecipeId());
			fdCustomerList.setRecipeName(data.getRecipeName());
			return fdCustomerList;
		}
		else if(data.getReturnType().equals(FDCustomerListInfo.class.getSimpleName())){
			FDCustomerList fdCustomerList = new FDCustomerListInfo();
			fdCustomerList.setCreateDate(data.getCreateDate());
			if(data.getCustomerId() != null)
			fdCustomerList.setCustomerPk(new PrimaryKey(data.getCustomerId()));
			fdCustomerList.setId(data.getCustomerListId());
			fdCustomerList.seteStoreType(data.geteStoreType());
			fdCustomerList.setLineItems(buildCustomerListLine(data.getLineItems()));
			fdCustomerList.setModificationDate(data.getModificationDate());
			fdCustomerList.setName(data.getName());
			fdCustomerList.setRecipeId(data.getRecipeId());
			fdCustomerList.setRecipeName(data.getRecipeName());
			return fdCustomerList;
		}
		return null;
	}

	private static FDCustomerList createListByType(EnumCustomerListType type) {
	    if (EnumCustomerListType.SHOPPING_LIST.equals(type)) {
	    	return new FDCustomerShoppingList();
	    } else if (EnumCustomerListType.CC_LIST.equals(type)) {
	    	return new FDCustomerCreatedList();
	    } else if (EnumCustomerListType.RECIPE_LIST.equals(type)) {
	    	return new FDCustomerRecipeList();
	    } else if (EnumCustomerListType.SO.equals(type)) {
	    	return new FDStandingOrderList();
	    } else {
		    return null;
	    }
	}

	private static List<FDCustomerListItem> buildCustomerListLine(List<FDCustomerListItemData> lineItems) {
		List<FDCustomerListItem>  customerListItemList = new ArrayList<FDCustomerListItem>();
		for (FDCustomerListItemData fdCustomerListItem : lineItems) {
			customerListItemList.add(buildFDCustomerListItem(fdCustomerListItem));
		}
		return customerListItemList;
	}

	public static FDCustomerListItem buildFDCustomerListItem(
			FDCustomerListItemData fdCustomerListItem) {
		if(fdCustomerListItem.getReturnType().equals(FDCustomerProductListLineItem.class.getSimpleName())){
			FDCustomerProductListLineItem productlistLineItem = new FDCustomerProductListLineItem(fdCustomerListItem.getProductListLineItem().getSkuCode(), buildFDConfiguration(fdCustomerListItem.getProductListLineItem().getConfigurationData()));
			productlistLineItem.setRecipeSourceId(fdCustomerListItem.getProductListLineItem().getRecipeSourceId());
			productlistLineItem.setSojustAddedItemToCart(fdCustomerListItem.getProductListLineItem().isSojustAddedItemToCart());
			productlistLineItem.setFrequency(fdCustomerListItem.getFrequency());
			productlistLineItem.setFirstPurchase(fdCustomerListItem.getFirstPurchase());
			productlistLineItem.setLastPurchase(fdCustomerListItem.getLastPurchase());
			productlistLineItem.setDeleted(fdCustomerListItem.getDeleted());
			if(fdCustomerListItem.getCustomerListItemId() != null)
			productlistLineItem.setId(fdCustomerListItem.getCustomerListItemId());
			return productlistLineItem;
		}
		else if (fdCustomerListItem.getReturnType().equals(FDCustomerRecipeListLineItem.class.getSimpleName())){
			FDCustomerRecipeListLineItem customerRecipeListLineItem = new FDCustomerRecipeListLineItem();
			if(fdCustomerListItem.getRecipeListLineItemData() != null){
				customerRecipeListLineItem.setRecipeId(fdCustomerListItem.getRecipeListLineItemData().getRecipeId());
				customerRecipeListLineItem.setRecipeName(fdCustomerListItem.getRecipeListLineItemData().getRecipeName());
			}
			customerRecipeListLineItem.setFrequency(fdCustomerListItem.getFrequency());
			customerRecipeListLineItem.setFirstPurchase(fdCustomerListItem.getFirstPurchase());
			customerRecipeListLineItem.setLastPurchase(fdCustomerListItem.getLastPurchase());
			customerRecipeListLineItem.setDeleted(fdCustomerListItem.getDeleted());
			customerRecipeListLineItem.setId(fdCustomerListItem.getCustomerListItemId());
			return customerRecipeListLineItem;
		} else return null;

	}

	private static FDConfiguration buildFDConfiguration(FDConfigurationData configurationData) {
		FDConfiguration configuration = new FDConfiguration(configurationData.getQuantity(), configurationData.getSalesUnit(), configurationData.getOptions());
		return configuration;
	}

	public static List<FDCustomerCreatedList> buildFDCustomerCreatedList(List<FDCustomerListData> data) {
		List<FDCustomerCreatedList> fdCustomerCreatedlist = new ArrayList<FDCustomerCreatedList>();
		for (FDCustomerListData fdCustomerListData : data) {
			fdCustomerCreatedlist.add((FDCustomerCreatedList) buildFDCustomerList(fdCustomerListData));
		}
		return fdCustomerCreatedlist;

	}

	public static List<FDCustomerListInfo> buildFDCustomerListInfo(List<FDCustomerListInfoData> data) {
		List<FDCustomerListInfo> fdCustomerListInfoList = new ArrayList<FDCustomerListInfo>();
		for (FDCustomerListInfoData fdCustomerListInfoData : data) {
			FDCustomerListInfo customerListinfo = (FDCustomerListInfo) buildFDCustomerList(fdCustomerListInfoData.getFdcustomerListData());
			customerListinfo.setCount(fdCustomerListInfoData.getCount());
			fdCustomerListInfoList.add(customerListinfo);
		}
		return fdCustomerListInfoList;
	}

	public static FDCustomerListData buildCustomerListData(FDCustomerList fdCustomerList) {
		FDCustomerListData customerListData = new FDCustomerListData();
		customerListData.setCreateDate(fdCustomerList.getCreateDate());
		if(fdCustomerList.getCustomerPk() != null)
		customerListData.setCustomerId(fdCustomerList.getCustomerPk().getId());
		customerListData.setCustomerListId(fdCustomerList.getId());
		customerListData.seteStoreType(fdCustomerList.geteStoreType());

		customerListData.setLineItems(buildCustomerListLineItem(fdCustomerList.getLineItems()));
		customerListData.setModificationDate(fdCustomerList.getModificationDate());
		customerListData.setName(fdCustomerList.getName());
		customerListData.setRecipeId(fdCustomerList.getRecipeId());
		customerListData.setRecipeName(fdCustomerList.getRecipeName());
		if(fdCustomerList instanceof FDCustomerProductList){
			customerListData.setReturnType(FDCustomerProductList.class.getSimpleName());
			if(fdCustomerList instanceof FDCustomerCreatedList){
				customerListData.setListType(EnumCustomerListType.CC_LIST.getName());
			}
			else if(fdCustomerList instanceof FDCustomerShoppingList){
				customerListData.setListType(EnumCustomerListType.SHOPPING_LIST.getName());
				if(fdCustomerList instanceof FDCustomerShoppingList){
					customerListData.setListType(EnumCustomerListType.SO.getName());
				}
			}
		}
		else if(fdCustomerList instanceof FDCustomerRecipeList){
			customerListData.setReturnType(FDCustomerRecipeList.class.getSimpleName());
			customerListData.setListType(EnumCustomerListType.RECIPE_LIST.getName());
		}
		else if (fdCustomerList instanceof FDCustomerListInfo){
			customerListData.setReturnType(FDCustomerListInfo.class.getSimpleName());
			customerListData.setListType(EnumCustomerListType.CC_LIST.getName());
		}
		return customerListData;
	}

	private static List<FDCustomerListItemData> buildCustomerListLineItem(List<FDCustomerListItem> lineItems) {
		List<FDCustomerListItemData>  customerListItemList = new ArrayList<FDCustomerListItemData>();
		for (FDCustomerListItem fdCustomerListItem : lineItems) {
			FDCustomerListItemData fdCustomerListItemData = new FDCustomerListItemData();
			fdCustomerListItemData.setFrequency(fdCustomerListItem.getFrequency());
			fdCustomerListItemData.setFirstPurchase(fdCustomerListItem.getFirstPurchase());
			fdCustomerListItemData.setLastPurchase(fdCustomerListItem.getLastPurchase());
			fdCustomerListItemData.setDeleted(fdCustomerListItem.getDeleted());
			fdCustomerListItemData.setCustomerListItemId(fdCustomerListItem.getId());
			if(fdCustomerListItem instanceof FDCustomerProductListLineItem){
				CustomerProductListLineItemData productlistLineItem = new CustomerProductListLineItemData();
				FDCustomerProductListLineItem customerProduct = (FDCustomerProductListLineItem) fdCustomerListItem;
				productlistLineItem.setConfigurationData(buildConfigurationData(customerProduct.getConfiguration()));
				productlistLineItem.setRecipeSourceId(customerProduct.getRecipeSourceId());
				productlistLineItem.setSkuCode(customerProduct.getSkuCode());
				productlistLineItem.setSojustAddedItemToCart(customerProduct.isSojustAddedItemToCart());
				fdCustomerListItemData.setProductListLineItem(productlistLineItem);
				fdCustomerListItemData.setReturnType(FDCustomerProductListLineItem.class.getSimpleName());

			}
			else if (fdCustomerListItem instanceof FDCustomerRecipeListLineItem){
				CustomerRecipeListLineItemData customerRecipeListLineItemData = new CustomerRecipeListLineItemData();
				FDCustomerRecipeListLineItem customerRecipieline = (FDCustomerRecipeListLineItem) fdCustomerListItem;
				customerRecipeListLineItemData.setRecipeId(customerRecipieline.getRecipeId());
				customerRecipeListLineItemData.setRecipeName(customerRecipieline.getRecipeName());
				fdCustomerListItemData.setRecipeListLineItemData(customerRecipeListLineItemData);
				fdCustomerListItemData.setReturnType(FDCustomerRecipeListLineItem.class.getSimpleName());
			}
			customerListItemList.add(fdCustomerListItemData);
		}
		return customerListItemList;
	}


	private static FDConfigurationData buildConfigurationData(FDConfiguration configuration) {
		FDConfigurationData fdConfigurationData = new FDConfigurationData();
		fdConfigurationData.setOptions(configuration.getOptions());
		fdConfigurationData.setQuantity(configuration.getQuantity());
		fdConfigurationData.setSalesUnit(configuration.getSalesUnit());
		return fdConfigurationData;
	}

	public static List<FDCustomerListData> buildCustomerListDataList(List<FDCustomerCreatedList> customerCreditedList) {
		List<FDCustomerListData> fdcustomerListDataList = new ArrayList<FDCustomerListData>();
		for (FDCustomerCreatedList fdCustomerCreatedList : customerCreditedList) {
			fdcustomerListDataList.add(buildCustomerListData(fdCustomerCreatedList));
		}
		return fdcustomerListDataList;
	}

	public static List<FDProductSelectionI> buildFDProductSelectionI(List<FDProductSelectionData> data) {
		List<FDProductSelectionI> productselectionList = new ArrayList<FDProductSelectionI>();
		for (FDProductSelectionData fdProductSelectionData : data) {
			productselectionList.add(buildFDProductSelectionI(fdProductSelectionData));
		}
		return productselectionList;
	}

	private static FDProductSelectionI buildFDProductSelectionI(FDProductSelectionData fdProductSelectionData) {

			ErpOrderLineModelData orderLineData = fdProductSelectionData.getOrderLine();
			FDProductSelection productselection = new FDProductSelection(buildFDSKU(orderLineData.getSku()),buildProductModel(orderLineData.getSku().getSkuCode()),
					buildFDConfiguration(orderLineData.getConfiguration()),null, buildUserContext(orderLineData.getUserCtx()),
					fdProductSelectionData.getOrderLine().getPlantID());
			productselection.setConfigurationDesc(orderLineData.getConfigurationDesc());
			productselection.setCustomerListLineId(fdProductSelectionData.getCustomerListLineId());
			productselection.setDeliveryStartDate(fdProductSelectionData.getDeliveryStartDate());
			productselection.setDepartmentDesc(orderLineData.getDepartmentDesc());
			productselection.setDescription(orderLineData.getDescription());
			productselection.setExternalAgency(ExternalAgency.safeValueOf(orderLineData.getExternalAgency()));
			productselection.setExternalGroup(orderLineData.getExternalGroup());
			productselection.setExternalSource(orderLineData.getExternalSource());
			productselection.setFDGroup(buildFDGroup(orderLineData.getGroup()));
			productselection.setFixedPrice(orderLineData.getPrice());
			productselection.setInvalidConfig(fdProductSelectionData.isInvalidConfig());
			productselection.setGroupQuantity(orderLineData.getGrpQuantity());
			productselection.setOrderId(fdProductSelectionData.getOrderId());
			productselection.setOriginatingProductId(orderLineData.getOriginatingProductId());
			productselection.setRecipeSourceId(orderLineData.getRecipeSourceId());
			productselection.setRequestNotification(orderLineData.isRequestNotification());
			if(fdProductSelectionData.getSaleStatus() != null)
			productselection.setSaleStatus(EnumSaleStatus.getSaleStatus(fdProductSelectionData.getSaleStatus()));
			productselection.setScaleQuantity(orderLineData.getScaleQuantity());
			productselection.setStatistics(buildSaleStatisticsI(fdProductSelectionData.getStatistics()));
			productselection.setYmalCategoryId(orderLineData.getYmalCategoryId());
			productselection.setYmalSetId(orderLineData.getYmalSetId());
			return productselection;
	}

	private static ProductModel buildProductModel(String skuCode) {
		if (skuCode == null) {
//			/throw new FDSkuNotFoundException("SKU code not set");
		}

		ProductModel cachedProduct = null;
		try {
			cachedProduct = ContentFactory.getInstance().getProduct(skuCode);
		} catch (FDSkuNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cachedProduct;
	}

	private static UserContext buildUserContext(UserContextData userCtx) {
		UserContext usercontext = new  UserContext();
		usercontext.setStoreContext(StoreContext.createStoreContext(EnumEStoreId.valueOfContentId(userCtx.getEstoreId())));
		if(userCtx.getPricingContext()!=null)
			usercontext.setPricingContext(buildPricingContextData(userCtx.getPricingContext()));
		return usercontext;
	}
	private  static PricingContext buildPricingContextData(PricingContextData pricingContextdata) {
		PricingContext pricingContext = new PricingContext(buildPricingZone(pricingContextdata.getPricingZone()));
		
		return pricingContext;
	}
	private static ZoneInfo buildPricingZone(ZoneInfoData zoneInfoData) {
		ZoneInfo zoneInfo = null;
		if(zoneInfoData!=null){
		 zoneInfo = new ZoneInfo(
				zoneInfoData.getZoneId(),
				zoneInfoData.getSalesOrg(),
				zoneInfoData.getDistributionChanel(),
				ZoneInfo.PricingIndicator.valueOf(zoneInfoData.getPricingIndicator().name()),
				buildPricingZone(zoneInfoData.getParent()));
		}
		return zoneInfo;
	}

	private static FDSku buildFDSKU(FDSkuData sku) {
		FDSku skuModel = new FDSku(sku.getSkuCode(), sku.getVersion());
		return skuModel;
	}

	private static FDGroup buildFDGroup(FDGroupData group) {
		FDGroup groupModel =  null;
		if(group != null){
		 groupModel = new FDGroup(group.getGroupId(), group.getVersion());
		groupModel.setSkipProductPriceValidation(group.isSkipProductPriceValidation());
		}
		return groupModel;
	}

	public static SaleStatisticsI buildSaleStatisticsI(SaleStatisticsData statistics) {
		FDQsProductListLineItem salestatistics = new FDQsProductListLineItem(statistics.getCustomerListItemData().getProductListline().getSkuCode(), buildFDConfiguration(statistics.getCustomerListItemData().getProductListline().getConfigurationData()),
				statistics.getCustomerListItemData().getProductListline().getRecipeSourceId());
		salestatistics.setDeliveryStartDate(statistics.getCustomerListItemData().getDeliveryStartDate());
		salestatistics.setOrderId(statistics.getCustomerListItemData().getOrderId());
		salestatistics.setOrderLineId(statistics.getCustomerListItemData().getOrderLineId());
		salestatistics.setSojustAddedItemToCart(statistics.getCustomerListItemData().getProductListline().isSojustAddedItemToCart());
		if(statistics.getCustomerListItemData() != null)
		salestatistics.setSaleStatus(EnumSaleStatus.getSaleStatus(statistics.getCustomerListItemData().getSaleStatus()));
		return salestatistics;
	}


}
