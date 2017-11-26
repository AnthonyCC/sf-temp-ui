<%@ page import="com.freshdirect.fdstore.FDProductInfo"%>
<%@ page import='com.freshdirect.cms.core.domain.ContentKey' %>
<%@ page import='com.freshdirect.cms.core.domain.ContentKeyFactory' %>
<%@ page import='com.freshdirect.cms.core.domain.ContentType' %>
<%@ page import='com.freshdirect.storeapi.content.ContentFactory' %>
<%@ page 
		contentType="image/jpg" 
		import='java.net.URL, java.awt.*, java.awt.event.*, java.awt.image.*, java.io.*, javax.imageio.*, com.freshdirect.storeapi.content.ProductModel
		, com.freshdirect.storeapi.content.*, com.freshdirect.webapp.util.*, com.freshdirect.framework.util.log.LoggerFactory, org.apache.log4j.Category, com.freshdirect.storeapi.content.ContentFactory
		, com.freshdirect.framework.util.NVL, com.freshdirect.fdstore.FDStoreProperties, com.freshdirect.webapp.util.JspMethods' 
%><% 

/* Make sure there's no wayward returns or spaces outside of code brackets, or the image could be corrupted. */

/*
 *	Query param settable values:
 *
 *		iW : image width
 *		iH : image height
 *		iBG : image background color (hex code as FFF or FFFFFF)
 *		iQ : image quality (float, default set at 1.0)
 *
 *		pId : product ID
 *		pSize : product image size code-letter (cx, p, c, cr, z, etc)
 *		pOx : product offset x
 *		pOy : product offset y
 *
 *		bSize : burst size (sm or lg. no value is also valid for no burst)
 *		bType : burst type (deal, fave, new, X) Using X forces no burst to be used.
 *		bVal : burst value (manually set deal amout to show, otherwise fetches from product)
 *		bOx : burst offset x
 *		bOy : burst offset y
 *
 *	The image is created like:
 *		background
 *		product
 *		overlay
 *
 *		So any offsets are always against the BACKGROUND layer.
 */

%><%!
	static Category LOGGER = LoggerFactory.getInstance("genImg");

	void log (boolean debug, String strToPrint) {
		if (debug) { LOGGER.debug(strToPrint); }
	}
%><%
String plantID=ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
boolean myDebug = true;
log(myDebug, "Starting generateImage (test)...");

BufferedImage prodImage = null;
BufferedImage overlay = null;
BufferedImage rating = null;
BufferedImage background = null;

//get the media path
String mediaPath = FDStoreProperties.getMediaPath();
//make the media static path
String mediaStaticPath = mediaPath.replaceAll("content/", "static/docroot/");


//all of the functions return a path with a starting slash, so remove trailing slash from base urls
if (!"".equals(mediaPath) && "/".equals(mediaPath.charAt(mediaPath.length()-1))) {
	mediaPath = mediaPath.substring(0, mediaPath.lastIndexOf("/"));
}
if (!"".equals(mediaStaticPath) && "/".equals(mediaStaticPath.charAt(mediaStaticPath.length()-1))) {
	mediaStaticPath = mediaStaticPath.substring(0, mediaStaticPath.lastIndexOf("/"));
}

URL ratingUrl = null;
URL overlayUrl = null;
URL prodImgUrl = new URL(mediaPath+"/media/images/temp/soon_80x80.gif"); //default, including if pSize is unavailable

//deals - //media_stat/images/deals/brst_sm_" + deal + (supportsPNG ? ".png" : ".gif");
//fave - src=\"//media_stat/images/bursts/brst_sm_fave"+(supportsPNG ? ".png" : ".gif")+"\" width=\"35px\" height=\"35px\
//new - //media_stat/images/bursts/brst_sm_new"+(supportsPNG ? ".png" : ".gif")+"\" width=\"35px\" height=\"35px\

