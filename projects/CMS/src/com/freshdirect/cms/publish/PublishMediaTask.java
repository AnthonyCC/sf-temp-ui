package com.freshdirect.cms.publish;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.net.*;

import org.apache.commons.httpclient.HttpURL;
import org.apache.webdav.lib.WebdavResource;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.application.service.DbService;

/**
 * Task to incrementally publish media files from a WebDAV repository.
 * <p>
 * Gets the time of the last successful publish and queries what media objects
 * have changed since then (via a SQL query). These files are then published 
 * relative to the specified directory.
 * <p>
 * Note: if there was no previous publish, no files are published at all.
 * 
 * @TODO refactor to not directly muck around in DB but use {@link com.freshdirect.cms.changecontrol.ChangeLogServiceI} instead
 * 
 * @TODO support for deletion and moving files
 */
public class PublishMediaTask extends DbService implements PublishTask {

	private final PublishServiceI publishService;
	private final String checkoutPath;
	private final String repositoryUrl;
	private String username;
	private String password;

	/**
	 * @param publishService publish service to query last publish from
	 * @param checkoutPath relative path to use for checkout base-path
	 * @param repositoryUrl URL of WebDAV repository
	 */
	public PublishMediaTask(PublishServiceI publishService, String checkoutPath, String repositoryUrl) {
		this.publishService = publishService;
		this.checkoutPath = checkoutPath;
		this.repositoryUrl = repositoryUrl;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getComment() {
		return "Checking out media";
	}

	public void execute(Publish publish) {
		File rootDir = new File(publish.getPath(), checkoutPath);
		Publish lastPublish = publishService.getMostRecentPublish();

		if (lastPublish == null) {
			System.err.println("No previous publish, skipping checkout");
			return;
		}

		try {
			HttpURL slideUrl = new HttpURL(repositoryUrl);
			if (username != null && password != null) {
				slideUrl.setUserinfo(username, password);
			}
			WebdavResource webdav = new WebdavResource(slideUrl); //  "http://<dav-host>/<slide-path>/files/"

			URL basePath = new URL(slideUrl.getURI()); // same as slideUrl
			for (Iterator i = getUris(lastPublish.getLastModified()).iterator(); i.hasNext();) {
				String childPath = (String) i.next(); // 	"/file1.ext"

				if (childPath.startsWith("/")) {
					childPath = childPath.substring(1, childPath.length());
				}

				// TODO maybe we should skip any *Thumbs.db* files here to avoid issues (?)
				URL url = new URL(basePath, URLEncoder.encode(childPath, "UTF-8") ); //  "http://<dav-host>/<slide-path>/files/file1.ext"
				webdav.setPath(URLDecoder.decode(url.getPath(), "UTF-8") ); //  "/<slide-path>/files/file1.ext"

				File file = new File(rootDir, childPath);
				createParentDirectory(file);
				if (!webdav.isCollection()) {
					System.out.println("checking out " + url.getPath() + " to " + file.getPath());
					webdav.getMethod(file);
				}
			}

		} catch (IOException e) {
			throw new CmsRuntimeException(e);
		}

	}

	private static String MEDIA_DELTAS = "select uri from cms.media where last_modified > ?";

	private List getUris(Date publishDate) {
		List uris = new ArrayList();
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(MEDIA_DELTAS);
			ps.setTimestamp(1, new Timestamp(publishDate.getTime()));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				uris.add(rs.getString("URI"));
			}
		} catch (SQLException e) {
			throw new CmsRuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e1) {
					throw new CmsRuntimeException(e1);
				}
			}
		}
		return uris;
	}

	private void createParentDirectory(File file) {
		File directory = file.getParentFile();
		if (directory != null && !directory.exists()) {
			directory.mkdirs();
		}
	}

}