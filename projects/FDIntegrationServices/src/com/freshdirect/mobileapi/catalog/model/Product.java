package com.freshdirect.mobileapi.catalog.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.TagModel;

public class Product {
	
	private final String id;
	private final String fullName;
	private final List<String> brandTags;
	private final List<String> tags;
	private final List<Image> images;
	private final float minQty;
	private final float maxQty;
	private final float incrementQty;
	private final String quantityText;
	private final String primaryBrand;
	private final String primaryHome;
	private final SkuInfo skuInfo;
	
	
	

	private Product(ProductBuilder builder) {
		id=builder.id;
		fullName=builder.fullName;
		brandTags=builder.brandTags;
		images=builder.images;
		minQty=builder.minQty;
		maxQty=builder.maxQty;
		incrementQty=builder.incrementQty;
		tags=builder.tags;
		primaryBrand=builder.primaryBrand;
		primaryHome=builder.primaryHome;
		quantityText=builder.quantityText;
		skuInfo=builder.skuInfo;
	}
	
	public String getId() {
		return id;
	}

	public List<String> getTags() {
		return tags;
	}

	public String getPrimaryBrand() {
		return primaryBrand;
	}

	public String getPrimaryHome() {
		return primaryHome;
	}

	public String getFullName() {
		return fullName;
	}

	public List<String> getBrandTags() {
		return brandTags;
	}

	public List<Image> getImages() {
		return images;
	}

	public float getMinQty() {
		return minQty;
	}

	public float getMaxQty() {
		return maxQty;
	}

	public float getIncrementQty() {
		return incrementQty;
	}

	public String getQuantityText() {
		return quantityText;
	}

	public SkuInfo getSkuInfo() {
		return skuInfo;
	}
	public static class ProductBuilder {
		
		private final String id;
		private final String fullName;
		private  List<String> brandTags;
		private  List<com.freshdirect.mobileapi.catalog.model.Image> images;
		private  List<String> tags;
		private float minQty=0;
		private float maxQty=0;
		private float incrementQty=0;
		private String quantityText="";
		private String primaryBrand;
		private String primaryHome;
		private SkuInfo skuInfo;
		private static final List<String> EMPTY=Collections.<String>emptyList();
		
		private static final List<com.freshdirect.mobileapi.catalog.model.Image> EMPTY_IMAGE=Collections.<com.freshdirect.mobileapi.catalog.model.Image>emptyList();
		
		private static boolean isEmpty(List<?> values) {
			return (values==null ||values.size()==0)?true:false;
		}
		private static boolean isEmpty(Set<?> values) {
			return (values==null ||values.size()==0)?true:false;
		}
        public ProductBuilder(String id, String fullName) {
            this.id = id;
            this.fullName= fullName;
        }

        public ProductBuilder brandTags(List<BrandModel> brands) {
        	if(isEmpty(brands))
        		this.brandTags=EMPTY;
        	
        	this.brandTags=new ArrayList<String>(brands.size());
        	for(BrandModel brand:brands) {
        		brandTags.add(brand.getFullName());
        	}
            return this;
        }
        
        public ProductBuilder images(List<com.freshdirect.fdstore.content.Image> images) {
        	if(isEmpty(images))
        		this.images=EMPTY_IMAGE;
        	this.images=new ArrayList<com.freshdirect.mobileapi.catalog.model.Image>(images.size());
        	
        	for(com.freshdirect.fdstore.content.Image image:images) {
        		this.images.add(new com.freshdirect.mobileapi.catalog.model.Image(image));
        		
        	}
            
            return this;
        }
        
        public ProductBuilder tags( Set<TagModel> tags) {
        	
            if(isEmpty(tags))
        		this.tags=EMPTY;
        	
        	this.tags=new ArrayList<String>(tags.size());
        	for(TagModel tag:tags) {
        		this.tags.add(tag.getKeywords());
        	}
        	/*FDProduct fdProduct = productModel.get
			if (fdProduct != null) 
			{
				boolean organic = fdProduct.hasOANClaim();
				if(organic) {
					types.add("Organic");
				}			
			}*/
            return this;
        }
        
        public ProductBuilder minQty(float val) {
             minQty = val;
             return this;
        }
        
