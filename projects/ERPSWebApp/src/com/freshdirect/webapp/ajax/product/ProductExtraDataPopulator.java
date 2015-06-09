package com.freshdirect.webapp.ajax.product;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.freshdirect.WineUtil;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.content.nutrition.EnumAllergenValue;
import com.freshdirect.content.nutrition.EnumClaimValue;
import com.freshdirect.content.nutrition.EnumOrganicValue;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDFactory;
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.fdstore.FDNutritionCache;
import com.freshdirect.fdstore.FDNutritionPanelCache;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.cache.EhCacheUtil;
import com.freshdirect.fdstore.content.AbstractProductModelImpl;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.Domain;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.EnumPopupType;
import com.freshdirect.fdstore.content.EnumProductLayout;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.MediaModel;
import com.freshdirect.fdstore.content.PopulatorUtil;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.TitledMedia;
import com.freshdirect.fdstore.content.view.WebHowToCookIt;
import com.freshdirect.fdstore.content.view.WebProductRating;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.util.HowToCookItUtil;
import com.freshdirect.fdstore.util.RatingUtil;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.BrandInfo;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.LabelAndLink;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.RecipeData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.SourceData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.WineData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.WineRating;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.MediaUtils;
import com.freshdirect.webapp.util.NutritionInfoPanelRendererUtil;

public class ProductExtraDataPopulator {
	private static final Logger LOG = LoggerFactory.getInstance( ProductExtraDataPopulator.class );

	public static ProductExtraData createExtraData( FDUserI user, ProductModel product, ServletContext ctx ) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		
		if ( product == null ) {
			BaseJsonServlet.returnHttpError( 500, "product not found" );
		}
		
		// Create response data object
		ProductExtraData data = new ProductExtraData();
		
		// First populate product-level data
		populateData( data, user, product,ctx );
		
