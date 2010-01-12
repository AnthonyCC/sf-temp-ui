package com.freshdirect.cms.ui.client.publish;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingModelMemoryProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.views.PublishView;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;

public class PublishList extends LayoutContainer {
	private static final String SELECTION_STYLE = "x-view-item-sel";
	private static final String HOVER_STYLE = "x-view-item-over";

	protected class NewPublishWidget extends Html {
		public NewPublishWidget() {
			super("<div><div class=\"publish-history-show-data-true\">" +
					"<div class=\"publish-history-id\">Current publish</div>" +
				  "</div></div>");
			addStyleName("publish-history-wrap");
			addStyleName("new-publish-wrap");
			setStyleAttribute("background-color",  "#FFFFEA !important");
		}

		@Override
		protected void onRender(Element target, int index) {
			super.onRender(target, index);
			sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS | Event.FOCUSEVENTS);
		}
				
		@Override
		public void onComponentEvent(ComponentEvent ce) {
			super.onComponentEvent(ce);
			switch (ce.getEventTypeInt()) {
			case Event.ONMOUSEOUT:
				onMouseOut(ce);
				break;
			case Event.ONMOUSEOVER:
				onMouseOver(ce);
				break;
			case Event.ONCLICK:
				onClick(ce);
			}
		}

		private void onMouseOut(ComponentEvent ce) {
			removeStyleName(HOVER_STYLE);
		}

		private void onMouseOver(ComponentEvent ce) {
			addStyleName(HOVER_STYLE);
		}
		
		public void onClick(ComponentEvent e) {
			setSelected(true);
			for (PublishListener listener : publishListeners) {
				listener.onPublishClicked("latest");
			}
		}
		
		public void setSelected(boolean selected) {
			if (selected) {
				progressPublish.setSelected(false);
				deselectAllHistoryItems();
				addStyleName(SELECTION_STYLE);
			} else {
				removeStyleName(SELECTION_STYLE);				
			}
		}
		
