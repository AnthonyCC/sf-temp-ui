package com.freshdirect.cms.ui.client.nodetree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.freshdirect.cms.ui.client.MainLayout;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.freshdirect.cms.ui.service.ContentService;
import com.freshdirect.cms.ui.service.ContentServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;


public class NodeTree extends ContentPanel {
	
	private static ContentServiceAsync cs; 
	private static RpcProxy<List<ContentNodeModel>> proxy;
	private static TreeLoader<ContentNodeModel> loader;
	
	private static TreeStore<ContentNodeModel> mainStore;
//	private static TreeStore<ContentNodeModel> searchStore;
	
	private NodeTreePanel tree;
	
	private TextField<String> searchField;
	
	private Synchronizer synchronizer = new Synchronizer();
	
	Set<String> allowedTypes = null;
	
	private NodeSelectListener selectListener; 

	// ==================================== inner classes ====================================
	
	public interface NodeSelectListener {		
		public void nodeSelected( ContentNodeModel node );
	}
	
	private class NodeTreeProxy extends RpcProxy<List<ContentNodeModel>> {		
		@Override
		protected void load( Object loadConfig, final AsyncCallback<List<ContentNodeModel>> callback ) {
			if ( loadConfig == null || loadConfig instanceof ContentNodeModel ) {
				ContentNodeModel model = (ContentNodeModel)loadConfig;
				MainLayout.startProgress( "Loading children", model != null ? model.getLabel() : "Root nodes", "" );
				cs.getChildren( (ContentNodeModel)loadConfig, new BaseCallback<List<ContentNodeModel>>() {

					@Override
					public void onSuccess( List<ContentNodeModel> result ) {
						MainLayout.stopProgress();
						callback.onSuccess( result );
					}

					@Override
					public void errorOccured( Throwable error ) {
						MainLayout.stopProgress();
						callback.onFailure( error );
					}

				} );
			}
		}
	}	
	
	private class NodeTreeLoader extends BaseTreeLoader<ContentNodeModel> {
		
		@SuppressWarnings( "unchecked" )
		public NodeTreeLoader( DataProxy proxy ) {
			super( proxy );
		}
		
		@Override
		public boolean hasChildren( ContentNodeModel parent ) {
			return parent.hasChildren();
		}		
	}
	
	
	private class NodeTreeIconProvider implements ModelIconProvider<ContentNodeModel> {
		@Override
		public AbstractImagePrototype getIcon( ContentNodeModel model ) {
			return IconHelper.createPath( "img/icons/" + model.getType() + ".gif" );
		}		
	}
	
	private class NodeTreeLabelProvider implements ModelStringProvider<ContentNodeModel> {
		@Override
		public String getStringValue( ContentNodeModel model, String property ) {
			if ( model == null )
				return "";

			StringBuilder sb = new StringBuilder( 256 );

			sb.append( model.getJavascriptPreviewLink() );

			if ( allowedTypes == null || allowedTypes.contains( model.getType() ) ) {
				sb.append( model.getLabel() );
				sb.append( " <span class=\"dimmed\">[" );
				sb.append( model.getId() );
				sb.append( "]</span>" );
			} else {
				sb.append( "<span class=\"disabled\">" );
				sb.append( model.getLabel() );
				sb.append( "</span>" );
			}
			return sb.toString();
		}
	}
	
    private class SearchCallback implements AsyncCallback<List<ContentNodeModel>> {
    	
        public void onSuccess( List<ContentNodeModel> result ) {
            mainStore.removeAll();
            for ( ContentNodeModel m : result ) {
            	mainStore.add( m, false );
            }
            mainStore.commitChanges();
            
            MainLayout.setStatus( "Search done." );
            if ( isMainTree() )
            	MainLayout.stopProgress();
            
            // TODO this doesn't work like expected 
            // store.add( result, false );
        }
        
        public void onFailure( Throwable caught ) {
        	MainLayout.setStatus( "Search failed." );
            if ( isMainTree() )
            	MainLayout.stopProgress();
            MessageBox.alert( "Error", "Details:" + caught.getMessage(), null );
        }
    }
		
    private class Synchronizer implements Listener<BaseEvent> {

    	private StringTokenizer tokens;
    	private List<ContentNodeModel> nodes;
    	ContentNodeModel currentNode;
    	
    	
    	public void synchronize( String path ) {
    		tokens = new StringTokenizer( path, "/" );
    		nodes = mainStore.getRootItems();
        	tree.collapseAll();    	
    		
    		tree.addListener( Events.Expand, this );
    		
    		eatToken();
    		tree.setExpanded( currentNode, true );
    	}
    	
