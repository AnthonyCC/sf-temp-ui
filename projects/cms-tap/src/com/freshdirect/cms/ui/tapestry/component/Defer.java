package com.freshdirect.cms.ui.tapestry.component;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;

public abstract class Defer extends AbstractComponent {

	public abstract Block getBlock();

	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		IMarkupWriter nested = writer.getNestedWriter();

		getBlock().renderBody(nested, cycle);

		renderBody(writer, cycle);

		nested.close();
	}

}