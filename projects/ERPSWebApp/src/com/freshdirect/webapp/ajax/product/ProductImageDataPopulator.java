package com.freshdirect.webapp.ajax.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.AbstractProductModelImpl;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.EnumProductLayout;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.product.data.ProductImageData;
import com.freshdirect.webapp.ajax.product.data.ProductImageData.ImageAtom;
import com.freshdirect.webapp.util.MediaUtils;

public class ProductImageDataPopulator {
	
	private static final Logger LOG = LoggerFactory.getInstance( ProductImageDataPopulator.class );

	public static ProductImageData createImageData( FDUserI user, ProductModel product ) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		
		if ( product == null ) {
			BaseJsonServlet.returnHttpError( 500, "product not found" );
		}
		
		ProductImageData data = new ProductImageData();
		populateData( data, user, product );
		
		return data;
	}
	

	public static ProductImageData createImageData( FDUserI user, String productId, String categoryId ) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		
		if ( productId == null ) {
			BaseJsonServlet.returnHttpError( 400, "productId not specified" );	// 400 Bad Request
		}
	
		// Get the ProductModel
		ProductModel product = PopulatorUtil.getProduct( productId, categoryId );
		
		return createImageData( user, product );
	}



	private static void populateData(ProductImageData data, FDUserI user, ProductModel productNode) throws FDSkuNotFoundException, FDResourceException {

		final DepartmentModel department = productNode.getDepartment();
		final SkuModel defaultSku = PopulatorUtil.getDefSku( productNode );		
		final FDProductInfo productInfo = defaultSku != null ? defaultSku.getProductInfo() : null;

		// the result
		List<ImageAtom> imageList = new ArrayList<ImageAtom>();

		// product images
		{
			// PROD_IMAGE_ZOOM
			if (productNode.getJumboImage() != null && !productNode.getJumboImage().isBlank()) {
				addImageWithLarge( productNode.getZoomImage(), productNode.getJumboImage(), imageList );
			} else {
				addImage( productNode.getZoomImage(), imageList );
			}
						
			// ALTERNATE_IMAGE
			addImage( productNode.getAlternateImage(), imageList );
			
			// DESCRIPTIVE_IMAGE (Wine Label)
			addImage( productNode.getDescriptiveImage(), imageList );
			
			// PROD_IMAGE (Secondary / Category Image)
			// addImage( productNode.getProdImage(), imageList );
			
			// PROD_IMAGE_PACKAGE
			addImage( productNode.getPackageImage(), imageList );

		}
		
		
		// brand images
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
			if(productInfo != null && FDStoreProperties.isSeafoodSustainEnabled()) {
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
			for (final BrandModel bm : prodBrands) {
				if ( bm.getLogoLarge() != null && bm.getLogoMedium() != null ) {
					addImageWithThumbnail( bm.getLogoLarge(), bm.getLogoMedium(), imageList );
				} else if ( bm.getLogoLarge() != null ) {
					addImage( bm.getLogoLarge(), imageList );
				} else if ( bm.getLogoMedium() != null ) {
					addImage( bm.getLogoMedium(), imageList );
				} else if ( bm.getLogoSmall() != null ) {
					addImage( bm.getLogoSmall(), imageList );
				} else {
					LOG.warn("Exclude brand " + bm.getContentName() + ", nothing to show");
				}
			}
		}
		
		// wine region
		/* first check if product is a wine */
		if (department != null && "usq".equalsIgnoreCase( department.getContentKey().getId() )) {
			List<DomainValue> wineRegion = productNode.getNewWineRegion();

			if (!wineRegion.isEmpty()) {
				DomainValue region = wineRegion.get(0);

				final String mediaPath = "/media/editorial/win_"+FDStoreProperties.getWineAssid().toLowerCase()+"/maps/"+region.getContentName()+".gif";

				if (MediaUtils.checkMedia(mediaPath)) {
					addImage( mediaPath, imageList );
				}
			}
		}
		
		data.setImages( imageList );
	}

	/**
	 * @see IMAGE_BLANK in {@link AbstractProductModelImpl#IMAGE_BLANK}
	 */
	private static void addImage(Image image, List<ImageAtom> coll) {
		if ( image != null && !image.isBlank() && coll != null ) {
			coll.add( new ImageAtom( image.getPath() ) );
		}
	}
	private static void addImageWithLarge(Image image, Image lgImage, List<ImageAtom> coll) {
		if ( image != null && !image.isBlank() && coll != null && lgImage != null && !lgImage.isBlank() ) {
			coll.add( new ImageAtom( image.getPath(), null, lgImage.getPath() ) );
		}
	}
	private static void addImageWithThumbnail(Image image, Image thImage, List<ImageAtom> coll) {
		if ( image != null && !image.isBlank() && coll != null && thImage != null && !thImage.isBlank() ) {
			coll.add( new ImageAtom( image.getPath(), thImage.getPath(), null ) );
		}
	}
	private static void addImage(String imageUrl, List<ImageAtom> coll) {
		if ( imageUrl != null && coll != null ) {
			coll.add( new ImageAtom( imageUrl ) );
		}
	}
}