		@Override
		public void handleEvent( BaseEvent be ) {
			if (tokens.countTokens() > 0) {
				nodes = mainStore.getChildren( currentNode );
				eatToken();
				if ( tokens.countTokens() == 0 ) {
					// last one
					tree.getSelectionModel().select( currentNode, false );
					tree.scrollIntoView( currentNode );
				} else {
					tree.setExpanded( currentNode, true );
				}
			} else {
				tree.removeListener( Events.Expand, this );
			}
		}
		
		private void eatToken() {
	    	if ( tokens.hasMoreTokens() ) {
	    		
	    		String token = tokens.nextToken();
	    		
	    		for ( ContentNodeModel node : nodes ) {
	    			if ( node.getKey().equals( token ) ) {
	    				currentNode = node;
	    				return;
	    			}
	    		}		
	    	}    	
	    	currentNode = null;
	    	return;
		}
    }    
    
    
	// ==================================== constructor methods ====================================
	
	public NodeTree() {
		this( null, false );
	}
	
	public NodeTree( final Set<String> aTypes, boolean multiSelect ) {

		this.allowedTypes = aTypes;

		// content service
		if ( cs == null )
			cs = (ContentServiceAsync)GWT.create( ContentService.class );

		// data proxy
		if ( proxy == null ) {
			proxy = new NodeTreeProxy();
		}

		// tree loader
		if ( loader == null ) {
			loader = new NodeTreeLoader( proxy );
		}

		// tree store
		if ( mainStore == null )
			mainStore = new TreeStore<ContentNodeModel>( loader );


		// TODO sorting? needed? here or server side?
		// store.setStoreSorter( new StoreSorter<ContentNodeModel>() {
		// @Override public int compare( Store<ContentNodeModel> store,
		// ContentNodeModel m1, ContentNodeModel m2, String property ) {
		// return m1.compareTo( m2 );
		// }
		// });
		
		tree = createTreePanel( multiSelect );

		setHeading( "Content Tree" );
		setScrollMode( Scroll.AUTO );

		createToolBar();
		loadRootNodes();

		this.setLayout( new FitLayout() );
		this.add( tree, new FitData() );

	}
	
	private NodeTreePanel createTreePanel( boolean multiSelect ) {		 
		
		// tree panel
		NodeTreePanel tree = new NodeTreePanel( this, mainStore );

		tree.addStyleName( "node-tree" );
		tree.setBorders( false );
		
		// selection model
		tree.setSelectionModel( new NodeTreeSelectionModel( this ) );
		tree.getSelectionModel().setSelectionMode( multiSelect ? SelectionMode.MULTI : SelectionMode.SINGLE );

		// icon provider
		tree.setIconProvider( new NodeTreeIconProvider() );

		// label provider
		tree.setLabelProvider( new NodeTreeLabelProvider() );
		
		return tree;
	}

	private void createToolBar() {
		// ============ toolbar for node tree ============
		
		ToolBar nodeToolBar = new ToolBar();

		
		// search field
		searchField = new TextField<String>();
		searchField.setFieldLabel("Search");
		searchField.setWidth(100);
		searchField.setEmptyText("Search...");
		searchField.addKeyListener( new KeyListener() {
		    @Override
		    public void componentKeyPress(ComponentEvent event) {
		        if ( event.getKeyCode() == KeyCodes.KEY_ENTER ) {
		        	searchAction();
		        }
		    }
		});
		nodeToolBar.add( searchField );
		
		
		// search button
		ToolButton searchButton = new ToolButton("x-tool-search"); 
		searchButton.setToolTip( new ToolTipConfig("Search", "Search in the content tree.") );  
		searchButton.addListener( Events.OnClick, new Listener<BaseEvent>() { 
			@Override public void handleEvent( BaseEvent be ) { 
				searchAction(); 
			}; 
		} );
		nodeToolBar.add( searchButton );
		
		
		
		nodeToolBar.add( new FillToolItem() );	
		
		// clear search button aka home button
		ToolButton homeButton = new ToolButton("home-button"); 
		homeButton.setToolTip( new ToolTipConfig("Home", "Reset the node tree.") );  
		homeButton.addListener( Events.OnClick, new Listener<BaseEvent>() { 
			@Override public void handleEvent( BaseEvent be ) { 
				clearSearchAction(); 
			}; 
		} );
		nodeToolBar.add( homeButton );
		
		this.setTopComponent( nodeToolBar );
	}
	
	
	
