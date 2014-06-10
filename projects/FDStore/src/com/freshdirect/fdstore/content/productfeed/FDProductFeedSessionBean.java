package com.freshdirect.fdstore.content.productfeed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.EJBException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.fdstore.PreviewLinkProvider;
import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.content.attributes.EnumAttributeType;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.fdstore.FDNutrition;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.ZonePriceModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.productfeed.Attributes.ProdAttribute;
import com.freshdirect.fdstore.content.productfeed.Configurations.Configuration;
import com.freshdirect.fdstore.content.productfeed.Configurations.Configuration.VariationOption;
import com.freshdirect.fdstore.content.productfeed.GroupPrices.GroupPrice;
import com.freshdirect.fdstore.content.productfeed.Images.Image;
import com.freshdirect.fdstore.content.productfeed.Inventories.Inventory;
import com.freshdirect.fdstore.content.productfeed.NutritionInfo.Nutrition;
import com.freshdirect.fdstore.content.productfeed.Prices.Price;
import com.freshdirect.fdstore.content.productfeed.Products.Product;
import com.freshdirect.fdstore.content.productfeed.Ratings.Rating;
import com.freshdirect.fdstore.content.productfeed.SaleUnits.SaleUnit;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


public class FDProductFeedSessionBean extends SessionBeanSupport {
    
	private static final long	serialVersionUID	= 270497771665382812L;
	
	private static Category LOGGER = LoggerFactory.getInstance( FDProductFeedSessionBean.class );
	
