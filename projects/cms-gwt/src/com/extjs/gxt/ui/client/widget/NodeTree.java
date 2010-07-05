package com.extjs.gxt.ui.client.widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.NodeTreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.MainLayout;
import com.freshdirect.cms.ui.client.nodetree.NodeTreeSelectionModel;
import com.freshdirect.cms.ui.client.nodetree.StringTokenizer;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.freshdirect.cms.ui.service.ContentService;
import com.freshdirect.cms.ui.service.ContentServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;


public class NodeTree extends ContentPanel {
	
	private class NodeTreeHeader extends Header {
		private ToolBar toolBar = new ToolBar();
		

		/**
		 * Returns the tool at the given index.
		 * 
		 * @param index
		 *            the index
		 * @return the tool
		 */
		public Component getTool(int index) {
			return toolBar.getItem(index);
		}

		/**
		 * Returns the number of tool items.
		 * 
		 * @return the count
		 */
		public int getToolCount() {
			return toolBar.getItemCount();
		}

		/**
		 * Adds a tool.
		 * 
		 * @param tool
		 *            the tool to be inserted
		 */
		public void addTool(Component tool) {
			insertTool(tool, getToolCount());
		}

		/**
		 * Inserts a tool.
		 * 
		 * @param tool
		 *            the tool to insert
		 * @param index
		 *            the insert location
		 */
		public void insertTool(Component tool, int index) {

				toolBar.insert(tool, index);

		}

		/**
		 * Removes a tool.
		 * 
		 * @param tool
		 *            the tool to remove
		 */
		public void removeTool(Component tool) {

				toolBar.remove(tool);

		}

		@Override
		protected void doAttachChildren() {
			super.doAttachChildren();
			ComponentHelper.doAttach(toolBar);
		}

		@Override
		protected void doDetachChildren() {
			super.doDetachChildren();
			ComponentHelper.doDetach(toolBar);
		}

		  protected void onRender(Element target, int index) {
			setElement(DOM.createDiv(), target, index);
			addStyleName("x-small-editor");
			addStyleName("nodetree-header");
			toolBar.setParent(this);
			toolBar.setLayoutOnChange(true);


			toolBar.render(getElement());


			if (icon != null) {
				setIcon(icon);
			}
		}

	}
	
	private static ContentServiceAsync cs; 
	private RpcProxy<List<TreeContentNodeModel>> proxy;
	private TreeLoader<TreeContentNodeModel> loader;
	
	private TreeStore<TreeContentNodeModel> mainStore;
	
	private NodeTreePanel tree;
	
	private TextField<String> searchField;
	
	private Synchronizer synchronizer = new Synchronizer();	
	
	Collection<String> allowedTypes = null;
	
	private NodeSelectListener selectListener; 
	private ArrayList<String> expandedPaths;
	
