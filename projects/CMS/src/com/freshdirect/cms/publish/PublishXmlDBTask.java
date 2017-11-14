/*
 * Created on Mar 28, 2005
 */
package com.freshdirect.cms.publish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.service.xml.ContentNodeSerializer;
import com.freshdirect.cms.publish.service.StoreFilterService;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.QuickDateFormat;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Publisk task to export all content nodes from a given service to an XML
 * file. If the file name ends with <code>.gz</code>, it's compressed with gzip.
 * Also records the following Dublin Core meta-data about the publish:
 * 
 * @see com.freshdirect.cms.application.service.xml.ContentNodeSerializer
 */
public class PublishXmlDBTask implements PublishTask {

    final static Logger LOG = LoggerFactory.getInstance(PublishXmlDBTask.class);
    
	public final static String NS_DC = "http://purl.org/dc/elements/1.1";

	private ContentServiceI contentService;
	
	private DraftContext draftContext = DraftContext.MAIN;
	
	private DataSource dataSource;
	
	private List<String> publishContentTypeId;

	/**
	 * @param contentService content service to publish from
	 * @param storeFilePath relative path of generated XML file
	 */
	public PublishXmlDBTask(ContentServiceI contentService, DataSource dataSource, List<String> publishContentTypeId) {
		this.setContentService(contentService);
		this.setDataSource(dataSource);
		this.setPublishContentTypeId(publishContentTypeId);
	}

	public PublishXmlDBTask() {
	
	}

	public void initialize() {
		publishContentTypeId = Arrays.asList("WebPage","PickList","PickListItem", "Anchor", "ImageBanner","TextComponent","Section","Schedule", "DarkStore");
	}
	
	@Override
    public void execute(Publish publish) {
		
		Map<ContentKey, ContentNodeI> publishNodes = new LinkedHashMap<ContentKey, ContentNodeI>();
		for(String contentTypeId : getPublishContentTypeId()){
			Map<ContentKey, ContentNodeI> nodes = getContentService().getContentNodes(getContentService().getContentKeysByType(ContentType.get(contentTypeId), draftContext), draftContext);
			publishNodes.putAll(nodes);
		}
		ContentNodeSerializer serializer = new ContentNodeSerializer();
		List<ContentNodeI> l = new ArrayList<ContentNodeI>(publishNodes.values());

		// filter nodes
		if (publish.getStoreId() != null) {
			final int n0 = l.size();
			LOG.info("Filtering " + l.size() + " nodes ..");
			l = StoreFilterService.defaultService().filterContentNodes(publish.getStoreId(), l, contentService, draftContext);
			final int n1 = l.size();
			final double perc = ((double)(n0-n1)*100)/(n0);
			LOG.info("  dropped " + (n0-n1) + " nodes ("+(Math.round(perc))+" %)");
		} else {
			LOG.warn("No store ID specified, skip filtering nodes");
		}
		
		Document doc = serializer.visitNodes(l);
		Element rootElement = doc.getRootElement();
		rootElement.addNamespace("dc", NS_DC);
		rootElement.addElement("dc:description").addText("PublishId: " + publish.getId());
		rootElement.addElement("dc:date").addText(QuickDateFormat.ISO_FORMATTER.format(publish.getTimestamp()));
		
		StringWriter writer = new StringWriter();
		try {
			
			OutputFormat format = new OutputFormat();
			format.setEncoding("UTF-8");
			XMLWriter xw = new XMLWriter(writer, format);
			
			if(FDStoreProperties.getFeedPublishCheck().equalsIgnoreCase("true")){
				
				HttpClient client = new HttpClient();
				PostMethod method = new PostMethod(FDStoreProperties.getFeedPublishURL());
				
				xw.write(doc);
				method.setParameter("feedId", publish.getId());
				method.setParameter("feedData", writer.toString());
				method.setParameter("storeId", publish.getStoreId());
				int httpStatusCode = client.executeMethod(method);
				xw.flush();
				xw.close();
				writer.close();
				
				if(httpStatusCode != 200){
					throw new CmsRuntimeException("Http Error"+httpStatusCode);
				}
			
			}else{
				
				String createPublishResponse = createPublish(publish, writer.toString());
				writer.close();
				
				if(StringUtil.isEmpty(createPublishResponse)){
					throw new CmsRuntimeException("");
				}
				
			}
		} catch (IOException e) {
			throw new CmsRuntimeException(e);
		} 
	}
	
	private String createPublish(Publish publish, String feedData) {
		
		String createPublishResponse = null;
		
		try {
			
			URL url = new URL(FDStoreProperties.getFeedPublishBackofficeURL());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			
			String input = "{\"feedId\":\""+publish.getId()+"\",\"feedData\":\""+feedData+"\",\"storeId\":\""+publish.getStoreId()+"\"}";
			
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
	
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
	
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	
			while ((createPublishResponse = br.readLine()) != null) {
				LOG.info("Create Publish feed: "+createPublishResponse);
			}
	
			conn.disconnect();

	  } catch (MalformedURLException malformedURLException) {

		   throw new CmsRuntimeException(malformedURLException);

	  } catch (IOException ioException) {

		   throw new CmsRuntimeException(ioException);

	 }
		
		return createPublishResponse;
	}

	@Override
	public String getComment() {
		return "Publish in DB";
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ContentServiceI getContentService() {
		return contentService;
	}

	public void setContentService(ContentServiceI contentService) {
		this.contentService = contentService;
	}

	public List<String> getPublishContentTypeId() {
		return publishContentTypeId;
	}

	public void setPublishContentTypeId(List<String> publishContentTypeId) {
		this.publishContentTypeId = publishContentTypeId;
	}
}