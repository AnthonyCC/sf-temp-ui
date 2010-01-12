package com.freshdirect.cms.ui.client.views;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.freshdirect.cms.ui.client.ActionBar;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.action.BasicAction;
import com.freshdirect.cms.ui.client.changehistory.ChangeHistory;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadHeader;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadHeaderCell;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadRow;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.publish.GwtValidationError;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BulkLoaderView extends LayoutContainer {
	private static final int PAGE_SIZE = 50;

	private class SaveNodeAction extends BasicAction<GwtSaveResponse> {
		public SaveNodeAction() {
			startSaveProgress("Saving changes", "Saving...");
		}

		public void onSuccess(GwtSaveResponse result) {
			showResultsPanel(result);
			stopProgress("");
		}

		@Override
		public void errorOccured(Throwable error) {
			MessageBox.alert("Error", "Error while saving changes: " + error.getMessage(), null);
			stopProgress("");
		}
	}

	private class SaveChangesSelectionListener extends SelectionListener<ButtonEvent> {
		Button upper;
		Button lower;

		public SaveChangesSelectionListener(Button upper, Button lower) {
			this.upper = upper;
			this.lower = lower;
		}

		@Override
		public void componentSelected(ButtonEvent ce) {
			upper.setEnabled(false);
			lower.setEnabled(false);
			CmsGwt.getBulkLoaderService().save(new SaveNodeAction());
		}
	}

	private final class FormUploadListener implements Listener<FormEvent> {
		private static final String UPLOAD_RESULT_HTML_DIV_OPEN = "<div class=\"file-upload-response\">";
		private static final String UPLOAD_RESULT_HTML_DIV_CLOSE = "</div>";

		@Override
		public void handleEvent(FormEvent be) {
			StringBuilder resultHtml = new StringBuilder(be.getResultHtml());
			if (resultHtml.indexOf(UPLOAD_RESULT_HTML_DIV_OPEN) == 0)
				resultHtml.delete(0, UPLOAD_RESULT_HTML_DIV_OPEN.length());
			int lastIndex;
			if ((lastIndex = resultHtml.lastIndexOf(UPLOAD_RESULT_HTML_DIV_CLOSE)) == resultHtml.length()
					- UPLOAD_RESULT_HTML_DIV_CLOSE.length())
				resultHtml.delete(lastIndex, resultHtml.length());

			if ("OK".equals(resultHtml.toString())) {
				CmsGwt.getBulkLoaderService().getPreviewHeader(new BaseCallback<GwtBulkLoadHeader>() {
					@Override
					public void onSuccess(GwtBulkLoadHeader result) {
						BulkLoaderView.this.showPreviewPanel(result);
					}

				});
			} else {
				MessageBox.alert("Error", "Error while processing XLS file: " + resultHtml, null);
			}
		}
	}

	private final static class GwtBulkLoadRowRenderer implements GridCellRenderer<GwtBulkLoadRow> {
		@Override
		public Object render(GwtBulkLoadRow model, String property, ColumnData config, int rowIndex, int colIndex,
				ListStore<GwtBulkLoadRow> store, Grid<GwtBulkLoadRow> grid) {
			String value = model.getCells().get(colIndex).getDisplayValue();
			if (value != null && value.trim().length() == 0)
				value = null;
			BoxComponent text;
			if (value != null) {
				text = new Text(value);
			} else {
				text = new Html("&nbsp;");
			}
			text.addStyleName("bulk-loader-state-" + model.getCells().get(colIndex).getStatus().getState().name().toLowerCase());
			text.setTitle(model.getCells().get(colIndex).getStatus().getMessage());
			return text;
		}
	}

	private ContentPanel previewPanel;
	private ContentPanel resultsPanel;

	private static BulkLoaderView instance = new BulkLoaderView();

	public static BulkLoaderView getInstance() {
		return instance;
	}

	public BulkLoaderView() {
		super();
		HtmlContainer headerMarkup = new HtmlContainer(
				"<table width=\"100%\" class=\"pageTitle\" cellspacing=\"0\" cellpadding=\"0\"><tbody><tr>"
						+ "<td valign=\"bottom\"><h1 class=\"view-title\">Bulk Load products from XLS file</h1></td>"
						+ "<td width=\"75\" valign=\"bottom\" align=\"right\" style=\"line-height: 0pt;\">"
						+ "<img width=\"75\" height=\"66\" src=\"img/banner_admin.gif\"/></td>"
						+ "</tr></tbody></table>");

		add(headerMarkup);
		add(new ActionBar());
		add(createUploadForm(), new MarginData(10));
		setScrollMode(Scroll.AUTO);
	}

	protected void showResultsPanel(GwtSaveResponse response) {
		if (resultsPanel != null) {
			remove(resultsPanel);
		}
		resultsPanel = new ContentPanel();

		if (response.getChangeSet() != null) {
			// we have changeset to show
			resultsPanel.setHeading("3. Step &ndash; View Results: Changeset " + response.getChangesetId());
			
			ChangeHistory changes = new ChangeHistory( new ChangeSetQueryResponse(response.getChangeSet()) );						
			changes.setHeight(400);
			resultsPanel.add(changes);			
			
		} else if (response.getValidationMessages() != null && response.getValidationMessages().size() > 0) {
			// we have errors to show
			resultsPanel.setHeading("3. Step &ndash; View Results: Validation Errors");
			ListStore<GwtValidationError> store = new ListStore<GwtValidationError>();
			store.add(response.getValidationMessages());
			ListView<GwtValidationError> view = new ListView<GwtValidationError>();
			view.setItemSelector("div.validation-error");
			view.setTemplate("<tpl for=\".\"><div class=\"validation-error\">{humanReadable}</div></tpl>");
			view.setStore(store);
			view.setHeight(300);
			resultsPanel.add(view);
		} else {
			// no changes
			resultsPanel.setHeading("3. Step &ndash; View Results: No changes");
			HBoxLayout layout = new HBoxLayout();
			layout.setPadding(new Padding(10));
			layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
			layout.setPack(BoxLayoutPack.CENTER);
			resultsPanel.setLayout(layout);
			resultsPanel.add(new Label("No changes"));
		}
		add(resultsPanel, new MarginData(10));
		layout();
	}

	protected void showPreviewPanel(GwtBulkLoadHeader header) {
		if (previewPanel != null) {
			remove(previewPanel);
		}
		if (resultsPanel != null) {
			remove(resultsPanel);
			resultsPanel = null;
		}
		insert(previewPanel = createPreviewPanel(header), 3, new MarginData(10));
		layout();
	}

	public ContentPanel createUploadForm() {
		ContentPanel panel = new ContentPanel();
		panel.setHeading("1. Step &ndash; Upload XLS file");
		panel.setWidth(500);
		final FormPanel form = new FormPanel();

		form.setAction(GWT.getModuleBaseURL() + "bulkUpload");
		form.setEncoding(FormPanel.Encoding.MULTIPART);
		form.setMethod(FormPanel.Method.POST);
		form.setHeaderVisible(false);
		form.setBodyBorder(false);
		form.setButtonAlign(HorizontalAlignment.CENTER);

		FileUploadField uploadField = new FileUploadField();
		uploadField.setFieldLabel("XLS file");
		uploadField.setAllowBlank(false);
		uploadField.setName("xlsFile");
		form.add(uploadField, new FormData("100%"));

		form.addButton(new Button("Upload", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (form.isValid()) {
					form.submit();
				}
			}
		}));

		form.addListener(Events.Submit, new FormUploadListener());

		panel.add(form, new MarginData(0, 25, 0, 0));

		return panel;
	}

	public ContentPanel createPreviewPanel(GwtBulkLoadHeader header) {
		ContentPanel previewPanel = new ContentPanel();
		previewPanel.setHeading("2. Step &ndash; Preview Load Results");

		LayoutContainer upperButtonBar = new LayoutContainer();
		HBoxLayout layout2 = new HBoxLayout();
		layout2.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		upperButtonBar.setLayout(layout2);

		final Button upper = new Button("Save Changes");
		final Button lower = new Button("Save Changes");
		upper.addSelectionListener(new SaveChangesSelectionListener(upper, lower));
		upper.setEnabled(false);

		HBoxLayoutData hLayoutData = new HBoxLayoutData();
		upperButtonBar.add(createLegendMarkup());
		hLayoutData = new HBoxLayoutData();
		hLayoutData.setFlex(1);
		upperButtonBar.add(new Text(), hLayoutData);
		upperButtonBar.add(upper);
		hLayoutData = new HBoxLayoutData();
		hLayoutData.setFlex(1);
		upperButtonBar.add(new Text(), hLayoutData);
		HtmlContainer hideThis = createLegendMarkup();
		hideThis.setStyleAttribute("visibility", "hidden");
		upperButtonBar.add(hideThis);

		previewPanel.add(upperButtonBar, new MarginData(10));

		RpcProxy<PagingLoadResult<GwtBulkLoadRow>> proxy = new RpcProxy<PagingLoadResult<GwtBulkLoadRow>>() {
			@Override
			protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtBulkLoadRow>> callback) {
				CmsGwt.getBulkLoaderService().getPreviewRows((PagingLoadConfig) loadConfig, callback);
			}
		};

		final PagingLoader<PagingLoadResult<ModelData>> loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy);
		loader.setRemoteSort(true);

		ListStore<GwtBulkLoadRow> store = new ListStore<GwtBulkLoadRow>(loader);

		final PagingToolBar toolBar = new PagingToolBar(PAGE_SIZE);
		toolBar.bind(loader);

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		for (int i = 0; i < header.getCells().size(); i++) {
			GwtBulkLoadHeaderCell headerCell = header.getCells().get(i);
			ColumnConfig config = new ColumnConfig(headerCell.getColumnName(), headerCell.getColumnName(), 100);
			config.setSortable(false);
			config.setMenuDisabled(true);
			config.setRenderer(new GwtBulkLoadRowRenderer());
			columns.add(config);
		}

		ColumnModel cm = new ColumnModel(columns);

		final Grid<GwtBulkLoadRow> grid = new Grid<GwtBulkLoadRow>(store, cm);
		grid.setAutoHeight(true);
		grid.addStyleName("bulk-loader-grid");

		ContentPanel panel = new ContentPanel();
		panel.setHeaderVisible(false);
		panel.setFrame(false);
		panel.setCollapsible(false);
		panel.setAnimCollapse(false);
		panel.setLayout(new FitLayout());
		panel.setScrollMode(Scroll.AUTO);
		panel.add(grid);
		panel.setBottomComponent(toolBar);

		previewPanel.add(panel, new MarginData(10));

		LayoutContainer lowerButtonBar = new LayoutContainer();
		layout2 = new HBoxLayout();
		layout2.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		lowerButtonBar.setLayout(layout2);

		lower.addSelectionListener(new SaveChangesSelectionListener(upper, lower));
		lower.setEnabled(false);

		lowerButtonBar.add(createLegendMarkup());
		hLayoutData = new HBoxLayoutData();
		hLayoutData.setFlex(1);
		lowerButtonBar.add(new Text(), hLayoutData);
		lowerButtonBar.add(lower);
		hLayoutData = new HBoxLayoutData();
		hLayoutData.setFlex(1);
		lowerButtonBar.add(new Text(), hLayoutData);
		hideThis = createLegendMarkup();
		hideThis.setStyleAttribute("visibility", "hidden");
		lowerButtonBar.add(hideThis);
		
		previewPanel.add(lowerButtonBar, new MarginData(10));

		CmsGwt.getBulkLoaderService().hasAnyError(new BaseCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				upper.setEnabled(!result);
				lower.setEnabled(!result);
			}
		});

		loader.load(0, PAGE_SIZE);

		return previewPanel;
	}

	public HtmlContainer createLegendMarkup() {
		return new HtmlContainer(
				"<dl class=\"bulk-loader-preview-legend\"><dt class=\"bulk-loader-state-create\"></dt><dd>create</dd>"
						+ "<dt class=\"bulk-loader-state-update\"></dt><dd>update</dd>"
						+ "<dt class=\"bulk-loader-state-error_cell\"></dt><dd>error</dd>"
						+ "<dt class=\"bulk-loader-state-warning\"></dt><dd>warning</dd>"
						+ "<dt class=\"bulk-loader-state-ignore_cell\"></dt><dd>ignore</dd>"
						+ "<dt class=\"bulk-loader-state-no_change\" style=\"border: 1px solid gray;\"></dt><dd>no change</dd></dl>");
	}
}
