/**
 * @author ekracoff
 * Created on Dec 17, 2004*/

package com.freshdirect.cms.listeners;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.imageio.ImageIO;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.framework.content.TemplateRenderer;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class Media extends ModelSupport implements Serializable {
	private String uri;

	private Integer height;

	private Integer width;

	private ContentType type;

	private String mimeType;

	private Date lastModified;

	public Media(PrimaryKey id, String uri, ContentType type, Integer width,
			Integer height, String mimeType, Date lastModified) {
		this.setPK(id);
		this.uri = uri;
		this.type = type;
		this.width = width;
		this.height = height;
		this.mimeType = mimeType;
		this.lastModified = lastModified;
	}

	public Media(String uri, ContentType type, String mimeType,
			Date lastModified) {
		this(null, uri, type, null, null, mimeType, lastModified);
	}

	public Integer getHeight() {
		return height;
	}

	public ContentType getType() {
		return type;
	}

	public String getUri() {
		return uri;
	}

	public Integer getWidth() {
		return width;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public ContentKey getContentKey() {
		return new ContentKey(this.type, getPK().getId());
	}

	public Date getLastModified() {
		return lastModified;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getExtension() {
		if (FDContentTypes.MEDIAFOLDER.equals(this.type)) {
			return null;
		}
		return uri.substring(uri.indexOf(".") + 1);
	}

	public static ContentType determineType(String uri, String mimeType) {
		if (mimeType == null || "".equals(mimeType)) {
			return FDContentTypes.MEDIAFOLDER;
		}

		if (mimeType.startsWith("image/")) {
			return FDContentTypes.IMAGE;
		}
		
		try {
			URL url = new URL("file://" + uri);
			if (TemplateRenderer.getInstance().isTemplate(url)) {
				return FDContentTypes.TEMPLATE;
			}
		} catch (MalformedURLException e) {
		}

		return FDContentTypes.HTML;
	}
	
	public static String getDefaultMimeType(ContentType type, String filename) {
	    if (FDContentTypes.MEDIAFOLDER.equals(type)) {
	        return null;
	    }
	    String lowercase = filename.toLowerCase();
            if (lowercase.endsWith(".gif")) {
                return "image/gif";
            }
            if (lowercase.endsWith(".jpg") || lowercase.endsWith(".jpeg")) {
                return "image/jpeg";
            }
            if (lowercase.endsWith(".png")) {
                return "image/png";
            }
            if (lowercase.endsWith(".tif")) {
                return "image/tiff";
            }
            if (lowercase.endsWith(".ftl") || lowercase.endsWith(".txt") || lowercase.endsWith(".jsp")|| lowercase.endsWith(".log")) {
                return "text/plain";
            }
            if (lowercase.endsWith(".html") || lowercase.endsWith(".htm")) {
                return "text/html";
            }
            if (lowercase.endsWith(".mov")) {
                return "video/quicktime";
            }
            if (lowercase.endsWith(".pdf")) {
                return "application/pdf";
            }
            if (lowercase.endsWith(".xls")) {
                return "application/vnd.ms-excel";
            }
            if (lowercase.endsWith(".db")) {
                return "application/octet-stream";
            }
            
	    if (FDContentTypes.IMAGE.equals(type)) {
	        // failback
	        return "image/jpeg";
	    }
            if (FDContentTypes.TEMPLATE.equals(type)) {
                return "text/plain";
            }
            return "application/octet-stream";
	}

	public static Media convertEventToMedia(String uri, String mimeType,
			byte[] content) throws IOException {
		ContentType type = Media.determineType(uri, mimeType);;
		Integer width = null;
		Integer height = null;
		if (FDContentTypes.IMAGE.equals(type)) {
			BufferedImage bimage = ImageIO.read(new ByteArrayInputStream(
					content));
			width = new Integer(bimage.getWidth());
			height = new Integer(bimage.getHeight());
		}

		return new Media(null, uri, type, width, height, mimeType, new Date());
	}

}
