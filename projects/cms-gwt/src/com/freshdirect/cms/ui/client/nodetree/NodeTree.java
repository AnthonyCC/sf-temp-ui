package com.freshdirect.cms.ui.client.nodetree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
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
import com.extjs.gxt.ui.client.widget.treepanel.NodeTreePanel;
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
	private RpcProxy<List<TreeContentNodeModel>> proxy;
	private TreeLoader<TreeContentNodeModel> loader;
	
	private TreeStore<TreeContentNodeModel> mainStore;
//	private static TreeStore<ContentNodeModel> searchStore;
	
	private NodeTreePanel tree;
	
	private TextField<String> searchField;
	
	private Synchronizer synchronizer = new Synchronizer();
	
	Set<String> allowedTypes = null;
	
	private NodeSelectListener selectListener; 
	private ArrayList<String> expandedPaths;

	// ==================================== inner classes ====================================
	
	public interface NodeSelectListener {		
		public void nodeSelected( TreeContentNodeModel node );
	}
	
	private class NodeTreeProxy extends RpcProxy<List<TreeContentNodeModel>> {
				
		@Override		
		protected void load(final Object parentNode, final AsyncCallback<List<TreeContentNodeModel>> callback ) {			
			if ( parentNode == null || parentNode instanceof TreeContentNodeModel ) {
				TreeContentNodeModel model = (TreeContentNodeModel)parentNode;
				
				if (model != null) {
					tree.getView().onLoadingChange(tree.findNode(model), true);
				}
				cs.getChildren( (TreeContentNodeModel)parentNode, new BaseCallback<List<TreeContentNodeModel>>() {

					@Override
					public void onSuccess( List<TreeContentNodeModel> result ) {									
						callback.onSuccess( result );
					}

					@Override
					public void errorOccured( Throwable error ) {
						//MainLayout.stopProgress();
						callback.onFailure( error );
					}

				} );
			}
		}
	}	
	
	private class NodeTreeLoader extends BaseTreeLoader<TreeContentNodeModel> {
		
		@SuppressWarnings( "unchecked" )
		public NodeTreeLoader( DataProxy proxy ) {
			super( proxy );
		}
		
		@Override
		public boolean hasChildren( TreeContentNodeModel parent ) {
			return parent.hasChildren();
		}		
	}
	
	
	private class NodeTreeIconProvider implements ModelIconProvider<TreeContentNodeModel> {
		@Override
		public AbstractImagePrototype getIcon( TreeContentNodeModel model ) {
			return IconHelper.createPath( "img/icons/" + model.getType() + ".gif" );
		}		
	}
	
	private class NodeTreeLabelProvider implements ModelStringProvider<TreeContentNodeModel> {
		@Override
		public String getStringValue( TreeContentNodeModel model, String property ) {
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
	
    private class SearchCallback implements AsyncCallback<List<TreeContentNodeModel>> {
    	
        public void onSuccess( List<TreeContentNodeModel> result ) {
            mainStore.removeAll();
            for ( TreeContentNodeModel m : result ) {
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
    	private List<TreeContentNodeModel> nodes;
    	TreeContentNodeModel currentNode;
    	private List<String> pathList;
    	    	
    	public void synchronize( String path ) {    		
        	tree.collapseAll();        	    		
        	tree.addListener( Events.Expand, this );    
    		initTokens(path);    		
    	}
    	
    	public void initTokens(String path) {    		
    		nodes = mainStore.getRootItems();
    		tokens = new StringTokenizer( path, "/" );
    		eatToken();
    		while (tree.isExpanded(currentNode)) {
    			nodes = mainStore.getChildren( currentNode );
    			eatToken();
    			if (currentNode == null) {
    				tree.removeListener( Events.Expand, this );
    				return;
    			}
    		}
    		
    		tree.getStore().getLoader().loadChildren(currentNode);
    		tree.getStore().getLoader().addLoadListener(new LoadListener() {
    			
    			@Override
				public void loaderLoad(LoadEvent le) {    				
    				tree.setExpanded(currentNode, true);
    				tree.getStore().getLoader().removeLoadListener(this);
				}
    			
    		});
    		
    	}
    	
    	public void refresh(List<String> paths) {
    		pathList = new ArrayList<String>();
    		if (paths == null || paths.size() == 0) {
    			return;
    		}
    		pathList.addAll(paths);
    		synchronize(pathList.remove(0));
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
				if (pathList != null && pathList.size() > 0) {
					String newPath = pathList.remove(0);
					
					initTokens(newPath);					
					return;
				}
				
				tree.removeListener( Events.Expand, this );
			}
		}    	
    	
		private void eatToken() {
			
	    	if ( tokens.hasMoreTokens() ) {
	    		
	    		String token = tokens.nextToken();
	    		
	    		for ( TreeContentNodeModel node : nodes ) {
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
		expandedPaths = new ArrayList<String>();		
		
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
			mainStore = new TreeStore<TreeContentNodeModel>( loader );


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
		
		tree.addListener(Events.Collapse, new Listener<TreePanelEvent<TreeContentNodeModel> >() {		

			@Override
			public void handleEvent(TreePanelEvent<TreeContentNodeModel> be) {
				for (Iterator<String> it = expandedPaths.iterator(); it.hasNext();) {
					String path = it.next();
					if (path.contains(be.getItem().getPath())) {
						it.remove();
					}
				}
			}		
		});
		
		tree.addListener(Events.Expand, new Listener<TreePanelEvent<TreeContentNodeModel> >() {

			@Override
			public void handleEvent(TreePanelEvent<TreeContentNodeModel> be) {				
				expandedPaths.add(be.getItem().getPath());				
			}
			
		});
		
		//tree.setCaching(false);
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
		
		// refresh button
		ToolButton refreshButton = new ToolButton("x-tool-refresh");
		refreshButton.setToolTip( new ToolTipConfig("Refresh", "Refresh the node tree.") );  
		refreshButton.addListener( Events.OnClick, new Listener<BaseEvent>() { 
			@Override public void handleEvent( BaseEvent be ) {
				refresh(expandedPaths);
			}; 
		} );
		nodeToolBar.add( refreshButton );
		
		
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

	
	protected List<String> getExpandedPaths() {
		return expandedPaths;
	}
	
	// ==================================== public methods ====================================

	public void setAllowedTypes( Set<String> aTypes ) {
		this.allowedTypes = aTypes;
	}
	
	public void setMultiSelect( boolean multiSelect ) {
		if ( tree == null )
			return;
		
		tree.getSelectionModel().setSelection( new ArrayList<TreeContentNodeModel>() );
		
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
	
	public void addSelectionChangedListener( SelectionChangedListener<TreeContentNodeModel> listener ) {
		tree.getSelectionModel().addListener( Events.SelectionChange, listener );
	}	
	public void removeSelectionChangedListener() {
		tree.getSelectionModel().removeAllListeners();
	}
	
	public TreeContentNodeModel getSelectedItem() {
		return tree.getSelectionModel().getSelectedItem();	
	}	
	public List<TreeContentNodeModel> getSelectedItems() {
		return tree.getSelectionModel().getSelectedItems();	
	}	
	public TreeContentNodeModel getSelectedParent() {
		return mainStore.getParent( tree.getSelectionModel().getSelectedItem() );
	}
	
	public String getSelectedPath() {
		TreeContentNodeModel node = tree.getSelectionModel().getSelectedItem();
		if ( node == null )
			return null;
		StringBuilder sb = new StringBuilder();
		getSelectedPathRecursive( node, sb );
		return sb.toString();
	}
	private void getSelectedPathRecursive ( TreeContentNodeModel node, StringBuilder sb ) {
		TreeContentNodeModel parent = mainStore.getParent( node );
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
        } 
    }
	
	private void clearSearchAction() {
    	searchField.setValue( "" );
        loadRootNodes();
    }
    
    
    public void synchronize( String path ) {
    	synchronizer.synchronize( path );
    }
    
    public void refresh(List<String> paths) {
    	synchronizer.refresh(paths);
    }
    
    public void invalidate() {
    	loader.load();
    }
    
//    public static void removeItemFromOrphans( String contentKey ) {
//    	TreeContentNodeModel orphans = mainStore.findModel( "key", "CmsQuery:orphans" );
//    	TreeContentNodeModel model = mainStore.findModel( "key", contentKey );
//    	mainStore.remove( orphans, model );
//    }
    
    public void scrollHack() {
		El elek = new El( tree.getElement() );
		int scroll = elek.getScrollTop();				
		elek.scrollTo( "top", 0 ).scrollTo( "top", 1 ).scrollTo( "top", scroll );
    }
} 
