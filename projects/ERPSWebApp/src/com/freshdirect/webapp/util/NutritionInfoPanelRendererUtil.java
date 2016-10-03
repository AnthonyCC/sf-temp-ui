package com.freshdirect.webapp.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.DocumentSource;

import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.fdstore.FDNutrition;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.xml.XMLSerializer;

public class NutritionInfoPanelRendererUtil {

    private static final Logger LOG = LoggerFactory.getInstance(NutritionInfoPanelRendererUtil.class);
    private static Transformer transformer = null;

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
    public static boolean renderClassicPanel(ErpNutritionModel nutritionModel, boolean showErpsExtra, Writer out) throws IOException {

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
    private static void renderAdditionalInfo(final ErpNutritionModel nutritionModel, boolean showErpsExtra, Writer out) throws IOException {

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
            FDNutrition fdn = new FDNutrition(ErpNutritionType.getType(key).getDisplayName(), nutritionModel.getValueFor(key), nutritionModel.getUomFor(key));
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

}
