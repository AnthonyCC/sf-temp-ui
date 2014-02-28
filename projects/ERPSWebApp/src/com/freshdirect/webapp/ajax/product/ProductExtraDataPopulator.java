package com.freshdirect.webapp.ajax.product;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.content.nutrition.EnumAllergenValue;
import com.freshdirect.content.nutrition.EnumClaimValue;
import com.freshdirect.content.nutrition.EnumOrganicValue;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDNutritionCache;
import com.freshdirect.fdstore.FDNutritionPanelCache;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
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
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.TitledMedia;
import com.freshdirect.fdstore.content.view.WebHowToCookIt;
import com.freshdirect.fdstore.content.view.WebProductRating;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.HowToCookItUtil;
import com.freshdirect.fdstore.util.RatingUtil;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.BrandInfo;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.LabelAndLink;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.RecipeData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.SourceData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.WineData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.WineRating;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.JspMethods;
import com.freshdirect.webapp.util.MediaUtils;
import com.freshdirect.webapp.util.NutritionInfoPanelRendererUtil;
import com.freshdirect.webapp.util.RestrictionUtil;

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
			productInfo	= FDCachedFactory.getProductInfo(defaultSku.getSkuCode());
			fdprd		= FDCachedFactory.getProduct(productInfo);
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

			if (!"".equalsIgnoreCase(kosherType)
					&& !"".equalsIgnoreCase(kosherSymbol)) {
				data.setKosherType(kosherType);
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
			} else if (fdprd.hasNutritionFacts()) {
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
		if (department != null && "usq".equalsIgnoreCase( department.getContentKey().getId() )) {
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
				wTypeIconPaths.add("/media/editorial/win_usq/icons/"+wtv.getContentName().toLowerCase()+".gif");
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
		
		
		// ==============
		//   Messaging
		// ==============
		
		// Party platter cancellation notice
		if ( productNode.isPlatter() ) {
			data.setMsgCancellation( "* Orders for this item cancelled after 3PM the day before delivery (or Noon on Friday/Saturday/Sunday and major holidays) will be subject to a 50% fee." ); 
		}

		// Party platter cutoff notice (header+text)
		TimeOfDay cutoffTime = RestrictionUtil.getPlatterRestrictionStartTime();
		if ( productNode.isPlatter() && cutoffTime != null ) {
			String headerTime;
			String bodyTime;
			if (new TimeOfDay("12:00 PM").equals(cutoffTime)) {
				headerTime = "10AM";
				bodyTime = "10AM";
			} else {
				SimpleDateFormat headerTimeFormat = new SimpleDateFormat("h:mm a");
				SimpleDateFormat bodyTimeFormat = new SimpleDateFormat("ha");
				
				headerTime = headerTimeFormat.format(cutoffTime.getAsDate());
				bodyTime = bodyTimeFormat.format(cutoffTime.getAsDate());
			}
			data.setMsgCutoffHeader( "Order by " + headerTime + " for Delivery Tomorrow" );
			data.setMsgCutoffNotice( "Freshly made item. Please <b>complete checkout by " + bodyTime + "</b> to order for delivery tomorrow." );
			
		}

		
		// Limited availability messaging
		
		// msgDayOfWeek		- Blocked days of the week notice		
		// msgDeliveryNote	- Another blocked days of the week notice
		
		DayOfWeekSet blockedDays = productNode.getBlockedDays();
		if (!blockedDays.isEmpty()) {
			int numOfDays=0;
			StringBuffer daysStringBuffer = null;
			boolean isInverted=true;
			
			if (blockedDays.size() > 3) {
				numOfDays = (7-blockedDays.size() );
			 	daysStringBuffer= new StringBuffer(blockedDays.inverted().format(true));
			} else {
				isInverted=false;
			  	daysStringBuffer = new StringBuffer(blockedDays.format(true));
				numOfDays = blockedDays.size();
			}
			
			
			if (numOfDays > 1 ) {
				//** make sundays the last day, if more than one in the list 
				if (daysStringBuffer.indexOf("Sundays, ")!=-1)  {
					daysStringBuffer.delete(0,9);
					daysStringBuffer.append(" ,Sundays");
				}
				
				//replace final comma with "and" or "or"
				int li = daysStringBuffer.lastIndexOf(",");
				daysStringBuffer.replace(li,li+1,(isInverted ?" and ": " or ") );
			}
			
			data.setMsgDayOfWeekHeader( "Limited Availability" );
			data.setMsgDayOfWeek( "This item is <b>" + ( isInverted ? "only" : "not" ) + "</b> available for delivery on <b>" + daysStringBuffer.toString() + "</b>." );
			
			data.setMsgDeliveryNote( "Only available for delivery on " + blockedDays.inverted().format(true) + "." );
		}
		
		

		// Lead time message
		if ( fdprd != null ) {
			int leadTime = fdprd.getMaterial().getLeadTime();		
			if( leadTime > 0 && FDStoreProperties.isLeadTimeOasAdTurnedOff() ) {
				data.setMsgLeadTimeHeader( JspMethods.convertNumToWord(leadTime) + "-Day Lead Time" );
				data.setMsgLeadTime( "Freshly made item. Please complete checkout at least two days in advance. (Order by Thursday for Saturday)." );
			}
		}
		
		// Kosher restrictions
		if ( fdprd != null && fdprd.getKosherInfo() != null && fdprd.getKosherInfo().isKosherProduction() ) {
			StringBuilder buf = new StringBuilder( "* Not available for delivery on Friday, Saturday, or Sunday morning." );
			
			DlvRestrictionsList globalRestrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date startDate = cal.getTime();

			cal.add(Calendar.DATE, 7);
			Date endDate = cal.getTime();
			
			DateRange dateRange = new DateRange(startDate, endDate);

			List<RestrictionI> kosherRestrictions = globalRestrictions.getRestrictions(
					EnumDlvRestrictionCriterion.DELIVERY,
					EnumDlvRestrictionReason.KOSHER,
					EnumDlvRestrictionType.ONE_TIME_RESTRICTION,
					dateRange);

	    	if ( kosherRestrictions.size() > 0 ) {
	    		buf.append( "<br/>Also unavailable during " );
	    		int s = kosherRestrictions.size();
				for ( int i = 0; i < s; i++ ) {
					RestrictionI r = kosherRestrictions.get( i );
					buf.append( "<b>" + r.getName() + "</b>, " + r.getDisplayDate() + ( ( i == s - 1 ) ? "." : "; " ) );
				}
	    	}
			
			data.setMsgKosherRestriction( buf.toString() );
		}
		
		// earliest availability - product not yet available but will in the near future
		if (defaultSku != null) {
			data.setMsgEarliestAvailability( defaultSku.getEarliestAvailabilityMessage() );
		}



		// --- --- //
		
		// Perishable product - freshness warranty
		if (FDStoreProperties.IsFreshnessGuaranteedEnabled() && productInfo.getFreshness() != null) {
			// method above returns either a positive integer encoded in string
			// or null
			data.setFreshness( Integer.parseInt(productInfo.getFreshness()) );
		}
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
		r.iconPath = "/media/editorial/win_usq/icons/rating_small/" + dv.getContentName() + ".gif";

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