	private static final String RATING = "RATING";
	private static final String SUSTAINABILITY_RATING = "SUSTAINABILITY_RATING";
	private static final String PRODUCT_CATEGORY_IMAGE = "PRODUCT_CATEGORY_IMAGE";
	private static final String PRODUCT_FEATURE_IMAGE = "PRODUCT_FEATURE_IMAGE";
	private static final String PRODUCT_DETAIL_IMAGE = "PRODUCT_DETAIL_IMAGE";
	private static final String PRODUCT_ZOOM_IMAGE = "PRODUCT_ZOOM_IMAGE";
	private static final String PRODUCT_IMAGE = "PRODUCT_IMAGE";
	private static final String URL_DOMAIN ="https://www.freshdirect.com";
	private final static JSch jsch=new JSch();
	private static final String sftpHost = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_PRODUCT_FEED_UPLOADER_FTP_HOST);
	private static final String sftpUser = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_PRODUCT_FEED_UPLOADER_FTP_USER);
	private static final String sftpPasswd = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_PRODUCT_FEED_UPLOADER_FTP_PASSWD);
	private static final String sftpDirectory = ErpServicesProperties.getProperty(ErpServicesProperties.PROP_PRODUCT_FEED_UPLOADER_FTP_WORKDIR);
    
    /** Constructor
     */
    public FDProductFeedSessionBean() {
        super();
    }
    
    /**
     * Template method that returns the cache key to use for caching resources.
     *
     * @return the bean's home interface name
     */
    protected String getResourceCacheKey() {
        return "com.freshdirect.content.productfeed.ejb.FDProductFeedHome";
    }
    
    /** Remove method for container to call
     * @throws EJBException throws EJBException if there is any problem.
     */
    public void ejbRemove() {
    }
    
    public boolean uploadProductFeed() throws FDResourceException, RemoteException {
    	
    	try {

			LOGGER.info("Inside uploadProductFeed()..");
			Products xmlProducts = new Products();
			JAXBContext jaxbCtx = JAXBContext.newInstance(Products.class);
			populateProducts(xmlProducts);								
			uploadProductFeedFile(xmlProducts, jaxbCtx);
			LOGGER.info("Available products fetched & uploaded: "+xmlProducts.getProduct().size());

		} catch (Exception e) {
			LOGGER.error("Exception :"+e.getMessage());
			throw new FDResourceException(e);
		}
    	return true;
    }
    
    
    private static void populateProducts(Products xmlProducts)
			throws FDResourceException {
		Set<ContentKey> skuContentKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.SKU);
		if(null !=skuContentKeys){
			LOGGER.info("Skus in CMS: "+skuContentKeys.size());
			for (Iterator<ContentKey> i = skuContentKeys.iterator(); i.hasNext();) {
				ContentKey key = (ContentKey) i.next();
				String skucode = key.getId();
//						if(skucode.equals("MEA0004687") || skucode.equals("FRU0005151")||skucode.equals("DAI0069651") ||skucode.equals("MEA1075865")){
					ProductModel productModel =null;
					FDProductInfo fdProductInfo = null;
					FDProduct fdProduct = null;
					try {
						productModel =ContentFactory.getInstance().getProduct(skucode);
						if(null != productModel && !productModel.isOrphan() && !"Archive".equalsIgnoreCase(productModel.getDepartment().getContentName())){
							fdProductInfo = FDCachedFactory.getProductInfo(skucode);
							fdProduct = FDCachedFactory.getProduct(fdProductInfo);		
							
						}
					} catch (FDSkuNotFoundException e) {
						//Ignore
					}
					if(null != fdProductInfo && null !=fdProduct){						
						/*if(!fdProductInfo.isAvailable()){
							continue;
						}*/				
						Product product = new Product();			
						xmlProducts.getProduct().add(product);
						
						populateProductBasicInfo(productModel, fdProductInfo,fdProduct, product);
						
						populateAttributes(fdProductInfo, fdProduct, product, productModel);
						
						populatePricingInfo(fdProduct,  product);		
										
						populateGroupScalePriceInfo(skucode,  product);		
											
						populateSalesUnitInfo(fdProduct,product);
											
						populateNutritionInfo(fdProduct, product);									
						
						populateConfigurationsInfo(fdProduct, product);										
						
						populateRatingInfo(fdProductInfo,  product);
											
						populateInventoryInfo(fdProductInfo,  product);
						
						populateImages(productModel, product);	
					}
						
//						}
			}
		}
	}

	private static void uploadProductFeedFile(Products xmlProducts,
			JAXBContext jaxbCtx) throws JAXBException, PropertyException,
			FileNotFoundException, IOException, JSchException, SftpException {

		ChannelSftp sftp = null;
		Session session = null;
		Channel channel = null;
		try {
			if(!xmlProducts.getProduct().isEmpty()){
				byte[] buffer = new byte[1024];
				Marshaller mar =jaxbCtx.createMarshaller();
				mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
				Date date = new Date();
				String filePath = "";//"C://Project Documents//ProductDataFeed//";
				String fileName = "FDProductFeed_"+df.format(date);
				File file = new File(filePath+fileName+".xml");
				StreamResult result = new StreamResult(file);				
				mar.marshal(xmlProducts, result);
				String zipFileName=filePath+fileName+".zip";
				FileOutputStream fos = new FileOutputStream(zipFileName);
				ZipOutputStream zos = new ZipOutputStream(fos);
				ZipEntry ze= new ZipEntry(file.getName());
				zos.putNextEntry(ze);
				int len;
				FileInputStream in = new FileInputStream(file);
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
 
				in.close();
				file.delete();
				zos.closeEntry();
 
				zos.close();
				
				LOGGER.info("SFTP: connecting to host " + sftpHost);
				Properties config = new Properties();
				config.put("StrictHostKeyChecking", "no");
				if(FDStoreProperties.isProductFeedUploadEnabled()){
					session = getSftpSession(config);
					session.connect();
					channel = session.openChannel("sftp");
					sftp = (ChannelSftp) channel;
					LOGGER.info("SFTP: Connecting..");
					FileInputStream fis = new FileInputStream(zipFileName);
					sftp.connect();
					sftp.put(fis,sftpDirectory+zipFileName);
					fis.close();
					//To delete the local file after the uploading is completed.
					file = new File(zipFileName);
					file.delete();
				}
 
			}else{
				LOGGER.info("No file generated. ");
			}
		} finally{
			LOGGER.info("SFTP: Disconnecting..");
			if(null !=sftp){
				sftp.disconnect();
			}
			if(null != session){
				session.disconnect();
			}			
			if(null != channel){
				channel.disconnect();
			}
		}
	}

	private static void populateProductBasicInfo(ProductModel productModel,
			FDProductInfo fdProductInfo, FDProduct fdProduct, Product product) {
		product.setSkuCode(fdProduct.getSkuCode());
		product.setUpc(fdProductInfo.getUpc());
		product.setMaterialNum(fdProduct.getMaterial().getMaterialNumber());
		product.setProdId(productModel.getContentName());
		product.setProdName(productModel.getFullName());
		product.setProdUrl(URL_DOMAIN+PreviewLinkProvider.getLink(productModel.getContentKey()));
		product.setCatId(productModel.getCategory().getContentName());
		product.setSubCatId(productModel.getParentId());
		product.setDeptId(productModel.getDepartment().getContentName());
		product.setProdStatus(null !=fdProductInfo.getAvailabilityStatus()?fdProductInfo.getAvailabilityStatus().getStatusCode():"");
		
	}

	private static void populateImages(ProductModel productModel,
			Product product) {
		Images images = new Images();
		product.setImages(images);								
		Image image = null;
		if(null !=productModel.getProdImage()){
			image = new Image();
			image.setImgType(PRODUCT_IMAGE);
			image.setImgUrl(URL_DOMAIN+productModel.getProdImage().getPath());
			images.getImage().add(image);
		}
		if(null !=productModel.getZoomImage()){
			image = new Image();
			image.setImgType(PRODUCT_ZOOM_IMAGE);
			image.setImgUrl(URL_DOMAIN+productModel.getZoomImage().getPath());
			images.getImage().add(image);
		}
		if(null !=productModel.getDetailImage()){
			image = new Image();
			image.setImgType(PRODUCT_DETAIL_IMAGE);
			image.setImgUrl(URL_DOMAIN+productModel.getDetailImage().getPath());
			images.getImage().add(image);
		}
		if(null !=productModel.getFeatureImage()){
			image = new Image();
			image.setImgType(PRODUCT_FEATURE_IMAGE);
			image.setImgUrl(URL_DOMAIN+productModel.getFeatureImage().getPath());
			images.getImage().add(image);
		}
		if(null !=productModel.getCategoryImage()){
			image = new Image();					
			image.setImgType(PRODUCT_CATEGORY_IMAGE);
			image.setImgUrl(URL_DOMAIN+productModel.getCategoryImage().getPath());
			images.getImage().add(image);
		}
	}

	private static void populateAttributes(FDProductInfo fdProductInfo, FDProduct fdProduct, Product product,ProductModel productModel) {
		Attributes attributes = new Attributes();
		product.setAttributes(attributes);		
		
		for (Iterator iterator = EnumAttributeName.iterator(); iterator.hasNext();) {			
			ProdAttribute attribute = new ProdAttribute();			
			attributes.getProdAttribute().add(attribute);
			EnumAttributeName enumAttr = (EnumAttributeName) iterator.next();
			attribute.setAttrName(enumAttr.getName());	
			attribute.setAttrValue("");
			Object attrValue = null;
			if(EnumAttributeType.BOOLEAN.equals(enumAttr.getType())){
				attrValue = fdProduct.getMaterial().getAttributeBoolean(enumAttr);
			}else if(EnumAttributeType.INTEGER.equals(enumAttr.getType())){
				attrValue = fdProduct.getMaterial().getAttributeInt(enumAttr);
			}else{
				attrValue = fdProduct.getMaterial().getAttribute(enumAttr);
			}
			if(null != attrValue){
				if(attrValue instanceof Boolean){
					attribute.setAttrValue(attrValue.toString());
				}else{
					attribute.setAttrValue(""+attrValue);
				}
			}
			
			
		}	
		
	}

	private static void populateConfigurationsInfo(
			FDProduct fdProduct, Product product) {
		Configurations configurations = new Configurations();
		product.setConfigurations(configurations);			
		
		FDVariation[] fdVariations=fdProduct.getVariations();
		if(null !=fdVariations){
			for (int j = 0; j < fdVariations.length; j++) {
				FDVariation fdVariation = fdVariations[j];
				Configuration configuration = new Configuration();			
				configurations.getConfiguration().add(configuration);
				configuration.setCharName(fdVariation.getName());
				FDVariationOption[] options =fdVariation.getVariationOptions();
				if(null !=options){
					for (int k = 0; k < options.length; k++) {
						FDVariationOption fdVariationOption = options[k];
						CharacteristicValuePrice[] cvPrices =fdProduct.getPricing().getCharacteristicValuePrices();
						VariationOption variationOption = new VariationOption();
						double price = 0.0;
						String pricingUnit = "NA";
						if(null != cvPrices){
							for (int l = 0; l < cvPrices.length; l++) {
								CharacteristicValuePrice characteristicValuePrice = cvPrices[l];
								if(characteristicValuePrice.getCharValueName().equalsIgnoreCase(fdVariationOption.getName())){
									price = characteristicValuePrice.getPrice();
									pricingUnit = characteristicValuePrice.getPricingUnit();
									break;
								}
								
							}
						}
															
						variationOption.setCharValueName(fdVariationOption.getName());								
						variationOption.setCharValueDesc(fdVariationOption.getDescription());						
						variationOption.setPrice(BigDecimal.valueOf(price));
						variationOption.setPricingUnit(pricingUnit);
						
						configuration.getVariationOption().add(variationOption);
						
					}
				}
				
			}
		}
	}

	private static void populateInventoryInfo(FDProductInfo fdProductInfo,
			 Product product) {
				
		ErpInventoryModel inventoryModel = fdProductInfo.getInventory();
		if(null !=inventoryModel){
			Inventories inventories = new Inventories();
			product.setInventories(inventories);			
			Inventory inventory = new Inventory();			
			inventories.getInventory().add(inventory);
			Calendar cal = Calendar.getInstance();
			cal.setTime(inventoryModel.getInventoryStartDate());
			inventory.setStartDate(cal);		
			inventory.setQuantity(""+inventoryModel.getEntries().get(0).getQuantity());
		}
	}

	private static void populateRatingInfo(FDProductInfo fdProductInfo,
			 Product product) {
		Ratings ratings = new Ratings();
		product.setRatings(ratings);
		Rating rating = null;
		if(null !=fdProductInfo.getSustainabilityRating()){
			rating = new Rating();			
			ratings.getRating().add(rating);
					
			rating.setRatingType(SUSTAINABILITY_RATING);			
			rating.setDesc(fdProductInfo.getSustainabilityRating().getShortDescription());
			rating.setRatingType(fdProductInfo.getSustainabilityRating().getStatusCode());
		}
		if(null !=fdProductInfo.getRating()){
			rating = new Rating();			
			ratings.getRating().add(rating);
			
			rating.setRatingType(RATING);			
			rating.setDesc(fdProductInfo.getRating().getShortDescription());
			rating.setRatingType(fdProductInfo.getRating().getStatusCode());
		}
	}

	private static void populateGroupScalePriceInfo(String skucode,
			 Product product) throws FDResourceException{
		GroupScalePricing gsp = null;
		GroupPrices groupPrices;
		try {
			FDGroup fdGroup =FDCachedFactory.getProductInfo(skucode).getGroup();			
			gsp = null !=fdGroup?FDCachedFactory.getGrpInfo(fdGroup):null;
		} catch (FDSkuNotFoundException e) {
		} catch (FDGroupNotFoundException e) {
		}
		if(null != gsp){
			groupPrices = new GroupPrices();
			product.setGroupPrices(groupPrices);
			GroupPrice groupPrice = new GroupPrice();			
			groupPrices.getGroupPrice().add(groupPrice);
			
			groupPrice.setZoneCode(ZonePriceListing.MASTER_DEFAULT_ZONE);
			groupPrice.setGroupMaterials(gsp.getMatList().toString());
			if(null !=gsp.getGrpZonePrice(ZonePriceListing.MASTER_DEFAULT_ZONE)){
				groupPrice.setUnitPrice(BigDecimal.valueOf(gsp.getGrpZonePrice(ZonePriceListing.MASTER_DEFAULT_ZONE).getMaxUnitPrice()));
				groupPrice.setUnitWeight(gsp.getGrpZonePrice(ZonePriceListing.MASTER_DEFAULT_ZONE).getMaterialPrices()[0].getPricingUnit());
				groupPrice.setGroupQuantity(""+gsp.getGrpZonePrice(ZonePriceListing.MASTER_DEFAULT_ZONE).getMaterialPrices()[0].getScaleLowerBound());
			}
			groupPrice.setGroupId(gsp.getGroupId());
			groupPrice.setGroupDesc(gsp.getLongDesc());
		}
	}

	private static void populatePricingInfo(FDProduct fdProduct, 
			Product product) {
		Prices prices = new Prices();
		product.setPrices(prices);
		
		ZonePriceModel zpModel = fdProduct.getPricing().getZonePrice(ZonePriceListing.MASTER_DEFAULT_ZONE);
		MaterialPrice[] materialPrices =zpModel.getMaterialPrices();
		for (MaterialPrice materialPrice : materialPrices) {
			Price price = new Price();			
			prices.getPrice().add(price);
			
			price.setZoneCode(ZonePriceListing.MASTER_DEFAULT_ZONE);					
			price.setUnitPrice(BigDecimal.valueOf(materialPrice.getPrice()));
			price.setUnitDescription(materialPrice.getPricingUnit());
			price.setUnitWeight(materialPrice.getPricingUnit());
			
			if(materialPrice.getPromoPrice() > 0.0){
				price.setSalePrice(BigDecimal.valueOf(materialPrice.getPromoPrice()));
			}
			
			price.setScaleQuantity(""+materialPrice.getScaleLowerBound());
			
		}
	}

	private static void populateSalesUnitInfo(FDProduct fdProduct,
			 Product product) {
		FDSalesUnit[] salesUnits = fdProduct.getSalesUnits();
		SaleUnits saleUnits = new SaleUnits();
		product.setSaleUnits(saleUnits);		
		
		for (FDSalesUnit fdSalesUnit : salesUnits) {
			SaleUnit saleUnit = new SaleUnit();			
			saleUnits.getSaleUnit().add(saleUnit);
			
			saleUnit.setName(fdSalesUnit.getName());			
			saleUnit.setBaseUnit(fdSalesUnit.getBaseUnit());
			saleUnit.setDescription(fdSalesUnit.getDescription());						
		}
	}

	private static void populateNutritionInfo(FDProduct fdProduct,
			 Product product) {
		NutritionInfo nutritionInfo = new NutritionInfo();
		product.setNutritionInfo(nutritionInfo);			
		List<FDNutrition> nutritionList = fdProduct.getNutrition();
		if(null != nutritionList)
		for (Iterator iterator = nutritionList.iterator(); iterator
				.hasNext();) {
			FDNutrition fdNutrition = (FDNutrition) iterator.next();
			Nutrition nutrition = new Nutrition();			
			nutritionInfo.getNutrition().add(nutrition);
			nutrition.setNutritionType(fdNutrition.getName());
			nutrition.setUom(fdNutrition.getUnitOfMeasure());
			nutrition.setValue(BigDecimal.valueOf(fdNutrition.getValue()));
			
		}
	}

	
	private static Session getSftpSession(Properties config)
			throws JSchException {
		Session session=jsch.getSession(sftpUser, sftpHost, 22);
		session.setPassword(sftpPasswd);				
		session.setConfig(config);
		return session;
	}
   
}
