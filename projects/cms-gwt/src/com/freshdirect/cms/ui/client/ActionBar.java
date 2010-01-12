package com.freshdirect.cms.ui.client;


import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout;
import com.extjs.gxt.ui.client.widget.layout.AnchorData;
import com.extjs.gxt.ui.client.widget.layout.AnchorLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.google.gwt.user.client.Element;

public class ActionBar extends LayoutContainer {

	private AbsoluteLayout containerLayout = new AbsoluteLayout();
	
	protected LayoutContainer textBar = new LayoutContainer();
	protected LayoutContainer buttonBar = new LayoutContainer();
	protected LayoutContainer linkBar = new LayoutContainer();
	
	protected Text textBarText = new Text();
	
	public ActionBar() {
		super();
        addStyleName("pageHeader");
        setHeight(40);

        setLayout(containerLayout);
        
        HBoxLayout textBarLayout = new HBoxLayout();
        textBarLayout.setPack(BoxLayoutPack.START);
        textBarLayout.setPadding(new Padding(10));
        textBar.setLayout(textBarLayout);        
        textBar.add(textBarText);
        textBar.setStyleAttribute("top", "0px");
        textBar.setStyleAttribute("left", "0px");

        
        HBoxLayout buttonBarLayout = new HBoxLayout();
        buttonBarLayout.setPack(BoxLayoutPack.CENTER);
        buttonBarLayout.setPadding(new Padding(10));
        buttonBar.setLayout(buttonBarLayout);
        buttonBar.setStyleAttribute("top", "0px");
        buttonBar.setStyleAttribute("left", "40%");
        
        FlowLayout linkBarLayout = new FlowLayout();
        linkBar.setLayout(linkBarLayout);        
        linkBar.setStyleAttribute("top", "10px");
        linkBar.setStyleAttribute("left", "60%");
        linkBar.setStyleAttribute("text-align", "right");
        
        add(textBar,new AnchorData("40% 100%"));
        add(buttonBar,new AnchorData("20% 100%"));
        add(linkBar,new AnchorData("40% 100%"));
	}
	

	public void setText(String text) {
		textBarText.setText(text);
	}
	
	public void addButton(Button b) {
		buttonBar.add(b);
	}

	public void addButton(Button b, Margins m) {
		buttonBar.add(b,new HBoxLayoutData(m));
	}
	
	public void addLink(Anchor anchor) {
		linkBar.add(anchor);
	}
	
	public void addLink(Anchor anchor, Margins m) {
		anchor.setStyleAttribute("wihte-space", "nowrap");
		linkBar.add(anchor,new HBoxLayoutData(m));
	}
	
	@Override
	public boolean layout() {
		linkBar.layout();
		return super.layout();
	}
}
