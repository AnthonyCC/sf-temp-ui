package com.freshdirect.webapp.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.DocumentSource;

import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.fdstore.FDNutrition;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.xml.XMLSerializer;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.product.ProductExtraDataPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;

public class NutritionInfoPanelRendererUtil {

	private static final Logger LOG = LoggerFactory.getInstance(NutritionInfoPanelRendererUtil.class);
	private static Transformer transformer = null;
//	public static final String  PANELMOBILAPI= ".panelMobilApi";
//	public static final String  PANELMOBILAPIPRODUCTDETAIL= ".panelMobilApiProductDetail";
//	

	static {
		try {
			InputStream xslStream = NutritionInfoPanelRendererUtil.class.getResourceAsStream("nutrition_label.xsl");
			transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xslStream));
		} catch (TransformerConfigurationException e) {
			LOG.error("Failed to create shared XSL transformer", e);
		} catch (TransformerFactoryConfigurationError e) {
			LOG.error("Failed to create shared XSL transformer", e);
		}
	}

	/**
	 * Render nutrition panel into output stream, the classic one
	 * 
	 * @param nutritionModel
	 * @param showErpsExtra
	 * @param out
	 * @return
	 * @throws IOException
	 */
	public static boolean renderClassicPanel(ErpNutritionModel nutritionModel, boolean showErpsExtra, Writer out)
			throws IOException {

		if (nutritionModel == null) {
			LOG.error("Did not find any nutrition information:");
			return false;
		}

		renderAdditionalInfo(nutritionModel, showErpsExtra, out);

		final List<FDNutrition> nutritionList = extractNutritions(nutritionModel);

		return renderPanelWithNutritionList(nutritionList, out);
	}

	/**
	 * 
	 * Make some pre-rendering based on certain nutrition entries
	 * 
	 * @param nutritionModel
	 * @param showErpsExtra
	 * @param out
	 *            Render target
	 * 
	 * @return List of Nutrition objects extracted from input model
	 * 
	 * @throws IOException
	 *             this exception may occur during writing to output stream
	 */
	private static void renderAdditionalInfo(final ErpNutritionModel nutritionModel, boolean showErpsExtra, Writer out)
			throws IOException {

		// render extra info
		if (showErpsExtra) {

			String infoSource = nutritionModel.getUomFor(ErpNutritionType.SOURCE);
			if (infoSource != null && infoSource.trim().length() > 0) {
				out.write("Information source: " + infoSource + "<br/>");
			}

			double netCarbs = nutritionModel.getNetCarbs();
			if (netCarbs > 0) {
				out.write("Net Carbs: " + netCarbs + "<br/>");
			}

			out.write("<br/>");

		}

		for (Iterator<String> nIter = nutritionModel.getKeyIterator(); nIter.hasNext();) {
			final String key = nIter.next();
			if ("IGNORE".equalsIgnoreCase(key)) {
				out.write("<b>This information is currently hidden from the website</b><br/><br/>");
			}
		}
	}

	/**
	 * Extract Nutrition objects out of nutrition model
	 * 
	 * @param nutritionModel
	 * @return
	 */
	private static List<FDNutrition> extractNutritions(final ErpNutritionModel nutritionModel) {
		final List<FDNutrition> nutritionList = new ArrayList<FDNutrition>();
		for (Iterator<String> nIter = nutritionModel.getKeyIterator(); nIter.hasNext();) {
			final String key = nIter.next();
			FDNutrition fdn = new FDNutrition(ErpNutritionType.getType(key).getDisplayName(),
					nutritionModel.getValueFor(key), nutritionModel.getUomFor(key));
			nutritionList.add(fdn);
		}
		return nutritionList;
	}

	/**
	 * Render classic nutrition panel based on input document
	 * 
	 * @param xslTransformer
	 * @param nutritionDoc
	 * @param out
	 *            output stream
	 * 
	 * @return result of render operation
	 */
	private static boolean renderNutritionPanel(final Object nutritionDoc, final Writer out) {
		if (transformer == null) {
			LOG.error("Shared XSL transformer is not available! Aborting ...");
			return false;
		}

		try {
			Document doc = new XMLSerializer().serializeDocument("nutrition", nutritionDoc);
			synchronized (transformer) {
				transformer.transform(new DocumentSource(doc), new StreamResult(out));
			}
			return true;
		} catch (TransformerException e) {
			LOG.error("Failed to render nutrition panel", e);
		}

		return false;
	}

	/**
	 * Performs rendering with a list of nutrition objects
	 * 
	 * @param nutritions
	 * @param out
	 *            output stream
	 * @return result of render operation
	 */
	public static boolean renderPanelWithNutritionList(final List<FDNutrition> nutritions, final Writer out) {
		return renderNutritionPanel(nutritions, out);
	}

	//TODO REMOVE THIS!!!!@
	public static void writeToFile(String input) {
		input.concat("\n\r");
		try {
			BufferedWriter writer = new BufferedWriter(
					new FileWriter("C:/Users/dheller/Documents/nutrition/soyoutput.html"));
			writer.write(input);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
/**
 * A method to get nutrition information by using the same soy file for NUTRITION PANEL that the
 * main website does.  NOTE: depends on depends on /WEB-INF/shared/soy/pdp/nutrition/default.soy
 * in the fdwebsite project.
 * @param user FDUserI
 * @param fdProduct com.freshdirect.fdstore.FDProduct
 * @param productModel com.freshdirect.fdstore.content.ProductModel
 * @param context servlet context for looking up the soy file.
 *   @param panelDomainName NutritionInfoPanelRendererUtil.PanelNameEnum.PANELMOBIL_API_PRODUCT_DETAIL orPANELMOBIL_API , <BR>
 *   for defining the namespance (panel name) that you wish in the soy file.
 * @return the rendered html for the nutrition panel in String format.
 * @throws FDResourceException
 * @throws FDSkuNotFoundException
 */
	public static String getSkuNutritionHtmlwithSoy(FDUserI user, FDProduct fdProduct, ProductModel productModel,
			ServletContext context,  PanelNameEnum panelDomainName, String cssValue) throws FDResourceException, FDSkuNotFoundException {
		

		//SoyFileSet.Builder builder = new SoyFileSet.Builder();
	 
		if (context == null) {
			throw new FDResourceException(
					" null servlet context in NutritionInfoPanelRenerUtil.getSkuNutritionHtmlwithSoy(), cannot proceed with operation ");
		}


		ProductExtraData extraData = null;
		try {									// FDUserI user, ProductModel product, String grpId, String grpVersion, boolean includeProductAboutMedia, String cssValue
			extraData = ProductExtraDataPopulator.createExtraData(user, productModel, (String) null, (String) null,
					true,cssValue );
		} catch (HttpErrorResponse e) {
			LOG.error("failure rendering soy panel for nutrition: ", e);
			throw new FDResourceException(e);

		}
		// convert the POJO to a hashmap using an utility
		Map<String, ?> potatoMapData = SoyTemplateEngine.convertToMap(extraData);
		SoyMapData mapData = new SoyMapData(potatoMapData);
		// create a tofu engine for our namespace, or what we are interested in
		// from the soy template
		//SoyTofu simpleTofu = tofu.forNamespace("pdp.nutrition");
		/*
		 * NOTE, THE regular website is uing the section namespace by default, we in mobile
		 * have a more specific namespace section for the mobile api, :
		 * panelMobilApi
		 */
		// String outputStr = simpleTofu.newRenderer(".panel")
		//String htmlStringFromtofu = simpleTofu.newRenderer(panelDomainName.getPanelName()).setData(mapData).render();
		  String htmlStringFromtofu="";
		try {
			htmlStringFromtofu = StringEscapeUtils.unescapeHtml(SoyTemplateEngine.getInstance().render(context, "pdp.nutrition"+panelDomainName.getPanelName(),mapData ));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// THIS IS JUST FOR DEBUGGING IN DEVELOPMENT, REMOVE BEFORE YOU CHECK IT
		// IN
		/* TODO */
	//	writeToFile(htmlStringFromtofu);
		return htmlStringFromtofu;

	}

	/*
	 * i WROTE A MAIN FOR TESTING THIS WITHOUT RUNNING APP
	 */
	public static void main(String[] args) {
		// "C:/FreshDirectTrunk/projects/FDWebSite/docroot/WEB-INF/shared/soy/pdp/nutrition/default.soy"
		String fullPath = "C:\\FreshDirectTrunk\\projects\\FDIntegrationServices\\docroot\\WEB-INF\\shared\\soy\\pdp\\nutrition\\default.soy";
		String fixedFullPath = fullPath.replace("FDIntegrationServices", "FDWebSite");
		System.out.println("fixedFullPath=: " + fixedFullPath);
	};
	
	public enum PanelNameEnum {
		 PANELMOBIL_API_PRODUCT_DETAIL (".panelMobilApiProductDetail"),
		 PANELMOBIL_API (".panelMobilApi");
		private String panelName;
		
		PanelNameEnum(String actualPanelName) {
	        this.panelName = actualPanelName;
		}
		public String getPanelName(){
			return this.panelName;
		}
		
	
		
		
	}

}
