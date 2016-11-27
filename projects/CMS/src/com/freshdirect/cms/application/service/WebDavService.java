package com.freshdirect.cms.application.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpURL;
import org.apache.webdav.lib.Property;
import org.apache.webdav.lib.PropertyName;
import org.apache.webdav.lib.ResponseEntity;
import org.apache.webdav.lib.WebdavResource;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.Version;

/**
 * WebDAV implementation of {@link com.freshdirect.cms.application.service.WebDavServiceI}.
 * Needs the base URL of the WebDAV repository via {@link #setRepositoryUrl(String)).
 */
public class WebDavService implements WebDavServiceI {
	
	private String repositoryUrl;

	public String getRepositoryUrl() {
		return repositoryUrl;
	}

	public void setRepositoryUrl(String repositoryUrl) {
		this.repositoryUrl = repositoryUrl;
	}

	public List getVersionHistory(ContentNodeI content) {
		List report = new ArrayList();
		WebdavResource webdav = null;
		try {
			HttpURL slideUrl = new HttpURL(repositoryUrl);

			webdav = new WebdavResource(slideUrl);

			URL basePath = new URL(slideUrl.getURI());

			String childPath = (String) content.getAttribute("path").getValue();
			if (childPath.startsWith("/")) {
				childPath = childPath.substring(1, childPath.length());
			}

			URL url = new URL(basePath, childPath);
			webdav.setPath(url.getPath());

			Vector versionProperties = new Vector();
			versionProperties.add(new PropertyName("DAV:", "version-name"));
			versionProperties.add(new PropertyName("DAV:", "creator-displayname"));
			versionProperties.add(new PropertyName("DAV:", "getlastmodified"));
			versionProperties.add(new PropertyName("DAV:", "getcontentlength"));
			versionProperties.add(new PropertyName("DAV:", "comment"));

			Enumeration reportResults = webdav.reportMethod(webdav.getHttpURL(), versionProperties);

			while ((reportResults != null) && reportResults.hasMoreElements()) {
				ResponseEntity r = (ResponseEntity) reportResults.nextElement();
				Map map = new HashMap();
				Enumeration props = r.getProperties();
				while (props.hasMoreElements()) {
					Property prop = (Property) props.nextElement();
					map.put(prop.getName().substring(2), prop.getPropertyAsString());
				}
				URL histUrl = new URL(basePath, r.getHref());
				Version version = new Version(
					(String) map.get("version-name"),
					(String) map.get("creator-displayname"),
					(String) map.get("getlastmodified"),
					histUrl.toString());
				report.add(version);
			}
		} catch (HttpException e) {
			throw new CmsRuntimeException(e);
		} catch (IOException e) {
			throw new CmsRuntimeException(e);
		}
		return report;
	}

}