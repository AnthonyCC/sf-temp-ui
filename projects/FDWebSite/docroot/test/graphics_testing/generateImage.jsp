<%@ page 
		contentType="image/jpg" 
		import='java.net.URL, java.awt.*, java.awt.event.*, java.awt.image.*, java.io.*, javax.imageio.*, com.sun.image.codec.jpeg.*, com.freshdirect.fdstore.content.ProductModel, com.freshdirect.fdstore.content.*, com.freshdirect.webapp.util.*, com.freshdirect.framework.util.log.LoggerFactory, org.apache.log4j.Category, com.freshdirect.fdstore.content.ContentFactory, com.freshdirect.cms.ContentKey, com.freshdirect.framework.util.NVL,com.freshdirect.fdstore.FDStoreProperties'
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
boolean myDebug = true;
log(myDebug, "Starting generateImage (test)...");

BufferedImage prodImage = null;
BufferedImage overlay = null;
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


if ( !"".equals(id) ) {
	
	//hard-coding as a product here
	id = "Product:"+request.getParameter("pId");

	//get product model
	ProductModel product = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(ContentKey.decode(id));

	//check if product model is null
	if (product != null) {
		//check for pSize
		if ( !"".equals(pSize) ) {
			pSizeTemp = pSize;
			if ("a".equals(pSizeTemp) || "c".equals(pSizeTemp) || "cr".equals(pSizeTemp) || "ct".equals(pSizeTemp) || "cx".equals(pSizeTemp) || "p".equals(pSizeTemp) || "f".equals(pSizeTemp) || "z".equals(pSizeTemp)) {
				pSize = pSizeTemp;
			}
		}
/*
		log(myDebug, "pSize : "+pSize);
		log(myDebug, "mediaPath+product.getAlternateImage().getPath() : "+mediaPath+product.getAlternateImage().getPath());
		log(myDebug, "pSize2 : "+pSize);
		log(myDebug, "mediaPath+product.getProdImage().getPath() : "+mediaPath+product.getProdImage().getPath());
		log(myDebug, "pSize3 : "+pSize);
		log(myDebug, "mediaPath+product.getCategoryImage().getPath() : "+mediaPath+product.getCategoryImage().getPath());
		log(myDebug, "pSize4 : "+pSize);
		log(myDebug, "mediaPath+product.getConfirmImage().getPath() : "+mediaPath+product.getConfirmImage().getPath());
		log(myDebug, "pSize5 : "+pSize);
		log(myDebug, "mediaPath+product.getDetailImage().getPath() : "+mediaPath+product.getDetailImage().getPath());
		log(myDebug, "pSize6 : "+pSize);
		log(myDebug, "mediaPath+product.getThumbnailImage().getPath() : "+mediaPath+product.getThumbnailImage().getPath());
		log(myDebug, "pSize7 : "+pSize);
		log(myDebug, "mediaPath+product.getZoomImage().getPath() : "+mediaPath+product.getZoomImage().getPath());
		log(myDebug, "pSize8 : "+pSize);
		log(myDebug, "mediaPath+product.getRolloverImage().getPath() : "+mediaPath+product.getRolloverImage().getPath());
		log(myDebug, "pSize9 : "+pSize);
*/
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

	}

}

//we should always have something here
try {
	prodImage = ImageIO.read(prodImgUrl);
	
	prodWidth = prodImage.getWidth(null);
	prodHeight = prodImage.getHeight(null);

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
}catch(Exception e){
	log(myDebug, "Got an Exception: " + e.getMessage());
}

//null check background image
if (background != null) {
//and the actual output
	JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
	JPEGEncodeParam jep = encoder.getDefaultJPEGEncodeParam(background);
	jep.setQuality(iQ, (boolean)true);
	encoder.encode(background, jep);
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

log(myDebug, "Ending generateImage (test)...");

%>