//defaults
	int prodWidth = 0;
	int prodHeight = 0;
	String pSize = NVL.apply(request.getParameter("pSize"), "c"); //this is the code-letter (cx,p,c,ct,cr,z, etc)
	String pSizeTemp = "";

	//check for a passed pOx
		int pOx = 0;
		String pOxTemp = NVL.apply(request.getParameter("pOx"), "0");
		pOx = Integer.valueOf(pOxTemp).intValue();

	//check for a passed pOy
		int pOy = 0;
		String pOyTemp = NVL.apply(request.getParameter("pOy"), "0");
		pOy = Integer.valueOf(pOyTemp).intValue();

	String bType = NVL.apply(request.getParameter("bType"), "auto");
	String bSize = NVL.apply(request.getParameter("bSize"), "sm");
	String bVal = "-1";
	int bValTemp = 0;

	//check for a passed pId
	String id = NVL.apply(request.getParameter("pId"), "");

	int overlayWidth = 0;
	int overlayHeight = 0;

	//check for a passed bOx
		int bOx = 0;
		String bOxTemp = NVL.apply(request.getParameter("bOx"), "0");
		bOx = Integer.valueOf(bOxTemp).intValue();

	//check for a passed bOy
		int bOy = 0;
		String bOyTemp = NVL.apply(request.getParameter("bOy"), "0");
		bOy = Integer.valueOf(bOyTemp).intValue();

	
	String rType = NVL.apply(request.getParameter("rType"), "add");
	String rVal = "-1";
	int rValTemp = 0;
	boolean ratingOnly = false;
		//check for passed rVal
		if (request.getParameter("rVal") != null && request.getParameter("rVal") != "") { rVal = request.getParameter("rVal"); }

	int ratingWidth = 0;
	int ratingHeight = 0;

	boolean rCent = "on".equals(NVL.apply(request.getParameter("rCent"), "false"));


	//check for a passed rOx
		int rOx = 0;
		String rOxTemp = NVL.apply(request.getParameter("rOx"), "NaN");
		if (!"NaN".equals(rOxTemp)) {
			rOx = Integer.valueOf(rOxTemp).intValue();
		}

	//check for a passed rOy
		int rOy = 0;
		String rOyTemp = NVL.apply(request.getParameter("rOy"), "NaN");
		if (!"NaN".equals(rOyTemp)) {
			rOy = Integer.valueOf(rOyTemp).intValue();
		}

	//check for a passed retImageWidth
		int retImageWidth = 0;
		String retImageWidthTemp = NVL.apply(request.getParameter("iW"), "0");
		retImageWidth = Integer.valueOf(retImageWidthTemp).intValue();

	//check for a passed retImageHeight
		int retImageHeight = 0;
		String retImageHeightTemp = NVL.apply(request.getParameter("iH"), "0");
		retImageHeight = Integer.valueOf(retImageHeightTemp).intValue();

	//check for a passed iQ
		float iQ = 1;
		String iQTemp = NVL.apply(request.getParameter("iQ"), "1");
		iQ = Float.valueOf(iQTemp).floatValue();

	String bgColorString = "";
	int bgR = 255;
	int bgG = 255;
	int bgB = 255;

	Color bgColor = new Color(bgR, bgG, bgB); //default to white

	//check for a passed bgColor
	if (request.getParameter("iBG") != null && request.getParameter("iBG") != "") {
		//parse bgColor into rgb

		bgColorString = request.getParameter("iBG");

		if (bgColorString.length() == 3) {
			bgR = Integer.valueOf(bgColorString.substring(0,1)+bgColorString.substring(0,1), 16).intValue();
			bgG = Integer.valueOf(bgColorString.substring(1,2)+bgColorString.substring(1,2), 16).intValue();
			bgB = Integer.valueOf(bgColorString.substring(2,3)+bgColorString.substring(2,3), 16).intValue();
		}else if (bgColorString.length() == 6) {
			bgR = Integer.valueOf(bgColorString.substring(0,2), 16).intValue();
			bgG = Integer.valueOf(bgColorString.substring(2,4), 16).intValue();
			bgB = Integer.valueOf(bgColorString.substring(4,6), 16).intValue();
		}
		
		bgColor = new Color(bgR, bgG, bgB);

	}

	
//check if we only want a rating
if ("exc".equalsIgnoreCase(rType)) {
	ratingOnly = true;
	//we only want a rating, so we need to move some things around
	//change "product" image to the clear image
	prodImgUrl = new URL(mediaPath+"/media/images/layout/clear.gif");
	//since we're only doing a rating, set it up
	if ("1,2,3,4,5,6,7,8,9,10,01,02,03,04,05,06,07,08,09".indexOf(rVal) > -1) {
		if (rVal.length() == 1) {
			rVal = "0"+rVal; //add leading zero
		}
		ratingUrl = new URL(mediaStaticPath+"/media_stat/images/ratings/"+rVal+".gif");
	}
}


