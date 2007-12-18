/*
 * Created on Mar 8, 2005
 */
package com.freshdirect.cms.ui.tapestry.tree;

import java.io.Serializable;

import net.sf.tacos.model.IKeyProvider;

import com.freshdirect.cms.ContentNodeI;

/**
 * @author vszathmary
 */
public class ContentKeyProvider implements IKeyProvider {

	public static ContentKeyProvider INSTANCE = new ContentKeyProvider();

	public Serializable getKey(Object value) {
		return ((ContentNodeI) value).getKey();
	}

}