	private boolean isSearchView = false;
	

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
					tree.getView().onLoading(tree.findNode(model));
//					tree.getView().onLoadingChange(tree.findNode(model), true);
				}
				cs.getChildren( (TreeContentNodeModel)parentNode, new BaseCallback<List<TreeContentNodeModel>>() {

					@Override
					public void onSuccess( List<TreeContentNodeModel> result ) {									
						callback.onSuccess( result );
					}

					@Override
					public void errorOccured( Throwable error ) {
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
				sb.append( model.getContentId() );
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
                    
            if ( isMainTree() )
            	MainLayout.getInstance().stopProgress();

            isSearchView = true;

            // TODO this doesn't work like expected 
            // store.add( result, false );
        }
        
        public void onFailure( Throwable caught ) {
            if ( isMainTree() )
            	MainLayout.getInstance().stopProgress();
            MessageBox.alert( "Error", "Details:" + caught.getMessage(), null );
        }
    }
	    
    private class Synchronizer implements Listener<BaseEvent> {

    	private StringTokenizer tokens;
    	private List<TreeContentNodeModel> nodes;
    	TreeContentNodeModel currentNode;
    	private List<String> pathList;
    	private boolean forceRefresh;
    	private int scrollPos;
    	
    	public void synchronize( String path ) {   
    		CmsGwt.log( "synchronizing : " + path );
        	tree.collapseAll();        	    		
        	tree.addListener( Events.Expand, this );   
    		initTokens(path);
    	}
    	    	
    	public void setForceRefresh(boolean fr) {
    		forceRefresh = fr;
    	}
    	
    	public void setScrollPosition(int pos) {
    		scrollPos = pos;
    	}
    	
    	public void initTokens(String path) {    		
    		nodes = mainStore.getRootItems();
    		tokens = new StringTokenizer( path, TreeContentNodeModel.pathSeparator );
    		eatToken();
    		TreePanel<TreeContentNodeModel>.TreeNode tn = null;
    		while ( ( tn = tree.findNode( currentNode ) ) != null && tn.isExpanded() ) {
    			nodes = mainStore.getChildren( currentNode );
    			eatToken();
    			if (currentNode == null) {
    				tree.removeListener( Events.Expand, this );
    				return;
    			}
    		}
    		
    		if (forceRefresh) {
	    		tree.getStore().getLoader().loadChildren(currentNode);
	    		tree.getStore().getLoader().addLoadListener(new LoadListener() {
	    			
	    			@Override
					public void loaderLoad(LoadEvent le) {    				
	    				tree.setExpanded(currentNode, true);
	    				tree.getStore().getLoader().removeLoadListener(this);
					}
	    			
	    		});
    		} 
    		else {
    			tree.setExpanded(currentNode, true);
    		}
    		
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
				} else {
					tree.setExpanded( currentNode, true );
				}
			} else {
				if (pathList != null && pathList.size() > 0) {
					String newPath = pathList.remove(0);
					
					initTokens(newPath);					
					return;
				}
				tree.getElement().setScrollTop(scrollPos);
				scrollHack();
				tree.removeListener( Events.Expand, this );
			}
		}    	
    	    	
		private void eatToken() {
			
	    	if ( tokens.hasMoreTokens() ) {
	    		
	    		String token = tokens.nextToken();
	    		
	    		for ( TreeContentNodeModel node : nodes ) {
	    			// node should not be null, but sometimes it is. This is the cause of the 'Yeti issue'. 
	    			if ( node != null && token.equals( node.getKey() ) ) {
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
		this( null, false, true );
	}
	
	public NodeTree( final Collection<String> aTypes, boolean multiSelect, boolean collapseButton ) {
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
		
		head = new NodeTreeHeader();
		tree = createTreePanel( multiSelect );

		setHeaderVisible(true);
		setHideCollapseTool(true);
		setScrollMode( Scroll.AUTO );

		createToolBar( collapseButton );


		
		this.setLayout( new FitLayout() );
		this.add( tree, new FitData() );

	}
	
	private NodeTreePanel createTreePanel( boolean multiSelect ) {		 
		
		// tree panel
		final NodeTreePanel tree = new NodeTreePanel( this, mainStore );

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
		
		loader.addLoadListener(new LoadListener(){			
			@Override
			public void loaderBeforeLoad(LoadEvent le) {			
				super.loaderBeforeLoad(le);
				if (le.getConfig() == null && !ManageStoreView.getInstance().isMasked()) {
					mask("Loading...");
				}
			}			
			
			@Override
			public void loaderLoad(LoadEvent le) {
				super.loaderLoad(le);
				if (le.getConfig() == null && !ManageStoreView.getInstance().isMasked()) {
					unmask();
					return;
				}
				if (ManageStoreView.getInstance().isMasked()) {
					ManageStoreView.getInstance().unmask();
				}
			}			
			
		});
		
		//tree.setCaching(false);
		return tree;
	}	
	
	private void createToolBar( @SuppressWarnings( "unused" ) boolean collapse ) {
		
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
		getHeader().addTool( searchField );
		
		
		// search button
		ToolButton searchButton = new ToolButton("x-tool-search"); 
		searchButton.setToolTip( new ToolTipConfig("Search", "Search in the content tree.") );  
		searchButton.addListener( Events.OnClick, new Listener<BaseEvent>() { 
			@Override public void handleEvent( BaseEvent be ) { 
				searchAction(); 
			}; 
		} );
		getHeader().addTool( searchButton );
		
		getHeader().addTool( new FillToolItem() );	
		
		// refresh button
		ToolButton refreshButton = new ToolButton("x-tool-refresh");
		refreshButton.setToolTip( new ToolTipConfig("Refresh", "Refresh the node tree.") );  
		refreshButton.addListener( Events.OnClick, new Listener<BaseEvent>() { 
			@Override public void handleEvent( BaseEvent be ) {
				refresh(expandedPaths, true);
			}; 
		} );
		getHeader().addTool( refreshButton );
		
		
		// clear search button aka home button
		ToolButton homeButton = new ToolButton("home-button"); 
		homeButton.setToolTip( new ToolTipConfig("Home", "Reset the node tree.") );  
		homeButton.addListener( Events.OnClick, new Listener<BaseEvent>() { 
			@Override public void handleEvent( BaseEvent be ) { 
				clearSearchAction(); 
			}; 
		} );
		getHeader().addTool( homeButton );
/*		
		if ( collapse ) {
			ToolButton collapseButton = new ToolButton("x-tool-left");
			collapseButton.setToolTip(new ToolTipConfig("Collapse","Collapses the tree panel."));
			collapseButton.addListener( Events.OnClick, new Listener<BaseEvent>() {
				@Override
				public void handleEvent( BaseEvent e ) {
					collapse();
				}
			} );
			
			nodeToolBar.add( collapseButton );
		}
		this.setTopComponent( nodeToolBar );
*/
	}
	
	
	
	// ==================================== private methods ====================================
	
	private boolean isMainTree() {
    	// not a really nice way to determine if we are the main tree or in a popup. (don't call this from a constructor!)
		return getParent() instanceof MainLayout;
	}

	
	public List<String> getExpandedPaths() {
		return expandedPaths;
	}
	
	// ==================================== public methods ====================================

	public void setAllowedTypes( Collection<String> aTypes ) {
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
		if ( isSearchView ) {
			return null;
		}
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
		sb.append( TreeContentNodeModel.pathSeparator );
		sb.append( node.getKey() );
	}
	
	public void deselectAll() {
		tree.getSelectionModel().deselectAll();
	}
	
    public void loadRootNodes() {    	
    	ManageStoreView.getInstance().mask("Loading...");    	
    	tree.getStore().removeAll();
	    loader.load( null );
	    expandedPaths = new ArrayList<String>();
	}
	
	public void search( String searchTerm ) {		
	    cs.search( searchTerm, new SearchCallback() ); 
	}
	
	@Override
	public void show() {
		super.show();
		scrollHack();
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
                MainLayout.getInstance().startProgress("Search", "Searching for " + searchField.getValue().trim(), "searching...");
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
		isSearchView = false;
        loadRootNodes();
    }
        
    public void synchronize( final String path ) {
    	if( isSearchView ) {
    		System.out.println("searchview is true so invalidate the tree");
    		loader.addLoadListener(new LoadListener() {
    			@Override
    			public void loaderLoad(LoadEvent le) {
    				super.loaderLoad(le);
	            	loader.removeLoadListener(this);
    				synchronize( path );
    			}
    		});
    		clearSearchAction();
    	} 
    	else {
        	synchronizer.setScrollPosition(tree.getElement().getScrollTop());
        	synchronizer.setForceRefresh(true);    	
        	synchronizer.synchronize( path );    		
    	}
    }
    
    public void refresh(List<String> paths, boolean forceRefresh) {
    	synchronizer.setScrollPosition(tree.getElement().getScrollTop());
    	synchronizer.setForceRefresh(forceRefresh);
    	synchronizer.refresh(paths);
    }
    
    public void invalidate() {
    	refresh(expandedPaths, true);
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
    
    @Override
    protected void onRender(Element parent, int pos) {
    	// TODO Auto-generated method stub
    	super.onRender(parent, pos);
    	head.disableTextSelection(false);
    }
} 
