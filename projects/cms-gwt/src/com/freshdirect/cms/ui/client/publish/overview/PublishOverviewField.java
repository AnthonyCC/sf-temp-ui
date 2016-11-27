package com.freshdirect.cms.ui.client.publish.overview;

import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;

public class PublishOverviewField extends MultiField<Float> {
	
	protected static class StatisticValue extends HtmlContainer {
		private Html percentageHtml;
		
		public StatisticValue(int width) {
			super("<div class=\"publish-summary-statistic\" style=\"width:"+width+"px\">" +
					"<div class=\"publish-summary-statistic-value\"></div></div>");
		}
		
		public void setPercentage(float width, int count) {
			percentageHtml = new Html();			
			int percentage = width < 1 ? 1 : (int)width;
			percentageHtml.setStyleAttribute("width", percentage + "px");
			percentageHtml.addStyleName("publish-summary-statistic-value-inner");
			percentageHtml.setHtml(String.valueOf(count));
			add(percentageHtml, ".publish-summary-statistic-value");
		}
	
	}
	
	private StatisticValue value;
	private int changeCount;
	private int width;
	
	private ChangeSetQuery query;
	
	public PublishOverviewField(int count, int width) {
		super();
		setWidth(width);
		value = new StatisticValue(width);
		add(new AdapterField(value));
		this.width = width;
		changeCount = count;				
	}
	
	public void setChangeSetQuery(ChangeSetQuery query) {
		this.query = query;
	}
	
	public ChangeSetQuery getChangeSetQuery() {
		return query;
	}
	
	@Override
	public void setValue(Float value) {
		float v = width * value;
		if (v > width - 3) {
			v = width - 3;
		}
		this.value.setPercentage( v, changeCount);
	}

}