if ( !"".equals(id)) {

	
	//start as a product here
	id = "Product:"+request.getParameter("pId");

	//get product model
	ProductModel product = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(ContentKeyFactory.get(id));

	//if not a product, check for a SKU instead
	if (product == null) {
		id = "Sku:"+request.getParameter("pId");
		//get sku model
		SkuModel sku = (SkuModel) ContentFactory.getInstance().getContentNodeByKey(ContentKeyFactory.get(id));

		if (sku != null) {
			//found a valid SKU
			log(myDebug, "Using sku : "+sku);
			if ("-1".equals(rVal)) {
			    FDProductInfo f = sku.getProductInfo();
				rVal = f.getRating(plantID) != null ? f.getRating(plantID).getStatusCode() : null;
				//no need to add leading zero to rating here, we'll do it further down
			}
			product = sku.getProductModel();
		}

	}

	//check if product model is null
	if (product != null) {
		//check for pSize
		if ( !"".equals(pSize) ) {
			pSizeTemp = pSize;
			if ("a".equals(pSizeTemp) || "c".equals(pSizeTemp) || "cr".equals(pSizeTemp) || "ct".equals(pSizeTemp) || "cx".equals(pSizeTemp) || "p".equals(pSizeTemp) || "f".equals(pSizeTemp) || "z".equals(pSizeTemp)) {
				pSize = pSizeTemp;
			}
		}

		//check that we have a product image and path
		if ("a".equals(pSize)) {
			if (product.getAlternateImage() != null && product.getAlternateImage().getPath() != "") {
				prodImgUrl = new URL(mediaPath+product.getAlternateImage().getPath());
			}
		}else if ("c".equals(pSize)) {
			if (product.getProdImage() != null && product.getProdImage().getPath() != "") {
				prodImgUrl = new URL(mediaPath+product.getProdImage().getPath());
			}
		}else if ("ct".equals(pSize)) {
			if (product.getCategoryImage() != null && product.getCategoryImage().getPath() != "") {
				prodImgUrl = new URL(mediaPath+product.getCategoryImage().getPath());
			}
		}else if ("cr".equals(pSize)) {
			if (product.getRolloverImage() != null && product.getRolloverImage().getPath() != "") {
				prodImgUrl = new URL(mediaPath+product.getRolloverImage().getPath());
			}
		}else if ("cx".equals(pSize)) {
			if (product.getConfirmImage() != null && product.getConfirmImage().getPath() != "") { 
				prodImgUrl = new URL(mediaPath+product.getConfirmImage().getPath());
			}
		}else if ("p".equals(pSize)) {
			if (product.getDetailImage() != null && product.getDetailImage().getPath() != "") {
				prodImgUrl = new URL(mediaPath+product.getDetailImage().getPath());
			}
		}else if ("f".equals(pSize)) {
			if (product.getThumbnailImage() != null && product.getThumbnailImage().getPath() != "") {
				prodImgUrl = new URL(mediaPath+product.getThumbnailImage().getPath());
			}
		}else if ("z".equals(pSize)) {
			if (product.getZoomImage() != null && product.getZoomImage().getPath() != "") {
				prodImgUrl = new URL(mediaPath+product.getZoomImage().getPath());
			}
		}
			
		//check for passed bSize
		if (request.getParameter("bSize") != null) {
			//see if it's one of our two options
			if ( "sm".equalsIgnoreCase(request.getParameter("bSize")) ) { bSize = "sm"; }
			if ( "lg".equalsIgnoreCase(request.getParameter("bSize")) ) { bSize = "lg"; }
			if ( "".equalsIgnoreCase(request.getParameter("bSize")) ) { bSize = ""; }
		}else{
			//if a value is not passed, try to guess which to use
			if ("p".equals(pSize)) {
				bSize = "lg";
			}
		}

		
		//check for passed bVal
		if (request.getParameter("bVal") != null && request.getParameter("bVal") != "") { bVal = request.getParameter("bVal"); }

		//check for passed bType
		if ( !"auto".equals(bType) ) { 
			if ( "deal".equalsIgnoreCase(request.getParameter("bType")) ) {
				if ("".trim().equals(bVal)) { bVal = "-1"; }
				//check for no bVal
				if (bVal=="-1") {
					//try to get product's deal amount
					bValTemp = product.getHighestDealPercentage();
					if (bValTemp>=0) {
						overlayUrl = new URL(mediaStaticPath+"/media_stat/images/deals/brst_"+bSize+"_"+bValTemp+".gif");
					}
				}else{
					overlayUrl = new URL(mediaStaticPath+"/media_stat/images/deals/brst_"+bSize+"_"+bVal+".gif");
				}
			}else if ( "fave".equalsIgnoreCase(request.getParameter("bType")) ) {
				overlayUrl = new URL(mediaStaticPath+"/media_stat/images/bursts/brst_"+bSize+"_fave.gif");
			}else if ( "new".equalsIgnoreCase(request.getParameter("bType")) ) {
				overlayUrl = new URL(mediaStaticPath+"/media_stat/images/bursts/brst_"+bSize+"_new.gif");
			}else if ( "bis".equalsIgnoreCase(request.getParameter("bType")) ) {
				overlayUrl = new URL(mediaStaticPath+"/media_stat/images/bursts/brst_"+bSize+"_bis.gif");
			}else if ( "sale".equalsIgnoreCase(request.getParameter("bType")) ) {
				overlayUrl = new URL(mediaStaticPath+"/media_stat/images/bursts/brst_"+bSize+"_sale.gif");
			}else if( "X".equalsIgnoreCase(request.getParameter("bType")) ) {
				//force no burst
				overlayUrl = null;
			}
		}else{
			//do logic to determine which busrt to show
				//isInCart
				//deal
				//isDisplayFave
				//isDisplayNew
				//isDisplayBackinStock
			
			//skipping isInCart
				//skipping is fave

			//try to get product's deal amount
			bValTemp = product.getHighestDealPercentage();
			if (bValTemp>0) {
				//is deal
				overlayUrl = new URL(mediaStaticPath+"/media_stat/images/deals/brst_"+bSize+"_"+bValTemp+".gif");
			}else if (product.isNew()){
				overlayUrl = new URL(mediaStaticPath+"/media_stat/images/bursts/brst_"+bSize+"_new.gif");
			}else if (product.isBackInStock()) {
				overlayUrl = new URL(mediaStaticPath+"/media_stat/images/bursts/brst_"+bSize+"_bis.gif");
			}
		}

		//if a value is not passed for bSize, eliminate for pSize == "z"
		if ("z".equals(pSize) && (bSize == null || "".equals(bSize))) { overlayUrl = null; }

		//all these qualify for no burst
		if (
			//no size
			(bSize == null || "".equals(bSize)) ||
			//type == deal, no value
			("deal".equalsIgnoreCase(request.getParameter("bType")) && ("-1".equals(bVal) && bValTemp <= 0))
		) { overlayUrl = null; }
		
		log(myDebug, "rVal to here : "+rVal);
		//check for a rating, unless we already have a forced value
		if ("-1".equals(rVal)) {
			//no rating yet, get from the product
			rVal = product.getProductRating();
			if (rVal.length() == 1) {
				rVal = "0"+rVal; //add leading zero
			} else if (rVal.length() == 3) { //from SKU
				rVal = rVal.substring(1);
			}
			//setup rating
			if (!"".equals(rVal)) {
				ratingUrl = new URL(mediaStaticPath+"/media_stat/images/ratings/"+rVal+".gif");
			}
		}else{
			//get from passed val/SKU-set val
			if ("1,2,3,4,5,6,7,8,9,10,01,02,03,04,05,06,07,08,09,001,002,003,004,005,006,007,008,009".indexOf(rVal) > -1) {
				if (rVal.length() == 1) {
					rVal = "0"+rVal; //add leading zero
				} else if (rVal.length() == 3) { //from SKU
					rVal = rVal.substring(1);
				}
				if (!"".equals(rVal)) {
					ratingUrl = new URL(mediaStaticPath+"/media_stat/images/ratings/"+rVal+".gif");
				}
			}
		}
		log(myDebug, "rVal to here2 : "+rVal);

	}

}