        public ProductBuilder maxQty(float val) {
        	maxQty = val;
        	return this;
        }
        
       
        public ProductBuilder incrementQty(float val) {
            incrementQty = val;
            return this;
        }
        
        public ProductBuilder primaryHome(String val) {
        	primaryHome = val;
            return this;
        }
        
        public ProductBuilder primaryBrand(String val) {
        	primaryBrand = val;
            return this;
        }
        
        public ProductBuilder quantityText(String val) {
        	quantityText = val;
            return this;
        }
        public ProductBuilder skuInfo(SkuInfo skuInfo) {
        	this.skuInfo=skuInfo;
        	return this;
        }
        public Product build() {
            return new Product(this);
        }
        
        /*Images: categoryImage,confirmImage,alternateImage,alternateProductImage,descriptiveImage,detailImage,extraImage,featureImage,itemImage,jumboImage,thumbnailImage */
    }
/*
	FDGroup:null
	activeYmalSet:null
	age:-1.0
	aka:X
	allDomainValues:[null, DomainValue[Domain[organic], false, false, 0]]
	allTags:[]
	alsoSoldAsName:X
	alsoSoldAsRefs:[]
	altText:
	alternateImage:null
	alternateProductImage:null
	autoconfigurable:false
	autoconfiguration:null
	backInStock:false
	backInStockAge:2.147483647E9
	backInStockDate:null
	blockedDays:DayOfWeekSet[]
	blurb:null
	brands:[bd_advil]
	browseRecommenderType:NONE
	category:fdx_drug_genmerch
	categoryImage:Image[null, /media/images/product/hba_two/hba_advil_cldsin_c.jpg, 80x80]
	completeTheMeal:[]
	componentGroups:[]
	confirmImage:Image[null, /media/images/layout/clear.gif, 1x1]
	containerWeightHalfPfloat:null
	containerWeightPfloat:null
	containerWeightQuart:null
	contentKey:ContentKey[Product:hba_advil_cldsin]
	contentName:hba_advil_cldsin
	contentType:P
	countryOfOrigin:[]
	crossSellProducts:[]
	dealPercentage:0
	defaultPrice:
	defaultPriceOnly:
	defaultSku:null
	defaultSkuCode:null
	defaultTemporaryUnavSku:null
	defaultUnitOnly:
	department:fdx_drug
	descriptiveImage:null
	detailImage:Image[null, /media/images/product/hba_two/hba_advil_cldsin_p.jpg, 150x150]
	disableAtpFailureRecommendation:false
	disabledRecommendations:false
	discontinued:true
	displayable:false
	displayableBasedOnCms:true
	donenessGuide:[]
	earliestAvailability:null
	editorial:null
	editorialTitle:null
	excludedForEBTPayment:false
	excludedRecommendation:false
	expertWeight:0
	extraImage:Image[null, /media_stat/images/layout/clear.gif, 1x1]
	fddefFrenching:null
	fddefGrade:null
	fddefRipeness:null
	fddefSource:null
	featureImage:Image[null, /media/images/product/hba_two/hba_advil_cldsin_f.jpg, 30x30]
	freshTips:null
	freshnessGuaranteed:null
	frozen:false
	fullName:Advil Caplets
	fullyAvailable:false
	giftcardType:[]
	glanceName:Advil Caplets
	grocery:true
	hasPartiallyFrozen:false
	hasSalesUnitDescription:false
	heatRating:-1
	hidden:false
	hideIphone:false
	hideUrl:null
	hideWineRatingPricing:false
	highestDealPercentage:0
	howtoCookitFolders:[]
	inPrimaryHome:false
	incrementMaxEnforce:true
	invisible:false
	itemImage:Image[null, /media_stat/images/layout/clear.gif, 1x1]
	jumboImage:Image[null, /media_stat/images/layout/clear.gif, 1x1]
	keywords:null
	layout:GENERIC
	navName:Advil Caplets
	new:false
	newAge:2.147483647E9
	newDate:null
	newWineRegion:[]
	newWineType:[]
	notSearchable:false
	nutritionMultiple:false
	orphan:false
	outOfSeason:false
	packageDescription:
	packageImage:Image[null, /media_stat/images/layout/clear.gif, 1x1]
	parentId:fdx_drug_genmerch
	parentKeys:[ContentKey[Category:fdx_drug_genmerch], ContentKey[Category:fdx_drug_mdccab_cold]]
	parentNode:fdx_drug_genmerch
	parentYmalSetSource:null
	partallyFrozen:null
	path:www.freshdirect.com/fdx_drug/fdx_drug_genmerch/hba_advil_cldsin
	perfectPair:null
	perishable:true
	platter:false
	preconfigured:false
	preferredSku:null
	priceCalculator:com.freshdirect.fdstore.content.PriceCalculator@4273007b
	primaryBrandName:Advil
	primaryHome:fdx_drug_mdccab_cold
	primaryProductModel:hba_advil_cldsin
	primarySkus:[HBA0063853]
	priority:7
	prodImage:Image[null, /media/images/product/hba_two/hba_advil_cldsin_c.jpg, 80x80]
	prodPageRatings:
	prodPageTextRatings:
	productAbout:null
	productBottomMedia:null
	productBundle:[]
	productDescription:TitledMedia: [, , Html[null, /media/editorial/hba/pain_reliever/hba_advil_cldsin_desc.txt] ]
	productDescriptionNote:null
	productLayout:EnumProductLayout:[ Name: Perishable Product Layout id: 1 Path:/includes/product/perishable_product.jsp
	productQualityNote:null
	productRating:
	productRatingEnum:X : No rating
	productTerms:null
	productTermsMedia:null
	qualifiedForPromotions:false
	quantityIncrement:1.0
	quantityMaximum:99.99
	quantityMinimum:1.0
	quantityText:Quantity
	quantityTextSecondary:null
	rating:[DomainValue[Domain[organic], false, false, 0]]
	ratingProdName:
	ratingRelatedImage:null
	recommendTable:null
	recommendedAlternatives:[]
	redirectUrl:null
	relatedProducts:[]
	relatedRecipes:[]
	retainOriginalSkuOrder:false
	rolloverImage:null
	salesUnitDescription:null
	salesUnitLabel:
	seafoodOrigin:
	searchable:true
	seasonText:null
	sellBySalesunit:
	servingSuggestion:null
	showSalesUnitImage:false
	showTopTenImage:false
	showWineRatings:false
	sideNavImage:null
	skuCodes:[HBA0063853]
	skus:[HBA0063853]
	soldBySalesUnits:false
	sourceProduct:hba_advil_cldsin
	specialLayout:null
	storeNode:null
	subtitle:
	sustainabilityRating:
	sustainabilityRatingEnum:X : No rating
	tags:[]
	tempUnavailable:false
	templateType:com.freshdirect.fdstore.content.EnumTemplateType@f30d00d
	temporaryUnavailable:false
	temporaryUnavailableOrAvailable:false
	thumbnailImage:Image[null, /media/images/product/hba_two/hba_advil_cldsin_f.jpg, 30x30]
	tieredDealPercentage:0
	unavailable:true
	unitOfMeasure:null
	upSellProducts:[]
	usageList:[]
	userContext:com.freshdirect.common.context.UserContext@391f72ce
	variationMatrix:[]
	variationOptions:[]
	weRecommendImage:[]
	weRecommendText:[]
	wineAging:
	wineAlchoholContent:
	wineCity:
	wineClassification:
	wineClassifications:[null]
	wineCountry:null
	wineCountryKey:null
	wineDomainValues:[null]
	wineFyi:null
	wineImporter:
	wineRating1:[]
	wineRating2:[]
	wineRating3:[]
	wineRatingValue1:null
	wineRatingValue2:null
	wineRatingValue3:null
	wineRegion:null
	wineReview1:null
	wineReview2:null
	wineReview3:null
	wineType:null
	wineVarietal:[]
	wineVfloatage:[]
	ymalCategories:[]
	ymalHeader:null
	ymalProducts:[]
	ymalRecipes:[]
	ymalSets:[]
	ymals:[]
	zoomImage:Image[null, /media/images/product/hba_two/hba_advil_cldsin_z.jpg, 260x260]
			*/
	

}
/*
ProdId	string	Primary Key
FullName	string	 
BrandTags	List<string>	 
Tags	List<string>	 
Images	List<string>	 
QtyMin	float	 
QtyMax	float	 
QtyIncrement	float	 
LastUpdate	Timestamp*/