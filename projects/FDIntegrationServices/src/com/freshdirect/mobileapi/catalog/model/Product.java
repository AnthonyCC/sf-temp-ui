package com.freshdirect.mobileapi.catalog.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.freshdirect.content.nutrition.EnumClaimValue;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
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
	private final WineAttributes wineAttributes;
	private final List<String> keywords;
	private final int productLayout;

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
		wineAttributes = builder.wineAttributes;
		keywords = builder.keywords;
		productLayout=builder.productLayout;
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
	
	public WineAttributes getWineAttributes() {
		return wineAttributes;
	}
	
	public List<String> getKeywords(){
		return this.keywords;
	}
	public int getProductLayout() {
		return productLayout;
	}

	public static final class WineAttributes {
		
		private String country;
		private List<String> regions;
		private String city;
		private List<String> type;
		private List<String> variety; // called Varietal in CMS for some reason
		private List<String> vintage;
		
		//Ratings not being transfered for nwo
		//TODO: add ratings and reviews??
		
		private String classificationText;
		private String importer;
		private String alchoholContent;
		private String againgNotes;
		private List<String> wineGrapes;
		
		//Bottom 2 are not used at all for now
		
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public List<String> getRegions() {
			return regions;
		}
		public void setRegions(List<String> regions) {
			this.regions = regions;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public List<String> getType() {
			return type;
		}
		public void setType(List<String> type) {
			this.type = type;
		}
		public List<String> getVariety() {
			return variety;
		}
		public void setVariety(List<String> variety) {
			this.variety = variety;
		}
		public List<String> getVintage() {
			return vintage;
		}
		public void setVintage(List<String> vintage) {
			this.vintage = vintage;
		}
		public String getClassificationText() {
			return classificationText;
		}
		public void setClassificationText(String classificationText) {
			this.classificationText = classificationText;
		}
		public String getImporter() {
			return importer;
		}
		public void setImporter(String importer) {
			this.importer = importer;
		}
		public String getAlchoholContent() {
			return alchoholContent;
		}
		public void setAlchoholContent(String alchoholContent) {
			this.alchoholContent = alchoholContent;
		}
		public String getAgaingNotes() {
			return againgNotes;
		}
		public void setAgaingNotes(String againgNotes) {
			this.againgNotes = againgNotes;
		}
		public List<String> getWineGrapes() {
			return wineGrapes;
		}
		public void setWineGrapes(List<String> wineGrapes) {
			this.wineGrapes = wineGrapes;
		}
		
	}
	
	public static class ProductBuilder {
		
		private final String id;
		private final String fullName;
		private List<String> brandTags;
		private List<com.freshdirect.mobileapi.catalog.model.Image> images;
		private List<String> tags;
		private List<String> keywords;
		private float minQty=0;
		private float maxQty=0;
		private float incrementQty=0;
		private String quantityText="";
		private String primaryBrand;
		private String primaryHome;
		private SkuInfo skuInfo;
		private WineAttributes wineAttributes;
		private int productLayout;
		
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
        
        public ProductBuilder tags( List<TagModel> tags) {
        	
        	//This now returns all tags instead of just current ones
        	if(this.tags == null)
        		this.tags = new ArrayList<String>(tags.size());
        	
        	if(isEmpty(tags)){
        		return this;
        	}

        	for(TagModel tag:tags) {
        		this.tags.add(tag.getName());
        	}
        	
        	/*
            if(isEmpty(tags))
        		this.tags=EMPTY;
        	
        	this.tags=new ArrayList<String>(tags.size());
        	for(TagModel tag:tags) {
        		this.tags.add(tag.getKeywords());
        	}
        	*/
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
        
        public ProductBuilder generateAdditionTagsFromProduct(ProductModel pm){
        	
        	if(pm == null)
        		return this;
        	
        	SortedSet<String> additionTags = new TreeSet<String>();
            try {
    			PriceCalculator pricing = pm.getPriceCalculator();
    			if (pricing.getKosherPriority() != 999 && pricing.getKosherPriority() != 0) {
    				additionTags.add("Kosher");
    			}
    			if (pricing.getProduct()!=null && pricing.getProduct().getClaims() != null) {
    				for (EnumClaimValue claim : pricing.getProduct().getClaims()) {
    					if ("FR_GLUT".equals(claim.getCode())) {
    						additionTags.add("Gluten Free");
    						break;
    					}
    				}
    			}
    			if (pricing.getDealPercentage() > 0 || pricing.getTieredDealPercentage() > 0 || pricing.getGroupPrice() != 0.0) {
    				additionTags.add("Sale");
    			}
    			
    			if (pm.isFullyAvailable()) {
    				if (pm.isBackInStock() || pm.isNew()) {
    					additionTags.add("New/Back in stock");
    				}
    			}
    			
    			FDProduct fdProduct = pricing.getProduct();
    			if (fdProduct != null) 
    			{
    				boolean organicClaim = fdProduct.hasOANClaim();
    				if(organicClaim) {
    					additionTags.add("Organic");
    				}
    			}
    			
    			String fullName = pm.getFullName();
    			if(fullName != null && fullName.toLowerCase().contains("organic")){
    				additionTags.add("Organic");			
    			}
            }  catch (FDResourceException e) {
    		} catch (FDSkuNotFoundException e) {
    		}
        	
            tags.addAll(additionTags);
            
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
		public ProductBuilder productLayout(int val) {
			productLayout = val;
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
        
        public ProductBuilder addKeyWords(String keywords){
//        	this.keywords = keywords;
        	if(keywords == null || keywords.isEmpty())
        		return this;
        	
        	String[] splitKeyWords = keywords.split(",");
        	if(splitKeyWords.length <= 0)
        		return this;
        	
        	List<String> tmp = new ArrayList<String>(splitKeyWords.length);
        	for(String s : splitKeyWords){
        		tmp.add(s.trim());
        	}
        	this.keywords = tmp;
        	return this;
        }
        
        public ProductBuilder generateWineAttributes(ProductModel pm){
        	if(pm != null){
        		wineAttributes = new WineAttributes();
        		
        		if(pm.getWineCity() != null && !pm.getWineCity().isEmpty())
        			wineAttributes.setCity(pm.getWineCity());
        		try {
        			//I think this does what I want
        			wineAttributes.setCountry(pm.getWineCountry() == null ? null : pm.getWineCountry().getLabel());
        		} catch (Exception ignored){}
        		
        		//Generating and Setting wine regions. there might be multiple later
        		List<String> tmp = new ArrayList<String>();
        		for(DomainValue dm : pm.getNewWineRegion()){
        			tmp.add(dm.getLabel());
        		}
        		if(tmp.size() > 0){
        			wineAttributes.setRegions(tmp);
        		}
        		
        		//Possibly will need to string split? need to check;
        		tmp = new ArrayList<String>();
        		for(DomainValue dm : pm.getNewWineType()){
            		tmp.add(dm.getLabel());        			
        		}
        		if(tmp.size() >0){
        			wineAttributes.setType(tmp);
        		}
        		tmp = new ArrayList<String>();
        		for(DomainValue dm : pm.getWineVarietal()){
        			tmp.add(dm.getLabel());
        		}
        		if(tmp.size() > 0)
        			wineAttributes.setVariety(tmp);
        		tmp = new ArrayList<String>();
        		for(DomainValue dm : pm.getWineVintage()){
        			tmp.add(dm.getLabel());
        		}
        		
        		if(tmp.size() > 0){
        			wineAttributes.setVintage(tmp);
        		}
        		
        		
        		if(pm.getWineClassification() != null && !pm.getWineClassification().isEmpty())
                	wineAttributes.setClassificationText(pm.getWineClassification());
        		
        		if(pm.getWineImporter() != null && !pm.getWineImporter().isEmpty())
            		wineAttributes.setImporter(pm.getWineImporter());
        		
        		if(pm.getWineAlchoholContent() != null && !pm.getWineAlchoholContent().isEmpty())
        			wineAttributes.setAlchoholContent(pm.getWineAlchoholContent());
        		
        		if(pm.getWineAging() != null && !pm.getWineAging().isEmpty())
        			wineAttributes.setAgaingNotes(pm.getWineAging());
        		
        		String grapes = pm.getWineType();
        		if(grapes != null && !grapes.isEmpty()){
        			String splitGrapes[] = grapes.split(",");
        			tmp = new ArrayList<String>();
        			for(String grape : splitGrapes){
        				tmp.add(grape.trim());
        			}
        			if(tmp.size() > 0){
        				wineAttributes.setWineGrapes(tmp);
        			}
        		}
        		
        	}
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