package com.freshdirect.delivery.map;

import java.io.Serializable;
import java.util.List;

import org.dom4j.Element;

public class Polygon implements Serializable{

	private static int IDGEN = 0;
	public final int id = IDGEN++;
	public final String label;
	public final int[][] points;
	
	public Polygon(String label, List points) {
		this.label = label;
		this.points = (int[][])points.toArray( new int[points.size()][2] );
	}

	public int[] getCenter() {
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;	

		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;	

		for (int i=0; i<points.length; i++) {
			int x = points[i][0];
			minX = Math.min( minX, x );
			maxX = Math.max( maxX, x );

			int y = points[i][1];
			minY = Math.min( minY, y );
			maxY = Math.max( maxY, y );
		}			
		
		
		int[] center = new int[2];
		center[0] = (minX+maxX)/2;
		center[1] = (minY+maxY)/2;

		return center; 
	}

	public void writePolygon(Element node) {
		Element p = node.addElement("polyline");

		p.addAttribute("id", "p-"+this.id);		

		StringBuffer b=new StringBuffer();
		for (int i=0; i<this.points.length; i++) {
			b.append(this.points[i][0]).append(',').append(this.points[i][1]).append(' ');
		}
		p.addAttribute("points", b.toString());
	}

	public void writeLabel(Element node) {
		int[] coords = this.getCenter();
		Element t = node.addElement("text");
		t.addAttribute("id", "l-"+this.id);		

		t.addAttribute("x", String.valueOf( coords[0] ));
		t.addAttribute("y", String.valueOf( coords[1] ));
		t.addText(this.label);
	}
}
