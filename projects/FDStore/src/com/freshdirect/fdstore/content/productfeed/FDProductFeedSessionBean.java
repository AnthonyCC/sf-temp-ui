package com.freshdirect.fdstore.content.productfeed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.EJBException;
import javax.mail.MessagingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.content.attributes.EnumAttributeType;
import com.freshdirect.content.nutrition.EnumClaimValue;
import com.freshdirect.content.nutrition.EnumKosherTypeValue;
import com.freshdirect.content.nutrition.EnumOrganicValue;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.fdstore.FDKosherInfo;
import com.freshdirect.fdstore.FDNutrition;
import com.freshdirect.fdstore.FDNutritionCache;
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
import com.freshdirect.fdstore.content.productfeed.Attributes.ProdAttribute;
import com.freshdirect.fdstore.content.productfeed.Brands.Brand;
import com.freshdirect.fdstore.content.productfeed.Claims.Claim;
import com.freshdirect.fdstore.content.productfeed.Configurations.Configuration;
import com.freshdirect.fdstore.content.productfeed.Configurations.Configuration.VariationOption;
import com.freshdirect.fdstore.content.productfeed.GroupPrices.GroupPrice;
import com.freshdirect.fdstore.content.productfeed.Images.Image;
import com.freshdirect.fdstore.content.productfeed.Inventories.Inventory;
import com.freshdirect.fdstore.content.productfeed.Ktyps.Ktyp;
import com.freshdirect.fdstore.content.productfeed.NutritionInfo.Nutrition;
import com.freshdirect.fdstore.content.productfeed.Orgns.Orgn;
import com.freshdirect.fdstore.content.productfeed.Prices.Price;
import com.freshdirect.fdstore.content.productfeed.Products.Product;
import com.freshdirect.fdstore.content.productfeed.Ratings.Rating;
import com.freshdirect.fdstore.content.productfeed.SaleUnits.SaleUnit;
import com.freshdirect.fdstore.content.productfeed.taxonomy.StoreTaxonomyFeedElement;
import com.freshdirect.fdstore.content.productfeed.taxonomy.TaxonomyFeedPopulator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.storeapi.StoreServiceLocator;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.BrandModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.fdstore.FDContentTypes;

public class FDProductFeedSessionBean extends SessionBeanSupport {

    private static final long serialVersionUID = 270497771665382812L;

    private static Category LOGGER = LoggerFactory.getInstance(FDProductFeedSessionBean.class);

    private static final String RATING = "RATING";
    private static final String SUSTAINABILITY_RATING = "SUSTAINABILITY_RATING";
    private static final String PRODUCT_CATEGORY_IMAGE = "PRODUCT_CATEGORY_IMAGE";
    private static final String PRODUCT_FEATURE_IMAGE = "PRODUCT_FEATURE_IMAGE";
    private static final String PRODUCT_DETAIL_IMAGE = "PRODUCT_DETAIL_IMAGE";
    private static final String PRODUCT_ZOOM_IMAGE = "PRODUCT_ZOOM_IMAGE";
    private static final String PRODUCT_IMAGE = "PRODUCT_IMAGE";
    private static final String URL_DOMAIN = "https://www.freshdirect.com";

    /**
     * Constructor.
     */
    public FDProductFeedSessionBean() {
        super();
    }

    /**
     * Template method that returns the cache key to use for caching resources.
     *
     * @return the bean's home interface name
     */
    @Override
    protected String getResourceCacheKey() {
        return "com.freshdirect.content.productfeed.ejb.FDProductFeedHome";
    }

    /**
     * Remove method for container to call.
     *
     * @throws EJBException
     *             throws EJBException if there is any problem.
     */
    @Override
    public void ejbRemove() {
    }

    public boolean uploadProductFeed() throws FDResourceException, RemoteException {
        try {
            LOGGER.info("Inside uploadProductFeed()..");
            Products xmlProducts = populateProducts();
            StoreTaxonomyFeedElement storeTaxonomyFeedElement = TaxonomyFeedPopulator.getInstance().populateStoreTaxonomyFeed();
            createAndUploadFeedFiles(xmlProducts, storeTaxonomyFeedElement);
            LOGGER.info("Available products fetched & uploaded: " + xmlProducts.getProduct().size());
        } catch (Exception e) {
            LOGGER.error("Exception :" + e.getMessage());
            throw new FDResourceException(e);
        }
        return true;
    }

