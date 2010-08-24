package com.freshdirect.webapp.taglib.fdstore.display;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.framework.webapp.BodyTagSupport;

public class UniqueFilterTag extends BodyTagSupport {
	private static final long serialVersionUID = -7851244467799232366L;

	private static final String GROUP_ID_PREFIX = "__uniqueFilter_";

	Collection<? extends ContentNodeModel> in;
	List<ContentNodeModel> filteredList;
	String out;
	String groupId;

	public Collection<? extends ContentNodeModel> getIn() {
		return in;
	}

	public void setIn(Collection<? extends ContentNodeModel> in) {
		this.in = in;
	}

	public String getOut() {
		return out;
	}

	public void setOut(String out) {
		this.out = out;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public int doStartTag() throws JspException {
		String groupVariableName = getGroupVariableName();
		@SuppressWarnings("unchecked")
		Set<ContentKey> group = (Set<ContentKey>) pageContext.getAttribute(groupVariableName);
		if (group == null) {
			group = new HashSet<ContentKey>();
			pageContext.setAttribute(groupVariableName, group);
		}

		if (in != null) {
			filteredList = new ArrayList<ContentNodeModel>(in.size());
			for (ContentNodeModel node : in)
				if (!group.contains(node.getContentKey())) {
					filteredList.add(node);
					group.add(node.getContentKey());
				}
		}

		pageContext.setAttribute(out, filteredList);

		if (filteredList == null || filteredList.size() == 0)
			return SKIP_BODY;
		else
			return EVAL_BODY_INCLUDE;
	}

	private String getGroupVariableName() {
		return GROUP_ID_PREFIX + convertToVariableName(groupId);
	}

	private String convertToVariableName(String id) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < id.length(); i++)
			if (Character.isJavaIdentifierPart(id.charAt(i)))
				buf.append(id.charAt(i));
			else
				buf.append('_');
		return buf.toString();
	}

	public static class TagEI extends TagExtraInfo {
		@Override
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] { new VariableInfo(data.getAttributeString("out"),
					"java.util.List<com.freshdirect.fdstore.content.ContentNodeModel>", true, VariableInfo.NESTED) };
		}
	}
}
