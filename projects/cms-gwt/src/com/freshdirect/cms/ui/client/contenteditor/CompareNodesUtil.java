package com.freshdirect.cms.ui.client.contenteditor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.freshdirect.cms.ui.client.Anchor;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.FieldFactory;
import com.freshdirect.cms.ui.client.MergePreviewPanel;
import com.freshdirect.cms.ui.client.action.BasicAction;
import com.freshdirect.cms.ui.client.fields.FieldHotSpot;
import com.freshdirect.cms.ui.client.fields.InheritanceField;
import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.model.GwtContextualizedNodeData;
import com.freshdirect.cms.ui.model.GwtContextualizedNodeI;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.TabDefinition;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;

public class CompareNodesUtil {
	
	private HashSet<String> differentAttributeKeys;
	private GwtNodeData editedNode;
	private GwtNodeData comparedNode;
	private ContentEditorPanel editorPanel;
	private Container<?> editorComponent;
	private MergePreviewPanel comparePopup;
	private HashSet<FieldHotSpot> hotspots;
	private HashSet<ContentForm> decoratedForms; 
	private Listener<BaseEvent> exitListener;
	
	private class CompareNodesAction extends BasicAction<GwtNodeData> {
		
		private GwtNodeData currentNode;
		
		public CompareNodesAction(GwtNodeData currentNode) {
			final String message = "Loading ";
			this.currentNode = currentNode;
			startLoadProgress(message, message + " ... " );
		}

		@Override
		public void onSuccess(final GwtNodeData result) {
			comparedNode = result;
	        differentAttributeKeys = CompareNodesUtil.compare( currentNode, comparedNode);
	        addDecoration();
			comparePopup = new MergePreviewPanel();
	        stopProgress( "Loaded successfully" );
	    }
	}
	
