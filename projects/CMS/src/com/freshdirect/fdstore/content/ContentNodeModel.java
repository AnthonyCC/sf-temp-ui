package com.freshdirect.fdstore.content;

import java.util.Comparator;
import java.util.List;

public interface ContentNodeModel extends ContentNodeI {

	public final static Comparator FULL_NAME_COMPARATOR = new Comparator() {
		public int compare(Object obj1, Object obj2) {
			ContentNodeModelImpl cn1 = (ContentNodeModelImpl) obj1;
			ContentNodeModelImpl cn2 = (ContentNodeModelImpl) obj2;
			String name1 = cn1.getFullName();
			String name2 = cn2.getFullName();

			if (name1 == null)
				name1 = "";
			if (name2 == null)
				name2 = "";

			return name1.compareTo(name2);
		}
	};

	public String getAltText();

	public String getBlurb();

	public Html getEditorial();

	public String getEditorialTitle();

	public String getFullName();

	public String getGlanceName();

	public String getNavName();

	public String getKeywords();

	public List getAssocEditorial();

	public boolean isSearchable();

	public boolean isHidden();

	public String getHideUrl();

	/**
	 * Generate a path string to this content object, that is suitable for
	 * OpenAdServer.
	 * 
	 * @return sitepage path for OAS
	 */
	public String getPath();

	public boolean isOrphan();

}
