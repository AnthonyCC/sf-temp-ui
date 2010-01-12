package com.freshdirect.cms.ui.client.contenteditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.freshdirect.cms.ui.client.FieldFactory;
import com.freshdirect.cms.ui.model.GwtContextualizedNodeI;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.TabDefinition;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.Element;

public class ContentForm extends FormPanel {
	GwtContextualizedNodeI cn;
	
	public static final int FORM_WIDTH = 740;

	final Set<String> keys; // attribute keys used in form
	
	final Map<String,FieldSet> sections;
	final Map<String,List<String>> sectionKeys = new HashMap<String,List<String>>();		
		
	protected void initForm(GwtContextualizedNodeI cn) {
		this.cn = cn;

		/*
		 * Initialize visual appearance
		 */
		setLabelAlign(LabelAlign.RIGHT);
		setHeaderVisible(false);
		setFrame(false);
		setBorders(false);
		setBodyBorder(false);	
		addStyleName("content-form");		
	}
		
	/**
	 * Tabless page constructor
	 * 
	 * @param source
	 * @param contextPath
	 */
	public ContentForm( GwtContextualizedNodeI cn ) {
		super();
		this.keys = cn.getNodeData().getNode().getAttributeKeys();
		this.sections = new HashMap<String,FieldSet>();

		initForm(cn);

		setStyleName("notab-form");
		FormItemLayout layout = new FormItemLayout();		
		layout.setParameterFactory(new AlternateRenderer(layout));
		layout.setDefaultWidth(FORM_WIDTH);
		
		List<String> attrKeys = new ArrayList<String>( cn.getNodeData().getNode().getAttributeKeys() );
		Collections.sort( attrKeys );

		
		FieldSet section = createSection(layout, null, attrKeys);
		sectionKeys.put("<default>", attrKeys);
		sections.put("<default>", section);
		
		add(section);
	}


	/**
	 * Renders form page with tabs header.
	 *
	 * @param source
	 * @param contextPath
	 * @param tabId ID of particular tab of node (optional). If not specified it creates a tab-less form
	 */
	public ContentForm( GwtContextualizedNodeI cn, String tabId ) {
		super();
		this.keys = new HashSet<String>();
		this.sections = new HashMap<String,FieldSet>();
		initForm(cn);

		final GwtNodeData nodeData = cn.getNodeData();

		final TabDefinition tabDefinition = nodeData.getTabDefinition();

		setHeading(nodeData.getNode().getLabel());
		
		int containerIndex = 0;

		for (String sectionId : tabDefinition.getSectionIds(tabId)) {
			String sectionLabel = tabDefinition.getSectionLabel(sectionId);

			FormItemLayout layout = new FormItemLayout();
			layout.setParameterFactory(new SectionAlternateRenderer(layout,	containerIndex));
			layout.setDefaultWidth(FORM_WIDTH);

			final List<String> sectionKeys = tabDefinition.getAttributeKeys(sectionId);
			FieldSet section = createSection(layout, sectionLabel, sectionKeys);
			sections.put(sectionId, section);
			this.sectionKeys.put(sectionId, sectionKeys);
			this.keys.addAll(sectionKeys);
			
			containerIndex += section.getItemCount();

			add(section);

		}
	}	


	/**
	 * Create attribute section
	 * 
	 * @param layout
	 * @param sectionLabel Label of field section (optional)
	 * @param collapsible Can it be collapsed?
	 * @param showBorders Show field set border?
	 * @param attrKeys Attribute keys to add
	 * @return
	 */
	protected FieldSet createSection(FormItemLayout layout, String sectionLabel, List<String> attrKeys) {
		FieldSet section = new FieldSet();

		if (sectionLabel != null) {
			section.setHeading( sectionLabel );
			section.setCollapsible( false );
			section.setBorders( false );
		} else {
			section.setCollapsible( false );
			section.setBorders( false );
		}

		section.setLayout( layout );
		section.setWidth( FORM_WIDTH );
		
		for ( String attributeKey : attrKeys ) {			
			Field<Serializable> field = FieldFactory.createStandardField( cn, attributeKey );
			if ( field != null ) {
				section.add( field );			
			}
		}
		
		return section;
	}
	
	public FieldSet getSection(String sectionId) {
		if (sectionId == null)
			return sections.get("<default>");
		else
			return sections.get(sectionId);
	}
	
	public Set<String> getKeys() {
		return keys;
	}
	
	public Map<String, FieldSet> getSections() {
		return sections;
	}
	
	public Map<String, List<String>> getSectionKeys() {
		return sectionKeys;
	}
	
	
	@Override
	protected void onRender(Element target, int index) {
		// TODO Auto-generated method stub
		super.onRender(target, index);
		getElement().getStyle().setOverflow(Overflow.VISIBLE);
	}
	
}