	public CompareNodesUtil( String key, String path ) {
		editorPanel = (ContentEditorPanel) ManageStoreView.getInstance().getDetailPanel();
		editedNode = editorPanel.getContentNode();
		editorComponent = editorPanel.getEditorComponent();
		hotspots = new HashSet<FieldHotSpot>();
		decoratedForms = new HashSet<ContentForm>();
		
		exitListener = new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				exitCompare();
			}
		};
		
		Anchor exitCompare = editorPanel.getCompareEndButton();
		exitCompare.show();
		exitCompare.addListener(Events.OnClick, exitListener);
		editorPanel.getActionBar().layout();
		editorPanel.getActionBar().repaint();
		Anchor compareButton = editorPanel.getCompareButton();
		compareButton.hide();
		
		CmsGwt.getContentService().getNodeData( key, path , new CompareNodesAction( editedNode ) );				            
	}
	
	private void addDecoration() {
		editorPanel.getActionBar().addStyleName("compare-mode-color");
		editorComponent.addStyleName("compare-mode-color");
		if(editorComponent instanceof ContentEditor) {
			addTabDecoration();
		}
		else {
			addFormDecoration((ContentForm) editorComponent );			
		}
	}

	private void addTabDecoration() {
		TabDefinition tabDef = editedNode.getTabDefinition();
		ContentEditor ce = (ContentEditor) editorComponent;
		final HashMap<String, ContentForm> panels = ce.getPanels();
		final HashMap<String, TabItem> tabs = ce.getTabs();
		
		for ( final String tabId : tabDef.getTabIds() ) {
			int k = 0;
			final String tabLabel = tabDef.getTabLabel( tabId );
			
			for (String sectionId : tabDef.getSectionIds(tabId)) {
				
				for (String key : tabDef.getAttributeKeys(sectionId)) {
					if (differentAttributeKeys.contains(key)) {
						k++;
					}
				}
				
			}
			// show number of differences if any found in tab
			TabItem tab = tabs.get(tabLabel);
			if (k > 0) {
				tab.setText(tabLabel + " (" + k + ")");
//				tab.setText(tabLabel);
			}
		}		
		
		addFormDecoration(panels.get( ContentEditor.getLastActiveTab() ));
	}
		
	public void addFormDecoration( ContentForm cf) {

		if( decoratedForms.contains(cf) ) {
			return;
		}
		
		final Map<String, ContentNodeAttributeI> nodeAttributes = editedNode.getNode().getOriginalAttributes();
		final Set<String> keys = cf.getKeys();
		
		
		for (String key : keys) {
			final ContentNodeAttributeI attr = nodeAttributes.get(key);

			if (attr.getFieldObject()== null)
				continue;
			
			Serializable otherValue = null;
			if( editedNode.getNode().getOriginalAttribute(key).isInheritable() ) {
				InheritanceField<Serializable> otherField= (InheritanceField<Serializable>) comparedNode.getNode().getOriginalAttribute(key).getFieldObject();
				if(otherField != null ) {
					otherValue = otherField.getEffectiveValue();
				}
			}
			else {
				otherValue=comparedNode.getFieldValue(key);
			}
			
			FieldHotSpot w = new FieldHotSpot( attr, key, otherValue );
			if( differentAttributeKeys.contains(key) ) {
				w.setActive(true);
			}
			w.render();
			hotspots.add(w);
		}
		
		decoratedForms.add(cf);
	}
	
	public GwtNodeData getComparedNode() {
		return comparedNode;
	}
	
	public GwtNodeData getEditedNode() {
		return editedNode;
	}
	
	public MergePreviewPanel getComparePopup() {
		return comparePopup;
	}
	
	public void addDifference(String key) {
		differentAttributeKeys.add(key);
	}

	public void removeDifference(String key) {
		differentAttributeKeys.remove(key);
	}
	
	public boolean isDefferent(String key) {
		return differentAttributeKeys.contains(key);
	}
		
	private void removeTabDecoration() {
		TabDefinition tabDef = editedNode.getTabDefinition();
		ContentEditor ce = (ContentEditor) editorComponent;
		final HashMap<String, TabItem> tabs = ce.getTabs();
		
		for ( final String tabId : tabDef.getTabIds() ) {
			String tabLabel = tabDef.getTabLabel( tabId );
			TabItem tab = tabs.get(tabLabel);
			tab.setText(tabLabel);				
		}			
	}
	
	private void removeHotspots() {
		for( FieldHotSpot h : hotspots) {
			h.removeFromParent();
		}
		hotspots = null;		
	}
		
	public void exitCompare() {
		editorPanel.getActionBar().removeStyleName("compare-mode-color");
		editorComponent.removeStyleName("compare-mode-color");
		if(comparePopup != null) {
			comparePopup.removeFromParent();
			comparePopup = null;			
		}
		if(editorComponent instanceof ContentEditor) {
			removeTabDecoration();
		}

		if(hotspots != null) {
			removeHotspots();
		}

		if(hotspots != null) {
			decoratedForms=null;
		}
		
		editorPanel.getCompareEndButton().removeListener(Events.OnClick, exitListener);
		editorPanel.getCompareEndButton().hide();
		editorPanel.getCompareButton().show();
		((BorderLayout) ManageStoreView.getInstance().getLayout()).expand(LayoutRegion.WEST);
		
	}
	
	public static HashSet<String> compare(GwtNodeData editedNode, GwtNodeData comparedNode) throws IllegalArgumentException {
		if (editedNode == null || comparedNode == null || !editedNode.getNode().getType().equals(comparedNode.getNode().getType())) {
			throw new IllegalArgumentException("One or more nodes are invalid");
		}
		
		HashSet<String> diff = new HashSet<String>();
		
		GwtContextualizedNodeI n = new GwtContextualizedNodeData(comparedNode, comparedNode.getCurrentContext()); 
		for (String attributeKey : comparedNode.getNode().getAttributeKeys()) {
				FieldFactory.createStandardField(n, attributeKey);
		}

		for (String attributeKey : editedNode.getNode().getAttributeKeys()) {
			if ( CompareNodesUtil.compareAttribute(attributeKey, editedNode, comparedNode) ) {
				diff.add(attributeKey);
			}
		}

		
		return diff;
	}
	
	public static boolean compareAttribute(String attributeKey, GwtNodeData editedNode, GwtNodeData comparedNode) {
		if (editedNode == null || comparedNode == null || !editedNode.getNode().getType().equals(comparedNode.getNode().getType())) {
			throw new IllegalArgumentException("One or more nodes are invalid");
		}
		Serializable editedFieldValue = null;
		Serializable comparedFieldValue = null;
		
		ContentNodeAttributeI editedNodeAttribute = editedNode.getNode().getOriginalAttribute(attributeKey);
		ContentNodeAttributeI comparedNodeAttribute = comparedNode.getNode().getOriginalAttribute(attributeKey);
		
		if( editedNodeAttribute.isInheritable() ) {
			if( editedNodeAttribute.getFieldObject() != null) {
				editedFieldValue = ( (InheritanceField<Serializable>) editedNodeAttribute.getFieldObject()).getEffectiveValue();
			}
			else {
				editedFieldValue = editedNodeAttribute.getValue();
				if(editedFieldValue == null) {
					ContentNodeAttributeI attr = editedNode.getContexts() == null ? null : editedNode.getContexts().getInheritedAttribute(editedNode.getCurrentContext() , attributeKey);
					editedFieldValue = attr == null ? null : attr.getValue();
				}
			}
			if( comparedNodeAttribute.getFieldObject() != null) {
				comparedFieldValue = ( (InheritanceField<Serializable>) comparedNodeAttribute.getFieldObject()).getEffectiveValue();
			}
			else {
				comparedFieldValue = comparedNodeAttribute.getValue();
				if(comparedFieldValue == null) {
					ContentNodeAttributeI attr = comparedNode.getContexts() == null ? null : comparedNode.getContexts().getInheritedAttribute(comparedNode.getCurrentContext() , attributeKey);
					comparedFieldValue = attr == null ? null : attr.getValue();
				}
			}
		}
		else {		
			editedFieldValue = editedNode.getFieldValue(attributeKey);
			comparedFieldValue = comparedNode.getFieldValue(attributeKey);			
		}

		System.out.println("compare attribute: " + attributeKey + " " + editedFieldValue + " <> "+comparedFieldValue);
		boolean result = ( (editedFieldValue  == null && comparedFieldValue != null) ||	(editedFieldValue != null && !editedFieldValue.equals(comparedFieldValue) ) );
		
		return result;
	}
}
