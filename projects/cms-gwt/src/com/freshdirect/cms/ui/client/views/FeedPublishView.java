package com.freshdirect.cms.ui.client.views;

import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.DetailPanel;
import com.freshdirect.cms.ui.client.publish.PublishListener;
import com.freshdirect.cms.ui.client.publish.PublishProgress;
import com.freshdirect.cms.ui.client.publish.PublishXList;
import com.freshdirect.cms.ui.client.publish.overview.FeedPublishOverview;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;
import com.freshdirect.cms.ui.service.BaseCallback;

/**
 * 
 * @author greg, csongor
 * 
 */
public class FeedPublishView extends LayoutContainer implements PublishListener {
	ListStore<GwtPublishData> store;

	private static FeedPublishView instance = new FeedPublishView();

	public static FeedPublishView getInstance() {
		return instance;
	}

	private PublishXList publishList;
	private LayoutContainer detailPanel;

	public FeedPublishView() {
		super();
		setLayout(new BorderLayout());

		setBorders(false);

		detailPanel = new LayoutContainer();
		detailPanel.setLayout(new CardLayout());

		publishList = new PublishXList();
		

		BorderLayoutData borderLayoutData = new BorderLayoutData(LayoutRegion.WEST);
		borderLayoutData.setCollapsible(true);
		borderLayoutData.setSplit(false);
		borderLayoutData.setSize(220);
		borderLayoutData.setMargins(new Margins(0, 5, 0, 0));

		publishList.addPublishListener(this);

		FeedPublishOverview.getInstance().addPublishListener(this);

		add(publishList, borderLayoutData);
		add(detailPanel, new BorderLayoutData(LayoutRegion.CENTER));
		load();
	}

	private void load() {
		CmsGwt.getContentService().getPublishHistoryByType(new BasePagingLoadConfig(0, GwtPublishData.FETCH_SIZE), "FEED",new BaseCallback<List<GwtPublishData>>() {
			@Override
			public void onSuccess(List<GwtPublishData> result) {
				loadData(result);
			}
		});
	}

	private void loadData(List<GwtPublishData> publishDataList) {
		final GwtPublishData latestPublishData = publishDataList != null
				&& publishDataList.size() > 0 ? publishDataList.get(0) : null;
		if (latestPublishData != null && latestPublishData.isInProgress()) {
			onPublishClicked(latestPublishData.getId());
		} else {
			onPublishClicked("latest");
		}
		publishList.loadData(publishDataList, false);
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.ui.client.publish.PublishListener#onPublishClicked(java.lang.String)
	 */
	@Override
	public void onPublishClicked(String publishId) {
		ChangeSetQuery query = new ChangeSetQuery();
		query.setPublishId(publishId);
		query.setPublishType("X");
		CmsGwt.getContentService().getPublishDataX(query, new BaseCallback<GwtPublishData>() {
			@Override
			public void onSuccess(GwtPublishData result) {
				setOverview(result);
			}
		});

	}

	@Override
	public void onPublishStarted(String publishId) {
		ChangeSetQuery query = new ChangeSetQuery();
		query.setPublishId(publishId);
		query.setPublishType("X");
		CmsGwt.getContentService().getPublishDataX(query, new BaseCallback<GwtPublishData>() {
			@Override
			public void onSuccess(GwtPublishData result) {
				if (result.isInProgress()) {
					publishList.loadProgress(result);
				} else {
					publishList.moveProgressToList(result);
				}
				setOverview(result);
			}
		});
	}

	public void setOverview(GwtPublishData publishData) {
		if (publishData.isInProgress()) {
			switchOverview(PublishProgress.getInstance());
			FeedPublishOverview.getInstance().loadData(publishData);
		} else {
			switchOverview(FeedPublishOverview.getInstance());
			FeedPublishOverview.getInstance().loadData(publishData);
		}
	}

	public void switchOverview(DetailPanel panel) {
		if (!detailPanel.getItems().contains(panel)) {
			detailPanel.add(panel);
		}
		((CardLayout) detailPanel.getLayout()).setActiveItem(panel);
	}
	
	@Override
	protected void onHide() {
		super.onHide();
		publishList.stopProgressTimer();
	}

	@Override
	protected void onShow() {
		super.onShow();
		publishList.resumeProgressTimer();
	}

	@Override
	public void onDetailRequest(ChangeSetQuery query) {		
	}
}