//we should always have something here
try {
	prodImage = ImageIO.read(prodImgUrl);
	
	//adjust for rating only
	if (ratingOnly && ratingUrl != null) {
		
		/* if we're reading a URL, don't use new File() */
		rating = ImageIO.read(ratingUrl);

		ratingWidth = rating.getWidth(null);
		ratingHeight = rating.getHeight(null);

		prodWidth = ratingWidth;
		prodHeight = ratingHeight;
	}else{
		//adjust for rating
		if (ratingUrl != null) {
			/* if we're reading a URL, don't use new File() */
			rating = ImageIO.read(ratingUrl);

			ratingWidth = rating.getWidth(null);
			ratingHeight = rating.getHeight(null);

			prodWidth = prodImage.getWidth(null);
			prodHeight = prodImage.getHeight(null);

			//set x/y
			if (!"NaN".equals(rOxTemp)) {
				rOx = Integer.valueOf(rOxTemp).intValue();
			}
			if (!"NaN".equals(rOyTemp)) {
				rOy = Integer.valueOf(rOyTemp).intValue();
			}else{
				rOy = prodHeight;
			}

			prodHeight = prodHeight + ratingHeight; //add rating height
		}else{
			prodWidth = prodImage.getWidth(null);
			prodHeight = prodImage.getHeight(null);
		}
	}

	//check to see if we're making a larger image than we have images for
	//if (retImageWidth > 0 || retImageHeight > 0) {
		//check passed values vs product values
		if (retImageWidth <= 0) { retImageWidth = prodWidth; }
		if (retImageHeight <= 0) { retImageHeight = prodHeight; }

		//we are, create a new one
		background = new BufferedImage(retImageWidth, retImageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D BGg = (Graphics2D)background.createGraphics();

		//check here for a color
		BGg.setBackground(bgColor);

		//actually paint
		BGg.clearRect(0, 0, retImageWidth, retImageHeight);

		BGg.drawImage(prodImage, pOx, pOy, null);

		
	//}else{
		//background = prodImage;
	//}

	if (overlayUrl != null) {
		/* if we're reading a URL, don't use new File() */
		overlay = ImageIO.read(overlayUrl);

		overlayWidth = overlay.getWidth(null);
		overlayHeight = overlay.getHeight(null);

		Graphics2D g = (Graphics2D)background.createGraphics();
		g.drawImage(overlay, bOx, bOy, null);
	}

	
	if (ratingUrl != null) {
		Graphics2D g = (Graphics2D)background.createGraphics();
		
		//before we draw, check if we need the rating centered
		if (rCent) {
			//we do, calc new offset
			rOx = (retImageWidth/2)-(ratingWidth/2);
		}

		g.drawImage(rating, rOx, rOy, null);
	}
}catch(Exception e){
	log(myDebug, "Got an Exception: " + e.getMessage());
}

//null check background image
if (background != null) {
//and the actual output
	//JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
	//JPEGEncodeParam jep = encoder.getDefaultJPEGEncodeParam(background);
	//jep.setQuality(iQ, (boolean)true); //change here to allow baseline as optional
	//encoder.encode(background, jep);
}
	response.getOutputStream().close();

/* Print debug messages */
	log(myDebug, "mediaPath : "+mediaPath);
	log(myDebug, "mediaStaticPath : "+mediaStaticPath);

	log(myDebug, "retImageWidth : "+retImageWidth);
	log(myDebug, "retImageHeight : "+retImageHeight);
	log(myDebug, "bgColorString : "+bgColorString);
	log(myDebug, "colors : "+bgR+", "+bgG+", "+bgB);
	log(myDebug, "iQ : "+iQ);

	log(myDebug, "prodImgUrl : "+prodImgUrl);
		log(myDebug, "prodWidth : "+prodWidth);
		log(myDebug, "prodHeight : "+prodHeight);

		log(myDebug, "pId : "+id);
		log(myDebug, "pSize : "+pSize);
		log(myDebug, "pOx : "+pOx);
		log(myDebug, "pOy : "+pOy);

	log(myDebug, "overlayUrl : "+overlayUrl);
		log(myDebug, "overlayWidth : "+overlayWidth);
		log(myDebug, "overlayHeight : "+overlayHeight);

		log(myDebug, "bType : "+bType);
		log(myDebug, "bSize : "+bSize);
		log(myDebug, "bVal : "+bVal);
		log(myDebug, "bValTemp : "+bValTemp);
		log(myDebug, "bOx : "+bOx);
		log(myDebug, "bOy : "+bOy);

	log(myDebug, "ratingUrl : "+ratingUrl);
		log(myDebug, "ratingWidth : "+ratingWidth);
		log(myDebug, "ratingHeight : "+ratingHeight);

		log(myDebug, "ratingOnly : "+ratingOnly);

		log(myDebug, "rType : "+rType);
		log(myDebug, "rVal : "+rVal);
		log(myDebug, "rValTemp : "+rValTemp);
		log(myDebug, "rOx : "+rOx);
		log(myDebug, "rOy : "+rOy);
		log(myDebug, "rCent : "+rCent);

log(myDebug, "Ending generateImage (test)...");

%>