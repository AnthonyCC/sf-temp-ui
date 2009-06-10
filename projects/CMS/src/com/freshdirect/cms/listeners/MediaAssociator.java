package com.freshdirect.cms.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;

public class MediaAssociator {

	private final static String PATH_SEPARATOR = "/";

	private final static String CHUNK_SEPARATOR = "_";

	private final static String EXTENSION_SEPARATOR = ".";

	/** Map of String (prefix) -> (Map of String (suffix) -> MediaAssociator.Rule */
	private final Map rules = new HashMap();

	public void addRule(String prefix, String suffix, ContentType type,
			String attributeName) {

		Map m = (Map) rules.get(prefix);
		if (m == null) {
			m = new HashMap();
			rules.put(prefix, m);
		}

		Rule r = (Rule) m.get(suffix);
		if (r != null) {
			throw new IllegalArgumentException(
					"Duplicate association rule for prefix: " + prefix
							+ " suffix: " + suffix);
		}
		r = new Rule(type, attributeName);
		m.put(suffix, r);
	}

	public MediaAssociation getAssociation(String uri) {
		String fullName = uri.substring(uri.lastIndexOf(PATH_SEPARATOR) + 1,
				uri.length());

		int extPos = fullName.lastIndexOf(EXTENSION_SEPARATOR);
		if (extPos == -1) {
			return null;
		}
		String name = fullName.substring(0, extPos);

		String prefix = null;
		String suffix = null;
		String id = "";

		StringTokenizer st = new StringTokenizer(name, CHUNK_SEPARATOR, true);
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			boolean chunkSep = CHUNK_SEPARATOR.equals(token);
			if (prefix == null && !chunkSep && st.hasMoreTokens()
					&& rules.get(token) != null) {
				prefix = token;
				id += token;
			} else if (!"".equals(id) && !chunkSep && !st.hasMoreTokens()) {
				suffix = token;
			} else {
				id += token;
			}
		}
		id = StringUtils.strip(id, CHUNK_SEPARATOR);

		if (id == null || suffix == null) {
			return null;
		}

		Map m = (Map) rules.get(prefix);
		if (m != null) {
			Rule r = (Rule) m.get(suffix);
			if (r != null) {
				return new MediaAssociation(new ContentKey(r.type, id),
						r.attributeName);
			}
		}

		return null;
	}

	private static class Rule {
		ContentType type;

		String attributeName;

		Rule(ContentType type, String attributeName) {
			this.type = type;
			this.attributeName = attributeName;
		}
	}
}
