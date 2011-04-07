/*
 * Created on Mar 28, 2005
 */
package com.freshdirect.cms.publish;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.xml.ContentNodeSerializer;
import com.freshdirect.framework.util.QuickDateFormat;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Publisk task to export all content nodes from a given service to an XML
 * file. If the file name ends with <code>.gz</code>, it's compressed with gzip.
 * Also records the following Dublin Core meta-data about the publish:
 * 
 * <ul>
 *  <li><code>dc:type</code> - file path</li>
 *  <li><code>dc:description</code> - "PublishId: <em>id</em>"</li>
 *  <li><code>dc:date</code>: publish date recorded in ISO format (yyyy-MM-dd HH:mm:ss,SSS)</li>
 * </ul>
 * 
 * @see com.freshdirect.cms.application.service.xml.ContentNodeSerializer
 */
public class PublishXmlTask implements PublishTask {

    final static Logger LOG = LoggerFactory.getInstance(PublishXmlTask.class);
    
	public final static String NS_DC = "http://purl.org/dc/elements/1.1";

	private final ContentServiceI contentService;
	
	private final String storeFilePath;

	/**
	 * @param contentService content service to publish from
	 * @param storeFilePath relative path of generated XML file
	 */
	public PublishXmlTask(ContentServiceI contentService, String storeFilePath) {
		this.contentService = contentService;
		this.storeFilePath = storeFilePath;
	}

	private void createParentDirectory(File file) {
		File directory = file.getParentFile();
		if (directory != null && !directory.exists()) {
			directory.mkdirs();
		}
	}

	public void execute(Publish publish) {
		Map nodes = contentService.getContentNodes(contentService.getContentKeys());

		ContentNodeSerializer serializer = new ContentNodeSerializer();
		List l = new ArrayList(nodes.values());
		Document doc = serializer.visitNodes(l);

		Element rootElement = doc.getRootElement();
		rootElement.addNamespace("dc", NS_DC);
		rootElement.addElement("dc:type").addText(storeFilePath);
		rootElement.addElement("dc:description").addText("PublishId: " + publish.getId());
		rootElement.addElement("dc:date").addText(QuickDateFormat.ISO_FORMATTER.format(publish.getTimestamp()));

		try {
			File file = new File(publish.getPath(), storeFilePath);
			createParentDirectory(file);

			LOG.info("writing out doc to " + file.getPath());
			Writer writer;
			if (file.getName().endsWith(".gz")) {
				writer = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(file)));
			} else {
				writer = new FileWriter(file);
			}

			OutputFormat format = new OutputFormat();
			format.setEncoding("UTF-8");
			format.setIndent("\t");
			format.setNewlines(true);

			XMLWriter xw = new XMLWriter(writer, format);
			xw.write(doc);
			xw.flush();
			xw.close();

		} catch (IOException e) {
			throw new CmsRuntimeException(e);
		}
	}

	public String getComment() {
		return "Writing store file " + storeFilePath;
	}

}