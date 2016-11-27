package com.freshdirect.cms.ui.client.publish;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.freshdirect.cms.ui.client.ActionBar;
import com.freshdirect.cms.ui.client.DetailPanel;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;

public class PublishProgress extends DetailPanel {

	private static PublishProgress instance = new PublishProgress();
	
	public static PublishProgress getInstance() {
		return instance;
	}
		
	private LayoutContainer header;
	private HtmlContainer headerMarkup;
	
	public PublishProgress() {
		super();

		header = new LayoutContainer();
		headerMarkup = new HtmlContainer(
				"<table width=\"100%\" class=\"pageTitle\" cellspacing=\"0\" cellpadding=\"0\">"
						+ "<tbody><tr>"
						+ "<td valign=\"bottom\">"
						+ "<h1 class=\"view-title\">Publish in Progress <span class=\"publish-id\"></span></h1>"
						+ "</td>"
						+ "<td width=\"75\" valign=\"bottom\" align=\"right\" style=\"line-height: 0pt;\">"
						+ "<img width=\"75\" height=\"66\" src=\"img/banner_publish.gif\"/>"
						+ "</td>" + "</tr>" + "</tbody></table>");
		ActionBar actionBar = new ActionBar();
		actionBar.addStyleName("compare-mode-color");
		header.add(headerMarkup);
		header.add(actionBar);

		setHeader(header);
		setBody(new LayoutContainer());
		Html gummyBears = new Html("<TABLE BORDER=\"0\" CELLSPACING=\"7\"><TR>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p0_0\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p0_1\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p0_2\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p0_3\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p0_4\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p0_5\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p0_6\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p0_7\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p0_8\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p0_9\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"</TR>" +
				"<TR>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p1_0\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p1_1\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p1_2\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p1_3\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p1_4\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p1_5\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p1_6\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p1_7\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p1_8\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p1_9\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"</TR>" +
				"<TR>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p2_0\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p2_1\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p2_2\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p2_3\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p2_4\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p2_5\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p2_6\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p2_7\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p2_8\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p2_9\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"</TR>" +
				"<TR>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p3_0\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p3_1\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p3_2\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p3_3\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p3_4\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p3_5\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p3_6\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p3_7\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p3_8\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p3_9\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"</TR>" +
				"<TR>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p4_0\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p4_1\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p4_2\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p4_3\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p4_4\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p4_5\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p4_6\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p4_7\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p4_8\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p4_9\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"</TR>" +
				"<TR>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p5_0\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p5_1\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p5_2\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p5_3\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p5_4\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p5_5\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p5_6\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p5_7\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p5_8\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"<TD><IMG SRC=\"img/progress/clear.gif\" NAME=\"p5_9\" ALT=\"\" WIDTH=\"50\" HEIGHT=\"55\"></TD>" +
				"</TR>");
		gummyBears.addListener(Events.Render, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {				
				doGummyBears();
			}
		});
		getBody().add(gummyBears);
	}	
	
	public void loadData(GwtPublishData data) {
		Html publishId;
		publishId = new Html("(#" + data.getId() + ")");			
		publishId.setTagName("span");
		headerMarkup.add(publishId, ".publish-id");
	}
	
	private native void doGummyBears() /*-{
		var i, x, nobear, bears ;
		if ($doc.images) {
			nobear = new Image();		
			nobear.src = "img/progress/clear.gif";	//blank
	
			bears = [];
			bears[1] = new Image();		
			bears[1].src = "img/progress/greenbear.gif";	//blue
			bears[2] = new Image();		
			bears[2].src = "img/progress/orangebear.gif";		//red
			bears[3] = new Image();		
			bears[3].src = "img/progress/whitebear.gif";	//white
	
	
			var flag = new Array( 	new Array(1,1,1,1,2,2,2,2,2,2),
				new Array(1,1,1,1,3,3,3,3,3,3),
				new Array(1,1,1,1,2,2,2,2,2,2),
				new Array(3,3,3,3,3,3,3,3,3,3),
				new Array(2,2,2,2,2,2,2,2,2,2),
				new Array(3,3,3,3,3,3,3,3,3,3)
			);
		}
		

		$wnd.test = function() {			
			x++;
			if (x == 10) {
				i++;	
				x=0;
			}
			$doc.images["p" + i + "_" + x].src = bears[flag[i][x]].src;
			if (i * x <= 44) {
				setTimeout('$wnd.test()',100);
			} else {
				i = 0;
				x = -1;
				$wnd.reset(); 
			}
		};

		$wnd.reset = function() {
			x++;
			if (x == 10) {
				i++;	
				x=0;
			}
			$doc.images["p" + i + "_" + x].src = nobear.src;				
			if (i * x <= 44) {
				setTimeout('$wnd.reset()',100);
			} else {
				i = 0;
				x = -1;
				$wnd.test(); 
			}
		};
		
		var go = function() {
			i = 0;
			x = -1;
			$wnd.test();
		};
	
	go();
	}-*/;
}
