package com.freshdirect.delivery.map;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

public class Layer implements Serializable{

	public final List polygons;
	public final String klazz;
	
	public Layer(List polygons, String klazz) {
		this.polygons = polygons;
		this.klazz = klazz;	
	}

	public void writeLayer(Element node) {	

		Element group = node.addElement("g");
		group.addAttribute("class", this.klazz);
		group.addAttribute( "onmouseover", "f(evt,'" + klazz + "-over');" );
		group.addAttribute( "onmouseout", "f(evt,'" + klazz + "');" );
		for (Iterator i=polygons.iterator(); i.hasNext(); ) {
			Polygon poly = (Polygon)i.next();
			poly.writePolygon(group); 				
		}

		group = node.addElement("g");
		group.addAttribute("class", this.klazz + "-text");
		for (Iterator i=polygons.iterator(); i.hasNext(); ) {
			Polygon poly = (Polygon)i.next();
			poly.writeLabel(group); 				
		}
	}

}