		return data;
	}
	
	public static ProductExtraData createExtraData( FDUserI user, String productId, String categoryId, ServletContext ctx ) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		
		if ( productId == null ) {
			BaseJsonServlet.returnHttpError( 400, "productId not specified" );	// 400 Bad Request
		}
	
		// Get the ProductModel
		ProductModel product = PopulatorUtil.getProduct( productId, categoryId );
		
		return createExtraData( user, product, ctx );
	}
	
	
	private static void populateData(ProductExtraData data, FDUserI user,
			ProductModel productNode, ServletContext ctx) throws FDResourceException, FDSkuNotFoundException {

		final String popupPage = "/shared/popup.jsp";

		final DepartmentModel department = productNode.getDepartment();
		// determine department type
		final String deptName =  department.getContentName();
		final String deptFullName =  department.getFullName();
		
		SkuModel defaultSku = PopulatorUtil.getDefSku( productNode );
		if ( defaultSku == null ) {
			throw new FDSkuNotFoundException("No default SKU found for product " + productNode.getContentName());
		}
		
		FDProduct fdprd = null;
		FDProductInfo productInfo = null;
		if (defaultSku != null) {
			try {
				productInfo	= FDCachedFactory.getProductInfo(defaultSku.getSkuCode());
			} catch (FDResourceException e) {
				LOG.debug(e);
			} catch (FDSkuNotFoundException e) {
				LOG.debug(e);
			}
			try {
				fdprd		= FDCachedFactory.getProduct(productInfo);
			} catch (FDResourceException e) {
				LOG.debug(e);
			} catch (FDSkuNotFoundException e) {
				LOG.debug(e);
			}
		}


		// extract allergens
		{
			@SuppressWarnings("unchecked")
			Set<EnumAllergenValue> common = productNode.getCommonNutritionInfo(ErpNutritionInfoType.ALLERGEN);
			if (!common.isEmpty()) {
				List<String> aList = new ArrayList<String>(common.size());
				for (EnumAllergenValue allergen : common) {
					if (!EnumAllergenValue.getValueForCode("NONE").equals(allergen)) {
						aList.add(allergen.toString());
					}
				}
				data.setAllergens(aList);
			}
		}
		
		// product description
		{
			if (productNode.getProductDescription() != null) {
				Html tm = productNode.getProductDescription();
				try {
					data.setProductDescription( fetchMedia(tm.getPath(), user, false) );
				} catch (IOException e) {
					LOG.error("Failed to fetch product description media " + tm.getPath(), e);
				} catch (TemplateException e) {
					LOG.error("Failed to fetch product description media " + tm.getPath(), e);
				}
			}
		}
		
		// product desc media
		if ( productNode.getProductDescriptionNote() != null ) {
			TitledMedia tm = (TitledMedia) productNode.getProductDescriptionNote();
			try {
				data.setProductDescriptionNote( fetchMedia(tm.getPath(), user, false) );
			} catch (IOException e) {
				LOG.error("Failed to fetch product description note media " + tm.getPath(), e);
			} catch (TemplateException e) {
				LOG.error("Failed to fetch product description note media " + tm.getPath(), e);
			}
		}
		
		// product about media
		if ( productNode.getProductAbout() != null ) {
			TitledMedia tm = (TitledMedia) productNode.getProductAbout();
			try {
				data.setProductAboutMedia( fetchMedia(tm.getPath(), user, false) );
			} catch (IOException e) {
				LOG.error("Failed to fetch donenessGuides media " + tm.getPath(), e);
			} catch (TemplateException e) {
				LOG.error("Failed to fetch donenessGuides media " + tm.getPath(), e);
			}
		}

		// organic claims
		{
			@SuppressWarnings("unchecked")
			Set<EnumOrganicValue> commonOrgs = (Set<EnumOrganicValue>) productNode.getCommonNutritionInfo(ErpNutritionInfoType.ORGANIC);
			if (!commonOrgs.isEmpty()) {
				List<String> aList = new ArrayList<String>(commonOrgs.size());
				for (EnumOrganicValue claim : commonOrgs) {
					if(!EnumOrganicValue.getValueForCode("NONE").equals(claim)) {
						//Changed for APPDEV-705

						//check for different text than what Enum has (Enum shows in ERPSy-Daisy)
						if(EnumOrganicValue.getValueForCode("CERT_ORGN").equals(claim)){
							// %><div>&bull; Organic</div><%
							aList.add("Organic");
						}else{
							//don't use empty
							if ( !"".equals(claim.getName()) ) {
								// %><div>&bull; <%= claim.getName() %></div><%
								aList.add(claim.getName());
							}
						}
					}
				}

				data.setOrganicClaims(aList);
			}
		}


		// claims
		{
			@SuppressWarnings("unchecked")
			Set<EnumClaimValue> common = productNode.getCommonNutritionInfo(ErpNutritionInfoType.CLAIM);
			if (!common.isEmpty()) {
				List<String> aList = new ArrayList<String>(common.size());
				for (EnumClaimValue claim : common) {
					if (!EnumClaimValue.getValueForCode("NONE").equals(claim) && !EnumClaimValue.getValueForCode("OAN").equals(claim)) {
						//Changed for APPDEV-705

						//check for different text than what Enum has (Enum shows in ERPSy-Daisy)
						if(EnumClaimValue.getValueForCode("FR_ANTI").equals(claim)){
							// %><div>&bull; Raised Without Antibiotics</div><%
							aList.add("Raised Without Antibiotics");
						}else{
							// %><div style="margin-left:8px; text-indent: -8px;">&bull; <%= claim %></div><%
							aList.add(claim.toString());
						}
					}
				}
				data.setClaims(aList);
			}
		}


		// kosher type and symbol
		{
			PriceCalculator _pc = productNode.getPriceCalculator();

			final String kosherType = _pc.getKosherType();
			final String kosherSymbol = _pc.getKosherSymbol();

			if(!"".equalsIgnoreCase(kosherType)) {
				data.setKosherType(kosherType);
			}
			if (!"".equalsIgnoreCase(kosherSymbol)) {
				data.setKosherSymbol(kosherSymbol);

				data.setKosherIconPath("/media/editorial/kosher/symbols/"+kosherSymbol.toLowerCase()+"_s.gif");
				data.setKosherPopupPath( popupPage + "?attrib=KOSHER&amp;spec="+kosherSymbol.toLowerCase()+"&amp;tmpl=small");
			}
		}


		// heating instructions
		{
			if (fdprd != null && fdprd.hasNutritionInfo(ErpNutritionInfoType.HEATING)) {
				data.setHeatingInstructions( fdprd.getNutritionInfoString(ErpNutritionInfoType.HEATING) );
			}
		}
		
		// partially frozen / baked
		Html frozenMedia = productNode.getPartallyFrozen();
		if (frozenMedia != null) {
			try {
				data.setPartiallyFrozenMedia(fetchMedia(frozenMedia.getPath(), user, false));
				data.setFrozen(true);
			} catch (IOException e) {
				LOG.error("Failed to fetch partially frozen media " + frozenMedia.getPath(), e);
			} catch (TemplateException e) {
				LOG.error("Failed to fetch partially frozen media " + frozenMedia.getPath(), e);
			}
		}
		
		// Deprecated due to APPBUG-1705, preserved because you never know ...
		if (productNode.isHasPartiallyFrozen()) {
			if ("SEAFOOD".equalsIgnoreCase(deptFullName)) {
				// seafood department
				data.setFrozenSeafood(true);
			} else if ("BAKERY".equalsIgnoreCase(deptFullName)) {
				// bakery dept
				data.setFrozenBakery(true);
			}
			
		}

		// origin (or Country of Origin Label, a.k.a. COOL)
		if (productInfo != null) {
			final String skuCode = productInfo.getSkuCode();
			final List<String> coolList = productInfo.getCountryOfOrigin();
			if (coolList != null && !coolList.isEmpty()) {
				if (skuCode != null && (skuCode).startsWith("MEA")) {
					data.setOriginTitle("Born, Raised and Harvested in");
				} else {
					data.setOriginTitle("Origin");
				}
			}
		}
		if (productInfo != null) {
			final List<String> coolList = productInfo.getCountryOfOrigin();
			if (coolList != null && !coolList.isEmpty()) {
				data.setOrigin( AbstractProductModelImpl.getCOOLText(coolList) );
			}
		}
		
		// season text
		data.setSeasonText(productNode.getSeasonText());
		
		// deli buying guide
		// Example1: https://www.freshdirect.com/product.jsp?catId=ptdol&productId=ptdol_ptdklmtaoliv&trk=cpage&trkd=qb
		{
		    final Double cwHalfPint = productNode.getContainerWeightHalfPint();
			final Double cwPint = productNode.getContainerWeightPint();
			final Double cwQuart = productNode.getContainerWeightQuart();

			if ((cwHalfPint != null)) {
		    	
		    	Map<String, Double> dt = new HashMap<String, Double>();
		    	
		    	dt.put(ProductExtraData.KEY_CT_PINT05, cwHalfPint);
		    	if ((cwPint != null)) {
			    	dt.put(ProductExtraData.KEY_CT_PINT, cwPint);
		    		if ((cwQuart != null)) {
				    	dt.put(ProductExtraData.KEY_CT_QUART, cwQuart);
		    		}
		    	}
		    	
		    	data.setBuyerGuide(dt);
		    }
		}
		
		// cheese 101
		if ("CHE".equalsIgnoreCase(deptName)) {
			data.setCheese101(true);
			data.setCheeseText("Learn the essentials &#151; from buying to serving");
			data.setCheesePopupPath("/departments/cheese/101_selecting.jsp");
		} else {
			data.setCheese101(false);
		}


		// product brands and links to brand pages
		// TODO brand categories
		{
			final boolean isWineLayout = EnumProductLayout.NEW_WINE_PRODUCT.equals(productNode.getProductLayout());
			final int MAX_BRANDS_TO_SHOW = isWineLayout ? 1 : 2;
			
			// get the brand logo, if any.
			@SuppressWarnings("unchecked")
			List<BrandModel> prodBrands = productNode.getDisplayableBrands(MAX_BRANDS_TO_SHOW);

			/*
			 * Append "Ocean-Friendly Seafood" brand to product
			 *  if sustainability rating is over 4
			 * 
			 * According to ticket http://jira.freshdirect.com:8080/browse/APPDEV-2328
			 */
			if(defaultSku != null && FDStoreProperties.isSeafoodSustainEnabled()) {
				EnumSustainabilityRating enumRating = productInfo.getSustainabilityRating();
				if ( enumRating != null) {
					if ( enumRating != null && enumRating.isEligibleToDisplay() && (enumRating.getId() == 4 || enumRating.getId() == 5) ) {
						ContentNodeModel ssBrandCheck = ContentFactory.getInstance().getContentNode("bd_ocean_friendly");
						if (ssBrandCheck instanceof BrandModel) {
							prodBrands.add( (BrandModel)ssBrandCheck );
						}
					}
				}
			}

			/* Process brands */
			List<BrandInfo> bInfo = new ArrayList<BrandInfo>(prodBrands.size());
			for (final BrandModel bm : prodBrands) {
				Html brandAttrib = bm.getPopupContent();

				BrandInfo bi = new BrandInfo();
				bi.id = bm.getContentName();
				bi.name = bm.getFullName();
				bi.alt = bm.getAltText();

				// brand logo data
				Image blogo = bm.getLogoSmall();
				if (blogo != null) {
					bi.logoPath = blogo.getPath();
					bi.logoWidth = blogo.getWidth();
					bi.logoHeight = blogo.getHeight();
				}
				// brand popup size
				// Popup window URL: /shared/brandpop.jsp?brandId=<brand.id>
				if (brandAttrib != null) {
					TitledMedia tm = (TitledMedia) brandAttrib;
					/* bi.popupSize = tm.getPopupSize();
					EnumPopupType popupType = EnumPopupType.getPopupType(tm
							.getPopupSize());
					bi.popupWidth = popupType.getWidth();
					bi.popupHeight = popupType.getHeight(); */
					bi.contentPath = "/shared/brandpop.jsp?brandId="+bm.getContentKey().getId();
				}

				bInfo.add(bi);
			}
			data.setBrands(bInfo);
		}
		
		// nutritions
		{
			final boolean useCache = true;
			final String skuCode = defaultSku.getSkuCode();
			
			NutritionPanel panel = null;

			if ( useCache ) {
				// For storefront get panel from cache
				panel = FDNutritionPanelCache.getInstance().getNutritionPanel( skuCode );
			} else {
				// No caching for erpsadmin, just get the real stuff directly
				panel = ErpFactory.getInstance().getNutritionPanel( skuCode );
			}

			if (panel != null) {
				// nutritionMap.put(skuCode, panel);
				data.setNutritionPanel(panel);
			} else if (fdprd != null && fdprd.hasNutritionFacts()) {
				// old style
				
				ErpNutritionModel nutritionModel = FDNutritionCache.getInstance().getNutrition( defaultSku.getSkuCode() );

				if (nutritionModel != null) {
					try {
						StringWriter wr = new StringWriter();
						if (NutritionInfoPanelRendererUtil.renderClassicPanel(nutritionModel, false, wr, ctx)) {
							data.setOldNutritionPanel(wr.toString());
						}
					} catch (IOException e) {
						LOG.error("Failed to render old nutrition panel", e);
					}
				}
				
			} else {
				// generate classic nutrition panel
				
				
				LOG.warn("Not found new nutrition info data for SKU " + skuCode);
			}
		}
		
		// ** how to cook it **

		// doneness guides
		{
			
			List<Html> donenessGuides = productNode.getDonenessGuide();
			if (donenessGuides != null && donenessGuides.size() > 0) {
				List<LabelAndLink> dgList = new ArrayList<LabelAndLink>();

				for (Html media : donenessGuides) {
					LabelAndLink lal = new LabelAndLink((TitledMedia) media);
					
					dgList.add(lal);
				}
				
				data.setDonenessGuideList(dgList);
			}
		}

		// how-to cook it folders
		{
			List<WebHowToCookIt> howToCookItResult = HowToCookItUtil.getHowToCookIt( productNode );
			data.setHowToCookItList(howToCookItResult);
		}
		
		// fresh tips
		{
			final Html ftMedia = productNode.getFreshTips();
			if (ftMedia != null) {
				LabelAndLink lal = new LabelAndLink((TitledMedia) ftMedia);
				data.setFreshTips(lal);
			}
		}
		
		// product web-ratings
		{
			WebProductRating webProductRating = RatingUtil.getRatings(productNode);
			data.setWebRating( webProductRating );
		}
		
		// usage list
		{
			if (productNode.getUsageList() != null && productNode.getUsageList().size() > 0) {
				List<String> aList = new ArrayList<String>();
				for (Domain d : productNode.getUsageList()) {
					aList.add(d.getLabel());
				}
				data.setUsageList(aList);
			}
		}
		
		// storage guide (category level)
		{
			CategoryModel _parentNode = (CategoryModel) productNode
					.getParentNode();

//			if (_parentNode != null && _parentNode.getAliasCategory() != null) {
//				_parentNode = _parentNode.getAliasCategory();
//			}
//
			if (_parentNode != null && _parentNode.getCategoryStorageGuideMedia() != null) {
				data.setCategoryStorageGuideLabel( department.getFullName() +" Storage Guide");
				data.setCategoryStorageGuideLink( popupPage + "?catId=" + _parentNode.getContentName() + "&attrib=CAT_STORAGE_GUIDE_MEDIA&tmpl=large" );

				TitledMedia tm = (TitledMedia) _parentNode.getCategoryStorageGuideMedia();
				try {
					data.setCategoryStorageGuide(fetchMedia(tm.getPath(), user, false));
				} catch (IOException e) {
					LOG.error("Failed to fetch media " + tm.getPath(), e);
				} catch (TemplateException e) {
					LOG.error("Failed to fetch media " + tm.getPath(), e);
				}
			}
		}
		
		// ingredients
		{
			// FIXME might be part of nutritions data set
			if (fdprd != null) {
				data.setIngredients(fdprd.getIngredients());
			}
		}
		
		
		// serving suggestions
		{
			data.setServingSuggestions(productNode.getServingSuggestion());
		}
		
		// origin
		// TBD
		if (defaultSku.getVariationMatrix() != null && defaultSku.getVariationMatrix().size() > 0) {
			Map<String,SourceData> map = new HashMap<String,SourceData>();
			
			// media
			Html _fdDefGrade = productNode.getFddefGrade();
			Html _fdDefSource = productNode.getFddefSource();
			
			TitledMedia tm = null;
			for (DomainValue dv : defaultSku.getVariationMatrix()) {
				Domain d = dv.getDomain();
				final String dName = d.getContentName();
				
				if ("grade".equalsIgnoreCase(dName)) {
					tm = (TitledMedia)_fdDefGrade;
				} else if ("source".equalsIgnoreCase(dName)) {
					tm = (TitledMedia)_fdDefSource;
				} else {
					continue;
				}
				
				SourceData sd = new SourceData();
				sd.label = dv.getValue();
				if (tm != null) {
					sd.path = tm.getPath();
				}
				
				map.put(dName, sd);
			}
			
			if (map.size() > 0) {
				data.setSource(map);
			}
		}
		
		// related recipes
		{
			final String recipeCatId = productNode.getParentId();
			List<RecipeData> rdl = new ArrayList<RecipeData>();
			for (Recipe r : productNode.getRelatedRecipes()) {
				if (r.isAvailable()) {
					// 
					final String recipeId = r.getContentName();
					final String recipeName = r.getName();
					
					RecipeData rd = new RecipeData(recipeId, recipeCatId, recipeName);
					rdl.add(rd);
				}
			}
			if (rdl.size() > 0) {
				data.setRelatedRecipes(rdl);
			}
		}
		
		// *** wine info ***
		
		/* first check if product is a wine */
		//APPDEV 4131
		//if (department != null && (WineUtil.getWineAssociateId()).equalsIgnoreCase( department.getContentKey().getId() )) {
		
		if (department != null){
			WineData wd = new WineData();

			/** code snipped from  WineRegionLabel#doStart method */
			DomainValue wineCountry = productNode.getWineCountry();
			List<DomainValue> wineRegion = productNode.getNewWineRegion();
			String wineCity = productNode.getWineCity();
			if (wineCity.trim().length() == 0)
				wineCity = null;
			List<DomainValue> wineVintageList = productNode.getWineVintage();
			DomainValue wineVintage = wineVintageList.size() > 0 ? wineVintageList.get(0) : null;
			if (wineVintage != null && "vintage_nv".equals(wineVintage.getContentKey().getId())) {
				wineVintage = null;
			}

			List<DomainValue> wTypes = productNode.getNewWineType();
			List<String> typesList = new ArrayList<String>();
			List<String> wTypeIconPaths = new ArrayList<String>();
			for (DomainValue wtv : wTypes) {
				final String type = wtv.getValue();
				typesList.add(type);
				wTypeIconPaths.add("/media/editorial/win_"+WineUtil.getWineAssociateId().toLowerCase()+"/icons/"+wtv.getContentName().toLowerCase()+".gif");
			}
			wd.types = typesList;
			wd.typeIconPaths = wTypeIconPaths;
			
			List<String> varietals = new ArrayList<String>();
			for (DomainValue wvv : productNode.getWineVarietal()) {
				varietals.add(wvv.getValue());
			}
			wd.varietals = varietals;
			
			if (wineCountry != null) {
				wd.country = wineCountry.getValue();
			}
			if (!wineRegion.isEmpty()) {
				wd.region = wineRegion.get(0).getLabel();
			}
			if (wineCity != null) {
				wd.city = wineCity;
			}
			if (wineVintage != null) {
				wd.vintage = wineVintage.getValue();
			}

			wd.classification = productNode.getWineClassification();

			// grape type / blending
			wd.grape = productNode.getWineType();

			wd.importer = productNode.getWineImporter();
			
			wd.agingNotes = productNode.getWineAging();
			
			wd.alcoholGrade = productNode.getWineAlchoholContent();

			// reviews
			List<WineRating> ratings = new ArrayList<WineRating>();
			if (productNode.getWineRating1() != null && productNode.getWineRating1().size() > 0) {
				WineRating r = processWineRating(productNode.getWineRating1(), user, productNode.getWineReview1());
				if (r != null) {
					ratings.add(r);
				}
			}
			if (productNode.getWineRating2() != null && productNode.getWineRating2().size() > 0) {
				WineRating r = processWineRating(productNode.getWineRating2(), user, productNode.getWineReview2());
				if (r != null) {
					ratings.add(r);
				}
			}
			if (productNode.getWineRating3() != null && productNode.getWineRating3().size() > 0) {
				WineRating r = processWineRating(productNode.getWineRating3(), user, productNode.getWineReview3());
				if (r != null) {
					ratings.add(r);
				}
			}

			if (ratings.size() > 0) {
				wd.ratings = ratings;
			}

			if (productNode.hasWineOtherRatings()) {
				// ??
			}
			
			data.setWineData(wd);
		}
		
		// Storage guides
		CategoryModel parentCat = productNode.getCategory();
		MediaModel catStorage = parentCat.getCategoryStorageGuideMedia();
		if ( catStorage != null ) {
			data.setStorageGuideCat( catStorage.getPath() );
		}
		DepartmentModel parentDept = productNode.getDepartment();
		MediaModel deptStorage = parentDept.getDeptStorageGuideMedia();
		if ( deptStorage != null ) {
			data.setStorageGuideDept( deptStorage.getPath() );
		}
		data.setStorageGuideTitle( parentDept.getFullName() + " Storage Guide" );
		
		
		// --- --- //
		
		// Perishable product - freshness warranty
		if (FDStoreProperties.IsFreshnessGuaranteedEnabled() && productInfo.getFreshness() != null) {
			// method above returns either a positive integer encoded in string
			// or null
			data.setFreshness( Integer.parseInt(productInfo.getFreshness()) );
		}
		
		
		/* placeholder for product family products */
		{
			List<ProductData> familyProducts = new ArrayList<ProductData>();

			/*SkuModel sku = productNode.getDefaultSku();
			
			FDProductInfo prodInfo = FDCachedFactory.getProductInfo( sku.getSkuCode() );
			FDProduct fdProd = FDCachedFactory.getProduct( prodInfo );
			*/
			
			String selectedProdSku = productNode.getDefaultSkuCode();
			String familyID = productInfo.getFamilyID();
			ErpProductFamilyModel products = null;
			List<String> skuCodes = null;
			if(familyID == null){
				
				try {
					products = FDFactory.getSkuFamilyInfo(productInfo.getMaterialIds()[0]);
				} catch (FDGroupNotFoundException e) {
					
					e.printStackTrace();
				}
				//skuCodes = products.getSkuList();
				familyID = products.getFamilyId();
				if(familyID!=null){
				EhCacheUtil.putListToCache(EhCacheUtil.FD_FAMILY_PRODUCT_CACHE_NAME,familyID, products.getSkuList());
				}
			}
			skuCodes = EhCacheUtil.getListFromCache(EhCacheUtil.FD_FAMILY_PRODUCT_CACHE_NAME, familyID);
			
			if(skuCodes == null&&familyID!=null){
				
				try {
					products = FDFactory.getFamilyInfo(familyID);
				} catch (FDGroupNotFoundException e) {
					
					e.printStackTrace();
				}
				skuCodes = products.getSkuList();
				EhCacheUtil.putListToCache(EhCacheUtil.FD_FAMILY_PRODUCT_CACHE_NAME,familyID, products.getSkuList());
			}
			
				
				if(skuCodes!=null && selectedProdSku!=null){
				duplicateSku : for (String skuCode : skuCodes) {
	
				if(selectedProdSku.equalsIgnoreCase(skuCode))
				{continue duplicateSku;}
					
				ProductModel productModel = PopulatorUtil.getProduct(skuCode);

				ProductData pd = new ProductData();
				SkuModel skuModel = null;

				if (!(productModel instanceof ProductModelPricingAdapter)) {
					// wrap it into a pricing adapter if naked
					productModel = ProductPricingFactory.getInstance()
							.getPricingAdapter(productModel,
									user.getPricingContext());
				}

				if (skuModel == null) {
					skuModel = productModel.getDefaultSku();
				}
				//String skuCode = skuModel.getSkuCode();

				try {
					if(skuModel==null)
					{continue duplicateSku;}
					
					FDProductInfo productInfo_fam = skuModel.getProductInfo();
					FDProduct fdProduct = skuModel.getProduct();

					PriceCalculator priceCalculator = productModel
							.getPriceCalculator();

					ProductDetailPopulator.populateBasicProductData(pd, user,
							productModel);
					
					ProductDetailPopulator.populateProductData(pd, user,
							productModel, skuModel, fdProduct, priceCalculator,
							null, true, true);
					ProductDetailPopulator.populatePricing(pd, fdProduct,
							productInfo_fam, priceCalculator);

					try {
						ProductDetailPopulator.populateSkuData(pd, user,
								productModel, skuModel, fdProduct);
					} catch (FDSkuNotFoundException e) {
						LOG.error("Failed to populate sku data", e);
					} catch (HttpErrorResponse e) {
						LOG.error("Failed to populate sku data", e);
					}

					ProductDetailPopulator.postProcessPopulate(user, pd,
							pd.getSkuCode());

				} catch (FDSkuNotFoundException e) {
					LOG.warn("Sku not found: " + skuCode, e);
				}

				familyProducts.add(pd);
				
			}
		}
			//sortByPopularity(familyProducts);


			data.setFamilyProducts(familyProducts);
		}
		
		
	}


	private static void sortByPopularity(List<ProductData> familyProducts) {
		 Collections.sort(familyProducts, new Comparator<ProductData>() {
				@Override
				public int compare(ProductData o1, ProductData o2) {
					 return (o1.getCustomerRating() > o2.getCustomerRating()) ? 1 : -1;
					}
		         });
		
	}

	private static WineRating processWineRating(List<DomainValue> wineRatingsDV, FDUserI user, Html reviewMedia) {
		if (wineRatingsDV == null || wineRatingsDV.size() == 0)
			return null;
		
		WineRating r = new WineRating();
		// pick only the first rating
		DomainValue dv = wineRatingsDV.get(0);
		r.reviewer = dv.getDomainName();
		r.rating = dv.getValue();
		r.ratingKey = dv.getContentName();
		r.iconPath = "/media/editorial/win_"+WineUtil.getWineAssociateId().toLowerCase()+"/icons/rating_small/" + dv.getContentName() + ".gif";

		if (reviewMedia != null) {
			TitledMedia tm = (TitledMedia)reviewMedia;
			try {
				r.media = fetchMedia(tm.getPath(), user, false);
			} catch (IOException e) {
				LOG.error("Failed to fetch wine rating media " + tm.getPath(), e);
			} catch (TemplateException e) {
				LOG.error("Failed to fetch wine rating media " + tm.getPath(), e);
			}
		}
		
		return r;
	}
	

	@SuppressWarnings( "unused" )
	private static ProductExtraData.PopupContent processTitleMedia(TitledMedia tm, ProductExtraData.PopupContent target) {
		if (target == null) {
			target = new ProductExtraData.PopupContent();
		}

		target.popupTitle = tm.getMediaTitle();
		target.popupSize = tm.getPopupSize();
		EnumPopupType popupType = EnumPopupType.getPopupType(tm
				.getPopupSize());
		target.popupWidth = popupType.getWidth();
		target.popupHeight = popupType.getHeight();

		return target;
	}


	private static String fetchMedia(String mediaPath, FDUserI user, boolean quoted) throws IOException, TemplateException {
		if (mediaPath == null)
			return null;

		Map<String,Object> parameters = new HashMap<String,Object>();
		
		/* pass user/sessionUser by default, so it doesn't need to be added every place this tag is used. */
		parameters.put("user", (FDUserI)user);
		parameters.put("sessionUser", (FDSessionUser)user);
		
		StringWriter out = new StringWriter();
				
		MediaUtils.render(mediaPath, out, parameters, false, 
				user != null && user.getPricingContext() != null ? user.getPricingContext() : PricingContext.DEFAULT);

		String outString = out.toString();
		
		//fix media if needed
		outString = MediaUtils.fixMedia(outString);
		
		return quoted ? JSONObject.quote( outString ) : outString;
	}
}
