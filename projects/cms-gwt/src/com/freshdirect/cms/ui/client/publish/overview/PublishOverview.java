package com.freshdirect.cms.ui.client.publish.overview;

import java.util.ArrayList;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.freshdirect.cms.ui.client.ActionBar;
import com.freshdirect.cms.ui.client.Anchor;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.DetailPanel;
import com.freshdirect.cms.ui.client.changehistory.ChangeHistory;
import com.freshdirect.cms.ui.client.publish.PublishListener;
import com.freshdirect.cms.ui.client.publish.PublishMessages;
import com.freshdirect.cms.ui.model.SummaryData;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;
import com.freshdirect.cms.ui.service.BaseCallback;

public class PublishOverview extends DetailPanel implements PublishListener {

	private LayoutContainer header;
	private HtmlContainer headerMarkup;
	private ActionBar actionBar;
	private Button startPublish;
	private TextArea commentField;

	private String publishId;
	
	private Anchor allMessages;
	private Anchor allChanges;
	private Anchor overview;
	
	private ArrayList<PublishListener> publishListeners = new ArrayList<PublishListener>();
	private GwtPublishData data;

	protected static class OverviewForm extends FormPanel {
		private FieldSet fieldSet;

		public OverviewForm(String heading, PublishListener listener) {
			super();
			setHeaderVisible(false);
			setBorders(false);
			setBodyBorder(false);

			fieldSet = new FieldSet();
			fieldSet.setHeading(heading);
			PublishOverviewLayout layout = new PublishOverviewLayout();
			layout.addPublishListener(listener);
			fieldSet.setLayout(layout);

			fieldSet.setWidth(300);
			fieldSet.setBorders(false);

			add(fieldSet);
		}

		public FieldSet getFieldSet() {
			return fieldSet;
		}
	}

	private static PublishOverview instance = new PublishOverview();

	public static PublishOverview getInstance() {
		return instance;
	}

