/**
 * @author ekracoff
 * Created on Dec 17, 2004*/

package com.freshdirect.cms.listeners;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.imageio.ImageIO;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class Media extends ModelSupport implements Serializable {

	private static final long serialVersionUID = -5332515202047947728L;

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
		
		return FDContentTypes.HTML;
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