    private Products populateProducts() throws FDResourceException {
        Products xmlProducts = new Products();
        Set<ContentKey> skuContentKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.SKU);
        if (skuContentKeys != null) {
            LOGGER.info("Skus in CMS: " + skuContentKeys.size());
            for (Iterator<ContentKey> i = skuContentKeys.iterator(); i.hasNext();) {
                ContentKey key = i.next();
                String skucode = key.getId();
                ProductModel productModel = null;
                FDProductInfo fdProductInfo = null;
                FDProduct fdProduct = null;
                	try {
                    productModel = ContentFactory.getInstance().getProduct(skucode);
                    if (productModel != null && !productModel.isOrphan() && !"Archive".equalsIgnoreCase(productModel.getDepartment().getContentName())) {
                        fdProductInfo = FDCachedFactory.getProductInfo(skucode);
                        fdProduct = FDCachedFactory.getProduct(fdProductInfo);
                    }
                } catch (FDSkuNotFoundException e) {
                    // Ignore
                }
                if (fdProductInfo != null && fdProduct != null) {
                    Product product = new Product();
                    xmlProducts.getProduct().add(product);
                    populateProductBasicInfo(productModel, fdProductInfo, fdProduct, product);
                    populateAttributes(fdProductInfo, fdProduct, product, productModel);
                    populatePricingInfo(fdProduct, product);
                    populateGroupScalePriceInfo(skucode, product);
                    populateSalesUnitInfo(fdProduct, product);
                    populateNutritionInfo(fdProduct, product);
                    populateConfigurationsInfo(fdProduct, product, productModel.getUserContext().getPricingContext());
                    populateRatingInfo(fdProductInfo, product, "1000"); // ::FDX:: What plant to consider?
                    populateInventoryInfo(fdProductInfo, product);
                    populateImages(productModel, product);
                    populateBrands(productModel, product);
                    populateClaims(fdProduct, product);				//claims of product
                    populateKtyps(fdProduct, product,"1000");		//KosherTypes of product
                    populateOrgns(product, fdProductInfo);			//OragnicTypes of product
                }
           }
        }
        return xmlProducts;
    }

    private void createAndUploadFeedFiles(Products xmlProducts, StoreTaxonomyFeedElement storeElement) throws FDResourceException {
        File zipFile = null;
        File productXmlFile = null;
        File taxonomyXmlFile = null;
        final EnumEStoreId actualStoreId = CmsManager.getInstance().getEStoreEnum();
        String productFeedFileName = createFeedFileName(actualStoreId, "Product");
        String taxonomyFeedFileName = createFeedFileName(actualStoreId, "Taxonomy");
        String zipFileName = productFeedFileName + ".zip";
        try {
            if (!xmlProducts.getProduct().isEmpty() && !storeElement.getDepartment().isEmpty()) {
                productXmlFile = createXmlFile(xmlProducts, productFeedFileName);
                taxonomyXmlFile = createXmlFile(storeElement, taxonomyFeedFileName);
                zipFile = createZipFile(Arrays.asList(productXmlFile, taxonomyXmlFile), zipFileName);
                uploadFeedFileToSubscribers(actualStoreId, zipFileName);
            } else {
                throw new FDResourceException("No feed file generated please check the log for error");
            }
        } finally {
            cleanupFeedFiles(Arrays.asList(productXmlFile, taxonomyXmlFile, zipFile));
        }
    }

    private void cleanupFeedFiles(List<File> files) {
        for (File file : files){
            if(file != null){
                LOGGER.info("File about to be deleted :" + file.getAbsolutePath());
                file.delete();
            }
        }
    }

    private void uploadFeedFileToSubscribers(final EnumEStoreId actualStoreId, String zipFileName) throws FDResourceException {
        List<ProductFeedSubscriber> productFeedSubscribers = collectProductFeedSubscribers();
        for (ProductFeedSubscriber subscriber : productFeedSubscribers) {
            try {
				uploadBySubscriber(actualStoreId, zipFileName, subscriber);
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String _msg=sw.getBuffer().toString();
				LOGGER.info(new StringBuilder("uploadBySubscriber failed with Exception...").append(_msg).toString());
				LOGGER.error(_msg);
				if(_msg!=null && _msg.indexOf("timed out while waiting to get an instance from the free pool")==-1) {
					email(Calendar.getInstance().getTime(), _msg, subscriber);
				}
			}
        }
    }

    private List<ProductFeedSubscriber> collectProductFeedSubscribers() throws FDResourceException {
        List<ProductFeedSubscriber> productFeedSubscribers = Collections.emptyList();
        Connection conn = null;
        try {
            conn = getConnection();
            productFeedSubscribers = ProductFeedDAO.getAllProductFeedSubscribers(conn);
        } catch (SQLException e) {
            LOGGER.error("FeedSubscriber DaO CONNECTION failed with SQLException: ", e);
            throw new FDResourceException("FeedSubscriber DAO CONNECTION failed with SQLException: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.error("connection close failed with SQLException: ", e);
                }
            }
        }
        return productFeedSubscribers;
    }

    private void uploadBySubscriber(final EnumEStoreId actualStoreId, String zipFileName, ProductFeedSubscriber subscriber) throws FDResourceException {
        if (!FDStoreProperties.isProductFeedGenerationDeveloperModeEnabled() && actualStoreId != null && subscriber.getStores() != null
                && subscriber.getStores().contains(actualStoreId)) {
            switch (subscriber.getType()) {
                case FTP:
                    FeedUploader.uploadToFtp(subscriber.getUrl(), subscriber.getUserid(), subscriber.getPassword(), subscriber.getDefaultUploadPath(), zipFileName);
                    break;
                case SFTP:
                    FeedUploader.uploadToSftp(subscriber.getUrl(), subscriber.getUserid(), subscriber.getPassword(), subscriber.getDefaultUploadPath(), zipFileName);
                    break;
                case S3:
                    FeedUploader.uploadToS3(subscriber.getUserid(), subscriber.getPassword(), subscriber.getUrl(), zipFileName);
                    break;
                default:
                    LOGGER.warn("Unknown product feed subscriber: " + subscriber);
            }
        }
    }

    private <T> File createXmlFile( T source, String fileName) throws FDResourceException {
        File rawFile = null;
        Marshaller marshaller;
        try {
            marshaller = JAXBContext.newInstance(source.getClass()).createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            rawFile = new File(fileName + ".xml");
            StreamResult result = new StreamResult(rawFile);
            marshaller.marshal(source, result);
        } catch (JAXBException e) {
            throw new FDResourceException(e, "Error creating the xml file: " + fileName);
        }
        return rawFile;
    }

    private File createZipFile(List<File> filesToZip, String zipFileName) throws FDResourceException {
        FileInputStream fileInputStream = null;
        ZipOutputStream zipOutputStream = null;
        File resultingZipFile = null;
        try {
            byte[] buffer = new byte[1024];

            FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);
            zipOutputStream = new ZipOutputStream(fileOutputStream);

            for (File file : filesToZip) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(zipEntry);
                int length;
                fileInputStream = new FileInputStream(file);
                while ((length = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, length);
                }
            }
            resultingZipFile = new File(zipFileName);
        } catch (Exception e) {
            throw new FDResourceException("feed serialization failed: " + e.getMessage(), e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (zipOutputStream != null) {
                    zipOutputStream.closeEntry();
                    zipOutputStream.close();
                }
            } catch (Exception e2) {
                LOGGER.error("Error while creating product feed zip file.", e2);
            }
        }
        return resultingZipFile;
    }

    private String createFeedFileName(final EnumEStoreId actualStoreId, final String prefix) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append("FD");
        if(prefix != null && !prefix.isEmpty()){
            fileNameBuilder.append(prefix);
        }
        fileNameBuilder.append("Feed_");
        if (actualStoreId != null) {
            fileNameBuilder.append(actualStoreId.getContentId());
        }
        fileNameBuilder.append("_").append(df.format(new Date()));
        String fileName = fileNameBuilder.toString();
        return fileName;
    }

    private void populateProductBasicInfo(ProductModel productModel, FDProductInfo fdProductInfo, FDProduct fdProduct, Product product) {
        ZoneInfo zone = productModel.getUserContext().getPricingContext().getZoneInfo();
        product.setSkuCode(fdProduct.getSkuCode());
        product.setUpc(fdProductInfo.getUpc());
        product.setMaterialNum(fdProduct.getMaterial().getMaterialNumber());
        product.setProdId(productModel.getContentName());
        product.setProdName(StringEscapeUtils.unescapeHtml(productModel.getFullName()));
        product.setProdUrl(URL_DOMAIN + StoreServiceLocator.previewLinkProvider().getLink(productModel.getContentKey(), ContentFactory.getInstance().getStoreKey(), true));
        populateParentInfo(productModel, product);
        product.setDeptId(productModel.getDepartment().getContentName());
        product.setProdStatus(fdProductInfo.getAvailabilityStatus(zone.getSalesOrg(), zone.getDistributionChanel()) != null ? fdProductInfo.getAvailabilityStatus(
                zone.getSalesOrg(), zone.getDistributionChanel()).getStatusCode() : "");
        product.setMinQuantity("" + productModel.getQuantityMinimum());
        product.setMaxQuantity("" + productModel.getQuantityMaximum());
        product.setQtyIncrement("" + productModel.getQuantityIncrement());
        // Required by Unbxd, [APPDEV-5329]
        product.setAkaName(productModel.getAka());
        product.setKeywords(productModel.getKeywords());
        product.setNotSearchable(productModel.isNotSearchable());
        product.setInvisible(productModel.isInvisible());
        product.setHideFromIphone(productModel.isHideIphone());
    }

    private void populateParentInfo(ProductModel productModel, Product product) {
        product.setCatId(productModel.getParentId());
        product.setParentCatId(productModel.getParentId());
        product.setRootCatId(productModel.getParentId());
        product.setSubCatId(productModel.getParentId());
        if (productModel.getCategory() != null) {
            product.setCategoryName(StringEscapeUtils.unescapeHtml(productModel.getCategory().getFullName())); // [APPDEV-5329]
            ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(productModel.getCategory().getParentId());
            if (contentNode != null && FDContentTypes.CATEGORY.equals(contentNode.getContentKey().getType())) {
                product.setParentCatId(contentNode.getContentName());
                contentNode = ContentFactory.getInstance().getContentNode(contentNode.getParentId());
                if (contentNode != null && FDContentTypes.CATEGORY.equals(contentNode.getContentKey().getType())) {
                    product.setRootCatId(contentNode.getContentName());
                } else {
                    product.setRootCatId(productModel.getCategory().getParentId());
                }
            }
        }
        if (productModel.getDepartment() != null) { // [APPDEV-5329]
            product.setDepartmentName(StringEscapeUtils.unescapeHtml(productModel.getDepartment().getFullName()));
        }
    }

    private void populateImages(ProductModel productModel, Product product) {
        Images images = new Images();
        product.setImages(images);
        Image image = null;
        if (productModel.getProdImage() != null) {
            image = createImage(PRODUCT_IMAGE, productModel, productModel.getProdImage().getPath());
            images.getImage().add(image);
        }
        if (productModel.getZoomImage() != null) {
            image = createImage(PRODUCT_ZOOM_IMAGE, productModel, productModel.getZoomImage().getPath());
            images.getImage().add(image);
        }
        if (productModel.getDetailImage() != null) {
            image = createImage(PRODUCT_DETAIL_IMAGE, productModel, productModel.getDetailImage().getPath());
            images.getImage().add(image);
        }
        if (productModel.getFeatureImage() != null) {
            image = createImage(PRODUCT_FEATURE_IMAGE, productModel, productModel.getFeatureImage().getPath());
            images.getImage().add(image);
        }
        if (productModel.getCategoryImage() != null) {
            image = createImage(PRODUCT_CATEGORY_IMAGE, productModel, productModel.getCategoryImage().getPath());
            images.getImage().add(image);
        }
    }

    private Image createImage(String imageType, ProductModel productModel, String imagePath) {
        Image image = new Image();
        image.setImgType(imageType);
        image.setImgUrl(URL_DOMAIN + imagePath);
        return image;
    }

    // Start :: Add Brand info for Hook logic
    private void populateBrands(ProductModel productModel, Product product) {
        Brands brands = new Brands();
        product.setBrands(brands);
        if (productModel.getBrands() != null) {
            for (BrandModel brandModel : productModel.getBrands()) {
                Brand brand = new Brand();
                brands.getBrand().add(brand);
                brand.setBrandName(StringEscapeUtils.unescapeHtml(brandModel.getFullName()));
            }
        }
    }
    // End:: Add Brand info for Hook logic
    
    private final static HashMap<EnumClaimValue,String> feedClaimTypes = new HashMap<EnumClaimValue,String>();
   	static {
   		feedClaimTypes.put(EnumClaimValue.getValueForCode("FR_DAIR"),"dairy free");
   		feedClaimTypes.put(EnumClaimValue.getValueForCode("FR_FAT"),"fat free");
   		feedClaimTypes.put(EnumClaimValue.getValueForCode("FR_GLUT"),"gluten free");
   		feedClaimTypes.put(EnumClaimValue.getValueForCode("GRASS_FED"),"grass fed");
   		feedClaimTypes.put(EnumClaimValue.getValueForCode("KOS_PAS"),"kosher for passover");
   		feedClaimTypes.put(EnumClaimValue.getValueForCode("FR_LACT"),"lactose free");
   		feedClaimTypes.put(EnumClaimValue.getValueForCode("LO_CALR"),"low calorie");
   		feedClaimTypes.put(EnumClaimValue.getValueForCode("LO_FAT"),"low fat");
   		feedClaimTypes.put(EnumClaimValue.getValueForCode("LO_SALT"),"low salt");
   		feedClaimTypes.put(EnumClaimValue.getValueForCode("LO_SODM"),"low sodium");
   		feedClaimTypes.put(EnumClaimValue.getValueForCode("VE_GAN"),"vegan");
   		feedClaimTypes.put(EnumClaimValue.getValueForCode("VEGGY"),"vegetarian");
   	}

    //Start:: populating claims of every product for search results sent to UNBXD APPDEV-6412
	private void populateClaims(FDProduct fdProduct, Product product) {
		Claims claims = new Claims();
		product.setClaims(claims);
		List<EnumClaimValue> claimValues = fdProduct.getClaims();
		if (null != claimValues && !claimValues.isEmpty()) {
			for (EnumClaimValue claimValue : claimValues) {
				for (EnumClaimValue code : feedClaimTypes.keySet()) {
					if (null != code) {
						if (claimValue.getCode().equals(code.getCode())) {
							Claim claim = new Claim();
							claims.getClaim().add(claim);
							claim.setCode(claimValue.getCode());
							claim.setName(claimValue.getName());
							claim.setPriority(claimValue.getPriority());
							claim.setKeyword(feedClaimTypes.get(code));
						}
					}
				}
			}
		}
	}
	//End
	private final static HashMap<EnumKosherTypeValue,String> feedKosherTypes = new HashMap<EnumKosherTypeValue,String>();
   	static {
   		feedKosherTypes.put(EnumKosherTypeValue.getValueForCode("NONE"), "Kosher");
   		feedKosherTypes.put(EnumKosherTypeValue.getValueForCode("DAIRY"),"Kosher");
   		feedKosherTypes.put(EnumKosherTypeValue.getValueForCode("PARVE"),"Kosher");
   		feedKosherTypes.put(EnumKosherTypeValue.getValueForCode("FISH"),"Kosher");
   		feedKosherTypes.put(EnumKosherTypeValue.getValueForCode("MEAT"),"Kosher");
   		feedKosherTypes.put(EnumKosherTypeValue.getValueForCode("DAIRY_EQ"),"Kosher");
   		feedKosherTypes.put(EnumKosherTypeValue.getValueForCode("GLATT"),"Kosher");
   	}
	//Start:: populating Kosher Type of every product for search results sent to UNBXD APPDEV-6412
	private void populateKtyps(FDProduct fdProduct, Product product, String plantID) {
		Ktyps ktyps = new Ktyps();
		product.setKtyps(ktyps);
		FDKosherInfo fdKosherInfo = fdProduct.getKosherInfo(plantID);
		if (null != fdKosherInfo && null != fdKosherInfo.getKosherType()) {
			EnumKosherTypeValue EnumKosherTypeValue = fdKosherInfo.getKosherType();
			for (EnumKosherTypeValue code : feedKosherTypes.keySet()) {
				if ((EnumKosherTypeValue.getCode()).equals(code.getCode())) {
					Ktyp ktyp = new Ktyp();
					ktyps.getKtyp().add(ktyp);
					ktyp.setCode(EnumKosherTypeValue.getCode());
					ktyp.setName(EnumKosherTypeValue.getName());
					ktyp.setKeyword(feedKosherTypes.get(code));
				}
			}
		}
	}

	private final static HashMap<EnumOrganicValue,String> feedOrganicTypes = new HashMap<EnumOrganicValue,String>();
	static {
		feedOrganicTypes.put(EnumOrganicValue.getValueForCode("CERT_ORGN"), "organic");
		feedOrganicTypes.put(EnumOrganicValue.getValueForCode("ORGN"), "organic");
	}
    
	//Start:: populating organic of every product for search results sent to UNBXD APPDEV-6412
	private void populateOrgns(Product product, FDProductInfo fdProductInfo) {
		Orgns orgns = new Orgns();
		product.setOrgns(orgns);
		List<EnumOrganicValue> nutritionIfo = FDNutritionCache.getInstance().getNutrition(fdProductInfo.getSkuCode()).getNutritionInfoList(ErpNutritionInfoType.ORGANIC);
		if (null != nutritionIfo && !nutritionIfo.isEmpty()) {
			for (EnumOrganicValue nutrition : nutritionIfo) {
				for (EnumOrganicValue code : feedOrganicTypes.keySet()) {
					if(null != code){
						if ((nutrition.getCode()).equals(code.getCode())) {
							Orgn orgn = new Orgn();
							orgns.getOrgn().add(orgn);
							orgn.setCode(nutrition.getCode());
							orgn.setName(nutrition.getName());
							orgn.setPriority(nutrition.getPriority());
							orgn.setKeyword(feedOrganicTypes.get(code));
						}
					}
				}
			}
		}
	}
	//end
		
    private void populateAttributes(FDProductInfo fdProductInfo, FDProduct fdProduct, Product product, ProductModel productModel) {
        Attributes attributes = new Attributes();
        product.setAttributes(attributes);

        for (Iterator<EnumAttributeName> iterator = EnumAttributeName.iterator(); iterator.hasNext();) {
            ProdAttribute attribute = new ProdAttribute();
            attributes.getProdAttribute().add(attribute);
            EnumAttributeName enumAttr = iterator.next();
            attribute.setAttrName(enumAttr.getName());
            attribute.setAttrValue("");
            Object attrValue = null;
            if (EnumAttributeType.BOOLEAN.equals(enumAttr.getType())) {
                attrValue = fdProduct.getMaterial().getAttributeBoolean(enumAttr);
            } else if (EnumAttributeType.INTEGER.equals(enumAttr.getType())) {
                attrValue = fdProduct.getMaterial().getAttributeInt(enumAttr);
            } else {
                attrValue = fdProduct.getMaterial().getAttribute(enumAttr);
            }
            if (attrValue != null) {
                if (attrValue instanceof Boolean) {
                    attribute.setAttrValue(attrValue.toString());
                } else {
                    attribute.setAttrValue("" + attrValue);
                }
            }
        }
    }

    private void populateConfigurationsInfo(FDProduct fdProduct, Product product, PricingContext pCtx) {
        Configurations configurations = new Configurations();
        product.setConfigurations(configurations);
        FDVariation[] fdVariations = fdProduct.getVariations();
        if (fdVariations != null) {
            for (int j = 0; j < fdVariations.length; j++) {
                FDVariation fdVariation = fdVariations[j];
                Configuration configuration = new Configuration();
                configurations.getConfiguration().add(configuration);
                configuration.setCharName(fdVariation.getName());
                FDVariationOption[] options = fdVariation.getVariationOptions();
                if (options != null) {
                    for (int k = 0; k < options.length; k++) {
                        FDVariationOption fdVariationOption = options[k];
                        CharacteristicValuePrice[] cvPrices = fdProduct.getPricing().getCharacteristicValuePrices(pCtx);
                        VariationOption variationOption = new VariationOption();
                        double price = 0.0;
                        String pricingUnit = "NA";
                        if (cvPrices != null) {
                            for (int l = 0; l < cvPrices.length; l++) {
                                CharacteristicValuePrice characteristicValuePrice = cvPrices[l];
                                if (characteristicValuePrice.getCharValueName().equalsIgnoreCase(fdVariationOption.getName())) {
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

    private void populateInventoryInfo(FDProductInfo fdProductInfo, Product product) {
        ErpInventoryModel inventoryModel = fdProductInfo.getInventory();
        if (inventoryModel != null) {
            Inventories inventories = new Inventories();
            product.setInventories(inventories);
            Inventory inventory = new Inventory();
            inventories.getInventory().add(inventory);
            Calendar cal = Calendar.getInstance();
            cal.setTime(inventoryModel.getInventoryStartDate());
            inventory.setStartDate(cal);
            inventory.setQuantity("" + inventoryModel.getEntries().get(0).getQuantity());
        }
    }

    private void populateRatingInfo(FDProductInfo fdProductInfo, Product product, String plantID) {
        Ratings ratings = new Ratings();
        product.setRatings(ratings);
        Rating rating = null;
        if (fdProductInfo.getSustainabilityRating(plantID) != null) {
            rating = new Rating();
            ratings.getRating().add(rating);
            rating.setRatingType(SUSTAINABILITY_RATING);
            rating.setDesc(fdProductInfo.getSustainabilityRating(plantID).getShortDescription());
            rating.setRatingType(fdProductInfo.getSustainabilityRating(plantID).getStatusCode());
        }
        if (fdProductInfo.getRating(plantID) != null) {
            rating = new Rating();
            ratings.getRating().add(rating);
            rating.setRatingType(RATING);
            rating.setDesc(fdProductInfo.getRating(plantID).getShortDescription());
            rating.setRatingType(fdProductInfo.getRating(plantID).getStatusCode());
        }
    }

    private void populateGroupScalePriceInfo(String skucode, Product product) throws FDResourceException {
        GroupScalePricing gsp = null;
        GroupPrices groupPrices;
        try {
            FDGroup fdGroup = FDCachedFactory.getProductInfo(skucode).getGroup(ZonePriceListing.DEFAULT_ZONE_INFO);//DEFAULT_SALES_ORG, ZonePriceListing.DEFAULT_DIST_CHANNEL);
            gsp = fdGroup != null ? FDCachedFactory.getGrpInfo(fdGroup) : null;
        } catch (FDSkuNotFoundException e) {
            // DO Nothing
        } catch (FDGroupNotFoundException e) {
            // DO Nothing
        }
        if (gsp != null) {
            groupPrices = new GroupPrices();
            product.setGroupPrices(groupPrices);
            GroupPrice groupPrice = new GroupPrice();
            groupPrices.getGroupPrice().add(groupPrice);
            groupPrice.setZoneCode(ZonePriceListing.DEFAULT_ZONE_INFO.getPricingZoneId()); // ::FDX::
            groupPrice.setGroupMaterials(gsp.getMatList().toString());
            if (gsp.getGrpZonePrice(ZonePriceListing.DEFAULT_ZONE_INFO) != null) { // ::FDX::
                groupPrice.setUnitPrice(BigDecimal.valueOf(gsp.getGrpZonePrice(ZonePriceListing.DEFAULT_ZONE_INFO).getMaxUnitPrice())); // ::FDX::
                groupPrice.setUnitWeight(gsp.getGrpZonePrice(ZonePriceListing.DEFAULT_ZONE_INFO).getMaterialPrices()[0].getPricingUnit()); // ::FDX::
                groupPrice.setGroupQuantity("" + gsp.getGrpZonePrice(ZonePriceListing.DEFAULT_ZONE_INFO).getMaterialPrices()[0].getScaleLowerBound()); // ::FDX::
            }
            groupPrice.setGroupId(gsp.getGroupId());
            groupPrice.setGroupDesc(gsp.getLongDesc());
        }
    }

    private void populatePricingInfo(FDProduct fdProduct, Product product) {
        Prices prices = new Prices();
        product.setPrices(prices);
        ZonePriceModel zpModel = fdProduct.getPricing().getZonePrice(ZonePriceListing.DEFAULT_ZONE_INFO);
        MaterialPrice[] materialPrices = zpModel.getMaterialPrices();
        for (MaterialPrice materialPrice : materialPrices) {
            Price price = new Price();
            prices.getPrice().add(price);
            price.setZoneCode(ZonePriceListing.MASTER_DEFAULT_ZONE);
            price.setUnitPrice(BigDecimal.valueOf(materialPrice.getPrice()));
            price.setUnitDescription(materialPrice.getPricingUnit());
            price.setUnitWeight(materialPrice.getPricingUnit());
            if (materialPrice.getPromoPrice() > 0.0) {
                price.setSalePrice(BigDecimal.valueOf(materialPrice.getPromoPrice()));
            }
            price.setScaleQuantity("" + materialPrice.getScaleLowerBound());
        }
    }

    private void populateSalesUnitInfo(FDProduct fdProduct, Product product) {
        FDSalesUnit[] salesUnits = fdProduct.getSalesUnits();
        SaleUnits saleUnits = new SaleUnits();
        product.setSaleUnits(saleUnits);
        for (FDSalesUnit fdSalesUnit : salesUnits) {
            SaleUnit saleUnit = new SaleUnit();
            saleUnits.getSaleUnit().add(saleUnit);
            saleUnit.setName(fdSalesUnit.getName());
            saleUnit.setBaseUnit(fdSalesUnit.getBaseUnit());
            saleUnit.setDescription(fdSalesUnit.getDescription());
            if ("LB".equalsIgnoreCase(fdSalesUnit.getBaseUnit()) && !"LB".equalsIgnoreCase(fdSalesUnit.getName())) {
                saleUnit.setEstimatedWeight("" + (fdSalesUnit.getNumerator() / (double) fdSalesUnit.getDenominator()));
            }
        }
    }

    private void populateNutritionInfo(final FDProduct fdProduct, final Product product) {
        NutritionInfo nutritionInfo = new NutritionInfo();
        product.setNutritionInfo(nutritionInfo);
        List<FDNutrition> nutritionList = fdProduct.getNutrition();
        if (nutritionList != null) {
            for (Iterator<FDNutrition> iterator = nutritionList.iterator(); iterator.hasNext();) {
                FDNutrition fdNutrition = iterator.next();
                Nutrition nutrition = new Nutrition();
                nutritionInfo.getNutrition().add(nutrition);
                nutrition.setNutritionType(fdNutrition.getName());
                nutrition.setUom(fdNutrition.getUnitOfMeasure());
                nutrition.setValue(BigDecimal.valueOf(fdNutrition.getValue()));

            }
        }
    }

    private static void email(Date processDate, String exceptionMsg, ProductFeedSubscriber subscriber) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="FDProductFeed upload for subscriber: "+(null !=subscriber?subscriber.getDescription():"")+": "+ (processDate != null ? dateFormatter.format(processDate) : " ");
			StringBuffer buff = new StringBuffer();
			buff.append("<html>").append("<body>");
			if(exceptionMsg != null) {
				buff.append("<b>").append(exceptionMsg).append("</b>");
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
					subject, buff.toString(), true, "");

		}catch (MessagingException e) {
			LOGGER.warn("Error notification(email) failed for FDProductFeed upload to subscriber: "+(null !=subscriber?subscriber.getDescription():""), e);
		}

	}
}