		public void raise() {
			((CardLayout) topElement.getLayout()).setActiveItem(this);			
		}
	}

	protected class ProgressPublishWidget extends Html {
		static final int POLL_INTERVAL = 10 * 1000; // 10 seconds

		private GwtPublishData publish;
		private Timer timer;

		public ProgressPublishWidget() {
			super();
			addStyleName("publish-history-wrap");
			addStyleName("new-publish-wrap");
		}

		public ProgressPublishWidget(GwtPublishData data) {
			this();
			setPublish(publish);
		}

		@Override
		protected void onRender(Element target, int index) {
			super.onRender(target, index);
			sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS | Event.FOCUSEVENTS);
		}

		@Override
		public void onComponentEvent(ComponentEvent ce) {
			super.onComponentEvent(ce);
			switch (ce.getEventTypeInt()) {
			case Event.ONMOUSEOUT:
				onMouseOut(ce);
				break;
			case Event.ONMOUSEOVER:
				onMouseOver(ce);
				break;
			case Event.ONCLICK:
				onClick(ce);
			}
		}

		private void onMouseOut(ComponentEvent ce) {
			removeStyleName(HOVER_STYLE);
		}

		private void onMouseOver(ComponentEvent ce) {
			addStyleName(HOVER_STYLE);
		}
		
		public void onClick(ComponentEvent be) {
			setSelected(true);
			for (PublishListener listener : publishListeners) {
				listener.onPublishClicked(getPublish().getId());
			}
		}

		public GwtPublishData getPublish() {
			return publish;
		}

		public void setPublish(GwtPublishData data) {
			publish = data;
			createProgressTimer();
			if (data != null) {
				setHtml("<div class=\"" + data.getStatus() + "\">"
						+ "<div class=\"publish-history-id\">#" + data.getId() + "</div>"
						+ "<div class=\"publish-history-comment\">"	+ data.getComment() + "</div>"
						+ "<div class=\"publish-history-info\"><b>" + data.getPublisher() + "</b><br>" + data.getCreated() + "</div>"
					  + "</div>");
				resumeTimer();
			} else {
				setHtml("<div></div>");
				stopTimer();
			}
		}
		
		public void setSelected(boolean selected) {
			if (selected) {
				newPublish.setSelected(false);
				deselectAllHistoryItems();
				addStyleName(SELECTION_STYLE);
			} else {
				removeStyleName(SELECTION_STYLE);
			}
		}

		public boolean isSelected() {
			return getStyleName() != null && getStyleName().contains(SELECTION_STYLE);
		}

		public void raise() {
			((CardLayout) topElement.getLayout()).setActiveItem(this);			
		}
		
		private void createProgressTimer() {
			if (timer == null) {
				timer = new Timer() {
					@Override
					public void run() {
						String progressId = publish != null ? publish.getId() : null;
						if (progressId != null) {
							ChangeSetQuery query = new ChangeSetQuery();
							query.setPublishId(progressId);
							CmsGwt.getContentService().getPublishData(query, new BaseCallback<GwtPublishData>() {
								@Override
								public void onSuccess(GwtPublishData result) {
									if (!result.isInProgress()) {
										cancel();
										moveProgressToList(result);

										if (isSelected()) {
											PublishView.getInstance().setOverview(result);
										}
									}
								}
							});
						}
					}
				};
			}
		}

		@Override
		protected void onHide() {
			super.onHide();
			stopTimer();
		}

		@Override
		protected void onShow() {
			super.onShow();
			resumeTimer();
		}
		
		protected void stopTimer() {
			if (timer != null)
				timer.cancel();
		}
		
		protected void resumeTimer() {
			if (timer != null)
				timer.scheduleRepeating(POLL_INTERVAL);			
		}
	}
	
	private class PublishHistorySelectionChangeListener extends SelectionChangedListener<PublishItemWrapper> {
		@Override
		public void selectionChanged(SelectionChangedEvent<PublishItemWrapper> se) {
			PublishItemWrapper selectedItem = se.getSelectedItem();
			if (selectedItem != null && selectedItem.isShowData()) {
				lastSelection = selectedItem;
			}
		}
	}
	
	private static class PublishItemWrapper extends BaseModel implements Serializable {
		private static final long serialVersionUID = 8793007826923565270L;

		public PublishItemWrapper() {
			setShowNext(true);
		}

		PublishItemWrapper(GwtPublishData data) {
			setPublishData(data);
			setShowNext(false);
		}

		public boolean isShowNext() {
			return (Boolean) get("showNext");
		}

		public void setShowNext(boolean showNext) {
			set("showNext", showNext);
			set("showData", !showNext);
		}

		public boolean isShowData() {
			return (Boolean) get("showData");
		}

		public void setShowData(boolean showData) {
			set("showData", showData);
			set("showNext", !showData);
		}

		public GwtPublishData getPublishData() {
			return get("publishData");
		}

		public void setPublishData(GwtPublishData publishData) {
			set("publishData", publishData);
		}
	}
	
	private LayoutContainer topElement;
	private NewPublishWidget newPublish;
	private ProgressPublishWidget progressPublish;
	private BasePagingLoader<BasePagingLoadResult<PublishItemWrapper>> loader;
	private PagingModelMemoryProxy proxy;
	private ListStore<PublishItemWrapper> publishStore;
	private ListView<PublishItemWrapper> publishHistory;
	private PublishItemWrapper lastSelection;
	private int loadedItemsCount;

	private ArrayList<PublishListener> publishListeners = new ArrayList<PublishListener>();

	private native String getTemplate() /*-{
		return ['<tpl for=".">',
		'<div class="publish-history-wrap">',
			'<div class="publish-history-show-more-{showNext}">',
				'<div>', 
					'See more...', 
				'</div>',
			'</div>',
			'<div class="publish-history-show-data-{showData}">', 
				'<tpl for="publishData">',
					'<div class="{statusCode}">', 
						'<div class="publish-history-id">#{id}</div>',
						'<div class="publish-history-comment"><i>{comment-short}</i></div>',
						'<div class="publish-history-info"><b>{publisher}</b><br>{created}</div>',
					'</div>',
				'</tpl>',
			'</div>',
		'</div>',
		'</tpl>',
		'<div class="x-clear"></div>'].join("");
	}-*/;

	public PublishList() {
		setLayout(new BorderLayout());
		setStyleName("publish-history-list");

		topElement = new LayoutContainer();
		topElement.setLayout(new CardLayout());

		newPublish = new NewPublishWidget();

		progressPublish = new ProgressPublishWidget();

		BorderLayoutData newPublishData = new BorderLayoutData(LayoutRegion.NORTH);
		newPublishData.setSize(72);

		topElement.add(progressPublish);
		topElement.add(newPublish);

		add(topElement, newPublishData);

		proxy = new PagingModelMemoryProxy(null);
		loader = new BasePagingLoader<BasePagingLoadResult<PublishItemWrapper>>(proxy);
		publishStore = new ListStore<PublishItemWrapper>(loader);
		LayoutContainer historyContainer = new LayoutContainer();
		historyContainer.setLayout(new FitLayout());

		publishHistory = new ListView<PublishItemWrapper>(publishStore) {
			@Override
			protected PublishItemWrapper prepareData(PublishItemWrapper model) {
				model.getPublishData().set("comment-short", Format.ellipse(model.getPublishData().getComment(), 40));
				return model;
			}
		};
		publishHistory.setAutoHeight(true);
		publishHistory.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		publishHistory.setTemplate(getTemplate());
		publishHistory.setOverStyle(HOVER_STYLE);
		publishHistory.setItemSelector("div.publish-history-wrap");
		publishHistory.setSelectStyle(SELECTION_STYLE);
		publishHistory.getSelectionModel().addSelectionChangedListener(new PublishHistorySelectionChangeListener());
		publishHistory.addListener(Events.OnClick, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				PublishItemWrapper selectedItem = publishHistory.getSelectionModel().getSelectedItem();
				if (selectedItem.isShowNext()) {
					CmsGwt.getContentService().getPublishHistory(
							new BasePagingLoadConfig(loadedItemsCount, GwtPublishData.FETCH_SIZE),
							new BaseCallback<List<GwtPublishData>>() {
								@Override
								public void onSuccess(List<GwtPublishData> result) {
									loadData(result, true);
								}
							});
				} else {
					newPublish.setSelected(false);
					progressPublish.setSelected(false);
					for (PublishListener listener : publishListeners) {
						listener.onPublishClicked(selectedItem.getPublishData().getId());
					}
				}
			}
		});		

		historyContainer.add(publishHistory);
		historyContainer.setScrollMode(Scroll.AUTO);

		add(historyContainer, new BorderLayoutData(LayoutRegion.CENTER));
	}

	public void loadData(List<GwtPublishData> data, boolean add) {
		int fetchedCount = data.size();
		@SuppressWarnings("unchecked")
		List<PublishItemWrapper> current = (List<PublishItemWrapper>) proxy.getData();
		if (current == null)
			current = new ArrayList<PublishItemWrapper>();
		if (add) {
			Iterator<PublishItemWrapper> it = current.iterator();
			while (it.hasNext()) {
				PublishItemWrapper item = it.next();
				if (item.isShowNext() || item.getPublishData() == null)
					it.remove();
			}
		} else {
			current.clear();
			loadedItemsCount = 0;
			if (data.get(0).isInProgress()) {
				loadProgress(data.get(0));
				data.remove(0);
			} else {
				loadNew();
			}
		}
		for (GwtPublishData item : data) {
			current.add(new PublishItemWrapper(item));
			loadedItemsCount++;
		}
		if (fetchedCount == GwtPublishData.FETCH_SIZE) {
			PublishItemWrapper _new = new PublishItemWrapper();
			_new.setPublishData(new GwtPublishData());
			current.add(_new);
		}
		proxy.setData(current);
		loader.load();
		updateHistorySelection();
	}

	public void loadNew() {
		progressPublish.setPublish(null);
		newPublish.setSelected(true);
		newPublish.raise();
	}

	public void loadProgress(GwtPublishData data) {
		progressPublish.setPublish(data);
		progressPublish.setSelected(true);
		progressPublish.raise();
	}
	
	public void moveProgressToList(GwtPublishData newData) {
		progressPublish.setPublish(null);
		newPublish.raise();
		@SuppressWarnings("unchecked")
		List<PublishItemWrapper> current = (List<PublishItemWrapper>) proxy.getData();
		if (current == null)
			current = new ArrayList<PublishItemWrapper>();
		PublishItemWrapper completedProgress = new PublishItemWrapper(newData);
		current.add(0, completedProgress);
		proxy.setData(current);
		loader.load();
		if (progressPublish.isSelected()) {
			List<PublishItemWrapper> selection = new ArrayList<PublishItemWrapper>();
			selection.add(completedProgress);
			publishHistory.getSelectionModel().setSelection(selection);			
		}
		updateHistorySelection();
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

	private void deselectAllHistoryItems() {
		publishHistory.getSelectionModel().deselectAll();
		lastSelection = null;
	}

	private void updateHistorySelection() {
		if (lastSelection != null) {
			List<PublishItemWrapper> selection = new ArrayList<PublishItemWrapper>();
			selection.add(lastSelection);
			publishHistory.getSelectionModel().setSelection(selection);
		}
	}

	public void stopProgressTimer() {
		super.onHide();
		progressPublish.stopTimer();
	}
	
	public void resumeProgressTimer() {
		super.onShow();
		progressPublish.resumeTimer();
	}
}