	public PublishOverview() {
		super();

		header = new LayoutContainer();
		headerMarkup = new HtmlContainer("<table width=\"100%\" class=\"pageTitle\" cellspacing=\"0\" cellpadding=\"0\">"
				+ "<tbody><tr>" + "<td valign=\"bottom\">"
				+ "<h1 class=\"view-title\">Publish Overview <span class=\"publish-id\"></span></h1>" + "</td>"
				+ "<td width=\"75\" valign=\"bottom\" align=\"right\" style=\"line-height: 0pt;\">"
				+ "<img width=\"75\" height=\"66\" src=\"img/banner_publish.gif\"/>" + "</td>" + "</tr>" + "</tbody></table>");
		actionBar = new ActionBar();
		startPublish = new Button("Start Publish");
		startPublish.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (commentField == null || commentField.getValue() == null || commentField.getValue().trim().length() == 0) {
					commentField.markInvalid("missing publish description");
					return;
				}
				commentField.clearInvalid();
				CmsGwt.getContentService().startPublish(commentField.getValue().trim(), new BaseCallback<String>() {

					@Override
					public void onSuccess(String result) {
						for (PublishListener listener : publishListeners) {
							listener.onPublishStarted(result);
						}
					}
				});
			}
		});
		
		overview = new Anchor("Overview");
		overview.addListener(Events.OnClick, new Listener<BaseEvent>() {		
			public void handleEvent(BaseEvent be) {
				loadData(data);
				overview.hide();
				allChanges.show();
				if (!publishId.equals("latest")) {
					allMessages.show();
				} else {
					startPublish.show();					
				}
			};
		});
		overview.hide();
		actionBar.addLink(overview, new Margins(0, 10, 0, 0));
		
		allChanges = new Anchor("Show all changes");
		allChanges.addListener(Events.OnClick, new Listener<BaseEvent>() {		
			public void handleEvent(BaseEvent be) {
				ChangeSetQuery q = new ChangeSetQuery();
				q.setPublishId(publishId);
				overview.show();
				allChanges.hide();
				startPublish.hide();
				if (!publishId.equals("latest")) {
					allMessages.show();
				}
				loadChanges(q);
			};
		});
		actionBar.addLink(allChanges, new Margins(0, 10, 0, 0));
		
		allMessages = new Anchor("Show all messages");
		allMessages.addListener(Events.OnClick, new Listener<BaseEvent>() {		
			public void handleEvent(BaseEvent be) {
				ChangeSetQuery q = new ChangeSetQuery();
				q.setPublishId(publishId);
				overview.show();
				allMessages.hide();
				allChanges.show();
				startPublish.hide();
				loadMessages(q);
			};
		});
		actionBar.addLink(allMessages, new Margins(0, 10, 0, 0));
		
		actionBar.addButton(startPublish);

		header.add(headerMarkup);
		header.add(actionBar);

		setHeader(header);
		setBody(new LayoutContainer(new FitLayout()));
	}

	public PublishOverview(GwtPublishData data) {
		this();
		this.data = data;
		loadData(this.data);
	}

	public void loadData(GwtPublishData data) {
		Html publishIdMarkup;
		this.data = data;
		publishId = data.getId();
		if (data.getId().equals("latest")) {
			publishIdMarkup = new Html("");
			startPublish.show();
			allMessages.hide();
			actionBar.addStyleName("compare-mode-color");
		} else {
			publishIdMarkup = new Html("(#" + data.getId() + ")");
			startPublish.hide();
			allMessages.show();
			actionBar.removeStyleName("compare-mode-color");
		}
		publishIdMarkup.setTagName("span");
		headerMarkup.add(publishIdMarkup, ".publish-id");
		getBody().removeAll();
		getBody().add(getStatisticPanel(data));
		overview.hide();		
		allChanges.show();
		getBody().layout();
	}
	
	public void loadMessages(ChangeSetQuery query) {		
		getBody().removeAll();
		getBody().add(new PublishMessages(query));
		getBody().layout();
	}
	
	public void loadChanges(ChangeSetQuery query) {
		getBody().removeAll();
		getBody().add(new ChangeHistory(query));
		getBody().layout();
	}

	protected LayoutContainer getStatisticPanel(GwtPublishData data) {
		LayoutContainer statisticPanel = new LayoutContainer(new ColumnLayout());

		
		FormPanel comment = new FormPanel();
		comment.setHeaderVisible(false);
		comment.setBorders(false);
		comment.setBodyBorder(false);
		statisticPanel.add(comment, new ColumnData(300));
		FieldSet fieldSet = new FieldSet();
		fieldSet.setHeading("Description");
		FormLayout layout = new FormLayout();
		layout.setHideLabels(true);
		layout.setLabelWidth(0);
		layout.setLabelSeparator("");
		layout.setDefaultWidth(250);
		fieldSet.setLayout(layout);

		fieldSet.setWidth(300);
		fieldSet.setBorders(false);
		if ("latest".equals(data.getId())) {
			commentField = new TextArea();
			commentField.setEmptyText("Enter your description here");
			fieldSet.add(commentField);			
		}
		else {
			fieldSet.add(new Html(data.getComment()));
		}
		comment.add(fieldSet);

		ChangeSetQuery query;
		
		if (data.getMessages().size() != 0 && !publishId.equals("latest")) {			
			OverviewForm messages = new OverviewForm("Messages", this);
			messages.addStyleName("publish-messages");

			int messageCount = data.getMessages().size();

			for (SummaryData message : data.getMessages()) {
				int value = message.getValue();
				String key = "Warning";
				if (message.getKey().equals("0")) {
					key = "Failure";
				}
				else if (message.getKey().equals("1")) {
					key = "Error";
				}
				
				query = new ChangeSetQuery();
				query.setMessageSeverity(Integer.valueOf(message.getKey()));				
				PublishOverviewField f = new PublishOverviewField(value, 150);		
				f.setChangeSetQuery(query);
				f.addStyleName(key);
				f.setValue((float) value / (float) messageCount);
				f.setFieldLabel(key);
				messages.getFieldSet().add(f);
			}
			statisticPanel.add(messages, new ColumnData(300));
		}

		OverviewForm contributors = new OverviewForm("Contributors", this);
		contributors.addStyleName("publish-contributors");

		for (SummaryData contributor : data.getContributors()) {
			int value = contributor.getValue();
			query = new ChangeSetQuery();
			query.setContributor(contributor.getKey());
			PublishOverviewField f = new PublishOverviewField(value, 150);
			f.setChangeSetQuery(query);
			f.setValue((float) value / (float) data.getChangeCount());
			f.setFieldLabel(contributor.getKey());
			contributors.getFieldSet().add(f);
		}

		OverviewForm types = new OverviewForm("Content Types", this);
		types.addStyleName("publish-types");

		for (SummaryData type : data.getTypes()) {
			int value = type.getValue();
			query = new ChangeSetQuery();
			query.setContentType(type.getKey());
			PublishOverviewField f = new PublishOverviewField(value, 150);
			f.setChangeSetQuery(query);
			f.setValue((float) value / (float) data.getChangeCount());
			f.setFieldLabel(type.getKey());
			types.getFieldSet().add(f);
		}

		statisticPanel.add(contributors, new ColumnData(300));
		statisticPanel.add(types, new ColumnData(300));

		return statisticPanel;
	}	

	public void addPublishListener(PublishListener listener) {
		if (!publishListeners.contains(listener)) {
			publishListeners.add(listener);
		}
	}

	public void removePublishListener(PublishListener listener) {
		if (!publishListeners.contains(listener)) {
			publishListeners.remove(listener);
		}
	}

	@Override
	public void onDetailRequest(ChangeSetQuery query) {
		query.setPublishId(publishId);
		overview.show();
		if (query.getMessageSeverity() == -1) {
			loadChanges(query);
			return;
		}
		loadMessages(query);
	}

	@Override
	public void onPublishClicked(String publishId) {		
	}

	@Override
	public void onPublishStarted(String publishId) {		
	}

}
