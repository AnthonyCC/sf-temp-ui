package com.freshdirect.cms.ui.client.fields;

import java.util.List;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.freshdirect.cms.ui.model.EnumModel;
import com.freshdirect.cms.ui.model.attributes.MultiEnumAttribute;
import com.google.gwt.user.client.Element;

public class EnumMultiSelectField extends TriggerField<String> {

	private Dialog checkBoxListHolder;
	private CheckBoxListView<EnumModel> listView;
	private ListStore<EnumModel> store;
	private boolean readOnly;
	private List<EnumModel> enumValues;

	public EnumMultiSelectField() {
		store = new ListStore<EnumModel>();
		listView = new CheckBoxListView<EnumModel>();
	}

	public EnumMultiSelectField(MultiEnumAttribute attribute) {
		this();
		store.add(attribute.getValues());
		setEditable(false);
		if (attribute.getValue() != null){
			for(EnumModel model:(List<EnumModel>)attribute.getValue()){
				listView.setChecked(model, true);
			}
			setValue((List<EnumModel>)attribute.getValue());
		}
	}
	
	public void setValue(List<EnumModel> valueList){
		this.enumValues = valueList; 
		StringBuilder builder = new StringBuilder();
		for(EnumModel model:valueList){
			builder.append(model.getLabel());
			builder.append(",");
		}
		builder.deleteCharAt(builder.length()-1);
		super.setValue(builder.toString());
	}
	
	public String getValue(){
		StringBuilder builder = new StringBuilder();
		for(EnumModel model:enumValues){
			builder.append(model.getKey());
			builder.append(",");
		}
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}

	@Override
	protected void onTriggerClick(ComponentEvent ce) {
		super.onTriggerClick(ce);
		if (readOnly) {
			return;
		}
		checkBoxListHolder.setSize(getWidth(), 200);
		listView.setWidth(getWidth());
		checkBoxListHolder.setPosition(getAbsoluteLeft(), getAbsoluteTop()
				+ getHeight());
		if (checkBoxListHolder.isVisible()) {
			checkBoxListHolder.hide();
		} else {
			checkBoxListHolder.show();
		}
	}

	@Override
	protected void onRender(Element target, int index) {
		super.onRender(target, index);

		checkBoxListHolder = new Dialog();
		checkBoxListHolder.setClosable(false);
		checkBoxListHolder.setHeaderVisible(false);
		checkBoxListHolder.setFooter(false);
		checkBoxListHolder.setFrame(false);
		checkBoxListHolder.setResizable(false);
		checkBoxListHolder.setAutoHide(false);
		checkBoxListHolder.getButtonBar().setVisible(false);
		checkBoxListHolder.setLayout(new FillLayout());
		checkBoxListHolder.add(listView);
		listView.setStore(store);
		
		listView.setDisplayProperty("label");

		checkBoxListHolder.addWindowListener(new WindowListener() {

			@Override
			public void windowHide(WindowEvent we) {
				setValue(parseCheckedValues(listView));
			}

		});

	}

	private List<EnumModel> parseCheckedValues(
			CheckBoxListView<EnumModel> checkBoxView) {
		List<EnumModel> selected = null;
		if (checkBoxView != null) {
			selected = checkBoxView.getChecked();
			
		}
		return selected;
	}
	
	public CheckBoxListView<EnumModel> getListView(){
		return listView;
	}

	public void setListView(CheckBoxListView listView) {
		this.listView = listView;
	}

	public ListStore getStore() {
		return store;
	}

	public void setStore(ListStore store) {
		this.store = store;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
}