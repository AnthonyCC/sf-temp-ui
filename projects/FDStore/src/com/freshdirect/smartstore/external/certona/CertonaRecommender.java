package com.freshdirect.smartstore.external.certona;

import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.smartstore.external.ExternalRecommender;
import com.freshdirect.smartstore.external.ExternalRecommenderCommunicationException;
import com.freshdirect.smartstore.external.ExternalRecommenderRequest;
import com.freshdirect.smartstore.external.RecommendationItem;

public class CertonaRecommender implements ExternalRecommender {

	private String scheme;
	private static DocumentBuilder builder = null;
	private final Logger logger = Logger.getLogger(CertonaRecommender.class);
	
	static {
		try {
			
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			
		} catch (ParserConfigurationException pce){}
	}
	
	public CertonaRecommender(String scheme) {
		this.scheme = scheme;
	}
	
	@Override
	public List<RecommendationItem> recommendItems(ExternalRecommenderRequest request) throws ExternalRecommenderCommunicationException {

		URL url;
		HttpURLConnection connection = null;
		List<RecommendationItem> results = new ArrayList<RecommendationItem>();
		try {
			String trackingId = CertonaUserContextHolder.getTrackingId();
			String sessionId = CertonaUserContextHolder.getSessionId();

			StringBuffer urlParameters = new StringBuffer("?appid=")
				.append(URLEncoder.encode(FDStoreProperties.getCertonaAppId(), "UTF-8"))
				.append("&trackingid=" + URLEncoder.encode(trackingId, "UTF-8"))
				.append("&sessionid=" + URLEncoder.encode(sessionId, "UTF-8"))
				.append("&scheme=" + URLEncoder.encode(this.scheme, "UTF-8"));

			List<String> excludedProductIds = CertonaUserContextHolder.getExcludeProductIds();
			if (excludedProductIds != null && !excludedProductIds.isEmpty()) {
				StringBuffer products = new StringBuffer();
				for (String productId : excludedProductIds) {
					products.append(productId).append(";");
				}
				urlParameters.append("&exitemid=" + URLEncoder.encode(products.toString().substring(0, products.toString().length() - 1), "UTF-8"));
			}
			
			if (this.scheme.equals("department1_rr")) {
				urlParameters.append("&department=" + URLEncoder.encode(CertonaUserContextHolder.getId(), "UTF-8"));
			} else if (this.scheme.equals("category1_rr")) {
					urlParameters.append("&category=" + URLEncoder.encode(CertonaUserContextHolder.getId(), "UTF-8"));
			} else if (this.scheme.equals("subcategory1_rr")) {
				urlParameters.append("&subcategory=" + URLEncoder.encode(CertonaUserContextHolder.getId(), "UTF-8"));
			} else if (this.scheme.equals("search1_rr")) {
				urlParameters.append("&context=" + URLEncoder.encode(CertonaUserContextHolder.getSearchParam(), "UTF-8"));
			} else {
				if (this.scheme.equals("certonaRelatedDepartment1rr")) {
					urlParameters.append("&department=" + URLEncoder.encode(CertonaUserContextHolder.getId(), "UTF-8"));
				}
				List<RecommendationItem> relatedProductIds = request.getItems();
				if (relatedProductIds != null && !relatedProductIds.isEmpty()) {
					StringBuffer products = new StringBuffer();
					for (RecommendationItem product : relatedProductIds) {
						products.append(product.getId()).append(";");
					}
					urlParameters.append("&context=" + URLEncoder.encode(products.toString().substring(0, products.toString().length() - 1), "UTF-8"));
				}
			}

			url = new URL(FDStoreProperties.getCertonaService() + urlParameters.toString());
			logger.info("Certona request: " + FDStoreProperties.getCertonaService() + urlParameters.toString());
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			Document document = builder.parse(connection.getInputStream());
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(writer));
			logger.info("Certona answer: " + writer.getBuffer().toString().replaceAll("\n|\r", ""));
			
//			Document document = builder.parse(new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><response>"
//					+ "<products>"
//					+ "<product>"
//					+ "<productid>mea_lafrda_brsktbrgr</productid>"
//					+ "</product>"
//					+ "<product>"
//					+ "<productid>mea_lafrda_shrtrbbrgr</productid>"
//					+ "</product>"
//					+ "</products>"
//					+ "<pageid>res09113010584253544725595</pageid>"
//					+ "</response>")));	

			//TODO: implement proper( ~ after all the details are revealed) parsing algorithm
			
			NodeList productList = document.getElementsByTagName("ProdID");
			List<String> recommendedProductIds = new ArrayList<String>();
			for (int i = 0; i < productList.getLength(); i++) {
				Node node = productList.item(i);
				results.add(new RecommendationItem(node.getTextContent(), ""));
				recommendedProductIds.add(node.getTextContent());
			}
			CertonaUserContextHolder.setRecommendedProductIds(recommendedProductIds);

			NodeList pageIds = document.getElementsByTagName("pageid");
			if (pageIds.getLength() > 0) {
				CertonaUserContextHolder.setPageId(pageIds.item(0).getTextContent());
			}
			
		} catch (Exception e) {

			logger.warn("Getting certona recommendations failed!", e);

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}

		return results;
	}
}