	// ==================================== private methods ====================================
	
	private boolean isMainTree() {
    	// not a really nice way to determine if we are the main tree or in a popup. (don't call this from a constructor!)
		return getParent() instanceof MainLayout;
	}

	// ==================================== public methods ====================================

	public void setAllowedTypes( Set<String> aTypes ) {
		this.allowedTypes = aTypes;
	}
	
	public void setMultiSelect( boolean multiSelect ) {
		if ( tree == null )
			return;
		
		tree.getSelectionModel().setSelection( new ArrayList<ContentNodeModel>() );
		
		if ( multiSelect ) {
			tree.getSelectionModel().setSelectionMode( SelectionMode.MULTI );			
		} else {
			tree.getSelectionModel().setSelectionMode( SelectionMode.SINGLE );
		}
	}
	
	public void addNodeSelectListener( NodeSelectListener listener ) {
		selectListener = listener;
	}
	public void removeNodeSelectListener() {
		selectListener = null;
	}
	public NodeSelectListener getNodeSelectListener() {
		return selectListener;
	}
	
	public void addSelectionChangedListener( SelectionChangedListener<ContentNodeModel> listener ) {
		tree.getSelectionModel().addListener( Events.SelectionChange, listener );
	}	
	public void removeSelectionChangedListener() {
		tree.getSelectionModel().removeAllListeners();
	}
	
	public ContentNodeModel getSelectedItem() {
		return tree.getSelectionModel().getSelectedItem();	
	}	
	public List<ContentNodeModel> getSelectedItems() {
		return tree.getSelectionModel().getSelectedItems();	
	}	
	public ContentNodeModel getSelectedParent() {
		return mainStore.getParent( tree.getSelectionModel().getSelectedItem() );
	}
	
	public String getSelectedPath() {
		ContentNodeModel node = tree.getSelectionModel().getSelectedItem();
		if ( node == null )
			return null;
		StringBuilder sb = new StringBuilder();
		getSelectedPathRecursive( node, sb );
		return sb.toString();
	}
	private void getSelectedPathRecursive ( ContentNodeModel node, StringBuilder sb ) {
		ContentNodeModel parent = mainStore.getParent( node );
		if ( parent != null )
			getSelectedPathRecursive( parent, sb );		
		sb.append( "/" );
		sb.append( node.getKey() );
	}
	
	public void deselectAll() {
		tree.getSelectionModel().deselectAll();
	}
	
    public void loadRootNodes() {
	    loader.load( null );
	}
	
	public void search( String searchTerm ) {		
	    cs.search( searchTerm, new SearchCallback() ); 
	}
	

	
	// ==================================== action methods ====================================
	
	public void selectAction() {
		if ( selectListener != null ) {
			selectListener.nodeSelected( getSelectedItem() );
		}
	}
	
    private void searchAction() {
        if (searchField.getValue() != null && searchField.getValue().trim().length() > 0) {
            if (isMainTree()) {
                // this is for the main tree :
                String newValue = "search/" + searchField.getValue().trim();
                MainLayout.startProgress("Search", "Searching for " + searchField.getValue().trim(), "searching...");
                if (!newValue.equals(History.getToken())) {
                    History.newItem(newValue);
                } else {
                    History.fireCurrentHistoryState();
                }
            } else {
                // search in popups is not using history
                search(searchField.getValue().trim());
            }

        } else {
            loadRootNodes();
        }
    }
	
	private void clearSearchAction() {
    	searchField.setValue( "" );
        loadRootNodes();
    }
    
    
    public void synchronize( String path ) {
    	System.out.println( "Synchronize to : " + path );    	
    	synchronizer.synchronize( path );
    }
    
    public void invalidate() {
    	loader.load();
    }
    
    public static void removeItemFromOrphans( String contentKey ) {
    	ContentNodeModel orphans = mainStore.findModel( "key", "CmsQuery:orphans" );
    	ContentNodeModel model = mainStore.findModel( "key", contentKey );
    	mainStore.remove( orphans, model );
    }
    
    public void scrollHack() {
		El elek = new El( tree.getElement() );
		int scroll = elek.getScrollTop();				
		elek.scrollTo( "top", 0 ).scrollTo( "top", 1 ).scrollTo( "top", scroll );
    }
} 
