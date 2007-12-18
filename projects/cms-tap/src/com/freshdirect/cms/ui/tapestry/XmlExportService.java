package com.freshdirect.cms.ui.tapestry;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.util.ContentType;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.service.xml.ContentNodeSerializer;
import com.freshdirect.cms.node.ContentNodeUtil;

public class XmlExportService extends AbstractService {

	public final static String SERVICE_NAME = "xmlExport";

	public ILink getLink(boolean post, Object parameter) {
		Object[] paramArray = (Object[])parameter;
		ContentKey rootKey = (ContentKey) paramArray[0];
		Map params = new HashMap(1);
		params.put("root", rootKey.getEncoded());
		return constructLink(post, params);
	}

	public void service(IRequestCycle cycle) throws IOException {
		String rootId = cycle.getParameter("root");
		ContentKey rootKey = ContentKey.decode(rootId);
		ContentNodeI root = rootKey.lookupContentNode();

		Set keys = ContentNodeUtil.collectReachableKeys(root, null);
		keys.add(rootKey);
		Map nodes = CmsManager.getInstance().getContentNodes(keys);

		ContentNodeSerializer s = new ContentNodeSerializer();
		Document doc = s.visitNodes(new ArrayList(nodes.values()));

		OutputFormat outformat = OutputFormat.createPrettyPrint();
		outformat.setEncoding("UTF-8");
		OutputStream out = response.getOutputStream(new ContentType(
				"text/xml;encoding=utf-8"));
		XMLWriter writer = new XMLWriter(out, outformat);
		writer.write(doc);
		writer.flush();
	}

	public String getName() {
		return SERVICE_NAME;
	}

}
